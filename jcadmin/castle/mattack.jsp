<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int cid = action.getParameterInt("cid");
	CastleBean castle = CastleUtil.getCastleById(cid);
	if(!action.isMethodGet()){

		int type = action.getParameterInt("atype");
		int x = action.getParameterInt("x");
		int y = action.getParameterInt("y");

		int[] count = new int[ResNeed.soldierTypeCount + 1];

		if(type != 2 && type != 3) {		// 非侦察
			for(int i = 0;i < count.length;i++) {
				count[i] = action.getParameterInt("count" + i);
			}
		} else {		//  侦察
			count[6] = action.getParameterInt("count6");

		}

		float heroSpeed = 100f;	// 默认20
		if(count[0] != 0) {
			CastleUserBean castleUser = CastleUtil.getCastleUser(castle.getUid());
			HeroBean hero = castleUser.getHero();
			if(hero != null)
				heroSpeed = hero.getHeroSoldier().getSpeed();
		}

		AttackThreadBean attackThreadBean = new AttackThreadBean(count,
				castle.getId(), castle.getX(), castle.getY(), castle,
				cid, x, y, 0, heroSpeed);		
		attackThreadBean.setCid(castle.getId());
		attackThreadBean.setType(type);
		if(count[8] != 0) {		// 如果携带投石车，选择攻击
			int opt = action.getParameterInt("opt");
			attackThreadBean.setOpt(opt);
		}
		float speed = AttackThreadBean.calcSpeed(count,
				castle.getX(), castle.getY(), castle.getRace(),
				x, y, heroSpeed);
		List cidList = StringUtil.toInts(request.getParameter("cids"));
		for(int i = 0;i < cidList.size();i++){
			Integer iid = (Integer)cidList.get(i);
			if(attackThreadBean.resetTarget(speed, 0, iid.intValue(), castle.getX(), castle.getY()))
				cacheService.addCacheAttack(attackThreadBean);
		}

		response.sendRedirect("castle.jsp?id="+cid);
		return;
	}
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	批量攻击<br/>
【<a href="castle.jsp?id=<%=cid%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>

<form action="mattack.jsp?cid=<%=cid%>" method=post>

目标城堡id<textarea name="cids" style="width:400px;height:150px;"></textarea><br/>
<%
	SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
	%><table><%
	for(int i = 1;i<so.length; i++) {
		SoldierResBean soldier = so[i];
%><tr><td><%=soldier.getSoldierName()%></td><td><input type=text name="count<%=i%>" value=""><%if(i==8){
%><select name="opt" value="0"><option value="0">随意攻击</option>
<%for(int i2=1;i2<ResNeed.buildingTypeCount;i2++)if(ResNeed.gatherOpt[i2]<=20){
%><option value="<%=i2%>"><%=ResNeed.getTypeName(i2)%></option><%
}%></select><%}%></td></tr><%
}%><tr><td>英雄</td><td><input type=text name="count0" value=""></td></tr></table>
<select name="atype" value="5">
<option value="5">支援</option>
<option value="0">攻击</option>
<option value="1">抢夺</option> 
<option value="2">侦察资源和军队</option>
<option value="3">侦察建筑和军队</option>
</select><br/><br/>

<input type=submit value="确认发出">
</form>
	</body>
</html>