<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.imglib.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  ImgLibAction action = new ImgLibAction(request);
	String tip = "";
	List list = action.service.getLibList(" 1 order by id desc");
	PagingBean paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
	Lib lib = null;
%>
<html>
	<head>
		<title>用户图库标题审查</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
==所有图库图片==<br/>	
<a href="index.jsp">返回首页</a>|<a href="showAll.jsp">刷新</a><br/>
<font color="red"><b><%=tip%></b></font>
		<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578>ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>用户ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>创建时间</font>
					</td>
					<td align=center>
						<font color=#1A4578>标题</font>
					</td>
					<td align=center>
						<font color=#1A4578>图片</font>
					</td>
					<td align=center>
						<font color=#1A4578>FLAG</font>
					</td>
				</tr>
				<%if(list != null && list.size() > 0){
					for (int i = paging.getStartIndex(); i < paging.getEndIndex(); i++) {
						lib = (Lib) list.get(i);
						%>
						<tr>
							<td><%=lib.getId()%></td>
							<td><a href="../user/queryUserInfo.jsp?id=<%=lib.getUserId()%>"><%=lib.getUserId()%></a></td>
							<td><%=DateUtil.formatSqlDatetime(lib.getCreateTime()) %></td>
							<td><%=StringUtil.toWml(lib.getTitle()) %></td>
							<td><img src="<%=action.ATTACH_URL_ROOT%>/<%=lib.getImg()%>" alt="loading..." /></td>
							<td><%=lib.getFlag()%></td>
						</tr>
						<%
					}
				}%>
		</table><%=paging.shuzifenye("showAll.jsp",false,"|",response)%>
	</body>
</html>