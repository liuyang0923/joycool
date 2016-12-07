<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%
//lbj_登录限制_start
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
//lbj_登录限制_end

IUserService userService = ServiceFactory.createUserService();
CustomAction action = new CustomAction(request);
String buser=null;
//判断通过用户名添加黑名单用户
String group = request.getParameter("group");
String bak = request.getParameter("bak");
String usernamee=(String)request.getParameter("username");
int interval = action.getParameterInt("interval");
String username=null;
if(usernamee!=null&&group!=null){
	ForbidUtil.addForbid(group,new ForbidUtil.ForbidBean(StringUtil.toInt(usernamee),0,bak),10000000);
	if(!group.equals("op"))
		ForbidUtil.getAddAdminUser(StringUtil.toInt(usernamee)).addFlag(group);
}

//判断通过用户名添加黑名单用户
String nickName=(String)request.getParameter("nickName");
UserBean toUser = null;
if(nickName!=null&&group!=null){
	nickName=nickName.trim();
   if(!(nickName.equals(""))){
   toUser = userService.getUser("nickname = '"+nickName+"'");
		if(toUser!=null){
			ForbidUtil.addForbid(group,new ForbidUtil.ForbidBean(toUser.getId(),0,bak),10000000);
			if(!group.equals("op"))
			    ForbidUtil.getAddAdminUser(toUser.getId()).addFlag(group);
		}else{
		    //response.sendRedirect("adminuser.jsp");
		    BaseAction.sendRedirect("/jcadmin/adminuser.jsp?group="+group, response);
			return;
		}
   }
}




//判断删除黑名单用户
if(request.getParameter("delete") != null){
	int deleteId = StringUtil.toInt(request.getParameter("delete"));
    ForbidUtil.deleteForbid(group,deleteId);
	//response.sendRedirect("adminuser.jsp");
	ForbidUtil.getAdminMap().remove(new Integer(deleteId));
	BaseAction.sendRedirect("/jcadmin/adminuser.jsp?group="+group, response);
	return;
}
//判断重新设定
if(request.getParameter("clear") != null){
	//response.sendRedirect("adminuser.jsp");
	BaseAction.sendRedirect("/jcadmin/adminuser.jsp?group="+group, response);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.buser.username.value == ''){
				alert("用户名称不能为空！");
				return false;
			}else{
				  return true;
				 }
		}
function checkform1(){
			if(document.nickName.nickName.value == ''){
				alert("用户昵称不能为空！");
				return false;
			}else{
				  return true;
				 }
		}
</script>
</head>
<body>
<p><a href="adminuser.jsp?group=chata">聊天监察</a>|
<a href="adminuser.jsp?group=foruma">论坛监察</a>|
<a href="adminuser.jsp?group=homea">家园监察</a>|
<a href="adminuser.jsp?group=tonga">帮会监察</a>|
<a href="adminuser.jsp?group=maila">信件监察</a>|
<a href="adminuser.jsp?group=teama">圈子监察</a>|
<a href="adminuser.jsp?group=newsa">新闻监察</a>|
<a href="adminuser.jsp?group=infoa">个人资料监察</a>|
<a href="adminuser.jsp?group=gamea">游戏监察</a></p>
<font color=red><%=group%>监察列表</font>
<form name="buser" action="adminuser.jsp?group=<%=group%>" method="post" onsubmit="return checkform()" >
用户ID:<input name="username" type="text"  size="38" ><br/>
备注:<input name="bak" type="text"  size="38" value="">
<input type="submit" value="添加">
</form>
<form name="nickName" action="adminuser.jsp?group=<%=group%>" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="nickName" type="text"  size="38" ><br/>
备注:<input name="bak" type="text"  size="38"  value="">
<input type="submit" value="添加">
</form>
<table width="100%" border="2">
<%
//显示封禁列表
List forbidList = ForbidUtil.getForbidList(group);
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	int i = 1;
	while(itr.hasNext()){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)itr.next();
	UserBean user = UserInfoUtil.getUser(forbid.getValue());
%>
<tr>
<td width="10%"><%=i%></td>
<td><%=forbid.getValue()%><%if(user!=null){%>(<a href="user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>)<%}%></td>
<td><%=forbid.getBak()%></td>
<td><%=DateUtil.formatDate2(forbid.getStartTime())%></td>
<td width="10%"><a href="adminuser.jsp?group=<%=group%>&delete=<%=forbid.getValue()%>">删除</a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<body>
</html>