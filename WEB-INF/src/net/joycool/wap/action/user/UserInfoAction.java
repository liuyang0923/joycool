/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jc.util.VerifyUtil;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.SecurityUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 * 
 */
public class UserInfoAction extends BaseAction {
	static IUserService service = ServiceFactory.createUserService();
	static IFriendService friendService = ServiceFactory.createFriendService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserBean loginUser = null;
		String tip = null;
		boolean flag = true;
		/**
		 * 取得参数
		 */
		String password = request.getParameter("password");
		String old = request.getParameter("old");	// 旧密码
		String originPassword = password;
		String nickName = request.getParameter("nickname");
		nickName = StringUtil.noEnter(nickName);
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_start
		// String gender = request.getParameter("gender");
		// String age = request.getParameter("age");
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_end
		String selfIntroduction = request.getParameter("selfIntroduction");
		selfIntroduction = StringUtil.removeCtrlAsc(selfIntroduction);
		if(selfIntroduction != null && selfIntroduction.length() > 200) {
			selfIntroduction = selfIntroduction.substring(0, 200);
		}
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		loginUser = getLoginUser(request);	// session中的
		UserBean cacheUser = UserInfoUtil.getUser(loginUser.getId());

		/**
		 * 检查参数
		 */
		// 判断年龄是否是数字
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_start
		// try {
		// int tttt = Integer.parseInt(age);
		// } catch (NumberFormatException e) {
		// request.setAttribute("result", "failure");
		// request.setAttribute("tip", "年龄必须是数字！");
		// request.setAttribute("backTo", backTo);
		// return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		// }
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_end
		if (password != null && (password.length() < 4 || password.length() > 20)) {
			tip = "密码长度不能少于4位,不能超过20位！";
			flag = false;
		} else if (nickName != null && nickName.equals("")) {
			tip = "昵称不能为空！";
			flag = false;
		} else if (nickName != null && StringUtil.getCLength(nickName) > 20) {
			tip = "昵称不能大于10个汉字！";
			flag = false;
		} else if(VerifyUtil.getVerifyCountByKey(1, new Integer(loginUser.getId())) > 5) {
			tip = "操作过于频繁,请稍后再试.";
			flag = false;
		}
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_start
		// else if (age == null || age.equals("")
		// || nickName.replace(" ", "").equals("")) {
		// tip = "年龄不能为空！";
		// flag = false;
		// }
		//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_end
		 else if (nickName != null && !SecurityUtil.checkNick(nickName)) {
			tip = "这个昵称已经被禁用！（数字、小数点以及某些词汇不能使用）";
			flag = false;
		 }else if (nickName != null && ("v".equals(nickName)
				|| service.getUser("nickname = '" + StringUtil.toSql(nickName) + "' and id != "
						+ loginUser.getId()) != null)) {
			tip = "这个昵称已经被人使用！";
			flag = false;

		} else {
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("info",loginUser.getId());
			if(forbid != null && (nickName != null || selfIntroduction != null)) {
				tip = "已经被封禁修改权限 - " + forbid.getBak();
				flag = false;
			} else {
				if (password!=null && !loginUser.getPassword().equals(password)) {
					// mcq_2006-8-30_更换密码算法_start
					password = net.joycool.wap.util.Encoder.encrypt(password);
					// mcq_2006-8-30_更换密码算法_start
					// password =
					// net.joycool.wap.util.Encoder.getMD5_Base64(password);
				}
				if(!"".equals(loginUser.getPassword())&&loginUser.getPassword()!=null&&
					    password != null && !loginUser.getPassword().equals(net.joycool.wap.util.Encoder.encrypt(old))) {
					tip = "输入的旧密码不正确";
					flag = false;
				}
				/**
				 * 检查参数
				 */
				//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_start
				// if (gender == null || gender.equals("")) {
				// gender = "1";
				// }
				// if (age == null || age.equals("")) {
				// age = "20";
				// }
				//macq_2006-10-27_封修改性别,年龄,到家园或交友中心修改_end
				if (selfIntroduction != null && selfIntroduction.equals("")) {
					selfIntroduction = "我是一头大懒猪，我就不写个人签名。";
				}
			}

		}

		// 填写信息不正确
		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
			request.setAttribute("backTo", backTo);
		}
		// 填写信息正确
		else {
			StringBuilder set = new StringBuilder();
			if (password!=null && !loginUser.getPassword().equals(password)) {
				set.append("password = '" + StringUtil.toSql(password) + "'");
				AdminAction.addUserLog(loginUser.getId(), 1, originPassword);
				loginUser.setPassword(password);
				cacheUser.setPassword(password);
			}
			if (nickName != null
					&& (!nickName.equals(loginUser.getNickName()))) {
				if (set.length() > 0) {
					set.append(", ");
				}
				set.append("nickname = '" + StringUtil.toSql(nickName) + "'");
				AdminAction.addUserLog(loginUser.getId(), 2, nickName);
				loginUser.setNickName(nickName);
				cacheUser.setNickName(nickName);
			}
			if (selfIntroduction != null
					 && !selfIntroduction.equals(loginUser.getSelfIntroduction())) {
				if (set.length() > 0) {
					set.append(", ");
				}
				AdminAction.addUserLog(loginUser.getId(), 7, selfIntroduction);
				set.append("self_introduction = '" + StringUtil.toSql(selfIntroduction) + "'");
				loginUser.setSelfIntroduction(selfIntroduction);
				cacheUser.setSelfIntroduction(selfIntroduction);
			}

			if (set.length() != 0) {
				String condition = "id = " + loginUser.getId();
				UserInfoUtil.service.updateUser(set.toString(), condition);
//				if (!UserInfoUtil.updateUser(set.toString(), condition,
//						loginUser.getId() + "")) {
//					return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//				}
				// zhul 2006-10-12_优化用户信息查询
//				loginUser = UserInfoUtil.getUser(loginUser.getId());
			}

//			UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser.getId());
//
//			HttpSession session = request.getSession();
//
//			loginUser.setUs(userStatus);
//
//			session.setAttribute(Constants.LOGIN_USER_KEY, loginUser);
//			loginUser.setIpAddress(request.getRemoteAddr());
//			loginUser.setUserAgent(request.getHeader("User-Agent"));
//			JoycoolSessionListener.updateOnlineUser(request, loginUser);

			request.setAttribute("result", "success");
			request.setAttribute("backTo", backTo);
			// zhul 2006-06-29 如果此页是从交友中心的修改页跳过来的，将接收一个参数标记 start
			String tag = request.getParameter("tag");
			if (tag != null)
				request.setAttribute("tag", "1");
			// zhul 2006-06-29 如果此页是从交友中心的修改页跳过来的，将接收一个参数标记 end
			VerifyUtil.addToMap(1, new Integer(loginUser.getId()), 5);// 登陆次数过多？
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
