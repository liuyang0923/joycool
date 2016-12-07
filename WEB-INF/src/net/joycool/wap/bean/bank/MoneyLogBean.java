package net.joycool.wap.bean.bank;

/**
 * 
 * @author Administrator 用户银行操作记录对象
 */
public class MoneyLogBean {

	// 记录id
	int id;

	// 用户id
	int userId;

	// 操作时间
	String time;

	// 用户操作款额
	long money;
	long current;		// 这次增加前的数字

	// 操作标志
	 int type;

	int rUserId;

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the money.
	 */
	public long getMoney() {
		return money;
	}

	/**
	 * @param money
	 *            The money to set.
	 */
	public void setMoney(long money) {
		this.money = money;
	}

	/**
	 * @return Returns the time.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            The time to set.
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return 返回 rUserId。
	 */
	public int getRUserId() {
		return rUserId;
	}

	/**
	 * @param userId
	 *            要设置的 rUserId。
	 */
	public void setRUserId(int userId) {
		rUserId = userId;
	}
	
	 public static String getTypeId(int id) {

	        if (id==1) {
	            return "存款";
	        }
	        if (id==2) {
	            return "取款";
	        }
	        if (id==3) {
	            return "贷款";
	        }
	        if (id==4) {
	            return "还贷";
	        }
	        if (id==5) {
	            return "拍卖";
	        }
	        if (id==6) {
	            return "股市";
	        }
	        if (id==7) {
	            return "大富豪";
	        }
	        if (id==8) {
	            return "六合赢钱";
	        }
	        if (id==9) {
	            return "乐币溢出";
	        }
	        if (id==10) {
	            return "道具抢劫卡";
	        }
	        if (id==11) {
	            return "交友系统离婚手续费";
	        }
	        if (id==12) {
	            return "系统管理员给用户加减乐币";
	        }
	        if (id==13) {
	            return "踩楼游戏";
	        }
	        if (id==14) {
	            return "转帐交易";
	        }
	        return "";
	 }

	/**
	 * @return Returns the current.
	 */
	public long getCurrent() {
		return current;
	}

	/**
	 * @param current The current to set.
	 */
	public void setCurrent(long current) {
		this.current = current;
	}
}
