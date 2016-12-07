<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.util.*"%><%
	HttpSession session = request.getSession(false);
	if(session != null && request.isRequestedSessionIdFromCookie()) {
		CookieUtil cu = new CookieUtil(request);
		if(cu.getCookieValue("jsid")==null){
			Cookie cookie = new Cookie("jsid", session.getId());	// joycool auto login cookie
			cookie.setMaxAge(90000000);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p>
<%=BaseAction.getTop(request, response)%>
<img src="/cast/img/logo.gif" alt="logo"/><br/>
【城堡战争大厅】<br/>
<a href="/cast4/s.jsp">城堡3区</a>revo&lt;名人堂&gt;<br/>
<a href="/cast5/s.jsp">城堡4区</a>crazy&lt;名人堂&gt;<br/>
<a href="/cast3/s.jsp">城堡2区</a>speed&lt;<a href="stat/out.jsp?i=2">名人堂</a>&gt;<br/>
<a href="s.jsp">城堡老区</a>&lt;<a href="stat/out.jsp?i=1">名人堂</a>&gt;<br/>
<br/>
<a href="/jcforum/forum.jsp?forumId=5545">+玩家交流区+</a><br/>
<a href="/Column.do?columnId=12721">++游戏目标和流程</a><br/>
<a href="/Column.do?columnId=12720">++城堡各区状态</a><br/>
<a href="/cast4/tool/mh.jsp?i=2">&gt;&gt;城堡3区小号举报</a><br/>
<br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>