<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.List"%><%!
static CacheService cacheService = CacheService.getInstance();%><%
	

	CastleAction action = new CastleAction(request);
	int cid = action.getCastle().getId();
	int id=action.getParameterInt("id");
	AttackThreadBean attack = null;
	if(id != 0)
		attack = cacheService.getCacheAttack(id);
	
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="指令"><p><%@include file="top.jsp"%>

<%if(attack==null){%>军队不存在<br/><%

}else{ 

	int race = CastleUtil.getCastleById(attack.getCid()).getRace();
	SoldierResBean[] so = ResNeed.getSoldierTs(race);

if(attack.getCid()==cid){

	if(attack.getToCid()==cid){
		if(attack.getFromCid()==0){
%>返回自<a href="search.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY()%>">(<%=attack.getX() %>|<%=attack.getY()%>)</a>的<%=attack.getTypeName()%><br/><%
		} else {
			CastleBean castle = CastleUtil.getCastleById(attack.getFromCid());
%>返回自<%if(castle==null){%>[?]<%}else{%><a href="search.jsp?x=<%=castle.getX() %>&amp;y=<%=castle.getY()%>"><%=castle.getCastleNameWml()%>(<%=castle.getX() %>|<%=castle.getY()%>)</a><%}%>的<%=attack.getTypeName()%><br/><%
		}

	} else if(attack.getType()==4){

%>在<a href="around.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY()%>">(<%=attack.getX() %>|<%=attack.getY()%>)</a>建立一个新的城堡<br/><%

	} else if(attack.getToCid()==0){	// tocid=0除了type==4的创建，就只有抢夺绿洲了

%>对绿洲<a href="search.jsp?x=<%=attack.getX() %>&amp;y=<%=attack.getY()%>">(<%=attack.getX() %>|<%=attack.getY()%>)</a>的<%=attack.getTypeName()%><br/><%

	} else {

		CastleBean castle = CastleUtil.getCastleById(attack.getToCid());
%>对<a href="search.jsp?x=<%=castle.getX() %>&amp;y=<%=castle.getY()%>"><%=castle.getCastleNameWml()%>(<%=castle.getX() %>|<%=castle.getY()%>)</a>的<%=attack.getTypeName()%><br/><%

}%>
兵力:<%=CastleUtil.getSoldierString(race,attack.getSoldierCountList(),attack.getHero())%><br/>
剩余<%=DateUtil.formatTimeInterval2(attack.getEndTime())%>到达于<%=DateUtil.formatTime(attack.getEndTime())%>
<%
	if(System.currentTimeMillis() - attack.getStartTime() <= 90000){
		%>-<a href="cancelA.jsp?id=<%=attack.getId()%>">取消</a><%
	}
	%><br/><%

}else if(attack.getToCid()==cid){

	CastleBean castle = CastleUtil.getCastleById(attack.getFromCid());
%>来自<a href="search.jsp?x=<%=castle.getX() %>&amp;y=<%=castle.getY()%>"><%=castle.getCastleNameWml()%>(<%=castle.getX() %>|<%=castle.getY()%>)</a>的<%=attack.getTypeName()%><br/>
兵力:未知<br/>
剩余<%=DateUtil.formatTimeInterval2(attack.getEndTime())%>到达于<%=DateUtil.formatTime(attack.getEndTime())%><br/>
<%}}%>
<a href="fun.jsp?t=22">返回集结点</a><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>