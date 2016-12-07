package net.joycool.wap.action.tong;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongCityRecordBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author bomb
 * @explain
 */
public class Tong2Action extends CustomAction{
	
	public static class Tong2User{
		static int CHOICE_COUNT = 4;
		static byte MIN_LEVEL = -4;
		static byte MAX_LEVEL = 14;
		
		public byte level;		// 能力值
		public byte effect;
		public byte choice;		// 本次特效需要选择哪个选项
		public boolean same;	// 提示与上次相同即可
		public void reset() {
			int rand = RandomUtil.nextInt(24);
			byte oldChoice = choice;
			effect = (byte) (rand / CHOICE_COUNT);
			choice = (byte) (rand % CHOICE_COUNT);
			if(effect == 1 || effect == 5) {	// 加倍或者三倍的时候总是（与前一次相同
				same = true;
				choice = oldChoice;
			} else
				same = false;
			if(level < 5)
				effect = 0;
		}
		public void incLevel(int add) {
			level += add;
			if(level > 14)
				level = 14;
		}
		public void decLevel(int add) {
			level -= add;
			if(level < -4)
				level = -4;
		}
		public int getRate(int uc) {		// 本次加固的比率
			if(uc != choice)			// 选择正确
				effect = -1;
			int r = 0;
			if(level > 4)
				r = level / 5;
			switch(effect) {
			case -1: {
				decLevel(2);
			} break;
			case 0: {		// 增加能力
				incLevel(1);
			} break;
			case 1: {		// 下次效果加倍
				r += r;
			} break;
			case 2: {		// 增加能力2
				incLevel(2);
			} break;
			case 3: {		// 下次效果+1
				r += 1;
			} break;
			case 4: {		// 下次效果=3（如果能力>0）
				r = 3;
			} break;
			case 5: {		// 下次效果三倍
				r *= 3;
			} break;
			}
			if(level <= 0)
				r = 0;
			else if(r == 0)
				r = 1;
			return r;
		}
	}
	
	public Tong2User getUser2() {
		Tong2User u = (Tong2User)session.getAttribute("t2user");
		if(u == null) {
			u = new Tong2User();
			u.reset();
			u.same = false;	// 第一次不会出现相同
			session.setAttribute("t2user", u);
		}
		return u;
	}
	
	static ICacheMap tongCityRecordCache = CacheManage.tongCityRecord;
	
	public static int TONG_SHOP_AGGRESSION = 1000;

	public static int TONG_SHOP_RECOVERY = 600;
	
	public static int TONG_SHOP_ASSAULT_TIME = 9000;
	
	public static int TONG_SHOP_RECOVERY_TIME = 6000;

	public static int TONG_SHOP_DEVELOP_GAME_POINT = 1000;

	public static int MIN_RANK_FOR_TONG = 35;

	public static int MIN_MONEY_FOR_TONG = 1000000;

	public static int MIN_SOCIAL_FOR_TONG = 1000;

	public static int PAGE_COUNT = 8;

	public static int TONG_ENDURE = 2000;


	final static int NUMBER_PER_PAGE = 10;

	static int SYSTEM_MAX_GAM_POINT = 2000000000;

	static int HOCKSHOP_NUMBER_PER_PAGE = 10;

	static int RANK_POINT = 1;

	UserBean loginUser;

	static int GROUP_SEND_MESSAGE_TIME = 1000 * 60 * 60;
	static IDummyService dummyService = ServiceFactory.createDummyService();
	static ITongService tongService = ServiceFactory.createTongService();
	
	public Tong2Action(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
	}

