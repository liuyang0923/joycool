<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.imglib.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  ImgLibAction action = new ImgLibAction(request);
	int modify = action.getParameterInt("m");
	int id = action.getParameterInt("id");		// 图片ID
	String tip = "";
	Lib lib = action.service.getLib(" id=" + id);
	if (lib == null){
		tip = "图片不存在.";
	}
%>
<html>
	<head>
		<title>用户图库标题审查</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<% if ("".equals(tip)){
%><%=StringUtil.toWml(lib.getTitle())%><br/>
<img src="<%=action.ATTACH_URL_ROOT%>/<%=lib.getImg()%>" alt="loading..." /><br/><a href="index.jsp">返回</a><%
} else {
%><font color="red"><%=tip%></font><br/><%	
}
%>
	</body>
</html>