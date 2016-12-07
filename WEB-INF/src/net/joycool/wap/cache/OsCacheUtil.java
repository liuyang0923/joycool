/**
 *作者：李北金
 *创建日期：2006-08-05
 *说明：本类用于控制缓存。
 */
package net.joycool.wap.cache;

import java.util.Hashtable;

/**
 * @author lbj
 * 
 */
public class OsCacheUtil {
	
	/**
	 * liq_2007-0-20_大航海游戏排名缓存
	 */
	public static String DHH_CACHE_GROUP = "dhh";
	
	/**
	 * liq_2007-02-08_大航海游戏排名缓存
	 */
	public static int DHH_CACHE_FLUSH_PERIOD = 60*60*24;
	
	/**
	 * liq_2007-5-30_乐宠游戏排名缓存
	 */
	public static String PET_CACHE_GROUP = "pet";
	
	/**
	 * liq_2007-05-30_乐宠排名缓存
	 */
	public static int PET_CACHE_FLUSH_PERIOD = 60*60*24;
	
	/**
	 * liq_2007-5-30_乐宠游戏排名缓存
	 */
	public static String PET_CACHE_GROUP_FIVE = "pet_five";
	
	/**
	 * liq_2007-05-30_乐宠排名缓存
	 */
	public static int PET_CACHE_FLUSH_PERIOD_FIVE = 60*60;
	
	
	/**
	 * liq_2007-5-30_乐宠游戏排名缓存
	 */
	public static String PET_CACHE_GROUP_MATCH = "pet_match";
	
	/**
	 * liq_2007-05-30_乐宠排名缓存
	 */
	public static int PET_CACHE_FLUSH_PERIOD_MATCH = 60*60;
	/**
<<<<<<< .mine
	 * liq_2007-02-08_问答接龙游戏历史大排名缓存组
	 */
	public static String GAME_QUESTION_TOTAL_CACHE_GROUP = "gamequestiontotal";
	
	/**
	 * liq_2007-02-08_问答接龙游戏历史大排名缓存时间
	 */
	public static int GAME_QUESTION_TOTAL_CACHE_FLUSH_PERIOD = 60*60*24;

	/**
	 * liq_2007-02-08_问答接龙游戏当日大排名缓存组
	 */
	public static String GAME_QUESTION_TODAY_CACHE_GROUP = "gamequestiontoday";
	
	/**
	 * liq_2007-02-08_问答接龙游戏当日大排名缓存时间
	 */
	public static int GAME_QUESTION_TODAY_CACHE_FLUSH_PERIOD = 60*60;
	
	/**
	 * 书库页面主页缓存时间  liq_2007-03-19随机选择5种图书,每种图书选一本
	 */
	public static int EBOOK_CACHE_GROUP_PAGE_FLUSH_PERIOD = 60*60*24;
	public static String EBOOK_CACHE_GROUP_PAGE = "EBOOK_GROUP_PAGE";
	
	/**
	 * 书库页面主页缓存时间  liq_2007-03-19在最新上传的80本书中随机选取5本
	 */
	public static int EBOOK_CACHE_GROUP_PAGE_5_FLUSH_PERIOD = 60*60*24;
	public static String EBOOK_CACHE_GROUP_PAGE_5 = "EBOOK_GROUP_PAGE_5";
	/**
	 * 新闻缓存组。 
=======
	 * 新闻缓存组。
>>>>>>> .r8625
	 */
	/*
	 * liq_2007-04-10_JSP广告缓存组
	 */
	public static String JSP_AVER_CACHE_GROUP = "jspadver";
	
	/**
	 * liq_2007-04-10_JSP广告缓存组
	 */
	public static int JSP_AVER_CACHE_FLUSH_PERIOD = 60*60*24*5;

	
	public static String NEWS_GROUP = "news";

	/**
	 * 游戏缓存组。
	 */
	public static String GAME_GROUP = "game";

	/**
	 * 
	 * 视频缓存组。
	 */
	public static String VIDEO_GROUP = "video";

	/**
	 * zhul 2006-09-16聊天室聊客组
	 */
	public static String ROOM_CHATER_GROUP = "roomChater";

	/**
	 * zhul 2006-09-16聊天室ID组
	 */
	public static String ROOM_ID_GROUP = "roomId";

	/**
	 * ZHUL 2006-09-18 不在本聊天室的人
	 */
	public static String OUT_ROOM_GROUP = "outRoomPeop";

