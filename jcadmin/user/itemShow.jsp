<%@ page language="java" import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.item.*,java.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*,com.jspsmart.upload.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	
	UserServiceImpl serviceImpl = new UserServiceImpl();
	
	String action = request.getParameter("a");
	
	if(action != null && action.equals("doupdate")){
		SmartUpload smUpload = new SmartUpload();
		smUpload.initialize(pageContext);
		smUpload.upload();
		String name = smUpload.getRequest().getParameter("name");
		
		String info = smUpload.getRequest().getParameter("info");
		
		String id = smUpload.getRequest().getParameter("id");
		
		String itemId = smUpload.getRequest().getParameter("item_id");
		File upfile = smUpload.getFiles().getFile(0);
		
		DbOperation dbOp = new DbOperation(4);
		String query = "update item_show set name = '" + StringUtil.toSql(name) + "', intro = '" + StringUtil.toSql(info) + "', item_id = " + itemId + " where id = " + id;
		
		dbOp.executeUpdate(query);
		
		dbOp.release();
		if(upfile!=null&&!upfile.isMissing())
			upfile.saveAs(Constants.RESOURCE_ROOT_PATH_OLD + "lx/e"+itemId+".gif");
		ICacheMap stuffCache = CacheManage.stuff;
		
		stuffCache.srm("itemShow");
		
		request.setAttribute("msg", "修改成功");
	}
	
	if(action != null && action.equals("doadd")){
		SmartUpload smUpload = new SmartUpload();
		smUpload.initialize(pageContext);
		smUpload.upload();
		String name = smUpload.getRequest().getParameter("name");
		
		String info = smUpload.getRequest().getParameter("info");
		
		String id = smUpload.getRequest().getParameter("id");
		
		String itemId = smUpload.getRequest().getParameter("item_id");
		File upfile = smUpload.getFiles().getFile(0);
		
		DbOperation dbOp = new DbOperation(4);
		
		String query = "insert into item_show set id = " + id + ", item_id = " + itemId + ", name = '" + StringUtil.toSql(name) + "', intro = '" + StringUtil.toSql(info) + "'";
		
		dbOp.executeUpdate(query);
		
		dbOp.release();
		if(upfile!=null&&!upfile.isMissing())
			upfile.saveAs(Constants.RESOURCE_ROOT_PATH_OLD + "lx/e"+itemId+".gif");
		ICacheMap stuffCache = CacheManage.stuff;
		
		stuffCache.srm("itemShow");
		
		request.setAttribute("msg", "增加成功");
	}
	
	
	
	HashMap itemShows = serviceImpl.getItemShowMap("item_id > 0");
	Set itemSet = itemShows.keySet();
	Iterator it = itemSet.iterator();
	
	List list = new ArrayList();
	
	while(it.hasNext()) {
		list.add((Integer)it.next());
	}
	
	Collections.sort(list);
	
	Iterator its = list.iterator();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<style>
	
	.td80{
	width:80px;text-align:center;
	}
	.td100{
	width:100px;text-align:center;
	}
	.td150{
	width:150px;text-align:center;
	}
	
	</style>
  </head>
  
  <body>
  	<a href="itemShow.jsp">刷新</a>|<a href="itemShow.jsp?a=add#add">增加</a><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<%if(action == null) {
  		
  	%>
  	
  	<table>
  		<tr><td>id</td><td class="td100">item_id</td><td class="td100">名字</td><td class="td150">简介</td><td class="td80">图片</td><td class="td80">操作</td></tr>
  		<%while(its.hasNext()) {
  			Integer itemId = (Integer)its.next();
  			ShowBean bean = (ShowBean)itemShows.get(itemId);
  		%>
  		<tr>
  		<td><%=bean.getId() %></td>
  		<td class="td100"><%=bean.getItemId() %></td>
  		<td class="td100"><%=bean.getName()%></td>
  		<td width="250"><%=bean.getIntro() %></td>
  		<td class="td80"><img src="/rep/lx/e<%=bean.getItemId() %>.gif" alt=""/></td>
  		<td class="td80"><a href="itemShow.jsp?a=update&id=<%=bean.getItemId() %>#update">修改</a></td>
  		</tr>
  		<%} %>
  	</table><br/>
  	<%} %>
  	<a href="itemShow.jsp">刷新</a>|<a href="itemShow.jsp?a=add#add">增加</a><br/><br/><br/>
  	
  	<%if(action != null && action.equals("update")) {
  		int id = Integer.parseInt(request.getParameter("id"));
  		ShowBean item = (ShowBean)itemShows.get(new Integer(id));
  	%>
  	<a name="update"></a>
  	修改：<br/>
  	<form action="itemShow.jsp?a=doupdate" method="post" enctype="multipart/form-data">
	id:<%=item.getId() %><br/>
	itemId:<input type="text" name="item_id" value="<%=item.getItemId() %>"/><br/>
	<input type="hidden" name="id" value="<%=item.getId() %>"/>
	名字:<input type="text" name="name" value="<%=item.getName() %>"/><br/>
	信息:<input type="text" name="info" value="<%=item.getIntro() %>"/><br/>
	图片:<input type="file" name="photo"/><br/>
	<input type="submit" value="修改"/>
	</form>
<%} else if(action != null && action.equals("add")) {%>
	<a name="add"></a>
  	新增：<br/>
  	<form action="itemShow.jsp?a=doadd" method="post" enctype="multipart/form-data">
	id:<input type="text" name="id" value=""/><br/>
	itemId:<input type="text" name="item_id" value=""/><br/>
	名字:<input type="text" name="name" value=""/><br/>
	信息:<input type="text" name="info" value=""/><br/>
	图片:<input type="file" name="photo"/><br/>
	<input type="submit" value="新增"/>
	</form>
<%} %>
  	
  		
  	
  </body>
</html>
