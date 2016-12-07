<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.imglib.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=30; %>
<%  ImgLibAction action = new ImgLibAction(request);
	int modify = action.getParameterInt("m");
	int pageId = action.getParameterInt("p");
	int id = action.getParameterInt("id");		// 这个ID是要修改的图片的ID
	int sid = action.getParameterInt("sid");	// 按ID查找
	String tip = "";
	PagingBean paging = null;
	int pageNow = 0;
	if (modify == 1){
		String title = action.getParameterNoEnter("title");
		if (id <= 0){
			tip = "ID错误<br/>";
		} else {
			if (title == null || "".equals(title) || title.length() > 12){
				tip = "没有输入标题或标题已长于12个字!";
			} else {
				// 修改标题
				SqlUtil.executeUpdate("update img_lib set title='" + StringUtil.toSql(title) + "' where id=" + id,5);
				// 删除log中的记录
				// SqlUtil.executeUpdate("delete from img_lib_log where img_id=" + id,5);
				tip = "图片ID:<font color=\"blue\">" + id + "</font>，标题已修改为:<font color=\"green\">" + StringUtil.toSql(title) + "</font>";
			}
		}
	}
	paging = new PagingBean(action,100000,COUNT_PRE_PAGE,"p");
	List list = ImgLibAction.service.getLibList(" 1 order by modify_time desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	Lib lib = null;
	if (sid > 0){
		lib = ImgLibAction.service.getLib(" id=" + sid);
	}
%>
<html>
	<head>
		<title>用户图库标题审查</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
==用户图库标题审核==<br/>	
<a href="index.jsp">刷新</a><br/>
flag=0,审核中;flag=1,通过;flag=2,未通过<br/>
<form action="index.jsp?p=<%=pageId %>" method="post">
按图片ID搜索:<input type="text" name="sid" value=""/>
<input type="submit" value="查找">
</form>
<%if (lib != null){
%><form action="index.jsp?m=1&id=<%=lib.getId()%>&p=<%=pageId %>" method="post">
<table border=1 width=100% align=center>
<tr><td><a href="show.jsp?id=<%=lib.getId() %>"><%=lib.getId()%></a></td>
<td><a href="../user/queryUserInfo.jsp?id=<%=lib.getUserId()%>"><%=lib.getUserId()%></a></td>
<td><%=DateUtil.formatSqlDatetime(lib.getCreateTime()) %></td>
<td><%=DateUtil.formatSqlDatetime(lib.getModifyTime()) %></td>
<td><%=StringUtil.toWml(lib.getTitle()) %></td>
<td><input type="text" name="title" value=""/></td>
<td><%=lib.getFlag()%></td>
<td><input type="submit" value="修改" onclick="return confirm('确定修改吗?')"></td></tr>
</table>
</form><%	
}%>
<font color="red"><b><%=tip%></b></font>
		<table border=1 width=100% align=center>
				<tr bgcolor=#C6EAF5>
					<td align=center>
						<font color=#1A4578>图片ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>用户ID</font>
					</td>
					<td align=center>
						<font color=#1A4578>创建时间</font>
					</td>
					<td align=center>
						<font color=#1A4578>修改时间</font>
					</td>
					<td align=center>
						<font color=#1A4578>标题</font>
					</td>
					<td align=center>
						<font color=#1A4578>修改标题</font>
					</td>
					<td align=center>
						<font color=#1A4578>FLAG</font>
					</td>
					<td align=center>
						<font color=#1A4578>操作</font>
					</td>
				</tr>
				<%if(list != null && list.size() > 0){
					for (int i = 0; i < list.size(); i++) {
						lib = (Lib) list.get(i);
						%>
						<form action="index.jsp?m=1&id=<%=lib.getId()%>&p=<%=pageId %>" method="post">
						<tr>
							<td><a href="show.jsp?id=<%=lib.getId() %>"><%=lib.getId()%></a></td>
							<td><a href="../user/queryUserInfo.jsp?id=<%=lib.getUserId()%>"><%=lib.getUserId()%></a></td>
							<td><%=DateUtil.formatSqlDatetime(lib.getCreateTime()) %></td>
							<td><%=DateUtil.formatSqlDatetime(lib.getModifyTime()) %></td>
							<td><%=StringUtil.toWml(lib.getTitle()) %></td>
							<td><input type="text" name="title" value=""/></td>
							<td><%=lib.getFlag()%></td>
							<td><input type="submit" value="修改" onclick="return confirm('确定修改吗?')"></td>
						</tr>
						</form>
						<%
					}
				}%>
		</table><%=paging.shuzifenye("index.jsp", false, "|", response)%>
	</body>
</html>