	/**
	 * liuyi 2006-10-28 交友广告回复数缓存组
	 */
	public static String FRIEND_ADV_COUNT_GROUP = "friendAdvCount";

	public static int FRIEND_ADV_COUNT_FLUSH_PERIOD = 30 * 60;
	
	
	/**
	 * liuyi 2006-10-28 北京浮生记排行榜缓存组
	 */
	public static String BJFS_USER_TOP_COUNT_GROUP = "bjfsTop";

	public static int BJFS_USER_TOP_FLUSH_PERIOD =  60 * 60;
	
	/**
	 * liuyi 2006-10-28 大富豪游戏缓存
	 */
	public static String WGAME_PK_BIG_GROUP = "wGamePKBig";

	public static int WGAME_PK_BIG_FLUSH_PERIOD =  10 * 60;
	
	/**
	 * macq_2007-5-8_新版本股票系统缓存组
	 */
	public static String STOCK2_TOP_COUNT_GROUP = "newStock2Top";

	public static int STOCK2_TOP_FLUSH_PERIOD =  1 * 60;

	/**
	 * liuyi 2006-12-02 在线用户id列表缓存组
	 */
	public static String ONLINE_USER_IDS_GROUP = "onlineUserIds";

	public static int ONLINE_USER_IDS_FLUSH_PERIOD = 4 * 60;

	/**
	 * liuyi 2006-12-20 在线用户活动session数缓存组
	 */
	public static String ONLINE_USER_COUNT_GROUP = "onlineUserCount";

	public static int ONLINE_USER_COUNT_FLUSH_PERIOD = 2 * 60;

	/**
	 * liuyi 2006-12-20 手机号－用户ID缓存组
	 */
	public static String USER_ID_GROUP = "userId";

	public static int USER_ID_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-12-28 排行榜缓存组
	 */
	public static String TOP_GROUP = "top";

	public static int TOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-10-28 结义酒缓存组
	 */
	public static String FRIEND_DRINK_GROUP = "friendDrink";

	public static int FRIEND_DRINK_FLUSH_PERIOD = 24 * 60 * 60;

	public static String FRIEND_DRINK_BEAN_GROUP = "friendDrinkBean";

	public static int FRIEND_DRINK_BEAN_FLUSH_PERIOD = 24 * 60 * 60;

	// 宾客缓存

	public static String FRIEND_GUEST_BEAN_GROUP = "friendGuestBean";

	public static int FRIEND_GUEST_FLUSH_PERIOD = 60 * 60;

	// 宾客动作缓存

	public static String FRIEND_ACTION_BEAN_GROUP = "friendActionBean";

	public static int FRIEND_ACTION_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-10-17 用户好友列表缓存组
	 */
	public static String ROOM_ONLINE_USER = "roomOnlinUser";

	/**
	 * liuyi 2006-10-29 用户等级排行缓存组
	 */
	public static String RANK_TOP_GROUP = "rankTopGroup";

	public static int RANK_TOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-11-29 交友广告缓存组
	 */
	public static String FRIEND_ADV_GROUP = "friendAdvGroup";

	public static int FRIEND_ADV_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2006-11-29 交友广告回复缓存组
	 */
	public static String FRIEND_ADV_MESSAGE_GROUP = "friendAdvMessageGroup";

	public static int FRIEND_ADV_MESSAGE_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2006-11-29 交友广告回复列表缓存组
	 */
	public static String FRIEND_ADV_MESSAGE_LIST_GROUP = "friendAdvMessageListGroup";

	public static int FRIEND_ADV_MESSAGE_LIST_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2006-11-29 交友广告列表缓存组
	 */
	public static String FRIEND_ADV_LIST_GROUP = "friendAdvListGroup";

	public static int FRIEND_ADV_LIST_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2006-11-29 交友搜索列表缓存组
	 */
	public static String FRIEND_ADV_SEARCH_LIST_GROUP = "friendAdvSearchListGroup";

	public static int FRIEND_ADV_SEARCH_LIST_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2006-10-29 用户社交指数排行缓存组
	 */
	public static String SOCIAL_TOP_GROUP = "socialTopGroup";

	public static int SOCIAL_TOP_FLUSH_PERIOD = 24 * 60 * 60;
	
	/**
	 * macq 2007-5-10 pk系统杀人排行榜缓存组
	 */
	public static String PK_USER_KTOP_GROUP = "pkUserKTop";

