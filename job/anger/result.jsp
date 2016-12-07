<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page errorPage=""%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.action.job.AngerAction" %><%@ page import="net.joycool.wap.util.StringUtil" %><%
response.setHeader("Cache-Control","no-cache");
AngerAction action=new AngerAction(request);
action.result(request);
if(session.getAttribute("angerpoint")==null){
    response.sendRedirect("index.jsp");
    return;
}
String images=(String)session.getAttribute("angerimg");
String card=(String)session.getAttribute("angercard");
String content=(String)session.getAttribute("angercontent");
String gamePoint=(String)session.getAttribute("angergamePoint");
String point=(String)session.getAttribute("angerpoint");
String count=(String)session.getAttribute("angercount");
String name=(String)session.getAttribute("angername");
int cardNum=StringUtil.toInt((String)session.getAttribute("angercardNum"));
String expression=(String)session.getAttribute("angerexpression");

//session.removeAttribute("angercount");
session.removeAttribute("angerrefresh");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="出气筒游戏" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="/img/job/anger/<%=images %>" alt="出气对象"/><br/>
<%=StringUtil.toWml(name)%><%=expression%><br/>
血条<img src="<%=("/img/job/anger/0.gif" )%>" alt="血条"/><br/>
你用<%=count%>招击败了<%=StringUtil.toWml(name)%>!奖励<%=point%>经验值！<br/>
<%--<%if(card!=null){%>奖励<%=card%><%=(cardNum>0 && cardNum<8)? "3" : "1"%>张！<%if(content!=null)%><%=content%>
	<%}%>--%>
<%if(cardNum>0 && cardNum<8){%>（在
<a href="/job/hunt/viewQuarryList.jsp">
狩猎专区</a>保存）
<%}%>
<%if(!("0".equals(gamePoint))){%>奖励<%=gamePoint%>乐币！<%}%> <br/>
<a href="index.jsp">再打一次</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>
