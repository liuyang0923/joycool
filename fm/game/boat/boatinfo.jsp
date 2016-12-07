<%@ page language="java" import="java.text.*,jc.family.game.*,net.joycool.wap.util.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
BoatAction action=new BoatAction(request,response);
int bid = action.getParameterInt("bid");
String[] rentType = {"亿乐币","酷币"};
BoatBean bean = BoatAction.service.getBoatBean(" id="+bid);
int mid = action.getParameterInt("mid");
MatchBean matchBean = (MatchBean) GameAction.matchCache.get(new Integer(mid));
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="购买龙舟"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(bean != null){
	DecimalFormat numFormat = new DecimalFormat("#.#");
%><%=StringUtil.toWml(bean.getName())%><br/>
说明:<%=StringUtil.toWml(bean.getBak())%><br/>
售价:<%=numFormat.format(bean.getRent())%><%=rentType[bean.getRentType()]%><br/>
加速能力:<%=BoatAction.speedStar(bean.getSpeed())%><br/>
极限速度:<%=BoatAction.maxSpeedStar(bean.getMaxSpeed())%><br/>
<%if(bean.getSpeAngleReset() == 1){%>特殊:拥有技能"复位",可使龙舟立刻变为直行,冷却时间4分钟<br/><%}%>
可使用:<%=bean.getUseTime()%>次<br/><%
	if(matchBean != null && matchBean.getType() == 1){
		if(matchBean.getState() == 0){ 
%><a href="buy.jsp?bid=<%=bean.getId()%>&amp;mid=<%=mid%>">购买</a><br/><%
		} else {
		%>非报名阶段,无法购买龙舟!<br/><%
		}
	}else {
	%>不存在龙舟赛事,不能购买龙舟!<br/><%
	}
}else{
%>无此龙舟<br/><%
}
%><a href="shop.jsp?mid=<%=mid%>">返回龙舟商店</a><br/><a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>