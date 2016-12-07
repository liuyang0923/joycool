<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	TongBean tong = CastleUtil.getTong(id);
	
int uid = action.getParameterInt("uid");
if(uid>0){
	TongPowerBean powerBean = service.getTongPowerByUid(uid);
	powerBean.togglePower(action.getParameterInt("i"));
	service.updateTongPower(uid, powerBean.getPower());
	response.sendRedirect("tong.jsp?id="+id);
	return;
}
	
List list = service.getTongUser(id, 0, 100);
List list2 = service.getTongPower(id);
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	联盟信息<br/><br/>
【<%=StringUtil.toWml(tong.getName())%>】<br/>
成员:<%=tong.getCount()%>|总人口:<%=tong.getPeople()%><br/>
描述:<%=StringUtil.toWml(tong.getInfo())%><br/>
<a href="tongChat.jsp?id=<%=id%>">联盟讨论</a><br/><br/>
<table class="tbg" cellpadding="2" cellspacing="1" width="400"><tr class="rbg"><td>称号</td><td>城主</td>
<td>任命</td>
<td>邀请</td>
<td>开除</td>
<td>描述</td>
<td>名称</td>
<td>外交</td>
</tr>
<%
for(int i=0;i<list2.size();i++){
	TongPowerBean bean = (TongPowerBean)list2.get(i);
Integer iid = new Integer(bean.getUid());
CastleUserBean user = CastleUtil.getCastleUser(iid.intValue());
%><tr><td><%=StringUtil.toWml(bean.getPowerName())%></td><td><a href="user.jsp?id=<%=user.getUid()%>"><%=user.getNameWml()%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=0"><%if(bean.isPowerTop()){%>√<%}else{%>×<%}%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=1"><%if(bean.isPowerInvite()){%>√<%}else{%>×<%}%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=2"><%if(bean.isPowerDelete()){%>√<%}else{%>×<%}%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=3"><%if(bean.isPowerIntro()){%>√<%}else{%>×<%}%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=4"><%if(bean.isPowerName()){%>√<%}else{%>×<%}%></a></td>
<td><a href="tong.jsp?id=<%=id%>&uid=<%=user.getUid()%>&i=5"><%if(bean.isPowerDip()){%>√<%}else{%>×<%}%></a></td>
</tr><%}%></table>
<br/>
<table class="tbg" cellpadding="2" cellspacing="1" width="300"><tr class="rbg"><td></td><td>成员</td><td>人口</td><td>城堡数</td></tr>
<%
for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
CastleUserBean user = CastleUtil.getCastleUser(iid.intValue());
if(user==null) continue;
%><tr><td><%=i+1%></td><td><a href="user.jsp?id=<%=user.getUid()%>"><%=user.getNameWml()%></a></td><td><%=user.getPeople()%></td><td><%=user.getCastleCount()%></td></tr>
<%
}
%></table>
<%@include file="bottom.jsp"%>
</html>