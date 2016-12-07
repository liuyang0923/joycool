package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 游戏中的元素
 * @datetime:1007-10-24
 */
public abstract class FarmObject {
	static int gidSeq = 1;
	static byte[] lock = new byte[0];
	
	int gid = getGidSeq();
	
	public static int getGidSeq() {
		synchronized(lock) {
			return ++gidSeq;
		}
	}

	public int getGid() {
		return gid;
	}
	
}
