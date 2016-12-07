package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.beginner.BeginnerHelpBean;
import net.joycool.wap.bean.beginner.BeginnerQuestionBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IBeginnerService;
import net.joycool.wap.util.db.DbOperation;

public class BeginnerServiceImpl implements IBeginnerService {
	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#addBeginnerQuestionBean(net.joycool.wap.bean.beginner.BeginnerQuestionBean)
	 */
	public boolean addBeginnerQuestion(BeginnerQuestionBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_beginner_question(name,key1,key2,key3,key4,key5,result) VALUES(?,?,?,?,?,?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getKey1());
			pstmt.setString(3, bean.getKey2());
			pstmt.setString(4, bean.getKey3());
			pstmt.setString(5, bean.getKey4());
			pstmt.setString(6, bean.getKey5());
			pstmt.setInt(7, bean.getResult());
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#delBeginnerQuestionBeane(java.lang.String)
	 */
	public boolean delBeginnerQuestion(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_beginner_question WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#getBeginnerQuestionBean(java.lang.String)
	 */
	public BeginnerQuestionBean getBeginnerQuestion(String condition) {
		String query = "SELECT * from jc_beginner_question";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			BeginnerQuestionBean beginnerQuestion = (BeginnerQuestionBean) OsCacheUtil
					.get(key, OsCacheUtil.BEGINNER_QUESTION_CACHE_GROUP,
							OsCacheUtil.BEGINNER_QUESTION_CACHE_FLUSH_PERIOD);
			if (beginnerQuestion != null) {
				return beginnerQuestion;
			}
		}

		BeginnerQuestionBean beginnerQuestion = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				beginnerQuestion = this.getBeginnerQuestion(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, beginnerQuestion,
					OsCacheUtil.BEGINNER_QUESTION_CACHE_GROUP);
		}
		return beginnerQuestion;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#getBeginnerQuestionBeanCount(java.lang.String)
	 */
	public int getBeginnerQuestionCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_beginner_question WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#getBeginnerQuestionBeanList(java.lang.String)
	 */
	public Vector getBeginnerQuestionList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_beginner_question";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector beginnerQuestionList = (Vector) OsCacheUtil
					.get(key, OsCacheUtil.BEGINNER_QUESTION_CACHE_GROUP,
							OsCacheUtil.BEGINNER_QUESTION_CACHE_FLUSH_PERIOD);
			if (beginnerQuestionList != null) {
				return beginnerQuestionList;
			}
		}
		Vector beginnerQuestionList = new Vector();
		BeginnerQuestionBean beginnerQuestion = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				beginnerQuestion = this.getBeginnerQuestion(rs);
				beginnerQuestionList.add(beginnerQuestion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, beginnerQuestionList,
					OsCacheUtil.BEGINNER_QUESTION_CACHE_GROUP);
		}
		return beginnerQuestionList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerQuestionService#updateBeginnerQuestionBean(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateBeginnerQuestion(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_beginner_question SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private BeginnerQuestionBean getBeginnerQuestion(ResultSet rs)
			throws SQLException {
		BeginnerQuestionBean beginnerQuestion = new BeginnerQuestionBean();
		beginnerQuestion.setId(rs.getInt("id"));
		beginnerQuestion.setName(rs.getString("name"));
		beginnerQuestion.setKey1(rs.getString("key1"));
		beginnerQuestion.setKey2(rs.getString("key2"));
		beginnerQuestion.setKey3(rs.getString("key3"));
		beginnerQuestion.setKey4(rs.getString("key4"));
		beginnerQuestion.setKey5(rs.getString("key5"));
		beginnerQuestion.setResult(rs.getInt("result"));
		return beginnerQuestion;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#addBeginnerHelp(net.joycool.wap.bean.beginner.BeginnerHelpBean)
	 */
	public boolean addBeginnerHelp(BeginnerHelpBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "INSERT INTO jc_beginner_help(user_id,send_count,receive_count,create_datetime) VALUES(?,?,?,now())";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
			pstmt.setInt(2, bean.getSendCount());
			pstmt.setInt(3, bean.getReceiveCount());
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#delBeginnerHelp(java.lang.String)
	 */
	public boolean delBeginnerHelp(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "DELETE FROM jc_beginner_help WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#getBeginnerHelp(java.lang.String)
	 */
	public BeginnerHelpBean getBeginnerHelp(String condition) {
		String query = "SELECT * from jc_beginner_help";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			BeginnerHelpBean beginnerHelp = (BeginnerHelpBean) OsCacheUtil
					.get(key, OsCacheUtil.BEGINNER_HELP_CACHE_GROUP,
							OsCacheUtil.BEGINNER_HELP_CACHE_FLUSH_PERIOD);
			if (beginnerHelp != null) {
				return beginnerHelp;
			}
		}

		BeginnerHelpBean beginnerHelp = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建查询语句

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				beginnerHelp = this.getBeginnerHelp(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, beginnerHelp,
					OsCacheUtil.BEGINNER_HELP_CACHE_GROUP);
		}
		return beginnerHelp;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#getBeginnerHelpCount(java.lang.String)
	 */
	public int getBeginnerHelpCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_beginner_help WHERE "
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

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#getBeginnerHelpList(java.lang.String)
	 */
	public Vector getBeginnerHelpList(String condition) {
		// 构建查询语句
		String query = "SELECT * from jc_beginner_help";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector beginnerHelpList = (Vector) OsCacheUtil
					.get(key, OsCacheUtil.BEGINNER_HELP_CACHE_GROUP,
							OsCacheUtil.BEGINNER_HELP_CACHE_FLUSH_PERIOD);
			if (beginnerHelpList != null) {
				return beginnerHelpList;
			}
		}
		Vector beginnerHelpList = new Vector();
		BeginnerHelpBean beginnerHelp = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				beginnerHelp = this.getBeginnerHelp(rs);
				beginnerHelpList.add(beginnerHelp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, beginnerHelpList,
					OsCacheUtil.BEGINNER_HELP_CACHE_GROUP);
		}
		return beginnerHelpList;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.joycool.wap.service.infc.IBeginnerService#updateBeginnerHelp(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateBeginnerHelp(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "UPDATE jc_beginner_help SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private BeginnerHelpBean getBeginnerHelp(ResultSet rs)
			throws SQLException {
		BeginnerHelpBean beginnerHelp = new BeginnerHelpBean();
		beginnerHelp.setId(rs.getInt("id"));
		beginnerHelp.setUserId(rs.getInt("user_id"));
		beginnerHelp.setSendCount(rs.getInt("send_count"));
		beginnerHelp.setReceiveCount(rs.getInt("receive_count"));
		beginnerHelp.setCreateDatetime(rs.getString("create_datetime"));
		return beginnerHelp;
	}
}
;