<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.db.*"%><%@ page import="net.joycool.wap.service.infc.IFriendService"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="java.util.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.PageUtil"%><%@ page import="net.joycool.wap.util.StringUtil"%><%@ page import="net.joycool.wap.framework.*"%><%response.setHeader("Cache-Control", "no-cache");%>
<%if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}
			int ITEM_PER_PAGE = 100;
			int totalPageCount = 0;
			int pageIndex = 0;
			String pageIndexs = (String) request.getParameter("pageIndex");
			if (pageIndexs != null)
				pageIndex = StringUtil.toInt(pageIndexs);
			else
				pageIndex = 0;
			if (pageIndex < 0)
				pageIndex = 0;
			int itemCount = 0;
			int j = 0;
			String prefixUrl = "jtStat.jsp";
			DbOperation dbOp = null;
			ResultSet rs = null;%>
<html>
	<head>
	</head>
	<body>
		用户普遍友好度水平及增长幅度
		<BR>
		<table width="800" border="1">
			<th align="center" width="40">
				排名
			</th>
			<th align="center">
				结义次数
			</th>
			<th align="center">
				割袍断义次数
			</th>
			<th align="center">
				时间
			</th>
			<%try {
				dbOp = new DbOperation();
				dbOp.init();
				rs = dbOp
						.executeQuery("select count(*) count from jc_admin_friend  ");
				if (rs.next())
					itemCount = rs.getInt("count");

				totalPageCount = PageUtil
						.getPageCount(ITEM_PER_PAGE, itemCount);
				pageIndex = PageUtil.getPageIndex(pageIndex, totalPageCount);
				if (pageIndex < 0)
					pageIndex = 0;
				rs = dbOp
						.executeQuery("select id ,jy_count,break_jy_count ,create_datetime from jc_admin_friend order by create_datetime desc limit "
								+ pageIndex
								* ITEM_PER_PAGE
								+ ","
								+ ITEM_PER_PAGE);

				%>
			<%while (rs.next()) {

					%>
			<tr>
				<td align="center">
					<%=(j + 1)%>
				</td>
				<td align="center">
					<%=rs.getInt("jy_count")%>
				</td>
				<td align="center">
					<%=rs.getInt("break_jy_count")%>
				</td>
				<td align="center">
					<%=rs.getString("create_datetime")%>
				</td>
				<%j++;
				}
			} catch (SQLException ex) {
				dbOp.release();
			}
			dbOp.release();

			%>
		</table>
		<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex,
					prefixUrl, false, "|", response);
			if (!"".equals(fenye)) {%>
		<%=fenye%>
		<br />
		<%}

		%>
		<br />
		<a href="http://wap.joycool.net/jcadmin/friend/index.jsp">返回交友中心数据分析首页</a>
		<br />
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
