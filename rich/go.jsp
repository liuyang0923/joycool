<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request,response);%><%@include file="filter.jsp"%><%
RichUserBean richUser = action.getRichUser();

RichNodeBean node = action.world.getNode(richUser);
int act = action.getParameterInt("a");

if(act==0&&richUser.readLog() > 0) {	// 有未读log
action.innerRedirect("log.jsp?a=0");
return; }
if(!richUser.isStatusPlay() && act==1) {	// 不在游戏状态
action.innerRedirect("go.jsp?a=0");
return; }
if(act == 1 && action.getDelay() > 0) {	// 需要等待，未冷却
action.innerRedirect("wait.jsp?a=0");
return; }
if(richUser.isBagTooFull()) {
action.innerRedirect("bag.jsp?a=0");
return; }
if(richUser.isInBank()) {
action.innerRedirect("bank.jsp?a=0");
return; }
if(richUser.isInShop() && node.getType()==RichNodeBean.TYPE_SHOP) {
action.innerRedirect("shop.jsp?a=0");
return; }
if(richUser.isInHouse() && node.getType()==RichNodeBean.TYPE_HOUSE) {
action.innerRedirect("house.jsp?a=0");
return; }
if(richUser.isInBusiness() && node.getType()==RichNodeBean.TYPE_BUSINESS) {
action.innerRedirect("business.jsp?a=0");
return; }
if(richUser.isInSpy() && node.getType()==RichNodeBean.TYPE_SPY) {
action.innerRedirect("spy.jsp?a=0");
return; }
if(richUser.isInMagic() && node.getType()==RichNodeBean.TYPE_MAGIC) {
action.innerRedirect("magic.jsp?a=0");
return; }
if(richUser.isInCourt() && node.getType()==RichNodeBean.TYPE_COURT) {
action.innerRedirect("court.jsp?a=0");
return; }
if(richUser.isInGame1() && node.getType()==RichNodeBean.TYPE_GAME1) {
action.innerRedirect("game1.jsp?a=0");
return; }
if(richUser.isInGame2() && node.getType()==RichNodeBean.TYPE_GAME2) {
action.innerRedirect("game2.jsp?a=0");
return; }
if(richUser.isInGame3() && node.getType()==RichNodeBean.TYPE_GAME3) {
action.innerRedirect("game3.jsp?a=0");
return; }

action.go();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(act==0){	// 无操作，显示地图
List nodeList = (List)request.getAttribute("nodeList"); %>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(richUser.isStatusPlay()){%><%if(action.getDelay() > 0){%>
<a href="go.jsp?a=1">GO</a>(还有<%=action.getDelay()%>毫秒)<br/>
<%}
if(action.getDelay() <= 0){%>
<a href="go.jsp?a=1">GO</a>|<a href="info.jsp">信息</a>|<a href="bag.jsp">道具</a>|<a href="map.jsp">地图</a>|<a href="log.jsp">日志</a><br/>
<%}else if(richUser.getGStatus()==0){%>
GO|<a href="info.jsp">信息</a>|<a href="bag.jsp">道具</a>|<a href="map.jsp">地图</a>|<a href="log.jsp">日志</a><br/>
<%}%><%}else{%>
已经破产，等下一局开始吧<br/>
<a href="map.jsp">地图</a>|<a href="log.jsp">日志</a><br/>
<%}%>
<% for(int i = 0;i < nodeList.size();i++){
RichNodeBean node2 = (RichNodeBean)nodeList.get(i);
%><%if(node==node2){%><%=richUser.getDirection()%><%}%><a href="view.jsp?n=<%=node2.getId()%>"><%=node2.getPattern()%><%=node2.getDesc()%></a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{
int dice = action.getAttributeInt("dice");%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您掷出了<%=dice%><br/>
<a href="go.jsp">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>