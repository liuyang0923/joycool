<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.Constants" %><%@ page import="net.joycool.wap.action.pk.PKAction" %><%@ page import="net.joycool.wap.bean.pk.PKNpcBean" %><%@ page import="net.joycool.wap.bean.pk.PKMissionBean" %><%
response.setHeader("Cache-Control","no-cache");
PKAction action = new PKAction(request);
action.mAccept(request);
String result =(String)request.getAttribute("result");
UserBean loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);
String url=("/pk/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=PKAction.cardTitle%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %>(3秒后返回场景)<br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else{
PKNpcBean pkNpc =(PKNpcBean)request.getAttribute("pkNpc");
PKMissionBean pkMission = (PKMissionBean)request.getAttribute("pkMission");
%>
<card title="<%=PKAction.cardTitle%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
任务名称：<%=pkMission.getName()%><br/>
任务描述：<%=pkMission.getDescription()%><br/>
<a href="/pk/mAcptResult.jsp?npcId=<%=pkNpc.getId()%>&amp;mId=<%=pkMission.getId()%>">接受</a>
<a href="/pk/npcMission.jsp?npcId=<%=pkNpc.getId()%>">放弃</a><br/>
<a href="/pk/index.jsp">返回场景</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}%>
</wml>