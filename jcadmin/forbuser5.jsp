<%@ page contentType="text/html;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%
IUserService userService = ServiceFactory.createUserService();
CustomAction action = new CustomAction(request);
String buser=null;
//判断通过用户名添加黑名单用户
String bak = request.getParameter("bak");
String usernamee=(String)request.getParameter("username");
int interval = action.getParameterInt("interval");
String username=null;
if(usernamee!=null){
	ForbidUtil.addForbid("tong",new ForbidUtil.ForbidBean(StringUtil.toInt(usernamee),0,bak),interval,adminUser.getName());
}

//判断通过用户名添加黑名单用户
String nickName=(String)request.getParameter("nickName");
UserBean toUser = null;
if(nickName!=null){
	nickName=nickName.trim();
   if(!(nickName.equals(""))){
   toUser = userService.getUser("nickname = '"+nickName+"'");
		if(toUser!=null){
		//lbj_log_oper_start
	    LogUtil.logOperation(adminUser.getName() + "帮会封禁用户: " + toUser.getId() + "昵称:" + toUser.getNickName());
	    //lbj_log_oper_end
			ForbidUtil.addForbid("tong",new ForbidUtil.ForbidBean(toUser.getId(),0,bak),interval, adminUser.getName());
		    
		}else{
		    //response.sendRedirect("forbuser5.jsp");
		    BaseAction.sendRedirect("/jcadmin/forbuser5.jsp", response);
			return;
		}
   }
}




//判断删除黑名单用户
if(request.getParameter("delete") != null){
	int deleteId = StringUtil.toInt(request.getParameter("delete"));
    ForbidUtil.deleteForbid("tong",deleteId);
	//response.sendRedirect("forbuser5.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser5.jsp", response);
	return;
}
//判断重新设定
if(request.getParameter("clear") != null){
	//response.sendRedirect("forbuser5.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser5.jsp", response);
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
<font color=red>封禁帮会权限列表</font>
<form name="buser" action="forbuser5.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="username" type="text"  size="38" ><br/>
理由:<input name="bak" type="text"  size="38" value="不良帮会举动">
<input name=interval type=radio value="30" >30分钟
<input name=interval type=radio value="60" checked>1小时
<input name=interval type=radio value="1440" >1天
<input name=interval type=radio value="4320" >3天
<input name=interval type=radio value="14400" >10天
<input name=interval type=radio value="43200" >1月
<input name=interval type=radio value="129600" >3月
<input type="submit" value="添加封禁">
</form>
<form name="nickName" action="forbuser5.jsp" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="nickName" type="text"  size="38" ><br/>
理由:<input name="bak" type="text"  size="38"  value="不良帮会举动">
<input name=interval type=radio value="30" >30分钟
<input name=interval type=radio value="60" checked>1小时
<input name=interval type=radio value="1440" >1天
<input name=interval type=radio value="4320" >3天
<input name=interval type=radio value="14400" >10天
<input name=interval type=radio value="43200" >1月
<input name=interval type=radio value="129600" >3月
<input type="submit" value="添加封禁">
</form>
<table width="100%" border="2">
<%
//显示封禁列表
List forbidList = ForbidUtil.getForbidList("tong");
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	int i = 1;
	while(itr.hasNext()){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)itr.next();
%>
<tr>
<td width="10%"><%=i%></td>
<td><%=forbid.getValue()%></td>
<td><%=forbid.getOperator()%></td>
<td><%=forbid.getBak()%></td>
<td><%=DateUtil.formatDate2(forbid.getEndTime())%></td>
<td width="10%"><a href="forbuser5.jsp?delete=<%=forbid.getValue()%>">删除</a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<body>
</html>