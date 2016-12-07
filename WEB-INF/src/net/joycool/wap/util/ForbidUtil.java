/*
 * Created on 2007-7-14
 *
 */
package net.joycool.wap.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.*;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author 禁止词汇或者封禁用户权限等等相关功能
 * 包括负责封禁的监察列表
 *  forbid数据表中，同时保存禁止的userid和管理员名单
 */
public class ForbidUtil {
	public static ICacheMap forbidGroupCache = CacheManage.forbidGroup;
	
	public static class ForbidGroup {
		String name;
		HashMap map = new HashMap();
		public HashMap getMap() {
			return map;
		}
		public boolean isForbid(int value) {
			ForbidBean bean = (ForbidBean)map.get(new Integer(value));
			if(bean != null)
				return bean.getEndTime() > System.currentTimeMillis();
			return false;
		}
		public void setMap(HashMap map) {
			this.map = map;
		}
		public synchronized void addForbid(ForbidBean bean, int minutes) {		// 对value封禁minutes分钟
			if(isForbid(bean.getValue())) {
				SqlUtil.executeUpdate("delete from forbid where `group`='" + name + "' and value=" + bean.getValue());
			}
			long now = System.currentTimeMillis();
			bean.setStartTime(now);
			bean.setEndTime(now + 60000l * minutes);
			map.put(Integer.valueOf(bean.getValue()), bean);
			ForbidService.addForbid(name, bean, minutes);
		}
		public synchronized void deleteForbid(int value) {
			map.remove(Integer.valueOf(value));
			SqlUtil.executeUpdate("delete from forbid where `group`='" + name + "' and value=" + value);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ForbidBean getForbid(int value) {
			ForbidBean bean = (ForbidBean)map.get(new Integer(value));
			if(bean != null && bean.getEndTime() > System.currentTimeMillis())
				return bean;
			return null;
		}
		public synchronized List getForbidList() {
			return new ArrayList(map.values());
		}
	}
	
	public static class ForbidBean {
		int value;			// 封禁的用户id或者其他类型
		int operator;		// 操作人员，是谁封禁的
		long startTime;
		long endTime;
		String bak;
		int flag;	// 封禁的类型，暂时用于家族聊天室，0表示禁言，1表示踢出
		public ForbidBean() { }
		public ForbidBean(int value, int operator, String bak) {
			this.value = value;
			this.operator = operator;
			this.bak = bak;
		}
		public ForbidBean(int value, int operator, String bak, int flag) {
			this.value = value;
			this.operator = operator;
			this.bak = bak;
			this.flag = flag;
		}
		public String getBak() {
			return bak;
		}
		public void setBak(String bak) {
			this.bak = bak;
		}
		public long getEndTime() {
			return endTime;
		}
		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}
		public long getStartTime() {
			return startTime;
		}
		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public int getOperator() {
			return operator;
		}
		public void setOperator(int operator) {
			this.operator = operator;
		}
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		
	}

	public static Pattern banWord = Pattern.compile("鸡巴|[烂妈贱]逼|[干日操][你逼]|插穴|我[日操干]|乱伦|强奸|鸡巴|电做|文做|做爱|自慰|肉棒|奶子|肛交|激情|插你|精液|射了|射精|奸|姦|淫|乳|裸|色聊|色友|肏|嫩逼|高潮|浪逼|屁眼|龟头|硬帮帮|色迷迷|大白奶|阴唇|尽情射|肥臀|口交|白臀|芳唇吮|边插|春情|耸挺|大肉棍|阴茎|阴道|性经历|鸡吧|性交|鸡鸡|舔舔|作爱|造爱|阴精|穴|茓|屄|骚");
	
	public static Pattern banWord2 = Pattern.compile("操|逼|鸡|舔|插|射|插");

	public static String replaceTo = ","; // 把屏蔽词汇替换

	public static boolean isBanWord(String word) {
		return banWord.matcher(word).lookingAt();
	}

	public static String replaceBanWord(String word) {
		return banWord.matcher(word).replaceAll(replaceTo);
	}
   
   public static ForbidGroup getGroup(String groupName) {
	   synchronized(forbidGroupCache) {
		   ForbidGroup group = (ForbidGroup)forbidGroupCache.get(groupName);
		   if(group == null) {
			   group = new ForbidGroup();
			   group.setName(groupName);
			   group.setMap(ForbidService.getForbidMap("`group`='" + groupName + "' and end_time>now()"));
			   forbidGroupCache.put(groupName, group);
		   }
		   return group;
	   }
   }
   
   
   public static boolean isForbid(String groupName, int value) {
	   ForbidGroup group = getGroup(groupName);
	   
	   return group.isForbid(value);
   }
   public static ForbidBean getForbid(String groupName, int value) {
	   ForbidGroup group = getGroup(groupName);
	   
	   return group.getForbid(value);
   }
   public static void addForbid(String groupName, ForbidBean bean, int minutes, String admin) {
	   AdminAction.addForbidLog(groupName, bean.getValue(), bean.getOperator(), admin + ":" + bean.getBak(), minutes, 0);
	   ForbidGroup group = getGroup(groupName);
	   group.addForbid(bean, minutes);
   }
   public static void addForbid(String groupName, ForbidBean bean, int minutes) {
	   AdminAction.addForbidLog(groupName, bean.getValue(), bean.getOperator(), bean.getBak(), minutes, 0);
	   ForbidGroup group = getGroup(groupName);
	   group.addForbid(bean, minutes);
   }
   public static void deleteForbid(String groupName, int value) {
	   deleteForbid(groupName, value, 0);
   }
   public static void deleteForbid(String groupName, int value, int oper) {
	   AdminAction.addForbidLog(groupName, value, oper, "", 0, 1);
	   ForbidGroup group = getGroup(groupName);
	   group.deleteForbid(value);
   }
   public static List getForbidList(String groupName) {
	   ForbidGroup group = getGroup(groupName);
	   return group.getForbidList();
   }
   
