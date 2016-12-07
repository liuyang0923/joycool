<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!
	private static java.text.DecimalFormat numFormat = new java.text.DecimalFormat("0.0");
	static CacheService cacheService = CacheService.getInstance();
	static CastleService castleService = CastleService.getInstance();
%><%
	
	
	SoldierAction action = new SoldierAction(request);
	action.hero();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="指挥官"><p><%@include file="top.jsp"%>
<%if(action.isResult("tip")){
	%><%=action.getTip()%><br/><%

}else{
	CastleUserBean user2 = action.getCastleUser();
	HeroBean hero = user2.getHero();
	
	if(hero != null){
		int attack = hero.getAttack();
		if(attack == 0)
			attack = hero.getAttack2();
		int point = hero.getFreePoint();
%>【<a href="heroR.jsp"><%=StringUtil.toWml(hero.getName())%></a>】(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%><br/>
攻击力:<%=attack%>(<%=hero.getStat1()%>)<%if(point>0&&hero.getStat1()<100){%><a href="hero.jsp?st=1">+1</a><%}%><br/>
防御力:<%=hero.getDefense()%>/<%=hero.getDefense2()%>(<%=hero.getStat2()%>)<%if(point>0&&hero.getStat2()<100){%><a href="hero.jsp?st=2">+1</a><%}%><br/>
军队攻击力加乘:<%=numFormat.format(hero.getAttackX()*100)%>%(<%=hero.getStat3()%>)<%if(point>0&&hero.getStat3()<100){%><a href="hero.jsp?st=3">+1</a><%}%><br/>
军队防御力加乘:<%=numFormat.format(hero.getDefenseX()*100)%>%(<%=hero.getStat4()%>)<%if(point>0&&hero.getStat4()<100){%><a href="hero.jsp?st=4">+1</a><%}%><br/>
恢复量:<%=hero.getHealthSpeed()%>/天(<%=hero.getStat5()%>)<%if(point>0&&hero.getStat5()<100){%><a href="hero.jsp?st=5">+1</a><%}%><br/>
经验值:<%=hero.getExpPercent()%>%(剩余分配点<%=point%>)<br/>
<%if(hero.getRank()==0){%>在等级0时可以重置指挥官的分配点数.<br/>
<a href="hero.jsp?st=6">&gt;&gt;重置指挥官的分配点</a><br/>
<br/>
你的指挥官当前生命值为<%=(int)(hero.getHealth(System.currentTimeMillis()))%>%<br/><br/><%}


	}else{	// 没有活着的英雄，训练新的
		int uid = user2.getUid();
		List heroes = castleService.getHeroList("uid="+uid);	// 拥有的所有英雄列表
		
if(heroes.size()>0){
	%>【请挑选一名指挥官复出】<br/><%
	for(int i=0;i<heroes.size();i++){
		HeroBean h = (HeroBean)heroes.get(i);
		int[] res = h.getReviveRes();
%><%=StringUtil.toWml(h.getName())%>(<%=h.getHeroSoldier().getSoldierName()%>)等级<%=h.getRank()%><br/>
所需资源:木<%=res[0]%>|石<%=res[1]%>|铁<%=res[2]%>|粮<%=res[3]%><br/>
所需时间:<%=DateUtil.formatTimeInterval2(res[4])%>-<a href="hero.jsp?a=2&amp;id=<%=h.getId()%>">复出</a>|<a href="heroD.jsp?id=<%=h.getId()%>">解散</a><br/><%
	}
}	
if(heroes.size()<3){
		%>【请挑选一名士兵训练成指挥官】<br/><%

		CastleBean castle = action.getCastle();
		CastleArmyBean army = CastleUtil.getCastleArmy(castle.getId());
		SoldierResBean[] ts = ResNeed.getSoldierTs(castle.getRace());
		boolean no = true;
		for(int i = 1;i<=5; i++) {
			SoldierResBean soldier = (SoldierResBean)ts[i];
			if(army.getCount(i)==0) continue;
			no=false;
int[] res = HeroBean.getReviveRes(soldier, 0);
%>[<%=soldier.getSoldierName()%>]所需资源:木<%=res[0]%>|石<%=res[1]%>|铁<%=res[2]%>|粮<%=res[3]%><br/>
所需时间:<%=DateUtil.formatTimeInterval2(res[4]/2)%>-<a href="hero.jsp?a=1&amp;t=<%=i%>">开始训练</a><br/><%
		}
		if(no){%>(没有可以训练的士兵)<br/><%}
}
	%>指挥官的粮食消耗:6每小时<br/><%

	}
}
%><a href="fun.jsp?t=37">返回开采所</a><br/><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>