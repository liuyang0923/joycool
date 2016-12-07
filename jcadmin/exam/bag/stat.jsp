<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.exam.bag.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%ExamAction action = new ExamAction(request);
  List list = ExamAction.service.getStatList();
  ExamStat stat = null;
  int total = 0;
%>
<html>
	<head>
		<title>备战考试</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<a href="index.jsp">返回首页</a><br/>
<font color="red">被删除的不计算在内。</font><br/>
<% for (int i = 0 ; i < list.size() ; i++){
	stat = (ExamStat)list.get(i);
	total += stat.getStat();
	%>(<%if(stat.getFlag()==1){%>初中<%}else{%>高中<%} %>)<%=ExamAction.typeString[stat.getType()]%>:<%=stat.getStat()%><br/><%
}%><font color="blue">目前总数：<%=total %>条。</font><br/>
	</body>
</html>