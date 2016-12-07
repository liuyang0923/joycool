<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!

%><%
	
	CastleAction action = new CastleAction(request);
	action.quest();
	CastleUserBean castleUser = action.getCastleUser();
	int quest = castleUser.getQuest();
	if(quest >= ResNeed.getQuestCount()) {
		response.sendRedirect("base.jsp");
		return;
	}
	CastleQuestBean q = ResNeed.getQuest(quest);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="新手任务"><p>
<a href="base.jsp">基础</a>|<a href="ad.jsp">建筑</a>|<a href="around.jsp">地图</a>|<a href="amsg.jsp">信息<%if(castleUser.getUnread()>0){%><%=castleUser.getUnread()%><%}%></a><br/>
<%if(castleUser.getQuestStatus()==0){

%>【任务<%=quest%>】<%=q.getTitle()%><br/>
<%if(action.isResult("tip")){%><%=action.getTip()%><%}else{%>“<%=q.getStartMsg()%>”<%}%><br/>
[命令]<br/>
<%=q.getMission()%><br/>
<%switch(quest) {
case 4:{
%>
<input format="*N" name="crank"/>
<anchor title="确定">完成任务<go href="quest.jsp">
<postfield name="r" value="$crank"/>
</go></anchor><br/>
<%} break;
case 6:{
%>
<input format="*N" name="casq"/>
<anchor title="确定">完成任务<go href="quest.jsp">
<postfield name="q" value="$casq"/>
</go></anchor><br/>
<%} break;
case 7:{
%>
(X<input format="*N" name="casx"/>|Y<input format="*N" name="casy"/>)
<anchor title="确定">完成任务<go href="quest.jsp">
<postfield name="x" value="$casx"/>
<postfield name="y" value="$casy"/>
</go></anchor><br/>
<%}break;
case 8:{
%>
<anchor title="确定">送出粮食<go href="quest.jsp">
<postfield name="c" value="1"/>
</go></anchor><br/>
<%} break;
case 16:{
%>
<input format="*N" name="cwood"/>
<anchor title="确定">完成任务<go href="quest.jsp">
<postfield name="r" value="$cwood"/>
</go></anchor><br/>
<%} break;
case 18:{
%>
<input format="*N" name="crank"/>
<anchor title="确定">完成任务<go href="quest.jsp">
<postfield name="r" value="$crank"/>
</go></anchor><br/>
<%} break;
}%>
<%}else{%><%if(quest>0){%>【任务<%=quest%>】<%=q.getTitle()%>(完成)<br/>
“<%if(action.isResult("tip")){%><%=action.getTip()%><%}else{%><%=q.getEndMsg()%><%}%>”<br/>
+你的奖励+<br/>
<%if(q.getWood()>0){%>木<%=q.getWood()%>|石<%=q.getStone()%>|铁<%=q.getFe()%>|粮<%=q.getGrain()%><br/><%}%>
<%if(q.getRewardMsg()!=null&&q.getRewardMsg().length()>0){%><%=q.getRewardMsg()%><br/><%}%>
<%if(q.getGold()>0){%><%=q.getGold()%>个金币<br/><%}%>
<%if(q.getSp()>0){%><%=q.getSp()%>天SP帐号<br/><%}%>
<%}else{%><%=q.getTitle()%><br/>“<%=q.getEndMsg()%>”<br/><%}%>
<a href="quest.jsp?next=1"><%if(quest+1 >= ResNeed.getQuestCount()){%>完成新手任务<%}else if(quest==0){%>开始第一个任务！<%}else{%>继续下一个任务<%}%></a><br/>

<%}%><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>