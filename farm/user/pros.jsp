<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");%><%@include file="/bank/checkpw.jsp"%><%
FarmAction action = new FarmAction(request);
action.pros();
FarmUserBean user = action.getUser();
FarmUserProBean[] pros = user.getPro();
FarmWorld world = action.world;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
===专业<a href="../help/pro.jsp">??</a>===<br/>
剩余点数<%=user.getProPoint()%><br/>
<% for(int i=0;i<pros.length;i++){
FarmUserProBean userPro = pros[i];
if(userPro==null) continue;
FarmProBean pro = world.getPro(i);
if(pro==null) continue;
%>
+
<a href="pro.jsp?id=<%=i%>"><%=pro.getName()%></a>
<%=userPro.getRank()%>级[
<%if(userPro.canUpgrade(pro)){%>
<a href="prosr.jsp?a=1&amp;id=<%=i%>">升级</a>
<%}else{%>
<%=userPro.getExp()%>/<%=userPro.getUpgradeExp()%>
<%}%>
]<a href="prosr.jsp?a=3&amp;id=<%=i%>">废弃</a>
<br/>
<%}%>

<% for(int i=0;i<pros.length;i++){
FarmUserProBean userPro = pros[i];
if(i != 3 && i != 4 && i != 6 && i != 5 && i != 7 && i != 8 && i != 9 && i != 10 && (user.getClass1()==0||i!=14)) continue;

if(userPro!=null) continue;
FarmProBean pro = world.getPro(i);
if(pro==null) continue;
%>
*<a href="pro.jsp?id=<%=i%>"><%=pro.getName()%></a>[<a href="prosr.jsp?a=2&amp;id=<%=i%>">学习</a>]<br/>
<%=pro.getInfo()%><br/>
<%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>