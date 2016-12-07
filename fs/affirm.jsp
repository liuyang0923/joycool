<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.action.fs.FSAction" %><%@ page import="net.joycool.wap.framework.BaseAction" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="<%=FSAction.title%>" >
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
确认要重新开始游戏吗？<br/>
<a href="/fs/start.jsp">是的，重新开始</a><br/>
<a href="/fs/index.jsp">点错了，继续刚才的</a><br/><br/>
<a href="/fs/help.jsp">返回浮生记首页</a><br/>
<a href="/lswjs/gameIndex.jsp">返回休闲娱乐城</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>