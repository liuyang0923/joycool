<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; 
	public static MoodService service =new MoodService();
%>
<html>
	<head>
		<title>全部心情回复</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			MoodAction action =new MoodAction(request);
			int id=action.getParameterInt("id");
		%>
		<table width="100%" style="border: none;">
			<tr>
				<td width=85% align=right style="border: none;">
					<form method="post" action="moodLast.jsp">
						<input type="submit" value="查看最新心情">
					</form>				
				</td>
				<td width=15% align=right style="border: none;">
					<form method="post" action="mood.jsp?f=all">
						<input type="submit" value="查看所有心情">
					</form>
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>心情ID</font></td>
				<td align=center><font color=#1A4578>用户</font></td>
				<td align=center><font color=#1A4578>创建时间</font></td>
				<td align=center><font color=#1A4578>回复内容</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<%
				int totalCount = SqlUtil.getIntResult("select count(*) from mood_reply",5);
			    PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				List list=service.getAllMoodReply(pageNow,COUNT_PRE_PAGE);
				MoodReply mr=null;
				for(int i=0;i<list.size();i++){
					mr=(MoodReply)list.get(i);
					UserBean user = UserInfoUtil.getUser(mr.getUserId());
					if (mr !=null){
						%><tr><td><%=mr.getId() %></td><%
						%><td><%=mr.getMoodId() %></td><%
						%><td><a href="../user/queryUserInfo.jsp?id=<%=mr.getUserId() %>"><%=user.getNickNameWml()%></a></td><%
						%><td><%=DateUtil.formatSqlDatetime(mr.getCreateTime()) %></td><%
						%><td><%=StringUtil.toWml(mr.getReply()) %></td><%
						%><td><a href="moodOperate.jsp?f=allReply&id=<%=mr.getId() %>" onclick="return confirm('确定删除？')">删</a>&nbsp;&nbsp;</td></tr><%
					}
				}			  
			  	%></table>
				<%=paging.shuzifenye("moodAllReply.jsp?id=" + id, true, "|", response)%>
	</body>
</html>