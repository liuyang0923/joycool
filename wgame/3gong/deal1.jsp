<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.Gong3Action"%><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("playingGong3") == null){
	//response.sendRedirect(("start.jsp"));
	BaseAction.sendRedirect("/wgame/3gong/start.jsp", response);
	return;
}
Gong3Action da = new Gong3Action();
da.deal1(request);
String result = (String) request.getAttribute("result");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%
//出错
if("failure".equals(result)){
%>
<card title="三公">
<p align="left">
<%=BaseAction.getTop(request, response)%>
三公<br/>
-------------------<br/>
<%=request.getAttribute("tip")%><br/>
<br/>
<a href="start.jsp">返回上一级</a><br/>
<a href="../hall.jsp">返回通吃岛</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%
} else {
	String url = ("deal2.jsp");
%>
<card title="三公" ontimer="<%=response.encodeURL(url)%>">
<timer value="50"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
三公<br/>
-------------------<br/>
<img src="img/run.gif" alt="runing..."/>&nbsp;
<img src="img/run1.gif" alt="runing..."/>&nbsp;
<img src="img/run2.gif" alt="runing..."/><br/>
5秒后出结果!<br/>
心急了?<a href="<%=url%>">直接进入</a><br/>
</p>
</card>
<%
}
%>
</wml>