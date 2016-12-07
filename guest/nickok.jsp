<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page
	import="java.util.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,jc.guest.*"%>
<% response.setHeader("Cache-Control","no-cache");
GuestHallAction action = new GuestHallAction(request,response);
   String tip = "";
   GuestUserInfo guestUser = action.getGuestUser();
   if (guestUser == null){
	   response.sendRedirect("nick.jsp");
	   return;
   }
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游乐园"><p>
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=guestUser.getUserNameWml()%>,欢迎来到游乐园!<br/>
您只需<a href="pw.jsp">设置登陆密码</a>,就能直接用昵称登陆,还能保存您获得的积分和游币,还能上排行榜哦!<br/>
同时我们强烈建议您<a href="bm.jsp">保存书签</a>,这样下次登陆更方便!<br/>
<a href="/guest/chat.jsp">游客聊天室</a>|<a href="/jcforum/index.jsp">论坛</a><br/>
<a href="/guest/nb/game.jsp">名字作战</a>|<a href="/guest/pt/instructions.jsp">智慧拼图</a><br/>
<a href="/guest/question/index.jsp">问答接龙</a>|<a href="/guest/fish/index.jsp">欢乐渔场</a><br/>
<a href="/guest/dicedx/index.jsp">骰子</a>|<a href="/guest/showhand/index.jsp">梭哈</a>|<a href="/guest/tiger/index.jsp">老虎机</a><br/>
<a href="/guest/farmer/index.jsp">完美农夫</a>|<a href="/guest/sd/index.jsp">数独</a><br/>
赶快去体验一下游乐园各种有趣的功能吧～<br/>
<a href="index.jsp">返回游乐园</a><br/>
<%
} else {
	%><%=tip%><br/><a href="/lswjs/index.jsp">返回</a><br/><%
}%>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>