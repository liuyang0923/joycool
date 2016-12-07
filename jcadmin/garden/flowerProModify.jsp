<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.garden.flower.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static FlowerService service =new FlowerService(); %>
<html>
	<head>
		<title>花之境属性管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%	FlowerAction action = new FlowerAction(request);
	request.setCharacterEncoding("utf8");
	String name = action.getParameterString("name");
	int price = action.getParameterInt("price");
	int time = action.getParameterInt("time");
	String comp = action.getParameterString("comp");
	int type = action.getParameterInt("type");
	int mode = action.getParameterInt("m");	//mode=1修改
	int fid = action.getParameterInt("fid");
	int compTime = action.getParameterInt("compTime");
	int successExp = action.getParameterInt("successExp");
	int growExp = action.getParameterInt("growExp");
	boolean result = false;
	if ( mode==1 ){
		%><a href="flowerProperty.jsp"><--返回</a>
		  <form method="post" action="flowerProModify.jsp">
		  		<input type="hidden" name="fid" value="<%=fid%>"/>
				花&nbsp;&nbsp;名&nbsp;&nbsp;:<input type="text" name="name" value="<%=FlowerUtil.getFlower(fid).getName() %>"/>
				价&nbsp;&nbsp;格&nbsp;&nbsp;:<input type="text" name="price" value="<%=FlowerUtil.getFlower(fid).getPrice() %>"/><br/>
				时间(秒):<input type="text" name="time" value="<%=FlowerUtil.getFlower(fid).getTime() %>"/>
				公&nbsp;&nbsp;式&nbsp;&nbsp;:<input type="text" name="comp" value="<%=FlowerUtil.getFlower(fid).getCompose()%>"/><br/>
				类&nbsp;&nbsp;别&nbsp;&nbsp;:<input type="text" name="type" value="<%=FlowerUtil.getFlower(fid).getType()%>"/><br/>
				合成时间(秒):<input type="text" name="compTime" value="<%=FlowerUtil.getFlower(fid).getCompTime()%>"/><br/>
				成就(合成后):<input type="text" name="successExp" value="<%=FlowerUtil.getFlower(fid).getSuccessExp()%>"/><br/>
				成就(种植后):<input type="text" name="growExp" value="<%=FlowerUtil.getFlower(fid).getGrowExp()%>"/><br/>
				<input type="hidden" name="m" value="2"/>
				<input type="submit" value="修改"/>
		  </form><%
	} else if(mode==2){
		result = SqlUtil.executeUpdate("update flower_property set name='" + name + "',price=" + price + ",time=" + time + ",compose='" + comp + "',type=" + type + ",comp_time = " + compTime + ",success_exp = " + successExp + ",grow_exp = " + growExp + " where id = " + fid,5);
		if (result){
			//更新Action中的MAP列表(缓存作用)
			FlowerUtil.resetFlowerMap();			
			%>操作成功。<br/><a href="flowerProperty.jsp"><--返回</a><%
		}else{
			%>操作失败。<br/><a href="flowerProperty.jsp"><--返回</a><%
		}		
		%><%
	}%>
	</body>
</html>