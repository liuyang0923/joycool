package net.joycool.wap.action.top;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.bean.friend.FriendVoteBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.ICharitarianService;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.service.infc.ITopService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class TopAction {
	private UserBean loginUser;

	private UserStatusBean usb = null;

	static IUserService userService = ServiceFactory.createUserService();

	static IFriendService friendService = ServiceFactory.createFriendService();;

	static ITopService topService = ServiceFactory.createTopService();

	static IBankService bankService = ServiceFactory.createBankService();

	static ICharitarianService charitarianService = ServiceFactory.createCharitarianService();;
	
	static ReentrantLock bigQueryLock = new ReentrantLock();
	public static boolean bigQueryLocked(long timeout) {
		try {
			return bigQueryLock.tryLock(timeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			return false;
		}
	}
	
	public IUserService getUserService() {
		return userService;
	}

	public ITopService getTopService() {
		return topService;
	}

	public IBankService getBankService() {
		return bankService;
	}


	public IFriendService getFriendService() {
		return friendService;
	}

	public ICharitarianService getCharitarianService() {
		return charitarianService;
	}
	

	public TopAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}

	/**
	 * liuyi 2006-12-28 赌场二级页面修改 赌神排行榜
	 * 
	 * @param request
	 */
	public void pk(HttpServletRequest request) {
		List ret = null;
		String key = "pklist";
		String group = OsCacheUtil.TOP_GROUP;
		ret = (List) OsCacheUtil.get(key, group, 24 * 60 * 60);
		if (ret == null) {
			String sql = "select user_id from (SELECT sum(win_count) count, user_id FROM wgame_history0 group by user_id) as temp "
					+ " where count>5 order by count desc limit 0,10";
			ret = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (ret == null) {
				ret = new ArrayList();
			}
			OsCacheUtil.put(key, ret, group);
		}
		request.setAttribute("pkList", ret);
		return;
	}

	public void pkMoney(HttpServletRequest request) {
		List ret = null;
		String key = "pklist";
		String group = OsCacheUtil.TOP_GROUP;
		ret = (List) OsCacheUtil.get(key, group, 24 * 60 * 60);
		if (ret == null) {
			String sql = "select user_id from (SELECT sum(money) money, user_id FROM wgame_history0 group by user_id) as temp "
					+ " where money>1000000 order by money desc limit 0,10";
			ret = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (ret == null) {
				ret = new ArrayList();
			}
			OsCacheUtil.put(key, ret, group);
		}
		request.setAttribute("pkList", ret);
		return;
	}

	// 财富排行榜
	public void gamePointTop(HttpServletRequest request) {
		// 获取登录用户属性bean
		usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// 获取登录用户乐币
		int gamePoint = usb.getGamePoint();
		if (gamePoint < 0) {
			gamePoint = 0;
		}
		// 获取登录用户存款记录
		// macq_2006-11-27_更改银行存款从缓存中获取_start
		StoreBean storeBean = BankCacheUtil
				.getBankStoreCache(loginUser.getId());
		// StoreBean storeBean = getBankService().getStore(
		// " user_id=" + loginUser.getId());
		// macq_2006-11-27_更改银行存款从缓存中获取_end
		long store = 0;
		if (storeBean != null) {
			// 获取登录用户存款金额
			store = storeBean.getMoney();
			// 判断登录用户存款金额
			if (store < 0) {
				store = 0;
			}
		}
		// mcq_2006-10-12_去除贷款_start
		// long load = getTopService().getUserLoadTotal(
		// "select sum(money) total_money from jc_bank_load_money where
		// user_id="
		// + loginUser.getId());
		// if (load < 0) {
		// load = 0;
		// }
		// mcq_2006-10-12_去除贷款_end
		// 获取用户总资产
		long total = gamePoint + store;

		// 查询语句
		String query = "select count(id) count from jc_money_top where money_total > "
				+ total;

		// liuyi 2006-11-30 排行榜优化 start
		String key = "" + (1 + total / 100000);
		Integer rankCount = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.GAMEPOINT_TOP_GROUP,
				OsCacheUtil.GAMEPOINT_TOP_FLUSH_PERIOD);
		if (rankCount == null) {

			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 执行查询
			int count = 0;
			ResultSet rs = dbOp.executeQuery(query);
			try {
				if (rs.next()) {
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			rankCount = new Integer(count);
			OsCacheUtil.put(key, rankCount, OsCacheUtil.GAMEPOINT_TOP_GROUP);
		}

		Vector moneyList = LoadResource.getMoneyTopList();
		if(rankCount.intValue() >= 1000)
			request.setAttribute("count", "无");
		else
			request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
		request.setAttribute("moneyList", moneyList);
		// liuyi 2006-11-30 排行榜优化 end
		return;
	}

	static Integer zeroInteger = Integer.valueOf(0);
	// 等级排行榜
	public void rankTop(HttpServletRequest request) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		int rank = userStatus.getRank();
		if (rank < 0) {
			rank = 0;
		}
		// liuyi 2006-11-27 用户等级排名缓存 start
		String query = "select count(id) count from user_status where rank>"
				+ rank;
		if(bigQueryLocked(100)) {
			try {
				Integer rankCount = (Integer) OsCacheUtil.get(query,
						OsCacheUtil.RANK_TOP_GROUP, OsCacheUtil.RANK_TOP_FLUSH_PERIOD);
				if (rankCount == null) {
					int count = SqlUtil.getIntResult(query);
		
					rankCount = new Integer(count);
					OsCacheUtil.put(query, rankCount, OsCacheUtil.RANK_TOP_GROUP);
				}
				Vector rankList = LoadResource.getRankTopList();
				request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
				request.setAttribute("userList", rankList);
			} finally {
				bigQueryLock.unlock();
			}
		}
	}

	// 社交排行榜排行榜
	public void socialTop(HttpServletRequest request) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		int social = userStatus.getSocial();
		if (social < 0) {
			social = 0;
		}
		// liuyi 2006-11-27 社交指数排行榜 start
		// 查询语句
		String query = "select count(id) count from jc_user_top where mark=3 and priority>"
				+ social;

		String key = "" + (1 + social / 200);
		Integer rankCount = zeroInteger;
		if(bigQueryLocked(100)) {
			try {
				rankCount = (Integer) OsCacheUtil.get(key,
						OsCacheUtil.SOCIAL_TOP_GROUP,
						OsCacheUtil.SOCIAL_TOP_FLUSH_PERIOD);
				if (rankCount == null) {
					int count = SqlUtil.getIntResult(query);
		
					rankCount = new Integer(count);
					OsCacheUtil.put(key, rankCount, OsCacheUtil.SOCIAL_TOP_GROUP);
				}
				Vector socialList = LoadResource.getSocialTopList();
				if(rankCount.intValue() >= 1000)
					request.setAttribute("count", "无");
				else
					request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
				request.setAttribute("socialList", socialList);
			} finally {
				bigQueryLock.unlock();
			}
		}

		// liuyi 2006-11-27 社交指数排行榜 end
		return;
	}

	// 通缉排行榜
	public void blackList(HttpServletRequest request) {
		// liuyi 2006-12-28 程序优化 start
		// Vector blackList = getTopService().getUserTopList(
		// " mark=1 order by priority asc limit 0,10");
		// request.setAttribute("blackList", blackList);
		Vector ret = null;
		String key = "blacklist";
		String group = OsCacheUtil.TOP_GROUP;
		ret = (Vector) OsCacheUtil.get(key, group, 60 * 60);
		if (ret == null) {
			ret = ServiceFactory.createTopService().getUserTopList(
					" mark=1 order by priority asc limit 0,10");
			OsCacheUtil.put(key, ret, group);
		}
		request.setAttribute("blackList", ret);
		return;
		// liuyi 2006-12-28 程序优化 end
	}

	// 荣誉排行榜
	public void credit(HttpServletRequest request) {
		// liuyi 2006-12-28 程序优化 start
		// Vector creditList = getTopService().getUserTopList(
		// " mark=2 order by priority asc limit 0,10");
		// request.setAttribute("creditList", creditList);
		// return;

		Vector ret = null;
		String key = "creditlist";
		String group = OsCacheUtil.TOP_GROUP;
		ret = (Vector) OsCacheUtil.get(key, group, 60 * 60);
		if (ret == null) {
			ret = ServiceFactory.createTopService().getUserTopList(
					" mark=2 order by priority asc limit 0,10");
			OsCacheUtil.put(key, ret, group);
		}
		request.setAttribute("creditList", ret);
		return;
		// liuyi 2006-12-28 程序优化 start
	}

	// 荣誉排行榜
	public UserBean getUser(int userId) {
		// UserBean user = getUserService().getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		return user;
	}

	// 抗日英雄榜
	public void spiritTop(HttpServletRequest request) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		int spirit = userStatus.getSpirit();
		if (spirit < 0) {
			spirit = 0;
		}

		// liuyi 2006-11-30 排行榜优化 start
		// 查询语句
		String query = "select count(id) count from jc_user_top where mark=4 and priority>"
				+ spirit;
		String key = "" + (1 + spirit / 200);
		Integer rankCount = zeroInteger;
		if(bigQueryLocked(100)) {
			try {
				rankCount = (Integer) OsCacheUtil.get(key,
						OsCacheUtil.SPIRIT_TOP_GROUP,
						OsCacheUtil.SPIRIT_TOP_FLUSH_PERIOD);
				if (rankCount == null) {
					int count = SqlUtil.getIntResult(query);
		
					rankCount = new Integer(count);
					OsCacheUtil.put(key, rankCount, OsCacheUtil.SPIRIT_TOP_GROUP);
				}
				Vector spiritList = LoadResource.getSpiritTopList();
				if(rankCount.intValue() >= 1000)
					request.setAttribute("count", "无");
				else
					request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
				request.setAttribute("spiritList", spiritList);
			} finally {
				bigQueryLock.unlock();
			}
		}
		// liuyi 2006-11-30 排行榜优化 end
		return;
	}

	// 股票排行榜
	public void stockTop(HttpServletRequest request) {
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		long stock = 0;//userStatus.getStock();
		if (stock < 0) {
			stock = 0;
		}

		// liuyi 2006-11-30 排行榜优化 start
		String key = "" + (1 + stock / 200);
		Integer rankCount = (Integer) OsCacheUtil
				.get(key, OsCacheUtil.STOCK_TOP_GROUP,
						OsCacheUtil.STOCK_TOP_FLUSH_PERIOD);
		if (rankCount == null) {

			DbOperation dbOp = new DbOperation();
			dbOp.init();
			// 查询语句
			String query = "select count(id) count from user_status where stock >"
					+ stock;
			// 执行查询
			int count = 0;
			ResultSet rs = dbOp.executeQuery(query);
			try {
				if (rs.next()) {
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbOp.release();

			rankCount = new Integer(count);
			OsCacheUtil.put(key, rankCount, OsCacheUtil.STOCK_TOP_GROUP);
		}
		Vector stockList = LoadResource.getStockTopList();
		request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
		request.setAttribute("stockList", stockList);
		// liuyi 2006-11-30 排行榜优化 end
		return;
	}

	// 慈善排行榜排行榜
	public void charitatianTop(HttpServletRequest request) {
		// liuyi 2006-12-28 慈善排行修改 start
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		UserStatusBean status = UserInfoUtil.getUserStatus(loginUser.getId());
		int charitarian = status.getCharitarian();

		// liuyi 2006-11-30 排行榜优化 start
		String key = "" + (1 + charitarian / 100);
		Integer rankCount = zeroInteger;
		if(bigQueryLocked(100)) {
			try {
				rankCount = (Integer) OsCacheUtil.get(key,
						OsCacheUtil.CHARITY_TOP_GROUP,
						OsCacheUtil.CHARITY_TOP_FLUSH_PERIOD);
				if (rankCount == null) {
					// 查询语句
					String query = "select count(id) count from jc_user_top where mark=5 and priority>" + charitarian;
		
					int count = SqlUtil.getIntResult(query);
		
		
					rankCount = new Integer(count);
					OsCacheUtil.put(key, rankCount, OsCacheUtil.CHARITY_TOP_GROUP);
				}
				Vector charitarianList = LoadResource.getCharitarianTopList();
				if(rankCount.intValue() >= 1000)
					request.setAttribute("count", "无");
				else
					request.setAttribute("count", String.valueOf(rankCount.intValue() + 1));
				request.setAttribute("charitarianList", charitarianList);
			} finally {
				bigQueryLock.unlock();
			}
		}
		// liuyi 2006-11-30 排行榜优化 end
		return;
		// //liuyi 2006-12-28 慈善排行修改 end
	}

	// 男性用户照片投票排行榜
	public void friendPhotoManTop(HttpServletRequest request) {
		if(bigQueryLocked(100)) {
			try {
				Vector friendPhotoManList = LoadResource.getFriendPhotoManTopList();
				request.setAttribute("friendPhotoManList", friendPhotoManList);
			} finally {
				bigQueryLock.unlock();
			}
		}
	}

	// 女性用户照片投票排行榜
	public void friendPhotoWomanTop(HttpServletRequest request) {
		if(bigQueryLocked(100)) {
			try {
				Vector friendPhotoWomanList = LoadResource.getFriendPhotoWomanTopList();
				request.setAttribute("friendPhotoWomanList", friendPhotoWomanList);
			} finally {
				bigQueryLock.unlock();
			}
		}
	}

	// 获取个人交友信息照片投票数量
	public long getFriendVote(int userId) {
		long voteCount = 0;
		FriendVoteBean friendVote = getFriendService().getFriendVote(
				"user_id=" + userId);
		if (friendVote != null) {
			voteCount = friendVote.getCount();
		} else {
			voteCount = -1;
		}
		return voteCount;
	}

	// 获取个人交友信息
	public FriendBean getFriend(int userId) {
		return getFriendService().getFriend(userId);
	}

	// liq_2007.3.25删除前天赌神排行榜中的帽子，并给昨天的赌神加帽子
	public void updateDuList(HttpServletRequest request) {
		List ret = null;
		String key = "pklist";
		String group = "top";
		userService = getUserService();
		ret = (List) OsCacheUtil.get(key, group, 24 * 60 * 60);
		//删除前一天赌神的帽子
		if (ret == null) {
			String sql = "select user_id from jc_user_top where mark=6";
			ret = SqlUtil.getIntList(sql);
			// 
			for (int i = 0; i < ret.size(); i++) {
				Integer userId = (Integer) ret.get(i);
				if (userId == null) {
					continue;
				}
				UserBean user = UserInfoUtil.getUser(userId.intValue());
				if (null != user.getUs2())
						userService.updateUserStatus(
								"image_path_id = 0", "user_id = "
										+ userId.intValue());
				UserStatusBean.flushUserHat(userId.intValue());
				UserInfoUtil.userStatusCache.srm(userId);
			}
			SqlUtil.executeUpdate("delete from jc_user_top where mark=6");
			// ///////////////////////////////////////给今天的赌神加帽子
			ret = null;
			sql = "select user_id from (SELECT sum(win_count) count, user_id FROM wgame_history0 where win_count>10 group by user_id) as temp "
					+ " order by count desc limit 0,10";
			ret = SqlUtil.getIntList(sql, Constants.DBShortName);
			// 
			for (int i = 0; i < ret.size(); i++) {
				Integer userId = (Integer) ret.get(i);
				if (userId == null) {
					continue;
				}
				UserBean user = UserInfoUtil.getUser(userId.intValue());
				if (null != user.getUs2())
					if (user.getUs2().getImagePathId() == 0)
						if(i == 0)
						    userService.updateUserStatus("image_path_id = '-10'","user_id = " + userId);
						else if(i == 1)
							userService.updateUserStatus("image_path_id = '-11'","user_id = " + userId);
						else if(i == 2)
							userService.updateUserStatus("image_path_id = '-12'","user_id = " + userId);
						else 
							userService.updateUserStatus("image_path_id = '-13'","user_id = " + userId);
				UserStatusBean.flushUserHat(userId.intValue());
				UserInfoUtil.userStatusCache.srm(userId);
				SqlUtil.executeUpdate("insert into jc_user_top set mark=6,priority="+i+",user_id="+userId);
			}

			// //////////////////////////////////////////////////////////////////////////////////
			if (ret == null) {
				ret = new ArrayList();
			}

			OsCacheUtil.put(key, ret, group);
		}
		
		request.setAttribute("pkList", ret);
	}
}
