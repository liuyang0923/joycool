<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	TongAction tongAction = new TongAction(request);
	
	int id = tongAction.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	
	
	//是否是高层管理
	TongPowerBean myPower = tongAction.getCastleService().getTongPowerByUid(tongAction.getLoginUser().getId());
	if(myPower == null || myPower.getPower() == 0) {
		response.sendRedirect("tong.jsp");
		return;
	}
	
	int uid = tongAction.getParameterInt("uid");
	
	if(uid == 0) {
		response.sendRedirect("tong.jsp");
		return;
	}
	CastleUserBean user2 = CastleUtil.getCastleUser(uid);
	if(user2==null||user2.getTong()!=tong.getId()){	// 保证为自己联盟的成员
		response.sendRedirect("tong.jsp");
		return;
	}
	
	
	if(tong.getUid() == tongAction.getLoginUser().getId() || myPower != null && myPower.isPowerTop()) {
	} else {
		response.sendRedirect("tong.jsp");
		return;
	}
	
	TongPowerBean powerBean = tongAction.getCastleService().getTongPowerByUid(uid);
	
	if(powerBean == null) {
		powerBean = new TongPowerBean();
		powerBean.setUid(uid);
		powerBean.setPowerName("高层");
		powerBean.setPower(0);
		powerBean.setTongId(id);
		tongAction.getCastleService().addTongPower(powerBean);
	}
	
	if(tongAction.hasParam("a")) {
		int a = tongAction.getParameterInt("a");
		if(a == 1) {
			if(powerBean.isPowerTop()){
				if(tong.getUid() == uid) {
					request.setAttribute("msg", "创始人的分配权限不能删除");
				} else {
					powerBean.deletePower(TongPowerBean.POWER_TOP);
				}
				
			} else {
				powerBean.addPower(TongPowerBean.POWER_TOP);
			}
		} else if(a == 2) {
			powerBean.togglePower(3);
		} else if(a == 3) {
			powerBean.togglePower(4);
		} else if(a == 4) {
			if(powerBean.isPowerInvite()){
				powerBean.deletePower(TongPowerBean.POWER_INVITE);
			} else {
				powerBean.addPower(TongPowerBean.POWER_INVITE);
			}
		} else if(a == 5){
			powerBean.togglePower(2);
		} else if(a == 6) {
			powerBean.togglePower(5);
		}
		
		tongAction.getCastleService().updateTongPower(uid, powerBean.getPower());
	}
	
	if(tongAction.hasParam("b")) {
		String name = tongAction.getParameterNoEnter("n");
		if(name!=null){
			if(name.length() > 10) {
				request.setAttribute("msg", "长度不能超过10");
			} else {
				powerBean.setPowerName(name);
				tongAction.getCastleService().updateTongPowerName(uid, powerBean.getPowerName());
			}
		}
	}
	CastleUserBean user = CastleUtil.getCastleUser(uid);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟任命"><p><%@include file="top.jsp"%>
<%=request.getAttribute("msg") !=null?request.getAttribute("msg")+"<br/>":"" %>
在这里你能授予成员在你联盟中的权利和位置<br/>
<%=StringUtil.toWml(powerBean.getPowerName())%>:<%=user.getNameWml()%><br/>
称号:<input name="n" value="" maxlength="10"/>
<anchor>修改<go href="powerAdd.jsp?b=1&amp;uid=<%=uid %>" method="post">
<postfield name="n" value="$n"/>
</go></anchor><br/>
==权限设置==<br/>
<a href="powerAdd.jsp?a=1&amp;uid=<%=uid %>">分配权</a>(<%=powerBean.isPowerTop() ? "有":"无" %>)<br/>
<a href="powerAdd.jsp?a=2&amp;uid=<%=uid %>">信息修改权</a>(<%=powerBean.isPowerIntro() ? "有":"无" %>)<br/>
<a href="powerAdd.jsp?a=3&amp;uid=<%=uid %>">名称修改权</a>(<%=powerBean.isPowerName() ? "有":"无" %>)<br/>
<a href="powerAdd.jsp?a=4&amp;uid=<%=uid %>">邀请权</a>(<%=powerBean.isPowerInvite() ? "有":"无" %>)<br/>
<a href="powerAdd.jsp?a=5&amp;uid=<%=uid %>">开除权</a>(<%=powerBean.isPowerDelete() ? "有":"无" %>)<br/>
<a href="powerAdd.jsp?a=6&amp;uid=<%=uid %>">外交权</a>(<%=powerBean.isPowerDip() ? "有":"无" %>)<br/>
<a href="tongM.jsp">返回联盟管理</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>