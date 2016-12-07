package net.joycool.wap.spec;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 反外挂，随机跳出反外挂页面，用对应的rand数字才能解锁
 *
 */
public class AntiAction extends CustomAction{
	public static int antiRate = 234;
	public static class AntiUser {
		public int userId;
		public int rand;		// 随机生成的id用于解除
		public String url;		// 返回url
	}
	
	public static AntiUser nullAntiUser = new AntiUser();
	public static HashMap antiUser = new HashMap();
	
	public static void addAntiUser(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(antiUser) {
			antiUser.put(key, nullAntiUser);
		}
	}
	
	public static void removeAntiUser(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(antiUser) {
			antiUser.remove(key);
		}
	}
	
	public static AntiUser getAntiUser(int userId) {
		return (AntiUser)getAntiUser(Integer.valueOf(userId));
	}
	public static AntiUser getAntiUser(Integer iid) {
		return (AntiUser)antiUser.get(iid);
	}
	
	public static boolean checkUser(int userId, HttpServletRequest request) {
		Integer key = Integer.valueOf(userId);
		AntiUser user = (AntiUser)antiUser.get(key);
		if(user == nullAntiUser) {
			int rand = RandomUtil.seqInt(98765);
			if(rand % antiRate == 0) {
				user = new AntiUser();
				user.userId = userId;
				user.url = StringUtil.getURI(request);
				user.rand = rand;
				synchronized(antiUser) {
					antiUser.put(Integer.valueOf(userId), user);
				}
				return true;
			} else
				return false;
		}
		return user != null;
	}
	
	public static void unCheckUser(int userId) {
		Integer key = Integer.valueOf(userId);
		synchronized(antiUser) {
			antiUser.put(key, nullAntiUser);
		}
	}
	
	public AntiUser getAntiUser() {
		UserBean loginUser = getLoginUser();
		if(loginUser == null)
			return null;
		AntiUser user = getAntiUser(loginUser.getId());
		if(user == nullAntiUser)
			return null;
		return user;
	}

	public AntiAction(HttpServletRequest request) {
		super(request);
	}
}
