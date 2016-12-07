/**
 * 
 */
package net.joycool.wap.action.chat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import net.joycool.wap.bean.chat.RoomRateBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.util.Constants;

/**
 * @author zhul 2006-09-1 控制bottom下的roomID的随机率
 * 
 */
public class RoomRateAction {

	private static IChatService service = ServiceFactory.createChatService();

	private static TreeMap roomMap = null;

	/**
	 * 从jc_room_rate表中加载随机数据
	 * 
	 * @return
	 */
	public static synchronized TreeMap getRoomMap() {

		if (roomMap != null) {
			return roomMap;
		}
		// 获取机率对应表
		roomMap = new TreeMap();

		ArrayList roomList = service.getRoomRateList(null);
		int size = roomList.size();
		int base = 0;
		for (int i = 0; i < size; i++) {
			RoomRateBean room = (RoomRateBean) roomList.get(i);
			base += room.getRate();
			if (room.getRate() != 0) {
				roomMap.put(new Integer(base), room.getRoomId() + "");
			}
		}
		roomMap.put(new Integer(Constants.RANDOM_BASE), base + "");

		// Set set = roomMap.keySet();
		// Iterator it = set.iterator();
		// while (it.hasNext()) {
		// Integer key = (Integer) it.next();
		// String roomId = (String) roomMap.get(key);
		// System.out.println(key + "/" + roomId);
		// }

		return roomMap;
	}

	public static void clearRoomMap() {
		roomMap = null;
	}

	/**
	 * 获取随机roomId
	 * 
	 * @return
	 */
	public static String getRandomRoomId() {
		TreeMap roomMap = getRoomMap();
		Random random = new Random();
		int base = Integer.parseInt((String) roomMap.get(new Integer(
				Constants.RANDOM_BASE)));
		if (base == 0)
			return "0";
		int rand = random.nextInt(base) + 1;
		Set set = roomMap.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			if (key.intValue() >= rand) {
				String roomId = (String) roomMap.get(key);
				return roomId;
			}
		}

		return "0";
	}

}
