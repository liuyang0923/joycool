<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.util.db.*" %><%@ page import="java.util.*" %><%@ page import="java.sql.*" %><%@ page import="java.text.*" %><%@ page import="java.net.*" %><%@ page import="net.joycool.wap.util.*" %><%response.setHeader("Cache-Control","no-cache");%>
<%!
int ITEM_PER_PAGE=100;
//时间格式HH:mm 如08：15
public String formatTime(String time){
	int startIndex=time.indexOf(" ");
	int endIndex=time.lastIndexOf(":");
	return time.substring(startIndex,endIndex);
}
//得到当前日期的下一天，如2006-08-15，返回为2006-08-16
public String getNextDay(String date){
	java.util.Date d = null;;
	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	try {
		d=df.parse(date);
	} catch (ParseException e) {
		e.printStackTrace();
		return "";
	}
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	c.add(Calendar.DAY_OF_YEAR,1);
	return df.format(c.getTime());
}
%>
<%
int i=1;
int mark=0;
int pageIndex=0;//页号
int itemCount=0;
int totalPageCount=0;
DbOperation dbOp=null;
String startDate=request.getParameter("date");//起始日期
String endDate=getNextDay(startDate);//结束日期
String prefixUrl="inviteList.jsp?date=";
prefixUrl=prefixUrl+startDate;
if(request.getParameter("pageIndex")!=null)
	pageIndex=StringUtil.toInt(request.getParameter("pageIndex"));
ResultSet rs=null;
String strWhere=" send_datetime>='"+startDate+"' and send_datetime<'"+endDate+"' ";

try{
	dbOp= new DbOperation();
dbOp.init();

rs=dbOp.executeQuery("select count(*) as count from jc_room_invite where "+strWhere);
if(rs.next())
	itemCount=rs.getInt("count");
totalPageCount=itemCount/ITEM_PER_PAGE;
if(itemCount%ITEM_PER_PAGE>0)
	totalPageCount++;
if(pageIndex>=totalPageCount)
	pageIndex--;
if(pageIndex<0)
	pageIndex=0;

 rs=dbOp.executeQuery("select a.nickname,b.* from user_info as a join jc_room_invite as b on a.id=b.user_id where "+strWhere+" order by b.id limit "+pageIndex*ITEM_PER_PAGE+","+ITEM_PER_PAGE);

%>
<html>
<head>
</head>
<body>
所有用户发送信息详细列表&nbsp;&nbsp;&nbsp;&nbsp;
<a href="http://wap.joycool.net/jcadmin/chat/inviteListSum.jsp">返回每日下发信息统计</a>
<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a><br/>
统计日期:<%=startDate %><br/>
<table width="800" border="1">
<th align="center">序号</th>
<th align="center">发送时间</th>
<th align="center">发送用户昵称</th>
<th align="center">发送用户ID</th>
<th align="center">发送内容</td>
<th align="center">接收手机号</th>
<th align="center">回复状态</th>
<th align="center">成功与否</th>
<tr>
<%
	while(rs.next()){ %>
<td><%=i%></td>
<td><%=formatTime(rs.getString("send_datetime"))%></td>
<td><%=rs.getString("nickname")%></td>
<td><%=rs.getInt("user_id")%></td>
<td><%=rs.getString("content")%></td>
<td><%=rs.getString("mobile")%></td>
<td><% mark=rs.getInt("mark"); if(mark==0){%>否<%}else{ %>是<%} %></td>
<td><% mark=rs.getInt("success_mark"); if(mark==0){%><font color=red>否</font><%}else{ %>是<%} %></td>
</tr>
<%i++;}
}catch(SQLException ex){
	dbOp.release();
}
	dbOp.release();%>
</table>
<br/>
<%
String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
if(!"".equals(fenye)){%><%=fenye%><br/><%} %>
<a href="http://wap.joycool.net/jcadmin/chat/inviteListSum.jsp">返回每日下发信息统计</a>
<a href="http://wap.joycool.net/jcadmin/chat/inviteIndex.jsp">邀请好友来乐酷首页</a>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/>
</body>
</html>