package jc.family.game.boat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jc.family.game.*;
import net.joycool.wap.util.db.DbOperation;

public class BoatService extends GameService {
	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 存入家族赛事
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertFmGame(BoatGameBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_game(m_id,fid1,num_total,rank,ship_id,game_type,max_speed,spend_time,prize,distance,game_point,score,boat_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMid());
			pstmt.setInt(2, bean.getFid1());
			pstmt.setInt(3, bean.getNumTotal());
			pstmt.setInt(4, bean.getRank());
			pstmt.setInt(5, bean.getShipId());
			pstmt.setInt(6, bean.getType());
			pstmt.setFloat(7, bean.getMaxSpeed());
			pstmt.setLong(8, bean.getSpendTime());
			pstmt.setLong(9, bean.getPrize());
			pstmt.setFloat(10, bean.getDistance());
			pstmt.setInt(11, bean.getGamePoint());
			pstmt.setInt(12, bean.getScore());
			pstmt.setInt(13, bean.getBoat().getBoatType());
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
	 * 插入家族成绩记录
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertFmRecord(BoatGameBean bean,int complete) {
		if (complete < 0 || complete > 1)
			complete = 1;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_boat_record(create_time,fm_id,use_time,boat_type,complete) VALUES(now(),?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFid1());
			pstmt.setLong(2, bean.getSpendTime());
			pstmt.setInt(3, bean.getBoat().getBoatType());
			pstmt.setInt(4, complete);
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
	 * 家族游戏表
	 * 
	 * @param cond
	 * @return
	 */
	public List getBoatGameList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_game where " + cond);
		try {
			while (rs.next()) {
				list.add(getGame(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	/**
	 * 家族游戏单个bean
	 * 
	 * @param cond
	 * @return
	 */
	public BoatGameBean getGameBean(String cond) {
		BoatGameBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_game where " + cond);
		try {
			if (rs.next()) {
				bean = getGame(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	BoatGameBean getGame(ResultSet rs) throws SQLException {
		BoatGameBean bean = new BoatGameBean();
		bean.setId(rs.getInt("id"));
		bean.setMid(rs.getInt("m_id"));
		bean.setFid1(rs.getInt("fid1"));
		bean.setRank(rs.getInt("rank"));
		bean.setScore(rs.getInt("score"));
		bean.setPrize(rs.getInt("prize"));
		bean.setType(rs.getInt("game_type"));
		bean.setShipId(rs.getInt("ship_id"));
		bean.setBoatType(rs.getInt("boat_type"));
		bean.setNumTotal(rs.getInt("num_total"));
		bean.setGamePoint(rs.getInt("game_point"));
		bean.setMaxSpeed(rs.getFloat("max_speed"));
		bean.setDistance(rs.getFloat("distance"));
		bean.setSpendTime(rs.getLong("spend_time"));
		return bean;
	}

	/**
	 * 参赛成员表
	 * 
	 * @param cond
	 * @return
	 */
	public List getMemberList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_member where " + cond);
		try {
			while (rs.next()) {
				list.add(getMember(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}
	
	/**
	 * 获得单个成员
	 * 
	 * @param cond
	 * @return
	 */
	public MemberBean getMemberBean(String cond) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_member where " + cond);
		try {
			if (rs.next()) {
				return getMember(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}
	
	/**
	 * 添加随机事件
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertAccident(AccidentBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_accident(speed1,angle1,distance1,speed2,angle2,distance2,speed_type,angle_type,distance_type,percent,name,bak,bigimg) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSpeed1());
			pstmt.setInt(2, bean.getAngle1());
			pstmt.setInt(3, bean.getDistance1());
			pstmt.setInt(4, bean.getSpeed2());
			pstmt.setInt(5, bean.getAngle2());
			pstmt.setInt(6, bean.getDistance2());
			pstmt.setInt(7, bean.getSpeedType());
			pstmt.setInt(8, bean.getAngleType());
			pstmt.setInt(9, bean.getDistanceType());
			pstmt.setInt(10, bean.getPercent());
			pstmt.setString(11, bean.getName());
			pstmt.setString(12, bean.getBak());
			pstmt.setString(13, bean.getBigImg());
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
	 * 修改随机事件
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterAccident(AccidentBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_game_accident set speed1=?,angle1=?,distance1=?,speed2=?,angle2=?,distance2=?,speed_type=?,angle_type=?,distance_type=?,percent=?,name=?,bak=?,bigimg=? where id="+bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSpeed1());
			pstmt.setInt(2, bean.getAngle1());
			pstmt.setInt(3, bean.getDistance1());
			pstmt.setInt(4, bean.getSpeed2());
			pstmt.setInt(5, bean.getAngle2());
			pstmt.setInt(6, bean.getDistance2());
			pstmt.setInt(7, bean.getSpeedType());
			pstmt.setInt(8, bean.getAngleType());
			pstmt.setInt(9, bean.getDistanceType());
			pstmt.setInt(10, bean.getPercent());
			pstmt.setString(11, bean.getName());
			pstmt.setString(12, bean.getBak());
			pstmt.setString(13, bean.getBigImg());
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
	 * 添加新龙舟
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertBoat(BoatBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_boat(speed,max_speed,boat_type,spe_anglereset,use_time,rent,rent_type,point,name,bak) VALUES(?,?,?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSpeed());
			pstmt.setInt(2, bean.getMaxSpeed());
			pstmt.setInt(3, bean.getBoatType());
			pstmt.setInt(4, bean.getSpeAngleReset());
			pstmt.setInt(5, bean.getUseTime());
			pstmt.setFloat(6, bean.getRent());
			pstmt.setInt(7, bean.getRentType());
			pstmt.setInt(8, bean.getPoint());
			pstmt.setString(9, bean.getName());
			pstmt.setString(10, bean.getBak());
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
	 * 修改原有龙舟属性
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterBoat(BoatBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_game_boat set speed=?,max_speed=?,boat_type=?,spe_anglereset=?,use_time=?,rent=?,rent_type=?,point=?,name=?,bak=? where id="+bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSpeed());
			pstmt.setInt(2, bean.getMaxSpeed());
			pstmt.setInt(3, bean.getBoatType());
			pstmt.setInt(4, bean.getSpeAngleReset());
			pstmt.setInt(5, bean.getUseTime());
			pstmt.setFloat(6, bean.getRent());
			pstmt.setInt(7, bean.getRentType());
			pstmt.setInt(8, bean.getPoint());
			pstmt.setString(9, bean.getName());
			pstmt.setString(10, bean.getBak());
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
	 * 所有类型船
	 * 
	 * @param cond
	 * @return
	 */
	public List getBoatList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_boat where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getBoat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 家族所有船
	 * 
	 * @param cond
	 * @return
	 */
	public List getFmBoatList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_fmboat where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getFmBoat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 随机事件表
	 * 
	 * @param cond
	 * @return
	 */
	public List getAccidentList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_accident where " + cond);
		try {
			while (rs.next()) {
				list.add(getAccident(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 随机事件单个bean
	 * 
	 * @param cond
	 * @return
	 */
	public AccidentBean getAccidentBean(String cond) {
		AccidentBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_accident where " + cond);
		try {
			if (rs.next()) {
				bean = getAccident(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	/**
	 * 单个船bean
	 * 
	 * @param cond
	 * @return
	 */
	public BoatBean getBoatBean(String cond) {
		BoatBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_boat where "
				+ cond);
		try {
			if (rs.next()) {
				bean = getBoat(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 单个家族船bean
	 * 
	 * @param cond
	 * @return
	 */
	public FmBoatBean getFmBoatBean(String cond) {
		FmBoatBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_fmboat where "
				+ cond);
		try {
			if (rs.next()) {
				bean = getFmBoat(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}
	
	/**
	 * 返回单个家族成绩
	 * 
	 * @param cond
	 * @return
	 */
	public BoatRecordBean getFmRecord(String cond) {
		BoatRecordBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_boat_record where "
				+ cond);
		try {
			if (rs.next()) {
				bean = getRecord(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	BoatBean getBoat(ResultSet rs) throws SQLException {
		BoatBean bean = new BoatBean();
		bean.setId(rs.getInt("id"));
		bean.setSpeed(rs.getInt("speed"));
		bean.setMaxSpeed(rs.getInt("max_speed"));
		bean.setRent(rs.getFloat("rent"));
		bean.setRentType(rs.getInt("rent_type"));
		bean.setBoatType(rs.getInt("boat_type"));
		bean.setUseTime(rs.getInt("use_time"));
		bean.setSpeAngleReset(rs.getInt("spe_anglereset"));
		bean.setPoint(rs.getInt("point"));
		bean.setName(rs.getString("name"));
		bean.setBak(rs.getString("bak"));
		bean.setSpeEffectTime(rs.getTimestamp("spe_effect_time").getTime());
		return bean;
	}

	FmBoatBean getFmBoat(ResultSet rs) throws SQLException {
		FmBoatBean bean = new FmBoatBean();
		bean.setId(rs.getInt("id"));
		bean.setFid(rs.getInt("fid"));
		bean.setBid(rs.getInt("bid"));
		bean.setUseTime(rs.getInt("use_time"));
		bean.setIsUse(rs.getInt("is_use"));
		return bean;
	}
	
	AccidentBean getAccident(ResultSet rs) throws SQLException {
		AccidentBean bean = new AccidentBean();
		bean.setId(rs.getInt("id"));
		bean.setAngle1(rs.getInt("angle1"));
		bean.setAngle2(rs.getInt("angle2"));
		bean.setSpeed1(rs.getInt("speed1"));
		bean.setSpeed2(rs.getInt("speed2"));
		bean.setDistance1(rs.getInt("distance1"));
		bean.setDistance2(rs.getInt("distance2"));
		bean.setAngleType(rs.getInt("angle_type"));
		bean.setSpeedType(rs.getInt("speed_type"));
		bean.setDistanceType(rs.getInt("distance_type"));
		bean.setPercent(rs.getInt("percent"));
		bean.setName(rs.getString("name"));
		bean.setBigImg(rs.getString("bigimg"));
		bean.setBak(rs.getString("bak"));
		return bean;
	}

	MemberBean getMember(ResultSet rs) throws SQLException {
		MemberBean bean = new MemberBean();
		bean.setId(rs.getInt("id"));
		bean.setMid(rs.getInt("m_id"));
		bean.setFid(rs.getInt("fid"));
		bean.setUid(rs.getInt("uid"));
		bean.setSeat(rs.getInt("seat"));
		bean.setPayMake(rs.getInt("pay_make"));
		bean.setPaySweep(rs.getInt("pay_sweep"));
		bean.setTotalHit(rs.getInt("total_hit"));
		bean.setContribution(rs.getInt("contribution"));
		return bean;
	}

	BoatRecordBean getRecord(ResultSet rs) throws SQLException {
		BoatRecordBean bean = new BoatRecordBean();
		bean.setFmId(rs.getInt("fm_id"));
		bean.setBoatType(rs.getInt("boat_type"));
		bean.setComplete(rs.getInt("complete"));
		bean.setUseTime(rs.getLong("use_time"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

}
