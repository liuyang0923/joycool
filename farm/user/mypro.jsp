<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
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
===专业技能===<br/>
<% for(int i=0;i<pros.length;i++){
FarmUserProBean userPro = pros[i];
if(userPro==null) continue;
FarmProBean pro = world.getPro(i);
if(pro==null) continue;
%>
+<%if(!userPro.getSkillList().isEmpty()){%>
<a href="skills.jsp?pro=<%=i%>"><%=pro.getName()%></a>
<%}else{%><%=pro.getName()%><%}%>
<%=userPro.getRank()%>级[<%=userPro.getExp()%>/<%=userPro.getUpgradeExp()%>]<br/>
<%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>