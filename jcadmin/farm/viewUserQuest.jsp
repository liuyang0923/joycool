<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
FarmNpcWorld nWorld = FarmWorld.getNpcWorld();
String idStr = request.getParameter("id");
int userId = action.getParameterInt("id");
FarmUserBean user=null;
if(userId>0)
user = FarmWorld.one.getFarmUserCache(userId);
if(user!=null){

if(action.hasParam("abort")){		// 放弃任务
int qid = action.getParameterInt("abort");
FarmUserQuestBean userQuest = nWorld.getUserQuest(qid);
if(userQuest!=null&&userQuest.getStatus() == 1){
user.getEndQuests().remove(new Integer(userQuest.getQuestId()));
}}

}
%>
<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body style="scrollbars:none;margin:0px">
<%if(user != null){%>
<%
List quests = new ArrayList(user.getEndQuests().values());
PagingBean paging = new PagingBean(action, quests.size(),20, "p");
for(int i=paging.getStartIndexR();i>paging.getEndIndexR();i--){
Integer iid = (Integer)quests.get(i);
FarmUserQuestBean userQuest = nWorld.getUserQuest(iid);
FarmQuestBean quest = FarmNpcWorld.one.getQuest(userQuest.getQuestId());
%>
<%=i+1%>.<a href="editQuest.jsp?id=<%=quest.getId()%>"><%=quest.getName()%></a>(<%=DateUtil.formatDate2(userQuest.getStartTime())%>)
-<a href="viewUserQuest.jsp?abort=<%=userQuest.getId()%>&amp;id=<%=user.getUserId()%>" onclick="return confirm('确认放弃[任务]<%=quest.getName()%>?')">删除</a><br/>
<%}%>
<%=paging.shuzifenye("viewUserQuest.jsp?id="+userId, true, "|", response, 20)%>
<%}else{%>
该人物不存在<br/>
<%}%>
	</body>
</html>