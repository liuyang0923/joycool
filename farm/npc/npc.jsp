<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.npc();
FarmNpcWorld world = action.world;
FarmUserBean farmUser = action.getUser();
FarmNpcBean npc = (FarmNpcBean)request.getAttribute("npc");
%><%@include file="../inc/trigger.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.getUser().getName().length()==0){%>
你是哪儿来的?我怎么从来没见过你?<br/>
<a href="../user/set.jsp">我要取个名字</a>
<%} else if(npc != null){
List talkList = npc.getTalkList();%>
[<%=npc.getName()%>]<br/>
<%=npc.getIntro()%><br/>

<%for(int i=0;i<talkList.size();i++){
FarmTalkBean talk = (FarmTalkBean)talkList.get(i);
if(talk.getQuest()!=0 && !farmUser.isQuestStart(talk.getQuest())||talk.getPreQuest()!=0 && !farmUser.isQuestEnd(talk.getPreQuest())||!FarmWorld.isCondition(talk.getConditionList(),farmUser)) continue;
%>+<a href="talk.jsp?id=<%=talk.getId()%>"><%=talk.getTitle()%></a><br/><%}%>

<%if(npc.isFlagBuy()){%>
<a href="buys.jsp">购买</a>|
<%}%>
<%if(npc.isFlagSell()){%>
<a href="sells.jsp">出售</a>|
<%}%>
<%if(npc.isFlagEquipSell()){%>
<a href="selles.jsp">回收物品</a>|
<%}%>
<%if(npc.isFlagRepair()){%>
<a href="repair.jsp">修理装备</a>|
<%}%>
<%if(npc.isFlagAuction()){%>
<a href="auction.jsp">拍卖</a>|
<%}%>
<%if(npc.isFlagSkill()){%>
<a href="skills.jsp">技能</a>|
<%}%>
<%if(npc.isFlagQuest()){%>
<a href="quests.jsp">任务</a>|
<%}%>
<%if(npc.isFlagCollect()){%>
<a href="collects.jsp">收藏</a>|
<%}%>
<%if(npc.getCarList().size()>0){%>
<a href="cars.jsp">远行</a>|
<%}%>
<%}else{%>
不存在的人物<br/>
<%}%><br/>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>