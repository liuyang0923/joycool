<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.wc.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wc.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="net.joycool.wap.util.*"%><%
response.setHeader("Cache-Control","no-cache");
WorldCupAction action = new WorldCupAction(request);
action.index();
//取得登陆用户信息
//UserBean loginUser = action.loginUser;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status = UserInfoUtil.getUserStatus(loginUser.getId());
Vector questionList = (Vector)request.getAttribute("questionList");
int count, i;
WcQuestionBean question = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="世界杯博彩">
<p align="center">世界杯博彩</p>
<p align="left">
<%=BaseAction.getTop(request, response)%>
您现有<%=status.getGamePoint()%>乐币<br/>
==博彩场次==<br/>
<%
count = questionList.size();
for(i = 0; i < count; i ++){
	question = (WcQuestionBean) questionList.get(i);
%>
<%=(i + 1)%>.<a href="question.jsp?id=<%=question.getId()%>"><%=StringUtil.toWml(question.getTitle())%>(开球时间<%=question.getEndDatetime()%>)</a><br/>
<%
}
%>
==博彩说明==<br/>
猜中结果的用户将按赔率获得相应乐币,如赔率为1：1.5,则下注100的用户将获得150乐币!届时中奖用户将会收到系统通知!<br/>
<a href="http://wap.joycool.net/forum/forumIndex.jsp?id=13">返回世界杯论坛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>