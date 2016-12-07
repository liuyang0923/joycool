<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	CastleArmyBean army = service.getCastleArmyById(id);
	CastleBean castle = CastleUtil.getCastleById(army.getCid());
	CastleBean castle2;
	if(army.getCid()==army.getAt())
		castle2=castle;
	else
		castle2 = CastleUtil.getCastleById(army.getAt());
	if(!action.isMethodGet()&&army!=null){

		for(int i = 0;i <= ResNeed.soldierTypeCount;i++) {
			int count = action.getParameterInt("count" + i);
			army.setCount(i,count);
		}
		service.updateSoldierCount(army);
		response.sendRedirect("marmy.jsp?id="+id);
		return;
	}

%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	修改军队<br/>
军队所属:【<a href="castle2.jsp?id=<%=castle.getId()%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>
军队位置:【<a href="castle2.jsp?id=<%=castle2.getId()%>"><%=castle2.getCastleNameWml()%></a>】(<%=castle2.getX()%>|<%=castle2.getY()%>)<br/>
军队种族:<%=castle.getRaceName()%><br/>
<form action="marmy.jsp?id=<%=id%>" method=post><%
	SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
	%><table><%
	for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
		int count = army.getCount(soldier.getType());
%><tr><td><%=soldier.getSoldierName()%></td><td><input type=text name="count<%=i%>" value="<%=count%>"></td></tr><%
}%><tr><td>英雄</td><td><input type=text name="count0" value="<%=army.getHero()%>"></td></tr></table>
<input type=submit value="确认修改">
</form>
	</body>
</html>