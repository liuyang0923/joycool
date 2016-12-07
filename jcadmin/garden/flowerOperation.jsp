<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService();%>
<%  FlowerAction action = new FlowerAction(request);
	int backId = action.getParameterInt("b");
	int uid = action.getParameterInt("uid");
	int mode = action.getParameterInt("mode");		//mode=1对种子操作  mode=2对花操作
	int operation = action.getParameterInt("ope");	//ope=1增加数量	ope=2减少数量
	List flowerTypeList = FlowerUtil.getFlowerTypeList();
	if ( backId == 1 ){
		//提交，返回到flowerIndex.jsp
		action.sendRedirect("flowerIndex.jsp?uid=" + uid,response);
		return;
	}%>
<html>
	<head>
		<title>花之境-数量操作</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<% int flowerId = action.getParameterInt("fid");
		   int maxCount = action.getParameterInt("c");
		   if ( mode==1 ){ 
				if ( operation == 1 ){ 
					%><font color="blue">[<%=FlowerUtil.getFlower(flowerId).getName()%>]</font>增加花种数量:<br/><%
				} else {
					%><font color="blue">[<%=FlowerUtil.getFlower(flowerId).getName()%>]</font>减少花种数量:<br/><%
				}
		   } else {
				if ( operation == 1 ){
					%><font color="blue">[<%=FlowerUtil.getFlower(flowerId).getName()%>]</font>增加花朵数量:<br/><%
				} else {
					%><font color="blue">[<%=FlowerUtil.getFlower(flowerId).getName()%>]</font>减少花朵数量:<br/><%
				}		   
		   }
		%>
		<form method = "post" action="flowerMess.jsp?uid=<%=uid%>&fid=<%=flowerId%>&mode=<%=mode%>&ope=<%=operation%>&b=<%=backId%>&maxC=<%=maxCount %>" >
			请输入数量:<input type="text" name="count"><br/>
			请输入数量:<select name="count2">
						<option value="0">0
					 	<option value="1">1
					 	<option value="2">2
					 	<option value="3">3
					 	<option value="4">4
						<option value="5">5
						<option value="10">10
						<option value="15">15
						<option value="20">20
					 </select><br/>
			<input type="submit" value="提交">
		</form>
		<a href="flowerDataList.jsp?b=<%=backId%>&uid=<%=uid%>"><--返回</a>
	</body>
<html>