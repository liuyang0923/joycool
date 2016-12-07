<%@ page contentType="text/vnd.wap.wml;charset=utf-8" session= "false"%><%@ page import="net.joycool.wap.bean.JaLineBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
Vector blockList = (Vector)request.getAttribute("blockList");
Integer isRoot = (Integer)request.getAttribute("isRoot");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(isRoot!=null&&isRoot.intValue()==1){%>
<card title="乐酷游戏社区">
<p align="center">
<%=BaseAction.getTop(request, response)%>
<%for(int i=0;i<blockList.size();i++){%>
<%=blockList.get(i)%>
<%}%>
</p>
</card>
<%}else{

int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
JaLineBean column = (JaLineBean) request.getAttribute("column");
%><card id="card1" title="<%=StringUtil.toWml(column.getTitle())%>">
<p align="<%--=column.getChildAlign()--%>left">
<%=BaseAction.getTop(request, response)%><%
if(column.getTopIcon()!= null && !column.getTopIcon().equals("")){
%><img src="<%=column.getTopIconURL()%>" alt=""/><br/><%
}
if(column.getTopText()!= null && !column.getTopText().equals("")){
%><%=column.getTopText()%><br/><%
}
%><%for(int i=0;i<blockList.size();i++){
String lineWml = (String)blockList.get(i);%><%
if(column.getChildParagraphStyle() == JaLineBean.CPS_HAS_NUMBER){
%><%if(lineWml.length()>0){%><%=i+1%>.<%}%><%
}
%><%=lineWml%><%}
String fenye = PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl, true, "<br/>", response);
if(fenye != null && !fenye.equals("")){
%>
<br/><%=fenye%><%
}
if(backTo != null){
%>
<a href="<%=backTo%>">返回上一级</a><br/><%
}
%><%if(column.getId()!=6765){%><%=BaseAction.getBottomShort(request, response)%><%}%>
</p>
</card>
<%}%>
</wml>