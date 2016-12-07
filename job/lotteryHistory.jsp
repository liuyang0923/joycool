<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.bean.job.*"%><%@ page import="net.joycool.wap.action.job.JCLotteryAction"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
//UserStatusBean status = null;
JCLotteryAction action = new JCLotteryAction(request);
action.lotteryHistory(request);
Vector list=(Vector)request.getAttribute("jcLotteryHistoryList");
JCLotteryNumberBean jcLottertNumber=(JCLotteryNumberBean)request.getAttribute("jcLottertNumber");
JCLotteryHistoryBean history=null;
IUserService userService =ServiceFactory.createUserService();
UserBean user=null;
//获取登陆用户信息
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
//status=action.getUserStatus(loginUser.getId());
//UserStatusBean status=UserInfoUtil.getUserStatus(loginUser.getId());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="中奖名单">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%
if(list.size()>0){
	Iterator itr = list.iterator();
	while(itr.hasNext()) {
		//得到每个具体得中奖人
		history = (JCLotteryHistoryBean) itr.next();
	%>
	第<%=history.getGuessId()%>期中奖号码为:<%=history.getGuessNumber()%><br/>
	<%
	user=UserInfoUtil.getUser(history.getUserId());
	if(user==null)continue;
	%>
	<%--<%if(user.getUs2()!=null){%><%=user.getUs2().getHatShow()%><%}%>--%><%=StringUtil.toWml(user.getNickName())%>:中<%=history.getWager()%>乐币<br/>
	<%}
}else if(jcLottertNumber!=null){%>
	第<%=jcLottertNumber.getGuessId()%>期中奖号码<%=jcLottertNumber.getNumber()%>，无用户中奖，奖金<%=jcLottertNumber.getLeftWager()%>将累积到下一期! 
<%}else{%>
	第1期中奖号码还没有产生,请等待开奖.
<%}%>
<br/>
<a href="/job/lottery.jsp">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>