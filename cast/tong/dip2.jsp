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
	
	if(action.hasParam("a")){
		int a = action.getParameterInt("a");
		
		if(a == 1) {
			action.addAgreeApply();
		} else if(a == 2) {
			action.acceptAgreeApply();
		} else if(a == 3) {
			action.deleteAgreeApply();
		} else if(a == 4){
			action.deleteAgree();
		}
		
	}
	
	List list = action.getCastleService().getTongAgreeApplyList("tong_id = " + id);
	
	List list2 = action.getCastleService().getTongAgreeApplyList("tong_id2 = " + id);
	
	List list3 = action.getCastleService().getTongAgreeList("tong_id = " + id);
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟资料"><p><%@include file="top.jsp"%>
<%if(tong != null) {%>
<%=action.getTip()!=TongAction.defaultTip? action.getTip() + "<br/>":""%>
<%=request.getAttribute("msg")!=null? request.getAttribute("msg") + "<br/>":""%>
<%}%>
<%if(myPower.isPowerDip()) {%>
外交:<br/>
X:<input name="x" format="*N"/><br/>
Y:<input name="y" format="*N"/><br/>
<select name="t">
<option value="1">联盟</option>
<option value="2">不侵略</option>
<option value="3">宣战</option>
</select>
<anchor>申请<go href="dip2.jsp?a=1">
<postfield name="x" value="$x"/>
<postfield name="y" value="$y"/>
<postfield name="t" value="$t"/>
</go></anchor><br/>
<%if(list.size() > 0) {%>
=发出的外交申请=<br/>
<%for(int i = 0; i < list.size(); i ++) {
	TongAgreeBean bean = (TongAgreeBean)list.get(i);
	if(bean.getType()==3) continue;	// 宣战的不显示
%>
<a href="tong.jsp?id=<%=bean.getTongId2()%>"><%=StringUtil.toWml(CastleUtil.getTong(bean.getTongId2()).getName())%></a>-<%=bean.getTypeName() %><a href="dip2.jsp?a=3&amp;id=<%=bean.getId() %>">删除</a><br/>
<%}} %>
<%if(list2.size() > 0) {%>
=收到的外交申请=<br/>
<%for(int i = 0; i < list2.size(); i ++) {
	TongAgreeBean bean = (TongAgreeBean)list2.get(i);
%>
<a href="tong.jsp?id=<%=bean.getTongId()%>"><%=StringUtil.toWml(CastleUtil.getTong(bean.getTongId()).getName())%></a>-<%=bean.getTypeName() %><a href="dip2.jsp?a=2&amp;id=<%=bean.getId() %>">接受</a>|<a href="dip2.jsp?a=3&amp;id=<%=bean.getId() %>">删除</a><br/>
<%} }%>
<%if(list3.size() > 0) {%>
=已建立外交=<br/>
<%for(int i = 0; i < list3.size(); i ++) {
	TongAgreeBean bean = (TongAgreeBean)list3.get(i);
%>
<a href="tong.jsp?id=<%=bean.getTongId2()%>"><%=StringUtil.toWml(CastleUtil.getTong(bean.getTongId2()).getName())%></a>-<%=bean.getTypeName() %><a href="dip2.jsp?a=4&amp;id=<%=bean.getId() %>">解除</a><br/>
<%} }}else { %>
您没有外交权限<br/>
<%} %>
<a href="tongM.jsp">返回联盟管理</a><br/><a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>