package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.spec.farm.FarmAction;
import net.joycool.wap.spec.farm.FarmNpcWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 掉落到地上的物品
 * @datetime:1007-10-24
 */
public class FarmDropBean extends MapDataBean {
	int[] data;	// 掉落内容
	long createTime;		// 创建时间
	int userId;		// 某人打掉落的，保护为该玩家10秒
	long protectTime;		// 这个时间之后不再保护
	
	public long getProtectTime() {
		return protectTime;
	}

	public void setProtectTime(long protectTime) {
		this.protectTime = protectTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public FarmDropBean() {
		createTime = System.currentTimeMillis();
	}
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int[] getData() {
		return data;
	}
	public void setData(int[] data) {
		this.data = data;
	}
	public String getLink(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getEditLink(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProtect(int protect) {
		protectTime = createTime + protect;
	}
	public boolean isProtected(int userId) {
		return userId != this.userId && FarmAction.now < protectTime;
	}
}
