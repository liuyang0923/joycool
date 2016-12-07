<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.mentalpic.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  MentalPicAction action = new MentalPicAction(request);
	String tip = "";
	int pageNow = 0;
	PagingBean paging = null;
	ArrayList pwList = null;
	int totalCount = 100000;
	MentalPicQuestion question = null;
	int del = action.getParameterInt("del");
	if (del > 0){
		boolean result = action.delQuestion(del);
		if (!result){
			tip = (String)request.getAttribute("tip");
		}
	}
	if ("".equals(tip)){
		paging = paging = new PagingBean(action,totalCount,COUNT_PRE_PAGE,"p");
		pageNow = paging.getCurrentPageIndex();
		pwList = (ArrayList)action.service.getQuestionList("flag=0 and del=0 order by id desc limit " + pageNow * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	}
%>
<html>
	<head>
		<title>看字选图类列表</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<a href="index.jsp">返回首页</a>|<a href="addWordPic.jsp">增加内容</a><br/>		
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>看字选图</font>
				</td>
				<td align=center>
					<font color=#1A4578>标题</font>
				</td>
				<td align=center>
					<font color=#1A4578>相关操作</font>
				</td>
			</tr>
			<%for (int i = 0;i < pwList.size(); i++){
				question = (MentalPicQuestion)pwList.get(i);
				%><tr>
					<td><%=question.getId() %></td>
					<td><%=StringUtil.toWml(question.getTitle())%></td>
					<td><a href="wpModify2.jsp?id=<%=question.getId()%>">修改内容</a>|<a href="wpModify.jsp?del=<%=question.getId()%>" onclick="return confirm('你确定要删除“<%=StringUtil.toWml(question.getTitle()) %>”?')">删除</a></td>
		 		</tr><%
			}%>
		</table><a href="index.jsp">返回首页</a>|<a href="addWordPic.jsp">增加内容</a><br/><%=paging.shuzifenye("wpModify.jsp", false, "|", response)%>
	</body>
</html>