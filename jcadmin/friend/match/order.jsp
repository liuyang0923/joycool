<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
String prices = "";
List list = null;
MatchOrder order = null;
PagingBean paging = null;
int uid = action.getParameterInt("uid");
UserBean user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在.";
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getOrderList(" 1 order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
} else {
	tip = user.getNickNameWml() + "(" + uid + ")";
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getOrderList(" user_id=" + uid + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}%>
<html>
	<head>
		<title>订单</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<%=tip%><br/>
		<form action="order.jsp" method="post">
			请输入UID:<input type="text" name="uid" />
			<input type="submit" value="提交" />
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户手机号</font>
				</td>
				<td align=center>
					<font color=#1A4578>姓名</font>
				</td>
				<td align=center>
					<font color=#1A4578>邮件地址</font>
				</td>
				<td align=center>
					<font color=#1A4578>商品名称</font>
				</td>
				<td align=center>
					<font color=#1A4578>商品编号</font>
				</td>
				<td align=center>
					<font color=#1A4578>购货时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>发货时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>积分价格</font>
				</td>
				<td align=center>
					<font color=#1A4578>实扣价格</font>
				</td>
			</tr>
			<%if (list != null && list.size() > 0){ 
				for (int i = 0 ; i < list.size() ; i++){
					order = (MatchOrder)list.get(i);
					if (order != null){
						%><tr>
							<td><%=order.getId()%></td>
							<td><%=order.getUserId()%></td>
							<td><%=order.getPhone()%></td>
							<td><%=order.getUserNameWml() %></td>
							<td><%=order.getAddressWml()%></td>
							<td><%=order.getGoodNameWml()%></td>
							<td><%=order.getGoodId()%></td>
							<td><%=DateUtil.formatSqlDatetime(order.getBuyTime())%></td>
							<td><%=DateUtil.formatSqlDatetime(order.getSendTime())%></td>						
							<td><%=order.getPrice()%></td>
							<td><%=order.getActualPrice()%></td>
						  </tr><%
					}
				}
			}%>
		</table>
		<%=paging!=null?paging.shuzifenye("order.jsp?uid=" + uid,true,"|",response):""%>
	</body>
</html>