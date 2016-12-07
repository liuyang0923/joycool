<%@ page language="java" import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.item.*,java.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	
	String action = request.getParameter("a");
	
	if(action != null && action.equals("doupdate")){
		
		String name = request.getParameter("name");
		
		String info = request.getParameter("info");
		
		String id = request.getParameter("id");
		
		String url = request.getParameter("url");
		
		String seq = request.getParameter("seq");
		
		DbOperation dbOp = new DbOperation(4);
		String query = "update shortcut set name = '" + StringUtil.toSql(name) + "', short_name = '" + StringUtil.toSql(info) + "', seq = " + seq + ", url = '" + url + "' where id = " + id;
		
		dbOp.executeUpdate(query);
		
		dbOp.release();
		
		ICacheMap stuffCache = CacheManage.stuff;
		
		synchronized(stuffCache){
			stuffCache.rm("scMap");
			stuffCache.rm("scList");
		}
		
		request.setAttribute("msg", "修改成功");
	}
	
	if(action != null && action.equals("doadd")){
		String name = request.getParameter("name");
		
		String info = request.getParameter("info");
		
		String id = request.getParameter("id");
		
		String seq = request.getParameter("seq");
		
		String url = request.getParameter("url");
		
		DbOperation dbOp = new DbOperation(4);
		
		String query = "insert into shortcut set id = " + id + ", seq = " + seq + ", name = '" + StringUtil.toSql(name) + "', short_name = '" + StringUtil.toSql(info) + "', url = '" + url + "'";
		
		dbOp.executeUpdate(query);
		
		dbOp.release();
		
		ICacheMap stuffCache = CacheManage.stuff;
		
		synchronized(stuffCache){
			stuffCache.rm("scMap");
			stuffCache.rm("scList");
		}
		
		request.setAttribute("msg", "增加成功");
	}
	
	List list1 = UserInfoUtil.getShortcutList();
	
	Iterator its = list1.iterator();
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<style>
	.td50{
	width:50px;text-align:center;
	}
	.td80{
	width:80px;text-align:center;
	}
	.td100{
	width:100px;text-align:center;
	}
	.td150{
	width:150px;text-align:center;
	}
	.td200{
	width:200px;text-align:center;
	}
	.td300{
	width:300px;text-align:center;
	}
	
	</style>
  </head>
  
  <body>
  	<a href="shortcut.jsp">刷新</a>|<a href="shortcut.jsp?a=add#add">增加</a>|<a href="module.jsp">模块</a><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<table>
  		<tr><td class="td50">id</td><td class="td80">seq/排序</td><td class="td100">名字</td><td class="td300">url</td><td class="td100">显示名字</td><td class="td80">操作</td></tr>
  		<%while(its.hasNext()) {
  			ShortcutBean bean = (ShortcutBean)its.next();
  		%>
  		<tr>
  		<td class="td50"><%=bean.getId() %></td>
  		<td class="td80"><%=bean.getSeq() %></td>
  		<td class="td100"><%=bean.getName()%></td>
  		<td class="td300"><%=bean.getUrl() %></td>
  		<td class="td100"><%=bean.getShortName() %></td>
  		<td class="td80"><a href="shortcut.jsp?a=update&id=<%=bean.getId() %>#update">修改</a></td>
  		</tr>
  		<%} %>
  	</table><br/><a href="shortcut.jsp">刷新</a>|<a href="shortcut.jsp?a=add#add">增加</a><br/><br/><br/>
  	
  	<%if(action != null && action.equals("update")) {
  		
  		HashMap map = UserInfoUtil.getShortcutMap();
  		
  		int id = Integer.parseInt(request.getParameter("id"));
  		
  		ShortcutBean item = (ShortcutBean)map.get(new Integer(id));
  	%>
  	<a name="update"></a>
  	修改：<br/>
  	<form action="shortcut.jsp?a=doupdate" method="post">
	id:<%=item.getId() %><br/>
	seq:<input type="text" name="seq" value="<%=item.getSeq() %>"/><br/>
	<input type="hidden" name="id" value="<%=item.getId() %>"/>
	名字:<input type="text" name="name" value="<%=item.getName() %>"/><br/>
	url:<input type="text" name="url" value="<%=item.getUrl() %>"/><br/>
	显示名字:<input type="text" name="info" value="<%=item.getShortName() %>"/><br/>
	<input type="submit" value="修改"/>
	</form>
<%} else if(action != null && action.equals("add")) {%>
	<a name="add"></a>
  	新增：<br/>
  	<form action="shortcut.jsp?a=doadd" method="post">
	id:<input type="text" name="id" value=""/><br/>
	seq:<input type="text" name="seq" value=""/><br/>
	名字:<input type="text" name="name" value=""/><br/>
	url:<input type="text" name="url" value=""/><br/>
	显示名字:<input type="text" name="info" value=""/><br/>
	<input type="submit" value="新增"/>
	</form>
<%} %>
  	
  		
  	
  </body>
</html>
