<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="java.util.List,net.joycool.wap.util.*"%><%
	
	int cid = action.getCastle().getId();
	BuildingBean buildingBean = action.getCastleService().getBuildingBean(ResNeed.HIDDEN_BUILD, cid);
	
	CastleArmyBean army = CastleUtil.getCastleArmy(cid);
	CastleArmyBean hiddenArmy = CastleUtil.getCastleHiddenArmy(cid);
	int[] counts = hiddenArmy.getCount();
	int sum = 0;
	for(int i = 0; i < counts.length; i++) {
		sum += counts[i];
	}
	SoldierResBean[] so = ResNeed.getSoldierTs(castleUser.getRace());
	if(buildingBean == null) {%>你还没有建造藏兵洞^_^<br/>
<a href="<%=("sbd.jsp?type=" + ResNeed.HIDDEN_BUILD)%>">建造</a><%
	} else {%>使用情况:<%=sum%>/<%=ResNeed.getHiddenCount(buildingBean.getGrade())%><br/>
<%for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		int count1 = hiddenArmy.getCount(soldier.getType());
		if(count1==0 && (count == 0||i>6)) continue;
%><%=soldier.getSoldierName()%>(有:<%=count%>|藏:<%=count1%>):<input name="count<%=soldier.getType()%>" format="*N"/><br/><%}%>
<anchor title="藏兵">藏兵<go href="hidSod.jsp?pos=<%=request.getParameter("pos")%>" method="post"><%for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		int count1 = hiddenArmy.getCount(soldier.getType());
		if(count1==0 && (count == 0||i>6)) continue;
%><postfield name="count<%=soldier.getType()%>" value="$count<%=soldier.getType()%>"/><%}%></go></anchor>|<anchor title="取兵">取兵<go href="cSod.jsp?pos=<%=request.getParameter("pos")%>" method="post">
<%for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
		int count1 = hiddenArmy.getCount(soldier.getType());
		if(count1==0 && (count == 0||i>6)) continue;
%><postfield name="count<%=soldier.getType()%>" value="$count<%=soldier.getType()%>"/><%}%>
</go></anchor><%}%><br/>