package jc.answer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.db.DbOperation;

public class HelpService {

	public boolean upd(String sql) {
		DbOperation db = new DbOperation(5);
		boolean success = db.executeUpdate(sql);
		db.release();
		return success;
	}

	/**
	 * 帖子库
	 * 
	 * @param cond
	 * @return
	 */
	public List get222ProblemList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from help_problem where "
				+ cond);
		try {
			while (rs.next()) {
				list.add(getProblem(rs));
			}
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
	 * @param pid
	 * @return
	 */
	public List get222AnswerList(String cond) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db.executeQuery("select * from help_answer where "
				+ cond);// del=0 order by id desc
		try {
			while (rs.next())
				list.add(getAnswer(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 我回复的帖子
	 * 
	 * @return
	 */
	public List getProblemList2(int uid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select a.* from help_problem a,(select distinct(p_id) as p_id from help_answer where user_id="
						+ uid
						+ ") b where a.id=b.p_id order by last_reply_time desc");
		try {
			while (rs.next())
				list.add(getProblem(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 根据id指定的帖子
	 * 
	 * @param pid
	 * @return
	 */
	public BeanProblem getProblem(int pid) {
		BeanProblem bp = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_problem where del=0 and id="
						+ pid);
		try {
			while (rs.next())
				bp = getProblem(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bp;
	}

	/**
	 * 用户最后帖子是否结束
	 * 
	 * @param uid
	 * @return
	 */
	public boolean isOvered(int uid) {
		BeanProblem bp = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_problem where del=0 and left(now(),10)>left(create_time,10) and user_id="
						+ uid + " order by id desc limit 1");
		try {
			if (rs.next())
				bp = getProblem(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		if (bp != null && bp.getIsOver() != 1)
			return false;
		else
			return true;
	}

	/**
	 * 最后发帖是否超过一天
	 * 
	 * @param uid
	 * @return
	 */
	public boolean IfCanPub(int uid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_problem where del=0 and left(now(),10)=left(create_time,10) and user_id="
						+ uid);
		try {
			if (rs.next())
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return true;
	}

	/**
	 * 获得具体回复
	 * 
	 * @param aid
	 * @return
	 */
	public BeanAnswer getAnswer(int aid) {
		BeanAnswer ba = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_answer where del=0 and id="
						+ aid);
		try {
			if (rs.next())
				ba = getAnswer(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return ba;
	}

	/**
	 * 所有的回复(前台)
	 * 
	 * @param pid
	 * @return
	 */
	public List getAnswerAll(int pid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select b.* from ((SELECT  id,p_id,a_cont,user_id,create_time,use_time,del,flag FROM help_answer where p_id="
						+ pid
						+ " and flag=1 and del=0)union(SELECT id,p_id,a_cont,user_id,create_time,'1970-01-01 00:00:00' as use_time,del,flag FROM help_answer where p_id="
						+ pid
						+ " and del=0 and flag=0)) b order by b.flag desc ,b.use_time desc,b.id desc");
		try {
			while (rs.next())
				list.add(getAnswer(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 回复是否存在
	 * 
	 * @param pid
	 * @param aid
	 * @return
	 */
	public boolean isAnswerExist(int pid, int aid) {
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_answer where del=0 and p_id="
						+ pid + " and id=" + aid);
		try {
			if (rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return false;
	}

	/**
	 * 帖子鸡蛋
	 * 
	 * @param pid
	 * @return
	 */
	public List getVoteList(int pid) {
		List list = new ArrayList();
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_vote where del=0 and p_id="
						+ pid + " order by id");
		try {
			while (rs.next())
				list.add(getVote(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return list;
	}

	/**
	 * 是否已经投票
	 * 
	 * @param pid
	 * @param uid
	 * @return
	 */
	public BeanVote isVote(int pid, int uid) {
		BeanVote bv = null;
		DbOperation db = new DbOperation(5);
		ResultSet rs = db
				.executeQuery("select * from help_vote where del=0 and p_id="
						+ pid + " and user_id=" + uid);
		try {
			if (rs.next())
				bv = getVote(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return bv;
	}

	BeanProblem getProblem(ResultSet rs) throws SQLException {
		BeanProblem bp = new BeanProblem();
		bp.setId(rs.getInt("id"));
		bp.setUid(rs.getInt("user_id"));
		bp.setIsSovle(rs.getInt("issolved"));
		bp.setIsOver(rs.getInt("isover"));
		bp.setNumView(rs.getInt("num_view"));
		bp.setNumReply(rs.getInt("num_reply"));
		bp.setNumEgg(rs.getInt("num_egg"));
		bp.setNumFlower(rs.getInt("num_flower"));
		bp.setDel(rs.getInt("del"));
		bp.setFlag(rs.getInt("flag"));
		bp.setPTitle(rs.getString("p_title"));
		bp.setPCont(rs.getString("p_cont"));
		bp.setPrize(rs.getString("prize"));
		bp.setCreateTime(rs.getTimestamp("create_time").getTime());
		bp.setLastReplyTime(rs.getTimestamp("last_reply_time").getTime());
		return bp;
	}

	BeanAnswer getAnswer(ResultSet rs) throws SQLException {
		BeanAnswer ba = new BeanAnswer();
		ba.setId(rs.getInt("id"));
		ba.setUid(rs.getInt("user_id"));
		ba.setPid(rs.getInt("p_id"));
		ba.setDel(rs.getInt("del"));
		ba.setFlag(rs.getInt("flag"));
		ba.setACont(rs.getString("a_cont"));
		ba.setCreateTime(rs.getTimestamp("create_time").getTime());
		ba.setUseTime(rs.getTimestamp("use_time").getTime());
		return ba;
	}

	BeanVote getVote(ResultSet rs) throws SQLException {
		BeanVote bv = new BeanVote();
		bv.setId(rs.getInt("id"));
		bv.setUid(rs.getInt("user_id"));
		bv.setPid(rs.getInt("p_id"));
		bv.setDel(rs.getInt("del"));
		bv.setFlag(rs.getInt("flag"));
		bv.setCreateTime(rs.getTimestamp("create_time").getTime());
		return bv;
	}
}
