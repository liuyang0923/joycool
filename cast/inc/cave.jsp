<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%
int caveAdd;
if(castleUser.getRace()==3)
	caveAdd = 2;
else
	caveAdd = 1;
int art = CastleUtil.getActiveArtType(action.getCastle());
if(art==8)
	caveAdd *= 200;
else if(art==18)
	caveAdd *= 100;
%>当前可以隐藏的资源<%=caveAdd * ResNeed.getBuildingT(building.getBuildType(),building.getGrade()).getValue()%><br/>
<%if(building.getGrade()<buildingt.getMaxGrade()){%>升级后可以隐藏的资源<%=caveAdd * ResNeed.getBuildingT(building.getBuildType(),building.getGrade()+1).getValue()%><br/><%}%>
