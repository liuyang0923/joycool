<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
action.card(request,response);
HappyCardBean card=null;
HappyCardBean nextCard=null;
String info=(String)request.getAttribute("info");
if(info==null){
card=(HappyCardBean)request.getAttribute("card");
nextCard=(HappyCardBean)request.getAttribute("nextCard");
}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发送贺卡">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null!=card){%>
<%=card.getTitle()%>(人气<%=card.getHits()%>)<br/>
<img src="<%=card.getImageUrl()%>" alt="loading..." /><br/>
<a href="/job/happycard/inGroupSend.jsp?id=<%=card.getId()%>">发给站内好友</a><br/>
<%--收卡人手机号<br/>
<input name="mobile" maxlength="11" value="" /><br/>
收卡人姓名(限4字)<br/>
<input name="receiver" maxlength="4" value="" /><br/>
您的姓名(限4字)<br/>
<input name="sender" maxlength="4" value="" /><br/>
祝福语：<br/>
<%=card.getContent()%><br/>
<anchor>提交发送
<go href="/job/happycard/singleSend.jsp" method="post">
<postfield name="mobile" value="$mobile" />
<postfield name="receiver" value="$receiver" />
<postfield name="sender" value="$sender" />
<postfield name="cardId" value="<%=card.getId()%>" />
</go>
</anchor><br/>--%>
下一张：<a href="/job/happycard/card.jsp?id=<%=nextCard.getId()%>"><%=nextCard.getTitle()%></a><br/>
<%}else{%>
您访问的贺卡不存在<br/>
<%}%>
<a href="/job/happycard/index.jsp">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
