<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
action.cardList(request,response);
Vector vecCard=(Vector)request.getAttribute("vecCard");
String none=(String)request.getAttribute("none");
//if(vecCard==null)
//vecCard=new Vector();
int itemCount=0;
itemCount=StringUtil.toInt((String)request.getAttribute("itemCount"));
HappyCardBean card=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
if(null!=request.getAttribute("des")){
String titlename=(String)request.getAttribute("des");%>
<card title="<%=titlename%>贺卡">
<%}else{%>
<card title="贺卡">
<%}%>
<p align="left">
<%=BaseAction.getTop(request, response)%><%=net.joycool.wap.action.auction.LuckyAction.viewPickup(6,request,response)%>
<%
if(none!=null){%>
不存在该类别<br/>
<%}else if(vecCard.size()==0)
{%>
此类别中没有贺卡<br/>
<%}else{
for(int i=0;i<vecCard.size();i++){
	card=(HappyCardBean)vecCard.get(i);
%>
<%=(itemCount+i+1)%>、<a href="/job/happycard/card.jsp?id=<%=card.getId()%>"><%=card.getTitle()%></a>(人气<%=card.getHits()%>)<br/>
<img src="<%=card.getImageUrl()%>" alt="loading..." /><br/>
<%}}%>
<%String pagination=(String)request.getAttribute("pagination");
if(!pagination.equals(""))
{%><%=pagination%><br/><%}%>
<a href="<%=("/job/happycard/index.jsp") %>">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>