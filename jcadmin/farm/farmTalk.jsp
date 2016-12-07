<%@ page contentType="text/html;charset=utf-8"%>
<%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld.getWorld();
FarmNpcWorld world = FarmNpcWorld.getWorld();
List talkList = world.talkList;

int pageIndex = action.getParameterInt("pageIndex");
PagingBean paging = new PagingBean(pageIndex,talkList.size(),20);

int j = 0;
String prefixUrl = "farmTalk.jsp";

NpcService service = new NpcService();

if (null != request.getParameter("delete")) {/*
	int id = StringUtil.toInt(request.getParameter("delete"));
	dbOp = new DbOperation();
	dbOp.init();
	dbOp
			.executeUpdate("delete from farm_map_node where id="
					+ id);
	dbOp.release();*/
}

%>
<%if (null != request.getParameter("add")) {
    int id = action.getParameterInt("id");
	String title = action.getParameterString("title");
	String content = action.getParameterString("content");
	String link = action.getParameterString("link");
	if (!title.equals("")) {
		FarmTalkBean talk = new FarmTalkBean();
		talk.setTitle(title);
		talk.setContent(content);
		talk.setLink(link);
		talk.initLink(world);
		talk.init();
		world.addTalk(talk);
      response.sendRedirect("farmTalk.jsp");
	} else {%>
<script>
	alert("请填写正确各项参数！");
	</script>
<%}
	}
	//List vec = service.getMapNodeList
	//		(" 1=1 order by id limit "
	//				+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
	List vec = talkList.subList(paging.getStartIndex(),paging.getEndIndex());
	FarmTalkBean talk = null;

	%>
<html>
<link href="common.css" rel="stylesheet" type="text/css">
<head>
</head>
<body>
对话后台
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
			内容
		</td>
		<td>
			后续内容
		</td>
		<td>
			操作
		</td>
	</tr>
	<%for (int i = 0; i < vec.size(); i++) {
		talk = (FarmTalkBean) vec.get(i);
%>
	<tr>
		<form method="post" action="farmTalk.jsp?delete=<%=talk.getId()%>">
		<td>
			<%=talk.getId()%>
		</td>
		<td>
			<%=talk.getTitle()%>
			<input type="hidden" id="id" name="id" value="<%=talk.getId() %>">
		</td>
		<td>
			<%=talk.getContent()%>
		</td>
		<td>
		<%for(int i2 = 0;i2<talk.getLinkList().size();i2++){
		FarmTalkBean talk2 = (FarmTalkBean)talk.getLinkList().get(i2);
		if(talk2==null){%>(未知)<%}else{%>
			<%=i2+1%>.<a href="editTalk.jsp?id=<%=talk2.getId()%>"><%=talk2.getTitle()%></a><br/>
		<%}}%>
		</td>
		<%/*%><td>
			<a href="farmTalk.jsp?delete=<%=talk.getId()%>">删除</a>
		</td>
		<%*/%>
		<td>
			<a href="editTalk.jsp?id=<%=talk.getId()%>">编辑</a>
		</td>
	</tr>
	</form>
	<%}%>
</table>
<%=paging.shuzifenye(prefixUrl, false, "|", response, 30)%>
<a href="index.jsp">返回新手管理首页</a><br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
<br />
<form method="post" action="farmTalk.jsp?add=1">
	名称：
	<input id="title" name="title"><br/>
	内容：
	 <textarea id="content" name="content" cols="60" rows="2"></textarea><br/>
	后续内容：
	 <input id="link" name="link"><br/>
	<input type="submit" id="add" name="add" value="增加">
	<br />
</form>
<br />
</body>
</html>
