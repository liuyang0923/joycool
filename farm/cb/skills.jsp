<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
FarmWorld fWorld = action.world;
FarmUserProBean pro = farmUser.getPro(FarmProBean.PRO_SKILL_A);
FarmUserProBean pro2 = farmUser.getPro(FarmProBean.PRO_SKILL_C);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="acb.jsp">自动攻击</a><br/>

<%
List skills = new ArrayList();
if(pro != null) skills.addAll(pro.getSkillList());
if(pro2 != null) skills.addAll(pro2.getSkillList());
if(skills.size()>0)
for(int i=0;i<skills.size();i++){
int skillId = ((Integer)skills.get(i)).intValue();
FarmSkillBean bean = fWorld.getSkill(skillId);
%>
<a href="ac.jsp?skill=<%=bean.getId()%>"><%=bean.getName()%></a>-
<a href="skill.jsp?id=<%=bean.getId()%>">技能属性</a><br/>
<%}%>
<a href="ac.jsp">普通攻击</a><br/>
<a href="cb.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>