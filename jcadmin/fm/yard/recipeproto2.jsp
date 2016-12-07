<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 30;
%><% //0种子,1食材,2调味品,3成品
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
List itemList=YardAction.yardService.getYardRecipeProtoBeanList("1 order by id desc");
int p = action.getParameterInt("p");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);
%><html>
<link href="../common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
菜谱<br/>
<table width="100%">
	<tr>
		<td>id</td>
		<td>名称</td>
		<td>价格</td>
		<td>评估价格</td>
		<td>所需原料</td>
		<td>原料公式</td>
		<td>生产成品</td>
		<td>等级</td>
	</tr><%
for(int i =0; i<itemList.size(); i++){
	YardRecipeProtoBean item=(YardRecipeProtoBean)itemList.get(i);
	float priceSum = 0;
	
	List productList = item.getMaterialList();
	for (int j = 0; j < productList.size(); j++) {
		int[] r = (int[]) productList.get(j);
		YardItemProtoBean item2 = YardAction.getItmeProto(r[0]);
		if(item2!=null){
			priceSum += (float)item2.getPrice()*r[1]/item2.getBuyCount();
		}
	}
	%><tr>
		<td><%=item.getId()%></td>
		<td><%=item.getName()%></td>
		<td><%=(float)item.getPrice()/10%>元</td>
		<td><%=YardAction.floatFormat1((float)priceSum/10)%>元</td>
		<td><%=YardAction.getItemListString(item.getMaterialList())%></td>
		<td><%=item.getMaterial()%></td>
		<td><%=item.getProduct()%></td>
		<td><%=item.getRank()+1%></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("recipeproto2.jsp", false, "|", response, COUNT_PER_PAGE)%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>

</body>
</html>