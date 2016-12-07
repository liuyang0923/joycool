/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.wgame.WGamePKBean;
import net.joycool.wap.bean.wgame.big.HistoryBigBean;
import net.joycool.wap.bean.wgame.big.WGamePKBigBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IWGamePKService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class WGamePKServiceImpl implements IWGamePKService {

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#addPKDice(net.joycool.wap.bean.wgamepk.PKDiceBean)
     */
    public boolean addWGamePK(WGamePKBean pk) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO wgame_pk SET left_user_id=?, "
                + "left_nickname=?, left_cards=?, "
                + "right_user_id=?, right_nickname=?, "
                + "right_cards=?, wager=?, start_datetime=now(), mark=?, win_user_id=?, game_id=?";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setInt(1, pk.getLeftUserId());
            pstmt.setString(2, pk.getLeftNickname());
            pstmt.setString(3, pk.getLeftCardsStr());
            pstmt.setInt(4, pk.getRightUserId());
            pstmt.setString(5, pk.getRightNickname());
            pstmt.setString(6, pk.getRightCardsStr());
            pstmt.setInt(7, pk.getWager());
            pstmt.setInt(8, pk.getMark());
            pstmt.setInt(9, pk.getWinUserId());
            pstmt.setInt(10, pk.getGameId());
        } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        //执行
        dbOp.executePstmt();
        pk.setId(dbOp.getLastInsertId());

        //释放资源
        dbOp.release();
        //zhul 2006-10-20 当用户PK时增加用户的PK度 start
        if(pk.getLeftUserId()!=0) UserInfoUtil.addUserPKTotal(pk.getLeftUserId());
        if(pk.getRightUserId()!=0) UserInfoUtil.addUserPKTotal(pk.getRightUserId());
        //zhul 2006-10-20 当用户PK时增加用户的PK度 end
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#deletePKDice(java.lang.String)
     */
    public boolean deleteWGamePK(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM wgame_pk WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKDice(java.lang.String)
     */
    public WGamePKBean getWGamePK(String condition) {
        WGamePKBean pk = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                pk = new WGamePKBean();
                pk.setId(rs.getInt("id"));
                pk.setEndDatetime(rs.getString("end_datetime"));
                pk.setLeftCardsStr(rs.getString("left_cards"));
                pk.setLeftNickname(rs.getString("left_nickname"));
                pk.setLeftUserId(rs.getInt("left_user_id"));
                pk.setMark(rs.getInt("mark"));
                pk.setPkDatetime(rs.getString("pk_datetime"));
                pk.setRightCardsStr(rs.getString("right_cards"));
                pk.setRightNickname(rs.getString("right_nickname"));
                pk.setRightUserId(rs.getInt("right_user_id"));
                pk.setStartDatetime(rs.getString("start_datetime"));
                pk.setWager(rs.getInt("wager"));
                pk.setWinUserId(rs.getInt("win_user_id"));
                pk.setGameId(rs.getInt("game_id"));  
                pk.setRightViewed(rs.getInt("right_viewed"));
                pk.setLeftViewed(rs.getInt("left_viewed"));
                pk.setFlag(rs.getInt("flag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return pk;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKDice(java.lang.String)
     */
    public Vector getWGamePKList(String condition) {
        Vector pkList = new Vector();
        WGamePKBean pk = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }       

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                pk = new WGamePKBean();
                pk.setId(rs.getInt("id"));
                pk.setEndDatetime(rs.getString("end_datetime"));
                pk.setLeftCardsStr(rs.getString("left_cards"));
                pk.setLeftNickname(rs.getString("left_nickname"));
                pk.setLeftUserId(rs.getInt("left_user_id"));
                pk.setMark(rs.getInt("mark"));
                pk.setPkDatetime(rs.getString("pk_datetime"));
                pk.setRightCardsStr(rs.getString("right_cards"));
                pk.setRightNickname(rs.getString("right_nickname"));
                pk.setRightUserId(rs.getInt("right_user_id"));
                pk.setStartDatetime(rs.getString("start_datetime"));
                pk.setWager(rs.getInt("wager"));
                pk.setWinUserId(rs.getInt("win_user_id"));
                pk.setGameId(rs.getInt("game_id"));
                pk.setFlag(rs.getInt("flag"));
                pkList.add(pk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return pkList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#updatePKDice(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateWGamePK(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE wgame_pk SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();     
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKJsbCount(java.lang.String)
     */
    public int getWGamePKCount(String condition){
    	int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM wgame_pk WHERE " + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
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
     * @see net.joycool.wap.service.infc.IWGamePKService#addPKDice(net.joycool.wap.bean.wgamepk.PKDiceBean)
     */
    public boolean addWGamePKBig(WGamePKBigBean pk) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO wgame_pk_big SET left_user_id=?, "
                + "left_nickname=?, left_cards=?, "
                + "right_user_id=?, right_nickname=?,"
                + "right_cards=?, content=?, wager=?, start_datetime=now(), mark=?, win_user_id=?, game_id=?";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setInt(1, pk.getLeftUserId());
            pstmt.setString(2, pk.getLeftNickname());
            pstmt.setString(3, pk.getLeftCardsStr());
            pstmt.setInt(4, pk.getRightUserId());
            pstmt.setString(5, pk.getRightNickname());
            pstmt.setString(6, pk.getRightCardsStr());
            pstmt.setString(7, pk.getContent());
            pstmt.setLong(8, pk.getWager());
            pstmt.setInt(9, pk.getMark());
            pstmt.setInt(10, pk.getWinUserId());
            pstmt.setInt(11, pk.getGameId());
        } catch (SQLException e) {
            e.printStackTrace();
            dbOp.release();
            return false;
        }
        
        dbOp.executePstmt();
        pk.setId(dbOp.getLastInsertId());

        dbOp.release();
        return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#deletePKDice(java.lang.String)
     */
    public boolean deleteWGamePKBig(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM wgame_pk_big WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKDice(java.lang.String)
     */
    public WGamePKBigBean getWGamePKBig(String condition) {
        WGamePKBigBean pkBig = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk_big";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                pkBig = new WGamePKBigBean();
                pkBig.setId(rs.getInt("id"));
                pkBig.setEndDatetime(rs.getString("end_datetime"));
                pkBig.setLeftCardsStr(rs.getString("left_cards"));
                pkBig.setLeftNickname(rs.getString("left_nickname"));
                pkBig.setLeftUserId(rs.getInt("left_user_id"));
                pkBig.setContent(rs.getString("content"));
                pkBig.setMark(rs.getInt("mark"));
                pkBig.setPkDatetime(rs.getString("pk_datetime"));
                pkBig.setRightCardsStr(rs.getString("right_cards"));
                pkBig.setRightNickname(rs.getString("right_nickname"));
                pkBig.setRightUserId(rs.getInt("right_user_id"));
                pkBig.setStartDatetime(rs.getString("start_datetime"));
                pkBig.setWager(rs.getLong("wager"));
                pkBig.setWinUserId(rs.getInt("win_user_id"));
                pkBig.setGameId(rs.getInt("game_id"));  
                pkBig.setRightViewed(rs.getInt("right_viewed"));
                pkBig.setLeftViewed(rs.getInt("left_viewed"));
                pkBig.setFlag(rs.getInt("flag"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return pkBig;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKDice(java.lang.String)
     */
    public Vector getWGamePKBigList(String condition) {
        Vector pkBigList = new Vector();
        WGamePKBigBean pkBig = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk_big";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }       

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
            	pkBig = new WGamePKBigBean();
            	pkBig.setId(rs.getInt("id"));
            	pkBig.setEndDatetime(rs.getString("end_datetime"));
            	pkBig.setLeftCardsStr(rs.getString("left_cards"));
            	pkBig.setLeftNickname(rs.getString("left_nickname"));
            	pkBig.setLeftUserId(rs.getInt("left_user_id"));
            	pkBig.setContent(rs.getString("content"));
            	pkBig.setMark(rs.getInt("mark"));
            	pkBig.setPkDatetime(rs.getString("pk_datetime"));
            	pkBig.setRightCardsStr(rs.getString("right_cards"));
            	pkBig.setRightNickname(rs.getString("right_nickname"));
            	pkBig.setRightUserId(rs.getInt("right_user_id"));
            	pkBig.setStartDatetime(rs.getString("start_datetime"));
            	pkBig.setWager(rs.getLong("wager"));
            	pkBig.setWinUserId(rs.getInt("win_user_id"));
            	pkBig.setGameId(rs.getInt("game_id"));
                pkBig.setFlag(rs.getInt("flag"));
                pkBigList.add(pkBig);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return pkBigList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#updatePKDice(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateWGamePKBig(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE wgame_pk_big SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();
  
        return result;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGamePKService#getPKJsbCount(java.lang.String)
     */
    public int getWGamePKBigCount(String condition){
    	int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM wgame_pk_big WHERE " + condition;

        ResultSet rs = dbOp.executeQuery(query);

        try {
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
     * @see net.joycool.wap.service.infc.IWGameService#addHistory(net.joycool.wap.bean.wgame.HistoryBean)
     */
    public boolean addHistoryBig(HistoryBigBean history) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO wgame_history0_big set user_id=?, log_date=now(), game_type=?, game_id=?, "
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
    public boolean deleteHistoryBig(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM wgame_history0_big WHERE " + condition;

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
    public HistoryBigBean getHistoryBig(String condition) {
        HistoryBigBean history = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_history0_big";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
       
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
                history = new HistoryBigBean();
                history.setId(rs.getInt("id"));
                history.setGameId(rs.getInt("game_id"));
                history.setDrawCount(rs.getInt("draw_count"));
                history.setGameType(rs.getInt("game_type"));
                history.setLogDate(rs.getString("log_date"));
                history.setLoseCount(rs.getInt("lose_count"));
                history.setMoney(rs.getLong("money"));
                history.setUserId(rs.getInt("user_id"));
                history.setWinCount(rs.getInt("win_count"));
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
    public int getHistoryBigCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT count(id) as c_id FROM wgame_history0_big";
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
    public Vector getHistoryBigList(String condition) {
        Vector historyBigList = new Vector();
        HistoryBigBean history = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_history0_big";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }             

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
                history = new HistoryBigBean();
                history.setId(rs.getInt("id"));
                history.setGameId(rs.getInt("game_id"));
                history.setDrawCount(rs.getInt("draw_count"));
                history.setGameType(rs.getInt("game_type"));
                history.setLogDate(rs.getString("log_date"));
                history.setLoseCount(rs.getInt("lose_count"));
                history.setMoney(rs.getInt("money"));
                history.setUserId(rs.getInt("user_id"));
                history.setWinCount(rs.getInt("win_count"));
                historyBigList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();

        return historyBigList;
    }

    
    public Vector getWGamePKBigListByCache(String condition){
        //构建查询语句
        String query = "SELECT * FROM wgame_pk_big";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }            
		// lbj_2006-08-05_缓存_start
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			Vector pkBigList = (Vector) OsCacheUtil.get(key,
					OsCacheUtil.WGAME_PK_BIG_GROUP, OsCacheUtil.WGAME_PK_BIG_FLUSH_PERIOD);
			if (pkBigList != null) {
				return pkBigList;
			}
		}
		Vector pkBigList = new Vector();
        WGamePKBigBean pkBig = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            while (rs.next()) {
            	pkBig = new WGamePKBigBean();
            	pkBig.setId(rs.getInt("id"));
            	pkBig.setEndDatetime(rs.getString("end_datetime"));
            	pkBig.setLeftCardsStr(rs.getString("left_cards"));
            	pkBig.setLeftNickname(rs.getString("left_nickname"));
            	pkBig.setLeftUserId(rs.getInt("left_user_id"));
            	pkBig.setContent(rs.getString("content"));
            	pkBig.setMark(rs.getInt("mark"));
            	pkBig.setPkDatetime(rs.getString("pk_datetime"));
            	pkBig.setRightCardsStr(rs.getString("right_cards"));
            	pkBig.setRightNickname(rs.getString("right_nickname"));
            	pkBig.setRightUserId(rs.getInt("right_user_id"));
            	pkBig.setStartDatetime(rs.getString("start_datetime"));
            	pkBig.setWager(rs.getLong("wager"));
            	pkBig.setWinUserId(rs.getInt("win_user_id"));
            	pkBig.setGameId(rs.getInt("game_id"));
                pkBig.setFlag(rs.getInt("flag"));
                pkBigList.add(pkBig);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();
		if (OsCacheUtil.USE_CACHE) {
			String key = query;
			OsCacheUtil.put(key, pkBigList, OsCacheUtil.WGAME_PK_BIG_GROUP);
		}
        return pkBigList;
    }
    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IWGameService#updateHistory(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateHistoryBig(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE wgame_history0_big SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

	public boolean validate(UserBean loginUser, WGamePKBigBean pkBigBean, HttpServletRequest request) {
		if (pkBigBean == null) {
			request.setAttribute("tip", "该局已被取消");
			request.setAttribute("result", "failure");
			return false;

		}
		if (pkBigBean.getLeftUserId() == loginUser.getId()) {
			request.setAttribute("tip", "您不能挑战自己的庄");
			request.setAttribute("result", "failure");
			return false;

		}
		if (pkBigBean.getMark() == WGamePKBigBean.PK_MARK_END) {
			request.setAttribute("tip", "该局已经结束");
			request.setAttribute("result", "failure");
			return false;

		}
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		if (storeBean == null || storeBean.getMoney() < pkBigBean.getWager()) {
			request.setAttribute("tip", "您的乐币不够");
			request.setAttribute("result", "failure");
			return false;
		}
		request.setAttribute("result", "success");
		return true;
	}
}
