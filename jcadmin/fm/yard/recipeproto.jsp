<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.util.StringUtil,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.yard.*,jc.family.*"%><%@include file="../../filter.jsp"%><%!
static int COUNT_PER_PAGE = 20;
static String[] type = {"初级菜谱","中级菜谱","","","","工厂"};
static String prefixUrl="recipeproto.jsp";%><% //0种子,1食材,2调味品,3成品
response.setHeader("Cache-Control","no-cache");
if(!group.isFlag(1)) return;
YardAction action=new YardAction(request,response);
List itemList=YardAction.yardService.getYardRecipeProtoBeanList("1 order by id desc");
int p = action.getParameterInt("p");
PagingBean paging = new PagingBean(action,itemList.size(),COUNT_PER_PAGE,"p");
if (action.hasParam("delete")){
	int itemid=action.getParameterInt("delete");
	if(itemid>0 && false){
		action.yardService.upd("delete from fm_yard_recipe_proto where id="+itemid);
		action.recipeProtoCache.srm(Integer.valueOf(itemid));
		action.recipeProtoList=null;		// 清空初级食谱列表
		action.middleRecipeProtoList=null;	// 中级食谱列表
		action.worksRecipeProtoList=null;	// 工厂列表
		// itemList=action.getRecipeProtoList();
		itemList=YardAction.yardService.getYardRecipeProtoBeanList("1 order by id desc");
		paging = new PagingBean(action,itemList.size(),COUNT_PER_PAGE,"p");
		%><script>alert("删除成功!");</script><%
	}
}
if(action.hasParam("add")){
	String name=action.getParameterNoEnter("name");
	float price=action.getParameterFloat("price");
	String stuff=action.getParameterNoEnter("stuff");
	String material=action.getParameterNoEnter("material");
	String product=action.getParameterNoEnter("product");
	int time=action.getParameterInt("time");
	int rank=action.getParameterInt("rank");
	int type=action.getParameterInt("type");
	String describe=action.getParameterString("describe");
	if(!"".equals(name)&&!"".equals(material)&&!"".equals(product)&&time!=0){
		YardRecipeProtoBean item=new YardRecipeProtoBean();
		item.setName(name);
		item.setPrice((int)(price*10));
		item.setStuff(stuff);
		item.setMaterial(material);
		item.setProduct(product);
		item.setTime(time*1000);
		item.setRank(rank-1);
		item.setType(type);
		item.setDescribe(describe);
		action.recipeProtoList=null;		// 清空初级食谱列表
		action.middleRecipeProtoList=null;	// 中级食谱列表
		action.worksRecipeProtoList=null;	// 工厂列表
		action.yardService.updateYardRecipeProtoBean(item,true);
		// itemList=action.getRecipeProtoList();
		itemList=YardAction.yardService.getYardRecipeProtoBeanList("1 order by id desc");
		paging = new PagingBean(action,itemList.size(),COUNT_PER_PAGE,"p");
	}else{
		%><script>alert("请填写正确各项参数!");</script><%
	}
}
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
		<td>所需原料</td>
		<td>原料公式</td>
		<td>生产成品</td>
		<td>等级</td>
		<td>类型</td>
		<td>操作</td>
		<td>操作</td>
	</tr><%
for(int i = paging.getStartIndex(); i<paging.getEndIndex(); i++){
	YardRecipeProtoBean item=(YardRecipeProtoBean)itemList.get(i);
	%><tr>
		<td><%=item.getId()%></td>
		<td><%=item.getName()%></td>
		<td><%=(float)item.getPrice()/10%>元</td>
		<td><%=YardAction.getItemListString(item.getMaterialList())%></td>
		<td><%=item.getMaterial()%></td>
		<td><%=item.getProduct()%></td>
		<td><%=item.getRank()+1%></td>
		<td><%=type[item.getType()]%></td>
		<td><a href="editrecipeproto.jsp?id=<%=item.getId()%>">编辑</a>	</td>
		<td><a href="recipeproto.jsp?delete=<%=item.getId()%>">删除</a></td>
	</tr><%
}
%></table>
<%=paging.shuzifenye("recipeproto.jsp", false, "|", response, COUNT_PER_PAGE)%>
<a href="index.jsp">返回餐厅管理页</a><br/>
<a href="/jcadmin/fm/index.jsp">返回家族管理页</a><br/>
<form method="post" action="recipeproto.jsp?add=1">
	名称:<input id="name" name="name"><br/>
	价格:<input id="price" name="price">元<br/>
	所需原料:<input id="stuff" name="stuff"><br/>
	原料公式:<input id="material" name="material"><br/>
	生产公式:<input id="product" name="product"><br/>
	时间:<input id="time" name="time">(秒)<br/>
	等级:<input id="rank" name="rank"><br/>
	类型:<select name="type">
		<option value="0">初级菜谱</option>
		<option value="1">中级菜谱</option>
		<option value="5">工厂</option>
	</select><br/>
	描述:<textarea id="describe" name="describe" cols="60" rows="2"></textarea><br/>
	<input type="submit" id="add" name="add" value="增加"><br/>
</form>
</body>
</html>