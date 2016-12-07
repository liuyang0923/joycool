<%@ page language="java" import="java.util.*,net.joycool.wap.bean.*,jc.answer.*,net.joycool.wap.util.*" pageEncoding="utf-8"%><%
	HelpAction action = new HelpAction(request);
	action.checkDel();
	List list = action.service.get222AnswerList(" del=0 order by id desc");
	PagingBean paging = null;
	if(list != null)
		paging = new PagingBean(action, list.size(), 30, "p");
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>回复审核</title>
  </head>
  <body>
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="5%">帖子ID</td>
		<td align="center" width="5%">用户ID</td>
		<td align="center" width="9%">用户昵称</td>
		<td align="center">回复内容</td>
		<td align="center" width="4%">操作</td>
	</tr><%
  	if(list != null){
		int count = 1;
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanAnswer ba = (BeanAnswer)list.get(i);
			if(ba != null){
		%><tr>
			<td align=center><%=ba.getId()%></td>
			<td align=center><%=ba.getUid()%></td>
			<td align=center><%=UserInfoUtil.getUser(ba.getUid()).getNickNameWml()%></td>
			<td align=left><%=StringUtil.toWml(ba.getACont())%></td>
			<td align=center><a href="answers.jsp?delaid=<%=ba.getId()%>" onclick="return confirm('你确定要删除这条回复嘛？')">删除</a></td>
		</tr><%
				count++;
			}
		}
	}
%></table><br/><%
  	if(paging != null){
  	%><%=paging.shuzifenye("answers.jsp?jcfr=1", true, "|", response)%><%
  	}
  	%>
  	<a href="index.jsp">返回求助首页</a>
  </body>
</html>
