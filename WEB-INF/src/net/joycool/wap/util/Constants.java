/*
 * Created on 2005-7-27
 *
 */
package net.joycool.wap.util;

/**
 * @author lbj
 * 
 */
public class Constants {

	public final static int IMAGE_SIZE = 70;

	public final static String DB = "mcoolwap";

	public final static String DBShortName = "mcool";

	public final static String DBServer = "192.168.0.40";

	public final static String DBUser = "joycool";

	public final static String DBPassword = "joycool";

	public final static String LOGIN_USER_KEY = "loginUser";
	
	public static String JC_MID = "jc_mid"; 

	public final static String NOT_REGISTER_KEY = "notRegistered";

	public final static String USER_COOKIE_NAME = "joycoolUser";

	public final static String ACTION_SUCCESS_KEY = "success";

	public final static String SYSTEM_FAILURE_KEY = "systemFailure";

	// liuyi 2006-12-21 注册登录修改 start
	public static int INDEX_ID = 6765;

	// liuyi 2006-12-21 注册登录修改 end

	// liuyi 2006-12-21 注册登录修改 end

	public final static int ONLINE_USER_PER_PAGE = 10;

	public final static int TEMPLATE_USER_ID = 138;

	public final static int MESSAGE_PER_PAGE = 10;

	public final static int BLOG_ARTICLE_PER_PAGE = 10;

	public final static int BLOG_IMAGE_PER_PAGE = 10;

	public final static int PGAME_PER_PAGE = 10;

	public final static int NEWS_WORD_PER_PAGE = 500;

	public final static int NEWS_PER_PAGE = 10;

	public final static int IMAGE_PER_PAGE = 5;

	public final static int BBS_ARTICLE_PER_PAGE = 10;

	public final static int GUESTBOOK_ARTICLE_PER_PAGE = 10;

	public static int MAX_ATTACH_SIZE = 500000;

	public static int MAX_ALBUMPHOTO_SIZE = 1000000;

	public final static String ATTACTH_TYPES = "GIF|gif|JPG|jpg|PNG|png|BMP|bmp|WBMP|wbmp";

	public final static String RESOURCE_ROOT_URL = "/rep/";
	public static String IMG_ROOT_URL = "http://img.joycool.net/rep";

	// liuyi 20070102 图片路径修改 start
	public static String RESOURCE_ROOT_PATH = "/usr/local/joycool-rep2/";
	public static String RESOURCE_ROOT_PATH_OLD = "/usr/local/joycool-rep/";

	// liuyi 20070102 图片路径修改 end

	public final static String NEWS_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "news/";

	public final static String JA_ICON_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "joycoolAdmin/icon/";

	public final static String JA_RING_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "joycoolAdmin/ring/";

	public final static String IMAGE_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "image/";

	public final static String EBOOK_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "ebook/";

	public final static String GAME_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "game/";

	public final static String PGAME_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "pgame/";

	public final static String FORUM_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "forum/";

	// 交友中心的帖图的路径
	public final static String FRIENDADVER_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "friendadver/";

	// 股市图片路径
	public final static String STOCK_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "stock/";

	// zhul 2006-09-15 我的家园中我的相册图片URL
	public final static String MYALBUM_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "home/myalbum/";

	public final static String URL_PREFIX = "http://wap.joycool.net/";

	public final static String EBOOK_FILE_URL = "/usr/local/joycool-rep/ebook/";

	public final static String FORUM_FILE_PATH = "/usr/local/joycool-rep/forum/";

	// 交友中心的帖图的路径
	public final static String FRIENDADVER_FILE_PATH = "/usr/local/joycool-rep/friendadver/";

	// ZHUL 2006-09-15 我的家园中我的相册图片本地路径
	public static String MYALBUM_FILE_PATH = "/usr/local/joycool-rep/home/myalbum";

	// macq 2006-11-03 交友中心个人形象照片上传本地路径
	public static String FRIEND_FILE_PATH = "/usr/local/joycool-rep/friend/attach";

	// macq 2006-11-03 交友中心个人形象照片URL
	public final static String FRIEND_FILE_PATH_RESOURCE_ROOT_URL = RESOURCE_ROOT_URL
			+ "friend/attach/";

