<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%! static int NUMBER_OF_PAGE = 30;%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.chat();
String url = ("chat.jsp");
TeamBean team = (TeamBean)request.getAttribute("team");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(team!=null){%><a href="chat.jsp">返回<%=team.getName()%></a><br/><%}%>
<%}else{
int id = action.getParameterInt("to");		// 对某用户发言
List actList = TeamAction.getActList();
PagingBean paging = new PagingBean(action, actList.size(),NUMBER_OF_PAGE, "p");
for(int i = paging.getStartIndex();i < paging.getEndIndex();i++){
TeamActBean act = (TeamActBean)actList.get(i);
%><a href="<%if(id==0){%>chat.jsp?act=<%=act.getId()%><%}else{%>chat.jsp?act=<%=act.getId()%>&amp;tid=<%=id%><%}%>"><%=act.getName2()%></a>
<%if(i%3==2){%><br/><%}else{%>,<%}%>
<%}%>
<%if(id==0){%>
<%=paging.shuzifenye("act.jsp", false, "|", response)%>
<%}else{%>
<%=paging.shuzifenye("act.jsp?to="+id, true, "|", response)%>
<%}%>
<a href="chat.jsp">返回聊天</a><br/>

<%}%>

<a href="index.jsp">返回圈子列表</a><br/>
</p>
</card>
</wml>