<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.CartBean"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.MyCartAction"%><%
response.setHeader("Cache-Control","no-cache");
MyCartAction action = new MyCartAction();
action.getCart(request);

Vector cartList = (Vector) request.getAttribute("cartList");
if(cartList==null){
response.sendRedirect("/user/login.jsp?backTo=/cart/mycart.jsp");
return;
}
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
int totalPageCount = ((Integer)request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer)request.getAttribute("pageIndex")).intValue();

int count, i;
CartBean cart = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="我的收藏夹">
<%=BaseAction.getTop(request, response)%>
<p align="left">
我的收藏夹<br/>
-------------------<br/>
<%
count = cartList.size();
for(i = 0; i < count; i ++){
	cart = (CartBean) cartList.get(i);
%>
<%=(10 * pageIndex + (i + 1))%>.<a href="<%=(StringUtil.toWml(cart.getUrl()))%>"><%=StringUtil.toWml(cart.getTitle())%></a>(<a href="<%=(prefixUrl + "&amp;pageIndex=" + pageIndex + "&amp;delete=" + cart.getId())%>">删除</a>)<br/>
<%
}
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "|", response);
%>
<%if(!fenye.equals("")){%><%=fenye%><br/><%}%>
<br/>
<%--
<a href="<%=backTo%>">返回上一级</a><br/>
--%>
<a href="/Column.do?columnId=8774">返回书城首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>

</card>
</wml>