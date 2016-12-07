<%@ page language="java" pageEncoding="utf-8"%>
<%@ page
	import="java.util.*,net.joycool.wap.spec.bottle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*,net.joycool.wap.bean.*"%>
<%!public final int COUNT_PRE_PAGE = 20;
	public static BottleService service = new BottleService();%>
<html>
	<head>
		<title>漂流瓶管理</title>
	</head>
	<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		<%
			BottleAction action = new BottleAction(request);
			String f = action.getParameterString("f");
			int id = action.getParameterInt("id");

			BottleBean bb = null;
			ArrayList list = null;
			PagingBean paging = null;
			String pageUrl = "";

			if ("findById".equals(f)) {
				//查找一个瓶子
				bb = service.getBottleById(id);
				//瓶子不存在的判断
				if (bb==null){
					action.sendRedirect("bottle.jsp",response);
					return;
				}else{
					int totalCount = 1;
					paging = new PagingBean(action, totalCount, COUNT_PRE_PAGE, "p");
	
					list = new ArrayList();
					list.add(bb);
					pageUrl = "bottle.jsp?id=0";
				}
			} else if ("findByUserId".equals(f)) {
				//根据用户ID查找其所有瓶子
				int totalCount = SqlUtil.getIntResult(
						"select count(*) from bottle_info where user_id=" + id,
						5);
				paging = new PagingBean(action, totalCount, COUNT_PRE_PAGE, "p");
				int pageNow = paging.getCurrentPageIndex();
				list = service.getBottleByUidp(id, pageNow, COUNT_PRE_PAGE);
				pageUrl = "bottle.jsp?f=findByUserId&id=" + id;
			} else if("toAllReply".equals(f)){
				action.sendRedirect("bottleAllReply.jsp",response);
				return;
			}else {
				//所有瓶子
				int totalCount = SqlUtil.getIntResult(
						"select count(*) from bottle_info", 5);
				paging = new PagingBean(action, totalCount, COUNT_PRE_PAGE, "p");
				int pageNow = paging.getCurrentPageIndex();
				list = service.getBottles(pageNow, COUNT_PRE_PAGE);
				pageUrl = "bottle.jsp?id=0";
			}
		%>
		<table width="100%" style="border: none;">
			<tr>
				<td style="border: none;">
					<form method="post" action="bottle.jsp?f=findById">
						瓶子ID：
						<input type="text" name="id">
						<input type="submit" value="查找">
					</form>
				</td>
			</tr>
			<tr>
				<td width=80% style="border: none;">
					<form method="post" action="bottle.jsp?f=findByUserId">
						用户ID：
						<input type="text" name="id">
						<input type="submit" value="查找">
					</form>
				</td>
				<td width=20% align=right style="border: none;">
					<form method="post" action="bottle.jsp?f=toAllReply">
						<input type="submit" value="查看所有回复">
					</form>
				</td>
				<td width=20% align=right style="border: none;">
					<form method="post" action="bottle.jsp?f=all">
						<input type="submit" value="查看所有瓶子">
					</form>
				</td>
			</tr>
		</table>
		<table border=1 width=100% align=center>
			<tr bgcolor=#C6EAF5>
				<td align=center>
					<font color=#1A4578>ID</font>
				</td>
				<td align=center>
					<font color=#1A4578>用户</font>
				</td>
				<td align=center>
					<font color=#1A4578>标签</font>
				</td>
				<td align=center>
					<font color=#1A4578>内容</font>
				</td>
				<td align=center>
					<font color=#1A4578>心情</font>
				</td>
				<td align=center>
					<font color=#1A4578>创建时间</font>
				</td>
				<td align=center>
					<font color=#1A4578>回复</font>
				</td>
				<td align=center>
					<font color=#1A4578>状态</font>
				</td>
				<td align=center>
					<font color=#1A4578>类型</font>
				</td>				
				<td align=center>
					<font color=#1A4578>操作</font>
				</td>
			</tr>
			<%
				for (int i = 0; i < list.size(); i++) {
					bb = (BottleBean) list.get(i);
					UserBean user = UserInfoUtil.getUser(bb.getUserId());
					if (bb != null) {
			%><tr>
				<td><%=bb.getId()%></td>
				<td><a href="../user/queryUserInfo.jsp?id=<%=bb.getUserId() %>"><%=user.getNickNameWml()%></a></td>
				<td><%=StringUtil.toWml(bb.getTitle())%></td>
				<td><%=StringUtil.toWml(bb.getContent())%></td>
				<td><%=StringUtil.toWml(bb.getMood())%></td>
				<td><%=DateUtil.formatSqlDatetime(bb.getCreateTime())%></td>
				<%
					if (bb.getReplyCount() != 0) {
				%><td>
					<font color=blue><%=bb.getReplyCount()%></font>
				</td>
				<%
					} else {
				%><td>
					<font color=gray><%=bb.getReplyCount()%></font>
				</td>
				<%
					}
				%><td><%=bb.getStatus()%></td>
				<td><%=bb.getType()%></td>
				<td><a href="bottleOperate.jsp?f=bottle&id=<%=bb.getId() %>" onclick="return confirm('确定删除？')">删</a>&nbsp;&nbsp;
					<a href="bottleReply.jsp?id=<%=bb.getId()%>">查回复</a>
				</td>
			</tr>
			<%
				}
				}
			%>
		</table>
		<%=paging.shuzifenye(pageUrl, true, "|", response)%>
	</body>
</html>