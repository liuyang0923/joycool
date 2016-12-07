<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,jc.family.game.pvz.*,java.util.*,jc.util.SimpleChatLog2,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%!
static int NUMBER_OF_PAGE = 10;%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
List attackList = action.getFruitHitsModeList(NUMBER_OF_PAGE,"info.jsp");
String pageStr = (String)request.getAttribute("fruitHitsModeList");
FruitTask task = null;
int tmp = 0;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
果园->目标|数量|时间<br/>
<%if (attackList != null || attackList.size() > 0) {
for (int i = 0 ; i < attackList.size() ; i++){
	tmp = StringUtil.toInt(attackList.get(i).toString());
	task = (FruitTask) vsGame.getFruitHitsMode().get(new Integer(tmp));
	if (task != null){
		%><%=task.getO1().getOrchardName()%><%=FruitAction.symbols[task.getO1().getSide()]%>-&gt;<%=FruitAction.symbols[task.getO2().getSide()]%><%=task.getO2().getOrchardName()%>,<%=task.getFruitCount()%>,<%=((task.getStartTime() + task.getArrivalTime() * 1000) - System.currentTimeMillis()) / 1000%>秒<br/><%
	}
}%><%=pageStr!=null?pageStr:""%><%
} else {
	%>暂无战况.<br/><%
}
%>
<a href="game.jsp">返回水果战</a>
<br/><%=BaseAction.getBottomShort(request,response)%></p></card></wml>