<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%!
	static String[] typeDetail = {
"",
"有了建筑图纸才能在奇迹城中升级世界奇迹.",
"建筑物的稳固度提升4倍.",
"军队的行军速度提升100%.",
"侦察兵的侦察能力提升5倍.",
"军队的粮食消耗减少50%.",
"士兵的训练时间减少50%.",
"可以建造大仓库和大粮仓.",
"山洞的储量提升200倍,同时来攻击的投石车会迷失方向而随意攻击.",
"这个宝物的效果会每24小时随机改变,有时候甚至出现反效果.",
"","",
"建筑物的稳固度提升3倍.",
"军队的行军速度提升50%.",
"侦察兵的侦察能力提升3倍.",
"军队的粮食消耗减少25%.",
"士兵的训练时间减少25%.",
"",
"山洞的储量提升100倍,同时来攻击的投石车会迷失方向而随意攻击.",
"",
};
%><%
int uid = castleUser.getUid();
List list = CastleUtil.getArtList();
List list2 = new ArrayList(4);
for(int i=0;i<list.size();i++){
	ArtBean art = (ArtBean)list.get(i);
	if(art.getUid()==uid)
		list2.add(new Integer(i));
}
int iart = action.getParameterIntS("i");
if(iart>=list.size())
	iart=-1;
if(iart<0){		// 显示列表，否则显示单个宝物
if(list2.size()>0){
%>==我拥有的宝物==<br/><%
	for(int i=0;i<list2.size();i++){
		int ii = ((Integer)list2.get(i)).intValue();
		ArtBean art = (ArtBean)list.get(ii);
		%><%=i+1%>.<a href="fun.jsp?pos=<%=pos%>&amp;i=<%=ii%>"><%=art.getName()%></a>(<%if(art.isActive()){%>已激活<%}else{%>未激活<%}%>)<br/><%
	}
}
%>==宝物(玩家)距离==<br/><%
CastleBean castle = action.getCastle();
for(int i=0;i<list.size();i++){
ArtBean art = (ArtBean)list.get(i);
if(art.getUid()==uid) continue;
CastleUserBean user2 = CastleUtil.getCastleUser(art.getUid());
CastleBean castle2 = CastleUtil.getCastleById(art.getCid());
if(castle2==null||user2==null) continue;
%><%=i+1%>.<a href="fun.jsp?pos=<%=pos%>&amp;i=<%=i%>"><%=art.getName()%></a>(<a href="user.jsp?uid=<%=art.getUid()%>"><%=user2.getNameWml()%></a>)
<%=StringUtil.numberFormat(CastleUtil.calcDistance((castle.getX()-castle2.getX()), (castle.getY()-castle2.getY())))%><br/><%}

}else{
	ArtBean art = (ArtBean)list.get(iart);
	CastleUserBean user2 = CastleUtil.getCastleUser(art.getUid());
	CastleBean castle2 = CastleUtil.getCastleById(art.getCid());
	List his = castleService.getArtHisList(" art_id="+art.getId()+" order by id desc");
%>[宝物:<%=art.getName()%>]<%if(art.getUid()==uid){ if(art.isActive()){%>(已激活)<%}else{%>(剩余<%=DateUtil.formatTimeInterval2(art.getCaptureTime() + DateUtil.MS_IN_DAY)%>激活)<%}}%><br/>
<%=typeDetail[art.getType()]%><%if(art.isFlagChange()){%>(!!该宝物的效果每天都可能改变)<%}%><br/>
拥有者:<a href="user.jsp?uid=<%=art.getUid()%>"><%=user2.getNameWml()%></a><br/>
城堡:<a href="search.jsp?cid=<%=castle2.getId()%>"><%=castle2.getCastleNameWml()%>(<%=castle2.getX()%>|<%=castle2.getY()%>)</a><br/>
联盟:<%if(user2.getTong()!=0){%><a href="tong/tong.jsp?id=<%=user2.getTong()%>"><%=StringUtil.toWml(CastleUtil.getTong(user2.getTong()).getName())%></a><%}else{%>-<%}%><br/>
作用范围:<%if(art.isFlagAccount()){%>城主<%}else{%>城堡<%}%><br/>
所需等级:宝库<%if(art.isFlagBig()){%>20<%}else{%>10<%}%>级<br/>
被占领的时间:<%if(art.getCaptureTime()==0){%>??<%}else{%><%=DateUtil.formatDate2(art.getCaptureTime())%><br/><%
if(his.size() > 1){
%>[曾经的拥有者]<br/><%for(int i=1;i<his.size();i++){
ArtBean his2 = (ArtBean)his.get(i);
CastleBean castle3 = CastleUtil.getCastleById(his2.getCid());
CastleUserBean user3 = CastleUtil.getCastleUser(his2.getUid());

%>[<%=DateUtil.formatDate2(his2.getCaptureTime())%>]<%if(castle3==null){%>[?]<%}else{%><a href="user.jsp?uid=<%=his2.getUid()%>"><%=user3.getNameWml()%></a><%}%>-<%if(castle3==null){%>[?]<%}else{%><a href="search.jsp?cid=<%=his2.getCid()%>"><%=castle3.getCastleNameWml()%>(<%=castle3.getX()%>|<%=castle3.getY()%></a>)<%}%><br/><%

}}%>
<%}%><br/>
<a href="fun.jsp?pos=<%=pos%>">返回宝库</a><br/>
<%

%><%}%>