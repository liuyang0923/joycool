package jc.family.game.emperor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jc.family.game.vs.VsAction;

public class EmperorAction extends VsAction {
	public static EmperorService service = new EmperorService();

	public EmperorAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * 确定玩家用的是什么招数
	 * 
	 * @param vsGame
	 * @param userBean
	 * @param attack
	 * @return
	 */
	public boolean checkAttack(EmperorGameBean vsGame, EmperorUserBean vsUser, int attack, boolean copy) {
		if (attack != 1 && attack != 2) 
			return false;
		vsUser.setOperation(attack);
		vsUser.setEffectUser(null);
		if (attack == 2) {
			int roleId = vsUser.getRole().getSkillId();
			if (copy) {roleId = vsUser.getCopySkillUser().getRole().getSkillId();}
			if (roleId < 0 || roleId > 10) 
				return false;
			else if (roleId > 4)
				vsUser.setOperation(3); // 辅助技能
		}
		return true;
	}

	public boolean setHitUser(EmperorGameBean vsGame, EmperorUserBean vsUser, List listA, List listB,boolean copy) {
		if (!this.hasParam("l") || !this.hasParam("i")) {
			return false;
		}
		vsGame.getSpecilUserList().remove(vsUser);
		List effectUserList = null;
		if ("a".equals(this.getParameterString("l"))) {
			EmperorRoleBean role = vsUser.getRole();
			if (vsUser.getCopySkillUser() != null) {
				role = vsUser.getCopySkillUser().getRole();
			}
			if (vsUser.getOperation() != 3 || role.getSkillType() != 2 || role.getEffectSide() != 0) // 操作友方队员必须是辅助技能
				return false;
			effectUserList = listA;
		} else {
			effectUserList = listB;
		}
		int index = this.getParameterInt("i");
		if (index < 0 || index >= effectUserList.size()) {
			return false;
		}
		EmperorUserBean beEffectUser = (EmperorUserBean) effectUserList.get(index);
		if (copy) {
			if (vsUser.getRole().getSkillId() != 10 || vsUser.getOperation() == 1) {
				return false;
			}
			if (!beEffectUser.isAhead() || beEffectUser.getRole().getSkillType() > 2) { // 不能复制被动技能,不能复制非出战队员的技能
				return false;
			}
			vsUser.setCopySkillUser(beEffectUser);
			return true;
		}
		// 查看玩家技能是否超过技能使用范围
		if (vsUser.getOperation() > 1 && vsUser.getRole().getEffectRange() == 1 && !beEffectUser.isAhead()) {
			return false;
		}
		vsUser.setEffectUser(beEffectUser);
		if (vsUser.getOperation() == 3) {
			vsGame.getSpecilUserList().add(vsUser);
		}
		return true;
	}
	
	/**
	 * 玩家选择角色
	 * 
	 * @param vsGame
	 * @param vsUser
	 * @param roleList
	 * @param index
	 */
	public void chooseRole(EmperorGameBean vsGame, EmperorUserBean vsUser, int index) {	
		List roleList = vsGame.getFmAChooseRoleList();
		if (vsUser.getSide() == 1) {
			roleList = vsGame.getFmBChooseRoleList();
		}
		if (roleList == null || roleList.size() < 1) {
			request.setAttribute("tip", "已无角色可以选,请联系管理员!");
			return;
		}
		if (index < 0 || index > roleList.size()) {
			request.setAttribute("tip", "无此角色,请重新选择!");
			return;
		}
		if (vsUser.getRole() != null) {
			request.setAttribute("tip", "您已经选择角色,无法更改!");
			request.setAttribute("result", "success");
			return;
		}
		synchronized (roleList) {
			EmperorChooseRoleBean chooseBean = (EmperorChooseRoleBean) roleList.get(index);
			if (chooseBean.beChoose) {
				request.setAttribute("tip", "该角色已有人选择,请重新选择!");
				return;
			}
			chooseBean.setBeChoose(true);
			chooseBean.setChooseUserNameWml(vsUser.getNickNameWml());
			vsUser.setBlood(chooseBean.getRole().getSumBlood()); // 初始化血量
			vsUser.setRole(chooseBean.getRole());
			request.setAttribute("tip", "角色选择成功!");
			request.setAttribute("result", "success");
		}
	}
	
	/**
	 * 往游戏中加入玩家
	 * 
	 * @param vsGame
	 * @param vsUser
	 */
	public void addVsUser (EmperorGameBean vsGame, EmperorUserBean vsUser) {
		if (vsUser == null || vsUser.getRole() == null) {
			return;
		}
		List userList = vsGame.getUserListA();
		int aliveNum = vsGame.getAliveNumA();
		if (vsUser.getSide() != 0) {
			userList = vsGame.getUserListB();
			aliveNum = vsGame.getAliveNumB();
		}
		synchronized (userList) {
			if (userList.contains(vsUser)) 
				return;
			if (userList.size() <= EmperorGameBean.outNum) {
				vsUser.setAhead(true);
			}
			userList.add(aliveNum,vsUser);
			if (vsUser.getSide() == 0) {
				vsGame.setAliveNumA(aliveNum + 1);
			} else {
				vsGame.setAliveNumB(aliveNum + 1);
			}
			if (System.currentTimeMillis() > vsGame.getStopEnter() + 120000){vsGame.setDie(vsUser);} // 超过一定时间的直接死亡
		}
	}
	
	/**
	 * 站起&坐下
	 * 
	 * @param seats
	 * @param vsUser
	 */
	public void sit (Object[] seats,EmperorGameBean vsGame, EmperorUserBean vsUser) {
		int sid = this.getParameterInt("sid");
		if (sid < 0 || sid > seats.length - 1) {
			request.setAttribute("tip","位置选择错误,请重新选择!");
			return;
		}
		if (this.hasParam("d")) {
			if (!vsUser.hasSit) {
				return;
			}
			EmperorUserBean seatUser = (EmperorUserBean) seats[sid];
			if (seatUser != null && seatUser.getUserId() == vsUser.getUserId()) {
				seats[sid] = null;
				vsUser.setHasSit(false);
				if (vsUser.getSide() == 0) {
					vsGame.getWaitUserA().add(vsUser);
				} else {
					vsGame.getWaitUserB().add(vsUser);
				}
			} 
		} else {
			if (vsUser.hasSit) {
				return;
			}
			EmperorUserBean seatUser = (EmperorUserBean) seats[sid];
			if (seatUser == null) {
				if (vsUser.getSide() == 0) {
					vsGame.getWaitUserA().remove(vsUser);
				} else {
					vsGame.getWaitUserB().remove(vsUser);
				}
				vsUser.setHasSit(true);
				seats[sid] = vsUser;
			} else {
				request.setAttribute("tip","位置已有人坐,请重新选择座位!");
			}
		}
	}
	
	/**
	 * 获得家族椅子
	 * 
	 * @param vsGame
	 * @param vsUser
	 * @return
	 */
	public Object[] getSeats (EmperorGameBean vsGame, EmperorUserBean vsUser) {
		Object[] seats = vsGame.getFmASeat();
		if (vsUser.getSide() == 1) {
			seats = vsGame.getFmBSeat();
		}
		return seats;
	}
	
	public int getGameType() {
		return EmperorGameBean.GType;
	}
}
