package jc.family.game.fightbox;

import java.io.IOException;
import java.util.*;
import javax.servlet.http.*;
import net.joycool.wap.util.RandomUtil;
import jc.family.game.vs.VsAction;

public class FightBoxAction extends VsAction {
	public FightBoxAction() {

	}

	public FightBoxAction(HttpServletRequest request) {
		super(request);
	}

	public FightBoxAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public int getGameType() {
		return BoxGameBean.GType;
	}
	
	/**
	 * 查看这个boxBean 里面是否包含自己
	 * 
	 * @param boxBean
	 * @param myId
	 * @return
	 */
	public static boolean isHasMe (BoxBean boxBean, int myId) {
		if (boxBean == null)
			return false;
		List list = boxBean.getBoxUserList();
		for (int i = 0; i < list.size(); i++) {
			BoxUserBean tempBean = (BoxUserBean) list.get(i);
			if (tempBean.getUserId() == myId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得一个点的box bean
	 * 
	 * @param game
	 * @return
	 */
	public BoxBean getBoxByIJ(BoxGameBean game) {
		Object[][] scene = game.getScene();
		int i = this.getParameterInt("i");
		int j = this.getParameterInt("j");
		if (i < 0 || j < 0 || i >= scene.length || j >= scene[i].length){
			return null;
		}
		return (BoxBean) scene[i][j];
	}
	
	/**
	 * 查看一个点该不该显示攻击连接
	 * 
	 * @param myBean
	 * @param i
	 * @param j
	 * @return
	 */
	public static boolean canAtack(BoxUserBean myBean,int i, int j) {
		if (myBean == null)
			return false;
		int currI = myBean.getAimI();
		int currJ = myBean.getAimJ();
		int tempI = Math.abs(currI - i);
		int tempJ = Math.abs(currJ - j);
		if (tempI < 2 && tempJ < 2) {
			return true;
		} else {
			return false;
		}
	}

	int [][] aimParam = {{5,1,6},{3,0,4},{7,2,8}};
	
	/**
	 * 用户操作完成的提示信息
	 */
	public void opreMessage () {
		int i = this.getParameterInt("i");
		int j = this.getParameterInt("j");
		BoxUserBean myBean = (BoxUserBean)getVsUser();
		if (myBean == null) {
			request.setAttribute("tip", "没有您的游戏信息!");
			return;
		}
		if (myBean.isDeath) {
			request.setAttribute("tip", "已经死亡,无法再操作!");
			return;
		}
		int aimI = myBean.getAimI();
		int aimJ = myBean.getAimJ();
		int tempI = Math.abs(aimI - i);
		int tempJ = Math.abs(aimJ - j);
		
		// 乱改参数,则跳转到攻击页面
		if (tempI > 1 || tempJ > 1) {
			try {
				response.sendRedirect("attack.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		int aimAttack = aimParam[i - aimI + 1][j - aimJ + 1];
		if (aimAttack != 0) {
			myBean.setAimAtack(aimAttack);
			myBean.setDefense(false);
			int[][] attackRange = getAttackRange(myBean.getAimI(),myBean.getAimJ(),myBean.getWeapon(),aimAttack);
			myBean.setAtackRange(attackRange);
			request.setAttribute("tip", "攻击指令已下达。");
		} else {
			request.setAttribute("tip", "防御指令已下达。");
		}
		myBean.setMapState(3);
//		myBean.setWatch(true);
	}
	
	public static int[][][][] attackRange = {
		{ { { -1, 0 }, { -2, 0 }, { -1, -1 }, { -1, 1 } },
				{ { 1, 0 }, { 2, 0 }, { 1, -1 }, { 1, 1 } },
				{ { 0, -1 }, { 0, -2 }, { 1, -1 }, { -1, -1 } },
				{ { 0, 1 }, { 0, 2 }, { 1, 1 }, { -1, 1 } },
				{ { -1, -1 }, { -2, -2 }, { -1, 0 }, { 0, -1 } },
				{ { -1, 1 }, { -2, 2 }, { -1, 0 }, { 0, 1 } },
				{ { 1, -1 }, { 2, -2 }, { 0, -1 }, { 1, 0 } },
				{ { 1, 1 }, { 2, 2 }, { 1, 0 }, { 0, 1 } }, },
		{ { { -2, 0 }, { -3, 0 }, { -2, -1 }, { -2, 1 } },
				{ { 2, 0 }, { 3, 0 }, { 2, 1 }, { 2, -1 } },
				{ { 0, -2 }, { 0, -3 }, { -1, -2 }, { 1, -2 } },
				{ { 0, 2 }, { 0, 3 }, { -1, 2 }, { 1, 2 } },
				{ { -2, -2 }, { -3, -3 }, { -1, -2 }, { -2, -1 } },
				{ { -2, 2 }, { -3, 3 }, { -1, 2 }, { -2, 1 } },
				{ { 2, -2 }, { 3, -3 }, { 1, -2 }, { 2, -1 } },
				{ { 2, 2 }, { 3, 3 }, { 1, 2 }, { 2, 1 } }, },
		{ { { -3, 0 }, { -4, 0 }, { -4, -1 }, { -4, 1 } },
				{ { 3, 0 }, { 4, 0 }, { 4, -1 }, { 4, 1 } },
				{ { 0, -3 }, { 0, -4 }, { -1, -4 }, { 1, -4 } },
				{ { 0, 3 }, { 0, 4 }, { -1, 4 }, { 1, 4 } },
				{ { -3, -3 }, { -4, -4 }, { -4, -5 }, { -5, -4 } },
				{ { -3, 3 }, { -4, 4 }, { -4, 5 }, { -5, 4 } },
				{ { 3, -3 }, { 4, -4 }, { 4, -5 }, { 5, -4 } },
				{ { 3, 3 }, { 4, 4 }, { 4, 5 }, { 5, 4 } }, } };
	
	/**
	 * 获得某人某点某方向的攻击范围(不做越界判断)
	 * @param i
	 * @param j
	 * @param weapon 武器: 1剑, 2枪, 3弓
	 * @param aimAtack 方向,1上, 2下, 3左, 4右, 5左上, 6右上, 7左下, 8右下
	 * @return
	 */
	public static int[][] getAttackRange(int i, int j, int weapon, int aimAttack) {
		if (weapon > 3) {
			weapon -= 3;
		}
		int[][] basicRange = attackRange[weapon - 1][aimAttack - 1];
		int[][] range = new int[4][2];
		for (int m = 0;m<basicRange.length;m++) {
			range[m][0] = basicRange[m][0] + i;
			range[m][1] = basicRange[m][1] + j;
		}
		return range;
	}

	// 出生点, 对战家族不一样
	public static int[][] iniLocationA = { { 2, 0 }, { 0, 5 }, { 1, 8 } };
	public static int[][] iniLocationB = { { 7, 9 }, { 9, 6 }, { 8, 1 } };
	
	// 获得一个出生地
	public int[] getIniLocation(int side) {
		int[][] iniLocation = iniLocationA;
		if (side == 1)
			iniLocation = iniLocationB;
		int random = RandomUtil.nextInt(iniLocation.length);
		return  iniLocation[random];
	}
	
}
