<%@ page contentType="text/vnd.wap.wml;charset=utf-8" %><%@include file="pageinc.jsp"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.bean.*,net.joycool.wap.cache.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%@ page import="java.util.*"%><%!
static int formatAxis(int a) {
 if(a<0) return a+CastleUtil.mapSize; else if(a>=CastleUtil.mapSize) return a-CastleUtil.mapSize; else return a;
}
%><%
	
	
	CastleAction action = new CastleAction(request);
	boolean flag = action.getAround();
	int dis = action.getParameterInt("dis");
//	if(dis<3) 
		dis=3;
	List list = null;
	PagingBean paging = null;
	if(flag){
		list = (ArrayList)(request.getAttribute("list"));
		paging = new PagingBean(action, list.size(), 5, "p");
	}
	int[][] map = CastleUtil.getMap();
	byte[][] mapType = CastleUtil.getMapType();
	int x = action.getAttributeInt("x");
	int y = action.getAttributeInt("y");
	action.curPage = 3;
	action.setAttribute2("casSwi","around.jsp");
%><?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/wml_1.1.xml">
<wml><card title="周围玩家"><p>
<%@include file="top.jsp"%>
<%if(!flag) {%><%=request.getAttribute("msg")%><%} else {
%>=<a href="search.jsp?x=<%=x%>&amp;y=<%=y%>"><%=x%>|<%=y%></a>周围的地图=<br/><%
	if(list.size() == 0) {
%>周围没有城堡<br/><%} else {

			for(int i = paging.getStartIndex();i < paging.getEndIndex(); i++) {
				
				int castleId = ((Integer)(list.get(i))).intValue();
			
				CastleBean castleBean = CastleUtil.getCastleById(castleId);
%><a href="<%=("search.jsp?x="+castleBean.getX()+"&amp;y="+castleBean.getY())%>"><%=castleBean.getX()%>|<%=castleBean.getY()%></a>&nbsp;<%=StringUtil.toWml(castleBean.getCastleName())%><br/>
<%}%><%=paging.shuzifenye("around.jsp?x="+x+"&amp;y="+y, true, "|", response)%>
<%}
int uid = action.getCastle().getUid();
int tongId = action.getCastleUser().getTong();
for(int j=y-dis;j <= y+dis;j++){
 int j2 = formatAxis(j);
 for(int i=x-dis;i <= x+dis;i++) {
  int i2 = formatAxis(i);
  int cid=map[i2][j2];
 if(cid!=0){
 CastleBean c = CastleUtil.getCastleById(cid);
 if(c.getRace()==5){%>◆<%
 }else if(c.getUid()==uid){
%>★<%}else if(tongId!=0){
	CastleUserBean u = CastleUtil.getCastleUser(c.getUid());
	if(tongId == u.getTong()){
%>☆<%}else{%>●<%}%><%}else{%>●<%}%><%}else{

if(mapType[i2][j2]==0){
%>□<%}else if(mapType[i2][j2]<=16){
%>○<%}else{

OasisBean oasis = CastleUtil.getOasisByXY(i2,j2);
if(oasis.getCid()==0){
%>△<%}else{%>▲<%}
}
}}%>|<%if(j==y-dis||j==y+dis){%><a href="around.jsp?x=<%=x%>&amp;y=<%=j2%>"><%=j2%></a><%}else{%><%=j2%><%}%><br/><%
}
int x1=formatAxis(x-dis);
int x2=formatAxis(x+dis);
%>
<a href="around.jsp?x=<%=x1%>&amp;y=<%=y%>"><%=x1%></a>-<%=x%>-<a href="around.jsp?x=<%=x2%>&amp;y=<%=y%>"><%=x2%></a><br/>
X:<input name="x" format="*N"/>Y:<input name="y" format="*N"/><br/><anchor>查询坐标<go href="search.jsp"><postfield name="x" value="$x"/><postfield name="y" value="$y"/></go></anchor><br/>
<%}%><a href="s.jsp">返回城堡战争首页</a><br/><%=BaseAction.getBottomShort(request, response)%></p></card></wml>