<%@ page language="java" import="net.joycool.wap.util.*,java.util.*,net.joycool.wap.service.impl.*,net.joycool.wap.bean.*,net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.spec.shop.*"%><%@ page contentType="text/html;charset=utf-8"%><%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	ShopAction shopAction = new ShopAction(request);
	
	String ads = request.getParameter("ad");
	
	String action = request.getParameter("a");
	
	String adtype = request.getParameter("adtype");
	if(ads != null && ads.length() > 0) {
		String link = request.getParameter("link");
		String info = request.getParameter("info");
		int type = shopAction.getParameterInt("type");
		
		ShopAction.shopService.saveAds(link,info, type);
		
		//response.sendRedirect("shopads.jsp");
		//return;
	}
	
	if(action != null) {
		int id = shopAction.getParameterInt("id");
		if(action.equals("d")){
			ShopAction.shopService.deleteShopAds(id);
			//response.sendRedirect("shopads.jsp");
			//return;
		}
	}
	
	if(adtype != null) {
		String name = request.getParameter("name");
		
		ShopAction.shopService.addAdsType(name);
	}
	List listStr = ShopUtil.getAds();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>添加道具</title>
    <script type="text/javascript" src="ajax.js"></script>
	<style>
	table{
	}
	table tr{
	}
	table tr td{
	border:1px solid green;
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
	
	</style>
  </head>
  
  <body>
  
  	<!-- 
  	
  	 -->
  	<p style="color:red;"><%=request.getAttribute("msg") != null ? request.getAttribute("msg") :"" %></p>
  	<table>
  		<tr><td class="td150">广告语</td><td class="td100">链接</td><td class="td100">类型</td><td class="td150">操作</td></tr>
  		<%for(int i = 0; i<listStr.size();i++)  {
  			String[] str = (String[])listStr.get(i);
  			int ii = StringUtil.toInt(str[0]);
  		%>
  		<tr>
  		<td class="td100"><%=str[2] %></td>
  		<td class="td100"><%=str[1]%></td>
  		<td class="td100"><%=str[3]%></td>
  		<td class="td150"><a href="shopads.jsp?a=d&id=<%=ii%>">删除</a></td>
  		</tr>
  		<%} %>
  	</table>
  	注意：<br/>
  	必须以反斜杠 / 开头，把 & 替换为 &amp;amp;<br/>
  	<a href="shop.jsp#add">增加新商品</a>|<a href="shopads.jsp">刷新</a>|<a href="add.jsp">新增物品</a>|<a href="pay.jsp">支付网关</a>|<a href="types.jsp">类别管理</a>
  	<a name="add"></a>
  	<br/>增加广告:<br/>
  	<form action="shopads.jsp?ad=1" method="post">
  		链接:<input name="link"/><br/>
  		描述:<input name="info"/><br/>
  		<select name="type">
  			<option value="0">0</option>
  			<%
  				List list = ShopAction.shopService.getAllAdsTypes();
  				for(int i = 0;i<list.size();i++){
  					String[] types = (String[])list.get(i);
  			%>
  			<option value="<%=types[0] %>"><%=types[1] %></option>
  			<%}
  			%>
  		</select>
  		<input type="submit" value="提交"/>
  	</form>
  	<br/><br/><br/><br/>
  	<table>
  		<tr><td class="td150">类型id</td><td class="td100">类型名字</td></tr>
  		<%for(int i = 0; i<list.size();i++)  {
  			String[] str = (String[])list.get(i);
  		%>
  		<tr>
  		<td class="td100"><%=str[0] %></td>
  		<td class="td100"><%=str[1]%></td>
  		</tr>
  		<%} %>
  	</table>
  	增加类型:<br/>
  	<form action="shopads.jsp?adtype=1" method="post">
  		<input name="name"/>
  		<input type="submit" value="提交"/>
  	</form>
  	
  </body>
</html>
