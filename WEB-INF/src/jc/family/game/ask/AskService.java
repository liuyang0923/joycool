package jc.family.game.ask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jc.family.game.GameHistoryBean;
import jc.family.game.GameService;
import jc.family.game.MemberBean;
import net.joycool.wap.util.db.DbOperation;

public class AskService extends GameService {

	/**
	 * 随即选择30条记录
	 * 
	 * @param cond
	 * @return
	 */
	public List selectAskListbyRandom() {
		DbOperation db = new DbOperation(5);
		try {
			// ResultSet rs = db
			// .executeQuery("select t1.id,question,answer1,answer2,answer3,answer4,rightanswers,state from fm_game_ask as t1"
			// +
			// " join (select round(rand()*((select max(id) from fm_game_ask)-(select min(id) from fm_game_ask))+(select min(id) from fm_game_ask)) as id) as t2"
			// + " where t1.id>=t2.id order by t1.id limit 30");
			ResultSet rs = db
					.executeQuery("select id,question,answer1,answer2,answer3,answer4,rightanswers,state from fm_game_ask limit 30");
			List list = new ArrayList();
			while (rs.next()) {
				AskBean ask = new AskBean();
				ask.setId(rs.getInt(1));
				ask.setQuestion(rs.getString(2));
				ask.setAnswer1(rs.getString(3));
				ask.setAnswer2(rs.getString(4));
				ask.setAnswer3(rs.getString(5));
				ask.setAnswer4(rs.getString(6));
				ask.setRightanswers(rs.getInt(7));
				ask.setState(rs.getInt(8));
				list.add(ask);
			}
			if (list.size() < 30) {
				return null;
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到list
	 * 
	 * @return
	 */
	public List selectAskList(int StartIndex, int CountPerPage) {
		DbOperation db = new DbOperation(5);
		try {
			ResultSet rs = db
					.executeQuery("select id,question,answer1,answer2,answer3,answer4,rightanswers,state from fm_game_ask order by id desc limit "
							+ StartIndex + "," + CountPerPage);
			List list = new ArrayList();
			while (rs.next()) {
				AskBean ask = new AskBean();
				ask.setId(rs.getInt(1));
				ask.setQuestion(rs.getString(2));
				ask.setAnswer1(rs.getString(3));
				ask.setAnswer2(rs.getString(4));
				ask.setAnswer3(rs.getString(5));
				ask.setAnswer4(rs.getString(6));
				ask.setRightanswers(rs.getInt(7));
				ask.setState(rs.getInt(8));
				list.add(ask);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到AskBean
	 * 
	 * @return
	 */
	public AskBean selectAskBean(int askid) {
		DbOperation db = new DbOperation(5);
		try {
			ResultSet rs = db
					.executeQuery("select id,question,answer1,answer2,answer3,answer4,rightanswers,state from fm_game_ask where id="
							+ askid);
			if (rs.next()) {
				AskBean ask = new AskBean();
				ask.setId(rs.getInt(1));
				ask.setQuestion(rs.getString(2));
				ask.setAnswer1(rs.getString(3));
				ask.setAnswer2(rs.getString(4));
				ask.setAnswer3(rs.getString(5));
				ask.setAnswer4(rs.getString(6));
				ask.setRightanswers(rs.getInt(7));
				ask.setState(rs.getInt(8));
				return ask;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 添加问题
	 * 
	 * @param bean
	 * @return
	 */
	public boolean insertAskBean(AskBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_ask(question,answer1,answer2,answer3,answer4,rightanswers) values(?,?,?,?,?,?)";
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getQuestion());
			pstmt.setString(2, bean.getAnswer1());
			pstmt.setString(3, bean.getAnswer2());
			pstmt.setString(4, bean.getAnswer3());
			pstmt.setString(5, bean.getAnswer4());
			pstmt.setInt(6, bean.getRightanswers());
			return dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			dbOp.release();
		}
	}

	/**
	 * 修改问题
	 * 
	 * @param bean
	 * @return
	 */
	public boolean updateAskBean(AskBean bean) {
		DbOperation dbOp = new DbOperation(5);
		String query = "update fm_game_ask set question=?,answer1=?,answer2=?,answer3=?,answer4=?,rightanswers=? where id="
				+ bean.getId();
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getQuestion());
			pstmt.setString(2, bean.getAnswer1());
			pstmt.setString(3, bean.getAnswer2());
			pstmt.setString(4, bean.getAnswer3());
			pstmt.setString(5, bean.getAnswer4());
			pstmt.setInt(6, bean.getRightanswers());
			return dbOp.executePstmt();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			dbOp.release();
		}
	}

	/**
	 * 添加用户问答结果
	 * 
	 * @param id
	 * @param fmid
	 * @param right
	 * @param wrong
	 * @param score
	 * @return
	 */
	public boolean insertaskscore(int userid, int fmid, int mid, int right, int wrong, int score, int exploit) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_member(m_id,fid,uid,ask_right,ask_wrong,ask_score,contribution) values("
				+ mid + "," + fmid + "," + userid + "," + right + "," + wrong + "," + score + "," + exploit + ")";
		dbOp.executeUpdate(query);
		dbOp.release();
		return false;
	}

	/**
	 * 添加家族问答结果
	 * 
	 * @param mid
	 * @param fmid
	 * @param ask_score
	 * @param nnt
	 * @param top
	 * @param score
	 * @param prize
	 * @param game_num
	 */
	public void insertaskfmscore(int mid, int fmid, int ask_score, int nnt, int top, int score, long prize, int game_num) {
		DbOperation dbOp = new DbOperation(5);
		String query = "insert into fm_game_game(m_id,fid1,ask_score,num_total,rank,score,prize,game_point,game_type,hold_time) values("
				+ mid
				+ ","
				+ fmid
				+ ","
				+ ask_score
				+ ","
				+ nnt
				+ ","
				+ top
				+ ","
				+ score
				+ ","
				+ prize
				+ ","
				+ game_num + ",3,now())";
		dbOp.executeUpdate(query);
		dbOp.release();
	}

	/**
	 * 查询家族问答历史
	 * 
	 * @param fid
	 * @param getStartIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectAskHistoryList(int fid, int getStartIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		String query = "select a.id,a.create_time,ifnull(b.rank,-1) from fm_game_match a left join fm_game_game b on a.id=b.m_id and b.fid1="
				+ fid
				+ " where a.game_type=3 and a.state=2 order by id desc limit "
				+ getStartIndex
				+ ","
				+ countPerPage;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				GameHistoryBean bean = new GameHistoryBean();
				bean.setMid(rs.getInt(1));
				bean.setHoldTime(rs.getDate(2).getTime());
				bean.setRank(rs.getInt(3));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 得到本家族的一场比赛的问答记录
	 * 
	 * @param mid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectAskHistory(int mid, int startIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		String query = "select fid1,rank,num_total,ask_score,hold_time from fm_game_game where game_type=3 and m_id="
				+ mid + " order by rank limit " + startIndex + "," + countPerPage;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				GameHistoryBean bean = new GameHistoryBean();
				bean.setFid1(rs.getInt(1));
				bean.setRank(rs.getInt(2));
				bean.setNumTotal(rs.getInt(3));
				bean.setShipId(rs.getInt(4));// 问答积分
				bean.setHoldTime(rs.getDate(5).getTime());
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 获得本家族此场游戏记录
	 * 
	 * @param mid
	 * @param fmid
	 * @return
	 */
	public GameHistoryBean selectMyFmAskHistory(int mid, int fmid) {
		DbOperation db = new DbOperation(5);
		String query = "select fid1,rank,num_total,ask_score,hold_time,score,prize,game_point from fm_game_game where game_type=3 and m_id="
				+ mid + " and fid1=" + fmid;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				GameHistoryBean bean = new GameHistoryBean();
				bean.setFid1(rs.getInt(1));
				bean.setRank(rs.getInt(2));
				bean.setNumTotal(rs.getInt(3));
				bean.setShipId(rs.getInt(4));// 问答积分
				bean.setHoldTime(rs.getDate(5).getTime());
				bean.setScore(rs.getInt(6));
				bean.setPrize(rs.getLong(7));
				bean.setGamePoint(rs.getInt(8));
				bean.setMid(mid);
				return bean;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 获得本家族成员的积分
	 * 
	 * @param mid
	 * @param startIndex
	 * @param countPerPage
	 * @return
	 */
	public List selectGameMember(int mid, int fmid, int startIndex, int countPerPage) {
		DbOperation db = new DbOperation(5);
		String query = "select uid,ask_right,ask_wrong,ask_score from fm_game_member where m_id=" + mid + " and fid="
				+ fmid + " order by ask_score desc limit " + startIndex + "," + countPerPage;
		ResultSet rs = db.executeQuery(query);
		List list = new ArrayList();
		try {
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setUid(rs.getInt(1));
				bean.setAsk_right(rs.getInt(2));
				bean.setAsk_wrong(rs.getInt(3));
				bean.setAsk_score(rs.getInt(4));
				list.add(bean);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * 获得本家族此场游戏记录
	 * 
	 * @param mid
	 * @param fmid
	 * @return
	 */
	public MemberBean selectMyGameMember(int mid, int userid) {
		DbOperation db = new DbOperation(5);
		String query = "select uid,ask_right,ask_wrong,ask_score,contribution from fm_game_member where m_id=" + mid
				+ " and uid=" + userid;
		ResultSet rs = db.executeQuery(query);
		try {
			while (rs.next()) {
				MemberBean bean = new MemberBean();
				bean.setUid(rs.getInt(1));
				bean.setAsk_right(rs.getInt(2));
				bean.setAsk_wrong(rs.getInt(3));
				bean.setAsk_score(rs.getInt(4));
				bean.setContribution(rs.getInt(5));
				bean.setMid(mid);
				return bean;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			db.release();
		}
	}

	/**
	 * executeUpdate
	 * 
	 * @param sql
	 * @return
	 */
	public boolean executeUpdate(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * getIntResult
	 * 
	 * @param sql
	 * @return
	 */
	public int getIntResult(String sql) {
		DbOperation db = new DbOperation(5);
		int success;
		try {
			success = db.getIntResult(sql);
			return success;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			db.release();
		}
	}

}
