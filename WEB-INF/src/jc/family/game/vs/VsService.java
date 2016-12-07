package jc.family.game.vs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.db.DbOperation;

public class VsService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 添加新挑战
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertVsChallenge(Challenge bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_challenge(fm_a,fm_b,c_time,game_id,fm_status,wager,user_a)values(?,?,now(),?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getFmA());
			pstmt.setInt(2, bean.getFmB());
			pstmt.setInt(3, bean.getGameId());
			pstmt.setInt(4, bean.getStatus());
			pstmt.setInt(5, bean.getWager());
			pstmt.setInt(6, bean.getUserA());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * fm_vs_score
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertFmVsScore(FmVsScore bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_score(fm_id,game_id,win,lose,tie,score)values(" + bean.getFmId() + ","
				+ bean.getGameId() + ",?,?,?,?) on duplicate key update win=" + bean.getWin() + ",lose="
				+ bean.getLose() + ",tie=" + bean.getTie() + ",score=" + bean.getScore();
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getWin());
			pstmt.setInt(2, bean.getLose());
			pstmt.setInt(3, bean.getTie());
			pstmt.setInt(4, bean.getScore());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setFmId(dbOp.getLastInsertId());
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * fm_vs_info
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertFmVsInFo(VsBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_info(fm_id,accept,vs_time,challenge,chall_time,chall_fmid,chall_gameid,challengeid)values("
				+ bean.getFmId()
				+ ","
				+ bean.getAccept()
				+ ","
				+ bean.getTime()
				+ ","
				+ bean.isChallenge()
				+ ","
				+ bean.getChallTime()
				+ ","
				+ bean.getChallFmid()
				+ ","
				+ bean.getChallGameId()
				+ ","
				+ bean.getChallengeId() + ")";
		// 准备
		dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean insertDetail(int fmId, int id, int userCount, String detail) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_exploits_detail(id,fm_id,user_count,detail)values(" + id + "," + fmId + ","
				+ userCount + ",'" + detail + "')";
		// 准备
		dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return true;
	}

	/**
	 * 插入fm_vs_exploits
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertExploits(int gameId, long cTime, int fmA, int fmB, int challengeId, int score, long wager,
			int isWin, int userA, int userB) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_vs_exploits(id,game_id,game_end,fm_a,fm_b,c_time,score,wager,is_win,user_a,user_b)values("
				+ challengeId
				+ ","
				+ gameId
				+ ",now(),"
				+ fmA
				+ ","
				+ fmB
				+ ",'"
				+ DateUtil.formatSqlDatetime(cTime)
				+ "'," + score + "," + wager + "," + isWin + "," + userA + "," + userB + ")";
		// 准备
		dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return true;
	}

	public boolean updateFmVsInFo(VsBean vsBean) {
		DbOperation db = new DbOperation(5);
		String query = "update fm_vs_info set vs_time=" + vsBean.getTime() + ",challenge=" + vsBean.isChallenge()
				+ ",chall_time=" + vsBean.getChallTime() + ",chall_fmid=" + vsBean.getChallFmid() + ",chall_gameid="
				+ vsBean.getChallGameId() + ",challengeid=" + vsBean.getChallengeId() + " where fm_id="
				+ vsBean.getFmId();
		boolean success = db.executeUpdate(query);
		db.release();
		return success;
	}

	/**
	 * 挑战列表
	 * 
	 * @param cond
	 * @return
	 */
	public List getChallengeList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_challenge where " + cond);
		try {
			while (rs.next()) {
				list.add(getChallenge(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public List getVsExploitsList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_exploits where " + cond);
		try {
			while (rs.next()) {
				list.add(getVsExploits(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public List getVsExploitDetailList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select b.id id,game_id,game_end,fm_a,fm_b,c_time,score,wager,user_count,is_win,detail,user_a,user_b from fm_vs_exploits_detail a left join fm_vs_exploits b on a.id=b.id where "
						+ cond);
		try {
			while (rs.next()) {
				VsExploits bean = new VsExploits();
				bean.setId(rs.getInt("id"));
				bean.setGameId(rs.getInt("game_id"));
				bean.setEndTime(rs.getTimestamp("game_end").getTime());
				bean.setFmA(rs.getInt("fm_a"));
				bean.setFmB(rs.getInt("fm_b"));
				bean.setcTime(rs.getTimestamp("c_time").getTime());
				bean.setScore(rs.getInt("score"));
				bean.setWager(rs.getInt("wager"));
				bean.setIsWin(rs.getInt("is_win"));
				bean.setUserCount(rs.getInt("user_count"));
				bean.setDetail(rs.getString("detail"));
				bean.setUserA(rs.getInt("user_a"));
				bean.setUserB(rs.getInt("user_b"));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public List getFmVsScoreList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_score where " + cond);
		try {
			while (rs.next()) {
				list.add(getFmSocre(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public List getVsConfigList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_config where " + cond);
		try {
			while (rs.next()) {
				list.add(getVsConfig(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public FmVsScore getFmVsScore(String cond) {
		FmVsScore bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_score where " + cond);
		try {
			if (rs.next()) {
				bean = getFmSocre(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public VsExploits getVsExploitDetail(String cond) {
		VsExploits bean = new VsExploits();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select b.id id,game_id,game_end,fm_a,fm_b,c_time,score,wager,user_count,is_win,detail,user_a,user_b from fm_vs_exploits_detail a left join fm_vs_exploits b on a.id=b.id where "
						+ cond);
		try {
			if (rs.next()) {
				bean.setId(rs.getInt("id"));
				bean.setGameId(rs.getInt("game_id"));
				bean.setEndTime(rs.getTimestamp("game_end").getTime());
				bean.setFmA(rs.getInt("fm_a"));
				bean.setFmB(rs.getInt("fm_b"));
				bean.setcTime(rs.getTimestamp("c_time").getTime());
				bean.setScore(rs.getInt("score"));
				bean.setWager(rs.getInt("wager"));
				bean.setIsWin(rs.getInt("is_win"));
				bean.setUserCount(rs.getInt("user_count"));
				bean.setDetail(rs.getString("detail"));
				bean.setUserA(rs.getInt("user_a"));
				bean.setUserB(rs.getInt("user_b"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 记录
	 * 
	 * @param cond
	 * @return
	 */
	public VsExploits getVsExploits(String cond) {
		VsExploits bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_exploits where " + cond);
		try {
			if (rs.next()) {
				bean = getVsExploits(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 
	 * @param cond
	 * @return
	 */
	public VsConfig getVsConfig(String cond) {
		VsConfig bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_config where " + cond);
		try {
			if (rs.next()) {
				bean = getVsConfig(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	/**
	 * 挑战bean
	 * 
	 * @param cond
	 * @return
	 */
	public Challenge getChallenge(String cond) {
		Challenge bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_challenge where " + cond);
		try {
			if (rs.next()) {
				bean = getChallenge(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	public VsBean getFmVsInFo(String cond) {
		VsBean bean = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from fm_vs_info where " + cond);
		try {
			if (rs.next()) {
				bean = getFmVsInFo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bean;
	}

	private Challenge getChallenge(ResultSet rs) throws SQLException {
		Challenge bean = new Challenge();
		bean.setId(rs.getInt("id"));
		bean.setFmA(rs.getInt("fm_a"));
		bean.setFmB(rs.getInt("fm_b"));
		bean.setCTime(rs.getTimestamp("c_time").getTime());
		bean.setGameId(rs.getInt("game_id"));
		bean.setStatus(rs.getInt("fm_status"));
		bean.setDTime(rs.getTimestamp("d_time").getTime());
		bean.setScore(rs.getInt("score"));
		bean.setWager(rs.getInt("wager"));
		bean.setUserA(rs.getInt("user_a"));
		bean.setUserB(rs.getInt("user_b"));
		return bean;
	}

	private FmVsScore getFmSocre(ResultSet rs) throws SQLException {
		FmVsScore bean = new FmVsScore();
		bean.setFmId(rs.getInt("fm_id"));
		bean.setGameId(rs.getInt("game_id"));
		bean.setWin(rs.getInt("win"));
		bean.setLose(rs.getInt("lose"));
		bean.setTie(rs.getInt("tie"));
		bean.setScore(rs.getInt("score"));
		bean.setVsTime(rs.getInt("vs_time"));
		return bean;
	}

	private VsExploits getVsExploits(ResultSet rs) throws SQLException {
		VsExploits bean = new VsExploits();
		bean.setId(rs.getInt("id"));
		// bean.setFmId(rs.getInt("fm_id"));
		bean.setGameId(rs.getInt("game_id"));
		bean.setEndTime(rs.getTimestamp("game_end").getTime());
		bean.setFmA(rs.getInt("fm_a"));
		bean.setFmB(rs.getInt("fm_b"));
		bean.setcTime(rs.getTimestamp("c_time").getTime());
		// bean.setChallengeId(rs.getInt("challenge_id"));
		bean.setScore(rs.getInt("score"));
		bean.setWager(rs.getInt("wager"));
		bean.setIsWin(rs.getInt("is_win"));
		// bean.setUserCount(rs.getInt("user_count"));
		// bean.setDetail(rs.getString("detail"));
		bean.setUserA(rs.getInt("user_a"));
		bean.setUserB(rs.getInt("user_b"));
		return bean;
	}

	private VsConfig getVsConfig(ResultSet rs) throws SQLException {
		VsConfig bean = new VsConfig();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setPreTime(rs.getInt("pre_time"));
		bean.setEnterTime(rs.getInt("enter_time"));
		bean.setMaxTime(rs.getInt("max_time"));
		bean.setHide(rs.getInt("hide"));
		bean.setFlag(rs.getInt("flag"));
		bean.setMinPlayer(rs.getInt("min_player"));
		bean.setMaxPlayer(rs.getInt("max_player"));
		bean.setGameClass(rs.getString("game_class"));
		bean.setMatchScoreCount(rs.getInt("match_score_count"));
		bean.setMatchCount(rs.getInt("match_count"));
		bean.setColumnId(rs.getInt("columnId"));
		return bean;
	}

	private VsBean getFmVsInFo(ResultSet rs) throws SQLException {
		VsBean bean = new VsBean();
		bean.setFmId(rs.getInt("fm_id"));
		bean.setAccept(rs.getInt("accept"));
		bean.setTime(rs.getInt("vs_time"));
		bean.setChallenge(rs.getBoolean("challenge"));
		bean.setChallTime(rs.getLong("chall_time"));
		bean.setChallFmid(rs.getInt("chall_fmid"));
		bean.setChallGameId(rs.getInt("chall_gameid"));
		bean.setChallengeId(rs.getInt("challengeid"));
		return bean;
	}
}