	public static int PK_USER_KTOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-10-29 反倭指数排行缓存组
	 */
	public static String SPIRIT_TOP_GROUP = "spiritTopGroup";

	public static int SPIRIT_TOP_FLUSH_PERIOD = 24 * 60 * 60;

	// macq_2006-12-27_城邦基金缓存组_start
	public static String TONG_USER_FUND_TOP_GROUP = "tongUserFundTop";

	public static int TONG_USER_FUND_TOP_FLUSH_PERIOD = 8 * 60 * 60;

	// macq_2006-12-27_城邦基金缓存组_end

	/**
	 * liuyi 2006-10-29 股票排行缓存组
	 */
	public static String STOCK_TOP_GROUP = "stockTopGroup";

	public static int STOCK_TOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-10-29 慈善排行缓存组
	 */
	public static String CHARITY_TOP_GROUP = "charityTopGroup";

	public static int CHARITY_TOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-10-29 乐币排行缓存组
	 */
	public static String GAMEPOINT_TOP_GROUP = "gamepointTopGroup";

	public static int GAMEPOINT_TOP_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-12-04 按手机号查用户缓存组
	 */
	public static String USER_BY_MOBILE_GROUP = "userByMobileGroup";

	public static int USER_BY_MOBILE_FLUSH_PERIOD = 4 * 60 * 60;

	/**
	 * liuyi 2006-12-06 链接缓存组
	 */
	public static String LINK_GROUP = "linkGroup";

	public static int Link_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2006-12-06 链接缓存组
	 */
	public static String LINK_LIST_GROUP = "linkListGroup";

	public static int LINK_LIST_FLUSH_PERIOD = 24 * 60 * 60;

	/**
	 * liuyi 2007-01-25 城墙沦陷缓存组
	 */
	public static String TONG_DESTROY_GROUP = "tongDestroyGroup";

	public static int TONG_DESTROY_FLUSH_PERIOD = 60 * 60;

	/**
	 * liuyi 2007-01-25 城墙沦陷列表缓存组
	 */
	public static String TONG_DESTROY_LIST_GROUP = "tongDestroyListGroup";

	public static int TONG_DESTROY_LIST_FLUSH_PERIOD = 60 * 60;
	
	/**
	 * liuyi 2007-02-08 帮会同盟组
	 */
	public static String FRIEND_TONG_GROUP = "friendTongGroup";

	public static int FRIEND_TONG_FLUSH_PERIOD = 60 * 60;

	/**
	 * wucx 2006-10-26
	 */
	public static String FRIEND_RING_GROUP = "friendRing";

	/**
	 * 送糖果缓存
	 */
	public static String CANDY_GROUP = "candyGroup";

	/**
	 * 跑马赔率缓存
	 */
	public static String HANDBOOKINGER_GROUP = "candyGroup";

	/**
	 * 跑马赔率缓存
	 */

	public static int HANDBOOKINGER_FLUSH_PERIOD = 60 * 60;

	/**
	 * 跑马赔率缓存
	 */
	public static String CARTOON_GROUP = "cartoonGroup";

	/**
	 * 跑马赔率缓存
	 */

	public static int CARTOON_FLUSH_PERIOD = 24 * 60 * 60;

	public static int ROOM_ONLINE_USER_FLUSH_PERIOD = 10 * 60;

	/**
	 * zhul_20006-09-18 不在本聊天室 的人缓存时间
	 */
	public static int OUTROOM_FLUSH_PERIOD = 3 * 60;

	/**
	 * zhul 2006-09-16聊天室ID组缓存时间
	 */
	public static int ROOMID_FLUSH_PERIOD = 5 * 60;

	/**
	 * zhul 2006-09-16聊天室聊客缓存时间
	 */
	public static int CHATER_FLUSH_PERIOD = 3 * 60;

	// ZHUL 2006-10-10 推荐日记、相册的缓存组
	public static String COMMEND_DIARYANDPHOTO_GROUP = "commendDiaryAndPhoto";

	// zhul 2006-10-10 推荐日记的缓存周期
	public static int COMMEND_DIARYANDPHOTO_FLUSH_PERIOD = 60 * 60;

	// zhul 2006-10-10 乐客日记相册组
	public static String HOME_DIARYANDPHOTO_GROUP = "homeDiaryAndPhoto";

	// zhul 2006-10-10 乐客日记相册缓存周期
	public static int HOME_DIARYANDPHOTO_FLUSH_PERIOD = 15 * 60;

