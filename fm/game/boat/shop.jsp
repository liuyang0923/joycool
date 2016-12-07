<%@ page language="java" import="jc.family.game.*,jc.family.*,net.joycool.wap.util.*,java.util.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
BoatAction action=new BoatAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
BoatBean currentBoat = null;
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟商店"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean != null){
	if(FamilyUserBean.isflag_game(fmbean.getFm_flags())){
		int mid = action.getParameterInt("mid");
		MatchBean matchBean = (MatchBean) GameAction.matchCache.get(new Integer(mid));
		if(matchBean != null && matchBean.getType() == 1){
			if(matchBean.getState() ==0){
				int type = action.getParameterInt("type");
				if(type <= 0 || type > 3)
					type = 1;
				List list = BoatAction.service.getBoatList(" boat_type="+type);
				currentBoat = BoatAction.getFmBoatBean(fmbean.getFm_id());
				if(currentBoat != null){
%>当前龙舟:<%=StringUtil.toWml(currentBoat.getName())%><br/>
加速能力:<%=BoatAction.speedStar(currentBoat.getSpeed())%><br/>
极限速度:<%=BoatAction.maxSpeedStar(currentBoat.getMaxSpeed())%><br/>
可使用:<%=currentBoat.getUseTime()%>次<br/><%if(currentBoat.getSpeAngleReset() == 1){%>特殊:拥有技能"复位",可使龙舟立刻变为直行,冷却时间4分钟<br/><%}%>
<%
				} else {
%>当前龙舟:无<br/>加速能力:无<br/>极限速度:无<br/><%
				}
%><a href="shop.jsp?mid=<%=mid%>">爆裂</a>&#160;<a href="shop.jsp?type=2&amp;mid=<%=mid%>">尖峰</a>&#160;<a href="shop.jsp?type=3&amp;mid=<%=mid%>">凤凰</a><br/><%
				if(list != null && list.size() > 0){
					for(int i=0;i<list.size();i++){
						BoatBean bean = (BoatBean)list.get(i);
						%><%=i+1%>.<a href="boatinfo.jsp?bid=<%=bean.getId()%>&amp;mid=<%=mid%>"><%=StringUtil.toWml(bean.getName())%></a><br/><%
					}
				}
			}else{
			%>非报名阶段,无法购买龙舟!<br/><%
			}
		} else {
		%>不存在龙舟赛事,不能购买龙舟!<br/><%
		}
	} else {
	%>您不是游戏管理员,不能购买龙舟!<br/><%
	}
}
%><a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>