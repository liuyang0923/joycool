<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.spec.farm.SimpleChatLog"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,java.util.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	int id = action.getParameterInt("id");
	if(id == 0)
		id = action.getCastleUser().getTong();
	TongBean tong = CastleUtil.getTong(id);
	
	TongAction tongAction = new TongAction(request);
	
	
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="联盟资料"><p><%@include file="top.jsp"%>
<%if(tong != null) {
List list = tongAction.getCastleService().getTongPower(id);
%>
<%=request.getAttribute("msg")!=null? request.getAttribute("msg") + "<br/>":""%>
【联盟高层】<br/>
<%
for(int i=0;i<list.size();i++){
	TongPowerBean bean = (TongPowerBean)list.get(i);
Integer iid = new Integer(bean.getUid());
CastleUserBean user = CastleUtil.getCastleUser(iid.intValue());
if(user==null) continue;
%><%=StringUtil.toWml(bean.getPowerName()) %>:<a href="../user.jsp?uid=<%=user.getUid()%>"><%=user.getNameWml()%></a><br/>
<%
}} %><a href="tong.jsp">返回联盟</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>