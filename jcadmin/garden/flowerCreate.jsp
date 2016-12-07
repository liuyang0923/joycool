<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService(); %>
<html>
	<head>
		<title>花之境属性管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%	FlowerAction action = new FlowerAction(request);
	String name = request.getParameter("name");
	int price = action.getParameterInt("price");
	int time = action.getParameterInt("time");
	String comp = action.getParameterString("comp");
	int type = action.getParameterInt("type");
	int compTime = action.getParameterInt("compTime");
	int successExp = action.getParameterInt("successExp");
	int growExp = action.getParameterInt("growExp");
	int mode = action.getParameterInt("m");	//mode=0询问  mode=1增加 mode=2修改
	
	boolean result = false;
	
	if ("".equals(name) || type == 0){
		%><font color="red">花名与类型必须输入。</font><br/>
		  <a href="flowerProperty.jsp"><--返回</a><%
		return;
	}
	
	result = true;
	result = SqlUtil.executeUpdate("insert into flower_property (id,name,price,time,compose,type,comp_time,success_exp,grow_exp) values (27,'" + name + "'," + price + "," + time + ",'" + comp + "'," + type + "," + compTime + "," + successExp + "," + growExp + ")",5);
	FlowerUtil.getFlowerMap();
	if (result){
		//更新Action中的MAP列表(缓存作用)
		FlowerUtil.resetFlowerMap();
		%>操作成功。<br/><a href="flowerProperty.jsp"><--返回</a><%
	}else{
		%>操作失败。<br/><a href="flowerProperty.jsp"><--返回</a><%
	}
%></body>
</html>