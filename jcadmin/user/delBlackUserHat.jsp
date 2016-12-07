<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.bean.UserStatusBean" %><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>删除恶人帽子&nbsp;&nbsp;<a href="index.jsp">返回</a>&nbsp;&nbsp;</p>
<p align=center>
<%
String action = request.getParameter("action");
if("delete".equals(action)){
int id = StringUtil.toInt(request.getParameter("id"));
if(id>0){	
    String sql = "update user_status set image_path_id=0 where user_id=" + id;
    SqlUtil.executeUpdate(sql, Constants.DBShortName);
    UserStatusBean.flushUserHat(id);
    UserInfoUtil.userInfoCache.srm(id);
    %>ID为<%= id %>的用户已经取消恶人的帽子了。<%
}
else{
	%>ID数据不对！<%
}
}
%>
</p>
<table width="90%" align="center" border="1">      
		<form method="post">
		<input type=hidden name="action" value="delete">
	    <tr>		
			<td width="30%">
				用户ID（数字）
			</td>
			<td width="70%">
				<input type=text name="id">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
</table>	