   // 定时执行，清空过期数据
   public static void task() {
	   SqlUtil.executeUpdate("delete from forbid where end_time < now()");
	   forbidGroupCache.clear();
   }
   
   
   
   
   
   
   
   public static class ForbidService {

		private static ForbidBean getForbid(ResultSet rs) throws SQLException {
			ForbidBean bean = new ForbidBean();
			bean.setValue(rs.getInt("value"));
			bean.setOperator(rs.getInt("operator"));
			bean.setStartTime(rs.getTimestamp("start_time").getTime());
			bean.setEndTime(rs.getTimestamp("end_time").getTime());
			bean.setBak(rs.getString("bak"));
			return bean;
		}

		public static boolean addForbid(String group, ForbidBean bean, int minutes) {
			DbOperation dbOp = new DbOperation(true);
			String query = "insert into forbid set `group`=?,value=?,operator=?,bak=?,start_time=now(),end_time=date_add(now(),interval ? minute),flag=?";
			
			if (!dbOp.prepareStatement(query)) {
				dbOp.release();
				return false;
			}
			PreparedStatement pstmt = dbOp.getPStmt();
			try {
				pstmt.setString(1, group);
				pstmt.setInt(2, bean.getValue());
				pstmt.setInt(3, bean.getOperator());
				pstmt.setString(4, bean.getBak());
				pstmt.setInt(5, minutes);
				pstmt.setInt(6, bean.getFlag());
				pstmt.execute();
			} catch (SQLException e) {
				e.printStackTrace();
				dbOp.release();
				return false;
			}
			
			dbOp.release();
			return true;
		}

		public static HashMap getForbidMap(String cond) {
			HashMap map = new HashMap();
			DbOperation dbOp = new DbOperation(true);
	
			String query = "SELECT * from forbid WHERE " + cond;
	
			ResultSet rs = dbOp.executeQuery(query);
			try {
				while (rs.next()) {
					ForbidBean bean = getForbid(rs);
					map.put(Integer.valueOf(bean.getValue()), bean);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			dbOp.release();
			return map;
		}
		
	}
   
	public static class AdminUser {
		int userId;
		int flag;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public boolean isFlag(int is) {
			return (flag & (1 << is)) != 0;
		}
		public void addFlag(int flagValue) {
			this.flag |= (1 << flagValue);
		}
		public void deleteFlag(int flagValue) {
			this.flag &= ~(1 << flagValue);
		}
		public void addFlag(String flagS) {
			addFlag(flagStringToInt(flagS));
		}
		public void deleteFlag(String flagS) {
			deleteFlag(flagStringToInt(flagS));
		}
		public static int flagStringToInt(String flagS) {
			int i = 0;
			for(;i < adminString.length;i++)
				if(flagS.equals(adminString[i]))
					break;
			return i;
		}
	}
	// 监察相关数据
	public static String[] adminString = {"system", "admin", "chata", "foruma", "maila", "homea", "tonga", "teama", "infoa", "gamea"};
	public static HashMap adminMap = null;
	public static synchronized HashMap getAdminMap() {
		if(adminMap == null) {
			adminMap = new HashMap();
			for(int i = 0;i < adminString.length;i++) {
				List list = getForbidList(adminString[i]);
				for(int j = 0;j < list.size();j++) {
					ForbidBean f = (ForbidBean)list.get(j);
					Integer userIdKey = new Integer(f.getValue());
					
					AdminUser au = (AdminUser)adminMap.get(userIdKey);
					if(au == null) {
						au = new AdminUser();
						au.userId = userIdKey.intValue();
						adminMap.put(userIdKey, au);
					}
					au.addFlag(i);
				}
			}
		}
		return adminMap;
	}
	public static AdminUser getAdminUser(int id) {
		if(adminMap == null)
			getAdminMap();
		return (AdminUser)adminMap.get(new Integer(id));
	}
	// 添加并返回监察
	public static AdminUser getAddAdminUser(int id) {
		if(adminMap == null)
			getAdminMap();
		Integer key = new Integer(id);
		AdminUser au = (AdminUser)adminMap.get(key);
		if(au == null) {
			au = new AdminUser();
			au.userId = id;
			adminMap.put(key, au);
		}
		return au;
	}
}
