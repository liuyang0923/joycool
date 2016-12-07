<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.Vector" %><%@ page import="net.joycool.wap.bean.jcforum.ForumBean" %><%@ page import="net.joycool.wap.action.jcforum.ForumAction" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.bean.jcforum.ForumContentBean" %><%@ page import="net.joycool.wap.util.PageUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil" %><%
response.setHeader("Cache-Control","no-cache");
ForumAction action=new ForumAction(request);
action.forum(request,response);
String forumId = (String) request.getAttribute("forumId");
ForumBean forum =(ForumBean)request.getAttribute("forum"); %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(forum==null){%>
<card title="论坛">
<%}else{%>
<card title="<%=StringUtil.toWml(forum.getTitle())%>">
<%}%>
<p align="left">

<%=BaseAction.getTop(request, response)%>
<%if(forum!=null){
Vector contentList = (Vector)request.getAttribute("contentList");
int totalPageCount = StringUtil.toInt((String) request.getAttribute("totalPageCount"));
int pageIndex =  StringUtil.toInt((String) request.getAttribute("pageIndex"));
String prefixUrl = (String) request.getAttribute("prefixUrl");
%>
警察局处理结果<br/>
------------------------------------------------<br/>
  <%if(contentList!=null){
   for(int i=0;i<contentList.size();i++){
   ForumContentBean forum1=(ForumContentBean)contentList.get(i);
   if(forum1!=null){%>
     <%if(forum1.getMark2()==1){%>**<%}else if(forum1.getMark1()==1){%>*<%}else{%><%=i+1%>.<%}%>
      <%=StringUtil.toWml(forum1.getTitle())%><br/>

      <%}
    }
   }%>

    <%
}
else{%>
 该论坛不存在!<br/>
<%}%>
 <a href="/user/onlineManager.jsp?forumId=355">返回警察局</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>