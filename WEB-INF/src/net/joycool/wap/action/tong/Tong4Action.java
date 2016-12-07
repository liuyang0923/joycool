package net.joycool.wap.action.tong;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.stock2.StockService;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.tong.TongApplyBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.tong.TongCityRecordBean;
import net.joycool.wap.bean.tong.TongDestroyHistoryBean;
import net.joycool.wap.bean.tong.TongFundBean;
import net.joycool.wap.bean.tong.TongHockshopBean;
import net.joycool.wap.bean.tong.TongUserBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Arith;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author bomb
 * @explain
 */
public class Tong4Action extends CustomAction{
	
	public static class Tong2User{
		static int CHOICE_COUNT = 4;
		static byte MIN_LEVEL = -4;
		static byte MAX_LEVEL = 14;
		
		public byte level;		// 能力值
		public byte effect;
		public byte choice;		// 本次特效需要选择哪个选项
		public void reset() {
			int rand = RandomUtil.nextInt(28);
			effect = (byte) (rand / CHOICE_COUNT);
			choice = (byte) (rand % CHOICE_COUNT);
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
			case 6: {		// 下次效果一倍
				r = 1;
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
		Tong2User u = (Tong2User)session.getAttribute("t4user");
		if(u == null) {
			u = new Tong2User();
			u.reset();
			session.setAttribute("t4user", u);
		}
		return u;
	}
	
	static ICacheMap tongCityRecordCache = CacheManage.tongCityRecord;

	public static int PAGE_COUNT = 8;

	public static int TONG_ENDURE = 2000;

	final static int NUMBER_PER_PAGE = 10;

	static int SYSTEM_MAX_GAM_POINT = 2000000000;

	static int HOCKSHOP_NUMBER_PER_PAGE = 10;

	static int TONG_CITY_REINFORCE = 10000;

	static int TONG_CITY_DESTORY = 10000;

	static int RANK_POINT = 1;

	// macq_帮会解除同盟扣除100亿帮会基金_end

	UserBean loginUser;
	
	static IDummyService dummyService = ServiceFactory.createDummyService();
	static ITongService tongService = ServiceFactory.createTongService();

	public Tong4Action(HttpServletRequest request) {
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
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：城墙加固记录
	 * @datetime:2007-7-5 16:26:12
	 * @param request
	 * @return void
	 */
	public void tongCityRecoveryRecord(HttpServletRequest request) {
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
					" tong_id=" + tong.getId() + " and mark=1 order by "
							+ orderBy + " desc");
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
			String prefixUrl = "tongCityRecoveryRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCityRecoveryList = getTongService()
					.getTongCityRecordList(
							"tong_id=" + tong.getId() + " and mark=1 order by "
									+ orderBy + " desc limit " + start + ","
									+ end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecoveryList", tongCityRecoveryList);
		} else {// 判断按帮会被破坏次数排序
			orderBy = "tongCityTongId";
			Vector tongCityRecoveryByIdList = this.getTongCiytRecordList(1);
			int totalCount = tongCityRecoveryByIdList.size();
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
			String prefixUrl = "tongCityRecoveryRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityRecoveryByIdList1 = tongCityRecoveryByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecoveryByIdList",
					tongCityRecoveryByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 城墙破坏记录
	 * @datetime:2006-12-25 下午05:27:54
	 * @param request
	 */
	public void tongCityRecord(HttpServletRequest request) {
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
					" tong_id=" + tong.getId() + " and mark=0 order by "
							+ orderBy + " desc");
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
			String prefixUrl = "tongCityRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			Vector tongCiytRecordList = getTongService().getTongCityRecordList(
					"tong_id=" + tong.getId() + " and mark=0 order by "
							+ orderBy + " desc limit " + start + "," + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCiytRecordList", tongCiytRecordList);
		} else {// 判断按帮会被破坏次数排序
			orderBy = "tongCityTongId";
			Vector tongCityRecordByIdList = this.getTongCiytRecordList(0);
			int totalCount = tongCityRecordByIdList.size();
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
			String prefixUrl = "tongCityRecord.jsp?tongId=" + tongId
					+ "&amp;orderBy=" + orderBy;
			int start = pageIndex * NUMBER_PER_PAGE;// 取得要显示的消息列表
			int end = NUMBER_PER_PAGE;
			int startIndex = Math.min(start, totalCount);
			int endIndex = Math.min(start + end, totalCount);
			List tongCityRecordByIdList1 = tongCityRecordByIdList.subList(
					startIndex, endIndex);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("tongCityRecordByIdList",
					tongCityRecordByIdList1);
		}
		request.setAttribute("orderBy", orderBy);
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("result", result);
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

	/**
	 * 
	 * @author macq
	 * @explain : 用户对城池的操作 加固或者破坏
	 * @datetime:2006-12-25 下午05:42:06
	 * @param request
	 */
	public void tongCityResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		boolean flag = false;
		int tongId = StringUtil.toInt(request.getParameter("tongId"));// 判断参数
		if (tongId <= 0) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongBean tong = TongCacheUtil.getTong(tongId);// 获取帮记录
		if (tong == null) {
			result = "failure";
			tip = "查询无此帮会!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		TongUserBean tongUser = this.getTongUser(tong.getId(), loginUser
				.getId());// 判断帮会会员信息
		if (tongUser != null) {
			flag = true;
		}
		String mark = request.getParameter("mark");
		if (mark == null) {
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		boolean isCooldown;
		synchronized(loginUser.getLock()) {
			isCooldown = isCooldown("tong", 2000);
		}
		float cityRate = 0;
		if(tong.getHighEndure() > 12500000)
			cityRate = (float)tong.getNowEndure() / 12500000;// 获得当前城墙耐久度比率
		else if(tong.getHighEndure() > 0)
			cityRate = (float)tong.getNowEndure() / tong.getHighEndure();
		int value = 0;
		UserHonorBean honor = loginUser.getUserHonor();
		float tongRate = honor.getTongRate();
		int productIdA = StringUtil.toInt(request.getParameter("productIdA"));
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());// 用户状态信息
		if (mark.equals("r")) {// 加固城墙
			if (productIdA > 0 ) {
				if(productIdA == 26){
					int userBagId = UserBagCacheUtil.getUserBagById(productIdA,loginUser.getId());
					// 16 为保护膜
					if (userBagId <= 0) {
						tip = "您没有此特殊道具!";
						result = "timeError";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						request.setAttribute("tong", tong);
						return;
					}
					//更新物品使用次数
					UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),
							userBagId);
					TongCacheUtil.updateTong("tong_recovery_time=now()","id="+tong.getId(),tong.getId());
					tong = TongCacheUtil.getTong(tongId);// 获取帮记录
				}else{
						tip = "参数错误!";
						result = "timeError";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						request.setAttribute("tong", tong);
						return;
				}	
			}
			int productId = StringUtil.toInt(request.getParameter("productId"));
			if (productId > 0 && productId == 16) {
				int userBagId = UserBagCacheUtil.getUserBagById(productId,
						loginUser.getId());
				// 16 为保护膜
				if (userBagId <= 0) {
					tip = "您没有此特殊道具!";
					result = "timeError";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				
				value = 500;
				if (tong.getHighEndure() < tong.getNowEndure() + value) {// 判断是否加固到极限,如果满足条件提升城墙的最高耐久度极限
					// TongCacheUtil.updateTongEndure("high_endure=high_endure+"
//					TongCacheUtil.updateTongEndure("high_endure="
//							+ (tong.getNowEndure() + value)
//							+ ",now_endure=now_endure+" + value,
//							"id=" + tongId, tongId, value, (tong.getNowEndure()
//									+ value - tong.getHighEndure()));
					tip = "城墙完好,还不需要修复.";
					result = "timeError";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				} else {
					TongCacheUtil.updateTongEndure("now_endure=now_endure+"
							+ value,// 加固城墙当前耐久度
							"id=" + tongId, tongId, value, 0);
				}
//				 更新物品使用次数
				UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), userBagId);
				if (flag) {// 增加用户的贡献度
					TongCacheUtil.updateTongUserDonation(
							"donation=donation+10", "user_id="
									+ loginUser.getId(), tong.getId(),
							loginUser.getId(), 500);
					tip = "您的加固已经成功！耐久度加" + value + "！您的帮会贡献度加10！";
				} else {
					tip = "您的加固已经成功！耐久度加" + value + "！";
				}
				TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong
						.getId(), loginUser.getId(), 1);
				if (tongCityRecord == null) {// 添加加固城墙记录
					tongCityRecord = new TongCityRecordBean();
					tongCityRecord.setTongId(tong.getId());
					tongCityRecord.setUserId(loginUser.getId());
					tongCityRecord.setCount(1);
					tongCityRecord.setMark(1);
					getTongService().addTongCityRecord(tongCityRecord);
					tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_1");// 更新缓存
				} else {// 更新加固城墙记录
					getTongService().updateTongCityRecord(
							"count=count+1,create_datetime=now()",
							"id=" + tongCityRecord.getId());
				}
			} else {
				if (userStatus.getGamePoint() < TONG_CITY_REINFORCE) {
					result = "failure";
					tip = "您没有足够的乐币加固城墙.";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				if (tong.getNowEndure() >= 20000000) {	// 最大两千万的城墙
					result = "failure";
					tip = "城墙已经达到最大值,无法继续加固.";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				if (cityRate < 0.1) {// 判断城墙耐久度比率
//					value = RandomUtil.nextInt(4) + 6;
					value = RandomUtil.nextInt(3) + 4;
				} else if (cityRate < 0.3) {
					value = RandomUtil.nextInt(3) + 4;
				} else if (cityRate < 0.5) {
					value = RandomUtil.nextInt(3) + 3;
				} else {
					value = RandomUtil.nextInt(3) + 2;
				}
				long tongRecoveryTime=tong.getTongRecoveryTime().getTime()+1000*60*30;
				//if(tongRecoveryTime-System.currentTimeMillis()>0 && tongRecoveryTime-System.currentTimeMillis()<=1000*60*30){
				if(tongRecoveryTime-System.currentTimeMillis()>=0){
					value = (int) (value * tongRate*1.5);
				}else{
					value = (int) (value * tongRate);
				}
				int add = 0;
				if(isCooldown) {
					Tong2User t2u = getUser2();
					add = 1 * t2u.getRate(getParameterInt("c"));
					t2u.reset();
					Integer extra = (Integer) request.getAttribute("extra-shop");	// 来自验证码的加成
					if(extra != null)
						add *= extra.intValue();
					value *= add;
					if(add > 0) {
						if (tong.getHighEndure() < tong.getNowEndure() + value) {// 判断是否加固到极限,如果满足条件提升城墙的最高耐久度极限
							TongCacheUtil.updateTongEndure("high_endure=high_endure+"
									+ value + ",now_endure=now_endure+" + value, "id="
									+ tongId, tongId, value, value);
						} else {
							TongCacheUtil.updateTongEndure("now_endure=now_endure+"
									+ value,// 加固城墙当前耐久度
									"id=" + tongId, tongId, value, 0);
						}
					}
				} else {
					value = 0;
				}
				if (flag) {// 增加用户的贡献度
					TongCacheUtil.updateTongUserDonation("donation=donation+"+add,
								"user_id=" + loginUser.getId(), tong.getId(),
								loginUser.getId(), add);
					tip = "您的加固已经成功！耐久度加" + value + "！您的帮会贡献度加"+add+"！您还有乐币"
							+ (userStatus.getGamePoint() - TONG_CITY_REINFORCE);
				} else {
					tip = "您的加固已经成功！耐久度加" + value + "！您还有乐币"
					+ (userStatus.getGamePoint() - TONG_CITY_REINFORCE);
				}
				if(add > 0) {
					UserInfoUtil.updateUserCash(loginUser.getId(), -TONG_CITY_REINFORCE, UserCashAction.OTHERS, null);// 更新用户乐币
					TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong
							.getId(), loginUser.getId(), 1);
					if (tongCityRecord == null) {// 添加加固城墙记录
						tongCityRecord = new TongCityRecordBean();
						tongCityRecord.setTongId(tong.getId());
						tongCityRecord.setUserId(loginUser.getId());
						tongCityRecord.setCount(add);
						tongCityRecord.setMark(1);
						getTongService().addTongCityRecord(tongCityRecord);
						tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_1");// 更新缓存
					} else {// 更新加固城墙记录
						getTongService().updateTongCityRecord(
								"count=count+" + add + ",create_datetime=now()",
								"id=" + tongCityRecord.getId());
					}
				}
			}
		} else if (mark.equals("d")) {// 破坏城墙
			Calendar cal = Calendar.getInstance();
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);
			if (currentHour < 19 || currentHour > 24) {
				result = "timeError";
				tip = "城墙只在晚19:00-24:00之间允许破坏!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			if (productIdA > 0 ) {
				if(productIdA == 25){
					int userBagId = UserBagCacheUtil.getUserBagById(productIdA,loginUser.getId());
					// 16 为保护膜
					if (userBagId <= 0) {
						tip = "您没有此特殊道具!";
						result = "timeError";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						request.setAttribute("tong", tong);
						return;
					}
					//更新物品使用次数
					UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),
							userBagId);
					TongCacheUtil.updateTong("tong_assault_time=now()","id="+tong.getId(),tong.getId());
					tong = TongCacheUtil.getTong(tongId);// 获取帮记录
				}else{
						tip = "参数错误!";
						result = "timeError";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						request.setAttribute("tong", tong);
						return;
				}	
			}
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us.getTong() <= 0) {
				result = "timeError";
				tip = "你不隶属于任何帮会成员，所以不能攻打该帮城墙";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			TongBean userTong = TongCacheUtil.getTong(us.getTong());
			if (userTong.getNowEndure() < TONG_ENDURE) {
				result = "timeError";
				tip = "你的帮会城墙告急，先回去守城吧！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			Date date = DateUtil.parseDate(userTong.getCreateDatetime(),
					DateUtil.normalDateFormat);
			long createTime = date.getTime();
			long currentTime = System.currentTimeMillis();
			if (currentTime - createTime < 1000 * 60 * 60 * 24 * 7) {
				result = "timeError";
				tip = "对不起，您的帮会必须成立7天以上，才能攻打其他帮会!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			// macq_2008-8-16_增加攻打城墙的判断
			if (tongIsDestroyed(userTong)) {
				TongCacheUtil.updateTong(
						"destroy_datetime='2000-01-01 00:00:00'", "id="
								+ userTong.getId(), userTong.getId());
			}
			date = DateUtil.parseDate(tong.getCreateDatetime(),
					DateUtil.normalDateFormat);
			createTime = date.getTime();
			currentTime = System.currentTimeMillis();
			if (currentTime - createTime < 1000 * 60 * 60 * 24 * 7) {
				result = "timeError";
				tip = "对不起，帮会必须成立7天以上，才能攻打其他帮会!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			if (tong.getNowEndure() == 0) {
				result = "timeError";
				tip = "城池已倒塌,破坏成功!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			// 帮会结盟不能攻击
			if (TongCacheUtil.isFriendTong(tong.getId(), userStatus.getTong())) {
				result = "timeError";
				tip = "帮会盟友不能相互攻击!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			if (tongIsDestroyed(tong)) {
				result = "timeError";
				tip = "英雄！本帮已经沦陷，自沦陷起3日内不能继续攻打。";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("tong", tong);
				return;
			}
			int productId = StringUtil.toInt(request.getParameter("productId"));
			// 判断是否有特殊道具
			if (productId == 15 || productId == 76) {
				// 15 为轰天炮
				int userBagId = UserBagCacheUtil.getUserBagById(productId,
						loginUser.getId());
				if (userBagId <= 0) {
					tip = "您没有此特殊道具!";
					result = "timeError";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				// 更新物品使用次数
				UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),
						userBagId);
				DummyProductBean dummyProduct = dummyService.getDummyProducts(productId);
				value = dummyProduct.getValue();
				if(productId == 76)
					value += RandomUtil.nextInt(50);
				Integer extra = (Integer) request.getAttribute("extra-shop");	// 来自验证码的加成
				if(extra != null)
					value *= extra.intValue();
				int cityValue = 0;
				if (tong.getNowEndure() - value <= 0) {// 破坏城墙
					cityValue = 0;
				} else {
					cityValue = value;
				}
				if (cityValue == 0) {
					destroyTong(tongId);
				} else {
					TongCacheUtil.updateTongEndure("now_endure=now_endure-"
							+ cityValue, "id=" + tongId, tongId, -cityValue, 0);// 更新城墙耐久度
				}
				if (flag) {// 如果是帮会会员,减去用户的贡献度
					TongCacheUtil.updateTongUserDonation(
							"donation=donation-10", "user_id="
									+ loginUser.getId(), tong.getId(),
							loginUser.getId(), -10);
					tip = "您的破坏已经成功！耐久度减" + value + "！您的帮会贡献度减10!";
				} else {
					tip = "您的破坏已经成功！耐久度减" + value + "！您还有乐币";
				}
				if(productId == 15) {	// 如果使用的石头类道具，不加记录
					TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong
							.getId(), loginUser.getId(), 0);
					if (tongCityRecord == null) {// 添加破坏城墙记录
						tongCityRecord = new TongCityRecordBean();
						tongCityRecord.setTongId(tong.getId());
						tongCityRecord.setUserId(loginUser.getId());
						tongCityRecord.setCount(1);
						tongCityRecord.setMark(0);
						getTongService().addTongCityRecord(tongCityRecord);
						tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_0");// 更新缓存
					} else {// 更新破坏城墙记录
						getTongService().updateTongCityRecord(
								"count=count+1,create_datetime=now()",
								"id=" + tongCityRecord.getId());
					}
				}
			} else {
				if (userStatus.getGamePoint() < TONG_CITY_DESTORY) {
					result = "failure";
					tip = "您没有足够的乐币破坏城墙.";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("tong", tong);
					return;
				}
				if (cityRate < 0.5) {// 判断城墙耐久度比率
					value = 4;
				} else{
					value = RandomUtil.nextInt(2) + 5;
				}

				long tongAssaultTime=tong.getTongAssaultTime().getTime()+1000*60*30;
				//if(tongAssaultTime-System.currentTimeMillis()>0 && tongAssaultTime-System.currentTimeMillis()<=1000*60*30){
				if(tongAssaultTime-System.currentTimeMillis()>=0){
					value = (int) (value * tongRate*1.5);
				}else{
					value = (int) (value * tongRate);
				}

				int add = 0;
				if(isCooldown) {
					Tong2User t2u = getUser2();
					add = 1 * t2u.getRate(getParameterInt("c"));
					t2u.reset();
					value *= add;
					if(add > 0) {
						if (tong.getNowEndure() - value <= 0) {
							destroyTong(tongId);
						} else {
							TongCacheUtil.updateTongEndure("now_endure=now_endure-"
									+ value, "id=" + tongId, tongId, -value, 0);// 更新城墙耐久度
						}
					}

					UserInfoUtil.updateUserCash(loginUser.getId(), -TONG_CITY_DESTORY, UserCashAction.OTHERS, null);// 更新用户乐币
				} else {
					value = 0;
				}
//				if (flag) {// 如果是帮会会员,减去用户的贡献度
//					if(isCooldown)
//						TongCacheUtil.updateTongUserDonation(
//							"donation=donation-10", "user_id="
//									+ loginUser.getId(), tong.getId(),
//							loginUser.getId(), -10);
//					tip = "您的破坏已经成功！耐久度减" + value + "！您的帮会贡献度减10！您还有乐币"
//							+ (userStatus.getGamePoint() - TONG_CITY_DESTORY);
//				} else {
					tip = "您的破坏已经成功！耐久度减" + value + "！您还有乐币"
							+ (userStatus.getGamePoint() - TONG_CITY_DESTORY);
//				}
				if(add>0) {
					TongCityRecordBean tongCityRecord = getTongCiytRecordList(tong
							.getId(), loginUser.getId(), 0);
					if (tongCityRecord == null) {// 添加破坏城墙记录
						tongCityRecord = new TongCityRecordBean();
						tongCityRecord.setTongId(tong.getId());
						tongCityRecord.setUserId(loginUser.getId());
						tongCityRecord.setCount(add);
						tongCityRecord.setMark(0);
						getTongService().addTongCityRecord(tongCityRecord);
						tongCityRecordCache.srm(tong.getId() + "_" + loginUser.getId() + "_0");// 更新缓存
					} else {// 更新破坏城墙记录
						getTongService().updateTongCityRecord(
								"count=count+"+add+",create_datetime=now()",
								"id=" + tongCityRecord.getId());
					}
				}
			}
		} else {// 非法参数
			result = "failure";
			tip = "参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// macq_2007-1-16_增加用户经验值_start
		RankAction.addPoint(loginUser, RANK_POINT);
		// macq_2007-1-16_增加用户经验值_end
		request.setAttribute("tong", tong);
		result = "success";
		request.setAttribute("tip", tip);
		request.setAttribute("result", result);
		return;
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

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮派排名
	 * @datetime:2006-12-25 下午04:57:03
	 * @param tongId
	 * @return
	 */
	public int getTongCompositor(int tongUserCount) {
		String sql = "SELECT count(id) FROM jc_tong where user_count>"
				+ tongUserCount;
		// 从缓存中取
		Integer count = (Integer) OsCacheUtil.get(sql,
				OsCacheUtil.TONG_COMPOSITOR_CACHE_GROUP,
				OsCacheUtil.TONG_COMPOSITOR_FLUSH_PERIOD);
		if (count == null) {
			int tongCompositor = SqlUtil.getIntResult(sql,
					Constants.DBShortName);
			if (tongCompositor == -1) {
				tongCompositor = 0;
			}
			count = new Integer(tongCompositor);
			// 放到缓存中
			OsCacheUtil
					.put(sql, count, OsCacheUtil.TONG_COMPOSITOR_CACHE_GROUP);
		}
		return count.intValue() + 1;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取帮会城墙图片显示
	 * @datetime:2007-1-6 下午03:12:42
	 * @param request
	 */
	public int tongCityImage(TongBean tong) {
		double ciryRate = Arith.div(tong.getNowEndure(), tong.getHighEndure(),
				2);// 获得当前城墙耐久度比率
		int value = 0;
		if (ciryRate < 0.1) {// 判断城墙耐久度比率
			value = 5;
		} else if (ciryRate < 0.3) {
			value = 4;
		} else if (ciryRate < 0.5) {
			value = 3;
		} else if (ciryRate < 0.9) {
			value = 2;
		} else {
			value = 1;
		}
		return value;
	}

	// 荒城
	public TongBean getTong(int tongId, int mark) {
		TongBean tong = getTongService().getTong(
				"id=" + tongId + " and mark=" + mark);
		return tong;
	}


	/*
	 * 数字分页
	 * 
	 * @param totalPageCount @param currentPageIndex @param prefixUrl @param
	 * addAnd @param separator @param pageIndex @param response @return
	 */
	public String shuzifenye(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			String pageIndex, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();

		if (addAnd) {
			prefixUrl += "&amp;" + pageIndex + "=";
		} else {
			prefixUrl += "?" + pageIndex + "=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

}
