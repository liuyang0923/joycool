<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.wxsj.framework.*"%><%@ page import="net.wxsj.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="java.util.ArrayList"%><%
response.setHeader("Cache-Control","no-cache");

UserBean loginUser = JoycoolInfc.getLoginUser(request);
if(loginUser == null){
	response.sendRedirect(("/user/login.jsp?backTo=" + "/wxsj/zippo/game/index.jsp"));
	return;
}

int typeId = StringUtil.toInt(request.getParameter("typeId"));
ZippoTypeBean type = ZippoFrk.getTypeById(typeId);
ArrayList zippoList = ZippoFrk.getZippoListByType(typeId);
ZippoBean zippo = null;
int i, count;
count = zippoList.size();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="猜名人专用火机">
<p align="left">
<%=StringUtil.toWml(type.getName())%><br/>
---------------<br/>
给你<%=count%>款火机，猜猜他们是哪个名人专用的吧？~<br/>
<%
for(i = 0; i < count; i ++){
	zippo = (ZippoBean) zippoList.get(i);
%>
<%=(i + 1)%>.<a href="/wxsj/zippo/game/zippo.jsp?id=<%=zippo.getId()%>"><%=StringUtil.toWml(zippo.getName())%></a>(<%=StringUtil.toWml(zippo.getSummary())%>)<br/>
<img src="<%=zippo.getImageUrl()%>" alt="图片"/><br/>
<%
}

int tCount = 0;
%>
在别的类里选：<br/>
<%if(typeId != 4){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=4">军旅风格</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%>
<%if(typeId != 1){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=1">都市风格</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%>
<%if(typeId != 6){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=6">车迷一族</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%>
<%if(typeId != 2){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=2">成功人士</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%>
<%if(typeId != 5){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=5">青春魅力</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%>
<%if(typeId != 3){%><a href="/wxsj/zippo/game/zippoList.jsp?typeId=3">个性人类</a><% tCount ++; if(tCount % 2 == 0){%><br/><%}else{%>&nbsp;<%}}%><br/>
<a href="/wxsj/zippo/game/index.jsp">返回游戏首页</a><br/>
<a href="/wxsj/zippo/rule.jsp">具体奖励和答题规则</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>