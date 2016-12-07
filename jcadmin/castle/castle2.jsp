<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
static String[] valueNames = {"木材","石头","铁块","粮食","木材%","石头%","铁块%","粮食%","仓库","粮仓","人口","士兵","山洞","文明度","城墙%","商人"};
static int calcGrainCost(List counts, SoldierResBean[] so, int hero){
	int sum = 0;
	for(int i = 1;i<so.length; i++) {
		int count = ((Integer)counts.get(i-1)).intValue();
		if(count > 0)
			sum += so[i].getPeople() * count;
	}
	sum += hero * 6;
	return sum;
}
	static SoldierThreadBean getSoldierThreadBean(ResultSet rs) throws SQLException{
		SoldierThreadBean bean = new SoldierThreadBean();
		bean.setId(rs.getInt("id"));
		bean.setCid(rs.getInt("cid"));
		bean.setSoldierType(rs.getInt("soldier_type"));
		bean.setCount(rs.getInt("count"));
		bean.setStartTime(rs.getLong("start_time"));
		bean.setEndTime(rs.getLong("end_time"));
		bean.setInterval(rs.getInt("interval_time"));
		
		return bean;
	}
	static List getCacheSoldierByCid(int cid) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from cache_soldier where cid = " + cid;
		
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				list.add(getSoldierThreadBean(rs));
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		
		return list;
	}
	static void setResValue(UserResBean castleRes, int modify, int modValue) {
		switch(modify){
		case 0:
			castleRes.setWoodSpeed(modValue);
			break;
		case 1:
			castleRes.setStoneSpeed(modValue);
			break;
		case 2:
			castleRes.setFeSpeed(modValue);
			break;
		case 3:
			castleRes.setGrainSpeed(modValue);
			break;
		case 4:
			castleRes.setOtherWoodSpeed(modValue);
			break;
		case 5:
			castleRes.setOtherStoneSpeed(modValue);
			break;
		case 6:
			castleRes.setOtherFeSpeed(modValue);
			break;
		case 7:
			castleRes.setOtherGrainSpeed(modValue);
			break;
		case 8:
			castleRes.setMaxRes(modValue);
			break;
		case 9:
			castleRes.setMaxGrain(modValue);
			break;
		case 10:
			castleRes.setPeople(modValue);
			break;
		case 11:
			castleRes.setPeople2(modValue);
			break;
		case 12:
			castleRes.setCave(modValue);
			break;
		case 13:
			castleRes.setCivil(modValue);
			break;
		case 14:
			castleRes.setWall(modValue);
			break;
		case 15:
			castleRes.setMerchant(modValue);
			service.updateUserMerchant(castleRes.getId(), modValue);
			break;
		}
	}
%><%
	CustomAction action = new CustomAction(request);
	int id = action.getParameterInt("id");
	int x = action.getParameterInt("x");
	int y = action.getParameterInt("y");
	int[][] map = CastleUtil.getMap();
	CastleBean castle = null;
	if(id==0){
		id = map[x][y];
		castle = CastleUtil.getCastleById(id);
	} else {
		castle = CastleUtil.getCastleById(id);
		x = castle.getX();
		y = castle.getY();
	}
	UserResBean castleRes = CastleUtil.getUserResBeanById(id);
	CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());

	List buildings = service.getAllBuilding(castle.getId());
	BuildingBean[] buildPos = new BuildingBean[41];
	for(int i = 0;i < buildings.size();i++){
		BuildingBean b = (BuildingBean)buildings.get(i);
		buildPos[b.getBuildPos()] = b;
	}
	int[] baseBuild = ResNeed.baseBuildRes[castle.getType2()];
	
	int modify = action.getParameterIntS("i");
	int modValue = action.getParameterInt("value");
	if(modify!=-1){
		if(modify==99){
			for(int i=0;i<valueNames.length;i++){
				modValue = action.getParameterIntS("value"+i);
				if(modValue!=-1)
					setResValue(castleRes,i, modValue);
			}
		} else {
			setResValue(castleRes,modify, modValue);
		}
	
		if(modify!=15)
			service.updateUserResAll(castleRes);
			
		if(modify==99){
			response.sendRedirect("user2.jsp?id="+castleRes.getUserId());
			return;
		}
	}
	
	
