package jc.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.user.RegisterAction;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author maning
 * 
 * 初级用户注册，登陆
 * 
 */
public class UserAction2 extends CustomAction {
	
	public static UserService2 service = new UserService2();
	public final static String LOGIN_USER_KEY2 = "loginUser2";
	
	public static ICacheMap userCache2 = CacheManage.addCache(new LinkedCacheMap(1000, true), "user2");	// A级用户

	public static UserService2 getService() {
		return service;
	}
	
	public UserAction2(HttpServletRequest request){
		super(request);
	}
	
	public UserAction2(){
		
	}

	/**
	 * A级用户登陆
	 * @param request
	 * @param mobile
	 * @param password
	 * @return
	 */
	public boolean Login2(HttpServletRequest request,String mobile,String password){
		UserBean2 user = null;
//		Pattern pattern = Pattern.compile("(1\\d{10})");
//		// 1、检测手机
//		if (mobile == null || "".equals(mobile)){
//			tip = "没有输入手机号,或手机格式不正确.";
//			request.setAttribute("tip", tip);
//			return false;
//		} else {
//			Matcher isMobile = pattern.matcher(mobile);
//			if (!isMobile.matches()){
//				tip = "手机格式错误.";
//				request.setAttribute("tip", tip);
//				return false;
//			}
//		}
		// 2、检测密码
		if (password == null || "".equals(password) || password.length() < 4 || password.length() > 10){
			tip = "密码错误.";
			request.setAttribute("tip", tip);
			return false;
		}
		// 3、再查看是否是A级用户
		user = service.getUserBean2("mobile='" + StringUtil.toSql(mobile) + "' and `password`='" + StringUtil.toSql(password) + "'");
		if (user != null){
			// 登陆成功
			HttpSession session = request.getSession();
			// 放入session
			session.setAttribute(LOGIN_USER_KEY2, new Integer(user.getId()));
			// 放入缓存
			userCache2.put(new Integer(user.getId()), user);
			tip = "登陆成功";
			return true;
		}
		return false;
	}
	
	/**
	 * A级用户注册
	 * @param request
	 * @return
	 */
	public boolean register2(HttpServletRequest request){
		String tip = "";
		boolean isRegistered = false;
		String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");
		// 1、检测输入的信息是否正确
		Pattern pattern = Pattern.compile("(1\\d{10})");
		if (mobile == null || "".equals(mobile)){
			tip = "没有输入手机号,或手机格式不正确.";
			request.setAttribute("tip", tip);
			return false;
		} else {
			Matcher isMobile = pattern.matcher(mobile);
			if (!isMobile.matches()){
				tip = "手机格式错误.";
				request.setAttribute("tip", tip);
				return false;
			}
		}
		if (password == null || StringUtil.toInt(password) == -1 || password.length() < 4 || password.length() > 10){
			tip = "密码错误.";
			request.setAttribute("tip", tip);
			return false;
		} else if (RegisterAction.forbidPwd.contains(password)){
			tip = "密码过于简单,请重新设置！";
			request.setAttribute("tip", tip);
			return false;
		}
		
		// 2、检测手机号是否被B级用户注册过
		int id = SqlUtil.getIntResult("select a.id from user_info a,user_status b where a.mobile='" + StringUtil.toSql(mobile) + "' and b.user_id=a.id order by b.last_login_time desc limit 1", 0);
		if ( id > 0){
			isRegistered = true;
		} else {
			// 3、检测是否被A级用户注册过
			if(service.getUserBean2(" mobile='" + StringUtil.toSql(mobile) + "'") != null){
				isRegistered = true;
			}
		}
		// 4、注册
		if (!isRegistered){
			UserBean2 user = new UserBean2();
			user.setMobile(mobile);
			user.setPassword(password);
			user.setNickName("");	// nick_name can not be Null
			int lastId = service.addNewUser(user);
			user.setId(lastId);
//			SqlUtil.executeUpdate("insert into user_info2 (mobile,password,create_time) values (" + StringUtil.toSql(mobile) + "," + StringUtil.toSql(password) + ",now())", 0);
			HttpSession session = request.getSession();
			session.setAttribute(LOGIN_USER_KEY2, new Integer(user.getId()));
			userCache2.put(new Integer(user.getId()), user);
			return true;
		} else {
			tip = "注册失败,您的号码已经注册过,请点击<a href=\"/guest/user/findpw.jsp\">找回您的密码</a>!<br/>或者重新输入:";
			request.setAttribute("tip", tip);
			return false;
		}
	}
	
	// 修改昵称等信息
	public boolean modifyInfo(HttpServletRequest request){
		String tip = "";
		UserBean2 user = null;
		user = getLoginUser2();
		if (user == null){
			tip = "请先注册用户.";
			request.setAttribute("tip", tip);
			return false;
		}
		String nickName = request.getParameter("nickname");
		int age = StringUtil.toInt((String)request.getParameter("age"));
		int gender = StringUtil.toInt((String)request.getParameter("gender"));
		// 1、检测输入是否正确
		if (nickName == null){
			tip = "没有输入昵称,请重新填写.";
			request.setAttribute("tip", tip);
			return false;
		} else {
			nickName = StringUtil.removeSpecialAsc(nickName);
			if ("".equals(nickName) || nickName.length() > 10){
				tip = "没有输入昵称或昵称太长,请重新填写.";
				request.setAttribute("tip", tip);
				return false;
			}
		}
		if (age < 10 || age > 60){
			tip = "年龄超出范围,请重新填写.";
			request.setAttribute("tip", tip);
			return false;
		}
		if (gender < 0 || gender > 1){
			tip = "请您输入性别.输入后不可更改.";
			request.setAttribute("tip", tip);
			return false;
		}
		// 2、注册到数据库
		SqlUtil.executeUpdate("update user_info2 set nick_name='" + StringUtil.toSql(nickName) + "',age=" + age + ",gender=" + gender + " where id=" + user.getId(), 0);
		user.setNickName(nickName);
		user.setAge(age);
		user.setGender(gender);
		userCache2.put(new Integer(user.getId()), user);
		return true;
	}
	
	// 取得A级用户
	public static UserBean2 getUser2(int uid){
		if (uid <= 0)
			return null;
		Integer key = Integer.valueOf(uid);
		synchronized(userCache2) {
			UserBean2 user = (UserBean2) userCache2.get(key);
			if (user == null) {
				user = service.getUserBean2(" id=" + uid);
				if (user == null) {
					return null;
				} 
				userCache2.put(key, user);
			}
			return user;
		}
	}
	
	// 取得当前登陆的A级用户
	public UserBean2 getLoginUser2(){
		HttpSession session = request.getSession();
		Integer key =  (Integer)session.getAttribute(LOGIN_USER_KEY2);
		if (key == null){
			return null;
		}
		return getUser2(key.intValue());
	}
	
	// 激活A账号
	public static UserBean2 checkMessage(String mobile){
		UserBean2 user = service.getUserBean2("mobile='" + StringUtil.toSql(mobile) + "'");
		if(user == null || user.getChecked() == 1){
			return null;
		}
		service.checkedUser(mobile);
		user.setChecked(1);
		userCache2.put(new Integer(user.getId()), user);
		return user;
	}
}
