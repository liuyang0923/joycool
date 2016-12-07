package net.joycool.wap.action.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.job.HappyCardBean;
import net.joycool.wap.bean.job.HappyCardSendBean;
import net.joycool.wap.bean.job.HappyCardStatBean;
import net.joycool.wap.bean.job.HappyCardTypeBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.PageUtil;
import net.joycool.wap.util.SmsUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class HappyCardAction {
	public static int DAY_FRIEND_INVITE_COUNT = 3;// 一天发送给同一好友贺卡次数

	public static int DAY_INVITE_COUNT = 50;// 一天发送贺卡次数

	public static int MONTH_INVITE_COUNT = 150;// 一月发送贺卡次数

	IJobService jobService = ServiceFactory.createJobService();

	/**
	 * /job/happycard/index.jsp 贺卡首页
	 * 
	 * @param request
	 * @param response
	 */
	public void index(HttpServletRequest request, HttpServletResponse response) {
		Vector vecHotCard = jobService
				.getHappyCardList(" 1=1 order by rand() desc limit 0,5 ");
		Vector vecNewCard = jobService
				.getHappyCardList(" 1=1 order by create_datetime desc limit 0,5 ");
		Vector vecCardCategory = jobService.getHappyCardCategoryList("1=1");
		request.setAttribute("vecHotCard", vecHotCard);
		request.setAttribute("vecNewCard", vecNewCard);
		request.setAttribute("vecCardCategory", vecCardCategory);
	}

	public IJobService getJobService() {
		return jobService;
	}

	/**
	 * fanys 2006-09-15 cardList.jsp 贺卡清单页面
	 * 
	 * @param request
	 * @param response
	 */
	public void cardList(HttpServletRequest request,
			HttpServletResponse response) {
		String strOrderWhere = " order by hits desc ";
		int pageIndex = 0;
		int PAGE_SIZE = 5;
		int itemCount = 0;
		int pageCount = 0;

		itemCount = jobService.getHappyCardCount("1=1");
		String pagination = null;
		if (null != request.getParameter("time"))

			strOrderWhere = " order by create_datetime desc ";

		else if (null != request.getParameter("id")) {
			int id = StringUtil.toInt(request.getParameter("id"));

			itemCount = jobService.getHappyCardCount("type_id= " + id);
			strOrderWhere = " and type_id=" + id + strOrderWhere;
			HappyCardTypeBean happycard = jobService.getHappyCardType("id="
					+ id);
			if (happycard != null) {
				String des = happycard.getDescription();
				request.setAttribute("des", des);
			} else {
				request.setAttribute("none", "0");
			}

		} else if (null != request.getParameter("hits")) {
			strOrderWhere = " order by hits desc ";
		}

		if (null != request.getParameter("pageIndex"))
			pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 

		pageCount = PageUtil.getPageCount(PAGE_SIZE, itemCount);
		pageIndex = PageUtil.getPageIndex(pageIndex, pageCount);
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector vecCard = jobService.getHappyCardList(" 1=1 " + strOrderWhere
				+ " limit " + pageIndex * PAGE_SIZE + "," + PAGE_SIZE);
		if (vecCard != null) {
			request.setAttribute("vecCard", vecCard);
		}
		if (null != request.getParameter("id"))
			pagination = PageUtil.shuzifenye(pageCount, pageIndex,
					"cardList.jsp?id=" + request.getParameter("id"), true, "|",
					response);
		else if (null != request.getParameter("time")) {
			pagination = PageUtil.shuzifenye(pageCount, pageIndex,
					"cardList.jsp?time=1", true, "|", response);
		} else if (null != request.getParameter("hits")) {
			pagination = PageUtil.shuzifenye(pageCount, pageIndex,
					"cardList.jsp?hits=1", true, "|", response);
		}
		request.setAttribute("pagination", pagination);
		request.setAttribute("itemCount", pageIndex * PAGE_SIZE + "");

	}

	/**
	 * card.jsp 站外贺卡发送页面
	 * 
	 * @param request
	 * @param response
	 */
	public void card(HttpServletRequest request, HttpServletResponse response) {
		int cardId = 0;
		HappyCardBean card = null;
		HappyCardBean nextCard = null;
		if (null != request.getParameter("id"))
			cardId = StringUtil.toInt(request.getParameter("id"));
		card(cardId, request);

	}

	/**
	 * 获取当前贺卡信息和下一张贺卡信息(同一子分类的下一张贺卡)
	 * 
	 * @param cardId
	 * @param request
	 */
	public void card(int cardId, HttpServletRequest request) {
		HappyCardBean card = null;
		HappyCardBean nextCard = null;

		card = jobService.getHappyCard("id=" + cardId);
		if (card != null) {

			nextCard = jobService.getHappyCard("id>" + cardId + " and type_id="
					+ card.getTypeId() + " limit 0,1");
			if (nextCard == null)
				nextCard = jobService.getHappyCard(" type_id="
						+ card.getTypeId() + " order by id  limit 0,1");

			request.setAttribute("card", card);
			request.setAttribute("nextCard", nextCard);
		} else
			request.setAttribute("info", "error");

	}

	/**
	 * inGroupSend.jsp 站内群发，进入页面
	 * 
	 * @param request
	 * @param response
	 */
	public void inGroupSend(HttpServletRequest request,
			HttpServletResponse response) {
		card(request, response);
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		Vector onlineFriendsList = null;
		Vector offlineFriendsList = null;
		IUserService userService = ServiceFactory.createUserService();
		// onlineFriendsList = userService
		// .getUserList("join (SELECT friend_id FROM user_friend WHERE user_id="
		// + loginUser.getId()
		// + " AND friend_id IN(SELECT user_id FROM jc_online_user)) as temp on
		// user_info.id=temp.friend_id order by id ");
		//
		// offlineFriendsList = userService
		// .getUserList("join (SELECT friend_id FROM user_friend WHERE user_id =
		// "
		// + loginUser.getId()
		// + " and friend_id NOT IN (SELECT user_id FROM jc_online_user)) as
		// temp on user_info.id=temp.friend_id order by id ");

		offlineFriendsList = UserInfoUtil.getUserOfflineFriendsList(loginUser
				.getId());
		onlineFriendsList = UserInfoUtil.getUserOnlineFriendsList(loginUser
				.getId());

		request.setAttribute("onlineFriendsList", onlineFriendsList);
		request.setAttribute("offlineFriendsList", offlineFriendsList);

	}

	/**
	 * 站外群发发送处理
	 * 
	 * @param request
	 * @param response
	 */
	public void outGroupSendCard(HttpServletRequest request,
			HttpServletResponse response) {
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		String mobile = null;
		String receiver = null;
		String sender = null;
		int cardId = 0;
		int userId = loginUser.getId();
		sender = request.getParameter("sender");
		cardId = StringUtil.toInt(request.getParameter("cardId"));

		boolean bValidateOK = false;
		String info = "";
		String mobileParam = null;
		for (int i = 0; i < 5; i++) {
			mobileParam = "mobile" + i;
			mobile = request.getParameter(mobileParam);
			receiver = request.getParameter("receiver" + i);
			if (!mobile.trim().equals("") && !receiver.trim().equals("")) {
				bValidateOK = validateGroup(userId, mobile, mobileParam,
						request);
				if (bValidateOK == false) {
					info = info + mobile + "发送失败!"
							+ request.getAttribute(mobileParam);
				}
			}
		}
		if (info.equals("")) {
			int inviteCount = 0;
			inviteCount = jobService.getHappyCardSendCount(" send_datetime>='"
					+ DateUtil.getToday() + "' and user_id="
					+ loginUser.getId());
			request.setAttribute("inviteCount", inviteCount + "");
		} else {
			request.setAttribute("info", info);
		}

		card(cardId, request);
	}

	/**
	 * 验证站外群发
	 * 
	 * @param userId
	 * @param mobile
	 * @param mobileParam
	 * @param request
	 * @return
	 */
	public boolean validateGroup(int userId, String mobile, String mobileParam,
			HttpServletRequest request) {
		String validateMsg = null;
		validateMsg = validateMobile(mobile);
		if (null != validateMobile(mobile)) {
			request.setAttribute(mobileParam, validateMsg);
			return false;
		}
		// validateMsg = isBeijingUser(mobile);
		// if (null != isBeijingUser(mobile)) {
		// request.setAttribute(mobileParam, validateMsg);
		// return false;
		// }
		validateMsg = validateSendCount(userId, mobile);
		if (null != validateMsg) {
			request.setAttribute(mobileParam, validateMsg);
			return false;
		}
		return true;
	}

	/*
	 * 验证用户能发送的贺卡是否超过限制 验证三项，1--同一天不能给同一用户超过3张 2--同一天不能超过10张 3--一个月内不能超过150张
	 */
	public String validateSendCount(int userId, String mobile) {
		int inviteCount = 0;

		inviteCount = jobService.getHappyCardSendCount(" send_datetime>='"
				+ DateUtil.getToday() + "' and user_id=" + userId
				+ " and mobile='" + StringUtil.toSql(mobile) + "' ");

		if (inviteCount >= DAY_FRIEND_INVITE_COUNT)
			return "您每天只能给同一位好友发" + DAY_FRIEND_INVITE_COUNT + "次！";
		inviteCount = jobService.getHappyCardSendCount(" send_datetime>='"
				+ DateUtil.getToday() + "' and user_id=" + userId
				+ " and in_or_out_mark=1 ");
		// 国庆期间一天可以发送的站外贺卡为50张
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();// 当前时间
		c.set(2006, 9, 1, 0, 0, 0);
		Date date10_1 = c.getTime();// 10月1日
		c.set(2006, 9, 8, 0, 0, 0);
		Date date10_8 = c.getTime();// 10月8日
		// System.out.println("now=" + now);
		// System.out.println("date10_1=" + date10_1);
		// System.out.println("date10_8=" + date10_8);
		if (now.after(date10_1) && now.before(date10_8))
			DAY_INVITE_COUNT = 50;
		else
			DAY_INVITE_COUNT = 50;
		if (inviteCount >= DAY_INVITE_COUNT)
			return "您每天只能发送" + DAY_INVITE_COUNT + "次！";

		inviteCount = jobService.getHappyCardSendCount(" send_datetime>='"
				+ DateUtil.getFirstDayOfMonth() + "' and user_id=" + userId
				+ " and in_or_out_mark=1 ");
		if (inviteCount >= MONTH_INVITE_COUNT)
			return "您每月只能发送" + MONTH_INVITE_COUNT + "次！";
		return null;

	}

	/**
	 * 验证手机号是否合法
	 * 
	 * @param mobile
	 * @return
	 */
	public String validateMobile(String mobile) {
		if (mobile == null)
			return "输入有误!";
		if (mobile.length() != 11) {
			return "手机号码必须为11位!";
		}
		if (isNum(mobile) == false) {
			return "手机号码必须为数字!";
		}
		if (isCMCCUser(mobile) == false) {
			return "对不起，您的好友须为移动用户！";
		}
		return null;
	}

	/**
	 * card.jsp 站外贺卡发送
	 * 
	 * @param request
	 * @param response
	 */
	public void sendCard(HttpServletRequest request,
			HttpServletResponse response) {
		String mobile = request.getParameter("mobile");
		String receiver = request.getParameter("receiver");
		String sender = request.getParameter("sender");
		int cardId = Integer.parseInt(request.getParameter("cardId"));
		card(cardId, request);
		String validateMsg = null;
		validateMsg = validateMobile(mobile);
		if (null != validateMobile(mobile)) {
			request.setAttribute("info", validateMsg);
			return;
		}
		validateMsg = isBeijingUser(mobile);
		if (null != validateMsg) {
			request.setAttribute("info", validateMsg);
			return;
		}

		if (receiver.equals("")) {
			request.setAttribute("info", "请输入您好友的姓名!");
			return;
		}
		if (receiver.length() > 4) {
			request.setAttribute("info", "您的好友的姓名不能超过4个字!");
			return;
		}
		if (sender.equals("")) {
			request.setAttribute("info", "请输入您的姓名!");
			return;
		}
		if (sender.length() > 4) {
			request.setAttribute("info", "您的姓名不能超过4个字!");
			return;
		}

		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		validateMsg = validateSendCount(loginUser.getId(), mobile);
		if (null != validateMsg) {
			request.setAttribute("info", validateMsg);
			return;
		}
		sendPush(mobile, sender, receiver, loginUser.getId(), cardId);
		// 贺卡点击率加1
		jobService.updateHappyCard("hits=hits+1", "id=" + cardId);
		int inviteCount = 0;
		inviteCount = jobService.getHappyCardSendCount(" send_datetime>='"
				+ DateUtil.getToday() + "' and user_id=" + loginUser.getId()
				+ " and in_or_out_mark=1 ");
		request.setAttribute("inviteCount", inviteCount + "");

	}

	/*
	 * 发送push
	 * 
	 */
	public void sendPush(String mobile, String sender, String receiver,
			int userId, int cardId) {
		IUserService userService = ServiceFactory.createUserService();
		sender = sender.replace("'", "");
		receiver = receiver.replace("'", "");
		HappyCardSendBean sendBean = new HappyCardSendBean();
		sendBean.setUserId(userId);
		sendBean.setMobile(mobile);
		sendBean.setSender(sender);
		sendBean.setReceiver(receiver);

		sendBean.setCardId(cardId);
		sendBean.setMark(0);
		sendBean.setSuccessMark(0);
		sendBean.setSendMark(1);

		sendBean.setReceiverId(-1);
		sendBean.setInOrOutMark(1);

		UserBean user = userService.getUser("mobile='" + StringUtil.toSql(mobile) + "'");
		if (null != user)
			sendBean.setNewUserMark(0);
		else
			sendBean.setNewUserMark(1);
		jobService.addHappyCardSend(sendBean);
		sendBean = jobService.getHappyCardSend(" mobile='" + StringUtil.toSql(mobile)
				+ "' and user_id=" + userId + " order by id desc limit 0,1");
		// liuyi_2006-11-23_修改push发送方法 start
		String pushMessage = receiver + ",您朋友" + sender + "送来免费贺卡,请查收.";
		String url = "wap.joycool.net/jcadmin/happyCardDeal.jsp?id="
				+ sendBean.getId();
		boolean success = false;
		try {
			success = SmsUtil.sendPush(pushMessage, mobile, url);
			// success = true;
		} catch (Exception ex) {
			success = false;
		}
		// liuyi_2006-11-23_修改push发送方法 end
		if (!success)
			jobService.updateHappyCardSend("success_mark=0", "id="
					+ sendBean.getId());
		else
			jobService.updateHappyCardSend("success_mark=1", "id="
					+ sendBean.getId());
	}

	/**
	 * 判断字符串是不是都是数字 fanys 2006-7-13
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isNum(String mobile) {
		char nums[] = "0123456789".toCharArray();
		boolean isExist = false;
		for (int i = 0; i < mobile.length(); i++) {
			isExist = false;
			for (int j = 0; j < nums.length; j++) {
				if (nums[j] == mobile.charAt(i))
					isExist = true;
			}
			if (isExist == false)
				return false;
		}
		return true;
	}

	/**
	 * 手机是否为北京用户
	 * 
	 * @param mobile
	 * @return
	 */
	private String isBeijingUser(String mobile) {
		// AreaBean area = AreaUtil.getAreaByMobile(mobile);
		// if (area == null || area.getCityname().equals("北京")) {
		// return "北京";
		// }
		return null;
	}

	/**
	 * 手机是否为移动用户
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isCMCCUser(String mobile) {
		String prefix = mobile.substring(0, 3);
		boolean isExistMobilePrefix = false;
		String[] mobilePrefix = new String[] { "134", "135", "136", "137",
				"138", "139", "159" };
		for (int i = 0; i < mobilePrefix.length; i++) {
			if (prefix.equals(mobilePrefix[i])) {
				isExistMobilePrefix = true;
				break;
			}
		}
		return isExistMobilePrefix;

	}

	/**
	 * 站内群发--发送处理页面 inGroupSend.jsp
	 * 
	 * @param request
	 */
	public void sendInGroup(HttpServletRequest request) {
		int cardId = 0;
		String onlineFriends = null;
		String offlineFriends = null;
		String[] arrOnlineFriends = null;
		String[] arrOfflineFriends = null;
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		int userId = loginUser.getId();
		cardId = StringUtil.toInt(request.getParameter("id"));
		onlineFriends = request.getParameter("onlineFriends");
		offlineFriends = request.getParameter("offlineFriends");
		if (!onlineFriends.equals("")) {
			if (onlineFriends.indexOf(";") != -1) {
				arrOnlineFriends = onlineFriends.split(";");
				for (int i = 0; i < arrOnlineFriends.length; i++) {
					// 接收贺卡用户id
					int receiverId = StringUtil.toInt(arrOnlineFriends[i]);
					// 计算今天发送数量
					int count = countSendTime(loginUser.getId(), receiverId);
					// 小于4条就发送
					if (count < 3) {
						sendNotice(userId, receiverId, cardId, "你的朋友"
								+ loginUser.getNickName() + "给你送来祝福。");
						jobService.updateHappyCard("hits=hits+1", "id="
								+ cardId);
					}
				}
			} else {
				// 接收贺卡用户id
				int receiverId = StringUtil.toInt(onlineFriends);
				// 计算今天发送数量
				int count = countSendTime(loginUser.getId(), receiverId);
				// 小于4条就发送
				if (count < 3) {
					sendNotice(userId, receiverId, cardId, "你的朋友"
							+ loginUser.getNickName() + "给你送来祝福。");
					jobService.updateHappyCard("hits=hits+1", "id=" + cardId);
				}
			}
		}
		if (offlineFriends != null && offlineFriends.length() > 0) {
			if (offlineFriends.indexOf(";") != -1) {
				arrOfflineFriends = offlineFriends.split(";");
				for (int i = 0; i < arrOfflineFriends.length; i++) {
					// 接收贺卡用户id
					int receiverId = StringUtil.toInt(arrOfflineFriends[i]);
					// 计算今天发送数量
					int count = countSendTime(loginUser.getId(), receiverId);
					// 小于4条就发送
					if (count < 3) {
						sendNotice(userId, receiverId, cardId, "你的朋友"
								+ loginUser.getNickName() + "给你送来祝福。");
						jobService.updateHappyCard("hits=hits+1", "id="
								+ cardId);
					}
				}
			} else {
				// 接收贺卡用户id
				int receiverId = StringUtil.toInt(offlineFriends);
				// 计算今天发送数量
				int count = countSendTime(loginUser.getId(), receiverId);
				// 小于4条就发送
				if (count < 3) {
					sendNotice(userId, receiverId, cardId, "你的朋友"
							+ loginUser.getNickName() + "给你送来祝福。");
					jobService.updateHappyCard("hits=hits+1", "id=" + cardId);
				}
			}
		}

	}

	/**
	 * 
	 * @author macq
	 * @explain：计算一天内一个用户给站内另一个用户发送贺卡条数
	 * @datetime:2007-7-2 8:57:17
	 * @return
	 * @return int
	 */
	public int countSendTime(int userId, int receiverId) {
		String sql = "select count(id)  from jc_happy_card_send where user_id="
				+ userId + " and receiver_id=" + receiverId
				+ " and to_days(now()) - to_days(send_datetime) <= 1";
		int count = SqlUtil.getIntResult(sql, Constants.DBShortName);
		return count;
	}

	/**
	 * 站内群发，给站内用户发送通知
	 * 
	 * @param fromUserId
	 *            发送者id
	 * @param toUserId
	 *            接收者id
	 * @param cardId
	 *            贺卡id
	 * @param message
	 *            消息
	 */
	public void sendNotice(int fromUserId, int toUserId, int cardId,
			String message) {
		HappyCardSendBean sendBean = new HappyCardSendBean();

		sendBean.setUserId(fromUserId);
		sendBean.setMobile("");
		sendBean.setSender("");
		sendBean.setReceiver("");

		sendBean.setCardId(cardId);
		sendBean.setMark(0);
		sendBean.setSuccessMark(1);
		sendBean.setSendMark(1);

		sendBean.setNewUserMark(0);
		sendBean.setReceiverId(toUserId);
		sendBean.setInOrOutMark(0);

		jobService.addHappyCardSend(sendBean);
		sendBean = jobService.getHappyCardSend("user_id=" + fromUserId
				+ " and receiver_id=" + toUserId
				+ " order by id desc limit 0,1 ");
		NoticeBean notice = new NoticeBean();
		notice.setUserId(toUserId);
		notice.setTitle(message);
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/job/happycard/inSendDeal.jsp?id=" + sendBean.getId());
		NoticeUtil.getNoticeService().addNotice(notice);

	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @param cardId
	 */
	public void getCard(HttpServletRequest request,
			HttpServletResponse response, int cardId) {
		HappyCardSendBean sendBean = new HappyCardSendBean();
		sendBean = jobService.getHappyCardSend(" id=" + cardId);
		request.setAttribute("sendCard", sendBean);
		IUserService userService = ServiceFactory.createUserService();
		// UserBean receiver = userService.getUser("id="
		// + sendBean.getReceiverId());
		// UserBean sender = userService.getUser("id=" + sendBean.getUserId());
		// zhul 2006-10-12_优化用户信息查询
		UserBean receiver = UserInfoUtil.getUser(sendBean.getReceiverId());
		// zhul 2006-10-12_优化用户信息查询
		UserBean sender = UserInfoUtil.getUser(sendBean.getUserId());

		request.setAttribute("receiver", receiver);
		request.setAttribute("sender", sender);

	}

	/**
	 * 更新贺卡发送记录 接收者查看贺卡时修改记录
	 * 
	 * @param sendId
	 */

	public void updateCardSend(int sendId) {
		jobService.updateHappyCardSend(" mark=1 , view_datetime=now() ", " id="
				+ sendId);
	}

	/**
	 * 后台统计，每天1点钟执行
	 * 
	 */
	public static void hourTask() {
		try {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			if (hour == 1) {
				statHappyCardSend();
			}
		} catch (Exception ex) {

		}
	}

	/**
	 * 每天统计一次前一天的站外贺卡发送情况
	 * 
	 */
	private static void statHappyCardSend() {
		IJobService jobService = ServiceFactory.createJobService();
		int inviteCount = 0;// 下发总数
		int acceptNewCount = 0;// 接收的新用户数
		int replyCount = 0;// 回复的用户总数
		int replyNewcount = 0;// 回复的新用户数
		int reachLimitCount = 0;// 发送达到上限的人数
		int inCount = 0; // 发送给站内的人数

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = df.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		String startDate = df.format(c.getTime());
		String strWhere = " send_datetime>='" + startDate
				+ "' and send_datetime<'" + endDate + "' and in_or_out_mark=1 ";

		String instrWhere = " send_datetime>='" + startDate
				+ "' and send_datetime<'" + endDate + "' and in_or_out_mark=0 ";
		// 下发总数
		inviteCount = jobService.getHappyCardSendCount(strWhere);
		// 接收的新用户数
		acceptNewCount = jobService.getHappyCardSendCount(strWhere
				+ " and new_user_mark=1");
		// 回复的用户总数
		replyCount = jobService.getHappyCardSendCount(strWhere + " and mark=1");
		// 回复的新用户数
		replyNewcount = jobService.getHappyCardSendCount(strWhere
				+ " and  new_user_mark=1 and view_datetime is not null ");
		// 发送达到上限的人数
		reachLimitCount = jobService
				.getHappyCardSendCount("select count(*) as c_id from (select count(*) as c_id from jc_happy_card_send where "
						+ strWhere
						+ " group by user_id having c_id>="
						+ DAY_INVITE_COUNT + ") as temp ");
		// 发送给站内的人数
		inCount = jobService.getHappyCardSendCount(instrWhere);
		HappyCardStatBean statBean = new HappyCardStatBean();
		statBean.setInviteCount(inviteCount);
		statBean.setAcceptNewCount(acceptNewCount);
		statBean.setReplyCount(replyCount);
		statBean.setReplyNewCount(replyNewcount);
		statBean.setReachLimitCount(reachLimitCount);
		statBean.setStatDatetime(startDate);
		statBean.setInCount(inCount);
		jobService.deleteHappyCardSendStat(" stat_datetime>='" + startDate
				+ "' and stat_datetime<' " + endDate + "'");
		jobService.addHappyCardSendStat(statBean);

	}

}
