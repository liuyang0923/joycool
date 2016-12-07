<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.set();
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
QuestionSetBean set = (QuestionSetBean)request.getAttribute("set");
boolean my = (loginUser.getId() == set.getUserId());
UserBean user = UserInfoUtil.getUser(set.getUserId());
%>
<%=StringUtil.toWml(set.getName())%><%if(user!=null){%>(<a href="/chat/post.jsp?toUserId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>)<%}%><br/>
<%=StringUtil.toWml(set.getInfo())%>(共<%=set.getQuestionList().size()%>题)<br/>
<%if(!my){%>
<%if(set.isFlagRelease()){%><%if(set.isReply(loginUser.getId())){%>(已完成)<a href="vreply.jsp?sid=<%=set.getId()%>">查看</a><%}else{%><a href="reply.jsp?sid=<%=set.getId()%>">开始答题</a><%}%><%}else{%>(未发布)<%}%><br/>
<%}else{
%>创建于<%=DateUtil.formatDate2(set.getCreateTime())%><br/>
<%
if(!set.isFlagRelease()){
List qList = set.getQuestionList();
for(int i = 0;i < qList.size();i++) {
QuestionBean q = (QuestionBean)qList.get(i);
%>
<%=i+1%>.<a href="setQ.jsp?sid=<%=set.getId() %>&amp;qid=<%=q.getId()%>"><%=StringUtil.toWml(q.getTitle())%></a><br/>
<%}}%>
<%if(!set.isFlagRelease()){%>
<a href="addQ.jsp?sid=<%=set.getId()%>">添加题目</a><br/>
<a href="release.jsp?sid=<%=set.getId()%>">发布本测试集</a><br/>
注意:如果选择发布则无法再次修改和添加.<br/>
<%}%>
<%}%>
<%if(set.isFlagRelease()&&set.getReplyCount()>0){%>
==挑战者(<%=set.getReplyCount()%>)==<br/>
<%
List replyList = action.service.getQuestionReplyList(" set_id=" + set.getId() + " order by id desc limit 5");
for(int i = 0;i < replyList.size();i++){
QuestionReplyBean reply = (QuestionReplyBean)replyList.get(i);
UserBean user2 = UserInfoUtil.getUser(reply.getUserId());
if(user2==null) continue;
%><a href="/chat/post.jsp?toUserId=<%=user2.getId()%>"><%=user2.getNickNameWml()%></a>(<%=QuestionAction.getScoreString(set, reply)%>分)
<%if(my||reply.getUserId()==loginUser.getId()){%><a href="vreply.jsp?id=<%=reply.getId()%>">查看</a><%}%><br/>
<%}%>
<%if(set.getReplyCount()>5){%><a href="setR.jsp?sid=<%=set.getId()%>">更多挑战记录</a><br/><%}%>
<%}%>

<%}%>
<a href="index.jsp">缘分测试</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>