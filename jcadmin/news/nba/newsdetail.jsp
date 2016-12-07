<%@ page language="java" import="net.joycool.wap.util.*,jc.news.nba.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	NbaAction action = new NbaAction(request);
	BeanNews bn = action.getNewById();
	int p = action.getParameterInt("p");
	String[] kind = {"暂无分类","姚明—火箭","易建联—篮网","科比—湖人","诸强动态","火热战报"};
%><html>
	<head>
		<title>新闻修改</title>
	</head>
	<body>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align="center" width="6%">
					<font color=#1A4578>醒目字段</font>
				</td>
				<td align="center" width="16%">
					<font color=#1A4578>新闻标语</font>
				</td>
				<td align="center">
					<font color=#1A4578>新闻内容</font>
				</td>
				<td align="center" width="10%">
					<font color=#1A4578>内容分类</font>
				</td>
				<td align="center" width="10%">
					<font color=#1A4578>添加时间</font>
				</td>
			</tr>
			<%
				if (bn != null) {
					if (!bn.getView().equals("")) {
			%><tr>
  				<td align=center><%=bn.getView()%></td>
				<%
					} else {
				%><td>
					无
				</td>
				<%
					}
				%><td><%=bn.getName()%></td>
				<td><%=StringUtil.toWml(bn.getCont())%></td>
  				<td align=center><%=kind[bn.getFlag()]%></td>
				<td align=center><%=DateUtil.formatDate2(bn.getCreateTime())%></td>
			</tr>
			</table>
			<%
				} else {
			%>没有这条新闻!<br/><%
				}
				if (p > 0) {
			%><a href="news.jsp?p=<%=p%>">返回新闻列表</a>
			<%
				} else {
			%><a href="news.jsp">返回新闻列表</a>
			<%
				}
			%><br />
			<a href="admin.jsp">返回NBA专题</a>
	</body>
</html>
