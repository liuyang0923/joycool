<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.questBegin();
FarmWorld fWorld = action.farmWorld;
FarmQuestBean quest = (FarmQuestBean)request.getAttribute("quest");
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%if(quest!=null){
FarmUserQuestBean userQuest = farmUser.getStartQuest(quest.getId());
if(userQuest!=null){
%>
<a href="../user/quest.jsp?id=<%=userQuest.getId()%>">查看刚接受的任务</a><br/>
<%}}%>
<%}else{%>

[任务]<%=quest.getName()%><%if(quest.getRank()>1){%>(等级<%=quest.getRank()%>)<%}%><br/>
<%=FarmWorld.replaceInfo(quest.getInfo(), farmUser)%><br/>
<% String objective = FarmWorld.getItemListString(quest.getMaterialList(), quest.getPrice())+FarmWorld.getCreatureListString(quest.getCreatureList());
if(objective.length()>0){%>
目标:<%if(quest.isFlagHideHunter()){%>(未知)<%}else{%><%=objective%><%}%><br/><%}%>
<% if(quest.getConditionList().size()!=0){
%>额外条件:<%=FarmWorld.getConditionString(quest.getConditionList())%><br/><%}%>
奖励:<%if(quest.isFlagHidePrize()){%>(未知奖励)<%}else{%><%=FarmWorld.getItemListString(quest.getProductList())%><%=FarmWorld.getPrizeString(quest.getPrizeList())%><%}%><br/>
<%if(quest.isFlagRedo()){%>(该任务可重复完成)<br/><%}%>
<a href="questB.jsp?a=1&amp;id=<%=quest.getId()%>">接受任务</a><br/>

<%}%>
<a href="quests.jsp">返回</a>|<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>