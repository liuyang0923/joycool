<%@ page contentType="text/html;charset=utf-8"%><%@include file="../filter.jsp"%><%@ page import="net.joycool.wap.util.*"%><%@ page import="jc.family.game.fightbox.*"%><%@ page import="jc.family.*"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="jc.family.game.vs.*"%><%@ page import="net.joycool.wap.bean.UserBean"%><%@ page import="java.util.*"%><%!

%><%
	if(!group.isFlag(0))
		return;
	CustomAction action = new CustomAction(request);
%>
<html>
	<head>
	</head>
<link href="../farm/common.css" rel="stylesheet" type="text/css">
	<body>
<h3>家族挑战赛在线</h3>
<%
List list=VsAction.getVsGameBean();
if(list!=null&&list.size()>0){
	for(int j=0;j<list.size();j++){
		VsGameBean bean=(VsGameBean)list.get(j);
		if(bean.getState()==bean.gameEnd) continue;
		
		

%>

<table cellpadding=2><tr><td>用户名</td><td></td><td></td><td>ip</td><td>ua</td></tr>
<%

	List ulist = new ArrayList(bean.getUserMap().values());
for(int i=0;i<ulist.size();i++){ 
VsUserBean vsUser = (VsUserBean)ulist.get(i);
UserBean user = UserInfoUtil.getUser(vsUser.getUserId());
FamilyHomeBean fm = FamilyAction.getFmByID(vsUser.getFmId());
%>
<tr><td>(<%=user.getId()%>)<a href="../user/queryUserInfo.jsp?id=<%=user.getId()%>"><%=user.getNickNameWml()%></a>(<%=LoadResource.getPostionNameByUserId2(user.getId())%>)</td>
<td align=right><%if(fm!=null){%><%=fm.getFm_nameWml()%><%}%></td>
<td align=right></td>
<td><%=user.getIpAddress()%></td><td width="300"><%=user.getUserAgent()%></td>
</tr>
<%}%>
</table>
<%}}%>
		<a href="index.jsp">返回上一级</a>
		<a href="/jcadmin/manage.jsp">返回管理首页</a>
		<br />
	</body>
</html>