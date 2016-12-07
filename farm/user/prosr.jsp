<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static String[] op={"","升级","学习","废弃"};%><%
response.setHeader("Cache-Control","no-cache");%><%@include file="/bank/checkpw.jsp"%><%
FarmAction action = new FarmAction(request);
int is = action.getParameterInt("id");
FarmProBean pro = action.world.getPro(is);
action.prosr();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
int act = action.getParameterInt("a");
int index = action.getParameterInt("id");%>
<%if(act>0&&act<4){%>
<a href="prosr.jsp?c=1&amp;id=<%=index %>&amp;a=<%=act%>">确认<%=op[act]%>[专业]<%=pro.getName()%></a>?<br/>
<%}%>

<%}%>
<a href="pros.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>