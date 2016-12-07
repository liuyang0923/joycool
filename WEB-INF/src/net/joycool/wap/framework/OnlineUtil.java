package net.joycool.wap.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import net.joycool.wap.util.DateUtil;

public class OnlineUtil {

	public static int scanInterval = 1000 * 60 * 5;

	public static int expireInterval = 1000 * 60 * 40;

	public static int activeSessionTime = 1000 * 60 * 10;

	public static Hashtable onlineHash = new Hashtable();
	
	public static OnlineThread thread = new OnlineThread();

	static class OnlineBean {
		private long lastModify = -1;
		String sessionId = null;
		private Object object = null;

		public OnlineBean(Object object) {
			this.object = object;
			lastModify = System.currentTimeMillis();
		}

		public void update() {
			this.lastModify = System.currentTimeMillis();
		}

		public Object getObject() {
			return object;
		}

		public long getLastModify() {
			return lastModify;
		}

		/**
		 * @return Returns the sessionId.
		 */
		public String getSessionId() {
			return sessionId;
		}

		/**
		 * @param sessionId The sessionId to set.
		 */
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
	}

	public static class OnlineThread extends Thread {
		public void run() {
			while (true) {
				try {
					Thread.sleep(scanInterval);
				} catch (InterruptedException e) {
					return;
				}
				try {
					// 删除onlineHash过期的对象
					System.out.println("*************"
							+ DateUtil.formatDate(new Date(),
									DateUtil.normalTimeFormat)
							+ "*******scan online********");
					Enumeration iter = onlineHash.keys();
					while (iter.hasMoreElements()) {
						String key = (String) iter.nextElement();
						OnlineBean onlineBean = (OnlineBean) onlineHash
								.get(key);
						if (onlineBean == null)
							continue;

						if (System.currentTimeMillis()
								- onlineBean.getLastModify() > expireInterval) {
							onlineHash.remove(key);
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public static void updateOnlineBean(String key, Object object) {
		if (key == null || object == null)
			return;

		{
			OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
			if (onlineBean == null) {
				// liuyi 2006-12-01 程序优化 start
				// synchronized (onlineHash)
				{
					onlineBean = (OnlineBean) onlineHash.get(key);
					if (onlineBean == null) {
						onlineBean = new OnlineBean(object);
						onlineHash.put(key, onlineBean);
					}
				}
				// liuyi 2006-12-01 程序优化 end
			} else {
				onlineBean.update();
			}
		}
	}

	/*
	 * 判断某个key是否在onlineHash里存在非空对象；
	 */
	public static boolean isOnline(String key) {
		Object onlineObject = getOnlineBean(key);
		return (onlineObject != null);
	}

	public static Object getOnlineBean(String key) {
		if (key == null)
			return null;

		OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
		if (onlineBean != null) {
			return onlineBean.getObject();
		}
		return null;
	}

	public static long getLastVisitTime(String key) {
		if (key == null)
			return -1;

		OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
		if (onlineBean != null) {
			return onlineBean.getLastModify();
		}
		return -1;
	}
	
	public static boolean isActive(String key){
		return System.currentTimeMillis() - getLastVisitTime(key) <= OnlineUtil.activeSessionTime;
	}
	
	public static int getIdleTime(String key) {
		return (int) ((System.currentTimeMillis() - getLastVisitTime(key)) / 60000);
	}

	public static void removeOnlineBean(String key) {
		if (key == null)
			return;

		onlineHash.remove(key);
	}

	public static int getOnlineBeanCount() {
		return onlineHash.size();
	}

	public static Hashtable getOnlineHash() {
		return onlineHash;
	}

	/**
	 * zhul 2006-10-16 获取所有在线用户Id
	 * 
	 * @return
	 */
	public static ArrayList getAllOnlineUser() {
		ArrayList onlineUser = new ArrayList();
		Enumeration enu = onlineHash.keys();
		while (enu.hasMoreElements()) {
			try {
				String key = (String) enu.nextElement();
				if (key != null && key.length() < 13 && key.length() > 3) {
					Integer.parseInt(key);
					onlineUser.add(key);
				}
			} catch (Exception e) {
			}
		}
		return onlineUser;
	}

	
	/**
	 * @return
	 */
	public static int getActiveSessionCount() {
		int count = 0;
		long time = System.currentTimeMillis();
		Enumeration enu = onlineHash.keys();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
			if (onlineBean != null
					&& (time - onlineBean.lastModify) < activeSessionTime) {
                 count++;
			}
		}
		return count;
	}

	public static void setOnlineSessionId(String key, String sessionid) {
		OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
		if (onlineBean != null) {
			onlineBean.setSessionId(sessionid);
		}
		
	}

	public static boolean isUserKicked(String key, String sessionid) {
		OnlineBean onlineBean = (OnlineBean) onlineHash.get(key);
		if (onlineBean != null && onlineBean.getSessionId() != null && sessionid != null) {
			return !sessionid.equals(onlineBean.getSessionId());
		}
		return false;
	}
}
