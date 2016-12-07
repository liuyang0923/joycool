<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.team.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; 
	public static QuestionService service =new QuestionService();
%>
<html>
	<head>
		<title>缘分测试管理</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			QuestionAction action =new QuestionAction(request);
			//String f=action.getParameterString("f");
			//int id=action.getParameterInt("id");
			
			QuestionSetBean qsb=null;
			List list=null;
			PagingBean paging=null;
		%>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>测试标题</font></td>
				<td align=center><font color=#1A4578>简单说明</font></td>
				<td align=center><font color=#1A4578>FLAG</font></td>
				<td align=center><font color=#1A4578>用户</font></td>
				<td align=center><font color=#1A4578>创建时间</font></td>
				<td align=center><font color=#1A4578>COUNT</font></td>
				<td align=center><font color=#1A4578>REPLY_COUNT</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<%	int count = SqlUtil.getIntResult("select count(id) from question_set", 3);
				paging = new PagingBean(action,count,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				list=service.getQuestionSetList(" 1 order by id desc limit " + (pageNow * COUNT_PRE_PAGE) + "," + COUNT_PRE_PAGE);
				for(int i=0;i<list.size();i++){
					qsb=(QuestionSetBean)list.get(i);
					UserBean user = UserInfoUtil.getUser(qsb.getUserId());
					if ( qsb != null ){
						%><tr><td><%=qsb.getId() %></td><%
						%><td><a href="quesList.jsp?id=<%=qsb.getId()%>"><%=StringUtil.toWml(qsb.getName())%></a></td><%
						%><td><%=StringUtil.toWml(qsb.getInfo())%></td><%
						%><td><%=qsb.getFlag()%></td><%
						%><td><a href="../user/queryUserInfo.jsp?id=<%=qsb.getUserId() %>"><%=user.getNickNameWml()%></a></td><%
						%><td><%=DateUtil.formatSqlDatetime(qsb.getCreateTime())%></td><%
						%><td><%=qsb.getCount() %></td><%
						%><td><%=qsb.getReplyCount() %></td><%
						%><td><a href="quesOperate.jsp?t=del&id=<%=qsb.getId()%>" onclick="return confirm('确定删除？')">删</a></tr><%
					}
				}%></table>
				<%=paging.shuzifenye("question.jsp?id=" + 0, true, "|", response)%>
	</body>
</html>