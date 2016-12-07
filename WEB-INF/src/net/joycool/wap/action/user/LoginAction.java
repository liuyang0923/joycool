/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jc.util.VerifyUtil;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserSettingBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.JoycoolSessionListener;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.Encoder;
import net.joycool.wap.util.LoginFilter;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.SmsUtil;
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
public class LoginAction extends BaseAction {
	static IUserService service = ServiceFactory.createUserService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		// liuyi 2006-09-19 返回进入页B start
		String backTo = (String) session.getAttribute(
				"pagebeforeclick"); // request.getParameter("backTo");
		// liuyi 2006-09-19 返回进入页B end
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		if (backTo.toLowerCase().indexOf("logout") != -1
				|| backTo.toLowerCase().indexOf("auto") != -1
				|| backTo.toLowerCase().indexOf("register") != -1) {
			backTo = BaseAction.INDEX_URL;
		}
		String userMob = (String) request.getParameter("userMob");
		if (userMob != null) {// 2008-7-10-zhouj:重大漏洞，不能用明码手机号直接取所有id，已经修正
			Vector userList = (Vector) service.getUserList("mobile='" + StringUtil.toSql(userMob)
					+ "' order by id desc");
			request.setAttribute("result", "userList");
			request.setAttribute("userList", userList);
			request.setAttribute("backTo", backTo);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		UserBean loginUser = null;
		// mcq_2006-8-30_更改登录方式用ID登录_start
		String uid = request.getParameter("uid");
		String password = request.getParameter("password");
		int id = 0;
		if(uid != null && uid.length() == 11 && uid.startsWith("1")) {
			id = SqlUtil.getIntResult("select a.id from user_info a,user_status b where a.mobile='" + StringUtil.toSql(uid) + "' and b.user_id=a.id order by b.last_login_time desc limit 1", Constants.DBShortName);
			// 如果不存在该手机号，到call_log2中寻找用户的preRegister，也就是发zc来的短信注册
			if(id == -1) {
				if(password == null)		// 用户密码框为空
					password = "5000";
				// 20131128修改，如果发短信指定密码的，要求5分钟内登陆，如果下发短信的，要求5小时内登陆
				int res = SqlUtil.getIntResult("select id from call_log2 where mobile='" + StringUtil.toSql(uid) + "' and password='" + StringUtil.toSql(password) + "' and (type=0 and adddate(create_time,interval 24 hour)>now() or adddate(create_time,interval 24 hour)>now())", 5);
				if(res != -1) {
					session.setAttribute("userMobile", uid);
					SqlUtil.executeUpdate("update call_log2 set status=1 where id=" + res, 5);
					if(password.length() > 5 && !RegisterAction.forbidPwd.contains(password))
						session.setAttribute("oripassword", password);	// 初始密码够复杂，不用重新输入
					return new ActionForward("/register.jsp");
				}
			}
		} else {
			id = StringUtil.toInt(uid);
		}
		
		String ip = LoginFilter.getIp(request);
		if(id > 0) {
			int vres = VerifyUtil.verify(id, ip, request);
			if(vres == -1)	// 验证码错误
				return new ActionForward("/user/login2.jsp");
			else if(vres > 0) {	// 错误次数多到需要验证码
				if(vres >= 10) {	// 错太多了，5分钟内禁止登陆
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "密码错误次数过多，请在5分钟后再试！");
					request.setAttribute("backTo", backTo);
					return new ActionForward("/user/login.jsp");
				} else {
					return new ActionForward("/user/login2.jsp");
				}
			}
		}
		
		// mcq_2006-8-30_更改登录方式用ID登录_start
		// String userName = request.getParameter("userName");
		
		String tip = null;
		boolean flag = true;

