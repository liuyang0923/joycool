package net.joycool.wap.bean;

import java.util.List;

/**
 * @author bomb
 * @explain 用户物品和乐币交易
 */
public class TradeBean {
	TradeUserBean user1, user2;		// 参与交易的双方

	public TradeUserBean getUser(int userId) {
		if(user1.getUserId() == userId)
			return user1;
		else if(user2.getUserId() == userId)
			return user2;
		return null;
	}
	
	// 得到对方用户
	public TradeUserBean getOUser(int userId) {
		if(user1.getUserId() == userId)
			return user2;
		else if(user2.getUserId() == userId)
			return user1;
		return null;
	}
	
	// 发起方用户作为id
	public int getId() {
		return user1.getUserId();
	}
	// 交易是否成功
	synchronized public boolean isComplete() {
		return user1.getStatus() == user2.getAccept() && user2.getStatus() == user1.getAccept();
	}
	
	// 更新用户提交的钱和物品数据
	synchronized public void update(int userId, int status, long money, List items) {
		TradeUserBean user = getUser(userId);
		user.update(money, items);
		user.setAccept(status);
	}
	
	synchronized public void accept(int userId, int status) {
		TradeUserBean user = getUser(userId);
		user.setAccept(status);
	}

	/**
	 * @return Returns the user1.
	 */
	public TradeUserBean getUser1() {
		return user1;
	}

	/**
	 * @param user1 The user1 to set.
	 */
	public void setUser1(TradeUserBean user1) {
		this.user1 = user1;
	}

	/**
	 * @return Returns the user2.
	 */
	public TradeUserBean getUser2() {
		return user2;
	}

	/**
	 * @param user2 The user2 to set.
	 */
	public void setUser2(TradeUserBean user2) {
		this.user2 = user2;
	}

}
