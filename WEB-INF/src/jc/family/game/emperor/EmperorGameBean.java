package jc.family.game.emperor;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jc.family.game.GameAction;
import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsUserBean;
import net.joycool.wap.util.RandomUtil;

public class EmperorGameBean extends VsGameBean {
	private static final long serialVersionUID = 1L;
	public static int outNum = 3; // 每回合每方出战人数
	public static int GType = 4;
	public static List roleProtoList = null; // 游戏人物角色

	/**
	 * 30s调用一次
	 * 
	 */
	public void calculate() {
		// 检查是否有中毒和噬血的
		checkVampireAndPoision();
		// 设置出战,检查有没加伤害人物
		checkAddHurt();
		int endA = userListA.size() < outNum ? userListA.size() : outNum; // A家族循环用
		int endB = userListB.size() < outNum ? userListB.size() : outNum; // B家族循环用

		// 计算辅助技能
		if (specilUserList.size() > 0) {
			for (int i = 0; i < specilUserList.size(); i++) {
				EmperorUserBean specilUser = (EmperorUserBean) specilUserList
						.get(i);
				auxiliarySkill(specilUser, false);
			}
			specilUserList = new ArrayList(); // 初始化辅助技能list,下回合用
		}

		// 整理玩家击杀战况!顺便整理战斗日志addFirst到hitInfomationList中
		if (userListA.size() > 0) {
			for (int i = 0; i < endA; i++) { // A家族出战队员
				EmperorUserBean hitA = (EmperorUserBean) userListA.get(i);
				if (hitA.getOperation() == 0|| hitA.getOperation() == 3)
					continue;
				if (hitA.getOperation() == 1) {// 普通击杀
					simpleHit(hitA);
				} else if (hitA.getOperation() == 2) {// 技能击杀
					if (hitA.getCopySkillUser() != null) {
						activeSkill(hitA, true);
					} else {
						activeSkill(hitA, false);
					}
				}
				hitA.setOperation(0);
				reSetUser(hitA);
			}
		}
		if (userListB.size() > 0) {
			for (int i = 0; i < endB; i++) { // B家族出战队员
				EmperorUserBean hitB = (EmperorUserBean) userListB.get(i);
				if (hitB.getOperation() == 0 || hitB.getOperation() == 3)
					continue;
				if (hitB.getOperation() == 1) { // 普通击杀
					simpleHit(hitB);
				} else if (hitB.getOperation() == 2) { // 技能击杀
					if (hitB.getCopySkillUser() != null) {
						activeSkill(hitB, true);
					} else {
						activeSkill(hitB, false);
					}
				}
				hitB.setOperation(0);
				reSetUser(hitB);
			}
		}

		// 先锋调换
		if (userListA.size() > 1) {
			EmperorUserBean userA = (EmperorUserBean) userListA.getFirst();
			if (userListA.size() > outNum) {
				userA.setAhead(false);
			}
			userListA.add(aliveNumA, userA);
			userListA.removeFirst();
		}
		if (userListB.size() > 1) {
			EmperorUserBean userB = (EmperorUserBean) userListB.getFirst();
			if (userListB.size() > outNum) {
				userB.setAhead(false);
			}
			userListB.add(aliveNumB, userB);
			userListB.removeFirst();
		}
		round++;
		lastRoundTime = System.currentTimeMillis();
		// 一方死光,游戏结束
		if (lastRoundTime > getStopEnter() && (aliveNumA == 0 || aliveNumB == 0)) {endGame();} 
	}

