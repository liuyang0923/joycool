<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.charitarian.CharitarianAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="net.joycool.wap.bean.UserStatusBean" %><%
response.setHeader("Cache-Control","no-cache");
CharitarianAction action = new CharitarianAction(request);
action.result(request);
String result=(String)request.getAttribute("result");
String money=(String)request.getAttribute("money");
String count=(String)request.getAttribute("count");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="慈善基金">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/charitarian/index.jsp">返回慈善基金首页</a><br/>
<a href="/charitarian/history.jsp">查看您的慈善账户</a><br/>
<a href="/charitarian/rule.jsp">慈善基金使用规则</a><br/>
<a href="/top/charitarianTop.jsp">查看慈善排行榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean userStatus =UserInfoUtil.getUserStatus(loginUser.getId());
%>
<card title="慈善基金">
<p align="left">
<%=BaseAction.getTop(request, response)%>
您捐献了<%=money%>个乐币！慈善指数加<%=count%>！非常感谢您的慷慨，将有<%=count%>个用户因您的仁慈，得到帮助！您手头上还有<%=userStatus.getGamePoint()%>个乐币！(不包括银行存款)<br/>
<a href="/charitarian/index.jsp">继续捐献</a><br/>
<a href="/charitarian/history.jsp">查看您的慈善账户</a><br/>
<a href="/charitarian/rule.jsp">慈善基金使用规则</a><br/>
<a href="/top/charitarianTop.jsp">查看慈善排行榜</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>