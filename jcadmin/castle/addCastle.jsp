<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
	static CastleService castleService = CastleService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int cid = action.getParameterInt("cid");
	CastleBean castle = CastleUtil.getCastleById(cid);
		int x=action.getParameterIntS("x");
		int y=action.getParameterIntS("y");
	if(!action.isMethodGet()){
		int type=action.getParameterInt("type");
		int race=action.getParameterInt("race");
		int uid=action.getParameterInt("uid");
		String name = action.getParameterNoEnter("name");
		String cname = action.getParameterNoEnter("cname");
		castle = CastleUtil.createCastle(uid, name, race, x, y, type, cname, request.getParameter("main")!=null);
		if(castle != null){
			BuildingBean city = new BuildingBean(ResNeed.CITY_BUILD, 1, castle.getId(), 1, 19);//19为主城的位置pos
			if(castle.isNatar())	// 纳塔的城堡位置不同，被奇迹占了
				city.setBuildPos(21);
			castleService.addBuilding(city);
		}
		response.sendRedirect("castle.jsp?x="+x+"&y="+y);
		return;
	}
	if(request.getParameter("del")!=null){
		CastleUtil.deleteArt(cid);
		response.sendRedirect("castle.jsp?x="+x+"&y="+y);
		return;
	}
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	添加城堡/城主<br/>
【<a href="castle.jsp?x=<%=x%>&y=<%=y%>">荒漠</a>】(<%=x%>|<%=y%>)<br/>

<form action="addCastle.jsp?x=<%=x%>&y=<%=y%>" method=post>
uid:<input type=text name="uid" value="">
名称:<input type=text name="name" value=""><br/><br/>
城堡名称:<input type=text name="cname" value="">
类型:<input type=text name="type" value="1">
种族:<select name="race"><%for(int i=1;i<=5;i++){%><option value="<%=i%>" <%if(i==5){%>selected<%}%>><%=ResNeed.raceNames[i]%></option><%}%></select>
设为主城:<input type=checkbox name="main" value="1">
<input type=submit value="确认添加">
</form>
	</body>
</html>