	/**
	 * 普通击杀
	 * 
	 * @param hit
	 * @param beHit
	 */
	public void simpleHit(EmperorUserBean hit) {

		StringBuilder simpleInfo = new StringBuilder();
		EmperorUserBean beHit = hit.getEffectUser();
		if (beHit == null || beHit.isDeath())
			return;
		simpleInfo.append(hit.getRole().getName());
		simpleInfo.append("对");
		simpleInfo.append(beHit.getRole().getName());
		simpleInfo.append("普通攻击!");

		int def = 0;
		int beSimpleHitTime = beHit.getBeSimpleHitTime();
		if (beHit.getAddDefUser() != null) {
			int canDef = 3 - beSimpleHitTime * 2; //
			if (canDef > 0)
				def += canDef;
			if (beSimpleHitTime == 1) { // 第一次被击杀时候
				EmperorUserBean addDefUser = beHit.getAddDefUser();
				addDefUser.setContribute(addDefUser.getRole().getSkillHurtContribute()); // 加贡献
			}
		}
		if (!beHit.isAhead() && def < 2 && beSimpleHitTime == 1) { // 当防御状态人被的防御不够一次击杀的时候,算上状态防御
			def += 2;
			beHit.setBeSimpleHitTime(beSimpleHitTime + 1);
		}
		checkHurtHiter(hit, beHit, simpleInfo); // 反伤
		int hurt = 2;
		if (hit.getAddHurtUser() != null) {
			hurt += 1;
		}
		hurt -= def;
		hurt = checkDefHurt(beHit, hurt, simpleInfo); // 金刚
		if (hurt > 0 && hit.getReductionHurtUser() != null) { // 天仁平世
			hurt--;
			EmperorUserBean reductionUser = hit.getReductionHurtUser();
			reductionUser.setContribute(reductionUser.getRole().getSkillHurtContribute());
		}
		if (hurt > 0) {
			int blood = beHit.getBlood();
			hurt = blood > hurt ? hurt : blood;
			beHit.setBlood(blood - hurt);
			simpleInfo.append(beHit.getRole().getName());
			simpleInfo.append("费血");
			simpleInfo.append(hurt);
			simpleInfo.append("滴!");
			if (beHit.getBlood() <= 0) {
				setDie (beHit);
			}
			hit.setContribute(hit.getContribute() + hit.getRole().getSimpleHurtContribute()); // 加贡献
		}
		InfoBean simpleInfoBean = new InfoBean();
		simpleInfoBean.setRound(round);
		simpleInfoBean.setInfo(simpleInfo.toString());
		informationList.addFirst(simpleInfoBean);
	}

