package net.joycool.wap.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.Integer2;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;

public class RoomCacheUtil {

	public static ICacheMap roomContentCache = CacheManage.roomContent;
	static ICacheMap chatListCache = CacheManage.chatList;
	static ICacheMap chat2ListCache = CacheManage.chat2List;
	public static HashMap linkManMap = new HashMap();
	
	public static int LINKMAN_SIZE = 15;

	private static HashMap roomContentCountMap = new HashMap(); // 每个聊天室公聊数量缓存

	private static Object mapLock = new Object(); // 当查询某个聊天室聊天记录所有id在内存中不存在时,改为查询数据库时的同步锁

	private static Object contentLock = new Object(); // 当在内存中不存在查询的聊天记录,改为查询数据库时的同步锁

	private static IChatService service = ServiceFactory.createChatService();

	/**
	 * 
	 * @author macq
	 * @explain：获取最近联系人map
	 * @datetime:2007-5-22 7:39:34
	 * @return
	 * @return HashMap
	 */
	public static HashMap getLinkManMap() {
		return linkManMap;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取一个用户最近所有联系人（15个人限制）
	 * @datetime:2007-5-22 7:45:37
	 * @param userId
	 * @return
	 * @return LinkedList
	 */
	public static LinkedList getLinkManByIdList(int userId) {
		LinkedList linkManList = (LinkedList) linkManMap
				.get(new Integer(userId));
		if (linkManList == null) {
			linkManList = new LinkedList();
		}
		return linkManList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：添加一个用户最近联系人
	 * @datetime:2007-5-22 7:46:26
	 * @param userId
	 * @return
	 * @return boolean
	 */
	static void addLinkManByIdList(int fromUserId, int toUserId) {
		//类型转换
		Integer fUserId = new Integer(fromUserId);
		Integer tUserId = new Integer(toUserId);
		//判断联系人map中是否存在该用户
		if (linkManMap.get(fUserId) != null) {
			LinkedList linkManList =(LinkedList)linkManMap.get(fUserId);
			if(linkManList.remove(tUserId)){
				linkManList.addFirst(tUserId);
			}else{
				//最多保存15个最近联系人
				if(linkManList.size()>LINKMAN_SIZE){
					linkManList.removeLast();
				}
				linkManList.addFirst(tUserId);
			}
		} else {
				LinkedList linkManList = new LinkedList();
				linkManList.add(tUserId);
				linkManMap.put(fUserId, linkManList);
		}
	}
	
	
	static byte[] lock = new byte[0];
	/**
	 *  
	 * @author macq
	 * @explain：维护2个人用户直接最近联系人方法
	 * @datetime:2007-5-22 8:13:39
	 * @param fromUserId
	 * @param toUserId
	 * @return void
	 */
	public static void addLinkManList(int fromUserId, int toUserId) {
		synchronized(lock){
		addLinkManByIdList(fromUserId,toUserId);
		addLinkManByIdList(toUserId,fromUserId);
		}
	}

	/**
	 * 获取roomContentCountMap。
	 * 
	 * @param contentId
	 */
	public static HashMap getRoomContentCountMap() {
		return roomContentCountMap;
	}

	/**
	 * 清空某条聊天记录的属性缓存。
	 * 
	 * @param contentId
	 */
	public static void flushRoomContent(int contentId) {
		roomContentCache.srm(contentId);
	}

	/**
	 * 取得某条聊天记录。
	 * 
	 * @param contentId
	 * @return
	 */
	public static JCRoomContentBean getRoomContent(int contentId) {
		Integer key = Integer.valueOf(contentId);
		synchronized(roomContentCache) {
			JCRoomContentBean content = (JCRoomContentBean) roomContentCache.get(key);
			if (content == null) {
				content = service.getContent("id = " + contentId);
				if (content == null) {
					return null;
				}
				roomContentCache.put(key, content);
			}
			return content;
		}
	}

	/**
	 * 增加某条聊天记录。 // mcq_2006-9-16_增加聊天缓存记录_start
	 * 
	 * @param contentId
	 * @return
	 */
	public static void addRoomContent(JCRoomContentBean bean) {
		// liuyi 2006-09-16 聊天室加缓存 start
		if (bean == null)
			return;
		roomContentCache.spt(bean.getId(), bean);
		// liuyi 2006-09-16 聊天室加缓存 end
		return;
	}

	/**
	 * 取得某条聊天记录。
	 * 
	 * @param contentId
	 * @return
	 */
	public static JCRoomContentBean getRoomContent1(int contentId) {
		return (JCRoomContentBean) roomContentCache.sgt(contentId);
	}

	/**
	 * 更新某条聊天记录。
	 * 
	 * @param set
	 * @param condition
	 * @param contentId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean updateRoomContent(String set, String condition,
			int contentId) {
		// 更新聊天记录
		if (!service.updateRoomContent(set, condition)) {
			return false;
		}
		// 清空缓存
		flushRoomContent(contentId);
		return true;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * 更新某条聊天记录。
	 * 
	 * @param condition
	 * @param contentId
	 * @return boolean
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean deleteRoomContent(String condition, int contentId,
			int roomId) {
		// liuyi 2006-09-16 聊天室加缓存 start
		JCRoomContentBean content = getRoomContent(contentId);
		if (content != null) {
			int fromId = content.getFromId();
			int toId = content.getToId();
			RoomCacheUtil.deletePrivateContentID(contentId, fromId);
			RoomCacheUtil.deletePrivateContentID(contentId, toId);
			RoomCacheUtil.deleteTwoUserContentID(contentId, fromId, toId);
		}
		// liuyi 2006-09-16 聊天室加缓存 end

		// 删除聊天记录
		if (!service.deleteContent(condition)) {
			return false;
		}
		deleteRoomContentId(roomId, contentId);
		// 清空缓存
		flushRoomContent(contentId);
		return true;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * 取得缓存的key。
	 * 
	 * @param contentId
	 * @return
	 */
	public static String getKey(int contentId) {
		return "" + contentId;
	}

	/**
	 * 获取单个聊天室公聊5000条聊天记录ID放入缓存
	 * 
	 * @param roomId
	 * @return
	 */
	public static Vector getRoomContentCountMap(int roomId) {
		// 判断map是否为空
		// if (roomContentCountMap != null) {
		// 判断map中是否包含该聊天室
		Vector flag = (Vector) roomContentCountMap.get(new Integer(roomId));
		if (flag == null) {
			// 获取该聊天室所有聊天记录id
			Vector contentList = service
					.getRoomIdCountList("select id from jc_room_content where (room_id="
							+ roomId
							+ ") and "
							+ "is_private=0 and mark!=2 order by id desc limit 1000");
			// 添加vector到map中
			roomContentCountMap.put(new Integer(roomId), contentList);
		}
		// 返回map
		return (Vector) roomContentCountMap.get(new Integer(roomId));
	}

	/**
	 * 清空一个聊天室的所有记录id.
	 * 
	 * @param roomId
	 * @return
	 */
	public static void flushRoomContentId(int roomId) {
		// 清除某个聊天室在map中的记录
		roomContentCountMap.remove(new Integer(roomId));
	}

	/**
	 * 
	 * @author macq
	 * @explain： 清空所有聊天室的所有记录id.
	 * @datetime:2007-3-12 11:10:46
	 * @param roomId
	 * @return void
	 */
	public static void flushRoomContentIdByALL() {
		// 清除所有聊天室在map中的记录
		roomContentCountMap = new HashMap();
	}

	/**
	 * 添加一条聊天记录到对应缓存对应id列表中
	 * 
	 * @param roomId
	 * @return
	 */
	public static void addRoomContentId(int roomId, JCRoomContentBean bean) {
		// liuyi 2006-09-16 聊天室加缓存 start
		Vector roomIdList = getRoomContentCountMap(roomId);
		// liuyi 2006-12-01 程序优化 start
		// synchronized (roomIdList)
		{
			if (!roomIdList.contains(new Integer(bean.getId()))) {
				roomIdList.add(0, new Integer(bean.getId()));
			}
		}
		// liuyi 2006-12-01 程序优化 end
		// liuyi 2006-09-16 聊天室加缓存 end
	}

	/**
	 * 删除一条聊天记录到对应缓存对应id列表中
	 * 
	 * @param roomId
	 * @return
	 */
	public static void deleteRoomContentId(int roomId, int contentId) {
		// liuyi 2006-09-16 聊天室加缓存 start
		Vector roomIdList = getRoomContentCountMap(roomId);
		// liuyi 2006-12-01 程序优化 start
		// synchronized (roomIdList)
		{
			roomIdList.remove(new Integer(contentId));
		}
		// liuyi 2006-12-01 程序优化 end
		// liuyi 2006-09-16 聊天室加缓存 end
	}

	/**
	 * liuyi 2006-09-16 根据用户id获取他的聊天记录ID列表,该方法确保返回一个Vector实例；
	 * 
	 * @param userId
	 * @return
	 */
	public static Vector getPrivateContentIDList(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(chat2ListCache) {
			Vector ret = (Vector) chat2ListCache.get(key);
	
			if (ret == null) {
				String sql = "select id from jc_room_content where from_id =" + userId
						+ " union select id from jc_room_content where to_id =" + userId
						+ " order by id desc limit 100";
				ret = (Vector) SqlUtil.getObjectList(sql);
	
				if (ret == null) {
					ret = new Vector();
				}
				chat2ListCache.put(key, ret);
			}
	
			return ret;
		}
	}

	/**
	 * macq 2006-11-07 聊天室列表缓存；
	 * 
	 * @param userId
	 * @return
	 */
	public static Vector getRoomListCache() {
		Vector roomList = null;
		String key = "room";
		roomList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.ROOM_BEAN_CACHE_GROUP,
				OsCacheUtil.ROOM_BEAN_CACHE_FLUSH_PERIOD);
		if (roomList == null) {
			roomList = service.getJCRoomList("1=1");
			OsCacheUtil.put(key, roomList, OsCacheUtil.ROOM_BEAN_CACHE_GROUP);
		}
		return roomList;
	}

	/**
	 * liuyi 2006-09-16 根据聊天记录id列表获取对应的聊天记录列表，该方法确保返回一个Vector实例；
	 * 
	 * @param contentIdList
	 * @return
	 */
	public static Vector getContentList(List contentIdList) {
		Vector ret = new Vector();

		if (contentIdList == null)
			return ret;

		for (int i = 0; i < contentIdList.size(); i++) {
			Integer id = (Integer) contentIdList.get(i);
			if (id == null)
				continue;

			JCRoomContentBean content = getRoomContent(id.intValue());
			if (content != null) {
				ret.add(content);
			}
		}

		return ret;
	}

	/**
	 * liuyi 2006-09-16 给某个用户的私聊id列表头添加一个聊天记录id
	 * 
	 * @param contentId
	 * @param userId
	 */
	public static void addPrivateContentID(int contentId, int userId) {
		Vector contentIdList = getPrivateContentIDList(userId);

		// 保证单线程
		// liuyi 2006-12-01 程序优化 start
		// synchronized (contentIdList)
		{
			if (!contentIdList.contains(new Integer(contentId))) {
				contentIdList.add(0, new Integer(contentId));
			}
		}
		// liuyi 2006-12-01 程序优化 end
	}

	/**
	 * liuyi 2006-09-16 给某个用户的私聊id列表头删除一个聊天记录id
	 * 
	 * @param contentId
	 * @param userId
	 */
	public static void deletePrivateContentID(int contentId, int userId) {
		Vector contentIdList = getPrivateContentIDList(userId);

		contentIdList.remove(new Integer(contentId));
	}

	/**
	 * liuyi 2006-09-18 根据两个用户id获取他们之间的聊天记录ID列表,该方法确保返回一个Vector实例；
	 * 
	 * @param userId
	 * @return
	 */
	public static Vector getTwoUserContentIDList(int userId1, int userId2) {
		Vector ret = new Vector();
		Integer2 key = null;
		if(userId1 > userId2)
			key = new Integer2(userId1, userId2);
		else
			key = new Integer2(userId2, userId1);

		synchronized (chatListCache) {
			ret = (Vector) chatListCache.get(key);
			if (ret == null) {
				String sql = "select id from jc_room_content where from_id="+userId1+" and to_id="+userId2+" union all "
						+ " select id from jc_room_content where from_id= "+userId2+" and to_id="+userId1+" order by id desc limit 30";
				ret = (Vector) SqlUtil.getObjectList(sql);

				if (ret == null) {
					ret = new Vector();
				}
				chatListCache.put(key, ret);
			}
		}

		return ret;
	}

	/**
	 * liuyi 2006-09-18 给两个用户的聊天id列表头添加一个聊天记录id
	 * 
	 * @param contentId
	 * @param userId
	 */
	public static void addTwoUserContentID(int contentId, int userId1,
			int userId2) {
		Vector contentIdList = getTwoUserContentIDList(userId1, userId2);

		// 保证单线程
		// liuyi 2006-12-01 程序优化 start
		// synchronized (OsCacheUtil.TWO_USER_CONTENT_GROUP)
		{
			if (!contentIdList.contains(new Integer(contentId))) {
				contentIdList.add(0, new Integer(contentId));
			}
		}
		// liuyi 2006-12-01 程序优化 end
	}

	/**
	 * liuyi 2006-09-18 给两个用户的聊天id列表头删除一个聊天记录id
	 * 
	 * @param contentId
	 * @param userId
	 */
	public static void deleteTwoUserContentID(int contentId, int userId1,
			int userId2) {
		Vector contentIdList = getTwoUserContentIDList(userId1, userId2);

		// 保证单线程
		// liuyi 2006-12-01 程序优化 start
		// synchronized (OsCacheUtil.TWO_USER_CONTENT_GROUP)
		{
			contentIdList.remove(new Integer(contentId));
		}
		// liuyi 2006-12-01 程序优化 end
	}

	/**
	 * liuyi 2006-10-17 获取某个聊天室的挂线用户id列表
	 * 
	 * @param roomId
	 * @return
	 */
	public static List getRoomOnlineUserIdList(int roomId) {
		List ret = (List) OsCacheUtil.get(roomId + "",
				OsCacheUtil.ROOM_ONLINE_USER,
				OsCacheUtil.ROOM_ONLINE_USER_FLUSH_PERIOD);

		if (ret == null) {
			String sql = "select user_id from jc_room_online where room_id="
					+ roomId;
			ret = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (ret == null) {
				ret = new ArrayList();
			}
			OsCacheUtil.put(roomId + "", ret, OsCacheUtil.ROOM_ONLINE_USER);
		}
		return ret;
	}
}
