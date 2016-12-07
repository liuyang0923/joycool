<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
List triggerList = world.triggerList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,triggerList.size(),20);

			String prefixUrl = "farmTrigger.jsp";

			%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String name = action.getParameterString("name");
	String bak = action.getParameterString("bak");
	String items = action.getParameterString("items");
	if (!name.equals("")) {
			TriggerBean trigger = new TriggerBean();
			trigger.setId(id);
            trigger.setName(name);
            trigger.setBak(bak);
            trigger.setInfo(action.getParameterString("info"));
            trigger.setEvent(request.getParameter("event"));
            trigger.setCondition(request.getParameter("condition"));
            trigger.setAction(request.getParameter("action"));
            trigger.setFlag(action.getParameterFlag("flag"));
            trigger.init();
            FarmWorld.setTrigger(trigger);
			world.addTrigger(trigger);
         response.sendRedirect("farmTrigger.jsp");
		} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			List vec = triggerList.subList(paging.getStartIndex(),paging.getEndIndex());
			TriggerBean trigger = null;

			%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
<body>
触发器
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
			事件
		</td>
		<td>
			条件
		</td>
		<td>
			执行
		</td>
		<td>
			内容
		</td>
		<td>
			标志
		</td>
		<td>
			备注
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		trigger = (TriggerBean) vec.get(i);
%>
	<tr>
		<td>
			<%=trigger.getId()%>
		</td>
		<td>
			<a href="editTrigger.jsp?id=<%=trigger.getId()%>"><%=trigger.getName()%></a>
		</td>
		<td>
			<%=FarmWorld.getEventString(trigger.getEventList())%>
		</td>
		<td>
			<%=FarmWorld.getConditionString(trigger.getConditionList())%>
		</td>
		<td>
			<%=FarmWorld.getActionString(trigger.getActionList())%>
		</td>
		<td width="500">
			<%=StringUtil.toWml(trigger.getInfo())%>
		</td>
		<td width="60">
	<%for(int flag=0;flag<TriggerBean.FLAG_COUNT;flag++){%>
	 <%if(trigger.isFlag(flag)){%><%=TriggerBean.flagString[flag]%>&nbsp;<%}%>
	 <%}%><br/>
		</td>
		<td>
			<%=trigger.getBak()%>
		</td>
	</tr>
	
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response)%>

<br />
<form method="post" action="farmTrigger.jsp?add=1">
id:<input name="id"><br/>
名称:<input name="name"><br/>
事件:<input name="event"><br/>
条件:<input name="condition"><br/>
执行:<input name="action"><br/>
标志:<%for(int flag=0;flag<TriggerBean.FLAG_COUNT;flag++){%>
	 <input type=checkbox name="flag" value="<%=flag%>"><%=TriggerBean.flagString[flag]%>
	 <%}%><br/>
显示信息:<textarea name="info" cols="60" rows="2"></textarea><br/>
备注:<textarea name="bak" cols="60" rows="2"></textarea><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>

<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
</body>
</html>