%><html>
	<head>
	</head>
<link href="main.css" rel="stylesheet" type="text/css">
	<body>
	城堡信息<br/><br/>
【<%=castle.getCastleNameWml()%>】(<%=castle.getX()%>|<%=castle.getY()%>)<br/>
<table class="tbg" cellpadding="2" cellspacing="1"><tr valign=top><td width="300">
城主:<a href="user.jsp?id=<%=castle.getUid()%>"><%=user.getNameWml()%></a><br/>
<%
int[] values = new int[valueNames.length];
int[] values2 = {
castleRes.getWoodSpeed(),
castleRes.getStoneSpeed(),
castleRes.getFeSpeed(),
castleRes.getGrainSpeed(),
castleRes.getOtherWoodSpeed(),
castleRes.getOtherStoneSpeed(),
castleRes.getOtherFeSpeed(),
castleRes.getOtherGrainSpeed(),
castleRes.getMaxRes(),
castleRes.getMaxGrain(),
castleRes.getPeople(),
castleRes.getPeople2(),
castleRes.getCave(),
castleRes.getCivil(),
castleRes.getWall(),
castleRes.getMerchant(),
};

int[] initResBlock = ResNeed.initResBlock[castle.getType2()];
int woodbase = initResBlock[0] * ResNeed.RES_BASE;
int stonebase=initResBlock[1] * ResNeed.RES_BASE;
int febase=initResBlock[2] * ResNeed.RES_BASE;
int grainbase=initResBlock[3] * ResNeed.RES_BASE;

for(int i = 1; i <= 40; i ++) {
  if(buildPos[i] !=null){
  	if(buildPos[i].getGrade()>0){
		BuildingTBean bt = ResNeed.getBuildingT(buildPos[i].getBuildType(),buildPos[i].getGrade());
		switch(bt.getBuildType()){
		case ResNeed.CITY_BUILD:
			break;
		case ResNeed.WOOD_BUILD:
			values[0]+=bt.getValue();
			woodbase-=ResNeed.RES_BASE;
			break;
		case ResNeed.FE_BUILD:
			values[2]+=bt.getValue();
			febase-=ResNeed.RES_BASE;
			break;
		case ResNeed.GRAIN_BUILD:
			values[3]+=bt.getValue();
			grainbase-=ResNeed.RES_BASE;
			break;
		case ResNeed.STONE_BUILD:
			values[1]+=bt.getValue();
			stonebase-=ResNeed.RES_BASE;
			break;
		case ResNeed.WOOD_FACTORY_BUILD:
			values[4]+=bt.getValue();
			break;
		case ResNeed.STONE_FACTORY_BUILD:
			values[5]+=bt.getValue();
			break;
		case ResNeed.FOUNDRY_BUILD:
			values[6]+=bt.getValue();
			break;
		case ResNeed.MOFANG_BUILD:
		case ResNeed.BREAD_BUILD:
			values[7]+=bt.getValue();
			break;
		case ResNeed.STORAGE_BUILD:
		case ResNeed.STORAGE2_BUILD:
			values[8]+=bt.getValue();
			break;
		case ResNeed.BARN_BUILD:
		case ResNeed.BARN2_BUILD:
			values[9]+=bt.getValue();
			break;
		case ResNeed.CAVE_BUILD:
			values[12]+=bt.getValue();
			break;
		case ResNeed.WALL_BUILD:
		case ResNeed.WALL2_BUILD:
		case ResNeed.WALL3_BUILD:
			values[14]+=bt.getValue();
			break;
		}
		values[10]+=bt.getTotalPeople();
		values[13]+=bt.getCivil();
	}
%><%=ResNeed.getTypeName(buildPos[i].getBuildType())%><%=buildPos[i].getGrade()%><%
} else if(i<=18){
%><%=ResNeed.getTypeName(baseBuild[i])%><%}else{
%>空地<%
}if((i)%4 == 0) {%><br/><%}else{%>.<%}%><%}%><br/>
<%
// 计算绿洲产量
List olist = service.getOasisList("cid="+castle.getId());
for(int j = 0;j < olist.size();j++) {
	OasisBean oasis = (OasisBean)olist.get(j);
		int add = 25;
		switch(oasis.getType()) {
		case 1: {
			values[4] += add;
		} break;
		case 2: {
			values[5] += add;
		} break;
		case 3: {
			values[6] += add;
		} break;
		case 4: {
			values[4] += add;
			values[7] += add;
		} break;
		case 5: {
			values[5] += add;
			values[7] += add;
		} break;
		case 6: {
			values[6] += add;
			values[7] += add;
		} break;
		case 7: {
			values[7] += add;
		} break;
		case 8: {
			values[7] += add;
			values[7] += add;
		} break;
		}
}
CastleUtil.getCastleArmy(id);
List armyList = service.getCastleArmyAtList(id);
List attackList = cacheService.getCacheAttackByToCid(id);
	// 计算绿洲的兵力
	List armyList4 = service.getOasisArmyAtCidList(id);


