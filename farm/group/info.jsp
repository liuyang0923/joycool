<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="../pageinc.jsp"%><%@ page import="net.joycool.wap.bean.dummy.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
GroupBean group = farmUser.getGroup();
FarmWorld world = action.world;
MapNodeBean node = world.getMapNode(farmUser.getPos());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="队伍成员">
<p align="left">
<%=BaseAction.getTop(request, response)%>

<%if(group != null){
Iterator iter = group.getUserMap().values().iterator();
while(iter.hasNext()){
GroupUserBean gu = (GroupUserBean)iter.next();
FarmUserBean user = world.getFarmUserCache(gu.getUserId());
if(user==null){
%>未知目标<br/><%}else{
BattleStatus bs = user.getCurStat();
MapNodeBean node2 = world.getMapNode(user.getPos());
MapBean map = world.getMap(node2.getMapId());
%><a href="../user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a>(<%=user.hp*100/bs.hp%>%<%=user.sp*100/bs.sp%>%)<%if(user.getTargetList().size()>0){%>战斗中<%}%><br/>
-<%=FarmWorld.getDirectionString(node,node2)%>(<%=map.getName()%>)<br/>
<%}%>
<%}%>
<a href="chat.jsp?o=1">队伍聊天</a>|
<a href="../map.jsp">返回场景</a><br/>
<br/>
<a href="oper.jsp?o=1">我要离开队伍!</a><br/>
<%}else{%>
你还没有加入任何队伍<br/>
<a href="../map.jsp">返回场景</a><br/>
<%}%>
<%@include file="bottom.jsp"%>
</p>
</card>
</wml>