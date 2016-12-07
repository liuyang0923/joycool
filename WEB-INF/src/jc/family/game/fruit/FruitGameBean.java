package jc.family.game.fruit;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsUserBean;

public class FruitGameBean extends VsGameBean {

	private static final long serialVersionUID = 2L;
	public static int GType = 2;// 2代表水果战游戏

	public int trendId = 0; // 分配动态ID
	private FruitFamilyBean familyA;// 家族Ａ（挑战者0）的一些信息
	private FruitFamilyBean familyB;// 家族Ｂ（应战者1）的一些比赛信息

	private int orchardACount;// 家族Ａ（挑战者）的果园数
	private int orchardBCount;// 家族Ａ（挑战者）的果园数
	private int orchardWCount;// 野生果园数

	private LinkedHashMap fruitHitsMode = new LinkedHashMap();// 家族交流的信息，key为动态的ID
	private LinkedList logList = new LinkedList();

	public static int sunglassIncrease = 10;	// 升级透镜加10采集率
	private long beginWaitTime;

	public FruitGameBean() {
		beginWaitTime = System.currentTimeMillis();
		iniGame();
	}

	public void iniGame() {
		iniMapBean();
		iniAB();
	}

	/**
	 * 分配ID。加锁了的。
	 * 
	 * @return
	 */
	public int getTrendId() {
		synchronized (this) {
			return ++trendId;
		}
	}

	public boolean startGame() {
		if (!super.startGame())
			return false;
		
		long now = System.currentTimeMillis();
		startTime = now;
		for (int i = 0; i < orchardBean.length; i++) {// 初始化果园阳光增长开始时间
			for (int j = 0; j < orchardBean[0].length; j++) {
				OrchardBean o = orchardBean[i][j];
				if (o != null) {
					o.setTime0(now);
				}
			}
		}
		return true;
	}

	public boolean endGame() {
		if (!super.endGame())
			return false;

		long now = System.currentTimeMillis();
		endTime = now;
		setfinaSunCount(now);
		cancelAll();// 取消所有未执行的task
		return true;
	}

	/**
	 * 输赢,返回赢家家族side,平的话返回task
	 * 
	 * @return
	 */
	public int calcGameResult() {
		if (orchardACount > orchardBCount)
			return 0;
		else if (orchardBCount > orchardACount)
			return 1;
		else
			return 2;
	}

	public long getBeginWaitTime() {
		return beginWaitTime;
	}

	public void setBeginWaitTime(long beginWaitTime) {
		this.beginWaitTime = beginWaitTime;
	}

	/**
	 * 计算时间间隔，以X天X时X分X秒的格式返回
	 * 
	 * @return
	 */
	public String getLeftStartTime() {
		int sec = (int) ((startTime - beginWaitTime) - (DateUtil.getCurrentTime() - beginWaitTime)) / 1000;
		return getDateFormatString(sec);
	}

	public String getGameSpendTime() {
		int sec = (int) (endTime - startTime) / 1000;
		return getDateFormatString(sec);
	}

	public String getDateFormatString(int sec) {
		String time = "";
		int tian = sec / (60 * 60 * 24);
		if (tian > 0) {
			sec = sec - tian * 24 * 60 * 60;
			time = tian + "天";
		}
		int shi = sec / (60 * 60);
		if (shi > 0) {
			sec = sec - shi * 60 * 60;
			time = time + shi + "时";
		}
		int fen = sec / 60;
		if (fen > 0) {
			sec = sec - fen * 60;
			time = time + fen + "分";
		}
		return time + (sec == 0 ? "" : sec + "秒");
	}
	
	public String getGameUsedTime(){
		int sec = (int) (System.currentTimeMillis() - startTime) / 1000;
		if(getState() == gameInit ){
			return "0分0秒";
		}
		if(getState() == gameEnd){
			return getGameSpendTime();
		}
		return getDateFormatString(sec);
	}

	public String getGameUrl() {
		return "/fm/game/fruit/enter.jsp";
	}

	public VsUserBean createUser() {
		return new FruitUserBean();
	}

	public LinkedList getLogList() {
		return logList;
	}

	public void setLogList(LinkedList logList) {
		this.logList = logList;
	}

	public int getOrchardACount() {
		return orchardACount;
	}

	public void setOrchardACount(int orchardACount) {
		this.orchardACount = orchardACount;
	}

