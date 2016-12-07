<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List tipList = new ArrayList();
List itemSetList = world.itemSetList;
if (null != request.getParameter("add")) {
String name = action.getParameterString("name");

if (!name.equals("")) {
	ItemSetBean item = new ItemSetBean();
	item.setId(action.getParameterInt("id"));
    item.setName(name);
    item.setInfo(request.getParameter("info"));
    item.setItems(request.getParameter("items"));
    item.setAttribute(request.getParameter("attribute"));
    item.setCount(request.getParameter("count"));
    item.init();
	world.addItemSet(item);
	world.initItemSet();
     response.sendRedirect("farmItemSet.jsp");
} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	PagingBean paging = new PagingBean(action, itemSetList.size(),20,"p");
	itemSetList = itemSetList.subList(paging.getStartIndex(), paging.getEndIndex());

	%>
<html>
	<head>
	</head>
<script src='js/tooltip.js' type='text/javascript'></script>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
套装物品后台
<br />

<table width="100%">
	<tr>
		<td>
			id
		</td>
		<td>
			名称
		</td>
		<td>
			物品
		</td>
		<td>
			属性
		</td>
		<td>
			参考数据
		</td>
		<td>
			描述
		</td>
	</tr>
	<%for (int i = 0; i < itemSetList.size(); i++) {
		ItemSetBean set = (ItemSetBean)itemSetList.get(i);
%>
	<tr>
		<td width=20>
			<%=set.getId()%>
		</td>
		<td width=100>
			<a href="editItemSet.jsp?id=<%=set.getId()%>"><%=set.getName()%></a>
		</td>
		<td width=500>
<% List itemList = set.getItemList();
for(int i2 = 0;i2 < itemList.size();i2++){
Integer iid = (Integer)itemList.get(i2);
DummyProductBean item=FarmWorld.getItem(iid.intValue());
if(item == null){
%>(未知)<%}else{ tipList.add(item);%>
<a  onmouseover='showdscp(event,"item<%=item.getId()%>")' onmousemove='movedscp(event,"item<%=item.getId()%>")' onmouseout='hinddscp(event,"item<%=item.getId()%>")'
href="editEquip.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>[<%=item.getGradeName()%>]&nbsp;等级<%=item.getRank()%>&nbsp;<%=FarmUtil.getRefData(item)%>
<%}%>
<br/>
<%}%>
		</td>
		<td width=150>
			<%=FarmWorld.itemSetString(set,0)%>
		</td>
		<td width=100><%=FarmUtil.getSetRefData(set)%></td>
		<td>
			<%=set.getInfo()%>
		</td>
	</tr>
	<%}%>
</table>
<%=paging.shuzifenye("farmItemSet.jsp", false, "|", response)%>

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
		<form method="post" action="farmItemSet.jsp?add=1">
id：<input name="id"><br/>
名称：<input name="name"><br/>
描述：<input name="info"><br/>
物品：<input name="items"><br/>
属性：<input name="attribute"><br/>
数量：<input name="count"><br/>

	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>

<br />
<%@include file="tipList.jsp"%>
	</body>
</html>
