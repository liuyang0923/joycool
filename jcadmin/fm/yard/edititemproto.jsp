<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);	
int id = action.getParameterInt("id");
if(id==0){
	response.sendRedirect("index.jsp");
	return;
}
YardItemProtoBean item=action.getItmeProto(id);
if(item==null){
	response.sendRedirect("index.jsp");
	return;
}
if(action.hasParam("edit")){
	String name=action.getParameterNoEnter("name");
	int capacity=action.getParameterInt("capacity");
	int price=(int)(action.getParameterFloat("price")*10);
	int buy=action.getParameterInt("buy");
	int baseprice=(int)(action.getParameterFloat("baseprice")*10);
	int rank=action.getParameterInt("rank");
	int time=action.getParameterInt("time");
	int type=action.getParameterInt("type");
	String product=action.getParameterNoEnter("product");
	if(!"".equals(name)&&buy!=0&&type<4){
		item.setName(name);
		item.setPrice(price);
		if(price==0)
			item.setPrice(baseprice);
		item.setBuyCount(buy);
		item.setType(type);
		item.setRank(rank);
		item.setBasePrice(baseprice);
		item.setTime(time);
		item.setCapacity(capacity);
		item.setProduct(product);
		action.itemProtoList=null;
		action.yardService.updateYardItemProtoBean(item,false);
		%><script>alert("成功");</script><%
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
%><html>
	<head>
		<link href="../common.css" rel="stylesheet" type="text/css">
	</head>
	<body>
	<form method="post" action="edititemproto.jsp?edit=1">
	<input id="id" name="id" type="hidden" value="<%=item.getId()%>"><br/>
	名称:<input id="name" name="name" value="<%=item.getName()%>"><br/>
	基准价格:<input id="baseprice" name="baseprice" value="<%=(float)item.getBasePrice()/10%>">元<br/>
	价格:<input id="price" name="price" value="<%=(float)item.getPrice()/10%>">元<br/>
	等级:<input id="rank" name="rank" value="<%=item.getRank()%>"><br/>
	发芽时间:<input id="time" name="time" value="<%=item.getTime()%>"><br/>
	最大数量:<input id="capacity" name="capacity" value="<%=item.getCapacity()%>"><br/>
	生产公式:<input id="product" name="product" value="<%=item.getProduct()%>"><br/>
	一次性卖数量:<input id="buy" name="buy" value="<%=item.getBuyCount()%>"><br/>
	类型:<select name="type">
	<option value="0" <%=item.getType()==0?"selected":""%> >种子</option>
	<option value="1" <%=item.getType()==1?"selected":""%> >食材</option>
	<option value="2" <%=item.getType()==2?"selected":""%> >调味品</option>
	<option	value="3" <%=item.getType()==3?"selected":""%> >成品</option>
	</select><br/>
	<input type="submit" id="add" name="add" value="修改"><br/>
</form>
<a href="itemproto.jsp">返回物品页</a><br/>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
</body>
</html>