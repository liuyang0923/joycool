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
	CastleUserBean user = CastleUtil.getCastleUser(id);

	// 修正玩家数据
	List listc = service.getCastleList(id);
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
			repairAll(id);
		}
		response.sendRedirect("index.jsp");
		return;
	}
%>