	// add by zhangyi tietu upload path
	// 这是本地windows测试目录，发布时请 让 TIETU_FILE_PATH = FORUM_FILE_PATH
	public final static String TIETU_FILE_PATH = FORUM_FILE_PATH;

	// add by zhangyi for tietu 统计发贴总数时统计表的的room_id
	public final static int TIETU_TOTAL_COUNT_ID = 10000001;

	// add by zhangyi for add chat hall start
	// 聊天室图片本地测试目录，发布时请做相关更改
	public final static String CHAT_IMG_PATH = "/img/chat/";

	// 聊天室表情图片本地测试目录，发布时请做相关更改
	public final static String CHAT_IMG_FACE_PATH = "/img/chat/face";

	// 如果为一次购买聊天室时，默认的过期时间差（天数）expire
	public final static int EXPIRE_DAYS = 8000;

	// 购买一平米（一个人）要交的费用
	public final static int CHAT_BUY_MONEY = 100000;

	// 租赁一平米（一个人）要交的费用
	public final static int CHAT_LEND_MONEY = 1000;

	// 聊天大庭房间id
	public final static int CHAT_BIGROOM_ID = 0;

	// add by zhangyi for add chat hall end
	// fanys_2006-09-07_start
	// 群英排行榜,王冠图片路径
	public final static String ROOM_INVITE_IMG_PATH = "/img/chat/invite/";
	//邀请好友来乐酷帽子
	public final static String DUMMYPRODUCT_HAT_IMG_PATH = "/img/dummyProduct/hat/";

	// 贺卡图片路径
	// liuyi 20070102 图片路径修改 start
	public final static String HAPPY_CARD_IMG_PATH = RESOURCE_ROOT_PATH
			+ "img/job/happycard/";

	// liuyi 20070102 图片路径修改 end

	// 乐酷荣誉公民,王冠图片路径
	public final static String CREDIT_IMG_PATH = "/img/user/top/credit/";
	
	// 2007.3.25Liq赌鬼公民,王冠图片路径
	public final static String DUGUI_IMG_PATH = "/img/duhat/";
	

	// fanys_2006-09-07_end

	// 机会卡价格
	public final static int JOB_CARD_MONEY = 1000;

	// 机会卡图片本地测试目录，发布时请做相关更改
	public final static String JOB_CARD_IMG_PATH = "/img/job/";

	// 猎物图片本地测试目录，发布时请做相关更改
	// liuyi 20070102 图片路径修改 start
	public final static String JOB_HUNT_IMG_PATH = RESOURCE_ROOT_URL
			+ "img/job/hunt/";

	// liuyi 20070102 图片路径修改 end

	// 武器失效时间
	public final static int JOB_HUNT_EXPIRE_TIME = 1;

	// 打猎-打中×1
	public final static int JOB_HUNT_POINT_CG = 1;

	// 打猎-没打到×0.3
	public final static double JOB_HUNT_POINT_SB = 0.3;

	// 打猎-被咬伤×0.7
	public final static double JOB_HUNT_POINT_YS = 0.7;

	// 数字最大取值范围
	public final static int SYS_MAX_INT = 2147483647;

	// macq_2006-11-20_游戏下注最大值_start
	public static int GAME_POINT_MAX_INT = 100000000; 

	// macq_2006-11-20_游戏下注最大值_end

	// 初始贷款利息比率Accrual
	public final static double BANK_LOAD_ACCRUAL = 3;

	// 初始存款利息比率Accrual
	public final static double BANK_STORE_ACCRUAL = 0;

	// 贷款时间
	public final static int BANK_LOAD_1_DAYS = 1;

	public final static int BANK_LOAD_2_DAYS = 2;

	public final static int BANK_LOAD_3_DAYS = 3;

	public final static int BANK_LOAD_4_DAYS = 4;

	public final static int BANK_LOAD_5_DAYS = 5;

	public final static int BANK_LOAD_6_DAYS = 6;

	public final static int BANK_LOAD_7_DAYS = 7;

	public final static int BANK_LOAD_8_DAYS = 8;

	public final static int BANK_LOAD_9_DAYS = 9;

	public final static int BANK_LOAD_10_DAYS = 10;

	public final static int BANK_LOAD_LAST_DAYS = 100;

