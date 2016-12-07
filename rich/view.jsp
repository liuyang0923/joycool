<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
RichUserBean richUser = action.getRichUser();
int nodeId = action.getParameterInt("n");
RichNodeBean node = action.world.getNode(nodeId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="go.jsp">返回</a><br/>
<%=node.getDetail(response)%>
<% for(int i = 0;i < node.next.size();i++){
RichNodeBean node2 = (RichNodeBean)node.next.get(i);%>
<a href="map.jsp?n=<%=node2.getId()%>">+</a>↑<%=node2.getPattern()%><a href="view.jsp?n=<%=node2.getId()%>"><%=node2.getDesc()%></a><br/>
<%}%>
<% for(int i = 0;i < node.prev.size();i++){
RichNodeBean node2 = (RichNodeBean)node.prev.get(i);%>
<a href="map.jsp?n=<%=node2.getId()%>">+</a>↓<%=node2.getPattern()%><a href="view.jsp?n=<%=node2.getId()%>"><%=node2.getDesc()%></a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>