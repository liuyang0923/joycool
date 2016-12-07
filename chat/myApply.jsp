<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
action.myApply(request);
String result=(String)request.getAttribute("result");
String url=("/chat/roomApply.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错处理
if("rankError".equals(result)){
%>
<card title="申请失败" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
-------------<br/>
对不起，只有10级以上的用户才可以申请建聊天室（3秒钟自动返回)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("only".equals(result)){
%>
<card title="申请失败" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
-------------<br/>
您已经申请或支持过一个聊天室（3秒钟自动返回)<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else if("success".equals(result)){
%>
<card title="申请表单">
<p align="left">
<%=BaseAction.getTop(request, response)%>
须知：您申请的聊天室，只有愿意加入的用户达到一定数量，管理员才会通过您的申请！<a href="/chat/roomApply.jsp">返回</a><br/>
聊天室名称:<input type="text" name="name" maxlength="7"/>限7字以内<br/>
聊天室主题:<input type="text" name="subject" maxlength="200"/><br/>
聊天室宣言:<input type="text" name="enounce" maxlength="200"/><br/>
    <anchor title="确定提交"><u>提交</u>
    <go href="/chat/addApply.jsp" method="post">
    <postfield name="name" value="$name"/>
    <postfield name="subject" value="$subject"/>
    <postfield name="enounce" value="$enounce"/>
    </go>
    </anchor>&nbsp;&nbsp;<a href="/chat/roomApply.jsp">返回</a><br/>
<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>