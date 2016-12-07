package net.joycool.wap.action.wgame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.ShortcutBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;


public class TorchAction extends CustomAction {
	public static ICacheMap stuffCache = CacheManage.stuff;
	
	public static int cost = 5000000;		// 需要500万乐币
	public static ICacheMap userCache = CacheManage.addCache(new LinkedCacheMap(2000, true), "torchUser");
	
	UserBean loginUser = null;
	
	public TorchAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	public static TorchBean getTorch(int id) {
		return (TorchBean)getTorchMap().get(Integer.valueOf(id));
	}
	
	public static HashMap getTorchMap() {
		HashMap map = (HashMap) stuffCache.sgt("torchMap");
		if(map == null) {
			loadTorches();
			map = (HashMap) stuffCache.sgt("torchMap");
		}
		return map;
	}
	public static List getTorchList() {
		List list = (List) stuffCache.sgt("torchList");
		if(list == null) {
			loadTorches();
			list = (List) stuffCache.sgt("torchList");
		}
		return list;
	}
	
	public static void loadTorches() {
		synchronized(stuffCache) {
			HashMap map = new HashMap();
			List list = dbGetTorches();
			Iterator iter = list.iterator();
			while(iter.hasNext()) {
				TorchBean bean = (TorchBean)iter.next();
				map.put(Integer.valueOf(bean.getId()), bean);
			}
			stuffCache.put("torchMap", map);
			stuffCache.put("torchList", list);
		}
	}
	
	static List dbGetTorches() {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(4);

		String query = "SELECT * from torch order by id desc";

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(dbGetTorch(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	static TorchBean dbGetTorch(ResultSet rs) throws SQLException {
		TorchBean bean = new TorchBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setStartTime(rs.getTimestamp("start_time").getTime());
		bean.setLife(rs.getInt("life") * 60000l);
		bean.setUserCount(rs.getInt("user_count"));
		return bean;
	}

	// 传递火炬
	public void pass() {
		int userId = getParameterInt("u");
		if(userId == 0)
			return;
		int torchId = getParameterIntS("t");
		TorchBean torch = getTorch(torchId);
		if(torch == null) {
			tip("tip", "参数错误");
			return;
		}
		if(torch.isOver() && loginUser.getId() != 431) {
			tip("tip", "这个火炬已经到达指定时间,不能继续传递!");
			return;
		}
		synchronized(torch) {
			if(torch.getUserId() != loginUser.getId() && loginUser.getId() != 431) {		// 不是他的火炬
				tip("tip", "火炬已经成功传递!");
				return;
			}
			
			UserBean to = UserInfoUtil.getUser(userId);
			if(to == null) {
				tip("tip", "无法传递火炬!");
				return;
			}

			UserInfoUtil.updateUserCash(loginUser.getId(), -cost, UserCashAction.GAME, null);
			
			TorchUserBean toUser = getAddTorchUser(userId);
			synchronized(toUser) {
				if(!toUser.hadTorch(torchId)) {
					toUser.addTorch(torchId);
					SqlUtil.executeUpdate("update torch_user set torch_count=torch_count+1,torches="
							+ toUser.getTorches() + " where user_id=" + userId);
					
					SqlUtil.executeUpdate("update torch set user_count=user_count+1,user_id=" + userId + " where id=" + torchId, 4);
					torch.addUserCount();
				} else {
					SqlUtil.executeUpdate("update torch set user_id=" + userId + " where id=" + torchId, 4);
				}
			}
			if(!torch.isStart()) {
				torch.setStartTime(System.currentTimeMillis());
				SqlUtil.executeUpdate("update torch set start_time=now() where id=" + torchId, 4);
			}
			
			torch.add(userId);

			NoticeAction.sendNotice(userId, "你刚刚接到了" + torch.getName(), "", NoticeBean.GENERAL_NOTICE, "", "/wgame/torch/torch.jsp?t=" + torchId);
			tip("tip", "火炬已经成功传递!");
		}
	}

	// 判断某人是否拿过某个火炬
	public static boolean hadTorch(int userId, int torchId) {
		TorchUserBean user = getTorchUser(userId);
		if(user == null)
			return false;
		return user.hadTorch(torchId);
	}

	
	public TorchUserBean getTorchUser() {
		return getTorchUser(loginUser.getId());
	}
	static TorchUserBean nullUser = new TorchUserBean();
	public static TorchUserBean getTorchUser(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(userCache) {
			TorchUserBean user = (TorchUserBean)userCache.get(key);
			if(user == null) {
				DbOperation dbOp = new DbOperation(true);

				String query = "SELECT * from torch_user where user_id=" + userId;

				ResultSet rs = dbOp.executeQuery(query);
				try {
					if (rs.next()) {
						user = new TorchUserBean();
						user.setUserId(userId);
						user.setTorches(rs.getLong("torches"));
						user.setTorchCount(rs.getInt("torch_count"));
					} else
						user = nullUser;
				} catch (SQLException e) {
					e.printStackTrace();
				}

				dbOp.release();
				userCache.put(key, user);
			}
			if(user == nullUser)
				return null;
			return user;
		}
	}
	
	public static TorchUserBean getAddTorchUser(int userId) {
		synchronized(userCache) {
			TorchUserBean user = getTorchUser(userId);
			if(user != null)
				return user;
			
			Integer key = Integer.valueOf(userId);
			user = new TorchUserBean();

			SqlUtil.executeUpdate("insert into torch_user set user_id=" + userId);
			
			userCache.put(key, user);
			return user;
		}
	}
	
	public static void clearUserCache() {
		userCache.clear();
	}
	
	public static float getPoint(long torches) {
		float sum = 0;
		List ts = getTorchList();
		for(int i = 0;i < ts.size();i++) {
			TorchBean torch = (TorchBean)ts.get(i);
			if(TorchUserBean.hadTorch(torches, torch.getId()))
				sum += torch.getPoint();
		}
		return sum;
	}
	
	public static float getPoint(TorchUserBean user) {
		return getPoint(user.getTorches());
	}
	
	// 计算所有玩家的火炬指数，保存到数据库
	public static void calcAll() {
		List userList = new ArrayList();
		
		DbOperation dbOp = new DbOperation(true);

		ResultSet rs = dbOp.executeQuery("select user_id,torches from torch_user");
		try {
			while (rs.next()) {
				TorchUserBean user = new TorchUserBean();
				user.setUserId(rs.getInt("user_id"));
				user.setTorches(rs.getLong("torches"));
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for(int i = 0;i < userList.size();i++) {
			TorchUserBean user = (TorchUserBean)userList.get(i);
			dbOp.executeUpdate("update torch_user set point=" + getPoint(user) + " where user_id=" + user.getUserId());
		}
		
		dbOp.release();
	}
}
