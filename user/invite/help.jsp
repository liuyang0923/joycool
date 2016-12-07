<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="java.net.URLEncoder"%><%@ page import="net.joycool.wap.bean.MessageBean"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.spec.InviteAction"%>
<%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request,response);
String tip = "";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%>感谢您邀请朋友来乐酷!<br/>
每邀请一个好友来乐酷,让他在ME中设置,是您邀请上来的,系统就会自动将你们加为好友,并记录下他是您邀请来的.<br/>
每周邀请最多的人,进行排名,前10名第二周戴一周的皇冠哦.(周日24点为截止时间)<br/>
特别提示:如果被邀请者确认和您的邀请关系,您还将获得5000万乐币的奖励!<br/>
但是,他只有在注册后1个小时内填写邀请信息有效,注册时间超过1小时,则不给奖励.<br/>
<a href="/chat/lastRank.jsp">看看本周最有号召力榜</a><br/><br/>
<a href="invite.jsp">立即邀请</a><br/>
<a href="index.jsp">返回邀请好友页</a><br/>
<a href="/bottom.jsp">返回ME</a><br/>
<%
} else {
%><%=tip%><br/><%
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>