<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.test.*"%><%
response.setHeader("Cache-Control","no-cache");
String info="";
if(request.getParameter("isFinished")!=null||session.getAttribute("isFinished")!=null){
	info="成人用品问卷调查已经结束。谢谢!";
	session.setAttribute("isFinished","1");
}else{
	info="您已经填写了成人用品问卷调查。谢谢参与！";
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="成人用品调查问卷">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=info%><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>