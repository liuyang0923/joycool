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
		<%	QuestionAction action =new QuestionAction(request);
			int id=action.getParameterInt("id");
			
			QuestionBean qb=null;
			List list=null;
			PagingBean paging=null;
			String pageUrl="";
		%>
		<table width="100%" style="border:none;">
			<tr>
				<td width=85% align=right style="border: none;">
					<form method="post" action="question.jsp">
						<input type="submit" value="返  回">
					</form>
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center><font color=#1A4578>ID</font></td>
				<td align=center><font color=#1A4578>题目ID</font></td>
				<td align=center><font color=#1A4578>标题</font></td>
				<td align=center><font color=#1A4578>内容</font></td>
				<td align=center><font color=#1A4578>回答</font></td>
				<td align=center><font color=#1A4578>FLAG</font></td>
				<td align=center><font color=#1A4578>操作</font></td>
			</tr>
			<%
				int count = SqlUtil.getIntResult("select count(id) from question where set_id = " + id , 3);
				paging = new PagingBean(action,count,COUNT_PRE_PAGE,"p");
				int pageNow = paging.getCurrentPageIndex();
				list=service.getQuestionList(" set_id= " + id + " order by id desc limit " + (pageNow * COUNT_PRE_PAGE) + "," + COUNT_PRE_PAGE);
				for(int i=0;i<list.size();i++){
					qb=(QuestionBean)list.get(i);
					if ( qb != null ){
						%><tr><td><%=qb.getId() %></td><%
						%><td><%=qb.getSetId()%></td><%
						%><td><%=StringUtil.toWml(qb.getTitle())%></td><%
						%><td><%=StringUtil.toWml(qb.getContent())%></td><%
						%><td><%=qb.getAnswer() %></td><%
						%><td><%=qb.getFlag() %></td><%
						%><td><a href="quesOperate.jsp?t=dellist&id=<%=qb.getId()%>&setId=<%=id %>" onclick="return confirm('确定删除？')">删</a></tr><%
					}
				}%></table>
				<%=paging.shuzifenye("quesList.jsp?id=" + id, true, "|", response)%>
	</body>
</html>