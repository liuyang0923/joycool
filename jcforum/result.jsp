<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.*" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.result();
ForumContentBean con = (ForumContentBean) request.getAttribute("con");
ForumBean forum = (ForumBean) request.getAttribute("forum");
int contentId = con.getId();
if(request.getAttribute("tip")==null){
response.sendRedirect("viewContent.jsp?contentId="+contentId);
return;
}%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(request.getAttribute("cart")!=null){%>
<card title="结果页面">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%><br/>
<a href="/cart/mycart.jsp">我的收藏夹</a><br/>
<a href="viewContent.jsp?contentId=<%=contentId%>">返回论坛</a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>

<%}else if(request.getAttribute("tip")!=null){%>
<card title="结果页面" ontimer="<%=response.encodeURL("viewContent.jsp?contentId="+contentId)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>(5秒钟跳转到上一级页面)<br/>
<a href="viewContent.jsp?contentId=<%=contentId%>">返回上一级</a><br/>
<a href="forum.jsp?forumId=<%=forum.getId()%>">返回<%=StringUtil.toWml(forum.getTitle())%></a><br/>
<a href="index.jsp">论坛首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>