<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.util.*"%><%
AppAction action = new AppAction(request);
if(action.hasParam("reset")){
	AppAction.reload();
	response.sendRedirect("index.jsp");
	return;
}
List list = AppAction.service.getAppList("1");
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	组件列表
	<br />
	<br />

	<table width="100%">
	<tr>
		<td>ID</td><td></td>
		<td>名称/简介</td>
		<td>分类</td>
		<td>目录/服务器地址</td>
		<td>评论</td>
		<td>评分(人数)</td>
		<td>安装</td><td>发布时间</td>
	</tr>
	<%for (int i = 0; i < list.size(); i++) {
		AppBean bean = (AppBean) list.get(i);
%>
<tr>
	<td width="20">
		<%=bean.getId()%>
	</td>
	<td width="20"><img src="/apps/img/<%if(bean.isFlagIcon()){%><%=bean.getId()%><%}else{%>0<%}%>.gif" alt="x"/></td>
	<td>
		<a href="edit.jsp?id=<%=bean.getId()%>"><%=bean.getName()%></a>(<%=bean.getShortName()%>,<%=bean.getName2()%>)<font color=red><%if(bean.isFlagHide()){%>[隐藏]<%}%><%if(bean.isFlagTest()){%>[测试]<%}%><%if(bean.isFlagOffline()){%>[离线]<%}%><%if(bean.isFlagClose()){%>[停用]<%}%></font><br/>
		<%=StringUtil.limitString(bean.getInfo(),40)%>
	</td>
	<td width="40">
		<%=AppAction.getType(bean.getType()).getName()%>
	</td>
	<td>
		<font color="red"><b><%=bean.getDir()%></b></font>(<a href="<%=bean.getUrl()%>admin" target="blank">管理</a>)<br/>
		<%=bean.getUrl()%>
	</td>
	<td width="40">
		<%if(bean.getReplyCount()>0){%><a href="content.jsp?id=<%=bean.getId()%>"><b><%=bean.getReplyCount()%></b></a><%}else{%>0<%}%>
	</td>
	<td width="80">
		<%if(bean.getAveScore()>0){%><%=bean.getAveScoreString()%>(<a href="score.jsp?id=<%=bean.getId()%>"><b><%=bean.getScoreCount() %></b></a>)<%}else{%>0<%}%>
	</td>
	<td width="40">
		<%if(bean.getCount()>0){%><a href="add.jsp?id=<%=bean.getId()%>"><b><%=bean.getCount()%></b></a><%}else{%>0<%}%>
	</td>
	<td width="120">
		<%=DateUtil.formatDate1(new Date(bean.getCreateTime()))%>
	</td>
</tr>

<%}%>
</table><br/>
	<a href="edit.jsp">添加新组件</a><br/><br/>
	<a href="index.jsp?reset=1">清空缓存重新载入</a><br/>
	<br />
</body>
</html>
