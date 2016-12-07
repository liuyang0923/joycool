<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.IBeginnerService" %><%@ page import="net.joycool.wap.bean.beginner.BeginnerHelpBean" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.OsCacheUtil" %><%
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IUserService userService = ServiceFactory.createUserService();
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
	if(document.id.userId.value == ''){
		alert("用户ID不能为空！");
		return false;
	}else if(isNaN(document.id.userId.value)){
		alert("用户ID只能是数字！");
		return false;
	}else{
		  return true;
	}
}

</script>
</head>
<body>
<H1 align="center">查询用户银行密码</H1>
<%
UserBean user = null;
UserSettingBean userSetting = null;
//判断通过用户名添加热心用户
int userId=StringUtil.toInt(request.getParameter("userId"));
if(userId>0){
    user = UserInfoUtil.getUser(userId);
    if(user!=null){
	userSetting = userService.getUserSetting("user_id="+userId);
	}

%>
<%if(userSetting != null){%>
<table width="100%" border="2">
<th>用户ID</th>
<th>用户姓名</th>
<th>银行密码</th>
<tr>
<td align="center"><%=user.getId()%></td>
<td align="center"><%=user.getNickName()%></td>
<td align="center"><%if(userSetting.getBankPw().equals("")){%>银行密码为空<%}else{%><%=userSetting.getBankPw()%><%}%></td>
</tr>
</table>
<%}else{%>没有查询到该用户的银行密码！<%}
}%>
<br/>
<p align="center">
<form name="id" action="checkPW.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="userId" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
</form>
</p>
<body>
</html>