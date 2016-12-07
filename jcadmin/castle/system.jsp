<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int count = action.getParameterIntS("count");
	if(count!=-1)
		CastleUtil.count=count;
	int building = action.getParameterIntS("building");
	if(building!=-1)
		CastleUtil.buildingSpeedup=building;
	int product = action.getParameterIntS("product");
	if(product!=-1)
		CastleUtil.productSpeedup=product;
	int training = action.getParameterIntS("training");
	if(training!=-1)
		CastleUtil.trainingSpeedup=training;
	int moving = action.getParameterIntS("moving");
	if(moving!=-1)
		CastleUtil.movingSpeedup=moving;
		
	int protect = action.getParameterIntS("protect");
	if(protect!=-1)
		CastleUtil.PROTECT_DAY=protect;
		
	int max = action.getParameterIntS("max");
	if(max!=-1)
		ResNeed.maxCastleCount=max;
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	系统参数<br/>
	
CastleUtil.count(城堡总量) = <form action="system.jsp"><input type=text name="count" value="<%=CastleUtil.count%>"><input type=submit value="确认修改"></form>
CastleUtil.buildingSpeedup(建造倍速) = <form action="system.jsp"><input type=text name="building" value="<%=CastleUtil.buildingSpeedup%>"><input type=submit value="确认修改"></form>
CastleUtil.productSpeedup(产量倍速) = <form action="system.jsp"><input type=text name="product" value="<%=CastleUtil.productSpeedup%>"><input type=submit value="确认修改"></form>
CastleUtil.trainingSpeedup(训练倍速) = <form action="system.jsp"><input type=text name="training" value="<%=CastleUtil.trainingSpeedup%>"><input type=submit value="确认修改"></form>
CastleUtil.movingSpeedup(行军倍速) = <form action="system.jsp"><input type=text name="moving" value="<%=CastleUtil.movingSpeedup%>"><input type=submit value="确认修改"></form>
<br/>
CastleUtil.PROTECT_DAY(新手保护期) = <form action="system.jsp"><input type=text name="protect" value="<%=CastleUtil.PROTECT_DAY%>"><input type=submit value="确认修改"></form>
ResNeed.maxCastleCount(最多分城) = <form action="system.jsp"><input type=text name="max" value="<%=ResNeed.maxCastleCount%>"><input type=submit value="确认修改"></form>
<br/>
<%@include file="bottom.jsp"%>
</body>
</html>