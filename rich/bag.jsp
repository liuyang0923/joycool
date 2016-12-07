<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.bag();
int act = action.getParameterInt("a");
RichUserBean richUser = action.getRichUser();
List bag = richUser.bag;
int option = action.getParameterInt("o");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(richUser.isBagTooFull()){	// 物品满了，必须丢弃
%><card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
背包东西太多啦，请选择一样丢弃：<br/>
<% for(int i = 0;i < bag.size();i++){
RichItemBean item = (RichItemBean)bag.get(i);%>
<a href="bag.jsp?a=2&amp;s=<%=i%>"><%=item.getName()%></a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%} else if(act==0){	// 显示拥有的道具
%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择要使用的道具：<br/>
<% for(int i = 0;i < bag.size();i++){
RichItemBean item = (RichItemBean)bag.get(i);%>
<a href="bag.jsp?a=1&amp;s=<%=i%>"><%=item.getName()%></a><br/>
<%}%>
<a href="go.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(action.isResult("success")){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="bag.jsp">继续使用道具</a><br/>
<a href="go.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(option > 0){
RichItemBean item = (RichItemBean)request.getAttribute("item");
int slot = action.getParameterInt("s");
String itemUrl = "bag.jsp?a=1&amp;s=" + slot + "&amp;o=" + option;
%>
<%if(item.getId()==20){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择要抢夺的道具：<br/>
<% 
RichUserBean target = (RichUserBean)request.getAttribute("target");
List bag2 = target.bag;
for(int i = 0;i < bag2.size();i++){
RichItemBean item2 = (RichItemBean)bag2.get(i);%>
<a href="<%=(itemUrl + "&amp;p="+i)%>"><%=item2.getName()%></a><br/>
<%}%>
<a href="go.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
<%}else {
RichItemBean item = (RichItemBean)request.getAttribute("item");
int slot = action.getParameterInt("s");
String itemUrl = "bag.jsp?a=1&amp;s=" + slot;%>
<%if(item.getId()==1){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择骰子的点数：<br/>
<%for(int i=1;i<=6;i++){%><a href="<%=(itemUrl + "&amp;o="+i)%>"><%=i%>点</a><br/><%}%>
<a href="bag.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(item.getId()==2 || item.getId()>=7 && item.getId()<=16){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择放的位置：<br/><%
List nodeList = action.world.getNodes(richUser);
for(int i = 0;i < nodeList.size();i++){
RichNodeBean node = (RichNodeBean)nodeList.get(i);%><a href="<%=(itemUrl + "&amp;o="+node.getId())%>"><%=node.getPattern()%><%=node.getDesc()%></a><br/><%}%>
<a href="bag.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(item.getId()==3 || item.getId()==4 || item.getId()==6 || item.getId()==17 || item.getId()==20 || item.getId()==21){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择使用的对象：<br/><%
Iterator iter = action.world.getNearbyUser(richUser).iterator();
while(iter.hasNext()){
RichUserBean target = (RichUserBean)iter.next();%><a href="<%=(itemUrl + "&amp;o="+target.getUserId())%>"><%=target.getFullName()%></a><br/><%}%>
<a href="bag.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else if(item.getId()==22){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
请选择使用的对象：<br/><%
Iterator iter = action.world.userList.iterator();
while(iter.hasNext()){
RichUserBean target = (RichUserBean)iter.next();
if(!target.isStatusPlay()){%>
<%=target.getFullName()%>(破产)<br/>
<%}else if(target.getGStatus()!=0){%>
<%=target.getFullName()%>(状态不明)<br/>
<%}else{%>
<a href="<%=(itemUrl + "&amp;o="+target.getUserId())%>"><%=target.getFullName()%></a><br/>
<%}}%>
<a href="bag.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%><%}%>
</wml>