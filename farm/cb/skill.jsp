<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.skill();
FarmWorld fWorld = action.world;
FarmSkillBean skill = (FarmSkillBean)request.getAttribute("skill");
long cooldown = 0;
if(skill.getCooldown()>0)
	cooldown = action.getUser().getCooldown(skill.getCooldownId(),action.now);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{%>
<%=skill.getName()%><br/>
<%=skill.getInfo()%><br/>
<%if(cooldown>0){%>
还需要<%=DateUtil.formatTimeInterval(cooldown)%>才能再次使用<br/>
<a href="skill.jsp?id=<%=skill.getId()%>">刷新</a><br/>
<%}else{%>
<a href="ac.jsp?skill=<%=skill.getId()%>">使用技能</a><br/>
<%}%>
<%}%>
<a href="skills.jsp?pro=<%=skill.getProId()%>">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>