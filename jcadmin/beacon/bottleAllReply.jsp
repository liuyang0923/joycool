<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%>
<%!public final int COUNT_PRE_PAGE = 20;
	public static BottleService service = new BottleService();%>
<html>
	<head>
		<title>所有漂流瓶回复</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			BottleAction action = new BottleAction(request);
		%>
		<table width="100%" style="border: none;">
			<tr>
				<td width=20% align=right style="border: none;">
					<form method="post" action="bottle.jsp?f=all">
						<input type="submit" value="查看所有瓶子">
					</form>
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr width=100% bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>瓶子ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>回复时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>回复内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<%
				//查找所有瓶子
				int totalCount =100000;// SqlUtil.getIntResult("select count(id) from bottle_viewer",5);
				PagingBean paging = new PagingBean(action, totalCount,
						COUNT_PRE_PAGE, "p");
				int pageNow = paging.getCurrentPageIndex();
				ArrayList list = service.getReplyList("order by id desc limit " + pageNow*COUNT_PRE_PAGE + "," + COUNT_PRE_PAGE);
				BottleReply br = null;
				for (int i = 0; i < list.size(); i++) {
					br = (BottleReply) list.get(i);
					UserBean user = UserInfoUtil.getUser(br.getUserId());
					if (br != null) {
			%><tr>
				<td><%=br.getId()%></td>
				<td><a href="bottle.jsp?f=findById&id=<%=br.getBottleId()%>"><%=br.getBottleId()%></a></td>
				<td><a href="../user/queryUserInfo.jsp?id=<%=br.getUserId() %>"><%=user.getNickNameWml()%></a></td>
				<td><%=DateUtil.formatSqlDatetime(br.getViewTime())%></td>
				<td><%=StringUtil.toWml(br.getReply())%></td>
				<td><a href="bottleOperate.jsp?f=allReply&id=<%=br.getId() %>" onclick="return confirm('确定删除？')">删</a></td>
			</tr>
			<%
				}
				}
			%>
		</table>
		<%=paging.shuzifenye("bottleAllReply.jsp?" , true, "|",response)%>
	</body>
	<table width=100% style="border: none;">
		<tr align=right>
			<td style="border: none;">
				<%-- <a href="bottle.jsp?f=all">返回</a> --%>
			</td>
		</tr>
	</table>
</html>