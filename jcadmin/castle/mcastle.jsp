<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.spec.castle.*,net.joycool.wap.cache.*,java.util.*,net.joycool.wap.framework.*,net.joycool.wap.util.*,net.joycool.wap.util.db.DbOperation"%><%!
static CastleService service = CastleService.getInstance();
%><%
	response.setHeader("Cache-Control", "no-cache");
	
	CustomAction action = new CustomAction(request);
	int cid = action.getParameterInt("cid");
	CastleBean castle = CastleUtil.getCastleById(cid);
	if(castle==null){
		response.sendRedirect("index.jsp");
		return;
	}
	UserResBean userRes = CastleUtil.getUserResBeanById(cid);
	if(!action.isMethodGet()){
		int fm = action.getParameterInt("fm");
		if(fm==0){
			int type=action.getParameterInt("type");
			int x=action.getParameterIntS("x");
			int y=action.getParameterIntS("y");
			if(x >= 0 && y >= 0 && x<CastleUtil.mapSize &&y<CastleUtil.mapSize){
				CastleUtil.getMap()[castle.getX()][castle.getY()]=0;
				castle.setX(x);
				castle.setY(y);
				CastleUtil.getMap()[x][y]=castle.getId();
			}
			int flag=action.getParameterFlag("flag");
			type |= flag;
			castle.setType(type);
			castle.setExpand(action.getParameterInt("expand"));
			castle.setExpand2(action.getParameterInt("expand2"));
			castle.setRace(action.getParameterInt("race"));
			String name = action.getParameterNoEnter("name");
			castle.setCastleName(name);
			service.updateCastle(castle);
		} else if(fm == 1){
			userRes.setPeople(action.getParameterInt("people"));
			userRes.setPeople2(action.getParameterInt("people2"));
			userRes.setMaxRes(action.getParameterInt("maxRes"));
			userRes.setMaxGrain(action.getParameterInt("maxGrain"));
			userRes.setCave(action.getParameterInt("cave"));
			userRes.setCivil(action.getParameterInt("civil"));
			userRes.setWoodSpeed(action.getParameterInt("wood"));
			userRes.setStoneSpeed(action.getParameterInt("stone"));
			userRes.setFeSpeed(action.getParameterInt("fe"));
			userRes.setGrainSpeed(action.getParameterInt("grain"));
			userRes.setFlag(action.getParameterFlag("flag"));
			service.updateUserResAll(userRes);
		} else if(fm == 2){
			int allGrade = action.getParameterIntS("grade");
			int allGrade2 = action.getParameterIntS("grade2");
			DbOperation db = new DbOperation(5);
			if((allGrade < 0 || allGrade2 < 0)&&!action.hasParam("every")) {	// 删除所有升级和研发
				db.executeUpdate("delete from castle_soldier_smithy where cid="+cid);
				CacheManage.castleSmithy.srm(cid);
			} else {
			
				SoldierSmithyBean[] smithys = CastleBaseAction.getSmithys(cid);
				
				for(int i=1;i<=8;i++){
					if(smithys[i]==null) {
						SoldierSmithyBean ssBean = new SoldierSmithyBean();
						int g1 = action.getParameterIntS("att" + i);
						if(g1==-1)
							g1=allGrade;
						int g2 = action.getParameterIntS("def" + i);
						if(g2==-1)
							g2=allGrade;
						if(g1!=-1)
							ssBean.setAttack(g1);
						if(g2!=-1)
							ssBean.setDefence(g2);
						ssBean.setCid(cid);
						ssBean.setSoldierType(i);
						if(g1!=-1&&g2!=-1){
						service.addSoldierSmithy(ssBean);
						smithys[i]=ssBean;
						}
					} else {
						int g1 = action.getParameterIntS("att" + i);
						if(g1==-1)
							g1=allGrade;
						int g2 = action.getParameterIntS("def" + i);
						if(g2==-1)
							g2=allGrade;
						if(g1!=-1)
							smithys[i].setAttack(g1);
						if(g2!=-1)
							smithys[i].setDefence(g2);
						if(g1!=-1&&g2!=-1)
						db.executeUpdate("UPDATE castle_soldier_smithy SET attack="+g1+",defence="+g2+" where id="+smithys[i].getId());
					}
				}
			}
			db.release();
		}
		response.sendRedirect("castle.jsp?id="+cid);
		return;
	}
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	修改城堡<br/>
【<a href="castle.jsp?id=<%=cid%>"><%=castle.getCastleNameWml()%></a>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>
<table><tr><td>
<form action="mcastle.jsp?cid=<%=cid%>" method=post>
名称:<input type=text name="name" value="<%=castle.getCastleNameWml()%>">(id:<%=castle.getId()%>)<br/>
种族:<input type=text name="race" value="<%=castle.getRace()%>"><br/>
占领城堡:<input type=text name="expand" value="<%=castle.getExpand()%>"><br/>
占领绿洲:<input type=text name="expand2" value="<%=castle.getExpand2()%>"><br/>
坐标:<input type=text name="x" value="<%=castle.getX()%>">|<input type=text name="y" value="<%=castle.getY()%>"><br/>
<input type=checkbox name="flag" value="5"<%if(castle.isNatar()){%> checked<%}%>>奇迹村<br/>
<input type=checkbox name="flag" value="6"<%if(castle.isPower()){%> checked<%}%>>无法攻打(自动回派攻打部队)<br/>
<input type=checkbox name="flag" value="7"<%if(castle.isLock()){%> checked<%}%>>临时冻结<br/>
<input type=checkbox name="flag" value="8"<%if(castle.isArt()){%> checked<%}%>>宝库村<br/>
类型:<input type=text name="type" value="<%=castle.getType2()%>"><br/>
<input type=submit value="确认修改">
</form><br/>
<br/>
<form action="mcastle.jsp?cid=<%=cid%>&fm=1" method=post>
人口:<input type=text name="people" value="<%=userRes.getPeople()%>"><br/>
士兵人口:<input type=text name="people2" value="<%=userRes.getPeople2()%>"><br/>
文明度:<input type=text name="civil" value="<%=userRes.getCivil()%>"><br/>
仓库:<input type=text name="maxRes" value="<%=userRes.getMaxRes()%>"><br/>
粮仓:<input type=text name="maxGrain" value="<%=userRes.getMaxGrain()%>"><br/>
山洞:<input type=text name="cave" value="<%=userRes.getCave()%>"><br/>

