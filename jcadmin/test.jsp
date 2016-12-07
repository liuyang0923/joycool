<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%!
public String getStringResult(String sql, String dbName){
	String ret = null;
	
	DbOperation dbOp = new DbOperation();
	dbOp.init(dbName);
	
	ResultSet rs = null;
	try {
		rs = dbOp.executeQuery(sql);
		if (rs != null && rs.next()) {
			ret = rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace(System.out);
	} finally {
		dbOp.release();
	}

	return ret;
}
%>
<%
    //boolean flag = SmsUtil.send("03", "message|http://wap.joycool.net","13810696137",1);
%>
<table width=80% border=1>
<tr>
  <td width=20%>聊天室</td>
  <td width=20%>页面显示内存在线人数</td>
  <td width=20%>数据库实际在线人数</td>
  <td width=20%>活动用户数</td>
</tr>
<%
int[] roomIds = null;
String sql = "select id from jc_room";
List roomIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
roomIds = new int[roomIdList.size()];
for(int i=0;i<roomIdList.size();i++){
	Integer roomId = (Integer)roomIdList.get(i);
	if(roomId==null)continue;
	
	roomIds[i] = roomId.intValue();
}
for(int i=0;i<roomIds.length;i++){
	JCRoomBean room = RoomUtil.getRoomById(roomIds[i]);
	if(room==null)continue;
	sql = "SELECT count(id) FROM jc_room_online where room_id=" + roomIds[i];
	int roomOnlineUserCount1 = room.getCurrentOnlineCount();
	int roomOnlineUserCount2 = SqlUtil.getIntResult(sql, Constants.DBShortName);
	sql = "select count(id) from jc_online_user where position_id=" + Constants.POSITION_CHAT + " and sub_id=" + roomIds[i];
	int roomActiveUserCount = SqlUtil.getIntResult(sql, Constants.DBShortName);	
%>
<tr>
  <td width=20%><%= room.getName() %></td>
  <td width=20%><%= roomOnlineUserCount1 %></td>
  <td width=20%><%= roomOnlineUserCount2 %></td>
  <td width=20%><%= roomActiveUserCount %></td>
</tr>
<%
}	
%>
</table>
<br>
<%
Hashtable onlineHash = (Hashtable)OnlineUtil.getOnlineHash().clone();
Iterator iter = onlineHash.keySet().iterator();

sql = "SELECT count(distinct user_id) FROM jc_online_user";
int onlineUserCount2 = SqlUtil.getIntResult(sql, Constants.DBShortName);
%>
总在线session数（包括在线注册用户和在线非注册用户）：<%= onlineHash.size() %>&nbsp;&nbsp;其中活跃session数目:<%= JoycoolSessionListener.getOnlineUserCount() %><br>
jc_online_user表里的在线注册用户数：<%= onlineUserCount2 %><br><br>