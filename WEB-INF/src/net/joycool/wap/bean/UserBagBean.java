package net.joycool.wap.bean;

import java.util.Date;

import net.joycool.wap.bean.dummy.DummyProductBean;

/**
 * @author macq
 * @datetime 2006-12-12 下午04:50:25
 * @explain 用户行囊
 */
public class UserBagBean {
	public static int MARK_BIND = (1 << 0);		// 已经绑定？
	int id;

	int userId;			// 物品拥有者
	int creatorId;		// 物品创建者

	int productId;

	int typeId;
	
	int time;

	int mark;		// 标记

	long useTime;		// 上一次使用的时间
	long endTime = 0;		// 道具过期时间
	
	/**
	 * @return Returns the creatorId.
	 */
	public int getCreatorId() {
		return creatorId;
	}

	/**
	 * @param creatorId The creatorId to set.
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * @return Returns the endTime.
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return Returns the useTime.
	 */
	public long getUseTime() {
		return useTime;
	}

	/**
	 * @param useTime The useTime to set.
	 */
	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	/**
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            要设置的 mark
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return productId
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            要设置的 productId
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}

	/**
	 * @return time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time
	 *            要设置的 time
	 */
	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * @return userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId 要设置的 typeId
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	// 返回字符串表示物品还可以使用几次等 (22)，如果多余100则显示??
	public String getTimeString(DummyProductBean item) {
		if(time > 1 || item.getTime() > 1) {
			if(time >= 100)
				return "(??)";
			else
				return "(" + time + ")";
		}
		return "";
	}
	
	public String getTimeString2(DummyProductBean item) {
		if(item.getTime() > 1 && item.getTime() < 100) {
			return "(" + time + "/" + item.getTime() + ")";
		} else {
			return getTimeString(item);
		}
	}
	
	// 已经过期？
	public boolean isEnd() {
		return endTime < System.currentTimeMillis();
	}
	
	public long getTimeLeft() {
		return endTime - System.currentTimeMillis();
	}
	
	public boolean isMarkBind() {
		return (mark & MARK_BIND) != 0;
	}
	public void addMarkBind() {
		addMark(MARK_BIND);
	}
	
	public void addMark(int add) {
		mark |= add;
	}
}
