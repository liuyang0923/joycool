<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.action.beginner.BeginnerQuestionAction" %><%@ page import="net.joycool.wap.bean.beginner.BeginnerQuestionBean" %><%
response.setHeader("Cache-Control","no-cache");
session.setAttribute("question","ok");
BeginnerQuestionAction action = new BeginnerQuestionAction(request);
action.question(request);
String result=(String)request.getAttribute("result");
//获取一条新手的题目 
BeginnerQuestionBean beginnerQuestion=(BeginnerQuestionBean)request.getAttribute("beginnerQuestion");
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
String url=("/beginner/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){
url = ("/lswjs/index.jsp");
%>
<card title="新手趣闻问答" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip")%>,3秒后自动跳转答题页面!<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("answerOver")){%>
<card title="新手趣闻问答">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(session.getAttribute("beginnerAnswerResult")!=null){%>
<%=(String)session.getAttribute("beginnerAnswerResult")%><br/>
<%session.removeAttribute("beginnerAnswerResult");
}%>
恭喜你已经完成所有乐酷新手答题。现有乐币<%=status.getGamePoint()%>，你可以：<br/>
<a href="/beginner/index.jsp">返回新手区</a><br/>
<a href="/lswjs/gameIndex.jsp">去趣味游戏专区</a><br/>
<a href="/chat/hall.jsp">去聊天</a><br/>
<a href="/friend/friendCenter.jsp">去交友</a><br/>
<a href="/lswjs/wagerHall.jsp">去赌场</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="新手趣闻问答" onenterbackward="<%=response.encodeURL(url)%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(session.getAttribute("beginnerAnswerResult")!=null){%>
<%=(String)session.getAttribute("beginnerAnswerResult")%><br/>
<%session.removeAttribute("beginnerAnswerResult");
}%>
<%=beginnerQuestion.getName()%><br/>
A:<a href="result.jsp?id=<%=beginnerQuestion.getId()%>&amp;rs=1"><%=beginnerQuestion.getKey1()%></a><br/>
B:<a href="result.jsp?id=<%=beginnerQuestion.getId()%>&amp;rs=2"><%=beginnerQuestion.getKey2()%></a><br/>
<%if(beginnerQuestion.getKey3()!=null && !beginnerQuestion.getKey3().equals("")){%>
C:<a href="result.jsp?id=<%=beginnerQuestion.getId()%>&amp;rs=3"><%=beginnerQuestion.getKey3()%></a><br/>
<%}%>
<%if(beginnerQuestion.getKey4()!=null && !beginnerQuestion.getKey4().equals("")){%>
D:<a href="result.jsp?id=<%=beginnerQuestion.getId()%>&amp;rs=4"><%=beginnerQuestion.getKey4()%></a><br/>
<%}%>
<%if(beginnerQuestion.getKey5()!=null && !beginnerQuestion.getKey5().equals("")){%>
E:<a href="result.jsp?id=<%=beginnerQuestion.getId()%>&amp;rs=5"><%=beginnerQuestion.getKey5()%></a><br/>
<%}%>
<br/>
你现有乐币<%=status.getGamePoint()%><br/>
<a href="index.jsp">返回新手区</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>
