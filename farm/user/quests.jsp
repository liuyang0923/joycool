<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
action.quests();
FarmNpcWorld nWorld = action.nWorld;
FarmUserBean farmUser = action.getUser();
List quests = farmUser.getQuests();
Map endQuests = farmUser.getEndQuests();
PagingBean paging = new PagingBean(action, endQuests.size(), 5, "p");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%
if(paging.getCurrentPageIndex()==0){ %>
==现有任务(<%=quests.size()%>)==<br/>
<%
for(int i=0;i<quests.size();i++){
FarmUserQuestBean userQuest = (FarmUserQuestBean)quests.get(i);
FarmQuestBean quest = action.nWorld.getQuest(userQuest.getQuestId());
%>
<a href="quest.jsp?id=<%=userQuest.getId()%>"><%=quest.getName()%></a><br/>
<%} }%>
<%if(farmUser.getQuestCreatureFinish().size()>0){%>
==任务猎杀状态==<br/>
<%
	Iterator iter = farmUser.getQuestCreatureFinish().iterator();
	while(iter.hasNext()) {
		int[] is = (int[])iter.next();
		CreatureTBean cr = nWorld.getCreatureT(is[1]);
%>
<%=cr.getName()%>-已猎杀<%=is[2]%><br/>
<%}%>
<%}%>
==已完成任务(<%=endQuests.size()%>)==<br/>
<%
List endQuestsOrder = new ArrayList(endQuests.values());

int c = 0;
for(int i=endQuestsOrder.size()-1;i>=0&&c < paging.getEndIndex();i--){
if(c >= paging.getStartIndex()){
Integer iid2 = (Integer)endQuestsOrder.get(i);
FarmUserQuestBean userQuest = nWorld.getUserQuest(iid2);
FarmQuestBean quest = nWorld.getQuest(userQuest.getQuestId());
%>
<%=i+1%>.<%=quest.getName()%>(<%=DateUtil.formatDate2(userQuest.getDoneTime())%>)<br/>
<%}
c++;
}%>
<%if(endQuests.size()>5){%>
<%if(paging.getCurrentPageIndex()==0&&endQuests.size()>10){%>
<a href="quests.jsp?p=1">更多已经完成任务</a><br/>
<%}else{%>
<%=paging.shuzifenye("quests.jsp", false, "|", response)%>
<%}%>
<%}%>
<a href="../map.jsp">返回场景</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>