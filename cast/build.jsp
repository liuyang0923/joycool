<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.framework.BaseAction,java.util.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%!
	static int[] advanceBuild = {
		ResNeed.CITY_BUILD,
		ResNeed.STORAGE_BUILD,
		ResNeed.BARN_BUILD,
		ResNeed.STORAGE2_BUILD,
		ResNeed.BARN2_BUILD,
		ResNeed.MARKET_BUILD,
		ResNeed.CAVE_BUILD,
		ResNeed.EMMBASSY_BUILD,
		ResNeed.GATHER_BUILD, 
		ResNeed.CASERN_BUILD, 
		ResNeed.RESEARCH_BUILD,
		ResNeed.TRAP_BUILD,
		ResNeed.HERO_BUILD,
		ResNeed.WALL_BUILD,
		ResNeed.WALL2_BUILD,
		ResNeed.WALL3_BUILD,
		ResNeed.SMITHY_BUILD,
		ResNeed.WHISTLE_BUILD,
		ResNeed.HIDDEN_BUILD,
		ResNeed.HEVO_BUILD,
		ResNeed.GUN_ROOM_BUILD,
		ResNeed.MOFANG_BUILD,
		ResNeed.WOOD_FACTORY_BUILD,
		ResNeed.STONE_FACTORY_BUILD,
		ResNeed.FOUNDRY_BUILD,
		ResNeed.BREAD_BUILD,
		ResNeed.FACTORY_BUILD,
		ResNeed.PALACE_BUILD,
		ResNeed.PALACE2_BUILD,
		ResNeed.HALL_BUILD,
		ResNeed.TRADE_BUILD,
		ResNeed.ARENA_BUILD,
		ResNeed.STONE2_BUILD,
		ResNeed.CASERN2_BUILD,
		ResNeed.HEVO2_BUILD,
		ResNeed.TREASURE_BUILD,
		ResNeed.WONDER_BUILD,
	};
%><%
	
	
	CastleAction action = new CastleAction(request);
	UserResBean userResBean = action.getUserResBean();
	BuildingTBean[] bts = ResNeed.getBuildingTs();
	boolean showall = action.hasParam("all");

	int race = action.getCastle().getRace();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
<% for(int i = 0; i < advanceBuild.length; i++) {
	BuildingTBean bt = bts[advanceBuild[i]];
	if(!action.canBuild(bt)) continue;

	boolean can = userResBean.canBuild(bt);
	if(!can && !showall) continue;
%><%if(i<9){%>0<%}%><%=i+1%>.<a href="sbd.jsp?type=<%=advanceBuild[i]%>&amp;pos=<%=request.getParameter("pos") %>"><%=bt.getName()%></a><br/><%
	} 
if(showall){%><a href="build.jsp">隐藏无法建造的建筑</a><%}else{%><a href="build.jsp?all=1">显示无法建造的建筑</a><%}%><br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>