	// mcq_2006_9_13_增加铃声缓存_start
	/**
	 * 
	 * 铃声缓存组。
	 */
	public static String RING_GROUP = "ring";

	// mcq_2006_9_13_增加铃声缓存_end

	// mcq_2006_9_13_家园历史访问量缓存组_start
	/**
	 * 
	 * 家园历史访问量缓存组。
	 */
	public static String HOME_HITS_GROUP = "homeHits";

	// mcq_2006_9_13_家园历史访问量缓存组_end

	/**
	 * 
	 * 家园日记排名缓存组。
	 */
	public static String HOME_DIARY_GROUP = "homeDiary";

	/**
	 * 
	 * 家园照片排名缓存组。
	 */
	public static String HOME_PHOTO_GROUP = "homePhoto";

	// mcq_2006_9_13_家园历史访问量缓存组_end

	/**
	 * 树状页面缓存组。
	 */
	public static String COLUMN_GROUP = "column";

	/**
	 * 聊天室管理员列表缓存组。
	 */
	public static String ROOM_MANAGERS_GROUP = "roomManagers";

	/**
	 * 静国神社缓存组。
	 */
	public static String SPIRIT_GROUP = "spirit";

	/**
	 * 我的邻居新日记缓存。
	 */
	public static String NEIGHBOR_DIARY_GROUP = "neighborDiary";

	/**
	 * 我的邻居新相片缓存组 。
	 */
	public static String NEIGHBOR_PHOTO_GROUP = "neighborPhoto";

	/**
	 * 系统消息ID缓存组 。
	 */
	public static String SYSTEM_NOTICE_GROUP = "systemNotice";

	/**
	 * 帮会消息ID缓存组 。
	 */
	public static String TONG_SYSTEM_NOTICE_GROUP = "tongSystemNotice";

	/**
	 * 系统消息读取记录缓存组 。
	 */
	public static String SYSTEM_NOTICE_READED_GROUP = "systemNoticeReaded";

	/**
	 * 即将举行的三个婚礼 。
	 */
	public static String MARRIAGE_TO_GROUP = "marriageTo";

	/**
	 * 正在举行的三个婚礼 。
	 */
	public static String MARRIAGE_NOW_GROUP = "marriageNow";

	/**
	 * 婚礼录像的三个婚礼 。
	 */
	public static String MARRIAGE_KINESCOPE_GROUP = "marriageKinescope";

	/**
	 * 大盘适时指数 。
	 */
	public static String GRAIL_TIME_GROUP = "grailTime";

	/**
	 * 股票实时指数 。
	 */
	public static String STOCK_TIME_GROUP = "stockTime";

	/**
	 * 股票昨日，今日价格 。 。
	 */
	public static String STOCK_GROUP = "stock";

	/**
	 * 用户信息的股票市值 。
	 */
	public static int USER_STOCK_FLUSH_PERIOD = 300;

	/**
	 * 用户信息的股票市值 。
	 */
	public static String USER_STOCK_GROUP = "userStock";

	/**
	 * 系统消息读取记录缓存时间 。
	 */
	public static int SYSTEM_NOTICE_READED_FLUSH_PERIOD = 3600;

	/**
	 * 聊天室记录缓存组 。
	 */
	public static String ROOM_BEAN_CACHE_GROUP = "roomBean";

	/**
	 * 聊天室记录缓存时间 。
	 */
	public static int ROOM_BEAN_CACHE_FLUSH_PERIOD = 3600;

	/**
	 * 系统消息ID缓存时间 。
	 */
	public static int SYSTEM_NOTICE_FLUSH_PERIOD = 3600;

	/**
	 * 帮会系统消息ID缓存时间 。
	 */
	public static int TONG_SYSTEM_NOTICE_FLUSH_PERIOD = 3600;

	/**
	 * 用户消息ID缓存时间 。
	 */
	public static int USER_GENERAL_NOTICE_FLUSH_PERIOD = 3600;

	/**
	 * 
	 * 友链信息缓存。
	 */
	public static String FRIEND_LINK_GROUP = "frindLink";

	/**
	 * 出气筒 。
	 */
	public static int ANGER_FLUSH_PERIOD = 3600;

	/**
	 * 
	 * 出气筒缓存。
	 */
	public static String ANGER_GROUP = "anger";

	/**
	 * 交友系统缓存。
	 */
	public static String FRIEND_CACHE_GROUP = "friendCity";

