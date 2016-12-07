<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="net.joycool.wap.action.NoticeAction,jc.credit.UserInfo,jc.credit.CreditAction,jc.match.*,java.util.*,net.joycool.wap.spec.friend.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*" %>
<%! public final int COUNT_PRE_PAGE=20; %>
<%
MatchAction action = new MatchAction(request);
String tip = "";
String prices = "";
List list = null;
MatchBuylog log = null;
PagingBean paging = null;
int uid = action.getParameterInt("uid");
UserBean user = UserInfoUtil.getUser(uid);
if (user == null){
	tip = "用户不存在.";
} else {
	tip = user.getNickNameWml() + "(" + uid + ")";
	paging = new PagingBean(action,1000,COUNT_PRE_PAGE,"p");
	list = MatchAction.service.getBuyLogList(" user_id=" + uid + " order by id desc limit " + paging.getCurrentPageIndex() * COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
}%>
<%@page import="java.io.File"%><html>
	<head>
		<title>用户购买记录</title>
	</head>
	<link href="../../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<a href="index.jsp">回首页</a><br/>
		<%=tip%><br/>
		<form action="buylog.jsp" method="post">
			请输入UID:<input type="text" name="uid" />
			<input type="submit" value="提交" />
		</form>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>物品ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>物品名称</font>
				</td>
				<td align=center>
					<font color=#1A4578>总数</font>
				</td>
				<td align=center>
					<font color=#1A4578>总价</font>
				</td>
				<td align=center>
					<font color=#1A4578>币种</font>
				</td>
				<td align=center>
					<font color=#1A4578>购买时间</font>
				</td>
			</tr>
			<%if (list != null && list.size() > 0){ 
				for (int i = 0 ; i < list.size() ; i++){
					log = (MatchBuylog)list.get(i);
					if (log != null){
						if (log.getCur() == 0){
							prices = StringUtil.bigNumberFormat2(log.getPrices());
						} else {
							prices = (log.getPrices() / 100f) + "";
						}
						%><tr>
							<td><%=log.getId()%></td>
							<td><%=log.getResId()%></td>
							<td><%=log.getResName()%></td>
							<td><%=log.getCount()%></td>						
							<td><%=prices%></td>
							<td><%=log.getCur()==0?"乐币":"酷币"%></td>
							<td><%=DateUtil.formatSqlDatetime(log.getBuyTime())%></td>
						  </tr><%
					}
				}
			}%>
		</table>
		<%=paging!=null?paging.shuzifenye("buylog.jsp?uid=" + uid,true,"|",response):""%>
	</body>
</html>