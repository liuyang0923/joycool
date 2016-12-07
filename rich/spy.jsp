<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.spy();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInSpy()){	 //离开间谍屋
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
欢迎来到间谍屋<br/>
以下是所有玩家的资料:<br/><%
Iterator iter = action.world.userList.iterator();
while(iter.hasNext()){
RichUserBean target = (RichUserBean)iter.next();%>
<%=target.getWmlName()%><br/>
<%if(target.isStatusPlay()){%>
现金:<%=target.getMoney()%><br/>
存款:<%=target.getSaving()%><br/>
点券:<%=target.getMoney2()%><br/>
<%}else{%>
(已破产)<br/>
<%}%><%}%>
<a href="spy.jsp?a=2">离开间谍屋</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>