	/**
	 * 交友系统缓存时间 。
	 */
	public static int FRIEND_CACHE_FLUSH_PERIOD = 60 * 15;

	/**
	 * 银行系统缓存。
	 */
	public static String BANK_CACHE_GROUP = "bank";

	/**
	 * 银行系统缓存时间 。
	 */
	public static int BANK_CACHE_FLUSH_PERIOD = 60 * 15;

	/**
	 * macq_2007-1-2_城帮ID记录缓存
	 */
	public static String TONG_ID_LIST_CACHE_GROUP = "tongList";

	/**
	 * macq_2007-1-2_城帮ID记录缓存时间 。
	 */
	public static int TONG_ID_LIST_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮排名记录缓存
	 */
	public static String TONG_COMPOSITOR_CACHE_GROUP = "tongCompositor";

	/**
	 * macq_2007-1-2_城帮排名记录缓存时间 。
	 */
	public static int TONG_COMPOSITOR_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮记录缓存
	 */
	public static String TONG_CACHE_GROUP = "tong";

	/**
	 * macq_2007-1-2_城帮记录缓存时间 。
	 */
	public static int TONG_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮会员记录缓存
	 */
	public static String TONG_USER_ID_LIST_CACHE_GROUP = "tongUserList";

	/**
	 * macq_2007-1-2_城帮基金记录缓存
	 */
	public static String TONG_FUND_CACHE_GROUP = "tongFund";

	/**
	 * macq_2007-1-2_城帮基金记录缓存时间 。
	 */
	public static int TONG_FUND_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮基金记录缓存
	 */
	public static String TONG_FUND_BY_ID_CACHE_GROUP = "tongFundId";

	/**
	 * macq_2007-1-2_城帮基金记录缓存时间 。
	 */
	public static int TONG_FUND_BY_ID_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮会员在线排序记录缓存
	 */
	public static String TONG_USER_ONLINE_CACHE_GROUP = "tongUserOnlineList";

	/**
	 * macq_2007-1-2_城帮会员在线排序记录缓存时间 。
	 */
	public static int TONG_USER_ONLINE_FLUSH_PERIOD = 2 * 60;
	
	/**
	 * macq_2007-1-2_城帮会员在线排序记录缓存
	 */
	public static String TONG_TRANSFER_TONG_ID_CACHE_GROUP = "transferTongId";
	/**
	 * guip_2007-12-23_城帮结盟记录缓存
	 */
	public static String TONG_ALLY_CACHE_GROUP ="tongAlly";
	/**
	 * macq_2007-1-2_城帮会员在线排序记录缓存时间 。
	 */
	public static int TONG_TRANSFER_TONG_ID_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2007-1-2_城帮基金用户Id缓存
	 */
	public static String TONG_FUND_ID_LIST_CACHE_GROUP = "tongFundList";

	/**
	 * macq_2007-1-2_城帮基金用户Id缓存时间 。
	 */
	public static int TONG_FUND_ID_LIST_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮会员记录缓存时间 。
	 */
	public static int TONG_USER_ID_LIST_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮当铺记录缓存
	 */
	public static String TONG_HOCKSHOP_CACHE_GROUP = "tongHockshop";

	/**
	 * macq_2007-1-2_城帮地点记录缓存时间 。
	 */
	public static int TONG_LOCATION_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2007-1-2_城帮地点记录缓存
	 */
	public static String TONG_LOCATION_CACHE_GROUP = "tongLocation";

	/**
	 * macq_2007-1-2_城帮当铺记录缓存时间 。
	 */
	public static int TONG_HOCKSHOP_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2006-12-13_用户拍卖物品ID列表缓存
	 */
	public static String USER_AUCTION_CACHE_GROUP = "userAuction";

	/**
	 * macq_2006-12-13_用户拍卖物品ID列表缓存时间 。
	 */
	public static int USER_AUCTION_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2006-12-13_用户拍卖物品记录缓存
	 */
	public static String USER_AUCTION_BY_ID_CACHE_GROUP = "userAuctionById";

	/**
	 * macq_2006-12-13_用户拍卖物品记录缓存缓存时间 。
	 */
	public static int USER_AUCTION_BY_ID_FLUSH_PERIOD = 60 * 5;
	
	/**
	 * macq_2006-12-13_用户拍卖物品记录缓存
	 */
	public static String LHC_RESULT_SET_CACHE_GROUP = "userAuctionById";

