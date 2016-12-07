<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%
	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="地图说明"><p>
□.未探索的区域,可能是荒漠也可能是绿洲<br/>
○.未被占领的荒漠,可以在这里建立新的城堡<br/>
●.这个位置已经建有城堡<br/>
☆.这个位置已经建有城堡,该城堡的城主和你在同一个联盟<br/>
★.这个位置已经建有城堡,你就是该城堡的城主<br/>
△.未被占领的绿洲<br/>
▲.已经被占领的绿洲<br/>
<br/>
<a href="faq2.jsp">返回进阶说明</a><br/>
<a href="../s.jsp">返回城堡战争首页</a><br/>
<%=BaseAction.getBottomShort(request, response)%></p></card></wml>