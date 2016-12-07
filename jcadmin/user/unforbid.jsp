<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%>
<%!
static String[] fs = {"chat","mail","forum","home","tong","team","info","game"};
%><%
response.setHeader("Cache-Control","no-cache");
CustomAction action = new CustomAction(request);
int id = action.getParameterInt("id");
int type=action.getParameterInt("type");
if(type>=0&&type<fs.length) {	// 解除封禁
	ForbidUtil.deleteForbid(fs[type],id);
}else if(type==100){
	if(SecurityUtil.getForbidUserIdMap().remove(new Integer(id)) != null)
		SqlUtil.executeUpdate("DELETE FROM blacklist_user WHERE user_id="+id);
}
%>