	/**
	 * 主动技
	 * 
	 * @param hit动作发起人 ,copy 是否是拷贝别人的技能
	 */
	public void activeSkill(EmperorUserBean hit, boolean copy) {
		StringBuilder hitInfo = new StringBuilder();
		EmperorUserBean beHit = hit.getEffectUser();
		if (beHit == null || beHit.isDeath())
			return; // 击杀目标已经死亡
		EmperorUserBean skillUser = null;
		if (copy) {
			skillUser = hit.getCopySkillUser();
			hitInfo.append(hit.getRole().getName());
			hitInfo.append("借用");
			hitInfo.append(hit.getCopySkillUser().getRole().getName());
			hitInfo.append("的技能!");
		} else {
			skillUser = hit;
		}
		hitInfo.append(hit.getRole().getName());
		hitInfo.append("对");
		hitInfo.append(beHit.getRole().getName());
		hitInfo.append("发动");
		hitInfo.append(skillUser.getRole().getSkillName());
		hitInfo.append("!");

		int skillSpecilHurt = skillUser.getRole().getSkillSpecilHurt(); // 基础特殊伤害
		int skillSimpleHurt = skillUser.getRole().getSkillSimpleHurt(); // 基础普通伤害
		if (skillSpecilHurt > 0 && hit.getNoHitUser() != null) { // 逆转乾坤的人,效果:无特殊伤害
			// 加贡献
			EmperorUserBean contributeUser = hit.getNoHitUser();
			contributeUser.setContribute(contributeUser.getContribute()
					+ contributeUser.getRole().getSkillHurtContribute());
			skillSpecilHurt = 0;
		}
		if (skillSpecilHurt > 0 && hit.getAddSpecilHurtUser() != null) { // 特殊伤害加成
			skillSpecilHurt += 2;
			// 加贡献
			EmperorUserBean contributeUser = skillUser.getAddSpecilHurtUser();
			contributeUser.setContribute(contributeUser.getContribute()
					+ contributeUser.getRole().getSkillHurtContribute());
		}
		if (hit.getAddHurtUser() != null) { // 普通伤害加成
			skillSimpleHurt += 1;
			// 加贡献
			EmperorUserBean contributeUser = skillUser.getAddHurtUser();
			contributeUser.setContribute(contributeUser.getContribute()
					+ contributeUser.getRole().getSkillHurtContribute());
		}
		if (!checkNoDef(hit, hitInfo)) {
			if (beHit.getAddDefUser() != null) { // 加防御的人
				skillSimpleHurt -= 3;
				// 加贡献
				EmperorUserBean contributeUser = beHit.getAddDefUser();
				int contri = contributeUser.getRole().getSkillHurtContribute();
				contributeUser.setContribute(contributeUser.getContribute() + contri);
			}
			if (!beHit.isAhead) {
				skillSimpleHurt -= 2;
			}
		}
		checkHurtHiter(hit, beHit, hitInfo); // 反伤
		skillSimpleHurt = checkDefHurt(beHit, skillSimpleHurt, hitInfo); // 金刚
		if (skillSimpleHurt > 0) {
			if (hit.getReductionHurtUser() != null && skillSimpleHurt > 0) {
				skillSimpleHurt--;
				EmperorUserBean reductionHurtUser = hit.getReductionHurtUser();
				reductionHurtUser.setContribute(reductionHurtUser.getRole()
						.getSkillHurtContribute()); // 加贡献
			}
		}
		if (skillSimpleHurt < 0)
			skillSimpleHurt = 0;
		if (skillSpecilHurt < 0)
			skillSpecilHurt = 0;
		int hurt = skillSpecilHurt + skillSimpleHurt;
		if (hurt > 0 && hit.getReductionHurtUser() != null) { // 天仁平世
			hurt--;
			EmperorUserBean reductionUser = hit.getReductionHurtUser();
			reductionUser.setContribute(reductionUser.getRole().getSkillHurtContribute());
		}
		if (hurt > 0) {
			int beHiterBlood = beHit.getBlood(); // 原来血
			hurt = beHiterBlood > hurt ? hurt : beHiterBlood; // 击杀血
			beHit.setBlood(beHiterBlood - hurt); // 把被击杀人血量重新赋值
			hitInfo.append(beHit.getRole().getName());
			hitInfo.append("费血");
			hitInfo.append(hurt);
			hitInfo.append("滴!");
			// 死亡日志
			if (beHit.getBlood() <= 0) { // 死亡
				setDie (beHit);
			}
		}
		if (skillSimpleHurt > 0 && skillUser.getRole().getSkillId() == 4) { // 噬血
			hit.setHasVampire(1);
		}
		if (skillSpecilHurt > 0) {
			if (skillUser.getRole().getSkillId() == 3) { // 中毒
				beHit.setBePoision(1);
			}
			if (skillUser.getRole().getSkillId() == 1) { // 怒火焚天技能特效
				List userList = null;
				if (beHit.getSide() == 0) {
					userList = userListA;
				} else {
					userList = userListB;
				}
				for (int i = 0; i < userList.size(); i++) {
					EmperorUserBean otherUser = (EmperorUserBean) userList.get(i);
					if (otherUser.isDeath())
						continue; // 已经死亡的跳过
					if (!otherUser.isAhead()) // 非出战状态的跳过
						continue;
					if (otherUser.getUserId() == beHit.getUserId())
						continue; // 已经击杀过的跳过
					int otherBlood = otherUser.getBlood() - 1;
					otherUser.setBlood(otherBlood);
					if (otherBlood <= 0) {
						setDie(otherUser);
						i--;
					}
					hitInfo.append("技能连烧");
					hitInfo.append(otherUser.getRole().getName());
					hitInfo.append(".");
					hitInfo.append(otherUser.getRole().getName());
					hitInfo.append("费血1滴!");
					checkHurtHiter(hit, otherUser, hitInfo);
				}
			}
		}
		InfoBean hitInfoBean = new InfoBean();
		hitInfoBean.setRound(round);
		hitInfoBean.setInfo(hitInfo.toString());
		informationList.addFirst(hitInfoBean);
		hit.setContribute(hit.getContribute()
				+ hit.getRole().getSkillHurtContribute()); // 加贡献
	}

