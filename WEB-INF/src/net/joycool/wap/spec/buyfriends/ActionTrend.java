package net.joycool.wap.spec.buyfriends;

import net.joycool.wap.util.SecurityUtil;

public class ActionTrend {
	public static ServiceTrend serviceTrend = ServiceTrend.getInstance();	
	
	public static void addPhotoTrend(int userId, int type, String content, String nickName) {
		BeanTrend trend = new BeanTrend();
		trend.setUid(userId);
		trend.setNickName(nickName);
		trend.setType(type);
		trend.setContent(content);
		serviceTrend.addTrend(trend);
	}
	
	/**
	 * 表示增加游戏动态 第一用户 做了某件事的动态
	 * @param userId   第一用户id
	 * @param type	   动态类型 BeanTreand.
	 * @param content  动态内容1%代表uid1,2%代表uid2，3%代表content里面的其他链接
	 * @param nickname 第一昵称
	 * @param title 动态标题
	 * @param link  动态链接
 	 */
	public static void addGameTrend(int userId, int type, String content, String nickname, String title, String link) {
		BeanTrend trend = new BeanTrend();
		trend.setUid(userId);
		trend.setNickName(nickname);
		trend.setType(type);
		trend.setTitle(title);
		trend.setLink(link);
		trend.setContent(content);
		
		serviceTrend.addGameTrend(trend);
	}
	
	/**
	 * 表示增加游戏动态 第一用户把第二用户怎么样了，或者第二用户被第一用户怎么样了。
	 * @param userId	第一用户id
	 * @param type		动态类型 BeanTreand.
	 * @param content   动态内容1%代表uid1,2%代表uid2，3%代表content里面的其他链接
	 * @param nickname  第一昵称
	 * @param userId2   第二用户id
	 * @param nickname2 第二昵称
	 */
	public static void addGameTrend(int userId, int type, String content, String nickname, int userId2, String nickname2) {
		BeanTrend trend = new BeanTrend();
		trend.setUid(userId);
		trend.setNickName(nickname);
		trend.setType(type);
		trend.setUid2(userId2);
		trend.setNickName2(nickname2);
		trend.setContent(content);
		
		serviceTrend.addGameTrend(trend);
	}
	
	
	/**
	 * 表示增加普通动态 第一用户把第二用户怎么样了，或者第二用户被第一用户怎么样了。
	 * @param userId	第一用户id
	 * @param type		动态类型 BeanTreand.
	 * @param content   动态内容1%代表uid1,2%代表uid2，3%代表content里面的其他链接
	 * @param nickname  第一昵称
	 * @param userId2   第二用户id
	 * @param nickname2 第二昵称
	 */
	public static void addTrend(int userId, int type, String content, String nickname, int userId2, String nickname2) {
		if(SecurityUtil.isAdmin(userId))
			return;
		BeanTrend trend = new BeanTrend();
		trend.setUid(userId);
		trend.setNickName(nickname);
		trend.setType(type);
		trend.setUid2(userId2);
		trend.setNickName2(nickname2);
		trend.setContent(content);
		
		serviceTrend.addTrend(trend);
	}
	
	/**
	 * 表示增加普通动态 第一用户 做了某件事的动态
	 * @param userId   第一用户id
	 * @param type	   动态类型 BeanTreand.
	 * @param content  动态内容1%代表uid1,2%代表uid2，3%代表content里面的其他链接
	 * @param nickname 第一昵称
	 * @param title 动态标题
	 * @param link  动态链接
 	 */
	public static void addTrend(int userId, int type, String content, String nickname, String title, String link) {
		if(SecurityUtil.isAdmin(userId))
			return;
		BeanTrend trend = new BeanTrend();
		trend.setUid(userId);
		trend.setNickName(nickname);
		trend.setType(type);
		trend.setTitle(title);
		trend.setLink(link);
		trend.setContent(content);
		
		serviceTrend.addTrend(trend);
	}
}
