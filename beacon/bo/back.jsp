<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*,net.joycool.wap.spec.bottle.*" %><%
	BottleAction action = new BottleAction(request);
	String f=action.getParameterString("f");
	if ("create".equals(f)){
		action.sendRedirect("create.jsp",response);
	}else if ("bottles".equals(f)){
		action.sendRedirect("bottles.jsp",response);
	}else if ("reve".equals(f)){
		action.sendRedirect("reversion.jsp",response);
	}else if("list".equals(f)){
		action.sendRedirect("list.jsp",response);
	}else{
		action.sendRedirect("bottles.jsp",response);
	}
%>