<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.guestbook.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%

response.setHeader("Cache-Control","no-cache");
BoardBean board = (BoardBean) request.getAttribute("board");
Vector contentList = (Vector) request.getAttribute("contentList");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
String rootBackTo = (String) request.getAttribute("rootBackTo");
int i, count;
ContentBean content = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=board.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<%=board.getName()%><br/>
--------------------<br/>
<%
count = contentList.size();
for(i = 0; i < count; i ++){
	content = (ContentBean) contentList.get(i);
%>
<%=(pageIndex * Constants.GUESTBOOK_ARTICLE_PER_PAGE + i + 1)%>.<%=StringUtil.toWml(content.getNickname())%>在<%=content.getCreateDatetime()%>说：<%=StringUtil.toWml(content.getContent())%><br/>
<%
}
%>
<%=PageUtil.shangxiafenye(totalPageCount, pageIndex, prefixUrl.replace("&", "&amp;"), true, "<br/>", response)%><br/>
第<%=(totalPageCount == 0? pageIndex : (pageIndex + 1))%>页|共<%=totalPageCount%>页<br/>
昵称：<br/><input name="nickname"  maxlength="100" value="匿名"/><br/>
内容：<br/><input name="content"  maxlength="1000" value="v"/><br/>
    <anchor title="确定">发表留言
    <go href="<%=(prefixUrl.replace("&", "&amp;"))%>" method="post">
	<postfield name="nickname" value="$nickname"/>
	<postfield name="content" value="$content"/>
	<postfield name="action" value="add"/>
    </go>
    </anchor><br/>
<br/>
<%--
<a href="<%=StringUtil.toWml("http://wap.joycool.net/Column.do?columnId=905&amp;jaLineId=2162")%>">返回上一级</a><br/>
--%>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>