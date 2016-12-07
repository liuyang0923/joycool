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
	public static String peopleIcon(int people) {
		if(people>=500)
			return "";
		else if(people>=300)
			return "1";
		else if(people>=100)
			return "2";
		else
			return "3";
	}
	public static String ots(int x, int y){
		x = ((x<<10+y)/7) % 4;
		if(x == 1)
			return "2";
		if(x == 2)
			return "1";
		return "";
	}
	public static String[][] types = {
		{"","2"},
		{"","2"},
		{"","2"},
		{"",""},
		{"",""},
		{"",""},
		{"","2"},
		{"",""},		
	};
public static String[] coord = {
"49,210,86,230,49,250,12,230",
"85,190,122,210,85,230,48,210",
"122,170,159,190,122,210,85,190",
"158,150,195,170,158,190,121,170",
"194,130,231,150,194,170,157,150",
"230,110,267,130,230,150,193,130",
"267,90,304,110,267,130,230,110",
"86,230,123,250,86,270,49,250",
"122,210,159,230,122,250,85,230",
"158,190,195,210,158,230,121,210",
"195,170,232,190,195,210,158,190",
"231,150,268,170,231,190,194,170",
"267,130,304,150,267,170,230,150",
"303,110,340,130,303,150,266,130",
"123,250,160,270,123,290,86,270",
"159,230,196,250,159,270,122,250",
"195,210,232,230,195,250,158,230",
"231,190,268,210,231,230,194,210",
"268,170,305,190,268,210,231,190",
"304,150,341,170,304,190,267,170",
"340,130,377,150,340,170,303,150",
"159,270,196,290,159,310,122,290",
"196,250,233,270,196,290,159,270",
"232,230,269,250,232,270,195,250",
"268,210,305,230,268,250,231,230",
"304,190,341,210,304,230,267,210",
"341,170,378,190,341,210,304,190",
"377,150,414,170,377,190,340,170",
"196,290,233,310,196,330,159,310",
"232,270,269,290,232,310,195,290",
"269,250,306,270,269,290,232,270",
"305,230,342,250,305,270,268,250",
"341,210,378,230,341,250,304,230",
"377,190,414,210,377,230,340,210",
"414,170,451,190,414,210,377,190",
"233,310,270,330,233,350,196,330",
"269,290,306,310,269,330,232,310",
"305,270,342,290,305,310,268,290",
"342,250,379,270,342,290,305,270",
"378,230,415,250,378,270,341,250",
"414,210,451,230,414,250,377,230",
"450,190,487,210,450,230,413,210",
"270,330,307,350,270,370,233,350",
"306,310,343,330,306,350,269,330",
"342,290,379,310,342,330,305,310",
"378,270,415,290,378,310,341,290",
"415,250,452,270,415,290,378,270",
"451,230,488,250,451,270,414,250",
"487,210,524,230,487,250,450,230",
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
	int[] baseBuild = null;
	if(castle!=null) {
		user=CastleUtil.getCastleUser(castle.getUid());
		baseBuild = ResNeed.baseBuildRes[castle.getType2()];
	}
	
	long now = System.currentTimeMillis();

%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
<script src="unx.js" type="text/javascript"></script>
	<body onload="start()"><%@include file="pages.jsp"%>
<%if(true){%>

<div id="lmid1"><div id="lmid2"><div id="lplz3"></div>
<div class="map_infobox" id="tb"><table class='f8 map_infobox_grey' cellspacing='1' cellpadding='2'><tr><td class='c b' colspan='2' align='center'>详情:</td></tr><tr><td width='45%' class='c s7'>玩家:</td><td class='c s7'>-</td></tr><tr><td class='c s7'>居民:</td><td class='c s7'>-</td></tr><tr><td class='c s7'>联盟:</td><td class='c s7'>-</td></tr></table></div>
<div class="mbg"></div><div id="map_content"><div class="mdiv" style="z-index:2">

<%
StringBuilder sb = new StringBuilder(4096);
int dis=3;
int uid = (castle==null?0:castle.getUid());
int tongId = 0;
if(user!=null)
	user.getTong();
for(int i=x-dis;i <= x+dis;i++){
 int i2=formatAxis(i);
 int x2 = i-x+dis;
if(x2!=0)	sb.append(',');
sb.append('[');%><%
 for(int j=y+dis;j >= y-dis;j--){ 
int j2=formatAxis(j);
int y2 = (j-y+dis);
 if(y2!=6)	sb.append(',');
%><img class="mt<%=(y2*7+x2)+1%>" src="img/<%if(map[i2][j2]!=0){CastleBean c = CastleUtil.getCastleById(map[i2][j2]);
	int peoples=0;
	if(map[i2][j2]!=0)
	 	if(map[i2][j2]!=0){
			UserResBean ur = CastleUtil.getUserResBeanById(map[i2][j2]);
			peoples=ur.getPeople();
			CastleBean c2 = CastleUtil.getCastleById(map[i2][j2]);
			CastleUserBean u = CastleUtil.getCastleUser(c2.getUid());
			String tongN="-";
			if(u.getTong()!=0)
				tongN=StringUtil.toWml(CastleUtil.getTong(u.getTong()).getName());
		 sb.append("{'x':'").append(i2).append("','y':'").append(j2).append("','ew':'").append(peoples).append("','name':'").append(u.getNameWml()).append("','dname':'").append(c2.getCastleNameWml()).append("','ally':'").append(tongN).append("','title':'城堡'}");
		} else {
		 sb.append("{'x':'").append(i2).append("','y':'").append(j2).append("','ew':null,'name':null,'dname':null,'ally':null,'title':''}");
		}
 if(c.getUid()==uid){
%>oc1<%=peopleIcon(peoples)%><%}else if(tongId!=0){
	CastleUserBean u = CastleUtil.getCastleUser(c.getUid());
	if(tongId == u.getTong()){
%>oc2<%=peopleIcon(peoples)%><%}else{%>oc3<%=peopleIcon(peoples)%><%}%><%}else{%>oc3<%=peopleIcon(peoples)%><%}%><%}else{

if(mapType[i2][j2]==0){%>ot3<%}else if(mapType[i2][j2]<=16){
%>ot<%=ots(i2,j2)%><%}else{

OasisBean oasis = CastleUtil.getOasisByXY(i2,j2);

%>o<%=mapType[i2][j2]-16%><%=types[mapType[i2][j2]-17][(i2+j2)%2]%><%
}
}%>.gif"/><%}%><%sb.append(']');
}%>
</div><img class="mdiv" style="z-index:15;" src="img/blank.gif" usemap="#karte" />

<map id="karte" name="karte"><%

for(int j=y-dis;j <= y+dis;j++){
int j2=formatAxis(j);
 for(int i=x-dis;i <= x+dis;i++){ 
 int i2=formatAxis(i);
 int x2 = i-x+dis;
 int y2 = 6-(j-y+dis);
%><area id="a_<%=x2%>_<%=y2%>" shape="poly" coords="<%=coord[(y2+x2*7)]%>" href="page1.jsp?x=<%=i2%>&y=<%=j2%>"/><%

}}%>
<area id="ma_n1" href="page3.jsp?x=<%=formatAxis(x)%>&y=<%=formatAxis(y-1)%>" coords="422,137,25" shape="circle" title="北"/>
<area id="ma_n2" href="page3.jsp?x=<%=formatAxis(x+1)%>&y=<%=formatAxis(y)%>" coords="427,324,25" shape="circle" title="东"/>
<area id="ma_n3" href="page3.jsp?x=<%=formatAxis(x)%>&y=<%=formatAxis(y+1)%>" coords="119,325,25" shape="circle" title="南"/>
<area id="ma_n4" href="page3.jsp?x=<%=formatAxis(x-1)%>&y=<%=formatAxis(y)%>" coords="114,133,25" shape="circle" title="西"/>

<area id="ma_n1p7" href="page3.jsp?x=<%=formatAxis(x)%>&y=<%=formatAxis(y-1)%>" coords="475,369, 497,357, 519,369, 497,381" shape="poly" title="北"/>
<area id="ma_n2p7" href="page3.jsp?x=<%=formatAxis(x+1)%>&y=<%=formatAxis(y)%>" coords="475,395, 497,383, 519,395, 497,407" shape="poly" title="东"/>
<area id="ma_n3p7" href="page3.jsp?x=<%=formatAxis(x)%>&y=<%=formatAxis(y+1)%>" coords="428,395, 450,383, 472,395, 450,407" shape="poly" title="南"/>
<area id="ma_n4p7" href="page3.jsp?x=<%=formatAxis(x-1)%>&y=<%=formatAxis(y)%>" coords="428,369, 450,357, 472,369, 450,381" shape="poly" title="西"/>
</map>
<%--
<script type="text/javascript">
text_details = '详情:';
text_spieler = '玩家:';
text_einwohner = '居民:';
text_allianz = '联盟:';
m_c.az = {"n1":301143,"n1p7":296337,"n2":301945,"n2p7":301951,"n3":302745,"n3p7":307551,"n4":301943,"n4p7":301937};
m_c.ad = [<%=sb.toString()%>];
m_c.z = {"x":"<%=x%>","y":"<%=y%>"};
m_c.size = 7;
function init_local(){
	map_init();
}
			</script>
--%>
<%for(int j=y-dis;j <= y+dis;j++){%><div id="my<%=6-(j-y+dis)%>"><%=formatAxis(j)%></div><%}%>
<%for(int i=x-dis;i <= x+dis;i++){%><div id="mx<%=i-x+dis%>"><%=formatAxis(i)%></div><%}%>
<div class="map_show_xy">
	<table width="100%" cellspacing="0" cellpadding="0">
	<tr>
	<td width="30%"><h1>地图</h1></td>
	<td width="33%" class="right nbr"><h1>(<span id="x"><%=x%></span></h1></td>
	<td width="4%" align="center"><h1>|</h1></td>
	<td width="33%" class="left nbr"><h1><span id="y"><%=y%></span>)</h1></td>
	</tr>
	</table>
</div><div class="map_insert_xy"><form method="post" action="page3.jsp">
<table align="center" cellspacing="0" cellpadding="3">
<tr>
<td><b>x</b></td>
<td><input id="mcx" class="fm fm25" name="x" value="<%=x%>" size="2" maxlength="4"/></td>
<td><b>y</b></td>
<td><input id="mcy" class="fm fm25" name="y" value="<%=y%>" size="2" maxlength="4"/></td>
<td></td>
<td><input type="image" value="ok" name="s1" src="img/ok.gif" width="50" height="20"/></td>
</tr></table></form></div>
</div></div></div>

<%String baseURL="page3.jsp";%><%@include file="pager.jsp"%>
<%}else {
if(mapType[x][y]>16){
OasisBean o = CastleUtil.getOasisByXY(x,y);
%>【绿洲】(<%=x%>|<%=y%>)<br/>
<%=oasisInfo3[mapType[x][y]-16]%><br/><%
if(o.getCid()!=0){
CastleBean ca = CastleUtil.getCastleById(o.getCid());
CastleUserBean us = CastleUtil.getCastleUser(ca.getUid());
%>种族:<%=ca.getRaceName()%><br/>
城主:<a href="user.jsp?id=<%=ca.getUid()%>"><%=us.getNameWml()%></a><br/>
城堡:<a href="castle.jsp?id=<%=ca.getId()%>"><%=ca.getCastleNameWml()%>(<%=ca.getY()%>|<%=ca.getX()%>)</a><br/><%

}}else{
	int[] block = ResNeed.initResBlock[mapType[x][y]];
%>【荒漠】(<%=x%>|<%=y%>)<br/>
<img src="img/1.gif"> <%=block[0]%> 伐木场<br/>
<img src="img/2.gif"> <%=block[1]%> 采石场<br/>
<img src="img/3.gif"> <%=block[2]%> 铁矿场<br/>
<img src="img/4.gif"> <%=block[3]%> 粮田<br/><%}
}%>
</html>