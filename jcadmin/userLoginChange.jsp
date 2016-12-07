<%--fanys add 2006-06-28--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.Util" %><%@ page import="net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end
response.setHeader("Cache-Control","no-cache");
String id=request.getParameter("id");
if(id!=null){
Util.setFlag(id);%>
<script>
alert("更新成功！");
window.navigate("userLoginChange.jsp");
</script>
<%}%>
<html>
<head>
<script language="javascript">
  function operate1()
     {
     if (confirm('你确定要提交信息吗？')) {
       return true;
       } else {
        return false;
       }
      }
</script>
<title>用户登录五步走管理后台</title>
</head>
<body>
<H1 align="center">用户登录五步走管理后台</H1>
<%
String flag=Util.getFlag();
String name=null;
if(flag.equals("zero")){
name="第零步";
}else if(flag.equals("first")){
name="第一步";
}else if(flag.equals("second")){
name="第二步";
}else if(flag.equals("three")){
name="第三步";
}else if(flag.equals("four")){
name="第四步";
}else if(flag.equals("five")){
name="第五步";
}
%>
<p align="center"><font color="#CC0033">现在用户登录设置为<%=name%></font></p>
<form action="userLoginChange.jsp" method="post" >
<div align="center">
第零步：
<input type="radio" name="id" value="zero" <%if(flag.equals("zero")){%>checked="checked"<%}%>/>
<br/>
第一步：
<input type="radio" name="id" value="first" <%if(flag.equals("first")){%>checked="checked"<%}%>/>
<br/>
第二步：
<input type="radio" name="id" value="second" <%if(flag.equals("second")){%>checked="checked"<%}%>/>
<br/>
第三步：
<input type="radio" name="id" value="three" <%if(flag.equals("three")){%>checked="checked"<%}%>/>
<br/>
第四步：
<input type="radio" name="id" value="four" <%if(flag.equals("four")){%>checked="checked"<%}%>/>
<br/>
第五步：
<input type="radio" name="id" value="five" <%if(flag.equals("five")){%>checked="checked"<%}%>/>
<br/>
<br/>
</div>
<p align="center"><input type="submit" value="提交" onClick="return operate1()"/></p>
</form>
</body>
</html>