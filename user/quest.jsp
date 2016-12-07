<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.cache.util.*,net.joycool.wap.bean.dummy.*,java.util.*,net.joycool.wap.spec.*,net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.action.user.SendAction" %><%
response.setHeader("Cache-Control","no-cache");

NewQuestAction newQuestAction = new NewQuestAction(request);
newQuestAction.quest();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="新手任务">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(newQuestAction.getUserQuest().getQuestStatus() > 0) {
	if(newQuestAction.getNewQuest().getId()!=0) {%>
<%=newQuestAction.getNewQuest().getEndMsg() %><br/>
<%} 
	if(newQuestAction.getNewQuest().getNextList().size() > 0) {
		if(newQuestAction.getNewQuest().getNextList().size() == 1) {
			if(newQuestAction.getNewQuest().getId() == 0) {%>
<%=newQuestAction.getNewQuest().getTitle() %><br/>
<%=newQuestAction.getNewQuest().getStartMsg() %><br/>
任务:<br/>
<%=newQuestAction.getNewQuest().getMission() %><br/>
<%} else { %>
获得奖励:<br/>
<%=newQuestAction.getReward()%>
<a href="quest.jsp?n=a&amp;i=0">继续下一个任务</a><br/>
<%} } else if(newQuestAction.getNewQuest().getNextList().size() == 2) {%>
选择下一个任务<br/><a href="quest.jsp?n=a&amp;i=0"><%=newQuestAction.getQuest(((Integer)(newQuestAction.getNewQuest().getNextList().get(0))).intValue()).getTitle() %></a><br/>
<a href="quest.jsp?n=a&amp;i=1"><%=newQuestAction.getQuest(((Integer)(newQuestAction.getNewQuest().getNextList().get(1))).intValue()).getTitle() %></a><br/>
<%}}} else {%>
<%=newQuestAction.getNewQuest().getTitle() %><br/>
<%=newQuestAction.getNewQuest().getStartMsg() %><br/>
<%=newQuestAction.getNewQuest().getMission() %><br/>
任务奖励:<br/>
<%=newQuestAction.getReward()%>
<%}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>