	/**
	 * 辅助技能
	 * 
	 * @param hit动作发起人
	 *            , copy是否是拷贝别人的技能
	 */
	public void auxiliarySkill(EmperorUserBean hit, boolean copy) {
		EmperorUserBean beHit = hit.getEffectUser();
		if (beHit == null || beHit.isDeath())
			return; // 击杀目标已经死亡
		StringBuilder auxiliaryInfo = new StringBuilder();
		EmperorUserBean skillUser = null;
		if (copy) {
			skillUser = hit.getCopySkillUser();
			auxiliaryInfo.append(hit.getRole().getName());
			auxiliaryInfo.append("借用");
			auxiliaryInfo.append(hit.getCopySkillUser().getRole().getName());
			auxiliaryInfo.append("的技能!");
		} else {
			skillUser = hit;
		}
		if (copy || hit.getRole().getId() != 10) {
			auxiliaryInfo.append(hit.getRole().getName());
			auxiliaryInfo.append("对");
			auxiliaryInfo.append(beHit.getRole().getName());
			auxiliaryInfo.append("发动");
			auxiliaryInfo.append(skillUser.getRole().getSkillName());
			auxiliaryInfo.append(".");
		}

		switch (skillUser.getRole().getSkillId()) {
		case 5: { // 加3防(辅助)
			checkAddAllBlood(beHit, auxiliaryInfo);
			beHit.setAddDefUser(hit);
			break;
		}
		case 6: { // 加2血(医生)(辅助)
			checkAddAllBlood(beHit, auxiliaryInfo);
			int startBlood = beHit.getBlood();
			int sumBlood = beHit.getRole().getSumBlood();
			int aimBlood = startBlood + 2;
			int endBlood = aimBlood > sumBlood ? sumBlood : aimBlood;
			beHit.setBlood(endBlood);
			hit.setContribute(hit.getRole().getSkillHurtContribute()); // 加贡献
			break;
		}
		case 7: { // 敌方伤害-1(不区分伤害类型)(辅助)
			checkAddAllBlood(beHit, auxiliaryInfo);
			List effectList = null;
			if (hit.getSide() == 0) {
				effectList = userListB;
			} else {
				effectList = userListA;
			}
			int end = effectList.size() < outNum ? effectList.size() : outNum; // 循环用
			for (int i = 0; i < end; i++) {
				EmperorUserBean user = (EmperorUserBean) effectList.get(i);
				user.setReductionHurtUser(hit);
			}
			break;
		}
		case 8: { // 给己方队友特殊技能伤害+2(辅助)
			checkAddAllBlood(beHit, auxiliaryInfo);
			beHit.setAddSpecilHurtUser(hit);
			break;
		}
		case 9: { // 特殊伤害无效(辅助)
			checkAddAllBlood(beHit, auxiliaryInfo);
			beHit.setNoHitUser(hit);
			break;
		}
		case 10: { // 复制技能(辅助)
			checkAddAllBlood(hit.getCopySkillUser(), auxiliaryInfo);
			int beCopySkillType = hit.getCopySkillUser().getRole().getSkillType();
			if (beCopySkillType == 1) {
				hit.setOperation(2);
				// activeSkill(hit,true);不能提前攻击
			} else if (beCopySkillType == 2) {
				auxiliarySkill(hit, true);
			}
			break;
		}
		}
		if (copy || hit.getRole().getId() != 10) {
			InfoBean aux = new InfoBean();
			aux.setRound(round);
			aux.setInfo(auxiliaryInfo.toString());
			informationList.addFirst(aux);
		}
	}

	/**
	 * 反伤(被动,被击杀时触发)
	 * 
	 * @param hit
	 * @param beHit
	 */
	public void checkHurtHiter(EmperorUserBean hit, EmperorUserBean beHit,
			StringBuilder info) {
		if (hit == null || hit.isDeath()) 
			return;
		if (beHit.getRole().getSkillId() == 12) { // 反伤
			if (beHit.getNoHitUser() != null) { // 逆转乾坤,效果:反伤无效
				EmperorUserBean noHurtUser = beHit.getNoHitUser();
				noHurtUser.setContribute(noHurtUser.getRole().getSkillHurtContribute());
				return;
			}
			// 加反伤日志
			info.append(beHit.getRole().getName());
			info.append(beHit.getRole().getSkillName());
			info.append("被触发!");
			info.append(hit.getRole().getName());
			info.append("费血1滴!");
			int hiterBlood = hit.getBlood() - 1;
			hit.setBlood(hiterBlood);
			if (hiterBlood <= 0) {
				setDie (beHit);
			}
			beHit.setContribute(beHit.getContribute()
					+ beHit.getRole().getSkillHurtContribute()); // 加贡献
		}
	}

	/**
	 * 金刚(被动,被攻击触发)
	 * 
	 */
	public int checkDefHurt(EmperorUserBean beHit, int hurt, StringBuilder info) {
		if (beHit.getRole().getSkillId() == 15) {
			hurt -= 1;

			info.append(beHit.getRole().getName());
			info.append(beHit.getRole().getSkillName());
			info.append("被触发!");

		}
		return hurt;
	}