产量:木<input type=text name="wood" value="<%=userRes.getWoodSpeed()%>" size=5>
石<input type=text name="stone" value="<%=userRes.getStoneSpeed()%>" size=5>
铁<input type=text name="fe" value="<%=userRes.getFeSpeed()%>" size=5>
粮<input type=text name="grain" value="<%=userRes.getGrainSpeed()%>" size=5><br/>

<input type=checkbox name="flag" value="0"<%if(userRes.isFlagWood()){%> checked<%}%>>木头产量增加25%<br/>
<input type=checkbox name="flag" value="1"<%if(userRes.isFlagStone()){%> checked<%}%>>石头产量增加25%<br/>
<input type=checkbox name="flag" value="2"<%if(userRes.isFlagFe()){%> checked<%}%>>铁块产量增加25%<br/>
<input type=checkbox name="flag" value="3"<%if(userRes.isFlagGrain()){%> checked<%}%>>粮食产量增加25%<br/>
<input type=checkbox name="flag" value="4"<%if(userRes.isFlagAttack()){%> checked<%}%>>军队攻击力增加10%<br/>
<input type=checkbox name="flag" value="5"<%if(userRes.isFlagDefense()){%> checked<%}%>>军队防御力增加10%<br/>
<input type=checkbox name="flag" value="6"<%if(userRes.isFlagArt()){%> checked<%}%>>军队粮食消耗减少50%<br/>
<input type=checkbox name="flag" value="7"<%if(userRes.isFlagArt2()){%> checked<%}%>>军队粮食消耗减少25%<br/>
<input type=submit value="确认修改">
</form>
</td><td valign=top>
<form action="mcastle.jsp?cid=<%=cid%>&fm=2&every=1" method=post>
<table class="tbg" cellpadding="2" cellspacing="1">
<tr><td></td>
<td class="rbg">兵种</td><td class="rbg">攻击等级</td><td class="rbg">防御等级</td>
</tr>
<%
SoldierSmithyBean[] fromSmithy = CastleBaseAction.getSmithys(cid);
SoldierResBean[] so3 = ResNeed.getSoldierTs(castle.getRace());
for(int i=1;i<=10;i++){
SoldierSmithyBean smithy = fromSmithy[i];
%><tr><td><%=i%>.</td><td align=left><%=so3[i].getSoldierName()%></td><td><%if(smithy==null){%>-<%}else{%>等级<input type=text size=1 name="att<%=i%>" value="<%=smithy.getAttack()%>"><%}%></td><td><%if(smithy==null){%>-<%}else{%>等级<input type=text size=1 name="def<%=i%>" value="<%=smithy.getDefence()%>"><%}%></td></tr><%

}
%>
</table>
<input type=submit value="确认修改">
</form>
<form action="mcastle.jsp?cid=<%=cid%>&fm=2" method=post>
<input type=text name="grade" value="20" size=5>
<input type=text name="grade2" value="20" size=5>
<input type=submit value="批量设置">
</form>
<form action="mcastle.jsp?cid=<%=cid%>&fm=2" method=post>
<input type=hidden name="grade" value="-1" size=5>
<input type=submit value="全部删除">
</form>
</td></tr></table>
	</body>
</html>