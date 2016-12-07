<%@ page language="java" contentType="text/vnd.wap.wml;charset=UTF-8"%><%@ page import="java.util.*,net.joycool.wap.util.*,jc.family.*,jc.family.game.*,jc.family.game.boat.*"%><%@ page import="net.joycool.wap.framework.BaseAction"%><%
BoatRecordBean recordBean = BoatAction.service.getFmRecord("complete=0 order by use_time");
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="赛龙舟"><p align="left"><%=BaseAction.getTop(request, response)%>
<img src="/fm/game/img/boat.gif" alt="logo" /><br/>
赛龙舟家族龙舟赛,为了兄弟,为了家族的荣耀,前进!!<br/>
<% 
if (recordBean != null) {
%>
龙舟历史最佳：<%=GameAction.getFormatDifferTime(recordBean.getUseTime())%><br/>
<% 
	FamilyHomeBean fmhome=BoatAction.getFmByID(recordBean.getFmId());
%>
<a href="/fm/myfamily.jsp?id=<%=fmhome.getId()%>"><%=StringUtil.toWml(fmhome.getFm_name())%></a>|<%=FamilyAction.sd.format(new Date(recordBean.getCreateTime()))%>|<%=BoatAction.boatType[recordBean.getBoatType()]%>龙舟<br/>
<%
}
%>
<a href="apprule.jsp">报名流程</a>|<a href="rule.jsp">游戏规则</a><br/>
<a href="lastrecord.jsp">本轮统计</a>|<a href="historyrank.jsp">历史排行</a><br/>
<a href="/fm/game/fmgame.jsp">返回家族活动</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>