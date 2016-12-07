<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="net.joycool.wap.spec.rich.*" %><%@ page import="java.util.*"%><%!

%><%
	CustomAction action = new CustomAction(request);
	long now = System.currentTimeMillis();
	String resetWorld=request.getParameter("resetWorld");
	if(resetWorld!=null){
		RichWorld world = RichAction.worlds[StringUtil.toId(resetWorld)];
		if(world.loaded) {
			world.reward();
			world.reset(now);
		}
		response.sendRedirect("rich.jsp");
		return;
	}
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>大富翁数据</h3>
<%
int cur = 1;
for(int j=0;j<RichAction.worlds.length;j++){
RichWorld world=RichAction.worlds[j]; if(world.getFlag() == 3){%>
第<%=cur%>房间<%%>(<%=world.getUserCount()%>人/<%=world.maxPlayer%>)<%if(world.isFull()){%>(满)<%}%>
<a href="rich.jsp?resetWorld=<%=j%>" onclick="return confirm('确定重置该房间？')">重置</a><br/>
每局<%=world.getInterval()/60%>分钟,剩<%=world.timeLeft(now)/60%>分钟,开始于<%=DateUtil.formatTime(world.getStartTime())%><br/>

<table cellpadding=2><tr><td>用户名</td><td></td><td></td><td></td><td>ip</td><td>ua</td></tr>
<%

String[] names = RichUserBean.roleNames;
int[] roleUser = world.roleUser;
for(int i=0;i<roleUser.length;i++){ 
if(roleUser[i]!=0){ 
UserBean user = UserInfoUtil.getUser(roleUser[i]);
%>
<tr><td>(<%=user.getId()%>)<a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td>
<td align=right><%=names[i]%></td>
<td align=right></td>
<td align=right></td>
<td><%=user.getIpAddress()%></td><td width="300"><%=user.getUserAgent()%></td>
</tr>
<%}}%>
</table>
<%cur++;}}%>
<br/>
<a href="rich2.jsp">大富翁详细系统数据</a><br/><br/>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>