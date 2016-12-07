<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.spec.*"%><%
response.setHeader("Cache-Control","no-cache");
InviteAction action = new InviteAction(request);
action.invite();
UserBean loginUser = action.getLoginUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="邀请好友来乐酷">
<p align="left">
<%=loginUser.getId()%>.joycool.net<br/>
<%=BaseAction.getBottom(request, response)%>
</p>
</card>
</wml>