	/**
	 * macq_2006-12-13_用户拍卖物品记录缓存缓存时间 。
	 */
	public static int LHC_RESULT_SET_FLUSH_PERIOD = 60*60*24;
	
	
	/**
	 * macq_2006-12-13_用户拍卖物品记录缓存
	 */
	public static String FRIEND_CENTER_SEEINTRODUCTION = "seeIntroduction";

	public static int FRIEND_CENTER_SEEINTRODUCTION_FLUSH_PERIOD = 60 * 10;

	/**
	 * macq_2006-12-18_用户拍卖物品历史记录ID列表缓存
	 */
	public static String USER_AUCTION_HISTORY_BY_ID_CACHE_GROUP = "userAuctionHistoryById";

	/**
	 * macq_2006-12-18_用户拍卖物品历史记录ID列表缓存时间 。
	 */
	public static int USER_AUCTION_HISTORY_BY_ID_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2006-12-18_用户拍卖物品历史记录缓存
	 */
	public static String USER_AUCTION_HISTORY_CACHE_GROUP = "userAuctionHistory";

	/**
	 * macq_2006-12-18_用户拍卖物品记录缓存缓存时间 。
	 */
	public static int USER_AUCTION_HISTORY_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2006-12-13_用户使用道具显示在公聊大厅的缓存
	 */
	public static String USER_BAG_POST_CACHE_GROUP = "userBagPost";

	/**
	 * macq_2006-12-13_用户使用道具显示在公聊大厅的缓存时间 。
	 */
	public static int USER_BAG_POST_FLUSH_PERIOD = 60 * 5;

	/**
	 * macq_2006-12-13_用户行囊物品ID缓存
	 */
	public static String USER_BAG_LIST_CACHE_GROUP = "userBagList";
	
	/**
	 * guip_2007-09-16_用户行囊赠送物品缓存
	 */
	public static String USER_BAG_ITEM_CACHE_GROUP = "userBagItem";
	
	/**
	 * guip_2007-09-16_用户银行log缓存
	 */
	public static String BANK_LOG_CACHE_GROUP = "bankLog";
	/**
	 * guip_2007-09-16_用户银行log缓存时间
	 */
	public static int BANK_LOG_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-12-13_用户行囊物品ID缓存时间 。
	 */
	public static int USER_BAG_LIST_FLUSH_PERIOD = 60 * 30;

	/**
	 * 交友广告缓存。
	 */
	public static String FRIEND_ADVER_CACHE_GROUP = "friendAdver";

	/**
	 * 交友广告缓存时间 。
	 */
	public static int FRIEND_ADVER_CACHE_FLUSH_PERIOD = 60 * 15;

	/**
	 * macq_2006-11-23_新手趣味问题答题库缓存组
	 */
	public static String BEGINNER_QUESTION_CACHE_GROUP = "beginnerQuestion";

	/**
	 * macq_2006-11-23_新手趣味问题答题库缓存时间
	 */
	public static int BEGINNER_QUESTION_CACHE_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-11-23_新手热心用户缓存组
	 */
	public static String BEGINNER_HELP_CACHE_GROUP = "beginnerHelp";

	/**
	 * macq_2006-11-24_新手热心用户缓存时间
	 */
	public static int BEGINNER_HELP_CACHE_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-11-24_慈善基金历史记录缓存组
	 */
	public static String CHARITARIAN_CACHE_GROUP = "Charitarian";

	/**
	 * macq_2006-11-23_慈善基金历史记录缓存时间
	 */
	public static int CHARITARIAN_CACHE_FLUSH_PERIOD = 10 * 60;

	/**
	 * macq_2006-11-30_用户家园缓存组
	 */
	public static String HOME_USER_CACHE_GROUP = "homeUser";

	public static String HOME_USER_PHONE_CACHE_GROUP = "homePhone";
	/**
	 * macq_2006-11-30用户家园记录缓存时间
	 */
	public static int HOME_USER_FLUSH_PERIOD = 60 * 15;

	/**
	 * macq_2006-11-30_家园外观缓存组
	 */
	public static String HOME_FACE_CACHE_GROUP = "homeFace";

	/**
	 * macq_2006-11-30_家园外观缓存时间
	 */
	public static int HOME_FACE_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-12-12_虚拟物品缓存组
	 */
	public static String DUMMY_CACHE_GROUP = "dummy";

	/**
	 * macq_2006-12-12_虚拟物品缓存时间
	 */
	public static int DUMMY_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-11-30_家园家具缓存组
	 */
	public static String HOME_FITMENT_CACHE_GROUP = "homeFitment";

