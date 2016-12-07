/*
 * Created on 2005-12-30
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import net.joycool.wap.bean.pgame.PGameBean;
import net.joycool.wap.bean.pgame.PGameProviderBean;
import net.joycool.wap.bean.pgame.UserOrderBean;
import net.joycool.wap.service.infc.IPGameService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class PGameServiceImpl implements IPGameService {

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IPGameService#getPGame(java.lang.String)
     */
    public PGameBean getPGame(String condition) {
        PGameBean game = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM pgame";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                game = new PGameBean();
                game.setId(rs.getInt("id"));
                game.setKb(rs.getInt("kb"));
                game.setName(rs.getString("name"));
                game.setPicUrl(rs.getString("pic_url"));
                game.setProviderId(rs.getInt("provider_id"));
                game.setDescription(rs.getString("description"));
                game.setGameFileUrl(rs.getString("game_file_url"));
                game.setFitMobile(rs.getString("fit_mobile"));
                game.setDownloadSum(rs.getInt("download_sum"));
                game.setFree(rs.getInt("free"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return game;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IPGameService#getPGameList(java.lang.String)
     */
    public Vector getPGameList(String condition) {
        Vector gameList = new Vector();
        PGameBean game = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return gameList;
        }

        //查询语句
        String sql = "SELECT * FROM pgame";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return gameList;
        }

        //将结果保存
        try {
            while (rs.next()) {
                game = new PGameBean();
                game.setId(rs.getInt("id"));
                game.setKb(rs.getInt("kb"));
                game.setName(rs.getString("name"));
                game.setPicUrl(rs.getString("pic_url"));
                game.setProviderId(rs.getInt("provider_id"));
                game.setDescription(rs.getString("description"));
                game.setGameFileUrl(rs.getString("game_file_url"));
                game.setFitMobile(rs.getString("fit_mobile"));
                game.setDownloadSum(rs.getInt("download_sum"));
                game.setFree(rs.getInt("free"));
                gameList.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return gameList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IPGameService#updatePGame(java.lang.String,
     *      java.lang.String)
     */
    public boolean updatePGame(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE pgame SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IPGameService#getPGameCount(java.lang.String)
     */
    public int getPGameCount(String condition) {
        int count = 0;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "SELECT count(id) as c_id FROM pgame WHERE " + condition;

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
     * @see net.joycool.wap.service.infc.IPGameService#getProvider(java.lang.String)
     */
    public PGameProviderBean getProvider(String condition) {
        PGameProviderBean pp = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT id, name FROM pgame_provider";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                pp = new PGameProviderBean();
                pp.setId(rs.getInt("id"));
                pp.setName(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return pp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.IPGameService#addUserOrder(net.joycool.wap.bean.pgame.UserOrderBean)
     */
    public boolean addUserOrder(UserOrderBean userOrder) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO user_order("
                + "user_id, mid, service_id, order_datetime, status) "
                + " VALUES(?, ?, ?, now(), ?)";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setInt(1, userOrder.getUserId());
            pstmt.setString(2, userOrder.getMid());
            pstmt.setString(3, userOrder.getServiceId());
            pstmt.setInt(4, userOrder.getStatus());
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
     * @see net.joycool.wap.service.infc.IPGameService#getUserOrder(java.lang.String)
     */
    public UserOrderBean getUserOrder(String condition) {
        UserOrderBean userOrder = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM user_order";
        if (condition != null) {
            sql = sql + " WHERE " + condition;
        }
        if (sql.indexOf("LIMIT") == -1) {
            sql = sql + " LIMIT 0, 1";
        }

        //查询
        ResultSet rs = dbOp.executeQuery(sql);
        if (rs == null) {
            //释放资源
            dbOp.release();
            return null;
        }

        //将结果保存
        try {
            if (rs.next()) {
                userOrder = new UserOrderBean();
                userOrder.setId(rs.getInt("id"));
                userOrder.setMid(rs.getString("mid"));
                userOrder.setOrderDatetime(rs.getString("order_datetime"));
                userOrder.setServiceId(rs.getString("service_id"));
                userOrder.setStatus(rs.getInt("status"));
                userOrder.setUserId(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return userOrder;
    }
}
