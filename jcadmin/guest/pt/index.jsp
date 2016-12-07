<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*,net.joycool.wap.util.*" %><%response.setHeader("Cache-Control","no-cache"); %>
<%JigsawAction action=new JigsawAction(request,response);
String cmd=action.getParameterString("cmd");
if(cmd!=null){
	if(cmd.equals("d")){
		boolean del=action.deleteJigsawBean();
		if(del){%>
			<script type="text/javascript">
			alert('删除成功~');
			</script>
		<%}else{%>
			<script type="text/javascript">
			alert('删除失败~');
			</script>
		<%}
	}else if(cmd.equals("y")||cmd.equals("x")){
		action.updateJigsawState(cmd);
	}
}
String p="";
if(request.getParameter("p")!=null){
	p="&amp;p="+request.getParameter("p");
}
%>
<html>
	<head>
		<title>增加图片信息</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<a href="index.jsp">刷新</a>&nbsp;<a href="add.jsp">添加图片</a><br/>
<form action="index.jsp?cmd=s&s=i" method="post">按ID搜索:<input type="text" name="sid"></input><input type="submit" value="确认"></form>
<form action="index.jsp?cmd=s&s=n" method="post">按名称搜索:<input type="text" name="sn"></input><input type="submit" value="确认" ></form><br/>
  <%
  List list=new ArrayList();
  if(request.getParameter("cmd")!=null&&"s".equals(request.getParameter("cmd"))){
  	 list=action.searchJigsaw();
  }else {
  	list=action.getJigsawLists();
  }
  if(list!=null){
	  if(list.size()>0){%>
		<table width="920"  border="0" cellpadding="0" cellspacing="0">
		  <tr bgcolor=#C6EAF5>
		    <th>ID</th>
		    <th>图片名称</th>
		    <th>规格（行x列）</th>
		    <th>难度类型</th>
		    <th>最后修改时间</th>
		    <th>状态</th>
		    <th>操作</th>
		  </tr>
		<% 
		for(int i=0;i<list.size();i++){
		JigsawBean bean=(JigsawBean)list.get(i);
		%>
		  <tr>
		    <td><%=bean.getId()%></td>
		    <td><%=StringUtil.toWml(bean.getPicName())%></td>
		    <td><%=bean.getPicRows()%>x<%=bean.getPicCols()%></td>
		    <td><% int level=bean.getPicGameLevel(); if(level==1){%>普通<%}else if(level==2){%>中等<%}else if(level==3){%>困难<%}%></td>
		    <td><%=DateUtil.formatSqlDatetime(bean.getUpdateTime())%></td>
		    <td><%int picState=bean.getPicState();if(picState==1){%>显示|<a href="index.jsp?cmd=y&amp;id=<%=bean.getId()%><%=p%>">隐藏</a><%}else if(picState==0){%><a href="index.jsp?cmd=x&amp;id=<%=bean.getId()%><%=p%>">显示</a>|隐藏<%}%></td>
		    <td><a href="see.jsp?id=<%=bean.getId()%><%=p%>">预览</a>&nbsp;<a href="update.jsp?id=<%=bean.getId()%>">修改</a>&nbsp;<a href="index.jsp?cmd=d&amp;id=<%=bean.getId()%><%=p%>" onClick="return confirm('真的要删除吗?')">删除</a></td>
		  </tr>
		  <%}%>
		</table>
		<%}}else{%>
		暂无拼图信息<br/>
		<%}%>
<%if(request.getAttribute("JigsawList")!=null&&!"".equals(request.getAttribute("JigsawList"))){%>
<%=request.getAttribute("JigsawList") %>
<%}%> 
	</body>
</html>