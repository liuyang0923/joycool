<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.util.*,java.util.*"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%!
	static CastleService service = CastleService.getInstance();
%><%
	CastleAction action = new CastleAction(request);	
%><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
<wml><card title="军队信息"><p><%@include file="top.jsp"%>
<%

	int cid = action.getCastle().getId();
	List list = service.getCastleArmyAtList(cid);
	
CastleArmyBean[] allArmy = new CastleArmyBean[6];
for(int i = 0;i < list.size();i++) {
	CastleArmyBean army = (CastleArmyBean)list.get(i);
	CastleBean c = CastleUtil.getCastleById(army.getCid());
	int race = c.getRace();
	if(allArmy[race] == null)
		allArmy[race] = new CastleArmyBean();
	allArmy[race].mergeCount(army);
}

	CastleArmyBean army2 = CastleUtil.getCastleArmy(cid);
%>=所属本城兵力=<br/>
<%=army2.getSoldierString(action.getCastle().getRace())%><br/>
=城堡内总兵力=<br/><%
for(int i = 1;i <= 5;i++)
	if(allArmy[i] != null) {
%><%=allArmy[i].getSoldierString(i)%><br/><%
	}
%><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>