<%@ page contentType="text/html;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.cache.util.*"%><%@ page import="net.joycool.wap.bean.jcforum.*"%><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl"%><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%
IUserService userService = ServiceFactory.createUserService();
CustomAction action = new CustomAction(request);
JcForumServiceImpl service = new JcForumServiceImpl();
String buser=null;
//判断通过用户名添加黑名单用户
String bak = request.getParameter("bak");
String usernamee=(String)request.getParameter("username");
int interval = action.getParameterInt("interval");
String username=null;
if(usernamee!=null){
	List list = net.joycool.wap.util.StringUtil.toInts2(usernamee);
	int dd=0;
	if(request.getParameter("d3")!=null) dd=3;
	if(request.getParameter("d7")!=null) dd=7;
	if(request.getParameter("d15")!=null) dd=15;
	for(int i=0;i<list.size();i++){
		int iid=((Integer)list.get(i)).intValue();
		ForbidUtil.addForbid("forum",new ForbidUtil.ForbidBean(iid,0,bak),interval,adminUser.getName());
		if(dd>0){
			List contentList=service.getForumContentList("del_mark=0 and create_datetime>date_add(now(),interval -"+dd+" day) and user_id="+iid);
			for(int j=0;j<contentList.size();j++){
				ForumContentBean cons=(ForumContentBean)contentList.get(j);
				ForumBean forum = ForumCacheUtil.getForumCache(cons.getForumId());
				ForumCacheUtil.deleteForumContent(cons, forum, 100);
			}
			ForumUserBean fu = ForumCacheUtil.getForumUser(iid);
			if(fu != null) {
				fu.decExp(5*contentList.size());
				fu.setThreadCount(fu.getThreadCount() - contentList.size());
				if(fu.getThreadCount() < 0)
					fu.setThreadCount(0);
				ForumCacheUtil.updateForumUser(fu);
			}
		}
	}
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
	    LogUtil.logOperation(adminUser.getName() + "论坛封禁用户: " + toUser.getId() + "昵称:" + toUser.getNickName());
	    //lbj_log_oper_end
			ForbidUtil.addForbid("forum",new ForbidUtil.ForbidBean(toUser.getId(),0,bak),interval, adminUser.getName());
		    
		}else{
		    //response.sendRedirect("forbuser3.jsp");
		    BaseAction.sendRedirect("/jcadmin/forbuser3.jsp", response);
			return;
		}
   }
}




//判断删除黑名单用户
if(request.getParameter("delete") != null){
	int deleteId = StringUtil.toInt(request.getParameter("delete"));
    ForbidUtil.deleteForbid("forum",deleteId);
	//response.sendRedirect("forbuser3.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser3.jsp", response);
	return;
}
//判断重新设定
if(request.getParameter("clear") != null){
	//response.sendRedirect("forbuser3.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser3.jsp", response);
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
<font color=red>封禁发贴列表</font>
<form name="buser" action="forbuser3.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="username" type="text"  size="38" >
删除该用户最近
<input name="d3" type="checkbox"><font color=red>3天</font>
<input name="d7" type="checkbox"><font color=red>7天</font>
<input name="d15" type="checkbox"><font color=red>15天</font>
的帖子
<br/>
理由:<input name="bak" type="text"  size="38" value="发贴不符合规范">
<input name=interval type=radio value="30" >30分钟
<input name=interval type=radio value="60" checked>1小时
<input name=interval type=radio value="1440" >1天
<input name=interval type=radio value="4320" >3天
<input name=interval type=radio value="14400" >10天
<input name=interval type=radio value="43200" >1月
<input name=interval type=radio value="129600" >3月
<input name=interval type=radio value="518400" >1年
<input name=interval type=radio value="1555200" >3年
<input type="submit" value="添加封禁">
</form>
<form name="nickName" action="forbuser3.jsp" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="nickName" type="text"  size="38" ><br/>
理由:<input name="bak" type="text"  size="38"  value="发贴不符合规范">
<input name=interval type=radio value="30" >30分钟
<input name=interval type=radio value="60" checked>1小时
<input name=interval type=radio value="1440" >1天
<input name=interval type=radio value="4320" >3天
<input name=interval type=radio value="14400" >10天
<input name=interval type=radio value="43200" >1月
<input name=interval type=radio value="129600" >3月
<input name=interval type=radio value="518400" >1年
<input name=interval type=radio value="1555200" >3年
<input type="submit" value="添加封禁">
</form>
<table width="100%" border="2">
<%
//显示封禁列表
List forbidList = ForbidUtil.getForbidList("forum");
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
<td width="10%"><a href="forbuser3.jsp?delete=<%=forbid.getValue()%>">删除</a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<body>
</html>