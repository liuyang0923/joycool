<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.mentalpic.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  MentalPicAction action = new MentalPicAction(request);
	int flag = action.getParameterInt("flag");
	int del = action.getParameterInt("del");
	if (flag < 0 || flag > 1){
		flag = 0;
	}
	MentalPicQuestion question = null;
	int totalCount = 100000;
	if (del > 0){
		// 删除
		action.delQuestion(del);
	}
	PagingBean paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
	int pageNow = paging.getCurrentPageIndex();
	List list = (ArrayList)action.service.getQuestionList("flag=" + flag +" and del=0 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
		<title>删除题库</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<a href="index.jsp">返回首页</a><br/>
		<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578><%if(flag==0){%>看字选图<%}else{%>看图选字<%} %></font>
					</td>
					<td align=center>
						<font color=#1A4578>标题</font>
					</td>
					<td align=center>
						<font color=#1A4578>相关操作</font>
					</td>
				</tr>
				<%for (int i = 0;i < list.size(); i++){
					question = (MentalPicQuestion)list.get(i);
					%><tr>
						<td><%=question.getId() %></td>
						<td><%=StringUtil.toWml(question.getTitle())%></td>
						<td><a href="del.jsp?del=<%=question.getId()%>&flag=<%=flag%>" onclick="return confirm('你确定要删除“<%=StringUtil.toWml(question.getTitle()) %>”?')">删除</a></td>
			 		</tr><%
				}%>
		  </table><%=paging.shuzifenye("pwModify.jsp", false, "|", response)%>
	</body>
</html>