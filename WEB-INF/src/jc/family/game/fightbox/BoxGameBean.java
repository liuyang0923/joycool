package jc.family.game.fightbox;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jc.family.game.GameAction;
import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsUserBean;
import net.joycool.wap.util.RandomUtil;

public class BoxGameBean extends VsGameBean{
	
	/**
	 *  父类的final方法,底层没用到
	 */
	private static final long serialVersionUID = 1L;
	public static String[] weapons = {"口", "剑","枪","弓","斧","矛","弩","双","我","防","攻","守","友","敌","杀"};
	public BoxGameBean() {
		init();
	}
	
	public void init() {
		Object[][] scene = this.scene;
		for (int i = 0; i < scene.length; i++) {
			for (int j = 0; j < scene[i].length; j++) {
				scene[i][j] = new BoxBean();
			}
		}
	}
	
	public static int GType = 1;
	
	FightBoxTask task = new FightBoxTask();
	
	public boolean startGame() {
		if (!super.startGame())
			return false;
		// 初始化
		task.setGame(this);
		lastRoundTime = System.currentTimeMillis();
		GameAction.fmTimer.scheduleAtFixedRate(task, new Date(System.currentTimeMillis() + 20000), 20000);
		
		return true;
	}

	public boolean endGame() {
		if(!super.endGame()) {
			return false;
		}
		task.cancel();
		// 统计数据
		Iterator iter = boxUserList.iterator();
		while(iter.hasNext()) {
			BoxUserBean vu = (BoxUserBean) iter.next();
			
			killed[vu.getSide()] += vu.getHitEnemyTime();
		}
		
		return true;
	}

	public VsUserBean createUser() {
		return new BoxUserBean();
	}

	int countFmA = 0; // A家族人数
	int countFmB = 0; // B家族人数
	int round = 1; // 回合
	int [] killed = new int[2];
	// 战场地图,存有相应的Boxbean或者null
	Object[][] scene = new Object[10][10]; // scene  [si:n]
	LinkedList informationList = new LinkedList(); // 战斗日志
	List boxUserList = new LinkedList(); // 参加游戏的玩家,不包括观众
	long lastRoundTime; 
	
	public int getKilled(int side) {
		return killed[side];
	}
	
	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getCountFmA() {
		return countFmA;
	}

	public void setCountFmA(int countFmA) {
		this.countFmA = countFmA;
	}

	public int getCountFmB() {
		return countFmB;
	}

	public void setCountFmB(int countFmB) {
		this.countFmB = countFmB;
	}

	public Object[][] getScene() {
		return scene;
	}

	public void setScene(Object[][] scene) {
		this.scene = scene;
	}
	
	public LinkedList getInformationList() {
		return informationList;
	}

	public void setInformationList(LinkedList informationList) {
		this.informationList = informationList;
	}

	public List getBoxUserList() {
		return boxUserList;
	}

	public void setBoxUserList(List boxUserList) {
		this.boxUserList = boxUserList;
	}

	public long getLastRoundTime() {
		return lastRoundTime;
	}

	Object [][] show = null;	// 计算得到的用于显示的数组
	
	public Object[][] getShow() {
		return show;
	}

	public void setShow(Object[][] show) {
		this.show = show;
	}

	public Object[][] getShow (BoxUserBean myBean, boolean current) {
		synchronized(this) {
			if(show == null) {
				show = new Object[10][10];
				for (int i = 0;i < boxUserList.size();i++) {
					BoxUserBean bu = (BoxUserBean) boxUserList.get(i);
					if (bu.isDeath())
						continue;
					setRole(bu);
				}
			}
		}
		Object[][] show2 = new Object[show.length][];
		for (int i = 0; i < show.length; i++) {
			show2[i] = (Object[]) show[i].clone();
		}

		if (myBean!=null && !myBean.isDeath) {
			BoxShowBean useShow = new BoxShowBean();
			useShow.setSide(myBean.getSide());
			useShow.setWeapon(8);
			show2[myBean.getCurrI()][myBean.getCurrJ()] = useShow;
			if(!current) {
				BoxShowBean useShow2 = new BoxShowBean();
				useShow2.setSide(myBean.getSide());
				useShow2.setWeapon(9);
				show2[myBean.getAimI()][myBean.getAimJ()] = useShow2;
				if (myBean.getMapState() == 3 && myBean.getAimAtack() != 0) {
					int[][] attackRange = myBean.getAtackRange();
					for (int i = 0; i < attackRange.length;i++) {			
						// 攻击范围有可能不足四个,其余的用负数表示不存在的攻击范围,则跳过
						if (attackRange[i][0] < 0 || attackRange[i][1] < 0 || attackRange[i][0] >= scene.length || attackRange[i][1] >= scene[attackRange[i][0]].length)
						continue;
						BoxShowBean useShow3 = new BoxShowBean();
						useShow3.setSide(14);
						show2[attackRange[i][0]][attackRange[i][1]] = useShow3;
					}
				}
			}
		}
		return show2;
	}

