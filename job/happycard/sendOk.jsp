<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.HappyCardAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
HappyCardAction action=new HappyCardAction();
int inviteCount=StringUtil.toInt(request.getParameter("count"));
if(inviteCount<=0)
inviteCount=0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="发送贺卡" ontimer="<%=response.encodeURL("/job/happycard/index.jsp")%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(null==request.getParameter("inGroupSend")){%>
发送成功！您今天还能发送<%=(HappyCardAction.DAY_INVITE_COUNT-inviteCount)%>张贺卡
<%}else{%>发送成功！<%}%><br/>
(3秒后跳转回贺卡首页)<br/>
<a href="/job/happycard/index.jsp">返回贺卡首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
