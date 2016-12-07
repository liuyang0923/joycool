<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.action.pk.PKWorld" %><%@ page import="net.joycool.wap.bean.pk.PKUserHSkillBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBSkillBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.userBSkillInfo(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转其它场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKUserHSkillBean userHSkill =(PKUserHSkillBean)request.getAttribute("pkUserHSkill");
PKUserBSkillBean userBSkill=(PKUserBSkillBean)PKWorld.getUserBSkill().get(new Integer(userHSkill.getSkillId()));
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=userBSkill.toDetail()%>
当前等级：<%=userHSkill.getRank()%><br/>
当前技能点：<%=userHSkill.getSkillKey()%><br/>
<%if(userHSkill.getExcersize()==1){
	%>修炼中<br/><%
}else{%>
	<anchor title="确定">修炼
	  <go href="userBSkillResult.jsp" method="post">
	    <postfield name="id" value="<%=userHSkill.getId()%>"/>
	  </go>
	</anchor>
	<a href="/pk/delUserBSkillAffirm.jsp?id=<%=userHSkill.getId()%>">删除技能</a><br/><br/>
<%}%><br/>
<a href="/pk/userBSkillList.jsp">返回被动技能</a><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>