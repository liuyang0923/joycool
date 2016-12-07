<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*, java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	CastleBean castleBean = action.getCastle();
	int cid = action.getCastle().getId();
	List list = action.getCastleService().getSoldierSmithy(cid);	
	BuildingBean casernBuilding = action.getCastleService().getBuildingBean(ResNeed.SMITHY_BUILD, cid);
	
	boolean flag = false;
	SoldierResBean[] so = ResNeed.getSoldierTs(castleUser.getRace());
	SoldierSmithyBean[] smithys = action.getSmithys();
	List smithyList = cacheService.getSoldierSmithyByCid(cid, 0);
	Iterator it = smithyList.iterator();
if(!flag) {
	if(casernBuilding == null)  {%>还没有建造铁匠铺^_^<br/>
<a href="<%=("sbd.jsp?type=" + ResNeed.SMITHY_BUILD)%>">建造</a><%}else {
	while(it.hasNext()) {
		SmithyThreadBean smithyBean = (SmithyThreadBean)it.next();
%><%=so[smithyBean.getSoldierType()].getSoldierName()%>的<%=smithyBean.getSmithyType() == 0 ?"攻击":"防御"%>升级中...<br/>
剩余<%=DateUtil.formatTimeInterval2(smithyBean.getEndTime())%>结束于<%=DateUtil.formatTime(smithyBean.getEndTime())%><br/>
<%}%>
<%
for(int i = 1;i < so.length;i++){
SoldierResBean soldier = so[i];
if(soldier.isFlagNoUpgrade()) continue;
if(smithys[i]!=null){
%>[<%=soldier.getSoldierName()%>]等级<%=smithys[i].getAttack()%><%if(smithyList.size()==0&&smithys[i].getAttack()<casernBuilding.getGrade()){%><a href="adu.jsp?t=0&amp;s=<%=i%>">升级</a><%}%><br/>
<%}else{%>
[<%=soldier.getSoldierName()%>]未研发<br/>
<%}}%>
<%}} else {%>铁匠铺正在建造或者升级中<br/><%}%>