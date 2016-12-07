<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@ page import="net.joycool.wap.cache.CacheManage"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
private static java.text.DecimalFormat numFormat = new java.text.DecimalFormat("0.0");
static String[] statusName = {"活动","待命","","训练"};
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
	if(action.hasParam("kill")){
		int killId = action.getParameterInt("kill");
		HeroBean hero = user.getHero();
		if(hero != null && hero.getId()==killId){
			user.deleteHero();
		}else{
			hero = service.getHero("id=" + killId);
		}
		if(hero!=null){
			hero.setStatus(HeroBean.STATUS_DEAD);
			hero.setTime(now);
			hero.setHealth(0);
			service.updateHeroSimple(hero);
		}
	}
	if(action.hasParam("live")){
		int liveId = action.getParameterInt("live");
		HeroBean hero = service.getHero("id=" + liveId);
		if(hero!=null){
			hero.setStatus(HeroBean.STATUS_ALIVE);
			hero.setHealth(100f);
			service.updateHeroSimple(hero);
			user.setHero(hero);
		}
	}

%><html>
	<head>
	</head>
<link href="common2.css" rel="stylesheet" type="text/css">
	<body>
<div id="lmidall"><div id="lmidlc"><div id="lmid1"><div id="lmid2" style="width:600;"><h1>英雄信息</h1>
<p>【<a href="user.jsp?id=<%=id%>"><%=StringUtil.toWml(user.getName())%></a>】</p>
<%
	CommonThreadBean train = null;
	List cacheList = cacheService.getCacheCommonList2(id,5);
	if(cacheList.size()>0){
	%><table cellspacing="1" cellpadding="2" class="tbg"><tr><td class="rbg">英雄</td><td class="rbg">等级</td><td class="rbg">剩余时间</td><td class="rbg">结束于</td></tr><%
		Iterator iterator = cacheList.iterator();
		while(iterator.hasNext()){
			CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
			if(cacheBean.getValue()<0){
				int imgId = -cacheBean.getValue();
				if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
%><tr><td><img class="unit u<%=imgId%>" src="img/blank.gif" title="<%=ResNeed.getSoldierRes(user.getRace(),-cacheBean.getValue()).getSoldierName()%>" alt="<%=ResNeed.getSoldierRes(user.getRace(),-cacheBean.getValue()).getSoldierName()%>"/>&emsp;英雄
</td><td>-</td><td><%=cacheBean.getTimeLeft()%></td><td><%=DateUtil.formatTime(cacheBean.getEndTime())%></td></tr><%
			}else{
				HeroBean hero = service.getHero("id="+cacheBean.getValue());
				if(hero==null)continue;
				int imgId = hero.getType();
				if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
				train = cacheBean;
%><tr><td><img class="unit u<%=imgId%>" src="img/blank.gif" title="<%=hero.getHeroSoldier().getSoldierName()%>" alt="<%=hero.getHeroSoldier().getSoldierName()%>"/>&emsp;<%=StringUtil.toWml(hero.getName())%></td><td>等级<%=hero.getRank()%>
</td><td><%=cacheBean.getTimeLeft()%></td><td><%=DateUtil.formatTime(cacheBean.getEndTime())%></td></tr><%
			}
		}
		%></table><br/><%
	}

HeroBean hero = user.getHero();
%><table cellspacing="1" cellpadding="2" class="tbg"><tr><td class="rbg">英雄</td><td class="rbg">等级</td><td class="rbg">资源</td><td class="rbg">时间</td></tr><%
List heroes = service.getHeroList("uid="+id);
for(int i=0;i<heroes.size();i++){
	HeroBean h = (HeroBean)heroes.get(i);
	int imgId = h.getType();
	if(imgId%10==4||imgId%10==5) imgId++; else if(imgId%10==6) imgId -=2;
	int[] res = h.getReviveRes();
	boolean revive = train!=null&&h.getId()==train.getValue();
	boolean alive = hero!=null&&h.getId()==hero.getId();
	int attack = h.getAttack();
	if(attack == 0)
		attack = h.getAttack2();
	int point = h.getFreePoint();
		
%><tr><td rowspan=<%if(revive||alive){%>8<%}else{%>7<%}%>><img class="unit u<%=imgId%>" src="img/blank.gif" title="<%=h.getHeroSoldier().getSoldierName()%>" alt="<%=h.getHeroSoldier().getSoldierName()%>"/><br/><br/><%=StringUtil.toWml(h.getName())%>
<%if(hero==null&&!revive){%><br/><br/><a href="hero.jsp?id=<%=id%>&live=<%=h.getId()%>" onclick="return confirm('确认复活?')">复活</a><%}else if(alive){%><br/><br/><a href="hero.jsp?id=<%=id%>&kill=<%=h.getId()%>" onclick="return confirm('确认死亡?')">死亡</a><%}%></td><td>等级<%=h.getRank()%></td>
<td><img src="img/1.gif"><%=res[0]%><img src="img/2.gif"><%=res[1]%><img src="img/3.gif"><%=res[2]%><img src="img/4.gif"><%=res[3]%></td><td><img src="img/clock.gif"><%=DateUtil.formatTimeInterval2(res[4])%></td></tr><%
		if(revive){
		%><tr><td colspan=4>剩余 <%=train.getTimeLeft()%>&emsp;结束于 <%=DateUtil.formatTime(train.getEndTime())%> (<%=statusName[h.getStatus()]%>)</td></tr><%
		}
		if(alive){
		%><tr><td colspan=4>生命值 <%=numFormat.format(hero.getHealth(now))%>% (<%=statusName[h.getStatus()]%>)</td></tr><%
		}
%><tr><td>攻击力</td><td><%=attack%></td><td><%=h.getStat1()%></td></tr>
<tr><td>防御力</td><td><%=h.getDefense()%>/<%=h.getDefense2()%></td><td><%=h.getStat2()%></td></tr>
<tr><td>团体攻击力</td><td><%=numFormat.format(h.getAttackX()*100)%>%</td><td><%=h.getStat3()%></td></tr>
<tr><td>团体防御力</td><td><%=numFormat.format(h.getDefenseX()*100)%>%</td><td><%=h.getStat4()%></td></tr>
<tr><td>恢复量</td><td><%=h.getHealthSpeed()%>/天</td><td><%=h.getStat5()%></td></tr>
<tr><td>经验值</td><td><%=h.getExpPercent()%>%&emsp;(<%=h.getExp()%>)</td><td><%=point%></td></tr><%
	}
%>
</table>
<%@include file="bottom.jsp"%>
</div></div></div></div>
</html>