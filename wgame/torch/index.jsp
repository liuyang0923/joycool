<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
TorchAction action = new TorchAction(request);
List torches = action.getTorchList();
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="奥运火炬手">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<img src="logo.gif" alt=""/><br/>

<% if(torches == null){%>

还没有开始<br/>

<%}else{%>

=传递中的奥运火炬=<br/>
<% for(int i=0;i<torches.size();i++){
TorchBean torch = (TorchBean)torches.get(i);
if((!torch.isStart()||torch.isOver())&&(loginUser==null||loginUser.getId()!=431)) continue;
UserBean user = UserInfoUtil.getUser(torch.getUserId());
%>

<a href="torch.jsp?t=<%=torch.getId()%>"><%=torch.getName()%></a>
*<%=StringUtil.numberFormat(torch.getPoint())%><br/>
<%if(user!=null){%>(<%=user.getNickNameWml()%>)<br/><%}%>
<%}%>


<%}%>
<br/>
<a href="top.jsp">上期火炬手排行榜</a><br/>
<a href="help.jsp">查看详细说明</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>