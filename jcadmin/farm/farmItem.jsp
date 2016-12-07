<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List tipList = new ArrayList();
if (null != request.getParameter("add")) {
	String name = action.getParameterString("name");
	
	if (!name.equals("")) {
			DummyProductBean item = new DummyProductBean();
			item.setId(action.getParameterInt("id"));
            item.setName(name);
            item.setIntroduction(action.getParameterString("introduction"));
            item.setDescription(action.getParameterString("description"));
			item.setMode(action.getParameterInt("mode"));
			item.setValue(action.getParameterInt("value"));
			item.setTime(action.getParameterInt("time"));
			item.setDummyId(action.getParameterInt("dummyId"));
			item.setPrice(action.getParameterInt("price"));
			item.setMark(action.getParameterInt("mark"));
			item.setBrush(action.getParameterInt("brush"));
			item.setBuyPrice(action.getParameterInt("buyPrice"));
			item.setBind(action.getParameterInt("bind"));
			item.setDue(action.getParameterInt("due"));
			item.setRank(action.getParameterInt("rank"));
			item.setSeq(action.getParameterInt("seq"));
			item.setStack(action.getParameterInt("stack"));
			item.setUnique(action.getParameterInt("unique"));
			item.setCooldown(action.getParameterInt("cooldown"));
			item.setAttribute("");
			item.setClass1(10);
			item.setUsage("");
			world.addItem(item);
         response.sendRedirect("farmItem.jsp");
	} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
	}
	List itemList = SqlUtil.getIntList("select id from item where dummy_id=11 and (class1=10 or class1=5) order by id", 4);
	PagingBean paging = new PagingBean(action, itemList.size(),30,"p");
	itemList = itemList.subList(paging.getStartIndex(), paging.getEndIndex());

	%>
<html>
<head>
</head>
<script src='js/tooltip.js' type='text/javascript'></script>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
	物品后台
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
			描述
		</td>
		<td>
			堆叠
		</td>
		<td>
			唯一
		</td>
		<td>
			绑定
		</td>
	</tr>
		<%for (int i = 0; i < itemList.size(); i++) {
			Integer iid = (Integer)itemList.get(i);
			DummyProductBean item = world.getItem(iid.intValue());
			tipList.add(item);
%>
	<tr>
		<td>
			<%=item.getId()%>
		</td>
		<td>
			<a onmouseover='showdscp(event,"item<%=item.getId()%>")' onmousemove='movedscp(event,"item<%=item.getId()%>")' onmouseout='hinddscp(event,"item<%=item.getId()%>")'
			href="editItem.jsp?id=<%=item.getId()%>"><%=item.getName()%></a>
		</td>
		<td>
			<%=item.getDescription()%>
		</td>
		<td>
			<%=item.getStack()%>
		</td>
		<td>
			<%=item.getUnique()%>
		</td>
		<td>
			<%=item.getBind()%>
		</td>
	</tr>
		
		<%}%>
	</table>
	<%=paging.shuzifenye("farmItem.jsp", false, "|", response,20)%>

	<a href="index.jsp">返回新手管理首页</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
<form method="post" action="farmItem.jsp?add=1">
id：<input name="id"><br/>
名称：<input name="name"><br/>
描述：<input name="description"><br/>
堆叠：<input name="stack" value="1"><br/>
唯一：<input name="unique" value="0"><br/>
绑定：<input name="bind" value="1"><br/>
<input type=hidden name="introduction" value="">
<input type=hidden name="dummyId" value="11">
<input type=hidden name="time" value="1">
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>

	<br />
<%@include file="usageList.jsp"%>
</body>
</html>
