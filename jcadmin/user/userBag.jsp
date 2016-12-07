<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IUserService" %><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.service.*" %><%@ page import="net.joycool.wap.service.factory.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean" %><%@ page import="net.joycool.wap.service.impl.DummyServiceImpl" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.service.impl.UserbagItemServiceImpl" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.item.UserItemLogBean" %><%
response.setHeader("Cache-Control","no-cache");
int userId = StringUtil.toInt(request.getParameter("userId"));
int del = StringUtil.toInt(request.getParameter("del"));
if(del > 0 && userId>0){
UserBagCacheUtil.updateUserBagCacheById("time=0", "id=" + del, userId, del);
response.sendRedirect(("userBag.jsp?userId="+userId));
}
UserBean user = UserInfoUtil.getUser(userId);
List userBagList = UserBagCacheUtil.getUserBagListCacheById(userId);

%>
<html>
<div align="center">
<H1 align="center">用户行囊管理后台</H1>
<p align="center" >

用户：<%=StringUtil.toWml(user.getNickName())%>(<%=user.getId()%>)
</p>
<table border="1" align="center" >
<tr>
	<td>
		id
	</td>
	<td>
		序号
	</td>
	<td>
		物品
	</td>
	<td>
		数量
	</td>
	<td>
		制造者
	</td>
	<td>
		期限
	</td>
	<td>
		操作
	</td>
</tr>
<%
for(int i=0;i<userBagList.size();i++){
    Integer userBagId=(Integer)userBagList.get(i);
    UserBagBean userBag=UserBagCacheUtil.getUserBagCache(userBagId.intValue());
    if(userBag==null)userBag=new UserBagBean();
    DummyProductBean dummyProduct=UserBagCacheUtil.getItem(userBag.getProductId());
    if(dummyProduct == null) continue;%>
<tr>
<td><a href="userBag2.jsp?id=<%=userBagId%>"><%=userBagId%></a></td>
<td><%=i+1%></td>
<td><%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%></td>
<td><%=userBag.getTime()%></td>
<td><%=userBag.getCreatorId()%></td>
<td><%=DateUtil.formatTimeInterval(userBag.getTimeLeft())%></td>
<td><a href="userBag.jsp?del=<%=userBagId%>&userId=<%=userId%>" onclick="return confirm('确认删除：<%=dummyProduct.getName()%><%=userBag.getTimeString(dummyProduct)%> ?')">删除</a></td>
</tr>
<%}%>
</table>
<a href="/jcadmin/manage.jsp">返回管理首页</a>
</html>