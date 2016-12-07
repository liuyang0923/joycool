<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.bean.*" %><%@ page import="net.joycool.wap.bean.dummy.*" %><%@ page import="net.joycool.wap.cache.util.*" %><%@include file="top.jsp"%><%
CustomAction action = new CustomAction(request, response);
FarmWorld world = FarmWorld.getWorld();
int id = action.getParameterInt("id");
TongBean tong = FarmTongWorld.getTong(id);
List member = FarmTongWorld.getTongUserList(id);
if(action.hasParam("flush")){
	for(int i=0;i<member.size();i++){
		Integer iid = (Integer)member.get(i);
		FarmUserBean user = FarmWorld.one.getFarmUserCache(iid);
		if(user!=null) {
			FarmTongWorld.farmTongUserCache.srm(iid);
			user.setTongUser(FarmTongWorld.getTongUser(iid.intValue()));
		}
	}
//	FarmTongWorld.farmTongCache.srm(id);
}

%>
			<html>
	<head>
	</head>
<link href="common.css" rel="stylesheet" type="text/css">
	<body>

<%if(tong != null){
%>
门派--<%=tong.getNameWml()%>(<%=tong.getCount()%>人)等级<%=tong.getRank()%><br/>
==门派弟子==<br/>
<%for(int i=0;i<member.size();i++){
Integer iid = (Integer)member.get(i);
FarmUserBean user = FarmWorld.one.getFarmUserCache(iid);
if(user==null || user.getTongUser() == null) continue;
%>
<a href="viewUser.jsp?id=<%=user.getUserId()%>"><%=user.getNameWml()%></a><%=user.getRank()%>级/<%=user.getClass1Name()%>/<%=user.getTongUser().getDutyName()%>
<%if(user.isFlagOffline()){%>(离线)<%}%><br/>
<%}%>

<a href="viewTong.jsp?flush=1&amp;id=<%=id%>">重新从数据库载入门派数据</a><br/>
==聊天记录==<br/>
<%
SimpleChatLog sc = SimpleChatLog.getChatLog(id);
PagingBean paging = new PagingBean(action, sc.size(),20,"p");
%>

<%=sc.getChatString(paging.getStartIndex(), 20)%>
<%=paging.shuzifenye("viewTong.jsp?id="+id, true, "|", response)%>
<%}else{%>
该门派不存在<br/>
<%}%>
<%@include file="bottom.jsp"%>
	</body>
</html>