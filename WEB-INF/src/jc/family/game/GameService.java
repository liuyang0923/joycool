package jc.family.game;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.db.DbOperation;

public class GameService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 存入参赛成员
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertMember(MemberBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_member(m_id,fid,uid,total_hit,contribution,pay_sweep,pay_make,seat) VALUES(?,?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getMid());
			pstmt.setInt(2, bean.getFid());
			pstmt.setInt(3, bean.getUid());
			pstmt.setInt(4, bean.getTotalHit());
			pstmt.setInt(5, bean.getContribution());
			pstmt.setInt(6, bean.getPaySweep());
			pstmt.setInt(7, bean.getPayMake());
			pstmt.setInt(8, bean.getSeat());
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
	 * 插入周排表数据
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertWeekGame(WeekMatchBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_weekgame(weekday,game_type,start_time,end_time,effect) VALUES(?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getWeekDay());
			pstmt.setInt(2, bean.getType());
			pstmt.setTimestamp(3, bean.getStartSQLtime());
			pstmt.setTimestamp(4, bean.getEndSQLtime());
			pstmt.setInt(5, bean.getEffect());
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
	 * 修改周排表中的某一赛事
	 * 
	 * @param bean
	 * @return
	 */
	public boolean alterWeekGame(WeekMatchBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_game_weekgame set weekday=?,game_type=?,start_time=?,end_time=?,effect=? where id="
				+ bean.getId();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getWeekDay());
			pstmt.setInt(2, bean.getType());
			pstmt.setTimestamp(3, bean.getStartSQLtime());
			pstmt.setTimestamp(4, bean.getEndSQLtime());
			pstmt.setInt(5, bean.getEffect());
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
	 * 成员报名表
	 * 
	 * @param cond
	 * @return
	 */
	public List getApplyList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery(cond);
		try {
			while (rs.next()) {
				list.add(getApply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 家族报名表
	 * 
	 * @param cond
	 * @return
	 */
	public List getFmApplyList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_fmapply where " + cond);
		try {
			while (rs.next()) {
				list.add(getFmApply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 成员报名bean
	 * 
	 * @param cond
	 * @return
	 */
	public ApplyBean getApplyBean(String cond) {
		ApplyBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_apply where " + cond);
		try {
			if (rs.next()) {
				bean = getApply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 家族报名bean
	 * 
	 * @param cond
	 * @return
	 */
	public FmApplyBean getFmApplyBean(String cond) {
		FmApplyBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_fmapply where " + cond);
		try {
			if (rs.next()) {
				bean = getFmApply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 赛事表
	 * 
	 * @param cond
	 * @return
	 */
	public List getMatchList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_match where " + cond);
		try {
			while (rs.next()) {
				list.add(getMatch(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 具体赛事
	 * 
	 * @param cond
	 * @return
	 */
	public MatchBean getMatchBean(String cond) {
		MatchBean bean = new MatchBean();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_match where " + cond);
		try {
			if (rs.next()) {
				bean = getMatch(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 家族游戏单个bean
	 * 
	 * @param cond
	 * @return
	 */
	public GameHistoryBean getHistoryGameBean(String cond) {
		GameHistoryBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_game where " + cond);
		try {
			if (rs.next()) {
				bean = getGameHistoryBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 赛事周排表
	 * 
	 * @param cond
	 * @return
	 */
	public List getWeekMatchList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_weekgame where " + cond);
		try {
			while (rs.next()) {
				list.add(getWeekMatch(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 周排表中单个赛事
	 * 
	 * @param cond
	 * @return
	 */
	public WeekMatchBean getWeekMatchBean(String cond) {
		WeekMatchBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_weekgame where " + cond);
		try {
			if (rs.next()) {
				bean = getWeekMatch(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	FmApplyBean getFmApply(ResultSet rs) throws SQLException {
		FmApplyBean bean = new FmApplyBean();
		bean.setId(rs.getInt("id"));
		bean.setFid(rs.getInt("fid"));
		bean.setMid(rs.getInt("m_id"));
		bean.setDel(rs.getInt("del"));
		bean.setTotalApply(rs.getInt("total_apply"));
		return bean;
	}

	ApplyBean getApply(ResultSet rs) throws SQLException {
		ApplyBean bean = new ApplyBean();
		bean.setId(rs.getInt("id"));
		bean.setUid(rs.getInt("uid"));
		bean.setFid(rs.getInt("fid"));
		bean.setMid(rs.getInt("m_id"));
		bean.setType(rs.getInt("game_type"));
		bean.setState(rs.getInt("state"));
		bean.setIsPay(rs.getInt("is_pay"));
		return bean;
	}

	MatchBean getMatch(ResultSet rs) throws SQLException {
		MatchBean bean = new MatchBean();
		bean.setId(rs.getInt("id"));
		bean.setType(rs.getInt("game_type"));
		bean.setState(rs.getInt("state"));
		bean.setState2(rs.getInt("state2"));
		bean.setFmCount(rs.getInt("fm_count"));
		bean.setStarttime(rs.getTimestamp("start_time"));
		bean.setEndtime(rs.getTimestamp("end_time"));
		bean.setEndTime(rs.getTimestamp("end_time").getTime());
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bean;
	}

	WeekMatchBean getWeekMatch(ResultSet rs) throws SQLException {
		WeekMatchBean bean = new WeekMatchBean();
		bean.setId(rs.getInt("id"));
		bean.setWeekDay(rs.getInt("weekday"));
		bean.setType(rs.getInt("game_type"));
		bean.setStarttime(rs.getTimestamp("start_time"));
		bean.setEnd_time(rs.getTimestamp("end_time"));
		bean.setEffect(rs.getInt("effect"));
		return bean;
	}

	// 返回一个int型的值
	public int selectIntResult(String sql) {
		DbOperation db = new DbOperation(5);
		try {
			int c = db.getIntResult(sql);
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return 0;
	}

	// 审批
	public boolean updateApply(int state, int mid, int uid, int fid) {
		DbOperation db = new DbOperation(5);
		boolean update = false;
		try {
			db.startTransaction();
			int ustate = db.getIntResult("select state from fm_game_apply where uid=" + uid + " and m_id=" + mid);
			String query = "update fm_game_apply set state =" + state + " where m_id=" + mid + " and uid=" + uid
					+ " and fid=" + fid;
			update = db.executeUpdate(query);
			if (!update) {
				return false;
			}
			query = "select * from fm_game_fmapply where fid=" + fid + " and m_id=" + mid;
			ResultSet rs = db.executeQuery(query);
			FmApplyBean fab = new FmApplyBean();
			while (rs.next()) {
				fab = getFmApply(rs);
				break;
			}
			if (fab == null) {
				return false;
			}
			if (state == 1) {// 不通过
				int total = fab.getTotalApply();
				if (ustate == 2) {// 如果用户是通过审批 ，再不通过审批的，人数才减一
					String str = "";
					if (total > 0) {
						str = "total_apply-1";
					} else {
						str = "0";
					}
					query = "update fm_game_fmapply set total_apply=" + str + " where fid=" + fid + " and m_id=" + mid;
					update = db.executeUpdate(query);
					if (!update) {
						return false;
					}
				}
			} else if (state == 2) {// 通过
				query = "update fm_game_fmapply set total_apply=total_apply+1 where fid=" + fid + " and m_id=" + mid;
				update = db.executeUpdate(query);
				if (!update) {
					return false;
				}
			}
			db.commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
		return update;
	}

	// 是否参加比赛
	public boolean updateFmApply(int mid, int fid, int cmd) {
		DbOperation db = new DbOperation(5);
		String query = "select count(id) from fm_game_fmapply where m_id=" + mid + " and fid=" + fid;
		int count;
		try {
			count = db.getIntResult(query);
			db.startTransaction();
			if (count > 0) {
				query = "update fm_game_fmapply set del=" + cmd + " where m_id=" + mid + " and fid=" + fid;
			} else {
				query = "insert into fm_game_fmapply(m_id, fid, del)values(" + mid + "," + fid + "," + cmd + ")";
			}
			boolean update = db.executeUpdate(query);
			if (!update) {
				return false;
			}
			if (cmd == 1) {// 不参加时删除，有关的报名信息
				query = "delete from fm_game_apply where m_id=" + mid + " and fid=" + fid;
				update = db.executeUpdate(query);
				if (!update) {
					return false;
				}
			}
			db.commitTransaction();
			return update;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 扣钱基金,注意缓存自己改
	 * 
	 * @param fid
	 * @param userid
	 * @param fund
	 * @return
	 */
	public boolean deductFmMoney(int fid, int userid, int fund) {
		DbOperation db = new DbOperation(5);
		boolean result = false;
		// 修改家族表里家族基金总额
		result = db.executeUpdate("update fm_home set  money=(money-" + fund + ")  where id=" + fid);
		db.release();
		return result;
	}

	/**
	 * 累加家族游戏积分信息
	 * 
	 * @param fmid
	 * @param gametype
	 * @param score
	 * @return
	 */
	public boolean insertFamilyGameScore(int fmid, int gametype, int score) {
		DbOperation db = new DbOperation(5);
		String query = "";
		if (gametype == 1) {
			query = "insert into fm_game_score(fmid,boat_score)values(" + fmid + "," + score
					+ ") on duplicate key update fmid=" + fmid + ",boat_score=boat_score+" + score;
		}
		if (gametype == 2) {
			query = "insert into fm_game_score(fmid,snow_score)values(" + fmid + "," + score
					+ ") on duplicate key update fmid=" + fmid + ",snow_score=snow_score+" + score;
		}
		if (gametype == 3) {
			query = "insert into fm_game_score(fmid,ask_score)values(" + fmid + "," + score
					+ ") on duplicate key update fmid=" + fmid + ",ask_score=ask_score+" + score;
		}
		boolean result = db.executeUpdate(query);
		db.release();
		return result;
	}

	/**
	 * 添加家族游戏信息
	 * 
	 * @param fmid
	 * @param fmExploit
	 * @param gameNum
	 * @return
	 */
	public boolean addFmGameInfo(int fmid, int fmExploit, int gameNum, String event, int eventType, int mid) {
		DbOperation db = new DbOperation(5);
		db.startTransaction();

		boolean result = false;
		// 插入家族功勋历史表
		String query = "insert into fm_home_exploit_history(fm_id,event,event_time,event_type,m_id)values(?,?,now(),?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, fmid);
			pstmt.setString(2, event);
			pstmt.setInt(3, eventType);
			pstmt.setInt(4, mid);
			result = db.executePstmt();
			if (!result) {
				return result;
			}
			query = "update fm_home set fm_exploit=fm_exploit+" + fmExploit + ",game_num=game_num+" + gameNum
					+ " where id=" + fmid;
			// 修改家族功勋
			result = db.executeUpdate(query);
			if (!result) {
				return result;
			}
			db.commitTransaction();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加家族游戏经验值
	 * 
	 * @param fmid
	 * @param fmExploit
	 * @param gameNum
	 * @return
	 */
	public boolean addFmGameInfo(int fmid, int gameNum) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_home set game_num=game_num+" + gameNum + " where id=" + fmid;
		boolean result = db.executeUpdate(query);
		db.release();
		return result;
	}

	/**
	 * 添加家族个人游戏信息
	 * 
	 * @param userid
	 * @param fmExploit
	 * @return
	 */
	public boolean addFmUserGameInfo(int userid, int fmid, int exploit, String event) {
		DbOperation db = new DbOperation(5);

		boolean result = false;
		// 插入用户功勋历史表
		String query = "insert into fm_user_exploit_history(userid,event,event_time,fmid)values(?,?,now(),?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return result;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, userid);
			pstmt.setString(2, event);
			pstmt.setInt(3, fmid);
			result = db.executePstmt();
			if (!result) {
				return result;
			}
			// 修改用户功勋
			result = db.executeUpdate("update fm_user set con_fm=con_fm+" + exploit + " where id=" + userid);
			if (!result) {
				return result;
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加赛程
	 * 
	 * @param bean
	 * @return
	 */
	public boolean addMatchBean(MatchBean bean) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_game_match (start_time,end_time,game_type) values(?,?,?)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setTimestamp(1, bean.getStartSQLtime());
			pstmt.setTimestamp(2, bean.getEndSQLtime());
			pstmt.setInt(3, bean.getType());
			pstmt.execute();
			bean.setId(db.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			db.release();
		}
		return true;
	}

	/**
	 * 如果家族报名未参赛扣积分，积分最小为0
	 * 
	 * @param fmid
	 * @param fmExploit
	 * @return
	 */
	public boolean updateFamilyGameScore(int fmid, int fmExploit, int gametype) {
		DbOperation db = new DbOperation(5);
		try {
			int score = 0;
			String[] gametypes = { "boat_score", "snow_score", "ask_score" };
			ResultSet rs = db.executeQuery("select " + gametypes[gametype] + " from fm_game_score where fmid=" + fmid);
			String query = "update fm_game_score set " + gametypes[gametype] + "=";

			if (rs.next()) {
				score = rs.getInt(1);
			}
			if (score - fmExploit < 0) {
				query += "0";
			} else {
				query += score - fmExploit;
			}
			db.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			return false;
		} finally {
			db.release();
		}
	}

	/**
	 * 家族游戏结束后，增加奖金到家族基金中
	 * 
	 * @param id
	 * @param fund
	 * @return
	 */
	public boolean updatFmFund(int id, long fund) {
		DbOperation db = new DbOperation(5);
		boolean rs = db.executeUpdate("update fm_home set  money=(money+" + fund + ")  where id=" + id);
		db.release();
		return rs;
	}

	/**
	 * 家族游戏表历史记录
	 * 
	 * @param cond
	 * @return
	 */
	public List getHistoryGameList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_game_game where " + cond);
		try {
			while (rs.next()) {
				list.add(getGameHistoryBean(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 家族游戏表历史记录
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private GameHistoryBean getGameHistoryBean(ResultSet rs) throws SQLException {
		GameHistoryBean bean = new GameHistoryBean();
		bean.setId(rs.getInt("id"));
		bean.setMid(rs.getInt("m_id"));
		bean.setFid1(rs.getInt("fid1"));
		bean.setFid2(rs.getInt("fid2"));
		bean.setSpendTime(rs.getLong("spend_time"));
		bean.setPrize(rs.getLong("prize"));
		bean.setNumTotal(rs.getInt("num_total"));
		bean.setRank(rs.getInt("rank"));
		bean.setShipId(rs.getInt("ship_id"));
		bean.setMaxSpeed(rs.getFloat("max_speed"));
		bean.setType(rs.getInt("game_type"));
		bean.setHoldTime(rs.getTimestamp("hold_time").getTime());
		bean.setDistance(rs.getFloat("distance"));
		bean.setScore(rs.getInt("score"));
		bean.setAskscore(rs.getInt("ask_score"));
		bean.setSnow_score(rs.getInt("snow_score"));
		bean.setGamePoint(rs.getInt("game_point"));
		return bean;
	}

	/**
	 * 添加家族动态
	 * 
	 * @param fmid
	 * @param url
	 */
	public boolean addMovement(int fmid, String movement, String url) {
		DbOperation db = new DbOperation(5);
		String query = "insert into fm_movement (fm_id,movement,movement_time,fm_url,fm_state) values (?,?,now(),?,2)";
		if (!db.prepareStatement(query)) {
			db.release();
			return false;
		}
		PreparedStatement pstmt = db.getPStmt();
		try {
			pstmt.setInt(1, fmid);
			pstmt.setString(2, movement);
			pstmt.setString(3, url);
			return db.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			db.release();
			return false;
		} finally {
			db.release();
		}
	}
}