	// 贷款时间对应利息
	// public final static double BANK_LOAD_1_DAYS_ACCRUAL = 3;
	//
	// public final static double BANK_LOAD_2_DAYS_ACCRUAL = 3.5;
	//
	// public final static double BANK_LOAD_3_DAYS_ACCRUAL = 4;
	//
	// public final static double BANK_LOAD_4_DAYS_ACCRUAL = 4.5;
	//
	// public final static double BANK_LOAD_5_DAYS_ACCRUAL = 5;
	//
	// public final static double BANK_LOAD_6_DAYS_ACCRUAL = 5.5;
	//
	// public final static double BANK_LOAD_7_DAYS_ACCRUAL = 6;
	//
	// public final static double BANK_LOAD_8_DAYS_ACCRUAL = 6.5;
	//
	// public final static double BANK_LOAD_9_DAYS_ACCRUAL = 7;
	//
	// public final static double BANK_LOAD_10_DAYS_ACCRUAL = 7.5;
	//
	// public final static double BANK_LOAD_LAST_DAYS_ACCRUAL = 8;
	public final static double BANK_LOAD_1_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_2_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_3_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_4_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_5_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_6_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_7_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_8_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_9_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_10_DAYS_ACCRUAL = 1;

	public final static double BANK_LOAD_LAST_DAYS_ACCRUAL = 1;

	// 还贷信息记录表 - 存款
	public final static int BANK_STORE_TYPE = 1;
	// 还贷信息记录表 - 取款
	public final static int BANK_WITHDRAW_TYPE = 2;
	// 还贷信息记录表 - 贷款
	public final static int BANK_LOAD_TYPE = 3;
	// 还贷信息记录表 - 还贷
	public final static int BANK_RETURN_TYPE = 4;
	//拍卖
	public final static int BANK_ACUTION_TYPE=5;
	//股市
	public final static int BANK_STOCK_TYPE=6;
	//大富豪
	public final static int BANK_DAFUHAO_TYPE=7;
	//六合赢钱
	public final static int BANK_LHC_TYPE=8;
	//乐币溢出
	public final static int BANK_OUT_OF_MONEY_TYPE=9;
	//道具抢劫卡
	public final static int BANK_USER_BAG_LOOT_TYPE=10;
	//交友系统离婚手续费
	public final static int BANK_FRIEND_DIVORCE_TYPE=11;
	//系统管理员给用户加减乐币
	public final static int BANK_ADMIN_OPERATE_TYPE=12;
	//踩楼游戏
	public final static int BANK_FLOOR_TYPE=13;
	//交易
	public final static int BANK_TRADE_TYPE=14;
	// 大富翁兑换场
	public final static int BANK_RICH_TYPE=15;
	// 铜板兑换
	public final static int BANK_FARM_TYPE=16;

	public final static String SPECIAL_MARK = "%JCSP%";

	public final static String SPECIAL_MONTH_MARK = "%JCSP%MONTH";

	public final static String SPECIAL_DAY_MARK = "%JCSP%DAY";

	public final static String SPECIAL_ONLINE_USER_COUNT_MARK = "%JCSP%ONLINE_USER_COUNT";

	public final static String SPECIAL_NEW_MESSAGE_COUNT_MARK = "%JCSP%NEW_MESSAGE_COUNT";

	public final static String SPECIAL_ONLINE_USER_NICKNAME_MARK = "%JCSP%ONLINE_USER_NICKNAME";

	public final static String SPECIAL_ONLINE_USER_NAME_MARK = "%JCSP%ONLINE_USER_NAME";

	public final static String SPECIAL_INDEX_SEARCH_MARK = "%JCSP%INDEX_SEARCH";

	// mcq_2006-8-18_添加游戏搜索_start
	public final static String SPECIAL_GAME_SEARCH_POST = "%JCSP%GAME_SEARCH_POST";

	// mcq_2006-8-18_添加游戏搜索_end

	// mcq_2006-8-18_添加手机游戏下载搜索_start
	public final static String SPECIAL_GAME_MOBILE_POST = "%JCSP%GAME_MOBILE_POST";
	
