<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.guest.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30; %>
<% 	GuestAction action = new GuestAction(request);
	String tip = "";
	int guestId = 0;
	int del = action.getParameterInt("d");
	Guest guest = null;
	List changedUserList = action.getChangedUserList();
	PagingBean paging = new PagingBean(action,changedUserList.size(),COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
//	List list = action.service.getChatList(" 1 limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>游客聊天室->刚改名的游客</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%if ("".equals(tip)){
%>共有记录:<%=changedUserList.size()%>条|<a href="index.jsp">返回</a>|<a href="changed.jsp">刷新</a><br/>
<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578>游客ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>昵称</font>
					</td>
					<td align=center>
						<font color=#1A4578>UID</font>
					</td>
					<td align=center>
						<font color=#1A4578>创建时间</font>
					</td>
					<td align=center>
						<font color=#1A4578>修改</font>
					</td>
				</tr>
				<%for (int i =  paging.getStartIndex();i <  paging.getEndIndex(); i++){
					guestId = StringUtil.toInt(changedUserList.get(i).toString());
					guest = action.getGuestById(guestId);
					%><tr>
						<td><%=guest.getId() %></td>
						<td><%=guest.getNickNameWml() %></td>
						<td><%=guest.getUid() %></td>
						<td><%=DateUtil.formatSqlDatetime(guest.getCreateTime()) %></td>
						<td><a href="changed.jsp?mid=<%=guest.getId()%>" onclick="return confirm('确定修改？')">修改</a><!-- |<a href="index.jsp?c=1&mid=<%=guest.getId() %>" onclick="return confirm('真的要踢吗？')">踢</a> --></td>
			 		</tr><%
				}%>
</table><%=paging.shuzifenye("changed.jsp", false, "|", response)%><br/>
<%
} else {
	%><font color="red"><%=tip%></font><br/><a href="index.jsp">返回</a><br/><%
}%>
	</body>
</html>