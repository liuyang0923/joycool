<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%
int select = action.getParameterInt("s");
%><%if(select==0){%>训练<%}else{%><a href="fun.jsp?pos=<%=pos%>">训练</a><%}%>|<%if(select==1){%>文明度<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=1">文明度</a><%}%>|<%if(select==2){%>忠诚度<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=2">忠诚度</a><%}%>|<%if(select==3){%>扩张<%}else{%><a href="fun.jsp?pos=<%=pos%>&amp;s=3">扩张</a><%}%><br/><%

	switch(select){
	case 0:{

boolean canTrain;
CastleBean castle = action.getCastle();
if(building.getBuildType()==ResNeed.PALACE_BUILD)
	canTrain = castle.canExpand(building.getGrade(), 0);
else
	canTrain = castle.canExpand(0, building.getGrade());
if(canTrain){
if(CastleUtil.getCastleArmy(castle.getId()).getCount(9) >= 3){
%>===建立新的城堡===<br/>
输入建立城堡的位置:<br/>
X:<input name="x" format="*N"/>Y:<input name="y" format="*N"/><br/>
<anchor>建立新的城堡<go href="search.jsp"><postfield name="x" value="$x"/><postfield name="y" value="$y"/></go></anchor><br/>
<%}

%><%@include file="casern.jsp"%><%

} else {
if(building.getBuildType()==ResNeed.PALACE_BUILD){

if(building.getGrade()<10&&castle.getExpand()==0){%>当行宫等级达到10的时候，你可以兴建一座新的城堡<%}else if(building.getGrade()<20&&castle.getExpand()==1){
%>当行宫等级达到20的时候，你可以兴建一座新的城堡<%}else{%>该城堡已经完成扩张<%}

}else{

if(building.getGrade()<10&&castle.getExpand()==0){%>当皇宫等级达到10的时候，你可以兴建一座新的城堡<%}else if(building.getGrade()<15&&castle.getExpand()==1){
%>当皇宫等级达到15的时候，你可以兴建一座新的城堡<%}else if(building.getGrade()<20&&castle.getExpand()==2){
%>当皇宫等级达到20的时候，你可以兴建一座新的城堡<%}else{%>该城堡已经完成扩张<%}
}
%><br/>
<% }

if(!castle.isNatar()&&building.getBuildType()==ResNeed.PALACE2_BUILD){if(castleUser.getMain()!=castle.getId()){%><br/><a href="setmain.jsp">>>把这座城堡设定为主城!</a><br/><%}else{%><br/>这座城堡是你的主城<br/><%}}%><br/><%

	}break;
	case 1:{
		long now = System.currentTimeMillis();
	
%>如果你要扩张你的城邦,就需要文明度.这个数值会随着时间自动增加.建筑的等级越高增加的速度越快.<br/>
这个城堡的生产力:<%=action.getUserResBean().getCivil()%>文明度/天<br/>
所有城堡的生产力:<%=castleUser.getCivilSpeed()%>文明度/天<br/>
你的城堡现在一共有<%=castleUser.getCivil(now)%>的文明度.如果你需要兴建一座城堡的话,你需要<%=castleUser.getNextCivil()%>的文明度.<br/>
<%

	}break;
	case 2:{
	
%>目前该城堡居民的忠诚度是<%=action.getUserResBean().getLoyalString()%>%。<br/><%

	}break;
	case 3:{
	
CastleBean castle = action.getCastle();
%><%if(castle.getExpand()==0){%>这个城堡还没有兴建新的城堡<br/><%}else{
	List list = SqlUtil.getIntList("select to_cid from castle_expand where from_cid="+castle.getId(),5);
%>从该城堡出发建立的城堡(<%=castle.getExpand()%>)<br/><%
	for(int i=0;i<list.size();i++){
		Integer iid=(Integer)list.get(i);
		CastleBean castle2 = CastleUtil.getCastleById(iid.intValue());
		UserResBean res2 = CastleUtil.getUserResBeanById(iid.intValue());
%><%=i+1%>.<a href="search.jsp?x=<%=castle2.getX()%>&amp;y=<%=castle2.getY()%>"><%=castle2.getCastleNameWml()%></a>(<%=castle2.getX()%>|<%=castle2.getY()%>)人口<%=res2.getPeople()%>建立于<%=DateUtil.sformatTime(castle2.getCreateTime())%><br/><%
}}

	}break;
	}
%>