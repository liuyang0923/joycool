<%@ page contentType="text/html;charset=utf-8"%><%@ page import="net.joycool.wap.framework.*"%><%@ page import="java.sql.*"%><%@ page import="net.joycool.wap.util.db.DbOperation"%><%@include file="top.jsp"%><%!
static CastleService service = CastleService.getInstance();
static CacheService cacheService = CacheService.getInstance();
static String[] valueNames = {"木材","石头","铁块","粮食","木材%","石头%","铁块%","粮食%","仓库","粮仓","人口","士兵","山洞","文明度","城墙%","商人"};
static int calcGrainCost(List counts, SoldierResBean[] so){
	int sum = 0;
	for(int i = 1;i<so.length; i++) {
		int count = ((Integer)counts.get(i-1)).intValue();
		if(count > 0)
			sum += so[i].getPeople() * count;
	}
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
// 修正城堡和所属玩家的数据
public static void repairAll(int id) {
	CastleBean castle = null;

	castle = CastleUtil.getCastleById(id);
	if(castle==null)
		return;
	UserResBean castleRes = CastleUtil.getUserResBeanById(id);
	CastleUserBean user = CastleUtil.getCastleUser(castle.getUid());

	List buildings = service.getAllBuilding(castle.getId());
	BuildingBean[] buildPos = new BuildingBean[41];
	for(int i = 0;i < buildings.size();i++){
		BuildingBean b = (BuildingBean)buildings.get(i);
		buildPos[b.getBuildPos()] = b;
	}
	int[] baseBuild = {1,3,9,2,1,3,9,2,1,3,9,2,1,3,9,2,1,1,1};
	

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
	
	int woodbase = 8;
	int stonebase=8;
	int febase=8;
	int grainbase=12;
	
	for(int i = 1; i <= 40; i ++) {
	  if(buildPos[i] !=null){
	  	if(buildPos[i].getGrade()>0){
			BuildingTBean bt = ResNeed.getBuildingT(buildPos[i].getBuildType(),buildPos[i].getGrade());
			switch(bt.getBuildType()){
			case ResNeed.CITY_BUILD:
				break;
			case ResNeed.WOOD_BUILD:
				values[0]+=bt.getValue();
				woodbase-=2;
				break;
			case ResNeed.FE_BUILD:
				values[2]+=bt.getValue();
				febase-=2;
				break;
			case ResNeed.GRAIN_BUILD:
				values[3]+=bt.getValue();
				grainbase-=2;
				break;
			case ResNeed.STONE_BUILD:
				values[1]+=bt.getValue();
				stonebase-=2;
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
				values[8]+=bt.getValue();
				break;
			case ResNeed.BARN_BUILD:
				values[9]+=bt.getValue();
				break;
			case ResNeed.CAVE_BUILD:
				values[12]+=bt.getValue();
				break;
			case ResNeed.WALL_BUILD:
				values[14]+=bt.getValue();
				break;
			}
			values[10]+=bt.getTotalPeople();
			values[13]+=bt.getCivil();
		}
	  }
	}
	
	List armyList = service.getCastleArmyAtList(id);
	List attackList = cacheService.getCacheAttackByToCid(id);
	CastleArmyBean hiddenArmy = CastleUtil.getCastleHiddenArmy(id);
	attackList.addAll(cacheService.getCacheAttackByFromCid(id));
	if(hiddenArmy!=null)
		armyList.add(hiddenArmy);
	
	SoldierResBean[] so = ResNeed.getSoldierTs(castle.getRace());
	for(int i=0;i<armyList.size();i++){
		CastleArmyBean army = (CastleArmyBean)armyList.get(i);
		CastleBean castleBean = CastleUtil.getCastleById(army.getCid());
		int people = army.getGrainCost(so);
		values[11]+=people;
	}
	for(int i=0;i<attackList.size();i++){
		AttackThreadBean attack = (AttackThreadBean)attackList.get(i);
		if(attack.getCid()!=id) continue;
		CastleBean castleBean = CastleUtil.getCastleById(attack.getToCid());
		List counts = attack.getSoldierCountList();
		int people = calcGrainCost(counts,so);
		values[11]+=people;
	}



	// 正在训练的士兵消耗粮食
	List soList = getCacheSoldierByCid(id);
	for(int i = 0; i < soList.size(); i++) {
		SoldierThreadBean sol = (SoldierThreadBean)soList.get(i);
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

	values[0] += woodbase;
	values[1] += stonebase;		
	values[2] += febase;
	values[3] += grainbase;
	if(values[8]==0)	values[8]=800;
	if(values[9]==0)	values[9]=800;


	boolean update = false;
	for(int i=0;i<valueNames.length;i++){
		if(values[i]!=values2[i]) {
			setResValue(castleRes,i, values[i]);
			if(i!=15)
				update=true;
		}
	}
	if(update)
		service.updateUserResAll(castleRes);
	else
		return;
		
	// 修正玩家数据
	List listc = service.getCastleList(castle.getUid());
	int[] uvalues = new int[3];;
	int[] uvalues2 = {
		user.getPeople(),
		user.getCivilSpeed(),
		user.getCastleCount(),
	};
	
	for(int i = 0; i < listc.size() ; i++) {
		CastleBean castleBean = (CastleBean)listc.get(i);
		UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
		
		uvalues[0] += userRes.getPeople();
		uvalues[1] += userRes.getCivil();
		uvalues[2] ++;
	}
	user.setPeople(uvalues[0]);
	user.setCivilSpeed(uvalues[1]);
	service.updateUserCivil(user);
}
// 修正玩家的数据，只修改总人口和文明度
public static void repairTotal(int id) {
			
	// 修正玩家数据
	List listc = service.getCastleList(castle.getUid());
	int[] uvalues = new int[3];;
	int[] uvalues2 = {
		user.getPeople(),
		user.getCivilSpeed(),
		user.getCastleCount(),
	};
	
	for(int i = 0; i < listc.size() ; i++) {
		CastleBean castleBean = (CastleBean)listc.get(i);
		UserResBean userRes = (UserResBean)CastleUtil.getUserResBeanById(castleBean.getId());
		
		uvalues[0] += userRes.getPeople();
		uvalues[1] += userRes.getCivil();
		uvalues[2] ++;
	}
	user.setPeople(uvalues[0]);
	user.setCivilSpeed(uvalues[1]);
	service.updateUserCivil(user);
}
%><%
	CustomAction action = new CustomAction(request);
	if(true){
		List idList = StringUtil.toInts(request.getParameter("ids"));
		for(int i = 0;i < idList.size();i++){
			int id = ((Integer)idList.get(i)).intValue();
			if(id<=0) continue;
			repairTotal(id);
		}
		response.sendRedirect("index.jsp");
		return;
	}
%>