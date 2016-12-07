<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.bean.pgame.*"%><%@ page import="net.joycool.wap.service.factory.*"%><%@ page import="net.joycool.wap.service.infc.IPGameService"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
int gameId = StringUtil.toInt(request.getParameter("id"));
if(gameId == -1){
	return;
}

IPGameService service = ServiceFactory.createPGameService();
PGameBean pgame = service.getPGame("id=" + gameId);
if(pgame == null){
	return;
}

if(session.getAttribute("MID") != null){
	//response.sendRedirect(pgame.getRealFileUrl());
}
else {
	String serviceId = request.getParameter("MISC_ServiceID");
	String mid = request.getParameter("MISC_MID");

	if(serviceId == null || mid == null){
		return;
	}

	UserOrderBean order = service.getUserOrder("mid = '" + mid + "' and service_id = '" + serviceId + "'");
	if(order == null){
		order = new UserOrderBean();
		order.setMid(mid);
		order.setServiceId(serviceId);
		if(!service.addUserOrder(order)){
			return;
		}		
	}
	session.setAttribute("MID", mid);
	//response.sendRedirect(pgame.getRealFileUrl());
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=pgame.getName()%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<a href="<%=pgame.getRealFileUrl()%>">确认下载</a><br/>
<br/>
<a href="PGameList.do?providerId=<%=pgame.getProviderId()%>" title="进入">返回上一级</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>