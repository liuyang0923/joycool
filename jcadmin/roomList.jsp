<%--fanys add 2006-06-28--%>
<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.chat.*"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.action.chat.JCRoomChatAction"%><%
response.setHeader("Cache-Control","no-cache");
JCRoomChatAction action=new JCRoomChatAction(request);
if(request.getParameter("recommend")!=null){
	int recommendFlag=Integer.parseInt(request.getParameter("recommend"));
	int roomId=Integer.parseInt(request.getParameter("roomId"));
	action.setRecommendRoom(roomId,recommendFlag);
}

action.roomList2(request);
Vector top = (Vector)request.getAttribute("top");
Vector room1 = (Vector)request.getAttribute("room1");
//if(room1!=null){
//得到不需要认证房间的分页参数
int totalPageCount1 = ((Integer) request.getAttribute("totalPageCount1")).intValue();
int pageIndex1 = ((Integer) request.getAttribute("pageIndex1")).intValue();
String prefixUrl1 = (String) request.getAttribute("prefixUrl1");
String backTo1 = (String) request.getAttribute("backTo1");

Vector room2 = (Vector)request.getAttribute("room2");
int totalPageCount = ((Integer) request.getAttribute("totalPageCount")).intValue();
int pageIndex = ((Integer) request.getAttribute("pageIndex")).intValue();
String prefixUrl = (String) request.getAttribute("prefixUrl");
String backTo = (String) request.getAttribute("backTo");
JCRoomBean room=null;
%>
=缺省链接=<br/>
<a href="chatHall.jsp?roomId=0">聊天大厅</a><br/>
<a href="chatHall.jsp?roomId=1">小黑屋</a><br/><br/>
＝推荐房屋＝<br/>
<table  border="1">
<th>序号</th><th>聊天室名称</th><th>缓存情况</th><th>房间内成员</th><th>操作</th>
<%
for(int i = 0; i < top.size(); i ++){
	room = (JCRoomBean)top.get(i);
%>
<tr><td><%=i+1%></td><td><a href="chatHall.jsp?roomId=<%=room.getId()%>" ><%=StringUtil.toWml(room.getName())%></a></td><td><a href="roomContentCacheMap.jsp?roomId=<%=room.getId()%>" ><%=StringUtil.toWml(room.getName())%></a></td><td><a href="roomUserList.jsp?roomId=<%=room.getId()%>" >房间内成员</a></td><td><a href="roomList.jsp?roomId=<%=room.getId()%>&recommend=0" >取消推荐</a></td></tr>
<%}%>
</table>
<br/>

＝可随意进入＝<br/>
<table  border="1"><th>序号</th><th>聊天室名称</th><th>缓存情况</th><th>房间内成员</th><th>房间介绍</th><th>排名</th><th>操作</th>

<%
for(int i = 0; i < room1.size(); i ++){
	room = (JCRoomBean)room1.get(i);
%>
<tr><td><%=i+1%></td><td><a href="chatHall.jsp?roomId=<%=room.getId()%>" ><%=StringUtil.toWml(room.getName())%></a></td><td><a href="roomContentCacheMap.jsp?roomId=<%=room.getId()%>" ><%=StringUtil.toWml(room.getName())%></a></td><td><a href="roomUserList.jsp?roomId=<%=room.getId()%>" >房间内成员</a></td>
<td>
<form name="desc" action="roomDescription.jsp" method="post">
<input type="text" name="description" value="<%=room.getDescription()%>">
<input type="hidden" name="id" value="<%=room.getId()%>">
<input type="submit" value="更新">
</form>
</td>
<td>
<form name="top" action="roomTop.jsp" method="post">
<input type="text" name="top" value="<%=room.getTop()%>">
<input type="hidden" name="id" value="<%=room.getId()%>">
<input type="submit" value="更新">
</form>
</td>
<%if(room.getMark()==0){%>
<td><a href="roomList.jsp?roomId=<%=room.getId()%>&recommend=1" >推荐</a></td>
<%}else{ %>
<td></td>
<%}%></tr><%}%>

</table>
<%
String fenye1 = action.shuzifenye(totalPageCount1, pageIndex1, prefixUrl1, true, "|", response);
if(!"".equals(fenye1)){
%>
<%=fenye1%><br/>
<%}%>
<br/>
=需要主人认证的=<br/>
<table  border="1"><th>序号</th><th>聊天室名称</th><th>房间内成员</th><th>操作</th>
<%
for(int i = 0; i < room2.size(); i ++){
	room = (JCRoomBean)room2.get(i);
%>
<tr><td><%=i+1%></td><td><a href="chatHall.jsp?roomId=<%=room.getId()%>" ><%=StringUtil.toWml(room.getName())%></a></td><td><a href="roomUserList.jsp?roomId=<%=room.getId()%>" >房间内成员</a></td>
<%if(room.getMark()==0){%>
<td><a href="roomList.jsp?roomId=<%=room.getId()%>&recommend=1" >推荐</a></td>
<%}else{ %>
<td></td>
<%}%></tr><%}%>
</table>
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){
%>
<%=fenye%><br/>
<%} %>
<br/><a href="<%=("chatmanage.jsp") %>">返回</a>
