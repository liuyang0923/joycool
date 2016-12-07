package jc.match;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import jc.credit.CreditAction;
import jc.credit.CreditCity;
import jc.credit.UserBase;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.LinkedCacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.shop.ShopAction;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class MatchAction extends CustomAction {

	public static String ATTACH_ROOT = Constants.RESOURCE_ROOT_PATH + "match/";
	public static String ATTACH_URL_ROOT = "/rep/match/";
	public static String ATTACH_GOOD_ROOT = ATTACH_ROOT + "good/";
	public static String ATTACH_GOOD_URL_ROOT = ATTACH_URL_ROOT + "good/";

	public static int MAX_WIDTH = 200; // 图片的限制
	public static int MAX_HEIGHT = 300;
	public static int S_MAX_WIDTH = 90; // 缩略图大小
	public static int S_MAX_HEIGHT = 100;

	public static long TODAY_MILLIS = 0;
	public static MatchService service = new MatchService();
	public static byte[] initLock = new byte[0];
	public static byte[] orderLock = new byte[0];
	public static HashMap resMap = null; // 虚拟物品列表
	public static List resList = new ArrayList(); // 虚拟物品List
	public static List topTenList = new ArrayList(); // 前十名
	public static List recoList = null; // 管理员推荐
	public static HashMap areaMap = null;	// 分区MAP
	public static LinkedHashMap topTrendsMap = null; // 置顶动态
	public static HashMap goodsMap = null; // 可兑换商品的Map

	public static long RANK_INTERVAL = 0;	// 排名更新计时
	public static long FANS_INTERVAL = 0;	// 粉丝更新计时
	public static long START_RANK_INTERVAL = 30 * 60 * 1000;

	// Chinese numerals
	public static String[] CHS_NUM = {"零","一","二","三","四","五","六","七","八","九","十","十一","十二","十三","十四","十五"};
	
	public static MatchInfo matchNow = null; // 当前的比赛信息
	
	public static long getSTART_RANK_INTERVAL() {
		return START_RANK_INTERVAL;
	}

	public static void setSTART_RANK_INTERVAL(long sTARTRANKINTERVAL) {
		START_RANK_INTERVAL = sTARTRANKINTERVAL;
	}

	public static MatchInfo getMatchNow() {
		return matchNow;
	}

	public static void setMatchNow(MatchInfo matchNow) {
		MatchAction.matchNow = matchNow;
	}

	public static LinkedHashMap getTopTrendsMap() {
		return topTrendsMap;
	}

	public static void setTopTrendsMap(LinkedHashMap topTrendsMap) {
		MatchAction.topTrendsMap = topTrendsMap;
	}

	public static List getResList() {
		return resList;
	}

	public static void setResList(List resList) {
		MatchAction.resList = resList;
	}

	public static List getTopTenList() {
		return topTenList;
	}

	public static void setTopTenList(List topTenList) {
		MatchAction.topTenList = topTenList;
	}

	public static List getRecoList() {
		return recoList;
	}

	public static void setRecoList(List recoList) {
		MatchAction.recoList = recoList;
	}

	public static List getTopTrendsList() {
		return new ArrayList(topTrendsMap.keySet());
	}

	public MatchAction(HttpServletRequest request) {
		super(request);
		// 取得虚拟物品
		getResMap();
		// 取得推荐用户
		getRecoListFromDB();
		// 更新排行榜,同时更新前十
		statCompose();
		// 取得置顶动态
		topTrendsMap = getTopTrends();
		// 更新今日粉丝排行
		todayFans();
		// 取得可兑换商品
		getGoodsMap();
		// 分区MAP
		if (areaMap == null || areaMap.size() == 0){
			areaMap = getAreaMap();
		}
	}

	public MatchAction() {

	}

	/**
	 * 返回从a到b,一共间隔了多久.
	 * @param a
	 * @param b
	 * @return
	 */
	public static String dayDiffStr(long a,long b){
		if (a >= b){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int days = DateUtil.dayDiff(a, b);
		int week = days / 7;
		days = days % 7;
		sb.append("为期");
		sb.append(CHS_NUM[week]);
		sb.append("周");
		if (days > 0){
			sb.append("零");
			sb.append(days);
			sb.append("天");
		}
		sb.append(",");
		return sb.toString();
	}
	
	/**
	 * 取得离今天最近的周三的日期的毫秒数。例如，今天是2010年6月7日，则返回2010年6月9日。
	 */
	public static long getWed() {
		// 取得离今天最近的周三的日期
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		// 星期日=1，星期三=4
		int weekNow = c.get(java.util.Calendar.DAY_OF_WEEK);
		if (weekNow < 4) {
			c.add(java.util.Calendar.DAY_OF_MONTH, 4 - weekNow);
		} else if (weekNow > 4) {
			c.add(java.util.Calendar.DAY_OF_MONTH, 7 - weekNow + 4);
		}
		return c.getTimeInMillis();
	}

	// 取得分区的map
	public static HashMap getAreaMap(){
		if (areaMap == null || areaMap.size() == 0){
			areaMap = service.getAreaMap(" 1");
		}
		return areaMap;
	}
	
	/**
	 * 取得一个分区bean
	 * @param key
	 * @return
	 */
	public static MatchArea getArea(int key){
		return (MatchArea)areaMap.get(new Integer(key));
	}
	
	/**
	 * 取得分区List
	 * @return
	 */
	public static List getAreaList(){
		return new ArrayList(areaMap.keySet());
	}
	
	/**
	 * 取得当前比赛(注:可能会返回Null)
	 */
	public static MatchInfo getCurrentMatch() {
		MatchInfo oldMatch = null;
		// 看是否有正在进行的比赛
		if (matchNow == null){
			matchNow = service.getInfo(" flag=1");
		}
		if (matchNow != null){
			// 当前比赛存在，看是否过期
			if (matchNow.getEndTime() <= System.currentTimeMillis() && matchNow.getFalg() == 1){
				// 比赛过期，但却没有结赛
				SqlUtil.executeUpdate("update match_info set flag=2 where id="
						+ matchNow.getId(), 5);
				// 把分区信息写入历史
				SqlUtil.executeUpdate(
						"insert into match_area_history(match_id,area_name,citys,`describe`,create_time,provinces,`count`,area_id) select "
								+ matchNow.getId()
								+ " as match_id,area_name,citys,`describe`,create_time,provinces,`count`,id as area_id from match_area",
						5);
				// 把现有的排名写入历史
				SqlUtil
				.executeUpdate(
						"insert into match_history (match_id,user_id,vote_count,rank,photo,photo2,area_id) select "
								+ matchNow.getId()
								+ " as match_id, r.user_id,r.vote_count,r.id as rank,u.photo,u.photo2,u.area_id from match_rank r,match_user u where u.user_id=r.user_id",
						5);
				oldMatch = matchNow;
				matchNow = null;
			} else if (matchNow.getEndTime() > System.currentTimeMillis() && matchNow.falg < 2){
				// 没过期，返回
				return matchNow;
			} else {
				// 过期并且已结赛
				oldMatch = matchNow;
				matchNow = null;
			}
		}
		if (matchNow == null){
			// 下一场比赛
			matchNow = service.getInfo(" flag=0 and start_time>="
					+ DateUtil.formatDate(new Date()) + " order by id limit 1");
			if (matchNow != null){
				// 是否可以开赛?
				if (matchNow.getStartTime() <= System.currentTimeMillis()){
					// 设置为开赛
					if (oldMatch != null){
						SqlUtil.executeUpdate("update match_info set flag=1,user_count=" + oldMatch.getUserCount() + ",fans_count=" + oldMatch.getFansCount() + " where id="
								+ matchNow.getId(), 5);
						matchNow.setUserCount(oldMatch.getUserCount());
						matchNow.setFansCount(oldMatch.getFansCount());
					} else {
						SqlUtil.executeUpdate("update match_info set flag=1,user_count=0,fans_count=0 where id="
								+ matchNow.getId(), 5);
						matchNow.setUserCount(0);
						matchNow.setFansCount(0);
					}
					matchNow.setFalg(1);
					// 同时要清除上次比赛的所有相关记录
					// 清空原排名
					SqlUtil.executeUpdate("truncate table match_rank", 5);
					// 修改user表，清空本赛季的靓点、消费数，所得的所有奢侈品
					SqlUtil.executeUpdate("update match_user set vote_count=0,consume=0,good0=0,good1=0,good2=0,good3=0,good4=0,good5=0,good6=0,good7=0", 5);
					// 清空user缓存
					matchUserCache.clear();
					// 清空投票表
					SqlUtil.executeUpdate("truncate table match_voted", 5);
					// 清空置顶动态
					SqlUtil.executeUpdate("truncate table match_top_trends", 5);
					// 清空置顶缓存
					topTrendsMap.clear();
					// 清空后台推荐
					SqlUtil.executeUpdate("truncate table match_reco", 5);
					// 清空每日粉丝表
					SqlUtil.executeUpdate("truncate table match_fans_ab2", 5);
					// 清空动态表
					SqlUtil.executeUpdate("truncate table match_trends", 5);
					// 清空官方推荐
					recoList.clear();
					/*
					 * 重新读取所有用户所在地的分区信息并记录
					 */
					chageArea(-100);
					return matchNow;
				} 
			}
			//  如果没有下一场还没开始，或上一场已结束的比赛
			matchNow = service.getInfo(" flag>0 order by start_time desc limit 1");
			return matchNow;
		} else {
			return matchNow;
		}
	}

	// 取得某一参赛用户
	public static ICacheMap matchUserCache = CacheManage.addCache(
			new LinkedCacheMap(1000, true), "matchUser");

	public static MatchUser getMatchUser(int uid) {
		MatchUser matchUser = null;
		synchronized (matchUserCache) {
			matchUser = (MatchUser) matchUserCache.get(new Integer(uid));
			if (matchUser == null) {
				matchUser = service.getMatchUser(" user_id=" + uid);
				if (matchUser != null) {
					matchUserCache.put(new Integer(uid), matchUser);
				}
			}
		}
		return matchUser;
	}

	// 取得某们粉丝
	public static ICacheMap matchFansCache = CacheManage.addCache(
			new LinkedCacheMap(1000, true), "matchFans");

	public static MatchFans getMatchFans(int uid) {
		MatchFans matchFans = null;
		synchronized (matchFansCache) {
			matchFans = (MatchFans) matchFansCache.get(new Integer(uid));
			if (matchFans == null) {
				matchFans = service.getMatchFans(" user_id=" + uid);
				if (matchFans != null) {
					matchFansCache.put(new Integer(uid), matchFans);
				}
			}
		}
		return matchFans;
	}

	// 取得虚拟物品表
	public static HashMap getResMap() {
		if (resMap != null) {
			return resMap;
		}
		synchronized (initLock) {
			if (resMap != null) {
				return resMap;
			}
			// 虚拟物品表
			resMap = service.getResMap("1");
			// 将物品分别按酷币\乐币放入两个List中
			resMapToList();
		}
		return resMap;
	}

	// 取得置顶动态
	public static LinkedHashMap getTopTrends() {
		if (topTrendsMap != null) {
			return topTrendsMap;
		}
		synchronized (initLock) {
			if (topTrendsMap != null) {
				return topTrendsMap;
			}
			// 取得置顶动态
			topTrendsMap = service.getTopTrendsMap("1");
		}
		return topTrendsMap;
	}

	// 取得可兑换商品Map
	public static HashMap getGoodsMap() {
		if (goodsMap != null) {
			return goodsMap;
		}
		synchronized (initLock) {
			if (goodsMap != null) {
				return goodsMap;
			}
			// 取得置顶动态
			goodsMap = service.getGoodMap("1");
		}
		return goodsMap;
	}

	// 取得推荐用户
	public static List getRecoListFromDB() {
		if (recoList != null) {
			return recoList;
		}
		synchronized (initLock) {
			if (recoList != null) {
				return recoList;
			}
			// 取得置顶动态
			recoList = service.getRecoList("1");
		}
		return recoList;
	}

	// 从Bean中取得动态
	public static MatchTopTrends getTop(int key) {
		if (topTrendsMap != null) {
			return (MatchTopTrends) topTrendsMap.get(new Integer(key));
		}
		return null;
	}

	// 从Map中取得虚拟物品Bean
	public static MatchRes getRes(int key) {
		if (resMap != null) {
			return (MatchRes) resMap.get(new Integer(key));
		}
		return null;
	}

	// 从Map中取得可兑换物品Bean
	public static MatchGood getGood(int key) {
		if (goodsMap != null) {
			return (MatchGood) goodsMap.get(new Integer(key));
		}
		return null;
	}

	// 随机取出一个动态
	public String getTopTrendsByRandom() {
		List list = getTopTrendsList();
		if (list.size() == 0) {
			return "";
		}
		MatchTopTrends topTrendsBean = getTop(((Integer) list.get(RandomUtil
				.nextInt(list.size()))).intValue());
		if (topTrendsBean != null) {
			return "<a href=\"" + topTrendsBean.getLinks() + "\">"
					+ topTrendsBean.getContentWml() + "</a><br/>";
		}
		return "";
	}

	/**
	 * 取得一个随机数 total:总范围,count:要显示的数量
	 */
	public static int getRandomInt(int start, int total, int count) {
		if (total == 0 || count == 0) {
			return 0;
		} else if (total <= count) {
			return 0;
		}
		int end = total - count;
		return RandomUtil.nextInt(start, end);
	}

	// 把虚拟物品MAP按酷币与乐币区分
	public static void resMapToList() {
		if (resMap == null || resMap.size() == 0) {
			return;
		}
		// 返回一个包含resMap的ID的List，并且这个List是按价值排序的。
		resList = sortList(new ArrayList(resMap.keySet()));
	}

	/**
	 * 传入一个list，按价值多少排序
	 */
	public static List sortList(List list) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		int tmp = 0;
		int tmpArrays[] = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			tmpArrays[i] = ((Integer) list.get(i)).intValue();
		}
		for (int i = 0; i < tmpArrays.length; i++) {
			for (int j = i; j < tmpArrays.length; j++) {
				if (getRes(tmpArrays[i]).getPrices() < getRes(tmpArrays[j])
						.getPrices()) {
					tmp = tmpArrays[i];
					tmpArrays[i] = tmpArrays[j];
					tmpArrays[j] = tmp;
				}
			}
		}
		List list2 = new ArrayList();
		for (int i = 0; i < tmpArrays.length; i++) {
			list2.add(tmpArrays[i] + "");
		}
		return list2;
	}

