<%@ page language="java" import="java.util.*,net.joycool.wap.bean.*,jc.answer.*,net.joycool.wap.util.*" pageEncoding="utf-8"%><%
	HelpAction action = new HelpAction(request);
	action.checkDel();
	List list = action.service.get222ProblemList(" del=0 order by id desc");
	PagingBean paging = null;
	if(list != null)
		paging = new PagingBean(action, list.size(), 30, "p");
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>所有帖子</title>
  </head>
  <body>	
  <table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align="center" colspan="6"><a href="helps.jsp">更新</a>
		</td>
	</tr>
	<tr bgcolor=#C6EAF5>
		<td align="center" width="4%">
			<font color=#1A4578>编号</font>
		</td>
		<td align="center" width="6%">
			<font color=#1A4578>帖子ID</font>
		</td>
		<td align="center" width="15%">
			<font color=#1A4578>标题</font>
		</td>
		<td align="center" width="20%">
			<font color=#1A4578>奖励</font>
		</td>
		<td align="center">
			<font color=#1A4578>帖子内容</font>
		</td>
		<td align="center" width="5%">	
			<font color=#1A4578>操作</font>
		</td>
	</tr><%
  	if(list != null){
		int count = 1;
		for(int i=paging.getStartIndex();i<paging.getEndIndex();i++){
			BeanProblem bp = (BeanProblem)list.get(i);
			if(bp != null){
		%><tr>
			<td align=center><%=count%></td>
			<td align=center><%=bp.getId()%></td>
			<td align=left><a href="help.jsp?pid=<%=bp.getId()%>"><%=StringUtil.toWml(bp.getPTitle())%></a>(<%=bp.getNumReply()%>/<%=bp.getNumView()%>)<%=DateUtil.formatDate2(bp.getLastReplyTime())%></td>
			<td align=left><%=StringUtil.toWml(bp.getPrize())%></td>
			<td align=left><%=StringUtil.toWml(bp.getPCont())%></td>
			<td align=center><a href="helps.jsp?delpid=<%=bp.getId()%>" onclick="return confirm('你确定要删除“<%=StringUtil.toWml(bp.getPTitle())%>”这个帖子嘛？')">删除</a></td>
		</tr><%
				count++;
			}
		}
	}
%></table><br/><%
  	if(paging != null){
  	%><%=paging.shuzifenye("helps.jsp?jcfr=1", true, "|", response)%><%
  	}
  	%>
  	<a href="index.jsp">返回求助首页</a>
  </body>
</html>
