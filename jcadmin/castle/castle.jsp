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
static CacheService cacheService = CacheService.getInstance();
static int formatAxis(int a) {
 if(a<0) return a+CastleUtil.mapSize; else if(a>=CastleUtil.mapSize) return a-CastleUtil.mapSize; else return a;
}
static String[] typeColor={"",
"","","","","#dd6666","red",
"#6666dd","#6666dd","#6666dd","blue","blue","blue","","","","",
"#66dd66","#dd6666","#6666dd","#00ff00","#ff0000","#0000ff","gray","black"};
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("cid");
	int x = action.getParameterInt("x");
	int y = action.getParameterInt("y");
	int pos = action.getParameterIntS("pos");
	if(pos != -1) {
		x = CastleUtil.pos2X(pos);
		y = CastleUtil.pos2Y(pos);
	}
	int[][] map = CastleUtil.getMap();
	byte[][] mapType = CastleUtil.getMapType();
	CastleBean castle = null;
	if(id==0){
		if(!CastleUtil.isInMap(x,y)){
			response.sendRedirect("index.jsp");
			return;
		}
		id = map[x][y];
		if(id!=0)
			castle = CastleUtil.getCastleById(id);
	} else {
		castle = CastleUtil.getCastleById(id);
		if(castle==null){
			response.sendRedirect("index.jsp");
			return;
		}
		x = castle.getX();
		y = castle.getY();
	}
	UserResBean castleRes = CastleUtil.getUserResBeanById(id);
	CastleUserBean user = null;
	int[] baseBuild = null;
	if(castle!=null) {
		user=CastleUtil.getCastleUser(castle.getUid());
		baseBuild = ResNeed.baseBuildRes[castle.getType2()];
	}

	List buildings = service.getAllBuilding(id);
	BuildingBean[] buildPos = new BuildingBean[41];
	for(int i = 0;i < buildings.size();i++){
		BuildingBean b = (BuildingBean)buildings.get(i);
		buildPos[b.getBuildPos()] = b;
	}
	
	
	long now = System.currentTimeMillis();
	if(action.hasParam("addres")){
		castleRes.increaseRes(action.getParameterInt("res1"),action.getParameterInt("res2"),action.getParameterInt("res3"),action.getParameterInt("res4"));
		response.sendRedirect("castle.jsp?id="+id);
		return;
	}
	if(action.hasParam("del")){
		CastleUtil.deleteCastleQuick(castle);
		response.sendRedirect("user.jsp?id="+castle.getUid());
		return;
	}
	if(action.hasParam("ca")){
		int ca = action.getParameterInt("ca");
		switch(ca){
		case 1:
			SqlUtil.executeUpdate("update cache_building set end_time="+now+" where cid="+id,5);
			break;
		case 2:
			SqlUtil.executeUpdate("update cache_attack set end_time="+now+" where cid="+id,5);
			break;
		case 3:
			SqlUtil.executeUpdate("update cache_merchant set end_time="+now+" where from_cid="+id,5);
			break;
		case 4:
			SqlUtil.executeUpdate("update cache_soldier set end_time="+now+" where cid="+id,5);
			break;
		case 5:
			SqlUtil.executeUpdate("update cache_soldier_smithy set end_time="+now+" where cid="+id,5);
			break;
		}
		response.sendRedirect("castle.jsp?id="+id);
		return;
	}
	
	boolean forbid = user!=null&&net.joycool.wap.util.ForbidUtil.isForbid("cast",user.getUid());
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城堡信息<br/><a href="page1.jsp?id=<%=id%>">效果图</a><br/><%if(castle!=null){
ArtBean art = CastleUtil.getCastleArt(castle.getId());
%>【<%=castle.getCastleNameWml()%>】(<%=castle.getX()%>|<%=castle.getY()%>)<%if(castle.isNatar()){%><font color=red><b>[奇迹村]</b></font><%}%><%if(castle.isPower()){%><font color=red><b>[不可侵犯]</b></font><%}%><%if(castle.isLock()){%><font color=red><b>[冻结]</b></font><%}%><%if(castle.isArt()){%><font color=red><b>[宝库]</b></font><%}%>-<a href="mcastle.jsp?cid=<%=id%>">修改</a>
<%if(castle.isArt()){%>-<a href="castle.jsp?id=<%=castle.getId()%>&del=1" onclick="return confirm('确认删除这个城堡？')">快速删除</a><%}%>
<%if(castle.isNatar()){
if(action.hasParam("addww")){
	SqlUtil.executeUpdate("insert into castle_ww set cid="+id+",uid="+castle.getUid()+",create_time=now(),name='世界奇迹'",5);
	response.sendRedirect("castle.jsp?id="+id);
	return;
}
WWBean ww = service.getWW("cid="+castle.getId());
if(ww==null){
%>-<a href="castle.jsp?cid=<%=castle.getId()%>&addww=1">添加到奇迹列表</a><%}}%><br/>
种族:<%=castle.getRaceName()%><br/>
城主:<a href="user.jsp?id=<%=castle.getUid()%>"><%=user.getNameWml()%></a><%if(forbid){%>(<font color=red><b>封禁</b></font>)<%}%><br/>
人口:<%=castleRes.getPeople()%><br/>
<a href="expand.jsp?id=<%=castle.getId()%>">绿洲(<%=castle.getExpand2()%>)|城堡(<%=castle.getExpand()%>)</a><br/>
[宝物]<%if(art!=null){%><%=art.getName()%>(<%=art.getTypeName()%>)-<a href="art.jsp?cid=<%=castle.getId()%>&id=<%=art.getId()%>">修改</a>|<a href="art.jsp?cid=<%=castle.getId()%>&del=1" onclick="return confirm('确认?')">删除</a><%}else{%><a href="art.jsp?cid=<%=castle.getId()%>">添加</a><%}%><br/>
<img src="img/1.gif"><%=castleRes.getWood(now)%>/<%=castleRes.getMaxRes()%>
<img src="img/2.gif"><%=castleRes.getStone(now)%>/<%=castleRes.getMaxRes()%>
<img src="img/3.gif"><%=castleRes.getFe(now)%>/<%=castleRes.getMaxRes()%>
<img src="img/4.gif"><%=castleRes.getGrain(now)%>/<%=castleRes.getMaxGrain()%><br/>
产量 <img src="img/1.gif"><%=castleRes.getWoodSpeed2()%>
<img src="img/2.gif"><%=castleRes.getStoneSpeed2()%>
<img src="img/3.gif"><%=castleRes.getFeSpeed2()%>
<img src="img/4.gif"><%=castleRes.getGrainRealSpeed2()%><br/>
<br/>
<%for(int i = 1; i <= 40; i ++) {
%><a href="mbuild.jsp?cid=<%=castle.getId()%>&i=<%=i%>"><%
  if(buildPos[i] !=null){
%><%=ResNeed.getTypeName(buildPos[i].getBuildType())%><%=buildPos[i].getGrade()%><%
} else if(i<=18){
%><%=ResNeed.getTypeName(baseBuild[i])%><%}else{
%>空地<%
}%></a><%
if((i)%4 == 0) {%><br/><%}else{%>.<%}%><%}%><br/>
<a href="castle2.jsp?id=<%=id%>">数据核对</a><br/><%}else {
if(mapType[x][y]>16){
OasisBean o = CastleUtil.getOasisByXY(x,y);
%>【绿洲】(<%=x%>|<%=y%>)<br/>
<%=oasisInfo3[mapType[x][y]-16]%><br/><%
if(o.getCid()!=0){
CastleBean ca = CastleUtil.getCastleById(o.getCid());
CastleUserBean us = CastleUtil.getCastleUser(ca.getUid());
%>种族:<%=ca.getRaceName()%><br/>
城主:<a href="user.jsp?id=<%=ca.getUid()%>"><%=us.getNameWml()%></a><br/>
城堡:<a href="castle.jsp?id=<%=ca.getId()%>"><%=ca.getCastleNameWml()%>(<%=ca.getX()%>|<%=ca.getY()%>)</a><br/><%

}}else{
	int[] block = ResNeed.initResBlock[mapType[x][y]];
%>【荒漠】(<%=x%>|<%=y%>)<br/>
<a href="addCastle.jsp?x=<%=x%>&y=<%=y%>">添加城堡</a><br/>
<img src="img/1.gif"> <%=block[0]%> 伐木场<br/>
<img src="img/2.gif"> <%=block[1]%> 采石场<br/>
<img src="img/3.gif"> <%=block[2]%> 铁矿场<br/>
<img src="img/4.gif"> <%=block[3]%> 粮田<br/><%}
}%>
<br/>
<%
int dis=7;
int uid = (castle==null?0:castle.getUid());
int tongId = 0;
if(user!=null)
	user.getTong();
