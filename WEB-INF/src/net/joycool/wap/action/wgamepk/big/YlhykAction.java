package net.joycool.wap.action.wgamepk.big;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.bean.wgame.big.HistoryBigBean;
import net.joycool.wap.bean.wgame.big.WGamePKBigBean;
import net.joycool.wap.service.impl.WGamePKServiceImpl;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;


public class YlhykAction extends PKBaseAction {
	
	public static WGamePKServiceImpl pkService = new WGamePKServiceImpl();
	
	public static int BK_NUMBER_PER_PAGE = 10;

	public static int ONLINE_NUMBER_PER_PAGE = 5;
	
	//最小下注金额十亿
	public static int GAMEPOINT = 100000000;

	// 最小下注金额一亿
	public static long MIX_GAMEPOINT = 100000000l;

	// 最大下注金额一千亿
	public static long MAX_GAMEPOINT = 50000000000l;

	public static String[] title = { "乐", "酷" };

	public YlhykAction(HttpServletRequest request) {
		super(request);
	}

	public static byte[] lock = new byte[0];
	/**
	 * 首页。
	 * 
	 * @param request
	 * @param response
	 */
	public void dealIndex(HttpServletRequest request) {
		// 取得参数
//		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
//		if (pageIndex < 0) {
//			pageIndex = 0;
//		}
//		String orderBy = "id";
//		String condition = "mark = " + WGamePKBigBean.PK_MARK_BKING
//				+ " and game_id = " + WGamePKBigBean.YLHYK;
//		int totalBkCount = pkService.getWGamePKBigCount(condition);
//		int totalBkPageCount = totalBkCount / BK_NUMBER_PER_PAGE;
//		if (totalBkCount % BK_NUMBER_PER_PAGE != 0) {
//			totalBkPageCount++;
//		}
//		if (pageIndex > totalBkPageCount - 1) {
//			pageIndex = totalBkPageCount - 1;
//		}
//		if (pageIndex < 0) {
//			pageIndex = 0;
//		}
//		String prefixUrl = "index.jsp";
		// 取得庄家列表
//		condition += " order by " + orderBy + " desc limit " + pageIndex
//				* BK_NUMBER_PER_PAGE + ", " + BK_NUMBER_PER_PAGE;
//		Vector bkList = pkService.getWGamePKBigList(condition);

		// 今日单把胜利金额前三名
		String condition = " right_user_id!=0 and mark="
				+ WGamePKBigBean.PK_MARK_END
				+ " and  id>(select max(id)-1000 from wgame_pk_big) order by wager desc limit 3";
		Vector topList = pkService.getWGamePKBigListByCache(condition);
		request.setAttribute("topList", topList);

//		request.setAttribute("totalBkCount", new Integer(totalBkCount));
//		request.setAttribute("totalBkPageCount", new Integer(totalBkPageCount));
//		request.setAttribute("pageIndex", new Integer(pageIndex));
//		request.setAttribute("prefixUrl", prefixUrl);
//		request.setAttribute("bkList", bkList);
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取用户银行存款
	 * @datetime:2007-7-16 17:19:09
	 * @return
	 * @return long
	 */
	public long getUserStore() {
		long storeMoney = 0;
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		if (storeBean != null) {
			storeMoney = storeBean.getMoney();
		}
		return storeMoney;
	}

	/**
	 * 坐庄开始。
	 * 
	 * @param request
	 */
	public void bkStart(HttpServletRequest request) {
		long storeMoney = getUserStore();
		String tip = null;
		String result = null;
		// 不够10个乐币
		if (storeMoney < MIX_GAMEPOINT) {
			tip = "对不起,您的乐币不够"+MIX_GAMEPOINT+",不能坐庄";
			result = "notEnoughMoney";
		} else {
			// 已经坐庄
			if (!canBk(loginUser.getId(), WGamePKBigBean.YLHYK)) {
				tip = "对不起,您已经坐庄了" + WGameBean.MAX_BK_COUNT + "个要乐还要酷赌局,不能再坐庄";
				result = "hasBk";
			} else {
				result = "continue";
			}
		}
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */
	public void bkDeal1(HttpServletRequest request) {
		//下注金额
		long wager = (long)StringUtil.toInt(request.getParameter("wager"))*GAMEPOINT;
		//用户输入内容
		String content = StringUtil.noEnter(request.getParameter("content"));
		//用户选择答案
		int rs = StringUtil.toInt(request.getParameter("rs"));
		
		String tip = null;
		String result = null;
		
		if (!canBk(loginUser.getId(), WGamePKBigBean.YLHYK)) {
			request.setAttribute("tip", "对不起,您已经坐庄了" + WGamePKBigBean.MAX_BK_COUNT + "个要乐还要酷赌局,不能再坐庄");
			request.setAttribute("result", "failure");
			return;
		}
		
		if (content==null || content.length() == 0) {
			content = "要乐还要酷";
		}
		if (content.length()>15) {	//输入内容判断
			request.setAttribute("tip", "输入内容最多15字");
			request.setAttribute("result", "failure");
			return;
		} else if (rs < 0 || rs > 1) {		//选择结果
			request.setAttribute("tip", "选项不正确,请重试");
			request.setAttribute("result", "failure");
			return;
		}
		
		synchronized(loginUser.getLock()) {
			long storeMoney = getUserStore();
			// 乐币不够
			if (storeMoney < wager) {
				request.setAttribute("tip", "对不起,您乐币不够");
				request.setAttribute("result", "failure");
				return;
			}
			// 乐币太少
			else if (wager < MIX_GAMEPOINT) {
				request.setAttribute("tip", "至少要下注"+MIX_GAMEPOINT+"乐币");
				request.setAttribute("result", "failure");
				return;
			}
			else if (wager > MAX_GAMEPOINT) {
				request.setAttribute("tip", "最多能下注" + MAX_GAMEPOINT + "乐币");
				request.setAttribute("result", "failure");
				return;
			}

			this.decLoginUserGamePoint(wager);
		}
		WGamePKBigBean ylhyk = new WGamePKBigBean();
		ylhyk.setGameId(WGamePKBigBean.YLHYK);
		ylhyk.setLeftUserId(loginUser.getId());
		ylhyk.setLeftNickname(loginUser.getNickName());
		ylhyk.setLeftCardsStr(String.valueOf(rs));
		ylhyk.setContent(StringUtil.noEnter(content));
		ylhyk.setMark(WGamePKBigBean.PK_MARK_BKING);
		ylhyk.setWager(wager);
		if (!addWGamePKBig(ylhyk)) {
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("ylhyk", ylhyk);

	}

	/**
	 * 取消坐庄
	 * 
	 * @param request
	 */
	public void cancelBk(HttpServletRequest request) {
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String condition = "left_user_id = " + loginUser.getId()
				+ " and mark = " + WGamePKBigBean.PK_MARK_BKING + " and id = "
				+ bkId;
		WGamePKBigBean ylhyk = pkService.getWGamePKBig(condition);
		long storeMoney = this.getUserStore();
		if (storeMoney > 0) {
			this.incLoginUserGamePoint(ylhyk.getWager());
			long wager = ylhyk.getWager() / 10;
			if (wager > storeMoney) {
				wager = storeMoney;
			}
			request.setAttribute("wager", new Long(wager));
			this.decLoginUserGamePoint(wager);
			pkService.deleteWGamePKBig("id = " + ylhyk.getId());
		}

		request.setAttribute("ylhyk", ylhyk);
	}

	/**
	 * 
	 * @author macq
	 * @explain：挑战庄家
	 * @datetime:2007-7-19 10:02:37
	 * @param request
	 * @return void
	 */
	public void pkOut(HttpServletRequest request) {
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String tip = null;
		String result = null;
		if (bkId == -1) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		String condition = "id = " + bkId;
		WGamePKBigBean ylhyk = pkService.getWGamePKBig(condition);
		if (ylhyk == null) {
			tip = "该局已经被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (ylhyk.getLeftUserId() == loginUser.getId()) {
			tip = "您不能挑战自己的庄";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (ylhyk.getMark() == WGamePKBigBean.PK_MARK_END) {
			tip = "该局已经结束";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		long storeMoney = this.getUserStore();
		// 乐币不够
		if (storeMoney < ylhyk.getWager()) {
			tip = "您的乐币不够";
			result = "failure";
		}

		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
		}
		// 发牌
		else {
			request.setAttribute("ylhyk", ylhyk);
			result = "success";
			request.setAttribute("result", result);
		}
	}

	/**
	 * 坐庄处理。
	 * 
	 * @param request
	 */
	public void chlStart(HttpServletRequest request) {
		int gameResult = 0;
		int winUserId = 0;
		int bkId = StringUtil.toInt(request.getParameter("bkId"));
		String tip = null;
		String result = null;
		if (bkId == -1) {
			tip = "该局已被取消";
			result = "failure";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		String condition = "id = " + bkId;
		WGamePKBigBean ylhyk;
		synchronized(lock) {		// 以免同时两人挑庄
			ylhyk = pkService.getWGamePKBig(condition);
			if (pkService.validate(loginUser, ylhyk, request) == false)
				return;
			int rResult = StringUtil.toInt(request.getParameter("result"));
			if (rResult < 0 || rResult > 1) {
				rResult = 0;
			}
			if (ylhyk.getLeftCardsToInt() != rResult) { // 庄家赢
				gameResult = BANKER_WIN;
				updateLeftInfo(ylhyk.getLeftUserId(), Constants.RANK_WIN);
				updatePKRightInfo(Constants.RANK_LOSE);
				winUserId = ylhyk.getLeftUserId();
			} else {// 庄家输
				gameResult = BANKER_LOSE;
				winUserId = loginUser.getId();
				updateLeftInfo(ylhyk.getLeftUserId(), Constants.RANK_WIN);
				updatePKRightInfo(Constants.RANK_WIN);
			}
			ylhyk.setRightUserId(loginUser.getId());
			this.dealResult(gameResult, ylhyk, WGamePKBigBean.YLHYK);
			long wager = ylhyk.getWager();
			ylhyk.setRightCardsStr(String.valueOf(rResult));
			ylhyk.setRightUserId(loginUser.getId());
			ylhyk.setRightNickname(loginUser.getNickName());
			ylhyk.setWinUserId(winUserId);
	
			String set = "right_user_id = " + loginUser.getId()
					+ ", right_nickname = '" + StringUtil.toSql(loginUser.getNickName())
					+ "', right_cards = '" + StringUtil.toSql(ylhyk.getRightCardsStr())
					+ "', end_datetime = now(), mark = " + WGamePKBean.PK_MARK_END
					+ ", win_user_id = " + winUserId + ", right_viewed = 1, wager="
					+ wager + ", flag = " + ylhyk.getFlag();
			condition = "id = " + bkId;
			if (!pkService.updateWGamePKBig(set, condition)) {
				return;
			}
			endWGamePKBig(ylhyk);
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("ylhyk", ylhyk);

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getChlStartNoticeTitle(loginUser.getNickName(), "要乐还要酷"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(ylhyk.getLeftUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamepkbig/ylhyk/viewPK.jsp?id=" + ylhyk.getId());
		notice.setMark(NoticeBean.PK_GAME);
		noticeService.addNotice(notice);
	}

	/**
	 * 看坐庄结果。
	 * 
	 * @param request
	 * @return
	 */
	public void viewWGamePK(HttpServletRequest request) {
		int id = StringUtil.toInt(request.getParameter("id"));
		if (id == -1) {
			return;
		}
		String condition = "id = " + id;
		WGamePKBigBean pkBig = pkService.getWGamePKBig(condition);
		request.setAttribute("pk", pkBig);
	}

	/**
	 * 
	 * @author macq
	 * @explain：今日战绩
	 * @datetime:2007-7-17 11:02:06
	 * @param request
	 * @return void
	 */
	public void history(HttpServletRequest request) {
		HistoryBigBean history = getHistory(loginUser.getId(), WGameBean.GT_PK,
				WGamePKBigBean.YLHYK);
		request.setAttribute("history", history);
	}

	/**
	 * @return 返回 title。
	 */
	public String[] getTitle() {
		return title;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：换算单位
	 * @datetime:2007-8-10 11:47:39
	 * @param number
	 * @return
	 * @return String
	 */
	public String bigNumberFormat(long number) {
		return String.valueOf(number / 100000000l) + "亿";
	}

}
