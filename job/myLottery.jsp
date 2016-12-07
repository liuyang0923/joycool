<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.job.JCLotteryAction"%><%
response.setHeader("Cache-Control","no-cache");
JCLotteryAction action = new JCLotteryAction(request);
action.myLottery(request);
String guessId=request.getParameter("guessId");
Vector list=(Vector)request.getAttribute("jcLotteryGuessList");
JCLotteryGuessBean jcLotteryGuess=null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="下注历史记录">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(list.size()>0){
	Iterator itr = list.iterator();%>
	第<%=guessId%>期您的下注记录:<br/>
	<%while(itr.hasNext()) {
		//得到每个具体得中奖人
		jcLotteryGuess = (JCLotteryGuessBean) itr.next();
	%>
	数字:<%=jcLotteryGuess.getNumber()%>,下注:<%=jcLotteryGuess.getWager()%>乐币<br/>
	<%}
}else{%>
第<%=guessId%>期您还没有下注<br/>
<%}%>
<a href="/job/lottery.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>