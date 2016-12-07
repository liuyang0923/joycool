/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.service.infc.IWGameService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class WGameServiceImpl implements IWGameService {
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#addHistory(net.joycool.wap.bean.wgame.HistoryBean)
     */
    public boolean addHistory(HistoryBean history) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO wgame_history0 set user_id=?, log_date=now(), game_type=?, game_id=?, "
                + "win_count=?, draw_count=?, lose_count=?, money=?";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
           pstmt.setInt(1, history.getUserId());
           pstmt.setInt(2, history.getGameType());
           pstmt.setInt(3, history.getGameId());
           pstmt.setInt(4, history.getWinCount());
           pstmt.setInt(5, history.getDrawCount());
           pstmt.setInt(6, history.getLoseCount());
           pstmt.setLong(7, history.getMoney());
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
    public boolean deleteHistory(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM wgame_history0 WHERE " + condition;

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
    public HistoryBean getHistory(String condition) {
        HistoryBean history = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_history0";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
       
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                history = new HistoryBean();
                history.setId(rs.getInt("id"));
                history.setGameId(rs.getInt("game_id"));
                history.setGameType(rs.getInt("game_type"));
                history.setLogDate(rs.getString("log_date"));
                
                history.setDrawCount(rs.getInt("draw_count"));
                history.setWinCount(rs.getInt("win_count"));
                history.setLoseCount(rs.getInt("lose_count"));
                history.setMoney(rs.getLong("money"));
                
                history.setDrawTotal(rs.getInt("draw_total"));
                history.setWinTotal(rs.getInt("win_total"));
                history.setLoseTotal(rs.getInt("lose_total"));
                history.setMoneyTotal(rs.getLong("money_total"));
                
                history.setUserId(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return history;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#getHistoryCount(java.lang.String)
     */
    public int getHistoryCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT count(id) as c_id FROM wgame_history0";
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
    public Vector getHistoryList(String condition) {
        Vector historyList = new Vector();
        HistoryBean history = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_history0";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }             

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                history = new HistoryBean();
                history.setId(rs.getInt("id"));
                history.setGameId(rs.getInt("game_id"));
                history.setDrawCount(rs.getInt("draw_count"));
                history.setGameType(rs.getInt("game_type"));
                history.setLogDate(rs.getString("log_date"));
                history.setLoseCount(rs.getInt("lose_count"));
                history.setMoney(rs.getInt("money"));
                history.setUserId(rs.getInt("user_id"));
                history.setWinCount(rs.getInt("win_count"));
                historyList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return historyList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#updateHistory(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateHistory(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE wgame_history0 SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
}
