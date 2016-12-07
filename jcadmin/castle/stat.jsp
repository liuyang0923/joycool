<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
static String[] titles = {"城主","联盟","进攻","防御","抢夺","英雄","奇迹"};
static int[] cols = {5,4,5,5,5,5,6,6};
static int COUNT_PER_PAGE = 20;
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
				id = map[x][y];
			}
		}else{
			session.setAttribute("x", new Integer(x));
			session.setAttribute("y", new Integer(y));
			id = map[x][y];
		}
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
	long now = System.currentTimeMillis();

	CastleUserBean castleUser = null;
	if(castle!=null)
		castleUser = CastleUtil.getCastleUser(castle.getUid());

	int type = action.getParameterInt("t");
	CastleUtil.statPeople();
	int cur = action.getParameterIntS("p");
	if(cur==-1){
		if(castleUser!=null){
			int userCur = 0;
			if(type==0)
				userCur = service.getCastleCurArray(castleUser.getUid());
			else if(type==1 && castleUser.getTong()!=0)
				userCur = service.getTongCurArray(castleUser.getTong());
			else if(type==2)
				userCur = service.getAttackCurArray(castleUser.getUid());
			else if(type==3)
				userCur = service.getDefenseCurArray(castleUser.getUid());
			else if(type==4)
				userCur = service.getRobCurArray(castleUser.getUid());
			else if(type==5)
				userCur = service.getHeroCurArray(castleUser.getUid());
			//System.out.println(userCur);
			if(userCur > 0) {
				cur = (userCur % COUNT_PER_PAGE == 0) ? (userCur / COUNT_PER_PAGE - 1) : (userCur / COUNT_PER_PAGE);
			} else{
				cur = 0;
			}
		}else{
			cur = 0;
		}
	}
	int start = cur * COUNT_PER_PAGE;
	int limit = COUNT_PER_PAGE + 1;
	List list = null;
	
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd"><html>
	<head><meta http-equiv="cache-control" content="max-age=0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="0">
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body><%@include file="pages.jsp"%>

<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2"><h1>排行榜</h1>
<p class="txt_menue">
<a href="stat.jsp">城主</a> |
<a href="stat.jsp?t=1">联盟</a> |
<a href="stat.jsp?t=2">进攻</a> |
<a href="stat.jsp?t=3">防御</a> |
<a href="stat.jsp?t=4">抢夺</a> |
<a href="stat.jsp?t=5">英雄</a> |
<a href="stat.jsp?t=6">奇迹</a></p>
<form method="post" action="stat.jsp?t=<%=type%>"><p>
<table cellspacing="1" cellpadding="2" class="tbg">
<tr>
<td class="rbg" colspan="<%=cols[type]%>"><a name="h2"></a><%=titles[type]%>排行榜</td>
</tr>

<%if(type==0){
	list = service.getCastleArray(start, limit);
	int count = list.size() > COUNT_PER_PAGE ? COUNT_PER_PAGE : list.size();
%>

<tr>
<td width="6%">&nbsp;</td><td width="35%">城主</td><td width="25%">联盟</td><td width="20%">人口</td><td width="14%">城堡</td>
</tr><%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
	CastleUserBean u = CastleUtil.getCastleUser(((Integer)obj[1]).intValue());
%><tr><%if(castle==null||castle.getUid()!=u.getUid()){%>
<td align="right" class="nbr"><%=obj[0]%>.&nbsp;</td>
<td class="s7"><a href="page5.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[3])%></a></td>
<td><%if(u.getTong()==0){%> - <%}else{%><a href="page4.jsp?id=<%=u.getTong()%>"><%=StringUtil.toWml(CastleUtil.getTong(u.getTong()).getName())%></a><%}%></td>
<td><%=obj[2]%></td>
<td><%=obj[4]%></td><%}else{%>
<td class="li ou nbr" align="right"><%=obj[0]%>.&nbsp;</td>
<td class="s7 ou"><a href="page5.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[3])%></a></td>
<td class="ou"><%if(u.getTong()==0){%> - <%}else{%><a href="page4.jsp?id=<%=u.getTong()%>"><%=StringUtil.toWml(CastleUtil.getTong(u.getTong()).getName())%></a><%}%></td>
<td class="ou"><%=obj[2]%></td>
<td class="re ou"><%=obj[4]%></td><%}%>
</tr><%}%>


<%}else if(type==1){
	list = service.getTongArray(start, limit);
	int count = list.size() > COUNT_PER_PAGE ? COUNT_PER_PAGE : list.size();
%>
<tr>
<td width="6%">&nbsp;</td><td>联盟</td><td width="20%">玩家</td><td width="24%">总人口</td>
</tr><%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
%><tr><%if(castleUser==null||castleUser.getTong()!=((Integer)obj[1]).intValue()){%>
<td align="right" class="nbr"><%=obj[0]%>.&nbsp;</td>
<td class="s7"><a href="page4.jsp?id=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[3])%></a></td>
<td><%=obj[4]%></td>
<td><%=obj[2]%></td><%}else{%>
<td class="li ou nbr" align="right"><%=obj[0]%>.&nbsp;</td>
<td class="s7 ou"><a href="page4.jsp?id=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[3])%></a></td>
<td class="ou"><%=obj[4]%></td>
<td class="re ou"><%=obj[2]%></td><%}%>
</tr><%}%>


