<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.wxsj.framework.*"%><%
UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀">
<p align="left">
回答每道题可获得100乐币与1点经验，答完30道题将获得更高的额外奖励哦<br/>
<a href="/wxsj/knife/start.jsp">开始答题</a><br/>
<a href="/lswjs/wagerHall.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>