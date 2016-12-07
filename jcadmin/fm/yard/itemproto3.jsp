<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 30;
static String[] typeName={"种子","食材","调味品","成品","未知"};%><% //0种子,1食材,2调味品,3成品
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
List itemList=YardAction.yardService.getYardItemProtoBeanList("t_type=1 order by id desc");
int p = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);

HashMap recipeMap = new HashMap();
List recipeList = YardAction.getRecipeProtoList();
for(int i =0; i<recipeList.size(); i++){
	YardRecipeProtoBean item=(YardRecipeProtoBean)recipeList.get(i);
	List matList = item.getMaterialList();
	for(int j=0;j<matList.size();j++){
		int[] ii = (int[])matList.get(j);
		List mapList = (List)recipeMap.get(new Integer(ii[0]));
		if(mapList==null){
			mapList = new ArrayList();
			recipeMap.put(new Integer(ii[0]),mapList);
		}
		mapList.add(item);
	}
}
HashMap seedMap = new HashMap();
List seedList=YardAction.yardService.getYardItemProtoBeanList("t_type=0 order by id desc");
for(int i =0; i<seedList.size(); i++){
	YardItemProtoBean item=(YardItemProtoBean)seedList.get(i);
	List matList = item.getProductList();
	for(int j=0;j<matList.size();j++){
		int[] ii = (int[])matList.get(j);
		List mapList = (List)seedMap.get(new Integer(ii[0]));
		if(mapList==null){
			mapList = new ArrayList();
			seedMap.put(new Integer(ii[0]),mapList);
		}
		mapList.add(item);
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
		<td>产生于</td>
		<td>使用于</td>
	</tr><%
itemList = itemList.subList(paging.getStartIndex(),paging.getEndIndex());
for(int i =0; i<itemList.size(); i++){
	YardItemProtoBean item=(YardItemProtoBean)itemList.get(i);
	float priceSum = 0;
	int countSum = 0;
	
	List productList = item.getProductList();
	for (int j = 0; j < productList.size(); j++) {
		int[] r = (int[]) productList.get(j);
		YardItemProtoBean item2 = YardAction.getItmeProto(r[0]);
		if(item2!=null){
			priceSum += item2.getPrice()*r[1];
			countSum += r[1];
		}
	}
	%><tr>
		<td><%=item.getId()%></td>
		<td><%=item.getNameWml()%></td>
		<td><%=(float)item.getBasePrice()/10%></td>
		<td><%
{
List tl = (List)seedMap.get(new Integer(item.getId()));
if(tl==null){
	%>(无)<%
}else{
	for(int j = 0;j < tl.size();j++){
		YardItemProtoBean i2 = (YardItemProtoBean)tl.get(j);
		%><a href="edititemproto.jsp?id=<%=i2.getId()%>"><%=i2.getName()%></a>,<%
	}
}
}

%></td>
		<td><%

{
List tl = (List)recipeMap.get(new Integer(item.getId()));
if(tl==null){
	%>(无)<%
}else{
	for(int j = 0;j < tl.size();j++){
		YardRecipeProtoBean i2 = (YardRecipeProtoBean)tl.get(j);
		%><a href="editrecipeproto.jsp?id=<%=i2.getId()%>"><%=i2.getName()%></a>,<%
	}
}
}

%></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("itemproto3.jsp", false, "|", response, COUNT_PER_PAGE)%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>

</body>
</html>