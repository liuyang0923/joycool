<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.friend.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.UserBean" %><%!
static java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy-MM-dd");%><%
response.setHeader("Cache-Control","no-cache");
PkgAction action = new PkgAction(request);
action.set();
PkgBean pkg = (PkgBean)request.getAttribute("pkg");;
if(pkg==null){
	response.sendRedirect("my1.jsp");
	return;
}
PkgTypeBean type = (PkgTypeBean)action.getPkgType(pkg.getType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="礼品店">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(pkg!=null){%>
<a href="send.jsp?id=<%=pkg.getId()%>">返回</a><br/>
<%}%>
<%}else{%>
<%=type.getName()%><br/>
发送时间:(如果不需要定时发送请留空)<br/>
<select name="date"><%
Calendar cal = Calendar.getInstance();
for(int i=0;i<14;i++){
	String startTime=sdf1.format(cal.getTime());
	cal.add(Calendar.DATE, 1);
%><option value="<%=startTime%>"><%=startTime%></option><%}%></select>
<select name="hour" ><%for(int i=0;i<24;i++){%><option value=" <%=i%>"><%=i%></option><%}%></select>点
<select name="minute" ><%for(int i=0;i<60;i+=5){%><option value=" <%=i%>"><%=i%></option><%}%></select>分<br/>
打开时间:(让礼包在选订的时间之前无法打开)<br/>
<select name="date2"><%
cal = Calendar.getInstance();
for(int i=0;i<14;i++){
	String startTime=sdf1.format(cal.getTime());
	cal.add(Calendar.DATE, 1);
%><option value="<%=startTime%>"><%=startTime%></option><%}%></select>
<select name="hour2" ><%for(int i=0;i<24;i++){%><option value=" <%=i%>"><%=i%></option><%}%></select>点
<select name="minute2" ><%for(int i=0;i<60;i+=5){%><option value=" <%=i%>"><%=i%></option><%}%></select>分<br/>
<anchor title="确定">确认提交
<go href="set.jsp?id=<%=pkg.getId()%>" method="post">
    <postfield name="date" value="$date"/>
    <postfield name="hour" value="$hour"/>
    <postfield name="minute" value="$minute"/>
    <postfield name="date2" value="$date2"/>
    <postfield name="hour2" value="$hour2"/>
    <postfield name="minute2" value="$minute2"/>
</go></anchor><br/>
<%if(pkg!=null){%>
如果不想设置时间限制请直接点击<a href="send.jsp?id=<%=pkg.getId()%>">返回</a><br/>
如果想删除之前的时间设置请点击<a href="set.jsp?date=&amp;id=<%=pkg.getId()%>">删除时间设置</a><br/>
<%}%>
<%}%>
<a href="my1.jsp">返回我的礼包列表</a><br/>
<a href="index.jsp">返回礼包店首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>