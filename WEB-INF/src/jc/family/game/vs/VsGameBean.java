package jc.family.game.vs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.FundDetail;
import jc.family.game.GameAction;
import jc.util.SimpleChatLog2;

public class VsGameBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 准备状态
	 */
	public static int gameInit = 0;
	/**
	 * 游戏状态:开始
	 */
	public static int gameStart = 1;
	/**
	 * 游戏状态:结束
	 */
	public static int gameEnd = 2;
	/**
	 * 游戏类型
	 */
	public static int GType = 0;
	/**
	 * 游戏名
	 */
	public static VsConfig vsConfig[] = null;
	public static int maxGameId = 0; // 最大的游戏id + 1

	int id;
	int gameType;
	int fmIdA;// 发起家族
	int fmIdB;// 应战家族
	long wager;// 赌注
	int state;// 游戏状态
	int gameResult; // 0,1,2平局

	int fmCountA;// A家族人数
	int fmCountB;// B家族人数

	int userA;// 发起者
	int userB;// 应战者

	boolean isScore = false;// 是否是比赛,比赛就强制计算积分

	public void setisScoree(boolean isScore) {
		this.isScore = isScore;
	}

	protected long startTime = System.currentTimeMillis() + 1 * 10 * 1000;// 开始时间
	protected long endTime = startTime + 1 * 60 * 1000;// 结束时间
	protected long stopEnter = 0l;// 停止进入时间

	protected HashMap userMap = new HashMap();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGameIdName() {
		return getGameIdName(gameType);
	}

	public static String getGameIdName(int gameType) {
		if (vsConfig == null || vsConfig.length < gameType) {
			return "游戏";
		}
		return vsConfig[gameType].getName();
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public int getFmIdA() {
		return fmIdA;
	}

	public void setFmIdA(int fmIdA) {
		this.fmIdA = fmIdA;
	}

	public String getFmANameWml() {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(fmIdA);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public int getFmIdB() {
		return fmIdB;
	}

	public String getFmBNameWml() {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(fmIdB);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public void setFmIdB(int fmIdB) {
		this.fmIdB = fmIdB;
	}

	/**
	 * 得到对战家族
	 * 
	 * @param fmid
	 * @return
	 */
	public String getChallFmidNameWml(int fmid) {
		FamilyHomeBean fmHome = FamilyAction.getFmByID(fmid == fmIdA ? fmIdB : fmIdA);
		if (fmHome == null) {
			return "";
		}
		return fmHome.getFm_nameWml();
	}

	public long getWager() {
		return wager;
	}

	public void setWager(long wager) {
		this.wager = wager;
	}

	public HashMap getUserMap() {
		return userMap;
	}

	public void setUserMap(HashMap userMap) {
		this.userMap = userMap;
	}

	public VsUserBean getVsUser(int userId) {
		return (VsUserBean) userMap.get(Integer.valueOf(userId));
	}

	public void addVsUser(VsUserBean vu) {
		userMap.put(Integer.valueOf(vu.getUserId()), vu);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getGameResult() {
		return gameResult;
	}

	public void setGameResult(int gameResult) {
		this.gameResult = gameResult;
	}

	public int getFmCountA() {
		return fmCountA;
	}

	public void setFmCountA(int fmCountA) {
		this.fmCountA = fmCountA;
	}

	public int getFmCountB() {
		return fmCountB;
	}

	public void setFmCountB(int fmCountB) {
		this.fmCountB = fmCountB;
	}

	public int getUserA() {
		return userA;
	}

	public void setUserA(int userA) {
		this.userA = userA;
	}

	public int getUserB() {
		return userB;
	}

	public void setUserB(int userB) {
		this.userB = userB;
	}

	public String getGameUrl() {
		return "/fm/game/fmgame.jsp";
	}

	public void setTime() {
		VsConfig vsConfig = getVsConfig(gameType);
		startTime = System.currentTimeMillis() + vsConfig.getPreTime() * 60 * 1000;// 开始时间
		endTime = startTime + vsConfig.getMaxTime() * 60 * 1000;// 结束时间
		if (vsConfig.getEnterTime() != 0) {
			stopEnter = startTime + vsConfig.getEnterTime() * 60 * 1000;// 停止进入时间
		}
	}

	public boolean startGame() {
		synchronized (this) {
			if (state != gameInit)
				return false;
			state = gameStart;
		}
		startTime = System.currentTimeMillis();
		GameAction.fmTimer.schedule(new VsGameTask(id, 2), new Date(endTime));
		return true;
	}

	public boolean endGame() {
		synchronized (this) {
			if (state != gameStart)
				return false;
			state = gameEnd;
		}
		endTime = System.currentTimeMillis();
		gameResult = calcGameResult();
		generateDetail();
		FmVsScore fmVsScoreA = getFmVsScore(fmIdA);
		FmVsScore fmVsScoreB = getFmVsScore(fmIdB);
		VsBean vsBeanA = VsAction.getFmVsInfo(fmIdA);
		VsBean vsBeanB = VsAction.getFmVsInfo(fmIdB);
		int score = 0;
		if (gameResult == 2) {
			GameAction.updateFmFund(fmIdA, wager * VsAction.e);
			GameAction.updateFmFund(fmIdB, wager * VsAction.e);

			FamilyHomeBean fmHome = FamilyAction.getFmByID(fmIdA);
			VsAction.service.insertFmFundDetail(fmIdA, wager * VsAction.e, FundDetail.VS_GAME_TYPE, fmHome.getMoney());
			fmHome = FamilyAction.getFmByID(fmIdB);
			VsAction.service.insertFmFundDetail(fmIdB, wager * VsAction.e, FundDetail.VS_GAME_TYPE, fmHome.getMoney());

			fmVsScoreA.setTie(fmVsScoreA.getTie() + 1);
			fmVsScoreB.setTie(fmVsScoreB.getTie() + 1);
		} else if (gameResult == 0) {
			GameAction.updateFmFund(fmIdA, wager * VsAction.e * 195 / 100);

			FamilyHomeBean fmHome = FamilyAction.getFmByID(fmIdA);
			VsAction.service.insertFmFundDetail(fmIdA, wager * VsAction.e * 195 / 100, FundDetail.VS_GAME_TYPE,
					fmHome.getMoney());

			fmVsScoreA.setWin(fmVsScoreA.getWin() + 1);
			fmVsScoreB.setLose(fmVsScoreB.getLose() + 1);
			if (isScore || vsBeanA.getGameTime()[gameType] < vsConfig[gameType].getMatchScoreCount()
					&& vsBeanB.getGameTime()[gameType] < vsConfig[gameType].getMatchScoreCount()) {
				score = calcScore(fmVsScoreA.getScore(), fmVsScoreB.getScore(), false);
				fmVsScoreA.setScore(fmVsScoreA.getScore() + score);
				fmVsScoreB.setScore(fmVsScoreB.getScore() - score);
			}
		} else {
			fmVsScoreB.setWin(fmVsScoreB.getWin() + 1);
			fmVsScoreA.setLose(fmVsScoreA.getLose() + 1);
			GameAction.updateFmFund(fmIdB, wager * VsAction.e * 195 / 100);

			FamilyHomeBean fmHome = FamilyAction.getFmByID(fmIdB);
			VsAction.service.insertFmFundDetail(fmIdB, wager * VsAction.e * 195 / 100, FundDetail.VS_GAME_TYPE,
					fmHome.getMoney());

			if (isScore || vsBeanA.getGameTime()[gameType] < vsConfig[gameType].getMatchScoreCount()
					&& vsBeanB.getGameTime()[gameType] < vsConfig[gameType].getMatchScoreCount()) {
				score = calcScore(fmVsScoreB.getScore(), fmVsScoreA.getScore(), false);
				fmVsScoreA.setScore(fmVsScoreA.getScore() - score);
				fmVsScoreB.setScore(fmVsScoreB.getScore() + score);
			}
		}
		VsAction.vsService.upd("delete from fm_vs_challenge where id=" + id);
		calcScore(fmIdA, fmCountA, detail[0]);
		calcScore(fmIdB, fmCountB, detail[1]);
		if (!isScore) {
			vsBeanA.setChallengeId(0); // 发起挑战方数据清除
		}
		VsAction.vsService.insertExploits(gameType, startTime, fmIdA, fmIdB, id, score, wager * 10000, gameResult,
				userA, userB);
		VsAction.vsService.insertFmVsScore(fmVsScoreA);
		VsAction.vsService.updateFmVsInFo(vsBeanA);
		VsAction.vsService.insertFmVsScore(fmVsScoreB);
		VsAction.vsService.updateFmVsInFo(vsBeanB);

		vsBeanA.getGameTime()[gameType] += 1;
		VsAction.service.upd("update fm_vs_score set vs_time=vs_time+1 where fm_id=" + fmIdA + " and game_id="
				+ gameType);
		vsBeanB.getGameTime()[gameType] += 1;
		VsAction.service.upd("update fm_vs_score set vs_time=vs_time+1 where fm_id=" + fmIdB + " and game_id="
				+ gameType);
		return true;
	}

	private FmVsScore getFmVsScore(int fmId) {
		FmVsScore fmVsScoreA = VsAction.vsService.getFmVsScore("fm_id=" + fmId + " and game_id=" + gameType);
		if (fmVsScoreA == null) {
			fmVsScoreA = new FmVsScore(fmId, gameType);
		}
		return fmVsScoreA;
	}

	public static int calcScore(int winScore, int lostScore, boolean isTie) {
		if (isTie) {
			return 0;
		}
		int score = 10 - (winScore - lostScore) / 20;
		if (score < 0) {
			score = 0;
		} else if (score > 20) {
			score = 20;
		}
		return score;
	}

	public int calcGameResult() {
		return 2;
	}

	private void calcScore(int fmId, int userCount, String detail) {
		VsBean vsBean = VsAction.getFmVsInfo(fmId);
		if (!isScore) {
			vsBean.setGameid(0);
		}
		vsBean.setTime(vsBean.getTime() + 1);
		VsAction.vsService.insertDetail(fmId, id, userCount, detail);
	}

	public boolean addUser(FamilyUserBean fmUser) {
		synchronized (this) {
			VsUserBean user = getVsUser(fmUser.getId());
			if (user == null) {
				user = createUser(); // 调用重载的方法获得子类的userbean
				user.setUserId(fmUser.getId());
				user.setFmId(fmUser.getFm_id());
				VsConfig vsConfig = getVsConfig(gameType);
				if (user.getFmId() == fmIdA) {
					if (vsConfig.maxPlayer <= fmCountA) {
						return false;
					}
					user.setSide(0);
					fmCountA++;
				} else {
					if (vsConfig.maxPlayer <= fmCountB) {
						return false;
					}
					user.setSide(1);
					fmCountB++;
				}
				user.init(this);
				userMap.put(Integer.valueOf(user.getUserId()), user);
			}
		}
		return true;
	}

	public VsUserBean createUser() {
		return null;
	}

	/**
	 * 是否还允许进入
	 * 
	 * @return
	 */
	public boolean notStopEnter() {
		return stopEnter == 0 || stopEnter > System.currentTimeMillis();
	}

	protected SimpleChatLog2 chat = SimpleChatLog2.getChatLog(); // 用于本游戏内临时聊天
	protected SimpleChatLog2 chat0 = SimpleChatLog2.getChatLog(); // 用于side0聊天
	protected SimpleChatLog2 chat1 = SimpleChatLog2.getChatLog(); // 用于side1聊天

	public SimpleChatLog2 getChat() {
		return chat;
	}

	public void setChat(SimpleChatLog2 chat) {
		this.chat = chat;
	}

	public SimpleChatLog2 getChat0() {
		return chat0;
	}

	public void setChat0(SimpleChatLog2 chat0) {
		this.chat0 = chat0;
	}

	public SimpleChatLog2 getChat1() {
		return chat1;
	}

	public SimpleChatLog2 getChat(int side) {
		if (side == 0)
			return chat0;
		else
			return chat1;
	}

	public void setChat1(SimpleChatLog2 chat1) {
		this.chat1 = chat1;
	}

	public boolean isStart() {
		return state == gameStart;
	}

	public boolean isEnd() {
		return state == gameEnd;
	}

	String[] detail = null;

	public void generateDetail() {
		detail = new String[2];
		detail[0] = "";
		detail[1] = "";
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getStopEnter() {
		return stopEnter;
	}

	public long getEnterTime() {
		return getVsConfig(gameType).getEnterTime();
	}

	public int getMaxPlayer() {
		return getVsConfig(gameType).getMaxPlayer();
	}

	public void setStopEnter(long stopEnter) {
		this.stopEnter = stopEnter;
	}

	public static void loadVsConfig() {
		List list = VsAction.vsService.getVsConfigList("1 order by id");
		maxGameId = 0;
		for (int i = 0; i < list.size(); i++) {
			VsConfig vc = (VsConfig) list.get(i);
			if (vc.getId() > maxGameId)
				maxGameId = vc.getId();
		}
		maxGameId++;
		vsConfig = new VsConfig[maxGameId];
		for (int i = 0; i < list.size(); i++) {
			VsConfig vc = (VsConfig) list.get(i);
			vsConfig[vc.getId()] = vc;
		}
	}

	public static VsConfig getVsConfig(int gameType) {
		return vsConfig[gameType];
	}

	public VsConfig getVsConfig() {
		return vsConfig[gameType];
	}

	public static int getMaxGameId() {
		return maxGameId;
	}

	public static VsGameBean createVsGame(int gameType) {
		VsConfig game = vsConfig[gameType];
		if (game == null || game.getHide() == 1) { // 隐藏的游戏不允许开始
			return null;
		}
		return (VsGameBean) game.createGame();
	}

	static {
		loadVsConfig();
	}

	public boolean isScore() {
		return isScore;
	}

	public void setScore(boolean isScore) {
		this.isScore = isScore;
	}
}
