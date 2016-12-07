<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.DateUtil"%><%@ page import="java.util.List"%><%
	CastleBean bean = action.getCastle();
	BuildingBean cityBuilding = action.getCastleService().getBuildingBean(ResNeed.CITY_BUILD, bean.getId());
	int cid = action.getCastle().getId();
	List attackMeList = cacheService.getCacheAttackByToCid(cid);
	List attackList = cacheService.getCacheAttackByFromCid(cid);
	List armyList = castleService.getCastleArmyAtList(cid);
	List armyList2 = castleService.getCastleArmyList(cid);
	List armyList3 = castleService.getOasisArmyList(cid);
	List armyList4 = castleService.getOasisArmyAtCidList(cid);
	String url = "fun.jsp?type="+action.getParameterString("type")+"&amp;pos="+action.getParameterString("pos");

if(attackMeList != null && attackMeList.size() > 0)  {
%>+接近中的军队<%if(attackMeList.size()>1){%>(<%=attackMeList.size()%>)<%}%><br/><%
for(int i=0;i<attackMeList.size();i++){
	AttackThreadBean attackBean = (AttackThreadBean)attackMeList.get(i);
	if(attackBean.getCid()!=attackBean.getToCid() && (attackBean.getType()==2||attackBean.getType()==3||attackBean.getType()==9)) continue;
%><%=i+1%>.<%if(attackBean.getCid()==attackBean.getToCid()){%>返回自<%}else{%>来自<%}

if(attackBean.getFromCid()!=0){
	CastleBean castleBean = CastleUtil.getCastleById(attackBean.getFromCid());
	if(castleBean==null){
%>[?]<%
}else{
%><a href="search.jsp?x=<%=castleBean.getX() %>&amp;y=<%=castleBean.getY() %>"><%=castleBean.getX() %>|<%=castleBean.getY() %></a><%}

} else {
%><a href="search.jsp?x=<%=attackBean.getX() %>&amp;y=<%=attackBean.getY() %>"><%=attackBean.getX() %>|<%=attackBean.getY() %></a><%
}

%>的<a href="army2.jsp?id=<%=attackBean.getId()%>"><%=attackBean.getTypeName()%><%if(attackBean.getHero()!=0){%>*<%}%></a>剩余<%=DateUtil.formatTimeInterval2(attackBean.getEndTime())%><br/>
<%}}

%>+城堡里的军队<%if(armyList.size()>1){%>(<%=armyList.size()%>)<%}%><br/><%
for(int i=0;i<armyList.size();i++){
	CastleArmyBean army = (CastleArmyBean)armyList.get(i);
	CastleBean castleBean = CastleUtil.getCastleById(army.getCid());
%><%=i+1%>.<%if(army.getCid()==cid){%>自己的<a href="move.jsp?id=<%=army.getId()%>">军队</a><%}else{%>来自<%=castleBean.getX()%>|<%=castleBean.getY()%>的<a href="move.jsp?id=<%=army.getId()%>">军队</a><%}%><%=army.getTotalCount()%><%if(army.getHero()!=0){%>*<%}%><br/>
<%}

if(armyList2 != null && armyList2.size() > 0)  {
%>+在其他城堡的军队<%if(armyList2.size()>1){%>(<%=armyList2.size()%>)<%}%><br/><%
for(int i=0;i<armyList2.size();i++){
	CastleArmyBean army = (CastleArmyBean)armyList2.get(i);
	CastleBean castleBean = CastleUtil.getCastleById(army.getAt());
%><%=i+1%>.支援<%=castleBean.getX()%>|<%=castleBean.getY()%>的<a href="move.jsp?id=<%=army.getId()%>">军队</a><%=army.getTotalCount()%><%if(army.getHero()!=0){%>*<%}%><br/>
<%}}

if(attackList != null && attackList.size() > 0){
%>+远离中的军队<%if(attackList.size()>1){%>(<%=attackList.size()%>)<%}%><br/><%
for(int i=0;i<attackList.size();i++){
	AttackThreadBean attackBean = (AttackThreadBean)attackList.get(i);
	CastleBean castleBean = null;
	if(attackBean.getToCid()!=0)
		castleBean = CastleUtil.getCastleById(attackBean.getToCid());
%><%=i+1%>.<%if(castleBean!=null){
%>对<a href="search.jsp?x=<%=castleBean.getX() %>&amp;y=<%=castleBean.getY() %>"><%=castleBean.getX() %>|<%=castleBean.getY() %></a>的<a href="army2.jsp?id=<%=attackBean.getId()%>"><%=attackBean.getTypeName()%></a><%if(attackBean.getHero()!=0){%>*<%}%><%
}else if(attackBean.getType()!=4){
%>对<a href="search.jsp?x=<%=attackBean.getX() %>&amp;y=<%=attackBean.getY() %>"><%=attackBean.getX() %>|<%=attackBean.getY() %></a>的<a href="army2.jsp?id=<%=attackBean.getId()%>"><%=attackBean.getTypeName()%></a><%if(attackBean.getHero()!=0){%>*<%}%><%
}else{
%>在<a href="around.jsp?x=<%=attackBean.getX() %>&amp;y=<%=attackBean.getY() %>"><%=attackBean.getX() %>|<%=attackBean.getY() %></a><a href="army2.jsp?id=<%=attackBean.getId()%>">建立</a>城堡<%
}%>,剩余<%=DateUtil.formatTimeInterval2(attackBean.getEndTime())%><%if(System.currentTimeMillis() - attackBean.getStartTime() <= 90000){%>-<a href="cancelA.jsp?id=<%=attackBean.getId()%>">取消</a><%}%><br/><%
}}

if(armyList3 != null && armyList3.size() > 0)  {
%>+在绿洲的军队<%if(armyList3.size()>1){%>(<%=armyList3.size()%>)<%}%><br/><%
for(int i=0;i<armyList3.size();i++){
	CastleArmyBean army = (CastleArmyBean)armyList3.get(i);
	int x = CastleUtil.pos2X(army.getAt());
	int y = CastleUtil.pos2Y(army.getAt());
%><%=i+1%>.支援绿洲<%=x%>|<%=y%>的<a href="move2.jsp?id=<%=army.getId()%>">军队</a><%=army.getTotalCount()%><%if(army.getHero()!=0){%>*<%}%><br/>
<%}}


if(armyList4 != null && armyList4.size() > 0)  {
%>+绿洲的防御<%if(armyList4.size()>1){%>(<%=armyList4.size()%>)<%}%><br/><%
for(int i=0;i<armyList4.size();i++){
	CastleArmyBean army = (CastleArmyBean)armyList4.get(i);
	CastleBean castleBean = CastleUtil.getCastleById(army.getCid());
%><%=i+1%>.来自<%=castleBean.getX()%>|<%=castleBean.getY()%>的<a href="move2.jsp?id=<%=army.getId()%>">军队</a><%=army.getTotalCount()%><%if(army.getHero()!=0){%>*<%}%><br/>
<%}}
%>
