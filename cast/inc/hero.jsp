<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%
int expand2 = action.getCastle().getExpand2();
CastleUserBean u = action.getCastleUser();
HeroBean hero = u.getHero();

if(hero==null){
	int uid = action.getUserBean().getId();
	List cacheList = cacheService.getCacheCommonList2(uid,5);
	if(cacheList.size()>0){
		Iterator iterator = cacheList.iterator();
		while(iterator.hasNext()){
			CommonThreadBean cacheBean = (CommonThreadBean)iterator.next();
			if(cacheBean.getValue()<0){
%>指挥官(<%=ResNeed.getSoldierRes(u.getRace(),-cacheBean.getValue()).getSoldierName()%>)正在训练中.<br/>
剩余时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
			}else{
				hero = castleService.getHero("id="+cacheBean.getValue());
				if(hero==null)continue;
%><%=StringUtil.toWml(hero.getName())%>(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%>正在训练中.<br/>
剩余时间<%=cacheBean.getTimeLeft()%>结束于<%=DateUtil.formatTime(cacheBean.getEndTime())%><br/><%
			}
		}
	} else {
		%><a href="hero.jsp">训练/复活指挥官</a><br/><%
		
	}
}else{
	%><%=StringUtil.toWml(hero.getName())%>(<%=hero.getHeroSoldier().getSoldierName()%>)等级<%=hero.getRank()%><br/>
当前生命值为<%=(int)(hero.getHealth(System.currentTimeMillis()))%>%<br/>
<a href="hero.jsp">查看详细信息</a><br/><%
	}

%>这个城堡占领了<%=expand2%>片<%if(expand2!=0){%><a href="oasis.jsp">绿洲</a><%}else{%>绿洲<%}%><br/><%

if(building.getGrade()<10&&expand2==0){
%>当开采所达到10级的时候可以开始占领一片绿洲<br/><%
}else if(building.getGrade()<15&&expand2==1){
%>当开采所达到15级的时候可以开始占领第二片绿洲<br/><%
%><%}else if(building.getGrade()<20&&expand2==2){
%>当开采所达到20级的时候可以开始占领第三片绿洲<br/><%
}else if(expand2==3){
%>三片绿洲的占领已经完成<br/><%}
else {
%>现在可以去占领一片新的绿洲了<br/><%}%>