<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
static CastleMessage getMessage2(ResultSet rs) throws Exception {
	CastleMessage bean = new CastleMessage(rs.getString("content"), rs.getInt("id"), rs.getTimestamp("time"), rs.getInt("uid"));
	bean.setDetail(rs.getString("detail"));
	bean.setType(rs.getInt("type"));
	bean.setTongId(rs.getInt("tong_id"));
	return bean;
}
static List getCastleMessageByUid(int uid, int start, int limit) {
	List list = new ArrayList();
	DbOperation db = new DbOperation(5);
	String query = "SELECT * FROM castle_message WHERE uid = "+uid+" order by id desc limit "+start+","+limit;
	
	try {
		ResultSet rs = db.executeQuery(query);
		while(rs.next()) {
			list.add(getMessage2(rs));
		}
	} catch (Exception e) {
		e.printStackTrace();
		return list;
	}finally{
		db.release();
	}
	
	
	return list;
}
// userid, 城堡列表
static void deleteAccount(int id, List list){
	DbOperation db = new DbOperation(5);
	for(int i = 0; i < list.size() ; i++) {
		CastleBean castle = (CastleBean)list.get(i);
		CastleUtil.deleteCastle(castle.getX(),castle.getY());
		int cid = castle.getId();
		db.executeUpdate("delete from castle_user_resource where id = " + cid);
		db.executeUpdate("delete from castle where id = " + cid);
		db.executeUpdate("delete from castle_building where cid = " + cid);
		CacheManage.castle.srm(cid);
		CacheManage.castleUserRes.srm(cid);
		db.executeUpdate("delete from castle_soldier_smithy where cid = " + cid);
		db.executeUpdate("delete from castle_hidden_soldier where cid = " + cid);
		db.executeUpdate("delete from castle_soldier where cid = " + cid);
		db.executeUpdate("delete from castle_soldier where at = " + cid);
		db.executeUpdate("delete from cache_attack where cid = " + cid);
		db.executeUpdate("delete from cache_merchant where from_cid = " + cid);
	}
	db.executeUpdate("delete from castle_user where uid="+id);
	CacheManage.castleUser.srm(id);
	db.release();
}

