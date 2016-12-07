<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.util.*,net.joycool.wap.framework.*"%><%
	
	
	CastleAction action = new CastleAction(request);
	CastleUserBean my = action.getCastleUser();
	int x, y;
	int pos = action.getParameterIntS("pos");
	if(pos == -1) {
		x = action.getParameterIntS("x");
		y = action.getParameterIntS("y");
	} else {
		x = CastleUtil.pos2X(pos);
		y = CastleUtil.pos2Y(pos);
	}

	CastleBean castleBean = null;
	UserResBean resBean = null;
	boolean flag = action.search();
	CastleUserBean user2 = null;
	if(flag) {
		castleBean = (CastleBean)request.getAttribute("castleBean");
		resBean = CastleUtil.getUserResBeanById(castleBean.getId());
		user2 = CastleUtil.getCastleUser(castleBean.getUid());
		x = castleBean.getX();
		y = castleBean.getY();
	} else {
		if(x<0) x=0;
		if(x>=CastleUtil.mapSize) x=CastleUtil.mapSize-1;
		if(y<0) y=0;
		if(y>=CastleUtil.mapSize) y=CastleUtil.mapSize-1;
	}
	CastleBean myCastleBean = action.getCastle();
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="侦查"><p><%@include file="top.jsp"%>
<%if(castleBean!=null) {
boolean locked=user2.isLocked()||castleBean.isLock();
	// 自己的城堡或者同盟等显示不同的符号
if(user2.getUid()==my.getUid()){%>★<%}else if(user2.getRace()==5){%>◆<%}else if(my.getTong()!=0&&my.getTong()==user2.getTong()){%>☆<%}else{%>●<%}

%><%=castleBean.getCastleNameWml()%>(<%=castleBean.getX()%>|<%=castleBean.getY()%>)<br/>
城主:<a href="user.jsp?uid=<%=castleBean.getUid() %>"><%=user2.getNameWml()%></a><%if(locked){%>(冻结)<%}%><br/>
种族:<%=user2.getRaceName()%><br/>
<%if(resBean!=null){%>人口:<%=resBean.getPeople()%><br/><%}%>
<%if(castleBean.getId()!=action.getCastle().getId()&&!locked){%><a href="attack.jsp?x=<%=castleBean.getX()%>&amp;y=<%=castleBean.getY()%>">出兵</a>|<a href="merchant.jsp?x=<%=castleBean.getX()%>&amp;y=<%=castleBean.getY()%>">派出商人</a><%}else{%>出兵|派出商人<%}%>|<a href="msg2.jsp?cid=<%=castleBean.getId()%>">战报</a><br/>
<%} else {
	int type = CastleUtil.getMapType(x,y);

if(type<=16){
	int[] block = ResNeed.initResBlock[type];
%>○荒漠(<%=x%>|<%=y%>)<br/>资源分布情况:伐木场<%=block[0]%>块,采石场<%=block[1]%>块,铁矿场<%=block[2]%>块,粮田<%=block[3]%>块<br/>
<a href="atkCy.jsp?t=4&amp;x=<%=x%>&amp;y=<%=y%>">建立一个新的城堡</a><br/><%}else{
	// 绿洲
	OasisBean oasis = CastleUtil.getOasisByXY(x, y);
	if(oasis.getCid()!=0){
		CastleBean oasisCastle = CastleUtil.getCastleById(oasis.getCid());
		CastleUserBean user3 = CastleUtil.getCastleUser(oasisCastle.getUid());
%>▲绿洲(<%=x%>|<%=y%>)<br/>占领方:<a href="user.jsp?uid=<%=oasisCastle.getUid()%>"><%=user3.getNameWml()%></a><br/>
所属城堡:<a href="search.jsp?cid=<%=oasis.getCid()%>"><%=oasisCastle.getCastleNameWml()%></a><br/><%}else{%>△绿洲(<%=x%>|<%=y%>)<br/><%}
%>资源情况:<%=ResNeed.oasisInfo[type - 16]%><br/>防御:未知<br/><a href="attack2.jsp?x=<%=x%>&amp;y=<%=y%>">出兵</a>|<a href="msg2.jsp?pos=<%=CastleUtil.xy2Pos(x,y)%>">战报</a><br/><%}%>
<%} %><a href="around.jsp?x=<%=x%>&amp;y=<%=y%>">查看周围地图</a><br/>
<%
int x2,y2;
%>往北-<%y2=CastleUtil.formatAxis(y-1);%><a href="search.jsp?x=<%=x%>&amp;y=<%=y2%>">(<%=x%>|<%=y2%>)</a><br/>
<%x2=CastleUtil.formatAxis(x-1);%><a href="search.jsp?x=<%=x2%>&amp;y=<%=y%>">(<%=x2%>|<%=y%>)</a>-<%x2=CastleUtil.formatAxis(x+1);%><a href="search.jsp?x=<%=x2%>&amp;y=<%=y%>">(<%=x2%>|<%=y%>)</a><br/>
往南-<%y2=CastleUtil.formatAxis(y+1);%><a href="search.jsp?x=<%=x%>&amp;y=<%=y2%>">(<%=x%>|<%=y2%>)</a><br/>
X:<input name="x" format="*N"/>Y:<input name="y" format="*N"/><br/><anchor>查询坐标<go href="search.jsp"><postfield name="x" value="$x"/><postfield name="y" value="$y"/></go></anchor><br/>
==本城信息==<br/>
<%=StringUtil.toWml(myCastleBean.getCastleName())%>(<%=myCastleBean.getX()%>|<%=myCastleBean.getY()%>)<br/>
<a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>