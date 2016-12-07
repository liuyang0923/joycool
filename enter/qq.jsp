<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.service.factory.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="com.google.gson.*"%><%@ page import="jc.qq.*"%><%@ page import="net.joycool.wap.bean.*"%><%!
	static String redirectUrl=java.net.URLEncoder.encode("http://qq.joycool.net/enter/qq.jsp");
	static int appId=101250747;
	static String appKey="7c126df4b6ca1037bf6bac6aa896e1bf";
	static net.joycool.wap.service.impl.UserServiceImpl service = new net.joycool.wap.service.impl.UserServiceImpl();
%><?xml version="1.0" encoding="UTF-8"?>
<%
String code=request.getParameter("code");
if(session.getAttribute("loginUser")==null){
if(code==null) {
	int linkId = StringUtil.toId((String) request.getParameter("s"));
	if(linkId>0)
		session.setAttribute("linkId", String.valueOf(linkId));
	int rnd=RandomUtil.nextInt(1000000);
	session.setAttribute("qqloginstate", new Integer(rnd));
	BaseAction.sendRedirect("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id="+appId+"&state="+rnd+"&redirect_uri="+redirectUrl, response);
	return;
}else{
int state=StringUtil.toInt(request.getParameter("state"));
Integer state2 = (Integer)session.getAttribute("qqloginstate");
if(state2!=null && state==state2.intValue()) {
	
	String accessTokenURL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=101250747&client_secret="+appKey+"&code="+code+"&state="+state+"&redirect_uri=" + redirectUrl;
	String ret = HttpUtil.getContent(accessTokenURL, "utf-8");
	// System.out.println("ret = " + ret);
	if(ret!=null && ret.indexOf("access_token") != -1){
		String accessToken = null;
		int pos1 = ret.indexOf("access_token=");
		int pos2 = ret.indexOf("&", pos1);
		accessToken=ret.substring(pos1+13, pos2);
		// System.out.println("accesstoken = " + accessToken);
		if(accessToken != null){
			Gson gson = new Gson();
			String openidURL = "https://graph.qq.com/oauth2.0/me?access_token=" + accessToken;
			String ret2 = HttpUtil.getContent(openidURL, "utf-8");
			int pos3 = ret2.indexOf("(");
			int pos4 = ret2.lastIndexOf(")");
			ret2 = ret2.substring(pos3 + 1, pos4).trim();
//			System.out.println("ret2 = " + ret2);
			ReturnOpenId o = gson.fromJson(ret2, ReturnOpenId.class);
			if(o!=null){
				int userId=0;
				synchronized(appKey){
					userId = SqlUtil.getIntResult("select user_id from qqlogin where openid='" + StringUtil.toSql(o.openid)+"'");
					if(userId<=0){	// 新注册用户
					
						String userInfoURL = "https://graph.qq.com/user/get_user_info?access_token="+accessToken+"&oauth_consumer_key="+appId+"&openid="+o.openid;
						String ret3 = HttpUtil.getContent(userInfoURL, "utf-8");
						// System.out.println("ret3 = " + ret3);
						ReturnUserInfo ui = gson.fromJson(ret3, ReturnUserInfo.class);
						if(ui!=null){

// 注册新用户，从RegisterAction拷贝
							int sameIp = SqlUtil.getIntResult("select count(*) from qqlogin where create_time>date_add(now(),interval -1 day) and ip='" + request.getRemoteAddr()+"'");
							if(sameIp>=5) {	// 2 each day per ip
							    BaseAction.sendRedirect("/user/login.jsp", response);
								return;
							}

							String nickName=ui.nickname.replaceAll("[\\t\\n\\r ]","");
							if (service.getUser("nickname = '" + StringUtil.toSql(nickName) + "'") != null) {	// 昵称有人用了，随机加3个数字
								nickName += RandomUtil.nextInt(1000);
							}
							UserBean user = new UserBean();
							int userID = SequenceUtil.getSeq("userID");
							user.setId(userID);
							int year=StringUtil.toInt(ui.year);
							if(year>1900)
								user.setAge(2015-year);
							user.setGender(ui.gender.equals("男")?1:0);
							user.setMobile("");
							if(SecurityUtil.checkNick(nickName)){
								user.setNickName(nickName);
							}else{
								user.setNickName(String.valueOf(userID));				
							}
							user.setPassword("");
							user.setSelfIntroduction("");
							service.addUser(user);
			
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
			
							UserStatusBean usb = UserInfoUtil.getUserStatus(user.getId());
							if (usb == null) {
								usb = new UserStatusBean();
								usb.setUserId(user.getId());
								usb.setPoint(0);
								usb.setRank(3);
								usb.setGamePoint(100000);
								usb.setNicknameChange(0);
								usb.setLoginCount(0);
								service.addUserStatus(usb);
			
								session.setAttribute("loginUser", user);
								// 赠新人卡
								UserBagCacheUtil.addUserBagCache(user.getId(), 33);
								
								NoticeBean notice = new NoticeBean();
								notice.setUserId(user.getId());
								notice.setTitle("QQ登陆成功!赠新人卡一张!");
								notice.setContent("");
								notice.setType(NoticeBean.GENERAL_NOTICE);
								notice.setHideUrl("");
								notice.setLink("/user/registerSuccess.jsp");
								ServiceFactory.createNoticeService().addNotice(notice);
			
							}
							
							SqlUtil.executeUpdate("insert into qqlogin set create_time=now(),user_id="+userID+",openid='"+StringUtil.toSql(o.openid)+"',ip='"+request.getRemoteAddr()+"'");
			
							UserBean loginUser = UserInfoUtil.getUser(userID);
							loginUser.setIpAddress(request.getRemoteAddr());
							loginUser.setUserAgent(request.getHeader("User-Agent"));
							JoycoolSessionListener.updateOnlineUser(request, loginUser);
						}
					}
				}
				if(userId>0){	// 注册成功或者登陆成功
					if (SecurityUtil.checkForbidUserId(userId)) {
					    BaseAction.sendRedirect("/user/login.jsp", response);
						return;
					}
					UserBean loginUser = UserInfoUtil.getUser(userId);
					if(loginUser!=null){
						loginUser.setIpAddress(request.getRemoteAddr());
						loginUser.setUserAgent(request.getHeader("User-Agent"));
						JoycoolSessionListener.updateOnlineUser(request, loginUser);
						session.setAttribute("cd-fourmSTime", Long.valueOf(System.currentTimeMillis() + 20000));	// 20秒内不能发帖或者回帖
						LogUtil.logLogin("qqlogin:" + loginUser.getId() + ":" + request.getRemoteAddr() + ":" + request.getHeader("User-Agent") + ":" + new CookieUtil(request).getCookieValue("jsid"));
					}
				}
			}
		}
	}
}
}}%>
<%
UserBean user = (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
if(user!=null){
	BaseAction.sendRedirect("/Column.do?columnId=16025", response);
	return;
}else if(code!=null){
    BaseAction.sendRedirect("/user/login.jsp", response);
	return;
}%>