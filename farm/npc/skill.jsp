<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.skill();
FarmWorld fWorld = action.farmWorld;
FarmSkillBean skill = (FarmSkillBean)request.getAttribute("skill");
FarmUserBean farmUser = action.getUser();
FarmUserProBean userPro = farmUser.getPro(skill.getProId());
FarmProBean pro = fWorld.getPro(skill.getProId());
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
<%=FarmWorld.formatMoney(skill.getPrice())%><br/>
需要(<%=pro.getName()%>)等级<%=skill.getRank()%><br/>
冷却:<%=DateUtil.formatTimeInterval(skill.getCooldown())%><br/>
<%if(userPro!=null&&userPro.getRank()>=skill.getRank()){%>
<a href="skill.jsp?a=1&amp;id=<%=skill.getId()%>">学习</a><br/>
<%}%>
<%}%>
<a href="skills.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>