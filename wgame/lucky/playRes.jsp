<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.auction.LuckyAction"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.bean.dummy.DummyProductBean"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control","no-cache");
LuckyAction action = new LuckyAction(request);
UserBean loginUser = action.getLoginUser();
if(ForbidUtil.isForbid("game",loginUser.getId())){
	response.sendRedirect("/bottom.jsp");
	return;
}
action.playResult();
if(action.isResult("return")) {
action.innerRedirect("play.jsp", response);
return;
}
String url = ("play.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(action.isResult("success")){ int reward = action.getAttributeInt("reward");%>
<card title="幸运转盘">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(reward == 0){ int rewardMoney = action.getAttributeInt("rewardMoney");%>
您获得了<%=rewardMoney%>乐币的奖励<br/>
<%}else{
DummyProductBean rewardItem = (DummyProductBean)request.getAttribute("rewardItem");
%>
您获得了物品：<br/>
<%=rewardItem.getName()%><br/>
<a href="/user/userBag.jsp">查看行囊</a><br/>
<%}%>
<a href="<%=url%>">返回幸运转盘</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{%>
<card title="幸运转盘"  ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=action.getTip()%>(3秒后返回)<br/>
<a href="<%=url%>">返回幸运转盘</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>