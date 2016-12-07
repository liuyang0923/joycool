<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="net.joycool.wap.util.SqlUtil"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.game.texas.*"%><%!
static int LOW_MONEY = 500;
%><%
response.setHeader("Cache-Control","no-cache");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="德州扑克">
<p align="left">
德州救济金发放处<br/>
[公告]积分不足<%=LOW_MONEY%>的玩家可以领取救济金1000，但每天最多领取一次，每人总计可以领取3次。<br/>
新玩家请先查看帮助，熟悉规则后去练习桌试手。<br/>
<%
UserBean loginUser = (UserBean) session.getAttribute("loginUser");
TexasUserBean tub = null;
tub = TexasGame.getUserBean(loginUser.getId());

if(request.getParameter("a")!=null&&tub.getMoney()<LOW_MONEY){
long now = System.currentTimeMillis();
if(tub.getPrizeCount()<15&&DateUtil.dayDiff(tub.getMoneyTime(), now)>0){
	SqlUtil.executeUpdate("update texas_user set money_time=now(),prize_count=prize_count+1 where user_id="+tub.getUserId(), 5);
	TexasGame.addUserMoneyDB(tub.getUserId(),1000);
	tub.setMoneyTime(now);
	tub.setPrizeCount(tub.getPrizeCount()+1);
}
}


if(tub!=null){%>现有积分<%=tub.getMoney()%><br/><%
if(tub.getMoney()<LOW_MONEY){
if(request.getParameter("a")==null){
%><a href="sav.jsp?a=1">领取今天的救济金</a><br/><%}else{
%>已经领取了今天的救济金<br/><%}%>
<%}}%><br/>
<a href="index.jsp">返回德州扑克大厅</a><br/>
<%=BaseAction.getBottomShort(request, response)%>
</p>
</card>
</wml>