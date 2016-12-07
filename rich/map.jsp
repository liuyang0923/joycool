<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.map();
RichUserBean richUser = action.getRichUser();
List nodeList = (List)request.getAttribute("nodeList");
int pos = action.getAttributeInt("pos");
RichNodeBean node2 = (RichNodeBean)nodeList.get(0);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<% for(int i = 0;i < nodeList.size();i++){
node2 = (RichNodeBean)nodeList.get(i);%>
<%if(node2.getId()!=pos){%><a href="map.jsp?n=<%=node2.getId()%>">+</a><%}%><%=node2.getPattern()%><a href="view.jsp?n=<%=node2.getId()%>"><%=node2.getDesc()%></a><br/>
<%}%>
<a href="go.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>