	/**
	 * 中辅助技能时候给全队人员加血(被动,被使用辅助技能时触发)
	 * 
	 */
	public void checkAddAllBlood(EmperorUserBean beHit, StringBuilder info) {
		if (beHit.getRole().getSkillId() == 14) {

			info.append(beHit.getRole().getName());
			info.append(beHit.getRole().getSkillName());
			info.append("被触发!");

			List effectList = null;
			if (beHit.getSide() == 0) {
				effectList = userListA;
			} else {
				effectList = userListB;
			}
			for (int i = 0; i < effectList.size(); i++) {
				EmperorUserBean effectUser = (EmperorUserBean) effectList
						.get(i);
				if (effectUser.isDeath())
					continue;
				int sumBlood = effectUser.getRole().getSumBlood();
				int aimBlood = effectUser.getBlood() + 1;
				int endBlood = aimBlood > sumBlood ? sumBlood : aimBlood;
				effectUser.setBlood(endBlood);
			}
		}
	}

	/**
	 * 击杀的时候无视对方防御(击杀时触发)
	 * 
	 * @param beHit
	 */
	public boolean checkNoDef(EmperorUserBean beHit, StringBuilder info) {
		if (beHit.getRole().getSkillId() == 13) {
			// 技能触发日志
			info.append(beHit.getRole().getName());
			info.append(beHit.getRole().getSkillName());
			info.append("被触发!");

			return true;
		}
		return false;
	}

	/**
	 * 查看 毒 和 噬血
	 * 
	 */
	public void checkVampireAndPoision () {
		for (int i = 0; i < userListA.size(); i++) {
			EmperorUserBean userA = (EmperorUserBean) userListA.get(i);
			if (userA.getHasVampire() > 0)
				calcuVamPir(userA);
			if (userA.getBePoision() > 0)
				calcuPoision(userA);
//			reSetUser(userA);
		}
		for (int i = 0; i < userListB.size(); i++) {
			EmperorUserBean userB = (EmperorUserBean) userListB.get(i);
			if (userB.getHasVampire() > 0)
				calcuVamPir(userB);
			if (userB.getBePoision() > 0)
				calcuPoision(userB);
//			reSetUser(userB);
		}
	}
	
	/**
	 * 设置出战玩家状态,and查看是否有增加攻击的角色出战
	 * 
	 */
	public void checkAddHurt() {
		int endA = userListA.size() < outNum ? userListA.size() : outNum; // A家族循环用
		int endB = userListB.size() < outNum ? userListB.size() : outNum; // B家族循环用
		EmperorUserBean addHurtUser = null;
		for (int i = 0; i < endA; i++) {
			EmperorUserBean userA = (EmperorUserBean) userListA.get(i);
			if (userA.isDeath()) break; // 遇到死亡的就停止
			userA.setAhead(true);
			if (userA.getRole().getSkillId() == 11) { // 出战给队友普通伤害量+1
				addHurtUser = userA;
			}
		}
		for (int i = 0; i < endB; i++) {
			EmperorUserBean userB = (EmperorUserBean) userListB.get(i);
			if (userB.isDeath()) break; // 遇到死亡的就停止
			userB.setAhead(true);
			if (userB.getRole().getSkillId() == 11) { // 出战给队友普通伤害量+1
				addHurtUser = userB;
			}
		}
		if (addHurtUser != null) {
			List userList = userListA;
			int end = endA;
			if (addHurtUser.getSide() == 1) {
				userList = userListB;
				end = endB;
			}
			for (int i = 0; i < end; i++) {
				EmperorUserBean tempUser = (EmperorUserBean) userList.get(i);
				if (tempUser.isDeath()) break;
				tempUser.setAddHurtUser(addHurtUser);
			}
		}
		if (addHurtUser != null) {
			// 技能触发日志
			StringBuilder addHurt = new StringBuilder();
			addHurt.append(addHurtUser.getRole().getName());
			addHurt.append(addHurtUser.getRole().getSkillName());
			addHurt.append("被触发!");
			InfoBean addHurtInfo = new InfoBean();
			addHurtInfo.setRound(round);
			addHurtInfo.setInfo(addHurt.toString());
			informationList.addFirst(addHurtInfo);
		}
		if (oneTurnDeathInfo.length() > 0) {
			oneTurnDeathInfo.append("阵亡!");
			InfoBean dieInfo = new InfoBean();
			dieInfo.setRound(round);
			dieInfo.setInfo(oneTurnDeathInfo.toString());
			informationList.addFirst(dieInfo);
			oneTurnDeathInfo = new StringBuilder();
		}
	}

