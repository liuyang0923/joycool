package jc.family.game;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jc.family.FamilyAction;
import jc.family.FamilyHomeBean;
import jc.family.FamilyUserBean;
import jc.family.game.ask.AskAction;
import jc.family.game.boat.BoatAction;
import jc.family.game.snow.SnowBean;
import jc.family.game.snow.SnowGameAction;
import jc.family.game.vs.VsAction;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.StaticCacheMap;

public class GameAction extends FamilyAction {

	public GameAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public static GameService service = new GameService();
	public static ICacheMap matchCache = CacheManage.addCache(new StaticCacheMap(1000), "fmMatch");
	public static Timer fmTimer = new Timer("fmTimer");
	public static byte[] gameLock = new byte[0]; // 游戏开始和结束

	public static void initGame() {
		List listMatch = service.getMatchList("start_time>now()");
		for (int i = 0; i < listMatch.size(); i++) {
			MatchBean matchBean = (MatchBean) listMatch.get(i);
			synchronized (matchCache) {
				matchCache.put(new Integer(matchBean.getId()), matchBean);
			}
			FamilyGameTask task = new FamilyGameTask(matchBean.id);
			fmTimer.schedule(task, matchBean.getStarttime());
		}
		VsAction.refunds();// 退回挑战金
		jc.family.game.vs.term.TermAction.termMatchStart();
	}

	// 后台临时加一场游戏比赛(游戏后台用)
	public void addMatch(int wid) {
		WeekMatchBean wmb = service.getWeekMatchBean(" id=" + wid);
		if (wmb == null)
			return;
		// 赛事表增加一场赛事
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (wmb != null && wmb.getWeekDay() == weekday) {
			if (wmb != null) {
				cal = weekMatchToGameMatch(cal, wmb);
			}
		}
	}

	// 获得正在比赛的赛事列表
	public static List getCurrentMatchList() {
		List currentMatchList = matchCache.keyList();
		if (currentMatchList != null && currentMatchList.size() > 0)
			return currentMatchList;
		List matchList = service.getMatchList(" state=0 order by id desc");
		if (matchList != null && matchList.size() > 0) {
			for (int i = 0; i < matchList.size(); i++) {
				MatchBean matchBean = (MatchBean) matchList.get(i);
				List list = service.getFmApplyList(" m_id=" + matchBean.getId());
				matchBean.setFmList(list);
				matchCache.put(new Integer(matchBean.getId()), matchBean);
			}
			currentMatchList = matchCache.keyList();
		}
		return currentMatchList;
	}

	// 从cache中获得某一赛事
	public MatchBean getMatch(int mid) {
		return (MatchBean) matchCache.get(new Integer(mid));
	}