CastleArmyBean hiddenArmy = CastleUtil.getCastleHiddenArmy(id);
attackList.addAll(cacheService.getCacheAttackByFromCid(id));
if(hiddenArmy!=null)
	armyList.add(hiddenArmy);
%>
<table class="tbg" cellpadding="2" cellspacing="1"><tr><td></td><td colspan=2>城堡里的军队</td><td>消耗</td></tr>
<%
SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
for(int i=0;i<armyList.size();i++){
	CastleArmyBean army = (CastleArmyBean)armyList.get(i);
	CastleBean castleBean = CastleUtil.getCastleById(army.getCid());
	SoldierResBean[] so2 = ResNeed.getSoldierTs(castleBean.getRace());
	int people = army.getGrainCost(so2);
	values[11]+=people;
%><tr><td><%=i+1%></td>
<td><%if(army.getCid()==id){%><%if(hiddenArmy!=army){%>自己的<a href="marmy.jsp?id=<%=army.getId()%>">军队</a><%}else{%>隐藏的军队<%}%></a><%}else{%>来自<a href="castle.jsp?id=<%=army.getCid()%>"><%=castleBean.getCastleNameWml()%></a>的<a href="marmy.jsp?id=<%=army.getId()%>">军队</a><%}%></td>
<td><%=army.getSoldierString(castleBean.getRace())%></td>
<td align=right><%=people%></td></tr><%}%>
<%
	for(int i = 0; i < armyList4.size(); i++) {
		CastleArmyBean army = (CastleArmyBean)armyList4.get(i);
		int race = CastleUtil.getCastleById(army.getCid()).getRace();
		int people = army.getGrainCost(ResNeed.getSoldierTs(race));
		values[11]+=people;
%><tr><td><%=i+1%></td><td><a href="castle.jsp?x=<%=CastleUtil.pos2X(army.getAt())%>&y=<%=CastleUtil.pos2Y(army.getAt())%>">绿洲的军队<a></td><td><%=army.getSoldierString(race)%></td><td align=right><%=people%></td></tr><%
	}
