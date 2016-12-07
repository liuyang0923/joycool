<%@ page language="java" import="java.text.*,jc.family.game.*,net.joycool.wap.util.*,jc.family.game.boat.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
BoatAction action=new BoatAction(request,response);
int bid = action.getParameterInt("bid");
String[] rentType = {"亿乐币","酷币"};
BoatBean bean = BoatAction.service.getBoatBean(" id="+bid);
//int mid = action.getParameterInt("mid");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="龙舟信息"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(bean != null){
	DecimalFormat numFormat = new DecimalFormat("#.#");
%><%=StringUtil.toWml(bean.getName())%><br/>
说明:<%=StringUtil.toWml(bean.getBak())%><br/>
售价:<%=numFormat.format(bean.getRent())%><%=rentType[bean.getRentType()]%><br/>
加速能力:<%=BoatAction.speedStar(bean.getSpeed())%><br/>
极限速度:<%=BoatAction.maxSpeedStar(bean.getMaxSpeed())%><br/>
<%if(bean.getSpeAngleReset() == 1){%>特殊:拥有技能"复位",可使龙舟立刻变为直行,冷却时间4分钟<br/><%}%>
<%}%>
<a href="seat.jsp">返回</a><br/><a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>