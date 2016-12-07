<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("cid");
	int pos = action.getParameterIntS("pos");
	if(pos!=-1) {
		response.sendRedirect("castle.jsp?pos="+pos);
		return;
	}
	if(action.hasParam("x")){
		response.sendRedirect("castle.jsp?x="+action.getParameterInt("x")+"&y="+action.getParameterInt("y"));
		return;
	}
	response.sendRedirect("castle.jsp?id="+id);
%>