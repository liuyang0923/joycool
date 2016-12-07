<%@ page language="java" import="net.joycool.wap.util.*,java.util.*,jc.family.game.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
GameAction action=new GameAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
String[] weekGame = {"","赛龙舟","雪仗","问答"};
String[] url = {"","boat/game.jsp","snow/snowMoney.jsp","ask/begin.jsp"};
List list = GameAction.getCurrentMatchList();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="家族活动"><p align="left"><%=BaseAction.getTop(request, response)%>
今日家族活动:<br/><%
if(fmbean!=null){
	int uid = action.getLoginUser().getId();
	if(list != null && list.size() > 0){
		for(int i=0;i<list.size();i++){
			Integer key = (Integer) list.get(i);
			MatchBean matchBean = (MatchBean) GameAction.matchCache.get(key);
			if(matchBean != null && matchBean.getState2() == 0 && matchBean.getState() < 2){
				%><%=weekGame[matchBean.getType()]%>(<%=DateUtil.formatTime(matchBean.getStarttime())%>-<%=DateUtil.formatTime(matchBean.getEndtime())%>)<br/><%
				ApplyBean applyBean = GameAction.service.getApplyBean("m_id="+matchBean.getId()+" and uid="+uid);
				if(applyBean == null){
				%>
				<%if(!action.isFmApply(key.intValue())){%>报名&#160;撤销报名<br/><%
				}else{ %><a href="apply.jsp?mid=<%=matchBean.getId()%>&amp;a=1">报名</a>&#160;撤销报名<br/><%}%>
				<%} else {
					if(applyBean.getIsPay() == 1){
				%>报名&#160;撤销报名<br/><%
					} else if(applyBean.getState() == 1){
				%>报名&#160;撤销报名<br/><%
					} else {
				%>报名&#160;<a href="apply.jsp?mid=<%=matchBean.getId()%>&amp;a=2">撤销报名<br/></a><%
					}
				}
				if(FamilyUserBean.isflag_game(fmbean.getFm_flags())){
				%><a href="examine.jsp?mid=<%=matchBean.getId()%>">审核</a>&#160;<%
				} else {
				%>审核&#160;<%
				}
				if(matchBean.getState() == 1){
				%><a href="<%=url[matchBean.getType()]%>?mid=<%=matchBean.getId()%>">开始游戏</a><br/><%
				} else {
				%>开始游戏<br/><%
				}
				if(matchBean.getType()==1){
					if(FamilyUserBean.isflag_game(fmbean.getFm_flags())){
						if(matchBean.getState() == 0){
						%><a href="boat/shop.jsp?mid=<%=matchBean.getId()%>">龙舟商店</a><br/><%
						}else{
						%>龙舟商店<br/><%
						}
					}else{
					%>龙舟商店<br/><%
					}
				}
			}
		}
	}else{
		%>暂无赛事<br/><%
	}%>当前家族挑战:<br/><%
	jc.family.game.vs.VsBean vsBean=jc.family.game.vs.VsAction.getFmVsInfo(fmbean.getFm_id());
	if(vsBean.getGameid()!=0){
	jc.family.game.vs.VsGameBean vsgame=jc.family.game.vs.VsAction.getVsGameById(vsBean.getGameid());
	%><%=vsgame.getGameIdName()%>(<%=DateUtil.formatTime(new Date(vsgame.getStartTime()))%>-<%=DateUtil.formatTime(new Date(vsgame.getEndTime()))%>)<br/>
	对战家族:<%=vsgame.getChallFmidNameWml(fmbean.getFm_id())%><br/>
	<a href="<%=vsgame.getGameUrl()%>?id=<%=vsgame.getId()%>&amp;c=1">进入游戏</a>|<a href="<%=vsgame.getGameUrl()%>?id=<%=vsgame.getId()%>">观看游戏</a><br/><%
	}else{
	%>暂无挑战<br/><%}%>
	&lt;&lt;&lt;&lt;&lt;&lt;&lt;&lt;<br/><a href="boat/match.jsp">赛龙舟</a><br/>
	<a href="snow/snowBallFight.jsp">打雪仗</a><br/>
	<a href="ask/index.jsp">问答</a><br/>
	<a href="historygame.jsp">本家族活动记录</a><br/>
<a href="/fm/myfamily.jsp?id=<%=fmbean.getFm_id()%>">返回我的家族</a><br/><%
}
%>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>