for(int j=y-dis;j <= y+dis;j++){
int j2=formatAxis(j);
 for(int i=x-dis;i <= x+dis;i++){ 
 int i2=formatAxis(i);%><a href="castle.jsp?x=<%=i2%>&y=<%=j2%>"><%if(map[i2][j2]!=0){CastleBean c = CastleUtil.getCastleById(map[i2][j2]);
 if(c.getRace()==5){%>◆<%
 }else if(c.getUid()==uid){
%>★<%}else if(tongId!=0){
	CastleUserBean u = CastleUtil.getCastleUser(c.getUid());
	if(tongId == u.getTong()){
%>☆<%}else{%>●<%}%><%}else{%>●<%}%><%}else{

if(mapType[i2][j2]==0){
%><font color="#dddddd">□</font><%}else if(mapType[i2][j2]<=16){
%><font color="<%=typeColor[mapType[i2][j2]]%>">○</font><%}else{

OasisBean oasis = CastleUtil.getOasisByXY(i2,j2);
if(oasis.getCid()==0){
%><font color="<%=typeColor[mapType[i2][j2]]%>">△</font><%}else{%><font color="<%=typeColor[mapType[i2][j2]]%>">▲</font><%}
}
}%><%}%></a>|<%=j2%><br/><%
}%>
<%=formatAxis(x-dis)%>-<%=x%>-<%=formatAxis(x+dis)%><a href="map.jsp?x=<%=x%>&y=<%=y%>">查看全图</a><br/><br/>
<%if(castle!=null){%>
<form action="castle.jsp?id=<%=id%>" method="post">
木:<input type=text name="res1" value="500"><br/>
石:<input type=text name="res2" value="500"><br/>
铁:<input type=text name="res3" value="500"><br/>
粮:<input type=text name="res4" value="500"><br/>
<input type=submit name="addres" value="添加资源">
</form>
<%
DbOperation db = new DbOperation(5);
List buildList = cacheService.getCacheBuildingByCid(id);
int count2 = db.getIntResult("select count(id) from cache_attack where cid="+id);
int count3 = db.getIntResult("select count(id) from cache_merchant where from_cid="+id);
int count4 = db.getIntResult("select count(id) from cache_soldier where cid="+id);
int count5 = db.getIntResult("select count(id) from cache_soldier_smithy where cid="+id);
db.release();
%>建造队列:<%=buildList.size()%>-<a href="castle.jsp?id=<%=id%>&ca=1" onclick="return confirm('确认?')">快速完成</a><%

Iterator iterator = buildList.iterator();
	while(iterator.hasNext()){
		BuildingThreadBean cacheBuildingBean = (BuildingThreadBean)iterator.next();
%>-<%=ResNeed.getTypeName(cacheBuildingBean.getType())%><%=cacheBuildingBean.getGrade()%>(<%=cacheBuildingBean.getTimeLeft()%>)<%}

%><br/>
攻击队列:<%=count2%>-<a href="castle.jsp?id=<%=id%>&ca=2" onclick="return confirm('确认?')">快速完成</a><br/>
商人队列:<%=count3%>-<a href="castle.jsp?id=<%=id%>&ca=3" onclick="return confirm('确认?')">快速完成</a><br/>
造兵队列:<%=count4%>-<a href="castle.jsp?id=<%=id%>&ca=4" onclick="return confirm('确认?')">快速完成</a><br/>
升级攻防队列:<%=count5%>-<a href="castle.jsp?id=<%=id%>&ca=5" onclick="return confirm('确认?')">快速完成</a><br/><%}%>
<%@include file="bottom.jsp"%>
</html>