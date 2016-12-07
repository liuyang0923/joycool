<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request,response);
FarmUserBean farmUser = action.getUser();
if(farmUser.getPos()==250){
	action.redirect("/farm/index.jsp");
	return;
}
int id = action.getParameterInt("id");
if((id==49||id==149)&&!farmUser.isInBattle()){
int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
if(hour<20||hour>21){
	action.redirect("/farm/index.jsp");
	return;
}
	int pos = farmUser.getPos();
	int newPos = id==49?4901:14914;
	farmUser.setPos(newPos);
	FarmWorld.nodeMovePlayer(pos, newPos, farmUser, 0);
	action.redirect("/farm/map.jsp");
	return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="战斗">
<p align="left">
<%=BaseAction.getTop(request, response)%>
每天晚8点和9点进行擂台比赛<br/>
如果正处于战斗中,需要先结束战斗才能进入.<br/>
&gt;&gt;<a href="tele.jsp?id=49">进入灵川(混战)擂台</a><br/>
&gt;&gt;<a href="tele.jsp?id=149">进入浔州(混战)擂台</a><br/>
<a href="../help/pvp.jsp">阅读pvp说明</a><br/>
注意:点击以上链接后将直接到达擂台入口处.入口处不允许战斗,所以不用担心安全问题.<br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>