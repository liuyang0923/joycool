<%@ page language="java" contentType="text/html; charset=utf-8" import="java.util.*,net.joycool.wap.spec.shop.*,net.joycool.wap.spec.pay.*" 
    pageEncoding="utf-8"%><%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

PayService payService = new PayService();



String action = request.getParameter("action");
if(action == null) {
//	System.out.println("no action");
} else if(action.equals("a")) {
	
	int id = 0;
	try{
		id = Integer.parseInt(request.getParameter("id"));
	}catch(Exception e) {
		id = 0;
	}
	
	String name = request.getParameter("name");
	
	//修改
	if(id > 0) {
		ShopAction.shopService.updateShopType(id, name);
//		System.out.println("update action");
		request.setAttribute("msg","修改成功");
	} else {
		ShopAction.shopService.addShopType(id, name);
		request.setAttribute("msg","增加成功");
	}
	
	
} else if(action.equals("u")) {
	int id = 0;
	try{
		id = Integer.parseInt(request.getParameter("id"));
	}catch(Exception e) {
		id = 0;
	}
	//PayBean pay = payService.getPayBeanById(id);
	
	ShopTypeBean typeBean = ShopAction.shopService.getShopTypeById(id);
	
	request.setAttribute("typeBean", typeBean);
}

String[] types = ShopService.getShopTypes();
int length = types.length;

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
<a href="types.jsp">刷新</a><br/>
<table>
	<tr><td>id</td>
	<td>分类</td>
	<td>操作</td>
	</tr>
	<%for(int i = 1; i < length; i++) {
		if(types[i] != null && types[i].length() > 0) {
	%>
	<tr><td><%=i %></td>
	<td><%=types[i] %></td>
	<td><a href="types.jsp?action=u&id=<%=i%>">修改</a></td>
	</tr>
	<%}} %>
</table>
<form action="types.jsp?action=a" method="post">
<input type="hidden" name="id" value="<%=request.getParameter("id") %>"/>
名字:<input name="name" value="<%= (request.getAttribute("typeBean") == null)?"": ((ShopTypeBean)request.getAttribute("typeBean")).getName()%>"/><br/>
<input type="submit" value="<%=(request.getAttribute("typeBean") == null)?"增加": "修改"%>"/>
</form>
<a href="shop.jsp">返回商城管理</a>
</body>
</html>