	/**
	 * 毒
	 * 
	 * @param bePoisionUser
	 */
	public void calcuPoision(EmperorUserBean bePoisionUser) {
		if (bePoisionUser.isDeath())
			return;
		if (bePoisionUser.getBePoision() == 1) {
			bePoisionUser.setBePoision(2);
		} else if (bePoisionUser.getBePoision() == 2) {
			bePoisionUser.setBePoision(0);
			int startBlood = bePoisionUser.getBlood();
			int endBlood = startBlood - 1;
			bePoisionUser.setBlood(endBlood);
			if (endBlood <= 0) {
				setDie (bePoisionUser);
			}
			StringBuilder poisonInfo = new StringBuilder();
			poisonInfo.append(bePoisionUser.getRole().getName());
			poisonInfo.append("被毒1血!");
			InfoBean infoBean = new InfoBean();
			infoBean.setRound(round);
			infoBean.setInfo(poisonInfo.toString());
			informationList.addFirst(infoBean);
		}
	}

	/**
	 * 噬血
	 * 
	 * @param hasVampirUser
	 */
	public void calcuVamPir(EmperorUserBean hasVampirUser) {
		if (hasVampirUser.isDeath())
			return;
		if (hasVampirUser.getHasVampire() == 1) {
			hasVampirUser.setHasVampire(2);
		} else if (hasVampirUser.getHasVampire() == 2) {
			hasVampirUser.setHasVampire(0);
			int startBlood = hasVampirUser.getBlood();
			if (startBlood < hasVampirUser.getRole().getSumBlood()) {
				hasVampirUser.setBlood(startBlood + 1);
				StringBuilder poisonInfo = new StringBuilder();
				poisonInfo.append(hasVampirUser.getRole().getName());
				poisonInfo.append("噬血1滴!");
				InfoBean infoBean = new InfoBean();
				infoBean.setRound(round);
				infoBean.setInfo(poisonInfo.toString());
				informationList.addFirst(infoBean);
			}
		}
	}

	/**
	 * 设置死亡
	 * 
	 * @param beHit
	 */
	public void setDie (EmperorUserBean beHit) {
		beHit.setBlood(0);
		beHit.setDieRound(round);
		beHit.setDeath(true);
		if (beHit.getSide() == 0) {
			aliveNumA--;
			userListA.remove(beHit);
			userListA.addLast(beHit);
		} else {
			aliveNumB--;
			userListB.remove(beHit);
			userListB.addLast(beHit);
		}
		if (oneTurnDeathInfo.length() > 0) {
			oneTurnDeathInfo.append(",");
		}
		oneTurnDeathInfo.append(beHit.getRole().getName()); // 加死亡日志
	}
	
	/**
	 * 重新设置用户
	 * 
	 */
	public void reSetUser(EmperorUserBean user) {
		user.setOperation(0); // user用户操作信息
		user.setBeSimpleHitTime(0); // user被普通击杀次数
		user.setAddBloodUser(null); // 给user加血的人
		user.setAddDefUser(null); // 给user加防的人
		user.setAddHurtUser(null); // 给user加普杀的人
		user.setAddSpecilHurtUser(null); // 给user加特殊伤害的人
		user.setCopySkillUser(null); // 被user拷贝的人
		user.setEffectUser(null); // 被user操作的人
		user.setNoHitUser(null); // 不准user发技能的人
		user.setReductionHurtUser(null); // 给user减伤害的人
	}

	/**
	 * 把玩家按照座位顺序加到对应家族的userList,并设置出战家族
	 * 
	 */
	public void prepareUsers() {
		synchronized (fmASeat) {
			synchronized (userListA) {
				for (int i = 0; i < fmASeat.length; i++) {
					EmperorUserBean userBean = (EmperorUserBean) fmASeat[i];
					if (userBean != null)
						userListA.addLast(userBean);
				}
			}
			fmASeat = new Object[7];
		}
		synchronized (fmBSeat) {
			synchronized (userListB) {
				for (int i = 0; i < fmBSeat.length; i++) {
					EmperorUserBean userBean = (EmperorUserBean) fmBSeat[i];
					if (userBean != null)
						userListB.addLast(userBean);
				}
			}
			fmBSeat = new Object[7];
		}
		// ==start== 把未进入游戏的玩家加入游戏,未选择角色的玩家分配角色
//		if (waitUserA.size() > 0) {
//			synchronized (waitUserA) {
//				for (int i = 0; i < waitUserA.size(); i++) {
//					EmperorUserBean userWait = (EmperorUserBean) waitUserA.get(i);
//					if (userWait.getRole() == null) { // 分配角色
//						sysSetRole(userWait);
//					}
//					userListA.addLast(userWait);
//				}
//				waitUserA = new ArrayList();
//			}
//		}
//		if (waitUserB.size() > 0) {
//			synchronized (waitUserB) {
//				for (int i = 0; i < waitUserB.size(); i++) {
//					EmperorUserBean userWait = (EmperorUserBean) waitUserB.get(i);
//					if (userWait.getRole() == null) { // 分配角色
//						sysSetRole(userWait);
//					}
//					userListB.addLast(userWait);
//				}
//				waitUserB = new ArrayList();
//			}
//		}
		aliveNumA = userListA.size();
		aliveNumB = userListB.size();
		// 设置出战人员
		checkAddHurt();
	}

