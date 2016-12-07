<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.util.db.*,net.joycool.wap.framework.*"%><%
	
	CastleAction action = new CastleAction(request);
	String u = request.getParameter("a");
	if(u != null && u.equals("u")) {
		String name = action.getParameterNoEnter("name");
		if(name == null || name.length() == 0) {
			request.setAttribute("msg", "不能为空");
		} else if(StringUtil.getCLength(name) > 10) {
			request.setAttribute("msg","不能大于5个字");
		} else {
			CacheService cacheService = CacheService.getInstance();
			if(cacheService.updateCityName(action.getCastle().getId(), name)) {
				request.setAttribute("msg","更改成功");
			} else {
				request.setAttribute("msg","更改失败");
			}
		}
	}
	CastleBean myCastleBean = action.getCastle();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="修改城市名字"><p><%@include file="top.jsp"%>
<%=StringUtil.toWml(myCastleBean.getCastleName())%>(<%=myCastleBean.getX()%>|<%=myCastleBean.getY()%>)<br/>
城主:<%=castleUser.getNameWml()%><br/>
<%if(request.getAttribute("msg") != null) {%><%=request.getAttribute("msg") %><br/><%} %>
修改城堡名字：<br/><input name="name" value=""/><br/>
<anchor>修改<go href="u.jsp?a=u" method="post">
<postfield name="name" value="$name"/></go></anchor>
<br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>