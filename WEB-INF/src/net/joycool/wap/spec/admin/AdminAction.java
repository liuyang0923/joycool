/*
 * Created on 2008-2-1
 *
 */
package net.joycool.wap.spec.admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.db.DbOperation;


/**
 * 
 * 说明：管理员权限等操作
 */
public class AdminAction extends CustomAction {
    public static int ORDER_ADMIN = 1; //权限：订单管理

    public static int USER_ADMIN = 2; //权限：用户管理
    
	public AdminAction(HttpServletRequest request) {
		super(request);
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
    
    public static HashMap groupMap = null;
    public static AdminGroupBean nullGroup = new AdminGroupBean();
    static {
    	nullGroup.setName("无");
    	nullGroup.setBak("无");
    }
    public static void clearGroup() {
    	groupMap = null;
    }
    static int[] lock = new int[0];
    public static HashMap getGroupMap() {
    	if(groupMap == null) {
    		synchronized(lock) {
    			if(groupMap == null) {
    				groupMap = getGroupMapDB();
    			}
    		}
    	}
    	return groupMap;
    }
    public static AdminGroupBean getAdminGroup(int groupId) {
    	if(groupId <= 0)
    		return nullGroup;
    	HashMap map = getGroupMap();
    	if(map == null)
    		return nullGroup;
    	
		AdminGroupBean g = (AdminGroupBean)map.get(new Integer(groupId));
		if(g == null)
			return nullGroup;
		
		return g;
    }
    
    public static HashMap getGroupMapDB() {
    	HashMap map = new LinkedHashMap();
    	
        DbOperation dbOp = new DbOperation(0);
        ResultSet rs = null;

        rs = dbOp.executeQuery("select * from admin_group order by seq");

        try {
            while (rs.next()) {
            	AdminGroupBean g = new AdminGroupBean();
            	g.setId(rs.getInt("id"));
            	g.setFlag(rs.getLong("flag"));
            	g.setFlag2(rs.getLong("flag2"));
            	g.setFlag3(rs.getLong("flag3"));
            	g.setFlag4(rs.getLong("flag4"));
            	g.setFlag5(rs.getLong("flag5"));
            	g.setBak(rs.getString("bak"));
            	g.setName(rs.getString("name"));
            	g.setCatalog(rs.getInt("catalog"));
            	g.setSeq(rs.getInt("seq"));
            	g.setDept(rs.getInt("dept"));
            	map.put(new Integer(g.getId()), g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        
    	groupMap = map;
    	return map;
    }
    public static boolean updateUserGroup(AdminGroupBean g, boolean add) {
    	
		DbOperation dbOp = new DbOperation(0);
		String query = modifySql(add, 
				"admin_group set name=?,bak=?,flag=?,flag2=?,flag3=?,flag4=?,flag5=?", g.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, g.getName());
			pstmt.setString(2, g.getBak());
			pstmt.setLong(3, g.getFlag());
			pstmt.setLong(4, g.getFlag2());
			pstmt.setLong(5, g.getFlag3());
			pstmt.setLong(6, g.getFlag4());
			pstmt.setLong(7, g.getFlag5());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && g.getId() == 0)
			g.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
    }
    public static boolean updatePermission(PermissionBean g, boolean add) {
    	
		DbOperation dbOp = new DbOperation(0);
		String query = modifySql(add, 
				"admin_permission set name=?,bak=?,parent=?", g.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, g.getName());
			pstmt.setString(2, g.getBak());
			pstmt.setLong(3, g.getParent());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && g.getId() == 0)
			g.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
    }
    
    public static PermissionBean getPermission(int id) {
    	return getPermissionDB(id);
    }
    public static List getPermissionList(String cond) {
    	return getPermissionListDB(cond);
    }
    
    public static void addUserGroup(AdminGroupBean g) {
    	updateUserGroup(g, true);
    	HashMap map = getGroupMap();
    	synchronized(lock) {
    		map.put(new Integer(g.getId()), g);
    	}
    }
    
    public static List getPermissionListDB(String cond) {
    	List list = new ArrayList();
    	
        DbOperation dbOp = new DbOperation(0);
        ResultSet rs = null;

        rs = dbOp.executeQuery("select * from admin_permission where " + cond);

        try {
            while (rs.next()) {
            	list.add(getPermission(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        
    	return list;
    }
    public static PermissionBean getPermissionDB(int id) {
        DbOperation dbOp = new DbOperation(0);
        ResultSet rs = null;
        PermissionBean g = null;
        rs = dbOp.executeQuery("select * from admin_permission where id=" + id);

        try {
            if (rs.next()) {
            	g = getPermission(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        return g;
    }
    public static PermissionBean getPermission(ResultSet rs) throws SQLException {
    	PermissionBean g = new PermissionBean();
    	g.setId(rs.getInt("id"));
    	g.setParent(rs.getInt("parent"));
    	g.setSeq(rs.getInt("seq"));
    	g.setBak(rs.getString("bak"));
    	g.setName(rs.getString("name"));
    	return g;
    }
   
    public static AdminUserBean getAdminUser(String cond) {
        DbOperation dbOp = new DbOperation(0);
        ResultSet rs = null;
        rs = dbOp.executeQuery("select * from admin where " + cond);
        AdminUserBean bean = null;
        try {
            if (rs.next()) {
            	bean = getAdminUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        return bean;
    }
    public static List getAdminUserList(String cond) {
        DbOperation dbOp = new DbOperation(0);
        ResultSet rs = null;
        List list = new ArrayList();
        rs = dbOp.executeQuery("select * from admin where " + cond);

        try {
            while (rs.next()) {
            	list.add(getAdminUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbOp.release();
        return list;
    }
    public static AdminUserBean getAdminUser(ResultSet rs) throws SQLException {
    	AdminUserBean g = new AdminUserBean();
    	g.setId(rs.getInt("id"));
    	g.setGroupId(rs.getInt("group_id"));
    	g.setGroupId2(rs.getInt("group_id2"));
    	g.setGroupId3(rs.getInt("group_id3"));
    	g.setName(rs.getString("name"));
    	g.setPassword(rs.getString("password"));
    	return g;
    }
    
    public static boolean updateUserPermission(AdminUserBean g, boolean add) {
    	
		DbOperation dbOp = new DbOperation(0);
		String query = modifySql(add, 
				"admin set group_id=?,group_id2=?,name=?,group_id3=?", g.getId());
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, g.getGroupId());
			pstmt.setInt(2, g.getGroupId2());
			pstmt.setString(3, g.getName());
			pstmt.setInt(4, g.getGroupId3());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		if(add && g.getId() == 0)
			g.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
    }
    
    
	// 每个long存储60个状态flag
	public long[] getParameterFlag(String name, int count) {
		if(count <= 0 || count > 1024)
			return null;
		List list = getParameterIntList2(name);
		return getFlag(list, count);
	}
	public static long[] getFlag(List list, int count) {
		long[] flags = new long[count];
		for(int i = 0;i < list.size();i++) {
			int isf = ((Integer)list.get(i)).intValue();
			int c = isf / 60;
			if(c >= 0 && c < count)
				flags[c] |= ( 1l << (isf % 60));
		}
		return flags;
	}
	public List getParameterIntList2(String name) {
		List list = new ArrayList();
		String[] p = request.getParameterValues(name);
		if(p != null) {
			if(p.length == 1 && p[0].indexOf(';') != -1) {
				p = p[0].split(";");
				for(int i = 0;i < p.length;i++) {
					int v = StringUtil.toInt(p[i]);
					if(v >= 0)
						list.add(Integer.valueOf(v));
				}
			} else {
				for(int i = 0;i < p.length;i++) {
					int v = StringUtil.toInt(p[i]);
					if(v >= 0)
						list.add(Integer.valueOf(v));
				}
			}
		}
		return list;
	}
	
	public static boolean addForbidLog(String group, int userId, int operator, String bak, int time, int status) {
		DbOperation dbOp = new DbOperation(true);
		String query = "insert into forbid_log set `group`=?,user_id=?,operator=?,bak=?,create_time=now(),status=?,time=?";
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, group);
			pstmt.setInt(2, userId);
			pstmt.setInt(3, operator);
			pstmt.setString(4, bak);
			pstmt.setInt(5, status);
			pstmt.setInt(6, time);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		dbOp.release();
		return true;
	}
	
	public static boolean addUserLog(int userId, int type, String bak) {
		DbOperation dbOp = new DbOperation(true);
		String query = "insert into user_log set user_id=?,type=?,bak=?,create_time=now()";
		
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, type);
			pstmt.setString(3, bak);
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		dbOp.release();
		return true;
	}
}
