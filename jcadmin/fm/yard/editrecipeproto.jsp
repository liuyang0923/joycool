<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);	
int id = action.getParameterInt("id");
if(id==0){
	response.sendRedirect("index.jsp");
	return;
}
YardRecipeProtoBean item=action.getRecipeProto(id);
if(item==null){
	response.sendRedirect("index.jsp");
	return;
}
if(action.hasParam("edit")){
	String name=action.getParameterNoEnter("name");
	int price=(int)(action.getParameterFloat("price")*10);
	String stuff=action.getParameterNoEnter("stuff");
	String material=action.getParameterNoEnter("material");
	String product=action.getParameterNoEnter("product");
	int time=action.getParameterInt("time");
	int rank=action.getParameterInt("rank");
	int type=action.getParameterInt("type");
	String describe=action.getParameterString("describe");
	if(!"".equals(name)&&!"".equals(material)&&!"".equals(product)&&time!=0){
		item.setName(name);
		item.setPrice(price);
		item.setStuff(stuff);
		item.setMaterial(material);
		item.setProduct(product);
		item.setTime(time*1000);
		item.setRank(rank-1);
		item.setType(type);
		item.setDescribe(describe);
		action.recipeProtoList=null;
		action.yardService.updateYardRecipeProtoBean(item,false);
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
%><html>
	<head>
		<link href="../common.css" rel="stylesheet" type="text/css">
	</head>
	<body>
	
<form method="post" action="editrecipeproto.jsp?edit=1">
	名称:<input id="name" name="name" value="<%=item.getName()%>"><br/>
	<input id="id" name="id" type="hidden" value="<%=item.getId()%>">
	价格:<input id="price" name="price" value="<%=(float)item.getPrice()/10%>">元<br/>
	所需原料:<input id="stuff" name="stuff" value="<%=item.getStuff()%>"><br/>
	原料公式:<input id="material" name="material" value="<%=item.getMaterial()%>"><br/>
	生产公式:<input id="product" name="product" value="<%=item.getProduct()%>"><br/>
	时间:<input id="time" name="time" value="<%=item.getTime()/1000%>">秒<br/>
	等级:<input id="rank" name="rank" value="<%=item.getRank()+1%>"><br/>
	类型:<select name="type">
		<option value="0">初级菜谱</option>
		<option value="1">中级菜谱</option>
		<option value="5">工厂</option>
	</select><br/>
	描述:<textarea id="describe" name="describe" cols="60" rows="2"><%=item.getDescribe()%></textarea><br/>
	<input type="submit" id="add" name="add" value="修改"><br/>
</form>
<a href="recipeproto.jsp">返回菜谱页</a><br/>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
</body>
</html>