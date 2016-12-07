<%@ page language="java" import="net.joycool.wap.spec.garden.*,java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	GardenAction gardenAction = new GardenAction(request);
	
	GardenUtil.DAY_SEC = 24 * 60 * 60;
	
	//GardenUtil.GAME_NAME = "";
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
  <b>农场后台</b><br/>
  	<form action="s.jsp">
  	用户ID:<input type="text" name="uid" value=""/>
  	<input type="submit" value="提交"/>
  	</form>
  	<a href="seed.jsp">增加新作物</a><br/><br/>
  <b>花园后台</b><br/>
<table width="100%" style="border: none;">
				<tr>
					<td style="border: none;" width="11%"><form method = "post" action = "flowerOperation.jsp?b=1" >
							用户ID:<input type="text" name="uid"/>
							<input type = "submit" value ="提交"/>
					</form></td>
					<td style="border: none;" width="30%"><form method = "post" action="flowerProperty.jsp">
					  	<input type = "submit" value ="更改属性表"/>
					</form></td>
				  </tr>
			  </table>
  </body>
</html>
