<%@ page language="java" import="java.io.*,org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.cache.util.*,java.util.*,org.apache.commons.fileupload.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%>
<%
	response.setHeader("Cache-Control","no-cache");
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	ShopAction shopAction = new ShopAction(request);
	String a = request.getParameter("a");
	
	if(a != null) {
		int index = shopAction.getParameterInt("index");
		
		if(a.equals("1")) {
			ShopConstant.INDEX[1] = index;
		} else if(a.equals("2")) {
			ShopConstant.INDEX[2] = index;
		} else if(a.equals("3")) {
			ShopConstant.ITEM[1] = index;
		} else if(a.equals("4")) {
			ShopConstant.ITEM[2] = index;
		} else if(a.equals("5")) {
			ShopConstant.ITEM[3] = index;
		}
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="ajax.js"></script>
    <script type="text/javascript" src="/jcadmin/js/JS_functions.js"></script>
<link href="../farm/common.css" rel="stylesheet" type="text/css">

  </head>
  
  <body>
  修改特别推荐<br/>
  <form action="sugupdate.jsp?a=1" method="post">
  特别推荐1：<input type="text" name="index" value="<%=ShopConstant.INDEX[1]%>"/><br/>
  <input type="submit" value="修改" />
  </form>
  <form action="sugupdate.jsp?a=2" method="post">
  特别推荐2：<input type="text" name="index" value="<%=ShopConstant.INDEX[2]%>"/><br/>
  <input type="submit" value="修改" />
  </form>
  <form action="sugupdate.jsp?a=3" method="post">
  装饰推荐：<input type="text" name="index" value="<%=ShopConstant.ITEM[1]%>"/><br/>
  <input type="submit" value="修改" />
  </form>
  <form action="sugupdate.jsp?a=4" method="post">
  娱乐推荐：<input type="text" name="index" value="<%=ShopConstant.ITEM[2]%>"/><br/>
  <input type="submit" value="修改" />
  </form>
  <form action="sugupdate.jsp?a=5" method="post">
  游戏推荐：<input type="text" name="index" value="<%=ShopConstant.ITEM[3]%>"/><br/>
  <input type="submit" value="修改" />
  </form>
  
  </body>
</html>
