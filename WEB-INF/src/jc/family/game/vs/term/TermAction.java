package jc.family.game.vs.term;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.game.GameAction;
import jc.family.game.vs.Challenge;
import jc.family.game.vs.VsAction;
import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsGameTask;

public class TermAction extends VsAction {

	public static TermService termService = new TermService();

	public static TermBean term;
	
	public static HashMap matchTaskMap = new HashMap();	// match id -> timertask, 用于取消

	public TermAction() {

	}

	public TermAction(HttpServletRequest request) {
		super(request);
	}

	public TermAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public static void nowTermMatchStart(int termid, int matchid) {// 立即开始比赛
		TermBean term = termService.getTermBean("id=" + termid);
		TermMatchBean termMatch = termService.getTermMatchBean("id=" + matchid);
		if (term != null && termMatch != null && term.getState() == 1 && term.getId() == termMatch.getTermId()) {
			termStart(term, termMatch);
		}
	}

	public static void cancelAll() {// 删除所有task
		Iterator iter = matchTaskMap.values().iterator();
		while (iter.hasNext()) {
			TermMatchTask task = (TermMatchTask) iter.next();
			if (task != null) {
				task.cancel();
			}
		}
		matchTaskMap.clear();
	}

	public static void reLoad() {// 重新加载
		synchronized(matchTaskMap) {
			cancelAll();
			termMatchStart();
		}
	}

	public static void termMatchStart() {// 处理比赛开始
		term = termService.getTermBean("state=0 limit 1");
		if (term == null) {
			return;
		}
		List matchList = termService.getTermMatchBeanList("challenge_id=0 and term_id=" + term.getId());
		if (matchList == null) {
			return;
		}
		term.termMatchList = matchList;
		for (int y = 0; y < matchList.size(); y++) {
			TermMatchBean termMatch = (TermMatchBean) matchList.get(y);
			if (termMatch.getStartTime() > System.currentTimeMillis()) {
				TimerTask task = new TermMatchTask(term, termMatch);
				matchTaskMap.put(new Integer(termMatch.getId()), task);
				GameAction.fmTimer.schedule(task, new Date(termMatch.getStartTime()));
			}
		}
	}
	
	/**
	 * 
	 * 自动开始比赛，用于争霸赛
	 * 
	 * @param vsGame
	 * @param challengeId
	 */
	public static boolean termStart(TermBean term, TermMatchBean termMatch) {
		VsGameBean vsgame = VsGameBean.createVsGame(term.getGameType());
		if (vsgame == null) {
			return false;
		}

		Challenge bean = new Challenge();
		bean.setFmA(termMatch.getFmidA());
		bean.setFmB(termMatch.getFmidB());
		bean.setGameId(term.getGameType());
		vsService.insertVsChallenge(bean);
		termMatch.setChallengeId(bean.getId());// 插入challenge获得id
		vsService.upd("update fm_vs_term_match set challenge_id=" + bean.getId() + " where id=" + termMatch.getId());

		vsgame.setId(termMatch.getChallengeId());
		vsgame.setFmIdA(termMatch.getFmidA());
		vsgame.setFmIdB(termMatch.getFmidB());
		vsgame.setGameType(term.getGameType());
		vsgame.setState(VsGameBean.gameInit);
		vsgame.setWager(termMatch.getWager());
		vsgame.setUserA(0);
		vsgame.setUserB(0);
		vsgame.setisScoree(true);// 计算积分
		vsgame.setTime();

		synchronized(matchTaskMap) {
			matchTaskMap.remove(new Integer(termMatch.getId()));
		}
		synchronized (fmVsGameMap) {
			fmVsGameMap.put(Integer.valueOf(termMatch.getChallengeId()), vsgame);
		}

		GameAction.fmTimer.schedule(new VsGameTask(termMatch.getChallengeId()), new Date(vsgame.getStartTime()));

		sendMessage(termMatch.getFmidA(), termMatch.getFmidB(), "[" + VsGameBean.getGameIdName(term.getGameType())
				+ "]争霸赛开始", "/fm/game/vs/match.jsp?id="+termMatch.getTermId()+"&fmid=");

		return true;
	}
	
	// 根据match id获得timertask的设定开始时间
	public static long getTaskTime(int id) {
		TermMatchTask task = (TermMatchTask)matchTaskMap.get(new Integer(id));
		if(task == null)
			return 0;
		return task.scheduledExecutionTime();
	}
}
