<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request,response);
RichUserBean richUser = action.getRichUser();
int read = action.getParameterInt("r");
if(read > 0){
richUser.setReadLogCount(read);
if(richUser.readLog() <= 0) {
action.innerRedirect("go.jsp");
return; }}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(richUser.readLog() > 0){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="log.jsp?r=<%=richUser.getLogCount()%>">确定</a><br/>
<%=richUser.log.getLogStringR(richUser.readLog())%>
<a href="log.jsp?r=<%=richUser.getLogCount()%>">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="go.jsp">返回</a><br/>
<%=richUser.log.getLogString(30)%>
<a href="go.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>