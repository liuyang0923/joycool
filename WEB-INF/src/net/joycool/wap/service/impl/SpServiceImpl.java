/*
 * Created on 2005-12-23
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.joycool.wap.bean.sp.HistoryBean;
import net.joycool.wap.bean.sp.OrderBean;
import net.joycool.wap.service.infc.ISpService;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 *  
 */
public class SpServiceImpl implements ISpService {

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.ISpService#addOrder(net.joycool.wap.bean.sp.OrderBean)
     */
    public boolean addOrder(OrderBean order) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO sp_order("
                + "mid, mobile, sp_service_id, service_id, order_datetime, status) "
                + " VALUES(?, ?, ?, ?, now(), ?)";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, order.getMid());
            pstmt.setString(2, order.getMobile());
            pstmt.setInt(3, order.getSpServiceId());
            pstmt.setString(4, order.getServiceId());
            pstmt.setInt(5, order.getStatus());
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
     * @see net.joycool.wap.service.infc.ISpService#addOrderByCount(net.joycool.wap.bean.sp.OrderBean)
     */
    public boolean addOrderByCount(OrderBean order) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO sp_order_by_count("
                + "mid, mobile, sp_service_id, service_id, order_datetime, status) "
                + " VALUES(?, ?, ?, ?, now(), ?)";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, order.getMid());
            pstmt.setString(2, order.getMobile());
            pstmt.setInt(3, order.getSpServiceId());
            pstmt.setString(4, order.getServiceId());
            pstmt.setInt(5, order.getStatus());
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
     * @see net.joycool.wap.service.infc.ISpService#udpateOrder(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateOrder(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE sp_order SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.ISpService#updateOrderByCount(java.lang.String,
     *      java.lang.String)
     */
    public boolean updateOrderByCount(String set, String condition) {
        boolean result;

        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        //构建更新语句
        String query = "UPDATE sp_order_by_count SET " + set + " WHERE " + condition;

        //执行更新
        result = dbOp.executeUpdate(query);

        //释放资源
        dbOp.release();

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.joycool.wap.service.infc.ISpService#addHistory(net.joycool.wap.bean.sp.HistoryBean)
     */
    public boolean addHistory(HistoryBean history) {
        //数据库操作类
        DbOperation dbOp = new DbOperation();
        dbOp.init();

        String query = "INSERT INTO sp_history("
                + "mid, oper_type, oper_datetime, service_id) "
                + " VALUES(?, ?, now(), ?)";

        //准备
        if (!dbOp.prepareStatement(query)) {
            dbOp.release();
            return false;
        }
        //传递参数
        PreparedStatement pstmt = dbOp.getPStmt();
        try {
            pstmt.setString(1, history.getMid());
            pstmt.setInt(2, history.getOperType());
            pstmt.setString(3, history.getServiceId());
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
    
    
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.ISpService#getOrder(java.lang.String)
     */
    public OrderBean getOrder(String condition) {
        OrderBean order = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_order";
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
                order = new OrderBean();
                order.setId(rs.getInt("id"));
                order.setMid(rs.getString("mid"));
                order.setMobile(rs.getString("mobile"));
                order.setOrderDatetime(rs.getString("order_datetime"));
                order.setServiceId(rs.getString("service_id"));
                order.setSpServiceId(rs.getInt("sp_service_id"));
                order.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return order;
    }
    /* (non-Javadoc)
     * @see net.joycool.wap.service.infc.ISpService#getOrderByCount(java.lang.String)
     */
    public OrderBean getOrderByCount(String condition) {
        OrderBean order = null;
        //数据操作类
        DbOperation dbOp = new DbOperation();
        //初始化
        if (!dbOp.init()) {
            return null;
        }

        //查询语句
        String sql = "SELECT * FROM sp_order_by_count";
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
                order = new OrderBean();
                order.setId(rs.getInt("id"));
                order.setMid(rs.getString("mid"));
                order.setMobile(rs.getString("mobile"));
                order.setOrderDatetime(rs.getString("order_datetime"));
                order.setServiceId(rs.getString("service_id"));
                order.setSpServiceId(rs.getInt("sp_service_id"));
                order.setStatus(rs.getInt("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //释放资源
        dbOp.release();
        //返回结果
        return order;
    }
}
