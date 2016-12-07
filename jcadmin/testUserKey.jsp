<%@ page contentType="text/html;charset=utf-8" %><%@ page import="net.joycool.wap.action.user.RankAction"%><%@ page import="net.joycool.wap.bean.NoticeBean"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.framework.BaseAction" %><%@ page import="net.joycool.wap.test.TestAction" %><p align=center>
<%
response.setHeader("Cache-Control","no-cache");
int userId = StringUtil.toInt(request.getParameter("userId"));
String userKey = "";
if(userId>0){
	String sql = "select user_key from jc_test_user where user_id=" + userId;
	userKey = SqlUtil.getStringResult(sql, Constants.DBShortName);
	%>ID为<%= userId %>的用户，答题ID是<%= userKey %><br><%
}
%>
<br>
调查问卷用户答题ID查询<br>
</p>
<table width="90%" align="center" border="1">
  <form method="post">
  <tr>
    <td width="30%"> 用户ID </td>
	<td width="70%">
	  <input type=text name="userId"><input type=submit value="提交">
	</td>
  </tr>
  </form>
</table>