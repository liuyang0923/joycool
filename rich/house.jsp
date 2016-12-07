<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.house();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInHouse()&&!action.isResult("tip")){	 // 取消买房
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
RichNodeBean node = action.world.getNode(richUser);	// 当前结点，取出房子价格
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(!action.isResult("tip")){	//
 %>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=node.getBuilding().getName()%>:<%=node.house.getLevelName()%><br/>
<%if(node.house.noOwner()){%>
房价<%=node.getPrice()%>，是否要购买？<br/>
<a href="house.jsp?a=1">买下来</a><br/>
<a href="house.jsp?a=2">算了，不买</a><br/>
<%}else{%>
升级需要<%=node.getPrice()%>，是否要升级？<br/>
<a href="house.jsp?a=1">确认升级</a><br/>
<a href="house.jsp?a=2">算了，不升级</a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="go.jsp">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>