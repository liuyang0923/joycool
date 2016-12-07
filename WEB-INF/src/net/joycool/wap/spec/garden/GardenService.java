package net.joycool.wap.spec.garden;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;

public class GardenService {

	private static GardenService gardenService;
	private GardenService(){}
	public static GardenService getInstance(){
		if(gardenService == null) {
			synchronized(GardenService.class) {
				if(gardenService == null) 
					gardenService = new GardenService();
			}
		}
		return gardenService;
	}
	
	public boolean updateGardenUser(String set, String condition) {
		DbOperation db = new DbOperation(5);
		String query = "update garden_user set " + set + " where " + condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean updateUserGold(int uid, int gold) {
		DbOperation db = new DbOperation(5);
		String query = "update garden_user set gold = " + gold + " where uid = " + uid;
		db.executeUpdate(query);
		db.release();
//		gardenUserCache.srm(uid);
		return true;
	}
	public static ICacheMap gardenUserCache = CacheManage.gardenUserCache;
	public boolean updateUserGoldAdd(int uid, int gold) {
		DbOperation db = new DbOperation(5);
		String query = "update garden_user set gold = gold + " + gold + " where uid = " + uid;
		db.executeUpdate(query);
		
		db.release();
//		gardenUserCache.srm(uid);
		return true;
	}
	
	public GardenUserBean addGardenUser(int uid) {
		GardenUserBean bean = new GardenUserBean(uid);
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_user set uid = " + uid +",grass_count = " + GardenAction.GRASS_COUNT + ", bug_count = " + GardenAction.BUG_COUNT + ", gold="+GardenUtil.defaultGold+",enter_time=now(),create_time=now(),field_count="+GardenUtil.defaultCount;
		db.executeUpdate(query);
		bean.setGold(GardenUtil.defaultGold);
		bean.setEnterTime(new Date());
		bean.setCreateTime(new Date());
		bean.setFieldCount(GardenUtil.defaultCount);
		bean.setGrassCount(GardenAction.GRASS_COUNT);
		bean.setBugCount(GardenAction.BUG_COUNT);
		bean.setTodayExp(GardenAction.ONE_DAY_EXP);
		db.release();
		return bean;
	}
	
	public List getGardenUsers(String condition){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select uid from garden_user where " + condition;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt(1)));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public List getGardenUsers(String sql,int connId){
		List list = new ArrayList();
		DbOperation db = new DbOperation(connId);
		
		ResultSet rs = db.executeQuery(sql);
		
		try{
			while(rs.next()) {
				list.add(new Integer(rs.getInt(1)));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public GardenUserBean getGardenUser(int uid) {
		GardenUserBean bean = null;
		DbOperation db = new DbOperation(5);
		
		String query = "select * from garden_user where uid = " + uid;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				bean = getGardenUserBean(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return bean;
	}
	
	private GardenUserBean getGardenUserBean(ResultSet rs) throws SQLException{
		GardenUserBean bean = new GardenUserBean();
		bean.setBugCount(rs.getInt("bug_count"));
		bean.setExp(rs.getInt("exp"));
		bean.setFieldCount(rs.getInt("field_count"));
		bean.setGold(rs.getInt("gold"));
		bean.setGrassCount(rs.getInt("grass_count"));
		bean.setUid(rs.getInt("uid"));
		bean.setCreateTime(rs.getTimestamp("create_time"));
		bean.setEnterTime(rs.getTimestamp("enter_time"));
		bean.setName(rs.getString("name"));
		return bean;
	}
	
	public boolean addGardenField(int uid) {
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_field set uid = " + uid;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean addGardenUserSeed(int uid, int seedId, int count, int type) {
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_user_seed set uid = " + uid + ", seed_id = " + seedId + ", count = " + count + ", type="+type;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean isContainSeed(int uid, int seedId) {
		DbOperation db = new DbOperation(5);
		String query = "select * from garden_user_seed where uid = " + uid + " and seed_id = " + seedId;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return false;
	}
	
	public boolean updateUserSeedCount(int uid, int seedId, int count,int type){
		if(isContainSeed(uid, seedId)) {
			DbOperation db = new DbOperation(5);
			String query = "update garden_user_seed set count = count + " + count + " where uid = " + uid + " and seed_id = " + seedId;
			db.executeUpdate(query);
			db.release();
			return true;
		} else {
			return addGardenUserSeed(uid, seedId, count,type);
		}
	}
	
	public boolean updateUserSeedCount2(int uid, int seedId, int count,int type){
		if(isContainSeed(uid, seedId)) {
			DbOperation db = new DbOperation(5);
			String query = "update garden_user_seed set count = " + count + " where uid = " + uid + " and seed_id = " + seedId;
			db.executeUpdate(query);
			db.release();
			return true;
		} else {
			return addGardenUserSeed(uid, seedId, count,type);
		}
	}
	
	public List getFields(String condition) {
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		
		String query = "select * from garden_field ";
		
		if(condition != null) {
			query += condition;
		}
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				GardenFieldBean bean = getFieldBean(rs);
				
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}		
		return list;
	}
	
	public List getUserFields(int uid) {
		List list = new ArrayList();
		
		DbOperation db = new DbOperation(5);
		
		String query = "select * from garden_field where uid = " + uid;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				GardenFieldBean bean = getFieldBean(rs);
				
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}		
		return list;
	}
	
	public GardenFieldBean getUserFieldBean(int id) {
		DbOperation db = new DbOperation(5);
		
		String query = "select * from garden_field where id = " + id;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				GardenFieldBean bean = getFieldBean(rs);
				return bean;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return null;
	}
	
	private GardenFieldBean getFieldBean(ResultSet rs) throws SQLException{
		GardenFieldBean bean = new GardenFieldBean();
		bean.setId(rs.getInt("id"));
		bean.setBug(rs.getInt("bug"));
		bean.setGrass(rs.getInt("grass"));
		bean.setQuarter(rs.getInt("quarter"));
		bean.setResultStartTime(rs.getInt("result_start_time"));
		bean.setSeedId(rs.getInt("seed_id"));
		bean.setStealCount(rs.getInt("steal_count"));
		bean.setUid(rs.getInt("uid"));
		bean.setState(rs.getInt("state"));
		return bean;
	}
	
	
	public boolean addSeed(GardenSeedBean bean) {
		String query = "insert into garden_seed set price ="+bean.getPrice()+",quarter="+bean.getQuarter()+",name='"+StringUtil.toSql(bean.getName())
		+"',type="+bean.getType()+",level="+bean.getLevel()+",info='"+StringUtil.toSql(bean.getInfo())+"',count="+bean.getCount()+",grown_time='"
		+bean.getGrowTime()+"',value="+bean.getValue()+",exp="+bean.getExp()+",grown='"+bean.getGrown()+"'";
		if(bean.getId() > 0)
			query += ",id="+bean.getId();
		DbOperation db = new DbOperation(5);
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean deleteSeed(String condition) {
		if(condition == null)
			return false;
		DbOperation db = new DbOperation(5);
		String query =  "delete from garden_seed where "+condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean updateSeed(String set, String where) {
		if(set == null || where == null)
			return false;
		DbOperation db = new DbOperation(5);
		String query =  "update garden_seed set " + set + " where "+where;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	
	public static List seedTypes = new ArrayList();
	public List getSeedTypes() {
		if(seedTypes.size() > 0) 
			return seedTypes;
		synchronized(seedTypes) {
			if(seedTypes.size() == 0) {
				DbOperation db = new DbOperation(5);
				String query = "select * from garden_seed_type order by id asc";
				ResultSet rs = db.executeQuery(query);
				try{
					while(rs.next()) {
						Object[] type = new Object[2];
						type[0] = new Integer(rs.getInt("id"));
						type[1] = new String(rs.getString("name"));
						seedTypes.add(type);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}finally{
					db.release();
				}
			}
		}
		return seedTypes;
	}
	
	public List getSeeds2(String condition){
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		
		String query = "select id from garden_seed where " + condition;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			while(rs.next()) {
				list.add(new Integer("id"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public static HashMap seedIdMap = new HashMap();
	public List getSeeds(int type) {
		List list = null;
		synchronized(seedIdMap) {
			list = (List)seedIdMap.get(new Integer(type));
			
			if(list == null || list.size() == 0) {
				list = new ArrayList();
				DbOperation db = new DbOperation(5);
				String query = "select id from garden_seed where type = " + type + " order by level asc";
				ResultSet rs = db.executeQuery(query);
				try{
					while(rs.next()) {
						list.add(new Integer(rs.getInt("id")));
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}finally{
					db.release();
				}
			}
		}		
		return list;
	}
	
	public static HashMap seedMap = new HashMap();
	public GardenSeedBean getSeedBean(int id) {
		GardenSeedBean bean = null;
		synchronized(seedIdMap) {
			bean = (GardenSeedBean)seedMap.get(new Integer(id));
			if(bean == null) {
				DbOperation db = new DbOperation(5);
				String query = "select * from garden_seed where id = " + id;
				ResultSet rs = db.executeQuery(query);
				try{
					if(rs.next()) {
						bean = getSeedBean(rs);
						
						seedMap.put(new Integer(id), bean);
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}finally{
					db.release();
				}
			}
		}
		return bean;
	}
	
	private GardenSeedBean getSeedBean(ResultSet rs) throws SQLException{
		GardenSeedBean bean = new GardenSeedBean();
		bean.setId(rs.getInt("id"));
		bean.setLevel(rs.getInt("level"));
		bean.setName(rs.getString("name"));
		bean.setPrice(rs.getInt("price"));
		bean.setQuarter(rs.getInt("quarter"));
		bean.setType(rs.getInt("type"));
		bean.setInfo(rs.getString("info"));
		bean.setCount(rs.getInt("count"));
		bean.setGrowTime(rs.getString("grown_time"));
		bean.setGrown(rs.getString("grown"));
		bean.setExp(rs.getInt("exp"));
		bean.setValue(rs.getInt("value"));
		return bean;
	}
	
	
	public GardenUserSeedBean getUserSeed(int id) {
		DbOperation db = new DbOperation(5);
		
		String query = "select * from garden_user_seed where id = " + id;
		
		ResultSet rs = db.executeQuery(query);
		
		try{
			if(rs.next()) {
				GardenUserSeedBean bean = getUserSeed(rs);
				
				return bean;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		
		return null;
	}
	
	
	public List getUserSeeds(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return list;
		
		String query = "select * from garden_user_seed where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			while(rs.next()) {
				GardenUserSeedBean bean = getUserSeed(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	private GardenUserSeedBean getUserSeed(ResultSet rs) throws SQLException{
		GardenUserSeedBean seedBean = new GardenUserSeedBean();
		seedBean.setId(rs.getInt("id"));
		seedBean.setUid(rs.getInt("uid"));
		seedBean.setSeedId(rs.getInt("seed_id"));
		seedBean.setCount(rs.getInt("count"));
		seedBean.setType(rs.getInt("type"));
		return seedBean;
	}
	
	
	public boolean updateField(String condition) {
		if(condition == null)
			return false;
		DbOperation db = new DbOperation(5);
		String query = "update garden_field set " + condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean isContainStore(int uid, int seedId) {
		DbOperation db = new DbOperation(5);
		String query = "select * from garden_store where uid = " + uid + " and seed_id = " + seedId;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return false;
	}
	
	public boolean isContainHistoryStore(int uid, int seedId) {
		DbOperation db = new DbOperation(5);
		String query = "select * from garden_history_store where uid = " + uid + " and seed_id = " + seedId;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return false;
	}
	
	public boolean updateUserStoreCount(int uid, int seedId, int count, boolean add){
		if(isContainStore(uid, seedId)) {
			DbOperation db = new DbOperation(5);
			if(add){
				String query = "update garden_store set count = count + " + count + " where uid = " + uid + " and seed_id = " + seedId;
				db.executeUpdate(query);
				db.release();
			} else {
				String query = "update garden_store set count = count - " + count + " where uid = " + uid + " and seed_id = " + seedId;
				db.executeUpdate(query);
				db.release();
			}
			return true;
		} else {
			return addGardenStore(uid, seedId, count);
		}
	}
	
	public boolean updateUserHistoryStoreCount(int uid, int seedId, int count, boolean add){
		if(isContainHistoryStore(uid, seedId)) {
			DbOperation db = new DbOperation(5);
			if(add){
				String query = "update garden_history_store set count = count + " + count + " where uid = " + uid + " and seed_id = " + seedId;
				db.executeUpdate(query);
				db.release();
			} else {
				String query = "update garden_history_store set count = count - " + count + " where uid = " + uid + " and seed_id = " + seedId;
				db.executeUpdate(query);
				db.release();
			}
			return true;
		} else {
			return addGardenHistoryStore(uid, seedId, count);
		}
	}
	
	public boolean addGardenStore(int uid, int seedId, int count) {
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_store set uid = " + uid + ", seed_id = " + seedId + ", count = " + count;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean addGardenHistoryStore(int uid, int seedId, int count) {
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_history_store set uid = " + uid + ", seed_id = " + seedId + ", count = " + count;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	
	private GardenStoreBean getGardenStore(ResultSet rs) throws SQLException {
		GardenStoreBean store = new GardenStoreBean();
		store.setCount(rs.getInt("count"));
		store.setId(rs.getInt("id"));
		store.setSeedId(rs.getInt("seed_id"));
		store.setUid(rs.getInt("uid"));
		
		return store;
	}
	
	public GardenStoreBean getStore(String condition){
		GardenStoreBean store = null;
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return null;
		
		String query = "select * from garden_store where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				store = getGardenStore(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return store;
		}finally{
			db.release();
		}
		return store;
	}
	
	public List getStoreList(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return list;
		
		String query = "select * from garden_store where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			while(rs.next()) {
				GardenStoreBean store = getGardenStore(rs);
				list.add(store);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public List getHistoryStoreList(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return list;
		
		String query = "select * from garden_history_store where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			while(rs.next()) {
				GardenStoreBean store = getGardenStore(rs);
				list.add(store);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public boolean deleteStore(String condition) {
		DbOperation db = new DbOperation(5);
		String query = "delete from garden_store where " + condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	
	
	
	
	
	public boolean addMessage(GardenMessage bean){
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_message set create_time=now(),uid="+bean.getUid()+",content='"+bean.getMessage()+"',from_uid="+bean.getFromUid()+",readed="+bean.getReaded();
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public List getMessages(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return list;
		String query = "select * from garden_message where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			while(rs.next()) {
				GardenMessage bean = getMessage(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public int getMessageCount(String condition) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return 0;
		String query = "select count(*) as count from garden_message where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
	
	private GardenMessage getMessage(ResultSet rs) throws SQLException{
		GardenMessage msg = new GardenMessage();
		msg.setId(rs.getInt("id"));
		msg.setUid(rs.getInt("uid"));
		msg.setMessage(rs.getString("content"));
		msg.setReaded(rs.getInt("readed"));
		msg.setCreateTime(rs.getTimestamp("create_time"));
		msg.setFromUid(rs.getInt("from_uid"));
		return msg;
	}
	
	public boolean updateMessages(String set,String condition) {
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return false;
		String query = "update garden_message set "+set+" where " + condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean deleteMessages(String condition) {
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return false;
		String query = "delete from garden_message where " + condition;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean isContainStat(int uid) {
		DbOperation db = new DbOperation(5);
		String query = "select * from garden_stat where uid = " + uid;
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}finally{
			db.release();
		}
		return false;
	}
	
	public boolean updateStat(int uid, int count, int price) {
		DbOperation db = new DbOperation(5);
		String query = "update garden_stat set count="+count+",price="+price+" where uid = " + uid;
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public boolean addStat(int uid, String nickName, int count, int price) {
		DbOperation db = new DbOperation(5);
		String query = "insert into garden_stat set uid="+uid+",count="+count+",price="+price+",nick_name = '" + StringUtil.toSql(nickName)+"'";
		db.executeUpdate(query);
		db.release();
		return true;
	}
	
	public List getStat(String condition) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		if(condition == null) 
			return list;
		String query = "select * from garden_stat where " + condition;
		ResultSet rs = db.executeQuery(query);
		try{
			while(rs.next()) {
				Object[] objs = new Object[5];
				objs[0] = new Integer(rs.getInt("id"));
				objs[1] = new Integer(rs.getInt("uid"));
				objs[2] = rs.getString("nick_name");
				objs[3] = new Integer(rs.getInt("count"));
				objs[4] = new Integer(rs.getInt("price"));
				list.add(objs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			db.release();
		}
		return list;
	}
	
	public int getCurStat(int uid) {
		int count = 0;
		DbOperation db = new DbOperation(5);
		String query = "select count(*) as count from garden_stat where price > (select price from garden_stat where uid = "+uid+")";
		ResultSet rs = db.executeQuery(query);
		try{
			if(rs.next()) {
				count = rs.getInt("count");
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return count;
		}finally{
			db.release();
		}
		return count;
	}
}