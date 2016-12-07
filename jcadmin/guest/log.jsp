<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.guest.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30; %>
<% 	GuestAction action = new GuestAction(request);
	String tip = "";
	int del = action.getParameterInt("d");
	GuestChat chat = null;
	if (del > 0){
		// 删除
		SqlUtil.executeUpdate("update guest_chat set del=1,content='内容已被删除' where id=" + del,5);
	}
	PagingBean paging = new PagingBean(action,100000,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	List list = action.service.getChatList(" 1 limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>游客聊天室->聊天记录</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%if ("".equals(tip)){
%>共有记录:<%=list.size()%>条|<a href="index.jsp"><-返回</a>|<a href="log.jsp">刷新</a><br/>
<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户1</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户2</font>
				</td>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>删除情况</font>
				</td>
				<td align=center>
					<font color=#1A4578>FLAG</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<%if(list != null && list.size() > 0){
				for (int i = paging.getStartIndex(); i < list.size(); i++) {
					chat = (GuestChat) list.get(i);
					%><tr>
						<td><%=chat.getId()%></td>
						<td><%=chat.getNcNameWml1()%></td>
						<td><%=chat.getNcNameWml2()%></td>
						<td><%=chat.getContentWml()%></td>
						<td><%=DateUtil.formatSqlDatetime(chat.getCreateTime())%></td>
						<td><%if(chat.getDel()==0){%>未删<%}else{%><font color="red">已删</font><%}%></td>
						<td><%=chat.getFlag()%></td>
						<td><a href="log.jsp?d=<%=chat.getId()%>" onclick="return confirm('确定删除吗?')">删</a></td>
					</tr><%
				}
			}%>
	</table><%=paging.shuzifenye("log.jsp",false,"|",response)%>
<%
} else {
	%><font color="red"><%=tip%></font><br/><a href="index.jsp">返回</a><br/><%
}%>
	</body>
</html>