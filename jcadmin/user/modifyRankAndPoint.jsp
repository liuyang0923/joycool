<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.cache.*,
                 net.joycool.wap.util.db.*"%><%
String action = request.getParameter("action");
int userId = StringUtil.toInt(request.getParameter("userId"));
if("update".equals(action)){
	UserBean user = UserInfoUtil.getUser(userId);
	UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
	if(userStatus==null || user==null){
		out.print("用户ID错误！");
		return;
	}
	int rank = StringUtil.toInt(request.getParameter("rank"));
	int point = StringUtil.toInt(request.getParameter("point"));
	if(rank<0)rank=0;
	if(point<0)point=0;
	String sql = "update user_status set rank=" + rank + ",point=" + point + " where user_id=" + userId;
	SqlUtil.executeUpdate(sql, Constants.DBShortName);
	UserInfoUtil.flushUserStatus(userId);
	
	BaseAction.sendRedirect("/jcadmin/user/modifyRankAndPoint.jsp?action=view&userId=" + userId, response);
	return;
}
%>                
<p>修改用户等级和经验值&nbsp;&nbsp;<a href="index.jsp">返回</a>
<p align=center>
<%
if(userId>0 && "view".equals(action)){
	UserBean user = UserInfoUtil.getUser(userId);
	UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
	%>用户<%= user.getNickName() %>的当前状态：<%= userStatus.getRank() %>级 &nsp;&nbsp;<%= userStatus.getPoint() %>点经验值<br/><%
}
%>
<table width="90%" align="center" border="1">
        <form method="post" action="/jcadmin/user/modifyRankAndPoint.jsp">
        <input type=hidden name="action" value="update">
        <tr>
		    <td width="30%">
				用户ID
			</td>
			<td width="70%">
				<input type=text name="userId">
			</td>
	    </tr>
		<tr>
		    <td width="30%">
				等级
			</td>
			<td width="70%">
				<input type=text name="rank">
			</td>
	    </tr>
	    <tr>		
			<td width="30%">
				经验值
			</td>
			<td width="70%">
				<input type=text name="point">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
</table>	
</p>