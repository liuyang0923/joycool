<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.skills();
FarmWorld fWorld = action.farmWorld;
List skills = (List)request.getAttribute("skills");
if(skills==null) {
	response.sendRedirect(("/farm/map.jsp"));
	return;
}
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
for(int i=0;i<skills.size();i++){
int skillId = ((Integer)skills.get(i)).intValue();
FarmSkillBean bean = fWorld.getSkill(skillId);
FarmUserProBean pro = farmUser.getPro(bean.getProId());
%>
<%if(pro==null||!pro.hasSkill(skillId)){%>
<%if(pro!=null&&pro.getRank()>=bean.getRank()&&(bean.getClass1()==0||bean.getClass1()==farmUser.getClass1())){%>
<a href="skill.jsp?id=<%=bean.getId()%>"><%=bean.getName()%></a>(<%=StringUtil.bigNumberFormat(bean.getPrice())%>)
<a href="skill.jsp?a=1&amp;id=<%=bean.getId()%>">直接学习</a><br/>
<%}else{%>
<a href="skill.jsp?id=<%=bean.getId()%>"><%=bean.getName()%></a>(无法学习)<br/>
<%}%>
<%}else{%>
<%=bean.getName()%>(已学会)<br/>
<%}%>
<%}%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>