<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%
if(!group.isFlag(0)) return;
CustomAction action = new CustomAction(request, response);
net.joycool.wap.service.infc.IDummyService dummyService = net.joycool.wap.service.factory.ServiceFactory.createDummyService();
int adminId = action.getParameterInt("aid");
AdminUserBean au = AdminAction.getAdminUser("id=" + adminId);
if(au==null){
	response.sendRedirect("quota.jsp");
	return;
}

int deleteId = action.getParameterInt("del");
if(deleteId>0) {
	net.joycool.wap.bean.dummy.DummyProductBean dummyProduct = dummyService.getDummyProducts(deleteId);
	String itemName = dummyProduct==null?"???":dummyProduct.getName();
	LogUtil.logAdmin(adminUser.getName()+"把管理员"+au.getName()+"的物品["+itemName+"(id"+deleteId+")]配额删除了");
	SqlUtil.executeUpdate("delete from admin_quota_item where id="+adminId+" and item_id=" + deleteId);
	response.sendRedirect("quota2.jsp?aid=" + adminId);
	return;
}

int addId = action.getParameterInt("add");
int modifyId = action.getParameterInt("id");

if(!action.isMethodGet()){
	if(modifyId>0){
		int itemCount = action.getParameterIntS("itemCount");
		if(itemCount>=0) {
			net.joycool.wap.bean.dummy.DummyProductBean dummyProduct = dummyService.getDummyProducts(modifyId);
			String itemName = dummyProduct==null?"???":dummyProduct.getName();
			LogUtil.logAdmin(adminUser.getName()+"把管理员"+au.getName()+"的物品["+itemName+"(id"+modifyId+")]配额修改为"+itemCount);
			SqlUtil.executeUpdate("update admin_quota_item set item_count=" + itemCount+" where id="+adminId+" and item_id=" + modifyId);
		}
	} else if(addId>0){
		int itemCount = action.getParameterIntS("itemCount");
		if(itemCount>=0) {
			net.joycool.wap.bean.dummy.DummyProductBean dummyProduct = dummyService.getDummyProducts(addId);
			String itemName = dummyProduct==null?"???":dummyProduct.getName();
			LogUtil.logAdmin(adminUser.getName()+"把管理员"+au.getName()+"的物品["+itemName+"(id"+addId+")]配额修改为"+itemCount);
			SqlUtil.executeUpdate("insert into admin_quota_item set item_count=" + itemCount+" , id="+adminId+" , item_id=" + addId 
			+ " ON DUPLICATE KEY UPDATE item_count="+itemCount);
		}
	}
	response.sendRedirect("quota2.jsp?aid=" + adminId);
	return;
}
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<%
	List list = SqlUtil.getIntsList("select id,item_id,item_count from admin_quota_item where id=" + adminId + " order by item_id");
%>
【<%=au.getName()%>】<br/>
<%if(modifyId<=0){%>
<form action="quota2.jsp?aid=<%=adminId%>" method="post">
物品id：<input type=text name="add" value=""> 
设置配额：<input type=text name="itemCount" value=""> 个
<input type=submit value="确认设置"><br/>
</form>
<br/>
<table width="450">
<tr>
	<td align="center">
		物品id
	</td>
	<td align="center">
		类型
	</td>
	<td align="center">
		物品名
	</td>
	<td align="center">
		道具配额
	</td>
	<td></td>
</tr>
	<%for (int i = 0; i < list.size(); i++) {
		int[] ints = (int[])list.get(i);
	net.joycool.wap.bean.dummy.DummyProductBean dummyProduct = dummyService.getDummyProducts(ints[1]);
%>
	<tr>
		<td align="center">
			<%=ints[1]%>
		</td>
		<td align=center>
			<%if(dummyProduct==null){%>???<%}else{%><%=dummyProduct.getTypeName()%><%}%>
		</td>
		<td align=center>
			<%if(dummyProduct==null){%>???<%}else{%><%=dummyProduct.getName()%><%}%>
		</td>
		<td align="center">
			<%=ints[2]%>
		</td>
		<td align=center>
			<a href="quota2.jsp?aid=<%=adminId%>&id=<%=ints[1]%>">修改</a>
			<a href="quota2.jsp?aid=<%=adminId%>&del=<%=ints[1]%>" onclick="return confirm('确认删除?')">删除</a>
		</td>
	</tr>
	<%}%>
</table><br/>
<a href="quota.jsp">返回</a><br/>
<%}else{%>
<%
int itemCount = SqlUtil.getIntResult("select item_count from admin_quota_item where id=" + adminId + " and item_id=" + modifyId);
net.joycool.wap.bean.dummy.DummyProductBean dummyProduct = dummyService.getDummyProducts(modifyId);
%>
<%=dummyProduct.getName()%> (<%=dummyProduct.getTypeName()%>) id : <%=modifyId%><br/>
<form action="quota2.jsp?aid=<%=adminId%>&id=<%=modifyId%>" method="post">
修改配额：<input type=text name="itemCount" value="<%=itemCount%>"> 个
<input type=submit value="确认修改"><br/>
</form>
<a href="quota2.jsp?aid=<%=adminId%>">返回</a><br/>
<%}%>

<br/>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
