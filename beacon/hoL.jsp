<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page language="java" import="java.util.*" pageEncoding="utf-8"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.action.money.*,net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.framework.BaseAction, net.joycool.wap.spec.buyfriends.*"%>
<%response.setHeader("Cache-Control","no-cache");
	net.joycool.wap.service.infc.IUserService service = ServiceFactory.createUserService();
	IHomeService homeService =ServiceFactory.createHomeService();

	int uid = Integer.parseInt(request.getParameter("uid"));
	
	session.setAttribute("inviteUid", new Integer(uid));
	
	int friendCount = service.getFriendCount("user_id = " + uid);
	UserBean user = UserInfoUtil.getUser(uid);
	int photoCount = homeService.getHomePhotoCount("user_id = " + uid);
	int diaryCount = homeService.getHomeDiaryCount("user_id =" + uid + " and del=0");
	ServiceTrend serviceTrend = ServiceTrend.getInstance();
	List trendList = serviceTrend.getTrendByUid(uid, 0, 5);
	//int forumCount = SqlUtil.getIntResult("select count(id) from jc_forum_content where user_id = " + StringUtil.trim(""+uid));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
	<card title="邀请">
	<p><%=BaseAction.getTop(request, response)%><%=user.getNickNameWml()%> 的家园<br/>
	<%=user.getGenderText()%>有<%=friendCount%>个好友<br/>
	<%=user.getGenderText()%>的相册有<%=photoCount%>张照片<br/>
	<%=user.getGenderText()%>有<%=diaryCount%>篇日记<br/>
	【<%=user.getNickName()%>的动态】<br/>
	<%
		for(int i = 0;i < trendList.size(); i ++) {
			BeanTrend trend = (BeanTrend)trendList.get(i);
	%>
			*<%=trend.getContentNoLink() %><br/>
	<%} %>
	----------------------<br/>
	想要看到更多关于<%=user.getGenderText()%>的动态和更详细的内容，那就赶快加入乐酷一族吧<br/>
	请输入您的昵称（10字以内）<br/>
	<input name="nickName"/><br/>
	请输入密码<br/>
	<input name="password"/><br/>
	<anchor title="post">注册<go href="<%=("gInvite.jsp?uid=" + uid)%>" method="post">
			<postfield name="nickName" value="$nickName" />
			<postfield name="mobile" value="<%=request.getParameter("mobile")%>" />
			<postfield name="password" value="$password" />
		</go></anchor>| <a href="/user/login.jsp">登录</a>
	
	<br/>
	<!-- 
	<%=BaseAction.getBottomShort(request, response)%> -->
	</p>
	</card>
</wml>