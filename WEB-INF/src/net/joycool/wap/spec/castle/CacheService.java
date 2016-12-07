package net.joycool.wap.spec.castle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class CacheService {
	
	private static CacheService cacheService;
	
	private CacheService(){}
	
	public static CacheService getInstance(){
		
		if(cacheService == null) {
			synchronized(CacheService.class) {
				if(cacheService == null) 
					cacheService = new CacheService();
			}
		}
		return cacheService;
	}

//********************************士兵缓存相关操作*****************************
	public static SoldierResBean[][] getAllSoldierInfo(){
		
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from castle_soldier_res";
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				SoldierResBean sBean = new SoldierResBean();
				sBean.setId(rs.getInt("type"));
				int type = sBean.getId() % 10;
				if(type == 0)
					sBean.setType(10);
				else
					sBean.setType(type);
				sBean.setSoldierName(rs.getString("soldier_name"));
				sBean.setShortName(rs.getString("short_name"));
				sBean.setInfo(rs.getString("info"));
				sBean.setWood(rs.getInt("wood"));
				sBean.setFe(rs.getInt("fe"));
				sBean.setGrain(rs.getInt("grain"));
				sBean.setTime(rs.getInt("build_time"));
				sBean.setAttack(rs.getInt("attack"));
				sBean.setDefense(rs.getInt("defense"));
				sBean.setSpeed(rs.getFloat("speed"));
				sBean.setAttack2(rs.getInt("attack2"));
				sBean.setDefense2(rs.getInt("defense2"));
				sBean.setAttack3(rs.getInt("attack3"));
				sBean.setDefense3(rs.getInt("defense3"));
				sBean.setAttack4(rs.getInt("attack4"));
				sBean.setDefense4(rs.getInt("defense4"));
				sBean.setStore(rs.getInt("store"));
				sBean.setStone(rs.getInt("stone"));
				sBean.setPeople(rs.getInt("people"));
				// 研发兵种的数据
				sBean.setWood2(rs.getInt("wood2"));
				sBean.setFe2(rs.getInt("fe2"));
				sBean.setGrain2(rs.getInt("grain2"));
				sBean.setStone2(rs.getInt("stone2"));
				sBean.setTime2(rs.getInt("time2"));
				sBean.setWood3(rs.getInt("wood3"));
				sBean.setFe3(rs.getInt("fe3"));
				sBean.setGrain3(rs.getInt("grain3"));
				sBean.setStone3(rs.getInt("stone3"));
				sBean.setPre(rs.getString("pre"));
				sBean.setPreList(StringUtil.toIntss(sBean.getPre()));
				sBean.setBuildType(rs.getInt("build_type"));
				sBean.setFlag(rs.getInt("flag"));
				
				sBean.setRace(rs.getInt("race"));
				list.add(sBean);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		SoldierResBean[][] res = new SoldierResBean[ResNeed.raceNames.length][];

		for(int i = 1;i < ResNeed.raceNames.length;i++)
			res[i] = new SoldierResBean[ResNeed.soldierTypeCount + 1];

		Iterator it = list.iterator();
		
		while(it.hasNext()) {
			SoldierResBean bean = (SoldierResBean)it.next();
			res[bean.getRace()][bean.getType()] = bean;
		}
		
		return res;
	}
	
	
	
//********************************建筑缓存相关操作*****************************
	public static BuildingTBean[] getBuildingTemplate(){
		DbOperation db = new DbOperation(5);
		
		BuildingTBean[] tBean = new BuildingTBean[ResNeed.buildingTypeCount];
		
		String query = "SELECT * FROM building_template";
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				BuildingTBean bean = new BuildingTBean();
				bean.setBuildType(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setInfo(rs.getString("info"));
				bean.setMaxGrade(rs.getInt("max_grade"));
				bean.setPre(rs.getString("pre"));
				bean.setFlag(rs.getInt("flag"));
				bean.setRace(rs.getInt("race"));
				if(bean.getPre() == null || bean.getPre().length() == 0)
					bean.setPreList(new ArrayList());
				else
					bean.setPreList(StringUtil.toIntss(bean.getPre()));
				tBean[bean.getBuildType()] = bean;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return tBean;
		}finally{
			db.release();
		}
		return tBean;
	}
	
	public boolean updateCityName(int cid, String name) {
		CastleService castleService = CastleService.getInstance();
		CastleBean bean = CastleUtil.getCastleById(cid);
		bean.setCastleName(name);
		if(!castleService.updateCityName(cid, name)){
			return false;
		}
		
		return true;
	}


//********************************建筑相关操作*********************************
	public boolean isCacheContain(int cid, int pos) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT id FROM cache_building where cid = " + cid +" and build_pos = " + StringUtil.trimAll(""+pos);
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return true;
		}finally{
			db.release();
		}
		return false;
	}
	
	public boolean isBuildingContain(int cid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "SELECT type FROM cache_building where cid = " + cid +" and type = " + StringUtil.trimAll(""+type);
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				int buildType = rs.getInt("type");
				for(int i = 0; i < ResNeed.advanceBuild.length; i++) {
					if(buildType == ResNeed.advanceBuild[i])
						return true;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return true;
		}finally{
			db.release();
		}
		return false;
	}
		
	public BuildingThreadBean getCacheBuildBean(int id) {
		DbOperation db = new DbOperation(5);
		BuildingThreadBean bean = new BuildingThreadBean();
		String query = "SELECT * FROM cache_building where id = " + id;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				bean = getBuildingThreadBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		return bean;
	}
	
	public HashMap getCacheBuildingType(int cid) {
		DbOperation db = new DbOperation(5);
		HashMap state = new HashMap();
		String query = "SELECT build_pos, grade FROM cache_building where cid =" + cid;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				if(rs.getInt("grade") > 1) {
					state.put(new Integer(rs.getInt("build_pos")), "升级中...");
				} else {
					state.put(new Integer(rs.getInt("build_pos")), "建造中...");
				}
				
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return state;
		}finally{
			db.release();
		}
		
		return state;
	}
	
	public List getCacheBuildingByCid(int cid) {
		DbOperation db = new DbOperation(5);
		
		List list = new ArrayList();
		
		String query = "SELECT * FROM cache_building where cid =" + cid + " order by end_time";
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				BuildingThreadBean bean = getBuildingThreadBean(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public List getCacheCommonList(int cid, int type) {
		DbOperation db = new DbOperation(5);
		
		List list = new ArrayList();
		
		String query = "SELECT * FROM cache_common where cid =" + cid + " and type=" + type;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getCommonThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	public List getCacheCommonList2(int uid, int type) {
		DbOperation db = new DbOperation(5);
		
		List list = new ArrayList();
		
		String query = "SELECT * FROM cache_common where uid =" + uid + " and type=" + type;
		try {
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getCommonThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	public CommonThreadBean getCacheCommon2(int uid, int type, int value) {
		DbOperation db = new DbOperation(5);
		CommonThreadBean bean = null;
		String query = "SELECT * FROM cache_common where uid =" + uid + " and type=" + type + " and value = " + value;
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				bean = getCommonThreadBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		return bean;
	}
	public CommonThreadBean getCacheCommon(int cid, int type, int value) {
		DbOperation db = new DbOperation(5);
		CommonThreadBean bean = null;
		String query = "SELECT * FROM cache_common where cid =" + cid + " and type=" + type + " and value = " + value;
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				bean = getCommonThreadBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return bean;
		}finally{
			db.release();
		}
		return bean;
	}
	
	//TODO
	public static List getAllCacheBuilding() {
		
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM cache_building where end_time <= " + System.currentTimeMillis() + " order by end_time";
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				BuildingThreadBean bean = getBuildingThreadBean(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private static BuildingThreadBean getBuildingThreadBean(ResultSet rs) throws SQLException{
		BuildingThreadBean threadBean = new BuildingThreadBean();
		
		//BuildingBean buildingBean = new BuildingBean();
		threadBean.setId(rs.getInt("id"));
		threadBean.setCid(rs.getInt("cid"));
		threadBean.setType(rs.getInt("type"));
		threadBean.setGrade(rs.getInt("grade"));
		threadBean.endTime = rs.getLong("end_time");
		threadBean.startTime = rs.getLong("start_time");
		threadBean.people = rs.getInt("people");
		threadBean.buildPos = rs.getInt("build_pos");
		
		return threadBean;
	}
	
	public static List getAllCacheCommon() {
		
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM cache_common where end_time <= " + System.currentTimeMillis() + " order by end_time";
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getCommonThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private static CommonThreadBean getCommonThreadBean(ResultSet rs) throws SQLException{
		CommonThreadBean threadBean = new CommonThreadBean();
		
		threadBean.setId(rs.getInt("id"));
		threadBean.setUid(rs.getInt("uid"));
		threadBean.setCid(rs.getInt("cid"));
		threadBean.setType(rs.getInt("type"));
		threadBean.setValue(rs.getInt("value"));
		threadBean.endTime = rs.getLong("end_time");
		threadBean.startTime = rs.getLong("start_time");
		
		return threadBean;
	}
	
	public boolean addCacheBuilding(BuildingThreadBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO cache_building(cid, type, grade, start_time, end_time, people, build_pos) values(?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getCid());
			pstmt.setInt(2, bean.getType());
			pstmt.setInt(3, bean.getGrade());
			pstmt.setLong(4, bean.startTime);
			pstmt.setLong(5, bean.endTime);
			pstmt.setInt(6, bean.getPeople());
			pstmt.setInt(7, bean.getBuildPos());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	public boolean addCacheCommon(CommonThreadBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO cache_common(uid, type, value, start_time, end_time,cid) values(?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getUid());
			pstmt.setInt(2, bean.getType());
			pstmt.setInt(3, bean.getValue());
			pstmt.setLong(4, bean.startTime);
			pstmt.setLong(5, bean.endTime);
			pstmt.setInt(6, bean.getCid());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	
	public boolean deleteCacheBuilding(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_building where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean deleteCacheCommon(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_common where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateCacheCommon(int id, long interval) {
		DbOperation db = new DbOperation(5);
		String query = "update cache_common set end_time = end_time + " + interval + " where id = " + id;
		if(db.executeUpdate(query)) {
			db.release();
			return true;
		}
		db.release();
		return false;		
	}
	
	
	public boolean deleteCacheBuilding(int cid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_building where cid = ? and type = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, cid);
			pstmt.setInt(2, type);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	public int getBuildingCount(int cid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "SELECT count(id) as count FROM cache_building WHERE cid = " + cid;
		try {
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
	
	
	
//*****************************士兵建造相关操作******************************************
	public static List getAllCacheSoldier() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_soldier WHERE end_time <= " + System.currentTimeMillis();
		
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				list.add(getSoldierThreadBean(rs));
			}
		
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	public boolean deleteCacheSoldier(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_soldier where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	public boolean updateCacheSoldier(int id) {
		DbOperation db = new DbOperation(5);
		//TODO interval_time => 5
		String query = "update cache_soldier set count = count - 1 , end_time = end_time + interval_time * 1000 where id = ?";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, id);
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
		
	}
	
	public boolean addCacheSoldier(SoldierThreadBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "INSERT INTO cache_soldier(cid, soldier_type, count, start_time, end_time, interval_time,type) values(?,?,?,?,?,?,?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{			
			pstmt.setInt(1, bean.getCid());
			pstmt.setInt(2, bean.getSoldierType());
			pstmt.setInt(3, bean.getCount());
			pstmt.setLong(4, bean.getStartTime());
			pstmt.setLong(5, bean.getEndTime());
			pstmt.setInt(6, bean.getInterval());
			pstmt.setInt(7, bean.getType());
			pstmt.execute();
			
			bean.setId(db.getLastInsertId());
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		
		return true;
	}
	// 每个兵营不同，1是兵营，2是马厩，3是工厂，4、5是大兵营大马厩
	public List getCacheSoldierByCid(int cid, int type) {
		DbOperation db = new DbOperation(5);
		List list = new ArrayList();
		String query = "select * from cache_soldier where cid = " + cid + " and type=" + type + " order by id";
		
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

	private static SoldierThreadBean getSoldierThreadBean(ResultSet rs) throws SQLException{
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

//*******************攻防升级**************************************
	
	public static List getAllCacheSoldierSmithy() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_soldier_smithy WHERE end_time <= " + System.currentTimeMillis();
		
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				list.add(getCacheSoldierSmithy(rs));
			}		
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		return list;
	}
	
	private static SmithyThreadBean getCacheSoldierSmithy(ResultSet rs) throws SQLException{
		SmithyThreadBean bean = new SmithyThreadBean();
		bean.setId(rs.getInt("id"));
		bean.setSmithyType(rs.getInt("smithy_type"));
		bean.setEndTime(rs.getLong("end_time"));
		bean.setSoldierType(rs.getInt("soldier_type"));
		bean.setCid(rs.getInt("cid"));
		bean.setStartTime(rs.getLong("start_time"));
		return bean;
	}
	
	public boolean addCacheSmithy(SmithyThreadBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into cache_soldier_smithy(cid, soldier_type, smithy_type, start_time, end_time) values(?,?,?,?,?)";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement ps = db.getPStmt();
		
		try{
			
			ps.setInt(1, bean.getCid());
			ps.setInt(2, bean.getSoldierType());
			ps.setInt(3, bean.getSmithyType());
			ps.setLong(4, bean.getStartTime());
			ps.setLong(5, bean.getEndTime());
			ps.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	/**
	 * uid的用户所有的正在升级的攻防
	 * @param cid
	 * @return
	 */
	public List getSoldierSmithyByCid(int cid) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_soldier_smithy where cid = " + cid;
		List list = new ArrayList();
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getCacheSoldierSmithy(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	public List getSoldierSmithyByCid(int cid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_soldier_smithy where cid = " + cid + " and smithy_type=" + type;
		List list = new ArrayList();
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getCacheSoldierSmithy(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	// 判断某种队列是否有
	public boolean containCommon(int cid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "select id from cache_common where cid = " + cid + " and type=" + type;
		try{
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return false;
	}
	// 由uid进行判断
	public boolean containCommon2(int uid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "select id from cache_common where uid = " + uid + " and type=" + type;
		try{
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return false;
	}
	
	/**
	 * cid的用户是否已经在升级攻防
	 * @param cid
	 * @return
	 */
	public boolean containSmithy(int cid, int type) {
		DbOperation db = new DbOperation(5);
		String query = "select id from cache_soldier_smithy where cid = " + cid + " and smithy_type=" + type;
		try{
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return false;
	}
	
	
	public boolean deleteSmithy(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_soldier_smithy where id = ?";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement ps = db.getPStmt();
		
		try{
			ps.setInt(1, id);
			ps.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
//*******************攻城相关操作***********************************
	
	public static List getAllCacheAttack(){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_attack WHERE end_time <= " + System.currentTimeMillis() + " order by end_time";
		
		try{
			ResultSet rs = db.executeQuery(query);
			
			while(rs.next()) {
				list.add(getAttackThreadBean(rs));
			}		
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		return list;
	}
	
	/**
	 * toCid被攻击的攻击
	 * @param toCid
	 * @return
	 */
	public List getCacheAttackByToCid(int toCid) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_attack where to_cid = " + toCid + " and type not in (2,3,9) and (type!=10 or cid!=to_cid) order by end_time";
		List list = new ArrayList();
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getAttackThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	public AttackThreadBean getCacheAttack(int id) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_attack where id = " + id;
		AttackThreadBean bean = null;
		try{
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				bean = getAttackThreadBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	public CommonThreadBean getCacheCommon(int id) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_common where id = " + id;
		CommonThreadBean bean = null;
		try{
			ResultSet rs = db.executeQuery(query);
			if(rs.next()) {
				bean = getCommonThreadBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		
		return bean;
	}
	
	/**
	 * fromCid的用户发起的进攻
	 * @param fromCid
	 * @return
	 */
	public List getCacheAttackByFromCid(int fromCid) {
		DbOperation db = new DbOperation(5);
		// 发出的军队，但是要排除对自己的绿洲的返回支援
		String query = "select * from cache_attack where from_cid = " + fromCid + " and from_cid=cid and (type!=5 or cid!=to_cid) order by end_time";
		List list = new ArrayList();
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getAttackThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	public List getCacheAttackList(String cond) {
		DbOperation db = new DbOperation(5);
		String query = "select * from cache_attack where " + cond;
		List list = new ArrayList();
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				list.add(getAttackThreadBean(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		
		return list;
	}
	
	/**
	 * 增加一次进攻线程
	 * @param bean
	 * @return
	 */
	public boolean addCacheAttack(AttackThreadBean bean) {
		DbOperation db = new DbOperation(5);
		
		String query = "insert into cache_attack(from_cid, to_cid, start_time, end_time, soldier_count,type,cid,x,y,opt,hero) values(?,?,?,?,?,?,?,?,?,?,?)";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		
		PreparedStatement ps = db.getPStmt();
		
		try{
			
			ps.setInt(1, bean.getFromCid());
			ps.setInt(2, bean.getToCid());
			ps.setLong(3, bean.getStartTime());
			ps.setLong(4, bean.getEndTime());
			ps.setString(5, bean.getSoldierCount());
			ps.setInt(6, bean.getType());
			ps.setInt(7, bean.getCid());
			ps.setInt(8, bean.getX());
			ps.setInt(9, bean.getY());
			ps.setInt(10, bean.getOpt());
			ps.setInt(11, bean.getHero());
			ps.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	/**
	 * 删除一次进攻
	 * @param id
	 * @return
	 */
	public boolean deleteAttackThreadBean(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_attack where id = ?";
		
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement ps = db.getPStmt();
		
		try{
			ps.setInt(1, id);
			ps.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
		
	}
	
	public boolean cancelAttackThreadBean(AttackThreadBean bean) {
		StringBuilder sb = new StringBuilder(64);
		DbOperation db = new DbOperation(5);
		sb.append("update cache_attack set end_time=");
		sb.append(bean.getEndTime());
		sb.append(",type=5,from_cid=");
		sb.append(bean.getToCid());
		if(bean.getWood() != 0 || bean.getStone() != 0 || bean.getFe() != 0 || bean.getGrain() != 0) {
			sb.append(",wood=");
			sb.append(bean.getWood());
			sb.append(',');
			sb.append("stone=");
			sb.append(bean.getStone());
			sb.append(',');
			sb.append("fe=");
			sb.append(bean.getFe());
			sb.append(',');
			sb.append("grain=");
			sb.append(bean.getGrain());
		}
		sb.append(",to_cid=");
		sb.append(bean.getFromCid());
		sb.append(",soldier_count='");
		sb.append(bean.getSoldierCount());
		sb.append("',hero=");
		sb.append(bean.getHero());
		sb.append(" where id =");
		sb.append(bean.getId());
		boolean flag = db.executeUpdate(sb.toString());
		db.release();
		return flag;
		
	}
	
	private static AttackThreadBean getAttackThreadBean(ResultSet rs) throws SQLException{
		AttackThreadBean bean = new AttackThreadBean();
		
		bean.setId(rs.getInt("id"));
		bean.setCid(rs.getInt("cid"));
		bean.setFromCid(rs.getInt("from_cid"));
		bean.setToCid(rs.getInt("to_cid"));
		bean.setStartTime(rs.getLong("start_time"));
		bean.setEndTime(rs.getLong("end_time"));
		bean.setSoldierCount(rs.getString("soldier_count"));
		bean.setType(rs.getInt("type"));
		bean.setX(rs.getInt("x"));
		bean.setY(rs.getInt("y"));
		bean.setWood(rs.getInt("wood"));
		bean.setStone(rs.getInt("stone"));
		bean.setFe(rs.getInt("fe"));
		bean.setGrain(rs.getInt("grain"));
		bean.setOpt(rs.getInt("opt"));
		bean.setHero(rs.getInt("hero"));
		return bean;
	}
	
	
	public static byte[] getUserBuildings(int cid) {
		byte[] bs = new byte[ResNeed.buildingTypeCount];
		DbOperation db = new DbOperation(5);
		String query = "select build_type,grade from castle_building WHERE cid=" + cid;
		
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				int type = rs.getInt(1);
				int grade = rs.getInt(2);
				if(bs[type] < grade)
					bs[type] = (byte)grade;
			}		
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}finally{
			db.release();
		}
		return bs;
	}
	
	//=================商人市场============================
	public boolean addCacheMerchant(MerchantBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into cache_merchant(from_cid, to_cid, start_time, end_time, count, wood, fe, grain, stone, type) values(?,?,?,?,?,?,?,?,?,0)";
		if(!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement ps = db.getPStmt();
		try{
			
			ps.setInt(1, bean.getFromCid());
			ps.setInt(2, bean.getToCid());
			ps.setLong(3, bean.getStartTime());
			ps.setLong(4, bean.getEndTime());
			ps.setInt(5, bean.getCount());
			ps.setInt(6, bean.getWood());
			ps.setInt(7, bean.getFe());
			ps.setInt(8, bean.getGrain());
			ps.setInt(9, bean.getStone());
			ps.execute();
			bean.setId(db.getLastInsertId());
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return true;
	}
	
	
	public boolean updateMerchant(MerchantBean merchantBean) {
		DbOperation db = new DbOperation(5);
		String query = "update cache_merchant set type=" + merchantBean.getType() + ",start_time=" + merchantBean.getStartTime() + ",end_time=" + merchantBean.getEndTime() + " where id =" + merchantBean.getId();
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	public boolean deleteMerchant(int id) {
		DbOperation db = new DbOperation(5);
		String query = "delete from cache_merchant where id =" + id;
		boolean flag = db.executeUpdate(query);
		db.release();
		return flag;
	}
	
	public static List getAllCacheMerchant() {
		
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM cache_merchant where end_time <= " + System.currentTimeMillis() + " order by end_time";
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				MerchantBean bean = getMerchantBean(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	
	//查询派出的商人
	public List getCacheMerchantByFromCid(int fromCid) {
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM cache_merchant where from_cid = " + fromCid;
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				MerchantBean bean = getMerchantBean(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	//查询派向自己的商人
	public List getCacheMerchantByToCid(int toCid) {
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		String query = "SELECT * FROM cache_merchant where to_cid = " + toCid + " order by end_time";
		try{
			ResultSet rs = db.executeQuery(query);
			while(rs.next()) {
				MerchantBean bean = getMerchantBean(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return list;
		}finally{
			db.release();
		}
		return list;
	}
	
	private static MerchantBean getMerchantBean(ResultSet rs) throws SQLException{
		MerchantBean bean = new MerchantBean();
		bean.setId(rs.getInt("id"));
		bean.setCount(rs.getInt("count"));
		bean.setEndTime(rs.getLong("end_time"));
		bean.setFe(rs.getInt("fe"));
		bean.setFromCid(rs.getInt("from_cid"));
		bean.setGrain(rs.getInt("grain"));
		bean.setStartTime(rs.getLong("start_time"));
		bean.setStone(rs.getInt("stone"));
		bean.setToCid(rs.getInt("to_cid"));
		bean.setWood(rs.getInt("wood"));
		bean.setType(rs.getInt("type"));
		return bean;
	}
	
}
