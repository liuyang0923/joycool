package jc.guest;


/**
 * 在线用户表
 */
public class OnlineGuest {
	
	public static int FIVE_MINUTES = 5 * 60 * 1000;
	
	GuestUserInfo guestUser;
	long lastVisitTime;		// 最后一次访问的时间
	String lastVisitUrl;	// 最后一次访问的地址
	
	public OnlineGuest(){
	}

	/**
	 * OnlineGuest(guestUser,访问时间,访问的页面)
	 * @param guestUser
	 * @param visitTime
	 * @param visitUrl
	 */
	public OnlineGuest(GuestUserInfo guestUser,long visitTime,String visitUrl){
		this.guestUser = guestUser;
		this.lastVisitTime = visitTime;
		this.lastVisitUrl = visitUrl;
	}
	public GuestUserInfo getGuestUser() {
		return guestUser;
	}
	public void setGuestUser(GuestUserInfo guestUser) {
		this.guestUser = guestUser;
	}
	public long getLastVisitTime() {
		return lastVisitTime;
	}
	public void setLastVisitTime(long lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}
	public String getLastVisitUrl() {
		return lastVisitUrl;
	}
	public void setLastVisitUrl(String lastVisitUrl) {
		this.lastVisitUrl = lastVisitUrl;
	}
	
	/**
	 * 返回值=现在时间-上次登陆时间(单位:毫秒)
	 * @return
	 */
	public long pastTime(){
		return System.currentTimeMillis() - this.getLastVisitTime();
	}
	
	/**
	 * 查看这个bean是不是还在线。如果超过5分钟没有动，就算做不在线
	 * @return
	 */
	public boolean isOnline(){
		return pastTime() <= FIVE_MINUTES;
	}
}