		// mcq_2006-8-29_登录用户五步走,第五步_start
		if (Util.getFlag().equals("five") && uid == null && password == null) {
			// 获取request范围中的mobile
			String message = request.getParameter("message");
			// liuyi 2006-12-21 注册登录修改 start
//			if (message == null) {
//				message = (String) session.getAttribute(
//						"userMobile");
//			}
			// liuyi 2006-12-21 注册登录修改 end
			// 转换字符为数字
			long mobile = StringUtil.toLong(message);
			// 判断输入手机号码是否为数字并判断输入号码的长度
			if (mobile < 0 || message.length() != 11) {
				tip = "对不起，请重新输入，获得ID和密码！";
				request.setAttribute("result", "five");
				request.setAttribute("tip", tip);
				request.setAttribute("backTo", backTo);
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			} else {
				// 验证输入号码是否为移动号码
				String prefix = message.substring(0, 3);
				boolean isExistMobilePrefix = false;
				String[] mobilePrefix = new String[] { "134", "135", "136",
						"137", "138", "139", "159", "158", "157", "156" };
				for (int i = 0; i < mobilePrefix.length; i++) {
					if (prefix.equals(mobilePrefix[i])) {
						isExistMobilePrefix = true;
						break;
					}

				}
				// 验证输入号码是否为移动号码
				if (!isExistMobilePrefix) {
					tip = "对不起，您的好友须为移动用户!";
					request.setAttribute("result", "five");
					request.setAttribute("tip", tip);
					request.setAttribute("backTo", backTo);
					return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
				}// 获取对应手机号的用户记录
				// UserBean checkUserMobile = service.getUser("mobile='" +
				// mobile
				// + "' order by id desc");

				String key = String.valueOf(mobile);

				String sql = "select a.id from user_info a,user_status b where a.mobile='"
						+ mobile + "' and b.user_id=a.id order by b.last_login_time desc limit 4";
				List userIdList = SqlUtil.getIntList(sql);
				if (userIdList == null) {
					userIdList = new ArrayList();
				}

				// 如果没有获取到记录提示重新输入
				if (userIdList.size() == 0) {
					tip = "对不起，请重新输入，获得ID和密码！";
					request.setAttribute("result", "five");
					request.setAttribute("tip", tip);
					request.setAttribute("backTo", backTo);
				} else {
					// 获取结果并传值到页面
					Vector checkUserMobile = new Vector();
					for (int i = 0; i < userIdList.size(); i++) {
						Integer userId = (Integer) userIdList.get(i);
						if (userId == null)
							continue;

						UserBean user = UserInfoUtil.getUser(userId.intValue());
						if (user != null) {
							checkUserMobile.add(user);
						}
					}
					request.setAttribute("checkUserMobile", checkUserMobile);
					// liuyi_2006-11-23_修改push发送方法 start
					Integer sendCount = (Integer) (SmsUtil.sendHash.get(key));
					if (sendCount == null) {
						sendCount = new Integer(0);
						SmsUtil.sendHash.put(key, sendCount);
					}
					if (sendCount.intValue() < SmsUtil.MAX_COUNT_PER_MOBILE) {
						sendCount = new Integer(sendCount.intValue() + 1);
						SmsUtil.sendHash.put(key, sendCount);

						String content = "您在乐酷有" + userIdList.size() + "个帐号.";
						for (int i = 0; i < userIdList.size(); i++) {
							Integer userId = (Integer) userIdList.get(i);
							if (userId == null)
								continue;

							UserBean user = UserInfoUtil.getUser(userId
									.intValue());
							String passWord = user.getPassword();
							String userMobile = user.getMobile();
							passWord = Encoder.decrypt(passWord);
							String send = "ID" + (i + 1) + ":" + userId + "密码"
									+ passWord;
							UserSettingBean userSetting = service.getUserSetting("user_id=" + user.getId());
							if(userSetting != null && userSetting.getBankPw() != null && userSetting.getBankPw().length() > 0) {
								send += "银" + userSetting.getBankPw() + ";";
							} else {
								send += ";";
							}
							if ((content.length() + send.length()) >= 66) {
								// 发送短信息
								//System.out.println(content);
								SmsUtil.send(SmsUtil.CODE, content, userMobile,
										SmsUtil.TYPE_SMS);
								content = send;
								if (i == (userIdList.size() - 1)) {
									System.out.println(content);
									SmsUtil.send(SmsUtil.CODE, content,
											userMobile, SmsUtil.TYPE_SMS);
								}
							} else {
								content = content + send;
								if (i == (userIdList.size() - 1)) {
								//	System.out.println(content);
									SmsUtil.send(SmsUtil.CODE, content,
											userMobile, SmsUtil.TYPE_SMS);
								}
							}
						}
						// liuyi_2006-11-23_修改push发送方法 end
						tip = "您的用户ID和密码已经发送到您的手机上，请注意接收和保存.(3秒钟自动返回)";
					} else {
						tip = "同一个手机号不能多次发送.(3秒钟自动返回)";
					}
					request.setAttribute("result", "sendMessage");
					request.setAttribute("tip", tip);
					request.setAttribute("backTo", backTo);
				}
			}
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// mcq_2006-8-29_登录用户五步走,第五步_end

		if (id < 0) {
			tip = "用户ID或手机号输入不正确";
			flag = false;
		} else if (SecurityUtil.checkForbidUserId(id)) {
			tip = "此ID已经被封禁.";
			flag = false;
		} else if (VerifyUtil.getVerifyCountByKey(1, new Integer(id)) > 5) {	// 5分钟内只让登陆5次
			tip = "登陆过于频繁,请稍后再试.";
			flag = false;
		} else if (password == null || password.equals("")) {
			tip = "密码不能为空！";
			flag = false;
		} else if (password.length() > 20) {
			tip = "密码输入过长";
			flag = false;
		} else {
			// mcq_2006-8-30_更换密码算法_start
			String password2 = net.joycool.wap.util.Encoder.encrypt(password);
			// mcq_2006-8-30_更换密码算法_end
			// password = net.joycool.wap.util.Encoder.getMD5_Base64(password);
			// loginUser = service.getUser("id = " + id);
			// zhul 2006-10-12_优化用户信息查询
			loginUser = UserInfoUtil.getUser(id);
			
			if (loginUser == null) {
				tip = "登录失败！请重试！";
				flag = false;
			} else if (!loginUser.getPassword().equals(password2)) {
				if (Util.getFlag().equals("four")) {
					tip = "登录失败！请重试！或输入手机号码找回用户名密码！";
					flag = false;
				} else {
					tip = "登录失败！请重试！";
					flag = false;
				}
				VerifyUtil.logFail(id, password, ip);
			} else if(!SecurityUtil.isPasswordSecure(password)) {
//				UserStatusBean us = loginUser.getUs2();
//				if (Util.getFlag().equals("four")) {
//					tip = "登录失败！请重试！";
//					flag = false;
//				}
			}
		}

		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
			request.setAttribute("backTo", backTo);
		} else {
			// ******warning:如果有游客账户的话,就进行绑定******
			jc.guest.GuestUserInfo guestUser = (jc.guest.GuestUserInfo)session.getAttribute(jc.guest.GuestHallAction.GUEST_KEY);
			if (guestUser != null){
				if (guestUser.getBuid() == 0 && jc.guest.GuestHallAction.service.getUserInfo(" buid=" + loginUser.getId() + " order by id limit 1") == null){
					// 此游客账户别绑定过正式用户，并且，此注册用户也没有绑定过别的游客，才可以互相绑定
					guestUser.setBuid(loginUser.getId());
					SqlUtil.executeUpdate("update guest_user_info set buid=" + loginUser.getId() + " where id=" + guestUser.getId(), 6);
				}
			}
			// ******绑定完毕******
			JoycoolSessionListener.logout(request);
			session = SecurityUtil.newSession(request);
			// ******如果存在游客用户,就再放回到session里
			if (guestUser != null){
				session.setAttribute(jc.guest.GuestHallAction.GUEST_KEY, guestUser);
			}
			// ******放置完毕******
			loginUser.setIpAddress(ip);
			loginUser.setUserAgent(request.getHeader("User-Agent"));
			JoycoolSessionListener.updateOnlineUser(request, loginUser);
			
			request.setAttribute("result", "success");
			request.setAttribute("backTo", backTo);

			// liuyi 2006-12-28 统计后台修改 start
			//LogUtil.logLogin(loginUser.getId() + ":" + request.getRemoteAddr());
			// liuyi 2006-12-28 统计后台修改 start
			
			VerifyUtil.logSuccess(id);	// 从错误次数中删除
			VerifyUtil.addToMap(1, new Integer(id), 5);// 登陆次数过多？
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
