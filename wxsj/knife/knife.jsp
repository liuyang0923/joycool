<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/knife/start.jsp"));
	return;
}

int id = StringUtil.toInt(request.getParameter("id"));

KnifeBean knife = KnifeFrk.getKnifeById(id);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="勇士爱军刀">
<p align="left">
<%=StringUtil.toWml(knife.getName())%><br/>
<img src="<%=knife.getImageUrl()%>" alt="图片"/><br/>
<%=StringUtil.toWml(knife.getIntroduction())%><br/>
<a href="/lswjs/wagerHall.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>