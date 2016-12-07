<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.action.dhh.*" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
DhhAction action = new DhhAction(request);
action.topall();
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/dhh/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
DhhUserBean dhhUser = action.getDhhUser();
PagingBean pagingBean = (PagingBean)request.getAttribute("page");
List fsUserList = (List)request.getAttribute("fsUserList");
%>
<card title="<%=DhhAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
海商王总排行榜<br/>
----------------<br/>
<%if(dhhUser!=null){%>
你最高分为：<%=dhhUser.getHighScore()%>￥，排名第<%=request.getAttribute("count")%>。<br/>
<%}%>
<%
DhhUserBean fsUser1 = null;
UserBean user = null;
for(int i =0 ;i<fsUserList.size();i++){
	fsUser1 = (DhhUserBean)fsUserList.get(i);
	user=UserInfoUtil.getUser(fsUser1.getUserId());
	if(user==null)continue;%>
	<%=i+1+pagingBean.getCurrentPageIndex()*10%>.
	<%if(loginUser.getId()==user.getId()){%>你自己<%}else{%>
	<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=StringUtil.toWml(user.getNickName())%></a>
	<%}%>
	最高分:<%=fsUser1.getHighScore()%>￥<br/>
	
<%}%>
<%=PageUtil.shuzifenye(pagingBean, "topall.jsp", false, "|", response)%>
<%if(!dhhUser.isGameOver()){%>
<a href="/dhh/play.jsp">返回游戏</a><br/>
<%}%>
<a href="/dhh/top.jsp">查看最近排行榜</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>