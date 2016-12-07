<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="java.util.Vector"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.action.stock2.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.bean.stock2.StockNoticeBean" %><%response.setHeader("Cache-Control", "no-cache");			
int ITEM_PER_PAGE = 5;
			int totalPageCount = 0;
			int pageIndex = 0;
			String pageIndexs = (String) request.getParameter("pageIndex");
			if (pageIndexs != null)
				pageIndex = StringUtil.toInt(pageIndexs);
			else
				pageIndex=0;
			if(pageIndex<0)
				pageIndex=0;
			int itemCount = 0;
			int j = 0;
			String prefixUrl = "stockNotice.jsp";

			StockService service = new StockService();
			DbOperation dbOp = new DbOperation();
			dbOp.init();
			ResultSet rs = dbOp
					.executeQuery("select count(*) count from stock_notice  ");
			if (rs.next())
				itemCount = rs.getInt("count");
			dbOp.release();
			totalPageCount = PageUtil.getPageCount(ITEM_PER_PAGE, itemCount);
			pageIndex = PageUtil.getPageIndex(pageIndex, totalPageCount);
			if(pageIndex<0)
			pageIndex=0;

			if (null != request.getParameter("delete")) {
				int id = StringUtil.toInt(request.getParameter("delete"));
				dbOp = new DbOperation();
				dbOp.init();
				dbOp
						.executeUpdate("delete from stock_notice where id="
								+ id);
				dbOp.release();
			}

			%>
<%if (null != request.getParameter("add")) {
				String name = null;
				name = request.getParameter("name").trim();
				String content = request.getParameter("content").trim();
				if (!name.equals("")) {
					StockNoticeBean notice = null;
					notice = service.getStockNotice("title='" + name + "'");
					if (notice == null) {
						notice = new StockNoticeBean();
						notice.setTitle(name);
						notice.setContent(content);
						service.addStockNotice(notice);
					} else {%>
<script>
			alert("该公告已存在！");
			</script>
<%}
				} else {%>
<script>
			alert("请填写正确各项参数！");
			</script>
<%}
			}
			Vector vec = service
					.getStockNoticeList(" 1=1 order by id desc limit "
							+ pageIndex * ITEM_PER_PAGE + "," + ITEM_PER_PAGE);
			StockNoticeBean notice = null;

			%>
<html>
	<head>
	</head>
	<body>
		股票公告后台
		<br />
		<br />
		<form method="post" action="stockNotice.jsp?add=1">
			公告名称：
			<input id="name" name="name"><BR>
			内容：
			 <textarea name="content" cols="120" rows="5"></textarea>
			<input type="submit" id="add" name="add" value="增加">
			<br />
		</form>


		<table width="800" border="1">
			<tr>
				<td>
					编号
				</td>
				<td>
					名称
				</td>
				<td>
					内容
				</td>
			</tr>
			<%for (int i = 0; i < vec.size(); i++) {
				notice = (StockNoticeBean) vec.get(i);
%>
			<tr>
				<form method="post" action="stockNotice.jsp?delete=<%=notice.getId()%>">
				<td>
					<%=i + 1%>
				</td>
				<td>
					<%=notice.getTitle()%>
					<input type="hidden" id="id" name="id" value="<%=notice.getId() %>">
				</td>
				<td>
					<%=notice.getContent()%>
				</td>
				<td>
					<a href="stockNotice.jsp?delete=<%=notice.getId()%>">删除</a>&nbsp;&nbsp;
				</td>
			</tr>
			</form>
			<%}%>
			<tr>
		</table>
		<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex,
					prefixUrl, false, "|", response);
			if (!"".equals(fenye)) {%>
		<%=fenye%>
		<br />
		<%}

		%><br/><a href="new.jsp">新股发行</a><br/><br/>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
