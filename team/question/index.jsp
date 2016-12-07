<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
QuestionAction action = new QuestionAction(request);
action.index();
String url = ("chat.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="缘分测试">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

List setList = action.service.getQuestionSetList("flag&1!=0 order by id desc limit 5");
for(int i = 0;i < setList.size();i++){
QuestionSetBean set = (QuestionSetBean)setList.get(i);
UserBean user = UserInfoUtil.getUser(set.getUserId());
%><%=i+1%>.<a href="set.jsp?sid=<%=set.getId()%>"><%=StringUtil.toWml(set.getName())%></a>
<%if(user!=null){%>(<%=user.getNickNameWml()%>)<%}%><br/>
<%}%>

<%}%>
<a href="all.jsp">查看更多测试</a><br/>
<a href="my.jsp">查看我编写的测试</a><br/>
<a href="create.jsp">创建我的测试</a><br/>
<a href="../index.jsp">返回</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>