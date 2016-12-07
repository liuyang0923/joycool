<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@page import="net.joycool.wap.util.RandomUtil"%><%@ page import="java.util.*,jc.guest.pt.*,jc.guest.*" %>
<%response.setHeader("Cache-Control","no-cache"); %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">

<wml>
<card title="智慧拼图">
<p align="left">

<%response.setHeader("Cache-Control","no-cache");
JigsawAction action=new JigsawAction(request,response);
int level=1;
if(request.getParameter("l")!=null){
	level=action.getParameterInt("l");
}%>
请选择难度:<br/>
<%if(level==1){%>普通<%}else{%><a href="piclist.jsp?l=1">普通</a><%}%>|<%if(level==2){%>中等<%}else{%><a href="piclist.jsp?l=2">中等</a><%}%>|<%if(level==3){%>困难<%}else{%><a href="piclist.jsp?l=3">困难</a><%}%><br/>
<%
List list= action.getJigsawList(level);// 得到所有的可以拼的图片的信息
if(list!=null&&list.size()>0){
	for(int i=0;i<list.size();i++){
		JigsawBean bean=(JigsawBean)list.get(i);
		%>
		<a href="picjigsaw.jsp?id=<%=bean.getId() %>"><%=bean.getPicName()%></a><br/>
	<%}}%>
<%if(request.getAttribute("PicList")!=null&&!"".equals(request.getAttribute("PicList"))){%>
<%=request.getAttribute("PicList")%>
<%}%> 
<a href="instructions.jsp">返回智慧拼图首页</a><br/>
<%=JigsawAction.winString==null?"还没有人玩过一关哦,快来做第一个吧!":JigsawAction.winString%><br/>
<%=BaseAction.getBottomShort(request,response)%></p>
</card>
</wml>