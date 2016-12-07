<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.userIsDeath(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/sceneList.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后跳转场景列表)<br/>
<a href="/pk/sceneList.jsp">场景列表</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
String flag = (String)request.getAttribute("flag");
%>
<card title="<%=PKAction.cardTitle%>" >
<p align="left">
<%=BaseAction.getTop(request, response)%>
游戏中您已死亡！<br/>
<%if(flag.equals("false")){%>
<a href="/pk/pkRevival.jsp">自动复生</a><br/>
<%}else{%>
<a href="/pk/pkRevival.jsp">选择复生</a>(将花费<%=PKAction.PK_REVIVAL%>乐币)<br/>
<%}%>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>