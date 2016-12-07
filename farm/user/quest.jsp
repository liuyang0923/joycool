<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.quest();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>

<%}else{
FarmUserQuestBean userQuest = (FarmUserQuestBean)request.getAttribute("userQuest");
FarmQuestBean quest = action.nWorld.getQuest(userQuest.getQuestId());
FarmUserBean farmUser = action.getUser();
FarmNpcBean npc = action.nWorld.getNpc(userQuest.getNpcId());
%>
[任务]<%=quest.getName()%><br/>
<%=quest.getObjective()%><br/>
<%if(npc!=null){%>来源:<%=npc.getName()%>[<%=action.world.getNodeDetail(npc.getPos())%>]<br/><%}%>
<% String objective = FarmWorld.getItemListString(quest.getMaterialList(), quest.getPrice())+FarmWorld.getCreatureListString(quest.getCreatureList());
if(objective.length()>0){%>
目标:<%if(quest.isFlagHideHunter()){%>(未知)<%}else{%><%=objective%><%}%><br/><%}%>
<% if(quest.getConditionList().size()!=0){
%>额外条件:<%=FarmWorld.getConditionString(quest.getConditionList())%><br/><%}%>
奖励:<%if(quest.isFlagHidePrize()){%>(未知奖励)<%}else{%><%=FarmWorld.getItemListString(quest.getProductList())%><%=FarmWorld.getPrizeString(quest.getPrizeList())%><%}%><br/>
<%if(!quest.isFlagNoAbort()){%><a href="quest.jsp?a=1&amp;id=<%=userQuest.getId()%>">放弃任务</a><br/><%}%>

<%}%>
<a href="quests.jsp">查看其他任务</a><br/>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>