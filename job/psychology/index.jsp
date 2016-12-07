<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.PsychologyAction"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
PsychologyAction action=new PsychologyAction();
int pageIndex=0;
PsychologyBean psychology=null;
Vector psychologyList=null;
String pagination=null;
action.psychology(request,response);
psychologyList=(Vector)request.getAttribute("psychologyList");
pagination=(String)request.getAttribute("pagination");
pageIndex=StringUtil.toInt((String)request.getAttribute("pageIndex"));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="心理测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(pageIndex==0){%>欢迎光临心理测评中心！选择你感兴趣的测试开始吧:)<br/><%}%>
<%
if(null!=psychologyList){
	for(int i=0;i<psychologyList.size();i++){
		psychology=(PsychologyBean)psychologyList.get(i);
%>
<a href="question.jsp?id=<%=psychology.getId()%>"><%=StringUtil.toWml(psychology.getTitle())%></a><br/>
<%}}%>
<%if(!(pagination==null||pagination.equals(""))){%><%=pagination%><br/><%}%>
跳到第<input name="pageNo" value="" maxlength="4" />页<anchor>Go<go href="index.jsp"><postfield name="pageNo" value="$pageNo" /></go></anchor><br/>
<a href="/lswjs/gameIndex.jsp" >返回游戏首页</a><br/>
<a href="/lswjs/index.jsp">返回导航中心</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
