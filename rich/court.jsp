<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.court();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInCourt()&&!action.isResult("tip")){	 //离开法院
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(!action.isResult("tip")){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
想要告谁藐视电脑?<br/><%
Iterator iter = action.world.userList.iterator();
while(iter.hasNext()){
RichUserBean target = (RichUserBean)iter.next();if(target!=richUser&&target.isStatusPlay()){%>
<a href="court.jsp?a=1&amp;o=<%=target.getUserId()%>"><%=target.getWmlName()%></a><br/>
<%}}%>
<a href="court.jsp?a=2">离开法院</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}else{%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%><br/>
<a href="go.jsp">确定</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%></wml>