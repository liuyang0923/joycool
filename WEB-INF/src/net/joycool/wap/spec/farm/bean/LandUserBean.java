package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 采集的用户数据
 * @datetime:1007-10-24
 */
public class LandUserBean {
	int landId;		// 所在地图
	int x, y;		// 所在位置
	/**
	 * @return Returns the landId.
	 */
	public int getLandId() {
		return landId;
	}
	/**
	 * @param landId The landId to set.
	 */
	public void setLandId(int landId) {
		this.landId = landId;
	}
	/**
	 * @return Returns the x.
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(int y) {
		this.y = y;
	}
}
