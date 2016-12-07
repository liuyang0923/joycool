/*
 * Created on 2005-5-21
 *
 */
package net.wxsj.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.joycool.wap.util.db.DbUtil;

/**
 * @author a
 *  
 */
public class DbOperation {
    private Connection conn;

    private Statement stmt;

    private PreparedStatement pstmt;

    private ResultSet rs;

    /**
     * 初始化。
     * 
     * @return
     */
    public boolean init() {
        if (conn == null) {
            conn = DbUtil.getConnection();
        }
        if (conn == null) {
            conn = DbUtil.getDirectConnection();
        }
        if (conn != null) {
            return true;
        }
        return false;
    }

    /**
     * 开始一次事务。
     * 
     * @return
     */
    public boolean startTransaction() {
        if (!init()) {
            return false;
        }
        try {
            conn.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 事务完成。
     * 
     * @return
     */
    public boolean commitTransaction() {
        if (!init()) {
            return false;
        }
        try {
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 初始化Statement。
     * 
     * @return
     */
    public boolean initStatement() {
        if (!init()) {
            return false;
        }
        if (stmt != null) {
            return true;
        }
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 执行查询，返回结果集。
     * 
     * @param query
     * @return
     */
    public ResultSet executeQuery(String query) {
        if (!initStatement()) {
            return null;
        }
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 执行更新，返回结果。
     * 
     * @param query
     * @return
     */
    public boolean executeUpdate(String query) {
        if (!initStatement()) {
            return false;
        }
        //System.out.println(query);
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 准备PreparedStatement。
     * 
     * @param query
     * @return
     */
    public boolean prepareStatement(String query) {
        //System.out.println(query);
        try {
            pstmt = conn.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public PreparedStatement getPStmt() {
        return pstmt;
    }

    public boolean setFetchSize(int size) {
        if (!initStatement()) {
            return false;
        }
        try {
            stmt.setFetchSize(size);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean executePstmt() {
        if (pstmt != null) {
            try {
                pstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 释放资源。
     */
    public void release() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源，但不释放数据库连接。
     */
    public void releaseWithoutConn() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPagingQuery(String query, int index, int count) {
        if (query == null) {
            return null;
        }

        if (index < 0) {
            index = 0;
        }

        String result = null;

        if (count == -1) {
            count = 99999999;
            result = query + " limit " + index + ", " + count;
        } else {
            result = query + " limit " + index + ", " + count;
        }

        return result;
    }
}
