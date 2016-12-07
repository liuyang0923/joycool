<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.index();
int uid = action.getParameterInt("uid");
UserBean u = UserInfoUtil.getUser(uid);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=u.getNickNameWml()%>的缘分测试<br/>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
int count = SqlUtil.getIntResult("select count(*) from question_set where user_id="+uid+" and flag&1 != 0", 3);
PagingBean paging = new PagingBean(action, count,10, "p");
List setList = action.service.getQuestionSetList("user_id="+uid+" order by id desc limit "+paging.getStartIndex()+",10");
for(int i = 0;i < setList.size();i++){
QuestionSetBean set = (QuestionSetBean)setList.get(i);
UserBean user = UserInfoUtil.getUser(set.getUserId());
%><%=i+1%>.<a href="set.jsp?sid=<%=set.getId()%>"><%=StringUtil.toWml(set.getName())%></a>
<%if(set.isFlagRelease()){%>(<%=set.getReplyCount()%>人)<%}%><br/>
<%}%>
<%=paging.shuzifenye("his.jsp", false, "|", response)%>
<%}%>
<a href="/home/home2.jsp?uid=<%=uid%>">返回<%=u.getGenderText()%>的家园</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>