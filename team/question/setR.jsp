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
<%=StringUtil.toWml(set.getName())%><%if(user!=null){%>(<%=user.getNickNameWml()%>)<%}%><br/>
创建时间:<%=DateUtil.formatDate2(set.getCreateTime())%><br/>
==挑战记录(<%=set.getReplyCount()%>)==<br/>
<%
PagingBean paging = new PagingBean(action, set.getReplyCount(), 10, "p");
List replyList = action.service.getQuestionReplyList(" set_id=" + set.getId() + " order by id desc limit " + paging.getStartIndex() + ",10");
for(int i = 0;i < replyList.size();i++){
QuestionReplyBean reply = (QuestionReplyBean)replyList.get(i);
UserBean user2 = UserInfoUtil.getUser(reply.getUserId());
%><%=user2.getNickNameWml()%>(<%=QuestionAction.getScoreString(set, reply)%>分)
<%if(my||reply.getUserId()==loginUser.getId()){%><a href="vreply.jsp?id=<%=reply.getId()%>">查看</a><%}%><br/>
<%}%>
<%=paging.shuzifenye("setR.jsp?sid="+set.getId(),true,"|",response)%>
<a href="set.jsp?sid=<%=set.getId()%>">返回</a><br/>

<%}%>

<a href="index.jsp">返回缘分测试</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>