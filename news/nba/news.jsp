<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%>
<%@ page import="net.joycool.wap.framework.BaseAction,jc.news.nba.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control","no-cache");
	NbaAction action = new NbaAction(request);
	BeanNews bn = action.getNewById();
	int from = action.getParameterInt("from");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新闻"><p><%=BaseAction.getTop(request, response)%><%
if(bn != null){
	%><%=bn.getName()%><br/> 
	<%=StringUtil.toWmlIgnoreAnd(bn.getCont())%><br/><%	
}else{
	%>没有此记录哦!<br/><%
}
if(from == 1){
%><a href="news1.jsp">返回上一级</a><%
}else if(from == 2){
%><a href="news2.jsp">返回上一级</a><%
}else if(from == 3){
%><a href="news3.jsp">返回上一级</a><%
}else if(from == 4){
%><a href="news4.jsp">返回上一级</a><%
}else{
%><a href="index.jsp">返回上一级</a><%
}%><br/>
<a href="rank.jsp">常规赛排名</a><br/>
<a href="index.jsp">返回NBA专题</a><br/><%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>