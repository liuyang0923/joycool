<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %><%@ taglib uri="/tags/struts-html" prefix="html" %><%@ taglib uri="/tags/struts-logic" prefix="logic" %><%
response.setHeader("Cache-Control","no-cache");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String friendAdverId=(String)request.getParameter("id");
String backTo=request.getParameter("backTo");
if(backTo==null||backTo.equals("")) backTo="/friendadver/friendAdverIndex.jsp";
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<logic:present name="result" scope="request">
<% String id=(String)request.getAttribute("id"); %>
<card title="发表话题" ontimer="<%=response.encodeURL("friendAdverMessage.jsp?id="+id)%>">
<timer value="50"/>
<p align="left"><%=BaseAction.getTop(request, response)%>
发表话题<br/>
-----------<br/>
<logic:present name="tip"><font color="red"><bean:write name="tip"/></font><br/></logic:present>
发表成功！5秒后自动跳转！<br/>
<a href="friendAdverMessage.jsp?id=<%=id%>">返回主帖</a><br/>
<a href="friendAdverIndex.jsp">返回交友广告</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:present>

<logic:notPresent name="result" scope="request">
<card title="发表话题">
<p align="left"><%=BaseAction.getTop(request, response)%>
发表话题<br/>
-----------<br/>
<logic:present name="tip">
<bean:write name="tip"/><br/>
</logic:present>
内容：<br/><input name="content" emptyok="false" size="20" maxlength="1000" value="v"/><br/>
<br/>
    <anchor title="确定">发表
    <go href="/friendadver/PostWord.do?friendAdverId=<%=friendAdverId%>" method="post">
	<postfield name="content" value="$content"/>
    </go>
    </anchor>
<br/>
<br/>
<a href="<%=(backTo.replace("&","&amp;"))%>">返回广告页</a><br/>
<a href="/friend/friendCenter.jsp">返回交友中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</logic:notPresent>
</wml>