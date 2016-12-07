<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmNpcWorld world = FarmNpcWorld.getWorld();
List factoryComposeList = null;
int factoryId = action.getParameterInt("factoryId");

int pageIndex = action.getParameterInt("pageIndex");
factoryComposeList = world.getFactoryComposeList(factoryId);
if(factoryId==0)
{
factoryComposeList =  world.factoryComposeList;
}
PagingBean paging = new PagingBean(pageIndex,factoryComposeList.size(),20);

String prefixUrl = "farmFactoryCompose.jsp?factoryId="+factoryId;


%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	String info = action.getParameterString("info");
	factoryId = action.getParameterInt("factoryId");
	int rank = action.getParameterInt("rank");
	int price = action.getParameterInt("price");
	int time = action.getParameterInt("time");
	String material = action.getParameterString("material");
	String product = action.getParameterString("product");
	if (!name.equals("")) {
		FactoryComposeBean factoryCompose = new FactoryComposeBean();
        factoryCompose.setName(name);
        factoryCompose.setRank(rank);
        factoryCompose.setInfo(info);
        factoryCompose.setFactoryId(factoryId);
        factoryCompose.setPrice(price);
        factoryCompose.setTime(time);
        factoryCompose.setMaterial(material);
        factoryCompose.setProduct(product);
        factoryCompose.init();
		world.addFactoryCompose(factoryCompose);
         response.sendRedirect("farmFactoryCompose.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	List vec = factoryComposeList.subList(paging.getStartIndex(),paging.getEndIndex());
	FactoryComposeBean factoryCompose = null;

	%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
加工厂的数据后台
<br />
<br />
<form method="post" action="farmFactoryCompose.jsp?add=1">
	名称：
	<input id="name" name="name"><br/>
    对应的工厂：
	 <input id="factoryId" name="factoryId"><br/>
	需要等级：
	 <input id="rank" name="rank"><br/>
	介绍：
	 <textarea name="info" cols="60" rows="2"></textarea><br/>
	需要的铜板：
	 <input id="price" name="price"><br/>
	需要的时间：
	 <input id="time" name="time"><br/>
	材料：
	 <input id="material" name="material"><br/>
	产品：
	 <input id="product" name="product"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>


<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td>
			名称
		</td>
		<td>
			对应的工厂
		</td>
		<td>
			需要等级
		</td>
		<td>
			介绍
		</td>
		<td>
			需要的铜板
		</td>
		<td>
			需要的时间
		</td>
		<td>
			材料
		</td>
		<td>
			产品
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		factoryCompose = (FactoryComposeBean) vec.get(i);
%>
	<tr>
		<td>
			<%=factoryCompose.getId()%>
		</td>
		
		<td>
			<%=factoryCompose.getName()%>
		</td>
		<td>
			<%=factoryCompose.getFactoryId()%>
		</td>
		<td>
			<%=factoryCompose.getRank()%>
		</td>
		<td>
			<%=factoryCompose.getInfo()%>
		</td>
		<td>
			<%=FarmWorld.formatMoney(factoryCompose.getPrice())%>
		</td>
		<td>
			<%=factoryCompose.getTime()%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(factoryCompose.getMaterialList())%>
		</td>
		<td>
			<%=FarmWorld.getItemListString(factoryCompose.getProductList())%>
		</td>
		<td>
			<a href="editFactoryCompose.jsp?id=<%=factoryCompose.getId()%>">编辑</a>
		</td>
	</tr>
	
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, true, "|", response)%>

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>
