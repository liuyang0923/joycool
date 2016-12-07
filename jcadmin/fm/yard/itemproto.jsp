<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 20;
static String prefixUrl="itemproto.jsp";
static String[] typeName={"种子","食材","调味品","成品","未知"};%><% //0种子,1食材,2调味品,3成品
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
List itemList=YardAction.yardService.getYardItemProtoBeanList("1 order by id desc");
int p = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
if (action.hasParam("delete")){
	int itemid=action.getParameterInt("delete");
	if(itemid>0 && false){
		action.yardService.upd("delete from fm_yard_item_proto where id="+itemid);
		action.itemProtoCache.srm(Integer.valueOf(itemid));
		action.itemProtoList=null;
		itemList=action.getItemProtoList();
		paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
		%><script>alert("删除成功!");</script><%
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
if(action.hasParam("add")){
	String name=action.getParameterNoEnter("name");
	int capacity=action.getParameterInt("capacity");
	int buy=action.getParameterInt("buy");
	int baseprice=(int)(action.getParameterInt("baseprice")*10);
	int rank=action.getParameterInt("rank");
	int time=action.getParameterInt("time");
	int type=action.getParameterInt("type");
	String product=action.getParameterNoEnter("product");
	if(!"".equals(name)&&buy!=0&&type<4){
		YardItemProtoBean item=new YardItemProtoBean();
		item.setName(name);
		item.setPrice((baseprice));
		item.setBuyCount(buy);
		item.setType(type);
		item.setRank(rank);
		item.setBasePrice((baseprice));
		item.setTime(time);
		item.setCapacity(capacity);
		item.setProduct(product);
		action.itemProtoList=null;
		action.yardService.updateYardItemProtoBean(item,true);
		itemList=action.getItemProtoList();
		paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
物品后台<br/>
<table width="100%">
	<tr>
		<td>id</td>
		<td>名称</td>
		<td>基准价格</td>
		<td>价格</td>
		<td>等级</td>
		<td>发芽时间</td>
		<td>生产公式</td>
		<td>最大数量</td>
		<td>一次性卖数量</td>
		<td>类型</td>
		<td>操作</td>
		<td>操作</td>
	</tr><%
itemList = itemList.subList(paging.getStartIndex(),paging.getEndIndex());
for(int i =0; i<itemList.size(); i++){
	YardItemProtoBean item=(YardItemProtoBean)itemList.get(i);
	%><tr>
		<td><%=item.getId()%></td>
		<td><%=item.getNameWml()%></td>
		<td><%=(float)item.getBasePrice()/10%></td>
		<td><%=(float)item.getPrice()/10%>元</td>
		<td><%=item.getRank()%></td>
		<td><%=item.getTime()%></td>
		<td><%=YardAction.getItemListString(item.getProductList())%></td>
		<td><%=item.getCapacity()%></td>
		<td><%=item.getBuyCount()%></td>
		<td><%=typeName[item.getType()>3?4:item.getType()]%></td>
		<td><a href="edititemproto.jsp?id=<%=item.getId()%>">编辑</a></td>
		<td><a href="itemproto.jsp?delete=<%=item.getId()%>">删除</a></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye(prefixUrl, false, "|", response, COUNT_PER_PAGE)%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
<form method="post" action="itemproto.jsp?add=1">
	名称:<input id="name" name="name"><br/>
	基准价格:<input id="baseprice" name="baseprice">元<br/>
	等级:<input id="rank" name="rank"><br/>
	发芽时间:<input id="time" name="time"><br/>
	最大数量:<input id="capacity" name="capacity"><br/>
	生产公式:<input id="product" name="product"><br/>
	一次性卖数量:<input id="buy" name="buy" value="1"><br/>
	类型:<select name="type">
	<option value="0">种子</option>
	<option value="1">食材</option>
	<option value="2">调味品</option>
	<option	value="3">成品</option>
	</select><br/>
	<input type="submit" id="add" name="add" value="增加"><br/>
</form>
</body>
</html>