	public static ITongService getTongService() {
		return tongService;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会城墙页面
	 * @datetime:2006-12-25 下午05:27:54
	 * @param request
	 */
	public void tongCity(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：商店开发度记录
	 * @datetime:2007-7-6 11:44:06
	 * @param request
	 * @return void
	 */
	public void tongCityShopRecord(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮会记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String orderBy = request.getParameter("orderBy");// 判断排序参数
		if (orderBy == null) {
			orderBy = "count";
		}
		if (orderBy.equals("count") || orderBy.equals("create_datetime")) {// 判断按次数跟时间排序
			int totalCount = getTongService().getTongCityRecordCount(
					" tong_id=" + tong.getId() + " and mark=3 order by "
							+ orderBy + " desc ");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCityShopList = getTongService().getTongCityRecordList(
					"tong_id=" + tong.getId() + " and mark=3 order by "
							+ orderBy + " desc limit " + start + "," + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityShopList", tongCityShopList);
		} else {// 判断按帮会排序
			orderBy = "tongCityTongId";
			Vector tongCityShopByIdList = this.getTongCiytRecordList(3);
			int totalCount = tongCityShopByIdList.size();
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / NUMBER_PER_PAGE;
			if (totalCount % NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "tongCityShopRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityShopByIdList1 = tongCityShopByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityShopByIdList", tongCityShopByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("tongCityCheck", "true");
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取每个帮会的破坏次数并排名
	 * @datetime:2006-12-25 下午12:16:12
	 * @param tongId
	 * @return
	 */
	public Vector getTongCiytRecordList(int mark) {
		String key = "tongCityRecord_" + mark;
		synchronized(tongCityRecordCache) {
			Vector tongCiytRecordList = (Vector) tongCityRecordCache.get(key);
			if (tongCiytRecordList == null) {
				tongCiytRecordList = new Vector();
				TongCityRecordBean tongCityRecord = null;

				DbOperation dbOp = new DbOperation(true);

				String query = "select sum(count) tong_count,tong_id from jc_tong_city_record  where mark="
						+ mark + " group by tong_id order by tong_count desc";

				ResultSet rs = dbOp.executeQuery(query);
				try {
					// 结果不为空
					while (rs.next()) {
						tongCityRecord = new TongCityRecordBean();
						tongCityRecord.setCount(rs.getInt("tong_count"));
						tongCityRecord.setTongId(rs.getInt("tong_id"));
						tongCiytRecordList.add(tongCityRecord);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dbOp.release();
				tongCityRecordCache.put(key, tongCiytRecordList);
			}
			return tongCiytRecordList;
		}
	}
	
	// 破坏帮会城墙
	public static void destroyTong(int tongId) {
		synchronized (LockUtil.tongLock.getLock(tongId)) {
			TongCacheUtil.updateTong(
					"now_endure=0,fund=fund/2,high_endure=high_endure/2,shop=shop/2,destroy_datetime=now(),honor_drop=honor_drop+honor,honor=0",// 更新城墙沦陷时间
					"id=" + tongId + " and now_endure>0", tongId);	// 避免一次灭多次，扣太多……
			OsCacheUtil.flushGroup(OsCacheUtil.TONG_DESTROY_LIST_GROUP, tongId + "");
			TongDestroyHistoryBean bean = new TongDestroyHistoryBean();
			bean.setTongId(tongId);
			tongService.addTongDestroyHistory(bean);
			TongHockshopBean tongHockshop = TongCacheUtil.getTongHockshop(tongId);
			tongHockshop.setDevelop(tongHockshop.getDevelop() / 2);
			tongService.updateTongHockshop("develop="+tongHockshop.getDevelop(), "tong_id=" + tongId);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：城池沦陷后3天内不得攻打
	 * @datetime:2007-7-31 9:47:41
	 * @param tongId
	 * @return
	 * @return boolean
	 */
	public static boolean tongIsDestroyed(TongBean bean) {
		if (bean == null) {
			return true;
		}
		long createTime = bean.getDestroyDatetime();
		long currentTime = System.currentTimeMillis();
		if (currentTime - createTime < 1000 * 60 * 60 * 24 * 3) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain：检查用户是否拥有特殊道具
	 * @datetime:2007-6-28 5:43:32
	 * @param weaponId
	 * @return
	 * @return String
	 */
	public String checkUserBag(int userBagId, int productId) {
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		if (userBag == null) {
			return "您没有此道具！";
		} else if (userBag.getUserId() != loginUser.getId()) {
			return "您没有此道具！";
		} else if (userBag.getProductId() != productId) {
			return "您没有特殊股道具！";
		}
		return "";
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取用户破换城墙记录
	 * @datetime:2007-1-9 下午06:39:22
	 * @param request
	 */
	public TongCityRecordBean getTongCiytRecordList(int tongId, int userId,
			int mark) {
		String key = tongId + "_" + userId + "_" + mark;
		synchronized(tongCityRecordCache) {
			TongCityRecordBean tongCityRecord = (TongCityRecordBean) tongCityRecordCache
					.get(key);
			if (tongCityRecord == null) {
				tongCityRecord = getTongService().getTongCityRecord(
						"tong_id=" + tongId + " and user_id=" + userId
								+ " and mark=" + mark);
				if (tongCityRecord != null) {
					tongCityRecordCache.put(key, tongCityRecord);
				}
			}
			return tongCityRecord;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 获取最新5条攻打城池的记录
	 * @datetime:2007-3-30 15:01:04
	 * @return
	 * @return Vector
	 */
	public Vector getTongCiytRecordListNew() {
		// String key = "1=1 order by create_datetime limit 0,5";
		// Vector tongCityRecordList = (Vector) OsCacheUtil
		// .get(key, OsCacheUtil.TONG_CITY_RECORD_CACHE_GROUP,
		// OsCacheUtil.TONG_CITY_RECORD_FLUSH_PERIOD);
		// if (tongCityRecordList == null) {
		// tongCityRecordList = getTongService().getTongCityRecordList(key);
		// if (tongCityRecordList == null) {
		// tongCityRecordList=new Vector();
		// }
		// OsCacheUtil.put(key, tongCityRecordList,
		// OsCacheUtil.TONG_CITY_RECORD_CACHE_GROUP);
		// }
		// 获取最新5条攻打城池的记录
		String sql = " mark=0 order by create_datetime desc limit 0,5";
		Vector tongCityRecordList = getTongService().getTongCityRecordList(sql);
		return tongCityRecordList;
	}

	/**
	 * 
	 * @author macq
	 * @explain：帮会道具商店
	 * @datetime:2007-7-3 1:57:09
	 * @param request
	 * @return void
	 */
	public void shop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 帮会开发显示页面
	 * @datetime:2006-12-28 下午05:56:28
	 * @param develop
	 * @return
	 */
	public void shopEmpolder(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));
		if (tongId <= 0) {
			doTip();
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);
		if (tong == null) {
			doTip("查询无此帮会!");
			return;
		}
		if (tong.getNowEndure() < TONG_ENDURE) {
			result = "priceError";
			tip = "只有当帮会城墙大于" + TONG_ENDURE + "点，才能进行商店开发!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		if (userStatus.getGamePoint() < TONG_SHOP_DEVELOP_GAME_POINT) {
			result = "priceError";
			tip = "您没有足够的乐币进行开发!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("tong", tong);
			return;
		}
		int gamePoint = userStatus.getGamePoint();
		boolean isCooldown;
		int add = 0;
		synchronized(loginUser.getLock()) {
			isCooldown = isCooldown("tong", 2000);
			if(isCooldown) {
				Tong2User t2u = getUser2();
				add = 1 * t2u.getRate(getParameterInt("c"));
				Integer extra = (Integer) request.getAttribute("extra-shop");	// 来自验证码的加成
				if(extra != null)
					add *= extra.intValue();
				t2u.reset();
			}
			if(add > 0) {		// 没有冷却，无效！
				TongCacheUtil.updateTongShop("shop=shop+" + add, "id=" + tong.getId(), tong
					.getId(), add);
				UserInfoUtil.updateUserCash(loginUser.getId(), -TONG_SHOP_DEVELOP_GAME_POINT, UserCashAction.OTHERS, null);// 更新用户乐币
			}
			gamePoint -= TONG_SHOP_DEVELOP_GAME_POINT;// 用户现有乐币
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断获取是否为帮会会员
		if (tongUser != null) {
			if(add > 0)
				TongCacheUtil.updateTongUserDonation("donation=donation+" + add,
					"user_id=" + loginUser.getId(), tong.getId(), loginUser
							.getId(), add);// 增加帮会会员贡献度
			// tongUser.setDonation(tongUser.getDonation() + 3);
			tip = "掌柜的：开发度增加" + add + "！非常感谢您的支持！目前商店开发度" + tong.getShop()
					+ "！您的帮会贡献度加了！花费乐币" + TONG_SHOP_DEVELOP_GAME_POINT
					+ ",您还有乐币" + gamePoint + "。（1秒钟跳转）";
		} else {
			tip = "掌柜的：开发度增加" + add + "！非常感谢您的支持！目前商店开发度" + tong.getShop() + "！花费乐币"
					+ TONG_SHOP_DEVELOP_GAME_POINT + ",您还有乐币" + gamePoint
					+ "。（1秒钟跳转）";
		}
		// macq_2007-1-16_增加用户经验值_start
		if(add > 0) {
			RankAction.addPoint(loginUser, RANK_POINT);
		// 增加日记记录
			TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong.getId(),
					loginUser.getId(), 3);
			if (tongCityRecord == null) {// 添加加固城墙记录
				tongCityRecord = new TongCityRecordBean();
				tongCityRecord.setTongId(tong.getId());
				tongCityRecord.setUserId(loginUser.getId());
				tongCityRecord.setCount(add);
				tongCityRecord.setMark(3);
				getTongService().addTongCityRecord(tongCityRecord);
				tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_1");// 更新缓存
			} else {// 更新加固城墙记录
				getTongService().updateTongCityRecord(
						"count=count+" + add + ",create_datetime=now()",
						"id=" + tongCityRecord.getId());
			}
		}
		// macq_2007-1-16_增加用户经验值_end
		request.setAttribute("gamePoint", gamePoint + "");
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：更新帮会商店开发度
	 * @datetime:2007-7-3 2:56:26
	 * @param shopD
	 * @return void
	 */
	public void updateTongShop(int shopDevelop, int tongId) {
		TongCacheUtil.updateTongShop("shop=shop-" + shopDevelop,
				"id=" + tongId, tongId, -shopDevelop);
	}

	/**
	 * 
	 * @author macq explain : 获取帮会成员
	 * @datetime:2006-12-25 上午11:39:05
	 * @param tongId
	 * @param userId
	 * @return
	 */
	public TongUserBean getTongUser(int tongId, int userId) {
		TongUserBean tongUser = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(userId);
		if (us != null && us.getTong() == tongId) {
			tongUser = TongCacheUtil.getTongUser(tongId, userId);
		}
		return tongUser;
	}

}
