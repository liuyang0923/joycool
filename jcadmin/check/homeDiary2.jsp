<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="java.io.*"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.action.home.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.FileUtil"%><%@ page import="net.joycool.wap.bean.home.*"%><%
response.setHeader("Cache-Control","no-cache");

HomeAction action= new HomeAction(request);
HomeDiaryBean homeDiary=null;
	int p =action.getParameterInt("p");

	int id =action.getParameterInt("id");
	homeDiary=action.getHomeService().getHomeDiary("id="+id);

%>
<html>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
<form action="homeDiary.jsp?id=<%=id%>&p=<%=p%>" method="post">
<textarea name="title" cols="60" rows="2"><%=StringUtil.toWml(homeDiary.getTitel())%></textarea><br/>
<textarea name="content" cols="60" rows="5"><%=StringUtil.toWml(homeDiary.getContent())%></textarea><br/>
<input type=submit value="确认修改"><br/>
</form><br/>
<a href="homeDiary.jsp?p=<%=p%>">返回</a><br/>
</body>
</html>