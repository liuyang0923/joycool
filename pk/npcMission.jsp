<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKNpcBean" %><%@ page import="net.joycool.wap.bean.pk.PKMissionBean" %><%@ page import="java.util.List" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.npcMission(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKNpcBean pkNpc =(PKNpcBean)request.getAttribute("pkNpc");
List mStartList = (List)request.getAttribute("mStartList");
List mAcceptList = (List)request.getAttribute("mAcceptList");
PKMissionBean pkMission = null;
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/mission.gif" alt="任务"/><br/>
乐酷老油条：<br/>
朋友，我来乐酷混久了人缘不错，有人托我找人帮忙做些事情，都是危险的事情，不要命的话你就去做吧！<br/>
<%
if(mAcceptList.size()>0){
for(int i=0;i<mAcceptList.size();i++){
	pkMission = (PKMissionBean)mAcceptList.get(i);
%>
<%=i+1%>.
<%=pkMission.getName()%>
<a href="/pk/mAccept.jsp?npcId=<%=pkNpc.getId()%>&amp;mId=<%=pkMission.getId()%>">查看任务详情</a><br/>
<%}
}else{%>
您有任务可以接受！<br/>
<%}%>
<br/>接受中的任务:<br/>
<%
if(mStartList.size()>0){
for(int i=0;i<mStartList.size();i++){
	pkMission = (PKMissionBean)mStartList.get(i);
%>
<%=i+1%>.
<%=pkMission.getName()%>
<a href="/pk/cMission.jsp?npcId=<%=pkNpc.getId()%>&amp;mId=<%=pkMission.getId()%>">提交任务</a><br/>
<%}
}else{%>
您没有接受任务！<br/>
<%}%><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>