<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="java.util.*,net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.framework.*,net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%!
static CacheService cacheService = CacheService.getInstance();
String[] attackType = {"接近的攻击","接近的支援","发出的攻击","发出的支援"};
%><%
	
	
	CastleAction action = new CastleAction(request);
	int tongId = action.getCastleUser().getTong();
	List list = null;
	int cid = action.getParameterIntS("cid");
	String title = null;
	int pos = -1;
	if(cid != -1) {
		CastleBean castle = CastleUtil.getCastleById(cid);
		if(castle!=null) {
			title = castle.getCastleNameWml();
			pos = CastleUtil.xy2Pos(castle.getX(), castle.getY());
		}
	}
	if(title == null) {
		int pos2 = action.getParameterIntS("pos");
		if(pos2 != -1) {
			pos = pos2;
		}
	}
	if(pos>0){
		if(tongId != 0)
			list = action.getCastleService().getCastleMessageListIndexPos("pos=" + pos + " and tong_id=" + tongId + " order by id desc limit 5");
		else
			list = action.getCastleService().getCastleMessageListIndexPos("pos=" + pos + " and uid=" + action.getCastleUser().getUid() + " order by id desc limit 5");
	}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="信息"><p><%@include file="top.jsp"%>
【<a href="search.jsp?pos=<%=pos%>"><%if(title!=null){%><%=title%><%}else{%>绿洲(<%=CastleUtil.pos2X(pos)%>|<%=CastleUtil.pos2Y(pos)%>)<%}%></a>的战报】<br/><%
if(list!=null&&list.size()!=0){
	for(int i = 0; i < list.size(); i++) {
				CastleMessage message = (CastleMessage)list.get(i);
%><%=DateUtil.converDateToBefore(message.getTime())%>:<%if(!message.hasDetail()){%><%=message.getContent()%><%}else{
%><a href="report.jsp?id=<%=message.getId()%>"><%=message.getContent()%></a><%}%><br/>
<%}}else{%>(暂无)<br/><%}%><a href="search.jsp?pos=<%=pos%>">返回</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>