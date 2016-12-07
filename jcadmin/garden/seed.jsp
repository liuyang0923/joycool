<%@ page language="java" import="net.joycool.wap.spec.garden.*,java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	GardenAction gardenAction = new GardenAction(request);
	
	int id = gardenAction.getParameterInt("id");
	if(id==0)
		id = 1;
	
	
	List list = GardenAction.gardenService.getSeeds(id);
	

	
	String a = request.getParameter("a");
	
	if(a != null && a.equals("delete")){
		int ids = gardenAction.getParameterInt("ids");
		GardenAction.gardenService.deleteSeed(" id = "+ids);
		
		GardenService.seedIdMap.clear();
		GardenService.seedMap.clear();
	}
	
	
	PagingBean paging = new PagingBean(gardenAction, list.size(), 10, "p");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="ajax.js"></script>
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
  <%
  List types = GardenAction.gardenService.getSeedTypes();
	%>
	<%for(int i = 0; i < types.size(); i ++) {
	Object[] type = (Object[])types.get(i);%>
<%if(i > 0) {%>.<%} 
if(id == ((Integer)type[0]).intValue()) {%><%=type[1]%><%} else {%><a href="seed.jsp?id=<%=type[0]%>"><%=type[1]%></a><%} %>
<%} %><br/>
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<table>
  		<tr><td>id</td><td class="td100">名称</td><td class="td100">类型</td><td class="td80">几季</td><td class="td150">等级</td><td class="td100">详细信息</td><td class="td80">数量</td><td class="td80">每个季所需时间</td><td class="td80">经验值</td><td class="td80">作物阶段</td><td class="td80">value</td><td class="td150">操作</td></tr>
  		<%for(int i = paging.getStartIndex(); i < paging.getEndIndex(); i++) { 
  			GardenSeedBean seed = (GardenSeedBean)GardenAction.gardenService.getSeedBean(((Integer)list.get(i)).intValue());
  		%>
  		<tr>
  		<td><%=seed.getId() %></td>
  		<td class="td100"><%=seed.getName() %></td>
  		<td class="td100"><%=seed.getType() %></td>
  		<td class="td80"><%=seed.getQuarter() %></td>
  		<td class="td150"><%=seed.getLevel() %></td>
  		<td class="td100"><%=seed.getInfo() %></td>
  		<td class="td80"><%=seed.getCount() %></td>
  		<td class="td80"><%=seed.getGrown() %></td>
  		<td class="td80"><%=seed.getExp() %></td>
  		<td class="td80"><%=seed.getGrowTime() %></td>
  		<td class="td80"><%=seed.getValue() %></td>
  		<td class="td150"><a href="seed.jsp?a=delete&ids=<%=seed.getId() %>" onclick="if(!confirm('确定删除?')) {return false;}">删除</a>|<a href="update.jsp?id=<%=seed.getId() %>&type=<%=id %>">修改</a></td>
  		</tr>
  		<%} %>
  	</table>
  	<%=paging.shuzifenye("seed.jsp?id="+id, true, "|", response)%>
  	<a href="add.jsp">增加新作物</a>|<a href="seed.jsp">刷新</a>
  </body>
</html>
