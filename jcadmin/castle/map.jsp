<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static int formatAxis(int a) {
 if(a<0) return a+CastleUtil.mapSize; else if(a>=CastleUtil.mapSize) return a-CastleUtil.mapSize; else return a;
}
static String[] typeColor={"",
"","","","","#dd6666","red",
"#6666dd","#6666dd","#6666dd","blue","blue","blue","","","","",
"#66dd66","#dd6666","#6666dd","#00ff00","#ff0000","#0000ff","gray","black"};
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	if(id==0)
		id = action.getParameterInt("cid");
	int x = action.getParameterInt("x");
	int y = action.getParameterInt("y");
	int pos = action.getParameterIntS("pos");
	if(pos != -1) {
		x = CastleUtil.pos2X(pos);
		y = CastleUtil.pos2Y(pos);
	}
	int[][] map = CastleUtil.getMap();
	byte[][] mapType = CastleUtil.getMapType();

	long now = System.currentTimeMillis();

	if(action.hasParam("a")){
		int x1 = action.getParameterInt("x1");
		int x2 = action.getParameterInt("x2");
		int y1 = action.getParameterInt("y1");
		int y2 = action.getParameterInt("y2");
		for(int i=x1;i<=x2;i++)
			for(int j=y1;j<=y2;j++){
				CastleUtil.getMapType(i,j);
			}
		response.sendRedirect("map.jsp?x="+x+"&y="+y);
		return;
	}
	if(action.hasParam("r")){
		int x1 = action.getParameterInt("x1");
		int x2 = action.getParameterInt("x2");
		int y1 = action.getParameterInt("y1");
		int y2 = action.getParameterInt("y2");
		DbOperation db = new DbOperation(5);
		for(int i=x1;i<=x2;i++){
			for(int j=y1;j<=y2;j++){
				mapType[i][j]=0;
			}
			db.executeUpdate("delete from castle_map_type where pos between " + CastleUtil.xy2Pos(i,y1) + " and " + CastleUtil.xy2Pos(i,y2));
		}
		db.release();
		response.sendRedirect("map.jsp?x="+x+"&y="+y);
		return;
	}

%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body style="font-size:10px;line-height:11px;" nowrap=true>
地图信息<br/>
<%
int dis=50;
int uid = 0;
int tongId = 0;

for(int j=y-dis;j <= y+dis;j++){
int j2=formatAxis(j);
 for(int i=x-dis;i <= x+dis;i++){ 
 int i2=formatAxis(i);%><a href="castle.jsp?x=<%=i2%>&y=<%=j2%>"><%if(map[i2][j2]!=0){
%><font color="<%=typeColor[mapType[i2][j2]]%>">●</font><%}else{

if(mapType[i2][j2]==0){
%><font color="#dddddd">□</font><%}else if(mapType[i2][j2]<=16){
%><font color="<%=typeColor[mapType[i2][j2]]%>">○</font><%}else{

OasisBean oasis = CastleUtil.getOasisByXY(i2,j2);
if(oasis.getCid()==0){
%><font color="<%=typeColor[mapType[i2][j2]]%>">△</font><%}else{%><font color="<%=typeColor[mapType[i2][j2]]%>">▲</font><%}
}
}%><%}%></a>|<%=j2%><br/><%
}%>
<%=formatAxis(x-dis)%>-<%=x%>-<%=formatAxis(x+dis)%><br/><br/>

<form action="map.jsp?x=<%=x%>&y=<%=y%>" method=post>
(<input type=text name="x1">)|(<input type=text name="y1">) - (<input type=text name="x2">)|(<input type=text name="y2">)<br/>
<input type=submit name="r" value="重置地图">
<input type=submit name="a" value="生成地图">
</form><br/>

<%@include file="bottom.jsp"%>
</html>