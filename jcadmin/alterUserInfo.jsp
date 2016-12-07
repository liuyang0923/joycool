<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.service.factory.*,net.joycool.wap.service.infc.*"%><%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.util.StringUtil" %><%@ page import="net.joycool.wap.util.UserInfoUtil,net.joycool.wap.framework.*" %><%
response.setHeader("Cache-Control","no-cache");
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("./login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
String inputUserId=request.getParameter("inputUserId");
String nickname=request.getParameter("nickname");
IUserService userService = ServiceFactory.createUserService();
UserBean user=null;
String tip=null;
if(inputUserId!=null)
{
user=UserInfoUtil.getUser(StringUtil.toInt(inputUserId));
}
if(nickname!=null)
{
user=userService.getUser("nickname like '"+StringUtil.toSqlLike(nickname)+"%'");
tip="没有查到与此相匹配的用户。";
}
String form2=request.getParameter("form2");

if(form2!=null)
{
	String userId=request.getParameter("userId");
	String nickName=request.getParameter("nickName");
	String introduce=request.getParameter("introduce");
	UserBean user1=userService.getUser("nickname='"+StringUtil.toSql(nickName)+"' and id!="+userId);
	if(user1!=null)
	{
		tip="此昵称已经存在!";
		user=UserInfoUtil.getUser(StringUtil.toInt(userId));
	}else{
	UserInfoUtil.updateUser("nickname='"+StringUtil.toSql(nickName)+"',self_introduction='"+StringUtil.toSql(introduce)+"'","id="+userId,userId+"");
	UserBean ou = (UserBean)OnlineUtil.getOnlineBean(userId);
	if(ou!=null){
		ou.setNickName(nickName);
		ou.setSelfIntroduction(introduce);
	}
	tip="用户"+userId+"信息已经更新成功!";
	}
}
%>
<html >
<head>
</head>
<div align="center">
<font color="red"><%=tip!=null?tip:""%></font><br/>
修改用户信息<br/>
<form name="form1" method="post" action="alterUserInfo.jsp">
用户ID：<input type="text" name="inputUserId" onKeyPress="if(event.keyCode<48||event.keyCode>57)  event.returnValue=false;" /><input type="submit" name="submit" value="确定"/>
</form>
</div>
<br/>
<div align="center">
查用户信息<br/>
<form name="form1" method="post" action="alterUserInfo.jsp">
用户昵称：<input type="text" name="nickname" /><input type="submit" name="submit" value="确定"/>
</form>
</div>

<% if(user!=null){%>
<table align="center" border="1" >
<caption></caption>
<form name="form2" method="post" action="alterUserInfo.jsp">
<tr><th>用户ID</th><td><input type="text" name="userId" value="<%=user.getId()%>" readonly="readonly" /><input type="hidden" name="form2" value="y"/></td></tr>
<tr><th>用户昵称</th><td><input type="text" name="nickName" value="<%=user.getNickName()%>"/></td></tr>
<tr><th>自我描述</th><td><textarea  name="introduce" rows="5" cols="15" /><%=user.getSelfIntroduction()%></textarea> </td></tr>
<tr><td align="center" colspan="2"><input type="submit" name="alter" value="更改" /><input type="reset" name="reset" value="重置"></td></tr>
</form>
</table>
<%}%>
</html>