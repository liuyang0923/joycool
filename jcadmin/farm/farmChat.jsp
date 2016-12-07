<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();		
int id = action.getParameterInt("id");
int mapId = id;
MapBean map = world.getMap(id);
if(map.getParent()!=0)
	mapId = map.getParent();

SimpleChatLog sc = SimpleChatLog.getChatLog("fm"+mapId);

%>

<html>
	<head>
	</head>
	<link href="common.css" rel="stylesheet" type="text/css">
	<body>

<%PagingBean paging = new PagingBean(action, sc.size(),30,"p");

%>
<%=sc.getChatString(paging.getStartIndex(), 30)%>
<%=paging.shuzifenye("farmChat.jsp?id="+id, true, "|", response)%><br/>

<a href="farmMap.jsp">返回上一级</a>&nbsp;<a href="farmMapNode.jsp?mapId=<%=map.getId()%>">查看结点</a>
</body>
</html>