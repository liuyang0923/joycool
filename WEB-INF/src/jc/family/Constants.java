package jc.family;

public class Constants {

	/**
	 * 家族级别,能招的人数
	 */
	public static int[] FM_LEVEL = { 0, 30, 50, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260, 280, 300, 320, 340,
			360, 380, 400 , 450, 500, 550, 600};

	/**
	 * 家族级别,能招的人数
	 */
	public static String[] FM_LEVEL_NAME = { "未入流", "大院", "世家", "大世家", "名门", "名门II", "名门III", "望族", "望族II", "望族III",
			"贵族", "贵族II", "贵族III", "豪门", "豪门II", "帝国", "帝国II" , "帝国III", "大帝国" };

	/**
	 * 家族升级需要的参加游戏数
	 */
	public static int[] FM_LEVEL_GAME = { 0, 5, 10, 20, 30, 45, 60, 80, 100, 125, 150, 180, 210, 245, 280, 320, 400, 620, 780,  1000};

	/**
	 * 家族级别决定可以添加的ALLY的数量
	 */
	public static int[] FM_LEVEL_ALLY = { 0, 2, 5, 5, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 , 10, 10, 10, 10, 10, 10};

	/**
	 * 家族升级需要的资金数
	 */
	public static long[] FM_LEVEL_MONEY = { 1000000000, 1500000000, 1500000000, 2000000000l, 2000000000l, 2000000000l,
			2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l,
			2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l, 2000000000l,
			2000000000l, 2000000000l, 10000000000000l };

	/**
	 * 家族创建最少资金,1E
	 */
	public static int MIN_MONEY_FOR_FM = 100000000;

	/**
	 * 家族创建最少等级
	 */
	public static int MIN_RANK_FOR_FM = 15;

	/**
	 * 家族创建最少社交值
	 */
	public static int MIN_SOCIAL_FOR_FM = 500;

	/**
	 * 解散家族后，再次创建至少要间隔的天数
	 */
	public static int MIN_LEAVE_DATE = 30;

	/**
	 * 加入家族后退出需要的时间
	 */
	public static int MIN_CREATE_DATE = 3;

	/**
	 * 家族改名最少资金
	 */
	public static int FM_RENAME = 1000000000;

	/**
	 * 家族发送通知未超过三条每次扣的钱
	 */
	public static int FM_NOTICE = 50000000;

	/**
	 * 家族发送通知超过三条扣的钱
	 */
	public static int FM_NOTICE_MORE = 100000000;

	/**
	 * 解散家族最少需要的资金10亿乐币
	 */
	public static int MIN_MONEY_DISSOLVE = 1000000000;

	/**
	 * 加入家族最小等级
	 */
	public static int MIN_RANK_FOR_JOIN = 0;

	/**
	 * 上传logo最小等级
	 */
	public static int MIN_GAME_POINT_UPLOAD = 20;

	/**
	 * 上传logo路径
	 */
	public static String FAMILY_IMG_PATH = "family/";

}
