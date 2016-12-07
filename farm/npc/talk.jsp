<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.talk();
FarmNpcWorld world = action.world;
int id = action.getParameterInt("id");
FarmUserBean farmUser = action.getUser();
FarmTalkBean talk = world.getTalk(id);
List talkList = talk.getLinkList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(talk==null){%>
不存在的数据<br/>

<%}else{
// 触发器
TriggerUtil.talkTrigger.trigger(id, farmUser);
%>
<%=FarmWorld.replaceInfo(talk.getContent(),action.getUser())%><br/>
<%if(talk.getQuestBegin()>0){
FarmQuestBean quest = world.getQuest(talk.getQuestBegin());
if(!farmUser.isQuestStart(quest.getId())&&!farmUser.isQuestEnd(quest.getId())&&FarmNpcWorld.canDoQuest(farmUser,quest)){
%>
[任务]<a href="questB.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a><br/>
<%}}%>
<%if(talk.getQuestEnd()>0){
FarmQuestBean quest = world.getQuest(talk.getQuestEnd());
FarmUserQuestBean userQuest = farmUser.getStartQuest(quest.getId());
if(userQuest!=null){
%>
完成[任务]<a href="questE.jsp?id=<%=userQuest.getId()%>"><%=quest.getName()%></a><br/>
<%}}%>
<%for(int i=0;i<talkList.size();i++){
FarmTalkBean link = (FarmTalkBean)talkList.get(i);
if(link.getQuest()!=0 && !farmUser.isQuestStart(link.getQuest())||link.getPreQuest()!=0 && !farmUser.isQuestEnd(link.getPreQuest())||!FarmWorld.isCondition(link.getConditionList(),farmUser)) continue;
%>+<a href="talk.jsp?id=<%=link.getId()%>"><%=FarmWorld.replaceInfo(link.getTitle(),action.getUser())%></a><br/><%}%>

<%}%>
<a href="npc.jsp">返回</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>