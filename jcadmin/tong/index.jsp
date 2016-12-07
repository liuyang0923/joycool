<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.service.factory.ServiceFactory"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="java.sql.ResultSet"%><%@ page import="net.joycool.wap.service.infc.IJcForumService"%><%@ page import="net.joycool.wap.bean.jcforum.ForumBean"%><%@ page import="net.joycool.wap.cache.NoticeCacheUtil"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.service.infc.ITongService" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%response.setHeader("Cache-Control", "no-cache");
%>
<%ITongService tongService = ServiceFactory.createTongService();
int NUMBER_PER_PAGE = 15;
List tongList = TongCacheUtil.getTongListById("id");
int totalCount = tongList.size();
int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
if (pageIndex == -1) {
	pageIndex = 0;
}
int totalPageCount = totalCount / NUMBER_PER_PAGE;
if (totalCount % NUMBER_PER_PAGE != 0) {
	totalPageCount++;
}
if (pageIndex > totalPageCount - 1) {
	pageIndex = totalPageCount - 1;
}
if (pageIndex < 0) {
	pageIndex = 0;
}
String prefixUrl = "index.jsp?orderBy=id";
int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
int end = NUMBER_PER_PAGE;
int startIndex = Math.min(start, totalCount);
int endIndex = Math.min(start + end, totalCount);
List tongList1 = tongList.subList(startIndex, endIndex);
CustomAction action = new CustomAction(request);
if(action.hasParam("name")){
	String name = action.getParameterNoEnter("name");
	tongList1 = SqlUtil.getIntList("select id from jc_tong where title like '" + StringUtil.toSql(name)+"%' limit 20");
}
%>
<html>
	<head>
	 <script language="javascript" >
function operate2(){
     if (confirm('您确定要执行操作吗?')) {
       return true;
       } else {
        return false;
       }
      }
function test(){
	if(document.tongForm.tongId.value.replace(/(^\s+|\s+$)/g,"") == ''){
		alert("帮会ID不能为空！");
		return false;
	}else if(isNaN(document.tongForm.tongId.value)){
		alert("帮会ID只能是数字！");
		return false;
	}
	return true;
}
</script>
<title>帮会基金管理后台</title>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
		
		<H1 align="center">帮会基金管理后台</H1>
		<hr>
		<br/>
		<table width="800" border="1" align="center">
			<th >编号</th>
			<th >帮会ID</th>
			<th>帮会名称</th>
			<th>帮主名称</th>
			<th>帮会基金</th>
			<th>帮会状态</th>
			<%for (int i = 0; i < tongList1.size(); i++) {
					Integer tongId = (Integer) tongList1.get(i);
					TongBean tong = TongCacheUtil.getTong(tongId.intValue());
					if(tong==null)continue;
					%>
			<tr>
					<td>
						<%=i + 1%>
					</td>
					<td>
						<a href="search.jsp?tongId=<%=tong.getId()%>"><%=tong.getId()%></a>
					</td>
					<td>
						<%=tong.getTitle()%>
					</td>
					<td>
						<%
						int userId = tong.getUserId();
						UserBean user = UserInfoUtil.getUser(userId);
						if(user!=null){%>
						<%=user.getNickName()%>
						<%}else{%>帮主名称有误或空闲<%}%>
					</td>
					<td>
						<%=tong.getFund()%>
					</td>
					<td>
						<%if(tong.getMark()==0){%>正常状态<%}else{%>荒城<%}%>
					</td>
			</tr>
			<%}%>
		</table>
		<%String fenye = PageUtil.shuzifenye(totalPageCount, pageIndex, prefixUrl, true, "|", response);
			if(!"".equals(fenye)){%><p align="center"><%=fenye%></p><br/><%}%>

<p align="center">
<form  action="search.jsp" method="get"  name="tongForm" onsubmit="return test()"><br/>
帮会ID
<input type="text" name="tongId" size="10" >&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="查询"><br/>
</form>
<form  action="index.jsp" method="get"  name="tongForm">
帮会名称
<input type="text" name="name" size="10" >&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="查询"><br/>
</form><br/></p>
<p align="center">
<%if(group.isFlag(0)){%>	<a href="honor.jsp">查看勋号</a><br/><%}%>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
</p>
</body>
</html>
