<%@ page language="java" pageEncoding="utf-8" import="net.joycool.wap.framework.BaseAction,net.joycool.wap.bean.PagingBean,java.util.List,jc.family.game.vs.*,jc.family.*"%><%
response.setHeader("Cache-Control","no-cache");
VsAction action=new VsAction(request,response);
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>对战信息</title>
 <link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  <body>
   <table border="1">
    <tr><td align="center">index</td>
    	<td align="center">GameName</td>
    	<td align="center">PreTime</td>
    	<td align="center">MaxTime</td>
    	<td align="center">EnterTime</td>
    </tr><%
    	VsConfig[] vs=VsGameBean.vsConfig;
		for (int i = 0; i < vs.length; i++){
		VsConfig vsConfig=vs[i];
			if(vsConfig!=null){%>
	<tr>
		<td align="center"><%=i + 1%></td>
		<td align="center"><%=vsConfig.getName()%></td>
		<td align="center"><%=vsConfig.getPreTime()%>分钟</td>
		<td align="center"><%=vsConfig.getMaxTime()%>分钟</td>
		<td align="center"><%=vsConfig.getEnterTime()%>分钟</td>
	</tr><%}
	}%>
	</table>
 	<a href="/jcadmin/fm/index.jsp">返回家族首页</a>
  </body>
</html>
