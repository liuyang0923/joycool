<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
HappyCardCategoryBean happyCardCategoryBean=null;
Vector vecCardCategory=null;
action.index(request,response);
vecCardCategory=(Vector)request.getAttribute("vecCardCategory");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷贺卡"> 
<p align="left">
<img src="/img/happycard.gif" alt="乐乐贺卡"/><br/>
<%=BaseAction.getTop(request, response)%>
=热门精品=<br/>
<%
HappyCardBean happyCardBean=null;
Vector vecHotCard=(Vector)request.getAttribute("vecHotCard");
for(int i=0;i<vecHotCard.size();i++){
	happyCardBean=(HappyCardBean)vecHotCard.get(i);
%>
<%=(i+1)%>、<a href="card.jsp?id=<%=happyCardBean.getId()%>"><%=happyCardBean.getTitle()%></a>(人气<%=happyCardBean.getHits()%>)<br/>
<%}%>
<a href="/job/happycard/cardList.jsp?hits=1">更多>></a><br/>
=最新上架=<br/>
<%
Vector vecNewCard=(Vector)request.getAttribute("vecNewCard");
for(int i=0;i<vecNewCard.size();i++){
	happyCardBean=(HappyCardBean)vecNewCard.get(i);
%>
<%=(i+1)%>、<a href="/job/happycard/card.jsp?id=<%=happyCardBean.getId()%>"><%=happyCardBean.getTitle()%></a>(人气<%=happyCardBean.getHits()%>)<br/>
<%}%>
<a href="/job/happycard/cardList.jsp?time=1">更多>></a><br/>
=贺卡分类=<br/>
<%
Vector vecCardType=null;
if(null!=vecCardCategory){
	for(int i=0;i<vecCardCategory.size();i++){
		happyCardCategoryBean=(HappyCardCategoryBean)vecCardCategory.get(i);
		vecCardType=action.getJobService().getHappyCardTypeList(" category_id="+happyCardCategoryBean.getId()+" order by id");
%>
[<%=happyCardCategoryBean.getName()%>]
<%
HappyCardTypeBean cardType=null;
for(int j=0;j<vecCardType.size();j++){
	cardType=(HappyCardTypeBean)vecCardType.get(j);
%><a href="/job/happycard/cardList.jsp?id=<%=cardType.getId()%>"><%=cardType.getName()%></a><%if(j<(vecCardType.size()-1)){%>|<%}%><%}%><br/>
<%}}%>

<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%--=BaseAction.getAdver(22,response)--%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
