package net.joycool.wap.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import net.joycool.wap.bean.CrownBean;
import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.PositionBean;
import net.joycool.wap.bean.RankBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.RoomInviteRankBean;
import net.joycool.wap.bean.friend.FriendVoteBean;
import net.joycool.wap.bean.friendlink.RandomLinkBean;
import net.joycool.wap.bean.friendlink.RandomSubLinkBean;
import net.joycool.wap.bean.home.HomeImageBean;
import net.joycool.wap.bean.job.CardBean;
import net.joycool.wap.bean.job.HuntQuarryAppearRateBean;
import net.joycool.wap.bean.pk.PKUserBean;
import net.joycool.wap.bean.top.MoneyTopBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IFriendLinkService;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IPKService;
import net.joycool.wap.service.infc.ITopService;
import net.joycool.wap.service.infc.IUserService;

public class LoadResource {
	// private static HashMap positionMap = null;

	public static HashMap rankMap = null;

	public static HashMap positionNameMap = null;

	public static HashMap ageMap = null;

	public static HashMap positionNameInfo = null;

	public static PositionBean position = null;

	public static Vector title;

	public static TreeMap cardMap = null;

	public static TreeMap linkMap = null;

	public static TreeMap linkSubMap = null;

	public static HashMap weaponMap = null;

	public static HashMap quarryMap = null;

	public static TreeMap arrowMap = null;

	public static TreeMap handGunMap = null;

	public static TreeMap huntGunMap = null;

	public static TreeMap ak47Map = null;
	
	public static TreeMap awpMap = null;

	public static LinkedHashMap mobileMap = null;

	public static Vector ringSearchList = null;

	public static Vector userMoneyList = null;

	public static Vector userRankList = null;

	public static Vector userSocialList = null;
	
	public static Vector pkUserKTopList = null;

	public static Vector userSpiritList = null;

	public static Vector friendPhotoManList = null;

	public static Vector friendPhotoWomanList = null;

	public static Vector charitarianList = null;

	public static Vector userStockList = null;

	public static Vector inviteRankList = null; // 上周群英排行榜榜单

	public static HashMap homeImageBigMap = null;

	public static HashMap homeImageSmallMap = null;

	public static HashMap homeUserImageMap = null;

	public static HashMap homeUserHomeImageMap = null;

	public static HashMap horseMap = null;

	private static Object userImageLock = new Object();

	// zhul 2006-09-21 星座
	public static HashMap constellation;

	// zhul 2006-09-21 个性
	public static HashMap personality;

	// zhul 2006-09-21 星座婚姻状况
	public static HashMap marriage;

	// zhul 2006-09-21 交友目的
	public static HashMap friendAim;

	// 创建工厂
	public static IUserService userService = ServiceFactory.createUserService();
	
	public static IPKService PKService = ServiceFactory.createPKService();

	public static IHomeService homeService = ServiceFactory.createHomeService();

	public static ITopService topService = ServiceFactory.createTopService();

	public static IFriendService friendService = ServiceFactory
			.createFriendService();

	public static ICharitarianService charitarianService = ServiceFactory
			.createCharitarianService();
	
	public static ICacheMap stuffCache = CacheManage.stuff;
	/*
	 * public static synchronized HashMap getPositionMap() { if (positionMap !=
	 * null) { return positionMap; } positionMap = new HashMap();
	 * positionMap.put("chat", new Integer(Constants.POSITION_CHAT));
	 * positionMap.put("wgame", new Integer(Constants.POSITION_WGAME));
	 * positionMap.put("wgamepk", new Integer(Constants.POSITION_WGAME));
	 * positionMap.put("wgamehall", new Integer(Constants.POSITION_WGAME));
	 * positionMap.put("ebook", new Integer(Constants.POSITION_EBOOK));
	 * positionMap.put("image", new Integer(Constants.POSITION_IMAGE));
	 * positionMap.put("news", new Integer(Constants.POSITION_NEWS));
	 * positionMap.put("game", new Integer(Constants.POSITION_GAME));
	 * positionMap.put("job", new Integer(Constants.POSITION_JOB)); return
	 * positionMap; }
	 */
	public static HashMap getHorse() {
		if (horseMap != null) {
			return horseMap;
		}
		horseMap = new HashMap();
		horseMap.put("0", "绝地");
		horseMap.put("1", "翻羽");
		horseMap.put("2", "奔宵");
		horseMap.put("3", "越影");
		horseMap.put("4", "逾辉");
		horseMap.put("5", "超光");
		horseMap.put("6", "腾雾");
		horseMap.put("7", "挟翼");
		horseMap.put("8", "骅骝");
		horseMap.put("9", "绿耳");
		horseMap.put("10", "盗骊");
		horseMap.put("11", "骐骥");
		horseMap.put("12", "纤离");
		horseMap.put("13", "翠龙");
		horseMap.put("14", "玉马");
		horseMap.put("15", "骕骦");
		horseMap.put("16", "沙丘");
		horseMap.put("17", "追风");
		horseMap.put("18", "白兔");
		horseMap.put("19", "蹑景");
		horseMap.put("20", "追电");
		horseMap.put("21", "飞翩");
		horseMap.put("22", "铜爵");
		horseMap.put("23", "晨凫");
		horseMap.put("24", "楚骓");
		horseMap.put("25", "九逸");
		horseMap.put("26", "天马");
		horseMap.put("27", "宛天");
		horseMap.put("28", "步景");
		horseMap.put("29", "大骊");
		horseMap.put("30", "赤兔");
		horseMap.put("31", "的卢");
		horseMap.put("32", "绝影");
		horseMap.put("33", "紫骍");
		horseMap.put("34", "惊帆");
		horseMap.put("35", "扬武");
		horseMap.put("36", "赭白");
		horseMap.put("37", "龙骧");
		horseMap.put("38", "黄骝");
		return horseMap;
	}

