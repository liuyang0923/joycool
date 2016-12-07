<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
FamilyAction action=new FamilyAction(request,response);
FamilyUserBean fmLoginUser=action.getFmUser();
if(fmLoginUser==null||fmLoginUser.getFm_id()==0){
response.sendRedirect("/fm/index.jsp");return;
}
%>
<wml><card title="解散家族"><p align="left">
请输入您的银行密码确认解散<br/>
<input name="bankpass" maxlength="20"/><br/>
<anchor title="提取">
确定
      <go href="rere.jsp" method="post">
        <postfield name="bankpass" value="$(bankpass)" />
      </go>
    </anchor><br/>
<a href="management.jsp">返回家族管理</a><br/>
<a href="/fm/myfamily.jsp">返回我的家族</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>