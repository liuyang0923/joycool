<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*"%><%@ page import="net.joycool.wap.service.infc.IBeginnerService" %><%@ page import="net.joycool.wap.bean.beginner.BeginnerHelpBean" %><%@ page import="net.joycool.wap.framework.*,net.joycool.wap.cache.OsCacheUtil" %><%
if(session.getAttribute("adminLogin") == null){
	//response.sendRedirect("../login.jsp");
	BaseAction.sendRedirect("/jcadmin/login.jsp", response);
	return;
}
IBeginnerService beginnerService = ServiceFactory.createBeginnerService();
IUserService userService = ServiceFactory.createUserService();
Vector beginnerHelpList=beginnerService.getBeginnerHelpList("1=1");
UserBean toUser = null;
//判断通过用户名添加热心用户
String userId=(String)request.getParameter("id");
int id =StringUtil.toInt(userId);
String username=null;
if(userId!=null && id>0){
    toUser = userService.getUser("id = "+id);
    if(toUser!=null){
	BeginnerHelpBean beginnerHelp = new BeginnerHelpBean();
	beginnerHelp.setUserId(id);
	beginnerHelp.setSendCount(0);
	beginnerHelp.setReceiveCount(0);
	beginnerService.addBeginnerHelp(beginnerHelp);
	//清空热心人的缓存组
	OsCacheUtil.flushGroup(OsCacheUtil.BEGINNER_HELP_CACHE_GROUP);
	}
    response.sendRedirect("beginnerManage.jsp");
   return;
}

//判断通过用户昵称添加黑名单用户
String nickName=(String)request.getParameter("nickName");
if(nickName!=null){
	nickName=nickName.trim();
   if(!(nickName.equals(""))){
   toUser = userService.getUser("nickname = '"+nickName+"'");
		if(toUser!=null){
			BeginnerHelpBean beginnerHelp = new BeginnerHelpBean();
			beginnerHelp.setUserId(toUser.getId());
			beginnerHelp.setSendCount(0);
			beginnerHelp.setReceiveCount(0);
			beginnerService.addBeginnerHelp(beginnerHelp);
			//清空热心人的缓存组
			OsCacheUtil.flushGroup(OsCacheUtil.BEGINNER_HELP_CACHE_GROUP);
			response.sendRedirect("beginnerManage.jsp");
			return;
			}
		}else{
		    response.sendRedirect("beginnerManage.jsp");
			return;
		}
}


//判断删除热心用户
if(request.getParameter("delete") != null){
	int deleteId = Integer.parseInt(request.getParameter("delete"));
    if(deleteId==-1){
     	return;
    }
	beginnerService.delBeginnerHelp("id="+deleteId);
	response.sendRedirect("beginnerManage.jsp");
	//清空热心人的缓存组
	OsCacheUtil.flushGroup(OsCacheUtil.BEGINNER_HELP_CACHE_GROUP);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.buser.id.value == ''){
				alert("用户ID不能为空！");
				return false;
			}else if(isNaN(document.buser.id.value)){
				alert("用户ID只能是数字！");
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
<H1 align="center">网站热心人管理后台</H1>
<table width="100%" border="2">
<th>id</th>
<th>用户ID</th>
<th>用户姓名</th>
<th>发送条数</th>
<th>接受条数</th>
<th>建立时间</th>
<th>操作</th>
<%
//显示封禁列表
if(beginnerHelpList != null){
	Iterator itr = beginnerHelpList.iterator();
	int i = 1;
	BeginnerHelpBean beginnerHelp = null;
	UserBean user = null;
	while(itr.hasNext()){
	beginnerHelp=(BeginnerHelpBean)itr.next();
		user=UserInfoUtil.getUser(beginnerHelp.getUserId());
		if(user==null)
		continue;
%>
<tr>
<td width="10%" align="center"><%=beginnerHelp.getId()%></td>
<td width="10%" align="center"><%=user.getId()%></td>
<td width="10%" align="center"><%=user.getNickName()%></td>
<td width="10%" align="center"><%=beginnerHelp.getSendCount()%></td>
<td width="10%" align="center"><%=beginnerHelp.getReceiveCount()%></td>
<td width="10%" align="center"><%=beginnerHelp.getCreateDatetime()%></td>
<td width="10%" align="center"><a href="beginnerManage.jsp?delete=<%=beginnerHelp.getId()%>">删除</a></td>
</tr>
<%    i ++;
	}
}
%>
</table>
<form name="buser" action="beginnerManage.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="id" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
</form>
<form name="nickName" action="beginnerManage.jsp" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="nickName" type="text"  size="38" >
<input type="submit" value="提交">
<input type="reset" value="重置">
</form>
<body>
</html>
