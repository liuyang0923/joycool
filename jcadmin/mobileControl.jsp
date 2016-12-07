<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%
if(request.getParameter("change") != null){
	SecurityUtil.getMobile = StringUtil.toInt(request.getParameter("change"));
	if(SecurityUtil.getMobile < 0 || SecurityUtil.getMobile > 2)
		SecurityUtil.getMobile = 2;
	//response.sendRedirect("mobileControl.jsp");
	BaseAction.sendRedirect("/jcadmin/mobileControl.jsp", response);
}

if(SecurityUtil.getMobile == 0){%>
不取手机号
<%}else {%>
<a href="mobileControl.jsp?change=0">不取手机号</a>
<%}%>
<br/>
<%if(SecurityUtil.getMobile == 1){%>
用lt旧接口
<%}else {%>
<a href="mobileControl.jsp?change=1">用lt旧接口</a>
<%}%>
<br/>
<%if(SecurityUtil.getMobile == 2){%>
用新接口
<%}else {%>
<a href="mobileControl.jsp?change=2">用新接口</a>
<%}%>