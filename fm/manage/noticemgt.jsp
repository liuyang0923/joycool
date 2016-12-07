<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%><wml><card title="家族通知"><p align="left">
家族通知是用于对家族成员发送即时通知:只发在线将收取10万乐币,发送所有或发送指定按发送人数收取(每人1万乐币),花费金额将自动从家族基金中扣除.家族通知每10分钟只能发1次.<br/>
家族通知(最多50字):<br/>
<input name="notice" maxlength="50"/><br/>
<anchor title="通知">只发在线<go href="renotice.jsp?cmd=on" method="post">
    <postfield name="notice" value="$(notice)" />
 </go></anchor>|
<anchor title="通知">发送所有<go href="renotice.jsp?cmd=all" method="post">
    <postfield name="notice" value="$(notice)" />
 </go></anchor>|
<anchor title="通知">发送指定<go href="noticeg.jsp" method="post">
	<postfield name="notice" value="$(notice)" />
  	</go></anchor><br/>
&lt;<a href="management.jsp">家族管理</a>&lt;<a href="/fm/myfamily.jsp">返回家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>