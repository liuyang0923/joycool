package net.joycool.wap.action.fs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author macq
 * @explain： 浮生记游戏
 * @datetime:2007-3-26 16:16:37
 */
public class FSAction extends CustomAction{

	int NUMBER_PER_PAGE = 10;

	// 游戏结束后判断是否奖励乐币金额字段
	public static int FS_GAMEOVER_MONEY = 50000000;
	public static String mode[] = { "标准", "短篇", "超短篇" };

	// 游戏奖励金额
	public static int FS_PRIZE_MONEY = 10000;
	public static String FS_USER_KEY = "fs_user_key";
	public static String title = "乐酷浮生记";

	// 治疗健康度一点所有金钱
	public static int CURE_MONEY = 3000;

	// 扩大行囊大小所需金钱
	public static int USER_BAG_MONEY = 25000;

	// 扩大行囊大小（一次加50）所需金钱
	public static int USER_BAG_COUNT = 10;

	// 默认战斗场景
	public int defaultSceneId = 0;

	UserBean loginUser;

	public static List log = new LinkedList();

	public static int logSize = 10;

	FSUserBean fsUser = null;

	// 用户无动作时间
	public static long PK_USER_INACTIVE = 30 * 1000 * 60;

	// 城市对应名称
	public static String[] FS_CITY_NAME = { "北京", "上海", "天津", "重庆", "广州", "深圳",
			"东莞", "济南" };

	public static FSService fsService = new FSService();

