<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@include file="top.jsp"%><%!
	public static String[] oasisInfo3 = {
		"",
		"<img src=\"img/1.gif\">+25%",
		"<img src=\"img/2.gif\">+25%",
		"<img src=\"img/3.gif\">+25%",
		"<img src=\"img/1.gif\">+25% <img src=\"img/4.gif\">+25%",
		"<img src=\"img/2.gif\">+25% <img src=\"img/4.gif\">+25%",
		"<img src=\"img/3.gif\">+25% <img src=\"img/4.gif\">+25%",
		"<img src=\"img/4.gif\">+25%",
		"<img src=\"img/4.gif\">+50%",
	};
static CastleService service = CastleService.getInstance();
static int formatAxis(int a) {
 if(a<0) return a+CastleUtil.mapSize; else if(a>=CastleUtil.mapSize) return a-CastleUtil.mapSize; else return a;
}
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("cid");
	int x = action.getParameterIntS("x");
	int y = action.getParameterIntS("y");
	int pos = action.getParameterIntS("pos");
	if(pos != -1) {
		x = CastleUtil.pos2X(pos);
		y = CastleUtil.pos2Y(pos);
	}
	int[][] map = CastleUtil.getMap();
	byte[][] mapType = CastleUtil.getMapType();
	CastleBean castle = null;
	if(id==0){
		if(x==-1||y==-1){
			if(session.getAttribute("x")==null)
				x=y=0;
			else {
				x=((Integer)session.getAttribute("x")).intValue();
				y=((Integer)session.getAttribute("y")).intValue();		
			}
		}else{
			session.setAttribute("x", new Integer(x));
			session.setAttribute("y", new Integer(y));
		}
		id = map[x][y];
		if(id!=0)
			castle = CastleUtil.getCastleById(id);
	} else {
		castle = CastleUtil.getCastleById(id);
		x = castle.getX();
		y = castle.getY();
		session.setAttribute("x", new Integer(x));
		session.setAttribute("y", new Integer(y));
	}
	UserResBean castleRes = CastleUtil.getUserResBeanById(id);
	CastleUserBean user = null;
	TongBean tong = null;
	int[] baseBuild = null;
	if(castle!=null) {
		user=CastleUtil.getCastleUser(castle.getUid());
		baseBuild = ResNeed.baseBuildRes[castle.getType2()];
		if(user.getTong()!=0)
			tong = CastleUtil.getTong(user.getTong());
	}

	List buildings = service.getAllBuilding(id);
	BuildingBean[] buildPos = new BuildingBean[41];
	for(int i = 0;i < buildings.size();i++){
		BuildingBean b = (BuildingBean)buildings.get(i);
		buildPos[b.getBuildPos()] = b;
	}
	
	
	long now = System.currentTimeMillis();
