package net.joycool.wap.action.tong;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.stock2.StockService;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntUserQuarryBean;
import net.joycool.wap.bean.stock2.StockBean;
import net.joycool.wap.bean.tong.TongApplyBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongCityRecordBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongFriendBean;
import net.joycool.wap.bean.tong.TongFundBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongManageLogBean;
import net.joycool.wap.bean.tong.TongTitleRateLogBean;
import net.joycool.wap.bean.tong.TongTransferBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.spec.tiny.TinyGame;
import net.joycool.wap.spec.tiny.TinyGame1;
import net.joycool.wap.spec.tiny.TinyGame2;
import net.joycool.wap.spec.tiny.TinyGame3;
import net.joycool.wap.util.Arith;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ContentList;
import net.joycool.wap.util.CountMap;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.world.ChatWorld;
import net.joycool.wap.world.ChatWorldBean;

/**
 * @author macq
 * @datetime 2006-12-24 下午04:23:39
 * @explain
 */
public class TongAction extends CustomAction{
	static ICacheMap tongCityRecordCache = CacheManage.tongCityRecord;
	public static TinyGame[] tinyGamesDepot = {new TinyGame1(2, 3), new TinyGame2(4, 4), new TinyGame3(3)};
	
	public static String[] rankNames = {"帮众", "骨干", "亲信", "堂主", "长老", "护法", "法王", "使者", "元老"};
	
	public static int TONG_SHOP_AGGRESSION = 1000;

	public static int TONG_SHOP_RECOVERY = 600;
	
	public static int TONG_SHOP_ASSAULT_TIME = 9000;
	
	public static int TONG_SHOP_RECOVERY_TIME = 6000;

	public static int TONG_SHOP_DEVELOP_GAME_POINT = 1000;

	public static int MIN_RANK_FOR_TONG = 35;

	public static int MIN_MONEY_FOR_TONG = 1000000;

	public static int MIN_SOCIAL_FOR_TONG = 1000;

	public static int PAGE_COUNT = 8;

	public static int TONG_ENDURE = 2000;

	// 帮会结盟费用
	public static int FRIEND_TONG_FEE = 1000000;

	static int TONG_HOCKSHOP_DEVELOP_POINT = 10000;

	final static int NUMBER_PER_PAGE = 10;

	static int SYSTEM_MAX_GAM_POINT = 2000000000;

	static int HOCKSHOP_NUMBER_PER_PAGE = 10;

	static int TONG_CITY_REINFORCE = 10000;

	static int TONG_CITY_DESTORY = 10000;

	static int BARREN = 100000000;

	static int tongExit = 10000;

	static int RANK_POINT = 1;

	// macq_帮会解除同盟扣除100亿帮会基金_start
	static long tongDisband = 1000000000l;

	// macq_帮会解除同盟扣除100亿帮会基金_end

	UserBean loginUser;

	static int GROUP_SEND_MESSAGE_TIME = 1000 * 60 * 60;
	static IDummyService dummyService = ServiceFactory.createDummyService();
	static ITongService tongService = ServiceFactory.createTongService();

	static IJcForumService forumService = ServiceFactory.createJcForumService();

	static IBankService bankService = ServiceFactory.createBankService();

	static IChatService chatService = ServiceFactory.createChatService();

	static IUserService userService = ServiceFactory.createUserService();

	static IJobService jobService = ServiceFactory.createJobService();

	static StockService stockService = new StockService();
	
	static INoticeService noticeService =ServiceFactory.createNoticeService();
	
	public static CountMap countMap = new CountMap();	// 当铺数据
	public static CountMap countMap2 = new CountMap();	// 开仓数据

	public TongAction(HttpServletRequest request) {
		super(request);
		if (getLoginUser() != null){
			loginUser = getLoginUser();
			loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
		}
	}

	public static ITongService getTongService() {
		return tongService;
	}