	/**
	 * 系统分配角色
	 * 
	 * @param beSet
	 */
//	public void sysSetRole(EmperorUserBean beSet) {
//		List roleList = fmAChooseRoleList;
//		if (beSet.getSide() == 1) {
//			roleList = fmBChooseRoleList;
//		}
//		for (int i = 0; i < roleList.size(); i++) {
//			EmperorChooseRoleBean chooseBean = (EmperorChooseRoleBean) roleList.get(i);
//			if (chooseBean.isBeChoose())
//				continue;
//			chooseBean.setBeChoose(true);
//			beSet.setRole(chooseBean.getRole());
//			beSet.setBlood(chooseBean.getRole().getSumBlood());
//			break;
//		}
//	}

	public EmperorGameBean() {
		init();
	}

	public void init() {
		if (roleProtoList == null)
			roleProtoList = EmperorAction.service.getPeopleList("1");
		if (roleProtoList.size() <= 0)
			return; // 数据库没数据
		List roleProtolistFb = new ArrayList(roleProtoList);
		// 把角色人物随机分给对战双方
		int count = 0; // 7个后停止
		while (count < 7) {
			setChooseBean (roleProtolistFb,true);
			setChooseBean (roleProtolistFb,false);
			count++;
		}
	}

	void setChooseBean (List basicList, boolean fmA) {
		int sumNum = basicList.size();
		int index = RandomUtil.nextInt(sumNum);
		EmperorRoleBean role = (EmperorRoleBean) basicList.get(index);
		EmperorChooseRoleBean chooseBean = new EmperorChooseRoleBean();
		chooseBean.setRole(role);
		if (fmA) {
			fmAChooseRoleList.add(chooseBean);
		} else {
			fmBChooseRoleList.add(chooseBean);
		}
		basicList.remove(role);
	}
	
	public boolean startGame() {
		if (!super.startGame())
			return false;
		task.setGame(this);
		lastRoundTime = System.currentTimeMillis();
		prepareUsers();// 第一回合时候把对战双方家族玩家按顺序放入userList 
		GameAction.fmTimer.scheduleAtFixedRate(task,
				new Date(System.currentTimeMillis() + 20000), 20000);
		return true;
	}

	public boolean endGame() {
		if (!super.endGame()) {
			return false;
		}
		// 释放资源
		task.cancel();
	//	fmAChooseRoleList = null;
	//	fmBChooseRoleList = null;
		return true;
	}

	public VsUserBean createUser() {
		return new EmperorUserBean();
	}

	public boolean addUser(EmperorUserBean userNew) {
		userNew.setHasAddToWait(true);
		checkUserList.add(userNew);
		if (userNew.getSide() == 0) {
			waitUserA.add(userNew);
		} else if (userNew.getSide() == 1) {
			waitUserB.add(userNew);
		}
		return true;
	}

	// 设置角色
	public void setRole(EmperorUserBean userNew, int index) {
		List chooseRoleList = new ArrayList();
		if (userNew.getSide() == 0) {
			chooseRoleList = fmAChooseRoleList;
		} else {
			chooseRoleList = fmBChooseRoleList;
		}
		if (index < 0 || index >= chooseRoleList.size()) {
			return;
		}
		EmperorChooseRoleBean chooseBean = (EmperorChooseRoleBean) chooseRoleList
				.get(index);
		if (chooseBean.beChoose)
			return; // 人物被选无法重复选择
		chooseBean.setBeChoose(true); // 设置人物已选
		userNew.setRole(chooseBean.getRole());
	}

	public String getGameUrl() {
		return "/fm/game/emp/enter.jsp";
	}

	public int calcGameResult() {
		if (aliveNumA > aliveNumB)
			return 0;
		else if (aliveNumB > aliveNumA)
			return 1;
		else
			return 2;
	}