static void changeRace(int uid, int race){
	CastleUserBean user = CastleUtil.getCastleUser(uid);
	if(user==null||user.getRace()==race||user.getCreateTime()>DateUtil.parseTime("2009-04-01 00:00:00").getTime())
		return;
	int oldRace = user.getRace();
	List list = service.getCastleList(uid);
	DbOperation db = new DbOperation(5);
	db.executeUpdate("update castle_user set race="+race+" where uid = " + uid);
	user.setRace(race);
	for(int i = 0; i < list.size() ; i++) {
		CastleBean castle = (CastleBean)list.get(i);
		int cid = castle.getId();
		db.executeUpdate("update castle set race="+race+" where id = " + cid);
		db.executeUpdate("delete from castle_soldier_smithy where cid = " + cid);
		db.executeUpdate("delete from cache_attack where cid = " + cid);
		db.executeUpdate("delete from cache_soldier where cid = " + cid);
		db.executeUpdate("delete from castle_soldier where cid = " + cid);
		db.executeUpdate("delete from castle_hidden_soldier where cid = " + cid);
		CacheManage.castle.srm(cid);
		CacheManage.castleSmithy.srm(cid);
		
		List buildings = service.getAllBuilding(cid);
		for(int j = 0;j < buildings.size();j++){
			BuildingBean b = (BuildingBean)buildings.get(j);
			if(b.getBuildType()==6||b.getBuildType()==7||b.getBuildType()==34||b.getBuildType()==26||b.getBuildType()==28||b.getBuildType()==29){
				CastleUtil.destroyBuilding(uid,b,0);
			}
		}
		List armyList = service.getCastleArmyList(cid);
		for(int j = 0;j < armyList.size();j++) {
			CastleArmyBean army = (CastleArmyBean)armyList.get(j);
			CastleUtil.removeArmyPeople(oldRace, army);
			db.executeUpdate("delete from castle_soldier where id = " + army.getId());
		}
		CastleArmyBean army = CastleUtil.getCastleArmy(cid);
		if(army!=null){
			CastleUtil.removeArmyPeople(oldRace, army);
			db.executeUpdate("delete from castle_soldier where id = " + army.getId());
		}
	}
	db.release();
}
%><%
	CustomAction action = new CustomAction(request);
	long now=System.currentTimeMillis();
	if(action.hasParam("massdelete")){
		
		List idList = StringUtil.toInts(request.getParameter("ids"));
		for(int i = 0;i < idList.size();i++){
			int id = ((Integer)idList.get(i)).intValue();
			if(id<=0) continue;
			CastleUserBean user = CastleUtil.getCastleUser(id);
			if(user==null) continue;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("insert into cache_common set uid=" + id + ",end_time=0,type=3,cid=0");
			db.release();
//			List list = service.getCastleList(id);
//			deleteAccount(id, list);
		}
		response.sendRedirect("index.jsp");
		return;
	}
	if(action.hasParam("changerace")){
		int id = action.getParameterInt("id");
		int race = action.getParameterInt("race");
		if(id!=0 && race >0 && race < 4) {
			changeRace(id, race);
			response.sendRedirect("user.jsp?id=" + id);
			return;
		}
	}
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("uid");
	CastleUserBean user = CastleUtil.getCastleUser(id);
	
	if(action.hasParam("gold")){
		int gold = action.getParameterInt("gold");
		if(user != null && gold <= 300 && gold >= -300) {
			user.setGold(user.getGold() + gold);
			SqlUtil.executeUpdate("update castle_user set quest_status=1,gold=" + user.getGold() + " where uid=" + user.getUid(), 5);
			SqlUtil.executeUpdate("insert into castle_gold set uid=" + user.getUid() + ",gold=" + gold + ",type=10,create_time=now(),`left`=" + user.getGold(), 5);
			response.sendRedirect("user.jsp?id=" + id);
			return;
		}
	}
	
	if(user==null){
		response.sendRedirect("index.jsp");
		return;
	}
	TongBean tongBean = null;
	List list = service.getCastleList(id);
	if(user.getTong() > 0) {
		tongBean = CastleUtil.getTong(user.getTong());
	}
	if(action.hasParam("name")){
		String name = request.getParameter("name");
		user.setName(name);
		SqlUtil.executeUpdate("update castle_user set name='"+StringUtil.toSql(name)+"' where uid="+id,5);
	}
	if(action.hasParam("delete")){
		DbOperation db = new DbOperation(5);
		db.executeUpdate("insert into cache_common set uid=" + id + ",end_time=0,type=3,cid=0");
		db.release();
		response.sendRedirect("index.jsp");
		return;
	}
	List msgList = getCastleMessageByUid(id, 0, 20);
	boolean forbid = user!=null&&net.joycool.wap.util.ForbidUtil.isForbid("cast",id);
	List cacheList = cacheService.getCacheCommonList2(id,3);
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城主信息<br/><br/>
【<%=StringUtil.toWml(user.getName())%>】<%if(forbid){%>(<font color=red><b>封禁</b></font>)<%}%>-<a href="muser.jsp?id=<%=user.getUid()%>">修改</a><br/>
<%if(cacheList.size()>0){
Iterator iterator = cacheList.iterator();
	while(iterator.hasNext()){
		CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
%><font color=red><b>删号中剩余<%=cacheBean.getTimeLeft()%></b></font><br/><%}
}%>
种族:<%=user.getRaceName()%><form action="user.jsp?changerace=1&id=<%=id%>" method="post"><select name="race"><%for(int i=1;i<4;i++){%><option value="<%=i%>" <%if(i==3){%>selected<%}%>><%=ResNeed.raceNames[i]%></option><%}%></select><input type=submit value="修改种族" onclick="return confirm('确认要修改该玩家的种族吗?')"></form><br/>
创建时间<%=DateUtil.formatDate2(user.getCreateTime())%>|
冻结时间<%=DateUtil.formatDate2(user.getLockTime())%><br/>
金币:<%=user.getGold()%>个<br/>
任务完成:<%=user.getQuest()%>(<%if(user.getQuestStatus()==0){%>未<%}%>完成)<br>
总人口:<%=user.getPeople()%>|文明度:<%=user.getCivil(now)%><br/>
<%if(tongBean != null) {%>所属联盟:<a href="tong.jsp?id=<%=tongBean.getId()%>"><%=StringUtil.toWml(tongBean.getName()) %></a><br/><%}%>
<%if(user.getInfo2().length()!=0){%><%=user.getInfo2()%><br/><%}%>
<a href="../user/queryUserInfo.jsp?id=<%=user.getUid() %>">用户信息</a><br/>
==城堡列表(<%=user.getCastleCount()%>)==<br/>
<table class="tbg" cellpadding="2" cellspacing="1"><tr class="rbg"><td></td><td>名字</td><td>坐标</td><td>人口</td><td></td></tr>
<%for(int i = 0; i < list.size() ; i++) {
	CastleBean castleBean = (CastleBean)list.get(i);
	UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
%><tr><td><%if(user.getCastleCount()!=1&&user.getMain()==castleBean.getId()){%>*<%}
%><%=i+1 %></td><td><a href="castle.jsp?id=<%=castleBean.getId() %>"><%=StringUtil.toWml(castleBean.getCastleName()) %></a></td><td>(<%=castleBean.getX() %>|<%=castleBean.getY() %>)</td><td><%=userRes.getPeople() %></td>
<td><a href="castle2.jsp?id=<%=castleBean.getId() %>">核对</a></td></tr>
<%} %></table><br/><%
HeroBean hero = user.getHero();
if(hero!=null){
%><%=StringUtil.toWml(hero.getName())%>(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%>(<%=hero.getHealth(now)%>)-<%
}%><a href="hero.jsp?id=<%=id%>">查看英雄信息</a><br/>

<form action="user.jsp?id=<%=id%>" method="post">
<input type=text name="name" value="">
<input type=submit value="修改城主名">
</form><%if(group.isFlag(0)){%>
<form action="user.jsp?id=<%=id%>" method="post">
<input type=text name="gold" value="">
<input type=submit value="增加金币">
</form><%}%>
<br/>
<a href="user2.jsp?id=<%=id%>">数据核对</a><br/>
<a href="report.jsp?id=<%=id%>">信息列表</a><br/><br/>
!!!<a href="user.jsp?delete=1&amp;id=<%=id%>" onclick="return confirm('确认删除该帐号?')">彻底删除帐号</a>(慎用)<br/>
<%@include file="bottom.jsp"%>
</html>