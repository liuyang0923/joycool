<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
response.setHeader("Cache-Control","no-cache");
String ebookId =request.getParameter("ebookId");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
对不起，你没有电子书下载卡。<br/>
卡片可以通过在书虫菜园论坛<br/>
发表大家认可的精华帖子得到，<br/>
也可以通过参加定向越野游戏得到(完成任务随机获得)。<br/>
<a href="/jcforum/forum.jsp?forumId=906">去书虫菜园论坛</a><br/>
<a href="/stage/index.jsp">参加定向越野</a><br/>
<a href="/lswjs/index.jsp">去乐酷的其他板块</a><br/>
<a href="/ebook/EBookInfo.do?ebookId=<%=ebookId%>">返回上一级</a><br/>

</p>
</card>
</wml>

