<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.bean.tong.TongCityRecordBean"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.UserBean" %><%@ page import="net.joycool.wap.bean.tong.TongBean" %><%@ page import="net.joycool.wap.cache.util.TongCacheUtil" %><%@ page import="net.joycool.wap.action.tong.TongAction" %><%@ page import="net.joycool.wap.bean.PagingBean" %><%@ page import="net.joycool.wap.framework.*" %><%

int  tongId=StringUtil.toInt(request.getParameter("tongId"));
if(tongId<=0){
response.sendRedirect("index.jsp");
return;
}
TongBean tong = TongCacheUtil.getTong(tongId);
if(tong==null){
response.sendRedirect("index.jsp");
return;
}
CustomAction action = new CustomAction(request);
PagingBean paging = new PagingBean(action, 1000, 20, "p");
int start = paging.getStartIndex();

List list = TongAction.getTongService().getTongCityRecordList(
							"tong_id=" + tong.getId() + " and mark=0 order by `count` desc limit " + start + ",20");

%>
<html>
	<head>
<meta http-equiv="expires" content="0" />
	<title>帮会管理后台</title>
</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
<body>
	<H1 align="center">帮会城墙攻打记录</H1>
	<hr>
	<br/>
	<table width="800" border="1" align="center">
	<tr>
	<td width="40">名次</td>
	<td>玩家</td>
	<td width="80">次数</td>
	<td width="120">ip</td>
	</tr>
<%for(int i=0;i<list.size();i++){
TongCityRecordBean bean = (TongCityRecordBean) list.get(i);
UserBean user = UserInfoUtil.getUser(bean.getUserId());
UserBean user2 = (UserBean)OnlineUtil.getOnlineBean(String.valueOf(user.getId()));
%>
	<tr>	
		<td><%=start+i+1%></td>
		<td>(<%=user.getId()%>)<a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)<%if(ForbidUtil.isForbid("game",user.getId())){%><font color="red">(封禁)</font><%}%></td>
		<td><%=bean.getCount()%></td>
		<td><%if(user2!=null){%><%=user2.getIpAddress()%><%}else{%><%}%></td>
	</tr>
<%}%>
	</table>
	<br /><%=paging.shuzifenye("wall2.jsp?tongId="+tongId,true,"|",response)%><br/>
<form  action="wall2.jsp" method="post"  name="tongForm"><br/>
帮会ID：
<input type="text" name="tongId" size="10" >&nbsp;&nbsp;&nbsp;&nbsp;
<input type="submit" value="查询"><br/>
</form><br/>
<p align="center">
<a href="search.jsp?tongId=<%=tongId%>">返回<%=tong.getTitle()%></a><br/>
	<a href="index.jsp">返回帮会管理后台</a><br/>
	<a href="/jcadmin/manage.jsp">返回管理首页</a>
</p>
</body>
</html>
