<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
int userId = action.getParameterInt("id");
int pos = action.getParameterInt("pos");
int bindPos = action.getParameterInt("bindPos");
FarmUserBean user=null;
if(userId>0){
user = FarmWorld.one.getFarmUserCache(userId);
if(user!=null){
FarmWorld.nodeMovePlayer(user.getPos(),pos,user,0);
FarmWorld.updateUserPos(user,pos);
user.setPos(bindPos);
FarmNpcWorld.updateUserBindPos(user);
user.setPos(pos);
}
response.sendRedirect("viewUser.jsp?id=" + userId);
return;
}
%>
