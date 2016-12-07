<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.bean.jcforum.ForumReplyBean" %><%@ page import="net.joycool.wap.cache.OsCacheUtil" %><%@ page import="net.joycool.wap.action.jcforum.ForumxAction,net.joycool.wap.action.jcforum.NewsTypeBean"%><%response.setHeader("Cache-Control", "no-cache");
if (session.getAttribute("adminLogin") == null) {
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
ForumxAction action = new ForumxAction(request);
String tip = "";
NewsTypeBean typeBean = null;
java.util.HashMap map = ForumxAction.getNewsTypeMap();
int mid = action.getParameterInt("m");
if (mid > 0){
	typeBean = (NewsTypeBean)map.get(new Integer(mid));
	if (typeBean == null){
		response.sendRedirect("news.jsp");
		return;
	}
	int submit = action.getParameterInt("s");
	if (submit > 0){
		// 提交修改
		int type = action.getParameterInt("type");
		int cid = action.getParameterInt("cid");
		int parentId = action.getParameterInt("parentId");
		String name = action.getParameterNoEnter("name");
		String cname = action.getParameterNoEnter("cname");
		if (type > 127){
			type = 0;
		}
		if(name != null && !("".equals(name)) && name.length() < 11 && type >= 0 && cid >= 0){
			SqlUtil.executeUpdate("update forum_news_type set name='" + StringUtil.toSql(name) + "',type=" + type + ",parent_id=" + parentId + ",column_id=" + cid + ",column_name='" + StringUtil.toSql(cname) + "' where id=" + typeBean.getId() ,2);
			// 清缓存
			ForumxAction.newsTypeMap = null;
			response.sendRedirect("news.jsp");
			return;
		} else {
			tip = "类型错误或没有输入标题或标题超过10个字.";
		}
	}
}
int nid = action.getParameterInt("nid");
if (nid == 1){
	int type = action.getParameterInt("type");
	int cid = action.getParameterInt("cid");
	String name = action.getParameterNoEnter("name");
	String cname = action.getParameterNoEnter("cname");
	if (type > 127){
		type = 0;
	}
	if(name != null && !("".equals(name)) && name.length() < 11 && type >= 0 && cid >= 0){
		//DbOperation db = new DbOperation(2);
		SqlUtil.executeUpdate("insert into forum_news_type (`name`,`count`,`type`,column_id,column_name) values ('" + StringUtil.toSql(name) + "',0," + type +"," + cid + ",'" + StringUtil.toSql(cname) + "')",2);
		//db.release();
		// 清缓存
		ForumxAction.newsTypeMap = null;
	} else {
		tip = "类型错误或没有输入标题或标题超过10个字.";
	}
}
List typeList = ForumxAction.getNewsTypeList();
%>
<html>
	<head>
		<title>新闻管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%if (mid > 0){
%><%=tip%><br/><form action="news.jsp?m=<%=mid%>&s=1" method="post">
名称:<input type="text" name="name" value="<%=StringUtil.toWml(typeBean.getName())%>" /><br/>
类型[数字]:<input type="text" name="type" value="<%=typeBean.getType()%>" /><br/>
column_id:<input type="text" name="cid" value="<%=typeBean.getColumnId()%>"/><br/>
column_name:<input type="text" name="cname" value="<%=StringUtil.toWml(typeBean.getColumnName())%>"/><br/>
parent_id:<input type="text" name="parentId" value="<%=typeBean.getParentId()%>"/><br/>
<input type="submit" value="修改" />
<input id="cmd" type="button" value="返回" onClick="javascript:window.location.href='news.jsp'">
</form><%
} else {
%>目前共有个<%=typeList.size()%>新闻分类。</br><%=tip%><br/>
	<form action="news.jsp?nid=1" method="post">
		名称:<input type="text" name="name" /><br/>
		类型[数字]:<input type="text" name="type" /><br/>
		column_id:<input type="text" name="cid" value="0"/><br/>
		column_name:<input type="text" name="cname" value=""/><br/>
		<input type="submit" value="添加新闻类型" /><br/>
	</form>
	<table border=1 width=100% align=center>
		<tr bgcolor=#C6EAF5>
			<td align=center>ID</td>
			<td align=center>标题</td>
			<td align=center>类型</td>
			<td align=center>column_id</td>
			<td align=center>column_name</td>
			<td align=center>parentId</td>
			<td align=center>修改</td>
		</tr>
		<%if (typeList != null && typeList.size() > 0){
			for (int i = 0 ; i < typeList.size() ; i++){
				typeBean = (NewsTypeBean)typeList.get(i);
				if (typeBean != null){
					%><tr>
						<td><%=typeBean.getId() %></td>
						<td><a href="readNews.jsp?id=<%=typeBean.getId()%>"><%=StringUtil.toWml(typeBean.getName())%></a></td>
					  	<td><%=typeBean.getType()%></td>
					  	<td><%=typeBean.getColumnId()%></td>
					  	<td><%=StringUtil.toWml(typeBean.getColumnName())%></td>
					  	<td><%=typeBean.getParentId()%></td>
					  	<td><a href="news.jsp?m=<%=typeBean.getId()%>">修改</a></td>
					  </tr><%
				}
			}
		}%>
	</table><%	
}%>
	</body>
</html>