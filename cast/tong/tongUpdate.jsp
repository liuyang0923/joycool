<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	TongAction action = new TongAction(request);
	TongPowerBean myPower = action.getCastleService().getTongPowerByUid(action.getLoginUser().getId());
	
	//是否是高层管理
	if(myPower == null || myPower.getPower() == 0) {
		response.sendRedirect("tong.jsp");
		return;
	}
	
	
	int id = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	
	action.updateTong();
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟资料"><p><%@include file="top.jsp"%>
<%if(tong != null) {%>
<%=request.getAttribute("msg")!=null? request.getAttribute("msg") + "<br/>":""%>
联盟名称:<br/>
<%=StringUtil.toWml(tong.getName())%><br/>
<%if(myPower.isPowerName()) {%>
<input name="n" value="<%=StringUtil.toWml(tong.getName()) %>"/>
<anchor>修改<go href="tongUpdate.jsp?a=1" method="post">
<postfield name="n" value="$n"/>
</go></anchor><br/>
<%} %>
联盟描述:<br/>
<%=StringUtil.toWml(tong.getInfo())%><br/>
<%if(myPower.isPowerIntro()) {%>
<input name="i" value="<%=StringUtil.toWml(tong.getInfo()) %>"/>
<anchor>修改<go href="tongUpdate.jsp?a=2" method="post">
<postfield name="i" value="$i"/>
</go></anchor><br/>
<%}} %>
<a href="tongM.jsp">返回联盟管理</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>