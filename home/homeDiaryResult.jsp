<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.home.HomeAction" %><%@ page import="net.joycool.wap.bean.home.HomeDiaryBean" %><%
response.setHeader("Cache-Control","no-cache");
HomeAction action = new HomeAction(request);
action.diaryResult(request);
String result = (String)request.getAttribute("result");
String url=("/home/homeDiaryList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="日记续写" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/home/homeDiaryList.jsp">返回日记列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
url=("/home/homeDiary.jsp?diaryId="+request.getParameter("diaryId"));
%>
<card title="日记续写" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转)<br/>
<a href="/home/homeDiary.jsp?diaryId=<%=request.getParameter("diaryId")%>">返回</a><br/>    
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
