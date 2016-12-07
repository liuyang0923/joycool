<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.all();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

List teamList = (List)request.getAttribute("teamList");
PagingBean paging = new PagingBean(action, teamList.size(), 10, "p");
int endIndex = paging.getEndIndex();
for(int i = paging.getStartIndex();i < endIndex;i++){
TeamBean team = action.getTeam(((Integer)teamList.get(i)).intValue());
if((team.getFlag()&4)!=0) continue;
%><%=i+1%>.<%if(team.isFlagOutBrowse()){%><a href="chat.jsp?ti=<%=team.getId()%>"><%=StringUtil.toWml(team.getName())%></a><%}else{%><%=StringUtil.toWml(team.getName())%><%}%>-
<a href="info.jsp?ti=<%=team.getId()%>">查看</a><br/>
<%}%>
<%=paging.shuzifenye("all.jsp", false, "|", response)%>
<%}%>
<a href="index.jsp">返回圈子首页</a><br/>
</p>
</card>
</wml>