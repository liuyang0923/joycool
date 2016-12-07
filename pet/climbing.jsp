<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.action.friend.FriendAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.friend.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.RankBean"%><%@ page import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.LoadResource"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.bean.tong.TongUserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.action.pet.*"%><%@ page import="net.joycool.wap.util.UserInfoUtil"%><%
response.setHeader("Cache-Control","no-cache");
ClimbAction action = new ClimbAction(request);
action.climbing();
String tip = (String)request.getAttribute("tip");
String result = (String)request.getAttribute("result");
PetUserBean petUser = (PetUserBean)request.getAttribute("petUser");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
MatchRunBean matchrunbean = (MatchRunBean)request.getAttribute("matchrunbean");
UserStatusBean statusBeans=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/pet/index.jsp");


//刷新
String refreshUrl = "/pet/climbing.jsp?id="+matchrunbean.getId()+"&amp;type="+matchrunbean.getType();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>



<% if("wait".equals(result)) {%>
<% if(matchrunbean.getCondition() == 0) {%>
<card title="攀岩竞高" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
已有<%=matchrunbean.getPeoplenumber()%>宠物参加(5人自动开始比赛)<br/>

<%//退出比赛链接
if((petUser != null)&&(matchrunbean != null)){
if((petUser.getMatchid() == matchrunbean.getId())&&(petUser.getMatchtype() == matchrunbean.getType())){
int order;
PlayerBean[] playbean = matchrunbean.getPlayer();
for(order = 0;order<action.CLIMB_PLAYNUMBER;order++)
if((playbean[order] != null)&&(playbean[order].getPetid() == petUser.getId()))
break;
%>

<a href="/pet/climbmatchact.jsp?task=3&amp;return=<%=order%>">退出比赛</a><br/>
<%}}%>

==运动员名单==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser != null)&&(petUser.getId() == playbean[i].getPetid())){%>

<a href="/pet/index.jsp">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>

<%}else{%>

<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>

<%}%>
(<%=playbean[i].getRank()%>级<%=action.getPetTypeName(playbean[i].getType())%>)
<br/>
<%}}}%>
<br/>
<a href="/pet/climbindex.jsp">返回泳池争霸比赛列表</a><br/>
</p>
</card>






<%} else if(matchrunbean.getCondition() == 1) {%>

<card title="攀岩竞高" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%//道具
if((petUser != null)&&(matchrunbean != null)){
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser.getId() == playbean[i].getPetid())&&(playbean[i].getStage()[0] != 0)){%>
<a href="/pet/climbmatchact.jsp?task=4">使用:<%=action.CLIMB_SWIM[playbean[i].getStage()[0]-1]%></a>

<%if((petUser.getId() == playbean[i].getPetid())&&(playbean[i].getStage()[1] != 0)){%>
(<%=action.CLIMB_SWIM[playbean[i].getStage()[1]-1]%>)
<%}%>

<br/>
<%}%>
<%}}}%>



<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(i > 2)//只显示前3名
		     	break;
			if(playbean[i] != null)
			{
%>
第<%=i+1%>名:<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
到达<%if(playbean[i].getPosition() >= action.TOTAL_LONG){%>终点<%}else{%><%=playbean[i].getPosition()%>米
<%}%>
<br/>
<%}}}%>
==比赛花絮==<br/>
<%=matchrunbean.toString()%>
==实时成绩==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%=i+1%>.
<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>
到达<%if(playbean[i].getPosition() >= action.TOTAL_LONG){%>终点.<%}else{%><%=playbean[i].getPosition()%>米
<%}%>
<br/>
<%}}}%>
<br/>
<a href="/pet/climbindex.jsp">返回攀岩竞高比赛列表</a><br/>
</p>
</card>






<%} else if(matchrunbean.getCondition() == 2) {%>

<card title="攀岩竞高">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(matchrunbean != null){
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
<%if((petUser != null)&&(petUser.getId() == playbean[i].getPetid())){%>
<%=StringUtil.toWml(playbean[i].getName())%>最终排名第:<%=i+1%><br/>
<%if((petUser.getMatchid() == matchrunbean.getId())&&(petUser.getMatchtype() == matchrunbean.getType())) {%>
<a href="/pet/matchact.jsp?task=3&amp;return=<%=i%>">退出比赛</a><br/>
<%}}%>
<%}}}%>


==比赛成绩==<br/>
<%
if(matchrunbean != null){
%>
<%
PlayerBean[] playbean = matchrunbean.getPlayer();
			for(int i=0;i<action.CLIMB_PLAYNUMBER;i++){
			if(playbean[i] != null)
			{
%>
第<%=i+1%>名:<a href="/pet/viewpet.jsp?id=<%=playbean[i].getPetid()%>">
<%=StringUtil.toWml(playbean[i].getName())%>
</a>(<%=playbean[i].getRank()%>级<%=action.getPetTypeName(playbean[i].getType())%>)
<br/>
<%}}}%>
<br/>

<a href="/pet/climbindex.jsp">返回攀岩竞高比赛列表</a><br/>
</p>
</card>
<%}%>




<%}else {%>
<card title="攀岩竞高" ontimer="<%=response.encodeURL(refreshUrl)%>">
<timer value="200"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=tip%>
<br/>
<a href="/pet/climbindex.jsp">返回攀岩竞高比赛列表</a><br/>
</p>
</card>
<%}%>
</wml>