package net.joycool.wap.util;

import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomOnlineBean;
import net.joycool.wap.bean.chat.RoomManagerBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.Integer2;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;

public class RoomUtil {
	static ICacheMap roomOnlineCache = CacheManage.roomOnline;
	private static Hashtable roomList = new Hashtable();

	private static Vector authorityChangeList = null;

	private static RoomUserBean roomUser = null;

	private static RoomManagerBean roomManager = null;

	public static Object roomUserLock = new Object();

	private static IChatService chatService = ServiceFactory
			.createChatService();

	private static final int scanInterval = 1000 * 60 * 2;

	// 聊天室在线用户刷新时间
//	private static final int ROOM_USER_FLUSH_PERIOD = 60 * 60;
	
	public static RoomScanner scanner = new RoomScanner();

	public static class RoomScanner extends Thread {

		public void run() {
			while (true) {
				try {
					String sql = "update jc_room set current_online_count=(select count(distinct user_id) from jc_room_online  where jc_room_online.room_id=jc_room.id)";
					SqlUtil.executeUpdate(sql, Constants.DBShortName);
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
				try {
					Thread.sleep(scanInterval);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	// 记录房间被更改
	public static void addChangedRoom(int roomId) {
		// 直接清除缓存
		roomList.remove(new Integer(roomId));
	}

	// 取得一个房间
	public static JCRoomBean getRoomById(int roomId) {
		JCRoomBean room = (JCRoomBean) roomList.get(new Integer(roomId));
		if (room == null) {
			room = chatService.getJCRoom("id = " + roomId);
			if (room != null) {
				roomList.put(new Integer(roomId), room);
			}
		}
		return room;
	}

	// 记录用户权限被更改
	public static void addChangedAuthority(int userId, int roomId) {
		// 判断changeRoomIdList里是否已经有这个roomId，如果没有，加一个。
		if (authorityChangeList != null) {
			// 判断更改列表中是否存在该用户
			boolean flag = authorityChangeList.contains(userId + "_" + roomId);
			// 如果不存在添加
			if (!flag) {
				authorityChangeList.add(userId + "_" + roomId);
			}
			return;
		}
		// 添加用户到更改信息列表中
		authorityChangeList = new Vector();
		authorityChangeList.add(userId + "_" + roomId);
	}

	// 取得用户权限，判断用户是否被允许访问
	public static int getAuthority(int userId, int roomId,
			HttpServletRequest request) {
		// fanys 2006-09-16 start

		// fanys 2006-09-16 end
		// 初始化结果
		int num = 0;
		// 初始化roomBean
		JCRoomBean jcRoom = null;
		// 获取session
		HttpSession session = request.getSession();
		// 判断userId_roomId是否在更改列表里
		if (authorityChangeList != null) {
			// 判断更改列表中是否存在该用户
			boolean flag = authorityChangeList.contains(userId + "_" + roomId);
			// 如果authorityChangeList存在
			if (flag) {
				// 根据ID从数据库里取得Bean
				authorityChangeList.removeElement(userId + "_" + roomId);
				roomUser = chatService.getJCRoomUser("room_id = " + roomId
						+ " and user_id=" + userId);
				// 如果没有得到记录，判断房间是否可以随意进入
				if (roomUser == null) {
					// 获取房间信息
					jcRoom = RoomUtil.getRoomById(roomId);
					// 随意进入得房间
					if (jcRoom.getGrantMode() == 0) {
						session.setAttribute(getRoomKey(roomId), "true");
						num = 1;
					} else {
						// 需要授权得房间
						session.setAttribute(getRoomKey(roomId), "apply");
						num = 3;
					}
					// 被管理员踢出
				} else if (roomUser.getStatus() == 0) {
					session.setAttribute(getRoomKey(roomId), "kick");
					num = 0;
					// 可以进入得房间
				} else if (roomUser.getStatus() == 1) {
					session.setAttribute(getRoomKey(roomId), "true");
					num = 1;
					// 等待审批得房间
				} else if (roomUser.getStatus() == 2) {
					session.setAttribute(getRoomKey(roomId), "waitiing");
					num = 2;
				}
			} else {
				// 从session中取值
				String check = (String) session
						.getAttribute(getRoomKey(roomId));
				// 如果session中得值为空
				if (check == null) {
					// 根据ID从数据库里取得Bean，
					// changeRoomIdList.removeElement(new Integer(roomId));
					roomUser = chatService.getJCRoomUser("room_id = " + roomId
							+ " and user_id=" + userId);
					// 如果没有得到记录，判断房间是否可以随意进入
					if (roomUser == null) {
						// 获取房间信息
						jcRoom = RoomUtil.getRoomById(roomId);
						// 随意进入得房间
						if (jcRoom.getGrantMode() == 0) {
							session.setAttribute(getRoomKey(roomId), "true");
							num = 1;
						} else {
							// 需要授权得房间
							session.setAttribute(getRoomKey(roomId), "apply");
							num = 3;
						}
						// 被管理员踢出
					} else if (roomUser.getStatus() == 0) {
						session.setAttribute(getRoomKey(roomId), "kick");
						num = 0;
						// 可以进入得房间
					} else if (roomUser.getStatus() == 1) {
						session.setAttribute(getRoomKey(roomId), "true");
						num = 1;
						// 等待审批得房间
					} else if (roomUser.getStatus() == 2) {
						session.setAttribute(getRoomKey(roomId), "waitiing");
						num = 2;
					}
				} else {
					// 可以进入得房间
					if (check.equals("true")) {
						num = 1;
						// 被管理员踢出
					} else if (check.equals("kick")) {
						num = 0;
						// 等待审批得房间
					} else if (check.equals("apply")) {
						num = 3;
						// 等待审批得房间
					} else if (check.equals("waitiing")) {
						num = 2;
					}
				}
			}
		} else {
			// 从session中取值
			String flag = (String) session.getAttribute(getRoomKey(roomId));
			// 如果session中得值为空
			if (flag == null) {
				// 根据ID从数据库里取得Bean，
				// changeRoomIdList.removeElement(new Integer(roomId));
				roomUser = chatService.getJCRoomUser("room_id = " + roomId
						+ " and user_id=" + userId);
				// 如果没有得到记录，判断房间是否可以随意进入
				if (roomUser == null) {
					// 获取房间信息
					jcRoom = RoomUtil.getRoomById(roomId);
					// 随意进入得房间
					if (jcRoom.getGrantMode() == 0) {
						session.setAttribute(getRoomKey(roomId), "true");
						num = 1;
					} else {
						// 需要授权得房间
						session.setAttribute(getRoomKey(roomId), "apply");
						num = 3;
					}
					// 被管理员踢出
				} else if (roomUser.getStatus() == 0) {
					session.setAttribute(getRoomKey(roomId), "kick");
					num = 0;
					// 可以进入得房间
				} else if (roomUser.getStatus() == 1) {
					session.setAttribute(getRoomKey(roomId), "true");
					num = 1;
					// 等待审批得房间
				} else if (roomUser.getStatus() == 2) {
					session.setAttribute(getRoomKey(roomId), "waitiing");
					num = 2;
				}
			} else {
				// 可以进入得房间
				if (flag.equals("true")) {
					num = 1;
					// 被管理员踢出
				} else if (flag.equals("kick")) {
					num = 0;
					// 等待审批得房间
				} else if (flag.equals("apply")) {
					num = 3;
					// 等待审批得房间
				} else if (flag.equals("waitiing")) {
					num = 2;
				}
			}
		}
		return num;
	}

	// 判断用户是不是管理员
	public static int getManagerRank(int userId, int roomId,
			HttpServletRequest request) {
		int num = 0;
		HttpSession session = request.getSession();
		// 判断userId_roomId是否在更改列表里
		if (authorityChangeList != null) {
			boolean flag = authorityChangeList.contains(userId + "_" + roomId);
			// 如果changeRoomIdList存在
			if (flag) {
				// 根据ID从数据库里取得Bean
				authorityChangeList.removeElement(userId + "_" + roomId);
				roomManager = chatService.getJCRoomManager("room_id = "
						+ roomId + " and user_id=" + userId);
				// 如果不是管理员返回0
				if (roomManager == null) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(0));
					num = 0;
					// 如果等于1是超级管理员即房间创建者
				} else if (roomManager.getMark() == 1) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(2));
					num = 2;
					// 如果等于2是被授权得管理员
				} else if (roomManager != null) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(1));
					num = 1;
				}
			}
		} else {
			// 从sessio中取值
			String flag = (String) session
					.getAttribute(getRoomManagerKey(roomId));
			// session值为空
			if (flag == null) {
				// 根据ID从数据库里取得Bean
				// changeRoomIdList.removeElement(new Integer(roomId));
				roomManager = chatService.getJCRoomManager("room_id = "
						+ roomId + " and user_id=" + userId);
				// 如果不是管理员返回0
				if (roomManager == null) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(0));
					num = 0;
					// 如果等于1是超级管理员即房间创建者
				} else if (roomManager.getMark() == 1) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(2));
					num = 2;
					// 如果等于2是被授权得管理员
				} else if (roomManager != null) {
					session.setAttribute(getRoomManagerKey(roomId),
							new Integer(1));
					num = 1;
				}
			} else {
				// 如果不是管理员返回0
				if (0 == Integer.parseInt(flag)) {
					num = 0;
					// 如果等于2是被授权得管理员
				} else if (2 == Integer.parseInt(flag)) {
					num = 2;
					// 如果等于1是超级管理员即房间创建者
				} else {
					num = 1;
				}
			}
		}
		return num;
	}

	/**
	 * zhul_2006-09-05
	 * 
	 * @param roomId
	 * @return 显示人数
	 */
	public static int getDisplayNum(int roomId) {

		Random random = new Random();
		// 聊天室挂线人数
		int onlines = RoomUtil.getRoomById(roomId).getCurrentOnlineCount();	

		// 聊天室实际活动人数
		String sql = "select count(id) from jc_online_user where position_id="
				+ ModuleBean.CHAT + " and sub_id=" + roomId;
		int actives = SqlUtil.getIntResultCache(sql, 10);

		// 显示人数
		int displays = 0;

		if (actives >= 0 && actives <= 5) // 活动人数在0--5 如下显示
		{
			if (onlines > 0 && onlines <= 10) // 挂线人数在0-10
			{
				displays = onlines; // 显示人数等于挂线人数
			} else if (onlines > 10 && onlines <= 30) // 挂线人数在10--30
			{
				displays = Math.max(10 * actives + random.nextInt(6), onlines);// 在10倍活动人数和挂线人数中取大
			} else // 挂线人数>30
			{
				displays = onlines; // 显示人数等于挂线人数
			}
		} else if (actives > 5 && actives <= 25) // 活动人数5--25,如下显示
		{
			if (onlines <= 50) // 挂线人数<=50
			{
				displays = Math.max(5 * actives, 3 * onlines); // 取5倍活动和3倍挂线中大的
			} else // 挂线人数>50
			{
				displays = Math.max(5 * actives + random.nextInt(6),
						2 * onlines); // 取10倍活动，2倍挂线之大
			}

		} else // 活动人数25以上，如下显示
		{
			displays = Math.max(5 * actives + random.nextInt(6), 200 + random
					.nextInt(10)); // 取10倍活动，400之大
		}

		return displays;

	}

	// fanys 2006-09-16 start
	private static String getRoomKey(int roomId) {
		return "allowVisitRoom" + roomId;
	}

	private static String getRoomManagerKey(int roomId) {
		return "roomManager" + roomId;
	}

	/**
	 * 将用户添加到在线用户列表里面 如果已经在在线用户列表里面则不添加，否则添加
	 * 
	 * @param userId
	 * @param roomId
	 * @return
	 */
	//liuyi 2006-11-04 服务器缓慢原因查找修改 start
	static Integer defaultValue = new Integer(0);
	public static void addRoomOnlineUser(int roomId, int userId) {
		//synchronized (roomUserLock) 
		{
			chatService.addOnlineUser(roomId + "," + userId + ",now()");
			roomOnlineCache.spt(new Integer2(userId, roomId), defaultValue);
		}
	}
	//liuyi 2006-11-04 服务器缓慢原因查找修改 end

	//liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static void deleteRoomOnlineUser(int roomId, int userId) {
		//synchronized (roomUserLock) 
		{
			chatService.deleteOnlineUser("user_id=" + userId + " and room_id="
					+ roomId);
			roomOnlineCache.srm(new Integer2(userId, roomId));
		}
	}
	//liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * 判断用户是否来过聊天室
	 * 
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public static boolean isUserInRoom(int roomId, int userId) {
		JCRoomOnlineBean onlineUser = null;
		Integer user = (Integer) roomOnlineCache.sgt(new Integer2(userId, roomId));
		if (null == user) {// 如果缓存中没有数据,到数据库中去取
			onlineUser = chatService.getOnlineUser("user_id=" + userId
					+ " and room_id=" + roomId);   
			if (null != onlineUser)// 如果数据库中依然没有,则返回
				return true;
			return false;
		}
		return true;
	}
	// fanys 2006-09-16 end

}
