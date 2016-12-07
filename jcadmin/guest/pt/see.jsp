<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="net.joycool.wap.util.RandomUtil,jc.guest.*"%><%@ page import="java.util.*,jc.guest.pt.*,net.joycool.wap.util.StringUtil" %><%response.setHeader("Cache-Control","no-cache"); %>
<%JigsawAction action=new JigsawAction(request,response);
JigsawBean bean = null;
int id = action.getParameterInt("id");
if (id <= 0){
	response.sendRedirect("index.jsp");
	return;
} else {
	bean = JigsawAction.service.selectOneJigsaw2(id);
	if (bean == null){
		response.sendRedirect("index.jsp");
		return;
	}
}
String p="";
if(request.getParameter("p")!=null){
	p="?p="+request.getParameter("p");
}
%>
<html>
	<head>
		<title>预览图片</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
ID:<%=bean.getId()%><br/>
图片名称:<%=StringUtil.toWml(bean.getPicName())%><br/>
图片预览:<br/>
<img src="<%=JigsawAction.ATTACH_URL_ROOT%><%=bean.getPicNum()%>.gif" alt="<%=StringUtil.toWml(bean.getPicName())%>" /><br/>
<a href="index.jsp<%=p%>">返回</a><br/>
	</body>
</html>