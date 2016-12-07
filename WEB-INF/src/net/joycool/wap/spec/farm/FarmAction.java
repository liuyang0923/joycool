package net.joycool.wap.spec.farm;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.spec.tiny.*;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author zhouj
 * @explain： 采集
 * @datetime:1007-10-24
 */
public class FarmAction extends CustomAction{
	public static long MONEY_EXCHANGE = 1000;		// 乐币对铜板比率
	public static FarmService service = new FarmService();
	public static FarmWorld world = FarmWorld.one;
	public static FarmNpcWorld nWorld = FarmNpcWorld.one;
	
	public static TinyGame[] tinyGames = {new TinyGame1(2, 3), new TinyGame2(3, 3), new TinyGame3(3)};
	
	UserBean loginUser;
	
	FarmUserBean farmUser = null;
	public static long now = 0;
	
	public static long USER_INACTIVE = 30 * 1000 * 60;		// 用户无动作时间

	public FarmAction(HttpServletRequest request) {
		super(request);
		check(); // check user
	}
	
	public FarmAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		check(); // check user
	}

	public void check() {
		world.prepare();		// 每个请求都检查……真麻烦
		loginUser = super.getLoginUser();
		farmUser = world.getFarmUser(loginUser.getId());
		now = System.currentTimeMillis();
	}

	public void fields() {
		List list = SqlUtil.getIntList("select id from farm_field where user_id=" + loginUser.getId(), 4);
		List fields = new ArrayList();
		for(int i = 0;i < list.size();i++) {
			Integer iid = (Integer)list.get(i);
			FarmFieldBean bean = FarmWorld.getField(iid);
			if(bean != null)
				fields.add(bean);
		}
		setAttribute("fields", fields);
	}
	
	public void field() {
		int id = getParameterInt("id");	// field id
		FarmFieldBean field = FarmWorld.getField(id);
		setAttribute("field", field);
	}
	
	public void plant() {
		int cropId = getParameterInt("c");	// crop id
		if(cropId > 0) {
			int id = getParameterInt("id");	// field id
		}
	}
	
	public boolean act() {
		int id = getParameterInt("id");	// field id
		FarmFieldBean field = null;
		FarmCropBean crop = null;
		if(id > 0) {
			field = FarmWorld.getField(id);
			setAttribute("field", field);
		}
		FarmUserProBean pro = farmUser.getPro(1);
		if(pro == null) {
			tip("tip", "没有该专业");
			return false;
		}
		int act = getParameterInt("a");
		switch(act) {
		case 1: {		// 种植
			int cropId = getParameterInt("c");
			if(cropId > 0)
				crop = world.getCrop(cropId);
			if(crop == null) {
				tip("tip", "种植失败");
			} else if(crop.getProId() != 1) {
				tip("tip", "种植失败");
			} else {
				field.setCropId(cropId);
				field.setStartTime(now);
				field.setActCount(0);
				SqlUtil.executeUpdate("update farm_field set start_time=now(),act_count=0,crop_id=" +
						cropId + " where id=" + id, 4);
				tip("tip", "种植了" + crop.getName());
			}
		} break;
		case 2: {		// 收割
			crop = field.getCrop();
			if(crop != null) {
				int count = field.getProductCount();
				SqlUtil.executeUpdate("update farm_field set crop_id=0 where id=" + id, 4);
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), crop.getProduct(), count);
				field.setCropId(0);
				
				tip("tip", "收割了" + crop.getName());
			} else {
				tip("tip", "收割失败");
			}
		} break;
		case 3: {		// 灌溉
			crop = field.getCrop();
			if(crop != null && field.canAct(now)) {
				if(field.canHarvest(now)) {
					tip("tip", "无法灌溉，该收割了");
				} else {
					TinyAction tiny = new TinyAction(request, response).checkGame(tinyGames[2]);
					if(tiny == null) return true;
					if(tiny.getGameResult() != -1) {
						field.act(now);
						FarmWorld.addFUPExp(pro, 1);
						SqlUtil.executeUpdate("update farm_field set act_count=" + field.getActCount() + " where id=" + id, 4);
						tip("tip", "灌溉成功");
					} else {
						field.actFail(now);
						tip("tip", "灌溉失败");
					}
				}
				
			} else {
				tip("tip", "无法灌溉");
			}
		} break;
		}
		return false;
	}
	
	// 申请一块土地，一人一块
	public void afield() {
		List list = SqlUtil.getIntList("select id from farm_field where user_id=" + loginUser.getId(), 4);
		if(list.size() > 0) {
			tip("tip", "暂时只能申请一块土地");
		} else {
			FarmFieldBean field = new FarmFieldBean();
			field.setCropId(0);
			field.setUserId(loginUser.getId());
			service.addField(field);
			tip("tip", "土地申请成功");
		}
	}
	
	// 以下是畜牧业
	public void feeds() {
		List list = SqlUtil.getIntList("select id from farm_feed where user_id=" + loginUser.getId(), 4);
		List feeds = new ArrayList();
		for(int i = 0;i < list.size();i++) {
			Integer iid = (Integer)list.get(i);
			FarmFeedBean bean = FarmWorld.getFeed(iid);
			if(bean != null)
				feeds.add(bean);
		}
		setAttribute("feeds", feeds);
	}
	
	public void feed() {
		int id = getParameterInt("id");	// feed id
		FarmFeedBean feed = FarmWorld.getFeed(id);
		setAttribute("feed", feed);
	}
	
	public void plant2() {
		int cropId = getParameterInt("c");	// crop id
		if(cropId > 0) {
			int id = getParameterInt("id");	// feed id
		}
	}
	
	public boolean act2() {
		int id = getParameterInt("id");	// feed id
		FarmFeedBean feed = null;
		FarmCropBean crop = null;
		if(id > 0) {
			feed = FarmWorld.getFeed(id);
			setAttribute("feed", feed);
		}
		FarmUserProBean pro = farmUser.getPro(2);
		if(pro == null) {
			tip("tip", "还没有学会该专业");
			return false;
		}
		int act = getParameterInt("a");
		switch(act) {
		case 1: {		// 种植
			int cropId = getParameterInt("c");
			if(cropId > 0)
				crop = world.getCrop(cropId);
			if(crop == null) {
				tip("tip", "养殖失败");
			} else if(crop.getProId() != 2) {
				tip("tip", "养殖失败");
			} else {
				feed.setCropId(cropId);
				feed.setStartTime(now);
				feed.setActCount(0);
				SqlUtil.executeUpdate("update farm_feed set start_time=now(),act_count=0,crop_id=" +
						cropId + " where id=" + id, 4);
				tip("tip", "养殖了" + crop.getName());
			}
		} break;
		case 2: {		// 收割
			crop = feed.getCrop();
			if(crop != null) {
				int count = feed.getProductCount();
				SqlUtil.executeUpdate("update farm_feed set crop_id=0 where id=" + id, 4);
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), crop.getProduct(), count);
				feed.setCropId(0);
				
				tip("tip", "收获了" + crop.getName());
			} else {
				tip("tip", "收获失败");
			}
		} break;
		case 3: {		// 灌溉
			crop = feed.getCrop();
			if(crop != null && feed.canAct(now)) {
				if(feed.canHarvest(now)) {
					tip("tip", "无法喂养，该收获了");
				} else {
					TinyAction tiny = new TinyAction(request, response).checkGame(tinyGames[1]);
					if(tiny == null) return true;
					if(tiny.getGameResult() != -1) {
						feed.act(now);
						FarmWorld.addFUPExp(pro, 1);
						SqlUtil.executeUpdate("update farm_feed set act_count=" + feed.getActCount() + " where id=" + id, 4);
						tip("tip", "喂养成功");
					} else {
						feed.actFail(now);
						tip("tip", "喂养失败");
					}
				}
			} else {
				tip("tip", "无法喂养");
			}
		} break;
		}
		return false;
	}
	
	// 申请一块养殖，一人一块
	public void afeed() {
		List list = SqlUtil.getIntList("select id from farm_feed where user_id=" + loginUser.getId(), 4);
		if(list.size() > 0) {
			tip("tip", "暂时只能申请一块土地");
		} else {
			FarmFeedBean feed = new FarmFeedBean();
			feed.setCropId(0);
			feed.setUserId(loginUser.getId());
			service.addFeed(feed);
			tip("tip", "土地申请成功");
		}
	}
	
	public void index() {
		
	}
	
	// 申请加入farm，如果没有这个用户则创建
	public void enter() {
		if(hasParam("ok")) {
			farmUser = world.getAddFarmUser(loginUser.getId());
			tip("tip", "欢迎来到桃花源,勤劳的人在这里都能获得丰收:)");
		}
	}
	
	public static int highMoney = 500;	// 超过这个数字，就不能再取款
	public void bank() {
		if(isMethodGet()) {
			
		} else {
			int act = getParameterInt("a");
			int money = getParameterInt("money");
			if(money <= 0) {
				tip("tip", "请填写正确的存取金额！");
				return;
			}
			switch(act) {
			case 1: {		// 取款
				if(money > highMoney) {
					tip("tip", "最多只能取" + FarmWorld.formatMoney(highMoney));
					return;
				}
				if(farmUser.getMoney() > highMoney) {
					tip("tip", "你身上已经有很多钱啦，不能再取。");
					return;
				}
				long get = money * MONEY_EXCHANGE;
				synchronized(loginUser.getLock()) {
					long saving = BankCacheUtil.getStoreMoney(loginUser.getId());
					if(saving < get) {
						tip("tip", "银行存款不足，取款失败！");
					} else {
						BankCacheUtil.updateBankStoreCacheById(-get, loginUser.getId(), 0 ,Constants.BANK_FARM_TYPE);
						UserInfoUtil.getMoneyStat()[Constants.BANK_FARM_TYPE] -= get;
						FarmWorld.addMoney(farmUser, money);
						tip("tip", "取款成功！获得" + FarmWorld.formatMoney(money));
					}
				}
			} break;
			case 2: {		// 存款
				if(money < 100 || money > 100000) {
					tip("tip", "单次存款不得超过10个金块，不得少于1个金块");
					return;
				}
				synchronized(loginUser.getLock()) {
					if(farmUser.getMoney() < money) {
						tip("tip", "存款失败，身上的铜板不够");
						return;
					}
					FarmWorld.addMoney(farmUser, -money);
					long get = money * MONEY_EXCHANGE;
					BankCacheUtil.updateBankStoreCacheById(get, loginUser.getId(), 0 ,Constants.BANK_FARM_TYPE);
					UserInfoUtil.getMoneyStat()[Constants.BANK_FARM_TYPE] += get;
					tip("tip", "存款完成！银行存款增加" + get + "乐币");
				}
			} break;
			}
		}
	}
	
	// 用户专业技能
	public void pros() {
		
	}
	
	// 用户专业技能操作（遗忘?）
	public void prosr() {
		int index = getParameterInt("id");	// 要遗忘的技能数组索引
		int act = getParameterInt("a");
		FarmUserProBean[] pros = farmUser.getPro();
		
		if(index >= 0 && index < pros.length) {
			FarmProBean pro = world.getPro(index);
			if(pro == null) {
				tip("tip", "不存在的专业");
				return;
			}
			FarmUserProBean userPro = pros[index];
			switch(act) {
			case 1: {		// 升级
				if(hasParam("c") && userPro != null) {
					if(farmUser.getProPoint() < pro.getPoint()) {
						tip("tip", "没有足够的升级点");
					} else if(userPro.getRank() >= userPro.getMaxRank()) {
						tip("tip", "无法升级了，已经达到最高等级");
					} else {
						if(userPro.canUpgrade()) {		// 如果无法升级也显示升级成功，只是无效
							FarmWorld.addFUPRank(userPro, 1);
							FarmWorld.addProPoint(farmUser, -pro.getPoint());
							
							FarmWorld.updateUserPro(farmUser, userPro);
						}
						tip("tip", "成功升级[专业]" + pro.getName());
					}
				}
			} break;
			case 2: {		// 学习
				if(hasParam("c") && userPro == null) {
					if(farmUser.getProPoint() >= pro.getPoint()) {
						userPro = new FarmUserProBean();
						userPro.setUserId(farmUser.getUserId());
						userPro.setPro(index);
						userPro.setMaxRank(pro.getMaxRank());
						service.addUserPro(userPro);
						pros[index] = userPro;
						FarmWorld.addProPoint(farmUser, -pro.getPoint());
						tip("tip", "成功学习[专业]" + pro.getName());
						FarmWorld.updateUserPro(farmUser, userPro);
					} else {
						tip("tip", "没有足够的升级点");
					}
				}
			} break;
			case 3: {		// 遗忘
				if(hasParam("c") && userPro != null) {
					if(index == FarmProBean.PRO_BATTLE) {
						// 如果身上有装备，禁止废除战斗
						if(farmUser.hasEquip()) {
							tip("tip", "废除战斗[专业]前请先移除身上的装备");
							return;
						}
					}
					pros[index] = null;
					service.removeUserPro(userPro.getId());
					FarmWorld.addProPoint(farmUser, userPro.getRank() * pro.getPoint());
					tip("tip", "成功废弃[专业]" + pro.getName());
					FarmWorld.removeUserPro(farmUser, userPro);
				}
			} break;
			}
		}
	}
	
	// 选择地图
	public void lands() {
		
	}
	
	// 在某一个地图
	public void land() {
		LandUserBean landUser = getLandUser();
		int id = getParameterInt("id");
		LandMapBean land = null;
		if(id > 0) {
			land = world.getLand(id);
			if(land != null && landUser.getLandId() != id) {
				landUser.setLandId(id);
				landUser.setX(0);
				landUser.setY(0);
			}
		} else {
			id = landUser.getLandId();
			if(id > 0)
				land = world.getLand(id);
		}
		if(land == null || land.getPos() != farmUser.getPos()) {
			tip("tip", "不存在的区域");
			return;
		}
		int x = landUser.getX();
		int y = landUser.getY();

		int dir = getParameterInt("d");
		switch(dir) {
		case 1: {
			x--;
			landUser.setX(x);
		} break;
		case 2: {
			x++;
			landUser.setX(x);
		} break;
		case 3: {
			y--;
			landUser.setY(y);
		} break;
		case 4: {
			y++;
			landUser.setY(y);
		} break;
		}
		
		setAttribute("nodes", getUserLandNodes(landUser));
	}
	
	// 采集
	public boolean landPick() throws ServletException, IOException {
		LandUserBean landUser = getLandUser();
		LandNodeBean node = getUserLandNode(landUser);
		
		if(node != null) {
			synchronized(node) {
				LandItemBean item = node.getItem();
				if(item != null) {
					FarmUserProBean pro = farmUser.getPro(item.getProId());
					if(pro == null || item.getRank() > pro.getRank()) {
						tip("tip", "无法采集,专业等级不够!需要[" + world.getPro(item.getProId()).getName() + "]等级" + item.getRank());
						return false;
					}
					TinyAction tiny = new TinyAction(request, response).checkGame(tinyGames[0]);
					if(tiny == null) return true;
					
					node.decCount();
					if(tiny.getGameResult() == -1) {		// 采集失败
						tip("tip", "采集失败");
					} else {
						if(item.getRank() >= pro.getRank() - 1)		// 采到物品等级等于当前等级，才能增加经验值
							FarmWorld.addFUPExp(pro, 1);
	
						UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), item.getItemId(), 1);
						tip("tip", "采集成功");						
					}

				} else {
					tip("tip", "这里没东西可以采集");
				}
			}
		}
		
		return false;
	}
	
	// 显示某专业的技能
	public void skills() {
		int proId = getParameterIntS("pro");
		setAttribute("pro", farmUser.getPro(proId));
	}
	
	// 使用技能制造？
	public void skill() {
		int id = getParameterInt("id");
		if(id > 0) {
			FarmSkillBean skill = world.getSkill(id);
			setAttribute("skill", skill);
			
			int act = getParameterInt("a");
			if(act == 1) {		// 直接使用技能
				FarmUserProBean pro = farmUser.getPro(skill.getProId());
				if(pro.hasSkill(id)) {
					synchronized(farmUser.getLock()) {
						if(skill.getCooldown() > 0 && !farmUser.isCooldown(skill.getCooldownId())) {
							tip = "该技能还未冷却，无法使用!";
						} else {
							int composeId = FarmWorld.skillCompose(farmUser, skill);
							if(composeId > 0) {
								if(skill.getRank() == pro.getRank())	// 使用相同等级的技能才能增加经验值
									FarmWorld.addFUPExp(pro, 1);
								tip = "使用技能成功";
								if(skill.getCooldown() > 0)
									FarmWorld.addCooldown(farmUser, skill.getCooldownId(), now, skill.getCooldown());
							} else if(composeId == -1){
								tip = "使用技能失败，请准备好足够的材料后再试！";
							} else  if(composeId == -3){
								tip = "使用技能失败，有些物品是唯一的，不能再制造！";
							} else{
								tip = "制造失败，材料也损失了！";
							}
						}
						tip("tip", tip);
					}
				} else {
					tip("tip", "不存在的技能");
				}
			}
		}
	}
	
	// 获得玩家所在的结点
	public LandNodeBean getUserLandNode(LandUserBean landUser) {
		LandMapBean land = world.getLand(landUser.getLandId());
		return land.getNode(landUser.getX(), landUser.getY());
	}
	
	// 获得玩家所在结点周围的一共五个结点（中上下左右）
	public LandNodeBean[] getUserLandNodes(LandUserBean landUser) {
		LandMapBean land = world.getLand(landUser.getLandId());
		LandNodeBean[] nodes = new LandNodeBean[5];
		nodes[0] = land.getNode(landUser.getX(), landUser.getY());
		nodes[1] = land.getNode(landUser.getX(), landUser.getY() - 1);
		nodes[2] = land.getNode(landUser.getX(), landUser.getY() + 1);
		nodes[3] = land.getNode(landUser.getX() - 1, landUser.getY());
		nodes[4] = land.getNode(landUser.getX() + 1, landUser.getY());
		return nodes;
	}
	
	// 查看当前任务
	public void quests() {
		
	}
	
	// 查看一个任务或者选择拒绝
	public void quest() {
		int id = getParameterInt("id");
		FarmUserQuestBean userQuest = nWorld.getUserQuest(id);
		if(userQuest == null) {
			tip("tip", "任务已不存在!");
			return;
		}
		setAttribute("userQuest", userQuest);
		int act = getParameterInt("a");
		FarmQuestBean quest = nWorld.getQuest(userQuest.getQuestId());
		if(act == 1) {		// 拒绝任务
			synchronized(farmUser.getLock()) {
				if(userQuest != null && userQuest.getStatus() == 0 && !quest.isFlagNoAbort()) {
					nWorld.removeUserQuest(farmUser, userQuest);
					farmUser.removeQuestFinishStatus(quest);
					tip("tip", "放弃了[任务]" + quest.getName());
					farmUser.flushNpcMark();
				} else {
					tip("tip", "任务无法放弃!");
				}
			}
		}
	}
	
	// 行走地图，其实应该是map node action，为了简化jsp名称
	public void map() {
		int nodeId = getParameterIntS("n");		// 去向的结点
		int direction = getParameterIntS("d");	// 去的方向
		
		int pos = farmUser.getPos();		// 当前结点
		MapNodeBean node = world.getMapNode(pos);
		
		if(nodeId == pos) {		// 正确匹配，移动位置吧
			if(farmUser.getTargetList().size() > 0) {
				setAttribute("node", node);
				tip("tip", "战斗中无法切换");
				return;
			}
			
			MapNodeBean to = node.getLinks(direction);
			if(to == null) {
				setAttribute("node", node);
				tip("tip", "错误的地区");
				return;
			}
			MapBean map = world.getMap(to.getMapId());
			setAttribute("node", node);
			if(map == null) {
				tip("tip", "不存在的地区");
				return;
			}
			if(to.getMapId() != node.getMapId()) {
				if(map.getRank() > farmUser.getRank()) {
					tip("tip", "无法进入该地区,级别不够");
					return;
				}
				if(!FarmWorld.isCondition(map.getConditionList(), farmUser)) {
					tip("tip", "没有达到进入该地区的条件");
					return;
				}
				tip("tip", "你来到了[" + map.getName() + "]");
			}
			if(farmUser.sp < map.getSp()) {
				setAttribute("node", node);
				tip("tip", "体力不支，休息一下吧");
				return;	
			}
			
			farmUser.decSp(map.getSp());
			if(!farmUser.isCooldown(5)) {
				tip("tip", "走的太快啦，歇歇再走");
				return;
			}
			if(map.getMaxPlayer() > 0 && node.getPlayers().size() >= map.getMaxPlayer()) {
				tip("tip", "前方拥挤不堪,换条路吧!");
				setAttribute("node", node);
				return;
			}
			FarmWorld.addCooldown(farmUser, 5, now, map.getCooldown());
			long savePosInterval = now - farmUser.savePos;
			if(savePosInterval > 0) {		// 5分钟后才保存当前位置
				if(savePosInterval > SAVE_POS_INTERVAL)
					farmUser.savePos = now + SAVE_POS_INTERVAL;
				else
					farmUser.savePos += SAVE_POS_INTERVAL;
				FarmWorld.updateUserPos(farmUser, to.getId());
			}
			FarmWorld.moveUser(farmUser, to, direction);
			TriggerUtil.posTrigger.trigger(to.getId(), farmUser);
			// 主动攻击的怪物
			List objList = to.getObjs();
			int count = 1;
			for(int i = 0;i < objList.size();i++) {
				Object obj = objList.get(i);
				if(obj instanceof CreatureBean) {
					CreatureBean cr = (CreatureBean)obj;
					if(cr.isVisible() && cr.getTemplate().isFlagAttack()
							&& cr.getLevel() >= farmUser.getProRank(FarmProBean.PRO_BATTLE) - 5) {
						farmUser.addTarget(cr);
						if(count >= map.getAttackCount())
							break;
						count++;
					}
				}
			}
			
			node = to;
		}
		
		setAttribute("node", node);
	}
	public static int SAVE_POS_INTERVAL = 60000 * 5;
	// 查看身上的装备
	public void equips() {
		
	}
	public void equip() {
		int act = getParameterInt("a");
		if(act == 1) {	// 取消装备
			if(farmUser.isCombat()) {
				tip("tip", "战斗中无法进行此操作");
				return;
			}
			if(farmUser.isDead()) {
				tip("tip", "人物已死亡,无法进行此操作");
				return;
			}
			int part = getParameterInt("i");		// 装备位置
			FarmWorld.removeEquip(farmUser, part);
			farmUser.resetCurStat();
		}
	}
	// 选择一个部位的装备
	public void equipc() {
		int act = getParameterInt("a");
		if(act == 1) {		// 选择装备
			if(farmUser.isCombat()) {
				tip("tip", "战斗中无法进行此操作");
				return;
			}
			if(farmUser.isDead()) {
				tip("tip", "人物已死亡,无法进行此操作");
				return;
			}
			int userbagId = getParameterInt("id");	// 装备id
			int part = getParameterInt("i");		// 装备位置
			FarmWorld.removeEquip(farmUser, part);
			String res = FarmWorld.addEquip(farmUser, part, userbagId);
			tip("tip", res);
			farmUser.resetCurStat();
		}
	}
	
	// 修改信息和设置等
	public void set() {
		if(isMethodGet()) {
			
		} else {
			String name = getParameterString("name");
			if(farmUser.getName().length() > 0) {	// 如果没有名称，第一次设定不需要钱
				synchronized(farmUser.getLock()) {
					int money = farmUser.getRenameMoney();
					if(farmUser.getMoney() < money) {
						tip("tip", "修改姓名需要" + FarmWorld.formatMoney(money) + ",请准备足够的现金!");
						return;
					}
					FarmWorld.addMoney(farmUser, -money);
				}
			}
			if(name.length() < 2 || name.length() > 4 || !StringUtil.isChinese(name)) {
				tip("tip", "姓名必须是2-4位的中文!");
				return;
			}
			if(!farmUser.getName().equals(name)) {
				if(SqlUtil.exist("select user_id from farm_user where name='" + StringUtil.toSql(name) + "' limit 1", 4)) {
					tip("tip", "该姓名已被人使用，请重新选择");
					return;
				}
				FarmWorld.updateUserInfo(farmUser, name);
			}
			tip("tip", "修改成功!");
		}
	}
	
	// 看书
	public void book() {
		int id = getParameterInt("id");
		FarmBookBean book = world.getBook(id);
		if(book == null) {
			tip("tip", "不存在的物体");
		} else {
			setAttribute("book", book);
		}
	}
	
	public boolean pick() {
		int id = getParameterInt("id");
		MapPickBean pick = null;
		if(id > 0)
			pick = world.getPick(id);
		if(pick == null || pick.getPos() != farmUser.getPos()) {
			tip("tip", "不存在的物体!");
			return false;
		}
		request.setAttribute("pick", pick);
		int act = getParameterInt("a");
		if(act == 0) {	// 查看
			
		} else {		// 仔细检查（拿到物品）
			if(pick.getQuestId() != 0 && !farmUser.isQuestStart(pick.getQuestId())) {
				tip("tip", "什么都没有发现!");
				return false;
			}
			TinyAction tiny = new TinyAction(request, response).checkGame(tinyGames[0]);
			if(tiny == null) return true;
			
			if(tiny.getGameResult() == -1) {
				tip("tip", "什么都没有找到!");
				return false;
			}
			List pickList = UserBagCacheUtil.checkAddProduct(farmUser.getUserId(), pick.getItemList());
			if(pickList.size() == 0) {
				tip("tip", "什么都没有发现!");
			} else {
				tip("tip", "你获得了" + FarmWorld.getItemListString(pickList));
			}
		}
		return false;
	}

	// 用户设置属性点
	public void battlePoint() {
		int act = getParameterInt("a");
		int[] set = getBattlePointSet();
		int sum = set[0] + set[1] + set[2] + set[3] + set[4];
		if(act == 1) {		// 正在设置
			int option = getParameterInt("o");
			int count = getParameterInt("c");
			if(count <= 0 || count > farmUser.getBattlePoint() - sum
					|| option < 0 || option >= FarmWorld.MAX_BATTLE_ATTR || farmUser.getBattlePoint() <= sum) {
				return;
			}
			set[option] += count;
		} else if(act == 2) {	// 确认设置
			if(set == null) {
				tip("tip", "请正确分配属性点!");
				return;
			}
			if(sum == 0) {
				tip("tip", "请正确分配属性点!");
				return;
			}
			if(sum > farmUser.getBattlePoint()) {
				tip("tip", "属性点分配错误!");
				return;
			}
			farmUser.addAttr(set);
			farmUser.setBattlePoint(farmUser.getBattlePoint() - sum);
			FarmWorld.saveBattlePoint(farmUser);
			setAttribute2(BATTLE_POINT_SET_KEY, new int[5]);
			tip("tip", "属性点分配成功!");
			farmUser.resetCurStat();
		} else if(act == 3) {		// 取消之前的设置从头来
			setAttribute2(BATTLE_POINT_SET_KEY, new int[5]);
		}
	}
	static String BATTLE_POINT_SET_KEY = "battle_point_set";
	// 获取保存在内存中的未生效设置(战斗属性点分配)
	public int[] getBattlePointSet() {
		int[] set = (int[])getAttribute2(BATTLE_POINT_SET_KEY);
		if(set == null) {
			set = new int[FarmWorld.MAX_BATTLE_ATTR];
			setAttribute2(BATTLE_POINT_SET_KEY, new int[5]);
		}
		return set;
	}
	
	public MapNodeBean getUserNode() {
		return world.getMapNode(farmUser.getPos());
	}
	
	public void addLog(String content) {
		world.log.add(content);
	}
	public String getLogString() {
		return world.log.toString();
	}
	
	public UserBean getLoginUser() {
		return loginUser;
	}

	public FarmUserBean getUser() {
		return farmUser;
	}
	
	public LandUserBean getLandUser() {
		return farmUser.getLandUser();
	}
	
	public long getDelay() {
		return farmUser.getNextActionTime() - now;
	}
	// 查看掉落物品和捡起
	public void drops() {
		int pick = getParameterIntS("o");
		if(pick >= 0) {
			if(farmUser.isDead()) {
				tip("tip", "人物已经死亡,无法进行此操作");
				return;
			}
			MapNodeBean node = getUserNode();
			synchronized(node) {
				if(node.getDropCount() <= pick)
					tip("tip", "不存在的物品");
				else {
					FarmDropBean drop = (FarmDropBean)node.getDrops().get(pick);
					if(drop.isProtected(farmUser.getUserId())) {
						tip("tip", "无法拾取该物品,请等待10秒后再试.");
						return;
					}
										
					int[] data = drop.getData();
					String tip = FarmWorld.dropString(data);
					tip("tip", "你获得了" + tip);
					if(data[0] == 0) {
						FarmWorld.addMoney(farmUser, data[1]);
					} else {
						DummyProductBean item = FarmWorld.getItem(data[0]);
						if(item == null || item.isBind() && UserBagCacheUtil.getUserBagItemCount(data[0], farmUser.getUserId()) > 0) {
							tip("tip", "已经拥有此类物品,无法再拾取");
							return;	
						}
						UserBagCacheUtil.addUserBagCacheStack(farmUser.getUserId(), data[0], data[1]);
						FarmWorld.addLog(farmUser.getPos(), farmUser.getNameWml() + "获得了" + tip);
						TriggerUtil.itemTrigger.trigger(data[0], farmUser);
					}
					node.getDrops().remove(pick);
				}
			}
			
		} else {

			
		}
	}
	
	public GroupBean getUserGroup() {
		return farmUser.getGroup();
	}
	
	// 聊天
	public void chat() {
		GroupBean group = farmUser.getGroup();
		if(group == null) {
			tip("tip", "你没有加入任何队伍");
			return;
		}
		String content = getParameterNoEnter("content");
		if(content != null) {		// 发言
			group.addChat(farmUser.getNameWml() + ":" + StringUtil.toWml(content) + "(" + DateUtil.formatTime(new Date()) + ")");
		}
		GroupUserBean user = group.getUser(farmUser.getUserId());
		user.setReadTotal(group.getChatTotal());
	}
	public void invite() {
		int userId = getParameterInt("id");
		FarmUserBean user = world.getFarmUserCache(userId);
		if(user == null) {
			tip("tip", "目标不存在");
			return;
		}
		if(user.getName().length() == 0) {
			tip("tip", "为了与其他人一同游玩,请先给自己取个名字吧!");
			return;
		}
		GroupUserBean gu = FarmWorld.getGroupUser(user.getUserId());
		if(gu != null || userId == farmUser.getUserId()) {	// 不能邀请自己
			tip("tip", "无法邀请这个玩家");
			return;
		}
		GroupBean group = farmUser.getGroup();
		if(group != null && group.isFull()) {
			tip("tip", "人员已满,无法邀请更多玩家");
			return;
		}
		gu = FarmWorld.addGroupUser(user.getUserId());
		gu.setGroupId(farmUser.getUserId());
		tip("tip", "正在邀请对方加入队伍,请耐心等待");
	}
	public void join() {
		if(request.getParameter("r") == null) {
			if(farmUser.getGroup() != null) {
				tip("tip", "你已经在其他队伍中");
				return;
			}
			GroupUserBean gu = FarmWorld.getGroupUser(farmUser.getUserId());
			if(gu == null) {
				tip("tip", "不存在的队伍");
				return;
			}
			
			FarmUserBean user = world.getFarmUserCache(gu.getGroupId());
			
			if(user == null) {		// 邀请方也不存在
				tip("tip", "不存在的队伍");
				return;
			}
			GroupBean group = user.getGroup();
			if(group == null) {		// 不存在队伍，创建
				group = new GroupBean();
				GroupUserBean guh = FarmWorld.addGroupUser(user.getUserId());
				guh.setGroupId(user.getUserId());
				group.addUser(guh);
				user.setGroup(group);
			}
			group.addUser(gu);
			farmUser.setGroup(group);
			tip("tip", "接受邀请成功加入了队伍");
		} else {
			if(farmUser.getGroup() == null) {
				FarmWorld.removeGroupUser(farmUser.getUserId());
				tip("tip", "拒绝了加入队伍的邀请");
			}
		}
	}
	public void oper() {
		int option = getParameterInt("o");
		switch(option) {
		case 1: {		// 离开组队
			FarmWorld.userLeaveGroup(farmUser);
			tip("tip", "已经离开了队伍,单独行动吧");
		} break;
		default :
			tip("tip", "没有进行任何操作");
		}
	}
	// 大地图采集药材和矿物
	public boolean pick2() {
		int id = getParameterInt("id");
		if(id > 0) {
			FarmObject obj = nWorld.getObject(id);
			if(!(obj instanceof PickBean)) {
				tip("tip", "不存在的目标");
				return false;
			}
			PickBean p = (PickBean)obj;
			if(p != null && p.getPos() == farmUser.getPos())
				request.setAttribute("pick", p);
			else {
				tip("tip", "不存在的目标");
				return false;
			}
			MapNodeBean node = world.getMapNode(farmUser.getPos());
			int act = getParameterInt("a");
			if(act == 1) {		// 进入采摘
				synchronized(node) {
					LandItemBean item = p.getLandItem();
					if(p.isAlive()) {
						FarmUserProBean pro = farmUser.getPro(item.getProId());
						if(pro == null || item.getRank() > pro.getRank()) {
							tip("tip", "无法采集,专业等级不够!需要[" + world.getPro(item.getProId()).getName() + "]等级" + item.getRank());
							return false;
						}
						TinyAction tiny = new TinyAction(request, response).checkGame(tinyGames[0]);
						if(tiny == null) return true;
						
						p.decCount();
						if(tiny.getGameResult() == -1) {		// 采集失败
							tip("tip", "采集失败");
						} else {
							if(item.getRank() >= pro.getRank() - 1)		// 采到物品等级等于当前等级，才能增加经验值
								FarmWorld.addFUPExp(pro, 1);
		
							UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), item.getItemId(), 1);
							tip("tip", "采集成功");						
						}

					} else {
						tip("tip", "这里没东西可以采集");
					}
				}
			}
		}
		return false;
	}
	// 把物品加入收藏
	public void collectItem() {
		int act = getParameterInt("a");
		if(act == 1) {
			int userbagId = getParameterInt("id");
			synchronized(farmUser.getLock()) {
				UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userbagId);
				if(userBag == null || userBag.getUserId() != farmUser.getUserId()) {
					tip("tip", "不存在的物品");
					return;
				}
				DummyProductBean item = FarmWorld.getItem(userBag.getProductId());
				if(!item.isFlagCollect()) {
					tip("tip", "此物品无法收藏");
					return;
				}
				if(!world.addUserCollectItem(farmUser.getUserId(), item.getId(), 0)) {
					tip("tip", "无法收藏,请查看并确认收藏盒");
					return;
				}
				UserBagCacheUtil.UseUserBagCache(userBag, 1);
				tip("tip", "物品已成功收藏");
			}
		}
	}
	// 乘坐驿站车辆
	public void car() {
		FarmCarBean car = farmUser.getCar();
		int act = getParameterInt("a");
		if(act == 1 && !car.isFlagNoStop()) {	// 中途下车
			tip("tip", "中途下车");
			farmUser.setCar(null);
			int pos = car.getPos(farmUser.getCarPos());
			farmUser.setPos(pos);
			FarmWorld.updateUserPos(farmUser, pos);
			FarmWorld.nodeMovePlayer(farmUser.getPos(), pos, farmUser, 0);
			return;
		}
		if(farmUser.isCooldown(5)) {
			farmUser.setCarPos(farmUser.getCarPos() + 1);
			if(farmUser.getCarPos() >= car.getLineCount()) {		// 抵达
				tip("tip", "成功抵达目的地!");
				farmUser.setCar(null);
				int pos = car.getEndPos();
				farmUser.setPos(pos);
				FarmWorld.updateUserPos(farmUser, pos);
				FarmWorld.nodeMovePlayer(farmUser.getPos(), pos, farmUser, 0);
				return;
			}
			FarmWorld.addCooldown(farmUser, 5, now, car.getCooldown());
		}
	}
	// 10秒跑一次
	public static void timer() {
		long now = System.currentTimeMillis();
		FarmWorld world;
	}
}
