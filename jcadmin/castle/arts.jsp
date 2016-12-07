<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	if(action.hasParam("reset"))
		CastleUtil.artList = null;
	List list = CastleUtil.getArtList();
	UserResBean castleRes = null;
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>
<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><div class="dname"><h1>宝物</h1>
<table cellspacing="1" cellpadding="2" class="tbg">
<tr><td class="rbg" colspan="4">宝物列表</td></tr>
<tr><td>序号</td><td>名字</td><td>城堡</td><td>玩家</td></tr>
<%
for(int i=0;i<list.size();i++){
%><tr><%
ArtBean art = (ArtBean)list.get(i);
CastleUserBean user2 = CastleUtil.getCastleUser(art.getUid());
CastleBean castle2 = CastleUtil.getCastleById(art.getCid());
%><td width="40"><%=i+1%></td><td><a href="art.jsp?id=<%=art.getId()%>"><%=art.getName()%></a></td><td><%if(castle2!=null){%><a href="page1.jsp?id=<%=art.getCid()%>"><%=castle2.getCastleNameWml()%></a><%}%></td><td><%if(user2!=null){%><a href="page5.jsp?uid=<%=art.getUid()%>"><%=user2.getNameWml()%></a><%}%></td>
</tr>
<%}%>
</table><br/><a href="arts.jsp?reset=1" onclick="return confirm('确认要重新从数据库读取数据？')">重载入所有宝物</a><br/><%@include file="bottom.jsp"%>
</div></div></div></div>

	</body>
</html>