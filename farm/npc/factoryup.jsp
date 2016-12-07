<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
FactoryBean factory = action.getUserFactory();
List procList = factory.getProcessList();
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
即将处理的加工列表<br/>

<%
for(int i=0;i<procList.size()&&i<10;i++){
FactoryProcBean proc = (FactoryProcBean)procList.get(i);
FactoryComposeBean compose = action.world.getFactoryCompose(proc.getComposeId());
FarmUserBean user = FarmWorld.one.getFarmUserCache(proc.getUserId());
%>
<%=compose.getName()%>
<%if(farmUser.getUserId()!=proc.getUserId()){%>
(<%if(user!=null){%><a href="../user/info.do?id=<%=user.getUserId()%>"><%=user.getNameWml()%><%}else{%>未知<%}%></a>)
<%}else{%>
(未知)
<%}%>
<br/>
<%}%>

<a href="factory.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>