	public void setRole (BoxUserBean bu) {
		int currI = bu.getCurrI();
		int currJ = bu.getCurrJ();
		BoxShowBean boxShowBean = (BoxShowBean) show[currI][currJ];
		if (boxShowBean == null) {
			boxShowBean = new BoxShowBean();
			boxShowBean.setSide(bu.getSide());
			boxShowBean.setWeapon(bu.getWeapon());
		} else {
			boxShowBean.setPersonNum(2);
			if (boxShowBean.getSide() != bu.getSide())
				boxShowBean.setSide(7);
		}
		show[currI][currJ] = boxShowBean;
	}
	
	/**
	 * 从页面调用,返回一个weapons的下标用来显示给用户看
	 * 
	 * @param boxShowBean
	 * @param vsUser
	 * @return
	 */
	public int checkWeap (BoxShowBean boxShowBean, BoxUserBean vsUser) {
		if (boxShowBean == null)
			return 0;
		int boxSide = boxShowBean.getSide();
		if (boxSide == 7)
			return 7;
		if (boxSide == 14)
			return 14;
		int weap = boxShowBean.getWeapon();
		if (vsUser != null) {
			if (vsUser.getSide() != boxSide) {
				if (boxShowBean.getPersonNum() > 1)
					weap = 13;
				else 
					weap += 3;
			} else {
				if (boxShowBean.getPersonNum() > 1)
					weap = 12;
			}
		}
		if (vsUser == null) {
			if (boxSide == 0) {
				weap = 10;
			} else if (boxSide == 1) {
				weap = 11;
			}
		}
		return weap;
	}

	/**
	 * 20s一轮  (包括移动,攻击,血量输出信息)
	 */
	public void caculate() {
		Object[][] scene = getScene();
		BoxBean currLocation = null;
		BoxBean AimLocation = null;
		BoxUserBean boxUser = null;
		synchronized (boxUserList) {
			// 把所有玩家移动到相应位置
			for (int i = 0; i < boxUserList.size(); i++) {
				boxUser = (BoxUserBean) boxUserList.get(i);
				
				if (boxUser.isDeath) continue; // 一个人死了,就不用执行了,但如果是当前回合死了,还要执行
				// zhouj修改，这里不会出现本回合死亡，死亡在下一个循环才会出现。之前代码是if (boxUser.isDeath && boxUser.getDieRound() != round)
				if (boxUser.getAimAtack() == 0) {
					boxUser.setDefense(true);
				} else {
					boxUser.setAimAtack(0); // 为下一轮做准备
				}
				if (boxUser.getMapState() != 3) {
					boxUser.setDefense(true);
					continue; // 如果玩家未完成下达指令(指令状态分为移动1,攻击2，完成3),则跳过
				}
				
				if(boxUser.getCurrI() == boxUser.getAimI() && boxUser.getCurrJ() == boxUser.getAimJ()) continue;	// 没有移动
				currLocation = (BoxBean) scene[boxUser.getCurrI()][boxUser.getCurrJ()];
				AimLocation = (BoxBean) scene[boxUser.getAimI()][boxUser.getAimJ()];
				boxUser.setCurrI(boxUser.getAimI());
				boxUser.setCurrJ(boxUser.getAimJ());
				currLocation.getBoxUserList().remove(boxUser);
				AimLocation.getBoxUserList().add(boxUser);
			}
			StringBuilder information2 = new StringBuilder(); // 阵亡信息用
			for (int j = 0; j < boxUserList.size(); j++) {
				boxUser = (BoxUserBean) boxUserList.get(j);
				
				if (boxUser.getMapState() != 3) continue; // 如果玩家未完成下达指令(指令状态分为移动1,攻击2，完成3),则跳过
				
				if (!boxUser.isDefense) {
					hit (boxUser,information2);
				}
				boxUser.setMapState(0);
			}
			if (information2.length() > 0) {
				information2.append("阵亡");
				FightInformationBean fib = new FightInformationBean();
				fib.setInformation(information2.toString());
				fib.setRound(round);
				informationList.addFirst(fib);
			}
			lastRoundTime = System.currentTimeMillis();
			round++;
			show = null;
		}
		// 超过stopEnter这个时间后,场上没双方家族人了就结束游戏
		if (System.currentTimeMillis() > stopEnter) {
			if (countFmA <= 0 || countFmB <= 0) 
				endGame();
		}
	}
	
