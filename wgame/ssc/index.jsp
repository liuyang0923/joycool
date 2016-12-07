<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.ssc.*" %><%@ page import="net.joycool.wap.util.*,java.util.List" %><%@ page import="net.joycool.wap.bean.UserBean" %><%
response.setHeader("Cache-Control","no-cache");
LhcAction action = new LhcAction(request);
synchronized(LhcWorld.class){
	LhcWorld.task();
}
LhcResultBean lhcResult =LhcWorld.bean;
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
List lhcWagerRecordList = action.getLhcService().getLhcWagerRecordList("mark=1 and prize>=100000000 order by id desc limit 5");	// 最新的5条中奖记录
LhcWagerRecordBean lhcWagerRecord = null;
UserBean user = null;
//if(loginUser==null){
//response.sendRedirect("/lswjs/index.jsp");
//return;
//}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="虚拟六时彩" ontimer="<%=response.encodeURL("index.jsp")%>"><timer value="300"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%-- <%=loginUser.showImg("logo.gif")%> --%>
<img src="logo.gif" alt="虚拟六时彩"/><br/>
<a href="help.jsp">虚拟六时彩说明</a><br/>
<%if(lhcResult==null){%>
第一期等待开奖中<br/>
<%}else{%>
==今天第<%=lhcResult.getTerm()%>期开奖==<br/>
<%=lhcResult.getNum1()%>,<%=lhcResult.getNum2()%>,<%=lhcResult.getNum3()%>,<%=lhcResult.getNum4()%>,<%=lhcResult.getNum5()%>,<%=lhcResult.getNum6()%>(<%=lhcResult.getNum7()%>)<br/>
<%=action.countName(lhcResult.getId())%><br/>
=============<br/>
<%
	int left = LhcWorld.getNextTimeLeft();
	if(left != -1){
		if(left >= 60){
			%>投注时间还剩<%=DateUtil.formatTimeInterval(left - 60)%><br/><%
		}
%>
离本期开奖时间还剩<%=DateUtil.formatTimeInterval(left)%><br/>
	<%}%>

<%
	//新增，时间判断。
	if (left != -1 && left >= 60){
%>
<a href="buy.jsp?type=8">平码</a>|<a href="buy.jsp?type=9">特码</a>|<a href="buy.jsp?type=1">大小</a>|<a href="buy.jsp?type=2">生肖</a>|<a href="buy.jsp?type=4">前后</a><br/>
<a href="buy.jsp?type=5">波色</a>|<a href="buy.jsp?type=6">五行</a>|<a href="buy.jsp?type=7">单双</a>|<a href="buy.jsp?type=3">家禽野兽</a><br/><br/>
<%}else{
	%>请等待下一期投注开始<br/><%
}%>

<%}%>
<a href="buyList.jsp">兑奖记录</a><br/>
<a href="lhcHistory.jsp">每期开奖结果</a><br/>
==最新中奖==<a href="more.jsp">更多</a><br/>
<%if (lhcWagerRecordList != null && lhcWagerRecordList.size() > 0){
	for (int i = 0 ; i < lhcWagerRecordList.size() ; i++){
		lhcWagerRecord = (LhcWagerRecordBean)lhcWagerRecordList.get(i);
		user = UserInfoUtil.getUser(lhcWagerRecord.getUserId());
		if (user != null){
			%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>中<%
			if(lhcWagerRecord.getType()<=7){
				%><%=LhcWorld.LHC_NAME_ARRAY[lhcWagerRecord.getType()][lhcWagerRecord.getNum()]%><%
			}else if(lhcWagerRecord.getType()==8){
				%>平码<%
			}else if(lhcWagerRecord.getType()==9){
				%>特码<%
			}%><%=lhcWagerRecord.getPrize() / 100000000%>亿<br/><%
		}
	}
}%>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>