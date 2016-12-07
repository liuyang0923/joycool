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
	String merchantId = request.getParameter("merchantId");
	String versionId = request.getParameter("versionId");
	String currency = request.getParameter("currency");
	String pmId = request.getParameter("pmId");
	String pcId = request.getParameter("pcId");
	String merchantKey = request.getParameter("merchantKey");
	String submitURL = request.getParameter("submitURL");
	String notifyURL = request.getParameter("notifyURL");
	String searchURL = request.getParameter("searchURL");
	
	PayBean pay = new PayBean();
	pay.setId(id);
	pay.setCurrency(currency);
	pay.setMerchantId(merchantId);
	pay.setMerchantKey(merchantKey);
	pay.setName(name);
	pay.setSubmitURL(submitURL);
	pay.setNotifyURL(notifyURL);
	pay.setPcId(pcId);
	pay.setPmId(pmId);
	pay.setVersionId(versionId);
	pay.setSearchURL(searchURL);
	
	
	
	//修改
	if(id > 0) {
		payService.updatePayBean(pay);
//		System.out.println("update action");
		request.setAttribute("msg","修改成功");
	} else {
		payService.addPayBean(pay);
		request.setAttribute("msg","增加成功");
	}
	
	
} else if(action.equals("u")) {
	int id = 0;
	try{
		id = Integer.parseInt(request.getParameter("id"));
	}catch(Exception e) {
		id = 0;
	}
	PayBean pay = payService.getPayBeanById(id);
	
	request.setAttribute("pay", pay);
}

List list = payService.getPayBeans(null);

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
<a href="pay.jsp">刷新</a><br/>
<table>
	<tr><td>id</td><td>名称</td>
	<td>merchantId</td>
	<td>versionId</td>
	<td>currency</td>
	<td>pmId</td>
	<td>pcId</td>
	<td>merchantKey</td>
	<td>提交地址</td>
	<td>通知地址</td>
	<td>查询地址</td>
	<td>操作</td>
	</tr>
	<%for(int i = 0; i < list.size(); i++) {
		PayBean payBean = (PayBean)list.get(i);
	%>
	<tr><td><%=payBean.getId() %></td><td><%=payBean.getName() %></td>
	<td><%=payBean.getMerchantId() %></td>
	<td><%=payBean.getVersionId() %></td>
	<td><%=payBean.getCurrency() %></td>
	<td><%=payBean.getPmId() %></td>
	<td><%=payBean.getPcId() %></td>
	<td><%=payBean.getMerchantKey() %></td>
	<td><%=payBean.getSubmitURL() %></td>
	<td><%=payBean.getNotifyURL() %></td>
	<td><%=payBean.getSearchURL() %></td>
	<td><a href="pay.jsp?action=u&id=<%=payBean.getId()%>">修改</a></td>
	</tr>
	<%} %>
</table>
<form action="pay.jsp?action=a" method="post">
<input type="hidden" name="id" value="<%=request.getParameter("id") %>"/>
名字:<input name="name" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getName()%>"/><br/>
merchantId:<input name="merchantId" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getMerchantId()%>"/><br/>
versionId:<input name="versionId" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getVersionId()%>"/><br/>
currency:<input name="currency" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getCurrency()%>"/><br/>
pmId:<input name="pmId" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getPmId()%>"/><br/>
pcId:<input name="pcId" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getPcId()%>"/><br/>
merchantKey:<input name="merchantKey" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getMerchantKey()%>"/><br/>
提交地址:<input name="submitURL" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getSubmitURL()%>"/><br/>
通知地址:<input name="notifyURL" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getNotifyURL()%>"/><br/>
查询地址:<input name="searchURL" value="<%= (request.getAttribute("pay") == null)?"": ((PayBean)request.getAttribute("pay")).getSearchURL()%>"/><br/>
<input type="submit" value="<%=(request.getAttribute("pay") == null)?"增加": "修改"%>"/>
</form>
</body>
</html>