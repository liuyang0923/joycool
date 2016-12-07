/*
 * Created on 2006-6-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.joycool.wap.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.util.db.DbUtil;

/**
 * @author q
 * 
 * 功能：数据库操作的工具类
 */
public class SqlUtil {
	public static ICacheMap intResultCache = CacheManage.intResult;
	public static ICacheMap intsResultCache = CacheManage.intsResult;
	
	public static String ROWNUM_TABLE = "tmp_rownum";
	public static String DBShortName = "mcool";
	
	public static String testServer;
	public static boolean isTest = false;
	public static void setTestServer(String test) {
		testServer = test;
		if(test != null)
			isTest = true;
	}

	public static int getLastInsertId(DbOperation dbOp, String table) {
		int id = 0;
		ResultSet rs = dbOp.executeQuery("select last_insert_id()");
		try {
			if (rs.next())
				id = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}
	
	public static int getLastInsertId(DbOperation dbOp) {
		int id = 0;
		ResultSet rs = dbOp.executeQuery("select last_insert_id()");
		try {
			if (rs.next())
				id = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	public static String getString(DbOperation dbOp, String sql) {
		String ret = null;

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} 

		return ret;
	}
	
	public static void execute(String sql, List params, String dbName)
			throws Exception {
		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		PreparedStatement st = null;
		try {
			dbOp.prepareStatement(sql);
			st = dbOp.getPStmt();

			if (params != null && params.size() > 0) {
				try {
					for (int i = 0; i < params.size(); i++) {
						st.setObject(i + 1, params.get(i));
					}
				} catch (Exception e) {
				}
			}

			st.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			dbOp.release();
		}
	}

	public static List getObjectList(String sql, List params, String dbName) {
		List ret = new Vector();

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			dbOp.prepareStatement(sql);
			st = dbOp.getPStmt();

			if (params != null && params.size() > 0) {
				try {
					for (int i = 0; i < params.size(); i++) {
						st.setObject(i + 1, params.get(i));
					}
				} catch (Exception e) {
				}
			}

			rs = st.executeQuery();
			while (rs.next()) {
				ret.add(rs.getObject(1));
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			dbOp.release();
		}

		return ret;
	}

	public static Object getObject(String sql, List params, String dbName) {
		Object ret = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			dbOp.prepareStatement(sql);
			st = dbOp.getPStmt();

			if (params != null && params.size() > 0) {
				try {
					for (int i = 0; i < params.size(); i++) {
						st.setObject(i + 1, params.get(i));
					}
				} catch (Exception e) {
				}
			}

			rs = st.executeQuery();
			if (rs.next()) {
				ret = rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
			dbOp.release();
		}

		return ret;
	}

	public static boolean executeUpdate(String sql, String dbName) {
		DbOperation dbOp = new DbOperation(dbName);

		boolean res = dbOp.executeUpdate(sql);
		
		dbOp.release();
		return res;
	}
	
	public static boolean executeUpdate(String sql) {
		DbOperation dbOp = new DbOperation(true);

		boolean res = dbOp.executeUpdate(sql);

		dbOp.release();
		return res;
	}
	public static boolean executeUpdate(String sql, int connId) {
		DbOperation dbOp = new DbOperation(connId);

		boolean res = dbOp.executeUpdate(sql);

		dbOp.release();
		return res;
	}

	public static String getStringResult(String sql, String dbName) {
		String ret = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static int getIntResult(String sql, String dbName) {
		int ret = -1;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static float getFloatResult(String sql, String dbName) {
		float ret = 0;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getFloat(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static long getLongResult(String sql, String dbName) {
		long ret = -1;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null && rs.next()) {
				ret = rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static List getIntList(String sql, String dbName) {
		List ret = null;
		DbOperation dbOp = new DbOperation(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				ret = new ArrayList();
				while (rs.next()) {
					ret.add(new Integer(rs.getInt(1)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}
	
	public static List getIntList(String sql) {
		List ret = null;
		DbOperation dbOp = new DbOperation(true);
		try {
			ret = dbOp.getIntList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		return ret;
	}
	public static List getIntList(String sql, int connId) {
		List ret = null;
		DbOperation dbOp = new DbOperation(connId);
		try {
			ret = dbOp.getIntList(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		return ret;
	}
	
	public static int getIntResult(String sql) {
		int ret = -1;
		DbOperation dbOp = new DbOperation(true);

		try {
			ret = dbOp.getIntResult(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		return ret;
	}
	public static int getIntResult(String sql, int connId) {
		int ret = -1;
		DbOperation dbOp = new DbOperation(connId);

		try {
			ret = dbOp.getIntResult(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}
		return ret;
	}
	
	public static int getIntResultCache(String sql, int period) {
		synchronized(intResultCache) {
			Integer ret = (Integer) intResultCache.get(sql);
			if (ret == null) {
				DbOperation dbOp = new DbOperation(true);
	
				ResultSet rs = null;
				try {
					rs = dbOp.executeQuery(sql);
					if (rs != null && rs.next()) {
						ret = Integer.valueOf(rs.getInt(1));
					}
				} catch (Exception e) {
					ret = Integer.valueOf(0);
					e.printStackTrace();
				} finally {
					dbOp.release();
				}
				
				intResultCache.put(sql, ret, period);
			}
			return ret.intValue();
		}
	}
	public static int getIntResultCache(String sql, int period, int connId) {
		synchronized(intResultCache) {
			Integer ret = (Integer) intResultCache.get(sql);
			if (ret == null) {
				DbOperation dbOp = new DbOperation(connId);
	
				ResultSet rs = null;
				try {
					rs = dbOp.executeQuery(sql);
					if (rs != null && rs.next()) {
						ret = Integer.valueOf(rs.getInt(1));
					}
				} catch (Exception e) {
					ret = Integer.valueOf(0);
					e.printStackTrace();
				} finally {
					dbOp.release();
				}
				
				intResultCache.put(sql, ret, period);
			}
			return ret.intValue();
		}
	}
	
	public static List getIntListCache(String sql, int period) {
		return getIntListCache(sql, period, 0);
	}
	public static List getIntListCache(String sql, int period, int connId) {
		List ret = null;
		synchronized(intsResultCache) {
			ret = (List)intsResultCache.get(sql);
			if(ret == null) {
				ret = new ArrayList();
				DbOperation dbOp = new DbOperation(connId);
		
				ResultSet rs = null;
				try {
					rs = dbOp.executeQuery(sql);
					while (rs.next()) {
						ret.add(new Integer(rs.getInt(1)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					dbOp.release();
				}
				intsResultCache.put(sql, ret, period);
			}
		}
		return ret;
	}
	
	public static List getObjectList(String sql) {
		List ret = new Vector();

		DbOperation dbOp = new DbOperation();
		dbOp.init();

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					ret.add(rs.getObject(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		return ret;
	}
	public static List getObjectList(String sql, int connId) {
		List ret = new ArrayList();

		DbOperation dbOp = new DbOperation(connId);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					ret.add(rs.getObject(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}
	
	public static List getObjectsList(String sql) {
		return getObjectsList(sql, 0);
	}
	public static List getObjectsList(String sql, int connId) {
		List ret = null;

		DbOperation dbOp = new DbOperation(connId);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				ret = new ArrayList();
				while (rs.next()) {
					int n = rs.getMetaData().getColumnCount();
					Object[] objs = new Object[n];
					for(int i = 0;i < n;i++)
						objs[i] = rs.getObject(i + 1);
					ret.add(objs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}
	
	public static List getIntsList(String sql) {
		return getIntsList(sql, 0);
	}
	public static List getIntsList(String sql, int connId) {
		List ret = null;

		DbOperation dbOp = new DbOperation(connId);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				ret = new ArrayList();
				while (rs.next()) {
					int n = rs.getMetaData().getColumnCount();
					int[] ints = new int[n];
					for(int i = 0;i < n;i++)
						ints[i] = rs.getInt(i + 1);
					ret.add(ints);
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static HashMap getMap(String sql, String dbName) {
		HashMap ret = null;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs != null) {
				ret = new HashMap();
				while (rs.next()) {
					ret.put(rs.getObject(1), "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			dbOp.release();
		}

		return ret;
	}

	/**
	 * 在一个事务里面执行sql语句
	 * 
	 * @param sqlString
	 * @param dbName
	 * @return
	 */
	public static boolean excuteWithinStraction(String[] sqlString,
			String dbName) {
		if (sqlString == null) {
			return false;
		}

		Connection con = null;
		int[] result = null;
		Statement st = null;
		boolean rs = false;
		try {
			con = DbUtil.getConnection();
			con.setAutoCommit(false);

			st = con.createStatement();
			for (int i = 0; i < sqlString.length; i++) {
				if ((sqlString[i] == null)
						|| (sqlString[i].trim().length() == 0)) {
					continue;
				}
				st.addBatch(sqlString[i]);
			}

			result = st.executeBatch();

			boolean onError = false;
			if (result != null) {
				for (int i = 0; i < result.length; i++) {
					if (result[i] < 0 && result[i] != Statement.SUCCESS_NO_INFO) {
						onError = true;
						break;
					}
				}
			}

			if (onError) {
				con.rollback();
			} else {
				con.commit();
				rs = true;
			}
		} catch (Exception e) {
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (Exception eee) {
			}
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (Exception e) {
				}
			}
			if (con != null) {
				try {
					con.setAutoCommit(true);
				} catch (Exception e) {
				}
				try {
					con.close();
				} catch (Exception e) {
				}
			}
		}
		return rs;
	}

	// /**
	// *
	// * @author macq
	// * @explain： 获取数据库最近记录id （注：该方法必须紧跟insert方法后，否则无效）
	// * @datetime:2007-3-8 14:12:53
	// * @param dbName
	// * 数据源名字
	// * @param table
	// * 数据库表的名字
	// * @return
	// * @return int
	// */
	// public static int getLastInsertId(String table, String dbName) {
	// DbOperation dbOp = new DbOperation();
	// dbOp.init(dbName);
	// int id = 0;
	// ResultSet rs = dbOp.executeQuery("select last_insert_id() from "
	// + table);
	// try {
	// if (rs.next())
	// id = rs.getInt(1);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// dbOp.release();
	// }
	//
	// return id;
	// }

	public static boolean exist(String sql, String dbName) {
		boolean ret = false;

		DbOperation dbOp = new DbOperation();
		dbOp.init(dbName);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs.next()) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}
	
	public static boolean exist(String sql) {
		return exist(sql, DBShortName);
	}
	public static boolean exist(String sql, int connId) {
		boolean ret = false;

		DbOperation dbOp = new DbOperation(connId);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery(sql);
			if (rs.next()) {
				ret = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static void createRownum(DbOperation dbOp, String sql) {

		removeRownum(dbOp);

		dbOp
				.executeUpdate("create temporary table "
						+ ROWNUM_TABLE
						+ " (rownum int AUTO_INCREMENT, id int, index using hash(rownum), index using hash(id))engine=memory select id from "
						+ sql);

	}
	
	public static int[] getNearId(String table, String where, int id, int connId) {
		int[] ret = new int[2];

		DbOperation dbOp = new DbOperation(connId);

		ResultSet rs = null;
		try {
			rs = dbOp.executeQuery("select id, previous, current, next from ( select @next as next, @next := current as current, previous, id from ( select @next := null ) as init, ( select @prev as previous, @prev := e.id as current, e.id from ( select @prev := null ) as init,"
					+ table + " as e " + where + " order by e.id) as a order by a.id desc ) as b where id=" + id);
			if (rs.next()) {
				ret[0] = rs.getInt(2);
				ret[1] = rs.getInt(4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbOp.release();
		}

		return ret;
	}

	public static void removeRownum(DbOperation dbOp) {

		dbOp.executeUpdate("drop temporary table if exists " + ROWNUM_TABLE);

	}

	public static int getRownumById(DbOperation dbOp, int id) {
		ResultSet rs = dbOp.executeQuery("select rownum from " + ROWNUM_TABLE
				+ " where id=" + id);

		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int getIdByRownum(DbOperation dbOp, int rownum) {
		ResultSet rs = dbOp.executeQuery("select id from " + ROWNUM_TABLE
				+ " where rownum=" + rownum);

		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static String modifySql(boolean add, String set, int id) {
		String query;
		if(add) {
			query = "insert into " + set;
			if(id > 0)
				query  += ",id=" + id;
		} else
			query = "update " + set + " where id=" + id;
		return query;
	}
}
