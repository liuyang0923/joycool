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

UserResBean castleRes = null;
CastleUserBean user = null;
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>
	
<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><h1><%=StringUtil.toWml(tong.getName())%> - <%=StringUtil.toWml(tong.getName())%></h1><p><table cellspacing="1" cellpadding="2" class="tbg">
<tr>
<td class="rbg" colspan="3">联盟</td>
</tr>

<tr>
<td width="50%" colspan="2">细则:</td>
<td width="50%">描述</td>
</tr>

<tr><td colspan="2"></td><td></td></tr>

<tr>
<td class="s7">快捷名:</td><td class="s7"><%=StringUtil.toWml(tong.getName())%></td>
<td rowspan="50" class="slr3"><p>联盟：<div class="c">-</div></p><p>不侵略契約(NAP)：<div class="c">-</div></p><p>交战中：<div class="c">-</div></td>
</tr>

<tr class="s7">
<td>名称:</td><td><%=StringUtil.toWml(tong.getName())%></td>
</tr>


<tr><td colspan="2"></td></tr>

<tr class="s7">
<td>排名:</td><td width="25%">?.</td>
</tr>

<tr class="s7">
<td>总人口:</td><td><%=tong.getPeople()%></td>
</tr>

<tr class="s7">
<td>成员:</td><td><%=tong.getCount()%></td>
</tr>

<tr><td colspan="2"></td></tr>

<%
for(int i=0;i<list2.size();i++){
	TongPowerBean bean = (TongPowerBean)list2.get(i);
Integer iid = new Integer(bean.getUid());
user = CastleUtil.getCastleUser(iid.intValue());
%><tr class="s7"><td><%=StringUtil.toWml(bean.getPowerName())%></td><td><a href="page5.jsp?id=<%=user.getUid()%>"><%=user.getNameWml()%></a></tr><%}%>

<tr><td colspan="2"></td></tr>
<tr><td colspan="2" class="slr3"><%=StringUtil.toWml(tong.getInfo())%></td></tr>
</table></p>
	
<p><table cellspacing="1" cellpadding="2" class="tbg">

<tr class="rbg">
<td width="6%">&nbsp;</td>
<td width="44%">玩家</td>
<td width="25%">人口</td>
<td width="19%">城堡</td></tr><%
for(int i=0;i<list.size();i++){
Integer iid = (Integer)list.get(i);
user = CastleUtil.getCastleUser(iid.intValue());
if(user==null) continue;
%><tr>
<td align="right"><%=i+1%>.</td><td class="s7"><a href="page5.jsp?id=<%=user.getUid()%>"><%=user.getNameWml()%></a></td><td><%=user.getPeople()%></td>
<td><%=user.getCastleCount()%></td></tr><%
}%></table></p>
</div></div></div></div>
</html>