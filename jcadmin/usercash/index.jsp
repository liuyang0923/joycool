<%@ page contentType="text/html;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%@ page import="net.joycool.wap.action.jcadmin.UserCashAction"%><%@ page import="net.joycool.wap.bean.jcadmin.UserCashBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="java.util.Vector"%><%
response.setHeader("Cache-Control", "no-cache");

//liuyi 2006-11-13 关闭现金流查询功能 start
if(true)return;
//liuyi 2006-11-13 关闭现金流查询功能 end

			Vector vec = null;
			int type = 0;
			int uId = 0;
			UserCashAction action = new UserCashAction(request);
			if (null != request.getParameter("search")) {

				uId = StringUtil.toInt(request.getParameter("uid"));
				vec = action.getUserCash(uId);

			}
			if (null != request.getParameter("time")) {
				uId = StringUtil.toInt(request.getParameter("uid"));
				vec = action.getUserCash(uId);
				//uId=StringUtil.toInt(request.getParameter("uid"));
			}
			if (null != request.getParameter("type")) {
				uId = StringUtil.toInt(request.getParameter("uid"));
				type = StringUtil.toInt(request.getParameter("type"));
				vec = action.getUserCash(uId, type);
				//uId=StringUtil.toInt(request.getParameter("uid"));

			}
%>
<html>
	<head>
	</head>
	<body>

		<form method="post" action="index.jsp?search=1">
			用户ID：
			<input id="uid" name="uid" />
			<input type="submit" id="search" name="search" value="查询" />
			<br />
		</form>

		<%if (vec != null) {
%>
		用户
		<%=uId%>
		的查询结果如下
		<br />
		<a href="index.jsp?time=1&amp;uid=<%=uId%>">按照日期 </a>
		<br />
		按照项目：

		<a href="index.jsp?type=2&amp;uid=<%=uId%>">赌博 </a>
		<a href="index.jsp?type=1&amp;uid=<%=uId%>">游戏</a>
		<a href="index.jsp?type=3&amp;uid=<%=uId%>">银行</a>
		<a href="index.jsp?type=4&amp;uid=<%=uId%>">赠送</a>
		<a href="index.jsp?type=5&amp;uid=<%=uId%>">机会卡 </a>
		<a href="index.jsp?type=6&amp;uid=<%=uId%>">打猎</a>
		<a href="index.jsp?type=0&amp;uid=<%=uId%>">其他 </a>
		<br />
		<table width="800" border="1">
		<th align="center">序号</th>
<th align="center">时间</th>
<th align="center">原因</th>

			<%for (int i = 0; i < vec.size(); i++) {
					UserCashBean userCashBean = (UserCashBean) vec.get(i);
%>
			<tr>
				<td>
					<%=i + 1%>
				</td>
				<td>
					<%=userCashBean.getCreateDatetime()%>
				</td>
				<td>
					<%=userCashBean.getReason()%>
				
				</td>
			</tr>
			<%}%>

		</table>
		<%}

		%>
		<a href="http://wap.joycool.net/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>
