<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 30;
static String[] typeName={"种子","食材","调味品","成品","未知"};%><% //0种子,1食材,2调味品,3成品
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
List itemList=YardAction.yardService.getYardItemProtoBeanList("t_type=0 order by id desc");
int p = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(p,itemList.size(),COUNT_PER_PAGE);

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
		<td>评估价格</td>
		<td>等级</td>
		<td>发芽时间</td>
		<td>生产公式</td>
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
		<td><%=YardAction.floatFormat1((float)priceSum/countSum/10)%>元</td>
		<td><%=item.getRank()%></td>
		<td><%=item.getTime()%></td>
		<td><%=YardAction.getItemListString(item.getProductList())%></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("itemproto2.jsp", false, "|", response, COUNT_PER_PAGE)%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>

</body>
</html>