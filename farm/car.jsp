<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
FarmCarBean car = farmUser.getCar();
if(car==null) {
response.sendRedirect(("map.jsp"));
return;
}
action.car();
MapNodeBean node = action.world.getMapNode(car.getPos(farmUser.getCarPos()));
String nodeMark = node.getNoName();
MapBean map = action.world.getMap(node.getMapId());
MapNodeBean[] nodes = node.getLinks();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源" ontimer="<%=response.encodeURL("car.jsp")%>">
<timer value="<%=car.getCooldown()/100%>"/>
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%>
<%=action.getTip()%><br/>
<a href="map.jsp">返回场景</a>
<%}else{%>
按线路行进中…(<%=farmUser.getCarPos()%>)<br/>
<%if(nodes[0]!=null){%>←<%}else{out.print('　');}%>
<%if(nodes[1]!=null){%>↑<%=nodes[1].getName2()%><%}else{out.print('　');}%>
<%if(nodes[2]!=null){%>→<%}else{%><%}%><br/>
<%if(nodes[3]!=null){%>←<%=nodes[3].getNoName()%><%}else{out.print('　');}%>
<%=node.getName()%><%=nodeMark%>
<%if(nodes[5]!=null){%>→<%=nodes[5].getName2()%><%}else{%><%}%><br/>
<%if(nodes[6]!=null){%>←<%}else{out.print('　');}%>
<%if(nodes[7]!=null){%>↓<%=nodes[7].getName2()%><%}else{out.print('　');}%>
<%if(nodes[8]!=null){%>→<%}else{%><%}%><br/>

<a href="car.jsp">刷新</a>|
<%if(!car.isFlagNoStop()){%><a href="car.jsp?a=1">中途离开</a>|<%}%>
<%=map.getName()%><br/>
<% List objList = node.getObjs();

for(int i = 0;i < objList.size();i++){
Object obj = objList.get(i);%>

<%if(obj instanceof FarmNpcBean){
FarmNpcBean npc = (FarmNpcBean)obj;
%>
<%=farmUser.getNpcMark(npc)%>N<%=npc.getName()%>|

<%}else if(false&&obj instanceof MapDataBean){
MapDataBean data = (MapDataBean)obj;
if(!data.isVisible()) continue;
%>
<%=data.getLink(response)%>|

<%}%>
<%}%>
<% objList = node.getPlayers();
for(int i = 0;i < objList.size();i++){
Object obj = objList.get(i);%>

<%if(obj instanceof FarmUserBean){
FarmUserBean user = (FarmUserBean)obj;
if(farmUser.equals(user)||user.getName().length()==0) continue;
%><%if(user.isDead()){%>X<%}%>
<%=user.getNameWml()%>|

<%}%>
<%}%>
<%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>