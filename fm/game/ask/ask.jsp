<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="jc.family.game.ask.*"%><%
AskAction action=new AskAction(request,response);
int randrom=net.joycool.wap.util.RandomUtil.nextInt(1000);
int work=action.nextask();
int mid=action.getParameterInt("mid");
if(mid==0){
response.sendRedirect("index.jsp");return;
}
if(work==1){
response.sendRedirect("/fm/game/fmgame.jsp");
return;
}
if(work==3){
Long times=(Long)request.getAttribute("quickTime");
Integer answer=(Integer)request.getAttribute("answer");
response.sendRedirect("quickprompt.jsp?mid="+mid+"&a="+answer+"&t="+times);
return;
}
if(work==5){
response.sendRedirect("over.jsp?mid="+mid);
return;
}
AskBean askBean=(AskBean)request.getAttribute("askBean");
Integer index=(Integer)request.getAttribute("index");
Object note=request.getAttribute("note");
if(askBean==null){
response.sendRedirect("index.jsp");
return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族问答" ontimer="<%=response.encodeURL("ask.jsp?mid="+mid+"&#38;a=5&#38;r="+randrom)%>"><timer value="220"/><p align="left"><%
if(note!=null){%>第<%=note%>题回答已超时<br/><%}%>
第<%=index%>题&#160;<%=StringUtil.toWml(askBean.getQuestion())%><br/>
A:<a href="ask.jsp?mid=<%=mid%>&#38;a=1&#38;r=<%=randrom%>"><%=StringUtil.toWml(askBean.getAnswer1())%></a><br/>
B:<a href="ask.jsp?mid=<%=mid%>&#38;a=2&#38;r=<%=randrom%>"><%=StringUtil.toWml(askBean.getAnswer2())%></a><br/>
C:<a href="ask.jsp?mid=<%=mid%>&#38;a=3&#38;r=<%=randrom%>"><%=StringUtil.toWml(askBean.getAnswer3())%></a><br/>
D:<a href="ask.jsp?mid=<%=mid%>&#38;a=4&#38;r=<%=randrom%>"><%=StringUtil.toWml(askBean.getAnswer4())%></a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>