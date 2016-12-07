<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction,net.joycool.wap.bean.chat.RoomInviteBean,net.joycool.wap.bean.UserStatusBean,net.joycool.wap.bean.friend.FriendBean,net.joycool.wap.action.friend.FriendAction,net.joycool.wap.spec.friend.MoodUserBean,net.joycool.wap.spec.friend.MoodService"%>
<%! static MoodService mservice = new MoodService(); %>
<%response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
UserBean loginUser = action.getLoginUser();
// 已登陆用户请直接回导航
if (loginUser != null){
	out.clearBuffer();
	response.sendRedirect("/lswjs/index.jsp");
	return;
}
// 如果没有传uid过来，或uid错误，则回到普通的登陆页面
int uid = action.getParameterInt("uid");
if (uid <= 0){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
UserBean user = UserInfoUtil.getUser(uid);
if (user == null){
	out.clearBuffer();
	response.sendRedirect("/user/login.jsp");
	return;
}
String tip = "";
int online = 1;	//0:离线 1:在线
UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(uid));
if (onlineUser == null){
	online=0;
}
String userNameStr = user.getNickNameWml() + "[" + user.getId() + "](" + (online==0?"离线":"在线") + ")" ;
MoodUserBean mub = mservice.getLastMoodBean(user.getId());
String backTo = action.getParameterNoEnter("backTo");
if (backTo == null || "".equals(backTo)){
	backTo = "/lswjs/index.jsp";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="登陆乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>不好意思,对方设置了交流权限,<%=user.getNickNameWml()%>只愿意和登陆用户交流.想打动<%=user.getGenderText()%>,就请登陆或<a href="/register.jsp">免费注册</a>吧.<br/>
ID或手机号:<br/>
<input type="text" name="uid" format="*N" maxlength="11"/><br/>
密码:<br/>
<input type="text" name="password"  maxlength="20" value=""/><br/>
<anchor title="确定">登录
	<go href="Login.do?backTo=/user/glogin.jsp?uid=<%=user.getId()%>" method="post">
	<postfield name="uid" value="$uid"/>
	<postfield name="password" value="$password"/>
	</go>
</anchor>|<a href="/register.jsp">注册送大礼</a><br/>
<a href="/user/guestViewInfo.jsp?userId=<%=user.getId()%>">点击查看<%=user.getNickNameWml()%>更多资料</a><br/>
<%=userNameStr%><br/>
<%=user.getGender()==0?"女":"男"%>/<%if(user.getCityname() != null){%><%=user.getCityname()%><%}else{%>未知<%}%>/<%=user.getAge()%>岁<br/>
<%FriendBean friend = FriendAction.getFriendService().getFriend(user.getId());
if(friend != null && friend.getAttach()!=null && !friend.getAttach().equals("")){
%><img src="<%=net.joycool.wap.util.Constants.IMG_ROOT_URL%>/friend/attach/<%=friend.getAttach()%>" alt="o"/><br/><%
}%>
<%if (mub != null){%>心情:<img src="/beacon/mo/img/<%=mub.getType()%>.gif" alt="<%=mub.getTypeName()%>" /><%=StringUtil.toWml(StringUtil.limitString(mub.getMood(),14))%><br/><%}%>
个性签名:<%=StringUtil.toWml(user.getSelfIntroduction())%><br/>
<a href="<%=backTo%>">返回上一页.我想再转转</a><br/>
<a href="/guest/chat.jsp">您还可以和在线游客聊天</a><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>