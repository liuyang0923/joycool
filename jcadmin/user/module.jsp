<%@ page language="java" import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.framework.CustomAction,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.item.*,java.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*"%><%@ page contentType="text/html;charset=utf-8"%><%!

static ModuleServiceImpl moduleService = new ModuleServiceImpl();
%><%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	CustomAction action=new CustomAction(request);
	String act = request.getParameter("a");
	HashMap moduleHash = PositionUtil.moduleHash;
	if(act != null && act.equals("doupdate")){
		
		String name = request.getParameter("name");
				
		int id = action.getParameterIntS("id");
		
		String url = request.getParameter("url");
		String url2 = request.getParameter("url2");
		
		String entryUrl = request.getParameter("entry");
		String shortName = request.getParameter("short");
		String posName = request.getParameter("pos");
		int flag = action.getParameterInt("flag");
		int pri = action.getParameterInt("pri");
		
		DbOperation dbOp = new DbOperation(0);
		String query = "update jc_module set name = '" + StringUtil.toSql(name) + "', url_pattern = '" + StringUtil.toSql(url) + "', return_url = '" + StringUtil.toSql(url2) +"', entry_url = '" + StringUtil.toSql(entryUrl) +"', short_name = '" + StringUtil.toSql(shortName) +"', pos_name = '" + StringUtil.toSql(posName) + "',flag="+flag+",priority="+pri+" where id = " + id;
		
		dbOp.executeUpdate(query);
		dbOp.release();
		
		ModuleBean module = moduleService.getModule("id="+id);
		if(module != null) {
			module = PositionUtil.getModule(id);
			if(module!=null){
				module.setUrlPattern(url);
				module.setReturnUrl(url2);
				module.setName(name);
				module.setEntryUrl(entryUrl);
				module.setShortName(shortName);
				module.setPosName(posName);
				module.setFlag(flag);
				module.setPriority(pri);
			}
		}
		
		request.setAttribute("msg", "修改成功");
	}
	
	if(act != null && act.equals("doadd")){
		String name = request.getParameter("name");
		
		String url = request.getParameter("url");
		String url2 = request.getParameter("url2");
		String image = request.getParameter("image");
		
		String entryUrl = request.getParameter("entry");
		String shortName = request.getParameter("short");
		String posName = request.getParameter("pos");
		int flag = action.getParameterInt("flag");
	
		ModuleBean module = new ModuleBean();
		module.setFlag(flag);
		module.setImage(image);
		module.setName(name);
		module.setUrlPattern(url);
		module.setReturnUrl(url2);
		module.setEntryUrl(entryUrl);
		module.setShortName(shortName);
		module.setPosName(posName);
		moduleService.addModule(module);
		PositionUtil.addModule(module);
		
		request.setAttribute("msg", "增加成功");
	}
	
	if(act!=null&&act.equals("reload")){
		PositionUtil.loadModuleConf();
		PositionUtil.resetUriMap();
	}
	
	Iterator iter = moduleHash.keySet().iterator();
	
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
  	<a href="module.jsp">刷新</a>|<a href="module.jsp?a=add#add">增加</a>|<a href="module2.jsp">列出所有uri</a><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<%if(act == null){%>
  	<table>
  		<tr><td>id</td><td>名字</td><td>url</td><td>返回</td><td>入口</td><td>位置</td><td>简称</td><td class="td80">操作</td></tr>
  		<%while (iter.hasNext()) {
			Integer iid = (Integer) iter.next();
			ModuleBean bean = (ModuleBean) moduleHash.get(iid);
  		%>
  		<tr>
  		<td width="30"><%=bean.getId() %></td>
  		<td width="100"><%=bean.getName()%></td>
  		<td align=left><%=bean.getUrlPattern() %></td>
  		<td align=left><%=bean.getReturnUrl() %></td>
  		<td align=left><%=bean.getEntryUrl() %></td>
  		<td align=left><%=bean.getPosName() %></td>
  		<td align=left><%=bean.getShortName() %></td>
  		<td width="40"><%if(bean.getId()<1000){%><a href="module.jsp?a=update&id=<%=bean.getId() %>#update">修改</a><%}%></td>
  		</tr>
  		<%} %>
  	</table><br/><a href="module.jsp">刷新</a>|<a href="module.jsp?a=add#add">增加</a><br/><br/><br/>
  	<%}%>
  	<%if(act != null && act.equals("update")) {
  		
  		
  		
  		int id = Integer.parseInt(request.getParameter("id"));
  		
  		ModuleBean item = moduleService.getModule("id="+id);
  	%>
  	<a name="update"></a>
  	修改：<br/>
  	<form action="module.jsp?a=doupdate" method="post">
	id:<%=item.getId() %><br/>
	<input type="hidden" name="id" value="<%=item.getId() %>"/>
	名字:<input type="text" name="name" value="<%=item.getName() %>"/><br/>
	模块图:<input type="text" name="image" value="<%=item.getImage() %>"/><br/>
	url:<input type="text" name="url" value="<%=item.getUrlPattern() %>"/><br/>
	返回url:<input type="text" name="url2" value="<%=item.getReturnUrl() %>"/><br/>
	flag:<input type="text" name="flag" value="<%=item.getFlag() %>"/><br/>
	优先级:<input type="text" name="pri" value="<%=item.getPriority() %>"/><br/>
	
	入口url:<input type="text" name="entry" value="<%=item.getEntryUrl() %>"/><br/>
	简短名称:<input type="text" name="short" value="<%=item.getShortName() %>"/><br/>
	状态名称:<input type="text" name="pos" value="<%=item.getPosName() %>"/><br/>
	<input type="submit" value="修改"/>
	</form>
<%} else if(act != null && act.equals("add")) {%>
	<a name="add"></a>
  	新增：<br/>
  	<form action="module.jsp?a=doadd" method="post">
	名字:<input type="text" name="name" value=""/><br/>
	模块图:<input type="text" name="image" value=""/><br/>
	url:<input type="text" name="url" value=""/><br/>
	返回:<input type="text" name="url2" value=""/><br/>
	flag:<input type="text" name="flag" value="0"/><br/>
	入口url:<input type="text" name="entry" value=""/><br/>
	简短名称:<input type="text" name="short" value=""/><br/>
	状态名称:<input type="text" name="pos" value=""/><br/>
	<input type="submit" value="新增"/>
	</form>
<%} %>
  	
<a href="module.jsp?a=reload" onclick="return confirm('确认要从数据库重新载入数据?')">重新载入</a><br/>
  	
  </body>
</html>
