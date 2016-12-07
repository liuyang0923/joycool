<%@ page language="java" import="net.joycool.wap.spec.garden.*,java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	GardenAction gardenAction = new GardenAction(request);
	
	int id = gardenAction.getParameterInt("id");
	if(id==0)
		id = 1;

	int a = gardenAction.getParameterInt("a");
	
	if(a == 1) {
		int ids = gardenAction.getParameterInt("id");
		String name = request.getParameter("name");
		int price = gardenAction.getParameterInt("price");
		int quarter = gardenAction.getParameterInt("quarter");
		int level = gardenAction.getParameterInt("level");
		int count = gardenAction.getParameterInt("count");
		String info = request.getParameter("info");
		String grown = request.getParameter("grown");
		String grownTime = request.getParameter("grown_time");
		int exp = gardenAction.getParameterInt("exp");
		int value = gardenAction.getParameterInt("value");
		int type = gardenAction.getParameterInt("type");
		
		GardenSeedBean bean = new GardenSeedBean();
		bean.setId(ids);
		bean.setPrice(price);
		bean.setName(name);
		bean.setQuarter(quarter);
		bean.setLevel(level);
		bean.setCount(count);
		bean.setInfo(info);
		bean.setGrown(grown);
		bean.setGrowTime(grownTime);
		bean.setExp(exp);
		bean.setValue(value);
		bean.setType(type);
		
		GardenAction.gardenService.addSeed(bean);
		
		GardenService.seedIdMap.clear();
		GardenService.seedMap.clear();
	}
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="ajax.js"></script>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<style>
	
	.td80{
	width:80px;text-align:center;
	}
	.td100{
	width:100px;text-align:center;
	}
	.td150{
	width:150px;text-align:center;
	}
	
	</style>
  </head>
  
  <body>
  <%
  List types = GardenAction.gardenService.getSeedTypes();
	%>
	<%for(int i = 0; i < types.size(); i ++) {
	Object[] type = (Object[])types.get(i);%>
<%if(i > 0) {%>.<%} 
if(id == ((Integer)type[0]).intValue()) {%><%=type[1]%><%} else {%><a href="seed.jsp?id=<%=type[0]%>"><%=type[1]%></a><%} %>
<%} %><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	
  	<a href="shop.jsp#add">增加新商品</a>|<a href="shop.jsp">刷新</a>|<a href="add.jsp">新增物品</a>|<a href="pay.jsp">支付网关</a>|<a href="types.jsp">类别管理</a>|<a href="shopads.jsp">广告语</a>|<a href="discount.jsp">打折管理</a>
  	<form action="add.jsp?a=1" method="post">
  	id:<input type="text" name="id"/><br/>
  	
  	名称:<input type="text" name="name"/><br/>
  	价格:<input type="text" name="price"/><br/>
  	几季:<input type="text" name="quarter"/><br/>
  	等级:<input type="text" name="level"/><br/>
  	成熟数量:<input type="text" name="count"/><br/>
  	详细信息:<input type="text" name="info"/><br/>
  	每个季所需时间:<input type="text" name="grown"/><br/>
  	经验值:<input type="text" name="exp"/><br/>
  	作物阶段:<input type="text" name="grown_time"/><br/>
  	value:<input type="text" name="value"/><br/>
  	类型:<select name="type">
  		<%for(int i = 0; i < types.size(); i ++) {
  			Object[] type = (Object[])types.get(i);%>
  			<option value="<%=type[0] %>"><%=type[1] %></option>
  		<%}%>
  	</select>
  		<input type="submit" value="增加"/>
  	</form>
  	
  	<a name="add"></a>
  	<div id="add" style="display:block;">
  	
    </div>
  </body>
</html>
