<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.creature();
FarmNpcWorld world = action.world;
CreatureBean creature = (CreatureBean)request.getAttribute("creature");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(creature != null){
CreatureTBean template = creature.getTemplate();
%>
<%=template.getName()%>(等级<%if(template.isFlagNoLevel()){%>??<%}else{%><%=creature.getLevel()%><%}%>)
<%if(template.isFlagElite()){%>精英<%}%><br/>
<%if(creature.isDead()){%>
(尸体)<br/>
<%}else{%>
<a href="../cb/cb.jsp?a=1&amp;id=<%=creature.getGid()%>">战斗</a><br/>
<%}%>
血:<%if(template.isFlagNoHp()){%>??/??<%}else{%><%=creature.getHp()%>/<%=creature.getHpMax()%><%}%><br/>
<%=creature.getInfo()%><br/>
<%if(template.isFlagCollect()){%>[可收藏]<br/><%}%>
<%}else{%>
不存在的对象<br/>
<%}%>
<a href="../map.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>