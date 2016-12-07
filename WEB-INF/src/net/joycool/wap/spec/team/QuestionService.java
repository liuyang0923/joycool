package net.joycool.wap.spec.team;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author zhouj
 * @explain： 答题
 * @datetime:1007-10-24
 */
public class QuestionService {
	public List getQuestionList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from question WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getQuestion(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private QuestionBean getQuestion(ResultSet rs) throws SQLException {
		QuestionBean bean = new QuestionBean();
		bean.setId(rs.getInt("id"));
		bean.setTitle(rs.getString("title"));
		bean.setContent(rs.getString("content"));
		bean.setFlag(rs.getInt("flag"));
		bean.setAnswer(rs.getInt("answer"));
		return bean;
	}
	
	public QuestionSetBean getQuestionSet(String cond) {
		QuestionSetBean bean = null;
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from question_set WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getQuestionSet(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}

	private QuestionSetBean getQuestionSet(ResultSet rs) throws SQLException {
		QuestionSetBean bean = new QuestionSetBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setInfo(rs.getString("info"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setCount(rs.getInt("count"));
		bean.setFlag(rs.getInt("flag"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setReplyCount(rs.getInt("reply_count"));
		return bean;
	}
	
	public QuestionReplyBean getQuestionReply(String cond) {
		QuestionReplyBean bean = null;
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from question_reply WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getQuestionReply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	public List getQuestionReplyList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from question_reply WHERE " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getQuestionReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	private QuestionReplyBean getQuestionReply(ResultSet rs) throws SQLException {
		QuestionReplyBean bean = new QuestionReplyBean();
		bean.setId(rs.getInt("id"));
		bean.setAnswer(rs.getString("answer"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setSetId(rs.getInt("set_id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setScore(rs.getInt("score"));
		return bean;
	}
	
	// 添加或者更新question set
	public boolean updateQuestionSet(QuestionSetBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(3);
		String query = SqlUtil.modifySql(add, 
				"question_set set name=?,info=?,user_id=?,flag=?,create_time=now()", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getInfo());
			pstmt.setInt(3, bean.getUserId());
			pstmt.setInt(4, bean.getFlag());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	// 添加或者更新question reply 答题记录
	public boolean updateQuestionReply(QuestionReplyBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(3);
		String query = SqlUtil.modifySql(add, 
				"question_reply set set_id=?,score=?,user_id=?,answer=?,create_time=now()", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getSetId());
			pstmt.setInt(2, bean.getScore());
			pstmt.setInt(3, bean.getUserId());
			pstmt.setString(4, bean.getAnswer());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	// 添加或者更新question
	public boolean updateQuestion(QuestionBean bean, boolean add) {
		DbOperation dbOp = new DbOperation(3);
		String query = SqlUtil.modifySql(add, 
				"question set title=?,content=?,flag=?,set_id=?,answer=?", bean.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getFlag());
			pstmt.setInt(4, bean.getSetId());
			pstmt.setInt(5, bean.getAnswer());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		if(add && bean.getId() == 0)
			bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	
	// 获得简单的问题列表，只有表面数据，用于展示
	public List getQuestionSetList(String cond) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(3);

		String query = "SELECT * from question_set where " + cond;

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getQuestionSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
}
