<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
String info=null;
HappyCardBean nextCard=null;
if(null==request.getParameter("sender"))
{
info="请输入您的姓名!";
}else{
HappyCardAction action=new HappyCardAction();
action.outGroupSendCard(request,response);
info=(String)request.getAttribute("info");
}
if(info==null){
int inviteCount=0;
inviteCount=StringUtil.toInt((String)request.getAttribute("inviteCount"));
//response.sendRedirect("/job/happycard/sendOk.jsp?count="+inviteCount);
BaseAction.sendRedirect("/job/happycard/sendOk.jsp?count="+inviteCount, response);
return;
}
nextCard=(HappyCardBean)request.getAttribute("nextCard");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发送贺卡" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
发送失败！<%if(null!=info){%><%=info%><%}%><br/>
<a href="#" >返回群发</a><br/>
<a href="#" >站内发送</a><br/>
下一张：<a href="/job/happycard/card.jsp?id=<%=nextCard.getId()%>"><%=nextCard.getTitle()%></a><br/>
<a href="/job/happycard/index.jsp">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
