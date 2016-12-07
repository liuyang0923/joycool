<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
action.card(request,response);
HappyCardBean card=null;
HappyCardBean nextCard=null;
card=(HappyCardBean)request.getAttribute("card");
nextCard=(HappyCardBean)request.getAttribute("nextCard");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发送贺卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null!=card){%>
<%=card.getTitle()%>(人气<%=card.getHits()%>)<br/>
<img src="<%=card.getImageUrl()%>" alt="loading..." /><br/>
<%for(int i=0;i<5;i++){ %>
<%=(i+1)%>、收卡人手机号<br/>
<input name="mobile<%=i%>" maxlength="11" value="" /><br/>
收卡人姓名<br/>
<input name="receiver<%=i%>" maxlength="4" value="" /><br/>
<%} %>
<br/>您的姓名<br/>
<input name="sender" maxlength="4" value="" /><br/>
祝福语：<br/>
<%=card.getContent()%><br/>
<anchor>提交发送
<go href="/job/happycard/outGroupSend2.jsp" method="post">
<% for(int i=0;i<5;i++){%>
<postfield name="mobile<%=i%>" value="<%="$mobile"+i%>" />
<postfield name="receiver<%=i%>" value="<%="$receiver"+i%>" />
<%}%>
<postfield name="sender" value="$sender" />
<postfield name="cardId" value="<%=card.getId()%>" />
</go>
</anchor><br/>
<%}%>
下一张：<a href="/job/happycard/card.jsp?id=<%=nextCard.getId()%>"><%=nextCard.getTitle()%></a><br/>
<a href="/job/happycard/index.jsp">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
