<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%response.setHeader("Cache-Control","no-cache");%>
<%!
public class InviteSuccessRankBean {
	public int userId;
	public String nickname;
	public int successCount;
	public int count;
}
%>
<%
InviteSuccessRankBean successBean=null;
HashMap hashTemp=new HashMap();
Vector rankList=new Vector();
DbOperation dbOp = new DbOperation();
dbOp.init();
int i=1;
//top 20，邀请好友最多的前20名
try{
ResultSet rs = dbOp.executeQuery("select a.id,a.nickname,count(*) as successCount from user_info as a join jc_room_invite as b on a.id=b.user_id where login_datetime is not null group by user_id order by successCount desc limit 0,20 ");
while(rs.next()){
	successBean=new InviteSuccessRankBean();
	successBean.userId=rs.getInt("id");
	successBean.nickname=rs.getString("nickname");
	successBean.successCount=rs.getInt("successCount");
	hashTemp.put(new Integer(i),successBean);
	i++;
}
for(int j=0;j<hashTemp.size();j++){
	successBean=(InviteSuccessRankBean)hashTemp.get(new Integer(j+1));
	rs = dbOp.executeQuery("select count(*) as count from jc_room_invite where user_id="+successBean.userId+" group by user_id");
	if(rs.next())
		successBean.count=rs.getInt("count");
	hashTemp.put(new Integer(j+1),successBean);
		
}
}catch(SQLException ex){
	dbOp.release();
}
dbOp.release();
%>
<html>
<head>
</head>
<body>
累计邀请成功最多的人(前20名)&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a><br/>
<table width="800" border="1">
<th align="center" width="40">排名</th>
<th align="center">用户昵称</th>
<th align="center">用户id</td>
<th align="center">发送总数</th>
<th align="center">邀请成功总数</th>
<%
for(int j=0;j<hashTemp.size();j++){
	successBean=(InviteSuccessRankBean)hashTemp.get(new Integer(j+1));
%>
<tr>
<td><%=(j+1)%></td>
<td><%=successBean.nickname %></td>
<td><%=successBean.userId %></td>
<td><%=successBean.count  %></td>
<td><%=successBean.successCount  %></td>
<%}%>
</table>
<br/>
<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>