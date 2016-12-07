<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.team.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
TeamAction action = new TeamAction(request);
action.member();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="乐酷圈子">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<%}else{
UserBean loginUser = action.getLoginUser();
TeamBean team = (TeamBean)request.getAttribute("team");
TeamUserBean tu2 = team.getUser(loginUser.getId());
if(tu2==null){
out.clearBuffer();
response.sendRedirect("index.jsp");
return;
}
List userList = team.getUserList();%>
<%=team.getName()%>(<%=team.getCount()%>人)<br/>
<%
for(int i = 0;i < userList.size();i++){
TeamUserBean tu = (TeamUserBean)userList.get(i);
net.joycool.wap.bean.UserBean user = UserInfoUtil.getUser(tu.getUserId());
if(loginUser.getId()!=tu.getUserId()){
%><a href="chat.jsp?to=<%=tu.getUserId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId(user.getId())%>)
<%if(tu2.getDuty()==10){%>-<a href="kick.jsp?id=<%=tu.getUserId()%>">踢出圈子</a><%}%>
<%}else{%>
<%=user.getNickNameWml()%>
<%}%><br/>
<%}%>
<a href="chat.jsp">聊天</a>|
<a href="info.jsp">返回</a><br/>
<%}%>
</p>
</card>
</wml>