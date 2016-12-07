package net.joycool.wap.spec.tiny;

/**
 * @author zhouj
 * @explain： 小型嵌入游戏基类2
 * @datetime:1007-10-24
 */
public class Tiny2Game implements Cloneable{
	public static int STATUS_PLAY = 0;
	public static int STATUS_OVER = 1;		// game over但是没有领取奖励
	public static int STATUS_END  = 2;		// 彻底结束
	
	static int idSeq = 1;
	static byte[] lock = new byte[0];
	
	public int id;	// game id, 游戏发起的唯一编号
	
	String returnURL;		// 游戏结束后返回的URL
	int result;
	int status = STATUS_PLAY;		// 游戏状态，0游戏中1游戏结束2游戏提交
	long finishTime;
	public static int finishDelay = 6 * 1000;	// 最多延迟?秒点结束（自动跳转是1秒）
	long startTime;
	public static int startDelay = 30 * 1000;	// 最多延迟?秒点答题（一般玩家是10秒内）
	/**
	 * @return Returns the result.
	 */
	public int getResult() {
		return result;
	}
	/**
	 * @param result The result to set.
	 */
	public void setResult(int result) {
		this.result = result;
	}
	/**
	 * @return Returns the returnURL.
	 */
	public String getReturnURL() {
		return returnURL;
	}
	/**
	 * @param returnURL The returnURL to set.
	 */
	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}
	public int getGameId() {
		return 0;
	}
	public String getGameURL() {
		return "/tiny2/index.jsp";
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
	
	public boolean isStatusPlay() {
		return status == STATUS_PLAY;
	}
	public boolean isStatusOver() {
		return status == STATUS_OVER;
	}
	public boolean isStatusEnd() {
		return status == STATUS_END;
	}
	public void init() {
		startTime = System.currentTimeMillis();
		status = STATUS_PLAY;
	}
	public Tiny2Game copy() {
		try {
			return (Tiny2Game)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	public String getName() {
		return "";
	}
	
	public boolean checkExpired(long now) {
		if(now - startTime > startDelay) {	// 过期了，重新生成
			init();
			return true;
		}
		return false;
	}
}
