<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.business();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInBusiness()&&!action.isResult("tip")){	 // 取消买房
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
<%=node.house.getLevelName()%><br/>
<%if(node.house == null || node.house.noOwner()){%>
价格<%=node.getPrice()%>，是否要购买？<br/>
<a href="business.jsp?a=1">买下来</a><br/>
<a href="business.jsp?a=2">算了，不买</a><br/>
<%}else if(node.house.getLevel()==0){%>
升级需要<%=node.getPrice()%>，请问要建造什么？<br/>
<a href="business.jsp?a=1&amp;o=1">研究所</a><br/>
<a href="business.jsp?a=1&amp;o=2">连锁超市</a><br/>
<a href="business.jsp?a=1&amp;o=3">饭店</a><br/>
<a href="business.jsp?a=2">算了，不建造</a><br/>
<%}else{%>
升级需要<%=node.getPrice()%>，是否要升级？<br/>
<a href="business.jsp?a=1">确认升级</a><br/>
<a href="business.jsp?a=2">算了，不升级</a><br/>
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