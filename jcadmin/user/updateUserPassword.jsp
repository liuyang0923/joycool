<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%>
<%
if(!group.isFlag(1)) return;
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>更新用户密码&nbsp;&nbsp;<a href="index.jsp">返回</a>&nbsp;&nbsp;<a href="addMoney.jsp">给用户加银行存款</a></p>
<p align=center>
<%
String mobile = request.getParameter("mobile");
String password = request.getParameter("password");
String defaultId = request.getParameter("defaultId");
if(defaultId==null) defaultId="";
if(password==null || password.trim().equals("")){
	password = "111111";
}
String id = request.getParameter("id");
if(mobile!=null && !mobile.equals("")){	
	if(mobile.startsWith("86")){
		mobile = mobile.substring(2);
	}
	String sql = "select id from user_info where mobile='" + mobile + "'";
    int userId = SqlUtil.getIntResult(sql, Constants.DBShortName);
    sql = "update user_info set password='" + Encoder.encrypt(password) + "' where mobile='" + mobile + "'";
    SqlUtil.executeUpdate(sql, Constants.DBShortName);
    UserInfoUtil.userInfoCache.srm(userId);
    net.joycool.wap.bean.UserBean onlineUser = (net.joycool.wap.bean.UserBean)OnlineUtil.getOnlineBean(String.valueOf(userId));
    if(onlineUser!=null)
    	onlineUser.setPassword(Encoder.encrypt(password));
    %>手机号为<%= mobile %>的用户密码已经更新为[<%= password %>]<%
}
if(id!=null && !id.equals("")){	
    String sql = "update user_info set password='" + Encoder.encrypt(password) + "' where id=" + id;
    SqlUtil.executeUpdate(sql, Constants.DBShortName);
    int userId = StringUtil.toInt(id);
    UserInfoUtil.userInfoCache.srm(userId);
    net.joycool.wap.bean.UserBean onlineUser = (net.joycool.wap.bean.UserBean)OnlineUtil.getOnlineBean(String.valueOf(userId));
    if(onlineUser!=null)
    	onlineUser.setPassword(Encoder.encrypt(password));
    %>ID为<%= id %>的用户密码已经更新为[<%= password %>]<%
}
%>
</p>
<table width="90%" align="center" border="1">
		<form method="post">
		<tr>
		    <td width="30%">
				密码
			</td>
			<td width="70%">
				<input type=text name="password">
			</td>
		</tr>
	    <tr>		
			<td width="30%">
				用户ID（数字）
			</td>
			<td width="70%">
				<input type=text name="id" value="<%=defaultId%>">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
</table>	