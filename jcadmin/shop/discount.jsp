<%@ page language="java" contentType="text/html; charset=utf-8" import="java.util.*,net.joycool.wap.spec.shop.*,net.joycool.wap.spec.pay.*" 
    pageEncoding="utf-8"%><%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

String action = request.getParameter("action");

if(action != null) {
	
	String i = request.getParameter("i");
	
	if(i.equals("0")) {
		float consume = Float.valueOf(request.getParameter("consume")).floatValue();
		float discount = Float.valueOf(request.getParameter("discount")).floatValue();
		
		ShopUtil.consumes[0] = consume;
		ShopUtil.discounts[0] = discount;
	} else if(i.equals("1")){
		float consume = Float.valueOf(request.getParameter("consume")).floatValue();
		float discount = Float.valueOf(request.getParameter("discount")).floatValue();
		ShopUtil.consumes[1] = consume;
		ShopUtil.discounts[1] = discount;
	}
}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style>
	table{
	}
	table tr{
	}
	table tr td{
	border:1px solid green;
	}
</style>
</head>
<body>
<%=request.getAttribute("msg")==null?"":request.getAttribute("msg")+"<br/>" %>
<a href="discount.jsp">刷新</a><br/><br/>
会员1管理<br/>
<form action="discount.jsp?action=a&i=0" method="post">
消费<input type="text" name="consume" value="<%=ShopUtil.consumes[0] %>"/>元打折<input type="text" name="discount" value="<%=ShopUtil.discounts[0] %>"/><br/>
<input type="submit" value="修改1"/>
</form>
<br/>
会员2管理<br/>
<form action="discount.jsp?action=a&i=1" method="post">
消费<input type="text" name="consume" value="<%=ShopUtil.consumes[1] %>"/>元打折<input type="text" name="discount" value="<%=ShopUtil.discounts[1] %>"/><br/>
<input type="submit" value="修改2"/>
</form><br/>
<a href="shop.jsp">返回商城管理</a>
</body>
</html>