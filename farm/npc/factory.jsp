<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.factory();
FactoryBean factory = (FactoryBean)request.getAttribute("factory");
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===<%=factory.getName()%><a href="../help/factory.jsp">??</a>===<br/>
<%=factory.getInfo()%><br/>
加工间隔<%=DateUtil.formatTimeInterval(factory.getInterval()*60)%><br/>
<%if(factory.getUndoneCount()>0){%>
当前还有<a href="factoryup.jsp"><%=factory.getUndoneCount()%>个加工未处理</a><br/>
<%}%>
<a href="factorycs.jsp">查看可加工清单</a><br/>
<a href="factoryps.jsp">领取产品</a><br/>
<a href="../map.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>