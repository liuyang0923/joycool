<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.wgame.*"%><%@ page import="net.joycool.wap.bean.wgame.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%
response.setHeader("Cache-Control","no-cache");
TorchAction action = new TorchAction(request);
UserBean loginUser = action.getLoginUser();
int torchId = action.getParameterInt("t");
TorchBean torch = TorchAction.getTorch(torchId);
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="奥运火炬手">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{

UserBean user = UserInfoUtil.getUser(torch.getUserId());
%>

*<%=torch.getName()%>*<br/>
火炬指数:<%=StringUtil.numberFormat(torch.getPoint())%><br/>
<%if(loginUser.getId()==torch.getUserId()||loginUser.getId()==431){%>
<a href="pass.jsp?t=<%=torchId%>">把火炬传递给我的好友</a><br/>
注意:火炬经过越多的玩家,指数越高,同时之前该火炬传递过的玩家的火炬指数也会增加.长时间不传递火炬将取消火炬手资格,传递火炬需要消耗500万乐币.<br/>
<%}%>
<%if(user!=null){%>火炬手:<a href="/user/ViewUserInfo.do?userId=<%=torch.getUserId()%>"><%=user.getNickNameWml()%></a><br/><%}%>
=最近传递的火炬手=<br/>
<%
List users = torch.getUsers();
for(int i=1;i<users.size()&&i<21;i++){
Integer iid = (Integer)users.get(i);
user = UserInfoUtil.getUser(iid.intValue());
%>
<%=i%>.<a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a><br/>
<%}%>


<%}%>
<br/>
<a href="help.jsp">查看详细说明</a><br/>
<a href="index.jsp">返回奥运火炬首页</a><br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>