	public FSAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		
		if(loginUser != null) {
			check(); // check fsuser
			fsUser.setActionTime(System.currentTimeMillis());
		}
	}

	public static FSService getService() {
		return fsService;
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-3-29 11:29:34
	 * @param request
	 * @return void
	 */
	public void check() {
		Integer key = new Integer(loginUser.getId());
		fsUser = (FSUserBean)session.getAttribute(FS_USER_KEY);
		FSUserBean fsUserCache = (FSUserBean) FSWorld.getFSUserMap().get(key);
		// session中不存在浮生记用户信息(先查数据库的操作是因为一个用户可能拥有多个角色)
		if (fsUser == null) {
			// 用户不在场景内进入默认场景
			if (fsUserCache == null) {
				// 获取浮生记系统中用户的信息
				fsUser = getService().getFSUser("user_id=" + loginUser.getId());
				// 判断是否为新用户
				if (fsUser == null) {
					fsUser = new FSUserBean();
					// 设置用户Id
					fsUser.setUserId(loginUser.getId());
					// 添加用户
					fsUser.reset();
					getService().addFSUser(fsUser);

				}
				FSWorld.getFSUserMap().put(key, fsUser);
				// 初始化场景数据
				if(fsUser.getDay() < 3)		// 如果>=3表示已经存储了
					FSWorld.refreshScene(fsUser);
			}
			else {
				fsUser = fsUserCache;
			}
		} else {
			// 判断用户是否下线
			if (fsUser.isOffline()) {
				// 判断用户map中是否包含fsuser信息
				if (fsUserCache != null) {
					fsUser = fsUserCache;
				} else {
					// 添加角色到用户map中
					FSWorld.getFSUserMap().put(key, fsUser);
				}
				fsUser.setOffline(false);
			}
		}
//		 更新fsUser当前session中的引用
		session.setAttribute(FS_USER_KEY, fsUser);
	}

	/**
	 * 开始游戏
	 */
	public void start() {
		String strType = request.getParameter("type");
		if (strType != null) {	//	改变游戏模式
			int type = StringUtil.toInt(strType);
			
			fsUser.setType(type);
			fsUser.reset();
			FSWorld.refreshScene(fsUser);
			
			tip("redirect");
		}
	}
	
	/**
	 * @author macq
	 * @explain： 场景页面
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void index() {
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain : 进入场景
	 * @datetime:2007-3-27 下午05:53:50
	 */
	public void enter(int sceneId, boolean flag) {
		int scene = fsUser.getSceneId();
		if (flag) {
			scene = sceneId;
			// 如果用户不在上一个页面改变用户场景
			if (fsUser.getSceneId() != scene) {
				// 设置初始pk用户所在场景
				fsUser.setSceneId(scene);
			}
		} else {
			// 获取上一个场景id
			scene = fsUser.getSceneId();
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：购买物品页面
	 * @datetime:2007-3-26 17:28:30
	 * @param request
	 * @return void
	 */
	public void sell() {
		// 从session中获取场景信息
		FSSceneBean scene = fsUser.getScene();
		;
		if (scene == null) {
			doTip(null, null);
			return;
		}
		// 获取准备买的物品Id
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			doTip(null, null);
			return;
		}
		FSProductBean Product = (FSProductBean) FSWorld.loadFSProduct().get(
				new Integer(productId));
		if (Product == null) {
			doTip(null, null);
			return;
		}
		// 判断场景内是否允许交易该商品
		FSUserBagBean userBag = (FSUserBagBean) fsUser.getProductMap().get(
				new Integer(productId));
		if (userBag == null) {
			doTip(null, "您的行囊中没有" + Product.getName() + "！");
			return;
		}
		// 判断场景内是否允许交易该商品
		FSUserBagBean fsProduct = (FSUserBagBean) scene.getSceneProductMap()
				.get(new Integer(productId));
		if (fsProduct == null) {
			doTip(null, "对不起，最近本市城管查得严" + Product.getName() + "不让交易了！");
			return;
		}
		request.setAttribute("userBag", userBag);
		session.setAttribute("fsBuy", "true");
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain：购买物品结果页面
	 * @datetime:2007-3-26 17:28:42
	 * @param request
	 * @return void
	 */
	public void sellResult() {
		// 防止刷新
		if (session.getAttribute("fsBuy") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsBuy");
		// 从session中获取场景信息
		FSSceneBean scene = fsUser.getScene();
		if (scene == null) {
			doTip(null, null);
			return;
		}
		// 获取准备买的物品Id
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取准备出售的物品数量
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0) {
			doTip(null, "请确认输入数量");
			return;
		}
		// 获取物品基础数据
		FSProductBean Product = (FSProductBean) FSWorld.loadFSProduct().get(
				new Integer(productId));
		if (Product == null) {
			doTip(null, null);
			return;
		}
		// 判断场景内是否允许交易该商品
		FSUserBagBean userProduct = (FSUserBagBean) fsUser.getProductMap().get(
				new Integer(productId));
		if (userProduct == null) {
			doTip(null, "您的行囊中没有" + Product.getName() + "！");
			return;
		}
		// 判断场景内是否允许交易该商品
		FSUserBagBean fsProduct = (FSUserBagBean) scene.getSceneProductMap()
				.get(new Integer(productId));
		if (fsProduct == null) {
			doTip(null, "对不起，最近本市城管查得严" + Product.getName() + "不让交易了！");
			return;
		}
		// 判断用户是否有足够的物品数量
		if (userProduct.getCount() < count) {
			doTip(null, "对不起，您没有足够数量的" + Product.getName() + "出售！当前拥有数量为"
					+ userProduct.getCount());
			return;
		}
		int countChange = userProduct.getCount() - count;
		// 判断出售后剩余数量
		if (countChange <= 0) {
			// 如果小于0删除该物品
			fsUser.getProductMap().remove(new Integer(productId));
		} else {
			// 减少持有数量
			userProduct.setCount(countChange);
		}
		// 出售物品给用户增加现金
		fsUser.setMoney(fsUser.getMoney() + fsProduct.getPrice() * count);
		addLog(StringUtil.toWml(loginUser.getNickName()) + "卖出"
				+ userProduct.getName() + "，单价" + fsProduct.getPrice() + "数量"
				+ count);
		doTip("success", userProduct.getName() + "出售成功，出售数量为" + count + "。");
	}

	/**
	 * 
	 * @author macq
	 * @explain：出售物品结果
	 * @datetime:2007-3-26 17:28:46
	 * @param request
	 * @return void
	 */
	public void buy() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		// 从session中获取场景信息
		FSSceneBean scene = fsUser.getScene();
		if (scene == null) {
			doTip(null, null);
			return;
		}
		// 获取准备买的物品Id
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			doTip(null, null);
			return;
		}
		// 判断场景内是否包含该物品
		FSUserBagBean fsProduct = (FSUserBagBean) scene.getSceneProductMap()
				.get(new Integer(productId));
		if (fsProduct == null) {
			doTip(null, "黑市内不出售该类商品");
			return;
		}
		// 获取用户现金
		int userMoney = fsUser.getMoney();
		// 判断用户是否可以购买
		if (userMoney <= 0) {
			doTip(null, "您的现金不够！");
			return;
		}
		request.setAttribute("fsProduct", fsProduct);
		doTip("success", null);
		session.setAttribute("fsSale", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain：出售物品结果页面
	 * @datetime:2007-3-26 17:28:48
	 * @param request
	 * @return void
	 */
	public void buyResult() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		// 防止刷新
		if (session.getAttribute("fsSale") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsSale");
		// 从session中获取场景信息
		FSSceneBean scene = fsUser.getScene();
		if (scene == null) {
			doTip(null, null);
			return;
		}
		// 获取准备买的物品Id
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取准备购买物品数量
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0) {
			doTip(null, "请确认输入数量");
			return;
		}
		// 判断场景内是否包含该物品
		FSUserBagBean fsProduct = (FSUserBagBean) scene.getSceneProductMap()
				.get(new Integer(productId));
		if (fsProduct == null) {
			doTip(null, "黑市内不出售该类商品");
			return;
		}
		// 获取用户行囊中物品
		Iterator it = fsUser.getProductMap().values().iterator();
		// 物品总数量
		int totalCount = 0;
		while (it.hasNext()) {
			FSUserBagBean userBag = (FSUserBagBean) it.next();
			totalCount += userBag.getCount();
		}
		// 判断是否房间容量已满
		if (fsUser.getUserBag() < totalCount + count) {
			doTip(null, "您的房间没有足够的位置，请扩大房间容量！");
			return;
		}
		// 获取用户现金
		int userMoney = fsUser.getMoney();
		// 获取购买物品总价格
		int productPrice = fsProduct.getPrice() * count;
		// 判断用户是否可以购买
		if (userMoney < productPrice) {
			doTip(null, "您的现金不够！");
			return;
		}
		// 更新用户现金
		fsUser.setMoney(fsUser.getMoney() - productPrice);
		// 判断用户是否已经拥护该物品
		FSUserBagBean userProduct = (FSUserBagBean) fsUser.getProductMap().get(
				new Integer(fsProduct.getProductId()));
		if (userProduct == null) {
			// 给用户添加物品
			userProduct = new FSUserBagBean();
			userProduct.setProductId(fsProduct.getProductId());
			userProduct.setName(fsProduct.getName());
			userProduct.setPrice(fsProduct.getPrice());
			userProduct.setCount(count);
			// 存入用户行囊
			fsUser.getProductMap().put(new Integer(userProduct.getProductId()),
					userProduct);
		} else {
			// 增加持有物品数量
			int newCount = userProduct.getCount() + count;
			userProduct.setPrice((userProduct.getPrice()
					* userProduct.getCount() + fsProduct.getPrice() * count)
					/ newCount);
			userProduct.setCount(newCount);

		}
		addLog(StringUtil.toWml(loginUser.getNickName()) + "购买"
				+ userProduct.getName() + "，单价" + fsProduct.getPrice() + "数量"
				+ count);
		doTip("success", userProduct.getName() + "购买成功，购买数量为" + count
				+ ",已经存入您的行囊");
	}

	/**
	 * 
	 * @author macq
	 * @explain：游戏结果页面
	 * @datetime:2007-3-26 17:28:51
	 * @param request
	 * @return void
	 */
	public void result() {
		String tip = "";
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		// if (fsUser.isGameOver()) {
		//			
		// doTip("success", tip);
		// return;
		// }
		// // 防止刷新
		// if (session.getAttribute("sceneList") == null) {
		// doTip("refresh", null);
		// return;
		// }
		// session.removeAttribute("sceneList");
		if (!fsUser.isGameOver()) {
			doTip(null, "游戏尚未结束,请继续!");
			return;
		}

		doTip("success", tip);
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行页面
	 * @datetime:2007-3-27 10:19:01
	 * @param request
	 * @return void
	 */
	public void bank() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		doTip("success", null);
		session.setAttribute("fsBank", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行操作结果页面
	 * @datetime:2007-3-27 10:19:04
	 * @param request
	 * @return void
	 */
	public void bankResult() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		// 防止刷新
		if (session.getAttribute("fsBank") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsBank");
		int saveMoney = StringUtil.toInt(request.getParameter("saveMoney"));
		int getMoney = StringUtil.toInt(request.getParameter("getMoney"));
		// 存钱
		if (saveMoney > 0) {
			if (fsUser.getMoney() < saveMoney) {
				doTip(null, "您没有足够的钱存入银行！");
				return;
			}
			// 减去现金
			fsUser.setMoney(fsUser.getMoney() - saveMoney);
			// 增加存款
			fsUser.setSaving(fsUser.getSaving() + saveMoney);
			doTip("success", "您成功的存入银行" + saveMoney + "元.");
		}// 取钱
		else if (getMoney > 0) {
			if (fsUser.getSaving() < getMoney) {
				doTip(null, "您在银行中没有足够的存款不能取钱！");
				return;
			}
			// 减去存款
			fsUser.setSaving(fsUser.getSaving() - getMoney);
			// 增加现金
			fsUser.setMoney(fsUser.getMoney() + getMoney);
			doTip("success", "您成功的从银行取出" + getMoney + "元.");
		}// 数据有问题
		else {
			doTip(null, "请确认操作方式");
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 医院页面
	 * @datetime:2007-3-27 10:22:40
	 * @param request
	 * @return void
	 */
	public void hospital() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		doTip("success", null);
		session.setAttribute("fsHospital", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain：医院结果页面
	 * @datetime:2007-3-27 10:22:53
	 * @param request
	 * @return void
	 */
	public void hospitalResult() {
		if (fsUser == null) {
			doTip(null, null);
			return;
		}
		if (session.getAttribute("fsHospital") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsHospital");
		// 治疗点数
		int count = StringUtil.toInt(request.getParameter("count"));
		if (count <= 0) {
			doTip(null, "输入数量有误！");
			return;
		}
		// 治疗所需钱数
		int needMoney = count * CURE_MONEY;
		// 判断是否可以治疗
		if (needMoney > fsUser.getMoney()) {
			doTip(null, "您的现金不够，多挣些钱去吧！");
			return;
		} else {
			// 更新用户钱
			fsUser.setMoney(fsUser.getMoney() - needMoney);
		}
		fsUser.setHealth(fsUser.getHealth() + count);
		// 如果健康度大于100，等于最大值
		if (fsUser.getHealth() > 100) {
			fsUser.setHealth(100);
		}
		doTip("success", "治疗完成，您现在的健康度为:" + fsUser.getHealth());
	}

	/**
	 * 
	 * @author zhouj
	 * @explain： 游戏正常结束
	 * @return void
	 */
	private void finishGame() {
		fsUser.setGameStatus(FSUserBean.STATUS_END);
		int totalMoney = fsUser.getMoney() + fsUser.getSaving()
				- fsUser.getDebt();
		int reward = totalMoney;
		// 超过1千万，有一定几率得到卡片(100%)
		if(reward >= 10000000) {
			UserBagCacheUtil.addUserBagCacheNotice(loginUser.getId(), 54, null);
		}
		// 判断是否可以奖励乐币
		if (reward > FS_GAMEOVER_MONEY) {
			reward = FS_GAMEOVER_MONEY;
		} else if (totalMoney < FS_PRIZE_MONEY) {
			reward = FS_PRIZE_MONEY;
		}
		UserInfoUtil.updateUserCash(loginUser.getId(), reward, 
				UserCashAction.GAME, "用户玩浮生记给用户增加" + reward + "乐币");

		String tip = "恭喜完成游戏，奖励" + reward + "乐币.";
		// 获取用户玩浮生记最高分记录
		FSTopBean highscore = getService().getHighscore(" user_id=" + fsUser.getUserId() + " and type=" + fsUser.getType());
		if(highscore == null) {
			highscore = new FSTopBean();
			getService().addHighscore(" user_id=" + fsUser.getUserId() + ",type=" + fsUser.getType());
		}
		int MaxMoney = highscore.getHighScore();
			
		if (totalMoney > MaxMoney) {
			// 更新数据里最高分记录
			getService().updateFSTop(
					"high_score=" + totalMoney,
					"user_id=" + fsUser.getUserId() + " and type=" + fsUser.getType());
			// 更新内存中玩家最高分记录
			tip = tip + "您创造了自己新的最高分新记录！";
		}

		if (totalMoney > highscore.getRecentHighScore()) {
			// 更新数据里最近最高分记录
			getService().updateFSTop(
					"create_datetime=curdate(),recent_high_score=" + totalMoney,
					"user_id=" + fsUser.getUserId() + " and type=" + fsUser.getType());
			// 更新内存中玩家最高分记录
			tip = tip + "您刷新了自己的最近高分新记录！";
		}
		fsUser.setGameResultTip(tip);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 场景列表页面
	 * @datetime:2007-3-27 10:25:12
	 * @param request
	 * @return void
	 */
	public void sceneList() {
		// 获取场景id
		int scene = getParameterIntS("sceneId");

		// 判断场景id是否改变
		if (scene >= 0) {
			
			// 更换场景日期减一
			fsUser.addDate();
			if (fsUser.isDateOver()) {
				if (!fsUser.isGameOver()) {
					// 在此设置游戏结束，并给予奖励
					finishGame();

				}
				// session.setAttribute("sceneList", "sceneList");
				doTip("timeOver", null);
				return;
			}
			// 如果负债大于零，更新当前负债增加原有基础的10%
			if (fsUser.getDebt() > 0) {
				int debt = (int) (fsUser.getDebt() * 1.1);
				fsUser.setDebt(debt);
			}
			// 更新用户当前场景
			fsUser.setSceneId(scene);

			// 初始化一个场景
			FSWorld.refreshScene(fsUser);
			FSWorld.refreshEvent(fsUser);

			doTip("redirect", null);
			return;
		}

		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 游戏总排行榜
	 * @datetime:2007-3-27 11:39:21
	 * @param request
	 * @return void
	 */
	public void topall() {
		int type = getType();
		setAttribute("type", type);
		
		FSTopBean highscore = getService().getHighscore(" user_id=" + fsUser.getUserId() + " and type=" + type);
		if(highscore == null)
			highscore = new FSTopBean();
		setAttribute("highscore", highscore.getHighScore());
		
		int count = SqlUtil.getIntResult(
				"select count(id) from fs_top where type=" + type + " and high_score> "
						+ highscore.getHighScore(), Constants.DBShortName);
		request.setAttribute("count", Integer.valueOf(count + 1));

		// 该查询在impl中启用缓存请注意，更新时间为1小时
		Vector topList = getService().getFSTopList(
				" type=" + type + " and high_score>0 order by high_score desc limit 100");
		int totalCount = topList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		
		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);
		
		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List list1 = topList.subList(start, end);
		request.setAttribute("fsUserList", list1);
		doTip("success", null);
	}

	// 最近排行榜，暂定5天
	public void top() {
		int type = getType();
		setAttribute("type", type);
		
		FSTopBean highscore = getService().getHighscore(" user_id=" + fsUser.getUserId() + " and type=" + type);
		if(highscore == null)
			highscore = new FSTopBean();
		setAttribute("highscore", highscore.getRecentHighScore());
		
		int count = SqlUtil.getIntResult(
				"select count(id) from fs_top where type=" + type + " and recent_high_score> "
						+ highscore.getRecentHighScore(), Constants.DBShortName);
		request.setAttribute("count", Integer.valueOf(count + 1));

		// 该查询在impl中启用缓存请注意，更新时间为1小时
		Vector topList = getService().getFSTopList(
				" type=" + type + " and recent_high_score>0 order by recent_high_score desc limit 100");
		int totalCount = topList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));

		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);
		
		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List list1 = topList.subList(start, end);
		request.setAttribute("fsUserList", list1);
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 重新开始游戏
	 * @datetime:2007-3-27 11:39:11
	 * @param request
	 * @return void
	 */
	public void refresh() {
		if (fsUser != null) {
			// 初始化用户
			fsUser.reset();
		}
		doTip("success", null);
	}
	
	/**
	 * 保存游戏
	 */
	public void savegame() {
		if(fsUser.isGameOver()) {
			tip(null, "游戏已经结束，无法保存");
			return;
		}
		
		if(getService().saveFSUser(fsUser))
			tip("success", "游戏已成功保存");
		else
			tip("success", "游戏保存失败");
	}

	/**
	 * 
	 * @author macq
	 * @explain： 邮局
	 * @datetime:2007-3-27 11:43:59
	 * @param request
	 * @return void
	 */
	public void postOffice() {
		if (fsUser.getDebt() == 0) {
			doTip(null, "您没有需要偿还的债务！");
			return;
		}
		doTip("success", null);
		session.setAttribute("fsPostOffice", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain： 邮局操作结果页面
	 * @datetime:2007-3-27 11:44:24
	 * @param request
	 * @return void
	 */
	public void postOfficeResult() {
		if (session.getAttribute("fsPostOffice") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsPostOffice");
		// 治疗点数
		int loan = StringUtil.toInt(request.getParameter("loan"));
		if (loan <= 0) {
			doTip(null, "输入金额有误！");
			return;
		}
		// 债务
		int needMoney = loan;
		// 判断是否可以偿还债务
		if (needMoney > fsUser.getMoney()) {
			doTip(null, "您的现金不够，多挣些钱去吧！");
			return;
		}
		// 更新用户钱
		fsUser.setMoney(fsUser.getMoney() - needMoney);
		// 更新用户债务
		fsUser.setDebt(fsUser.getDebt() - loan);
		// 如果债务小于0，等于0
		if (fsUser.getDebt() < 0) {
			fsUser.setDebt(0);
			doTip("success", "本次更还债务" + loan + "元，债务全部还清！");
		} else {
			doTip("success", "本次更还债务" + loan + "元，您还背负" + fsUser.getDebt()
					+ "元债务");
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：黑市页面
	 * @datetime:2007-3-27 11:46:02
	 * @param request
	 * @return void
	 */
	public void blackMarket() {
		// 从session中获取当前场景内物品
		FSSceneBean fsScene = fsUser.getScene();
		if (fsScene == null) {
			doTip(null, null);
			return;
		}
		HashMap sceneProductMap = fsScene.getSceneProductMap();
		if (sceneProductMap.size() <= 0) {
			doTip(null, null);
			return;
		}
		doTip("success", null);
	}

	/**
	 * 
	 * @author macq
	 * @explain： 房屋中介页面
	 * @datetime:2007-3-27 13:55:36
	 * @param request
	 * @return void
	 */
	public void userBag() {
		doTip("success", null);
		session.setAttribute("fsUserBag", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain：房屋中介操作结果页面
	 * @datetime:2007-3-27 13:56:18
	 * @param request
	 * @return void
	 */
	public void userBagResult() {
		// 防止刷新
		if (session.getAttribute("fsUserBag") == null) {
			doTip("refresh", null);
			return;
		}
		session.removeAttribute("fsUserBag");
		// 限制房屋数量为200
		if (fsUser.getUserBag() >= 200) {
			doTip(null, "你的房子比局长的房子都大了，还租房？？？");
			return;
		}
		// 计算扩大后的房间和扩大房间所需金额
		int needBag = fsUser.getUserBag() + USER_BAG_COUNT;
		int needMoney = USER_BAG_MONEY * (needBag - FSUserBean.INIT_BAG_COUNT)
				/ 10;
		// 判断是否可以扩充
		if (needMoney > fsUser.getMoney()) {
			doTip(null, "身上没个" + (needMoney + 10000) + "还想租更大的房子？多挣些钱去吧！");
			return;
		}
		// 取得随机数，判断是否被骗
		int flag = RandomUtil.nextInt(2);
		// 更新用户钱（中介多骗一点，这部分以后再修改）
		fsUser.setMoney(fsUser.getMoney() - needMoney - RandomUtil.nextInt(5)
				* 1000 - 1000);
		if (flag == 1) {
			// 更新用户行囊大小
			fsUser.setUserBag(fsUser.getUserBag() + USER_BAG_COUNT);
			doTip("success", "虽然租到了一个大一点的房子，不过感觉被中介骗了不少钱");
		} else {
			// 被打减少健康度
			fsUser.setHealth(fsUser.getHealth() - 5);
			doTip("success", "您交了中介费没见到房，要钱时出来几个大汉打了你一顿，中介费也拿不回来……");
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 添加浮生记用户
	 * @datetime:2007-3-27 9:30:54
	 * @param bean
	 * @return
	 * @return boolean
	 */
	public boolean addFSUser(FSUserBean bean) {
		if (getService().addFSUser(bean)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加场景动态log
	 * @datetime:2007-3-26 下午04:22:16
	 * @param content
	 * @return
	 */
	public void addLog(String content) {
		synchronized (log) {
			// 判断场景log是否大于50条
			if (log.size() > logSize) {
				log.remove(0);
			}
			// 添加场景log
			log.add(content);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 场景内log
	 * @datetime:2007-3-29 19:22:47
	 * @param log
	 * @return
	 * @return String
	 */
	public String toString(List log) {
		StringBuilder result = new StringBuilder();
		int listSize = log.size();
		int startIndex = 0;
		if (listSize > logSize) {
			startIndex = listSize - logSize;
		}
		for (int i = startIndex; i < listSize; i++) {
			result.append(log.get(i));
			result.append("<br/>");
		}
		return result.toString();
	}

	/**
	 * 
	 * @author macq
	 * @explain：定时清空无效session中的数据
	 * @datetime:2007-3-26 18:11:00
	 * @return void
	 */
	public static void timeTask() {
		// 获取所有玩游戏的用户
		HashMap fsUserMap = FSWorld.getFSUserMap();
		if (fsUserMap != null) {
			HashMap map = (HashMap) fsUserMap.clone();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Integer id = (Integer) iter.next();
				FSUserBean bean = (FSUserBean) map.get(id);
				if (bean == null)
					continue;
				// 判断用户是否下线
				if (bean.userOffline()) {
					// 设置用户为离线
					bean.setOffline(true);
					getService().saveFSUser(bean);
					// 删除map中用户的信息
					FSWorld.getFSUserMap()
							.remove(new Integer(bean.getUserId()));
				}

			}
		}
	}

	public static String getMode(int type) {
		return mode[type];
	}
	
	/**
	 * 从request得到type，如果不存在，返回用户的type
	 */
	public int getType() {
		int type = getParameterIntS("type");
		if (type >= 0) {
			if(type >= 0 || type < FSUserBean.TYPE_MAX)
				return type;
		}
		
		return fsUser.getType();
	}
	
	public FSUserBean getFsUser() {
		return fsUser;
	}
}
