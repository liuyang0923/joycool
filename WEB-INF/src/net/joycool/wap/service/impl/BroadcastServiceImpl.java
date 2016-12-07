/*
 * Created on 2005-9-13
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.bean.AllyBean;
import net.joycool.wap.bean.broadcast.BroadcastBean;
import net.joycool.wap.service.infc.IBroadcastService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author Bomb
 *  
 */
public class BroadcastServiceImpl implements IBroadcastService {
	public List getBroadcast(int start, int limit) {
		List list = new ArrayList();
		BroadcastBean bean = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "select * from wap_broadcast_nba order by id desc limit ?,?";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return list;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        ResultSet rs = null;
       
        try {
			pstmt.setInt(1, start);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
            while (rs.next()) {
                bean = new BroadcastBean();
                bean.setId(rs.getInt("id"));
                bean.setMsg(rs.getString("msg"));
                bean.setBroadcaster(rs.getString("broadcaster"));
                bean.setTime(rs.getTime("time"));
                list.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
		return list;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#getBroadcast(int)
	 */
	public BroadcastBean getBroadcast(int id) {
		BroadcastBean bean = null;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "select * from wap_broadcast_nba where id=?";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return null;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        ResultSet rs = null;
       
        try {
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new BroadcastBean();
                bean.setId(rs.getInt("id"));
                bean.setMsg(rs.getString("msg"));
                bean.setBroadcaster(rs.getString("broadcaster"));
                bean.setTime(rs.getTime("time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
		return bean;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#deleteBroadcast(int, java.lang.String, java.lang.String)
	 */
	public void updateBroadcast(int id, String broadcaster, String msg) {
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "update wap_broadcast_nba set broadcaster=?,msg=? where id=?";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
        	pstmt.setString(1, broadcaster);
        	pstmt.setString(2, msg);
			pstmt.setInt(3, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbOp.executePstmt();

        dbOp.release();
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#deleteBroadcast(int)
	 */
	public void deleteBroadcast(int id) {
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "delete from wap_broadcast_nba where id=?";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
			pstmt.setInt(1, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbOp.executePstmt();

        dbOp.release();
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#getBroadcastNum()
	 */
	public int getBroadcastNum() {

        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "select count(*) from wap_broadcast_nba";
        int count = 0;
        
        try {
        	ResultSet rs = dbOp.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
		return count;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#addWapAlly(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean addWapAlly(String name, String title, String url, String contact) {
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "insert into wap_ally (name,title,url,contact) values(?,?,?,?)";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
       
        try {
			pstmt.setString(1, name);
			pstmt.setString(2, title);
			pstmt.setString(3, url);
			pstmt.setString(4, contact);
			pstmt.executeUpdate();

        } catch (Exception e) {
        	return false;
        } finally{
        	dbOp.release();
        }
        return true;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#getWapAlly(int)
	 */
	public AllyBean getWapAlly(String name) {
		AllyBean bean = null;

        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "select * from wap_ally where name=?";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return null;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        ResultSet rs = null;
       
        try {
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new AllyBean();
                bean.setTitle(rs.getString("title"));
                bean.setUrl(rs.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
		return bean;
	}

	/* (non-Javadoc)
	 * @see net.joycool.wap.service.infc.IBroadcastService#addBroadcast(java.lang.String, java.lang.String)
	 */
	public void addBroadcast(String broadcaster, String msg) {
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建查询语句
        String query = "insert into wap_broadcast_nba (broadcaster,msg,time) values(?,?,now())";
        
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
       
        try {
			pstmt.setString(1, broadcaster);
			pstmt.setString(2, msg);
			dbOp.executePstmt();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
	}




}
