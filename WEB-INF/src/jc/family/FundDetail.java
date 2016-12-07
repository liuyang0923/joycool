package jc.family;

/**
 * 家族明细
 * 
 * @author qiuranke
 * 
 */
public class FundDetail {

	/**
	 * 捐款
	 */
	public static final int GIFT_TYPE = 0;
	/**
	 * 取款
	 */
	public static final int USER_TYPE = 1;
	/**
	 * 改名
	 */
	public static final int RENAME_TYPE = 2;
	/**
	 * 通知
	 */
	public static final int NOTICE_TYPE = 3;
	/**
	 * 升级
	 */
	public static final int LEVEL_TYPE = 4;
	/**
	 * 帮派转家族
	 */
	public static final int TONG_TYPE = 5;
	/**
	 * 家族对战
	 */
	public static final int VS_GAME_TYPE = 6;
	/**
	 * 家族活动
	 */
	public static final int GAME_TYPE = 7;
	/**
	 * 管理员
	 */
	public static final int ADMIN_TYPE = 8;
	/**
	 * 家族餐厅
	 */
	public static final int YARD_TYPE = 9;

	int id;
	int fmId;
	long cTime;
	long sum;// 变动金额
	int type;// 类型
	long balance;// 余额

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFmId() {
		return fmId;
	}

	public void setFmId(int fmId) {
		this.fmId = fmId;
	}

	public long getcTime() {
		return cTime;
	}

	public void setcTime(long cTime) {
		this.cTime = cTime;
	}

	public long getSum() {
		return sum;
	}

	public void setSum(long sum) {
		this.sum = sum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

}
