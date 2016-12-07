<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.dhh.*" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=DhhAction.title%>" >
<timer value="30"/> 
<p align="left">
<%=BaseAction.getTop(request, response)%>
确认要重新开始游戏吗？<br/>
<a href="play.jsp?sceneId=-2">是的，重新开始</a><br/>
<a href="play.jsp">点错了，继续刚才的</a><br/><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>