package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.tong.TongLocationBean;
import net.joycool.wap.bean.wgamefight.WGameFightBean;
import net.joycool.wap.bean.wgamefight.WGameFightUserBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.infc.IWGameFightService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;
import net.wxsj.util.StringUtil;

/** 
 * @author guip
 * @explain：
 * @datetime:2007-10-22 14:25:27
 */
public class WGameFightServiceImpl implements IWGameFightService{

	public int getWGameFightCount(String condition){
		int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM wgame_pk_fight WHERE " + condition;

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
	public WGameFightBean getWGameFight(String condition){
		WGameFightBean  fightBean = null;
		 //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk_fight";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }
        query = query + " LIMIT 0, 1";

        //查询
        ResultSet rs = dbOp.executeQuery(query);

        try {
            //结果不为空
            if (rs.next()) {
            	fightBean = new WGameFightBean();
            	fightBean.setId(rs.getInt("id"));
            	fightBean.setEndDatetime(rs.getString("end_datetime"));
            	fightBean.setLeftCardsStr(rs.getString("left_cards"));
            	fightBean.setLeftNickname(rs.getString("left_nickname"));
            	fightBean.setLeftUserId(rs.getInt("left_user_id"));
            	fightBean.setMark(rs.getInt("mark"));
            	fightBean.setRightCardsStr(rs.getString("right_cards"));
            	fightBean.setRightNickname(rs.getString("right_nickname"));
            	fightBean.setRightUserId(rs.getInt("right_user_id"));
            	fightBean.setStartDatetime(rs.getString("start_datetime"));
            	fightBean.setWager(rs.getInt("wager"));
            	fightBean.setWinUserId(rs.getInt("win_user_id"));
            	fightBean.setGameId(rs.getInt("game_id"));
            	fightBean.setContent(rs.getString("content"));
            	fightBean.setLeftViewed(rs.getString("left_viewed"));
            	fightBean.setRightViewed(rs.getString("right_viewed"));
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //释放资源
            dbOp.release();
            
		return fightBean;
	}
	public WGameFightUserBean getWGameFightUser(int userId, int groupId){
		String key = getUGKey(userId, groupId);
		WGameFightUserBean fightBean = (WGameFightUserBean) OsCacheUtil.get(
				key,
				OsCacheUtil.FIGHT_CACHE_GROUP,
				OsCacheUtil.FIGHT_FLUSH_PERIOD);
		
		if(fightBean==null){
		 //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String condition = "user_id=" + userId + " and group_id=" + groupId + " and mark=0";
        String query = "SELECT * FROM wgame_pkuser_fight where " + condition;

        //查询
        ResultSet rs = dbOp.executeQuery(query);
        try {
            //结果不为空
            while (rs.next()) {
            	fightBean = this.getFightUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
        //释放资源
        dbOp.release();
        OsCacheUtil.put(key, fightBean,
				OsCacheUtil.FIGHT_CACHE_GROUP);
		}
		
		return fightBean;
	}
	private String getUGKey(int userId, int groupId) {
		return String.valueOf(userId) + "-" + groupId;
	}
	public boolean deleteWGameFight(String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "DELETE FROM wgame_pk_fight WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }
	public Vector getWGameFightList(String condition){
		Vector fightList = new Vector();
		WGameFightBean  fightBean = null;
		 //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pk_fight";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }       

        //查询
        ResultSet rs = dbOp.executeQuery(query);
        try {
            //结果不为空
            while (rs.next()) {
            	fightBean = new WGameFightBean();
            	fightBean.setId(rs.getInt("id"));
            	fightBean.setEndDatetime(rs.getString("end_datetime"));
            	fightBean.setLeftCardsStr(rs.getString("left_cards"));
            	fightBean.setLeftNickname(rs.getString("left_nickname"));
            	fightBean.setLeftUserId(rs.getInt("left_user_id"));
            	fightBean.setMark(rs.getInt("mark"));
            	fightBean.setRightCardsStr(rs.getString("right_cards"));
            	fightBean.setRightNickname(rs.getString("right_nickname"));
            	fightBean.setRightUserId(rs.getInt("right_user_id"));
            	fightBean.setStartDatetime(rs.getString("start_datetime"));
            	fightBean.setWager(rs.getInt("wager"));
            	fightBean.setWinUserId(rs.getInt("win_user_id"));
            	fightBean.setGameId(rs.getInt("game_id"));
            	fightBean.setContent(rs.getString("content"));
            	fightList.add(fightBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();
            
		return fightList;
	}
	public Vector getWGameFightUserList(int userId,String condition){
		String key = getKey(userId);
		WGameFightUserBean  fightBean = null;
      //从缓存中取
		Vector fightList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FIGHT_CACHE_GROUP,
				OsCacheUtil.FIGHT_FLUSH_PERIOD);
		// 缓存中没有
		if (fightList == null) {
		fightList =new Vector();
		 //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "SELECT * FROM wgame_pkuser_fight";
        if (condition != null) {
            query = query + " WHERE " + condition;
        }       

        //查询
        ResultSet rs = dbOp.executeQuery(query);
        try {
            //结果不为空
            while (rs.next()) {
            	fightBean = new WGameFightUserBean();
            	fightBean.setId(rs.getInt("id"));
            	fightBean.setUserId(rs.getInt("user_id"));
            	fightBean.setGroupId(rs.getInt("group_id"));
            	fightBean.setContent(rs.getString("content"));
            	fightBean.setMark(rs.getInt("mark"));
            	fightList.add(fightBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //释放资源
        dbOp.release();
        //放到缓存中
		OsCacheUtil.put(key, fightList,
				OsCacheUtil.FIGHT_CACHE_GROUP);
		}
		return fightList;
	}
	 public boolean updateWGameFight(String set, String condition) {
	        boolean result;

	        //数据库操作类
	        DbOperation dbOp = new DbOperation();
	        dbOp.init();

	        //构建更新语句
	        String query = "UPDATE wgame_pk_fight SET " + set + " WHERE " + condition;

	        //执行更新
	        result = dbOp.executeUpdate(query);
	        //释放资源
	        dbOp.release();
	        return result;
	 }
	 public boolean updateWGameFightUser(String set, String condition) {
	        boolean result;

	        //数据库操作类
	        DbOperation dbOp = new DbOperation();
	        dbOp.init();

	        //构建更新语句
	        String query = "UPDATE wgame_pkuser_fight SET " + set + " WHERE " + condition;

	        //执行更新
	        result = dbOp.executeUpdate(query);
	        //释放资源
	        dbOp.release();
	        return result;
	 }
	    public boolean addWGameFight(WGameFightBean fight) {
	        //数据库操作类
	        DbOperation dbOp = new DbOperation();
	        dbOp.init();

	        String query = "INSERT INTO wgame_pk_fight SET left_user_id=?, "
	                + "left_nickname=?, left_cards=?, "
	                + "right_user_id=?, right_nickname=?,"
	                + "right_cards=?, content=?, wager=?, start_datetime=now(), mark=?, win_user_id=?, game_id=?,left_viewed=?";

	        //准备
	        if (!dbOp.prepareStatement(query)) {
	            dbOp.release();
	            return false;
	        }
	        //传递参数
	        PreparedStatement pstmt = dbOp.getPStmt();
	        try {
	            pstmt.setInt(1, fight.getLeftUserId());
	            pstmt.setString(2, fight.getLeftNickname());
	            pstmt.setString(3, fight.getLeftCardsStr());
	            pstmt.setInt(4, fight.getRightUserId());
	            pstmt.setString(5, fight.getRightNickname());
	            pstmt.setString(6, fight.getRightCardsStr());
	            pstmt.setString(7, fight.getContent());
	            pstmt.setLong(8, fight.getWager());
	            pstmt.setInt(9, fight.getMark());
	            pstmt.setInt(10, fight.getWinUserId());
	            pstmt.setInt(11, fight.getGameId());
	            pstmt.setString(12, fight.getLeftViewed());
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
	    public boolean addWGameFightUser(WGameFightUserBean fightUser)
	    {
//	    	数据库操作类
	        DbOperation dbOp = new DbOperation();
	        dbOp.init();

	        String query = "INSERT INTO wgame_pkuser_fight SET user_id=?, "
	                + "group_id=?, content=?";
	        //准备
	        if (!dbOp.prepareStatement(query)) {
	            dbOp.release();
	            return false;
	        }
	        //传递参数
	        PreparedStatement pstmt = dbOp.getPStmt();
	        try {
	            pstmt.setInt(1, fightUser.getUserId());
	            pstmt.setInt(2, fightUser.getGroupId());
	            pstmt.setString(3, fightUser.getContent());  
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
	    public String getKey(int userId) {
			return String.valueOf(userId);
		}
	    public void flushFightUserList(int userId) {
			String key = String.valueOf(userId);
			OsCacheUtil.flushGroup(OsCacheUtil.FIGHT_CACHE_GROUP, key);
		}
	    public void flushFightUser(int userId, int groupId) {
			OsCacheUtil.flushGroup(OsCacheUtil.FIGHT_CACHE_GROUP, getUGKey(userId, groupId));
		}
	    private WGameFightUserBean getFightUser(ResultSet rs) throws SQLException {
	    	WGameFightUserBean fightBean = new WGameFightUserBean();
        	fightBean.setId(rs.getInt("id"));
        	fightBean.setUserId(rs.getInt("user_id"));
        	fightBean.setGroupId(rs.getInt("group_id"));
        	fightBean.setContent(rs.getString("content"));
        	fightBean.setMark(rs.getInt("mark"));
        	return fightBean;
	    }
}
