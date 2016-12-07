<%@ page language="java" import="java.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.bean.item.*,java.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*"%><%@ page contentType="text/html;charset=utf-8"%><%!

static ModuleServiceImpl moduleService = new ModuleServiceImpl();
%><%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	
	String action = request.getParameter("a");
	HashMap uriMapModule = PositionUtil.uriMapModule;
	if(action != null && action.equals("del")){
		
		String url = request.getParameter("url");
			if(url!=null){
			synchronized(uriMapModule){
				uriMapModule.remove(url);
			}
		}
		request.setAttribute("msg", "删除成功");
	}

	
	
	HashSet hs = new HashSet(uriMapModule.keySet());
	hs.remove(null);
	Iterator iter = new TreeSet(hs).iterator();
	
	
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
  	<a href="module.jsp">返回模块列表</a>|<a href="module2.jsp">列出所有uri</a><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<table>
  		<tr><td width=40></td><td class="td150">uri</td><td class="td100">模块</td><td class="td80">操作</td></tr>
  		<%
  		int i = 0;
  		while (iter.hasNext()) {
			String moduleUrl = (String) iter.next();
			ModuleBean bean = (ModuleBean) uriMapModule.get(moduleUrl);
  		%>
  		<tr><td><%=++i%></td>
  		<td align=left><%=moduleUrl %></td>
  		<td class="td150"><%if(bean==null||bean.getId()<0){%>(空)<%}else{%><a href="module.jsp?a=update&id=<%=bean.getId()%>"><%=bean.getShortName()%>(<%=bean.getId()%>)</a><%}%></td>
  		<td class="td80"><a href="module2.jsp?a=del&url=<%=java.net.URLEncoder.encode(moduleUrl) %>#update">删除</a></td>
  		</tr>
  		<%} %>
  	</table><br/><a href="module.jsp">刷新</a>|<a href="module.jsp?a=add#add">增加</a><br/><br/><br/>
  	
  	<%if(action != null && action.equals("update")) {
  		
  		
  		
  		int id = Integer.parseInt(request.getParameter("id"));
  		
  		ModuleBean item = moduleService.getModule("id="+id);
  	%>
  	<a name="update"></a>
  	修改：<br/>
  	<form action="module.jsp?a=doupdate" method="post">
	id:<%=item.getId() %><br/>
	<input type="hidden" name="id" value="<%=item.getId() %>"/>
	名字:<input type="text" name="name" value="<%=item.getName() %>"/><br/>
	url:<input type="text" name="url" value="<%=item.getUrlPattern() %>"/><br/>
	返回url:<input type="text" name="url2" value="<%=item.getReturnUrl() %>"/><br/>
	<input type="submit" value="修改"/>
	</form>
<%} else if(action != null && action.equals("add")) {%>
	<a name="add"></a>
  	新增：<br/>
  	<form action="module.jsp?a=doadd" method="post">
	id:<input type="text" name="id" value=""/><br/>
	名字:<input type="text" name="name" value=""/><br/>
	url:<input type="text" name="url" value=""/><br/>
	返回:<input type="text" name="url2" value=""/><br/>
	<input type="submit" value="新增"/>
	</form>
<%} %>
  	
  		
  	
  </body>
</html>
