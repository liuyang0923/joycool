<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.spec.InviteAction "%><%@ page import="net.joycool.wap.bean.UserBean "%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.home.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.spec.buyfriends.*"%><%
response.setHeader("Cache-Control","no-cache");

InviteAction action = new InviteAction(request, response);

//action.index();
int userId = action.getSessionInviteUser();
if(userId == 0) {
	action.redirect("/wapIndex.jsp");
	return;
}

String mobile = (String)session.getAttribute("userMobile");
UserBean loginUser = (UserBean) session.getAttribute("loginUser");

if(mobile==null && loginUser == null){
	if(request.getParameter("sId") == null){
		boolean ismobile = false;//SecurityUtil.isMobile(request);
		if(ismobile){
			session.setAttribute("allowVisit", "true");
			String eurl = "http://"+request.getServerName()+response.encodeURL("/invite.jsp?sId=1");
		eurl = "http://211.157.107.130/abc/jc.jsp?fr=" + net.joycool.wap.util.Encoder.encrypt(eurl);
		response.sendRedirect("http://61.136.63.17/zhui/i.jsp?sd=" + URLEncoder.encode(other.util.Base64Encoder.zhuiEncode(eurl.getBytes()),"utf-8"));

			return;
		}else{
			mobile="";
			session.setAttribute("userMobile", mobile);
		}
		
	} else {
		String ip = request.getRemoteAddr();
		
		if(ip.startsWith("211.157.107.1")){
			mobile = request.getParameter("_mn_");
			if(mobile != null) {
				mobile = Encoder.decrypt(mobile);
				if (mobile.startsWith("86")) {
					mobile = mobile.substring(2);
				}
				String fip = request.getParameter("ip");
				if(fip!=null && (!SecurityUtil.isMobileIp(fip))) {
					System.out.println(mobile + "-!!! " + request.getParameter("for"));
					mobile="";
				} else {
					System.out.println(mobile + "-" + request.getParameter("for"));
				}
			} else {
				mobile = "";
			}
			session.setAttribute("userMobile", mobile);
			return;
		} else if(mobile==null){
//			mobile="13513231117";
//			session.setAttribute("userMobile", mobile);
			return;
		}
	}
}

if(loginUser != null){
	response.sendRedirect("/home/home2.jsp?userId=" + userId);
	return;
}
UserBean user = UserInfoUtil.getUser(userId);

//int uid = action.//Integer.parseInt(request.getParameter("uid"));
	
	//session.setAttribute("inviteUid", new Integer(uid));
HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷社区">
<p align="left">
<img src="img/invite.gif"/><br/>
+邀请您的好友在乐酷中的昵称为[<%=user.getNickNameWml()%>]+<br/>
<%
List list = UserInfoUtil.getUserFriends(userId);
ServiceTrend serviceTrend = ServiceTrend.getInstance();
List trendList = serviceTrend.getTrendByUid(userId, 0, 5);
%>
<%if(list.size() > 0) {%><%=user.getGenderText()%>有<%=list.size() %>个好友<br/><%}%>
<%if(homeUser!=null){
	if(homeUser.getPhotoCount() > 0) {
%><%=user.getGenderText()%>的相册有<%=homeUser.getPhotoCount() %>张照片<br/>
<%} 
if(homeUser.getDiaryCount() > 0) { 
%><%=user.getGenderText()%>有<%=homeUser.getDiaryCount() %>篇日记<br/>
<%}
}%>
【<%=user.getGenderText()%>的动态】<br/>
<%if(trendList == null || trendList.size() == 0) {%>
(暂无)<br/>
<%} else {
	for(int i = 0;i < trendList.size(); i ++) {
		BeanTrend trend = (BeanTrend)trendList.get(i);
%>*<%=trend.getContentNoLink() %><br/>
<%}   }
%>---------<br/><%
if(mobile.length()<5) {%>
想要看这个家伙更多的秘密和每天的一举一动么？赶紧注册，立刻开始你的“乐”“酷”之旅！<br/>
<a href ="http://joycool.net/user/<%=response.encodeURL("login.jsp")%>">登录</a>|<a href ="http://joycool.net/<%=response.encodeURL("register.jsp")%>">注册</a><br/>
<%} else {%>
想要看这个家伙更多的秘密和每天的一举一动么？赶紧注册，立刻开始你的“乐”“酷”之旅！<br/>
给自己取个酷酷的名字(10字以内)<br/>
<input name="nickname" maxlength="10"/><select name="gender" value="0">
    <option value="1">男</option>
    <option value="0">女</option>
</select><br/>
设定一个密码<br/>
<input name="password" maxlength="20"/><br/>
<anchor title="post">完成注册<go href ="http://joycool.net/user/<%=response.encodeURL("Register.do")%>" method="post">
	<postfield name="nickname" value="$nickname" />
	<postfield name="password" value="$password" />
	<postfield name="gender" value="$gender" />
	<postfield name="age" value="<%=RandomUtil.nextInt(6)+18%>" />
</go></anchor><br/>
<%} %>
wap.joycool.net<br/>
</p>
</card>
</wml>