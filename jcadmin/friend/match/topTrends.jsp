<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=10; %>
<%
MatchAction action = new MatchAction(request);
MatchTopTrends topTrendsBean = null;
String tip = "";
int id = 0;
int add = action.getParameterInt("a");
int del = action.getParameterInt("d");
if (add > 0){
	String content = action.getParameterNoEnter("c");
	String link = action.getParameterString("l");
	if (content == null || "".equals("content")){
		tip = "没有输入内容。";
	} else if (link == null || "".equals("link")){
		tip = "没有输入连接。";
	} else {
		topTrendsBean = new MatchTopTrends();
		topTrendsBean.setContent(content);
		topTrendsBean.setLinks(link);
		int lastId = MatchAction.service.addTopTrends(topTrendsBean);
		topTrendsBean.setId(lastId);
		MatchAction.getTopTrendsMap().put(new Integer(lastId),topTrendsBean);
	}
}
if (del > 0){
	MatchAction.getTopTrendsMap().remove(new Integer(del));
	SqlUtil.executeUpdate("delete from match_top_trends where id=" + del,5);
}
List topTrends = MatchAction.getTopTrendsList();
int size = topTrends.size();
%>
<html>
	<head>
		<title>编辑置顶动态</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>目前总数:<%=size %><br/>
		<%if(!"".equals(tip)){%><%=tip%><br/><%tip="";}%>
		<form action="topTrends.jsp?a=1" method="post">
			请输入内容:<input type="text" name="c" />
			请输入连接:<input type="text" name="l" />
			<input type="submit" value="提交" />
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>连接</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<% for (int i = 0 ; i < size ; i++){
				topTrendsBean = MatchAction.getTop(((Integer)topTrends.get(i)).intValue());
				if (topTrendsBean != null){
					%><tr>
					<td><%=topTrendsBean.getContentWml()%></td>
					<td><%=topTrendsBean.getLinks() %></td>
					<td><a href="topTrends.jsp?d=<%=topTrendsBean.getId()%>" onclick="return confirm('确认删除？')">删除</a></td>
			  </tr><%
				}
			}
			%>
		</table>
	</body>
</html>