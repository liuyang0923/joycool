<%@ page language="java" import="net.joycool.wap.spec.garden.*,java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	GardenAction gardenAction = new GardenAction(request);
	
	
	int uid = gardenAction.getParameterInt("uid");
	
	GardenUserBean bean = GardenUtil.getUserBean(uid);
	
	String a = request.getParameter("a");
	
	if(a!= null) {
		int gold = gardenAction.getParameterInt("gold");
		int exp = gardenAction.getParameterInt("exp");
		
		bean.addGold(gold);
		bean.setExp(bean.getExp()+exp);
		GardenAction.gardenService.updateUserGoldAdd(uid, gold);
		GardenUtil.updateExp(uid, exp);
		
		response.sendRedirect("s.jsp?uid="+uid);
		return;
	}
	
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
  <%if(bean == null) {%>
  没有该用户的农场
  <%} else {%>
    用户ID:<%=uid %><br/>
    昵称:<%=UserInfoUtil.getUser(uid).getNickNameWml() %><br/>
    现金:<%=bean.getGold() %><br/>
    登记:<%=GardenUtil.getLevel(bean.getExp()) %><br/>
    增加经验和现金:<br/>
  	<form action="s.jsp?a=1" method="post">
  	用户ID:<input type="text" name="uid" value="<%=uid %>"/><br/>
  	现金:<input type="text" name="gold" value=""/><br/>
  	经验:<input type="text" name="exp" value=""/><br/>
  	<input type="submit" value="提交"/><br/>
  	</form>
  	<%} %>
  </body>
</html>
