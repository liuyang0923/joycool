<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,jc.guest.wall.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 20; %>
<%WallAction action = new WallAction(request);
String tip = "";
int wid = 0;
List list = null;
WallBean bean = null;
PagingBean paging = null;
int type = action.getParameterInt("type");	//0:列表;1:添加;2:修改
int yeshu = action.getParameterInt("p");
if (type == 0){
	wid = action.getParameterInt("wid");
	int visible = action.getParameterInt("v");
	if (wid > 0){
		SqlUtil.executeUpdate("update wall set visible=" + (visible==0?1:0) + " where id=" + wid,5);
	}
	list = WallAction.service.getWallList("1 order by create_time");
	paging = new PagingBean(action, list.size(), COUNT_PRE_PAGE, "p");
} else if (type == 1){
	if (action.getParameterInt("s")==1){
		// 添加新的
		bean = new WallBean();
		bean.setTitle(action.getParameterNoEnter("title"));
		bean.setUid(action.getParameterInt("uid"));
		bean.setShortCont(action.getParameterNoEnter("shortCont"));
		bean.setContent(action.getParameterString("content"));
		if (action.checkBean(bean)){
			WallAction.service.addWall(bean);
			response.sendRedirect("index.jsp");
			return;
		} else {
			tip = (String)request.getAttribute("tip");
		}
	}
} else if (type == 2){
	wid = action.getParameterInt("wid");
	if (wid <= 0){
		tip = "要修改的墙不存在!";
	} else {
		bean = WallAction.service.getWallBean(" id=" + wid);
		if (bean == null){
			tip = "要修改的墙不存在!";
		} else {
			if (action.getParameterInt("s") == 1){
				// 提交修改
				bean.setTitle(action.getParameterNoEnter("title"));
				bean.setUid(action.getParameterInt("uid"));
				bean.setShortCont(action.getParameterNoEnter("shortCont"));
				bean.setContent(action.getParameterString("content"));
				bean.setCreateTimeStr(DateUtil.formatSqlDatetime(DateUtil.parseTime(action.getParameterString("ct"))));
				if (action.checkBean(bean)){
					SqlUtil.executeUpdate("update wall set title='" + StringUtil.toSql(bean.getTitle()) + "',uid=" + bean.getUid() + ",short_cont='" + StringUtil.toSql(bean.getShortCont()) + "',`content`='" + StringUtil.toSql(bean.getContent())  + "',create_time='" + bean.getCreateTimeStr() + "' where id=" + bean.getId(),5);
					response.sendRedirect("index.jsp?p=" + yeshu);
					return;
				} else {
					tip = (String)request.getAttribute("tip");
				}
			}
		}
	}
}
%>
<html>
	<head>
		<title>乐酷用户墙</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%=tip%><br/>
<%if (type == 0){
// 列表
%><a href="index.jsp?type=1">添加新的</a>&nbsp;总数<%=list!=null?list.size():0%>贴<br/><%
if (list != null){
%><table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>ID</td>
		<td align=center>标题</td>
		<td align=center>用户ID</td>
		<td align=center>简版</td>
		<td align=center>原版</td>
		<td align=center>创建时间</td>
		<td align=center>显示状态</td>
		<td align=center>操作</td>
	</tr>
	<%for (int i = paging.getStartIndex() ; i < paging.getEndIndex() ; i++){
			bean = (WallBean)list.get(i);
			if (bean != null){
				%><tr>
					<td><%=bean.getId()%></td>
					<td><%=bean.getTitleWml()%></td>
					<td><%=bean.getUid()%></td>
					<td><%=StringUtil.toWml(StringUtil.limitString(bean.getShortCont(),24))%></td>
					<td><%=StringUtil.toWml(StringUtil.limitString(bean.getContent(),40))%></td>
					<td><%=DateUtil.formatSqlDatetime(bean.getCreateTime())%></td>
					<td><a href="index.jsp?wid=<%=bean.getId()%>&v=<%=bean.getVisible()%>&p=<%=yeshu%>"><%=bean.getVisibleStr()%></a></td>
					<td><a href="index.jsp?type=2&wid=<%=bean.getId()%>&p=<%=yeshu %>">修改</a></td>
				</tr><%
			}
	}%>
</table><%=paging != null?paging.shuzifenye("index.jsp", false, "|", response):""%><br/>
<a href="index.jsp?type=1">添加新的</a>&nbsp;总数<%=list!=null?list.size():0%>贴<br/>
<%
}
} else if (type == 1){
// 添加
%>添加新的:<br/><form action="index.jsp?type=1&s=1" method="post">
标题:<input type="text" name="title" value="<%=bean!=null?bean.getTitleWml():""%>"/>(12字内)<br/>
UID:<input type="text" name="uid" value="<%=bean!=null?bean.getUid():0%>"/><br/>
简版:<input type="text" name="shortCont" value="<%=bean!=null?bean.getShortContWml():""%>"/>(50字内)<br/>
原文:(1500字内)<br/>
<textarea name="content" rows="10" cols="80" border="1"><%=bean!=null?bean.getContent():"" %></textarea><br/>
<input type="submit" value="确认添加" />
<input type="button" value="返回列表" onClick="javascript:window.location.href='index.jsp'">
</form>
<%
} else if (type == 2){
// 修改
%>修改:<br/><form action="index.jsp?type=2&wid=<%=bean.getId()%>&s=1&p=<%=yeshu %>" method="post">
标题:<input type="text" name="title" value="<%=bean.getTitleWml()%>"/>(12字内)<br/>
UID:<input type="text" name="uid" value="<%=bean.getUid()%>"/><br/>
时间格式:1970-01-01 00:00:00<br/>
时间:<input type="text" name="ct" value="<%=DateUtil.formatSqlDatetime(bean.getCreateTime())%>"/><br/>
简版:<input type="text" name="shortCont" value="<%=bean.getShortContWml()%>"/>(50字内)<br/>
原文:(1500字内)<br/>
<textarea name="content" rows="10" cols="80" border="1"><%=bean.getContent()%></textarea><br/>
<input type="submit" value="确认修改" />
<input type="button" value="返回列表" onClick="javascript:window.location.href='index.jsp?p=<%=yeshu %>'">
</form>
<%
}
%>
	</body>
</html>
