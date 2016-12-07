<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.quests();
FarmWorld fWorld = action.farmWorld;
FarmNpcBean npc = (FarmNpcBean)request.getAttribute("npc");
if(npc==null) {
	response.sendRedirect(("/farm/map.jsp"));
	return;
}
List begin = npc.getQuestBeginList();
List end = npc.getQuestEndList();
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
==提交任务==<br/>
<%
for(int i=0;i<end.size();i++){
int id = ((Integer)end.get(i)).intValue();
FarmQuestBean quest = action.world.getQuest(id);
FarmUserQuestBean userQuest = farmUser.getStartQuest(quest.getId());
%>
<%if(userQuest!=null && !quest.isFlagHideEnd()){%>
<a href="questE.jsp?id=<%=userQuest.getId()%>"><%=quest.getName()%></a><br/>
<%}%>
<%}%>

==接受任务==<br/>
<%
for(int i=0;i<begin.size();i++){
int id = ((Integer)begin.get(i)).intValue();
FarmQuestBean quest = action.world.getQuest(id);
if(quest.isFlagClosed()) continue;
%>
<%if(!farmUser.isQuestStart(quest.getId())){%>
<%if(farmUser.canDoQuest(quest, action.now)){
if(quest.isFlagHideBegin()) continue;
%>
<%if(FarmNpcWorld.canDoQuest(farmUser,quest)){%>
<a href="questB.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>
<%}else{%>
<%=quest.getName()%>(未知)
<%}%>

<%}else{%>
<%=quest.getName()%>(已完成)
<%}%>

<br/>
<%}%>
<%}%>
<a href="npc.jsp">返回</a>|
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>