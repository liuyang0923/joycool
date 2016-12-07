<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.Constants"%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<head>
<meta http-equiv="Cache-Control" content="max-age=0" forua="true"/>
</head>

<card title="聊天室冲值">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<br/>

聊天室冲值<br/><br/>
<%

String backTo = request.getParameter("backTo");
if(backTo == null){
	backTo = BaseAction.INDEX_URL;
}

String rs = request.getParameter("rs");
if (rs == null){
		//response.sendRedirect(response
		//		.encodeURL("../failure.jsp"));
		BaseAction.sendRedirect("/failure.jsp", response);
}
else if(("1").equals(rs)){
		%>
		冲值成功！
		<%
}else if(("0").equals(rs)){
		%>
		冲值失败！
		<%
}else{
		//response.sendRedirect(response
		//		.encodeURL("../failure.jsp"));
		BaseAction.sendRedirect("/failure.jsp", response);
}
%>
<br/><br/>
    <anchor><prev/>返回上一级</anchor><br/>
    <a href="hall.jsp?roomId=<%=Constants.CHAT_BIGROOM_ID%>" title="进入聊天大厅">进入聊天大厅</a><br/>   


<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>