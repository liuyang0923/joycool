<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.StringUtil,java.util.List,jc.family.*"%><%@include file="../filter.jsp"%><%!
	static int NUMBER_OF_PAGE = 30;
%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
FamilyAction action=new FamilyAction(request,response);

int userId = action.getParameterInt("uid");
if(userId>0){
	response.sendRedirect("/jcadmin/user/queryUserInfo.jsp?id="+userId);
	return;
}

int fmId=action.getParameterInt("fid");
FamilyHomeBean fmHome=action.getFmByID(fmId);
if(fmHome==null){
	%><script type="text/javascript">alert('家族不存在')</script>
	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
	<%
	return;
}
jc.util.SimpleChatLog2 sc = jc.util.SimpleChatLog2.getChatLog(fmId,"fm");
PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>家族后台</title>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
<a href="chat.jsp?fid=<%=fmId%>">刷新</a><br/>
<%=sc.getChatString2(0,paging.getStartIndex(), NUMBER_OF_PAGE,"chat.jsp?a=1")%>
<%=paging.shuzifenye("chat.jsp?fid="+fmId,true, "|", response)%>
  <br/><br/>
  <a href="/jcadmin/fm/index.jsp">返回家族首页</a><br/>

  </body>
</html>