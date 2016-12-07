package net.joycool.wap.action.job.punch;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.LinkBuffer;
import net.joycool.wap.util.RandomUtil;

/**
 * @author bomb
 * @explain：	打小强
 * @datetime:2007-9-1 10:10
 */

public class PunchWorld {
	
	public static int COUNT_PER_ROOM = 12;		// 一个房间有几个
	public int MAX_TYPE = 4;		// 类型数量
	
	byte[] data = new byte[COUNT_PER_ROOM];	// 每个位置的小强 0 尸体 1 蚊子 2 小强 3 老鼠 
	int left;		// 剩余的小强
	
	static String[] typeName = {"尸体", "蚊子", "苍蝇", "小强", "老鼠"};
	static int[] typeMoney = {0, 10000, 10000, 25000, 100000};
		
	public PunchWorld() {
		reset();
	}
	
	static byte randomCell[] = {1,2,3,3,3,3,3,3,3,4};
	public void reset() {
		for(int i = 0;i < COUNT_PER_ROOM;i++) {
			data[i] = randomCell[RandomUtil.nextInt(10)];
		}
		left = COUNT_PER_ROOM;
	}
	
	/**
	 * 打一个位置，返回打的结果
	 * @param pos
	 * @return
	 */
	byte[] lock = new byte[0];
	public byte punch(int pos) {
		if(pos < 0 || pos >= COUNT_PER_ROOM)
			return -1;
		synchronized(lock) {
			byte res = data[pos];
			if(res != 0) {
				data[pos] = 0;
				left--;
				if(left == 0) {
					reset();
				}
			}
			return res;
		}
	}
	
	public String getTypeName(int type) {
		return typeName[type];
	}
	
	public int getTypeMoney(int type) {
		return typeMoney[type];
	}

	/**
	 * @return Returns the data.
	 */
	public byte[] getData() {
		return data;
	}
	
	public String getDataString(HttpServletResponse response) {
		LinkBuffer lb = new LinkBuffer(response, "-", 4);
		for(int i = 0;i < data.length;i++) {
			byte type = data[i];
			if(type == 0)
				lb.appendLink("#c", typeName[type]);
			else
				lb.appendLink("punch.jsp?p=" + i, typeName[type]);
		}
		return lb.toString();
	}
}