	// 战斗信息用
	public static String[] position = {"[攻]","[守]"};
	
	/**
	 * 攻击
	 * 
	 * @param game
	 * @param box
	 * @param boxUser1
	 */
	public void hit (BoxUserBean boxUser1,StringBuilder information2) {
		int [][] range = boxUser1.getAtackRange();
		int [] aimAtack = null;
		int aimI = -1;
		int aimJ = -1;
		int count = 0;
		BoxBean box = null;
		StringBuilder information = new StringBuilder(); // 战斗信息用
		information.append(position[boxUser1.getSide()]);
		information.append(boxUser1.getNickNameWml());
		information.append(getBasicInformation(boxUser1));
		for (int i=0;i<range.length;i++) {
			aimAtack = range[i];
			aimI = aimAtack[0];
			aimJ = aimAtack[1];
			// 攻击范围有可能不足四个,其余的用负数表示不存在的攻击范围,则跳过
			if (aimI < 0 || aimJ < 0 || aimI >= scene.length || aimJ >= scene[aimI].length)
				continue;
			box = (BoxBean) scene[aimI][aimJ];
			BoxUserBean boxUser2 = null;
			int blood = 0;
			int userBlood = 0;
			List list = box.getBoxUserList();
			for (int m = 0; m < list.size(); m++) {
				boxUser2 = (BoxUserBean) list.get(m);
				if (boxUser2.isDeath) continue;
				userBlood = boxUser2.getBlood();
				if (boxUser2.isDefense) {
					blood = 1;
				} else {
					blood = 2;
				}
				// 目标血量减少
				if(blood > userBlood)
					blood = userBlood;
				boxUser2.setBlood(userBlood - blood);
				// 击杀总血量统计
				if (boxUser1.getSide() == boxUser2.getSide()) {
					boxUser1.setHitFriendBlood(boxUser1.getHitFriendBlood() + blood);
				} else {
					boxUser1.setHitEnemyBlood(boxUser1.getHitEnemyBlood() + blood);
				}
				// 战斗日志用
				if (count > 0) {
					information.append("、");
				}
				information.append(position[boxUser2.getSide()]);
				information.append(boxUser2.getNickNameWml());
				count++;
				// 人物阵亡操作
				if (boxUser2.getBlood() == 0) {
					// 击杀数统计
					if (boxUser1.getSide() == boxUser2.getSide()) {
						boxUser1.setHitFriendTime(boxUser1.getHitFriendTime() + 1);
					} else {
						boxUser1.setHitEnemyTime(boxUser1.getHitEnemyTime() + 1);
					}
					boxUser2.setDeath(true);
					boxUser2.setDieRound(round);
					if (boxUser2.getSide() == 1) {
						countFmB--;
					} else {
						countFmA--;
					}
					list.remove(boxUser2);
					if (information2.length() > 0) {
						information2.append("、");
					}
					information2.append(position[boxUser2.getSide()]);
					information2.append(boxUser2.getNickNameWml());
				}
			}
		}
		// 如果有战斗,则添加日志
		if (count > 0) {
			FightInformationBean fib = new FightInformationBean();
			fib.setInformation(information.toString());
			fib.setRound(round);
			informationList.addFirst(fib);
		}
		boxUser1.setAtackRange(null);
	}
	
	static String[][] information = {
		{},
		{"一剑劈中","执剑一挥，砍中"},
		{"一枪刺中","手舞长枪，锋刃划伤"},
		{"拉弓如满月，一箭正中","施展百步穿杨，射中"},
	};
	
	/**
	 * 用户前台显示的人物被打信息
	 * 
	 */
	public static String getBasicInformation (BoxUserBean boxUser1) {
		int weap = boxUser1.getWeapon();
		if (weap > 3) {
			weap -= 3;
		}
		String[] basicInformation = information[weap];
		String[] myInformation = new String[basicInformation.length];
		for (int i = 0; i < basicInformation.length;i++) {
			myInformation[i] = basicInformation[i];
		}
		int random = RandomUtil.nextInt(myInformation.length);
		return myInformation[random];
	}
	
	public String getGameUrl() {
		return "/fm/game/fightbox/enter.jsp";
	}
	
	/**
	 * 输赢,返回赢家家族side,平的话返回2
	 * 
	 * @return
	 */
	public int calcGameResult() {
		if (countFmA > countFmB)
			return 0;
		else if (countFmB > countFmA)
			return 1;
		else
			return 2;
	}
	
}