%>
</table><br/>
<table class="tbg" cellpadding="2" cellspacing="1"><tr><td></td><td colspan=2>在路上的军队</td><td>消耗</td></tr>
<%
for(int i=0;i<attackList.size();i++){
	AttackThreadBean attack = (AttackThreadBean)attackList.get(i);
	if(attack.getCid()!=id) continue;
	CastleBean castleBean = CastleUtil.getCastleById(attack.getToCid());
	List counts = attack.getSoldierCountList();
	int people = calcGrainCost(counts,so,attack.getHero());
	values[11]+=people;
%><tr><td><%=i+1%></td>
<td><%if(attack.getToCid()==id){%>(返回)<%}%><%if(attack.getType()!=4){
%>对<a href="castle.jsp?id=<%=castleBean.getId()%>"><%=castleBean.getCastleNameWml()%></a>的<a href="marmy2.jsp?id=<%=attack.getId()%>"><%=attack.getTypeName()%></a><%
}else{%>(<%=attack.getX()%>|<%=attack.getY()%>)建立城堡<%}%><br/><%=DateUtil.formatTimeInterval2(attack.getEndTime())%></td>
<td><%=CastleUtil.getSoldierString(castle.getRace(),counts,attack.getHero())%></td>
<td align=right><%=people%></td></tr><%}%>
</table>
<a href="mattack.jsp?cid=<%=castle.getId()%>">群发攻击</a><br/>
<%
// 正在训练的士兵消耗粮食
List soList = getCacheSoldierByCid(id);
for(int i = 0; i < soList.size(); i++) {
	SoldierThreadBean sol = (SoldierThreadBean)soList.get(i);
	if(sol.getSoldierType()<=10)	// 大于10的是陷进之类的
	values[11] += so[sol.getSoldierType()].getPeople()*sol.getCount();
}

List mList = cacheService.getCacheMerchantByFromCid(id);
for(int i = 0; i < mList.size(); i++) {
	MerchantBean merchant = (MerchantBean)mList.get(i);
	if(merchant.getType() == 1) {
		
	}
	values[15]+=merchant.getCount();
}
List tlist = service.getTradeList("cid="+id);
for(int i=0;i<tlist.size();i++){
	TradeBean trade = (TradeBean)tlist.get(i);
	values[15]+=trade.getSupplyMerchant();
}
%>

</td><td width="300">

<%
	values[0] += woodbase;
	values[1] += stonebase;		
	values[2] += febase;
	values[3] += grainbase;
	if(values[8]==0)	values[8]=800;
	if(values[9]==0)	values[9]=800;
%>

<table class="tbg" cellpadding="2" cellspacing="1">
<tr><td></td><td>当前值</td><td>理论值</td><td></td></tr>
<%for(int i=0;i<valueNames.length;i++){
%><tr><td><%=valueNames[i]%></td><td><%if(values[i]!=values2[i]){%><font color=red><%=values2[i]%></font><%}else{%><%=values2[i]%><%}%></td><td><%=values[i]%></td>
<td><a href="castle2.jsp?id=<%=id%>&i=<%=i%>&value=<%=values[i]%>" onclick="return confirm('<%=valueNames[i]%>:<%=values2[i]%>-><%=values[i]%>.确认修正数据?')">修正数据</a></td></tr>
<%}%>
<tr><td colspan=2></td>
<td colspan=2>
<a href="#" onclick="if(true){document.modall.submit();return false;}else return false;">修正全部数据</a>
</td></tr>
</table>
<form name=modall action="castle2.jsp?id=<%=id%>&i=99" method=post>
<%for(int i=0;i<valueNames.length;i++){
if(values[i]==values2[i]) continue;
%><input type=hidden name="value<%=i%>" value="<%=values[i]%>"/>
<%}%>
</form>
<table class="tbg" cellpadding="2" cellspacing="1">
<tr><td></td>
<td class="rbg">兵种</td><td class="rbg">攻击等级</td><td class="rbg">防御等级</td>
</tr>
<%
SoldierSmithyBean[] fromSmithy = CastleBaseAction.getSmithys(id);
SoldierResBean[] so3 = ResNeed.getSoldierTs(castle.getRace());
for(int i=1;i<=10;i++){
SoldierSmithyBean smithy = fromSmithy[i];
%><tr><td><%=i%>.</td><td align=left><%=so3[i].getSoldierName()%></td><td><%if(smithy==null){%>-<%}else{%>等级<%=smithy.getAttack()%><%}%></td><td><%if(smithy==null){%>-<%}else{%>等级<%=smithy.getDefence()%><%}%></td></tr><%

}
%>
</table>
</td></tr></table>
<br/>
<a href="castle.jsp?id=<%=id%>">返回城堡</a><br/>
<br/>

<%@include file="bottom.jsp"%>
</html>