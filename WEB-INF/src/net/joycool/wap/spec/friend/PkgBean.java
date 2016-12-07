package net.joycool.wap.spec.friend;

import java.util.*;

import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 礼包
 */
public class PkgBean {
	public static int STATUS_CREATE = 0;	// 刚创建
	public static int STATUS_SEND = 1;		// 定时发送中
	public static int STATUS_SENT = 2;	// 刚发送未打开
	public static int STATUS_OPEN = 3;	// 已经打开
	
	int id;
	int userId;		// 礼包创建人
	int toId;		// 礼包接收人
	int type;		// 礼包类型，对应pkgTypeBean
	long createTime;	// 礼包创建时间
	long sendTime;		// 未发送前保存定时发送的时间，发送后，保存发送的准确时间
	long openTime;		// 未打开前保存的是可以打开的时间限制，打开后保存打开时间
	
	String item;		// 多个物品id，用逗号间隔
	String title;
	String content;		// 礼包内容文字
	String itemName;	// 多个物品取得名字和数量信息
	
	int money;			// 内含乐币数
	int status;			// 礼包状态
	
	// 选择的时候用于放临时数据，确认后放回到money和item
	List itemList;	// 选择放置物品
	int preMoney;	// 选择的红包
	
	public void initItem() {
		// 根据item字符串，生成itemList，包含多个Integer
		if(item.length() > 0)
			itemList = StringUtil.toInts(item);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public long getOpenTime() {
		return openTime;
	}
	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public List getItemList() {
		return itemList;
	}

	public void setItemList(List itemList) {
		this.itemList = itemList;
	}

	public int getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(int preMoney) {
		this.preMoney = preMoney;
	}
}