	// mcq_2007-3-6_后天添加动态聊天室信息_start
	public final static String SPECIAL_RANDOM_CHAT_MESSAGE_POST = "%JCSP%RANDOM_CHAT_MESSAGE_POST";
////////////////////////////////////////////////////////////////////////////////////////////////
//	Liq_2007-3-16_在女性频道中添加动态聊天室信息_start
	public final static String SPECIAL_RANDOM_CHAT_MESSAGE_POST_THREE = "%JCSP%RANDOM_CHAT_MESSAGE_POST_THREE";
	
	public final static String SPECIAL_RANDOM_CHAT_MESSAGE_POST_ONE ="%JCSP%RANDOM_CHAT_MESSAGE_POST_ONE";
	
	//  Liq_2007-3-19-随机获取美容最新更新的6条信息_start
	public final static String SPECIAL_RANDOM_WOMAN_MEIRONG = "%JCSP%RANDOM_WOMAN_MEIRONG";
	
	//  Liq_2007-3-19-随机获取心情最新更新的6条信息_start
	public final static String SPECIAL_RANDOM_WOMAN_XINQING = "%JCSP%RANDOM_WOMAN_XINQING";

	//  Liq_2007-3-19_取得电子书	都市言情栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_DUSHI = "%JCSP%RANDOM_BOOK_DUSHI";
	
	//  Liq_2007-3-19_取得电子书 西方浪漫栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_LANGMAN = "%JCSP%RANDOM_BOOK_LANGMAN";
	
	//  Liq_2007-3-19_取得电子书 审美系列栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_XILIE = "%JCSP%RANDOM_BOOK_XILIE";
	
	//  Liq_2007-3-19_取得电子书 网络文学栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_WANGLUO = "%JCSP%RANDOM_BOOK_WANGLUO";
	
	//  Liq_2007-3-19_取得电子书 武侠经典栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_WUXIA = "%JCSP%RANDOM_BOOK_WUXIA";
	
	//  Liq_2007-3-19_取得 两性常识,性爱技巧 栏目最新更新的五条内容_start
	public final static String SPECIAL_NEW_SEX_JIQIAO = "%JCSP%NEW_SEX_JIQIAO";
	
	//  Liq_2007-3-19_取得 性健康与性疾病 栏目最新更新的五条内容_start
	public final static String SPECIAL_NEW_SEX_JIANKANG = "%JCSP%NEW_SEX_JIANKANG";
	
	//  Liq_2007-3-19_取得 两性心里，情感故事 栏目最新更新的五条内容_start
	public final static String SPECIAL_NEW_SEX_QINGGAN = "%JCSP%NEW_SEX_QINGGAN";
	
//	Liq_2007-3-16_在两性频道中增加午夜私语论坛信息_start
	public final static String SPECIAL_RANDOM_CHAT_MESSAGE_SEX_THREE = "%JCSP%RANDOM_CHAT_MESSAGE_SEX_THREE";
	
//	Liq_2007-3-16_在最新上传的80本书中随机选取5本_start
	public final static String SPECIAL_RANDOM_EBOOK_80_5 = "%JCSP%RANDOM_EBOOK_80_5";
	
//	Liq_2007-3-16_随机选择5种图书,每种图书选一本_start
	public final static String SPECIAL_RANDOM_EBOOK_TYPE_5 = "%JCSP%RANDOM_EBOOK_TYPE_5";
///////////////////////////////////////////////////////////////////////////////////////////////////////////
	//  Liq_2007-3-28-乐酷主页,判断是否登录,显示不同的链接_start
	public final static String SPECIAL_JOYCOOL_LOGIN = "%JCSP%SPECIAL_JOYCOOL_LOGIN";
	//  Liq_2007-3-28-乐酷主页,显示两张图片_start
	public final static String SPECIAL_JOYCOOL_PICTURE = "%JCSP%SPECIAL_JOYCOOL_PICTURE";
	//  Liq_2007-3-28-乐酷主页,显示大盘指数_start
	public final static String SPECIAL_JOYCOOL_STOCK = "%JCSP%SPECIAL_JOYCOOL_STOCK";
	//  Liq_2007-3-28-乐酷主页,显示大盘指数_start
	public final static String SPECIAL__STOCK = "%JCSP%SPECIAL_STOCK";
//	Liq_2007-3-16_在首页中任意一条论坛信息_start
	public final static String SPECIAL_RANDOM_CHAT_MESSAGE = "%JCSP%RANDOM_CHAT_MESSAGE";
//	Liq_2007-3-16_在首页中任意一条交友信息_start
	public final static String SPECIAL_RANDOM_FRIEND = "%JCSP%RANDOM_CHAT_FRIEND";
//	Liq_2007-3-16_在首页中添加第一的帮会信息_start
	public final static String SPECIAL_FIRST_TONG = "%JCSP%_FIRST_TONG";
//	Liq_2007-3-16_在首页中添加一条日记信息_start
	public final static String SPECIAL_FIRST_DIARY = "%JCSP%_FIRST_DIARY";
	//  Liq_2007-3-19_取得电子书 禁书栏目最新20本书的随机一本_start
	public final static String SPECIAL_RANDOM_BOOK_SEX = "%JCSP%RANDOM_BOOK_SEX";
	//  Liq_2007-3-19_取得游戏栏目最新20个中的随机一个_start
	public final static String SPECIAL_RANDOM_GAME = "%JCSP%RANDOM_GAME";
	//  Liq_2007-3-19_取得铃声栏目最新20个中的随机一个_start
	public final static String SPECIAL_RANDOM_RING = "%JCSP%RANDOM_RING";
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// mcq_2007-3-6_推荐网友图片_start
	public final static String SPECIAL_PHOTO_MARK_POST = "%JCSP%PHOTO_MARK_POST";
	