	public static HashMap getConstellation() {
		if (constellation != null) {
			return constellation;
		}

		constellation = new HashMap();
		constellation.put("0", "白羊座");
		constellation.put("1", "金牛座");
		constellation.put("2", "双子座");
		constellation.put("3", "巨蟹座");
		constellation.put("4", "狮子座");
		constellation.put("5", "处女座");
		constellation.put("6", "天秤座");
		constellation.put("7", "天蝎座");
		constellation.put("8", "人马座");
		constellation.put("9", "摩羯座");
		constellation.put("10", "宝瓶座");
		constellation.put("11", "双鱼座");

		return constellation;
	}

	public static HashMap getPersonality() {
		if (personality != null) {
			return personality;
		}

		personality = new HashMap();
		personality.put("0", "温柔体贴");
		personality.put("1", "活泼开朗");
		personality.put("2", "古灵精怪");
		personality.put("3", "憨厚老实");
		personality.put("4", "豪情奔放");
		personality.put("5", "天真淳朴");

		return personality;
	}

	public static HashMap getMarriage() {
		if (marriage != null)
			return marriage;
		marriage = new HashMap();
		marriage.put("0", "未婚");
		marriage.put("1", "已婚");
		marriage.put("2", "离异");
		marriage.put("3", "丧偶");
		return marriage;
	}

	public static HashMap getFriendAim() {
		if (friendAim != null)
			return friendAim;
		friendAim = new HashMap();
		friendAim.put("0", "恋人");
		friendAim.put("1", "知己");
		friendAim.put("2", "玩伴");
		friendAim.put("3", "解闷");
		friendAim.put("4", "其他");
		return friendAim;
	}

	public static HashMap getRankMap() {
		if (rankMap != null) {
			return rankMap;
		}
		rankMap = new HashMap();
		Vector rankList = userService.getRankList(null);
		Iterator itr = rankList.iterator();
		while (itr.hasNext()) {
			RankBean rank = (RankBean) itr.next();
			rankMap.put(new Integer(rank.getRankId()), rank);
		}
		return rankMap;
	}

	public static void addPosition(int id, String name, String url, String pos) {
		PositionBean position = new PositionBean();
		position.setId(id);
		position.setPositionName(name);
		position.setPositionInfo(name);
		position.setUrl(url);
		positionNameMap.put(new Integer(id), position);
		if(pos != null)
			positionNameInfo.put(pos, position);
		
	}
	
	public static void addPosition(int id, String name, String info, String url, String pos) {
		PositionBean position = new PositionBean();
		position.setId(id);
		position.setPositionName(name);
		position.setPositionInfo(info);
		position.setUrl(url);
		positionNameMap.put(new Integer(id), position);
		if(pos != null)
			positionNameInfo.put(pos, position);
	}
	/*
	 * 
	 * 功能:初始化用户位置信息 mcq_2006-6-20
	 */
	public static void loadPosition() {
		synchronized(LoadResource.class) {
			positionNameMap = new HashMap();
			positionNameInfo = new HashMap();
			addPosition(0, "闲逛中", BaseAction.INDEX_URL, null);
			addPosition(1, "聊天", "/chat/hall.jsp", "chat");
			addPosition(2, "玩游戏", "游戏", "/lswjs/index.jsp", "wgame");
			addPosition(2, "玩游戏", "游戏", "/lswjs/index.jsp", "wgamehall");
			addPosition(2, "玩游戏", "游戏", "/lswjs/index.jsp", "wgamepk");
			addPosition(3, "电子书", "电子书", "/Column.do?columnId=5185", "ebook");
			addPosition(4, "图片", "图片", "/Column.do?columnId=4381", "image");
			addPosition(5, "新闻", "新闻", "/Column.do?columnId=3840", "news");
			addPosition(6, "下游戏", "/Column.do?columnId=5186", "game");
			addPosition(7, "打工赚钱", "/job/question.jsp", "job");
			addPosition(8, "下铃声", "/Column.do?columnId=5188", "ring");
			addPosition(9, "下视频", "/Column.do?columnId=5189", "video");
			addPosition(10, "论坛", "/jcforum/index.jsp", "jcforum");
			addPosition(11, "龙虎榜", BaseAction.INDEX_URL, "top");
			addPosition(12, "聊天", "/chat/hall.jsp", "user");
			addPosition(13, "家园", "/home/viewAllHome.jsp", "home");
	
			addPosition(14, "炒股", "/stock2/index.jsp", "stock2");
			addPosition(15, "宠物", "/pet/index.jsp", "pet");
			addPosition(16, "浮生记", "/fs/help.jsp", "fs");
			addPosition(17, "海商王", "/dhh/help.jsp", "dhh");
			addPosition(18, "帮会", "/tong/tongCenter.jsp", "tong");
			addPosition(19, "拍卖", "/auction/auctionHall.jsp", "auction");
			addPosition(20, "大富翁", "/rich/index.jsp", "rich");
			addPosition(21, "大富豪", "游戏", "/lswjs/index.jsp", "wgamepkbig");
			addPosition(22, "银行", "/bank/bank.jsp", "bank");
			addPosition(23, "街霸", "/wgamefight/index.jsp", "wgamefight");
			addPosition(24, "侠客秘境", "/pk/help.jsp", "pk");
			addPosition(25, "桃花源", "/farm/index.jsp", "farm");
			addPosition(26, "问答接龙", "/question/index.jsp", "question");
			addPosition(27, "圈子", "/team/index.jsp", "team");
			addPosition(28, "城堡", "/cast/s.jsp", "cast");
			addPosition(32, "城堡", "/cast5/s.jsp", "cast5");
			addPosition(33, "城堡", "/cast3/s.jsp", "cast3");
			addPosition(34, "城堡", "/cast4/s.jsp", "cast4");
			addPosition(35, "城堡", "/cast2/s.jsp", "cast2");
			addPosition(29, "朋友", "/beacon/bFri/myInfo.jsp", "beacon");
			addPosition(30, "乐秀", "/shop/index.jsp", "shop");
			addPosition(31, "农场", "/garden/s.jsp", "garden");
			addPosition(32, "交友", "/friend/friendCenter.jsp", "friend");
			addPosition(33, "酷秀", "/kshow/index.jsp", "kshow");
			addPosition(100, "离开", BaseAction.INDEX_URL, null);
			addPosition(101, "发呆", BaseAction.INDEX_URL, null);
			addPosition(102, "@睡觉", BaseAction.INDEX_URL, null);
			addPosition(103, "@吃饭", BaseAction.INDEX_URL, null);
			addPosition(104, "@工作", BaseAction.INDEX_URL, null);
			addPosition(105, "@学习", BaseAction.INDEX_URL, null);
			addPosition(106, "@忙碌", BaseAction.INDEX_URL, null);
			addPosition(107, "@洗澡", BaseAction.INDEX_URL, null);
			addPosition(108, "@电视", BaseAction.INDEX_URL, null);
			addPosition(109, "@游戏", BaseAction.INDEX_URL, null);
			addPosition(110, "@约会", BaseAction.INDEX_URL, null);
			addPosition(111, "@外出", BaseAction.INDEX_URL, null);
		}
	}
	
