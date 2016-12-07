<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
SwimAction action = new SwimAction(request);
action.swimming();
String tip = (String)request.getAttribute("tip");
String result = (String)request.getAttribute("result");
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
MatchRunBean matchrunbean = (MatchRunBean)request.getAttribute("matchrunbean");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
if(matchrunbean==null){
	response.sendRedirect("swimindex.jsp");
	return;
}
//刷新
String refreshUrl = ("swimming.jsp?id="+matchrunbean.getId()+"&amp;type="+matchrunbean.getType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>



<% if("wait".equals(result)) {%>
<% if(matchrunbean.getCondition() == 0) {%>
<card title="泳池争霸" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
已有<%=matchrunbean.getPeoplenumber()%>宠物参加(5人自动开始比赛)<br/>

<%//退出比赛链接
if((petUser != null)&&(matchrunbean != null)){
if((petUser.getMatchid() == matchrunbean.getId())&&(petUser.getMatchtype() == matchrunbean.getType())){
int order;
PlayerBean[] playbean = matchrunbean.getPlayer();
for(order = 0;order<action.SWIM_PLAYNUMBER;order++)
if((playbean[order] != null)&&(playbean[order].getPetid() == petUser.getId()))
break;
%>

<a href="swimmatchact.jsp?task=3&amp;return=<%=order%>">退出比赛</a><br/>
<%}}%>

==运动员名单==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.SWIM_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser != null)&&(petUser.getId() == playbean[i].getPetid())){%>

<a href="index.jsp">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>

<%}else{%>

<a href="viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>

<%}%>
(<%=playbean[i].getRank()%>级<%=action.getPetTypeName(playbean[i].getType())%>)
<br/>
<%}}}%>
<br/>
<a href="swimindex.jsp">返回泳池争霸比赛列表</a><br/>
</p>
</card>






<%} else if(matchrunbean.getCondition() == 1) {%>

<card title="泳池争霸" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%//道具
if((petUser != null)&&(matchrunbean != null)){
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.SWIM_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser.getId() == playbean[i].getPetid())&&(playbean[i].getStage()[0] != 0)){%>
<a href="swimmatchact.jsp?task=4">使用:<%=action.STAGE_SWIM[playbean[i].getStage()[0]-1]%></a>

<%if((petUser.getId() == playbean[i].getPetid())&&(playbean[i].getStage()[1] != 0)){%>
(<%=action.STAGE_SWIM[playbean[i].getStage()[1]-1]%>)
<%}%>|
<%}%>
<%}}}%>
<a href="<%=refreshUrl%>">刷新</a><br/>
<%if(matchrunbean != null){%>
<% PlayerBean[] playbean = matchrunbean.getPlayer();
	for(int i=0;i<action.SWIM_PLAYNUMBER;i++){
		if(playbean[i] != null){%>
<%=i+1%>.<a href="viewpet.jsp?id=<%=playbean[i].getPetid()%>"><%=StringUtil.toWml(playbean[i].getName())%></a>
<%if(playbean[i].getPosition() >= action.TOTAL_LONG){%>终点.<%}else{%><%=playbean[i].getPosition()%>米<%}%><br/>
<%}}}%>
==比赛花絮==<br/>
<%=matchrunbean.toString()%>

<br/>
<a href="swimindex.jsp">返回泳池争霸比赛列表</a><br/>
</p>
</card>

<%} else if(matchrunbean.getCondition() == 2) {%>

<card title="泳池争霸">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(matchrunbean != null){
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.SWIM_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser != null)&&(petUser.getId() == playbean[i].getPetid())){%>
<%=StringUtil.toWml(playbean[i].getName())%>最终排名第:<%=i+1%><br/>
<%if((petUser.getMatchid() == matchrunbean.getId())&&(petUser.getMatchtype() == matchrunbean.getType())) {%>
<a href="matchact.jsp?task=3&amp;return=<%=i%>">退出比赛</a><br/>
<%}}%>
<%}}}%>


==比赛成绩==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.SWIM_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
第<%=i+1%>名:<a href="viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
(<%=playbean[i].getRank()%>级<%=action.getPetTypeName(playbean[i].getType())%>)
<br/>
<%}}}%>
<br/>

<a href="swimindex.jsp">返回泳池争霸比赛列表</a><br/>
</p>
</card>
<%}%>




<%}else {%>
<card title="泳池争霸" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>
<br/>
<a href="swimindex.jsp">返回泳池争霸比赛列表</a><br/>
</p>
</card>
<%}%>
</wml>