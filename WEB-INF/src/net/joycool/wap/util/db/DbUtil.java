/*
 * Created on 2005-5-21
 *
 */
package net.joycool.wap.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import net.joycool.wap.util.Constants;

/**
 * @author a
 *  
 */
public class DbUtil {
    public static Context initContext;
    static {
    	try {
    		ctx = (Context)new InitialContext().lookup("java:comp/env");
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    public static Context ctx;

    public static boolean hasInited = false;

    public static void init() {
        try {
//            initContext = new InitialContext();
//            ctx = (Context) initContext.lookup("java:comp/env");
        	DataSource ds = (DataSource) ctx.lookup("jdbc/mcool");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得数据库连接。
     * 
     * @return
     */
    public static Connection getConnection() {
        try {
//            initContext = new InitialContext();
//            ctx = (Context) initContext.lookup("java:comp/env");
        	DataSource ds = (DataSource) ctx.lookup("jdbc/mcool");
            Connection conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static DataSource getDataSource(String jndi) {
    	try {
			return (DataSource) ctx.lookup(jndi);
		} catch (NamingException e) {}
		return null;
    }
    public static boolean checkConnection(String jndi) {
        try {
        	DataSource ds = (DataSource) ctx.lookup(jndi);
            Connection conn = ds.getConnection();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Connection getConnection2(String jndi) {
        try {
        	DataSource ds = (DataSource) ctx.lookup(jndi);
            Connection conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Connection getConnection(String databaseName) {
        try {
//            initContext = new InitialContext();
//            ctx = (Context) initContext.lookup("java:comp/env");
        	DataSource ds = (DataSource) ctx.lookup("jdbc/" + databaseName);
            Connection conn = ds.getConnection();
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getDirectConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://"
                    + Constants.DBServer + ":3306/" + Constants.DB,
                    Constants.DBUser, Constants.DBPassword);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Connection getDirectConnection(String db, String user, String pw) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://" + db, user, pw);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
