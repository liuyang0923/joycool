<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.spec.app.*"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.bean.*"%>
<%! static int COUNT_PRE_PAGE = 30;%><%
AppAction action = new AppAction(request);
int id=action.getParameterInt("id");
AppBean app = AppAction.service.getApp("id=" + id);
int del=action.getParameterInt("del");
if(del > 0){
	HashMap map = AppAction.getAppUserMap(del);
	AppAddBean appAdd = (AppAddBean)map.get(new Integer(app.getId()));
	if (appAdd != null){
		appAdd.setUserId(del);
		boolean result = AppAction.service.delUserApp(appAdd);
		if (result){
			map.remove(new Integer(app.getId()));
			if(app.getCount()>0)
				app.setCount(app.getCount()-1);
				SqlUtil.executeUpdate("update app set `count`=" + app.getCount() + " where id=" + app.getId(),5);
			}
		}
}
int add=action.getParameterInt("add");
if(add > 0){
	Map map = AppAction.getAppUserMap(add);
	if(!map.containsKey(new Integer(app.getId()))) {
		AppAddBean au = new AppAddBean();
		au.setUserId(add);
		au.setAppId(id);
		au.setFlag(0);
		AppAction.service.addAppUser(au);
		map.put(new Integer(id), au);
	}
}
int totalCount = SqlUtil.getIntResult("select count(id) from app_add where app_id = " + id,5);
PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
int pageNow = paging.getCurrentPageIndex();
List list = AppAction.service.getAppUserList(" app_id=" + id + " order by id desc limit " + (pageNow * COUNT_PRE_PAGE) + "," + COUNT_PRE_PAGE);
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	组件列表
	<br />
	<br />

<body>
	组件安装列表(共<%=app.getCount()%>人)
	<br />
	<br />

	<table width="300">
	<tr>
		<td>ID</td>
		<td>玩家</td>
		<td>安装时间</td><td></td>
	</tr>
	<%for (int i = 0; i < list.size(); i++) {
		AppAddBean bean = (AppAddBean) list.get(i);
%>
<tr>
	<td>
		<%=bean.getId()%>
	</td>
	<td>
		<a href="../user/queryUserInfo.jsp?id=<%=bean.getUserId()%>"><%=bean.getUserId()%></a>
	</td>
	<td width="120">
		<%=DateUtil.sformatTime(new Date(bean.getCreateTime()))%>
	</td>
	<td width=60><a href="add.jsp?id=<%=id%>&del=<%=bean.getUserId()%>&p=<%=pageNow%>" onclick="return confirm('确认删除用户安装的组件?');">删除</a></td>
</tr>
	
<%}%>
</table><br/><%=paging.shuzifenye("add.jsp?id=" + id, true, "|",response)%><br/>
<form action="add.jsp?id=<%=id%>&p=<%=pageNow%>" method=post><input type=text name="add"><input type=submit value="添加"></form><br/>
	<a href="index.jsp">返回列表</a><br/><br/>
	<br />
</body>
</html>