	/**
	 * 每天0点调用,从 周排表 里面根据 星期几 获取赛事加入 赛事表 TODO 可以会被执行两次
	 */
	public static void inserTodayMatch() {
		synchronized (matchCache) {
			matchCache.clear();
		}

		service.upd("update fm_vs_info set vs_time=0,challenge=0,chall_time=0,chall_fmid=0,chall_gameid=0,challengeid=0");// 清除对战本日记录
		service.upd("update fm_vs_score set vs_time=0");// 清除本游戏场数日记录
		service.upd("delete from fm_vs_challenge");// 清除对战发起表

		synchronized (jc.family.game.vs.VsAction.fmVsInfoMap) {// 清除对战本日记录
			jc.family.game.vs.VsAction.fmVsInfoMap.clear();
		}
		synchronized (jc.family.game.vs.VsAction.fmVsGameMap) {// 清除GameBean
			jc.family.game.vs.VsAction.fmVsGameMapclear();
		}

		dealUv();// 处理uv

		service.upd("truncate table fm_game_fmapply");
		service.upd("truncate table fm_game_apply");
		Calendar cal = Calendar.getInstance();
		int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
		List list = service.getWeekMatchList("effect=0 and weekday=" + weekday);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				WeekMatchBean wmb = (WeekMatchBean) list.get(i);
				if (wmb != null) {
					cal = weekMatchToGameMatch(cal, wmb);
				}
			}
		}
	}

	/**
	 * 
	 * @param cal
	 * @param wmb
	 * @return
	 */
	private static Calendar weekMatchToGameMatch(Calendar cal, WeekMatchBean wmb) {
		MatchBean matchBean = new MatchBean();
		matchBean.type = wmb.type;

		cal.setTime(wmb.getStarttime());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		matchBean.setStarttime(cal.getTime());

		cal.setTime(wmb.getEndtime());
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		matchBean.setEndtime(cal.getTime());

		service.addMatchBean(matchBean);
		synchronized (matchCache) {
			matchCache.put(new Integer(matchBean.getId()), matchBean);
		}
		FamilyGameTask task = new FamilyGameTask(matchBean.id);
		fmTimer.schedule(task, matchBean.getStarttime());
		return cal;
	}

	/**
	 * 开始游戏
	 * 
	 * @param mid
	 */
	public static void startGame(int mid) {
		MatchBean matchBean;
		synchronized (gameLock) {
			matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
			if (matchBean == null || matchBean.getState2() == 1 || matchBean.getState() != 0)
				return;
			matchBean.setState(1);
			service.upd(" update fm_game_match set state=1,create_time=now() where id=" + matchBean.getId());
		}
		try {// 防止游戏数据异常，线程死掉
			switch (matchBean.type) {
			case 1:
				BoatAction.startMatch(mid);
				break;
			case 2:
				SnowGameAction.makePair(mid);
				break;
			case 3:
				AskAction.initaskdata(mid);
				break;
			}
			FamilyGameTask task = new FamilyGameTask(mid, 2);
			fmTimer.schedule(task, matchBean.endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 结束游戏
	 * 
	 * @param mid
	 */
	public static void endGame(int mid) {
		MatchBean matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
		synchronized (gameLock) {
			matchBean = (MatchBean) matchCache.get(Integer.valueOf(mid));
			if (matchBean == null || matchBean.getState2() == 1 || matchBean.getState() != 1)
				return;
			matchBean.setState(2);
			service.upd("update fm_game_match set state=2,end_time=now() where id=" + mid);
		}
		try {// 防止游戏数据异常，线程死掉
			switch (matchBean.type) {
			case 1:
				BoatAction.overMatch(mid);
				break;
			case 2:
				SnowGameAction.getSave(mid);
				break;
			case 3:
				AskAction.endaskdata(mid);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 报名列表显示
	public List getGameList() {
		int mid = getParameterInt("mid");
		FamilyUserBean fmUser = getFmUser();
		if (mid < 1) {
			return null;
		}
		int c = service.selectIntResult("select count(f.id) from (select * from  fm_game_apply where fid ="
				+ fmUser.getFm_id() + "  and m_id =" + mid + " and is_pay=0) f ,(select * from fm_user where fm_id="
				+ fmUser.getFm_id() + " ) g where f.uid= g.id ");
		net.joycool.wap.bean.PagingBean paging = new net.joycool.wap.bean.PagingBean(this, c, 10, "p");
		List list = service.getApplyList("select * from (select * from  fm_game_apply where fid =" + fmUser.getFm_id()
				+ "  and m_id =" + mid + " and is_pay=0) f ,(select * from fm_user where fm_id=" + fmUser.getFm_id()
				+ " ) g where f.uid= g.id order by state asc limit " + paging.getStartIndex() + ","
				+ paging.getCountPerPage());
		String s = paging.shuzifenye("examine.jsp?mid=" + mid, true, "|", response);
		setAttribute("applyList", s);
		return list;
	}

	// 审批用户的报名情况
	public int updateApply() {
		int mid = getParameterInt("mid");
		int cmd = getParameterInt("cmd");
		int uid = getParameterInt("uid");

		if (mid == 0 || cmd == 0 || uid == 0) {
			return 0;// 用户该参数时会出现的情况
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 4;// 不是家族成员
		}
		if (!FamilyUserBean.isflag_game(fmUser.getFm_flags())) {
			return 1;// 无权利
		}
		boolean apply = service.updateApply(cmd, mid, uid, fmUser.getFm_id());
		if (!apply) {
			return 2;
		}
		MatchBean smb = (MatchBean) matchCache.get(new Integer(mid));
		if (smb != null) {
			if (smb.getType() == 2) {// 雪仗 审批的时候将该成员放入缓存
				if (smb.getState() != 2) {// 没有完赛的情况
					SnowBean gameDetails = (SnowBean) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
					if (gameDetails != null) {
						HashMap hm = gameDetails.getMemberBean();
						if (hm != null) {
							MemberBean bean = new MemberBean();
							bean.setFid(fmUser.getFm_id());
							bean.setMid(mid);
							bean.setUid(uid);
							hm.put(new Integer(uid), bean);

//							SnowMoneyBean sbean = new SnowMoneyBean();
//							SnowGameAction saction = new SnowGameAction(request, response);
//							int smoney = saction.getOneSnowMoney();
//							sbean.setMoneyHold(smoney);
//							sbean.setUid(uid);
//
//							synchronized (SnowGameAction.snowMoneyCache) {
//								SnowGameAction.snowMoneyCache.put(new Integer(uid), sbean);
//							}
						}
					}
				}
			}
		}
		return 3;
	}

	// 比赛开始后审批，查看该家族是否参赛,按照缓存里的数据进行判断
	public boolean isFmApply(int mid) {
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return false;// 不是家族成员
		}
		MatchBean smb = (MatchBean) matchCache.get(new Integer(mid));
		if (smb != null) {// 有该赛事，说明已经开赛，可以报名了
			int state = smb.getState();
			if (state == 1) {// 正在比赛中
				Object gameDetails = (Object) smb.getGameMap().get(new Integer(fmUser.getFm_id()));
				if (gameDetails == null) {
					return false;// 家族比赛开始前，无人成功报名
				}
			}
		}
		return true;
	}

	// 设置是否参加比赛
	public int passGame() {
		int mid = getParameterInt("mid");
		int g = getParameterInt("sub");
		if (mid == 0 || g == 0) {
			return 0;
		}
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 1;// 不是家族成员
		}
		if (!FamilyUserBean.isflag_game(fmUser.getFm_flags())) {
			return 2;// 无权利
		}
		int state = service.selectIntResult("select state from fm_game_match where id=" + mid);
		if (state == 1 || state == 2) {
			return 5;
		}
		int cmd = 0;
		int apply = 4;
		if (g == 1) {
			cmd = 1;
		}// 不参加
		if (g == 2) {
			cmd = 0;
			apply = 5;
		}// 参加
		boolean update = service.updateFmApply(mid, fmUser.getFm_id(), cmd);
		if (!update) {
			return 3;// 数据库错误
		}
		return apply;// 设置成功
	}

	// 判断家族是否参加某场游戏
	public int sureApplyGame() {
		int mid = getParameterInt("mid");
		FamilyUserBean fmUser = getFmUser();
		if (fmUser == null) {
			return 0;// 不是家族成员
		}
		if (!FamilyUserBean.isflag_game(fmUser.getFm_flags())) {
			return 0;// 无权利
		}
		int fid = fmUser.getFm_id();
		int isExits = service.selectIntResult("select count(id) from fm_game_fmapply where m_id=" + mid + " and fid="
				+ fid);
		if (isExits != 0) {
			int state = service
					.selectIntResult("select del from fm_game_fmapply where m_id=" + mid + " and fid=" + fid);
			if (state == 1) {
				return 2;// 没参加
			}
		}
		MatchBean smb = (MatchBean) matchCache.get(new Integer(mid));
		if (smb != null) {// 有该赛事，说明已经开赛，可以报名了
			int state = smb.getState();
			if (state == 1) {// 正在比赛中
				return 3;
			}
		}
		return 1;// 参加
	}

	// 判断用户是否被禁赛
	public boolean checkBlack(int mid, int uid, int fmid) {
		if (mid <= 0 || uid <= 0) {
			return false;
		}
		ApplyBean bean = service.getApplyBean(" m_id=" + mid + " and uid=" + uid + " and fid=" + fmid);
		if (bean != null && bean.getState() == 2) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFmApply(int mid, int fid) {
		if (mid <= 0 || fid <= 0) {
			return false;
		}
		FmApplyBean bean = service.getFmApplyBean(" m_id=" + mid + " and fid=" + fid);
		if (bean != null && bean.getDel() == 0)
			return true;
		return false;
	}

	/**
	 * 是否被禁赛
	 * 
	 * @param mid
	 * @param uid
	 * @return
	 */
	public boolean isGameBan(MatchBean matchbean, int uid) {
		FamilyUserBean fmUser = getFmUserByID(uid);
		if (matchbean.getType() == 1) {// 1(龙舟)2(雪仗)3(问答)
			if (FamilyUserBean.isBlocked_boat(fmUser.getFm_state())) {
				return true;
			}
			return false;
		}
		if (matchbean.getType() == 2) {// 1(龙舟)2(雪仗)3(问答)
			if (FamilyUserBean.isBlocked_snow(fmUser.getFm_state())) {
				return true;
			}
			return false;
		}
		if (matchbean.getType() == 3) {// 1(龙舟)2(雪仗)3(问答)
			if (FamilyUserBean.isBlocked_ask(fmUser.getFm_state())) {
				return true;
			}
			return false;
		}
		return false;
	}

	// 报名
	public void memberApply(int mid, int uid, int fid) {
		int param = this.getParameterInt("a");
		if (param != 1 && param != 2)
			return;
		MatchBean matchBean = getMatch(mid);
		if (matchBean == null)
			return;
		if (isGameBan(matchBean, uid)) {
			request.setAttribute("tip", "你已经被管理员禁赛");
			return;
		}
		FamilyHomeBean fmHome = getFmByID(fid);
		if (fmHome == null || fmHome.getFm_member_num() < 5) {
			request.setAttribute("tip", "你的家族人数不足5人");
			return;
		}
		String[] weekGame = { "", "赛龙舟", "雪仗", "问答" };
		FmApplyBean fmApplyBean = GameAction.service.getFmApplyBean("m_id=" + mid + " and fid=" + fid);
		if (fmApplyBean != null) {
			if (param == 1) {
				if (fmApplyBean.getDel() != 1) {
					ApplyBean applyBean = GameAction.service.getApplyBean(" m_id=" + mid + " and uid=" + uid);
					if (applyBean == null) {
						/*
						 * GameAction.service注释掉是因为，只在第一个报名参赛的时候插入家族报名数据，但是人数不增加，
						 * 在审批的时候增加.upd( "update fm_game_fmapply set
						 * total_apply=total_apply+1 where m_id=" + mid + " and
						 * fid=" + fid);
						 */
						GameAction.service.upd("insert into fm_game_apply (m_id,uid,fid)values(" + mid + "," + uid
								+ "," + fid + ")");
						request.setAttribute("tip", "报名成功，请等待审核。");
					} else {
						if (applyBean.getState() == 0 || applyBean.getState() == 2)
							request.setAttribute("tip", "您已经报名!不用重复报名!");
						else
							request.setAttribute("tip", "管理员不允许您报名!");
					}
				} else {
					request.setAttribute("tip", "报名失败!家族管理员已经设定不参加此次游戏!");
				}
			} else if (param == 2) {
				ApplyBean applyBean = GameAction.service.getApplyBean(" m_id=" + mid + " and uid=" + uid);
				if (applyBean != null) {
					if (applyBean.getIsPay() != 1) {
						GameAction.service.upd("delete from fm_game_apply where m_id=" + mid + " and uid=" + uid);
						if (applyBean.getState() == 1 && fmApplyBean.getTotalApply() > 0) {
							if (fmApplyBean.getTotalApply() == 1) {
								GameAction.service.upd("delete from fm_game_fmapply where m_id=" + mid + " and fid="
										+ fid);
							} else {
								GameAction.service
										.upd("update fm_game_fmapply set total_apply=total_apply-1 where m_id=" + mid
												+ " and fid=" + fid);
							}
						}
						request.setAttribute("tip", "撤销成功，您已取消" + weekGame[matchBean.getType()] + "活动的加入申请!");
					} else {
						request.setAttribute("tip", "您已经进入" + weekGame[matchBean.getType()] + "活动,不能撤销报名!");
					}
				} else {
					request.setAttribute("tip", "无须撤销报名!");
				}
			}
		} else {
			if (param == 1) {
				if (matchBean.getState() == 0) {
					GameAction.service.upd("insert into fm_game_fmapply (total_apply,m_id,fid)values(0," + mid + ","
							+ fid + ")");
					GameAction.service.upd("insert into fm_game_apply (m_id,uid,fid)values(" + mid + "," + uid + ","
							+ fid + ")");
					request.setAttribute("tip", "报名成功，请等待审核。");
				} else {
					request.setAttribute("tip", "报名失败,已经超过比赛报名时间!");
				}
			} else if (param == 2) {
				request.setAttribute("tip", "已经没有您的报名信息!");
			}
		}
	}

	// 给一个时间差,返回一个"*时*分*秒"的字符串
	public static String getFormatDifferTime(long time) {
		StringBuilder strTime = new StringBuilder();
		long allsecs = time / 1000;
		long allmins = allsecs / 60;
		long hours = allmins / 60;
		long mins = allmins - hours * 60;
		long secs = allsecs - allmins * 60;
		if (hours != 0) {
			strTime.append(hours);
			strTime.append("时");
		}
		if (mins != 0) {
			strTime.append(mins);
			strTime.append("分");
		}
		if (secs != 0) {
			strTime.append(secs);
			strTime.append("秒");
		}
		return strTime.toString();
	}

	public static String timeFormat(long date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(date));
	}

	/**
	 * 累加家族游戏积分信息
	 * 
	 * @param fmid
	 * @param gametype
	 *            1:龙舟2:雪仗3:问答
	 * @param score
	 * @return
	 */
	public static boolean addScore(int fmid, int gametype, int score) {
		if (service.insertFamilyGameScore(fmid, gametype, score)) {
			return true;
		}
		return false;
	}

	/**
	 * 累加家族游戏经验值
	 * 
	 * @param fmid
	 * @param game_num
	 * @return
	 */
	public static boolean addFmGameInfo(int fmid, int game_num) {
		FamilyHomeBean fmHome = getFmByID(fmid);
		if (fmHome == null) {
			return false;
		}
		if (service.addFmGameInfo(fmid, game_num)) {
			fmHome.setGame_num(fmHome.getGame_num() + game_num);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param fmid
	 * @param fm_exploit
	 * @param game_num
	 * @param event
	 * @param event_type
	 *            增加类型，1:问答 2:龙舟3:雪仗4:排行榜
	 * @param mid
	 * @return
	 */
	public static boolean addFmGameInfo(int fmid, int fm_exploit, int game_num, String event, int event_type, int mid) {
		FamilyHomeBean fmHome = getFmByID(fmid);
		if (fmHome == null) {
			return false;
		}
		if (service.addFmGameInfo(fmid, fm_exploit, game_num, event, event_type, mid)) {
			fmHome.setFm_exploit(fmHome.getFm_exploit() + fm_exploit);
			fmHome.setGame_num(fmHome.getGame_num() + game_num);
			return true;
		}
		return false;
	}

	/**
	 * 增加个人功勋功勋
	 * 
	 * @param userid
	 * @param fm_exploit
	 * @return
	 */
	public static boolean addFmUserGameInfo(int userid, int fmid, int fm_exploit, String event) {
		FamilyUserBean fmUser = getFmUserByID(userid);
		if (fmUser == null) {
			return false;
		}
		if (service.addFmUserGameInfo(userid, fmid, fm_exploit, event)) {
			fmUser.setCon_fm(fmUser.getCon_fm() + fm_exploit);
			return true;
		}
		return false;
	}

	/**
	 * 历史赛事,(后台用)
	 * 
	 * @return
	 */
	public List getHistoryMatch() {
		return service.getMatchList(" state=2 order by id desc limit 100");
	}

	/**
	 * 如果家族报名未参赛扣积分，积分最小为0
	 * 
	 * @param fmid
	 * @param fm_exploit
	 * @param gametype
	 *            1:龙舟 2:雪仗3:问答
	 * @return
	 */
	public static boolean fineFamilyGameScore(int fmid, int fm_exploit, int gametype) {
		FamilyHomeBean fmHome = getFmByID(fmid);
		if (fmHome == null) {
			return false;
		}
		if (service.updateFamilyGameScore(fmid, fm_exploit, gametype)) {
			return true;
		}
		return false;
	}

	/**
	 * 游戏结束后更新家族基金总数
	 * 
	 * @param fid
	 * @param fund
	 * @return
	 */
	public static boolean updateFmFund(int fid, long fund) {
		FamilyHomeBean familyHomeBean = getFmByID(fid);// 更新家族基金缓存
		if (familyHomeBean != null) {
			boolean update = service.updatFmFund(fid, fund);
			if (update) {
				synchronized (familyHomeBean) {
					familyHomeBean.setMoney(familyHomeBean.getMoney() + fund);
				}
				return true;
			}
		}
		return false;
	}
}
