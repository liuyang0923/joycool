<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*,net.joycool.wap.bean.friend.FriendBean,net.joycool.wap.cache.ICacheMap,net.joycool.wap.cache.CacheManage" %>
<%! static int COUNT_PRE_PAGE=30;
	net.joycool.wap.service.impl.FriendServiceImpl service = new net.joycool.wap.service.impl.FriendServiceImpl();%>
<%CustomAction action = new CustomAction(request);
UserBean user = null;
FriendBean friend = null;
List list = null;
int uid = action.getParameterInt("uid");
int change = action.getParameterInt("c");
PagingBean paging = new PagingBean(action,100000000,COUNT_PRE_PAGE,"p");
if (change == 1){
	// 修改
	SqlUtil.executeUpdate("update jc_friend set friend_condition='我要交友' where user_id=" + uid,0);
	List cacheList = CacheManage.getCacheList();
	ICacheMap cache = (ICacheMap)cacheList.get(57);
	cache.srm(uid);
	uid=0;
}
if (uid > 0){
	// 按UID查找
	list = service.getFriendList(" user_id=" + uid);
} else {
	// 全部
	list = service.getFriendList("1 limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}
%>
<html>
	<head>
		<title>交友信息</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
	<form action="cond.jsp" method="post">
		UID：<input type="text" name="uid" />
		<input type="submit" value="查找" />
		<input id="cmd" type="button" value="返回列表" onClick="javascript:window.location.href='cond.jsp'" />
	</form>
	<table border=1 width=100% align=center>
		<tr bgcolor=#C6EAF5>
			<td align=center><font color=#1A4578>ID</font></td>
			<td align=center><font color=#1A4578>UID</font></td>
			<td align=center><font color=#1A4578>用户名</font></td>
			<td align=center><font color=#1A4578>交友条件</font></td>
			<td align=center><font color=#1A4578>修改</font></td>
		</tr>
	<%if (list != null && list.size() > 0){
		for (int i = 0 ; i < list.size() ; i++){
			friend = (FriendBean)list.get(i);
			if (friend != null){
				user = UserInfoUtil.getUser(friend.getUserId());
				if (user != null){
					%><tr>
						<td><%=i+paging.getStartIndex()+1%></td>
						<td><%=user.getId()%></td>
						<td><%=user.getNickNameWml()%></td>
						<td><%=StringUtil.toWml(friend.getFriendCondition())%></td>
						<td><a href="cond.jsp?c=1&uid=<%=user.getId()%>" onClick="return confirm('真的要修改吗?')">改</a></td>
					</tr><%
				}
			}
		}
	}%>
	</table>
	<%=paging.shuzifenye("cond.jsp", false, "|", response)%>
	</body>
</html>