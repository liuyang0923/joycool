<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.JCLotteryAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("Lottery") == null){
	BaseAction.sendRedirect("/job/lottery.jsp", response);
	return;
}
session.removeAttribute("Lottery");
JCLotteryAction action = new JCLotteryAction(request);
action.userCommit(request);
String number=request.getParameter("number");
String guessId=request.getParameter("guessId");
String wager=(String)request.getAttribute("wager");
String result = (String) request.getAttribute("result");
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
String url = ("/job/lottery.jsp");
%>
<card title="乐透彩票" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%><br/>
<%=request.getAttribute("tip")%><br/>
您还有<%=status.getGamePoint()%>乐币<br/>
<a href="/job/lotteryHistory.jsp">上期中奖名单</a><br/>
<a href="/job/lotteryHelp.jsp">规则与帮助</a><br/>
<a href="/job/lottery.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%//下注正常
} else if("success".equals(result)){
String url = ("/job/lottery.jsp");
%>
<card title="乐透彩票" newcontext="true" ontimer="<%=response.encodeURL(url)%>">
<timer value="500"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
下注成功<br/>
下注期号为:第<%=guessId%>期<br/>
你下注的号码为:<%=number%>.<br/>
累计下注为:<%=wager%>乐币.<br/>
您还有<%=status.getGamePoint()%>乐币<br/>
<a href="/job/lotteryHistory.jsp">上期中奖名单</a><br/>
<a href="/job/lotteryHelp.jsp">规则与帮助</a><br/>
<a href="/job/lottery.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>