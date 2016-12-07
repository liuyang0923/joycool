package jc.family.game.vs;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.FundDetail;
import jc.family.game.GameAction;
import jc.family.game.vs.term.TermBean;
import jc.family.game.vs.term.TermMatchBean;
import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;

public class VsAction extends FamilyAction {

	public static VsService vsService = new VsService();
	public static HashMap fmVsGameMap = new HashMap();
	public static HashMap fmVsInfoMap = new HashMap();
	public static long e = 100000000L;
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-M-d");
	public static DecimalFormat numFormat = new DecimalFormat("0.##");
	VsGameBean game = null;
	VsUserBean vsUser = null;

	public VsAction() {

	}

	public VsAction(HttpServletRequest request) {
		super(request);
		game = getVsGame2();
		if (game != null)
			vsUser = game.getVsUser(getLoginUser().getId());
	}

	public VsAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		game = getVsGame2();
		if (game != null)
			vsUser = game.getVsUser(getLoginUser().getId());
	}

	public VsUserBean getVsUser() {
		return vsUser;
	}

	/**
	 * 获得家族对战信息
	 * 
	 * @param fmId
	 * @return
	 */
	public static VsBean getFmVsInfo(int fmId) {
		VsBean vsBean = null;
		Integer key = Integer.valueOf(fmId);
		synchronized (fmVsInfoMap) {
			vsBean = (VsBean) fmVsInfoMap.get(key);
			if (vsBean == null) {
				vsBean = vsService.getFmVsInFo("fm_id=" + fmId);
				if (vsBean == null) {
					vsBean = new VsBean(fmId);
					vsService.insertFmVsInFo(vsBean);
				} else {
					List list = vsService.getFmVsScoreList("fm_id=" + fmId);
					for (int i = 0; i < list.size(); i++) {
						FmVsScore fm = (FmVsScore) list.get(i);
						vsBean.gameTime[fm.getGameId()] = fm.getVsTime();
					}
				}
				fmVsInfoMap.put(key, vsBean);
			}
		}
		return vsBean;
	}

	/**
	 * 获得游戏信息
	 * 
	 * @param fmId
	 * @return
	 */
	public static VsGameBean getVsGameById(int gameId) {
		VsGameBean vsGameBean = null;
		Integer key = Integer.valueOf(gameId);
		synchronized (fmVsGameMap) {
			vsGameBean = (VsGameBean) fmVsGameMap.get(key);
		}
		return vsGameBean;
	}

	/**
	 * 得到游戏Bean
	 * 
	 * @param gameId
	 * @return
	 */
	public VsGameBean getVsGame(int gameType) {
		VsGameBean game = (VsGameBean) session.getAttribute("vsgame");
		if (game != null && game.getGameType() == gameType)
			return game;
		return null;
	}

	public VsGameBean getVsGame2() {
		VsGameBean game = (VsGameBean) session.getAttribute("vsgame");
		if (game != null && game.getGameType() == getGameType())
			return game;
		return null;
	}

	public VsGameBean getVsGame() {
		return game;
	}

	/**
	 * 得到参加游戏人员
	 * 
	 * @param gemeId
	 * @return
	 */
	public VsUserBean getVsUser(int gemeId) {
		VsGameBean game = getVsGame(gemeId);
		return game.getVsUser(getLoginUser().getId());
	}

	/**
	 * 添加游戏bean
	 * 
	 * @param game
	 */
	public void addVsGame(VsGameBean game) {
		this.game = game;
		session.setAttribute("vsgame", game);
	}

	/**
	 * 处理对战成员
	 * 
	 * @return
	 */
	public boolean dealVsUser() {
		int userid = getParameterInt("uid");
		if (userid == 0) {
			this.tip = "ID输入错误,请重新输入.";
			return false;
		}
		FamilyUserBean fmMUser = getFmUser();
		if (fmMUser == null) {
			return false;
		}
		if (!FamilyUserBean.isflag_game(fmMUser.getFm_flags())) {
			this.tip = "操作失败,您没有权限";
			return false;
		}
		if (getFmId(userid) != fmMUser.getFm_id()) {
			this.tip = "操作失败,该用户不是您的家族成员.";
			return false;
		}
		FamilyUserBean fmUser = getFmUserByID(userid);
		if (fmUser == null) {
			this.tip = "操作失败,该用户不是您的家族成员.";
			return false;
		}
		if (FamilyUserBean.isVsGame(fmUser.getFm_state())) {
			fmUser.setFm_state(fmUser.getFm_state() - FamilyUserBean.VS_GAME);
			this.tip = "操作成功,已将" + fmUser.getNickNameWml() + "移除家族挑战精英.";
		} else {
			fmUser.setFm_state(fmUser.getFm_state() + FamilyUserBean.VS_GAME);
			this.tip = "操作成功,已将" + fmUser.getNickNameWml() + "添加到家族挑战精英.";
		}
		if (service.updateFmUser("fm_state = " + fmUser.getFm_state(), "id=" + fmUser.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * 开始挑战
	 * 
	 * @return
	 */
	public boolean startChallenge(int fmId, int prize, int gameId) {

		Challenge challenge = new Challenge(getFmId(), fmId, gameId, prize, getLoginUser().getId(),
				Challenge.undisposed);
		vsService.insertVsChallenge(challenge);

		FamilyHomeBean fm = FamilyAction.getFmByID(getFmId());
		sendMessageToGameM(fmId, challenge.getId(), fm.getFm_name());// 发通知

		VsBean vsBean = getFmVsInfo(getFmId());// 存入vsBean
		vsBean.setChallenge(true);
		vsBean.setChallFmid(fmId);
		vsBean.setChallGameId(gameId);
		vsBean.setChallTime(System.currentTimeMillis());
		vsBean.setChallengeId(challenge.getId());

		vsService.updateFmVsInFo(vsBean);
		// GameAction.updateFmFund(challenge.getFmA(), -challenge.getWager() *
		// e);// 扣钱

		// vsBean = getFmVsInfo(fmId);
		// vsBean.setAccept(vsBean.getAccept() + 1);

		return true;
	}

	/**
	 * 取消挑战
	 * 
	 * @param fmUser
	 * @param vsBean
	 * @return
	 */
	public boolean cancelchall(FamilyUserBean fmUser, VsBean vsBean) {
		Challenge challenge = vsService.getChallenge("id=" + vsBean.getChallengeId());
		if (challenge == null || challenge.getStatus() == Challenge.agree) {
			this.tip = "操作失败,该家族已接受了您的挑战邀请.";
			return false;
		}
		vsService.upd("delete from fm_vs_challenge where id=" + vsBean.getChallengeId());

		// GameAction.updateFmFund(challenge.getFmA(), challenge.getWager() *
		// e);// 扣钱

		vsBean.setChallenge(false);
		vsBean.setChallFmid(0);
		vsBean.setChallGameId(0);
		vsBean.setChallTime(0);
		vsBean.setChallengeId(0);

		vsService.updateFmVsInFo(vsBean);

		// this.tip = "取消挑战申请成功,退回挑战金" + challenge.getWager() + "亿.";
		this.tip = "取消挑战申请成功.";
		return true;

	}

	/**
	 * 
	 * 同意挑战游戏
	 * 
	 * @param vsGame
	 * @param challengeId
	 */
	public boolean agreeChallenge(Challenge challenge) {
		VsGameBean vsgame = VsGameBean.createVsGame(challenge.getGameId());
		if (vsgame == null) {
			return false;
		}

		GameAction.updateFmFund(challenge.getFmB(), -challenge.getWager() * e);// 扣钱
		service.insertFundHistory(challenge.getFmB(), 0, "", "扣除<" + VsGameBean.getGameIdName(challenge.getGameId())
				+ ">挑战金" + challenge.getWager() * e, 1);

		FamilyHomeBean fmHome = FamilyAction.getFmByID(challenge.getFmB());
		service.insertFmFundDetail(challenge.getFmB(), -challenge.getWager() * e, FundDetail.VS_GAME_TYPE,
				fmHome.getMoney());

		GameAction.updateFmFund(challenge.getFmA(), -challenge.getWager() * e);// 扣钱
		service.insertFundHistory(challenge.getFmA(), 0, "", "扣除<" + VsGameBean.getGameIdName(challenge.getGameId())
				+ ">挑战金" + challenge.getWager() * e, 1);

		fmHome = FamilyAction.getFmByID(challenge.getFmA());
		service.insertFmFundDetail(challenge.getFmA(), -challenge.getWager() * e, FundDetail.VS_GAME_TYPE,
				fmHome.getMoney());

		vsgame.setId(challenge.getId());
		vsgame.setFmIdA(challenge.getFmA());
		vsgame.setFmIdB(challenge.getFmB());
		vsgame.setGameType(challenge.getGameId());
		vsgame.setState(VsGameBean.gameInit);
		vsgame.setWager(challenge.getWager());
		vsgame.setUserA(challenge.getUserA());
		vsgame.setUserB(getLoginUser().getId());
		vsgame.setTime();

		synchronized (fmVsGameMap) {
			fmVsGameMap.put(Integer.valueOf(challenge.getId()), vsgame);
		}

		GameAction.fmTimer.schedule(new VsGameTask(challenge.getId()), new Date(vsgame.startTime));

		VsBean vsBean = getFmVsInfo(challenge.getFmA());

		vsBean.setChallenge(false);
		vsBean.setChallFmid(0);
		vsBean.setChallGameId(0);
		vsBean.setChallTime(0);
		vsBean.setChallengeId(0);

		vsBean.setGameid(challenge.getId());
		vsService.updateFmVsInFo(vsBean);
		vsBean = getFmVsInfo(challenge.getFmB());
		vsBean.setGameid(challenge.getId());
		vsService.updateFmVsInFo(vsBean);

		vsService.upd("update fm_vs_challenge set user_b=" + getLoginUser().getId() + ",fm_status=" + Challenge.agree
				+ " where id=" + challenge.getId());

		sendMessage(challenge.getFmA(), challenge.getFmB(), "[" + VsGameBean.getGameIdName(challenge.getGameId())
				+ "]挑战赛开始", "/fm/game/vs/vsgame.jsp?id=");

		return true;
	}


	/**
	 * 是否家族精英
	 * 
	 * @return
	 */
	public boolean isFmElites() {
		FamilyUserBean fmUser = getFmUser();
		return isFmElites(fmUser);
	}

	/**
	 * 是否家族精英
	 * 
	 * @return
	 */
	public boolean isFmElites(FamilyUserBean fmUser) {
		if (fmUser == null) {
			return false;
		}
		if (FamilyUserBean.isVsGame(fmUser.getFm_state())) {
			return true;
		}
		return false;
	}

	/**
	 * 是否家族精英
	 * 
	 * @return
	 */
	public boolean isFmElites(int userId) {
		FamilyUserBean fmUser = getFmUserByID(userId);
		return isFmElites(fmUser);
	}

	/**
	 * 发通知给家族游戏管理员
	 * 
	 * @param fmId
	 * @param challId
	 * @param name
	 */
	public static void sendMessageToGameM(int fmId, int challId, String name) {
		List list = service.selectUserIdList(fmId, " and fm_flags&" + FamilyUserBean.FLAG_GAME);
		for (int i = 0; i < list.size(); i++) {
			NoticeAction.sendNotice(((Integer) list.get(i)).intValue(), name + "家族向您发起挑战", NoticeBean.GENERAL_NOTICE,
					"/fm/game/vs/challengedo.jsp?chall=" + challId);
		}
	}

	/**
	 * 发通知给家族精英
	 * 
	 * @param fmIdA
	 * @param fmIdB
	 */
	public static void sendMessage(int fmIdA, int fmIdB, String notice, String url) {
		List list = service.selectUserIdList(fmIdA, " and fm_state&" + FamilyUserBean.VS_GAME);
		for (int i = 0; i < list.size(); i++) {
			NoticeAction.sendNotice(((Integer) list.get(i)).intValue(), notice, NoticeBean.GENERAL_NOTICE, url + fmIdA);
		}
		list = service.selectUserIdList(fmIdB, " and fm_state&" + FamilyUserBean.VS_GAME);
		for (int i = 0; i < list.size(); i++) {
			NoticeAction.sendNotice(((Integer) list.get(i)).intValue(), notice, NoticeBean.GENERAL_NOTICE, url + fmIdB);
		}
	}

	public int getGameType() {
		return 0;
	}

	/**
	 * 每日凌晨0点-8点不能发起挑战申请
	 * 
	 * @return
	 */
	public boolean isTime() {
		Date now = new Date();
		if (now.getHours() < 8) {
			return false;
		}
		return true;

	}

	/**
	 * 
	 * @param fmId
	 * @return
	 */
	public boolean isInFm(int fmId) {
		if (game.fmIdA == fmId) {
			return true;
		}
		if (game.fmIdB == fmId) {
			return true;
		}
		return false;
	}

	public static List getVsGameBean() {
		return new ArrayList(fmVsGameMap.values());
	}

	public String getDouble(double num) {
		return numFormat.format(num);
	}

	public boolean deleteChallenge(VsBean vsBean) {
		// Challenge challenge = vsService.getChallenge("id=" +
		// vsBean.getChallengeId());
		// GameAction.updateFmFund(challenge.getFmA(), challenge.getWager() *
		// e);// 扣钱
		vsService.upd("delete from fm_vs_challenge where id=" + vsBean.getChallengeId());

		vsBean.setChallenge(false);
		vsBean.setChallFmid(0);
		vsBean.setChallGameId(0);
		vsBean.setChallTime(0);
		vsBean.setChallengeId(0);

		vsService.updateFmVsInFo(vsBean);

		// vsBean = getFmVsInfo(challenge.getFmB());
		// vsBean.setAccept(vsBean.getAccept() - 1);
		return true;
	}

	/**
	 * 得到家族积分
	 * 
	 * @param fmId
	 * @return
	 */
	public int[] getFmSocre(int fmId) {
		int[] Socre = new int[VsGameBean.getMaxGameId()];
		List list = vsService.getFmVsScoreList("fm_id=" + fmId);
		for (int i = 0; i < list.size(); i++) {
			FmVsScore fm = (FmVsScore) list.get(i);
			Socre[fm.getGameId()] = fm.getScore();
		}
		return Socre;
	}

	/**
	 * 游戏发生问题退回全部挑战金
	 */
	public static void refunds() {
		List list = vsService.getChallengeList("fm_status=" + Challenge.agree);
		for (int i = 0; i < list.size(); i++) {
			Challenge challenge = (Challenge) list.get(i);
			GameAction.updateFmFund(challenge.getFmA(), challenge.getWager() * e);
			GameAction.updateFmFund(challenge.getFmB(), challenge.getWager() * e);

			FamilyHomeBean fmHome = FamilyAction.getFmByID(challenge.getFmA());
			service.insertFmFundDetail(challenge.getFmA(), challenge.getWager() * e, FundDetail.VS_GAME_TYPE,
					fmHome.getMoney());
			fmHome = FamilyAction.getFmByID(challenge.getFmB());
			service.insertFmFundDetail(challenge.getFmB(), challenge.getWager() * e, FundDetail.VS_GAME_TYPE,
					fmHome.getMoney());

		}
		service.upd("delete from fm_vs_challenge");// 清除对战发起表
	}

	static long outTime = 24 * 60 * 60 * 1000l;

	public static void fmVsGameMapclear() {// 清除超过outTIme的记录
		List list = getVsGameBean();
		for (int i = 0; i < list.size(); i++) {
			VsGameBean vsGame = (VsGameBean) list.get(i);
			if (vsGame != null && vsGame.getEndTime() + outTime < System.currentTimeMillis()) {
				fmVsGameMap.remove(Integer.valueOf(vsGame.getId()));
			}
		}
	}

}
