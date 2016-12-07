<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.spec.tiny.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page errorPage=""%><%@ page import="net.joycool.wap.service.infc.IJobService" %><%@ page import="net.joycool.wap.service.factory.ServiceFactory" %><%@ page import="net.joycool.wap.action.job.AngerAction" %><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%!
static Tiny2Game[] games = {
new Tiny2Game2(50, 300),
};
%><%
response.setHeader("Cache-Control","no-cache");
AngerAction action=new AngerAction(request);

	Tiny2Action action2 = new Tiny2Action(request, response);
	if(action2.checkGame()) return;
	if(action2.getGame() == null){
		if(net.joycool.wap.util.RandomUtil.nextInt(150) == 0){
			action2.startGame(games[0]);
			return;
		}
	}

action.ventAnger(request);
String bleed=(String)session.getAttribute("angerbleed");
String tip=(String)request.getAttribute("tip");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="出气筒游戏">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(tip!=null){%>
<%=tip%><br/>
<a href="/job/anger/index.jsp">返回上级</a><br/>
<a href="/lswjs/wagerHall.jsp">游戏首页</a><br/>
<%}else{
if("0".equals(bleed)){
out.clearBuffer();
response.sendRedirect("index.jsp");
return;
}
if((String)session.getAttribute("angergender")==null){
out.clearBuffer();
response.sendRedirect("index.jsp");
return;
}
int angersCount=StringUtil.toInt((String)session.getAttribute("angersCount"));
if(angersCount<0)
angersCount=0;
String name=(String)session.getAttribute("angername");
session.removeAttribute("angerrefresh");
String images=(String)session.getAttribute("angerimg");
String gender=(String)session.getAttribute("angergender");
String relation=(String)session.getAttribute("angerrelation");
String expression=(String)session.getAttribute("angerexpression");
%><%=loginUser.showImg("/img/job/anger/"+images)%>
<%=StringUtil.toWml(name)%><%=expression%><br/>
血条<img src="/img/job/anger/<%=bleed%>.gif" alt="血条"/><br/>
<anchor title="提交">左拳(1万)
    <go  href="/job/anger/jump.jsp" method="post">
      <postfield name="actions" value="1"/>
        <postfield name="angersCount" value="<%=angersCount%>"/>
     </go>
</anchor>
<anchor title="提交">右拳(1万)
    <go  href="/job/anger/jump.jsp" method="post">
      <postfield name="actions" value="2"/>
      <postfield name="angersCount" value="<%=angersCount%>"/>
         </go>
</anchor><br/>
<anchor title="提交">飞腿(1万)
    <go  href="/job/anger/jump.jsp" method="post">
      <postfield name="actions" value="3"/>
      <postfield name="angersCount" value="<%=angersCount%>"/>
     </go>
</anchor> 
<anchor title="提交"> 折椅(1万)
    <go  href="/job/anger/jump.jsp" method="post">
      <postfield name="actions" value="4"/>
      <postfield name="angersCount" value="<%=angersCount%>"/>
    </go>
</anchor><br/>
<a href="/job/anger/index.jsp">饶了<%if(gender.equals("1")){%>他<%}else{%>她<%}%>（返回上级）</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>