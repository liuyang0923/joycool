<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*"%><%@ page import="net.joycool.wap.bean.PagingBean"%><%!
static CacheService cacheService = CacheService.getInstance();
static CastleService castleService = CastleService.getInstance();%><%
	CastleAction action = new CastleAction(request);
	int pos = action.getParameterInt("pos");
	BuildingBean building;
	int type = action.getParameterInt("t");
	if(pos == 0) {	// 根据建筑类型也可以使用，如果类型该建筑有多个，选择任意一个
		building = action.getCastleService().getBuildingBean(type, action.getCastle().getId());
		if(building!=null)
			pos = building.getBuildPos();
	} else {
		building = action.getCastleService().getBuildingBeanByPos(action.getCastle().getId(),pos);
	}
	
	if(building==null) {
		if(type == 1 || type == 2 || type == 3 || type == 9)
			response.sendRedirect("base.jsp");
		else
			response.sendRedirect("ad.jsp");
		return;
	}
	BuildingTBean buildingt = ResNeed.getBuildingT(building.getBuildType());
	action.setAttribute2("casSwi","fun.jsp?t="+building.getBuildType());
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="城堡战争"><p><%@include file="top.jsp"%>
【<%=ResNeed.getTypeName(building.getBuildType())%>】等级<%=building.getGrade()%><br/>
<%
	if(building.getGrade()==0){%>该建筑还没完成建造<br/><%
	}else{
String defURL = "sUp.jsp?pos=" + pos;
	switch(building.getBuildType()) {
		case ResNeed.CITY_BUILD:{
%><%@include file="inc/city.jsp"%><%	
		}break;
		case ResNeed.HERO_BUILD:{
%><%@include file="inc/hero.jsp"%><%	
		}break;
		case ResNeed.WOOD_BUILD:
		case ResNeed.FE_BUILD:
		case ResNeed.GRAIN_BUILD:
		case ResNeed.STONE_BUILD:{
%><%@include file="inc/res4.jsp"%><%
		}break;
		case ResNeed.HIDDEN_BUILD:{
%><%@include file="inc/hidden.jsp"%><%
			}break;
		case ResNeed.SMITHY_BUILD:{
%><%@include file="inc/sm.jsp"%><%	
			}break;
		case ResNeed.HEVO_BUILD:
		case ResNeed.FACTORY_BUILD:
		case ResNeed.CASERN_BUILD:
		case ResNeed.HEVO2_BUILD:
		case ResNeed.CASERN2_BUILD:{
%><%@include file="inc/casern.jsp"%><%
			}break;
		case ResNeed.GUN_ROOM_BUILD:{
%><%@include file="inc/sm2.jsp"%><%
			}break;
		case ResNeed.GATHER_BUILD:{
%><%@include file="inc/gather.jsp"%><%
			}break;
		case ResNeed.WHISTLE_BUILD:{
%><%@include file="inc/whistle.jsp"%><%
			}break;
		case ResNeed.TRAP_BUILD:{
%><%@include file="inc/trap.jsp"%><%
			}break;
		case ResNeed.RESEARCH_BUILD:{
%><%@include file="inc/research.jsp"%><%
			}break;
		case ResNeed.STORAGE_BUILD:
		case ResNeed.STORAGE2_BUILD:{
%><%@include file="inc/storage.jsp"%><%
			}break;
		case ResNeed.BARN_BUILD:
		case ResNeed.BARN2_BUILD:{
%><%@include file="inc/barn.jsp"%><%
			}break;
		case ResNeed.WOOD_FACTORY_BUILD:
		case ResNeed.STONE_FACTORY_BUILD:
		case ResNeed.FOUNDRY_BUILD:
		case ResNeed.BREAD_BUILD:
		case ResNeed.MOFANG_BUILD:{
%><%@include file="inc/res4p.jsp"%><%
			}break;
		case ResNeed.CAVE_BUILD:{
%><%@include file="inc/cave.jsp"%><%
			}break;
		case ResNeed.MARKET_BUILD:{
%><%@include file="inc/market.jsp"%><%
			}break;
		case ResNeed.PALACE_BUILD:
		case ResNeed.PALACE2_BUILD:{
%><%@include file="inc/palace.jsp"%><%
			}break;
		case ResNeed.WALL_BUILD:{
%><%@include file="inc/wall.jsp"%><%
			}break;
		case ResNeed.HALL_BUILD:{
%><%@include file="inc/hall.jsp"%><%
			}break;
		case ResNeed.EMMBASSY_BUILD:{
%><%@include file="inc/emmbassy.jsp"%><%
			}break;
		case ResNeed.ARENA_BUILD:{
%><%@include file="inc/arena.jsp"%><%
			}break;
		case ResNeed.TRADE_BUILD:{
%><%@include file="inc/trade.jsp"%><%
			}break;
		case ResNeed.STONE2_BUILD:{
%><%@include file="inc/stone2.jsp"%><%
			}break;
		case ResNeed.WONDER_BUILD:{
%><%@include file="inc/wonder.jsp"%><%
			}break;
		case ResNeed.TREASURE_BUILD:{
%><%@include file="inc/treasure.jsp"%><%
			}break;
	}
%><%if(building.getGrade()>=buildingt.getMaxGrade()||pos < 19 && castleUser.getMain()!=action.getCastle().getId() && building.getGrade()>=10){%><%=buildingt.getName()%>建造完成<%}else{%><a href="<%=defURL%>">++升级到等级<%=building.getGrade()+1%></a><%}%><br/>
<%}%><a href="s.jsp">返回城堡战争首页</a><br/></p></card></wml>