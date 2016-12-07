<%@ page contentType="text/html;charset=utf-8" session="false" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%
HttpSession session = request.getSession(false);
if(session!=null){
	Cookie cookie = new Cookie("SID", "");
	cookie.setMaxAge(0);
	cookie.setPath("/");
	response.addCookie(cookie);
}
String name = request.getParameter("name");
if(request.getParameter("del")!=null){
	session.invalidate();
}
%>
<html>
<head>

</head>

<script>
function query(){
if(document.queryForm.session.value.length !=6 && document.queryForm.session.value.length !=8){
alert('wrong session id');
return false;
}
if(document.queryForm.session.value.length ==6)
document.queryForm.session.value = document.queryForm.session.value + '00';
document.queryForm.action=document.queryForm.action + ';' + document.queryForm.session.value;
document.queryForm.submit();
return true;
}
</script>

<body>
<%if(session!=null&&name!=null&&session.getAttribute(name)!=null){%>
<%=StringUtil.outputObject(session.getAttribute(name))%><br/>
<%}%>
<form name="queryForm" action="session.jsp" method="post">
内容:<input name="name" type="text"  size="" ><br/>
session:<input name="session" type="text"  size="38" value="">
</form>
<input type="button" value="提交" onclick="return query()"><br/><br/>
<%if(session!=null){
Enumeration e = session.getAttributeNames();
while(e.hasMoreElements()){
Object en = e.nextElement();
%><a href="<%=("session.jsp?name="+java.net.URLEncoder.encode((String)en,"utf-8"))%>"><%=en%></a>
=<font color=red><%=session.getAttribute((String)en)%></font><br/>
<%}}%>
<a href="session.jsp?del=1">清除session</a>
<body>
</html>