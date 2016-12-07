<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.*"%><%@page import="java.util.List"%><%

FamilyAction action=new FamilyAction(request);
FamilyUserBean fmUser = action.getFmUser();
int fmId = fmUser.getFmId();
int setPos = action.getParameterIntS("p");
if(setPos>=0 && setPos < 30){
	fmUser.toggleSetting(setPos);
	FamilyAction.service.updateFmUser("setting=" + fmUser.getSetting(), "id=" + fmUser.getId());
}

%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="其他"><p align="left"><%=BaseAction.getTop(request, response)%>
【我的设置】<br/>
<a href="set.jsp?p=0">聊天室显示链接</a>(<%if(!fmUser.isSettingChatLink()){%>是<%}else{%>否<%}%>)<br/>
<br/>
<a href="noticelist.jsp">家族通知记录</a><br/>
<br/>
<a href="exit.jsp">x退出家族x</a><br/>
<br/>
&lt;<a href="myfamily.jsp">返回家族</a>&lt;<a href="index.jsp">家族首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>