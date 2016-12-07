<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.buyResult(request);
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
<%=request.getAttribute("tip") %>(3秒后跳转其它场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refurbish")){
out.clearBuffer();
BaseAction.sendRedirect("/pk/index.jsp",response);
return;
}else if(result.equals("error")){
String type = (String)request.getAttribute("type");
String npcId=(String)request.getAttribute("npcId");
url=("/pk/npcInfo.jsp?npcId="+npcId+"&amp;type="+type);
%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转售卖页面)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<a href="/pk/sceneList.jsp">其它场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
int type = StringUtil.toInt((String)request.getAttribute("type"));
String npcId=(String)request.getAttribute("npcId");
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
//装备
if(type==PKAction.PK_EQUIP){
%><%=request.getAttribute("tip") %><br/>
 <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回购买页面</a><br/>
<%}//物品
else if(type==PKAction.PK_MEDICINE){
%><%=request.getAttribute("tip") %><br/>
 <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回购买页面</a><br/>
<%}//主动技能
else if(type==PKAction.PK_USER_SKILL){
%><%=request.getAttribute("tip") %><br/>
 <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回购买页面</a><br/>
<%}//被动技能
else if(type==PKAction.PK_USER_BSKILL){
%><%=request.getAttribute("tip") %><br/>
 <a href="/pk/npcInfo.jsp?npcId=<%=npcId%>&amp;type=<%=type%>">返回购买页面</a><br/>
<%}%> 
<a href="/pk/userBSkillList.jsp">我的内功</a>
<a href="/pk/userSkillList.jsp">我的外功</a><br/>
<a href="/pk/pkUserBag.jsp">我的行囊</a>
<a href="/pk/index.jsp">返回入口</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>