	/**
	 * macq_2006-11-30_家园家具缓存时间
	 */
	public static int HOME_FITMENT_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2007-3-30_论坛中最新帮会帖子缓存组
	 */
	public static String TONG_FORUM_NEW_CACHE_GROUP = "tongForumNew";
	public static int TONG_FORUM_NEW_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-11-30_用户破坏城墙缓存组
	 */
	public static String TONG_CITY_RECORD_TONG_ID_CACHE_GROUP = "tongCityRecordTongId";

	/**
	 * macq_2006-11-30_用户破坏城墙缓存时间
	 */
	public static int TONG_CITY_RECORD_TONG_ID_FLUSH_PERIOD = 60 * 30;

	/**
	 * macq_2006-11-30_家园家具类型缓存组
	 */
	public static String HOME_FITMENT_TYPE_CACHE_GROUP = "homeFitmentType";

	/**
	 * macq_2006-11-30_家园家具类型缓存时间
	 */
	public static int HOME_FITMENT_TYPE_FLUSH_PERIOD = 60 * 60;

	/**
	 * macq_2006-11-30_家园房间缓存组
	 */
	public static String HOME_ROOM_CACHE_GROUP = "homeRoom";

	/**
	 * macq_2006-11-30家园房间缓存时间
	 */
	public static int HOME_ROOM_FLUSH_PERIOD = 60 * 60;

	/**
	 * CATALOGBEAN缓存组 。
	 */
	public static String CATALOG_GROUP = "catalog";

	/**
	 * 
	 * 新闻缓存时间。
	 */
	public static int NEWS_FLUSH_PERIOD = 3600;

	/**
	 * 游戏缓存时间。
	 */
	public static int GAME_FLUSH_PERIOD = 3600;

	/**
	 * 视频缓存时间。
	 */
	public static int VIDEO_FLUSH_PERIOD = 3600;

	// mcq_2006_9_13_增加铃声缓存_start
	/**
	 * 铃声缓存时间。
	 */
	public static int RING_FLUSH_PERIOD = 3600;

	// mcq_2006_9_13_增加铃声缓存_end

	/**
	 * 树状页面缓存时间。
	 */
	public static int COLUMN_FLUSH_PERIOD = 3600;

	/**
	 * 家园历史访问量缓存时间。
	 */
	public static int HOME_HITS_FLUSH_PERIOD = 3600;

	/**
	 * 用户属性缓存时间。
	 */
	public static int USER_STATUS_FLUSH_PERIOD = 3600 * 24;

	/**
	 * 聊天记录缓存时间。
	 */
	public static int ROOM_CONTENT_FLUSH_PERIOD = 3600;

	/**
	 * 聊天室管理员列表查询缓存时间。
	 */
	public static int ROOM_MANAGERS_FLUSH_PERIOD = 900;

	/**
	 * 聊天室管理员查询缓存时间。
	 */
	public static int ROOM_MANAGER_FLUSH_PERIOD = 900;

	/**
	 * 静国神社 缓存时间 。
	 */
	public static int SPIRIT_FLUSH_PERIOD = 3600;

	/**
	 * 家园日记排名 缓存时间 。
	 */
	public static int HOME_DIARY_FLUSH_PERIOD = 60 * 10;

	/**
	 * 家园照片排名 缓存时间 。
	 */
	public static int HOME_PHOTO_FLUSH_PERIOD = 60 * 10;

	/**
	 * 我的邻居新日记缓存时间。
	 */
	public static int NEIGHBOR_DIARY_FLUSH_PERIOD = 900;

	/**
	 * 我的邻居新相片缓存时间 。
	 */
	public static int NEIGHBOR_PHOTO_FLUSH_PERIOD = 900;

	/**
	 * 友链信息缓存时间。
	 */
	public static int FRIEND_LINK_FLUSH_PERIOD = 3600;

	/**
	 * catalogbean缓存时间 。
	 */
	public static int CATALOG_FLUSH_PERIOD = 3600;

	/**
	 * 是否使用缓存。
	 */
	public static boolean USE_CACHE = true;

	/**
	 * wucx 2006-10-26 结婚戒指缓存周期
	 * 
	 */
	public static final int FRIEND_RING_FLUSH_PERIOD = 3600 * 24;

	/**
	 * 吃糖果缓存时间
	 */
	public static final int CANDY_FLUSH_PERIOD = 3600 * 24;

