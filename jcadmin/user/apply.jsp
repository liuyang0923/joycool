<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.spec.admin.*,  net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*, net.joycool.wap.cache.util.*,
                 net.joycool.wap.util.db.*"%><%!
static IUserService service = ServiceFactory.createUserService();
%><%response.setHeader("Cache-Control","no-cache");%>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%
ApplyAction action = new ApplyAction(request);
int id = action.getParameterInt("id");

%>
<table width="800" align="center" cellpadding="2">
	<tr>
<td></td>
<td>目标用户</td>
		<td>详细信息</td>
		<td>时间</td>
		<td>状态</td>
		<td></td>
	</tr>
    	<% 
	UserApplyBean apply = action.getUserApply("id="+id);;
	UserBean user = UserInfoUtil.getUser(apply.getUserId());
	int solve = action.getParameterIntS("solve");
	if(solve>=0) {
		apply.setStatus(solve);
		SqlUtil.executeUpdate("update user_apply set status="+solve+" where id=" + apply.getId(),2);
	}
%>
<tr bgcolor="#ccccff"><td width=40><%=apply.getId()%></td>
<td colspan=2>
<%if(user!=null){%><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%}%>(<%=apply.getUserId()%>)
- <font color="#dd0000"><%=apply.getTypeName()%></font>
</td>
	<td width="80"><%=DateUtil.sformatTime(apply.getCreateTime())%></td>
	<td width="60"><%=apply.getStatusName()%></td>
<td width="50"></td>
</tr>
<tr><td></td>
	<td width="60">
		<a href="queryUserInfo.jsp?id=<%=apply.getToId()%>"><%=apply.getToId()%></a>
	</td>
	<td colspan=3><%=StringUtil.toWml(apply.getContent())%></td>
</tr>
</table>
<table width="800" align="center" cellpadding="2">
<tr><td>设置为-<a href="apply.jsp?id=<%=apply.getId()%>&solve=2">已处理</a>|<a href="apply.jsp?id=<%=apply.getId()%>&solve=1">处理中</a>|<a href="apply.jsp?id=<%=apply.getId()%>&solve=0">未处理</a></td></tr>
<tr><td><a href="applys.jsp">返回列表</a></td></tr>
</table>
</p>

