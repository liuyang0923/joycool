<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.framework.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.util.*,net.joycool.wap.cache.*,net.joycool.wap.util.db.*,java.sql.*"%><%@ page import="net.joycool.wap.action.chat.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.*,net.joycool.wap.service.factory.*,net.joycool.wap.bean.chat.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%
String action = request.getParameter("action");

if("update".equals(action)){
	int type = StringUtil.toInt(request.getParameter("type"));
	int sendCount = StringUtil.toInt(request.getParameter("sendCount"));
	if(sendCount<0){
		sendCount = 0;
	}
	int maxSendCount = StringUtil.toInt(request.getParameter("maxSendCount"));
	if(maxSendCount<0){
		maxSendCount = 0;
	}
	if(type!=-1 && maxSendCount>0){
	    String sql = "update sms_log set send_count=" + sendCount + ",max_send_count=" + maxSendCount + 
	                 ",last_update_time=now() where type=" + type;
	    SqlUtil.executeUpdate(sql, Constants.DBShortName);
	}
}
%>
<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a><br/><br/>

PUSH下发数量<br/>
<table width=70% border=1>
  <tr>
    <td>类型</td>
    <td>已经发送数量</td>
    <td>最大允许发送数量</td>
  </tr>
<%
DbOperation dbOp=null;
ResultSet rs=null;
try{
	dbOp= new DbOperation();
	dbOp.init();
	rs=dbOp.executeQuery("select * from sms_log where type=" + SmsUtil.TYPE_PUSH);
	while(rs!=null && rs.next()){
		int type = rs.getInt("type");
		int sendCount = rs.getInt("send_count");
		int maxSendCount = rs.getInt("max_send_count");
		String lastUpdateTime = rs.getString("last_update_time");
		%>		
  <tr>
    <td>PUSH</td>
    <td><%= sendCount %></td>
    <td><%= maxSendCount %></td>
  </tr>
  </form>
		<%
	}
}
catch(SQLException e){
	e.printStackTrace();
}
finally{
	dbOp.release(); 
}
%>
</table>
<br/>
<br/>

重置push<br/>
<table width=70% border=1>
<form method="post">
  <input type=hidden name="action" value="update">
  <input type=hidden name="type" value="<%= SmsUtil.TYPE_PUSH %>">		
  <tr>
    <td>PUSH</td>
    <td>已经发送数量<input name="sendCount" value="0"></td>
    <td>最大允许发送数量<input name="maxSendCount" value="10000">&nbsp;&nbsp;<input type=submit value="更新"></td>
  </tr>
  </form>
</table>  