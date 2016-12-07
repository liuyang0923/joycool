package net.joycool.wap.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.util.db.DbOperation;

/**
 * fanys 2006-07-06
 * 
 * @author Administrator
 * 
 */
public class TestServiceImpl implements ITestService {
	public TestServiceImpl() {

	}

	public AnswerBean getAnswer(String condition) {
		String strsql = "select * from jc_test_answer where " + condition;
		AnswerBean answer = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		try {
			ResultSet rs = dbOp.executeQuery(strsql);
			if (rs.next()) {
				answer = getAnswerBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return answer;
	}

	public QuestionBean getQuestion(String condition) {
		String strsql = "select * from jc_test_question where " + condition;
		QuestionBean question = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		try {
			ResultSet rs = dbOp.executeQuery(strsql);
			if (rs.next()) {
				question = getQuestionBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return question;
	}

	public RecordBean getRecord(String condition) {
		String strsql = "select * from jc_test_record where " + condition;
		RecordBean record = null;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		try {
			ResultSet rs = dbOp.executeQuery(strsql);
			if (rs.next()) {
				record = getRecordBean(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return record;
	}

	public boolean addRecord(RecordBean record) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = " insert into jc_test_record(user_id,question_id,answer_id,answer_datetime,mark) values (?,?,?,now(),?)";
		if (!dbOp.prepareStatement(strsql)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, record.getUserId());
			pstmt.setInt(2, record.getQuestionId());
			pstmt.setInt(3, record.getAnswerId());
			pstmt.setInt(4, record.getMark());
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

	public Vector getAnswerList(String condition) {
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = "select * from jc_test_answer where " + condition
				+ " order by id ";
		ResultSet rs = dbOp.executeQuery(strsql);
		Vector vecAnswerList = new Vector();
		try {
			AnswerBean answer = null;
			while (rs.next()) {
				answer = getAnswerBean(rs);
				vecAnswerList.add(answer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return vecAnswerList;
	}

	private AnswerBean getAnswerBean(ResultSet rs) throws SQLException {
		AnswerBean answer = new AnswerBean();
		answer.setId(rs.getInt(1));
		answer.setQuestionId(rs.getInt(2));
		answer.setAnswer(rs.getString(3));
		answer.setNextQuestionId(rs.getInt(4));
		return answer;

	}

	private QuestionBean getQuestionBean(ResultSet rs) throws SQLException {
		QuestionBean question = new QuestionBean();
		question.setId(rs.getInt(1));
		question.setQuestion(rs.getString(2));
		question.setQuestionId(rs.getInt(3));
		question.setMultiple(rs.getInt(4));
		return question;
	}

	private RecordBean getRecordBean(ResultSet rs) throws SQLException {
		RecordBean record = new RecordBean();
		record.setId(rs.getInt(1));
		record.setUserId(rs.getInt(2));
		record.setQuestionId(rs.getInt(3));
		record.setAnswerId(rs.getInt(4));
		record.setAnswerTime(rs.getString(5));
		record.setMark(rs.getInt(6));
		return record;
	}

	public int getQuestionCount(String condition) {
		int count = 0;
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String strsql = "select count(*) as c_id from jc_test_question where "
				+ condition;
		ResultSet rs = dbOp.executeQuery(strsql);
		try {
			while (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return count;
	}

}
