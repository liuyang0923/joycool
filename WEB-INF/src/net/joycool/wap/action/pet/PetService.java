package net.joycool.wap.action.pet;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import net.joycool.wap.action.dhh.DhhShipBean;
import net.joycool.wap.action.dhh.DhhUserBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;
import java.util.TreeMap;

/**
 * @author liq
 * @explain： 乐宠
 * @datetime:2007-5-31
 */
public class PetService {

	// 宠物时间 48小时
	public static long PET_AGE = 60 * 1000 * 60 * 48;

	// public static long PET_AGE = 1000 * 60;

	public Vector getPetList(String condition) {
		Vector petTypeList = null;
		PetTypeBean petTypeBean = null;
		// 构建查询语句
		String query = "SELECT * from pet_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector petList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP,
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD);
			if (petList != null) {
				return petList;
			}
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		petTypeList = new Vector();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petTypeBean = new PetTypeBean();
				petTypeBean.setId(rs.getInt("id"));
				petTypeBean.setName(rs.getString("name"));
				petTypeBean.setAge(rs.getInt("age"));
				petTypeBean.setAgile(rs.getInt("agile"));
				petTypeBean.setExp(rs.getInt("exp"));
				petTypeBean.setFriend(rs.getInt("friend"));
				petTypeBean.setHealth(rs.getInt("health"));
				petTypeBean.setHungry(rs.getInt("hungry"));
				petTypeBean.setIntel(rs.getInt("intel"));
				petTypeBean.setStrength(rs.getInt("strength"));
				petTypeBean.setTenacious(rs.getInt("tenacious"));
				petTypeBean.setPrice(rs.getInt("price"));
				petTypeBean.setImage(rs.getString("image"));
				petTypeBean.setAl(rs.getInt("al"));
				petTypeBean.setIn(rs.getInt("in"));
				petTypeBean.setSt(rs.getInt("st"));
				petTypeBean.setShot(rs.getString("shot"));
				petTypeBean.setLon(rs.getString("lon"));

