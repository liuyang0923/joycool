<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.*,
                 java.sql.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*,
                 net.joycool.wap.bean.*,
                 net.joycool.wap.service.factory.*,
                 net.joycool.wap.service.infc.*,
                 net.joycool.wap.util.db.*"%>
<%
if(!group.isFlag(1)) return;

%><meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<p>查询用户密码&nbsp;&nbsp;<a href="index.jsp">返回</a>&nbsp;&nbsp;<a href="addMoney.jsp">给用户加银行存款</a></p>
<p align=center>
<%
String id = request.getParameter("id");
IUserService service = ServiceFactory.createUserService();
UserBean user = null;
if(id!=null){
	user = service.getUser("id=" + id);
	if(user!=null){
%>
<table width="90%" align="center" border="1">      
		<tr>
			<td width="30%">
				用户ID
			</td>
			<td width="30%">
				<%= user.getId() %>
			</td>			
		</tr>
		<tr>
			<td width="30%">
				手机号
			</td>
			<td width="30%">
				<%= user.getMobile() %>
			</td>			
		</tr>
		<tr>
			<td width="30%">
				密码
			</td>
			<td width="30%">
				[<%= Encoder.decrypt(user.getPassword()) %>]
			</td>			
		</tr>
		<tr>
			<td width="30%">
				昵称
			</td>
			<td width="30%">
				<%= user.getNickName() %>
			</td>			
		</tr>
</table>
<%
    }
    else{
%>
    id为<%= id %>的用户不存在！
<%    	
    }
}    
%>
</p>
<table width="90%" align="center" border="1">      
		<form method="post">
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