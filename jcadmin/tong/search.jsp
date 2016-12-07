<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.infc.ITongService" %><%@ page import="net.joycool.wap.bean.tong.*" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");

int  tongId=StringUtil.toInt(request.getParameter("tongId"));
if(tongId<=0){
response.sendRedirect("index.jsp");
return;
}
TongBean tong = TongCacheUtil.getTong(tongId);
if(tong==null){
response.sendRedirect("index.jsp");
return;
}
TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tongId);
%>
<html>
	<head>
	<title>帮会管理后台</title>
	 <script language="javascript" >
function test(){
	if(document.tongForm.money.value.replace(/(^\s+|\s+$)/g,"") == ''){
		alert("输入金额不能为空！");
		return false;
	}else if(isNaN(document.tongForm.money.value)){
		alert("输入金额只能是数字！");
		return false;
	}
	return true;
}
</script>
</head>
<body>
	<H1 align="center">帮会管理后台</H1>
	<hr>
	<br/>
	<table width="800" border="1" align="center">
	<th>帮会ID</th>
	<th>帮会名称</th>
	<th>帮主名称</th>
	<th>帮会基金</th>
	<th>帮会状态</th>
	<th>操作</th>
	<tr>	
		<form method="post" action="result.jsp" name="tongForm" onsubmit="return test()">
			<td>
				<%=tong.getId()%>
			</td>
			<td>
				<%=tong.getTitle()%>
				<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
			</td>
			<td>
<%if(tong.getUserId()>0){
UserBean user = UserInfoUtil.getUser(tong.getUserId());
if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickName()%></a>
<%}}else{%>(无)<%}%>
			</td>
			<td>
				<%=tong.getFund()%>
			</td>
			<td>
				<%if(tong.getMark()==0){%>正常状态<%}else{%>荒城<%}%>
			</td>
			<td>
			<select name="operate">
			<option value="1">加</option>
			<option value="0">减</option>
			</select>&nbsp;&nbsp;
			<input type="text" name="money" size="20" >乐币&nbsp;&nbsp;
			<input type="submit" value="确定"><br/>
			</td>
		</form>
	</tr>
	</table>
	<br />
<form  action="search.jsp" method="post"  name="tongForm" onsubmit="return test()"><br/>
帮会ID：
<input type="text" name="tongId" size="10" >&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="查询"><br/>
</form><br/>
<p align="center">
	<a href="index.jsp">返回帮会管理后台</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
</p>
<table width="800" border="1" align="center">
	<th><a href="high.jsp?tongId=<%=tongId%>">高层</a>/<a href="member.jsp?tongId=<%=tongId%>">总人数</a></th>
	<th>城墙<a href="wall.jsp?tongId=<%=tongId%>">加固</a>.<a href="wall2.jsp?tongId=<%=tongId%>">攻打</a></th>
	<th><a href="shop.jsp?tongId=<%=tongId%>">商店开发度</a></th>
	<th><a href="hshop.jsp?tongId=<%=tongId%>">当铺开发度</a></th>
	<th>仓库开发度</th>
	<th>创建时间</th>
	<th>副帮主</th>
	<th>副帮主</th>
<tr>
<td><%=tong.getCadreCount()%>/<%=tong.getUserCount()%></td>
<td><%=tong.getNowEndure()%>/<%=tong.getHighEndure()%></td>
<td><%=tong.getShop()%></td>
<td><%=tongHockshop.getDevelop()%></td>
<td><%=tong.getDepotWeek()%>/<%=tong.getDepotLast()%>/<%=tong.getDepot()%></td>
<td><%=tong.getCreateDatetime()%></td>
<td>
<%if(tong.getUserIdA()>0){
UserBean user = UserInfoUtil.getUser(tong.getUserIdA());
if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickName()%></a>
<%}}else{%>(无)<%}%>
</td>
<td>
<%if(tong.getUserIdB()>0){
UserBean user = UserInfoUtil.getUser(tong.getUserIdB());
if(user!=null){%><a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickName()%></a>
<%}}else{%>(无)<%}%>
</td>
<td><a href="chat/chatList.jsp?key=<%=tong.getId() %>">聊天室</a></td>
</tr>
</table>
<form method="post" action="result.jsp?operate=2">
<input type="text" name="money" size="10" >
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="增加商店">
</form>
<form method="post" action="result.jsp?operate=3">
<input type="text" name="money" size="10" >
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="增加城墙">
</form>
<form method="post" action="result.jsp?operate=4">
<input type="text" name="money" size="10" >
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="增加城墙上限">
</form>
<form method="post" action="result.jsp?operate=5">
<input type="text" name="name" size="10" value="荒城">
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="重置帮会名">
</form>
<form method="post" action="result.jsp?operate=6">
<input type="text" name="money" size="10" >
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="增加仓库">
</form>
<form method="post" action="result.jsp?operate=7">
<input type="text" name="money" size="10" >
<input type="hidden" id="id" name="tongId" value="<%=tong.getId() %>">
<input type="submit" value="增加当铺">
</form>
</body>
</html>
