<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	TongAction action = new TongAction(request);
	TongPowerBean myPower = action.getCastleService().getTongPowerByUid(action.getLoginUser().getId());
	
	//是否是高层管理
	if(myPower == null || myPower.getPower() == 0) {
		response.sendRedirect("tong.jsp");
		return;
	}
	
	int id = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	if(action.hasParam("uid"))
		action.quitTong();
//	action.deleteTongUser();
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟管理"><p><%@include file="top.jsp"%>
<%if(tong != null) {
int total = tong.getCount();
PagingBean paging = new PagingBean(action, total, 10, "p1");
List list = action.getService().getTongUser(id, paging.getStartIndex(), paging.getCountPerPage());

%>
【<%=StringUtil.toWml(tong.getName())%>】<br/>
<%if(myPower.isPowerName()) {%><a href="tongUpdate.jsp">修改名称</a><br/><%} %>
<%if(myPower.isPowerIntro()) {%><a href="tongUpdate.jsp">修改描述</a><br/><%} %>
<%=request.getAttribute("msg")!=null? request.getAttribute("msg") + "<br/>":""%>
<%if(myPower.isPowerDip()) {%>
<a href="dip2.jsp">外交管理</a><br/>
<%} %>
<%if(myPower.isPowerInvite()) {%>
<%
	int total2 = action.getCastleService().getTongInviteFromTongCount(tong.getId());

	PagingBean paging2 = new PagingBean(action, total2, 5, "p2");
	List list2 = action.getCastleService().getTongInviteFromTong(tong.getId(),paging2.getStartIndex(), paging2.getCountPerPage());
	
	for(int i = 0; i < list2.size(); i++) {
		TongInviteBean bean = (TongInviteBean)list2.get(i);
		CastleUserBean user = CastleUtil.getCastleUser(bean.getToUid());
%>
邀请<%if(user==null){%>[?]<%}else{%><a href="../user.jsp?uid=<%=user.getUid() %>"><%=user.getNameWml()%></a><%}%>加入联盟|<a href="cTong.jsp?a=d&amp;id=<%=bean.getId()%>&amp;pos=<%=request.getParameter("pos") %>">删除</a><br/>
<%}%><%=paging2.shuzifenye("tongM.jsp", false, "|", response)%>
<%} %>
=成员=<br/>
<%
for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
CastleUserBean user = CastleUtil.getCastleUser(iid.intValue());
if(user==null) continue;
%><%=i+1%>.<a href="../user.jsp?uid=<%=user.getUid()%>"><%=user.getNameWml()%></a>
<%if(myPower.isPowerTop()) {%>
/<a href="powerAdd.jsp?uid=<%=user.getUid()%>">任命</a><%} %>
<%if(myPower.isPowerDelete()) {%>
/<a href="tongM.jsp?uid=<%=user.getUid()%>">开除</a><%} %><br/>
<%}%>
<%=paging.shuzifenye("tongM.jsp", false, "|", response)%>
<%} %>
<%if(myPower.isPowerInvite()) {%>
邀请玩家加入联盟：<br/>
X:<input name="x" format="*N"/><br/>
Y:<input name="y" format="*N"/><br/>
<anchor>邀请<go href="cTong.jsp?a=i&amp;pos=<%=request.getParameter("pos") %>">
<postfield name="x" value="$x"/>
<postfield name="y" value="$y"/>
</go></anchor><br/>
<%} %>
<a href="tong.jsp">返回联盟</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>