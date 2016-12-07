<%@ page contentType="text/vnd.wap.wml;charset=utf-8"%><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.spec.farm.*"%><%@ page import="net.joycool.wap.spec.farm.bean.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%
response.setHeader("Cache-Control","no-cache");
FarmAction action = new FarmAction(request);
FarmUserBean farmUser = action.getUser();
if(farmUser.getCar()!=null) {
response.sendRedirect(("car.jsp"));
return;
}
action.map();%><%@include file="inc/trigger.jsp"%><%
MapNodeBean node = (MapNodeBean)request.getAttribute("node");
String nodeMark = node.getNoName();
int mapId = node.getMapId();
MapBean map = FarmAction.world.getMap(mapId);
if(map.getParent()!=0)
	mapId = map.getParent();
MapNodeBean[] nodes = node.getLinks();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml>
<card title="桃花源">
<p align="left">
<%=BaseAction.getTop(request, response)%>
<%if(action.isResult("tip")){%><%=action.getTip()%><br/><%}%>
<%if(farmUser.getInviteTongUser() > 0){
FarmUserBean user = action.world.getFarmUserCache(farmUser.getInviteTongUser());
if(user!=null&&user.getTongUser()!=null){
TongBean tong = FarmTongWorld.getTong(user.getTongUser().getTongId());
%>
<%=user.getNameWml()%>邀请你加入[<a href="tong/tong.jsp?id=<%=tong.getId()%>"><%=tong.getNameWml()%></a>]:<a href="tong/join.jsp">加入</a>|<a href="tong/join.jsp?r=1">拒绝</a><br/>
<%}}%>
<%if(farmUser.getGroup()==null){
GroupUserBean gu = FarmWorld.getGroupUser(farmUser.getUserId());
if(gu!=null){
FarmUserBean uh = action.world.getFarmUserCache(gu.getGroupId());
%>
<%if(uh==null){%>未知目标<%}else{%><%=uh.getNameWml()%><%}%>邀请你加入队伍<a href="group/join.jsp">接受</a>|<a href="group/join.jsp?r=1">拒绝</a><br/>
<%}}%>
<%if(farmUser.isAlive()){%>
<%if(farmUser.getTargetList().size()>0){%>
<a href="cb/cb.jsp">继续战斗</a><br/>
注意:结束战斗前无法切换场景<br/>
<%}else{%>
<%if(nodes[0]!=null){%><a href="map.jsp?d=0&amp;n=<%=node.getId()%>">←</a><%}else{out.print('　');}%>
<%if(nodes[1]!=null){%><a href="map.jsp?d=1&amp;n=<%=node.getId()%>">↑<%=nodes[1].getName2()%></a><%}else{out.print('　');}%>
<%if(nodes[2]!=null){%><a href="map.jsp?d=2&amp;n=<%=node.getId()%>">→</a><%}else{%><%}%><br/>
<%if(nodes[3]!=null){%><a href="map.jsp?d=3&amp;n=<%=node.getId()%>">←<%=nodes[3].getNoName()%></a><%}else{out.print('　');}%>
<%=node.getName()%><%=nodeMark%>
<%if(nodes[5]!=null){%><a href="map.jsp?d=5&amp;n=<%=node.getId()%>">→<%=nodes[5].getName2()%></a><%}else{%><%}%><br/>
<%if(nodes[6]!=null){%><a href="map.jsp?d=6&amp;n=<%=node.getId()%>">←</a><%}else{out.print('　');}%>
<%if(nodes[7]!=null){%><a href="map.jsp?d=7&amp;n=<%=node.getId()%>">↓<%=nodes[7].getName2()%></a><%}else{out.print('　');}%>
<%if(nodes[8]!=null){%><a href="map.jsp?d=8&amp;n=<%=node.getId()%>">→</a><%}else{%><%}%><br/>
<%}%>
<%}else{%>
<a href="cb/dead.jsp?id=<%=farmUser.getUserId()%>">人物已经死亡!!!</a><br/>
<%}%>
<a href="cb/cb.jsp">休息</a>|
<a href="user/items.jsp">物品</a>|

<%GroupBean group = farmUser.getGroup();
TongUserBean tongUser = farmUser.getTongUser();

if(group!=null){
int count = group.getUnreadTotal(farmUser.getUserId());%>
<a href="group/info.jsp">组</a><%=group.getUserCount()%><a href="group/chat.jsp">聊</a><%=count%>
<%if(tongUser==null){%>|<%}%><%}%>

<%
if(tongUser!=null){
SimpleChatLog sc = SimpleChatLog.getChatLog(tongUser.getTongId());
int count = sc.getUnreadTotal(action.getAttribute2("tongchat"));%>
<a href="tong/chat.jsp">门</a><%=count%>|<%}%>


<a href="map.jsp">刷新</a>|
<%if(node.getDropCount()>0){%><a href="drops.jsp">掉落</a>|<%}

SimpleChatLog sc2 = SimpleChatLog.getChatLog("fm"+mapId);

%>
<a href="mapD.jsp"><%=map.getName()%></a><a href="chat.jsp"><%=sc2.getUnreadTotal(action.getAttribute2("mapchat" + mapId))%></a><br/>
<%=node.getInfo()%><br/>
<% List objList = node.getObjs();

for(int i = 0;i < objList.size();i++){
Object obj = objList.get(i);%>

<%if(obj instanceof FarmNpcBean){
FarmNpcBean npc = (FarmNpcBean)obj;
if(npc.isFlagHide()) continue;
%>
<%=farmUser.getNpcMark(npc)%>N<a href="npc/npc.jsp?id=<%=npc.getId()%>"><%=npc.getName()%></a>|

<%}else if(obj instanceof MapDataBean){
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
<a href="user/info.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a>|

<%}%>
<%}%>
<%if(nodeMark.length()>0){%><br/><%}%>

<%@include file="bottom.jsp"%>
</p>
</card>
</wml>