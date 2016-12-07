<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.skills();
FarmWorld fWorld = action.world;
FarmUserProBean pro = (FarmUserProBean)request.getAttribute("pro");
List skills = pro.getSkillList();
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
%>
<a href="skill.jsp?id=<%=bean.getId()%>"><%=bean.getName()%></a>
<%if(!bean.isFlagBattle()){%>-<a href="skill.jsp?a=1&amp;id=<%=bean.getId()%>">直接使用</a><%}%><br/>
<%}%>
<a href="mypro.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>