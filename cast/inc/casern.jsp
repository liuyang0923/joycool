<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.framework.*"%><%@ page import="java.util.List"%><%@ page import="net.joycool.wap.util.*"%><%

	int cid = action.getCastle().getId();
	List cacheList = cacheService.getCacheSoldierByCid(cid,building.getBuildType());

	int canTrainType = ResNeed.canTrainTypes[building.getBuildType()];	// 对应士兵可训练的建筑，例如行宫和皇宫都可以训练拓荒
	int trainCount = -1;
	if(canTrainType==20){
		trainCount = CastleUtil.calcTrainCount(action.getCastle(), 9, building.getBuildType(), building.getGrade());
	}
	int trainCost = ResNeed.trainCosts[building.getBuildType()];	// 士兵训练成本，1表示原始，3表示3倍

	float rate = ResNeed.getBuildingT(ResNeed.CASERN_BUILD,building.getGrade()).getValue() / 100f;
	CastleArmyBean army = CastleUtil.getCastleArmy(cid);
	SoldierResBean[] so = ResNeed.getSoldierTs(castleUser.getRace());
if(cacheList.size()>0){
	for(int i = 0; i < cacheList.size(); i++) {
	SoldierThreadBean sol = (SoldierThreadBean)cacheList.get(i);
%><%if(i==0){%>下一个单位将在<%=DateUtil.formatTimeInterval2(sol.getEndTime())%>内完成<br/><%}%>
有<%=sol.getCount()%>个<%=so[sol.getSoldierType()].getSoldierName() %>正在训练,剩余<%=DateUtil.formatTimeInterval2(sol.getAllEndTime())%>,结束于<%=DateUtil.formatTime(sol.getAllEndTime())%><br/>
<%}}%>
<% 
SoldierSmithyBean[] smithys = action.getSmithys();
for(int i=1;i<so.length;i++){
SoldierResBean soldier = so[i];
if(soldier.getBuildType()!=canTrainType) continue;
if(smithys[i]!=null||soldier.isFlagResearched()){
%>[<a href="soldier.jsp?t=<%=soldier.getType()%>"><%=soldier.getSoldierName()%></a>](现有<%=army.getCount(soldier.getType())%>)<%if(trainCount!=-1){%>(最多<%=i==9?trainCount:trainCount/3%>)<%}%><br/>
木<%=soldier.getWood()*trainCost%>|石<%=soldier.getStone()*trainCost%>|铁<%=soldier.getFe()*trainCost%>|粮<%=soldier.getGrain()*trainCost%><br/>
训练时间<%=DateUtil.formatTimeInterval2((int)(soldier.getTime()*rate))%>
<input name="count<%=soldier.getType()%>" format="*N"/>
<anchor title="训练">训练<go href="train.jsp?type=<%=soldier.getType()%>&amp;bt=<%=building.getBuildType()%>" method="post">
<postfield name="count" value="$count<%=soldier.getType()%>" />
<postfield name="pos" value="<%=pos%>" />
</go></anchor><br/>
<%}else{%>
[<a href="soldier.jsp?t=<%=soldier.getType()%>"><%=soldier.getSoldierName()%></a>]未研发<br/>
<%}}%>