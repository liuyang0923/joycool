<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.tong.*,net.joycool.wap.cache.util.*,java.util.*"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.util.ForumCacheUtil"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.impl.JcForumServiceImpl" %><%response.setHeader("Cache-Control", "no-cache");
			if (session.getAttribute("adminLogin") == null) {
				//response.sendRedirect("../login.jsp");
				BaseAction.sendRedirect("/jcadmin/login.jsp", response);
				return;
			}%>
<html>
	<head>
<%
//增加版块
if (null != request.getParameter("a")) {
	CustomAction action = new CustomAction(request);
	
	int tongId = action.getParameterInt("tongId");
	int stockId = action.getParameterInt("stockId");
	TongCacheUtil.updateTong("stock_id = "+ stockId," id = "+ tongId, tongId);
	TongBean tong = TongCacheUtil.getTong(tongId);
	
	String str = tong.getTitle();
%>
<script type="text/javascript">
alert("设置成功");
</script>
<%
}
%>
	</head>
	<body>
	<form action="editTong.jsp?a=1" method="post">
		帮会ID:<input name="tongId" value=""/>
		股票ID:<input name="stockId" value=""/>
		<input type="submit" value="设置" onclick="if(!confirm('确定设置吗?')) {return false;}"/>
	</form>
	</body>
</html>
