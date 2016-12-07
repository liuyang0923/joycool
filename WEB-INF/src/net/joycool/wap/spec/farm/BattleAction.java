package net.joycool.wap.spec.farm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.RandomUtil;

/**
 * @author zhouj
 * @explain： 战斗
 * @datetime:1007-10-24
 */
public class BattleAction extends FarmAction{
	public static FarmWorld world = FarmWorld.one;
	public static FarmNpcWorld nWorld = FarmNpcWorld.one;
	
	public BattleAction(HttpServletRequest request) {
		super(request);
		FarmAction.now = System.currentTimeMillis();
	}
	
	public BattleAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		FarmAction.now = System.currentTimeMillis();
	}

	// 战斗页面
	public void combat() {
		if(farmUser.isDead()) {
			tip("tip", "人物已经死亡");
			return;
		}
		int act = getParameterInt("a");
		int id = getParameterInt("id");
		switch(act) {
		case 1: {		// 加入一个目标
			Object obj = nWorld.getObject(id);
			if(obj != null && obj instanceof CreatureBean) {
				CreatureBean cr = (CreatureBean)obj;
				if(cr.isVisible() && cr.isAlive())
					farmUser.addTarget2(obj);
			}
		} break;
		case 2: {		// 把一个目标变为默认（第一目标）
			List targetList = farmUser.getTargetList();
			if(targetList != null) {
				synchronized(targetList) {
					Object obj = targetList.remove(id);
					if(obj != null)
						targetList.add(0, obj);
				}
			}
		} break;
		case 3: {		// 删除一个目标
//			Object obj = nWorld.getObject(id);
//			if(obj != null) {
//				farmUser.removeTarget(obj);
//			}
		} break;
		case 4: {		// 加入一个玩家为目标
			FarmUserBean user = world.getFarmUserCache(id);
			if(user != null && farmUser.getPos() == user.getPos() && user.isAlive() && !user.isFlagOffline() && !FarmWorld.isGroup(farmUser, user)) {
				MapNodeBean node = world.getMapNode(farmUser.getPos());
				MapBean map = world.getMap(node.getMapId());
				if(!map.isFlagPeace()) {
					if(map.isFlagOOO() && user.getTargetList().size() > 0) {
						tip("tip", "这里是公平对战区域，对方已经在战斗中，请不要打扰。");
						return;
					}
					farmUser.addTarget2(user);
					user.addTarget2(farmUser);
				}
			}
		} break;
		}
	}

	// 攻击npc
	public void attackCreature() {
//		int id = getParameterInt("id");	// 攻击的怪物id
		List targetList = farmUser.getTargetList();
		if(targetList.size() == 0) {
			tip("tip", "没有选择目标");
			return;
		}
		
		if(!farmUser.isCooldown(5)) {
			tip("tip", "技能还未冷却,稍后再试");
			return;
		}
		if(farmUser.isDead()) {
			tip("tip", "人物已经死亡");
			return;
		}
		
		Object obj = targetList.get(0);
		if(obj instanceof FarmUserBean) {
			attackPlayer((FarmUserBean)obj);
			return;
		}
			
		CreatureBean creature = (CreatureBean)obj;
		
		if(creature.isDead()) {
			farmUser.removeTarget(creature);
			tip("tip", "目标已经死亡");
			return;	
		}

		farmUser.addTarget2(creature);
		
		int skillId = getParameterInt("skill");
		if(skillId > 0) {
			
			FarmSkillBean skill = world.getSkill(skillId);
			if(skill == null || !skill.isFlagBattle()) {
				tip("tip", "无法使用,请正确选择技能");
				return;	
			}
			List costList = skill.getCostList();
			for(int ia = 0;ia < costList.size();ia++) {
				int[] usage = (int[])costList.get(ia);
				switch(usage[0]) {
				case 1: {		// 消耗血
					if(farmUser.hp < usage[1]) {
						tip("tip", "技能使用失败，血不足");
						return;
					}
				} break;
				case 2: {		// 消耗气力
					if(farmUser.mp < usage[1]) {
						tip("tip", "技能使用失败，气力不足");
						return;
					}
				} break;
				case 3: {		// 消耗体力
					if(farmUser.sp < usage[1]) {
						tip("tip", "技能使用失败，体力不足");
						return;
					}
				} break;
				}
			}
			
			
			FarmWorld.skillCost(farmUser, skill.getCostList());

			doAttack(targetList, skill);
		} else  {
			if(farmUser.mp < 2) {
				tip("tip", "没有足够的气力");
				return;	
			}
			
			farmUser.decMp(2);
			doAttack(targetList, null);
		}
		CreatureTBean template = creature.getTemplate();
		if(creature.isDead()) {
			farmUser.removeTarget(creature);
			tip("tip", "打死了" + template.getName());
			if(!template.isFlagAnimal())		// 杀死小动物不能获得战斗经验
				addBattleExp(1, creature.getLevel());
			
		} else {
			tip("tip", "攻击了" + template.getName());
		}
	}
	
	public void doAttack(FarmUserBean farmUser, List creatureList) {
		doAttack(creatureList, null);
	}
	// 玩家攻击怪物
	public void doAttack(List targetList, FarmSkillBean skill) {
		BattleStatus bs = farmUser.getCurStat();

		if(skill != null) {
			// 增加主动技经验
			FarmUserProBean pro = farmUser.getPro(skill.getProId());
			if(pro == null || !pro.hasSkill(skill.getId()))
				return;
			if(skill.getRank() == pro.getRank())
				FarmWorld.addFUPExp(pro, 1);
			
			List effectList = skill.getEffectList();
			for(int i = 0;i < effectList.size();i++) {
				int[] ii = (int[])effectList.get(i);
				switch(ii[0]) {
				case 1: {		// 倍数伤害
					FarmWorld.doAttack(farmUser, targetList.get(0), ii[1] / 100.0f, 0);
				} break;
				case 2: {		// 对不同目标造成伤害
					if(targetList.size() >= ii[2]) {
						FarmWorld.doAttack(farmUser, targetList.get(ii[2] - 1), ii[1] / 100.0f, 0);
					}
				} break;
				case 3: {		// 击晕效果，只对怪物有效，对boss和玩家无效
					Object obj = targetList.get(0);
					if(obj instanceof CreatureBean) {
						CreatureBean c = (CreatureBean)obj;
						if(!c.getTemplate().isFlagBoss())
							c.setCooldown(System.currentTimeMillis() + ii[1] * 1000);
					}
				} break;
				case 4: {		// 防御增加气力
					farmUser.incHp(ii[1] / 100.0f);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许生命");
				} break;
				case 5: {		// 防御增加气力
					farmUser.incMp(ii[1] / 100.0f);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许气力");
				} break;
				case 6: {		// 防御增加气力
					farmUser.incSp(ii[1] / 100.0f);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许体力");
				} break;
				case 7: {		// 倍数伤害+数量
					FarmWorld.doAttack(farmUser, targetList.get(0), ii[1] / 100.0f, ii[2]);
				} break;
				case 8: {		// 防御增加气力
					farmUser.incHp(ii[1]);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许生命");
				} break;
				case 9: {		// 防御增加气力
					farmUser.incMp(ii[1]);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许气力");
				} break;
				case 10: {		// 防御增加气力
					farmUser.incSp(ii[1]);
					FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "战斗中恢复了少许体力");
				} break;
				}
			}
		} else {
			FarmWorld.doAttack(farmUser, targetList.get(0), 1f, 0);
		}
		
		FarmWorld.addCooldown(farmUser, 5, now, bs.attackInterval);
		FarmWorld.creatureAttack(farmUser, targetList);
	}
	
	// 增加战斗经验
	public void addBattleExp(int add, int level) {
		FarmUserProBean pro = farmUser.getPro(FarmProBean.PRO_BATTLE);
		if(pro != null && level >= pro.getRank() - 1)		// 学习了战斗并且打死的怪物大于等于自己的战斗级别
			FarmWorld.addFUPExp(pro, add);
	}
	
	// 休息恢复
	public void rest() {
		if(!farmUser.isCooldown(5)) {
			tip("tip", "技能还未冷却,稍后再试");
			return;
		}
		if(farmUser.getTargetList().size() > 0) {
			tip("tip", "战斗中无法休息");
			return;
		}
		if(farmUser.isDead()) {
			tip("tip", "人物已经死亡");
			return;
		}
		farmUser.incHp(0.3f);
		farmUser.incMp(0.18f);
		farmUser.incSp(0.24f);
		FarmWorld.addCooldown(farmUser, 5, now, 5000);
		tip("tip", "休息片刻,恢复了些许体力");
	}
	
	// 攻击其他玩家
	public void attackPlayer(FarmUserBean user) {
		if(user.isDead()) {
			farmUser.removeTarget(user);
			tip("tip", "目标已经死亡");
			return;	
		}
		if(farmUser.mp < 2) {
			tip("tip", "没有足够的气力");
			return;	
		}
		List targetList = farmUser.getTargetList();
		farmUser.decMp(2);
		
		int skillId = getParameterInt("skill");
		if(skillId > 0) {
			
			FarmSkillBean skill = world.getSkill(skillId);
			List costList = skill.getCostList();
			for(int ia = 0;ia < costList.size();ia++) {
				int[] usage = (int[])costList.get(ia);
				switch(usage[0]) {
				case 1: {		// 消耗血
					if(farmUser.hp < usage[1]) {
						tip("tip", "技能使用失败，血不足");
						return;
					}
				} break;
				case 2: {		// 消耗气力
					if(farmUser.mp < usage[1]) {
						tip("tip", "技能使用失败，气力不足");
						return;
					}
				} break;
				case 3: {		// 消耗体力
					if(farmUser.sp < usage[1]) {
						tip("tip", "技能使用失败，体力不足");
						return;
					}
				} break;
				}
			}
			
			
			FarmWorld.skillCost(farmUser, skill.getCostList());

			doAttack(targetList, skill);
		} else  {
			doAttack(targetList, null);
		}

		if(user.isDead()) {
			farmUser.removeTarget(user);
			tip("tip", "打死了" + user.getNameWml());
			FarmWorld.updateUserHonor(farmUser, user);
		} else {
			tip("tip", "攻击了" + user.getNameWml());
		}
	}
	
	// 死亡状态的操作
	public void dead() {
		if(farmUser.isAlive()) {
			tip("tip", "人物没有死亡");
			return;
		}
		int act = getParameterInt("a");
//		int id = getParameterInt("id");
		switch(act) {
		case 1: {		// 复活
			if(world.getMap(world.getMapNode(farmUser.getPos()).getMapId()).isFlagArena())
				tip("tip", "你又回到了人世间,但是损失部分战斗经验");
			else
				tip("tip", "你又回到了人世间");
			FarmWorld.nodeRemovePlayer(farmUser.getPos(), farmUser);
			farmUser.setPos(farmUser.getBindPos());
			FarmWorld.updateUserPos(farmUser, farmUser.getBindPos());
			farmUser.revive();
		} break;
		case 2: {		// 

		} break;
		case 3: {		// 

		} break;
		}
	}
	
	// 使用药品
	public void use() {
		int userbagId = getParameterInt("id");
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userbagId);
		if(userBag != null && userBag.getUserId() == farmUser.getUserId()) {
			String res = FarmWorld.useItem(farmUser, userBag);
			if(res != null) {
				tip("tip", res);
				return;
			}
			UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
					+ userbagId, farmUser.getUserId(), userbagId);
			tip("tip", "使用物品成功");
		} else {
			tip("tip", "不存在的物品");
		}
	}
	static int[] fleeRate = {100, 40, 25, 15};		// 逃跑成功几率
	// 逃跑，有一定几率失败
	public void flee() {
		int rate = farmUser.getTargetList().size();
		if(rate > 0) {
			if(!farmUser.isCooldown(5)) {
				tip("tip", "技能还未冷却,稍后再试");
				return;
			}
			MapNodeBean node = world.getMapNode(farmUser.getPos());
			MapBean map = world.getMap(node.getMapId());
			if(map.isFlagNoFlee()) {
				tip("tip", "无法逃跑");
				return;
			}
			if(!RandomUtil.percentRandom(60)) {
				tip("tip", "逃跑失败");
				FarmWorld.creatureAttack(farmUser, farmUser.getTargetList());
				return;
			}
			FarmWorld.addCooldown(farmUser, 5, now, 2000);
			Object obj = farmUser.getTargetList().remove(0);
			if(obj instanceof FarmUserBean) {
				FarmUserBean user = (FarmUserBean)obj;
				user.removeTarget(farmUser);
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "逃离了[" + user.getNameWml() + "]的追捕");
			} else if(obj instanceof CreatureBean) {
				CreatureBean cr = (CreatureBean)obj;
				CreatureTBean template = cr.getTemplate();
				if(template.isFlagNoFlee()) {
					tip("tip", "无法逃跑");
					return;
				}
				if(template.isFlagRecover()) {
					cr.recover(0.1f);
				}
				FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "逃离了[" + template.getName() + "]的追捕");
			}
			if(farmUser.getTargetList().size() > 0) {
				FarmWorld.creatureAttack(farmUser, farmUser.getTargetList());
				tip("tip", "成功逃出一段距离,摆脱了一个目标");
			} else {
				tip("tip", "成功逃出一段距离,摆脱了目标");
			}
		} else {
			tip("tip", "成功逃离了所有目标");
		}
	}
}
