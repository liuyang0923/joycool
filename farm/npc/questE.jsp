<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
NpcAction action = new NpcAction(request);
action.questEnd();
FarmUserQuestBean userQuest = (FarmUserQuestBean)request.getAttribute("userQuest");
FarmUserBean farmUser = action.getUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="quests.jsp">返回</a><br/>
<%}else{
FarmQuestBean quest = action.world.getQuest(userQuest.getQuestId());

if(userQuest.isFinished()){%>

完成[任务]<%=quest.getName()%><br/>
<%=FarmWorld.replaceInfo(quest.getEndInfo(),farmUser)%><br/>
获得了:<%=FarmWorld.getItemListString(quest.getProductList())%><%=FarmWorld.getPrizeString(quest.getPrizeList())%><br/>
<%if(quest.getNext()==0){%>
<a href="quests.jsp">确定</a><br/>
<%}else{%>
<a href="questB.jsp?id=<%=quest.getNext()%>">确定</a><br/>
<%}%>


<%}else{%>
[任务]<%=quest.getName()%><br/>
<%=FarmWorld.replaceInfo(quest.getRequestInfo(),farmUser)%><br/>
<a href="../user/quest.jsp?id=<%=userQuest.getId()%>">查看任务</a><br/>
<a href="questE.jsp?a=2&amp;id=<%=userQuest.getId()%>">放弃任务</a><br/>
<a href="quests.jsp">返回</a><br/>
<%}%>

<%}%>
<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>