				petTypeList.add(petTypeBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, petTypeList, OsCacheUtil.PET_CACHE_GROUP);
		}
		// 释放资源
		dbOp.release();
		return petTypeList;
	}

	public PetTypeBean getPet(String condition) {
		PetTypeBean petBean = null;

		// 构建查询语句
		String query = "SELECT * from pet_type";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			petBean = (PetTypeBean) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP,
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD);
			if (petBean != null) {
				return petBean;
			}
		}

		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				petBean = new PetTypeBean();
				petBean.setId(rs.getInt("id"));
				petBean.setName(rs.getString("name"));
				petBean.setAge(rs.getInt("age"));
				petBean.setAgile(rs.getInt("agile"));
				petBean.setExp(rs.getInt("exp"));
				petBean.setFriend(rs.getInt("friend"));
				petBean.setHealth(rs.getInt("health"));
				petBean.setHungry(rs.getInt("hungry"));
				petBean.setIntel(rs.getInt("intel"));
				petBean.setStrength(rs.getInt("strength"));
				petBean.setTenacious(rs.getInt("tenacious"));
				petBean.setPrice(rs.getInt("price"));
				petBean.setImage(rs.getString("image"));
				petBean.setAl(rs.getInt("al"));
				petBean.setIn(rs.getInt("in"));
				petBean.setSt(rs.getInt("st"));
				petBean.setMark(rs.getInt("mark"));
				petBean.setShot(rs.getString("shot"));
				petBean.setLon(rs.getString("lon"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, petBean, OsCacheUtil.PET_CACHE_GROUP);
		}

		// 释放资源
		dbOp.release();
		return petBean;
	}

	// ////////////////////////////////////////////////////////////////////////////////
	public PetUserBean getUser(String condition) {
		PetUserBean petUser = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句
		String query = "SELECT * from pet_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				petUser = new PetUserBean();
				petUser.setId(rs.getInt("id"));
				petUser.setName(rs.getString("name"));
				petUser.setUser_id(rs.getInt("user_id"));
				petUser.setHealth(rs.getInt("health"));
				petUser.setType(rs.getInt("type"));
				petUser.setSex(rs.getInt("sex"));
				petUser.setAge(rs.getInt("age"));
				petUser.setExp(rs.getInt("exp"));
				petUser.setRank(rs.getInt("rank"));
				petUser.setHungry(rs.getInt("hungry"));
				petUser.setIntel(rs.getInt("intel"));
				petUser.setStrength(rs.getInt("strength"));
				petUser.setAgile(rs.getInt("agile"));
				petUser.setTenacious(rs.getInt("tenacious"));
				petUser.setFriend(rs.getInt("friend"));
				petUser.setCreatetime(rs.getString("createtime"));
				petUser.setSpot(rs.getInt("spot"));
				petUser.setClear(rs.getInt("clear"));
				petUser.setIntegral(rs.getInt("integral"));
				petUser.setToday(rs.getInt("today"));
				petUser.setYesterday(rs.getInt("yesterday"));
				petUser.setLeftintegral(rs.getInt("leftintegral"));
				// 设定年龄
				changeAge(petUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return petUser;
	}

	public int getCount(String condition) {
		// 数据库操作类
		ResultSet rs = null;
		int xxx = 0;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		int x = 0;
		// 构建查询语句
		String query = "select count(*) from pet_user where " + condition;
		try {
			// 查询
			rs = dbOp.executeQuery(query);
			while (rs.next()) {
				xxx = rs.getInt(1) + 1;
			}
			;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();
		return xxx;
	}
/**
 *  
 * @author guip
 * @explain：修改
 * @datetime:2007-7-20 10:11:44
 * @param petUser
 * @return void
 */
	public void changeAge(PetUserBean petUser) {
		String aa = petUser.getCreatetime().substring(0, 19);
		Date createtime = DateUtil.parseDate(aa, DateUtil.normalTimeFormat);
		Calendar c = Calendar.getInstance();
		//Date now = new Date();
		long temp = (c.getTimeInMillis() - createtime.getTime()) / PET_AGE;
		//temp = (now.getTime() - createtime.getTime()) / PET_AGE;
		petUser.setAge((int) temp + 1);
	}

	/**
	 *  
	 * @author guip
	 * @explain：该实现启用缓存请注意，更新时间为12小时
	 * @datetime:2007-7-20 12:12:43
	 * @param condition
	 * @param time
	 * @return
	 * @return Vector
	 */
	public Vector getUserList(String condition, int time) {
		PetUserBean petUser = null;
		// 构建查询语句
		String query = "SELECT * from pet_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector ebookList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_FIVE, time);
			if (ebookList != null) {
				return ebookList;
			}
		}
		Vector petUserList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petUser = new PetUserBean();
				petUser.setId(rs.getInt("id"));
				petUser.setName(rs.getString("name"));
				petUser.setUser_id(rs.getInt("user_id"));
				petUser.setHealth(rs.getInt("health"));
				petUser.setType(rs.getInt("type"));
				petUser.setSex(rs.getInt("sex"));
				//petUser.setAge(rs.getInt("age"));
				petUser.setExp(rs.getInt("exp"));
				petUser.setRank(rs.getInt("rank"));
				petUser.setHungry(rs.getInt("hungry"));
				petUser.setIntel(rs.getInt("intel"));
				petUser.setStrength(rs.getInt("strength"));
				petUser.setAgile(rs.getInt("agile"));
				petUser.setTenacious(rs.getInt("tenacious"));
				petUser.setFriend(rs.getInt("friend"));
				petUser.setCreatetime(rs.getString("createtime"));
				petUser.setSpot(rs.getInt("spot"));
				petUser.setClear(rs.getInt("clear"));
				petUser.setIntegral(rs.getInt("integral"));
				petUser.setToday(rs.getInt("today"));
				petUser.setYesterday(rs.getInt("yesterday"));
				petUser.setLeftintegral(rs.getInt("leftintegral"));
				//设定年龄
				changeAge(petUser);
				petUserList.add(petUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, petUserList, OsCacheUtil.PET_CACHE_GROUP_FIVE);
		}
		return petUserList;
	}

	/**
	 * 取得所有宠物列表
	 */
	public Vector getAllUserList(String condition) {
		PetUserBean petUser = null;
		// 构建查询语句
		String query = "SELECT * from pet_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		Vector petUserList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petUser = new PetUserBean();
				petUser.setId(rs.getInt("id"));
				petUser.setName(rs.getString("name"));
				petUser.setUser_id(rs.getInt("user_id"));
				petUser.setHealth(rs.getInt("health"));
				petUser.setType(rs.getInt("type"));
				petUser.setSex(rs.getInt("sex"));
				petUser.setAge(rs.getInt("age"));
				petUser.setExp(rs.getInt("exp"));
				petUser.setRank(rs.getInt("rank"));
				petUser.setHungry(rs.getInt("hungry"));
				petUser.setIntel(rs.getInt("intel"));
				petUser.setStrength(rs.getInt("strength"));
				petUser.setAgile(rs.getInt("agile"));
				petUser.setTenacious(rs.getInt("tenacious"));
				petUser.setFriend(rs.getInt("friend"));
				petUser.setCreatetime(rs.getString("createtime"));
				petUser.setSpot(rs.getInt("spot"));
				petUser.setClear(rs.getInt("clear"));
				petUser.setIntegral(rs.getInt("integral"));
				petUser.setToday(rs.getInt("today"));
				petUser.setYesterday(rs.getInt("yesterday"));
				petUser.setLeftintegral(rs.getInt("leftintegral"));

				petUserList.add(petUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return petUserList;
	}

	public boolean addUser(PetUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO pet_user(user_id,name,health,type,sex,age,exp,hungry,intel,strength,agile,tenacious,friend,rank,spot,clear,createtime) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUser_id());
			pstmt.setString(2, bean.getName());
			pstmt.setInt(3, bean.getHealth());
			pstmt.setInt(4, bean.getType());
			pstmt.setInt(5, bean.getSex());
			pstmt.setInt(6, bean.getAge());
			pstmt.setInt(7, bean.getExp());
			pstmt.setInt(7, bean.getRank());
			pstmt.setInt(8, bean.getHungry());
			pstmt.setInt(9, bean.getIntel());
			pstmt.setInt(10, bean.getStrength());
			pstmt.setInt(11, bean.getAgile());
			pstmt.setInt(12, bean.getTenacious());
			pstmt.setInt(13, bean.getFriend());
			pstmt.setInt(14, bean.getRank());
			pstmt.setInt(15, bean.getSpot());
			pstmt.setInt(16, bean.getClear());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		bean.setId(dbOp.getLastInsertId());

		bean.setCreatetime(SqlUtil.getString(dbOp,
				"select createtime from pet_user where id = " + bean.getId()));
		// 释放资源
		dbOp.release();

		return true;
	}

	public boolean delUser(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM pet_user WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		// 清空用户宠物列表缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP,
				"SELECT * from pet_user WHERE " + condition);
		return result;
	}

	public boolean updateUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE pet_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		// 清空用户宠物列表缓存
		OsCacheUtil.flushGroup(OsCacheUtil.PET_CACHE_GROUP,
				"SELECT * from pet_user WHERE " + condition);
		return result;
	}

	public int getUserCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM pet_user WHERE "
				+ condition;
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return count;
	}

	public Vector getMatchEventList(String condition) {
		Vector eventList = null;
		MatchEventBean eventBean = null;
		// 构建查询语句
		String query = "SELECT * from pet_event";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector petList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP,
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD);
			if (petList != null) {
				return petList;
			}
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		eventList = new Vector();
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				eventBean = new MatchEventBean();
				eventBean.setId(rs.getInt("id"));
				eventBean.setGameid(rs.getInt("gameid"));
				eventBean.setFactor(rs.getFloat("factor"));
				eventBean.setDescription(rs.getString("description"));

				eventList.add(eventBean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, eventList, OsCacheUtil.PET_CACHE_GROUP);
		}
		// 释放资源
		dbOp.release();
		return eventList;
	}

	public MatchFactorBean getFactor(String condition) {
		MatchFactorBean factorBean = null;
		// 构建查询语句
		String query = "SELECT * from pet_factor";
		if (condition != null) {
			query = query + " WHERE " + condition + " limit 1";
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			factorBean = (MatchFactorBean) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP,
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD);
			if (factorBean != null) {
				return factorBean;
			}
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				factorBean = new MatchFactorBean();
				factorBean.setId(rs.getInt("id"));
				factorBean.setAl(rs.getFloat("al"));
				factorBean.setIn(rs.getFloat("in"));
				factorBean.setSt(rs.getFloat("st"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, factorBean, OsCacheUtil.PET_CACHE_GROUP);
		}
		// 释放资源
		dbOp.release();
		return factorBean;
	}

	public boolean addMatchUser(MatchUserBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO pet_match_user(name,pet_user,yesterday) VALUES(?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setInt(2, bean.getPet_user());
			pstmt.setInt(3, bean.getYesterday());

		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		dbOp.release();

		return true;
	}
	
	public MatchUserBean getMatchUser(String condition) {
		MatchUserBean bean = null;
		// 构建查询语句
		String query = "SELECT * from pet_match_user";
		if (condition != null) {
			query = query + " WHERE " + condition + " limit 1";
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			bean = (MatchUserBean) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH,
					OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH);
			if (bean != null) {
				return bean;
			}
		}
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				bean = new MatchUserBean();
				bean.setId(rs.getInt("id"));
				bean.setPet_user(rs.getInt("pet_user"));
				bean.setYesterday(rs.getInt("yesterday"));
				bean.setName(rs.getString("name"));
				bean.setTotalstake(rs.getLong("totalstake"));
				bean.setWintime(rs.getInt("wintime"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, bean, OsCacheUtil.PET_CACHE_GROUP_MATCH);
		}
		// 释放资源
		dbOp.release();
		return bean;
	}

	/**
	 * 取得赌博参赛宠物列表
	 */
	public Vector getMatchUserList(String condition) {
		MatchUserBean bean = null;
		// 构建查询语句
		String query = "SELECT * from pet_match_user";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		Vector beanList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				bean = new MatchUserBean();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setPet_user(rs.getInt("pet_user"));
				bean.setYesterday(rs.getInt("yesterday"));

				beanList.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return beanList;
	}

	
	public boolean updateMatchUser(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "UPDATE pet_match_user SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}


	public boolean executeUpdate(String query) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean addStake(MatchStakeBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "INSERT INTO pet_match_stake(user_id,pet_id,stake,user_name,pet_name) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUser_id());
			pstmt.setInt(2, bean.getPet_id());
			pstmt.setLong(3, bean.getStake());
			pstmt.setString(4, bean.getUser_name());
			pstmt.setString(5, bean.getPet_name());

		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();

		// 释放资源
		dbOp.release();

		return true;
	}

	/**
	 * 该实现启用缓存请注意，更新时间为12小时
	 */
	public MatchStakeBean getStake(String condition) {
		MatchStakeBean petStake = null;
		// 构建查询语句
		String query = "SELECT * from pet_match_stake";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petStake = new MatchStakeBean();
				petStake.setUser_id(rs.getInt("user_id"));
				petStake.setPet_id(rs.getInt("pet_id"));
				petStake.setStake(rs.getInt("stake"));
				petStake.setUser_name(rs.getString("user_name"));
				petStake.setPet_name(rs.getString("pet_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return petStake;
	}

	/**
	 * 该实现启用缓存请注意，更新时间为12小时
	 */
	public Vector getStakeList(String condition) {
		MatchStakeBean petStake = null;
		// 构建查询语句
		String query = "SELECT * from pet_match_stake";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		 //缓存
		 if (OsCacheUtil.USE_CACHE) {
		 String key = query;
		 Vector ebookList = (Vector) OsCacheUtil.get(key,OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH);
		 if (ebookList != null) {
		 return ebookList;
		 }
		 }
		Vector petStakeList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petStake = new MatchStakeBean();
				petStake.setUser_id(rs.getInt("user_id"));
				petStake.setPet_id(rs.getInt("pet_id"));
				petStake.setStake(rs.getInt("stake"));
				petStake.setUser_name(rs.getString("user_name"));
				petStake.setPet_name(rs.getString("pet_name"));

				petStakeList.add(petStake);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		 //缓存
		 if (OsCacheUtil.USE_CACHE) {
		 String key = query;
		 OsCacheUtil.put(key, petStakeList, OsCacheUtil.PET_CACHE_GROUP_MATCH);
		 }
		return petStakeList;
	}

	/**
	 *取得按照下注者分组的赌注总和,也就是每个下注者一共下多少赌注
	 */
	public Vector getSumStakeList() {
		MatchStakeBean petStake = null;
		// 构建查询语句
		String query = "SELECT user_id,SUM(stake) FROM pet_match_stake where 1= 1 group by user_id";

		Vector petStakeList = new Vector();
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				petStake = new MatchStakeBean();
				petStake.setUser_id(rs.getInt("user_id"));
				petStake.setStake(rs.getLong("SUM(stake)"));

				petStakeList.add(petStake);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return petStakeList;
	}

	public boolean updateStake(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(4);
		String query = "UPDATE pet_match_stake SET " + set
				+ " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();

		return result;
	}

	public long getStakeSum(String condition) {
		long count = -1;
		// 构建更新语句
		String query = "SELECT SUM(stake) FROM pet_match_stake where "
				+ condition;
		// 数据库操作类
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if(((Long) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)) != null){
			count = ((Long) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)).longValue();
			if (count >= 0) {
				return count;
			}
			}
		}
		DbOperation dbOp = new DbOperation(4);

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getLong("SUM(stake)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Long(count), OsCacheUtil.PET_CACHE_GROUP_MATCH);
		}
		return count;
	}

	public int getStakeCount(String condition) {
		int count = -1;
		// 构建更新语句
		String query = "SELECT count(*) FROM pet_match_stake where "
				+ condition;
		// 数据库操作类
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if(((Integer) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)) != null){
			count = ((Integer) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)).intValue();
			if (count >= 0) {
				return count;
			}
			}
		}
		DbOperation dbOp = new DbOperation(4);

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		if(count < 0)
			count = 0;
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.PET_CACHE_GROUP_MATCH);
		}
		return count;
	}
	
	public int getTotalCount() {
		int count = -1;
		// 构建更新语句
		String query = "select count(distinct user_id) from pet_match_stake";
		// 数据库操作类
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			if(((Integer) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)) != null){
			count = ((Integer) OsCacheUtil.get(key,
					OsCacheUtil.PET_CACHE_GROUP_MATCH, OsCacheUtil.PET_CACHE_FLUSH_PERIOD_MATCH)).intValue();
			if (count >= 0) {
				return count;
			}
			}
		}
		DbOperation dbOp = new DbOperation(4);

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				count = rs.getInt("count(distinct user_id)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		if(count < 0)
			count = 0;
		// 缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, new Integer(count), OsCacheUtil.PET_CACHE_GROUP_MATCH);
		}
		return count;
	}
}
