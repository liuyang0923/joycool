<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,  net.joycool.wap.bean.tong.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*, net.joycool.wap.cache.util.*,
                 net.joycool.wap.util.db.*"%><%!
%><%
if(!group.isFlag(1))
	return;
%>
<link href="../farm/common.css" rel="stylesheet" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>查询用户名&nbsp;&nbsp;<a href="index.jsp">返回</a>&nbsp;&nbsp;<a href="addMoney.jsp">给用户加银行存款</a></p>
<p align=left>
<%
String mobile = request.getParameter("mobile");

UserBean user = null;

if(mobile==null || mobile.equals("")) return;

if(mobile.startsWith("86")){
	mobile = mobile.substring(2);
}
String condition = "mobile='" + StringUtil.toSql(mobile) + "'";
List registerList = SqlUtil.getObjectsList("select id,password,create_time from call_log2 where " + condition,5);
List messageList = SqlUtil.getObjectsList("select id,content,addtime from receive_message_history where " + condition,5);

%>
    <%=mobile%>注册信息<br/>
<table width="400" cellpadding="2">
	<tr>
		<td>id</td>
		<td>密码</td>
		<td>时间</td>
	</tr>
	<%for(int i=0;i<registerList.size();i++){
	Object[] objs = (Object[])registerList.get(i);
	%><tr><td><%=objs[0]%></td><td><%=objs[1]%></td><td><%=objs[2]%></td></tr><%}%>
</table>

    <%=mobile%>短信信息<br/>
<table width="400" cellpadding="2">
	<tr>
		<td>id</td>
		<td>内容</td>
		<td>时间</td>
	</tr>
	<%for(int i=0;i<messageList.size();i++){
	Object[] objs = (Object[])messageList.get(i);
	%><tr><td><%=objs[0]%></td><td><%=objs[1]%></td><td><%=objs[2]%></td></tr><%}%>
</table>

</p>
<table width="400"  border="1">
        <form method="get" action="queryRegister.jsp">
		<tr>
			<td width="30%">
				用户手机号
			</td>
			<td width="70%">
				<input type=text name="mobile">
				<input type=submit value="查询">
			</td>
		</tr>
		</form>
</table>	