	/**
	 * 正在举行的三个婚礼
	 * 
	 */
	public static final int MARRIAGE_NOW_FLUSH_PERIOD = 900;

	/**
	 * 即将举行的三个婚礼
	 */
	public static final int MARRIAGE_TO_FLUSH_PERIOD = 900;

	/**
	 * 结婚录像的三个婚礼
	 */
	public static final int MARRIAGE_KINESCOPE_FLUSH_PERIOD = 900;

	/**
	 * 大盘适时指数 。
	 */
	public static int GRAIL_TIME_FLUSH_PERIOD = 300;

	/**
	 * 股票实时指数 。
	 */
	public static int STOCK_TIME_FLUSH_PERIOD = 300;

	/**
	 * 股票昨日，今日价格 。 。
	 */
	public static int STOCK_FLUSH_PERIOD = 3600;

	/**
	 * 乐酷论坛缓存。
	 */
	public static String FORUM_CACHE_GROUP = "forum";

	/**
	 * 乐酷论坛缓存时间 。
	 */
	public static int FORUM_CACHE_FLUSH_PERIOD = 60 * 15;

	/**
	 * 梅图修长新贴总数缓存。
	 */
	public static String NEW_PIC_GROUP = "newPic";

	/**
	 * 梅图修长新贴总数缓存时间 。
	 */
	public static int NEW_PIC_FLUSH_PERIOD = 60 * 15;
	
	/**
	 * guip_2007-08-28_踩楼缓存组
	 */
	public static String FLOOR_CACHE_GROUP = "floor";

	/**
	 * guip_2007-08-28_踩楼缓存时间
	 */
	public static int FLOOR_FLUSH_PERIOD = 60 * 60;

	/**
	 * guip_2007-08-28_踩楼排行榜缓存组
	 */
	public static String FLOORTOP_CACHE_GROUP = "floorTop";

	/**
	 * guip_2007-08-28_踩楼排行榜缓存时间
	 */
	public static int FLOORTOP_FLUSH_PERIOD = 60 * 60;
	
	/**
	 * guip_2007-11-02_乐酷街霸缓存组
	 */
	public static String FIGHT_CACHE_GROUP = "fight";
	
	/**
	 * guip_2007-11-02_乐酷街霸缓存时间
	 */
	public static int FIGHT_FLUSH_PERIOD = 60 * 60;
	/**
	 * guip_2007-09-28_银行log缓存组
	 */
	public static String MONEYLOG_CACHE_GROUP = "moneyLog";

	/**
	 * guip_2007-09-28_银行log缓存时间
	 */
	public static int MONEYLOG_FLUSH_PERIOD = 60 * 60;
	/**
	 * 加入缓存。
	 * 
	 * @param key
	 * @param value
	 * @param group
	 */
	public static void put(String key, Object value, String group) {
		// liuyi 2006-09-15 对于null的group，取默认的group
		if (key == null || value == null) {
			return;
		}

		CacheAdmin.putInCache(key, value, group);
	}

	/**
	 * 从缓存中取出来。
	 * 
	 * @param key
	 * @param flushPeriod
	 * @return
	 */
	public static Object get(String key, int flushPeriod) {
		return get(key, null, flushPeriod);
	}

	/**
	 * liuyi 2006-09-15 从缓存里面取出来
	 * 
	 * @param key
	 * @param group
	 * @param flushPeriod
	 * @return
	 */
	public static Object get(String key, String group, int flushPeriod) {
		// liuyi 2006-09-15 对于null的group，取默认的group
		if (key == null) {
			return null;
		}

		return CacheAdmin.getFromCache(key, group, flushPeriod);
	}

	/**
	 * 清空一个缓存组。
	 * 
	 * @param group
	 */
	public static void flushGroup(String group) {
		if (group == null) {
			return;
		}
		CacheAdmin.flushGroup(group);
	}

	/**
	 * 清空所有缓存。
	 * 
	 */
	public static void flushAll() {
		CacheAdmin.flushAll();
	}

	/**
	 * 清空一个缓存组。
	 * 
	 * @param group
	 */
	public static void flushGroup(String group, String key) {
		if (group == null || key == null) {
			return;
		}
		CacheAdmin.getCacheMap(group).remove(key);
	}

	public static Hashtable getGroup(String group) {
		if (group == null)
			return null;
		return CacheAdmin.getCacheMap(group);
	}
}
