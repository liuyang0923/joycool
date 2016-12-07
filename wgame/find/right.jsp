<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.user.RankAction"%><%
response.setHeader("Cache-Control","no-cache");
String mode = request.getParameter("mode");
if(session.getAttribute("findjoycool") != null){
session.removeAttribute("findjoycool");
int reward = 1000;
if(!mode.equals("2"))
	reward = 2000;
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserInfoUtil.updateUserStatus("game_point=game_point+" + reward,
				"user_id=" + loginUser.getId(), loginUser.getId(),0, 
				"找乐酷游戏记给用户增加" + reward + "乐币");
RankAction.addPoint(loginUser, 5);
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="一起找乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="img/logo.gif" alt="logo"/><br/>
一起找乐酷<br/>
-------------------<br/>
<%if(!mode.equals("2")){%>
您正确的访问到乐酷社区了，获得1000乐币和5经验值的奖励，若通过书签访问乐酷，会获得更多奖励哦！<br/>
<%}else{%>
您通过书签正确的访问到乐酷社区了，获得2000乐币和5经验值的奖励！注意：老用户们由于存的书签过长，在页面内的网址框内可能无法显示，请放心访问。<br/>
<%}%>
<a href="/enter/enter.jsp">马上存乐酷到书签</a><br/>
<a href="play.jsp">再来一次</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>