package jc.family.game.pvz;

import java.util.*;
import javax.servlet.http.*;

import net.joycool.wap.util.RandomUtil;
import jc.family.game.vs.VsAction;

public class PVZAction extends VsAction {

	public PVZAction() {

	}

	public PVZAction(HttpServletRequest request) {
		super(request);
	}

	public PVZAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public int getGameType() {
		return PVZGameBean.GType;
	}
	
	/**
	 * 得到当前僵尸是否是攻击状态(另外一个状态是前进)
	 * 
	 * @param zombieBean
	 * @param plantBean
	 * @return
	 */
	public boolean getCanAttack(ZombieBean zombieBean,PlantBean plantBean){
		if (plantBean != null) {
			if (plantBean.getProto().getType() == 4) { // 高防御
				zombieBean.setState(1); // 主要对撑杆僵尸(杆子掉落)和跳跳僵尸(弹簧掉落)起作用
				return true;
			} else {
				if (zombieBean.getProto().getType() != 1 && zombieBean.getProto().getType() != 2) {
						return true;
				} else {
					if (zombieBean.getState() == 1)
						return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 僵尸的移动或者攻击
	 * @param vsGame
	 */
	public void checkZombieMove (PVZGameBean vsGame, PVZUserBean vsUser) {
		Object[][] view = vsGame.getGameMap();
		if (this.hasParam("i")) {
			int indexI = this.getParameterInt("i");
			int indexJ = this.getParameterInt("j");
			int indexN = this.getParameterInt("n");
			if (indexI >= 0 && indexJ >= 0 && indexI < view.length && indexJ < view[0].length) {
				PVZBean pvzBean = (PVZBean) view[indexI][indexJ];
				LinkedList zombieList = pvzBean.getZombieList();
				if (zombieList.size() > 0) {
					if (indexN >= 0 && indexN < zombieList.size()) {
						ZombieBean zombieBean = (ZombieBean) zombieList.get(indexN);
						if (zombieBean.getActionTime() <= System.currentTimeMillis()) {
							PlantBean plantBean = pvzBean.getPlantBean();
							StringBuilder tips = new StringBuilder();
							if (getCanAttack (zombieBean ,plantBean)) { // 攻击
								if (plantBean.getProto().getType() == 3 && plantBean.getState() == 1) { // 番茄
									int attack = plantBean.getProto().getAttack();
									int hp = zombieBean.getHp() - attack;
									tips.append(plantBean.getProto().getName());
									tips.append("爆破,对");
									tips.append(zombieBean.getProto().getName());
									tips.append("造成");
									tips.append(attack);
									tips.append("伤害!");
									zombieBean.setHp(hp);
									if (hp < 0) {
										tips.append(zombieBean.getProto().getName());
										tips.append("死亡!");
										vsGame.setZombieDieNum(vsGame.getZombieDieNum() + 1);
										StringBuilder info = new StringBuilder();
										info.append(plantBean.getProto().getName());
										info.append("在第");
										info.append(indexJ+1);
										info.append("列炸掉了一个");
										info.append(zombieBean.getProto().getName());
										info.append(".");
										vsGame.getFightInformationList().addFirst(info.toString());
										zombieList.remove(zombieBean);
										vsGame.getZombieLineNum()[indexJ] -= 1;
									}
									request.setAttribute("tip", tips.toString());
									pvzBean.setPlantBean(null); // 番茄爆破死亡
								} else {
									int attack = zombieBean.getProto().getAttack();
									int hp = plantBean.getHp() - attack;
									tips.append(zombieBean.getProto().getName());
									tips.append("击中了");
									tips.append(plantBean.getProto().getName());
									tips.append("，造成");
									tips.append(attack);
									tips.append("伤害!");
									if (hp <= 0) {
										tips.append(plantBean.getProto().getName());
										tips.append("死亡!");
										pvzBean.setPlantBean(null);
										vsGame.setPlantDieNum(vsGame.getPlantDieNum() + 1);
										StringBuilder info = new StringBuilder();
										info.append(zombieBean.getProto().getName());
										info.append("在第");
										info.append(indexJ+1);
										info.append("列吃掉了一个");
										info.append(plantBean.getProto().getName());
										info.append(".");
										vsGame.getFightInformationList().addFirst(info.toString());
									} else {
										plantBean.setHp(hp);
									}
									zombieBean.setActionTime(System.currentTimeMillis() + zombieBean.getProto().getAttackCd());
									request.setAttribute("tip", tips.toString());
								}
							} else { // 移动
								if (indexI != 1) {
									zombieList.remove(zombieBean);
									((PVZBean) view[indexI - 1][indexJ]).getZombieList().add(zombieBean);
									long actionTime = 0l;
									if ((zombieBean.getProto().getType() == 1 || zombieBean.getProto().getType() == 2) && zombieBean.getState() == 0) {
										actionTime = System.currentTimeMillis() + zombieBean.getProto().getMoveCd()/2;
									} else {
										actionTime = System.currentTimeMillis() + zombieBean.getProto().getMoveCd();
									}
									zombieBean.setActionTime(actionTime);
									zombieBean.setCurrI(indexI--);
									tips.append(zombieBean.getProto().getName());
									tips.append("前进了一格!");
									request.setAttribute("tip", tips.toString());
								} else {
									int broken = vsGame.getBroken();
									// 攻陷
									PVZBean checkPvzBean = (PVZBean) view[0][indexJ];
									if (checkPvzBean.getState() != 2) {
										for (int m = 0; m < view.length; m++) {
											PVZBean pvzBean1 = (PVZBean) view[m][indexJ];
											pvzBean1.setState(2);
										}
										vsGame.setBroken(++broken);
										tips.append("大门已攻陷");
									}
									vsGame.getZombieLineNum()[indexJ] = 100;
									if (broken == 2) {	// 攻陷2个大门时,结束游戏
										vsGame.endGame();
										tips.append(",游戏胜利!");
									}
									request.setAttribute("tip", tips.toString());
								}
								if (plantBean != null && zombieBean.getProto().getType() == 1) zombieBean.setState(1); // 撑杆僵尸杆子掉落
							}
							vsUser.setOperNum(vsUser.getOperNum() + 1); // 增加用户操作数
						} else {
							request.setAttribute("tip", "攻击失败,技能冷却中!");
						}
					} 
				}
			}
		}
	}
	
	/**
	 * 植物的攻击
	 * @param view
	 * @param currI
	 * @param currJ
	 */
	public void plantAttack(PVZGameBean vsGame,PVZUserBean vsUser, int currI, int currJ) {
		Object[][] view = vsGame.getGameMap();
		PVZBean pvzBean = (PVZBean) view[currI][currJ];
		PlantBean plantBean = pvzBean.getPlantBean();
		if (plantBean != null) {
			if (plantBean.getActionTime() <= System.currentTimeMillis()) {
				plantBean.setActionTime(System.currentTimeMillis() + plantBean.getProto().getAttackCd());
				
				PlantProtoBean plantProto = plantBean.getProto();
				StringBuilder tips = new StringBuilder();
				if (plantProto.getType() == 1 || plantProto.getType() == 5) { //攻击型植物
					int space = plantProto.getAttackspace();
					int range = currI + space;
					if (range >= view.length) {range = view.length - 1;}
					for (int i = currI; i <= range; i++) {
						PVZBean tempPvzBean = (PVZBean) view[i][currJ];
						LinkedList zombieList = tempPvzBean.getZombieList();
						if (zombieList.size() > 0) {
							ZombieBean zombieBean = (ZombieBean) zombieList.get(0);
							int attack = 0;
							if (plantProto.getType() == 5 && RandomUtil.percentRandom(30)) { // 30%几率可以打出黄油的植物(代表植物:玉米)
								attack = plantProto.getAttack() * 2; // 攻击力翻倍
								zombieBean.setActionTime(zombieBean.getActionTime() + 10000); // 固定僵尸5s
							} else {
								attack = plantProto.getAttack();
							}
							int hp = zombieBean.getHp() - attack;
							tips.append(plantProto.getName());
							tips.append("击中了");
							tips.append(zombieBean.getProto().getName());
							tips.append("[第");
							tips.append(i + 1);
							tips.append("排]");
							tips.append(",造成");
							tips.append(attack);
							tips.append("伤害!");
							tips.append(zombieBean.getProto().getName());
							if (hp <= 0) {
								tips.append("死亡!");
								zombieList.remove(zombieBean);
								vsGame.setZombieDieNum(vsGame.getZombieDieNum() + 1);
								//豌豆在第二列杀死了一个普通僵尸。
								StringBuilder info = new StringBuilder();
								info.append(plantProto.getName());
								info.append("在第");
								info.append(currJ+1);
								info.append("列杀死了一个");
								info.append(zombieBean.getProto().getName());
								info.append(".");
								vsGame.getZombieLineNum()[currJ] -= 1;
								vsGame.getFightInformationList().addFirst(info.toString());
							} else {
								zombieBean.setHp(hp);
								tips.append("还有");
								tips.append(hp);
								tips.append("血.");
							}
							vsUser.setOperNum(vsUser.getOperNum() + 1);
							request.setAttribute("tip", tips.toString());
							break;
						}
						continue;
					}
					tips.append("攻击没有命中僵尸!");
				} else if (plantProto.getType() == 0){ // 阳光
					vsGame.plantSun +=plantProto.getAttack();
					vsUser.setOperNum(vsUser.getOperNum() + 1);
					tips.append("植物方获得阳光");
					tips.append(plantProto.getAttack());
					tips.append("!");
					if(vsGame.plantSun >= 10000) {
						vsGame.endGame();
					}
					request.setAttribute("tip", tips.toString());
				} else if (plantProto.getType() == 2 || plantProto.getType() == 4) {
					request.setAttribute("tip", "防御植物不用操作!");
				} else if (plantProto.getType() == 3) { // 播种后一段时间为成长期,长成后被咬就爆破,代表植物:番茄
					if (plantBean.getState() == 0) plantBean.setState(1);
					plantBean.setActionTime(999999999999999l);
					request.setAttribute("tip", "番茄长大了!");
				}
			} else {
				request.setAttribute("tip", "操作失败,技能冷却中!");
			}
		} else {
			request.setAttribute("tip", "植物已经死亡!");
		}
	}
}
