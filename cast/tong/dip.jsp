<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog,net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	TongAction action = new TongAction(request);
	int id = action.getParameterInt("id");
	if(id == 0)
		id = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	
	List list3 = action.getCastleService().getTongAgreeList("tong_id = " + id);
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟外交"><p><%@include file="top.jsp"%>
==外交协议列表==<br/>
<%if(list3.size()==0){%>(暂无)<br/>
<%}else{ for(int i = 0; i < list3.size(); i ++) {
	TongAgreeBean bean = (TongAgreeBean)list3.get(i);
%>
<%=bean.getTypeName() %>-<a href="tong.jsp?id=<%=bean.getTongId2()%>"><%=StringUtil.toWml(CastleUtil.getTong(bean.getTongId2()).getName())%></a><br/>
<%}}%>
<a href="tong.jsp?id=<%=id%>">返回联盟</a><br/><a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>