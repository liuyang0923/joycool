<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}

int id = StringUtil.toInt(request.getParameter("id"));
ZippoBean zippo = ZippoFrk.getZippoById(id);

int prevId = id - 1;
int nextId = id + 1;
ZippoBean prevZippo = ZippoFrk.getZippoById(prevId);
ZippoBean nextZippo = ZippoFrk.getZippoById(nextId);

if(prevZippo != null){
	if(prevZippo.getTypeId() != zippo.getTypeId()){
		prevZippo = null;
	}
}

if(nextZippo != null){
	if(nextZippo.getTypeId() != zippo.getTypeId()){
		nextZippo = null;
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用火机">
<p align="left">
<%=StringUtil.toWml(zippo.getName())%><br/>
<img src="<%=zippo.getImageUrl()%>" alt="图片"/><br/>
<%=StringUtil.toWml(zippo.getSummary())%><br/>
<%=StringUtil.toWml(zippo.getIntroduction())%><br/>
<%
if(prevZippo != null){
%>
<a href="/wxsj/zippo/game/zippo.jsp?id=<%=prevZippo.getId()%>">上一个:<%=StringUtil.toWml(prevZippo.getName())%></a><br/>
<%
}
%>
<%
if(nextZippo != null){
%>
<a href="/wxsj/zippo/game/zippo.jsp?id=<%=nextZippo.getId()%>">下一个:<%=StringUtil.toWml(nextZippo.getName())%></a><br/>
<%
}
%>
<a href="/wxsj/zippo/game/zippoList.jsp?typeId=<%=zippo.getTypeId()%>">返回上一级</a><br/>
你认为谁适合这款火机？(点击姓名确定)<br/>
<a href="/wxsj/zippo/game/chooseStar.jsp?id=1&amp;zippoId=<%=zippo.getId()%>">1.刘德华</a>&nbsp;<a href="/wxsj/zippo/game/chooseStar.jsp?id=3&amp;zippoId=<%=zippo.getId()%>">2.周杰伦</a><br/>
<a href="/wxsj/zippo/game/chooseStar.jsp?id=5&amp;zippoId=<%=zippo.getId()%>">3.张铁林</a>&nbsp;<a href="/wxsj/zippo/game/chooseStar.jsp?id=8&amp;zippoId=<%=zippo.getId()%>">4.庞龙</a><br/>
<a href="/wxsj/zippo/game/chooseStar.jsp?id=4&amp;zippoId=<%=zippo.getId()%>">5.周星驰</a>&nbsp;<a href="/wxsj/zippo/game/chooseStar.jsp?id=2&amp;zippoId=<%=zippo.getId()%>">6.张曼玉</a><br/>
<a href="/wxsj/zippo/game/chooseStar.jsp?id=7&amp;zippoId=<%=zippo.getId()%>">7.李咏</a>&nbsp;<a href="/wxsj/zippo/game/chooseStar.jsp?id=6&amp;zippoId=<%=zippo.getId()%>">8.贝克汉姆</a><br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>