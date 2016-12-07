/*
 * Created on 2005-5-21
 *
 */
package net.joycool.wap.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.servlet.ServletContext;

/**
 * @author a
 *  
 */
public class DbOperation {
	public static int defaultCon = 0;		// 默认的连接名
	public static int smsCon = 1;		// 论坛数据库
	public static int forumCon = 2;		// 论坛数据库
	public static int chatCon = 3;		// 聊天数据库
	public static int dataCon = 4;		// 静态数据库，主要是桃花源数据
	public static int beaCon = 5;		// facebook
	
	public static String[] connDb = {"jdbc/mcool", "jdbc/sms", "jdbc/mcool", "jdbc/mcool", "jdbc/mcool", "jdbc/bea", "jdbc/bea", "jdbc/bea", "jdbc/bea", "jdbc/bea"};
	public static String testServer;
	
    private Connection conn;

    private Statement stmt;

    private PreparedStatement pstmt;

    private ResultSet rs;
    
    public DbOperation() {
    	
    }
    
    public DbOperation(boolean startInit) {
    	if(startInit)
    		init();
    }
    public DbOperation(int connId) {
    	conn = DbUtil.getConnection2(connDb[connId]);
    }
    public DbOperation(String databaseName) {
    	init(databaseName);
    }
    
    public DbOperation(int i,int j) {
    	try {
//          initContext = new InitialContext();
//          ctx = (Context) initContext.lookup("java:comp/env");
      	Class.forName("com.mysql.jdbc.Driver");
      	conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mcoolinvitation","root","123456");
      	
    	} catch (Exception e){
      	e.printStackTrace();
      }
    }
    /**
     * 初始化。
     * 
     * @return
     */
    public boolean init() {
        conn = DbUtil.getConnection();
//        if (conn == null) {
//            conn = DbUtil.getDirectConnection();
//        }
        if (conn != null) {
            return true;
        }
        return false;
    }
    
    /**
     * 初始化。
     * 
     * @return
     */
    public boolean init(String databaseName) {
        conn = DbUtil.getConnection(databaseName);
        
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
//    	if(query.indexOf("receive_message") == -1 && query.indexOf("wgame_") == -1 && query.indexOf("last_insert_id") == -1)
//    		System.out.println(query);
        if (!initStatement()) {
            return null;
        }
 //       System.out.println(query);
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
        	System.out.println(query);
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
//    	if(query.indexOf("receive_message") == -1 && query.indexOf("wgame_") == -1 && query.indexOf("last_insert_id") == -1 && query.indexOf("jc_room_online") == -1)
//    		System.out.println(query);
        if (!initStatement()) {
            return false;
        }
//        System.out.println(query);
        try {
        	//liuyi 2006-09-26 防止sql注入漏洞 start
        	query.replace('#','+');
        	//liuyi 2006-09-26 防止sql注入漏洞 end
            stmt.executeUpdate(query);
        } catch (SQLException e) {
        	System.out.println(query);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	public int getLastInsertId() {
		int id = 0;
		ResultSet rs = executeQuery("select last_insert_id()");
		try {
			if (rs.next())
				id = rs.getInt(1);
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

    /**
     * 准备PreparedStatement。
     * 
     * @param query
     * @return
     */
    public boolean prepareStatement(String query) {
//        System.out.println(query);
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
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxId(String tableName) {
        try {
            String query = "SELECT max(id) AS max_id FROM " + tableName;
            rs = executeQuery(query);
            if (rs.next()) {
                return rs.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMaxId(String fieldName, String tableName, String condition) {
        try {
            String query = "SELECT max(" + fieldName + ") AS max_id FROM " + tableName;
            if(condition != null){
                query += " WHERE " + condition;
            }
            rs = executeQuery(query);
            if (rs.next()) {
                return rs.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List getIntList(String sql) throws SQLException {
		rs = executeQuery(sql);
		if (rs != null) {
			List ret = new ArrayList();
			while (rs.next()) {
				ret.add(new Integer(rs.getInt(1)));
			}
			return ret;
		}
		return null;
    }
    
    public int getIntResult(String sql) throws SQLException {
		rs = executeQuery(sql);
		if (rs != null && rs.next()) {
			return rs.getInt(1);
		}
		return -1;
    }
    
    // 从数据库载入dbname
    public static void loadConfig(ServletContext context) {
    	
    	testServer = context.getInitParameter("testServer");
    	
    	DbOperation db = new DbOperation(true);
    	try {
    		ResultSet rs = db.executeQuery("select * from config limit 1");
    		if(rs.next()) {
	    		connDb[0] = rs.getString("db1");
	    		connDb[1] = rs.getString("db2");
	    		connDb[2] = rs.getString("db3");
	    		connDb[3] = rs.getString("db4");
	    		connDb[4] = rs.getString("db5");
	    		connDb[5] = rs.getString("db6");
	    		connDb[6] = rs.getString("db7");
	    		connDb[7] = rs.getString("db8");
	    		connDb[8] = rs.getString("db9");
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	db.release();
    }
    
    public static void modifyConfig(int i, String jndi) {
    	if(i <= 0 || i >= connDb.length)
    		return;
    	connDb[i] = jndi;
    	DbOperation db = new DbOperation(true);
    	try {
    		db.executeUpdate("update config set db" + (i+1) + "='" + jndi + "'");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	db.release();
    }
    // 判断连接状态是否正常
    public boolean isClosed() {
    	if(conn == null)
    		return true;
    	try {
			return conn.isClosed();
		} catch (SQLException e) {
			return true;
		}
    }
}
