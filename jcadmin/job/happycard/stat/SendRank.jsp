<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%response.setHeader("Cache-Control","no-cache");%>
<%!
public class UserInfo{
	String nickname;
	int userId;
	int totalCount;
	int outCount;
	int outReplyCount;
	/**
	 * @return Returns the nickname.
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname The nickname to set.
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return Returns the outCount.
	 */
	public int getOutCount() {
		return outCount;
	}

	/**
	 * @param outCount The outCount to set.
	 */
	public void setOutCount(int outCount) {
		this.outCount = outCount;
	}

	/**
	 * @return Returns the outReplyCount.
	 */
	public int getOutReplyCount() {
		return outReplyCount;
	}

	/**
	 * @param outReplyCount The outReplyCount to set.
	 */
	public void setOutReplyCount(int outReplyCount) {
		this.outReplyCount = outReplyCount;
	}

	/**
	 * @return Returns the totalCount.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount The totalCount to set.
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
%>
<%
Vector vec=new Vector();
DbOperation dbOp = new DbOperation();
dbOp.init();
UserInfo userInfo=null;
//top 20，邀请好友最多的前20名
ResultSet rs = dbOp.executeQuery("select a.id,a.nickname,count(*) as count from user_info as a join jc_happy_card_send as b on a.id=b.user_id  group by user_id order by count desc limit 0,20 ");
while(rs.next()){
	userInfo=new UserInfo();
	userInfo.setUserId(rs.getInt(1));
	userInfo.setNickname(rs.getString(2));
	userInfo.setTotalCount(rs.getInt(3));
	vec.add(userInfo);
}


%>
<html>
<head>
</head>
<body>
贺卡发送最多的人(前20名)&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">贺卡管理首页</a><br/>
<table width="800" border="1">
<th align="center">编号</th>
<th align="center" width="40">用户名</th>
<th align="center">用户ID</th>
<th align="center">发送总数</th>
<th align="center">站外发送总数</th>
<th align="center">站外发送回复数</th>
<%
int outReplyCount=0;
int inSendCount=0;
for(int i=0;i<vec.size();i++){
userInfo=(UserInfo)vec.get(i);
rs=dbOp.executeQuery("select count(*) from jc_happy_card_send where user_id="+userInfo.getUserId()+" and in_or_out_mark=1");
if(rs.next())
inSendCount=rs.getInt(1);
rs=dbOp.executeQuery("select count(*) from jc_happy_card_send where user_id="+userInfo.getUserId()+" and mark=1 and in_or_out_mark=1 ");
if(rs.next())
outReplyCount=rs.getInt(1);
%>
<tr>
<td><%=(i+1)%></td>
<td><%=userInfo.getNickname() %></td>
<td><%=userInfo.getUserId() %></td>
<td><%=userInfo.getTotalCount()%></td>
<td><%=inSendCount %></td>
<td><%=outReplyCount %></td>
</tr>
<%
}	
dbOp.release();
%>
</table>
<br/>
<a href="http://wap.joycool.net/jcadmin/job/happycard/stat/index.jsp">贺卡管理首页</a>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>