	public static StockService getStockService() {
		return stockService;
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static IJobService getJobService() {
		return jobService;
	}

	public static IChatService getChatService() {
		return chatService;
	}

	public static IBankService getBankService() {
		return bankService;
	}

	public static IJcForumService getForumService() {
		return forumService;
	}
	public static INoticeService getNoticeService() {
		return noticeService;
	}
	
	// static {
	// oneMiniteThread scanner = new oneMiniteThread();
	// scanner.start();
	// }

	/**
	 * 
	 * @author macq
	 * @explain : 按名称查询帮会
	 * @datetime:2006-12-29 下午01:38:12
	 * @param request
	 */
	public void tongSearch(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String tongTitle = request.getParameter("tongTitle");
		if (tongTitle == null || "".equals(tongTitle.replace(" ", ""))) {
			result = "failure";
			tip = "请输入帮会名称!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = getTongService().getTong("title like'%" + StringUtil.toSql(tongTitle) + "%'");
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 城帮排名列表首页
	 * @datetime:2006-12-24 下午04:25:02
	 */
	public void tongList(HttpServletRequest request) {
		String orderBy = request.getParameter("orderBy");
		if (orderBy == null) {
			orderBy = "honor";
		}
		List tongList = TongCacheUtil.getTongListById(orderBy);
		int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongList.jsp?orderBy=" + orderBy;
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongList", tongList1);
		request.setAttribute("orderBy", orderBy);
		// macq_2007_3_30_按照帮会成立倒叙显示_start
		List newTongList = TongCacheUtil.getTongListById("id");
		int totalCount1 = newTongList.size();
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 == -1) {
			pageIndex1 = 0;
		}
		int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
		if (totalCount1 % NUMBER_PER_PAGE != 0) {
			totalPageCount1++;
		}
		if (pageIndex1 > totalPageCount1 - 1) {
			pageIndex1 = totalPageCount1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		String prefixUrl1 = "tongList.jsp";
		// 取得要显示的消息列表x
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex1 = Math.min(start1, totalCount1);
		int endIndex1 = Math.min(start1 + end1, totalCount1);
		List newTongList1 = newTongList.subList(startIndex1, endIndex1);
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("newTongList", newTongList1);
		// macq_2007_3_30_按照帮会成立倒叙显示_start
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 城帮中心首页
	 * @datetime:2006-12-24 下午04:25:02
	 */
	public void tongCenter(HttpServletRequest request) {
		String orderBy = request.getParameter("orderBy");
		if (orderBy == null) {
			orderBy = "honor";
		}
		List tongList = TongCacheUtil.getTongListById(orderBy);
		int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongCenter.jsp?orderBy=" + orderBy;
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongList", tongList1);
		request.setAttribute("orderBy", orderBy);
		// macq_2007-3-30_显示开发度前十名的帮会帮主_start
		List userList = TongCacheUtil.getTongListById("userId");
		int totalCount1 = userList.size();
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 == -1) {
			pageIndex1 = 0;
		}
		int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
		if (totalCount1 % NUMBER_PER_PAGE != 0) {
			totalPageCount1++;
		}
		if (pageIndex1 > totalPageCount1 - 1) {
			pageIndex1 = totalPageCount1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		String prefixUrl1 = "tongCenter.jsp";
		// 取得要显示的消息列表x
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex1 = Math.min(start1, totalCount1);
		int endIndex1 = Math.min(start1 + end1, totalCount1);
		List userList1 = userList.subList(startIndex1, endIndex1);
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("userList", userList1);
		// macq_2007-3-30_显示开发度前十名的帮会帮主_end
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 具体帮会首页(判断帮主,副帮主,会员,非会员)
	 * @datetime:2006-12-24 下午04:25:02
	 */
	public void tong(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取帮会ID
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if(tongId <= 0)
			tongId = loginUser.getUs().getTong();
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮户记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getStockId() > 0) {
			StockBean stock = getStockService().getStock(
					"id=" + tong.getStockId());
			if (stock != null) {
				request.setAttribute("stock", stock);
			}
		}
		List onlineTongUserId = this.getOnlineTongUserIdList(tongId);// 获取在线帮会会员
		// 分页
		int totalCount = onlineTongUserId.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tong.jsp?tongId=" + tong.getId();
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List onlineTongUserId1 = onlineTongUserId.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("onlineTongUserId", onlineTongUserId1);
		request.setAttribute("tong", tong);
		request.setAttribute("tongOnlineUserCount", totalCount + "");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会聊天室
	 * @datetime:2007-7-11 11:39:25
	 * @param request
	 * @return void
	 */
	public void tongChat(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取帮会ID
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮户记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断帮会会员信息
		if (tongUser == null) {
			result = "failure";
			tip = "您不是该帮成员，无法进入该聊天室!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		List chatList = ChatWorld.get(String.valueOf(tong.getId()),
				ChatWorldBean.TONG_CHAT);// 获取帮会聊天室记录列表
		// 分页
		PagingBean paging = new PagingBean(this, chatList.size(), NUMBER_PER_PAGE, "p");
		
		List chatList1 = chatList.subList(paging.getStartIndex(), paging.getEndIndex());
		request.setAttribute("paging", paging);
		request.setAttribute("chatList", chatList1);
		request.setAttribute("tong", tong);
		int tongOnlineUserCount = this.getOnlineTongUserIdList(tongId).size();// 获取在线帮会会员
		request.setAttribute("tongOnlineUserCount", tongOnlineUserCount + "");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会聊天室发言页面
	 * @datetime:2007-7-11 11:39:38
	 * @param request
	 * @return void
	 */
	public void tongChatPost(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取帮会ID
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮户记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断帮会会员信息
		if (tongUser == null) {
			result = "failure";
			tip = "您不是该帮成员，无法在聊天室发言!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会聊天室结果页面
	 * @datetime:2007-7-11 11:39:55
	 * @param request
	 * @return void
	 */
	public void tongChatPostResult(HttpServletRequest request) {
		String result = null;
		String tip = null;

		// 获取帮会ID
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮户记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断帮会会员信息
		if (tongUser == null) {
			result = "failure";
			tip = "您不是该帮成员，无法在该聊天室发言!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String content = getParameterNoCtrl("content");
		if (content == null || content.length() == 0) {
			result = "contentError";
			tip = "发言内容不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if(!isCooldown("tongchat", 5000)) {
			request.setAttribute("result", "contentError");
			request.setAttribute("tip", "你的发言太快了！请先休息一会再继续。");
			request.setAttribute("tong", tong);
			return;
		}
		session.setAttribute("tongchatPostTime", System.currentTimeMillis()
				+ "");
		ChatWorldBean chatWorld = new ChatWorldBean();
		chatWorld.setId(System.currentTimeMillis());
		chatWorld.setUserId(loginUser.getId());
		chatWorld.setContent(content);
		chatWorld.setCreateDatetime(DateUtil.getCurrentTimeAsStr());
		// 增加聊天室信息
		ChatWorld.put(String.valueOf(tong.getId()), chatWorld,
				ChatWorldBean.TONG_CHAT);
		request.setAttribute("tong", tong);
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会公告
	 * @datetime:2006-12-25 下午04:22:32
	 * @param request
	 */
	public void tongNotice(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会所有成员
	 * @datetime:2006-12-25 下午05:08:45
	 * @param request
	 */
	public void tongUserList(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断参数
		if ((orderBy == null) || (orderBy.equals(""))) {
			orderBy = "donation";
		}
		if (orderBy.equals("donation")) {
			List tongUserList = TongCacheUtil.getTongUserListById(tongId);
			int totalCount = tongUserList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}

			String prefixUrl = "tongUserList.jsp?tongId=" + tong.getId()
					+ "&amp;orderBy=" + orderBy;

			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongUserList1 = tongUserList.subList(startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongUserList", tongUserList1);
		} else {
			List onlinUserList = this.getOnlineTongUserList(tong.getId());
			int totalCount = onlinUserList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongUserList.jsp?tongId=" + tong.getId()
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List onlinUserList1 = onlinUserList.subList(startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongUserList", onlinUserList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会城墙页面
	 * @datetime:2006-12-25 下午05:27:54
	 * @param request
	 */
	public void tongCity(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：城墙加固记录
	 * @datetime:2007-7-5 16:26:12
	 * @param request
	 * @return void
	 */
	public void tongCityRecoveryRecord(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断排序参数
		if (orderBy == null) {
			orderBy = "count";
		}
		if (orderBy.equals("count") || orderBy.equals("create_datetime")) {// 判断按次数跟时间排序
			int totalCount = getTongService().getTongCityRecordCount(
					" tong_id=" + tong.getId() + " and mark=1 order by "
							+ orderBy + " desc");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityRecoveryRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCityRecoveryList = getTongService()
					.getTongCityRecordList(
							"tong_id=" + tong.getId() + " and mark=1 order by "
									+ orderBy + " desc limit " + start + ","
									+ end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecoveryList", tongCityRecoveryList);
		} else {// 判断按帮会被破坏次数排序
			orderBy = "tongCityTongId";
			Vector tongCityRecoveryByIdList = this.getTongCiytRecordList(1);
			int totalCount = tongCityRecoveryByIdList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityRecoveryRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityRecoveryByIdList1 = tongCityRecoveryByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecoveryByIdList",
					tongCityRecoveryByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：当铺开发度记录
	 * @datetime:2007-7-6 11:44:06
	 * @param request
	 * @return void
	 */
	public void tongCityHockShopRecord(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断排序参数
		if (orderBy == null) {
			orderBy = "count";
		}
		if (orderBy.equals("count") || orderBy.equals("create_datetime")) {// 判断按次数跟时间排序
			int totalCount = getTongService().getTongCityRecordCount(
					" tong_id=" + tong.getId() + " and mark=2 order by "
							+ orderBy + " desc");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityHockShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCityHockShopList = getTongService()
					.getTongCityRecordList(
							"tong_id=" + tong.getId() + " and mark=2 order by "
									+ orderBy + " desc limit " + start + ","
									+ end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityHockShopList", tongCityHockShopList);
		} else {// 判断按帮会排序
			orderBy = "tongCityTongId";
			Vector tongCityHockShopByIdList = this.getTongCiytRecordList(2);
			int totalCount = tongCityHockShopByIdList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityHockShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityHockShopByIdList1 = tongCityHockShopByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityHockShopByIdList",
					tongCityHockShopByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：商店开发度记录
	 * @datetime:2007-7-6 11:44:06
	 * @param request
	 * @return void
	 */
	public void tongCityShopRecord(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断排序参数
		if (orderBy == null) {
			orderBy = "count";
		}
		if (orderBy.equals("count") || orderBy.equals("create_datetime")) {// 判断按次数跟时间排序
			int totalCount = getTongService().getTongCityRecordCount(
					" tong_id=" + tong.getId() + " and mark=3 order by "
							+ orderBy + " desc ");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCityShopList = getTongService().getTongCityRecordList(
					"tong_id=" + tong.getId() + " and mark=3 order by "
							+ orderBy + " desc limit " + start + "," + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityShopList", tongCityShopList);
		} else {// 判断按帮会排序
			orderBy = "tongCityTongId";
			Vector tongCityShopByIdList = this.getTongCiytRecordList(3);
			int totalCount = tongCityShopByIdList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityShopByIdList1 = tongCityShopByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityShopByIdList", tongCityShopByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 城墙破坏记录
	 * @datetime:2006-12-25 下午05:27:54
	 * @param request
	 */
	public void tongCityRecord(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断排序参数
		if (orderBy == null) {
			orderBy = "count";
		}
		if (orderBy.equals("count") || orderBy.equals("create_datetime")) {// 判断按次数跟时间排序
			int totalCount = getTongService().getTongCityRecordCount(
					" tong_id=" + tong.getId() + " and mark=0 order by "
							+ orderBy + " desc");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCiytRecordList = getTongService().getTongCityRecordList(
					"tong_id=" + tong.getId() + " and mark=0 order by "
							+ orderBy + " desc limit " + start + "," + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCiytRecordList", tongCiytRecordList);
		} else {// 判断按帮会被破坏次数排序
			orderBy = "tongCityTongId";
			Vector tongCityRecordByIdList = this.getTongCiytRecordList(0);
			int totalCount = tongCityRecordByIdList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityRecordByIdList1 = tongCityRecordByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecordByIdList",
					tongCityRecordByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取每个帮会的破坏次数并排名
	 * @datetime:2006-12-25 下午12:16:12
	 * @param tongId
	 * @return
	 */
	public Vector getTongCiytRecordList(int mark) {
		String key = "tongCityRecord_" + mark;
		synchronized(tongCityRecordCache) {
			Vector tongCiytRecordList = (Vector) tongCityRecordCache.get(key);
			if (tongCiytRecordList == null) {
				tongCiytRecordList = new Vector();
				TongCityRecordBean tongCityRecord = null;

				DbOperation dbOp = new DbOperation(true);

				String query = "select sum(count) tong_count,tong_id from jc_tong_city_record  where mark="
						+ mark + " group by tong_id order by tong_count desc";

				ResultSet rs = dbOp.executeQuery(query);
				try {
					// 结果不为空
					while (rs.next()) {
						tongCityRecord = new TongCityRecordBean();
						tongCityRecord.setCount(rs.getInt("tong_count"));
						tongCityRecord.setTongId(rs.getInt("tong_id"));
						tongCiytRecordList.add(tongCityRecord);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dbOp.release();
				tongCityRecordCache.put(key, tongCiytRecordList);
			}
			return tongCiytRecordList;
		}
	}

	// 破坏帮会城墙
	public static void destroyTong(int tongId) {
		synchronized (LockUtil.tongLock.getLock(tongId)) {
			TongCacheUtil.updateTong(
					"now_endure=0,fund=fund/2,high_endure=high_endure/2,shop=shop/2,destroy_datetime=now(),honor_drop=honor_drop+honor,honor=0",// 更新城墙沦陷时间
					"id=" + tongId + " and now_endure>0", tongId);	// 避免一次灭多次，扣太多……
			OsCacheUtil.flushGroup(OsCacheUtil.TONG_DESTROY_LIST_GROUP, tongId + "");
			TongDestroyHistoryBean bean = new TongDestroyHistoryBean();
			bean.setTongId(tongId);
			tongService.addTongDestroyHistory(bean);
			TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tongId);
			tongHockshop.setDevelop(tongHockshop.getDevelop() / 2);
			tongService.updateTongHockshop("develop="+tongHockshop.getDevelop(), "tong_id=" + tongId);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：城池沦陷后3天内不得攻打
	 * @datetime:2007-7-31 9:47:41
	 * @param tongId
	 * @return
	 * @return boolean
	 */
	public static boolean tongIsDestroyed(TongBean bean) {
		if (bean == null) {
			return true;
		}
		long createTime = bean.getDestroyDatetime();
		long currentTime = System.currentTimeMillis();
		if (currentTime - createTime < 1000 * 60 * 60 * 24 * 3) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain：检查用户是否拥有特殊道具
	 * @datetime:2007-6-28 5:43:32
	 * @param weaponId
	 * @return
	 * @return String
	 */
	public String checkUserBag(int userBagId, int productId) {
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		if (userBag == null) {
			return "您没有此道具！";
		} else if (userBag.getUserId() != loginUser.getId()) {
			return "您没有此道具！";
		} else if (userBag.getProductId() != productId) {
			return "您没有特殊股道具！";
		}
		return "";
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取用户破换城墙记录
	 * @datetime:2007-1-9 下午06:39:22
	 * @param request
	 */
	public TongCityRecordBean getTongCiytRecordList(int tongId, int userId,
			int mark) {
		String key = tongId + "_" + userId + "_" + mark;
		synchronized(tongCityRecordCache) {
			TongCityRecordBean tongCityRecord = (TongCityRecordBean) tongCityRecordCache
					.get(key);
			if (tongCityRecord == null) {
				tongCityRecord = getTongService().getTongCityRecord(
						"tong_id=" + tongId + " and user_id=" + userId
								+ " and mark=" + mark);
				if (tongCityRecord != null) {
					tongCityRecordCache.put(key, tongCityRecord);
				}
			}
			return tongCityRecord;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取最新5条攻打城池的记录
	 * @datetime:2007-3-30 15:01:04
	 * @return
	 * @return Vector
	 */
	public Vector getTongCiytRecordListNew() {
		// String key = "1=1 order by create_datetime limit 0,5";
		// Vector tongCityRecordList = (Vector) OsCacheUtil
		// .get(key, OsCacheUtil.TONG_CITY_RECORD_CACHE_GROUP,
		// OsCacheUtil.TONG_CITY_RECORD_FLUSH_PERIOD);
		// if (tongCityRecordList == null) {
		// tongCityRecordList = getTongService().getTongCityRecordList(key);
		// if (tongCityRecordList == null) {
		// tongCityRecordList=new Vector();
		// }
		// OsCacheUtil.put(key, tongCityRecordList,
		// OsCacheUtil.TONG_CITY_RECORD_CACHE_GROUP);
		// }
		// 获取最新5条攻打城池的记录
		String sql = " mark=0 order by create_datetime desc limit 0,5";
		Vector tongCityRecordList = getTongService().getTongCityRecordList(sql);
		return tongCityRecordList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：获取最新的5条关于帮会的论坛帖子
	 * @datetime:2007-3-30 20:15:30
	 * @return
	 * @return Vector
	 */
	public Vector getForumListNew() {
		String key = "select a.tong_id,b.* from jc_forum a join jc_forum_content b "
				+ "on b.forum_id = a.id where a.mark=1 order by b.id desc limit 0,5";
		Vector forumContentList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.TONG_FORUM_NEW_CACHE_GROUP,
				OsCacheUtil.TONG_FORUM_NEW_FLUSH_PERIOD);
		if (forumContentList == null) {
			forumContentList = getForumService().getForumContentListNew(key);
			if (forumContentList == null) {
				forumContentList = new Vector();
			}
			OsCacheUtil.put(key, forumContentList,
					OsCacheUtil.TONG_FORUM_NEW_CACHE_GROUP);
		}
		return forumContentList;
	}

	/**
	 * 
	 * @author macq
	 * @explain :
	 * @datetime:2007-1-9 下午06:39:22
	 * @param request
	 */
	public void tongExit(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getUserId() == loginUser.getId()) {// 判断是否为帮主
			result = "tongError";
			tip = "帮主不能退出帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
		// .getId());
		// if (tongUser == null) {// 判断是否为帮会成员
		// result = "tongError";
		// tip = "您不是该帮派成员!";
		// request.setAttribute("result", result);
		// request.setAttribute("tip", tip);
		// request.setAttribute("tong", tong);
		// return;
		// }
		//帮会成员的判断
		TongUserBean tongUser = TongCacheUtil.getTongUser(tong.getId(),loginUser.getId());
		if (tongUser == null) {// 判断是否为成员
			result = "tongError";
			tip = "已经退出帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		
		if (tong.getUserIdA() == loginUser.getId()
				|| tong.getUserIdB() == loginUser.getId()) {// 判断是否为副帮主
			if (tong.getUserIdA() == loginUser.getId()) {
				TongCacheUtil.updateTong(
						"user_id_a=-1,user_count=user_count-1", "id="
								+ tong.getId(), tong.getId());
				ForumBean forumBean = ForumCacheUtil.getForumCacheBean(tong
						.getId());
				String myid = null;
				if (forumBean != null) {

					myid = forumBean.getUserId().replace(
							"_" + loginUser.getId(), "");
					getForumService().updateForum("user_id='" + myid + "'",
							"tong_id=" + tong.getId());
					ForumCacheUtil.deleteTongForum(tong.getId());
					ForumCacheUtil.deleteTongForumList();

				}
			} else {
				TongCacheUtil.updateTong(
						"user_id_b=-1,user_count=user_count-1", "id="
								+ tong.getId(), tong.getId());
				ForumBean forumBean = ForumCacheUtil.getForumCacheBean(tong
						.getId());
				String myid = null;
				if (forumBean != null) {

					myid = forumBean.getUserId().replace(
							"_" + loginUser.getId(), "");
					getForumService().updateForum("user_id='" + myid + "'",
							"tong_id=" + tong.getId());
					ForumCacheUtil.deleteTongForum(tong.getId());
					ForumCacheUtil.deleteTongForumList();

				}
			}
		} 
		//判断是否为高层
		else if(tongUser.getMark()==3)
		{
			TongCacheUtil.updateTong("user_count=user_count-1 ,cadre_count=cadre_count-1", "id="
					+ tong.getId(), tong.getId());
		}
		else {
			TongCacheUtil.updateTong("user_count=user_count-1", "id="
					+ tong.getId(), tong.getId());
		}
		TongCacheUtil.deleteTongUser("user_id=" + loginUser.getId(), loginUser
				.getId(), tong.getId());// 删除帮会记录
		getTongService().updateTongApply(
				"mark=4,create_datetime=now()",
				"mark=1 and tong_id=" + tong.getId() + " and user_id="
						+ loginUser.getId());// 帮会申请表更改状态标志
		getUserService().updateOnlineUser("tong_id=0",
				"user_id=" + loginUser.getId());// 更新用户在线表帮会标志
		// macq_2007-2-8_帮会成员退出帮会扣除1万乐币(不足一万扣为0)_start

		UserInfoUtil.updateUserCash(loginUser.getId(), -tongExit,
				UserCashAction.OTHERS, "退出帮会,扣除" + tongExit + "乐币");
		UserInfoUtil.updateUserTong(loginUser.getId(), 0);

		// macq_2007-2-8_帮会成员退出帮会扣除1万乐币(不足一万扣为0)_end
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 申请加入帮会
	 * @datetime:2006-12-27 下午08:20:30
	 * @param request
	 */
	public void tongApply(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() > 0 || us.getTong() == -1) {// 判断用户是否已经是某个帮派的成员判断用户是否已经是某个帮派的成员
			result = "exist";
			tip = "对不起，您是某帮成员或您已经申请了某帮,不能再申请了!（3秒钟跳转到城市首页）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		session.setAttribute("tongApplyCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 申请入帮处理结果页面
	 * @datetime:2006-12-27 下午08:46:34
	 * @param request
	 */
	public void tongApplyResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (session.getAttribute("tongApplyCheck") == null) {// 防止刷新
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("tongApplyCheck");
		String content = getParameterNoEnter("content");
		if (content == null || content.length() == 0) {
			result = "contentError";
			tip = "请确认输入内容!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// liuyi 2007-01-16 帮会修改 start
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus.getTong() > 0) {
			TongBean userTong = TongCacheUtil.getTong(userStatus.getTong());
			String tongTitle = (userTong == null) ? "某" : userTong.getTitle();
			// TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
			// .getId());
			// if (tongUser != null) {// 判断用户是否已经是某个帮派的成员
			result = "exist";
			tip = "对不起，您是" + tongTitle + "帮成员,不能再申请了!（3秒钟跳转到城市首页）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
			// liuyi 2007-01-16 帮会修改 end
		}
		TongApplyBean tongApply = getTongService().getTongApply(
				"user_id=" + loginUser.getId() + " and mark=0");
		if (tongApply != null) {// 判断用户是否已经是某个帮派的成员
			result = "exist";
			tip = "对不起，您已经申请了某帮，不能再申请了！（3秒钟跳转到城市首页）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}

		tongApply = new TongApplyBean();
		tongApply.setUserId(loginUser.getId());
		tongApply.setContent(content);
		tongApply.setTongId(tong.getId());
		tongApply.setMark(0);
		getTongService().addTongApply(tongApply);// 添加申请信息
		// 更新申请人的帮会字段
		UserInfoUtil.updateUserTong(tongApply.getUserId(), -1);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：申请撤销入帮
	 * @datetime:2007-7-16 13:16:49
	 * @param request
	 * @return void
	 */
	public void tongApplyCancel(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus.getTong() > 0) {
			TongBean userTong = TongCacheUtil.getTong(userStatus.getTong());
			String tongTitle = (userTong == null) ? "某" : userTong.getTitle();
			result = "error";
			tip = "对不起，您是" + tongTitle + "帮成员,不能撤销申请了!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
			// liuyi 2007-01-16 帮会修改 end
		}
		TongApplyBean tongApply = getTongApply(loginUser.getId(), tong.getId());
		if (tongApply == null) {// 判断用户是否已经是某个帮派的成员
			result = "error";
			tip = "对不起，您没有申请进入该帮会！";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		getTongService().delTongApply("id=" + tongApply.getId());// 删除申请入帮记录
		// 更新申请人的帮会字段
		UserInfoUtil.updateUserTong(tongApply.getUserId(), 0);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会基金首页
	 * @datetime:2006-12-27 下午10:30:46
	 * @param request
	 */
	public void tongFund(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus == null) {
			result = "failure";
			tip = "用户信息有误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());
		if (tongUser != null) {// 判断用户是否已经是某个帮派的成员
			tip = "你对帮会贡献度为" + tongUser.getDonation() + "<br/>现有乐币"
					+ userStatus.getGamePoint();
		} else {
			tip = "现有乐币" + userStatus.getGamePoint();
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		request.setAttribute("tong", tong);
		session.setAttribute("tongFundCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会基金捐赠结果
	 * @datetime:2006-12-27 下午10:42:00
	 * @param request
	 */
	public void tongFundResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (session.getAttribute("tongFundCheck") == null) {// 防止刷新
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("tongFundCheck");
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count < 1 || count > 10000) {
			result = "countError";
			tip = "捐赠数量必须大于1份,最多一次性捐赠1万份!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		long fundMoney;
		UserStatusBean userStatus;
		synchronized(loginUser.getLock()) {
			userStatus = UserInfoUtil.getUserStatus(loginUser
					.getId());
			fundMoney = count * Constants.TONG_FUND_MONEY;
			if (userStatus.getGamePoint() < fundMoney) {
				result = "failure";
				tip = "您没有足够的乐币捐赠!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			UserInfoUtil.updateUserCash(loginUser.getId(), -fundMoney,
					14, "用户捐赠帮会基金");// 更新用户乐币
		}
		TongCacheUtil.updateTong("fund=fund+" + fundMoney
				+ ",fund_total=fund_total+" + fundMoney, "id=" + tong.getId(),
				tong.getId());// 更新帮会总基金
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 获取是否为帮会会员
		if (tongUser != null) {// 判断用户是否已经是某个帮派的成员
			if (fundMoney >= 1000000) {// macq_2007-1-16_每笔捐赠大于100万时,相对每100万增加1点贡献度
				count = count / 5;
				TongCacheUtil.updateTongUserDonation("donation=donation+"
						+ count, "user_id=" + loginUser.getId(), tong.getId(),
						loginUser.getId(), count);// 更新用户贡献度
			} else {
				count = 0;
			}
			TongFundBean tongFund = TongCacheUtil.getTongFund(tong.getId(),
					loginUser.getId(), 1);// 判断用户是否捐献过基金
			if (tongFund != null) {
				TongCacheUtil.updateTongFund("money=money+" + fundMoney,
						"tong_id=" + tong.getId() + " and user_id="
								+ loginUser.getId() + " and mark=1", tong
								.getId(), loginUser.getId(), 1, "money");
			} else {
				tongFund = new TongFundBean();
				tongFund.setTongId(tong.getId());
				tongFund.setCurrentTongId(tongUser.getTongId());
				tongFund.setUserId(loginUser.getId());
				tongFund.setMark(1);
				tongFund.setMoney(fundMoney);
				TongCacheUtil.addTongFund(tongFund, "money");// 增加帮会基金捐赠记录
			}
			if (count > 0) {
				tip = "你捐献了" + fundMoney + "个乐币！帮会贡献增加" + count + "！你还有"
						+ (userStatus.getGamePoint() - fundMoney) + "个乐币！";
			} else {
				tip = "你捐献了" + fundMoney + "个乐币！！你还有"
						+ (userStatus.getGamePoint() - fundMoney) + "个乐币！";
			}
		} else {
			tip = "你捐献了" + fundMoney + "个乐币！！你还有"
					+ (userStatus.getGamePoint() - fundMoney) + "个乐币！";
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会基金规则
	 * @datetime:2006-12-27 下午02:33:22
	 * @param request
	 */
	public void tongFundHelp(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会基金使用明细显示
	 * @datetime:2006-12-27 下午02:45:23
	 * @param request
	 */
	public void tongFundUse(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		List tongFundUseList = TongCacheUtil.getTongFundListById(tong.getId(),
				0, "id");
		int totalCount = tongFundUseList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongFundUse.jsp?tongId=" + tong.getId();
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongFundUseList1 = tongFundUseList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongFundUseList", tongFundUseList1);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取用户捐赠排名
	 * @datetime:2006-12-27 下午03:07:37
	 * @param tongId
	 * @param userTongFundMoney
	 * @return
	 */
	public void TongFundTop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断获取是否为帮会会员
		if (tongUser != null) {
			long userFundMoney = this.getTongFundMoney(tong.getId(), loginUser
					.getId(), 1);// 获取用户捐赠基金数量
			String key = "" + (1 + userFundMoney / 10000);
			Integer fundCount = (Integer) OsCacheUtil.get(key,
					OsCacheUtil.TONG_USER_FUND_TOP_GROUP,
					OsCacheUtil.TONG_USER_FUND_TOP_FLUSH_PERIOD);
			if (fundCount == null) {
				DbOperation dbOp = new DbOperation();
				dbOp.init();
				// 执行查询
				int count = 0;
				String sql = "select count(id) count from jc_tong_fund where tong_id="
						+ tong.getId()
						+ " and money>"
						+ userFundMoney
						+ " and mark=1";
				ResultSet rs = dbOp.executeQuery(sql);
				try {
					if (rs.next()) {
						count = rs.getInt("count");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dbOp.release();
				fundCount = new Integer(count);
				OsCacheUtil.put(key, fundCount,
						OsCacheUtil.TONG_USER_FUND_TOP_GROUP);
			}
			tip = "您目前贡献度" + tongUser.getDonation() + "点,排名:"
					+ (fundCount.intValue() + 1);
			request.setAttribute("tip", tip);
		}
		List FundTopList = TongCacheUtil.getTongFundListById(tong.getId(), 1,
				"money");
		int totalCount = FundTopList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongFundTop.jsp?tongId=" + tong.getId();
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List FundTopList1 = FundTopList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("FundTopList", FundTopList1);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 当铺首页
	 * @datetime:2006-12-28 上午10:54:12
	 * @param request
	 */
	public void hockshop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongHockshopBean hockShop = TongCacheUtil.getTongHockshop(tongId);// 获取帮会当铺
		if (hockShop == null) {
			result = "failure";
			tip = "该帮会没有当铺!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int totalCount = getJobService().getHuntUserQuarryCount(
				"user_id=" + loginUser.getId());// 获取用户所有打猎物品
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / HOCKSHOP_NUMBER_PER_PAGE;
		if (totalCount % HOCKSHOP_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "hockshop.jsp?tongId=" + tong.getId();
		int start = pageIndex * HOCKSHOP_NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = HOCKSHOP_NUMBER_PER_PAGE;

		String condition = " user_id=" + loginUser.getId() + " LIMIT " + start
				+ "," + end;
		Vector quarryList = getJobService().getHuntUserQuarryList(condition);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("quarryList", quarryList);
		request.setAttribute("hockShop", hockShop);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会道具商店
	 * @datetime:2007-7-3 1:57:09
	 * @param request
	 * @return void
	 */
	public void shop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongShopCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会道具信息
	 * @datetime:2007-7-3 3:45:01
	 * @param request
	 * @return void
	 */
	public void shopInfo(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			result = "error";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() != loginUser.getId()
				&& tong.getUserIdA() != loginUser.getId()
				&& tong.getUserIdB() != loginUser.getId()) {
			result = "error";
			tip = "只有帮主才可以进行此操作！";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		DummyProductBean dummyProduct = dummyService.getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "error";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		request.setAttribute("tong", tong);
		request.setAttribute("dummyProduct", dummyProduct);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：兑换商店道具
	 * @datetime:2007-7-3 2:32:30
	 * @param request
	 * @return void
	 */
	public void shopProduct(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong;
		synchronized(LockUtil.tongLock.getLock(tongId)) {
			tong = TongCacheUtil.getTong(tongId);
			if (tong == null) {
				result = "failure";
				tip = "查询无此帮会!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			if (tong.getUserId() != loginUser.getId()
					&& tong.getUserIdA() != loginUser.getId()
					&& tong.getUserIdB() != loginUser.getId()) {
				result = "error";
				tip = "只有帮主才可以进行此操作！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					.getId());
			List userBagList = UserBagCacheUtil.getUserBagListCacheById(loginUser
					.getId());
			if (userBagList != null) {
				if (userStatus.getUserBag() <= userBagList.size()) {
					result = "error";
					tip = "您的行囊已满.存放该物品";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
			}
			int productId = StringUtil.toInt(request.getParameter("productId"));
			if (productId <= 0) {
				result = "error";
				tip = "参数错误!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			if (productId == 15) {// 轰天炮
				if (tong.getShop() < TONG_SHOP_AGGRESSION) {
					result = "error";
					tip = "帮会商店开发度不够!";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				// 更新帮会商店开发度
				updateTongShop(TONG_SHOP_AGGRESSION, tongId);
				// 向用户行囊添加物品
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), productId);
				tip = "轰天炮已经放入你的行囊，请注意查收!";
			} else if (productId == 16) {// 防护膜
				if (tong.getShop() < TONG_SHOP_RECOVERY) {
					result = "error";
					tip = "帮会商店开发度不够!";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				// 更新帮会商店开发度
				updateTongShop(TONG_SHOP_RECOVERY, tongId);
				// 向用户行囊添加物品
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), productId);
				tip = "防护膜已经放入你的行囊，请注意查收!";
			} else if (productId == 25) {// 强力攻城战鼓
				if (tong.getShop() < TONG_SHOP_ASSAULT_TIME) {
					result = "error";
					tip = "帮会商店开发度不够!";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				// 更新帮会商店开发度
				updateTongShop(TONG_SHOP_ASSAULT_TIME, tongId);
				// 向用户行囊添加物品
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), productId);
				tip = "攻城战鼓已经放入你的行囊，请注意查收!";
			} else if (productId == 26) {// 合金防御壁垒
				if (tong.getShop() < TONG_SHOP_RECOVERY_TIME) {
					result = "error"; 
					tip = "帮会商店开发度不够!";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				// 更新帮会商店开发度
				updateTongShop(TONG_SHOP_RECOVERY_TIME, tongId);
				// 向用户行囊添加物品
				UserBagCacheUtil.addUserBagCacheStack(loginUser.getId(), productId);
				tip = "守城战鼓已经放入你的行囊，请注意查收!";
			} else {
				result = "error";
				tip = "参数错误!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		session.setAttribute("tongShopCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新帮会商店开发度
	 * @datetime:2007-7-3 2:56:26
	 * @param shopD
	 * @return void
	 */
	public void updateTongShop(int shopDevelop, int tongId) {
		TongCacheUtil.updateTongShop("shop=shop-" + shopDevelop,
				"id=" + tongId, tongId, -shopDevelop);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 当铺出售物品页面
	 * @datetime:2006-12-28 上午11:56:34
	 * @param request
	 */
	public void hockshopSale(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		int goodsId = StringUtil.toInt(request.getParameter("goodsId"));
		int goodsTpye = StringUtil.toInt(request.getParameter("goodsTpye"));
		if (tongId <= 0 || goodsId <= 0 || goodsTpye <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tong
				.getId());// 获取帮会当铺
		if (tongHockshop == null) {
			result = "failure";
			tip = "该帮会没有当铺!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int develop = tongHockshop.getDevelop();// 帮会当前开发度
		String hockshopMaxPoint = hockshopDevelop(develop);// 当铺收购物品价格峰值跟回收比率
		String[] priceRate = hockshopMaxPoint.split("_");
		int price = StringUtil.toInt(priceRate[0]);
		int rate = StringUtil.toInt(priceRate[1]);
		if (price <= 0 || rate <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (goodsTpye == 1) {// 打猎物品典当
			HuntUserQuarryBean huntUserQuarry = getJobService()
					.getHuntUserQuarry(
							"quarry_id=" + goodsId + " and user_id="
									+ loginUser.getId());
			if (huntUserQuarry == null) {
				result = "failure";
				tip = "您没有该商品可以典当!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			HashMap quarryMap = LoadResource.getQuarryMap();
			HuntQuarryBean quarry = (HuntQuarryBean) quarryMap.get(new Integer(
					huntUserQuarry.getQuarryId()));
			if (quarry == null) {
				result = "failure";
				tip = "无此猎物!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			int quarryPrice = quarry.getPrice();
			quarryPrice = (int) Arith.div(Arith.mul(quarryPrice, rate), 100, 0);
			if (quarryPrice > price) {
				result = "priceError";
				tip = "掌柜的陪笑道：客官，本店本小利微，收不了您这么大件的东西，请您换个要典当的东西？（2秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			request.setAttribute("quarryPrice", quarryPrice + "");
			request.setAttribute("huntUserQuarry", huntUserQuarry);
		}
		request.setAttribute("tong", tong);
		request.setAttribute("goodsTpye", goodsTpye + "");
		request.setAttribute("result", result);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("hockshopSaleCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 当铺处理结果页面
	 * @datetime:2006-12-28 下午04:09:32
	 * @param request
	 */
	public void hockshopResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("hockshopSaleCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("hockshopSaleCheck");
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		int goodsId = StringUtil.toInt(request.getParameter("goodsId"));
		int goodsTpye = StringUtil.toInt(request.getParameter("goodsType"));
		int goodsCount = StringUtil.toInt(request.getParameter("goodsCount"));
		if (tongId <= 0 || goodsCount <= 0 || goodsId <= 0 || goodsTpye <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (goodsCount > 1000) {
			result = "priceError";
			tip = "一次出售商品数量最大限制为1000个!(2秒后跳转)";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tong
				.getId());// 获取帮会当铺
		if (tongHockshop == null) {
			result = "failure";
			tip = "该帮会没有当铺!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int develop = tongHockshop.getDevelop();// 帮会当前开发度
		String hockshopMaxPoint = hockshopDevelop(develop);// 当铺收购物品价格峰值跟回收比率
		String[] priceRate = hockshopMaxPoint.split("_");
		int price = StringUtil.toInt(priceRate[0]);
		int rate = StringUtil.toInt(priceRate[1]);
		if (price <= 0 || rate <= 0) {
			doTip();
			return;
		}
		if (goodsTpye == 1) {// 打猎物品典当
			HuntUserQuarryBean huntUserQuarry = getJobService()
					.getHuntUserQuarry(
							"quarry_id=" + goodsId + " and user_id="
									+ loginUser.getId());// 获取用户猎物记录
			if (huntUserQuarry == null
					|| huntUserQuarry.getQuarryCount() < goodsCount) {// 判断是否存在该猎物并且拥有足够数量
				result = "priceError";
				tip = "您没有该商品或没有" + goodsCount + "个该类商品可以典当!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			HashMap quarryMap = LoadResource.getQuarryMap();// 获取猎物map
			HuntQuarryBean quarry = (HuntQuarryBean) quarryMap.get(new Integer(// 获取一条猎物记录
					huntUserQuarry.getQuarryId()));
			int quarryPrice = quarry.getPrice();// 取得猎物参考价格
			quarryPrice = (int) Arith.div(Arith.mul(quarryPrice, rate), 100, 0);// 计算单个物品当铺回收价格
			int quarryPriceTotal = quarryPrice * goodsCount;// 计算回收价格总价
			if (quarryPriceTotal > price) {// 判断小店是否可以回收
				result = "priceError";
				tip = "掌柜的陪笑道：客官，本店本小利微，收不了您这么大件的东西，请您换个要典当的东西？（2秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			int tongPrice = (int) Arith.mul(quarryPriceTotal, Arith.div(tong
					.getRate(), 100, 2));// 收取帮会设置税率相应价格,回收捐赠帮会
			TongCacheUtil.updateTong("fund=fund+" + tongPrice
					+ ",fund_total=fund_total+" + tongPrice, "id="
					+ tong.getId(), tong.getId());// 更新帮会基金
			TongTitleRateLogBean tongTitleRateLog = new TongTitleRateLogBean();
			tongTitleRateLog.setUserId(loginUser.getId());
			tongTitleRateLog.setTongId(tong.getId());
			tongTitleRateLog.setGoodsId(goodsId);
			tongTitleRateLog.setGoodsType(goodsTpye);
			tongTitleRateLog.setMoney(quarryPriceTotal);
			tongTitleRateLog.setRateMoney(tongPrice);
			getTongService().addTongTitleRateLog(tongTitleRateLog);// 增加城帮税收记录
			TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
					.getId());// 判断获取是否为帮会会员
			if (tongUser != null) {
				TongCacheUtil.updateTongUserDonation("donation=donation+1",
						"user_id=" + loginUser.getId(), tong.getId(), loginUser
								.getId(), 1);// 更新会员贡献度
			}
			if (huntUserQuarry.getQuarryCount() == goodsCount) {// 判断删除用户拥有猎物记录
				getJobService().deleteHuntUserQuarry(
						"id=" + huntUserQuarry.getId());
			} else {// 判断更新用户拥有猎物记录数量
				getJobService().updateHuntUserQuarry(
						"quarry_count=quarry_count-" + goodsCount,
						"user_id=" + loginUser.getId() + " and id="
								+ huntUserQuarry.getId());
			}
			int updatePrice = (int) Arith.mul(quarryPriceTotal, Arith.div(
					100 - tong.getRate(), 100, 2));// 用户得到的售卖金额
			UserInfoUtil.updateUserCash(loginUser.getId(), updatePrice, UserCashAction.OTHERS, "在当铺出售商品增加乐币"
					+ updatePrice);// 更新用户乐币
			UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					.getId());// 查询用户乐币
			tip = "掌柜的：您卖了" + goodsCount + "个" + quarry.getName() + "，获得"
					+ updatePrice + "乐币!其中" + tong.getRate()
					+ "％作为城市税收缴纳给帮会基金！您现有乐币" + userStatus.getGamePoint()
					+ "（2秒钟跳转)!";
			request.setAttribute("tip", tip);
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会开发显示页面
	 * @datetime:2006-12-28 下午05:56:28
	 * @param develop
	 * @return
	 */
	public void empolder(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			doTip();
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getNowEndure() < TONG_ENDURE) {
			result = "priceError";
			tip = "只有当帮会城墙大于" + TONG_ENDURE + "点，才能进行当铺开发!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		Integer sid = (Integer)session.getAttribute("hockshopCheck");
		if (sid == null || sid.intValue() != getParameterInt("s")) {// 防止刷新
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("hockshopCheck");
		TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tong
				.getId());
		// 帮会当铺开发度大于1000万停止开发
		if (tongHockshop.getDevelop() > 10000000) {
			result = "priceError";
			tip = "开发度已经达到1千万，恭喜贵帮当铺开发完成！";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus.getGamePoint() < TONG_HOCKSHOP_DEVELOP_POINT) {
			result = "priceError";
			tip = "您没有足够的乐币增加帮会当铺开发度!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		boolean isCooldown;
		synchronized(loginUser.getLock()) {
			isCooldown = isCooldown("tong", 1200);
		}
		
		if(isCooldown)
			UserInfoUtil.updateUserCash(loginUser.getId(), -TONG_HOCKSHOP_DEVELOP_POINT, UserCashAction.OTHERS, null);// 更新用户乐币
		int gamePoint = userStatus.getGamePoint() - TONG_HOCKSHOP_DEVELOP_POINT;// 用户现有乐币
		float ciryRate = (float)tong.getNowEndure() / tong.getHighEndure();	// 获取帮会城池当前耐久度比率
		int value = 0;
		if (ciryRate < 0.1) {// 获取开发度相应值
			value = 1;
		} else if (ciryRate < 0.3) {
			value = RandomUtil.nextIntNoZero(3);
		} else if (ciryRate < 0.5) {
			value = RandomUtil.nextIntNoZero(3) + 1;
		} else {
			value = RandomUtil.nextIntNoZero(3) + 2;
		}
		if(isCooldown)
			TongCacheUtil.updateTongHockshopDevelop("develop=develop+" + value,// 更新帮会当铺开发度
					"tong_id=" + tong.getId(), tong.getId(), value);
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断获取是否为帮会会员
		if (tongUser != null) {
			if(isCooldown)
				TongCacheUtil.updateTongUserDonation("donation=donation+1",
						"user_id=" + loginUser.getId(), tong.getId(), loginUser
							.getId(), 1);// 增加帮会会员贡献度
			// tongUser.setDonation(tongUser.getDonation() + 3);
			tip = "掌柜的：开发度增加" + value + "！非常感谢您的支持！目前当铺开发度"
					+ tongHockshop.getDevelop() + "！您的帮会贡献度加1！花费乐币"
					+ TONG_HOCKSHOP_DEVELOP_POINT + ",您还有乐币" + gamePoint
					+ "。（1秒钟跳转）";
		} else {
			tip = "掌柜的：开发度增加" + value + "！非常感谢您的支持！目前当铺开发度"
					+ tongHockshop.getDevelop() + "！花费乐币"
					+ TONG_HOCKSHOP_DEVELOP_POINT + ",您还有乐币" + gamePoint
					+ "。（1秒钟跳转）";
		}
		// macq_2007-1-16_增加用户经验值_start
		if(isCooldown) {
			RankAction.addPoint(loginUser, RANK_POINT);
			// macq_2007-1-16_增加用户经验值_end
			// 日志记录
			TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong.getId(),
					loginUser.getId(), 2);
			if (tongCityRecord == null) {// 添加加固城墙记录
				tongCityRecord = new TongCityRecordBean();
				tongCityRecord.setTongId(tong.getId());
				tongCityRecord.setUserId(loginUser.getId());
				tongCityRecord.setCount(1);
				tongCityRecord.setMark(2);
				getTongService().addTongCityRecord(tongCityRecord);
				tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_1");// 更新缓存
			} else {// 更新加固城墙记录
				getTongService().updateTongCityRecord(
						"count=count+1,create_datetime=now()",
						"id=" + tongCityRecord.getId());
			}
			int[] count = TongAction.countMap.getCount(loginUser.getId());
			count[0]++;
		}
		request.setAttribute("gamePoint", gamePoint + "");
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会管理中的用户申请入帮列表
	 * @datetime:2006-12-29 上午10:05:05
	 * @param request
	 */
	public void tongApplyList(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getUserId() == loginUser.getId()
				|| tong.getUserIdA() == loginUser.getId()
				|| tong.getUserIdB() == loginUser.getId()) {// 判断是否为帮会管理员
			int totalCount = getTongService().getTongApplyCount(
					"tong_id=" + tong.getId() + " and mark=0");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongApplyList.jsp?tongId=" + tong.getId();
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongApplyList = getTongService().getTongApplyList(
					"tong_id=" + tong.getId()
							+ " and mark=0 order by id desc limit " + start
							+ ", " + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongApplyList", tongApplyList);
			request.setAttribute("totalCount", totalCount + "");

			int tongUserCount = tong.getUserCount();
			request.setAttribute("tongUserCount", tongUserCount + "");
			request.setAttribute("tong", tong);
			result = "success";
			request.setAttribute("result", result);
			return;
		} else {
			result = "failure";
			tip = "您没有管理帮会的权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
	}

	/**
	 * 处理申请结盟
	 * 
	 * @param request
	 */
	public void fApply(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取所有申请结盟帮派个数
		String sql = "(select ftong_id from jc_tong_friend where tong_Id="
				+ tongId
				+ " "
				+ "and mark=0) union (select tong_id from jc_tong_friend where ftong_Id="
				+ tongId + " and mark=0)";
		List fApplyList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (fApplyList == null) {
			fApplyList = new ArrayList(0);
		}
		int fApplyCount = fApplyList.size();
		request.setAttribute("tong", tong);
		request.setAttribute("fApplyCount", fApplyCount + "");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 申请结盟列表列表
	 * 
	 * @param request
	 */
	public void fApplyList(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		 //获取所有申请结盟记录
		/*String sql = "(select ftong_id from jc_tong_friend where tong_Id="
				+ tongId
				+ " "
				+ "and mark=0) union (select tong_id from jc_tong_friend where ftong_Id="
				+ tongId + " and mark=0)";
		List fApplyList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (fApplyList == null) {
			fApplyList = new ArrayList(0);
		}
		int totalCount = fApplyList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "fApplyList.jsp?tongId=" + tong.getId();
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List fApplyList1 = fApplyList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("fApplyList", fApplyList1);*/
		session.setAttribute("fApplyList", "true");
		request.setAttribute("tong", tong);
		//获得一个帮会申请结盟记录
		TongFriendBean tongAlly  = getAllyTongId(tongId);
		if(tongAlly == null)
		{
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tongAlly",tongAlly);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 处理申请结盟结果
	 * 
	 * @param request
	 */
	public void fApplyResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取帮会信息
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 防止刷新
		if (session.getAttribute("fApplyList") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("fApplyList");
		// 判断是否为帮会管理员
		boolean isTongAdmin = (tong.getUserId() == loginUser.getId()
				|| tong.getUserIdA() == loginUser.getId() || tong.getUserIdB() == loginUser
				.getId());
		if (!isTongAdmin) {
			result = "failure";
			tip = "您不是帮主或者副帮主，无权进行结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int fTongId = StringUtil.toInt(request.getParameter("fTongId"));
		if (fTongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取盟友信息
		TongBean fTong = TongCacheUtil.getTong(fTongId);
		if (fTong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (TongCacheUtil.isFriendTong(tongId, fTongId)) {
			result = "error";
			tip = "您和" + fTong.getDescription() + "已经是盟友!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// 处理结盟标志位(1代表同意 2代表决绝)
		int flag = 0;
		flag = StringUtil.toInt(request.getParameter("flag"));
		if (flag < 0) {
			flag = 1;
		}
		ITongService service = getTongService();
		// 决绝结盟
		if (flag == 2) {
			// 更新帮会结盟字段
			//getTongService().updateTongFriend("mark=2", sql);
			OsCacheUtil
			.flushGroup(OsCacheUtil.TONG_ALLY_CACHE_GROUP);
			tip = "操作成功,您已经代表" + tong.getTitle() + "决绝了同" + fTong.getTitle()
					+ "结盟!";
		} else {// 同意结盟
			// 更新帮会结盟字段
			//getTongService().updateTongFriend("mark=1", sql);
			TongFriendBean bean = getAllyTongId(tongId);
			if(bean.getTongId()<=bean.getFTongId())
			{
				service.addTongFriend(bean);
			}else{
				bean.setTongId(tongId);
				bean.setFTongId(fTongId);
			service.addTongFriend(bean);
			}
			tip = "操作成功,您已经代表" + tong.getTitle() + "同" + fTong.getTitle()
					+ "结盟!";
			OsCacheUtil
			.flushGroup(OsCacheUtil.TONG_ALLY_CACHE_GROUP);
		}
		OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_TONG_GROUP);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 帮会解除同盟
	 * 
	 * @param request
	 */
	public void tongDisband(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 判断是否为帮会管理员
		boolean isTongAdmin = (tong.getUserId() == loginUser.getId()
				|| tong.getUserIdA() == loginUser.getId() || tong.getUserIdB() == loginUser
				.getId());
		if (!isTongAdmin) {
			result = "failure";
			tip = "您不是帮主或者副帮主，无权进行结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取盟友帮会Id
		int fTongId = StringUtil.toInt(request.getParameter("fTongId"));
		if (fTongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean fTong = TongCacheUtil.getTong(fTongId);
		if (fTong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (!TongCacheUtil.isFriendTong(tongId, fTongId)) {
			result = "failure";
			tip = "您和" + fTong.getTitle() + "不是盟友!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		request.setAttribute("fTong", fTong);
		// 设置防止刷新的参数
		session.setAttribute("tongDisband", "ture");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 解除帮会盟友结果
	 * 
	 * @param request
	 */
	public void tongDisbandResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取帮会信息
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 防止刷新
		if (session.getAttribute("tongDisband") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("tongDisband");
		// 判断是否为帮会管理员
		boolean isTongAdmin = (tong.getUserId() == loginUser.getId()
				|| tong.getUserIdA() == loginUser.getId() || tong.getUserIdB() == loginUser
				.getId());
		if (!isTongAdmin) {
			result = "failure";
			tip = "您不是帮主或者副帮主，无权进行结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int fTongId = StringUtil.toInt(request.getParameter("fTongId"));
		if (fTongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取盟友信息
		TongBean fTong = TongCacheUtil.getTong(fTongId);
		if (fTong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (!TongCacheUtil.isFriendTong(tongId, fTongId)) {
			result = "error";
			tip = "您和" + fTong.getDescription() + "不是盟友!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// 获取帮会基金
		if (tong.getFund() < tongDisband) {
			result = "error";
			tip = "对不起,贵帮基金不足以支违约金!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// 扣除帮会基金
		TongCacheUtil.updateTong("fund=fund-" + tongDisband, "id="
				+ tong.getId(), tong.getId());// 更新帮会基金
		// 更新帮会结盟sql语句
		String sql = null;
		if (tong.getId() <= fTong.getId()) {
			sql = "tong_id=" + tong.getId() + " and ftong_id=" + fTong.getId();
		} else {
			sql = "tong_id=" + fTong.getId() + " and ftong_id=" + tong.getId();
		}
		// 更新帮会结盟字段
		getTongService().updateTongFriend("mark=3", sql);
		// 清除缓存
		OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_TONG_GROUP);
		request.setAttribute("tong", tong);
		tip = "您已经代表" + tong.getTitle() + "撕毁了同" + fTong.getTitle() + "的盟约!";
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 入帮申请书页面
	 * @datetime:2006-12-29 上午10:51:38
	 * @param request
	 */
	public void tongApplyInfo(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int applyId = StringUtil.toInt(request.getParameter("applyId"));
		if (applyId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongApplyBean tongApply = getTongService().getTongApply(
				"id=" + applyId + " and mark=0");
		if (tongApply == null) {
			result = "failure";
			tip = "查询无此申请记录!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongApply.getTongId());
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getUserId() == loginUser.getId()// 判断是否为帮会管理员
				|| tong.getUserIdA() == loginUser.getId()
				|| tong.getUserIdB() == loginUser.getId()) {
			UserBean user = UserInfoUtil.getUser(tongApply.getUserId());
			if (user == null) {
				result = "failure";
				tip = "该用户不存在!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			UserStatusBean userStatus = UserInfoUtil.getUserStatus(tongApply
					.getUserId());
			if (userStatus == null) {
				result = "failure";
				tip = "该用户不存在!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			request.setAttribute("user", user);
			request.setAttribute("userStatus", userStatus);
			request.setAttribute("tongApply", tongApply);
			request.setAttribute("tong", tong);
			result = "success";
			request.setAttribute("result", result);
			session.setAttribute("tongApplyInfoCheck", "true");
			return;
		} else {
			result = "failure";
			tip = "您没有管理帮会的权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 申请入帮处理结果页面
	 * @datetime:2006-12-29 上午11:10:37
	 * @param request
	 */
	public void tongApplyEnd(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int operation = StringUtil.toInt(request.getParameter("operation"));
		if (operation < 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int applyId = StringUtil.toInt(request.getParameter("applyId"));
		if (applyId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongApplyBean tongApply = getTongService().getTongApply(
				"id=" + applyId + " and mark=0");// 获取申请记录
		if (tongApply == null) {
			result = "failure";
			tip = "查询无此申请记录!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongApply.getTongId());// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (session.getAttribute("tongApplyInfoCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(tongApply.getUserId());
		session.removeAttribute("tongApplyInfoCheck");
		if (tong.getUserId() == loginUser.getId()// 判断是否为帮会管理员
				|| tong.getUserIdA() == loginUser.getId()
				|| tong.getUserIdB() == loginUser.getId()) {
			if (operation == 0 || us == null || us.getTong() > 0) {// 0代表决绝，如果该用户已经是别的帮会成员，也一样只能拒绝
				getTongService().updateTongApply(
						"mark=2,manager_id=" + loginUser.getId(),
						"id=" + tongApply.getId());
				// 更新申请人的帮会字段
				UserInfoUtil.updateUserTong(tongApply.getUserId(), 0);
				tip = "拒绝成功！（3秒钟跳转）";
				this.addNotice(tongApply.getUserId(), "帮会拒绝你加入");// 发送消息给申请者
			} else {// 1代表同意
				TongUserBean tongUser = this.getTongUser(tong.getId(),
						tongApply.getUserId());// 查询申请用户是否已经成为帮会会员
				if (tongUser == null) {// 如果帮会用户中不存在添加申请用户进入帮会
					tongUser = new TongUserBean();
					tongUser.setTongId(tong.getId());
					tongUser.setUserId(tongApply.getUserId());
					tongUser.setMark(0);
					tongUser.setDonation(0);
					TongCacheUtil.addTongUser(tongUser);// 增加帮会会员
				}
				getTongService().updateTongApply(
						"mark=1,manager_id=" + loginUser.getId(),
						"id=" + tongApply.getId());
				tip = "批准成功！（3秒钟跳转）";// 更新申请记录状态
				// 更新帮会会员登录状态
				getUserService().updateOnlineUser("tong_id=" + tong.getId(),
						"user_id=" + tongApply.getUserId());
				// 增加帮会成员数量
				TongCacheUtil.updateTong("user_count=user_count+1", "id="
						+ tong.getId(), tong.getId());
				// 更新申请人的帮会字段
				// liuyi 2007-01-16 帮会修改 start
				// liuyi 2007-01-18 更新用户状态bug start
				if (tong.getReward() > 0
						&& (tong.getFund() - tong.getReward() > 0)) { // 新人奖金大于0并且帮会基金够
					UserInfoUtil.updateUserCash(tongApply.getUserId(), tong.getReward(),
							UserCashAction.OTHERS, "加入帮会获得" + tong.getReward()
									+ "乐币");
					this.addNotice(tongApply.getUserId(), "您获得了"
							+ tong.getTitle() + "的新人奖金" + tong.getReward()
							+ "乐币");// 发送消息给申请者
					TongCacheUtil.updateTong("fund=fund-" + tong.getReward(),
							"id=" + tong.getId());
				}
				UserInfoUtil.updateUserTong(tongApply.getUserId(), tong.getId());
				// liuyi 2007-01-18 更新用户状态bug end
				this.addNotice(tongApply.getUserId(), "帮会批准您的加入");// 发送消息给申请者
				ActionTrend.addTrend(tongApply.getUserId(), BeanTrend.TYPE_WGAME, "%1加入了帮会%3", UserInfoUtil.getUser(tongApply.getUserId()).getNickName(), tong.getTitle(), "/tong/tong.jsp?tongId="+tong.getId());
				// liuyi 2007-01-16 帮会修改 end
			}
			result = "success";
			request.setAttribute("tong", tong);
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else {
			result = "failure";
			tip = "您没有管理帮会的权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 任命帮会干部
	 * @datetime:2007-1-24 下午05:48:20
	 * @param tongId
	 * @return
	 */
	public void tongCadre(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getCadreCount() >= 10) {
			result = "error";
			tip = "帮会高层任命数量达到上线!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() != loginUser.getId()) {
			result = "error";
			tip = "您没有此权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		String sql = "select user_id from jc_tong_user where tong_id="
				+ tong.getId() + " and mark=0";
		List tongUserList = SqlUtil.getIntList(sql, Constants.DBShortName);
		if (tongUserList == null) {
			tongUserList = new ArrayList();
		}
		int totalCount = tongUserList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongCadre.jsp?tongId=" + tongId;
		int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongUserList1 = tongUserList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongUserList", tongUserList1);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 任命帮会干部头衔页面
	 * @datetime:2007-1-24 下午05:59:10
	 * @param request
	 */
	public void tongCadreJump(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int userId = StringUtil.toInt(request.getParameter("userId"));// 判断参数
		if (userId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong.getUserId() != loginUser.getId()) {
			result = "failure";
			tip = "您没有此权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		result = "success";
		request.setAttribute("tong", tong);
		request.setAttribute("userId", userId + "");
		request.setAttribute("result", result);
		session.setAttribute("tongCadreJump", "ture");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 任命助手结果页面
	 * @datetime:2007-1-24 下午06:07:26
	 * @param request
	 */
	public void tongCadreResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String name = request.getParameter("name");// 判断参数
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (session.getAttribute("tongCadreJump") == null) {// 防止刷新
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("tongCadreJump");
		if (tong.getCadreCount() >= 10) {
			result = "failure";
			tip = "帮会高层任命数量达到上线!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() != loginUser.getId()) {
			result = "inputError";
			tip = "您没有此权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (name == null || "".equals(name) || name.length() > 6) {
			result = "inputError";
			tip = "对不起,称号不符合规范,请重新任命!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		int userId = StringUtil.toInt(request.getParameter("userId"));// 判断参数
		if (userId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean usb = UserInfoUtil.getUserStatus(userId);
		if(usb == null || usb.getTong() != tong.getId()) {
			request.setAttribute("result", "inputError");
			request.setAttribute("tip", "任命失败");
			request.setAttribute("tong", tong);
			return;
		}
		// 增加帮会干部数量
		TongCacheUtil.updateTong("cadre_count=cadre_count+1", "id="
				+ tong.getId(), tong.getId());
		// 更改用户为帮会干部并更新描述
		TongCacheUtil
				.updateTongUser("mark=3,honor='" + StringUtil.toSql(name) + "'", "user_id="
						+ userId + " and tong_id=" + tong.getId(),
						tong.getId(), userId);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 废除帮会干部
	 * @datetime:2007-1-24 下午07:12:17
	 * @param request
	 */
	public void tongCadreDelete(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (session.getAttribute("nominateAssistant") == null) {// 防止刷新
			result = "refrush";
			request.setAttribute("result", result);
			request.setAttribute("tong", tong);
			return;
		}
		session.removeAttribute("nominateAssistant");
		int userId = StringUtil.toInt(request.getParameter("userId"));// 判断参数
		if (userId <= 0) {
			result = "error";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() != loginUser.getId()) {// 判断操作用户是否为帮主
			result = "error";
			tip = "您没有此权限!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// 更改帮会干部数量
		if (tong.getCadreCount() <= 0) {
			TongCacheUtil.updateTong("cadre_count=0", "id=" + tong.getId(),
					tong.getId());
		} else {
			TongCacheUtil.updateTong("cadre_count=cadre_count-1", "id="
					+ tong.getId(), tong.getId());
		}
		// 废除用户帮会干部功能并更新描述为空
		TongCacheUtil.updateTongUser("mark=0,honor=''", "user_id=" + userId
				+ " and tong_id=" + tong.getId(), tong.getId(), userId);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 开除帮会会员
	 * @datetime:2007-1-25 上午10:11:51
	 * @param request
	 */
	public void tongExpel(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int userId = StringUtil.toInt(request.getParameter("userId"));// 判断参数
		if (userId <= 0) {
			result = "error";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() != loginUser.getId()
				&& tong.getUserIdA() != loginUser.getId()
				&& tong.getUserIdB() != loginUser.getId()) {// 判断操作用户是否为帮主
			result = "error";
			tip = "您没有权限开除帮会会员!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}

		TongUserBean tongUser = this.getTongUser(tong.getId(), userId);
		if (tongUser == null) {
			result = "error";
			tip = "用户不存在!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tongUser.getMark() == 3) {
			result = "error";
			tip = "对不起,他是帮主任命的干部,必须先解除其职务!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		if (tong.getUserId() == userId || tong.getUserIdA() == userId
				|| tong.getUserIdB() == userId) {// 判断是否为副帮主
			result = "error";
			tip = "帮主与副帮主不能被开除!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		} else {
			TongCacheUtil.updateTong("user_count=user_count-1", "id="
					+ tong.getId(), tong.getId());
		}
		TongCacheUtil.deleteTongUser("user_id=" + userId, userId, tong.getId());// 删除帮会会员记录
		getTongService()
				.updateTongApply(
						"mark=3,manager_id=" + loginUser.getId()
								+ ",create_datetime=now()",
						"mark=1 and tong_id=" + tong.getId() + " and user_id="
								+ userId);// 帮会申请表更改状态标志
		getUserService().updateOnlineUser("tong_id=0", "user_id=" + userId);// 更新用户在线表帮会标志
		UserInfoUtil.updateUserTong(userId, 0);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 向用户发送用户消息方法
	 * @datetime:2006-12-29 下午01:19:55
	 * @param userId
	 * @param Title
	 */
	public void addNotice(int userId, String Title) {
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle(Title);
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/tong/tongList.jsp");
		ServiceFactory.createNoticeService().addNotice(notice);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 当铺回收价格峰值跟回收比率
	 * @datetime:2006-12-28 下午03:04:23
	 * @param develop
	 * @return
	 */
	public String hockshopDevelop(int develop) {
		String value = "";
		if (develop < 100) {
			value = "100_50";
		} else if (develop < 1000) {
			value = "1000_55";
		} else if (develop < 10000) {
			value = "5000_60";
		} else if (develop < 100000) {
			value = "20000_65";
		} else if (develop < 500000) {
			value = "50000_70";
		} else if (develop < 1000000) {
			value = "100000_75";
		} else {
			value = SYSTEM_MAX_GAM_POINT + "_80";
		}
		return value;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 转换时间
	 * @datetime:2006-12-27 下午05:52:12
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public String transitionDatetime(String datetime) {
		if (datetime == null || datetime.replace(" ", "").equals("")) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String[] date = datetime.split(" ");
		String[] time = date[1].split(":");
		sb.append(date[0]);
		sb.append(" ");
		sb.append(time[0]);
		sb.append(":");
		sb.append(time[1]);
		return sb.toString();
	}

	/**
	 * 
	 * @author macq explain : 获取帮会成员
	 * @datetime:2006-12-25 上午11:39:05
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public TongUserBean getTongUser(int tongId, int userId) {
		TongUserBean tongUser = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(userId);
		if (us != null && us.getTong() == tongId) {
			tongUser = TongCacheUtil.getTongUser(tongId, userId);
		}
		return tongUser;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取一个用户的累计捐赠基金数量
	 * @datetime:2006-12-27 下午02:58:13
	 * @param tongId
	 * @param userId
	 * @param mark
	 * @return
	 */
	public long getTongFundMoney(int tongId, int userId, int mark) {
		long fundMoney = 0;
		TongFundBean tongFund = TongCacheUtil.getTongFund(tongId, userId, mark);
		if (tongFund != null) {
			fundMoney = tongFund.getMoney();
		}
		return fundMoney;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取一个用户的捐赠基金记录
	 * @datetime:2007-1-2 下午02:01:04
	 * @param tongId
	 * @param userId
	 * @param mark
	 * @return
	 */
	public TongFundBean getTongFund(int tongFundId) {
		TongFundBean tongFund = TongCacheUtil.getTongFundById(tongFundId);
		return tongFund;
	}

	public static int getTongRank(TongUserBean bean) {
		int donation = bean.getDonation();
		int rank = 0;
		if (donation < 500) {
			rank = 0;
		} else if (donation < 2000) {
			rank = 1;
		} else if (donation < 10000) {
			rank = 2;
		} else if (donation < 30000) {
			rank = 3;
		} else if (donation < 100000) {
			rank = 4;
		} else if (donation < 200000) {
			rank = 5;
		} else if (donation < 500000) {
			rank = 6;
		} else if (donation < 1000000) {
			rank = 7;
		} else {
			rank = 8;
		}
		return rank;
	}
	/**
	 * 
	 * @author macq
	 * @explain : 帮会称号
	 * @datetime:2006-12-29 下午02:16:18
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public static String getTongTitle(TongUserBean bean) {
		if (bean.getMark() == 3) {
			return bean.getHonor();
		}

		return rankNames[getTongRank(bean)];
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会在线成员id列表
	 * @datetime:2006-12-25 下午12:16:12
	 * @param tongId
	 * @return
	 */
	public List getOnlineTongUserIdList(int tongId) {
		String key = tongId + "_isOnline";
		// 从缓存中取
		List onlineUser = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP,
				OsCacheUtil.TONG_USER_ONLINE_FLUSH_PERIOD);
		if (onlineUser == null) {
			String sql = "SELECT user_id FROM jc_online_user where tong_id="
					+ tongId;
			onlineUser = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (onlineUser == null) {
				onlineUser = new ArrayList();
			}
			// 放到缓存中
			OsCacheUtil.put(key, onlineUser,
					OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP);
		}
		return onlineUser;
	}

	// /**
	// *
	// * @author macq
	// * @explain : 获取帮会在线成员id数量
	// * @datetime:2006-12-25 下午12:16:12
	// * @param tongId
	// * @return
	// */
	// public int getOnlineTongUserIdCount(int tongId) {
	// String sql = "SELECT count(id) c_id FROM jc_online_user where tong_id="
	// + tongId;
	// int onlineTongUserIdCount = SqlUtil.getIntResult(sql,
	// Constants.DBShortName);
	// if (onlineTongUserIdCount == -1) {
	// onlineTongUserIdCount = 0;
	// }
	// return onlineTongUserIdCount;
	// }

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮派排名
	 * @datetime:2006-12-25 下午04:57:03
	 * @param tongId
	 * @return
	 */
	public int getTongCompositor(int tongUserCount) {
		String sql = "SELECT count(id) FROM jc_tong where user_count>"
				+ tongUserCount;
		// 从缓存中取
		Integer count = (Integer) OsCacheUtil.get(sql,
				OsCacheUtil.TONG_COMPOSITOR_CACHE_GROUP,
				OsCacheUtil.TONG_COMPOSITOR_FLUSH_PERIOD);
		if (count == null) {
			int tongCompositor = SqlUtil.getIntResult(sql,
					Constants.DBShortName);
			if (tongCompositor == -1) {
				tongCompositor = 0;
			}
			count = new Integer(tongCompositor);
			// 放到缓存中
			OsCacheUtil
					.put(sql, count, OsCacheUtil.TONG_COMPOSITOR_CACHE_GROUP);
		}
		return count.intValue() + 1;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会城墙图片显示
	 * @datetime:2007-1-6 下午03:12:42
	 * @param request
	 */
	public int tongCityImage(TongBean tong) {
		double ciryRate = Arith.div(tong.getNowEndure(), tong.getHighEndure(),
				2);// 获得当前城墙耐久度比率
		int value = 0;
		if (ciryRate < 0.1) {// 判断城墙耐久度比率
			value = 5;
		} else if (ciryRate < 0.3) {
			value = 4;
		} else if (ciryRate < 0.5) {
			value = 3;
		} else if (ciryRate < 0.9) {
			value = 2;
		} else {
			value = 1;
		}
		return value;
	}

	/**
	 * 申请帮会
	 */
	public void tongErect(HttpServletRequest request) {

		String result = null;
		String tip = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() > 0) {
			tip = "每个人只能属于一个帮会，您已经是某帮的成员，不能创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (us.getTong() == -1) {
			tip = "您已经申请了某帮，不能创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongManageLogBean log = getTongService()
				.getTongManageLog(
						"user_id="
								+ loginUser.getId()
								+ " and mark=4 and to_days(now())-to_days(create_datetime)<30");
		if (log != null) {
			tip = "解散帮会后一个月内不能再创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		if (status.getGamePoint() < MIN_MONEY_FOR_TONG
				|| status.getRank() <= MIN_RANK_FOR_TONG
				|| status.getSocial() < MIN_SOCIAL_FOR_TONG) {
			result = "Error";
			request.setAttribute("result", result);
			return;
		}

	}

	/**
	 * 选择地点
	 */
	public void tongLocation(HttpServletRequest request) {

		String result = null;
		String tip = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() > 0) {
			tip = "每个人只能属于一个帮会，您已经是某帮的成员，不能创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (us.getTong() == -1) {
			tip = "您已经申请了某帮，不能创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongManageLogBean log = getTongService()
				.getTongManageLog(
						"user_id="
								+ loginUser.getId()
								+ " and mark=4 and to_days(now())-to_days(create_datetime)<30");
		if (log != null) {
			tip = "解散帮会后一个月内不能再创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		if (status.getGamePoint() < 1000000
				|| status.getRank() < MIN_RANK_FOR_TONG
				|| status.getSocial() < 1000) {
			result = "userError";
			request.setAttribute("result", result);
			return;
		}
		// 名称错误
		String tongName = request.getParameter("tongName");
		if(tongName != null)
			tongName = StringUtil.noEnter(tongName);
		if (tongName == null || tongName.equals("")) {
			tip = "请输入帮会名称";
			result = "nameError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean bean = getTongService().getTong("title='" + StringUtil.toSql(tongName) + "'");
		if (bean != null) {
			tip = "已经存在该帮会，请重新输入名称";
			result = "nameError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 成功
		Vector locationList = getTongService().getTongLocationList("1=1");
		result = "success";
		request.setAttribute("tongName", tongName);
		request.setAttribute("locationList", locationList);
		session.setAttribute("tongerect", "ok");
		request.setAttribute("result", result);

	}

	/**
	 * 申请结果
	 */
	public void erectResult(HttpServletRequest request) {

		String result = null;
		String tip = null;
		if (session.getAttribute("tongerect") == null) {
			tip = "参数错误！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("tongerect");
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() > 0) {
			tip = "每个人只能属于一个帮会，您已经是某帮的成员，不能创建帮会！";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		// 资格不够
		if (status.getGamePoint() < MIN_MONEY_FOR_TONG
				|| status.getRank() < MIN_RANK_FOR_TONG
				|| status.getSocial() < MIN_SOCIAL_FOR_TONG) {
			result = "userError";
			request.setAttribute("result", result);
			return;
		}
		String tongName = request.getParameter("tongName");
		if(tongName != null)
			tongName = StringUtil.noEnter(tongName);
		if (tongName == null || tongName.length() < 2) {
			tip = "帮会名称非法";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		
		String locationId = request.getParameter("locationId");
		if (tongName != null && locationId != null) {
			if (UserInfoUtil.updateUserCash(loginUser.getId(), -1000000,
					UserCashAction.OTHERS, "建立帮会扣钱100万")) {

				// 添加帮会
				TongBean tong = new TongBean();
				tong.setLocationId(StringUtil.toInt(locationId));
				tong.setUserId(loginUser.getId());
				tong.setUserIdA(-1);
				tong.setUserIdB(-1);
				tong.setTitle(tongName);
				tong.setHighEndure(1000);
				tong.setNowEndure(100);
				tong.setUserIdB(-1);
				tong.setUserIdA(-1);
				tong.setUserCount(1);
				tong.setDescription(tongName + "成立了！");
				tong.setRate(10);
				TongCacheUtil.addTong(tong);
				tong = getTongService().getTong(
						"user_id=" + loginUser.getId()
								+ " order by create_datetime desc");
				if (tong != null) {
					ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_CREATE, "%1创建了帮会%3", loginUser.getNickName(), tong.getTitle(), "/tong/tong.jsp?tongId="+tong.getId());
					// 有帮会
					UserInfoUtil.updateUserTong(loginUser.getId(), tong.getId());// 更新用户乐币
					// 修改jc_online_user表
					getUserService().updateOnlineUser(
							"tong_id=" + tong.getId(),
							"user_id=" + loginUser.getId());

					// 添加人员
					TongUserBean user = new TongUserBean();
					user.setMark(2);
					user.setUserId(loginUser.getId());
					user.setTongId(tong.getId());
					TongCacheUtil.addTongUser(user);

					// 添加当铺
					TongHockshopBean bean = new TongHockshopBean();
					bean.setTongId(tong.getId());
					TongCacheUtil.addTongHockshop(bean);
					// 添加帮会论坛
					ForumBean forum = new ForumBean();
					forum.setUserId(loginUser.getId() + "");
					forum.setMark(1);
					forum.setTitle(tongName);
					forum.setDescription("欢迎大家来做客!");
					forum.setTongId(tong.getId());
					getForumService().addForum(forum);
					OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP,
							"TongForum");

					result = "success";
					request.setAttribute("result", result);
					request.setAttribute("tongName", tongName);
					request.setAttribute("tongId", tong.getId() + "");
				}

			}

		}

	}

	/**
	 * 管理帮会
	 */
	public void tongManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		if (tong != null) {
			request.setAttribute("tong", tong);

		} else {
			tong = getTongTwo(tongId);
			request.setAttribute("tongfu", tong);

		}
	}

	/**
	 * 解散帮会
	 */
	public void tongDissolve(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean isuser = getTongOne(tongId);

		if (isuser != null) {
			int del = StringUtil.toInt(request.getParameter("del"));
			if (del == isuser.getId()) {
				if (request.getParameter("time").equals("1")) {
					UserStatusBean status = (UserStatusBean) UserInfoUtil
							.getUserStatus(loginUser.getId());
					if (status.getGamePoint() < 100000
							|| status.getPoint() < 10000) {
						request.setAttribute("tip",
								"对不起您的乐币不足10万或经验不足1万，您不能解散帮会。");
						request.setAttribute("result", "4");
						request.setAttribute("isuser", isuser);

					} else {
						request.setAttribute("tip",
								"您真的要解散帮会吗？您还有转让帮主等可以选择，不一定非得解散!");
						request.setAttribute("result", "2");
						request.setAttribute("isuser", isuser);

					}

				} else if (request.getParameter("time").equals("2")) {
					if (UserInfoUtil.updateUserStatus(
							"game_point=game_point-100000,point=point-10000",
							"user_id=" + loginUser.getId(), loginUser.getId(),
							UserCashAction.OTHERS, "解散帮会扣钱10万,经验1万")) {
						// 论坛无帮主

						// 给帮众发消息
						Vector users = (Vector) getTongService()
								.getTongUserList("tong_id=" + del);
						if (users != null) {
							Calendar cal = Calendar.getInstance();
							int day = cal.get(Calendar.DAY_OF_MONTH);
							int month = cal.get(Calendar.MONTH) + 1;
							int year = cal.get(Calendar.YEAR);
							long money = 0;
							if (isuser.getUserCount() > 0)
								money = isuser.getFund()
										/ isuser.getUserCount();
							String content = "由于经营不善，"
									+ StringUtil.toWml(isuser.getTitle())
									+ "帮于" + year + "年" + month + "月" + day
									+ "日被帮主"
									+ StringUtil.toWml(loginUser.getNickName())
									+ "解散 ，你现在是自由身了，获得遣散费" + money + "乐币";

							// 系统消息
							NoticeBean sysnotice = new NoticeBean();
							sysnotice.setUserId(0);
							sysnotice.setTitle(content);
							sysnotice.setContent("");
							sysnotice.setHideUrl("");
							sysnotice.setType(NoticeBean.SYSTEM_NOTICE);
							sysnotice.setLink("/tong/tong.jsp?tongId=" + del);
							sysnotice.setTongId(del);
							NoticeUtil.getNoticeService().addNotice(sysnotice);
							OsCacheUtil
									.flushGroup(OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
							// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);

							for (int i = 0; i < users.size(); i++) {
								TongUserBean user = (TongUserBean) users.get(i);
								if (user != null) {
									UserBean userBean = (UserBean) UserInfoUtil
											.getUser(user.getUserId());
									if (userBean != null) {
										UserInfoUtil.updateUserCash(user.getUserId(), money,
												UserCashAction.OTHERS, "解散帮会获遣散费乐币" + money);
										UserInfoUtil.updateUserTong(user.getUserId(), 0);
										// 修改在线用户消息
										getUserService().updateOnlineUser("tong_id=0",
												"user_id=" + user.getUserId());

									}
								}
							}
						}
						// 在聊天大厅增加解散帮会物品信息
						JCRoomContentBean jcRoomContent = new JCRoomContentBean();
						jcRoomContent = new JCRoomContentBean();
						jcRoomContent.setFromId(isuser.getUserId());
						jcRoomContent.setToId(0);
						jcRoomContent.setFromNickName(loginUser.getNickName());
						jcRoomContent.setToNickName(isuser.getTitle());
						jcRoomContent.setContent(StringUtil.toWml(isuser
								.getTitle())
								+ "帮由于经营不善倒闭，帮主"
								+ StringUtil.toWml(loginUser.getNickName())
								+ "羞愧难当，经验降低１万");
						jcRoomContent.setAttach("");
						jcRoomContent.setIsPrivate(0);
						jcRoomContent.setRoomId(0);
						jcRoomContent.setSecRoomId(-1);
						jcRoomContent.setMark(10);
						getChatService().addContent(jcRoomContent);
						TongCacheUtil
								.updateTong(
										"mark=1,title='荒城',user_count=0,cadre_count=0,user_id=-1,user_id_a=-1,user_id_b=-1,rate=0",
										"id=" + del, del);
						TongCacheUtil.deleteTongUserAll("tong_id=" + del);
						TongManageLogBean log = new TongManageLogBean();
						log.setUserId(loginUser.getId());
						log.setReceiveUserId(0);
						log.setTongId(del);
						log.setMark(4);
						getTongService().addTongManageLog(log);

						// 删除申请记录
						deleteApplyList(del);
						getForumService().updateForum(
								"title='荒城',description='',user_id=''",
								"tong_id=" + isuser.getId());
						ForumCacheUtil.deleteTongForum(isuser.getId());
						ForumCacheUtil.deleteTongForumList();
						request
								.setAttribute("tip",
										"您的帮会已经解散，城市变成荒城，扣除您10万乐币，1万经验。胜败乃兵家常事，大侠请重新来过吧。");
						request.setAttribute("result", "3");
						request.setAttribute("isuser", isuser);
					}

				} else {
					request.setAttribute("tip", "您没有资格解散帮会!");
					request.setAttribute("result", "5");

				}

			} else {
				request.setAttribute("result", "1");
				request.setAttribute("tip",
						"解散帮会需要扣除帮主10万乐币，1万经验。而且一个月内不能申请新的帮会。");
				request.setAttribute("isuser", isuser);

			}
		} else {
			request.setAttribute("tip", "您没有资格解散帮会!");
			request.setAttribute("result", "5");
		}
	}

	/**
	 * 改旗易帜
	 */
	public void changeName(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);

		request.setAttribute("tong", tong);
		session.setAttribute("changeNameResult", "ok");

	}

	/**
	 * 改旗易帜结果
	 */
	public static int changeNameMoney = 1000000000;
	public void changeNameResult(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		String result = null;
		String tip = null;
		if (tong != null) {
			request.setAttribute("tongId", tong.getId() + "");
		}
		String tongName = request.getParameter("tongName");
		tongName = StringUtil.noEnter(tongName);

		if (session.getAttribute("changeNameResult") == null) {
			result = "nameError";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("changeNameResult");
		if (tongName == null || tongName.equals("")) {
			tip = "请输入帮会名称";
			result = "nameError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (StringUtil.getCLength(tongName) > 12) {
			tip = "帮会名称过长，请不要超过6个中文字";
			result = "nameError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean bean = getTongService().getTong("title='" + StringUtil.toSql(tongName) + "'");
		if (bean != null) {
			tip = "已经存在该帮会，请重新输入名称";
			result = "nameError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int id = getParameterInt("tongId");
		TongBean isuser = getTongService().getTong(
				"user_id=" + loginUser.getId() + " and id=" + id
						+ " and mark=0");
		if (isuser != null) {
			UserStatusBean status = (UserStatusBean) UserInfoUtil
					.getUserStatus(loginUser.getId());
			if (status.getGamePoint() < changeNameMoney) {
				request.setAttribute("tip", "对不起您的乐币不足" + changeNameMoney + "，您不能改旗易帜。");
				request.setAttribute("result", "userError");
			} else {
				// 成功
				result = "success";
				request.setAttribute("tongName", tongName);
				if (TongCacheUtil.updateTong("title='" + StringUtil.toSql(tongName) + "'", "id="
						+ id, id)) {
					UserInfoUtil.updateUserCash(loginUser.getId(), -changeNameMoney,
							UserCashAction.OTHERS, "帮会易帜扣乐币1000");
					tip = "您的帮会已更名为" + tongName;
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					getForumService().updateForum("title='" + StringUtil.toSql(tongName) + "'",
							"tong_id=" + isuser.getId());
					ForumCacheUtil.deleteTongForum(isuser.getId());
					ForumCacheUtil.deleteTongForumList();
					// 给帮众发消息
					Calendar cal = Calendar.getInstance();
					int day = cal.get(Calendar.DAY_OF_MONTH);
					int month = cal.get(Calendar.MONTH) + 1;
					int year = cal.get(Calendar.YEAR);
					String content = "自" + year + "年" + month + "月" + day
							+ "日起，" + StringUtil.toWml(isuser.getTitle())
							+ "帮更名为" + StringUtil.toWml(tongName) + "帮，帮主特此通知。";
					// noticeContentMap.put(isuser.getId() + "_2", content);
					// noticeUserMap.put(isuser.getId() + "_2", userList);
					// noticeTitleMap.put(isuser.getId() + "_2", "帮会改名了");

					// 发系统消息

					// 系统消息
					NoticeBean sysnotice = new NoticeBean();
					sysnotice.setUserId(0);
					sysnotice.setTitle(content);
					sysnotice.setContent("");
					sysnotice.setHideUrl("");
					sysnotice.setType(NoticeBean.SYSTEM_NOTICE);
					sysnotice.setLink("/tong/tong.jsp?tongId=" + tong.getId());
					sysnotice.setTongId(tong.getId());
					NoticeUtil.getNoticeService().addNotice(sysnotice);
					OsCacheUtil
							.flushGroup(OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
					// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);

					return;
				}
			}
		} else {
			tip = "您无权易帜";
			result = "userError";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
		}

	}

	/**
	 * 帮助转让
	 */
	public void tongTransfer(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		request.setAttribute("tong", tong);
		session.setAttribute("transferResult", "ok");
	}

	/**
	 * 帮助转让本帮兄弟
	 */
	public void tongUsers(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);

		request.setAttribute("tong", tong);
		if (tong != null) {
			String result = null;
			List tongList = TongCacheUtil.getTongUserListById(tong.getId());
			// liuyi 2007-01-16 帮会修改 start
			int totalCount = tongList.size();
			// getTongService().getTongUserCount(
			// "tong_id=" + tong.getId() + " and user_id!="
			// + loginUser.getId());
			// liuyi 2007-01-16 帮会修改 end
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}

			String prefixUrl = "tongUsers.jsp?tongId=" + tong.getId();

			// 取得要显示的消息列表
			int start = pageIndex * NUMBER_PER_PAGE;
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongList1 = tongList.subList(startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongList", tongList1);
			result = "success";
			request.setAttribute("result", result);
			//session.setAttribute("transferResult", "ok");
			return;
		}
	}

	/**
	 * 帮助转让其他帮帮主
	 */
	public void tongHosts(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);

		request.setAttribute("tong", tong);
		String result = null;
		List tongList = TongCacheUtil.getTongListByIdOrderByCount();
		int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongHosts.jsp?tongId=" + tongId;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongList", tongList1);
		result = "success";
		request.setAttribute("result", result);
		//session.setAttribute("transferResult", "ok");
		return;

	}
	
	/**
	 *  
	 * @author macq
	 * @explain：帮主转让通知
	 * @datetime:2007-9-6 13:26:12
	 * @param request
	 * @return void
	 */
	public void transferToNotice(HttpServletRequest request) {
		String tip = null;
		//if (session.getAttribute("transferResult") == null) {
		//	result = "userError";
		//	tip = "参数错误";
		//	request.setAttribute("result", result);
		//	request.setAttribute("tip", tip);
		//	return;
		//}
		//session.removeAttribute("transferResult");
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if(tongId<=0){
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		if(getTransferTongId(tongId)!=null){
			request.setAttribute("tip", "您在之前一小时内发送过转让通知.请一小时后再次发送");
			request.setAttribute("tongId", String.valueOf(tongId));
			request.setAttribute("result", "userError");
			return;
		}
		TongBean tong = getTongOne(tongId);
		if(tong==null){
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		request.setAttribute("tongId", tongId + "");

		if (tong != null) {
			if (request.getParameter("user") != null) {
				//UserStatusBean status = (UserStatusBean) UserInfoUtil
				//		.getUserStatus(loginUser.getId());
				//if (status.getGamePoint() < 100000) {
				//	request.setAttribute("tip", "对不起您的乐币不足10万，您不能转让帮会。");
				//	request.setAttribute("result", "userError");
				//	return;
				//}
				// liuyi 2007-01-16 帮会修改 start
				String user = request.getParameter("user");
				int userId = StringUtil.toInt(user);
				if (userId < 1) {
					request.setAttribute("tip", "该用户不存在。");
					request.setAttribute("result", "userError");
					return;
				}
				UserStatusBean userStatusBean = UserInfoUtil
						.getUserStatus(userId);
				if (userStatusBean == null) {
					request.setAttribute("tip", "该用户不存在。");
					request.setAttribute("result", "userError");
					return;
				}
				if (userStatusBean.getTong() != tong.getId()
						|| userStatusBean.getRank() <= TongAction.MIN_RANK_FOR_TONG
						|| userStatusBean.getGamePoint() < TongAction.MIN_MONEY_FOR_TONG
						|| userStatusBean.getSocial() <= TongAction.MIN_SOCIAL_FOR_TONG) {
					request.setAttribute("tip", "该用户还不满足帮主的条件。");
					request.setAttribute("result", "userError");
					return;
				}
				
				// 系统消息
				NoticeBean notice = new NoticeBean();
				notice.setUserId(userId);
				notice.setTitle("帮会转让通知.");
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setLink("/tong/transferReceiveNotice.jsp?tongId=" + tong.getId());
				NoticeUtil.getNoticeService().addNotice(notice);
				putTransferTongId(tongId,loginUser.getId(),userId,TongTransferBean.TONG_USER);
			} else if (request.getParameter("host") != null) {
				int host = StringUtil.toInt(request.getParameter("host"));
				if (host <=0) {
					request.setAttribute("tip", "该帮主不存在。");
					request.setAttribute("result", "userError");
					return;
				}
				//UserStatusBean status = (UserStatusBean) UserInfoUtil
				//		.getUserStatus(loginUser.getId());
				//if (status.getGamePoint() < 100000) {
				//	request.setAttribute("tip", "对不起您的乐币不足10万，您不能转让帮会。");
				//	request.setAttribute("result", "userError");
				//	return;
				//}
				TongBean tonghost = getTongService().getTong(
						"user_id=" + host + " and mark=0");
				if (tonghost != null) {
					NoticeBean notice = new NoticeBean();
					notice.setUserId(host);
					notice.setTitle("帮会转让通知.");
					notice.setContent("");
					notice.setHideUrl("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setLink("/tong/transferReceiveNotice.jsp?tongId=" + tong.getId());
					NoticeUtil.getNoticeService().addNotice(notice);
					putTransferTongId(tongId,loginUser.getId(),host,TongTransferBean.TONG_OTHER_USER);
				}
			}
			
			request.setAttribute("tongId",String.valueOf(tong.getId()));
			tip = "帮主转让通知已经成功发出,请等待对方确认！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "success");
			return;
		}
	}
	/**
	 *  
	 * @author guip
	 * @explain：添加帮会结盟缓存
	 * @datetime:2007-12-23 15:22:26
	 * @param tongId
	 * @return
	 * @return List
	 */
	public void putAllyTongId(int tongId,int FTongId,int mark) {
		String key = String.valueOf(FTongId);
		// 从缓存中取
		Integer tongIdMark = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.TONG_ALLY_CACHE_GROUP,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (tongIdMark == null) {
			TongFriendBean bean =  new TongFriendBean();
			bean.setTongId(tongId);
			bean.setFTongId(FTongId);
			bean.setMark(mark);
			// 放到缓存中
			OsCacheUtil.put(key,bean,
					OsCacheUtil.TONG_ALLY_CACHE_GROUP);
		}
		return ;
	}
	/**
	 *  
	 * @author macq
	 * @explain：添加帮会转让缓存
	 * @datetime:2007-9-6 15:22:26
	 * @param tongId
	 * @return
	 * @return List
	 */
	public void putTransferTongId(int tongId,int tongUserId,int receiveUserId,int mark) {
		String key = tongId + "";
		// 从缓存中取
		Integer tongIdMark = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_CACHE_GROUP,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (tongIdMark == null) {
			TongTransferBean bean =  new TongTransferBean();
			bean.setTongId(tongId);
			bean.setTongUserId(tongUserId);
			bean.setReceiveUserId(receiveUserId);
			bean.setMark(mark);
			// 放到缓存中
			OsCacheUtil.put(key,bean,
					OsCacheUtil.TONG_TRANSFER_TONG_ID_CACHE_GROUP);
		}
		return ;
	}
	/**
	 *  
	 * @author guip
	 * @explain：查询帮会结盟缓存
	 * @datetime:2007-12-23 15:27:05
	 * @param tongId
	 * @return void
	 */
	public TongFriendBean getAllyTongId(int tongId) {
		String key = String.valueOf(tongId);
		// 从缓存中取
		TongFriendBean tongAlly = (TongFriendBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_ALLY_CACHE_GROUP,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (tongAlly == null) {
			return null;
		}
		return tongAlly;
	}
	/**
	 *  
	 * @author macq
	 * @explain：查询帮会转让缓存
	 * @datetime:2007-9-6 15:27:05
	 * @param tongId
	 * @return void
	 */
	public TongTransferBean getTransferTongId(int tongId) {
		String key = tongId + "";
		// 从缓存中取
		TongTransferBean tongTransfer = (TongTransferBean) OsCacheUtil.get(key,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_CACHE_GROUP,
				OsCacheUtil.TONG_TRANSFER_TONG_ID_FLUSH_PERIOD);
		// 缓存中没有
		if (tongTransfer == null) {
			return null;
		}
		return tongTransfer;
	}
	
	
	/***
	 *  
	 * @author macq
	 * @explain：查看帮主转让通知内容
	 * @datetime:2007-9-6 13:48:17
	 * @param request
	 * @return void
	 */
	public void transferReceiveNotice(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <=0) {
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		TongTransferBean tongTransfer = getTransferTongId(tongId);
		if(tongTransfer==null){
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮户记录
		if (tong == null) {
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		int userId = tongTransfer.getTongUserId();
		if (userId <=0) {
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		UserBean user = UserInfoUtil.getUser(userId);
		if(user==null){
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			return;
		}
		if(tongTransfer.getMark()==TongTransferBean.TONG_USER){
			int receiveUserId = tongTransfer.getReceiveUserId();
			if (receiveUserId <=0) {
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			if(receiveUserId!=loginUser.getId()){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			UserStatusBean userStatusBean = UserInfoUtil.getUserStatus(receiveUserId);
			if (userStatusBean.getTong() != tong.getId()
					|| userStatusBean.getRank() <= TongAction.MIN_RANK_FOR_TONG
					|| userStatusBean.getGamePoint() < TongAction.MIN_MONEY_FOR_TONG
					|| userStatusBean.getSocial() <= TongAction.MIN_SOCIAL_FOR_TONG) {
				request.setAttribute("tip", "您还不满足当选帮主的条件。");
				request.setAttribute("result", "userError");
				return;
			}
			request.setAttribute("tong", tong);
			request.setAttribute("tongTransfer", tongTransfer);
			request.setAttribute("result", "success");
			return;
		}else if(tongTransfer.getMark()==TongTransferBean.TONG_OTHER_USER){
			int host = tongTransfer.getReceiveUserId();
			if (host <=0) {
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			if(host!=loginUser.getId()){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			TongBean tonghost = getTongService().getTong(
					"user_id=" + host + " and mark=0");
			if(tonghost==null){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			request.setAttribute("tong", tong);
			request.setAttribute("tongTransfer", tongTransfer);
			request.setAttribute("result", "success");
		}else{
			request.setAttribute("tip", "参数错误");
			request.setAttribute("result", "userError");
			request.setAttribute("flag", "0");
			return;
		}
	}

	/**
	 * 帮助转让结果
	 */
	static byte[] lock1 =  new byte[0]; 
	public void transferResult(HttpServletRequest request) {
		synchronized(lock1){
			String tip = null;
			//if (session.getAttribute("transferResult") == null) {
			//	result = "userError";
			//	tip = "参数错误";
			//	request.setAttribute("result", result);
			//	request.setAttribute("tip", tip);
			//	return;
			//}
			//session.removeAttribute("transferResult");
			int tongId = StringUtil.toInt(request.getParameter("tongId"));
			if(tongId<=0){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			TongTransferBean tongTransfer = getTransferTongId(tongId);
			if(tongTransfer==null){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			//原来该帮帮主ID
			int tongUserId = tongTransfer.getTongUserId();
			if(tongUserId<=0){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			UserBean tongUser = UserInfoUtil.getUser(tongUserId);
			if(tongUser==null){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
			TongBean tong = getTongService().getTong(
					"user_id=" + tongUserId + " and mark=0");
			if(tong==null){
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}

	
			//if (tong != null) {
			if (tongTransfer.getMark()==TongTransferBean.TONG_USER) {
				//UserStatusBean status = (UserStatusBean) UserInfoUtil
				//		.getUserStatus(loginUser.getId());
				//if (status.getGamePoint() < 100000) {
				//	request.setAttribute("tip", "对不起您的乐币不足10万，您不能转让帮会。");
				//	request.setAttribute("result", "userError");
				//	return;
				//}
				// liuyi 2007-01-16 帮会修改 start
				//String user = request.getParameter("user");
				int userId = tongTransfer.getReceiveUserId();
				if (userId < 1) {
					request.setAttribute("tip", "该用户不存在。");
					request.setAttribute("result", "userError");
					return;
				}
				UserStatusBean userStatusBean = UserInfoUtil
						.getUserStatus(userId);
				if (userStatusBean == null) {
					request.setAttribute("tip", "该用户不存在。");
					request.setAttribute("result", "userError");
					return;
				}
				if (userStatusBean.getTong() != tong.getId()
						|| userStatusBean.getRank() <= TongAction.MIN_RANK_FOR_TONG
						|| userStatusBean.getGamePoint() < TongAction.MIN_MONEY_FOR_TONG
						|| userStatusBean.getSocial() <= TongAction.MIN_SOCIAL_FOR_TONG) {
					request.setAttribute("tip", "该用户还不满足帮主的条件。");
					request.setAttribute("result", "userError");
					return;
				}
				// liuyi 2007-01-16 帮会修改 end
				TongCacheUtil.updateTong("user_id=" + userId, "id="
						+ tong.getId(), tong.getId());
				TongCacheUtil.updateTongUser("mark=0", "user_id="
						+ tongUser.getId(), tong.getId(), tongUser.getId());
				TongCacheUtil.updateTongUser("mark=2", "user_id=" + userId, tong
						.getId(), userId);
				if (tong.getUserIdA() == userId)
					TongCacheUtil.updateTong("user_id_a=-1", "id="
							+ tong.getId(), tong.getId());
				else if (tong.getUserIdB() == userId)
					TongCacheUtil.updateTong("user_id_b=-1", "id="
							+ tong.getId(), tong.getId());
				UserInfoUtil.updateUserStatus("point=point+100", "user_id="
						+ userId, userId, UserCashAction.OTHERS,
						"帮会转让新帮主获取100经验");
	
				// 给帮众发消息
				Calendar cal = Calendar.getInstance();
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int month = cal.get(Calendar.MONTH) + 1;
				int year = cal.get(Calendar.YEAR);
				UserBean userBean = (UserBean) UserInfoUtil.getUser(userId);
				String content = "由于压力过大，帮主"
						+ StringUtil.toWml(tongUser.getNickName()) + "于"
						+ year + "年" + month + "月" + day + "日将"
						+ StringUtil.toWml(tong.getTitle()) + "转让给"
						+ StringUtil.toWml(userBean.getNickName())
						+ "内阁暂时不变，钦此";
				// noticeContentMap.put(tong.getId() + "_3", content);
				// noticeUserMap.put(tong.getId() + "_3", userList);
				// noticeTitleMap.put(tong.getId() + "_3", "帮主转让");
	
				// 系统消息
				NoticeBean sysnotice = new NoticeBean();
				sysnotice.setUserId(0);
				sysnotice.setTitle(content);
				sysnotice.setContent("");
				sysnotice.setHideUrl("");
				sysnotice.setType(NoticeBean.SYSTEM_NOTICE);
				sysnotice.setLink("/tong/tong.jsp?tongId=" + tong.getId());
				sysnotice.setTongId(tong.getId());
				NoticeUtil.getNoticeService().addNotice(sysnotice);
				OsCacheUtil.flushGroup(OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
				// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
				tip = "帮主转让已成功！（3秒钟跳转到城市首页）";
				request.setAttribute("tip", tip);
				// 转让记录
				TongManageLogBean log = new TongManageLogBean();
				log.setUserId(tongUser.getId());
				log.setReceiveUserId(userId);
				log.setTongId(tong.getId());
				log.setMark(1);
				getTongService().addTongManageLog(log);
				// // 被转让记录
				// log = new TongManageLogBean();
				// log.setUserId(StringUtil.toInt(user));
				// log.setTongId(tong.getId());
				// log.setMark(5);
				//
				// getTongService().addTongManageLog(log);
				ForumBean forumBean = ForumCacheUtil.getForumCacheBean(tong
						.getId());
				String myid = null;
				if (forumBean != null) {
					{
						if (tong.getUserIdA() == userBean.getId())
							myid = userBean.getId() + "_" + tong.getUserIdB();
	
						else if (tong.getUserIdB() == userBean.getId())
							myid = userBean.getId() + "_" + tong.getUserIdA();
						else
							myid = userBean.getId() + "_" + tong.getUserIdA()
									+ "_" + tong.getUserIdB();
						myid = myid.replace("_-1", "");
						getForumService().updateForum("user_id='" + myid + "'",
								"tong_id=" + tong.getId());
						ForumCacheUtil.deleteTongForum(tong.getId());
						ForumCacheUtil.deleteTongForumList();
					}
				}
				request.setAttribute("tongId", tong.getId() + "");
				request.setAttribute("result", "success");
				return;
			} else if (tongTransfer.getMark()==TongTransferBean.TONG_OTHER_USER) {
				//UserStatusBean status = (UserStatusBean) UserInfoUtil
				//		.getUserStatus(loginUser.getId());
				//if (status.getGamePoint() < 100000) {
				//	request.setAttribute("tip", "对不起您的乐币不足10万，您不能转让帮会。");
				//	request.setAttribute("result", "userError");
				//	return;
				//}
				//String host = request.getParameter("host");
				int host =tongTransfer.getReceiveUserId();
				TongBean tonghost = getTongService().getTong(
						"user_id=" + host + " and mark=0");
				if (tonghost != null) {
					request.setAttribute("tongId", tonghost.getId() + "");
					TongCacheUtil
							.updateTong(
									"fund=0,fund_total=0,user_id=-1,user_id_a=-1,user_id_b=-1,mark=1,user_count=0,cadre_count=0,rate=0,title='荒城'",
									"id=" + tong.getId(), tong.getId());
					TongCacheUtil.updateTong("fund=fund+" + tong.getFund()
							+ ",fund_total=fund_total+" + tong.getFundTotal()
							+ ",user_count=user_count+" + tong.getUserCount(),
							"id=" + tonghost.getId(), tonghost.getId());
					getForumService().updateForum("user_id=''",
							"tong_id=" + tong.getId());
					ForumCacheUtil.deleteTongForum(tong.getId());
					ForumCacheUtil.deleteTongForumList();
					List users = TongCacheUtil
							.getTongUserListById(tong.getId());
					
					tongService.updateTongUser("mark=0,tong_id="
							+ tonghost.getId(), "tong_id=" + tong.getId());
					
					if (users != null) {
						String sql = "update jc_online_user set tong_id="
								+ tonghost.getId() + " where tong_id="
								+ tong.getId();
						SqlUtil.executeUpdate(sql);
						for(int i = 0;i < users.size();i++) {
							Integer iid = (Integer)users.get(i);
							UserInfoUtil.updateUserTong(iid.intValue(), tonghost.getId());
							TongCacheUtil.flushTongUser(0, iid.intValue());
						}
						// liuyi 2007-01-16 帮会修改 end
					}
					UserInfoUtil.updateUserStatus("point=point+100", "user_id="
							+ host, host,
							UserCashAction.OTHERS, "帮会转让新帮主获取100经验");
					// 给帮众发消息
	
					Calendar cal = Calendar.getInstance();
					int day = cal.get(Calendar.DAY_OF_MONTH);
					int month = cal.get(Calendar.MONTH) + 1;
					int year = cal.get(Calendar.YEAR);
					UserBean userBean = (UserBean) UserInfoUtil
							.getUser(host);
					String content = "由于压力过大，帮主"
							+ StringUtil.toWml(tongUser.getNickName()) + "于"
							+ year + "年" + month + "月" + day + "日将"
							+ tong.getTitle() + "转让给" + tonghost.getTitle()
							+ "帮主" + StringUtil.toWml(userBean.getNickName())
							+ ", 自此" + StringUtil.toWml(tong.getTitle()) + "并入"
							+ StringUtil.toWml(tonghost.getTitle())
							+ "，听从新帮主领导，原内阁解散。钦此！";
					// noticeContentMap.put(tong.getId() + "_4", content);
					// noticeUserMap.put(tong.getId() + "_4", users);
					// noticeTitleMap.put(tong.getId() + "_4", "帮主转让");
	
					// 系统消息
					NoticeBean sysnotice = new NoticeBean();
					sysnotice.setUserId(0);
					sysnotice.setTitle(content);
					sysnotice.setContent("");
					sysnotice.setHideUrl("");
					sysnotice.setType(NoticeBean.SYSTEM_NOTICE);
					sysnotice.setLink("/tong/tong.jsp?tongId=" + tong.getId());
					sysnotice.setTongId(tong.getId());
					NoticeUtil.getNoticeService().addNotice(sysnotice);
					OsCacheUtil.flushGroup(
							OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP, tong
									.getId()
									+ "_isOnline");
					OsCacheUtil.flushGroup(
							OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP, tonghost
									.getId()
									+ "_isOnline");
					OsCacheUtil
							.flushGroup(OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
					// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
					TongCacheUtil.flushTongUserAll();
					OsCacheUtil
							.flushGroup(OsCacheUtil.TONG_USER_ID_LIST_CACHE_GROUP);
					// 转让记录
					TongManageLogBean log = new TongManageLogBean();
					log.setUserId(tongUser.getId());
					log.setReceiveUserId(host);
					log.setTongId(tong.getId());
					log.setMark(1);
					getTongService().addTongManageLog(log);
					// // 被转让记录
					// log = new TongManageLogBean();
					// log.setUserId(StringUtil.toInt(host));
					// log.setTongId(tong.getId());
					// log.setMark(2);
					// getTongService().addTongManageLog(log);
					// 删除申请记录
					deleteApplyList(tong.getId());
					tip = "帮主转让已成功！您的帮会会员并入"
							+ StringUtil.toWml(tonghost.getTitle())
							+ "，城市变为荒城（3秒钟跳转到城市首页）";
					getForumService().updateForum(
							"title='荒城',description='',user_id=''",
							"tong_id=" + tong.getId());
					ForumCacheUtil.deleteTongForum(tong.getId());
					ForumCacheUtil.deleteTongForumList();
					request.setAttribute("tip", tip);
					request.setAttribute("result", "success");
					return;
				}
			}else{
				request.setAttribute("tip", "参数错误");
				request.setAttribute("result", "userError");
				return;
			}
		//}
		}
	}

	/**
	 * 任命助手
	 */
	public void nominateAssistant(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		request.setAttribute("tong", tong);
		session.setAttribute("nominateResult", "ok");
		// macq_获取帮会高层人员列表_2007-1-24_start
		if (tong != null) {
			String sql = "select user_id from jc_tong_user where mark=3 and tong_id="
					+ tong.getId();
			List tongUserList = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (tongUserList == null) {
				tongUserList = new ArrayList();
			}
			request.setAttribute("tongUserList", tongUserList);
		}
		// macq_获取帮会高层人员列表_2007-1-24_end
		session.setAttribute("nominateAssistant", "ture");
		return;
	}

	/**
	 * 负帮主候选人列表
	 */
	public void assistantList(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		String result = null;
		String tip = null;
		request.setAttribute("tong", tong);
		if (tong != null && tong.getUserIdA() > -1 && tong.getUserIdB() > -1) {
			result = "failure";
			tip = "对不起，你已经有两名帮主了，不能增加副帮主了！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tong != null) {
			int totalCount = getTongService().getTongAssistantCount(
					tong.getId());
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}

			String prefixUrl = "assistantList.jsp?tongId=" + tong.getId();

			// 取得要显示的消息列表
			int start = pageIndex * NUMBER_PER_PAGE;
			int end = NUMBER_PER_PAGE;
			Vector tongList = getTongService().getTongAssistantList(
					tong.getId(), start, end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongList", tongList);
			result = "success";
			request.setAttribute("result", result);
			session.setAttribute("nominateResult", "ok");
		}

	}

	/**
	 * 任命助手结果
	 */
	public void nominateResult(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongOne(tongId);
		String result = null;
		String tip = null;
		if (tong != null)
			request.setAttribute("tongId", tong.getId() + "");
		if (session.getAttribute("nominateResult") == null) {
			result = "userError";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("nominateResult");
		if (tong != null) {
			if (request.getParameter("add") != null) {
				String assistantUserId = request.getParameter("add");
				// liuyi 2007-01-19 任命副帮主判断 start
				UserStatusBean status = UserInfoUtil.getUserStatus(StringUtil
						.toInt(assistantUserId));
				if (status == null || status.getTong() != tong.getId()) {
					result = "failure";
					tip = "对不起，该用户不是本帮会成员！（3秒钟跳转）";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
				// liuyi 2007-01-19 任命副帮主判断 end
				if ((tong.getUserIdA() > -1 && tong.getUserIdB() > -1)) {
					result = "failure";
					tip = "对不起，你已经有两名帮主了，不能增加副帮主了！（3秒钟跳转）";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				} else if ((tong.getUserIdA() == -1)) {
					TongUserBean user = getTongService()
							.getTongUser(
									"user_id=" + assistantUserId
											+ " and mark=0 and tong_id="
											+ tong.getId());
					if (user != null) {
						if (TongCacheUtil.updateTongUser("mark=1", "user_id="
								+ assistantUserId, tong.getId(), StringUtil
								.toInt(assistantUserId))) {
							TongCacheUtil.updateTong("user_id_a="
									+ assistantUserId, "id=" + tong.getId(),
									tong.getId());
							UserBean bean = (UserBean) UserInfoUtil
									.getUser(StringUtil.toInt(assistantUserId));
							if (bean != null) {
								result = "success";
								request.setAttribute("result", result);

								NoticeBean notice = new NoticeBean();
								notice.setUserId(StringUtil
										.toInt(assistantUserId));

								notice.setTitle("任命副帮主");
								notice.setContent("由于劳苦功高，你刚刚被帮主"
										+ StringUtil.toWml(loginUser
												.getNickName()) + "任命为副帮主");
								notice.setHideUrl("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setLink("");
								NoticeUtil.getNoticeService().addNotice(notice);

								tip = "你已经任命"
										+ StringUtil.toWml(bean.getNickName())
										+ "为副帮主！（3秒钟跳转）";
								ForumBean forumBean = ForumCacheUtil
										.getForumCacheBean(tong.getId());
								String myid = null;
								if (forumBean != null) {

									myid = forumBean.getUserId() + "_"
											+ bean.getId();
									getForumService().updateForum(
											"user_id='" + myid + "'",
											"tong_id=" + tong.getId());
									ForumCacheUtil
											.deleteTongForum(tong.getId());
									ForumCacheUtil.deleteTongForumList();
								}
								request.setAttribute("tip", tip);
							}

						}
					}

				} else if ((tong.getUserIdB() == -1)) {
					TongUserBean user = getTongService()
							.getTongUser(
									"user_id=" + assistantUserId
											+ " and mark=0 and tong_id="
											+ tong.getId());
					if (user != null) {
						if (TongCacheUtil.updateTongUser("mark=1", "user_id="
								+ assistantUserId, tong.getId(), StringUtil
								.toInt(assistantUserId))) {
							TongCacheUtil.updateTong("user_id_b="
									+ assistantUserId, "id=" + tong.getId(),
									tong.getId());

							UserBean bean = (UserBean) UserInfoUtil
									.getUser(StringUtil.toInt(assistantUserId));
							if (bean != null) {
								result = "success";
								request.setAttribute("result", result);

								NoticeBean notice = new NoticeBean();
								notice.setUserId(StringUtil
										.toInt(assistantUserId));

								notice.setTitle("任命副帮主");
								notice.setContent("由于劳苦功高，你刚刚被帮主"
										+ StringUtil.toWml(loginUser
												.getNickName()) + "任命为副帮主");
								notice.setHideUrl("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setLink("");
								NoticeUtil.getNoticeService().addNotice(notice);

								tip = "你已经任命"
										+ StringUtil.toWml(bean.getNickName())
										+ "为副帮主！（3秒钟跳转）";
								ForumBean forumBean = ForumCacheUtil
										.getForumCacheBean(tong.getId());
								String myid = null;
								if (forumBean != null) {

									myid = forumBean.getUserId() + "_"
											+ bean.getId();
									getForumService().updateForum(
											"user_id='" + myid + "'",
											"tong_id=" + tong.getId());
									ForumCacheUtil
											.deleteTongForum(tong.getId());
									ForumCacheUtil.deleteTongForumList();
								}
								request.setAttribute("tip", tip);
							}

						}
					}
				}
			} else if (request.getParameter("del") != null) {
				String del = request.getParameter("del");
				TongUserBean user = getTongService().getTongUser(
						"user_id=" + del + " and mark=1 and tong_id="
								+ tong.getId());
				if (user != null) {
					if (tong.getUserIdB() == StringUtil.toInt(del)) {
						if (TongCacheUtil.updateTongUser("mark=0", "user_id="
								+ del, tong.getId(), StringUtil.toInt(del))) {
							TongCacheUtil.updateTong("user_id_b=-1", "id="
									+ tong.getId(), tong.getId());
							UserBean bean = (UserBean) UserInfoUtil
									.getUser(StringUtil.toInt(del));
							if (bean != null) {
								result = "success";
								NoticeBean notice = new NoticeBean();
								notice.setUserId(tong.getUserIdA());

								notice.setTitle("废除副帮主");
								notice.setContent("由于玩忽职守，你刚刚被帮主"
										+ StringUtil.toWml(loginUser
												.getNickName()) + "废除");
								notice.setHideUrl("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setLink("");
								NoticeUtil.getNoticeService().addNotice(notice);
								request.setAttribute("result", result);
								tip = "你已经废除"
										+ StringUtil.toWml(bean.getNickName())
										+ "副帮主的身份！（3秒钟跳转）";
								ForumBean forumBean = ForumCacheUtil
										.getForumCacheBean(tong.getId());
								String myid = null;
								if (forumBean != null) {

									myid = forumBean.getUserId().replace(
											"_" + bean.getId(), "");
									getForumService().updateForum(
											"user_id='" + myid + "'",
											"tong_id=" + tong.getId());
									ForumCacheUtil
											.deleteTongForum(tong.getId());
									ForumCacheUtil.deleteTongForumList();

								}
								request.setAttribute("tip", tip);
							}
						}

					} else if (tong.getUserIdA() == StringUtil.toInt(del)) {
						if (TongCacheUtil.updateTongUser("mark=0", "user_id="
								+ del, tong.getId(), StringUtil.toInt(del))) {
							TongCacheUtil.updateTong("user_id_a=-1", "id="
									+ tong.getId(), tong.getId());
							UserBean bean = (UserBean) UserInfoUtil
									.getUser(StringUtil.toInt(del));
							if (bean != null) {
								result = "success";
								NoticeBean notice = new NoticeBean();
								notice.setUserId(tong.getUserIdA());

								notice.setTitle("废除副帮主");
								notice.setContent("由于玩忽职守，你刚刚被帮主"
										+ StringUtil.toWml(loginUser
												.getNickName()) + "废除");
								notice.setHideUrl("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setLink("");
								NoticeUtil.getNoticeService().addNotice(notice);
								request.setAttribute("result", result);
								tip = "你已经废除"
										+ StringUtil.toWml(bean.getNickName())
										+ "副帮主的身份！（3秒钟跳转）";
								ForumBean forumBean = ForumCacheUtil
										.getForumCacheBean(tong.getId());
								String myid = null;
								if (forumBean != null) {

									myid = forumBean.getUserId().replace(
											"_" + bean.getId(), "");
									getForumService().updateForum(
											"user_id='" + myid + "'",
											"tong_id=" + tong.getId());
									ForumCacheUtil
											.deleteTongForum(tong.getId());
									ForumCacheUtil.deleteTongForumList();

								}
								request.setAttribute("tip", tip);
							}

						}

					} else {
						tip = "此人不是副帮主，无需废除 ！";
						result = "failure";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
					}

				}
			}

		}

	}

	/**
	 * 会员管理
	 */
	public void memberManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		int count = 0;
		if (tong != null) {
			count = getTongService().getTongApplyCount(
					"tong_id=" + tong.getId() + " and mark=0");
		}
		request.setAttribute("count", count + "");
	}

	/**
	 * 税收管理
	 */
	public void revenueManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		// 会员捐款
		long donation = 0;
		TongTitleRateLogBean bean = null;
		Vector revenueList = null;
		int revenue = 0;
		if (tong != null) {
			donation = getTongService().getTongDonationCount(tong.getId());
			revenueList = getTongService().getTongTitleRateLogList(

					"tong_id=" + tong.getId()
							+ " and to_days(now())-to_days(create_datetime)=1");
			if (revenueList != null) {
				for (int j = 0; j < revenueList.size(); j++) {
					bean = (TongTitleRateLogBean) revenueList.get(j);
					if (bean != null)
						revenue = revenue + bean.getRateMoney();
				}
			}
			request.setAttribute("revenue", revenue + "");
			request.setAttribute("donation", donation + "");
			session.setAttribute("revenueResult", "ok");
		}

	}

	/**
	 * 修改税务结果
	 */
	public void revenueResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("revenueResult") == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("revenueResult");
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		if (tong != null) {
			int revenue = StringUtil.toInt(request.getParameter("revenue"));
			if (revenue > 0 && revenue < 101) {
				TongCacheUtil.updateTong("rate=" + revenue, "id="
						+ tong.getId(), tong.getId());
				result = "success";
				tip = "您的税收已经改为" + revenue + "％！（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
			} else {
				result = "failure";
				tip = "请重新输入税收(1~100)（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);

			}
		} else {
			result = "failure";
			tip = "您无权修改税收！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
		}
	}

	/**
	 * 取走资金
	 */
	public void fundManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		// 取走的累积资金
		long count = 0;
		if (tong != null) {
			request.setAttribute("tong", tong);
			count = getTongService().getTongFundCount(tong.getId(),
					loginUser.getId());
			request.setAttribute("count", count + "");
			session.setAttribute("fundmanage", "ok");

		}

	}

	/**
	 * 取资金结果
	 */
	public void fundResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("fundmanage") == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("fundmanage");
		int tongId = StringUtil.toInt(request.getParameter("tongId"));

		synchronized(LockUtil.tongLock.getLock(tongId)) {
			TongBean tong = getTongTwo(tongId);
	
			if (tong != null) {
				request.setAttribute("tong", tong);
				int fund = StringUtil.toInt(request.getParameter("fund"));
				if (fund > 0 && fund <= tong.getFund()) {
					if (UserInfoUtil
							.updateUserCash(loginUser.getId(), fund, 14,
									"取走帮会资金" + fund + "乐币")) {
						TongCacheUtil.updateTong("fund=fund-" + fund, "id="
								+ tong.getId(), tong.getId());
						TongFundBean bean = new TongFundBean();
						bean.setUserId(loginUser.getId());
						bean.setTongId(tong.getId());
						bean.setMoney(fund);
						bean.setMark(0);
						TongCacheUtil.addTongFund(bean, "id");
						result = "success";
						tip = "你已经取走了" + fund + "乐币！要用这笔钱为帮会做贡献哦！（3秒钟跳转）";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
					}
				} else {
					result = "failure";
					tip = "帮会资金不足或者输入有错！（3秒钟跳转）";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
	
				}
			} else {
				result = "failure";
				tip = "您无权取走资金！（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
			}
		}
	}

	/**
	 * liuyi 2007-01-16 新人奖金管理
	 * 
	 * @param request
	 */
	public void rewardResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("reward") == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		if (tong != null) {
			int reward = StringUtil.toInt(request.getParameter("reward"));
			if (reward >= 0 && reward <= 100000000) {
				TongCacheUtil.updateTong("reward=" + reward, "id="
						+ tong.getId(), tong.getId());
				result = "success";
				tip = "您的新人奖金已经改为" + reward + "！（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);

				session.removeAttribute("reward");
			} else {
				result = "failure";
				tip = "只能是0到1亿,请重新输入新人奖金（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);

			}
		} else {
			result = "failure";
			tip = "您无权修改新人奖金！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
		}
	}

	/**
	 * liuyi 2007-01-16 帮会公告管理
	 * 
	 * @param request
	 */
	public void descResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("desc") == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		if (tong != null) {
			String desc = request.getParameter("desc");
			if (desc != null && desc.length() > 0 && desc.length() <= 200) {
				TongCacheUtil.updateTong("description='" + StringUtil.toSql(desc) + "'", "id="
						+ tong.getId(), tong.getId());
				result = "success";
				tip = "您的帮会公告已经改为" + desc + "！（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);

				session.removeAttribute("desc");
			} else {
				result = "failure";
				tip = "请重新输入帮会公告（3秒钟跳转）";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);

			}
		} else {
			result = "failure";
			tip = "您无权修改帮会公告！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
		}
	}

	/**
	 * 消息管理
	 */
	public void noticeManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		session.setAttribute("noticesendgroup", "ok");
		request.setAttribute("tong", tong);

	}

	/**
	 * 
	 * @author guip
	 * @explain：帮会昵称修改
	 * @datetime:2007-7-30 16:17:37
	 * @param request
	 * @return void
	 */
	public void reNameManage(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);

	}

	/**
	 * 
	 * @author guip
	 * @explain：帮会昵称修改结果处理
	 * @datetime:2007-7-30 17:09:42
	 * @param request
	 * @return void
	 */
	public void reNameResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		String shortName = request.getParameter("name");
		if(shortName != null)
			shortName = StringUtil.noEnter(shortName);
		TongBean tong = getTongTwo(tongId);
		if (tong == null) {
			result = "failure";
			tip = "您无权修改昵称！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		if (shortName == null || shortName.length() > 2) {
			result = "failure";
			tip = "不能为空或字数不能超过2个字";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongCacheUtil.updateTong("short_title=" + "'" + StringUtil.toSql(shortName) + "'", "id="
				+ tongId);
		result = "success";
		tip = "帮会昵称修改成功（3秒钟跳转）";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
	}

	/**
	 * 消息管理结果
	 */
	public void noticeResult(HttpServletRequest request) {

		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = getTongTwo(tongId);
		request.setAttribute("tong", tong);
		if (session.getAttribute("noticesendgroup") == null) {
			result = "refrush";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		session.removeAttribute("noticesendgroup");

		if (tong != null) {
			// 判断是否刷新

			// 判断群发时间(限制1小时发送一次)
			if (session.getAttribute("noticesendgroupTime") != null) {
				long groupSendMsg = StringUtil.toLong((String) session
						.getAttribute("noticesendgroupTime"));
				if (groupSendMsg == -1) {
					result = "failure";
					tip = "参数错误";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
				if (System.currentTimeMillis() - groupSendMsg < GROUP_SEND_MESSAGE_TIME) {
					result = "timeError";
					tip = "此功能每小时才可使用一次";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
			}
			// 获取消息内容
			String notice = getParameterNoCtrl("notice");
			if (notice == null || notice.equals("")) {
				result = "failure";
				tip = "发言内容不能为空";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// 判断脏话
			Vector contentlist = ContentList.getContentList();
			if (contentlist != null) {
				String conName = null;
				for (int i = 0; i < contentlist.size(); i++) {
					conName = (String) contentlist.get(i);
					if (notice.contains(conName)) {
						result = "failure";
						tip = "请注意您的发言内容";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						return;
					}
				}
			}
			UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					.getId());
			long sendGamePoint = 100 * tong.getUserCount();
			if (userStatus.getGamePoint() < sendGamePoint) {
				result = "failure";
				tip = "对不起,您的乐币不够!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			if (UserInfoUtil.updateUserCash(loginUser.getId(), -sendGamePoint,
					UserCashAction.OTHERS, "发帮会通知扣钱" + sendGamePoint)) {
				// List userList =
				// TongCacheUtil.getTongUserListById(tong.getId());
				// noticeContentMap.put(tong.getId() + "_5", notice);
				// noticeUserMap.put(tong.getId() + "_5", userList);
				// noticeTitleMap.put(tong.getId() + "_5", "帮会通知");

				// 系统消息
				NoticeBean sysnotice = new NoticeBean();
				sysnotice.setUserId(0);
				sysnotice.setTitle(notice);
				sysnotice.setContent("");
				sysnotice.setHideUrl("");
				sysnotice.setType(NoticeBean.SYSTEM_NOTICE);
				sysnotice.setLink("/tong/tong.jsp?tongId=" + tong.getId());
				sysnotice.setTongId(tong.getId());
				// macq_2007-5-16_增加帮会消息类型_start
				sysnotice.setMark(NoticeBean.TONG);
				// macq_2007-5-16_增加帮会消息类型_end
				NoticeUtil.getNoticeService().addNotice(sysnotice);
				OsCacheUtil.flushGroup(OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP);
				// OsCacheUtil.flushGroup(OsCacheUtil.SYSTEM_NOTICE_READED_GROUP);
				result = "success";
				tip = "帮会通知发送成功！共发" + tong.getUserCount() + "条！（3秒钟跳转）";
				session.setAttribute("noticesendgroupTime", System
						.currentTimeMillis()
						+ "");
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
			}
		} else {
			result = "failure";
			tip = "您无权发帮会通知！（3秒钟跳转）";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
		}

	}

	// 帮主和副帮主都能操作的
	public TongBean getTongTwo(int tongId) {
		TongBean tong = TongCacheUtil.getTong(tongId);

		if (tong != null && tong.getMark() == 0) {
			if (tong.getUserId() == loginUser.getId()) {

				return tong;
			} else if (tong.getUserIdA() == loginUser.getId()) {

				return tong;
			} else if (tong.getUserIdB() == loginUser.getId()) {

				return tong;
			}
			return null;
		}
		return null;

	}

	// 帮主能操作的
	public TongBean getTongOne(int tongId) {
		TongBean tong = TongCacheUtil.getTong(tongId);

		if (tong != null && tong.getMark() == 0) {
			if (tong.getUserId() == loginUser.getId()) {

				return tong;

			}
			return null;
		}
		return null;
	}

	// 荒城
	public TongBean getTong(int tongId, int mark) {
		TongBean tong = getTongService().getTong(
				"id=" + tongId + " and mark=" + mark);
		return tong;
	}

	/**
	 * 买荒城页面
	 */
	public void buyBarren(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		request.setAttribute("tongId", tongId + "");
		String result = null;
		String tip = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() > 0) {
			TongBean tong = getTong(us.getTong(), 0);

			result = "failure";
			if (tong != null)
				tip = "对不起，您是" + StringUtil.toWml(tong.getTitle())
						+ "帮成员，不能申请购买这座荒城!（3秒钟跳转到城市首页）";
			else
				tip = "对不起，您是某帮成员，不能申请购买这座荒城!（3秒钟跳转到城市首页）";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (us.getTong() == -1) {
			result = "failure";
			tip = "对不起，您是已经申请了某帮，不能申请购买这座荒城!（3秒钟跳转到城市首页）";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		TongManageLogBean log = getTongService()
				.getTongManageLog(
						"user_id="
								+ loginUser.getId()
								+ " and mark=4 and to_days(now())-to_days(create_datetime)<30");
		if (log != null) {
			tip = "解散帮会后一个月内不能再创建帮会！（3秒钟跳转到城市首页）";
			result = "failure";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
        // 资格不够
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		if (status.getGamePoint() < MIN_MONEY_FOR_TONG
				|| status.getRank() <= MIN_RANK_FOR_TONG
				|| status.getSocial() < MIN_SOCIAL_FOR_TONG) {
			tip = "您的资格不够创建帮会！（3秒钟跳转到城市首页）";
			result = "failure";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = getTong(tongId, 1);
		request.setAttribute("tong", tong);
		request.setAttribute("money", status.getGamePoint() + "");
		StoreBean store = getBankService().getStore(
				"user_id=" + loginUser.getId());
		if (store != null) {
			request.setAttribute("bank", store.getMoney() + "");
		} else {
			request.setAttribute("bank", "0");
		}

	}

	/**
	 * 买荒城结果页面
	 */
	public void buyBarrenResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());// 用户状态信息
		if (userStatus.getGamePoint() < BARREN) {
			result = "failure";
			tip = "对不起，您的乐币数不足以买下该城，看看是不是去银行取点钱或找朋友借点钱。";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("tongId", tongId + "");
			return;

		}

		TongBean tong = getTong(tongId, 1);
		if (tong != null) {
			if (UserInfoUtil.updateUserStatus("tong=" + tong.getId()
					+ ",game_point=game_point-" + BARREN, "user_id="
					+ loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "用户买荒城花费" + BARREN))// 更新用户乐币))
			{
				// liuyi 2007-01-19 购买荒城bug修改 start
				TongUserBean bean = new TongUserBean();
				bean.setTongId(tong.getId());
				bean.setUserId(loginUser.getId());
				bean.setMark(2);
				TongCacheUtil.addTongUser(bean);
				// liuyi 2007-01-19 购买荒城bug修改 end
				TongCacheUtil.updateTong("mark=0,user_id=" + loginUser.getId(),
						"id=" + tong.getId(), tong.getId());
				getForumService().updateForum(
						"user_id='" + loginUser.getId() + "'",
						"tong_id=" + tong.getId());
				ForumCacheUtil.deleteTongForum(tong.getId());
				ForumCacheUtil.deleteTongForumList();

				result = "success";
				tip = "购买成功！请安心等待！（3秒钟跳转到城市首页）";
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				request.setAttribute("tongId", tongId + "");
			}

		}

	}

	/**
	 * 
	 * @author macq
	 * @explain : 按在线用户顺序排名帮会成员
	 * @datetime:2007-1-2 下午03:09:31
	 * @param tongId
	 * @return Vector
	 */
	public List getOnlineTongUserList(int tongId) {
		String key = tongId + "";
		// 从缓存中取
		List onlineUser = (List) OsCacheUtil.get(key,
				OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP,
				OsCacheUtil.TONG_USER_ONLINE_FLUSH_PERIOD);
		// 缓存中没有
		if (onlineUser == null) {
			onlineUser = new ArrayList();
			List offline = new ArrayList();
			// 从数据库中取
			List tongUserList = TongCacheUtil.getTongUserListById(tongId);
			// 为空
			if (tongUserList == null) {
				tongUserList = new ArrayList();
			}
			Integer userId = null;
			boolean flag = false;
			for (int i = 0; i < tongUserList.size(); i++) {
				userId = (Integer) tongUserList.get(i);
				flag = OnlineUtil.isOnline(userId.toString());
				if (flag) {
					onlineUser.add(userId);
				} else {
					offline.add(userId);
				}
			}
			onlineUser.addAll(offline);
			// 放到缓存中
			OsCacheUtil.put(key, onlineUser,
					OsCacheUtil.TONG_USER_ONLINE_CACHE_GROUP);
		}
		return onlineUser;
	}

	/**
	 * liuyi 2007-01-25 城墙沦陷记录
	 * 
	 * @param request
	 */
	public void tongDestoryHistory(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		List tongList = TongCacheUtil.getTongDestroyHistoryIdList(tongId);
		int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongDestroyHistory.jsp?tongId=" + tongId;
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(tongList.size()));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongDestroyList", tongList1);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会信息
	 * @datetime:2007-1-2 下午04:09:26
	 * @param tongId
	 * @param mark
	 * @return
	 */
	public TongBean getTong(int tongId) {
		TongBean tong = TongCacheUtil.getTong(tongId);
		return tong;
	}

	/**
	 * 帮会解散或者帮会
	 */
	public boolean deleteApplyList(int tongId) {
		Vector tongApplyList = getTongService().getTongApplyList(
				"tong_id=" + tongId + " and mark=0 ");
		if (tongApplyList != null) {
			TongApplyBean tongapply = null;
			UserStatusBean status = null;
			for (int i = 0; i < tongApplyList.size(); i++) {
				tongapply = (TongApplyBean) tongApplyList.get(i);
				if (tongapply != null) {
					status = (UserStatusBean) UserInfoUtil
							.getUserStatus(tongapply.getUserId());
					if (status != null) {
						UserInfoUtil.updateUserTong(tongapply.getUserId(), 0);
					}
				}
			}
			getTongService().delTongApply("tong_id=" + tongId + " and mark=0 ");
			return true;
		}

		return false;

	}

	/**
	 * liuyi 2007-02-10 帮会的联盟列表
	 * 
	 * @param request
	 */
	public void friendTongs(HttpServletRequest request) {
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		List tongList = TongCacheUtil.getFriendTongIds(tongId);
		int totalCount = tongList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "friendTongs.jsp?tongId=" + tongId;
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List tongList1 = tongList.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(tongList.size()));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("tongIdList", tongList1);
		return;
	}

	/**
	 * liuyi 2007-02-10 结盟申请判断
	 * 
	 * @param request
	 */
	public void friendTongApply(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus == null || userStatus.getTong() < 1) {
			result = "failure";
			tip = "您还没加入某个帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int myTongId = userStatus.getTong();
		TongBean myTong = TongCacheUtil.getTong(myTongId);
		if (myTong == null) {
			result = "failure";
			tip = "您的帮会不存在!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		boolean isTongAdmin = (myTong.getUserId() == loginUser.getId()
				|| myTong.getUserIdA() == loginUser.getId() || myTong
				.getUserIdB() == loginUser.getId());
		if (!isTongAdmin) {
			result = "failure";
			tip = "您不是帮主或者副帮主，无权进行结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tongId == myTongId) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		boolean isFriendTong = TongCacheUtil.isFriendTong(tongId, myTongId);
		if (isFriendTong) {
			result = "failure";
			tip = "你们已经是盟友，不需要再结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		return;
	}

	public void friendTongApplyResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus == null || userStatus.getTong() < 1) {
			result = "failure";
			tip = "您还没加入某个帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int myTongId = userStatus.getTong();
		TongBean myTong = TongCacheUtil.getTong(myTongId);
		if (myTong == null) {
			result = "failure";
			tip = "您的帮会不存在!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (tongId == myTongId) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("myTong", myTong);
		boolean isTongAdmin = (myTong.getUserId() == loginUser.getId()
				|| myTong.getUserIdA() == loginUser.getId() || myTong
				.getUserIdB() == loginUser.getId());
		if (!isTongAdmin) {
			result = "failure";
			tip = "您不是帮主或者副帮主，无权进行结盟!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (myTong.getFund() < TongAction.FRIEND_TONG_FEE) {
			result = "failure";
			tip = "对不起，贵帮基金不足以支付结盟手续费(" + TongAction.FRIEND_TONG_FEE + "乐币)！";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		//TongFriendBean bean = service.getTongFriend(condition);
		TongFriendBean bean = getAllyTongId(tongId);
		if (bean == null) {
			//bean = new TongFriendBean();
			//bean.setTongId(Math.min(tongId, myTongId));
			//bean.setFTongId(Math.max(tongId, myTongId));
			//bean.setMark(0);
			//service.addTongFriend(bean);
			TongCacheUtil.updateTong("fund=fund-" + TongAction.FRIEND_TONG_FEE,
					"id=" + myTongId);
			OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_TONG_GROUP);
            //加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(myTong.getTitle()+ "向你结盟");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(tong.getUserId());
			notice.setHideUrl("");
			notice.setLink("/tong/fApplyList.jsp?tongId=" +tongId);
			getNoticeService().addNotice(notice);
			putAllyTongId(myTongId,tongId,TongFriendBean.ALLYMARK);
			result = "success" ;
			request.setAttribute("result", result);
		} else {
			//if (bean.getMark() == 0) { // 已经申请
			//	result = "failure";
			//	tip = "对不起，贵帮已经申请跟" + tong.getTitle() + "结盟！";
			//	request.setAttribute("result", result);
			//	request.setAttribute("tip", tip);
			//	return;
			//}
			if (bean.getMark() == 1) { // 已经是盟友
				result = "failure";
				tip = "对不起，贵帮已经同" + tong.getTitle() + "结盟！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			//if (bean.getMark() == 2 || bean.getMark() == 3) { // 已经拒绝或者解除
			//	service.updateTongFriend("mark=0", condition);
			//	TongCacheUtil.updateTong("fund=fund-"
			//			+ TongAction.FRIEND_TONG_FEE, "id=" + myTongId);
			//	OsCacheUtil.flushGroup(OsCacheUtil.FRIEND_TONG_GROUP);
			//	request.setAttribute("result", result);
			//	request.setAttribute("tip", tip);
			//	return;
			//}
		}
	}

	/*
	 * 数字分页
	 * 
	 * @param totalPageCount @param currentPageIndex @param prefixUrl @param
	 * addAnd @param separator @param pageIndex @param response @return
	 */
	public String shuzifenye(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			String pageIndex, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}
		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;" + pageIndex + "=";
		} else {
			prefixUrl += "?" + pageIndex + "=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

	public TongApplyBean getTongApply(int userId, int tongId) {
		TongApplyBean tongApply = getTongService().getTongApply(
				"user_id=" + userId + " and tong_id=" + tongId + " and mark=0");
		return tongApply;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会旗帜日志记录
	 * @datetime:2007-8-1 17:57:22
	 * @param request
	 * @return void
	 */
	public void tongHonorList(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int orderBy = StringUtil.toInt(request.getParameter("orderBy"));
		if (orderBy < 0 || orderBy > 1) {
			orderBy = 0;
		}
		request.setAttribute("orderBy", String.valueOf(orderBy));
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int totalCount = getTongService().getHonorCount(
				"tong_id=" + tong.getId() + " and flag=" + orderBy);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}

		int totalPageCount = totalCount / NUMBER_PER_PAGE;
		if (totalCount % NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "tongHonorList.jsp?tongId=" + tongId
				+ "&amp;orderBy=" + orderBy;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		List honorList = getTongService().getHonorList(
				"tong_id=" + tong.getId() + " and flag=" + orderBy
						+ " order by id desc limit " + start + "," + end);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("honorList", honorList);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * @author macq
	 * @explain：帮会旗帜赠送页面
	 * @datetime:2007-8-1 17:57:37
	 * @param request
	 * @return void
	 */
	public void presentHonor(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (tong.getId() != us.getTong()) {
			result = "tongError";
			tip = "您不是该帮会成员，不能赠送!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		int count = UserBagCacheUtil.getUserBagById(41, loginUser.getId());
		if (count <= 0) {
			result = "tongError";
			tip = "您的行囊里没有旗帜道具,不能赠送!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会旗帜赠
	 * @datetime:2007-8-1 17:57:51
	 * @param request
	 * @return void
	 */
	public void presentHonorRs(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (tong.getId() != us.getTong()) {
			result = "tongError";
			tip = "您不是该帮会成员，不能赠送!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		int userBagId = UserBagCacheUtil.getUserBagById(41, loginUser.getId());
		if (userBagId <= 0) {
			result = "tongError";
			tip = "您的行囊里没有旗帜道具,不能赠送!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		boolean flag = UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),
				userBagId);
		if (flag) {
			// 更新帮会荣誉度
			TongCacheUtil.updateTong("honor=honor+1", "id=" + tong.getId(),
					tong.getId());
			// 增加贡献记录
			getTongService().changeHonor(loginUser.getId(), tong.getId(), 0);
		}
		result = "success";
		tip = "赠送成功!";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：抢帮会旗帜
	 * @datetime:2007-8-2 9:49:15
	 * @param request
	 * @return void
	 */
	public void tongHonorDrop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() <= 0) {
			result = "tongError";
			tip = "您不隶属于任何帮会成员，不能抢旗帜!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tong", tong);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：抢帮会旗帜结果
	 * @datetime:2007-8-2 9:51:42
	 * @param request
	 * @return void
	 */
	static byte[] lock = new byte[0];

	public void tongHonorDropRs(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getTong() <= 0) {
			result = "tongError";
			tip = "您不隶属于任何帮会成员，不能抢旗帜!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		// 同步更新
		synchronized (lock) {
			if (tong.getHonorDrop() == 0) {
				result = "tongError";
				tip = "帮会旗帜已被抢空！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			// 更新帮会丢失旗帜数
			TongCacheUtil.updateTong(" honor_drop=honor_drop-1",
					"id=" + tongId, tongId);
			// 增加抢旗帜记录
			getTongService().changeHonor(loginUser.getId(), tong.getId(), 1);
			// 增加用户行囊记录
			UserBagCacheUtil.addUserBagCache(loginUser.getId(), 41);
		}
		result = "success";
		tip = "抢旗成功!";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		request.setAttribute("tong", tong);
		return;
	}
	
	public static double maxSalary = 28000d * 100000000;	// 工资最高限额
	
	public static void weekSalary(HttpServletRequest request) {
		String sql = "select a.id,a.now_endure,b.develop from jc_tong a,jc_tong_hockshop b where a.mark=0 and a.now_endure>=10000 and a.id=b.tong_id and b.develop>=1000000";
		List list = SqlUtil.getIntsList(sql);
		// 先计算总金额
		double totalSalary = 0;
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			int[] cur = (int[])iter.next();
			long money = (long)cur[1] * (long)(5000 * ((float)(int)(cur[2] / 1000000) / 10 + 1));
			if(money > 100000000000l)
				money = 100000000000l;
			totalSalary += money;
		}
		double rate = 1;
		if(maxSalary < totalSalary)
			rate = maxSalary  / totalSalary;
		if(rate < 0)
			rate = 0.5;
		
		iter = list.iterator();
		while(iter.hasNext()) {
			int[] cur = (int[])iter.next();
			long money = (long)cur[1] * (long)(5000 * ((float)(int)(cur[2] / 1000000) / 10 + 1));
			if(money > 100000000000l)
				money = 100000000000l;
			money = (long)(money * rate / 10000) * 10000;
			TongCacheUtil.updateTong("fund=fund+" + money , "id=" + cur[0], cur[0]);
		}
		request.setAttribute("salary", list);
	}
}
