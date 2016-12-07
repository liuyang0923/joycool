package jc.news.nba;

import java.sql.*;
import java.util.*;
import net.joycool.wap.util.db.DbOperation;

public class NbaService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	public List getNumLiveEvent() {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select id from nba_live_event");
		try {
			while (rs.next())
				list.add(new Integer(rs.getInt("id")));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 赛事库
	 * 
	 * @param rid
	 * @return
	 */
	// static_value=2
	public List getMatchList(String cound) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from nba_match where " + cound);
		try {
			while (rs.next())
				list.add(getBeanMatch(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 直播库
	 * 
	 * @param rid
	 * @return
	 */
	// del=0 and match_id="+ mid + " order by create_time desc
	public List getLiveList(String cound) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_live where " + cound);
		try {
			while (rs.next())
				list.add(getBeanLive(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 新闻库
	 * 
	 * @param cound
	 * @return
	 */
	// order by create_time desc
	public List getNews(String cound) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_news where " + cound);
		try {
			while (rs.next())
				list.add(getBeanNews(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 排行库
	 * 
	 * @param cound
	 * @return
	 */
	// del=0 order by id desc
	public List getRank(String cound) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_rank where " + cound);
		try {
			while (rs.next())
				list.add(getBeanRank(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 回复库
	 * 
	 * @param cound
	 * @return
	 */
	// del=0 order by id desc
	public List getReplyList(String cound) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from nba_reply where " + cound);
		try {
			while (rs.next())
				list.add(getBeanReply(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	public BeanMatch getMchById(int mid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_match where id="
				+ mid);
		try {
			while (rs.next())
				return getBeanMatch(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return null;
	}

	public BeanNews getTheNew(int id) {
		BeanNews bn = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_news where id=" + id);
		try {
			if (rs.next())
				bn = getBeanNews(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bn;
	}

	public BeanReply getReply(int rid) {
		BeanReply br = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from nba_reply where id="
				+ rid + " order by id desc");
		try {
			if (rs.next())
				br = getBeanReply(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return br;
	}

	public BeanSupport getBeanSuport(int uid, int mid) {
		BeanSupport bs = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from nba_support where user_id=" + uid
						+ " and match_id=" + mid);
		try {
			if (rs.next())
				bs = getBeanSupport(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bs;
	}

	BeanLive getBeanLive(ResultSet rs) throws SQLException {
		BeanLive bl = new BeanLive();
		bl.setId(rs.getInt("id"));
		bl.setDel(rs.getInt("del"));
		bl.setFlag(rs.getInt("flag"));
		bl.setMatchId(rs.getInt("match_id"));
		bl.setCont(rs.getString("cont"));
		bl.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bl;
	}

	BeanMatch getBeanMatch(ResultSet rs) throws SQLException {
		BeanMatch bm = new BeanMatch();
		bm.setId(rs.getInt("id"));
		bm.setFlag(rs.getInt("flag"));
		bm.setDel(rs.getInt("del"));
		bm.setPart(rs.getInt("part"));
		bm.setSumLive(rs.getInt("sum_live"));
		bm.setSumReply(rs.getInt("sum_reply"));
		bm.setSupport1(rs.getInt("support_1"));
		bm.setSupport2(rs.getInt("support_2"));
		bm.setStaticValue(rs.getInt("static_value"));
		bm.setCode(rs.getString("code"));
		bm.setTeam1(rs.getString("team1"));
		bm.setTeam2(rs.getString("team2"));
		bm.setCreateTime(rs.getTimestamp("create_time").getTime());
		bm.setStartTime(rs.getTimestamp("start_time").getTime());
		return bm;
	}

	BeanNews getBeanNews(ResultSet rs) throws SQLException {
		BeanNews bn = new BeanNews();
		bn.setId(rs.getInt("id"));
		bn.setFlag(rs.getInt("flag"));
		bn.setView(rs.getString("view"));
		bn.setName(rs.getString("name"));
		bn.setCont(rs.getString("cont"));
		bn.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bn;
	}

	BeanRank getBeanRank(ResultSet rs) throws SQLException {
		BeanRank br = new BeanRank();
		br.setId(rs.getInt("id"));
		br.setDel(rs.getInt("del"));
		br.setFlag(rs.getInt("flag"));
		br.setLoc(rs.getInt("loc"));
		br.setCont(rs.getString("cont"));
		br.setCreateTime(rs.getTimestamp("create_time").getTime());
		return br;
	}

	BeanReply getBeanReply(ResultSet rs) throws SQLException {
		BeanReply br = new BeanReply();
		br.setId(rs.getInt("id"));
		br.setMatchId(rs.getInt("match_id"));
		br.setUid(rs.getInt("user_id"));
		br.setCont(rs.getString("cont"));
		br.setCreateTime(rs.getTimestamp("create_time").getTime());
		br.setDel(rs.getInt("del"));
		br.setFlag(rs.getInt("flag"));
		return br;
	}

	BeanSupport getBeanSupport(ResultSet rs) throws SQLException {
		BeanSupport bs = new BeanSupport();
		bs.setId(rs.getInt("id"));
		bs.setMatch_id(rs.getInt("match_id"));
		bs.setUser_id(rs.getInt("user_id"));
		bs.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bs;
	}

}