CastleUtil.getMapType(x,y);
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>
<%if(castle!=null){%>


<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><div class="dname"><h1><%=castle.getCastleNameWml()%></h1></div>

<div></div>
<div class="map_details_right">
<div class="f10 b"><div class="ddb">&nbsp;<%=castle.getCastleNameWml()%></div><div class="ddb">&nbsp;(<%=x%>|<%=y%>)&nbsp;</div></div>

<table class="f10" style="clear:both;">
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>种族:</td><td> <b><%=user.getRaceName()%></b></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>联盟:</td><td><%if(user.getTong()!=0){%><a href="page4.jsp?id=<%=user.getTong()%>"><%=StringUtil.toWml(tong.getName())%></a><%}else{%>-<%}%></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>所有者:</td><td><a href="page5.jsp?id=<%=user.getUid()%>"> <b><%=user.getNameWml()%></b></a></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>居民:</td><td><b><%=castleRes.getPeople()%></b></td>
</tr>
</table>
</div>
<div class="map_details_troops">
<div class="f10 b">&nbsp;生产力:</div>
<table class="f10">
<tr>
<td><img class="res" src="img/1.gif"></td>
<td>木材:</td><td align="right"><b><%=castleRes.getWoodSpeed2()%>&nbsp;</b></td><td>每小时</td>
</tr>
<tr>
<td><img class="res" src="img/2.gif"></td>
<td>泥土:</td><td align="right"><b><%=castleRes.getStoneSpeed2()%>&nbsp;</b></td><td>每小时</td>
</tr>
<tr>
<td><img class="res" src="img/3.gif"></td>
<td>铁块:</td><td align="right"><b><%=castleRes.getFeSpeed2()%>&nbsp;</b></td><td>每小时</td>
</tr>
<tr>
<td><img class="res" src="img/4.gif"></td>
<td>粮食:</td><td align="right"><b><%=castleRes.getGrainRealSpeed2()%>&nbsp;</b></td><td>每小时</td>
</tr>
</table></div>

<div id="f<%=castle.getType2()%>"><%for(int i = 1; i <= 40; i ++) {
  if(buildPos[i] !=null){
%><%
} if(i<=18){
%><%if(buildPos[i]!=null&&buildPos[i].getGrade()!=0){%><img src="/rep/castle/img/lvl<%=buildPos[i].getGrade()%>.gif" id="rf<%=i%>" class="rf<%=i%>"><%}%><%}else{
%><%
}if((i)%4 == 0) {%><%}else{%><%}%><%}%></div>
</div></div></div>
<%String baseURL="page1.jsp";%><%@include file="pager.jsp"%>


<%}else {
if(mapType[x][y]>16){
OasisBean o = CastleUtil.getOasisByXY(x,y);
%>
<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><div class="dname"><h1>绿洲 (<%=x%>|<%=y%>)</h1></div>
<img src="/rep/castle/img/w<%=mapType[x][y]-16%>.jpg" id="resfeld">
<%
if(o.getCid()!=0){
CastleBean ca = CastleUtil.getCastleById(o.getCid());
CastleUserBean us = CastleUtil.getCastleUser(ca.getUid());
if(us.getTong()!=0)
	tong = CastleUtil.getTong(us.getTong());

%>

<div class="map_details_right">
<table class="f10">
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>种族:</td><td> <b><%=ca.getRaceName()%></b></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>联盟:</td><td><%if(us.getTong()!=0){%><a href="page4.jsp?id=<%=us.getTong()%>"><%=StringUtil.toWml(tong.getName())%></a><%}else{%>-<%}%></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>所有者:</td><td><a href="page5.jsp?id=<%=ca.getUid()%>"><%=us.getNameWml()%></a></td>
</tr>
<tr>
<td><img src="img/blank.gif" width="3" height="12" border="0"></td>
<td>城堡:</td><td><a href="page1.jsp?id=<%=ca.getId()%>"><%=ca.getCastleNameWml()%></a></td>
</tr>
</table>
</div>
<%}%>

</div></div></div>

<%}else{
	int[] block = ResNeed.initResBlock[mapType[x][y]];
	int t = mapType[x][y]&0x2f;
	if(t<=0||t>12)t=1;
%><div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><div class="dname"><h1>荒漠 (<%=x%>|<%=y%>)</h1></div>

<div id="f<%=t%>"></div>

<div id="pr" class="map_details_right">
<div class="f10 b">&nbsp;资源分配:</div>

<table class="f10">
<tr>
<td><img class="res" src="img/1.gif"></td>
<td class="s7 b"><%=block[0]%></td><td> 伐木场</td>
</tr>
<tr>
<td><img class="res" src="img/2.gif"></td>
<td class="s7 b"><%=block[1]%></td><td> 采石场</td>
</tr>
<tr>
<td><img class="res" src="img/3.gif"></td>
<td class="s7 b"><%=block[2]%></td><td> 铁矿场</td>
</tr>
<tr>
<td><img class="res" src="img/4.gif"></td>
<td class="s7 b"><%=block[3]%></td><td>粮田</td>
</tr>

</table>
</div>
</div></div></div>
<%}
}%>
</html>