/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.AreaBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.RoomInviteBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.ChatServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.InviteAction;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.spec.buyfriends.ActionBuyFriend;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.AreaUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SequenceUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.Util;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class RegisterAction extends BaseAction {
	static UserServiceImpl service = new UserServiceImpl();
	static ChatServiceImpl chatService = new ChatServiceImpl();
	
	public static HashSet forbidPwd = new HashSet();	// 禁止的密码，过于简单
	static {
		forbidPwd.add("1234");
		forbidPwd.add("12345");
		forbidPwd.add("123456");
		forbidPwd.add("1234567");
		forbidPwd.add("12345678");
		forbidPwd.add("123456789");
		forbidPwd.add("000000");
		forbidPwd.add("111111");
		forbidPwd.add("222222");
		forbidPwd.add("333333");
		forbidPwd.add("444444");
		forbidPwd.add("555555");
		forbidPwd.add("666666");
		forbidPwd.add("777777");
		forbidPwd.add("888888");
		forbidPwd.add("999999");
		forbidPwd.add("0000");
		forbidPwd.add("1111");
		forbidPwd.add("2222");
		forbidPwd.add("3333");
		forbidPwd.add("4444");
		forbidPwd.add("5555");
		forbidPwd.add("6666");
		forbidPwd.add("7777");
		forbidPwd.add("8888");
		forbidPwd.add("9999");
		forbidPwd.add("4321");
		forbidPwd.add("54321");
		forbidPwd.add("654321");
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		

		UserStatusBean usb = null;
		String tip = null;
		boolean flag = true;
		/**
		 * 取得参数
		 */
		// String userName = request.getParameter("userName");
		
		String password = request.getParameter("password");
		String originPassword = password;
		String nickName = request.getParameter("nickname");
		nickName = StringUtil.noEnter(nickName);
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String selfIntroduction = request.getParameter("selfIntroduction");
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		HttpSession session = request.getSession();
		UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
		if(loginUser != null) {
			request.setAttribute("result", "success");
			request.setAttribute("backTo", backTo);

			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		/**
		 * 检查参数
		 */
		// if (userName == null || userName.equals("")) {
		// tip = "用户名不能为空！";
		// flag = false;
		// } else
		// if (!net.joycool.wap.util.StringUtil.isCorrect(userName)) {
		// tip = "用户名只能是字母或者数字！";
		// flag = false;
		// } else
		// if (service.getUser("user_name = '" + userName + "'") != null) {
		// tip = "这个用户名已经被使用！";
		// flag = false;
		// } else
		if (nickName == null || nickName.length() <= 2) {
			tip = "昵称不能少于2个汉字！";
			flag = false;
		} else if (StringUtil.getCLength(nickName) > 20) {
				tip = "昵称不能大于10个汉字！";
				flag = false;
		} else if (!SecurityUtil.checkNick(nickName)) {
			tip = "这个昵称已经被禁用!(数字/小数点以及某些词汇不能使用)";
			flag = false;
		} else if (password == null) {
			tip = "密码不能为空！";
			flag = false;
		} else if (password.length() < 4) {
			tip = "为了保护帐号的安全,密码长度最好大于4！";
			flag = false;
		} else if (forbidPwd.contains(password)) {
			tip = "密码过于简单,请重新设置！";
			flag = false;
		} else if (!net.joycool.wap.util.StringUtil.isCorrect(password)) {
			tip = "密码只能是字母或者数字！";
			flag = false;
		} else {
			// mcq_2006-9-7_更新密码算法_start
			// password = net.joycool.wap.util.Encoder.getMD5_Base64(password);
			password = net.joycool.wap.util.Encoder.encrypt(password);
			// mcq_2006-9-7_更新密码算法_end
			/**
			 * 检查参数
			 */
			if (gender == null || gender.equals("")) {
				gender = "1";
			}
			if (age == null || age.equals("")) {
				age = "20";
			}
			if (selfIntroduction == null || selfIntroduction.equals("")) {
				selfIntroduction = "我是一头大懒猪，我就不写个人签名。";
			}

		}

		String mobile = (String) session.getAttribute("userMobile");//Util.getPhoneNumber(request);
		if(mobile == null || mobile.length() < 5) {
			response.sendRedirect("/verify.jsp");
			return null;
		}
		List userIdList = SqlUtil.getIntList("select a.id from user_info a,user_status b where a.mobile='" + mobile + "' and b.user_id=a.id limit 1", Constants.DBShortName);
		if(userIdList == null || userIdList.size() > 0){	// 数据库查询出错需要重新注册
			response.sendRedirect("/register.jsp");
			return null;
		}
		// mcq_2006-9-9_允许一个手机注册N个帐户_start
		/*
		 * if (mobile != null) { if (service.getUser("mobile = '" + mobile +
		 * "'") != null) { tip = "这个手机号已经被注册！"; flag = false; } }
		 */
		// mcq_2006-9-9_允许一个手机注册N个帐户_end
		// 填写信息不正确
		Integer inviteKey = (Integer)session.getAttribute(InviteAction.inviteUserKey);
		Integer inviteType = (Integer)session.getAttribute(InviteAction.inviteTypeKey);
		if (flag == false) {
			
			if(inviteKey == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", tip);
				request.setAttribute("backTo", backTo);
				
			} else {
				request.setAttribute("tip", tip);
				request.setAttribute("flag", new Boolean(flag));
				return new ActionForward("/beacon/gInvite.jsp?uid="+inviteKey.intValue());
			}
			
			
		}
		// 填写信息正确
		else {
			AreaBean area = null;
			if (mobile == null) {
				mobile = "fttodeath";
			} else {
				area = AreaUtil.getAreaByMobile(mobile);
			}
			if (service.getUser("nickname = '" + StringUtil.toSql(nickName) + "'") != null) {	// 昵称有人用了，随机加3个数字
//				tip = "这个昵称已经被人使用！";
//				flag = false;
				nickName += RandomUtil.nextInt(1000);
			}
			UserBean user = new UserBean();
			int userID = SequenceUtil.getSeq("userID");;
			user.setId(userID);
			user.setAge(StringUtil.toInt(age));
			user.setGender(StringUtil.toInt(gender));
			user.setMobile(mobile);
			user.setNickName(StringUtil.trim(nickName));
			user.setPassword(password);
			user.setSelfIntroduction(selfIntroduction);
			if (area != null) {
				user.setPlaceno(area.getPlaceno());
				user.setCityname(area.getCityname());
				user.setCityno(area.getCityno());
			}

			if (!service.addUser(user)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// 添加Blog,添加用户状态,添加一条系统消息
			else {
				// 注册成功，写入特殊的pv日志表示，用于统计数据
				StringBuilder logsb = new StringBuilder(128);
				logsb.append(user.getMobile());
				logsb.append(':');
				logsb.append(user.getId());
				logsb.append(':');
				logsb.append(user.getNickName().replace(':', '_'));
				logsb.append(':');
				logsb.append(request.getRemoteAddr());
				logsb.append(':');
				logsb.append("/user/RegisterOk.do");
				logsb.append(":0:");
				logsb.append(session.getId());
				logsb.append(':');
				String linkIds = (String) session.getAttribute("linkId");
				logsb.append(StringUtil.toId(linkIds));
				logsb.append(":0");
				LogUtil.logPv(logsb.toString());
				
				AdminAction.addUserLog(user.getId(), 2, nickName);
				OsCacheUtil.flushGroup(OsCacheUtil.USER_ID_GROUP, mobile);
				// zhul-新增代码块 2006-06-07 start
				// 在此判断此用户是否有用户状态记录，如果没有，向user_status表中增加一条本用户的新记录。
				// user=service.getUser("user_name='"+user.getUserName()+"'");
				usb = UserInfoUtil.getUserStatus(user.getId());
				// usb = service.getUserStatus("user_id="
				// + user.getId());
				if (usb == null) {
					usb = new UserStatusBean();
					usb.setUserId(user.getId());
					usb.setPoint(0);
					if(inviteKey == null) {
						usb.setRank(0);
					} else {
						usb.setRank(3);
					}
					usb.setGamePoint(100000);
					usb.setNicknameChange(0);
					usb.setLoginCount(0);
					if (!service.addUserStatus(usb)) {
						return mapping
								.findForward(Constants.SYSTEM_FAILURE_KEY);
					}
					// add by mcq 2006-07-24 for stat user money history start
//					MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER,
//							10000, Constants.PLUS, user.getId());
					// add by mcq 2006-07-24 for stat user money history end
					// 求头衔
					// String name = null;
					// if (user.getGender() == 1) {
					// name = "乳臭未干";
					// } else {
					// name = "初出闺房";
					// }
					// 发送系统消息
					// liuyi 20060901 start 注册和注销功能
					request.getSession().setAttribute("user", user);
					// 赠新人卡
					UserBagCacheUtil.addUserBagCache(user.getId(), 33);
					
					NoticeBean notice = new NoticeBean();
					notice.setUserId(user.getId());
					notice.setTitle("注册成功!赠新人卡一张!");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/registerSuccess.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
					// liuyi 20060901 end 注册和注销功能
				}
				// zhul-2006-06-07 end
				// loginUser = service.getUser("id=" + userID);
				// zhul 2006-10-12_优化用户信息查询
				loginUser = UserInfoUtil.getUser(userID);
				int linkId = 0;
				if(linkIds != null) {
					linkId = StringUtil.toId(linkIds);
					if (linkId != 0)
						SqlUtil.executeUpdate("insert into jump_register set create_time=now(),link_id=" + linkId
								+ ",user_id=" + userID, 5);
				}
				// 更新字段
//				if (!UserInfoUtil.updateUser("enable_blog = 1",
//						" id=" + userID, userID)) {
//					return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//				}
			}

			// 保存信息 
			loginUser.setIpAddress(request.getRemoteAddr());
			loginUser.setUserAgent(request.getHeader("User-Agent"));
			JoycoolSessionListener.updateOnlineUser(request, loginUser);
			
//			String mid = (String)session.getAttribute(Constants.JC_MID);
//			service.updateMid(loginUser, mid);

			// liuyi 2006-12-20 登录注册修改 start
			OsCacheUtil.flushGroup(OsCacheUtil.USER_ID_GROUP, mobile);
			// liuyi 2006-12-20 登录注册修改 end

			// Cookie cookie = new Cookie(Constants.USER_COOKIE_NAME, loginUser
			// .getUserName());
			// cookie.setMaxAge(3600 * 24 * 30);
			// cookie.setPath("/");
			// cookie.setDomain(".joycool.net");
			// response.addCookie(cookie);
			request.setAttribute("result", "success");
			request.setAttribute("backTo", backTo);
			
			
			
			if(inviteType != null){
				int sendUid = inviteKey.intValue();
				UserBean sendUser = UserInfoUtil.getUser(sendUid);
				CacheManage.friendTrend.spt(new Integer(loginUser.getId()), new LinkedList());	// 新注册的人动态列表为空
				
				//邀请者与被邀请者相互必须加为好友
				service.addFriend(loginUser.getId(), sendUid);
				service.addFriend(sendUid, loginUser.getId());
				ActionTrend.addTrend(sendUid, BeanTrend.TYPE_BE_FRIEND, "%1 和 %2 成为好友", 
						sendUser.getNickName(),loginUser.getId(), nickName);
						
				ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_BE_FRIEND, "%1 和 %2 成为好友", 
						nickName,sendUid, sendUser.getNickName());
				// 谁邀请了谁
				RoomInviteBean invite = new RoomInviteBean();
				invite.setUserId(sendUser.getId());
				invite.setMobile(sendUser.getMobile());
				invite.setName(sendUser.getNickName());
				invite.setContent("");
				invite.setMark(0);
				invite.setNewUserMark(1);
				invite.setInviteeId(loginUser.getId());
				chatService.addRoomInviteOk(invite);
				
				switch(inviteType.intValue()) {
				case 0: {		// 普通邀请
					return new ActionForward("/user/invite.jsp?uid="+sendUid);
				}
				case 1: {		// 奴隶邀请
					//邀请成为奴隶
					ActionBuyFriend.register(loginUser, sendUser);
					return new ActionForward("/beacon/gInvite.jsp?uid="+sendUid);
				}
				}
				
				
			}

			// 往论坛用户表写入数据
			// DbOperation dbOp = new DbOperation();
			// dbOp.init();
			// dbOp.startTransaction();
			// service.addForumUser(loginUser.getId(), loginUser.getUserName(),
			// originPassword, dbOp);
			// dbOp.commitTransaction();
			// dbOp.release();
		}
		
		if(flag) {
			String red = (String)session.getAttribute("red");
			if(red != null) {
				session.removeAttribute("red");
				response.sendRedirect(red);
				return null;
			}
				
		}
		
		
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}

	public boolean autoRegister(HttpServletRequest request) {
		IUserService service = ServiceFactory.createUserService();

		UserBean loginUser = null;
		UserStatusBean usb = null;
		String tip = null;
		boolean flag = true;

		// String userName = request.getParameter("userName");
		String userID = request.getParameter("userID");
		String password = request.getParameter("password");
		String originPassword = password;
		String nickName = request.getParameter("nickname");
		String gender = request.getParameter("gender");
		String age = request.getParameter("age");
		String selfIntroduction = request.getParameter("selfIntroduction");
		String backTo = (String) request.getSession().getAttribute(
				"pagebeforeclick"); // request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		if (backTo.toLowerCase().indexOf("logout") != -1
				|| backTo.toLowerCase().indexOf("auto") != -1
				|| backTo.toLowerCase().indexOf("register") != -1) {
			backTo = BaseAction.INDEX_URL;
		}

		if (nickName == null || nickName.replace(" ", "").equals("")) {
			tip = "昵称不能为空！";
			flag = false;
		} else if ("v".equals(nickName)
				|| service.getUser("nickname = '" + StringUtil.toSql(nickName) + "'") != null) {
			tip = "这个昵称已经被人使用！";
			flag = false;
		} else if (!SecurityUtil.checkNick(nickName)) {
			tip = "这个昵称已经被禁用！";
			flag = false;
		} else if (password == null || password.equals("")) {
			tip = "密码不能为空！";
			flag = false;
		} else if (!net.joycool.wap.util.StringUtil.isCorrect(password)) {
			tip = "密码只能是字母或者数字！";
			flag = false;
		} else {
			// mcq_2006-9-7_更新密码算法_start
			// password = net.joycool.wap.util.Encoder.getMD5_Base64(password);
			password = net.joycool.wap.util.Encoder.encrypt(password);
			// mcq_2006-9-7_更新密码算法_end
			/**
			 * 检查参数
			 */
			if (gender == null || gender.equals("")) {
				gender = "1";
			}
			if (age == null || age.equals("")) {
				age = "20";
			}
			if (selfIntroduction == null || selfIntroduction.equals("")) {
				selfIntroduction = "我是一头大懒猪，我就不写个人签名。";
			}

		}

		String mobile = Util.getPhoneNumber(request);

		// mcq_2006-9-9_允许一个手机注册N个帐户_start
		// mcq_2006-9-9_允许一个手机注册N个帐户_end
		// 填写信息不正确
		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
			request.setAttribute("backTo", backTo);
		}
		// 填写信息正确
		else {
			AreaBean area = null;
			if (mobile == null) {
				mobile = "fttodeath";
			} else {
				area = AreaUtil.getAreaByMobile(mobile);
			}
			UserBean user = new UserBean();
			user.setId(StringUtil.toInt(userID));
			user.setAge(StringUtil.toInt(age));
			user.setGender(StringUtil.toInt(gender));
			user.setMobile(mobile);
			user.setNickName(StringUtil.trim(nickName));
			user.setPassword(password);
			user.setSelfIntroduction(selfIntroduction);
			if (area != null) {
				user.setPlaceno(area.getPlaceno());
				user.setCityname(area.getCityname());
				user.setCityno(area.getCityno());
			}

			if (!service.addUser(user)) {
				tip = "自动注册失败！";
				request.setAttribute("tip", tip);
				return false;
			}
			// 添加Blog,添加用户状态,添加一条系统消息
			else {
				OsCacheUtil.flushGroup(OsCacheUtil.USER_ID_GROUP, mobile);
				// zhul-新增代码块 2006-06-07 start
				// 在此判断此用户是否有用户状态记录，如果没有，向user_status表中增加一条本用户的新记录。
				usb = UserInfoUtil.getUserStatus(user.getId());
				if (usb == null) {
					usb = new UserStatusBean();
					usb.setUserId(user.getId());
					usb.setPoint(0);
					usb.setRank(0);
					usb.setGamePoint(10000);
					usb.setNicknameChange(0);
					usb.setLoginCount(0);
					if (!service.addUserStatus(usb)) {
						tip = "自动注册失败！";
						request.setAttribute("tip", tip);
						return false;
					}
					// add by mcq 2006-07-24 for stat user money history start
					MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER,
							10000, Constants.PLUS, user.getId());
					// add by mcq 2006-07-24 for stat user money history end

					// 发送系统消息
					// liuyi 20060901 start 注册和注销功能
					request.getSession().setAttribute("user", user);

					NoticeBean notice = new NoticeBean();
					notice.setUserId(user.getId());
					notice.setTitle("赠与您10000乐币！");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/registerSuccess.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
					// liuyi 20060901 end 注册和注销功能
				}
				// zhul-2006-06-07 end
				// zhul 2006-10-12_优化用户信息查询
				loginUser = UserInfoUtil.getUser(StringUtil.toInt(userID));
//				// 更新字段
//				if (!UserInfoUtil.updateUser("enable_blog = 1",
//						" id=" + userID, userID)) {
//					tip = "自动注册失败！";
//					request.setAttribute("tip", tip);
//					return false;
//				}
			}

			// 保存信息
			loginUser.setIpAddress(request.getRemoteAddr());
			loginUser.setUserAgent(request.getHeader("User-Agent"));
			JoycoolSessionListener.updateOnlineUser(request, loginUser);

			// liuyi 2006-12-20 登录注册修改 start
			OsCacheUtil.flushGroup(OsCacheUtil.USER_ID_GROUP, mobile);
			// liuyi 2006-12-20 登录注册修改 end

			request.setAttribute("result", "success");
			request.setAttribute("backTo", backTo);
		}
		return flag;
	}
}