	public static HashMap getPositionNameMap() {
		if (positionNameMap != null) {
			return positionNameMap;
		}
		loadPosition();

		return positionNameMap;
	}

	public static HashMap getPositionInfoMap() {
		if (positionNameInfo != null) {
			return positionNameInfo;
		}
		loadPosition();

		return positionNameInfo;
	}

	// zhul 2006-06-20 start 交友中心模块 用户年龄信息映射
	public static HashMap getAgeMap() {
		if (ageMap != null) {
			return ageMap;
		}
		ageMap = new HashMap();
		ageMap.put(new Integer(Constants.AGESUB17), "17以下");
		ageMap.put(new Integer(Constants.AGE18_20), "18-20");
		ageMap.put(new Integer(Constants.AGE21_25), "21-25");
		ageMap.put(new Integer(Constants.AGE26_30), "26-30");
		ageMap.put(new Integer(Constants.AGE31_40), "31-40");
		ageMap.put(new Integer(Constants.AGEUP41), "41以上");
		return ageMap;
	}

	// zhul 2006-06-20 end 交友中心模块 用户年龄信息映射
	/*
	 * 功能:随机取得社区描述词汇 mcq_2006-6-20
	 */
	public static String getTitleName() {
		int count = 0;
		String name = null;
		if (title == null) {
			title = new Vector();
			title.add("趣味");
			title.add("多彩");
			title.add("最大");
			title.add("神奇");
			title.add("欢乐");
			title.add("超级");
			title.add("乐酷");
			title.add("温馨");
		}
		count = title.size();
		count = RandomUtil.nextInt(count - 1);
		name = (String) title.get(count);
		return name;
	}

	/*
	 * 功能:通过uri得到用户所在位置信息 mcq_2006-6-20
	 */
	public static PositionBean getPostionByUri(String uri) {
		HashMap map = LoadResource.getPositionInfoMap();
		PositionBean positionBean = (PositionBean) map.get(uri);
		return positionBean;
	}

	/*
	 * 功能:通过positionId得到用户所在位置信息 mcq_2006-6-20
	 */
	public static PositionBean getPostionById(int positionId) {
		HashMap map = LoadResource.getPositionNameMap();
		PositionBean positionBean = (PositionBean) map.get(new Integer(
				positionId));
		return positionBean;
	}

	/**
	 * liuyi 2006-12-20 在线用户状态显示修改(15分钟不活跃，显示发呆；原来的发呆变为闲逛)
	 * 
	 * @param key
	 *            在线sessionid或者登录用户id
	 * @param positionId
	 *            位置id
	 * @return
	 */
	public static PositionBean defaultPositionBean;
	static {
		defaultPositionBean = new PositionBean();
		defaultPositionBean.setPositionInfo("发呆");
		defaultPositionBean.setPositionName("发呆");
		defaultPositionBean.setUrl(BaseAction.INDEX_URL);
	}
	public static PositionBean getPostionById(String key, int positionId) {
		boolean isActive = OnlineUtil.isActive(key);
		if (isActive) {
			HashMap map = LoadResource.getPositionNameMap();
			return (PositionBean) map.get(new Integer(positionId));
		} else {
			return defaultPositionBean;
		}
	}
	
	// 根据用户id得到当前状态名称
	public static String getPostionNameByUserId(int userId) {
		String key = String.valueOf(userId);
		UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(key);
		
		if(onlineUser == null)
			return "离线";
		if(OnlineUtil.isActive(key)) {
			ModuleBean bean = PositionUtil.getModule(onlineUser.getPositionId());
			if(bean == null)
				return "闲逛中";
			else
				return bean.getPositionName();
		} else {
			return "发呆";
		}
	}
	// 根据用户id得到当前状态名称后面加数字
	public static String getPostionNameByUserId2(int userId) {
		String key = String.valueOf(userId);
		UserBean onlineUser = (UserBean)OnlineUtil.getOnlineBean(key);
		StringBuilder sb = new StringBuilder(8);
		if(onlineUser == null)
			sb.append("离线");
		else if(OnlineUtil.isActive(key)) {
			ModuleBean bean = PositionUtil.getModule(onlineUser.getPositionId());
			if(bean == null) {
				sb.append("闲逛中");
			} else {
				sb.append(bean.getPositionName());
			}
			int idleTime = (int)((System.currentTimeMillis() - OnlineUtil.getLastVisitTime(key)) / 60000);
			if(idleTime > 0 && idleTime < 10)
				sb.append(idleTime);
			if(onlineUser.status != null) {
				sb.append('|');
				sb.append(onlineUser.status);
			}
		} else {
			sb.append("发呆");
		}
		return sb.toString(); 
	}

