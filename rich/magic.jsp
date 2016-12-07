<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="java.util.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
RichAction action = new RichAction(request);%><%@include file="filter.jsp"%><%
action.magic();
RichUserBean richUser = action.getRichUser();
if(!richUser.isInMagic()&&!action.isResult("tip")){	 //离开魔法屋
response.sendRedirect(("go.jsp"));
return; }
int act = action.getParameterInt("a");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><%if(!action.isResult("tip")){%>
<card title="大富翁">
<p align="left">
<%=BaseAction.getTop(request, response)%>
想要对谁使用魔法?<br/><%
Iterator iter = action.world.userList.iterator();
while(iter.hasNext()){
RichUserBean target = (RichUserBean)iter.next();if(target.isStatusPlay()){%>
<a href="magic.jsp?a=1&amp;o=<%=target.getUserId()%>"><%=target.getWmlName()%></a><br/>
<%}}%>
<a href="magic.jsp?a=2">离开魔法屋</a><br/>
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