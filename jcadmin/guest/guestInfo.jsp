<%@ page language="java" pageEncoding="utf-8"%><%@include file="../filter.jsp"%>
<%@ page import="java.util.regex.Matcher,java.util.regex.Pattern,java.util.*,jc.guest.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public static int COUNT_PRE_PAGE = 30; %>
<%GuestHallAction action = new GuestHallAction(request,response);
String tip = "";
List list = null;
int order = 0;	//查找。1:ID,2:昵称,3:手机
PagingBean paging = null;
GuestUserInfo guestUser = null;
int rid = action.getParameterInt("rid");
int re = action.getParameterInt("r");
// 重置昵称
if (rid > 0 && re == 1){
	guestUser = GuestHallAction.getGuestUser(rid);
	if (guestUser != null){
		guestUser.setUserName(String.valueOf(guestUser.getId()));
		SqlUtil.executeUpdate("update guest_user_info set user_name='" + guestUser.getUserName() + "' where id=" + guestUser.getId(),6);
	}
}
order = action.getParameterInt("o");
paging = new PagingBean(action,100000,COUNT_PRE_PAGE,"p");
//int del = action.getParameterInt("del");
//if (del > 0){
//// 删除
//	guestUser = GuestHallAction.service.getUserInfo(" id=" + del);
//	if (guestUser != null){
//		// 从数据库中删除
//		SqlUtil.executeUpdate("delete from guest_user_info where id=" + del,6);
//		// 从缓存中删除
//		GuestHallAction.onlineGuestCache.srm(guestUser.getId());
//		// 从关注列表里删除
//		SqlUtil.executeUpdate("delete from user_focus where left_uid=" + del + " or right_uid=" + del,6);
//		// 删除他发送的未读消息
//		SqlUtil.executeUpdate("delete from focus_msg where readed = 0 and (left_uid=" + del + " or right_uid=" + del + ")",6);
//	}
//}
switch (order){
case 0:{	// 显示列表
	list = GuestHallAction.service.getUserList("1 order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
	break;
}
case 1:{	// 按ID查找
	int id = action.getParameterInt("uid");
	if (id <= 0){
		tip = "ID输入错误.<a href=\"guestInfo.jsp\">返回列表</a>";
	} else {
		list = GuestHallAction.service.getUserList(" id=" + id + " limit 1");
		if (list == null || list.size() == 0){
			tip = "没有找到此ID的用户.<a href=\"guestInfo.jsp\">返回列表</a>";
		} else {
			tip = "<a href=\"guestInfo.jsp\">返回列表</a>";
		}
	}
	break;
}
case 2:{	// 按昵称查找
	String userName = action.getParameterNoEnter("un");
	if (userName == null || "".equals(userName)){
		tip = "没有输入昵称.<a href=\"guestInfo.jsp\">返回列表</a>";
	} else {
		list = GuestHallAction.service.getUserList(" user_name='" + userName + "' limit 1");
		if (list == null || list.size() == 0){
			tip = "没有找到此昵称的用户.<a href=\"guestInfo.jsp\">返回列表</a>";
		}  else {
			tip = "<a href=\"guestInfo.jsp\">返回列表</a>";
		}
	}
	break;
}
case 3:{	// 按手机查找
	String mobile = action.getParameterNoEnter("um");
	if (mobile == null || "".equals(mobile)){
		tip = "没有找到此绑定手机的用户.<a href=\"guestInfo.jsp\">返回列表</a>";
	} else {
		Pattern pattern = Pattern.compile("(1\\d{10})");
		Matcher isPhone = pattern.matcher(mobile);
		if (!isPhone.matches()){
			tip = "手机号输入错误.<a href=\"guestInfo.jsp\">返回列表</a>";
		} else {
			list = GuestHallAction.service.getUserList(" mobile='" + mobile + "' limit 1");
			if (list == null || list.size() == 0){
				tip = "没有找到绑定此手机的用户.<a href=\"guestInfo.jsp\">返回列表</a>";
			} else {
				tip = "<a href=\"guestInfo.jsp\">返回列表</a>";
			}
		} 
	}
	break;
}
}
%>
<html>
	<head>
		<title>游客大厅用户</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<a href="pt/index.jsp">拼图后台</a><br/>
<%=tip%><br/>
<form action="guestInfo.jsp?o=1" method="post">
输入用户UID:<input type="text" name="uid"/><input type="submit" value="确定" />
</form>
<form action="guestInfo.jsp?o=2" method="post">
输入用户昵称:<input type="text" name="un"/><input type="submit" value="确定" />
</form>
<form action="guestInfo.jsp?o=3" method="post">
输入用户手机:<input type="text" name="um"/><input type="submit" value="确定" />
</form>
<table border=1 width=100% align=center>
	<tr bgcolor=#C6EAF5>
		<td align=center>Uid</td>
		<td align=center>用户昵称</td>
		<td align=center>绑定用户</td>
		<%if(group.isFlag(1)){%><td align=center>密码</td><%}%>
		<td align=center>创建时间</td>
		<td align=center>绑定手机</td>
		<td align=center>操作</td>
	</tr>
<%if (list != null && list.size() > 0){
	for(int i = 0 ; i < list.size() ; i++){
		guestUser = (GuestUserInfo)list.get(i);
		if (guestUser != null){
			%><tr>
		    	<td><%=guestUser.getId()%></td>
		    	<td><%=guestUser.getUserNameWml()%></td>
		    	<td><%=guestUser.getBuid()%></td>
		    	<%if(group.isFlag(1)){%><td><%=guestUser.getPassword()%></td><%}%>
		    	<td><%=DateUtil.formatSqlDatetime(guestUser.getCreateTime())%></td>
		    	<td><%=guestUser.getMobile()%></td>
		    	<td align="center"><a href="guestInfo2.jsp?id=<%=guestUser.getId()%>">修改</a>|<a href="guestInfo.jsp?rid=<%=guestUser.getId()%>&r=1" onClick="return confirm('确定要重置昵称?')">重置</a></td>
		    </tr><%
		}
	}
}%>
</table>
<%=paging.shuzifenye("guestInfo.jsp", false, "|", response)%>
	</body>
</html>