	public final static String SPECIAL_WGAME_HALL_JINHUA_POST = "%JCSP%WGAME_HALL_JINHUA_POST";

	// mcq_2006-8-18_添加手机游戏下载搜索_end

	// mcq_2006-8-18_添加铃声下载搜索_start
	public final static String SPECIAL_RING_SEARCH = "%JCSP%SPECIAL_RING_SEARCH";

	// mcq_2006-8-18_添加铃声下载搜索_end

	public final static int IMAGE_THUMBNAIL_WIDTH = 60;

	public final static int POSITION_NULL = 0;

	public final static int POSITION_CHAT = 1;

	public final static int POSITION_WGAME = 2;

	public final static int POSITION_EBOOK = 3;

	public final static int POSITION_IMAGE = 4;

	public final static int POSITION_NEWS = 5;

	public final static int POSITION_GAME = 6;

	public final static int POSITION_JOB = 7;

	public final static int POSITION_RING = 8;

	public final static int POSITION_VIDEO = 9;

	public final static int POSITION_FORUM = 10;

	public final static int POSITION_TOP = 11;

	public final static int POSITION_USER = 12;

	public final static int POSITION_HOME = 13;

	// liuyi 2007-01-03 股票位置
	public final static int POSITION_STOCK = 14;

	// mcq_1_增加经验值设置 时间:2006-6-11
	// 普通浏览
	public final static int RANK_GENERAL = 1;

	// 人人对战
	public final static int RANK_LOSE = 5;

	// 人人对战
	public final static int RANK_DRAW = 10;

	// 人人对战
	public final static int RANK_WIN = 20;

	// 人机对战
	public final static int RANK_PK_DRAW = 5;

	// 人机对战
	public final static int RANK_PK_WIN = 10;

	// 人机对战
	public final static int RANK_PK_LOSE = 5;

	// 发送动作
	public final static int RANK_ACTION = 5;

	// 看电子书
	public final static int RANK_EBOOK = 2;

	// mcq_end

	// 交友中心模块，将用户输入的年龄段信息常量化
	public final static int AGESUB17 = 1;

	public final static int AGE18_20 = 2;

	public final static int AGE21_25 = 3;

	public final static int AGE26_30 = 4;

	public final static int AGE31_40 = 5;

	public final static int AGEUP41 = 6;

	// 循环次数
	public final static int LOOK_COUNT = 1;

	// mcq_end
	// 机器人坐庄的次数
	public final static int PKROBOT_COUNT = 1;

	// mcq_end
	// 机器人的ID
	public final static int PKROBOT_ID = 383697;

	// mcq_end
	// 机器人的昵称
	public final static String PKROBOT_NICKNAME = "风花雪叶";

	// mcq_end
	// 机器人的昵称
	public final static int PKROBOT_WAGER = 1000;

	// mcq_end
	// 机器人的昵称
	public final static int PRIVATE_MESSAGE_COUNT = 50;

