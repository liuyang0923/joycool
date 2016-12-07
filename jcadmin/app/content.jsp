<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter2.jsp"%><%if(!group.isFlag(0))return;%><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.spec.app.*,net.joycool.wap.util.*,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%>
<%! static int COUNT_PRE_PAGE = 30;%><%
AppAction action = new AppAction(request);
int del=action.getParameterInt("d");
int id=action.getParameterInt("id");
int replyId=action.getParameterInt("rid");
AppBean appBean = AppAction.service.getApp(" id = " + id);
if (appBean == null){
	action.sendRedirect("index.jsp" + id,response);
	return;
}
if (del == 1 && replyId != 0){
	List list = action.service.getAppReplyList(" id = " + replyId);
	if (list.size() > 0){
		AppReplyBean replyBean = (AppReplyBean)list.get(0);
		if (replyBean != null){
			action.service.delReplyById(replyBean);
		}
		action.sendRedirect("content.jsp?id=" + id,response);
		return;
	}
}
int totalCount = appBean.getReplyCount();
PagingBean paging = new PagingBean(action, totalCount,COUNT_PRE_PAGE, "p");
int pageNow = paging.getCurrentPageIndex();
ArrayList replyList = (ArrayList)action.getReplyList(appBean,pageNow,COUNT_PRE_PAGE);
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	评论列表(共:<%=totalCount%>条)
	<br />
	<br />

<body>
	<table width="100%">
	<tr>
		<td>ID</td>
		<td>玩家</td>
		<td>评论</td>
		<td>发布时间</td>
		<td>操作</td>
	</tr>
	<%AppReplyBean bean = null;
	  for (int i = 0; i < replyList.size(); i++) {
		bean = (AppReplyBean) replyList.get(i);
%>
<tr>
	<td>
		<%=bean.getId()%>
	</td>
	<td>
		<a href="../user/queryUserInfo.jsp?id=<%=bean.getUserId()%>"><%=bean.getUserId()%></a>
	</td>
	<td>
		<%=bean.getContent()%>
	</td>
	<td>
		<%=DateUtil.sformatTime(new Date(bean.getCreateTime()))%>
	</td>
	<td>
		<a href="content.jsp?id=<%=bean.getAppId()%>&d=1&rid=<%=bean.getId()%>" onclick="return confirm('确定删除？')">删</a>
	</td>
</tr>
	
<%}%>
</table><br/><%=paging.shuzifenye("content.jsp?id=" + appBean.getId(), true, "|",response)%>
	<a href="index.jsp">返回列表</a><br/><br/>
	<br />
</body>
</html>
