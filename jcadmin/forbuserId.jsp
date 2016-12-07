<%@ page contentType="text/html;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.BlackListUserBean" %><%
IUserService userService = ServiceFactory.createUserService();
String buser=null;
//判断通过用户名添加黑名单用户
if(request.getParameter("userId")!=null){
	List list = net.joycool.wap.util.StringUtil.toInts2(request.getParameter("userId"));
	for(int i=0;i<list.size();i++){
		int userId=((Integer)list.get(i)).intValue();
		
		if(!SecurityUtil.getForbidUserIdMap().containsKey(new Integer(userId))){
			UserBean user = UserInfoUtil.getUser(userId);
			if(user!=null){
				BlackListUserBean bean = new BlackListUserBean();
				bean.setUserId(userId);
				if(userService.addBlackListUser(bean)){
					bean=userService.getBlackListUser(" user_id="+userId);
					if(bean!=null){
						SecurityUtil.getForbidUserIdMap().put(new Integer(userId),bean);
						LogUtil.logOperation(adminUser.getName() + "全站封禁用户: " + bean.getUserId());
					}
				}
			}
		}
	}

   BaseAction.sendRedirect("/jcadmin/forbuserId.jsp", response);
   return;
}

//判断删除黑名单用户
if(request.getParameter("delete") != null){
	int deleteId = StringUtil.toInt(request.getParameter("delete"));
	userService.deleteBlackListUser(" user_id="+deleteId);
	SecurityUtil.getForbidUserIdMap().remove(new Integer(deleteId));
	BaseAction.sendRedirect("/jcadmin/forbuserId.jsp", response);
	return;
}
%>
<html>
<head>
 <script language="javascript" >
function checkform(){
			if(document.buser.userId.value == ''){
				alert("用户id不能为空！");
				return false;
			}else{
				  return true;
				 }
		}
</script>
</head>
<body>
<form name="buser" action="forbuserId.jsp" method="post" onsubmit="return checkform()" >
用户Id:<input name="userId" type="text"  size="38" >
<input type="submit" value="确认封禁">
</form>
<form name="duser" action="forbuserId.jsp" method="get">
用户Id:<input name="delete" type="text"  size="38" >
<input type="submit" value="确认解除封禁">
</form>
<p>按用户ID封禁列表</p>
<table width="100%" border="2">
<tr>
<td width="30">ID</td>
<td width="80">用户id</td>
<td>昵称</td>
<td width="160">记录时间</td>
<td width="40">操作</td>
</tr>
<%
//显示封禁列表
List list = userService.getBlackListUserList(" 1 order by id desc limit 30");
HashMap buserIdMap = SecurityUtil.getForbidUserIdMap();
Iterator itr = list.iterator();
int i = 1;
while(itr.hasNext()){
	BlackListUserBean bean = (BlackListUserBean)itr.next();
	int bUserId = bean.getUserId();
	UserBean user = UserInfoUtil.getUser(bUserId);
	if(user==null){continue;}
%>
<tr>
<td width="30"><%=bean.getId()%></td>
<td width="80"><%=bUserId%></td>
<td><%=user.getNickName()%></td>
<td width="160"><%=bean.getCreateDatetime()%></td>
<td width="40"><a href="forbuserId.jsp?delete=<%=bUserId%>">删除</a></td>
</tr>
<%i ++;
}
%>
</table>

<body>
</html>