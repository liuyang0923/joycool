<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	
	
	BuildingAction action = new BuildingAction(request);
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="取消建造"><p><%
if(action.hasParam("c")){
boolean cancelled = action.cancel();
%><%@include file="top.jsp"%><%=request.getAttribute("msg")%><br/><%
}else{
int id = action.getParameterInt("id");
BuildingThreadBean bean = cacheService.getCacheBuildBean(id);
if(bean!=null&&bean.getCid()==action.getCastle().getId()){
%>真的要取消[<%=ResNeed.getBuildingT(bean.getType()).getName()%>]等级<%=bean.getGrade()%>的建造/升级吗?<br/>
<a href="cancel.jsp?c=1&amp;id=<%=id%>">确认取消</a><br/><%}}%>
<a href="amsg.jsp">返回信息</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>