<%}else if(type==5){
	list = service.getHeroArray(start, limit);
	int count = list.size() > COUNT_PER_PAGE ? COUNT_PER_PAGE : list.size();
%>
<tr>
<td width="2%">&nbsp;</td><td>英雄</td><td>玩家</td><td width="40">等级</td><td width="80">经验值</td>
</tr><%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
	SoldierResBean so = (SoldierResBean)obj[6];
	int imgId = so.getId();
	if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
%><tr><%if(castleUser==null||castleUser.getUid()!=((Integer)obj[1]).intValue()){%>
<td align="right" class="nbr"><%=obj[0]%>.&nbsp;</td>
<td class="s7"><img class="unit u<%=imgId%>" src="img/blank.gif" title="<%=so.getSoldierName()%>" alt="<%=so.getSoldierName()%>"/>&emsp;<%=obj[2]%></td>
<td><a href="page5.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[5])%></a></td>
<td><%=obj[4]%></td>
<td><%=obj[3]%></td><%}else{%>
<td class="li ou nbr" align="right"><%=obj[0]%>.&nbsp;</td>
<td class="s7 ou"><img class="unit u<%=imgId%>" src="img/blank.gif" title="<%=so.getSoldierName()%>" alt="<%=so.getSoldierName()%>"/>&emsp;<%=obj[2]%></td>
<td class="ou"><a href="page5.jsp?uid=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[5])%></a></td>
<td class="ou"><%=obj[4]%></td>
<td class="re ou"><%=obj[3]%></td><%}%>
</tr><%}%>

<%}else if(type==6){
	list = service.getWWList("1 order by lvl desc,id asc");
	int count = list.size();
%>
<tr>
<td width="2%">&nbsp;</td><td>名称</td><td>玩家</td><td width="80">联盟</td><td width="40">等级</td>
</tr><%for(int i=0;i<count;i++){
	WWBean obj = (WWBean)list.get(i);
	CastleBean c = CastleUtil.getCastleById(obj.getCid());
	CastleUserBean user = CastleUtil.getCastleUser(c.getUid());
String tongName ="-";
if(user.getTong()!=0)
	tongName = StringUtil.toWml(CastleUtil.getTong(user.getTong()).getName());
%><tr><%if(castleUser==null||castleUser.getUid()!=c.getUid()){%>
<td align="right" class="nbr"><%=i+1%>.&nbsp;</td>
<td class="s7"><a href="page1.jsp?id=<%=c.getId()%>"><%=StringUtil.toWml(obj.getName())%></a></td>
<td><a href="page5.jsp?uid=<%=c.getUid()%>"><%=user.getNameWml()%></a></td>
<td><%=tongName%></td>
<td><%=obj.getLevel()%></td><%}else{%>
<td class="li ou nbr" align="right"><%=i+1%>.&nbsp;</td>
<td class="s7 ou"><a href="page1.jsp?id=<%=c.getId()%>"><%=StringUtil.toWml(obj.getName())%></a></td>
<td class="ou"><a href="page5.jsp?uid=<%=c.getUid()%>"><%=user.getNameWml()%></a></td>
<td class="ou"><%=tongName%></td>
<td class="re ou"><%=obj.getLevel()%></td><%}%>
</tr><%}%>


<%}else if(type==2||type==3||type==4){
	if(type==2)
		list = service.getAttackArray(start, limit);
	else if(type==3)
		list = service.getDefenseArray(start, limit);
	else
		list = service.getRobArray(start, limit);
	int count = list.size() > COUNT_PER_PAGE ? COUNT_PER_PAGE : list.size();
%>
<tr>
<td width="6%">&nbsp;</td><td width="35%">城主</td><td width="20%">人口</td><td width="19%">城堡</td><td width="20%">点数</td>
</tr><%for(int i=0;i<count;i++){
	Object[] obj = (Object[])list.get(i);
	CastleUserBean u = CastleUtil.getCastleUser(((Integer)obj[1]).intValue());
%><tr><%if(castle==null||castle.getUid()!=u.getUid()){%>
<td align="right" class="nbr"><%=obj[0]%>.&nbsp;</td>
<td class="s7"><a href="page5.jsp?id=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[2])%></a></td>
<td><%=u.getPeople()%></td>
<td><%=u.getCastleCount()%></td>
<td><%=obj[3]%></td><%}else{%>
<td class="li ou nbr" align="right"><%=obj[0]%>.&nbsp;</td>
<td class="s7 ou"><a href="page5.jsp?id=<%=obj[1]%>"><%=StringUtil.toWml((String)obj[2])%></td>
<td class="ou"><%=u.getPeople()%></td>
<td class="ou"><%=u.getCastleCount()%></td>
<td class="re ou"><%=obj[3]%></td><%}%>
</tr><%}%>



<%}%>

</table></p>
<p><table class="tbg" cellspacing="1" cellpadding="0"><tr><td>
<table class="tbg" cellspacing="0" cellpadding="2">
<tr>
<td width="60"><b>查找：</b></td>
<td width="40">排名：</td>
<td width="40"><input class="fm f30" type="Text" name="p" value="<%=cur%>" maxlength="5"></td>

<td width="40" align="center"><i>或者</i></td>
<td width="40">名字</td>
<td width="40"><input class="fm f80" type="Text" name="spieler" value="" size="10" maxlength="20"></td>

<td class="c r7"><%if(cur > 0) {%><a href="stat.jsp?t=<%=type%>&p=<%=cur-1%>">&laquo; 后退</a><%}else{%>&laquo; 后退<%}%> | <%if(list.size() > COUNT_PER_PAGE) {%><a href="stat.jsp?t=<%=type%>&p=<%=cur+1%>">往前 &raquo;</a><%}else{%>往前 &raquo;<%}%></td>
</tr>

</table>
</td></tr>
</table></p><p><input type="image" value="ok" border="0" name="s1" src="img/ok.gif" width="50" height="20" ></input></p>
</div></div></div></div>
</body>
</html>