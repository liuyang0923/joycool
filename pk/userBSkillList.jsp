<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.action.pk.PKWorld" %><%@ page import="java.util.List" %><%@ page import="net.joycool.wap.bean.pk.PKUserHSkillBean" %><%@ page import="net.joycool.wap.bean.pk.PKUserBSkillBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.userBSkillList(request);
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
List userHskill1 =(List)request.getAttribute("userHskill1");
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="../img/pk/bSkill.gif" alt="被动技能"/><br/>
<%
if(userHskill1.size()>0){
	PKUserHSkillBean userHSkill = null;
	PKUserBSkillBean userBSkill = null;
	for(int i=0;i<userHskill1.size();i++){
		userHSkill=(PKUserHSkillBean)userHskill1.get(i);
		userBSkill=(PKUserBSkillBean)PKWorld.getUserBSkill().get(new Integer(userHSkill.getSkillId()));
		if(userBSkill==null)continue;%>
		<%=i+1%>.<%=userBSkill.getName()%>
	<a href="/pk/userBSkillInfo.jsp?id=<%=userHSkill.getId()%>">详情</a>
		<%if(userHSkill.getExcersize()==1){%>修炼中<%}
	%><br/>
	<%}
}else{%>您目前没有学习被动技能<%}%><br/>
	<%--}<%else{%>
		<anchor title="确定">修炼
		  <go href="userBSkillResult.jsp" method="post">
		    <postfield name="id" value="<%=userHSkill.getId()%>"/>
		  </go>
		</anchor><br/>
	<%}%> --%>

<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>