	public int getOrchardBCount() {
		return orchardBCount;
	}

	public void setOrchardBCount(int orchardBCount) {
		this.orchardBCount = orchardBCount;
	}

	public int getOrchardWCount() {
		return orchardWCount;
	}

	public void setOrchardWCount(int orchardWCount) {
		this.orchardWCount = orchardWCount;
	}

	public LinkedHashMap getFruitHitsMode() {
		return fruitHitsMode;
	}

	public void setFruitHitsMode(LinkedHashMap fruitHitsMode) {
		this.fruitHitsMode = fruitHitsMode;
	}

	public FruitFamilyBean getFamilyA() {
		return familyA;
	}

	public void setFamilyA(FruitFamilyBean familyA) {
		this.familyA = familyA;
	}

	public FruitFamilyBean getFamilyB() {
		return familyB;
	}

	public void setFamilyB(FruitFamilyBean familyB) {
		this.familyB = familyB;
	}

	private OrchardBean[][] orchardBean = new OrchardBean[12][9];

	public OrchardBean[][] getOrchardBean() {
		return orchardBean;
	}

	public void setOrchardBean(OrchardBean[][] orchardBean) {
		this.orchardBean = orchardBean;
	}

	// 初始化 果园分布地图
	public void iniMapBean() {

		int AC = 4;
		int BC = 4;
		// long time=System.currentTimeMillis();

		// 分别产生两队随机的果园阳光采集率五个
		int[] fAOrchard = getRankOrchard();
		int[] fBOrchard = getRankOrchard();

		int a[] = { 2, 2, 2, 2, 2, 1, 1, 1 };
		int b[] = { 3, 3, 3, 3, 3, 1, 1, 1 };

		int c[] = new int[24];
		int d[] = new int[24];

		int e[] = { 1, 1, 1, 1, 1 };
		int f[] = new int[24];

		int g[] = { 1, 1, 1, 1, 1 };
		int h[] = new int[24];

		int v[] = { 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 };

		// AB列随机生成
		for (int i = 0; i < a.length; i++) {
			int num = 0;
			do {
				num = net.joycool.wap.util.RandomUtil.nextInt(c.length);
			} while (c[num] != 0);
			c[num] = a[i];
		}

		// HI列随机生成
		for (int i = 0; i < b.length; i++) {
			int num = 0;
			do {
				num = net.joycool.wap.util.RandomUtil.nextInt(d.length);
			} while (d[num] != 0);
			d[num] = b[i];
		}

		// CD列生成随机野生果园五个
		for (int i = 0; i < e.length; i++) {
			int num = 0;
			do {
				num = net.joycool.wap.util.RandomUtil.nextInt(f.length);
			} while (f[num] != 0);
			f[num] = e[i];
		}

		// FG列生成随机野生果园五个
		for (int i = 0; i < g.length; i++) {
			int num = 0;
			do {
				num = net.joycool.wap.util.RandomUtil.nextInt(h.length);
			} while (h[num] != 0);
			h[num] = g[i];
		}

		// 将一维数组c、f、v、d、h组合成一个二维数据
		for (int i = 0; i < orchardBean[1].length; i++) {// 以列循环
			for (int j = 0; j < orchardBean.length; j++) {// 这是行
				if (i == 0) {

					if (c[j] == 2) {// 挑战方的果园
						orchardBean[j][i] = FruitAction.makeFamilyOrchard(j, i, fAOrchard[AC], 0);
						orchardBean[j][i].setTime0(startTime);
						AC--;
					} else if (c[j] == 1) {// 野生果园
						orchardBean[j][i] = FruitAction.makeWildOrchard(1, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (c[j] == 0) {// 路
						orchardBean[j][i] = null;
					}

				} else if (i == 1) {

					if (c[j + 12] == 2) {
						orchardBean[j][i] = FruitAction.makeFamilyOrchard(j, i, fAOrchard[AC], 0);
						orchardBean[j][i].setTime0(startTime);
						AC--;
					} else if (c[j + 12] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(1, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (c[j + 12] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 2) {

					if (f[j] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(2, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (f[j] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 3) {

					if (f[j + 12] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(2, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (f[j + 12] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 4) {

					if (v[j] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(3, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (v[j] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 5) {

					if (h[j] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(2, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (h[j] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 6) {

					if (h[j + 12] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(2, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (h[j + 12] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 7) {

					if (d[j] == 3) {
						orchardBean[j][i] = FruitAction.makeFamilyOrchard(j, i, fBOrchard[BC], 1);
						orchardBean[j][i].setTime0(startTime);
						BC--;
					} else if (d[j] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(1, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (d[j] == 0) {
						orchardBean[j][i] = null;
					}

				} else if (i == 8) {

					if (d[j + 12] == 3) {// 应战方的果园
						orchardBean[j][i] = FruitAction.makeFamilyOrchard(j, i, fBOrchard[BC], 1);
						orchardBean[j][i].setTime0(startTime);
						BC--;
					} else if (d[j + 12] == 1) {
						orchardBean[j][i] = FruitAction.makeWildOrchard(1, j, i);
						orchardBean[j][i].setTime0(startTime);
					} else if (d[j + 12] == 0) {
						orchardBean[j][i] = null;
					}

				}
			}
		}
		return;
	}

	public void iniAB() {// 初始化 对战家族 的信息,和果园数
		familyA = new FruitFamilyBean();
		familyB = new FruitFamilyBean();
		orchardACount = 5;// 初始化每个家族五个果园
		orchardBCount = 5;
		orchardWCount = 18;
		return;
	}

	public int[] getRankOrchard() {// 随机产生几个五个阳光采集率，合为200
		int I = 0;// 中间变量
		int A = RandomUtil.nextInt(60 + I + 1 - 20) + 20;

		I = 40 - A;
		int B = RandomUtil.nextInt(60 + I + 1 - 20) + 20;

		I = 40 * 2 - B - A;
		int C = RandomUtil.nextInt(60 + I + 1 - 20) + 20;

		I = 40 * 3 - C - B - A;
		int D = RandomUtil.nextInt(60 + I + 1 - 20) + 20;

		int E = 200 - A - B - C - D;
		int[] rank = { A, B, C, D, E };
		return rank;
	}

	// 用于判断比赛是否结束
	public int checkGameOver() {// 判断比赛是否结束

		int state = getState();
		if (state == gameEnd) {
			return gameEnd;
		} else if (state == gameStart) {
			if (orchardACount == 0 || orchardBCount == 0) {
				endGame();
				return 0;
			}
			return gameStart;
		}
		return 4;// 以上都不满足时
	}

	public void getAttack(OrchardBean o1, OrchardBean o2, int fruitCount, int userID) {// 到时间攻击

		FruitUserBean user = (FruitUserBean) getVsUser(userID);
		int userSide = user.getSide();

		FruitFamilyBean bean1 = getFruitFamilyBean(userSide);

		synchronized(this) {
			FruitFamilyBean bean2 = getFruitFamilyBean(o2.getSide());
	
			if (o2.getSide() == userSide) {// 所攻打果园已经是自己家族的时候
	
				o2.setFruitCount(o2.getFruitCount() + fruitCount);
	
			} else {
	
				int leftFruit = 0;// 攻击后剩下的水果量
				int defLeftFruit = 0;	// 防御方剩下的水果
				float I = 0f;
				if (o2.side != 3) {
					I = (float) (fruitCount * (1 + 0.05 * bean1.getFruitATKGrade()) - o2.getFruitCount()
							* (1 + 0.05 * bean2.getFruitHP()));
				} else {
					I = (float) (fruitCount * (1 + 0.05 * bean1.getFruitATKGrade()) - o2.getFruitCount());
				}
	
				if (I > 0) {
					leftFruit = (int) Math.ceil(Math.abs(I) / (1 + 0.05 * bean1.getFruitATKGrade()));
				}
				if (leftFruit > 0) {
	
					if (userSide == 0) {
						orchardACount++;
					} else if (userSide == 1) {
						orchardBCount++;
					}
	
					if (o2.getSide() == 1) {
						orchardBCount--;
					} else if (o2.getSide() == 3) {
						orchardWCount--;
					} else if (o2.getSide() == 0) {
						orchardACount--;
					}
					
					int cost = fruitCount - leftFruit;	//  消耗的水果 
					logList.addFirst(getTrendsStr2(2, o2, o2.getFruitCount(), getFmNameBySide(userSide), getFmNameBySide(1 - userSide), userSide));// 添加占领
					
					bean1.setFruitBeatted(bean1.getFruitBeatted() + o2.getFruitCount());// 修改该家族消灭的水果数
					bean1.setFruitSacrificed(bean1.getFruitSacrificed() + cost);// 修改该家族水果烈士数
					bean1.setFruitTotalcount(bean1.getFruitTotalcount() - cost);// 修改该家族水果总量
					bean1.setSunTotalCaptureRate(bean1.getSunTotalCaptureRate() + o2.getSunCaptureRate());// 增加总的采集率
					if (o2.getSide() != 3) {
						bean2.setFruitBeatted(bean2.getFruitBeatted() + cost);
						bean2.setFruitSacrificed(bean2.getFruitSacrificed() + o2.getFruitCount());
						bean2.setFruitTotalcount(bean2.getFruitTotalcount() - o2.getFruitCount());
						bean2.setSunTotalCaptureRate(bean2.getSunTotalCaptureRate()-o2.getSunCaptureRate());//减去家族的采集率
					}
					o2.setSide(userSide);// 占领，修改果园归属方
					o2.setFruitCount(leftFruit);
					user.setBeatFruitCount(user.getBeatFruitCount() + o2.getFruitCount());// 修改用户的操作记录
					o2.setTime0(System.currentTimeMillis());// 攻下后果园阳光总量清零，时间清零
					o2.setSun0(0);
					
					o2.setSunCaptureRate(o2.getSunCaptureRate() - o2.getSunGlassGrade() * sunglassIncrease);
					o2.setSunGlassGrade(0);
					
					// 查看战斗是否结束
					checkGameOver();
	
				} else {
					if (o2.getSide() != 3)
						defLeftFruit = (int) Math.ceil(Math.abs(I) / (1 + 0.05 * bean2.getFruitHP()));
					else
						defLeftFruit = (int) Math.ceil(Math.abs(I));
					int cost = o2.getFruitCount() - defLeftFruit;
					if (o2.getSide() != 3) {
						bean2.setFruitBeatted(bean2.getFruitBeatted() + fruitCount);
						bean2.setFruitSacrificed(bean2.getFruitSacrificed() + cost);
						bean2.setFruitTotalcount(bean2.getFruitTotalcount() - cost);
						logList.addFirst(getTrendsStr2(3, o2, fruitCount, getFmNameBySide(userSide), getFmNameBySide(1 - userSide), userSide));// 防守成功
					} else {
						logList.addFirst(getTrendsStr2(4, o2, fruitCount, getFmNameBySide(userSide), null, userSide));
					}
					bean1.setFruitBeatted(bean1.getFruitBeatted() + cost);
					bean1.setFruitSacrificed(bean1.getFruitSacrificed() + fruitCount);
					bean1.setFruitTotalcount(bean1.getFruitTotalcount() - fruitCount);
					o2.setFruitCount(defLeftFruit);
					user.setBeatFruitCount(user.getBeatFruitCount() + cost);// 修改用户的操作记录
				}
	
			}
		}
	}

	public void setfinaSunCount(long now) {
		int sunA = 0;
		int sunB = 0;
		
		int sunAGlass = 0;
		int sunBGlass = 0;
		for (int i = 0; i < orchardBean.length; i++) {
			for (int j = 0; j < orchardBean[0].length; j++) {
				OrchardBean o = orchardBean[i][j];
				if (o != null) {
					if (o.getSide() == 0) {
						sunA += o.getSunCount(now);
						sunAGlass+=o.getSunCaptureRate();
					} else if (o.getSide() == 1) {
						sunB += o.getSunCount(now);
						sunBGlass+=o.getSunCaptureRate();
					}
					o.setSunCount(o.getSunCount(now));
				}
			}
		}
		familyA.setSunTotalCount(sunA);
		familyA.setSunTotalCaptureRate(sunAGlass);
		familyB.setSunTotalCount(sunB);
		familyB.setSunTotalCaptureRate(sunBGlass);
	}

	public FruitFamilyBean getFruitFamilyBean(int side) {// 根据side得到那个家族的家族游戏信息
		if (side == 0) {
			return familyA;
		} else if (side == 1) {
			return familyB;
		}
		return null;
	}

	public String getFmNameBySide(int side) {
		if (side == 0) {
			return getFmANameWml();
		} else if (side == 1) {
			return getFmBNameWml();
		}
		return null;
	}

	public int getOrchardCountBySide(int side) {
		if (side == 0) {
			return orchardACount;
		} else if (side == 1) {
			return orchardBCount;
		} else if (side == 3) {
			return orchardWCount;
		}
		return 0;
	}

	/**
	 * 取得战斗动态文字
	 * 
	 * @return
	 */
	public String getTrendsStr(int actionType, OrchardBean o1, OrchardBean o2, int fruitCount, long arriveTime,
			String fmAName, int ttype, int grade) {
		StringBuilder sb = new StringBuilder();
		switch (actionType) {
		case (1): { // 攻击
			if (o1.getSide() == 0) {
				sb.append("■");
			} else if (o1.getSide() == 1) {
				sb.append("▲");
			}
			sb.append(o1.getOrchardName());
			sb.append("果园向");
			if (o2.getSide() == 0) {
				sb.append("■");
			} else if (o2.getSide() == 1) {
				sb.append("▲");
			} else if (o2.getSide() == 3) {
				sb.append("野生");
			}
			sb.append(o2.getOrchardName());
			sb.append("果园扔出");
			sb.append(fruitCount);
			sb.append("水果,预计");
			sb.append(arriveTime);
			sb.append("秒后抵达.");
			break;
		}
		case (4): { // 科技信息
			if (o1.getSide() == 0) {
				sb.append("■");
			} else if (o1.getSide() == 1) {
				sb.append("▲");
			}
			sb.append(fmAName);
			sb.append(o1.getOrchardName());
			sb.append("果园[");
			if (ttype == 1) {
				sb.append("尖刺果皮");
			} else if (ttype == 2) {
				sb.append("加厚果皮");
			} else if (ttype == 4) {
				sb.append("果影分身");
			} else if (ttype == 3) {
				sb.append("喷气水果");
			} else if (ttype == 5) {
				sb.append("阳光透镜");
			}
			sb.append("]等级");
			sb.append(grade);
			sb.append("升级成功!");
			break;
		}
		case (5): {// 战况
			sb.append(o1.getOrchardName());
			sb.append("->");
			sb.append(o2.getOrchardName());
			sb.append(",");
			sb.append(fruitCount);
			sb.append(",");
			break;
		}
		}
		return sb.toString();
	}
	
	public String getTrendsStr2(int actionType, OrchardBean o2, int fruitCount, String fromFm, String toFm, int userSide) {
		StringBuilder sb = new StringBuilder();
		switch (actionType) {
		case (2): { // 占领
			if (userSide == 0) {
				sb.append("■");
			} else if (userSide == 1) {
				sb.append("▲");
			}
			sb.append(fromFm);
			sb.append("占领");
			sb.append(o2.getOrchardName());
			sb.append("果园,消灭");
			if (o2.getSide() == 0) {
				sb.append("■");
				sb.append(toFm);
			} else if (o2.getSide() == 1) {
				sb.append("▲");
				sb.append(toFm);
			} else if (o2.getSide() == 3) {
				sb.append("野生");
			}
			sb.append("水果");
			sb.append(fruitCount);
			sb.append("个.");
			break;
		}
		case (3): { // 防守
			if (o2.getSide() == 0) {
				sb.append("■");
			} else if (o2.getSide() == 1) {
				sb.append("▲");
			}
			sb.append(toFm);
			sb.append("成功防御了攻击,消灭");
			if (userSide == 0) {
				sb.append("■");
				sb.append(fromFm);
			} else if (userSide == 1) {
				sb.append("▲");
				sb.append(fromFm);
			}
			sb.append("水果");
			sb.append(fruitCount);
			sb.append("个.");
			break;
		}
		case (4): { // 攻打野生果园失败
			if (userSide == 0) {
				sb.append("■");
			} else {
				sb.append("▲");
			}
			sb.append(fromFm);
			sb.append("攻打");
			sb.append(o2.getOrchardName());
			sb.append("果园失败,损失水果");
			sb.append(fruitCount);
			sb.append("个.");
			break;
		}
		}
		return sb.toString();
	}

	public void cancelAll() {
		Iterator iter = fruitHitsMode.values().iterator();
		while (iter.hasNext()) {
			FruitTask task = (FruitTask) iter.next();
			task.cancel();
		}
		fruitHitsMode.clear();
	}
}
