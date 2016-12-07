<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.cache.*"%><%
String userId = request.getParameter("userId");
if(userId!=null){
    UserInfoUtil.userInfoCache.srm(StringUtil.toInt(userId));
    UserInfoUtil.userInfoCache.srm(StringUtil.toInt(userId));
    out.println("清除用户状态缓存：" + userId);
}
%>
用户状态缓存管理<br>
<br>
<table width="90%" align="center" border="1">        
		<form method="post">
		<tr>
			<td width="30%">
				用户ID（数字）
			</td>
			<td width="70%">
				<input type=text name="userId">
				<input type=submit value="提交">
			</td>
		</tr>
		</form>
</table>	