	// mcq_end
	// zhul 2006-07-19 打猎游戏的武器 start
	public final static int ARROW = 1;

	public final static int HANDGUN = 2;

	public final static int HUNTGUN = 3;

	public final static int AK47 = 4;
	//MACQ_2007-6-27_增加狙击步枪
	public final static int AWP = 5;

	// zhul 2006-07-19 打猎游戏的武器 end

	// zhul 2006-07-19 武器功击动物结果start
	public final static int HIT = 1;

	public final static int NOHIT = 2;

	public final static int HARM = 3;

	// zhul 2006-07-19 武器功击动物结果end

	// zhul 2006-07-19 随机基数
	public final static int RANDOM_BASE = 100000;

	// mcq_2006-07-25_增加金融统计位置_start
	// 管理员行为
	public final static int ADMINACTION = 1;

	// 用户注册
	public final static int USERREGISTER = 2;

	
	// 游戏玩家胜率概率 liq 2007.4.12
	public  static int GAMEPLAYERWIN = 1000000000;
	
	// 二十一点
	public final static int G21 = 3;

	// 老虎机
	public final static int TIGER = 4;

	// 猜大小
	public final static int DICE = 5;

	// 射门
	public final static int FOOTBALL = 6;
	
	// 篮球 liq 2007.4.2
	public final static int BASKETBALL = 26;

	// 老虎杠子鸡 liq 2007.4.2
	public final static int LGJ = 27;
	
	// 单双 liq 2007.4.2
	public final static int WORE = 28;
	
	// 三公
	public final static int GONG3 = 7;

	// 骰子比大小
	public final static int DICEDX = 8;

	// 开心辞典
	public final static int JOB = 9;

	// 听歌猜名
	public final static int MUSIC = 10;

	// 机会卡
	public final static int CARD = 11;

	// 打猎
	public final static int HUNT = 12;

	// 乐透
	public final static int LOTTERY = 13;

	// 动作
	public final static int SENDACTION = 14;

	// 其他
	public final static int OTHER = 15;

	// 剪刀石头布
	public final static int JSB = 16;

	// ShowHand
	public final static int SHOWHAND = 17;

	// liuyi 2007-01-03 银行现金流统计
	public final static int BANK = 18;

	// 跑马
	public final static int HANDBOOKINGER = 19;

	// liuyi 2007-01-03 股票现金流统计
	public final static int STOCK = 20;

	// mcq_2006-07-25_增加金融统计位置_end

	public final static int PLUS = 0;

	public final static int SUBTRATION = 1;

	public final static String LAST_MODULE_URL = "lastModuleUrl";

	public final static String CURRENT_MODULE_URL = "currentModuleUrl";

	public final static String LAST_MODULE = "lastModule";

	public final static String CURRENT_MODULE = "currentModule";

	// mcq_2005-8-4_当前用户是否能贷到给定数目的款_start
	public final static int GERATERTHAN = 1;

	public final static int LESSTHAN = 2;

	public final static int LESSTHANTURE = 3;

	public final static int LOANTRUE = 0;

	// mcq_2005-8-4_当前用户是否能贷到给定数目的款_end

	// mcq_2005-8-6_乐币峰值_start
	public final static int MAX_GAME_POINT = 2100000000;

	// mcq_2005-8-6_乐币峰值_end

	// 打猎图片的上传地址：
	// liuyi 20070102 图片路径修改 start
	public final static String HUNT_IMAGE_PATH = Constants.RESOURCE_ROOT_PATH
			+ "img/job/hunt";

	// liuyi 20070102 图片路径修改 end

	// 现在31天的星座运势
	public final static int LUCK_DAYS = 31;

	// mcq_2006-8-4_获取聊天室缓存数量初始值为2000_start
	public final static int ROOM_CONTENT_COUNT = 2000;

	// mcq_2006-8-4_获取聊天室缓存数量初始值为2000_start

	// mcq_2006-9-19_用户个人家园标志位_start
	public final static int USER_INFO_HOME_MARK = 1;

	// mcq_2006-9-19_用户个人家园标志位_end

	// mcq_2006-9-19_用户个人交友信息标志位_start
	public final static int USER_INFO_FRIEND_MARK = 1;

	// mcq_2006-9-19_用户个人交友信息标志位_end

