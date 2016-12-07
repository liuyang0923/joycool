<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
%>
<html>
<body>
<a href="forsalutatory.jsp ">设置欢迎词</a><br/>
<a href="forcontent.jsp ">设置脏话内容表</a><br/>
<a href="forbuser.jsp ">设置禁止某人发言</a>|
<a href="forbuser2.jsp ">发信件</a>|
<a href="forbuser3.jsp ">发贴</a>|
<a href="forbuser4.jsp ">家园留言</a>|
<a href="forbuser5.jsp ">帮会</a>|
<a href="forbuser7.jsp ">个人资料</a>|
<a href="forbuser6.jsp ">圈子</a>|
<a href="forbuser8.jsp ">游戏</a><br/>
<a href="forchatbid.jsp">添加用户到小黑屋列表</a><br/>
<a href="roomList.jsp ">查看聊天记录</a><br/>
<a href="forcompain.jsp">查看投诉记录</a><br/>
<a href="transferRoom.jsp">转让房间</a><br/>
<a href="zjChatManager.jsp">审批自建聊天室</a><br/><hr>
审查<a href="check/homePic.jsp">家园图片</a>.<a href="check/homeDiary.jsp">家园日记</a>.<a href="check/homeDiaryr.jsp">日记回复</a>.<a href="check/homeReview.jsp">家园留言</a><br/>
<a href="check/chatPic.jsp">聊天室图片</a>.<a href="check/head.jsp">用户资料</a>.<a href="user/team.jsp">圈子聊天</a>.<a href="check/simpleChat.jsp">特殊聊天</a><br/>
<a href="check/forumPic.jsp">论坛图片</a>.<a href="check/forums.jsp">论坛主题</a>.<a href="check/forumrs.jsp">论坛回复</a><br/>
<a href="check/friendAdver.jsp">交友广告</a>.<a href="check/friendAdverMessage.jsp">广告回复</a><br/>
<a href="beacon/bottle.jsp">漂流瓶</a>.<a href="beacon/mood.jsp">心情</a>.<a href="beacon/question.jsp">缘分测试</a>.<a href="friend/help/answers.jsp">求助回复审核</a><br/>
<a href="guest/guestInfo.jsp">游客相关</a><br/>
<br/>
<a href="check/img.jsp"><b>图片先审后发</b></a><br/>
</html>