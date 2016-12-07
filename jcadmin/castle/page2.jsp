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
	int[] baseBuild = null;
	if(castle!=null) {
		user=CastleUtil.getCastleUser(castle.getUid());
		baseBuild = ResNeed.baseBuildRes[castle.getType2()];
	} else {
		response.sendRedirect("page1.jsp");
		return;
	}

	List buildings = service.getAllBuilding(id);
	BuildingBean[] buildPos = new BuildingBean[41];
	for(int i = 0;i < buildings.size();i++){
		BuildingBean b = (BuildingBean)buildings.get(i);
		buildPos[b.getBuildPos()] = b;
	}
	int swap=26;
	{
		BuildingBean tmp = buildPos[19];
		buildPos[19]=buildPos[swap];
		buildPos[swap]=tmp;
	}
	swap=39;
	for(int i=19;i<buildPos.length;i++)
		if(buildPos[i]!=null&&buildPos[i].getGrade()!=0&&buildPos[i].getBuildType()==22&&i!=swap){
			BuildingBean tmp = buildPos[i];
			buildPos[i]=buildPos[swap];
			buildPos[swap]=tmp;
		}
	swap=40;
	for(int i=19;i<buildPos.length;i++)
		if(buildPos[i]!=null&&buildPos[i].getGrade()!=0&&(buildPos[i].getBuildType()==26||buildPos[i].getBuildType()==28||buildPos[i].getBuildType()==29)&&i!=swap){
			BuildingBean tmp = buildPos[i];
			buildPos[i]=buildPos[swap];
			buildPos[swap]=tmp;
		}
	
	
	long now = System.currentTimeMillis();
	int bgmap = 0;
	if(castleRes.getBuildingGrade(ResNeed.raceWall[castle.getRace()])!=0&&castle.getRace()>0&&castle.getRace()<4){
		bgmap=castle.getRace();
	}
	String sw = "";
	CookieUtil cu = new CookieUtil(request,response);
	String t3l = cu.getCookieValue("t3l");
	if("1".equals(t3l)){
		sw=" class=\"on\"";
	}
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<script src="mt-full.js" type="text/javascript"></script>
<script>
function vil_levels_toggle(){var $i=$('levels'),_i=$('lswitch');$i.toggleClass('on');_i.toggleClass('on');if($i.hasClass('on')){document.cookie='t3l=1; expires=Wed, 1 Jan 2020 00:00:00 GMT';}
else{document.cookie='t3l=1; expires=Thu, 01-Jan-1970 00:00:01 GMT';}
}
</script>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>
<%if(castle!=null){%>
<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><div class="dname"><h1><%=castle.getCastleNameWml()%></h1></div>

<%for(int i = 1; i <= 40; i ++) {
  if(buildPos[i] !=null){
%><%
} if(i>18&&(buildPos[i]==null||buildPos[i].getBuildType()!=26&&buildPos[i].getBuildType()!=28&&buildPos[i].getBuildType()!=29)){
%><%if(buildPos[i]!=null){%><img src="/rep/castle/img/b<%if(buildPos[i].getBuildType()==38){%>38_<%=buildPos[i].getGrade()/20%><%}else{%><%=buildPos[i].getBuildType()%><%if(buildPos[i].getGrade()==0){%>b<%}%><%}%>.gif" class="<%
if(buildPos[i].getBuildType()==38){
	%>ww g40_<%=buildPos[i].getGrade()/20%><%
}else{
	%>d<%if(i!=39){%><%=i-18%><%}else{%>x1<%}}%>"><%}else{
%><%if(i<40&&(!castle.isNatar()||i!=19&&i!=25&&i!=29&&i!=30&&i!=33&&i!=26)){%><img src="/rep/castle/img/b0.gif" class="d<%=i-18%>" width="75" height="100"><%}%><%}%><%}else{
%><%
}if((i)%4 == 0) {%><%}else{%><%}%><%}%><div class="d2_x d2_<%=bgmap%>"></div>
<div id="levels"<%=sw%>><%for(int i = 19; i <= 40; i ++) {
  if(buildPos[i] !=null){
%><div class="<%if(buildPos[i].getBuildType()==38){%>ww g40_<%=buildPos[i].getGrade()/20%><%}else if(i!=39){%>d<%=i-18%><%}else{%>d21<%}%>"><%=buildPos[i].getGrade()%></div><%
 }}
%></div>
<img src="img/blank.gif" id="lswitch"<%=sw%> onclick="vil_levels_toggle()" />
</div></div></div></div>
<%String baseURL="page2.jsp";%><%@include file="pager.jsp"%>
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