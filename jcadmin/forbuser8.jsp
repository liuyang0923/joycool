<%@ page contentType="text/html;charset=utf-8"%><%@include file="filter.jsp"%><%@ page import="net.joycool.wap.spec.farm.*" %><%@ page import="java.util.*"%><%@ page import="net.joycool.wap.service.infc.IUserService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.*,net.joycool.wap.framework.*"%><%
IUserService userService = ServiceFactory.createUserService();
CustomAction action = new CustomAction(request);
String buser=null;
//判断通过用户名添加黑名单用户
String bak = request.getParameter("bak");
String usernamee=(String)request.getParameter("username");
int interval = action.getParameterInt("interval");
String username=null;
if(usernamee!=null){
	List list = net.joycool.wap.util.StringUtil.toInts2(usernamee);
	for(int i=0;i<list.size();i++){
		int iid=((Integer)list.get(i)).intValue();
		ForbidUtil.addForbid("game",new ForbidUtil.ForbidBean(iid,0,bak),interval,adminUser.getName());
		if(request.getParameter("pun")!=null){	// 扣除现金
			UserInfoUtil.updateUserCash(iid, -2000000000,0, "扣除外挂的所有乐币");
			SqlUtil.executeUpdate("DELETE FROM jc_hunt_user_quarry WHERE user_id="+iid);	// 删除所有猎物
		}
		if(request.getParameter("pun2")!=null){	// 扣除现金
			long saving = BankCacheUtil.getStoreMoney(iid);
			if(saving > 10000000000l)
				saving = 10000000000l;
			BankCacheUtil.updateBankStoreCacheById(-saving, iid, 0 ,Constants.BANK_ADMIN_OPERATE_TYPE);
		}
		if(request.getParameter("pun3")!=null){	// 扣除桃花源金钱和扔进监狱
			SqlUtil.executeUpdate("update farm_user set pos=250,bind_pos=250,money=0 where user_id=" + iid, 4);
			FarmUserBean user = FarmWorld.one.getFarmUserCache(iid);
			if(user !=null){
				user.setPos(250);
				user.setBindPos(250);
				user.setMoney(0);
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
	    LogUtil.logOperation(adminUser.getName() + "游戏封禁用户: " + toUser.getId() + "昵称:" + toUser.getNickName());
	    //lbj_log_oper_end
			ForbidUtil.addForbid("game",new ForbidUtil.ForbidBean(toUser.getId(),0,bak),interval, adminUser.getName());
		    
		}else{
		    //response.sendRedirect("forbuser8.jsp");
		    BaseAction.sendRedirect("/jcadmin/forbuser8.jsp", response);
			return;
		}
   }
}




//判断删除黑名单用户
if(request.getParameter("delete") != null){
	int deleteId = StringUtil.toInt(request.getParameter("delete"));
    ForbidUtil.deleteForbid("game",deleteId);
	//response.sendRedirect("forbuser8.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser8.jsp", response);
	return;
}
//判断重新设定
if(request.getParameter("clear") != null){
	//response.sendRedirect("forbuser8.jsp");
	BaseAction.sendRedirect("/jcadmin/forbuser8.jsp", response);
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
function forbid(reason,interval){
	if(!interval)
		interval=8;
	buser.bak.value=reason;
	buser.pun.checked="checked";
	var r = document.getElementsByName("interval");
	r[interval].checked="checked";
	buser.submit();
}
</script>
</head>
<body>
<font color=red>封禁游戏权限列表</font>
<form name="buser" action="forbuser8.jsp" method="post" onsubmit="return checkform()" >
用户ID:<input name="username" type="text"  size="38" >
<input type=button value="外挂钓鱼" onclick="forbid('使用外挂钓鱼')">
<input type=button value="外挂打猎" onclick="forbid('使用外挂打猎')">
<input type=button value="外挂开商" onclick="forbid('使用外挂开商')">
<input type=button value="外挂加墙" onclick="forbid('使用外挂加墙')">
<input type=button value="外挂开当" onclick="forbid('使用外挂开当')"><br/>
<input name="pun" type="checkbox">扣除现金, <input name="pun2" type="checkbox">扣除存款, <input name="pun3" type="checkbox">桃花源监狱<br/>
理由:<input name="bak" type="text"  size="38" value="挂机行为">
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
<form name="nickName" action="forbuser8.jsp" method="post" onsubmit="return checkform1()" >
用户昵称:<input name="nickName" type="text"  size="38" ><br/>
理由:<input name="bak" type="text"  size="38"  value="挂机行为">
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
List forbidList = ForbidUtil.getForbidList("game");
if(forbidList != null){
	Iterator itr = forbidList.iterator();
	int i = 1;
	while(itr.hasNext()&&i<=20){
	ForbidUtil.ForbidBean forbid = (ForbidUtil.ForbidBean)itr.next();
%>
<tr>
<td width="10%"><%=i%></td>
<td><%=forbid.getValue()%></td>
<td><%=forbid.getOperator()%></td>
<td><%=forbid.getBak()%></td>
<td><%=DateUtil.formatDate2(forbid.getEndTime())%></td>
<td width="10%"><a href="forbuser8.jsp?delete=<%=forbid.getValue()%>">删除</a></td>
</tr>
<%
	    i ++;
	}
}
%>
</table>
<body>
</html>