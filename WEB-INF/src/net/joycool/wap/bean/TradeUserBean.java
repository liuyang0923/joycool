package net.joycool.wap.bean;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;

/**
 * @author bomb
 * @explain 交易一方
 */
public class TradeUserBean {
	int userId;		
	long money;		// 交易乐币
	List items = new ArrayList();		// 物品，userbagid
	int accept = 0;		// accept status, 对应对方的status
	int status = 1;		// 用户一旦改变自己的物品，此状态+1，用于判断当前用户同意的是否是最新状态
	boolean read = true;		// 是否阅读，如果还没有，则不允许再次发送notice
	
	/**
	 * @return Returns the read.
	 */
	public boolean isRead() {
		return read;
	}


	/**
	 * @param read The read to set.
	 */
	public void setRead(boolean read) {
		this.read = read;
	}


	public TradeUserBean(int userId) {
		this.userId = userId;
	}
	
	
	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return Returns the accept.
	 */
	public int getAccept() {
		return accept;
	}
	/**
	 * @param accept The accept to set.
	 */
	public void setAccept(int accept) {
		this.accept = accept;
	}
	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}
	/**
	 * @param items The items to set.
	 */
	public void setItems(List items) {
		this.items = items;
	}
	/**
	 * @return Returns the money.
	 */
	public long getMoney() {
		return money;
	}
	/**
	 * @param money The money to set.
	 */
	public void setMoney(long money) {
		this.money = money;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}


	public void update(long money, List items) {
//		this.items.clear();
		if(items != null)
			this.items = items;
		this.money = money;
//		if(items != null && items.length() > 0) {
//			String[] items2 = items.split(";");
//			for(int i = 0;i < items2.length;i++) {
//				int tmp = StringUtil.toInt(items2[i]);
//				if(tmp > 0)
//					this.items.add(Integer.valueOf(tmp));
//			}
//		}
		status++;
	}

	public void empty() {
		money = 0;
		items.clear();
	}
	
	public boolean isEmpty() {
		return money == 0 && items.size() == 0;
	}
}
