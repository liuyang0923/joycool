<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.applys();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
TeamBean team = (TeamBean)request.getAttribute("team");

List applys = team.getApplys();
for(int i = 0;i < applys.size();i++){
Integer iid = (Integer)applys.get(i);
UserBean user = UserInfoUtil.getUser(iid.intValue());
%><a href="/user/ViewUserInfo.do?userId=<%=user.getId()%>"><%=user.getNickNameWml()%></a>
-<a href="invite.jsp?id=<%=user.getId()%>">允许</a>|<a href="invite.jsp?d=1&amp;id=<%=user.getId()%>">拒绝</a><br/><%
}%>
<a href="info.jsp">返回</a><br/>

<%}%>
<a href="index.jsp">返回圈子首页</a><br/>
</p>
</card>
</wml>