	// zhul_2006-07-11_将机会卡游戏数据加载到内存里 start
	public static TreeMap getCardMap() {
		// 内存中只有一个cardMap
		if (cardMap != null) {
			return cardMap;
		}

		cardMap = new TreeMap();

		IJobService jobService = ServiceFactory.createJobService();
		Vector cardList = jobService.getCardList(null);
		int size = cardList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			CardBean card = (CardBean) cardList.get(i);
			area += card.getAppearRate();
			if (card.getAppearRate() != 0) {
				cardMap.put(new Integer(area), card);
				// System.out.println(area+"--"+card.getId()+"--"+card.getCardName());
			}
		}
		cardMap.put(new Integer(10000), new Integer(area));

		return cardMap;
	}

	// zhul_2006-07-11_将游戏数据加载到内存里 end

	// zhul_2006-07-13_将jc_random_link中的数据加载到内存里 start
	static byte[] lock1 = new byte[0];

	public static TreeMap getLinkMap() {
		if (linkMap != null) {
			return linkMap;
		}
		synchronized (lock1) {
			if (linkMap != null) {
				return linkMap;
			}
			linkMap = new TreeMap();
			IFriendLinkService linkService = ServiceFactory
					.createFriendLinkService();
			Vector linkList = linkService.getRandomLinkList(null);
			int size = linkList.size();
			int area = 0;
			for (int i = 0; i < size; i++) {
				RandomLinkBean randomLink = (RandomLinkBean) linkList.get(i);
				area += randomLink.getAppearRate();
				if (randomLink.getAppearRate() != 0) {
					linkMap.put(new Integer(area), randomLink);
				}
			}
			linkMap.put(new Integer(1000000), new Integer(area));

			return linkMap;
		}
	}

	// zhul_2006-07-13_将jc_random_link中的数据加载到内存里 end

	/**
	 * macq_2007-4-11_加载欺骗友链二级页面_strat
	 */
	static byte[] lock2 = new byte[0];

	public static TreeMap getLinkSubMap() {
		if (linkSubMap != null) {
			return linkSubMap;
		}
		synchronized (lock2) {
			if (linkSubMap != null) {
				return linkSubMap;
			}
			linkSubMap = new TreeMap();
			IFriendLinkService linkService = ServiceFactory
					.createFriendLinkService();
			Vector linkSubList = linkService.getRandomSubLinkList(null);
			for (int i = 0; i < linkSubList.size(); i++) {
				RandomSubLinkBean randomSubLink = (RandomSubLinkBean) linkSubList
						.get(i);
				//判断map中是否包含当前友链ID,如包含添加当前bean到values的list中
				if(linkSubMap.containsKey(new Integer(randomSubLink.getLinkId()))){
					List linkList = (List)linkSubMap.get(new Integer(randomSubLink.getLinkId()));
					linkList.add(randomSubLink);
				}else{
					//添加友链2级页面数据
					List linkList = new ArrayList();
					linkList.add(randomSubLink);
					linkSubMap.put(new Integer(randomSubLink.getLinkId()),linkList);
				}
			}
		}
		return linkSubMap;
	}

	public void clearLinkSubMap() {
		linkSubMap = null;
	}

	public void clearLinkMap() {
		linkMap = null;
	}

	public void clearCardMap() {
		cardMap = null;
	}

	// zhul_2006-07-18 将武器加载到内存start
	public static HashMap getWeaponMap() {
		if (weaponMap != null) {
			return weaponMap;
		}

		IJobService jobService = ServiceFactory.createJobService();
		weaponMap = jobService.getHuntWeaponMap(null);

		return weaponMap;

	}

	// 将内存中weaponMap清空
	public void clearWeaponMap() {
		weaponMap = null;
	}

	// zhul_2006-07-18 将武器加载到内存end

	// zhul_2006-07-18 将动物加载到内存start
	public static HashMap getQuarryMap() {
		if (quarryMap != null) {
			return quarryMap;
		}

		IJobService jobService = ServiceFactory.createJobService();
		quarryMap = jobService.getHuntQuarryMap(null);

		return quarryMap;

	}

	// 将内存中quarryMap清空
	public void clearQuarryMap() {
		quarryMap = null;
	}

	// zhul_2006-07-18 将动物加载到内存end

	// zhul_2006-07-18 将各种武器对应的动物出现机率加载到内存 start
	public static TreeMap getArrowMap() {
		if (arrowMap != null) {
			return arrowMap;
		}

		arrowMap = new TreeMap();
		IJobService jobService = ServiceFactory.createJobService();
		Vector arrowList = jobService.getHuntQuarryAppearRateList("weapon_id="
				+ Constants.ARROW + " order by quarry_id");
		int size = arrowList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			HuntQuarryAppearRateBean appearRate = (HuntQuarryAppearRateBean) arrowList
					.get(i);
			area += appearRate.getAppearRate();
			if (appearRate.getAppearRate() != 0) {
				arrowMap.put(new Integer(area), new Integer(appearRate
						.getQuarryId()));
				// System.out.println(area+"--"+appearRate.getAppearRate()+"--"+appearRate.getQuarryId()+"--"+appearRate.getWeaponId());
			}
		}
		arrowMap.put(new Integer(Constants.RANDOM_BASE), new Integer(area));

		return arrowMap;
	}

	// 将内存中arrowMap清空
	public void clearArrowMap() {
		arrowMap = null;
	}

	public static TreeMap getHandGunMap() {
		if (handGunMap != null) {
			return handGunMap;
		}

		handGunMap = new TreeMap();
		IJobService jobService = ServiceFactory.createJobService();
		Vector arrowList = jobService.getHuntQuarryAppearRateList("weapon_id="
				+ Constants.HANDGUN + " order by quarry_id");
		int size = arrowList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			HuntQuarryAppearRateBean appearRate = (HuntQuarryAppearRateBean) arrowList
					.get(i);
			area += appearRate.getAppearRate();
			if (appearRate.getAppearRate() != 0) {
				handGunMap.put(new Integer(area), new Integer(appearRate
						.getQuarryId()));
				// System.out.println(area+"--"+appearRate.getAppearRate()+"--"+appearRate.getQuarryId()+"--"+appearRate.getWeaponId());
			}
		}
		handGunMap.put(new Integer(Constants.RANDOM_BASE), new Integer(area));

		return handGunMap;
	}

	// 将内存中handGunMap清空
	public void clearHandGunMap() {
		handGunMap = null;
	}

	public static TreeMap getHuntGunMap() {
		if (huntGunMap != null) {
			return huntGunMap;
		}

		huntGunMap = new TreeMap();
		IJobService jobService = ServiceFactory.createJobService();
		Vector arrowList = jobService.getHuntQuarryAppearRateList("weapon_id="
				+ Constants.HUNTGUN + " order by quarry_id");
		int size = arrowList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			HuntQuarryAppearRateBean appearRate = (HuntQuarryAppearRateBean) arrowList
					.get(i);
			area += appearRate.getAppearRate();
			if (appearRate.getAppearRate() != 0) {
				huntGunMap.put(new Integer(area), new Integer(appearRate
						.getQuarryId()));
				// System.out.println(area+"--"+appearRate.getAppearRate()+"--"+appearRate.getQuarryId()+"--"+appearRate.getWeaponId());
			}
		}
		huntGunMap.put(new Integer(Constants.RANDOM_BASE), new Integer(area));

		return huntGunMap;
	}

	// 将内存中huntGunMap清空
	public void clearHuntGunMap() {
		huntGunMap = null;
	}

	public static TreeMap getAk47Map() {
		if (ak47Map != null) {
			return ak47Map;
		}

		ak47Map = new TreeMap();
		IJobService jobService = ServiceFactory.createJobService();
		Vector arrowList = jobService.getHuntQuarryAppearRateList("weapon_id="
				+ Constants.AK47 + " order by quarry_id");
		int size = arrowList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			HuntQuarryAppearRateBean appearRate = (HuntQuarryAppearRateBean) arrowList
					.get(i);
			area += appearRate.getAppearRate();
			if (appearRate.getAppearRate() != 0) {
				ak47Map.put(new Integer(area), new Integer(appearRate
						.getQuarryId()));
				// System.out.println(area+"--"+appearRate.getAppearRate()+"--"+appearRate.getQuarryId()+"--"+appearRate.getWeaponId());
			}
		}
		ak47Map.put(new Integer(Constants.RANDOM_BASE), new Integer(area));

		return ak47Map;
	}

	// 将内存中ak47Map清空
	public void clearAk47Map() {
		ak47Map = null;
	}
	
	public static TreeMap getAWPMap() {
		if (awpMap != null) {
			return awpMap;
		}

		awpMap = new TreeMap();
		IJobService jobService = ServiceFactory.createJobService();
		Vector arrowList = jobService.getHuntQuarryAppearRateList("weapon_id="
				+ Constants.AWP + " order by quarry_id");
		int size = arrowList.size();
		int area = 0;
		for (int i = 0; i < size; i++) {
			HuntQuarryAppearRateBean appearRate = (HuntQuarryAppearRateBean) arrowList
					.get(i);
			area += appearRate.getAppearRate();
			if (appearRate.getAppearRate() != 0) {
				awpMap.put(new Integer(area), new Integer(appearRate
						.getQuarryId()));
				// System.out.println(area+"--"+appearRate.getAppearRate()+"--"+appearRate.getQuarryId()+"--"+appearRate.getWeaponId());
			}
		}
		awpMap.put(new Integer(Constants.RANDOM_BASE), new Integer(area));

		return awpMap;
	}

	// 将内存中ak47Map清空
	public void clearAWPMap() {
		awpMap = null;
	}

	// zhul_2006-07-18 将各种武器对应的动物出现机率加载到内存 end

	public static LinkedHashMap getMobileMap() {
		if (mobileMap != null) {
			return mobileMap;
		}
		mobileMap = new LinkedHashMap();
		mobileMap.put("诺基亚", "诺");
		mobileMap.put("摩托罗拉", "摩");
		mobileMap.put("索爱", "索");
		mobileMap.put("西门子", "西");
		mobileMap.put("阿尔卡特", "阿");
		mobileMap.put("三星", "三");
		mobileMap.put("飞利浦", "飞");
		mobileMap.put("ＬＧ", "LG");
		mobileMap.put("所有品牌", "all");
		return mobileMap;
	}

	public static Vector getRingSearchList() {
		if (ringSearchList != null) {
			return ringSearchList;
		}
		ringSearchList = new Vector();
		ringSearchList.add("AMR");
		ringSearchList.add("MID");
		ringSearchList.add("MMF");
		ringSearchList.add("MP3");
		return ringSearchList;
	}

	// mcq_2006-8-18_获取用户持有乐币排序前10名_start
	public static Vector getMoneyTopList() {
		if (userMoneyList != null) {
			return userMoneyList;
		}
		userMoneyList = new Vector();
		Vector moneyList = topService
				.getMoneyTopList(" 1=1 order by money_total desc limit 0,10");
		Iterator itr = moneyList.iterator();
		while (itr.hasNext()) {
			MoneyTopBean money = (MoneyTopBean) itr.next();
			userMoneyList.add(money);
		}
		return userMoneyList;
	}

	// mcq_2006-8-18_获取用户持有乐币排序前10名_end

	// mcq_2006-8-18_获取用户等级排序前10名_start
	public static Vector getRankTopList() {
		if (userRankList != null) {
			return userRankList;
		}
		userRankList = new Vector();
		Vector userList = userService.getUserStatusList(
				" 1=1 order by rank desc limit 0,10", true);
		Iterator itr = userList.iterator();
		while (itr.hasNext()) {
			UserStatusBean userStatus = (UserStatusBean) itr.next();
			userRankList.add(userStatus);
		}
		return userRankList;
	}

	static String charitarianListKey = "charitarianList";
	static String socialListKey = "socialList";
	static String spiritListKey = "spiritList";
	
	// mcq_2006-11-3_获取用户社交指数排序前10名_start
	public static Vector getSocialTopList() {
		synchronized(stuffCache) {
			Vector userSocialList = (Vector)stuffCache.get(socialListKey);
			if (userSocialList != null) {
				return userSocialList;
			}
			userSocialList = new Vector();
			List userList = SqlUtil.getIntList(
					"select user_id from jc_user_top where mark=3 order by priority desc limit 10");
			Iterator itr = userList.iterator();
			while (itr.hasNext()) {
				Integer iid = (Integer)itr.next();
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(iid.intValue());
				userStatus.setUser(UserInfoUtil.getUser(iid.intValue()));
				userSocialList.add(userStatus);
			}
			stuffCache.put(socialListKey, userSocialList);
			return userSocialList;
		}
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：杀人王排名
	 * @datetime:2007-5-10 10:31:51
	 * @return
	 * @return Vector
	 */
	public static Vector getPKUserKTopList() {
		if (pkUserKTopList != null) {
			return pkUserKTopList;
		}
		pkUserKTopList = new Vector();
		Vector userList = PKService.getPKUserList(" 1=1 order by old_kcount desc limit 0,10");
		Iterator itr = userList.iterator();
		while (itr.hasNext()) {
			PKUserBean pkUser = (PKUserBean) itr.next();
			pkUserKTopList.add(pkUser);
		}
		return pkUserKTopList;
	}
	/**
	 *  
	 * @author macq
	 * @explain：清空杀人王排名
	 * @datetime:2007-5-10 10:32:40
	 * @return void
	 */
	public static void clearPKUserKTopList() {
		pkUserKTopList = null;
	}

	// mcq_2006-8-18_获取用户等级排序前10名_end
	// wucx_2006-11-7_获取用户反倭值排序前10名_start
	public static Vector getSpiritTopList() {
		synchronized(stuffCache) {
			Vector userSpiritList = (Vector)stuffCache.get(spiritListKey);
			if (userSpiritList != null) {
				return userSpiritList;
			}
			userSpiritList = new Vector();
			List userList = SqlUtil.getIntList(
					"select user_id from jc_user_top where mark=4 order by priority desc limit 10");
			Iterator itr = userList.iterator();
			while (itr.hasNext()) {
				Integer iid = (Integer)itr.next();
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(iid.intValue());
				userStatus.setUser(UserInfoUtil.getUser(iid.intValue()));
				userSpiritList.add(userStatus);
			}
			stuffCache.put(spiritListKey, userSpiritList);
			return userSpiritList;
		}
	}

	// wucx_2006-11-7_获取用户反倭值排序前10名_end
	// wucx_2006-11-15_获取用户股票排序前10名_start
	public static Vector getStockTopList() {
		if (userStockList != null) {
			return userStockList;
		}
		userStockList = new Vector();
		Vector userList = userService.getUserStatusList(
				" user_id>0 order by stock desc limit 0,10", true);
		Iterator itr = userList.iterator();
		while (itr.hasNext()) {
			UserStatusBean userStatus = (UserStatusBean) itr.next();
			userStockList.add(userStatus);
		}
		return userStockList;
	}

	// macq_2006-11-21_获取交友中心男性用户照片投票排序前10名(规则是7天内登录过网站的用户)_start
	public static Vector getFriendPhotoManTopList() {
		if (friendPhotoManList != null) {
			return friendPhotoManList;
		}
		friendPhotoManList = new Vector();
		List userList = friendService
				.getFriendVoteList2("select a.id,a.user_id ,a.count from jc_friend_vote a ,jc_friend d,user_status b  where to_days(now())-to_days(b.last_login_time)<8 and a.user_id=b.user_id and a.user_id=d.user_id and gender=1 order by a.count desc limit 10");
		Iterator itr = userList.iterator();
		while (itr.hasNext()) {
			FriendVoteBean friendVote = (FriendVoteBean) itr.next();
			friendPhotoManList.add(friendVote);
		}
		return friendPhotoManList;
	}

	// macq_2006-11-21_获取交友中心女性用户照片投票排序前10名(规则是7天内登录过网站的用户)_start
	public static Vector getFriendPhotoWomanTopList() {
		if (friendPhotoWomanList != null) {
			return friendPhotoWomanList;
		}
		friendPhotoWomanList = new Vector();
		List userList = friendService
				.getFriendVoteList2("select a.id,a.user_id ,a.count from jc_friend_vote a ,jc_friend d,user_status b  where to_days(now())-to_days(b.last_login_time)<8 and a.user_id=b.user_id and a.user_id=d.user_id and gender=0 order by a.count desc limit 10");
		Iterator itr = userList.iterator();
		while (itr.hasNext()) {
			FriendVoteBean friendVote = (FriendVoteBean) itr.next();
			friendPhotoWomanList.add(friendVote);
		}
		return friendPhotoWomanList;
	}

	// macq_2006-11-24_慈善家排行榜_start
	// liuyi 2006-12-28 慈善排行榜修改－－返回list为userid start
	public static Vector getCharitarianTopList() {
		synchronized(stuffCache) {
			Vector ret = (Vector)stuffCache.get(charitarianListKey);
			if (ret == null) {
				ret = new Vector();
				String sql = "select user_id from jc_user_top where mark=5 order by priority desc limit 10";
				List topUserIdList = SqlUtil.getIntList(sql);
	
				if (topUserIdList != null) {
					for (int i = 0; i < topUserIdList.size(); i++) {
						Integer userId = (Integer) topUserIdList.get(i);
						if (userId == null)
							continue;
	
						ret.add(userId);
					}
				}
				stuffCache.put(charitarianListKey, ret);
			}
			return ret;
		}
	}

	// liuyi 2006-12-28 慈善排行榜修改－－返回list为userid end
	// macq_2006-11-24_慈善家排行榜_end

	// 清空内存中慈善家排行榜前10名的用户
	public static void clearCharitarianTopList() {
		stuffCache.srm(charitarianList);
	}

	// 清空内存中照片投票前10名的用户
	public static void clearFriendPhotoManTopList() {
		friendPhotoManList = null;
	}

	// 清空内存中照片投票前10名的用户
	public static void clearFriendPhotoWomanTopList() {
		friendPhotoWomanList = null;
	}

	// 清空内存中乐币前10名的用户
	public static void clearMoneyTopList() {
		userMoneyList = null;
	}

	// 清空内存中等级前10名的用户
	public static void clearRankTopList() {
		userRankList = null;
	}

	// 清空内存中社交前10名的用户
	public static void clearSocialTopList() {
		stuffCache.srm(socialListKey);
	}

	// 清空内存中反倭值前10名的用户
	public static void clearSpiritTopList() {
		stuffCache.srm(spiritListKey);
	}

	// 清空内存中股票值前10名的用户
	public static void clearStockTopList() {
		userStockList = null;
	}

	/**
	 * fanys 2006-08-22 得到上周群英排行榜排名
	 * 
	 * @return
	 */
	public static Vector getYesterdayInivteRank() {
		if (null == inviteRankList) {
			RoomInviteRankBean rankBean = null;
			IChatService chatService = ServiceFactory.createChatService();
			IUserService userService = ServiceFactory.createUserService();
			CrownBean crown = null;

			inviteRankList = new Vector();
			Vector vecRankList = chatService
					.getRoomInviteRankList(" a.create_datetime>='"
							+ getYesterday() + "' order by count desc ");
			for (int i = 0; i < vecRankList.size(); i++) {
				rankBean = (RoomInviteRankBean) vecRankList.get(i);
				crown = userService.getCrown(" id=" + rankBean.getResourceId());
				rankBean.setCrown(crown);
				inviteRankList.add(rankBean);

			}
		}
		return inviteRankList;
	}

	/**
	 * 每周一重新设置一下上周群英排行榜
	 * 
	 */
	public static void loadYesterdayInviteRank() {
		RoomInviteRankBean rankBean = null;
		IChatService chatService = ServiceFactory.createChatService();
		IUserService userService = ServiceFactory.createUserService();
		CrownBean crown = null;
		inviteRankList = new Vector();
		Vector vecRankList = chatService
				.getRoomInviteRankList(" a.create_datetime>='" + getYesterday()
						+ "' order by count desc ");
		for (int i = 0; i < vecRankList.size(); i++) {
			rankBean = (RoomInviteRankBean) vecRankList.get(i);
			crown = userService.getCrown(" id=" + rankBean.getResourceId());
			rankBean.setCrown(crown);
			inviteRankList.add(rankBean);

		}
	}

	/**
	 * fanys 2006-08-22 得到上周群英排行榜排名
	 * 
	 * @return
	 */
	public static Vector getLastWeekInivteRank() {
		if (null == inviteRankList) {
			RoomInviteRankBean rankBean = null;
			IChatService chatService = ServiceFactory.createChatService();
			IUserService userService = ServiceFactory.createUserService();
			CrownBean crown = null;
			inviteRankList = new Vector();
			Vector vecRankList = chatService
					.getRoomInviteRankList(" a.create_datetime>='"
							+ getFirstDayOfLastWeek()
							+ "' order by count desc ");
			for (int i = 0; i < vecRankList.size(); i++) {
				rankBean = (RoomInviteRankBean) vecRankList.get(i);
				crown = userService.getCrown(" id=" + rankBean.getResourceId());
				rankBean.setCrown(crown);
				inviteRankList.add(rankBean);

			}
		}
		return inviteRankList;
	}

	/**
	 * 每周一重新设置一下上周群英排行榜
	 * 
	 */
	public static void loadLastWeekInviteRank() {
		RoomInviteRankBean rankBean = null;
		IChatService chatService = ServiceFactory.createChatService();
		IUserService userService = ServiceFactory.createUserService();
		CrownBean crown = null;
		inviteRankList = new Vector();
		Vector vecRankList = chatService
				.getRoomInviteRankList(" a.create_datetime>='"
						+ getFirstDayOfLastWeek() + "' order by count desc ");
		for (int i = 0; i < vecRankList.size(); i++) {
			rankBean = (RoomInviteRankBean) vecRankList.get(i);
			crown = userService.getCrown(" id=" + rankBean.getResourceId());
			rankBean.setCrown(crown);
			inviteRankList.add(rankBean);

		}
	}

	/**
	 * fanys 2006-08-22 得到当前日期得上一周的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfLastWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 1);
		return getFirstDayOfWeek(c.getTime());
	}

	/**
	 * fanys 2006-08-22 取得某日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());

	}

	public static String getYesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(c.getTime());
	}

	// mcq 2006-09-20_加载家园大图片_start
	public static HashMap getHomImageBigFileMap() {
		if (homeImageBigMap != null) {
			return homeImageBigMap;
		}
		boolean isWindows = false;
		String osName = System.getProperty("os.name");
		if (osName != null) {
			isWindows = (osName.toLowerCase().indexOf("windows") != -1);
		}
		// String imagePath = "/usr/local/joycool-rep/home/big";
		String imagePath = (isWindows) ? Constants.HOME_IMAGE_BIG_WINDOWS_ROOT
				: Constants.HOME_IMAGE_BIG_LINUX_ROOT;
		homeImageBigMap = new HashMap();
		Vector homeImageList = homeService.getHomeImageList("1=1");
		HomeImageBean homeImage = null;
		File imageFile = null;
		imageFile = new File(imagePath);
		if(imageFile.exists() && imageFile.isDirectory()) {
			BufferedImage image = null;
			for (int i = 0; i < homeImageList.size(); i++) {
				homeImage = (HomeImageBean) homeImageList.get(i);
				imageFile = new File(imagePath + "/" + homeImage.getId() + ".gif");
				try {
					image = ImageIO.read(imageFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				homeImageBigMap.put(homeImage.getId() + "", image);
			}
		} else {
			System.out.println("加载家园大图片失败，目录" + imagePath + "不存在！");
		}
		return homeImageBigMap;
	}

	// mcq 2006-09-20_加载家园大图片_end

	// mcq 2006-09-20_加载家园小图片_start
	public static HashMap getHomImageSmallFileMap() {
		if (homeImageSmallMap != null) {
			return homeImageSmallMap;
		}
		boolean isWindows = false;
		String osName = System.getProperty("os.name");
		if (osName != null) {
			isWindows = (osName.toLowerCase().indexOf("windows") != -1);
		}
		// String imagePath = "/usr/local/joycool-rep/home/small";
		String imagePath = (isWindows) ? Constants.HOME_IMAGE_SMALL_WINDOWS_ROOT
				: Constants.HOME_IMAGE_SMALL_LINUX_ROOT;
		homeImageSmallMap = new HashMap();
		Vector homeImageList = homeService.getHomeImageList("1=1");
		HomeImageBean homeImage = null;
		File imageFile = null;
		imageFile = new File(imagePath);
		if(imageFile.exists() && imageFile.isDirectory()) {
			BufferedImage image = null;
			for (int i = 0; i < homeImageList.size(); i++) {
				homeImage = (HomeImageBean) homeImageList.get(i);
				imageFile = new File(imagePath + "/" + homeImage.getId() + ".gif");
				try {
					image = ImageIO.read(imageFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				homeImageSmallMap.put(homeImage.getId() + "", image);
			}
		} else {
			System.out.println("加载家园小图片失败，目录" + imagePath + "不存在！");
		}
		return homeImageSmallMap;
	}

	// mcq 2006-09-20_加载家园小图片_end

	/**
	 * 从内存中获取一个用户的所有家园仓库中图片记录。
	 * 
	 * @param userId
	 * @return
	 */
	public static Vector getHomeUserImageList(int userId) {
		if (homeUserImageMap == null) {
			homeUserImageMap = new HashMap();
		}
		String key = userId + "";
		// 从缓存中取
		Vector homeUserImageList = (Vector) homeUserImageMap.get(key);
		// 缓存中没有
		if (homeUserImageList == null) {
			// synchronized (userImageLock)
			{
				// 从缓存中取
				homeUserImageList = (Vector) homeUserImageMap.get(key);
				if (homeUserImageList == null) {
					// 从数据库中取
					homeUserImageList = homeService
							.getHomeUserImageList("user_id = " + userId
									+ " and home_id=0 order by type_id desc");
					// 为空
					if (homeUserImageList == null) {
						return null;
					}
					// 放到缓存中
					homeUserImageMap.put(key, homeUserImageList);
				}
			}
		}
		return homeUserImageList;
	}

	/**
	 * 从内存中删除一个用户的所有家园图片记录。
	 * 
	 * @param userId
	 * @return
	 */
	public static boolean deleteHomeUserImageList(int userId) {
		if (homeUserImageMap == null) {
			homeUserImageMap = new HashMap();
		}
		String key = userId + "";
		// 删除内存中每个用户的所有家园图片信息
		homeUserImageMap.remove(key);
		return true;
	}

	/**
	 * 从内存中获取一个用户的所有家园已展示图片记录。
	 * 
	 * @param userId
	 * @param homeId
	 * @return
	 */
	public static Vector getHomeUserHomeImageList(int userId, int homeId) {
		if (homeUserHomeImageMap == null) {
			homeUserHomeImageMap = new HashMap();
		}
		String key = userId + "_" + homeId;
		// 从缓存中取
		Vector homeUserImageList = (Vector) homeUserHomeImageMap.get(key);
		// 缓存中没有
		if (homeUserImageList == null) {
			// synchronized (userImageLock)
			{
				// 从缓存中取
				homeUserImageList = (Vector) homeUserHomeImageMap.get(key);
				if (homeUserImageList == null) {
					// 从数据库中取
					homeUserImageList = homeService
							.getHomeUserImageList("user_id=" + userId
									+ " and home_id=" + homeId
									+ " order by type_id");
					// 为空
					if (homeUserImageList == null) {
						return null;
					}
					// 放到缓存中
					homeUserHomeImageMap.put(key, homeUserImageList);
				}
			}
		}
		return homeUserImageList;
	}

	/**
	 * 从内存中删除一个用户的所有家园已展示图片记录。
	 * 
	 * @param userId
	 * @param homeId
	 * @return
	 */
	public static boolean deleteHomeUserHomeImageList(int userId, int homeId) {
		if (homeUserHomeImageMap == null) {
			homeUserHomeImageMap = new HashMap();
		}
		String key = userId + "_" + homeId;
		// 删除内存中每个用户的所有家园图片信息
		homeUserHomeImageMap.remove(key);
		// 清空用户仓库缓存
		deleteHomeUserImageList(userId);
		return true;
	}

	/**
	 * 获取年龄区段类型
	 * 
	 * @param age
	 */
	public static int getAgeType(int age) {
		int type = 0;
		if (age <= 16) {
			type = 1;
		} else if (age <= 18) {
			type = 2;
		} else if (age <= 20) {
			type = 3;
		} else if (age <= 25) {
			type = 4;
		} else if (age <= 30) {
			type = 5;
		} else if (age <= 40) {
			type = 6;
		} else if (age <= 50) {
			type = 7;
		} else {
			type = 8;
		}
		return type;
	}

	/**
	 * 获取身高区段类型
	 * 
	 * @param height
	 */
	public static int getHeightType(int height) {
		int type = 0;
		if (height <= 150) {
			type = 1;
		} else if (height <= 160) {
			type = 2;
		} else if (height <= 170) {
			type = 3;
		} else if (height <= 180) {
			type = 4;
		} else if (height <= 190) {
			type = 5;
		} else if (height <= 200) {
			type = 6;
		} else {
			type = 7;
		}
		return type;
	}

	/**
	 * 获取身高区段类型
	 * 
	 * @param weight
	 */
	public static int getWeightType(int weight) {
		int type = 0;
		if (weight <= 40) {
			type = 1;
		} else if (weight <= 45) {
			type = 2;
		} else if (weight <= 50) {
			type = 3;
		} else if (weight <= 55) {
			type = 4;
		} else if (weight <= 60) {
			type = 5;
		} else if (weight <= 65) {
			type = 6;
		} else if (weight <= 70) {
			type = 7;
		} else if (weight <= 80) {
			type = 8;
		} else {
			type = 9;
		}
		return type;
	}
}