//	// 取得参赛用户的总数
//	public static int getPlayerCount() {
//		return SqlUtil.getIntResult("select count(user_id) from match_user", 5);
//	}
//
//	// 取得参赛用户的总数
//	public static int getFansCount() {
//		return SqlUtil.getIntResult("select count(user_id) from match_fans", 5);
//	}

	/**
	 * 判断日期是否存在
	 */
	public boolean isDateExist(int year,int month,int day){
		try{
			Calendar c = Calendar.getInstance();
			c.setLenient(false);
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month - 1);
			c.set(Calendar.DATE, day);
			c.get(Calendar.YEAR);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	// 从可信度数据表中取得城市
	public String getPlaceString(int id) {
		CreditCity city = null;
		if (id > 0) {
			city = CreditAction.service.getCity(" id=" + id);
			if (city != null) {
				return city.getCity();
			}
		}
		return "未填写";
	}

	/**
	 * 城市排名
	 */
	public int cityRank(MatchUser matchUser){
		if (matchUser == null){
			return 0;
		} else if (matchUser.getPlaceId() == 0){
			return 0;
		}
		CreditCity city = CreditAction.service.getCity(" id=" + matchUser.getPlaceId());
		if (city == null){
			return 0;
		}
		// 1、取得与我同城市的所有选美用户
		int tmp = 0;
		String sameUsers = "";
		StringBuilder sb = new StringBuilder();
		List list = SqlUtil.getIntList("select mu.user_id from match_user mu,credit_user_base ub where ub.city=" + city.getId() + " and mu.user_id=ub.user_id",5);
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				tmp = StringUtil.toInt(list.get(i).toString());
				if (tmp > 0){
					sb.append(tmp);
					sb.append(",");
				}
			}
			if (sb.length() > 0){
				sameUsers = sb.substring(0, sb.length() - 1);
			}
		} else {
			return 1;
		}
		// 2、根据靓点排名
		list = SqlUtil.getIntList("select user_id from match_user where user_id in (" + sameUsers + ") order by vote_count desc,user_id desc",5);
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				tmp = StringUtil.toInt(list.get(i).toString());
				if (tmp == matchUser.getUserId()){
					return i+1;
				}
			}
		} else {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 城区排名。根据后台设置好的大区来排
	 * @param matchUser
	 * @return
	 */
	public int cityRank2(MatchUser matchUser){
		if (matchUser == null || matchUser.getPlaceId() == 0){
			return 0;
		}
		// 1、找出大区中所有人，并排名；
		List list = SqlUtil.getIntList("select user_id from match_user where area_id=" + matchUser.getAreaId() + " order by vote_count desc,user_id desc", 5);
		// 2、计算“我”的名次。
		int tmp = 0;
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				tmp = StringUtil.toInt(list.get(i).toString());
				if (tmp == matchUser.getUserId()){
					return i+1;
				}
			}
		} else {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 查看是否第一次上传照片。查看的方法——看图片历史记录中是否有他的记录。
	 */
	public static boolean isFirstEntry(int uid) {
		if (uid <= 0) {
			return true;
		}
		return SqlUtil.getIntResult(
				"select id from match_photo_history where user_id=" + uid
						+ " limit 1;", 5) > 0;
	}

	/**
	 * 添加我的投票宣言
	 */
	public boolean addEnounce(String str) {
		if (str == null || "".equals(str) || str.length() > 20) {
			request.setAttribute("tip", "没有填写用户宣言或宣言字数太长.");
			return false;
		}
		UserBase userBase = null;
		UserBean loginUser = this.getLoginUser();
		if (loginUser == null) {
			request.setAttribute("tip", "您没有登陆.");
			return false;
		}
		userBase = CreditAction.getUserBaseBean(loginUser.getId());
		if (userBase == null || userBase.getCity() <= 0){
			request.setAttribute("tip", "您没有在可信度中填写所在省市区域.");
			return false;
		}
		SqlUtil.executeUpdate("update match_user set enounce = '"
				+ StringUtil.toSql(str) + "' where user_id="
				+ loginUser.getId(), 5);
		// 改缓存
		MatchUser matchUser = getMatchUser(loginUser.getId());
		if (matchUser != null) {
			matchUser.setEncounce(str);
			// 如果用户所在地被置空了的话，就重新写入所在地
			if (matchUser.getPlaceId() == 0){
				SqlUtil.executeUpdate("update match_user set place_id=" + userBase.getCity() + " where user_id=" + loginUser.getId(), 5);
				matchUser.setPlaceId(userBase.getCity());
			}
			// 如果还没有分区的话就加分区
			if (matchUser.getPlaceId() != 0 && matchUser.getAreaId() == 0){
				int areaId = SqlUtil.getIntResult("select flag from credit_city where id=" + matchUser.getPlaceId(),5);
				MatchArea matchArea = getArea(areaId);
				if (matchArea != null){
					SqlUtil.executeUpdate("update match_user set area_id=" + matchArea.getId() + " where user_id=" + matchUser.getUserId(), 5);
					matchUser.setAreaId(matchArea.getId());
					matchArea.setCount(matchArea.getCount() + 1);
					SqlUtil.executeUpdate("update match_area set count=count+1 where id=" + matchArea.getId(), 5);
				}
			}
			matchUserCache.put(new Integer(matchUser.getUserId()), matchUser);
		}
		MatchEnouHistory bean = new MatchEnouHistory();
		bean.setUserId(loginUser.getId());
		bean.setEnounce(str);
		service.addEnouHistory(bean);
		return true;
	}

	/**
	 * 处理（缩小,旋转）。src:待处理图像地址;fitWidth,fitHeight:宽高限制,dest:新图像名称。
	 */
	public static void fitImage(String src, int fitWidth, int fitHeight,
			String dest) throws IOException {
		dest = dest.toLowerCase();
		BufferedImage bi2 = ImageIO.read(new java.io.File(src));

		int width = bi2.getWidth();
		int height = bi2.getHeight();

		if (width <= fitWidth && height <= fitHeight) {
			if(dest.endsWith("gif")) {		// 如果上传的是gif图片，生成gif格式，否则全部生成jpg
				ImageIO.write(bi2, "gif", new java.io.File(dest));
			} else {
				FileOutputStream out = new FileOutputStream(dest); // 输出到文件流
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(bi2); // 近JPEG编码
				out.close();
			}
			return;
		}
		boolean rotate = height < width;
		if (rotate) {
			int tmp = height;
			height = width;
			width = tmp;
		}
		double scale = 1;
		if (width > fitWidth || height > fitHeight) {
			if (width * fitHeight > height * fitWidth) {
				scale = (double) fitWidth / width;
				width = fitWidth;
				height = (int) Math.round(height * scale);
			} else {
				scale = (double) fitHeight / height;
				height = fitHeight;
				width = (int) Math.round(width * scale);
			}
		}

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bi.createGraphics();

		if (rotate) {
			g2d.rotate(Math.toRadians(90));
			g2d.translate(0, -width);
		}
		if (scale != 1)
			g2d.scale(scale, scale);
		g2d.drawImage(bi2, 0, 0, null);

		if(dest.endsWith("gif")) {		// 如果上传的是gif图片，生成gif格式，否则全部生成jpg
			ImageIO.write(bi, "gif", new java.io.File(dest));
		} else {
			FileOutputStream out = new FileOutputStream(dest); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(bi); // 近JPEG编码
			out.close();
		}
	}

	/**
	 * 更新排行榜
	 */
	public static void statCompose() {
		MatchInfo matchInfo = getCurrentMatch();
		// 小于30人就时时更新
		if (matchInfo != null && matchInfo.getUserCount() <= 30){
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table match_rank");
			db
					.executeUpdate("insert into match_rank (user_id,vote_count) (select user_id,vote_count from match_user where checked < 3 order by vote_count desc,user_id desc)");
			topTenList.clear();
			// 更新前十的池子
			topTenList = service.getRankList(" 1 order by id asc limit 10");
			db.release();
			return;
		}
		long now = System.currentTimeMillis();
		if (now < RANK_INTERVAL)
			return;
		synchronized (initLock) {
			if (now < RANK_INTERVAL)
				return;
			DbOperation db = new DbOperation(5);
			db.executeUpdate("truncate table match_rank");
			db
					.executeUpdate("insert into match_rank (user_id,vote_count) (select user_id,vote_count from match_user where checked < 3 order by vote_count desc,user_id desc)");
			topTenList.clear();
			// 更新前十的池子
			topTenList = service.getRankList(" 1 order by id asc limit 10");
			db.release();
			RANK_INTERVAL = now + START_RANK_INTERVAL;
		}
	}

	/**
	 * 更新今日粉丝排行榜
	 */
	public static void todayFans() {
		// 如果粉丝数在30以下，就时时。
		MatchInfo matchInfo = getCurrentMatch();
		if (matchInfo != null && matchInfo.getFansCount() <= 30){
			DbOperation db = new DbOperation(5);
			// 更新今日粉丝排名表
			db.executeUpdate("truncate table match_fans_rank");
			db
					.executeUpdate("insert into match_fans_rank (user_id,vote_count,prices) (select left_uid,count(left_uid),sum(prices) from match_fans_ab2 group by left_uid order by prices desc)");
			db.release();
			return;
		}
		long now = System.currentTimeMillis();
		if (now < FANS_INTERVAL)
			return;
		synchronized (initLock) {
			if (now < FANS_INTERVAL)
				return;
			DbOperation db = new DbOperation(5);
			// 更新今日粉丝排名表
			db.executeUpdate("truncate table match_fans_rank");
			db
					.executeUpdate("insert into match_fans_rank (user_id,vote_count,prices) (select left_uid,count(left_uid),sum(prices) from match_fans_ab2 group by left_uid order by prices desc)");
			db.release();
			FANS_INTERVAL = now + START_RANK_INTERVAL;
		}
	}

	/**
	 * 清空今日粉丝的记录与排行
	 */
	public static void todayFansRank() {
		synchronized (initLock) {
			// 更新今日粉丝表
			SqlUtil.executeUpdate("truncate table match_fans_ab2", 5);
			// 更新今日粉丝排名表
			SqlUtil.executeUpdate("truncate table match_fans_rank", 5);
		}
	}

	/**
	 * 操作靓点
	 */
	public MatchUser updateVote(MatchUser matchUser, int vote) {
		if (matchUser == null || vote == 0) {
			return null;
		} else {
			if (vote > 0) {
				// 加本赛季的靓点
				matchUser.setVoteCount(matchUser.getVoteCount() + vote);
				// 同时加总靓点
				matchUser.setTotalVote(matchUser.getTotalVote() + vote);
			} else {
				vote = Math.abs(vote);
				matchUser.setVoteCount(vote > matchUser.getVoteCount() ? 0
						: matchUser.getVoteCount() - vote);
				matchUser.setTotalVote(vote > matchUser.getTotalVote() ? 0
						: matchUser.getTotalVote() - vote);
			}
			SqlUtil.executeUpdate("update match_user set vote_count="
					+ matchUser.getVoteCount() + ",total_vote=" + matchUser.getTotalVote() + " where user_id="
					+ matchUser.getUserId(), 5);
			// 改缓存
			matchUserCache.put(new Integer(matchUser.getUserId()), matchUser);
		}
		return matchUser;
	}

	/**
	 * 上传
	 */
	public boolean upPic(SmartUpload smUpload) {
		String fileName = "";
		String fileName2 = "";
		String shortName = "";
		File upFile = null;
		UserBase userBase = null;
		UserBean loginUser = this.getLoginUser();
		if (loginUser == null){
			this.setAttribute("tip", "请先登陆.");
			return false;
		}
		userBase = CreditAction.getUserBaseBean(loginUser.getId());
		if (userBase == null ||userBase.getCity() <= 0){
			this.setAttribute("tip", "您没有在可信度中填写所在省市区域.");
			return false;
		}
		try {
			// 得到上传的图片
			upFile = smUpload.getFiles().getFile(0);
			if ("".equals(upFile.getFileName())) {
				this.setAttribute("tip", "请选择一张照片.");
				return false;
			}
			shortName = System.currentTimeMillis() + "." + upFile.getFileExt();
			fileName = ATTACH_ROOT + shortName;
			fileName2 = ATTACH_ROOT + "s" + shortName;
			// 存入服务器
			upFile.saveAs(fileName, SmartUpload.SAVE_PHYSICAL);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SmartUploadException e) {
			e.printStackTrace();
			return false;
		}
//		if (upFile == null) {
//			request.setAttribute("tip", "上传失败,没有找到图片.");
//			return false;
//		} else {
			try {
				// 调整图片
				fitImage(fileName, MAX_WIDTH, MAX_HEIGHT, fileName);
				// 生成缩略图
				fitImage(fileName, S_MAX_WIDTH, S_MAX_HEIGHT, fileName2);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			// 存入数据库
			MatchUser matchUser = getMatchUser(loginUser.getId());
			if (matchUser == null) {
				matchUser = new MatchUser();
				matchUser.setUserId(loginUser.getId());
				matchUser.setPhoto(shortName);
				matchUser.setPhoto2("s" + shortName);
				matchUser.setEncounce("");
				matchUser.setPhotoFrom(0);
				matchUser.setTotalVote(0);
				matchUser.setTotalConsume(0);
				matchUser.setPlaceId(userBase.getCity());	// 我的所在地区
			} else {
				matchUser.setPhoto(shortName);
				matchUser.setPhoto2("s" + shortName);
				matchUser.setChecked(0);	// 改为审核未通过
				matchUser.setPlaceId(userBase.getCity());	// 我的所在地区
			}
			// 如果大区ID=0的话就尝试写入大区信息。
			if (matchUser.getAreaId() == 0 && matchUser.getPlaceId() > 0){
				matchUser.setAreaId(SqlUtil.getIntResult("select flag from credit_city where id=" + matchUser.getPlaceId(),5));
			}
			addMatchUser(matchUser);
			matchUserCache.put(new Integer(matchUser.getUserId()), matchUser);
			return true;
//		}
	}

	/**
	 * 添加参赛用户
	 */
	public void addMatchUser(MatchUser matchUser) {
		if (matchUser == null) {
			return;
		}
		if (service.addUser(matchUser) == 2) {
			// 参赛总人数++
			MatchInfo matchInfo = getCurrentMatch();
			if (matchInfo != null) {
				SqlUtil.executeUpdate(
						"update match_info set user_count=user_count+1 where id="
								+ matchInfo.getId(), 5);
				matchInfo.setUserCount(matchInfo.getUserCount() + 1);
			}
			// 某一分区的总人数++
			if (matchUser.getAreaId() > 0){
				SqlUtil.executeUpdate("update match_area set count=count+1 where id=" + matchUser.getAreaId(), 5);
				MatchArea matchArea = getArea(matchUser.getAreaId());
				if (matchArea != null){
					matchArea.setCount(matchArea.getCount() + 1);
				}
			}
		}
	}

	/**
	 * 更新一位用户的大区信息
	 * @param uid
	 * 要写入分区信息的uid。如果传入-100，则所有人的分区信息全部更新
	 */
	public static void chageArea(int uid){
		if (uid == -100){
			// 更新所有人的所在地
			SqlUtil.executeUpdate("update match_user m,credit_user_base c set m.place_id=c.city where m.user_id=c.user_id", 5);
			// 更新所有人的分区
			SqlUtil.executeUpdate("update match_user m,credit_city c set m.area_id= c.flag where m.place_id=c.id", 5);
			// 更新各分区的总人数
			List list = getAreaList();
			if (list != null && list.size() > 0){
				MatchArea matchArea = null;
				for (int i = 0 ; i < list.size() ; i++){
					if (list.get(i) != null){
						int count = 0;
						matchArea = (MatchArea)areaMap.get(list.get(i));
						if (matchArea != null){
							count = SqlUtil.getIntResult("select count(user_id) from match_user where area_id=" + matchArea.getId(), 5);
							SqlUtil.executeUpdate("update match_area set count=" + count + " where id=" + matchArea.getId(), 5);
							matchArea.setCount(count);
						}
					}
				}
			}
			// 最后清空用户缓存
			matchUserCache.clear();
		} else {
			MatchUser matchUser = getMatchUser(uid);
			if (matchUser != null){
				// 重新读取的该用户的分区ID
				int areaId = SqlUtil.getIntResult("select c.flag from credit_city c,match_user m where m.user_id=" + matchUser.getUserId() + " and m.place_id=c.id" , 5);
				MatchArea matchArea = getArea(areaId);
				if (areaId > 0 && matchArea != null){
					// 此用户还没有分区ID
					if (matchUser.getAreaId() == 0){
						// 更新数据库
						SqlUtil.executeUpdate("update match_user set area_id=" + matchArea.getId() + " where user_id=" + matchUser.getUserId(), 5);
						SqlUtil.executeUpdate("update match_area set count=count+1 where id=" + matchArea.getId(), 5);
						// 更新缓存
						matchArea.setCount(matchArea.getCount() + 1);
						matchUser.setAreaId(matchArea.getId());
					} else {
						if (matchArea.getId() != matchUser.getAreaId()){
							// 写入新的分区
							SqlUtil.executeUpdate("update match_user set area_id=" + matchArea.getId() + " where user_id=" + matchUser.getUserId(), 5);
							// 原分区人数-1
							SqlUtil.executeUpdate("update match_area set count=count-1 where id=" + matchUser.getAreaId() + " and count>0", 5);
							// 新分区人数+1
							SqlUtil.executeUpdate("update match_area set count=count+1 where id=" + matchArea.getId(), 5);
							// 更新缓存
							matchUser.setAreaId(matchArea.getId());
							matchArea.setCount(matchArea.getCount() + 1);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 购买虚拟物品(res:要购买的物品,count:购买数量)
	 */
	public boolean buy(MatchRes res, int count) {
		MatchInfo matchInfo = getCurrentMatch();
		if (matchInfo == null || matchInfo.getFalg() != 1) {
			request.setAttribute("tip", "抱歉,比赛已结束,不能购买.");
			return false;
		}
		if (res == null) {
			request.setAttribute("buyResultStr", "该物品不存在.");
			return false;
		}
		if (count < 1 || count > 99) {
			request.setAttribute("buyResultStr", "物品数量错误(1-99).");
			return false;
		}
		int loginUid = this.getLoginUser().getId();
		long consume = (long)res.getPrices() * count;
		if (res.getCur() == 0) {
			// 购买乐币商品
			// 1,取得该用户的乐币数量
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUid);
			int money = us == null ? 0 : us.getGamePoint();
			if (money / res.getPrices() < count) {
				request.setAttribute("buyResultStr", "抱歉,您的余额不足暂时不能购买.");
				return false;
			}
			// 2,扣钱
			if (UserInfoUtil.updateUserCash(loginUid, 0 - consume, 19, "选秀买了" + count + "个" + res.getResName())) {
				request.setAttribute("buyResultStr", "恭喜您购买了" + count + "个"
						+ res.getResName());
			}
		} else {
			// 购买酷币商品
			// 1,取得该用户的乐币数量
			UserInfoBean bean = ShopAction.shopService.getUserInfo(loginUid);
			if (bean == null) {
				request.setAttribute("buyResultStr", "抱歉,您的余额不足暂时不能购买.");
				return false;
			}
			float money = bean.getGold();
			float consumef = consume / 100f;
			if (money < consumef) {
				request.setAttribute("buyResultStr", "抱歉,您的余额不足暂时不能购买.");
				return false;
			}
			// 2,扣钱
			ShopUtil.updateUserGold(loginUid, consumef, 6);
			request.setAttribute("buyResultStr", "恭喜您购买了" + count + "个"
					+ res.getResName());
		}
		// 3,更新用户所持有的物品
		MatchFans fans = getMatchFans(loginUid);
		int good[] = null;
		if (fans != null) {
			good = fans.getGood();
			good[res.getId()] = good[res.getId()] + count;
			SqlUtil
					.executeUpdate("update match_fans set good" + res.getId()
							+ "=" + good[res.getId()] + " where user_id="
							+ loginUid, 5);
			// 改缓存
			fans.setGood(good);
			matchFansCache.put(new Integer(fans.getUserId()), fans);
		} else {
			fans = new MatchFans();
			fans.setUserId(loginUid);
			good = new int[8];
			good[res.getId()] = count;
			fans.setGood(good);
			service.addFans(fans);
			matchFansCache.put(new Integer(fans.getUserId()), fans);
		}
		// 4,写动态
		MatchTrends trend = new MatchTrends();
		trend.setLeftUid(loginUid);
		trend.setContent("%l购买了" + count + "个" + res.getResName());
		trend.setLink("");
		trend.setFlag(1);
		service.addTrends(trend);
		// 5、写入购买记录(后台查询用)
		MatchBuylog log = new MatchBuylog();
		log.setUserId(loginUid);
		log.setResId(res.getId());
		log.setResName(res.getResName());
		log.setCount(count);
		log.setPrices((int)consume);
		log.setCur(res.getCur());
		service.addBuyLog(log);
		return true;
	}

	/**
	 * 统计我有哪些物品,各多少个.物品的实际数量=购买总数-已用掉的数量.参数links指向物品说明页的地址
	 */
	public String statMyGoods(String links) {
		if (links == null || "".equals(links)) {
			return "";
		}
		MatchFans fans = getMatchFans(this.getLoginUser().getId());
		if (fans == null) {
			return "暂无";
		}
		StringBuilder sb = new StringBuilder();
		int good[] = fans.getGood();
		for (int i = 0; i < good.length; i++) {
			if (good[i] > 0) {
				sb.append("<a href=\"" + links + "&amp;rid=" + i + "\">"
						+ getRes(i).getResName() + "</a>:" + good[i] + "个,");
			}
		}
		if (sb.length() > 0) {
			return sb.substring(0, sb.length() - 1);
		} else {
			return "暂无";
		}
	}

	/**
	 * 某一参赛用户目前有哪些物品，总数是多少。
	 * 
	 * @param uid
	 * @return
	 */
	public String getSheHas(MatchUser matchUser) {
		if (matchUser == null) {
			return "暂无";
		}
		StringBuilder sb = new StringBuilder();
		int good[] = matchUser.getGood();
		for (int i = 0; i < good.length; i++) {
			if (good[i] > 0) {
				sb.append(getRes(i).getResName() + good[i] + "个,");
			}
		}
		if (!"".equals(sb.toString())) {
			return sb.substring(0, sb.length() - 1);
		}
		return "暂无";
	}

	/**
	 * 确认投票清单
	 */
	public boolean checkVote(HttpServletRequest request) {
		MatchInfo matchInfo = getCurrentMatch();
		if (matchInfo == null || matchInfo.getFalg() != 1) {
			request.setAttribute("tip", "抱歉,比赛已结束,无法投票.");
			return false;
		}
		// 取得我所拥有的物品列表
		MatchFans fans = getMatchFans(this.getLoginUser().getId());
		if (fans == null) {
			request.setAttribute("tip", "您还没有奢侈品,<a href=\"shop.jsp\">请购买</a>.");
			return false;
		}
		int good[] = fans.getGood();
		// HashMap matchVoteMap = new HashMap();
		int voteGood[] = new int[8];
		if (good.length > 0) {
			int tmp = 0;
			int voteCount = 0;
			String voteList = "";
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < good.length; i++) {
				// 看我在前一页面是否写了要投的数目
				tmp = this.getParameterInt("g" + i);
				if (tmp > 0) {
					// 我并没有tmp个相应物品
					if (tmp > good[i]) {
						request.setAttribute("tip",
								"抱歉,你选择的奢侈品有误,不能为她增加靓点.请重新选择.");
						return false;
					} else {
						// 有,写入清单,并且计算能增加的靓点
						sb.append(tmp);
						sb.append("个");
						sb.append(getRes(i).getResName());
						sb.append(",");
//						sb.append(tmp + "个" + getRes(i).getResName() + ",");
						voteCount += tmp * getRes(i).getPoint();
						voteGood[i] = tmp;
					}
				}
			}
			if (sb.length() > 0) {
				voteList = sb.substring(0, sb.length() - 1);
			} else {
//				request.setAttribute("tip", "您还没有奢侈品,<a href=\"shop.jsp\">请购买</a>.");
				request.setAttribute("tip", "抱歉,你选择的奢侈品有误,不能为她增加靓点.请重新选择.");
				return false;
			}
			session.setAttribute("voteGood", voteGood);
			request.setAttribute("voteList", voteList);
			request.setAttribute("voteCount", voteCount + "");
			return true;
		}
		return false;
	}

	/**
	 * 投票
	 */
	public boolean doVote(int uid) {
		MatchInfo matchInfo = getCurrentMatch();
		if (matchInfo == null || matchInfo.getFalg() != 1) {
			request.setAttribute("tip", "抱歉,比赛已结束,无法投票.");
			return false;
		}
		if (uid <= 0) {
			request.setAttribute("tip", "用户不存在.");
			return false;
		}
		if (!isFirstEntry(uid)) {
			request.setAttribute("tip", "无法给初次参赛,并且照片未通过审核的用户投票.");
			return false;
		}
		int loginUid = this.getLoginUser().getId();
		MatchUser matchUser = getMatchUser(uid);
		if (matchUser == null) {
			request.setAttribute("tip", "参赛用户不存在.");
			return false;
		}
		if (matchUser.getChecked() == 3) {
			request.setAttribute("tip", "该用户已被取消参赛资格.");
			return false;
		}
		// 1、取得我目前所持有的物品
		MatchFans fans = getMatchFans(loginUid);
		if (fans == null) {
			request.setAttribute("tip", "您没有虚拟物品.请去乐后商城购买.");
			return false;
		}
		int good[] = fans.getGood(); // 这是粉丝所持有的物品
		int matchUserGood[] = matchUser.getGood(); // 这是用户自己所持有的物品
		// 2、检查session中得到的用户要投的物品
		int voteGood[] = (int[]) session.getAttribute("voteGood");
		if (voteGood == null) {
			request.setAttribute("tip", "所投的商品错误或没有找到商品.");
			return false;
		}
		int totalPrices = 0;
		for (int i = 0; i < voteGood.length; i++) {
			if (voteGood[i] != 0) {
				if (good[i] < voteGood[i]) {
					request
							.setAttribute("tip",
									"抱歉,你选择的礼品数量有误,不能为她增加靓点.请重新选择.");
					return false;
				} else {
					// 累加靓点值
					totalPrices += voteGood[i] * getRes(i).getPoint();
				}
			}
		}

		// 3、检查确认无误，扣掉相应的物品，给被投票用户加相应的靓点

		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		int gamePoint = 0; // 乐币靓点数
		int point = 0; // 酷币靓点数
		String sql = "";
		String content = "";
		for (int i = 0; i < voteGood.length; i++) {
			if (voteGood[i] != 0) {
				good[i] = good[i] - voteGood[i];
				if (good[i] < 0) {
					good[i] = 0;
				}
				matchUserGood[i] = matchUserGood[i] + voteGood[i]; // 把从粉丝那里扣得的物品加到被投票的用户上
				// fans表的SQL语句
				sb.append("good");
				sb.append(i);
				sb.append("=");
				sb.append(good[i]);
				sb.append(",");
				// matchUser表的SQL语句
				sb3.append("good");
				sb3.append(i);
				sb3.append("=good");
				sb3.append(i);
				sb3.append("+");
				sb3.append(voteGood[i]);
				sb3.append(",");
				// 页面提示
				sb2.append(voteGood[i]);
				sb2.append("个");
				sb2.append(getRes(i).getResName());
				sb2.append(",");
				// 查看是酷币靓点还是乐币靓点，在最后边写入数据库
				if (getRes(i).getCur() == 0) {
					gamePoint += (getRes(i).getPoint() * voteGood[i]);
				} else {
					point += (getRes(i).getPoint() * voteGood[i]);
				}
			}
		}
		if (sb.length() > 0) {
			sql = sb.substring(0, sb.length() - 1);
		}
		if (sb2.length() > 0) {
			content = sb2.substring(0, sb2.length() - 1);
		}
		MatchRank rank = service.getMatchRank(" user_id=" + uid);
		if (!"".equals(sql)) {
			// 3.1，扣除我的相应物品
			SqlUtil.executeUpdate("update match_fans set " + sql
					+ " where user_id=" + loginUid, 5);
			fans.setGood(good);
			// 改缓存
			matchFansCache.put(new Integer(fans.getUserId()), fans);
			// 3.2，给她加相应靓点
			SqlUtil.executeUpdate("update match_user set " + sb3.toString()
					+ "vote_count=vote_count+" + totalPrices
					+ ",total_vote=total_vote+" + totalPrices
					+ " where user_id=" + uid, 5);
			matchUser.setVoteCount(matchUser.getVoteCount() + totalPrices);
			matchUser.setTotalVote(matchUser.getTotalVote() + totalPrices);
			matchUser.setGood(matchUserGood);
			// 改缓存
			matchUserCache.put(new Integer(matchUser.getUserId()), matchUser);
			int newRank = SqlUtil.getIntResult(
					"select id from match_rank where vote_count<"
							+ matchUser.getVoteCount()
							+ " order by id asc limit 1", 5);
//			if (newRank == -1) {
//				newRank = 1;
//			}
			// 3.3,写动态
			MatchTrends trend = new MatchTrends();
			trend.setLeftUid(loginUid);
			trend.setRightUid(uid);
			trend.setContent("%l送给%r" + content);
			trend.setLink("");
			trend.setFlag(0);
			service.addTrends(trend);
			// 3.3.1写入记录
			todayFans();
			sb3.append("prices=prices+" + totalPrices + ",");
			// 3.3.3写入今日记录
			MatchFansAb2 fansAb2 = service.getMatchFansAb2(" left_uid="
					+ loginUid + " and right_uid=" + uid);
			if (fansAb2 == null) {
				// 今日无记录
				fansAb2 = new MatchFansAb2();
				fansAb2.setLeftUid(loginUid);
				fansAb2.setRightUid(uid);
				fansAb2.setGood(voteGood);
				fansAb2.setPrices(totalPrices);
				service.addFansAb2(fansAb2);
			} else {
				SqlUtil.executeUpdate("update match_fans_ab2 set "
						+ sb3.substring(0, sb3.length() - 1) + " where id="
						+ fansAb2.getId(), 5);
			}

			// 3.3.4写入投票总人数,此用户从来没投过票，则视为新的粉丝
			if (SqlUtil.getIntResult("select id from match_fans_ab where left_uid=" + loginUid ,5) <= 0){
				matchInfo.setFansCount(matchInfo.getFansCount() + 1);
				SqlUtil.executeUpdate(
						"update match_info set fans_count=fans_count+1 where id="
								+ matchInfo.getId(), 5);
			}
			
			// 3.3.5写入总记录
			MatchFansAb fansAb = service.getMatchFansAb(" left_uid=" + loginUid
					+ " and right_uid=" + uid);
			if (fansAb == null) {
				// 无记录
				fansAb = new MatchFansAb();
				fansAb.setLeftUid(loginUid);
				fansAb.setRightUid(uid);
				fansAb.setGood(voteGood);
				fansAb.setPrices(totalPrices);
				service.addFansAb(fansAb);
			} else {
				SqlUtil.executeUpdate("update match_fans_ab set "
						+ (sb3.toString().endsWith(",") ? sb3.substring(0, sb3
								.length() - 1) : sb3.toString()) + " where id="
						+ fansAb.getId(), 5);
			}
			MatchVoted matchVoted = new MatchVoted();
			matchVoted.setUserId(loginUid);
			matchVoted.setGood(voteGood);
			matchVoted.setPrices(totalPrices);
			matchVoted.setVoteCount(1);
			sb3.append("vote_count=vote_count+1");
			service.addVoted(matchVoted, sb3.toString().endsWith(",") ? sb3
					.substring(0, sb3.length() - 1) : sb3.toString());
			if (newRank == -1){
				// 此用户目录还没有进入排名，所以单独写一个提示
				request.setAttribute("tip", "感谢您为"
						+ UserInfoUtil.getUser(matchUser.getUserId())
								.getNickNameWml()
						+ "投票,她现在的靓点是"
						+ matchUser.getVoteCount()
						+ ",您为她增加了"
						+ totalPrices
						+ "靓点.");
			} else {
				// 正常的提示
				request.setAttribute("tip", "感谢您为"
						+ UserInfoUtil.getUser(matchUser.getUserId())
								.getNickNameWml()
						+ "投票,她现在的靓点是"
						+ matchUser.getVoteCount()
						+ ",您为她增加了"
						+ totalPrices
						+ "靓点,"
						+ UserInfoUtil.getUser(matchUser.getUserId())
								.getNickNameWml()
						+ "因您的礼物,她的排名上升了"
						+ (rank == null ? matchInfo.getUserCount() : rank.getId()
								- newRank) + "位.目前排名暂时为" + newRank + "!请再接再厉啦!");
			}
			session.removeAttribute("voteGood");
			// 记录靓点总数
			matchInfo.setVoteCount(matchInfo.getVoteCount() + totalPrices);
			matchInfo.setGamePointCount(matchInfo.getGamePointCount()
					+ gamePoint);
			matchInfo.setPointCount(matchInfo.getPointCount() + point);
			SqlUtil.executeUpdate(
					"update match_info set vote_count=vote_count+"
							+ totalPrices
							+ ",game_point_count=game_point_count+" + gamePoint
							+ ",point_count=point_count+" + point
							+ " where id=" + matchInfo.getId(), 5);
			return true;
		}
		return false;
	}

	/**
	 * 最佳支持者.type=0：历史最佳支持者；type=1本日最佳支持者
	 */
	public String getMyBestFans(int uid, int type) {
		if (uid <= 0) {
			return null;
		}
		int loginUid = this.getLoginUser().getId();
		if (type == 0) {
			MatchFansAb fansAb = service.getMatchFansAb(" right_uid=" + uid
					+ " order by prices desc limit 1");
			if (fansAb == null) {
				return "";
			} else {
				return "<a href=\"/user/ViewUserInfo.do?userId="
						+ fansAb.getLeftUid()
						+ "\">"
						+ UserInfoUtil.getUser(fansAb.getLeftUid())
								.getNickNameWml() + "</a>是"
						+ (uid == loginUid ? "我" : "她") + "历史最佳支持者,共投了"
						+ fansAb.getPrices() + "个靓点.";
			}
		} else if (type == 1) {
			MatchFansAb2 fansAb2 = service.getMatchFansAb2(" right_uid=" + uid
					+ " order by prices desc limit 1");
			if (fansAb2 == null) {
				return "";
			} else {
				return "<a href=\"/user/ViewUserInfo.do?userId="
						+ fansAb2.getLeftUid()
						+ "\">"
						+ UserInfoUtil.getUser(fansAb2.getLeftUid())
								.getNickNameWml() + "</a>是"
						+ (uid == loginUid ? "我" : "她") + "本日最佳簇拥,共投了"
						+ fansAb2.getPrices() + "个靓点.";
			}
		}
		return "";
	}

	/**
	 * 取得照片地址。如果matchUser==null或user中的照片正在审核，则从历史记录中取得最新的照片。isSmallPic==true，
	 * 返回缩略图。否则返回大图。
	 */
	public String getCurrentPhoto(MatchUser matchUser, boolean isSmallPic) {
		if (matchUser == null) {
			return ATTACH_URL_ROOT + "o.gif";
		} else {
			if (matchUser.getChecked() == 1 || matchUser.getChecked() == 3) {
				return getCurrentAddress(matchUser.getPhotoFrom())
						+ (isSmallPic ? matchUser.getPhoto2() : matchUser
								.getPhoto());
			} else {
				MatchPhotoHistory history = MatchAction.service
						.getMatchPhotoHistory(" user_id="
								+ matchUser.getUserId()
								+ " order by id desc limit 1");
				if (history != null) {
					return getCurrentAddress(history.getPhotoFrom())
							+ (isSmallPic ? history.getPhoto2() : history
									.getPhoto());
				} else {
					return ATTACH_URL_ROOT + "o.gif";
				}
			}
		}
	}

	/**
	 * 取得图片的相对地址
	 */
	public String getCurrentAddress(int photoFrom) {
		if (photoFrom == 0) {
			return MatchAction.ATTACH_URL_ROOT;
		} else {
			return Constants.MYALBUM_RESOURCE_ROOT_URL;
		}
	}

	/**
	 * 取得图片的绝对地址
	 */
	public String getCurrentAddress2(int photoFrom) {
		if (photoFrom == 0) {
			return MatchAction.ATTACH_ROOT;
		} else {
			return Constants.MYALBUM_FILE_PATH;
		}
	}

	/**
	 * 添加商品
	 */
	public boolean addGood(SmartUpload smUpload) {
		String gname = smUpload.getRequest().getParameter("gname");
		int price = StringUtil.toInt(smUpload.getRequest()
				.getParameter("price"));
		int price2 = StringUtil.toInt(smUpload.getRequest().getParameter(
				"price2"));
		int count = StringUtil.toInt(smUpload.getRequest()
				.getParameter("count"));
		int flag = StringUtil.toInt(smUpload.getRequest().getParameter("flag"));
		int prio = StringUtil.toInt(smUpload.getRequest().getParameter("prio"));
		String describe = smUpload.getRequest().getParameter("describe");
		File upFile = smUpload.getFiles().getFile(0);
		if (gname == null || "".equals(gname) || gname.length() > 20) {
			this.setAttribute("tip", "没有输入品名或品名超过了20字");
			return false;
		}
		if (price < 0) {
			this.setAttribute("tip", "专柜价格输入错误");
			return false;
		}
		if (price2 <= 0) {
			this.setAttribute("tip", "靓点价格输入错误");
			return false;
		}
		if (count < 0) {
			this.setAttribute("tip", "库存输入错误错误");
			return false;
		}
		if (flag < 0 || flag > 5) {
			this.setAttribute("tip", "分类错误");
			return false;
		}
		if (flag > 0) {
			if (SqlUtil.getIntResult("select id from match_good where flag="
					+ flag, 5) > 0) {
				this.setAttribute("tip", "此类别已存在商品");
				return false;
			}
		}
		if (prio < 1 || prio > 10) {
			this.setAttribute("tip", "优先级输入错误");
			return false;
		}
		if (describe == null || "".equals(describe) || describe.length() > 200) {
			this.setAttribute("tip", "没有输入描述或描述超过了200字");
			return false;
		}
		if (upFile == null || "".equals(upFile.getFileName())) {
			this.setAttribute("tip", "没有选择图片?");
			return false;
		}
		String fileName = "";
		String shortName = "";
		// 存入服务器
		try {
			shortName = System.currentTimeMillis() + "." + upFile.getFileExt();
			fileName = ATTACH_GOOD_ROOT + shortName;
			upFile.saveAs(fileName, SmartUpload.SAVE_PHYSICAL);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (SmartUploadException e) {
			e.printStackTrace();
			return false;
		}
		// 存入商品表
		MatchGood matchGood = new MatchGood();
		matchGood.setGoodName(gname);
		matchGood.setPrice(price); // 专柜价格
		matchGood.setPrice2(price2); // 靓点价格
		matchGood.setCount(count);
		matchGood.setCountNow(count); // 现在库存=库存
		matchGood.setFlag(flag);
		matchGood.setPrio(prio); // 优先级
		matchGood.setDescribe(describe);
		matchGood.setPhoto(shortName);
		matchGood.setHide(1); // 不显示
		matchGood.setBuyCount(0); // 购买数
		int id = service.addGood(matchGood);
		matchGood.setId(id);
		goodsMap.put(new Integer(matchGood.getId()), matchGood);
		return true;
	}

	/**
	 * 修改商品
	 */
	public boolean modifyGood(SmartUpload smUpload, MatchGood matchGood) {
		if (matchGood == null) {
			this.setAttribute("tip", "没有输入原商品bean");
			return false;
		}
		String gname = smUpload.getRequest().getParameter("gname");
		int price = StringUtil.toInt(smUpload.getRequest()
				.getParameter("price"));
		int price2 = StringUtil.toInt(smUpload.getRequest().getParameter(
				"price2"));
		int count = StringUtil.toInt(smUpload.getRequest()
				.getParameter("count"));
		int flag = StringUtil.toInt(smUpload.getRequest().getParameter("flag"));
		int prio = StringUtil.toInt(smUpload.getRequest().getParameter("prio"));
		String describe = smUpload.getRequest().getParameter("describe");
		File upFile = smUpload.getFiles().getFile(0);
		if (flag > 0) {
			int id = SqlUtil.getIntResult(
					"select id from match_good where flag=" + flag, 5);
			if (id > 0 && id != matchGood.getId()) {
				this.setAttribute("tip", "该商品已存在,请在原商品记录中修改.");
				return false;
			}
		}
		if (gname == null || "".equals(gname) || gname.length() > 20) {
			this.setAttribute("tip", "没有输入品名或品名超过了20字");
			return false;
		}
		if (price < 0) {
			this.setAttribute("tip", "专柜价格输入错误");
			return false;
		}
		if (price2 < 0) {
			this.setAttribute("tip", "靓点价格输入错误");
			return false;
		}
		if (count < 0) {
			this.setAttribute("tip", "库存输入错误错误");
			return false;
		}
		if (flag < 0 || flag > 5) {
			this.setAttribute("tip", "分类错误");
			return false;
		}
		if (prio < 1 || prio > 10) {
			this.setAttribute("tip", "优先级输入错误");
			return false;
		}
		if (describe == null || "".equals(describe) || describe.length() > 200) {
			this.setAttribute("tip", "没有输入描述或描述超过了200字");
			return false;
		}
		if (!"".equals(upFile.getFileName())) {
			// 记录原图片
			String oldName = matchGood.getPhoto();
			// 上传新图片
			String fileName = "";
			String shortName = "";
			try {
				shortName = System.currentTimeMillis() + "."
						+ upFile.getFileExt();
				fileName = ATTACH_GOOD_ROOT + shortName;
				upFile.saveAs(fileName, SmartUpload.SAVE_PHYSICAL);
				matchGood.setPhoto(shortName);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			} catch (SmartUploadException e) {
				e.printStackTrace();
				return false;
			}
			// 删除原图片
			java.io.File file = new java.io.File(ATTACH_GOOD_ROOT + oldName);
			if (file.exists()) {
				file.delete();
			}
		}
		// 修改商品表
		matchGood.setGoodName(gname);
		matchGood.setPrice(price); // 专柜价格
		matchGood.setPrice2(price2); // 靓点价格
		matchGood.setCount(count);
		matchGood.setCountNow(count); // 现在库存=库存
		matchGood.setFlag(flag);
		matchGood.setPrio(prio); // 优先级
		matchGood.setDescribe(describe);
		service.modifyGood(matchGood);
		goodsMap.put(new Integer(matchGood.getId()), matchGood);
		return true;
	}

	/**
	 * 是否可以兑换物品
	 */
	public boolean isCanExch(MatchGood matchGood) {
		if (matchGood == null) {
			return false;
		}
		MatchInfo matchInfo = getCurrentMatch();
		if (matchGood.getFlag() == 0) {
			// 靓点兑换物品，谁都可以买
			return true;
		}
		MatchRank matchRank = service.getMatchRank(" user_id="
				+ this.getLoginUser().getId());
		if (matchRank != null && matchInfo != null && matchInfo.getFalg()==2) {
			switch (matchGood.getFlag()) {
			case 1: {
				// 只有第一名可以买
				return matchRank.getId() == 1;
			}
			case 2:
				return matchRank.getId() == 2;
			case 3:
				return matchRank.getId() == 3;
			case 4:
				// 只有4、5、6名可以买
				return matchRank.getId() >= 4 && matchRank.getId() <= 6;
			case 5:
				// 只有7，8，9，10名可以买
				return matchRank.getId() >= 7 && matchRank.getId() <= 10;
			}
		}
		return false;
	}

	/**
	 * 确认兑换商品(matchUser:兑换者,goodId:要兑换的物品ID)
	 */
	public boolean confirmExch(MatchUser matchUser, int goodId) {
		if (matchUser == null) {
			this.setAttribute("tip", "找不到参赛用户.");
			return false;
		}
		String gid = (String) session.getAttribute("gid");
		if (gid == null || StringUtil.toInt(gid) != goodId) {
			this.setAttribute("tip", "您没有选择要兑换的物品?");
			return false;
		}
//		MatchInfo matchInfo = getCurrentMatch();
//		if (getGood(goodId) != null && getGood(goodId).getFlag() > 0) {
//			if (matchInfo == null || matchInfo.getFalg() != 2) {
//				request.setAttribute("tip", "抱歉,比赛尚未结束,不能兑换物品.");
//				return false;
//			}
//		}
		if (getGood(goodId).getFlag() > 0){
			this.setAttribute("tip", "非靓点商品不可兑换.");
			return false;
		}
		String phone = this.getParameterNoEnter("p");
		String name = this.getParameterNoEnter("n");
		String address = this.getParameterNoEnter("a");
		if (StringUtil.toLong(phone) <= 0) {
			this.setAttribute("tip", "电话号码输入错误,只能输入数字.");
			return false;
		}
		if (name == null || "".equals(name) || name.length() > 10) {
			this.setAttribute("tip", "您没有输入姓名或姓名多于10个汉字.");
			return false;
		}
		if (address == null || "".equals(address) || address.length() > 100) {
			this.setAttribute("tip", "您没有输入地址或地址多于100个汉字.");
			return false;
		}
		// 把session中的标记删除
		session.removeAttribute("gid");
		synchronized (initLock) {
			MatchGood matchGood = getGood(goodId);
			if (matchGood == null) {
				this.setAttribute("tip", "没有找到相关物品.");
				return false;
			} else if (matchGood.getCountNow() <= 0) {
				this.setAttribute("tip", "此商品已无库存.");
				return false;
			}
//			} else if (!isCanExch(matchGood)) {
//				this.setAttribute("tip", "根据您的排名,不能兑换此商品.");
//				return false;
//			}
			if (matchUser.getCurrentVote2() >= matchGood.getPrice2()) {
				// 扣掉靓点
				SqlUtil.executeUpdate(
						"update match_user set cons_count=cons_count+1,total_consume=total_consume+"
								+ matchGood.getPrice2() + " where user_id="
								+ matchUser.getUserId(), 5);
				// 修改商品的现在库存数和购买次数
				SqlUtil
						.executeUpdate(
								"update match_good set count_now=count_now-1,buy_count=buy_count+1 where count_now>0 and id="
										+ matchGood.getId(), 5);
				// 改缓存
				matchUser.setTotalConsume(matchUser.getTotalConsume()
						+ matchGood.getPrice2());
				matchUser.setConsCount(matchUser.getConsCount() + 1);
				matchUserCache.put(new Integer(matchUser.getUserId()),
						matchUser);
				if (matchGood.getCountNow() - 1 >0){
					matchGood.setCountNow(matchGood.getCountNow() - 1);
				} else {
					matchGood.setCountNow(0);
				}
				goodsMap.remove(new Integer(matchGood.getId()));
				goodsMap.put(new Integer(matchGood.getId()), matchGood);
				// 写订单
				MatchOrder matchOrder = new MatchOrder();
				matchOrder.setUserId(matchUser.getUserId());
				matchOrder.setPhone(phone);
				matchOrder.setUserName(name);
				matchOrder.setAddress(address);
				matchOrder.setGoodName(matchGood.getGoodName());
				matchOrder.setGoodId(matchGood.getId());
				matchOrder.setSendTime(getWed()); // 现在最近的周三的日期(毫秒数)
				matchOrder.setPrice(matchGood.getPrice2());
				matchOrder.setActualPrice(matchGood.getPrice2());
				int lastId = service.addOrder(matchOrder);
				// 写订货记录
				MatchExch matchExch = new MatchExch();
				matchExch.setUserId(matchUser.getUserId());
				matchExch.setGoodId(matchGood.getId());
				matchExch.setGoodName(matchGood.getGoodName());
				matchExch.setVote(matchGood.getPrice2());
				matchExch.setOrderId(lastId);
				service.addExch(matchExch);
				return true;
			} else {
				this.setAttribute("tip", "您的靓点不够,不能兑换"
						+ matchGood.getGoodNameWml() + "商品.该商品靓点"
						+ matchGood.getPrice2() + ",邀请好友投票可以获得更多靓点!");
				return false;
			}
		}
	}

	/**
	 * 新创建一个比赛
	 */
	public boolean createMatch(MatchInfo matchInfo) {
		if (matchInfo == null) {
			return false;
		}
		Date startDate = DateUtil.parseDate(matchInfo.getStartTimeStr());
		Date endDate = DateUtil.parseDate(matchInfo.getEndTimeStr());
		if (startDate == null) {
			request.setAttribute("tip", "开始日期错误.");
			return false;
		}
		if (endDate == null) {
			request.setAttribute("tip", "结束日期错误.");
			return false;
		}
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		if (startTime < System.currentTimeMillis()) {
			request.setAttribute("tip", "开赛时间小于当前日期?");
			return false;
		}
		if (endTime <= startTime) {
			request.setAttribute("tip", "结束日期先于开始日期?");
			return false;
		}
		matchInfo.setStartTime(startTime);
		matchInfo.setEndTime(endTime);
		// 查看传入的开始时间与结束时间是否与已录入的时间重复。
		MatchInfo matchInfo2 = null;
		List list = service.getInfoList(" 1");
		for (int i = 0; i < list.size(); i++) {
			matchInfo2 = (MatchInfo) list.get(i);
			if (matchInfo2 != null) {
				// 1、开赛时间是否与已录入的时间有冲突
				if (matchInfo2.getStartTime() == matchInfo.getStartTime()) {
					request.setAttribute("tip", "与现有开赛日期重复.");
					return false;
				}
				if (matchInfo2.getEndTime() == matchInfo.getEndTime()) {
					request.setAttribute("tip", "与现有的闭赛日期重复.");
					return false;
				}
				// 2、是否在其它的赛程中
				if ((matchInfo.getStartTime() > matchInfo2.getStartTime() && matchInfo
						.getStartTime() < matchInfo2.getEndTime())
						|| (matchInfo.getEndTime() > matchInfo2.getStartTime() && matchInfo
								.getEndTime() < matchInfo2.getEndTime())) {
					request.setAttribute("tip", "开赛日期在"
							+ matchInfo2.getTitleWml() + "的赛程中.");
					return false;
				}
				// 3、整个赛程中是否包含其它赛程
				if (matchInfo.getStartTime() < matchInfo2.getStartTime()
						&& matchInfo.getEndTime() > matchInfo2.getEndTime()) {
					request.setAttribute("tip", "您所选择的日期中包含"
							+ matchInfo2.getTitleWml() + "比赛.");
					return false;
				}
			}
		}
		matchInfo.setFalg(0);
		service.addInfo(matchInfo);
		return true;
	}
	
	/**
	 * 把bean中的省市信息转成字符串。flag=0，提取城市信息；flag=1，提取省份信息。
	 * @param matchArea
	 * @param flag
	 * @return
	 */
	public String getCityString(MatchArea matchArea,int flag){
		if (matchArea == null){
			return "";
		}
		String tmp = "";
		List list = null;
		StringBuilder sb = new StringBuilder();
		// 市
		if (flag == 0){
			if ("".equals(matchArea.getCitys())){
				return "";
			} else {
				list = SqlUtil.getObjectList("select city from credit_city where id in(" + matchArea.getCitys() + ")", 5);
			}
		} else {
			// 省
			if ("".equals(matchArea.getProvinces())){
				return "";
			} else {
				list = SqlUtil.getObjectList("select province from credit_province where id in(" + matchArea.getProvinces() + ")", 5);
			}
		}
		if (list != null && list.size() > 0){
			for (int i = 0 ; i < list.size() ; i++){
				tmp = list.get(i).toString();
				if (tmp != null && !"".equals(tmp)){
					sb.append(tmp);
					sb.append(",");
				}
			}
		} else {
			return "";
		}
		if (sb.length() > 0){
			return sb.substring(0, sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 取得某一用户的最新的N个论坛主题
	 * @param matchUser
	 * @param count
	 * @return
	 */
	public List getForumTopicList(MatchUser matchUser,int count){
		if (matchUser == null){
			return null;
		}
		if (count < 3)
			count = 3;
		List list = new ArrayList();
		list = SqlUtil.getIntList("select id from jc_forum_content where user_id=" + matchUser.getUserId() + " and del_mark=0 order by id desc limit 3",2);
		return list;
	}
	
}