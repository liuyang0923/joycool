<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="jc.family.game.vs.*,jc.family.game.fruit.*,jc.family.*,jc.family.game.pvz.*,java.util.*,jc.util.SimpleChatLog2,net.joycool.wap.bean.PagingBean,net.joycool.wap.util.*"%><%!
static int NUMBER_OF_PAGE = 3;%><%
FruitAction action=new FruitAction(request);
%><%@include file="inc.jsp"%><%
String tip = "";
FruitUserBean fub = null;
FruitFamilyBean ffb = null;
HashMap userMap = null;
int uid = action.getParameterInt("uid");
int showId = action.getParameterInt("t");
ffb = vsGame.getFruitFamilyBean(showId);
if (ffb == null){
	// 找不到bean
	response.sendRedirect("end.jsp?t=" + showId);
	return;
} else {
	userMap = vsGame.getUserMap();
	if (userMap == null){
		// 家族内没有任何人进入
		response.sendRedirect("end.jsp?t=" + showId);
		return;
	}
}
if (uid <= 0){
	tip = "用户ID错误.";
} else {
	fub = (FruitUserBean)userMap.get(new Integer(uid));
	if (fub == null){
		tip = "没有此用户的记录.";
	}
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族水果战"><p align="left"><%=BaseAction.getTop(request, response)%>
<%if ("".equals(tip)){
%><%=fub.getNickNameWml()%><br/>
操作数:<%=fub.getOperateCount()%><br/>
扔出水果:<%=fub.getThrowFruitCount()%><br/>
消灭水果:<%=fub.getBeatFruitCount()%><br/>
科技:<%=fub.getUpdTeckCount()%><br/>
<a href="end.jsp?t=<%=showId%>">返回</a><br/>
<%
} else {
%><%=tip%><br/><%	
}%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>