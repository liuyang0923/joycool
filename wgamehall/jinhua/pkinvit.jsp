<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgamehall.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.PositionUtil"%><%
response.setHeader("Cache-Control","no-cache");
String toUserId = (String)request.getParameter("userId");
JinhuaAction action = new JinhuaAction(request);
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean usTemp = UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="砸金花">
<p align="center">砸金花</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/jinhua.gif" alt="砸金花"/><br/>
<%
if(usTemp.getGamePoint() > 500 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=0" title="进入">5百底1千赌注局</a><br/>
<%};
if(usTemp.getGamePoint() > 1000 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=1" title="进入">1千底1万赌注局</a><br/>
<%};
if(usTemp.getGamePoint() > 2000 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=2" title="进入">2千底2万赌注局</a><br/>
<%};
if(usTemp.getGamePoint() > 5000 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=3" title="进入">5千底5万赌注局</a><br/>
<%};
if(usTemp.getGamePoint() > 10000 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=4" title="进入">1万底10万赌注局</a><br/>
<%};
if(usTemp.getGamePoint() > 100000 ) {
%>
<a href="http://wap.joycool.net/wgamehall/jinhua/invite.jsp?userId=<%=toUserId %>&amp;type=5" title="进入">10万底100万赌注局</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>
