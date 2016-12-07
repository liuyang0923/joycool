<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; 
	public static MoodService service =new MoodService();
%>
<html>
	<head>
		<title>最新心情</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			MoodAction action =new MoodAction(request);
		%>
		<table width="100%" style="border:none;">
			<tr>
				<td width=85% align=right style="border: none;">
					<form method="post" action="moodAllReply.jsp">
						<input type="submit" value="查看所有回复">
					</form>
				</td>
				<td width=15% align=right style="border:none;">
					<form method="post" action="mood.jsp?f=all">
						<input type="submit" value="查看所有心情">
					</form>				
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>用户</font></td>
				<td align=center><font color=#1A4578>心情</font></td>
				<td align=center><font color=#1A4578>创建时间</font></td>
				<td align=center><font color=#1A4578>类型</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<%
				int totalCount = SqlUtil.getIntResult("select count(*) from mood_user",5);
			    PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				List list=service.getAllLastMood(pageNow,COUNT_PRE_PAGE);
				MoodUserBean mub=null;
				for(int i=0;i<list.size();i++){
					mub=(MoodUserBean)list.get(i);
					UserBean user = UserInfoUtil.getUser(mub.getUserId());
					if (mub !=null){
						%><tr><td><a href="../user/queryUserInfo.jsp?id=<%=mub.getUserId() %>"><%=user.getNickNameWml()%></a></td><%
						%><td><%=StringUtil.toWml(mub.getMood()) %></td><%
						%><td><%=DateUtil.formatSqlDatetime(mub.getCreateTime()) %></td><%
						%><td><img src="../../beacon/mo/img/<%=mub.getType()%>.gif"/>&nbsp;<%=mub.getType()%></td><%
						%><td><a href="moodOperate.jsp?f=lastMood&uid=<%=mub.getUserId() %>" onclick="return confirm('确定删除？')">删</a>&nbsp;&nbsp;</td></tr><%
					}
				}			  
			  	%></table>
				<%=paging.shuzifenye("moodLast.jsp?uid=" + mub.getUserId(), true, "|", response)%>
	</body>
</html>