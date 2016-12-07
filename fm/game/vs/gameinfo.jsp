<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="net.joycool.wap.framework.BaseAction,jc.family.game.vs.*,jc.family.*"%><%
VsAction action=new VsAction(request,response);
int id=action.getParameterInt("fmId");
int gameid=action.getParameterInt("id");
if(gameid<1||gameid>=VsGameBean.vsConfig.length){
response.sendRedirect("/fm/index.jsp");return;
}
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="<%=VsGameBean.getGameIdName(gameid)%>"><p align="left"><%=BaseAction.getTop(request, response)%><%
FmVsScore fmScore=action.vsService.getFmVsScore("fm_id="+id+" and game_id="+gameid);
%><img src="/rep/family/vs<%=gameid%>.gif" alt="logo"/><br/>
挑战积分:<%=fmScore!=null?fmScore.getScore()+"":"未参赛"%><br/>
胜:<%=fmScore!=null?fmScore.getWin():0%>|负:<%=fmScore!=null?fmScore.getLose():0%>|平:<%=fmScore!=null?fmScore.getTie():0%><br/>
本日已进行:<%=fmScore!=null?fmScore.getVsTime():0%>次<br/>
<a href="/fm/topgame.jsp?a=<%=gameid+3%>">挑战排行</a>|<a href="/Column.do?columnId=<%=VsGameBean.getVsConfig(gameid).getColumnId()%>">游戏说明</a><br/>
<a href="vsgame.jsp?id=<%=id%>">返回家族挑战</a><br/>
<%=BaseAction.getBottomShort(request,response)%>
</p></card></wml>