	// mcq_2006-9-19_用户个人家园标图片存放路径_start

	public static String HOME_IMAGE_SMALL_LINUX_ROOT = "/usr/local/joycool-rep/home/small";
	public static String HOME_IMAGE_SMALL_WINDOWS_ROOT ="E:/eclipse/workspace/joycool-portal/img/home/small";
	public static String HOME_IMAGE_BIG_LINUX_ROOT = "/usr/local/joycool-rep/home/big";
	public static String HOME_IMAGE_BIG_WINDOWS_ROOT ="E:/eclipse/workspace/joycool-portal/img/home/big";
	
	public static String CREATE_HOME_IMAGE_SMALL_LINUX_ROOT = "/usr/local/joycool-rep/home/small";
	public static String CREATE_HOME_IMAGE_SMALL_WINDOWS_ROOT ="E:/eclipse/workspace/joycool-portal/img/home/upload/small";
	public static String CREATE_HOME_IMAGE_BIG_LINUX_ROOT = "/usr/local/joycool-rep/home/big";
	public static String CREATE_HOME_IMAGE_BIG_WINDOWS_ROOT ="E:/eclipse/workspace/joycool-portal/img/home/upload/big";
	//家园图片路径
	public final static String HOME_USER_BIG_RESOURCE_ROOT_URL = "/usr/local/joycool-rep/home/big/";

	public final static String HOME_USER_SMALL_RESOURCE_ROOT_URL = "/usr/local/joycool-rep/home/small/";

	// mcq_2006-9-19_用户个人家园标图片存放路径_end

	// mcq_2006-9-19_用户个人家园标初始房间图片_start
	public static int HOME_HOUSE_INIT_BACKGROUND = 84;

	// mcq_2006-9-19_用户个人家园标初始房间图片_end

	// mcq_2006-9-19_用户个人家园标初始背景图片_start
	public static int HOME_IMAGE_INIT_BACKGROUND = 18;

	// mcq_2006-9-19_用户个人家园标初始房屋类型_start
	public static int HOME_HOUSE_INIT_TYPE = 1;

	// mcq_2006-9-19_用户个人家园标初始背景图片_end

	// mcq_2006-9-19_用户个人家园标图片类型_start
	public static int HOME_IMAGE_BACKGROUND_TYPE = 1000;

	public static int HOME_IMAGE_TABLE_TYPE = 200;

	public static int HOME_IMAGE_CHAIR_TYPE = 150;

	public static int HOME_IMAGE_BUREAU_TYPE = 100;

	public static int HOME_IMAGE_BED_TYPE = 50;

	public static int HOME_IMAGE_PET_TYPE = 1;

	// mcq_2006-9-19_用户个人家园标图片类型_start

	// mcq_2006-9-26_注册个人家园赠送50点经验值_start
	public static int REGISTER_HOME_POINT = 50;

	// mcq_2006-9-26_注册个人家园赠送50点经验值_end

	// mcq_2006-9-26_家园照片推荐标志_start
	public static int HOME_PHOTO_MARK = 1;

	// mcq_2006-9-26_家园照片推荐标志_start

	// mcq_2006-9-26_家园照片推荐标志_start
	public static int HOME_DAILY_MARK = 1;

	// mcq_2006-9-26_家园照片推荐标志_start

	// mcq_2006-10-20_用户未读取通知标记_start
	public static String USER_NOTICE_COUNT = "noticeCount";

	// mcq_2006-10-20_用户未读取通知标记_end

	// liuyi 2006-11-01 结义的最小友好度 start
	public static int MIN_FRIEND_LEVEL_FOR_JY = 300;

	// liuyi 2006-11-01 结义的最小友好度 end

	// macq_2006-11-27_受赠金额_start
	public static int CHARITARIAN_USER_MONEY = 10000;

	// macq_2006-12-27_帮会基金受赠金额_start
	public static int TONG_FUND_MONEY = 200000;

	// macq_2006-12-27_帮会基金受赠金额_end

	// macq_2006-11-27_受赠金额_end
	// macq_2006-11-30_home image type_start
	public static int HOME_IMAGE_SMALL_TYPE = 0;

	public static int HOME_IMAGE_BIG_TYPE = 1;
	// macq_2006-11-30_home image type_end
}
