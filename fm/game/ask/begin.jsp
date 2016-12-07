<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.ask.*"%><%
AskAction action=new AskAction(request,response);
int result=action.beginask();
int mid=action.getParameterInt("mid");
if(mid==0){
response.sendRedirect("index.jsp");return;
}
if(result==-1){
response.sendRedirect("over.jsp?mid="+mid);return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml"><%
if(result==0){%>
<wml><card title="家族" ontimer="<%=response.encodeURL("/fm/game/fmgame.jsp")%>"><timer value="30"/><p align="left">
<%=action.getTip()%><br/>
<a href="/fm/game/fmgame.jsp">直接返回</a><br/><%
}else{%>
<wml><card title="家族问答"><p align="left"><%=BaseAction.getTop(request, response)%>
家族问答已开始,每道题作答时间为15秒,超过时间该题计为回答错误.结束答题后,显示最终答题统计,并计算家族总分<br/>
<a href="ask.jsp?mid=<%=mid%>">开始答题</a><br/>
<a href="/fm/myfamily.jsp?id=<%=result%>">返回我的家族</a><br/><%
}%> 
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>