	int round = 1;
	int aliveNumA; // A家族存活人数
	int aliveNumB; // B家族存活人数
	long lastRoundTime = 0l;
	List waitUserA = new ArrayList(); // 准备时间时A家族的玩家
	List waitUserB = new ArrayList(); // 准备时间时B家族的玩家
	List checkUserList = new ArrayList(); // MVP榜用
	List fmAChooseRoleList = new ArrayList(); // A家族玩家选择游戏人物的时候用,选择了一个,标记一个
	List fmBChooseRoleList = new ArrayList(); // B家族玩家选择游戏人物的时候用,选择了一个,标记一个
	List specilUserList = new ArrayList(); // 用来计算使用辅助技能玩家的操作(在计算击杀之前)
	LinkedList userListA = new LinkedList(); // 攻方玩家列表
	LinkedList userListB = new LinkedList(); // 防守方玩家列表
	LinkedList informationList = new LinkedList(); // 战斗信息
	Object[] fmASeat = new Object[7]; // A家族玩家出战顺序
	Object[] fmBSeat = new Object[7]; // B家族玩家出战顺序
	EmperorTask task = new EmperorTask(); // 30s 一统计

	public static int getOutNum() {
		return outNum;
	}

	public static void setOutNum(int outNum) {
		EmperorGameBean.outNum = outNum;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getAliveNumA() {
		return aliveNumA;
	}

	public void setAliveNumA(int aliveNumA) {
		this.aliveNumA = aliveNumA;
	}

	public int getAliveNumB() {
		return aliveNumB;
	}

	public void setAliveNumB(int aliveNumB) {
		this.aliveNumB = aliveNumB;
	}

	public long getLastRoundTime() {
		return lastRoundTime;
	}

	public void setLastRoundTime(long lastRoundTime) {
		this.lastRoundTime = lastRoundTime;
	}

	public List getWaitUserA() {
		return waitUserA;
	}

	public void setWaitUserA(List waitUserA) {
		this.waitUserA = waitUserA;
	}

	public List getWaitUserB() {
		return waitUserB;
	}

	public void setWaitUserB(List waitUserB) {
		this.waitUserB = waitUserB;
	}

	public List getCheckUserList() {
		return checkUserList;
	}

	public void setCheckUserList(List checkUserList) {
		this.checkUserList = checkUserList;
	}

	public List getFmAChooseRoleList() {
		return fmAChooseRoleList;
	}

	public void setFmAChooseRoleList(List fmAChooseRoleList) {
		this.fmAChooseRoleList = fmAChooseRoleList;
	}

	public List getFmBChooseRoleList() {
		return fmBChooseRoleList;
	}

	public void setFmBChooseRoleList(List fmBChooseRoleList) {
		this.fmBChooseRoleList = fmBChooseRoleList;
	}

	public List getSpecilUserList() {
		return specilUserList;
	}

	public void setSpecilUserList(List specilUserList) {
		this.specilUserList = specilUserList;
	}

	public LinkedList getUserListA() {
		return userListA;
	}

	public void setUserListA(LinkedList userListA) {
		this.userListA = userListA;
	}

	public LinkedList getUserListB() {
		return userListB;
	}

	public void setUserListB(LinkedList userListB) {
		this.userListB = userListB;
	}

	public LinkedList getInformationList() {
		return informationList;
	}

	public void setInformationList(LinkedList informationList) {
		this.informationList = informationList;
	}

	public Object[] getFmASeat() {
		return fmASeat;
	}

	public void setFmASeat(Object[] fmASeat) {
		this.fmASeat = fmASeat;
	}

	public Object[] getFmBSeat() {
		return fmBSeat;
	}

	public void setFmBSeat(Object[] fmBSeat) {
		this.fmBSeat = fmBSeat;
	}

	public EmperorTask getTask() {
		return task;
	}

	public void setTask(EmperorTask task) {
		this.task = task;
	}

	// StringBuilder fightInformation = new StringBuilder(); // 攻击日志
	// StringBuilder hideInfo = new StringBuilder(); // 躲避日志
	// StringBuilder beHurtInfo = new StringBuilder(); // 被击中的人
	// StringBuilder specilInfo = new StringBuilder(); // 特殊技能日志
	// StringBuilder deathInfo = new StringBuilder(); // 死亡日志
	StringBuilder oneTurnDeathInfo = new StringBuilder(); // 死亡日志

}
