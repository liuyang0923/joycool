<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.job.JCLotteryAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%
response.setHeader("Cache-Control","no-cache");
JCLotteryAction action = new JCLotteryAction(request);
action.lottery(request);
//取得登陆用户信息
UserBean loginUser = (UserBean) request.getSession().getAttribute(Constants.LOGIN_USER_KEY);
//UserStatusBean status=action.getUserStatus(loginUser.getId());
UserStatusBean status = new UserStatusBean();
if (loginUser != null){
	status=UserInfoUtil.getUserStatus(loginUser.getId());	
}
JCLotteryNumberBean jcLotteryNumber=(JCLotteryNumberBean)request.getAttribute("jcLotteryNumber");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐透彩票">
<p align="left">
<%=BaseAction.getTop(request, response)%>
第<%=jcLotteryNumber.getGuessId()%>期乐透彩票<br/>
累计奖金:<%=jcLotteryNumber.getLeftWager()%>乐币<br/>
开奖时间:<%=jcLotteryNumber.getLotteryDate()%><br/>
请输入竞猜号码和下注数额(每注1000,最多10亿,您现有<%=status.getGamePoint()%>乐币):<br/>
号码:(1-100):<br/>
<input type="text" name="number" maxlength="3" format="*N"  title="号码"/><br/>
下注:(1千-10亿):<br/>
<input type="text" name="wager" maxlength="10" format="*N"  value="1000" title="下注"/><br/>
<anchor title="确定">确定
    <go href="/job/lotteryresult.jsp" method="post">
    <postfield name="number" value="$number"/>
    <postfield name="wager" value="$wager"/> 
    <postfield name="guessId" value="<%=jcLotteryNumber.getGuessId()%>"/> 
    </go>
</anchor><br/>
<a href="/job/lotteryHistory.jsp">上期中奖名单</a><br/>
<a href="/job/myLottery.jsp?guessId=<%=jcLotteryNumber.getGuessId()%>">我本期的下注</a><br/>
<a href="/job/lotteryHelp.jsp">规则与帮助</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>