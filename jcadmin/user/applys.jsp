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
int count = SqlUtil.getIntResult("select count(id) from user_apply",2);
PagingBean paging = new PagingBean(action, count, 10, "p");
List applyList = action.getUserApplyList("1 order by id desc limit " + paging.getStartIndex()+","+paging.getCountPerPage());

if(applyList != null){
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
    	<%    for(int i = 0;i<applyList.size();i++){
	UserApplyBean apply = (UserApplyBean)applyList.get(i);
	UserBean user = UserInfoUtil.getUser(apply.getUserId());
%>
<tr bgcolor="#ccccff"><td width=40><%=apply.getId()%></td>
<td colspan=2>
<%if(user!=null){%><a href="queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a><%}%>(<%=apply.getUserId()%>)
- <font color="#dd0000"><%=apply.getTypeName()%></font>
</td>
	<td width="80"><%=DateUtil.sformatTime(apply.getCreateTime())%></td>
	<td width="60"><%if(apply.getStatus()==0){%><font color=red><%=apply.getStatusName()%></font><%}else{%><%=apply.getStatusName()%><%}%></td>
<td width="50"><a href="apply.jsp?id=<%=apply.getId()%>">处理</a></td>
</tr>
<tr><td></td>
	<td width="60">
		<a href="queryUserInfo.jsp?id=<%=apply.getToId()%>"><%=apply.getToId()%></a>
	</td>
	<td colspan=3><%=StringUtil.toWml(apply.getContent())%></td>
</tr>
<%}%>
<tr><td colspan=6><%=paging.shuzifenye("applys.jsp",false,"|",response)%></td></tr>
</table>
<%}%>

<a href="/jcadmin/manage.jsp">返回管理首页</a><br/>
</p>
