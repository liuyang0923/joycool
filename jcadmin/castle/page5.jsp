<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
%><%
	CustomAction action = new CustomAction(request);
	long now=System.currentTimeMillis();

	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("uid");
	CastleUserBean user = CastleUtil.getCastleUser(id);
	if(user==null){
		response.sendRedirect("index.jsp");
		return;
	}
	TongBean tongBean = null;
	List list = service.getCastleList(id);
	if(user.getTong() > 0) {
		tongBean = CastleUtil.getTong(user.getTong());
	}

UserResBean castleRes = null;
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>
	<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><h1>城主资料</h1>
	
<p><table cellspacing="1" cellpadding="2" class="tbg">
	<tr>
	<td class="rbg" colspan="3">城主 <%=StringUtil.toWml(user.getName())%></td>
	</tr>
	
	<tr>
	<td width="50%" colspan="2">详细资料:</td>
	<td width="50%">描述:</td>
	</tr>
	
	<tr><td colspan="2"></td><td></td></tr>
	<tr><td class="s7">排名:</td><td class="s7">?</td>
	<td rowspan="11" class="slr3"><%=user.getInfo2()%></td></tr>
	<tr class="s7"><td>种族:</td><td><%=user.getRaceName()%></td></tr>
	<tr class="s7"><td>联盟:</td><td><%if(tongBean != null) {%><a href="page4.jsp?id=<%=tongBean.getId()%>"><%=StringUtil.toWml(tongBean.getName()) %></a><%}else{%>-<%}%></td></tr>
	<tr class="s7"><td>城堡:</td><td><%=user.getCastleCount()%></td></tr>
	<tr class="s7"><td>人口:</td><td><%=user.getPeople()%></td></tr><tr><td></td><td></td></tr>
	<tr class="s7"><td colspan="2"> &raquo; 撰写消息</td></tr>

	<tr><td colspan="2" class="slr3"></td></tr>
	</table></p><p>
	<table cellspacing="1" cellpadding="2" class="tbg">
	
	<tr>
	<td class="rbg" colspan="3">村庄:</td>
	</tr>
	
	<tr>
	<td width="50%">名字</td>
	<td width="25%">人口</td>
	<td width="25%">坐标</td>
	</tr><%for(int i = 0; i < list.size() ; i++) {
	CastleBean castleBean = (CastleBean)list.get(i);
	UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
%><tr><td class="s7"><a href="page1.jsp?id=<%=castleBean.getId()%>" style="display:block;float:left;"><%=StringUtil.toWml(castleBean.getCastleName()) %></a><%if(castleBean.getId()==user.getMain()){%> <span style="display:block;float:left;" class="c">&nbsp;（主村）</span><%}%></td>
		<td><%=userRes.getPeople() %></td>
		<td>(<%=castleBean.getX() %>|<%=castleBean.getY() %>)</td>
		</tr><%} %></table></p>
	
</div></div></div></div>
</body>
</html>