<%@ page language="java" contentType="text/html; charset=utf-8" import="net.joycool.wap.cache.util.*,net.joycool.wap.spec.app.*,net.joycool.wap.bean.dummy.*,net.joycool.wap.util.*,java.util.*,net.joycool.wap.spec.shop.*,net.joycool.wap.spec.pay.*,net.joycool.wap.bean.*" 
    pageEncoding="utf-8"%><%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

ShopAction action = new ShopAction(request);

int uid = action.getParameterInt("user_id");
PagingBean paging = new PagingBean(action,1000,30,"p");
List list;
if(uid==0)
	list = ShopAction.shopService.getBuyRecord("1 order by time desc limit " + paging.getStartIndex()+",30");
else
	list = ShopAction.shopService.getBuyRecord(uid, " order by time desc ", paging.getStartIndex(), 30);
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
<table>
	<tr><td>id</td>
	<td>购买用户</td>
	<td>价格</td>
	<td>物品</td>
	<td>时间</td>
	<td>赠送用户</td>
	</tr>
	<%for(int i = 0; i < list.size(); i++) {
		GoldRecordBean recordBean = (GoldRecordBean)list.get(i);
		
		
		
	%>
	<tr><td><%=recordBean.getId() %></td>
	<td><%=""+recordBean.getUid()+"/"+UserInfoUtil.getUser(recordBean.getUid()).getNickNameWml() %></td>
	<td><%=recordBean.getGoldString() %></td>
	<td><%if(recordBean.getType()==2){
	AppBean app = AppAction.getApp(recordBean.getItemId());
	%><%=app.getName()%><%}else if(recordBean.getType()==3||recordBean.getType()==4){
	%>酷秀<%
	} else if(recordBean.getType()==6){
	%>选秀<%
	} else if(recordBean.getItemId()>0){
ItemBean bean = ShopAction.shopService.getShopItemById(recordBean.getItemId());
DummyProductBean productBean = UserBagCacheUtil.getItem(bean.getItemId());
	%><%=productBean.getName() %><%}%></td>
	<td><%=recordBean.getTime() %></td>
	<td><%if(recordBean.getUserId() == 0) {%>自己<%} else {%><%=""+recordBean.getUserId()+"/"+UserInfoUtil.getUser(recordBean.getUserId()).getNickNameWml() %><%} %></td>
	</tr>
	<%} %>
</table>
<%=paging.shuzifenye("record.jsp?user_id="+uid,true,"|",response)%><br/>
<form action="record.jsp" method="get">
<input type="text" name="user_id"/>
<input type="submit" value="查询"/>
</form>
</body>
</html>