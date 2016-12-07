<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.fs.*" %><%
response.setHeader("Cache-Control","no-cache");
FSAction action = new FSAction(request);
action.result();
String result =(String)request.getAttribute("result");
String url=("/fs/index.jsp");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<%if(result.equals("failure")){%>
<card title="<%=FSAction.title%>" ontimer="<%=response.encodeURL(url)%>">
<timer value="30"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=request.getAttribute("tip") %><br/>
<a href="/fs/index.jsp">返回城市</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
<%}else if(result.equals("refresh")){
out.clearBuffer();
BaseAction.sendRedirect("/fs/result.jsp",response);
return;
}else{
FSUserBean fsUser = action.getFsUser();%>
<card title="<%=FSAction.title%>">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%=fsUser.getGameResultTip()%><br/>
<%if(fsUser.getGameStatus()==FSUserBean.STATUS_LOSE){%>
您没有完成游戏，请再接再励。<br/>
<%}else{%>
时间很快就过去了，你处理了所有商品准备回老家结婚。<br/>
现金：<%=fsUser.getMoney()%><br/>
存款：<%=fsUser.getSaving()%><br/>
债务：<%=fsUser.getDebt()%><br/>
健康：<%=fsUser.getHealth()%><br/>
名声：<%=fsUser.getHonor()%><br/>
<%}%>
<a href="/fs/top.jsp">查看排行榜</a><br/>
<%@include file="bottom.jsp"%>
</p>
</card>
<%}%>
</wml>