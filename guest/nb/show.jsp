<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%response.setHeader("Cache-Control","no-cache");%>
<%@page import="net.joycool.wap.bean.*,net.joycool.wap.util.StringUtil,jc.guest.battle.*,java.util.ArrayList"%><%@page import="net.joycool.wap.framework.BaseAction"%>
<%GamepageAction action=new GamepageAction(request); 
if(session.getAttribute("stored")==null){
	response.sendRedirect("game.jsp");
	return;
}
int pagination=StringUtil.toId(request.getParameter("j"));
if(pagination<0)
	pagination=0;
int x=0;  
ArrayList depictstring=((Stored)session.getAttribute("stored")).getDepict();
%>
<?xml version="1.0" encoding="UTF-8"?><!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="战斗记录"><p>
<%=BaseAction.getTop(request,response)%>
<%
for(int i=pagination;i<depictstring.size();i++){if(x>19){
		break;}
x++;
%>
<%=i+1%>.<%=StringUtil.toWml(depictstring.get(i).toString())%><br/>
<%}if((pagination-20)>=0){ %>
		<a href="show.jsp?j=<%=pagination-20 %>">上一页</a>		
		<%}if((!((depictstring.size()-pagination)>20 && (depictstring.size() -(pagination+20))!=0)) && pagination>0){%>
			<br/>
	<%}if((depictstring.size()-pagination)>20 && (depictstring.size() -(pagination+20))!=0){ %>
		<a href="show.jsp?j=<%=pagination+20%>">下一页</a><br/>
	<%}%>
<a href="battle.jsp">返回战斗</a><br/>
<a href="game.jsp">返回游戏首页</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p>
</card>
</wml>
