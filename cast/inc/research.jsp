<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*, java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%
	CastleBean castleBean = action.getCastle();
	int cid = action.getCastle().getId();
	List list = action.getCastleService().getSoldierSmithy(cid);	
	BuildingBean casernBuilding = action.getCastleService().getBuildingBean(ResNeed.RESEARCH_BUILD, cid);
	
	boolean flag = false;
	SoldierResBean[] so = ResNeed.getSoldierTs(castleUser.getRace());
	SoldierSmithyBean[] smithys = action.getSmithys();
	List smithyList = cacheService.getSoldierSmithyByCid(cid, 2);
	Iterator it = smithyList.iterator();
if(!flag) {
	if(casernBuilding == null)  {%>还没有建造研究所^_^<br/>
<a href="<%=("sbd.jsp?type=" + ResNeed.RESEARCH_BUILD)%>">建造</a><%}else {
	while(it.hasNext()) {
		SmithyThreadBean smithyBean = (SmithyThreadBean)it.next();
%><%=so[smithyBean.getSoldierType()].getSoldierName()%>研发中…<br/>
剩余<%=DateUtil.formatTimeInterval2(smithyBean.getEndTime())%>结束于<%=DateUtil.formatTime(smithyBean.getEndTime())%><br/>
<%}%>
<%
for(int i = 1;i < so.length;i++){
SoldierResBean soldier = so[i];
if(soldier.isFlagResearched()) continue;
%>[<%=soldier.getSoldierName()%>]
<%if(smithys[i]==null){%><%if(smithyList.size()==0){%><a href="adu.jsp?t=2&amp;s=<%=i%>">研发</a><%}else{%>研发中<%}%><%}else{%>已研发<%}%><br/>
<%}%>

<%}} else {%>军械库正在建造或者升级中<br/><%}%>