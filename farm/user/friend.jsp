<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
FarmWorld world = action.world;
List userFriends = action.getLoginUser().getOnlineFriendList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="好友">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(userFriends!=null)
for(int i=0;i<userFriends.size();i++){
String userIdKey=userFriends.get(i).toString();
FarmUserBean user = world.getFarmUserCache(StringUtil.toInt(userIdKey));
if(user==null) continue;
if(user==null){
%>未知目标<%}else{
MapNodeBean node2 = world.getMapNode(user.getPos());
MapBean map = world.getMap(node2.getMapId());
%><a href="../user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a>
(<%=map.getName()%>)<br/>
<%}%>
<%}%>

<a href="../map.jsp">返回场景</a><br/>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>