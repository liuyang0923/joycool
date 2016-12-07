<%@ page language="java" import="jc.family.game.*,jc.family.*,net.joycool.wap.framework.BaseAction" contentType="text/vnd.wap.wml;charset=UTF-8"%><%
GameAction action=new GameAction(request,response);
FamilyUserBean fmbean = action.getFmUser();
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="游戏报名"><p align="left"><%=BaseAction.getTop(request, response)%><%
if(fmbean!=null){
	int mid = action.getParameterInt("mid");
	MatchBean matchBean = GameAction.service.getMatchBean("state>2 and id="+mid);//只要未完赛就可以报名
	if(matchBean != null){
		int uid = action.getLoginUser().getId();
		int fid = fmbean.getFm_id();
		action.memberApply(mid, uid, fid);
		if(request.getAttribute("tip") != null){
			%><%=request.getAttribute("tip")%><br/><%
		}
	}else {
		%>无此赛事<br/><%
	}
}else{
	%>您还没有参加任何家族,还不能参加游戏<br/><%
}
%><a href="fmgame.jsp">返回</a><br/>
<%=BaseAction.getBottomShort(request,response)%></p></card></wml>