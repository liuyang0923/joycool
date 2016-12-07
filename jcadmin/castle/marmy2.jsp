<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	AttackThreadBean attack = cacheService.getCacheAttack(id);
	if(attack==null){
		response.sendRedirect("index.jsp");
		return;
	}
	CastleArmyBean army = attack.toArmy();
	CastleBean castle = CastleUtil.getCastleById(attack.getCid());
	if(!action.isMethodGet()&&army!=null){

		for(int i = 0;i <= ResNeed.soldierTypeCount;i++) {
			int count = action.getParameterInt("count" + i);
			army.setCount(i,count);
		}
		SqlUtil.executeUpdate("update cache_attack set soldier_count='"+army.toString()+"',hero="+army.getHero()+" where id="+attack.getId(),5);
		response.sendRedirect("marmy2.jsp?id="+id);
		return;
	}

%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	修改军队<br/>
军队所属:【<a href="castle2.jsp?id=<%=castle.getId()%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>
军队种族:<%=castle.getRaceName()%><br/>
<form action="marmy2.jsp?id=<%=id%>" method=post><%
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