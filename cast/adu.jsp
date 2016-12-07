<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.*"%><%!
static String[] title = {"武器升级","防具升级","兵种研发"};
%><%

	CasernAction action = new CasernAction(request);
	int type = action.getParameterInt("t");
	if(type < 0 || type > 2) type = 2;
	int stype = action.getParameterInt("s");
	int race=action.getCastleUser().getRace();
	int pos = action.getParameterInt("pos");
	SoldierSmithyBean ssBean = action.getCastleService().getSoldierSmithy(action.getCastle().getId(), stype);
	if(ssBean == null){
		ssBean = new SoldierSmithyBean();
		ssBean.setSoldierType(stype);
	}
	int casernGrade = 0;
	if(type==0){
		casernGrade = action.getUserResBean().getBuildingGrade(ResNeed.SMITHY_BUILD);
	}else if(type==1){
		casernGrade = action.getUserResBean().getBuildingGrade(ResNeed.GUN_ROOM_BUILD);
	}else{
		casernGrade = action.getUserResBean().getBuildingGrade(ResNeed.RESEARCH_BUILD);
	}
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="<%=title[type]%>"><p>
<%@include file="top.jsp"%>
<%if(casernGrade == 0)  {%>
建筑还没有建造<br/>
<%}else if(type==0&&ssBean.getAttack()>=casernGrade||type==1&&ssBean.getDefence()>=casernGrade){
	%>建筑等级不够<br/><%
}else { 
	SoldierResBean soldier = ResNeed.getSoldierRes(race,stype);
	if(type==0){
	ResTBean res = ResNeed.getAttackResource(race,stype, ssBean.getAttack() + 1);
%>【<%=soldier.getSoldierName()%>】等级<%=ssBean.getAttack()%><a href="upDef.jsp?t=0&amp;s=<%=stype %>&amp;pos=<%=pos%>">升级</a><br/>
升到等级<%=ssBean.getAttack() + 1%>所需资源:<br/>
木<%=res.getWood()%>|石<%=res.getStone()%>|铁<%=res.getFe()%>|粮<%=res.getGrain()%><br/>
所需时间<%=DateUtil.formatTimeInterval2(ResNeed.getGradeTime(casernGrade,res.getTime()))%><br/><%
	} else if(type==1) {
	ResTBean res = ResNeed.getAttackResource(race,stype, ssBean.getDefence() + 1);
%>【<%=soldier.getSoldierName()%>】等级<%=ssBean.getDefence()%><a href="upDef.jsp?t=1&amp;s=<%=stype %>&amp;pos=<%=request.getParameter("pos")%>">升级</a><br/>
升到等级<%=ssBean.getDefence() + 1%>所需资源:<br/>
木<%=res.getWood()%>|石<%=res.getStone()%>|铁<%=res.getFe()%>|粮<%=res.getGrain()%><br/>
所需时间<%=DateUtil.formatTimeInterval2(ResNeed.getGradeTime(casernGrade,res.getTime()))%><br/><%
} else {
%>【<%=soldier.getSoldierName()%>】<a href="upDef.jsp?t=2&amp;s=<%=stype %>&amp;pos=<%=request.getParameter("pos")%>">研发</a><br/>
<%=ResNeed.getResearchPreString(race,stype)%><br/>
升到等级<%=ssBean.getDefence() + 1%>所需资源:<br/>
木<%=soldier.getWood2()%>|石<%=soldier.getStone2()%>|铁<%=soldier.getFe2()%>|粮<%=soldier.getGrain2()%><br/>
所需时间<%=DateUtil.formatTimeInterval2(soldier.getTime2())%><br/><%

}
	}
%>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>