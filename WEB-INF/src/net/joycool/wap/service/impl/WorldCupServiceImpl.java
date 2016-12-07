/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.wc.WcAnswerBean;
import net.joycool.wap.bean.wc.WcAnswerRecordBean;
import net.joycool.wap.bean.wc.WcQuestionBean;
import net.joycool.wap.service.infc.IWorldCupService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class WorldCupServiceImpl implements IWorldCupService {
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#addHistory(net.joycool.wap.bean.wgame.HistoryBean)
     */
    public boolean addQuestion(WcQuestionBean question) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO jc_wc_question set title=?, result=?, end_datetime=?, create_datetime=now()";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, question.getTitle());
            pstmt.setInt(2, question.getResult());
            //fanys 2006-06-15 start
            pstmt.setString(3, question.getEndDatetime2());
            //end fanys 2006-06-15 end
        } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        //执行
        dbOp.executePstmt();

        //释放资源
        dbOp.release();

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#deleteHistory(java.lang.String)
     */
    public boolean deleteQuestion(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM jc_wc_question WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistory(java.lang.String)
     */
    public WcQuestionBean getQuestion(String condition) {
        WcQuestionBean question = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_question";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                question = new WcQuestionBean();
                question.setId(rs.getInt("id"));
                question.setCreateDatetime(rs.getString("create_datetime"));
                question.setEndDatetime(rs.getString("end_datetime"));
                question.setResult(rs.getInt("result"));
                question.setTitle(rs.getString("title"));                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return question;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryCount(java.lang.String)
     */
    public int getQuestionCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT count(id) as c_id FROM jc_wc_question";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryList(java.lang.String)
     */
    public Vector getQuestionList(String condition) {
        Vector questionList = new Vector();
        WcQuestionBean question = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_question";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }        

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                question = new WcQuestionBean();
                question.setId(rs.getInt("id"));
                question.setCreateDatetime(rs.getString("create_datetime"));
                question.setEndDatetime(rs.getString("end_datetime"));
                question.setResult(rs.getInt("result"));
                question.setTitle(rs.getString("title")); 
                questionList.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();        

        return questionList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#updateHistory(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateQuestion(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE jc_wc_question SET " + set + " WHERE "
                + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#addHistory(net.joycool.wap.bean.wgame.HistoryBean)
     */
    public boolean addAnswer(WcAnswerBean answer) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO jc_wc_answer set title=?, question_id=?, money=?";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, answer.getTitle());
            pstmt.setInt(2, answer.getQuestionId());
            pstmt.setFloat(3, answer.getMoney());
        } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        //执行
        dbOp.executePstmt();

        //释放资源
        dbOp.release();

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#deleteHistory(java.lang.String)
     */
    public boolean deleteAnswer(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM jc_wc_answer WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistory(java.lang.String)
     */
    public WcAnswerBean getAnswer(String condition) {
        WcAnswerBean answer = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_answer";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                answer = new WcAnswerBean();
                answer.setId(rs.getInt("id"));
                answer.setTitle(rs.getString("title"));
                answer.setQuestionId(rs.getInt("question_id"));
                answer.setMoney(rs.getFloat("money"));                                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return answer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryCount(java.lang.String)
     */
    public int getAnswerCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT count(id) as c_id FROM jc_wc_answer";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryList(java.lang.String)
     */
    public Vector getAnswerList(String condition) {
        Vector answerList = new Vector();
        WcAnswerBean answer = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_answer";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }        

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                answer = new WcAnswerBean();                
                answer.setId(rs.getInt("id"));
                answer.setTitle(rs.getString("title"));
                answer.setQuestionId(rs.getInt("question_id"));
                answer.setMoney(rs.getFloat("money"));   
                answerList.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();        

        return answerList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#updateHistory(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateAnswer(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE jc_wc_answer SET " + set + " WHERE "
                + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#addHistory(net.joycool.wap.bean.wgame.HistoryBean)
     */
    public boolean addAnswerRecord(WcAnswerRecordBean answerRecord) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO jc_wc_answer_record set user_id=?, question_id=?, answer_id=?, wager=?, create_datetime=now()";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setInt(1, answerRecord.getUserId());
            pstmt.setInt(2, answerRecord.getQuestionId());
            pstmt.setInt(3, answerRecord.getAnswerId());
            pstmt.setInt(4, answerRecord.getWager());
        } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        //执行
        dbOp.executePstmt();

        //释放资源
        dbOp.release();

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#deleteHistory(java.lang.String)
     */
    public boolean deleteAnswerRecord(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM jc_wc_answer_record WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistory(java.lang.String)
     */
    public WcAnswerRecordBean getAnswerRecord(String condition) {
        WcAnswerRecordBean answerRecord = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_answer_record";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                answerRecord = new WcAnswerRecordBean();
                answerRecord.setId(rs.getInt("id"));                
                answerRecord.setQuestionId(rs.getInt("question_id"));
                answerRecord.setAnswerId(rs.getInt("answer_id"));
                answerRecord.setWager(rs.getInt("wager"));
                answerRecord.setUserId(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return answerRecord;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryCount(java.lang.String)
     */
    public int getAnswerRecordCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT count(id) as c_id FROM jc_wc_answer_record";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                count = rs.getInt("c_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryList(java.lang.String)
     */
    public Vector getAnswerRecordList(String condition) {
        Vector answerRecordList = new Vector();
        WcAnswerRecordBean answerRecord = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM jc_wc_answer_record";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }        

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                answerRecord = new WcAnswerRecordBean();                
                answerRecord.setId(rs.getInt("id"));                
                answerRecord.setQuestionId(rs.getInt("question_id"));
                answerRecord.setAnswerId(rs.getInt("answer_id"));
                answerRecord.setWager(rs.getInt("wager"));
                answerRecord.setUserId(rs.getInt("user_id"));
                answerRecordList.add(answerRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();        

        return answerRecordList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#updateHistory(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateAnswerRecord(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE jc_wc_answer_record SET " + set + " WHERE "
                + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
}
