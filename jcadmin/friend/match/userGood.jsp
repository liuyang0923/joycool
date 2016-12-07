<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
String prices = "";
List list = null;
MatchExch matchExch = null;
PagingBean paging = null;

int uid = action.getParameterInt("uid");
UserBean user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在.";
} else {
	tip = user.getNickNameWml() + "(" + uid + ")";
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getExchList(" user_id=" + uid + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}%>
<html>
	<head>
		<title>用户购买记录</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a>|<a href="good.jsp">回上页</a><br/>
		<%=tip%><br/>
		<form action="userGood.jsp" method="post">
			请输入UID:<input type="text" name="uid" />
			<input type="submit" value="提交" />
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>商品</font>
				</td>
				<td align=center>
					<font color=#1A4578>购买时间</font>
				</td>
			</tr>
			<%if (list != null && list.size() > 0){ 
				for (int i = 0 ; i < list.size() ; i++){
					matchExch = (MatchExch)list.get(i);
					if (matchExch != null){
						%><tr>
							<td><%=matchExch.getId()%></td>
							<td><%=UserInfoUtil.getUser(matchExch.getUserId()).getNickNameWml()%>(<%=matchExch.getUserId()%>)</td>
							<td><%=matchExch.getGoodId()%></td>
							<td><%=DateUtil.formatSqlDatetime(matchExch.getBuyTime())%></td>						
						  </tr><%
					}
				}
			}%>
		</table>
		<%=paging!=null?paging.shuzifenye("userGood.jsp?uid=" + uid,true,"|",response):""%>
	</body>
</html>