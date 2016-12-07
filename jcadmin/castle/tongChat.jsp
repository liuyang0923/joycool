<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%!
static int NUMBER_OF_PAGE = 20;%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	TongBean tong = CastleUtil.getTong(id);
	SimpleChatLog sc = SimpleChatLog.getChatLog("ca"+id);
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
<%PagingBean paging = new PagingBean(action, sc.size(),NUMBER_OF_PAGE,"p");

%>
<a href="tongChat.jsp?id=<%=id%>">刷新</a>|<a href="tong.jsp?id=<%=id%>">返回联盟</a><br/>
<%=sc.getChatString(paging.getStartIndex(), NUMBER_OF_PAGE)%>
<%=paging.shuzifenye("tongChat.jsp?id="+id, true, "|", response)%>
<br/><%@include file="bottom.jsp"%>
</html>