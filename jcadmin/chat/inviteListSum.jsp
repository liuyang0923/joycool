<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.net.*" %><%@ page import="net.joycool.wap.util.*" %><%@ page import="net.joycool.wap.bean.chat.RoomInviteStatBean"%><%@ page import="net.joycool.wap.service.infc.IChatService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%response.setHeader("Cache-Control","no-cache");%>
<%!
int ITEM_PER_PAGE=100;
//日期格式：yyyy-MM-dd,例如2006-08-15
public String formatTime(String time){
	int startIndex=time.indexOf(" ");
	return time.substring(0,startIndex);
}
public  void statTodayInvite() {
	IChatService chatService = ServiceFactory.createChatService();
	int inviteCount = 0;// 下发总数
	int acceptNewCount = 0;// 接收的新用户数
	int replyCount = 0;// 回复的用户总数
	int replyNewcount = 0;// 回复的新用户数
	int reachLimitCount = 0;// 发送达到上限的人数

	Calendar c = Calendar.getInstance();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String startDate = df.format(c.getTime());
	c.add(Calendar.DAY_OF_YEAR, 1);
	String endDate = df.format(c.getTime());

	
	String strWhere = " send_datetime>='" + startDate
			+ "' and send_datetime<'" + endDate + "' ";
	inviteCount = chatService.getRoomInviteCount(strWhere);
	acceptNewCount = chatService.getRoomInviteCount(strWhere
			+ " and new_user_mark=1");
	replyCount = chatService.getRoomInviteCount(strWhere + " and mark=1");
	replyNewcount = chatService.getRoomInviteCount(strWhere
			+ " and  invitee_id is not null ");
	reachLimitCount = chatService
			.getRoomInviteCount("select count(*) as c_id from (select count(*) as c_id from jc_room_invite where "
					+ strWhere
					+ " group by user_id having c_id>=5) as temp ");
	RoomInviteStatBean statBean = new RoomInviteStatBean();
	statBean.setInviteCount(inviteCount);
	statBean.setAcceptNewCount(acceptNewCount);
	statBean.setReplyCount(replyCount);
	statBean.setReplyNewCount(replyNewcount);
	statBean.setReachLimitCount(reachLimitCount);
	statBean.setStatDatetime(startDate);
	chatService.deleteRoomInviteStat(" stat_datetime>='"+startDate+"' and stat_datetime<' "+endDate+"'");
	chatService.addRoomInviteStat(statBean);

}

%>
<%
statTodayInvite();
int totalPageCount=0;
int pageIndex=0;
int itemCount=0;
String prefixUrl="inviteListSum.jsp";
DbOperation dbOp=null;
ResultSet rs=null;
if(request.getParameter("pageIndex")!=null)
	pageIndex=StringUtil.toInt(request.getParameter("pageIndex"));
int i=1;
try{
	dbOp= new DbOperation();
	dbOp.init();
	rs=dbOp.executeQuery("select count(*) as count from jc_room_invite_stat ");
	if(rs.next())
		itemCount=rs.getInt("count");
	itemCount=itemCount;
	totalPageCount=itemCount/ITEM_PER_PAGE;
	if(itemCount%ITEM_PER_PAGE>0)
		totalPageCount++;
	if(pageIndex>=totalPageCount)
		pageIndex--;
	if(pageIndex<0)
		pageIndex=0;
	rs=dbOp.executeQuery("select * from jc_room_invite_stat order by stat_datetime desc limit "+pageIndex*ITEM_PER_PAGE+","+ITEM_PER_PAGE);
%>
<html>
<head>
</head>
<body>
每日下发信息统计&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a><br/>
<table width="800" border="1">
<th align="center">统计日期</th>
<th align="center">下发总数</th>
<th align="center">接收的新用户数</td>
<th align="center">回复的用户总数</th>
<th align="center">回复的新用户数</th>
<th align="center">发送达到上限的人数</th>

<%
String date="";
while(rs.next()){
	date=formatTime(rs.getString("stat_datetime")) ;
%>
<tr>

<td><a href="http://wap.joycool.net/jcadmin/chat/inviteList.jsp?date=<%=date%>"><%=date %></a></td>
<td><%=rs.getInt("invite_count") %></td>
<td><%=rs.getInt("accept_new_count") %></td>
<td><%=rs.getInt("reply_count") %></td>
<td><%=rs.getInt("reply_new_count") %></td>
<td><%=rs.getInt("reach_limit_count") %></td>
</tr>
<%i++;}
}catch(SQLException ex)
{
	dbOp.release();
	}
dbOp.release(); 
%>
</table>
<font color=red>注：点击日期可以查看当天的所有用户发送消息列表</font><br/>
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, false, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%} %>


<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>