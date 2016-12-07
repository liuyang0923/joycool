<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
	
	CustomAction customAction = new CustomAction(request);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争游戏指南"><p>
详细游戏指南<br/>
游戏的主旨是通过运用各种技巧建设城堡，发展兵力，从而挑战更强的敌人，获得更多的资源。在游戏中充分发挥自己的实力，实现雄霸天下的梦想！！<br/>
<a href="race.jsp">++种族介绍</a><br/>
<a href="soldiers.jsp">++兵种介绍</a><br/>
<a href="builds.jsp">++建筑介绍</a><br/>
<a href="faq.jsp">++常见问题</a><br/>
<a href="faq2.jsp">++城堡进阶说明</a><br/>
<a href="int.jsp?t=1">地图介绍</a>|<a href="int.jsp?t=2">城堡介绍</a><br/>
<a href="int.jsp?t=3">资源介绍</a>|<a href="int.jsp?t=5">攻城介绍</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>