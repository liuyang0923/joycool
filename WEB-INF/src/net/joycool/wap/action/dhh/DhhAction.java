package net.joycool.wap.action.dhh;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author liq
 * @explain： 大航海
 * @datetime:2007-4-20
 */
public class DhhAction extends CustomAction{

	int NUMBER_PER_PAGE = 10;
	
	public static String DHH_USER_KEY = "dhh_user_key";

	// 游戏奖励金额
	public static int SAILOR_MONEY = 10;

	public static String title = "乐酷海商王";
	
	UserBean loginUser;

	public static int logSize = 10;

	DhhUserBean dhhUser = null;

	// 用户无动作时间30分钟
//	public static long PK_USER_INACTIVE = 30 * 1000 * 60;
	public static long PK_USER_INACTIVE = 1000 * 10;

	public static DHHService service = new DHHService();

	public DhhAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		if(loginUser != null) {
			check(); // check user
			dhhUser.setActionTime(System.currentTimeMillis());
		}
	}

	public DHHWorld getWorld() {
		return DHHWorld.world;
	}
	
	public static DHHService getService() {
		return service;
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
		dhhUser = (DhhUserBean)session.getAttribute(DHH_USER_KEY);
		DhhUserBean userCache = (DhhUserBean) DHHWorld.getDHHUserMap().get(key);
		if (dhhUser == null) {
			if (userCache == null) {
				// 获取浮生记系统中用户的信息
				dhhUser = getService().getUser("user_id=" + loginUser.getId());
				// 判断是否为新用户
				if (dhhUser == null) {
					dhhUser = new DhhUserBean();
					// 设置用户Id
					dhhUser.setUserId(loginUser.getId());
					// 添加用户
					getService().addUser(dhhUser);
				}
				DHHWorld.getDHHUserMap().put(key, dhhUser);
			}// 用户在场景内直接进入上一个场景
			else {
				dhhUser = userCache;
			}

		} else {
			// 判断用户是否下线
			if (dhhUser.isOffline()) {
				// 判断用户map中是否包含fsuser信息
				if (userCache != null) {
					// 更新fsUser当前session中的引用
					dhhUser = userCache;
				} else {
					// 添加角色到用户map中
					DHHWorld.getDHHUserMap().put(key, dhhUser);
				}
				dhhUser.setOffline(false);
			}
		}
		session.setAttribute(DHH_USER_KEY, dhhUser);
	}

	/**
	 * @author liq
	 * @explain： 游戏首页
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void index() {

	}

	/**
	 * @author macq
	 * @explain： 游戏港口页面play.jsp
	 * @datetime:
	 * @param request
	 * @return void
	 */
	public void play() {
		//
		int sceneId = StringUtil.toInt(request.getParameter("sceneId"));
		//重新开始
		if(sceneId < -1) {
			dhhUser.reset();
			getService().updateUser(
					"ship_id = "+dhhUser.getShip()
					+", city_id = "+dhhUser.getCity()
					+", money = "+dhhUser.getMoney()
					+", saving = "+dhhUser.getSaving()
					+", past_time = "+dhhUser.getPasttime()
					,"user_id = "+dhhUser.getUserId());
		}
		return;
	}
	
	// 查看船只信息
	public void ship() {
		int id = StringUtil.toInt(request.getParameter("id"));
		if(id > 0) {
			DhhShipBean ship = (DhhShipBean)getWorld().shipMap.get(new Integer(id));
			request.setAttribute("ship", ship);
		}
	}

	/**
	 * @author macq
	 * @explain： 游戏城市跳转页面
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void go() {
		
		String event = "祝贺你,此次航行一帆风顺!";
		int cityid = StringUtil.toInt(request.getParameter("cityid"));
		DhhShipBean ship = getUserShip();
		//正常的时间
		int time = (DHHWorld.cityDist[dhhUser.getCity()-1][cityid-1] / ship.getSpeed());
		//航行过程中发生事件
		if(RandomUtil.percentRandom(20))
		{
			switch(RandomUtil.nextInt(6))
			{
			case 0:
				event = "遭遇台风，你的船受损严重！";
//				航行时间在基础上增加3天
				time += 3;
				break;
			case 1:
				event = "海豚在前面带路，你的船航行起来很轻松。";
//				航行时间缩短为１天
		        time = 1;
				break;
			case 2:
				event = "你拯救了几名海难水手，他们送你一些钱表示感谢。";
//				获得5万现金
				dhhUser.setMoney((int) (dhhUser.getMoney() * 1.1));
				break;
			case 3:
				event = "天气炎热，饮水不足，水手们没有精神干活，船速变慢了。";
//				航行时间在基础上增加1天
				time += 1;
				break;
			case 4:
				event = "你的水手中有人手脚不干净。";
//				货物按数量损失10％
				lostCargo(90);
				break;
			case 5:
				event = "海盗来袭，你扔掉部分货物才加速逃脱。";
//				货物按数量损失20％
				lostCargo(80);
				break;
			}
		}
		
		//水手工资
		int wag = time * SAILOR_MONEY * ship.getSailor();
		dhhUser.setMoney(dhhUser.getMoney() - wag);
		String wages = "你支付"+ship.getSailor()+"名水手"+time+"天的工资,共计:"+Integer.toString(wag);
		
		String message="经过"+time+"天的航行,你到达"+((DhhCityBean)getWorld().cityMap.get(new Integer(cityid))).getName();
		//时间增加
		dhhUser.setPasttime(dhhUser.getPasttime() + time);
		//改变用户所在城市
		dhhUser.setCity(cityid);
		request.setAttribute("event", event);
		request.setAttribute("message", message);
		request.setAttribute("wages", wages);
		ifOver();
		return;
	}

	/**
	 * 发生事件,船上的货物减少
	 * 
	 * @param lost
	 */
	public void lostCargo(int lost) {
		int count = 0;
		for (int i = 0; i < dhhUser.getProductMap().keySet().size(); i++) {
			// 取得现在分类商品有多少货物
			int now = ((UserBagBean) dhhUser.getProductMap().get(
					dhhUser.getProductMap().keySet().toArray()[i])).getNumber();
			// 取得损失后剩下多少货物
			int last = Math.round((float)now * lost / 100);
			// 统计一共损失多少货物
			count += (now - last);
			// 设置分类损失剩余的货物
			((UserBagBean) dhhUser.getProductMap().get(
					dhhUser.getProductMap().keySet().toArray()[i]))
					.setNumber(last);
		}
		// 船上的总货物减少
		dhhUser.setVolume(dhhUser.getVolume() - count);
	}
	
	/**
	 * 游戏结束类型判断
	 * 
	 * @param lost
	 */
	public void ifOver() {
		if(dhhUser.isGameOver())
			return;
		//游戏失败
		if((dhhUser.getMoney() + dhhUser.getSaving()) < -1000)
		{
			dhhUser.setGameStatus(DhhUserBean.STATUS_LOSE);
		}
		//游戏结束
		if(dhhUser.getPasttime() > DhhUserBean.TOTAL_DAY)
		{
			dhhUser.setGameStatus(DhhUserBean.STATUS_END);
		}
	}
	

	/**
	 * 
	 * @author zhouj
	 * @explain： 游戏正常结束
	 * @return void
	 */
	private void finishGame() {
		if (dhhUser.getGameStatus() == DhhUserBean.STATUS_LOSE) {
			int totalMoney = dhhUser.getMoney() + dhhUser.getSaving();
			int reward = totalMoney / 5;
			String tip = "你拖欠水手工资过多，被他们扔海里喂鱼去了。";
			dhhUser.setGameResultTip(tip);
		}

		if (dhhUser.getGameStatus() == DhhUserBean.STATUS_END) {
			int totalMoney = dhhUser.getMoney() + dhhUser.getSaving();
			String tip = "";
			if(totalMoney >= 10000) {
				int reward = totalMoney / 5;
	
				UserInfoUtil.updateUserCash(loginUser.getId(), reward, 
						UserCashAction.GAME, "大航海给用户增加" + reward + "乐币");
	
				tip += "恭喜完成游戏，奖励" + reward + "乐币。";
			} else {
				tip += "恭喜完成游戏，但好像没赚到什么钱";
			}
				
			
			// 获取用户玩浮生记最高分记录
			int MaxMoney = dhhUser.getHighScore();
			if (totalMoney > MaxMoney) {
				// 更新数据里最高分记录
				getService().updateUser(
						"high_score=now(),total_score=" + totalMoney,
						"user_id=" + dhhUser.getUserId());
				// 更新内存中玩家最高分记录
				dhhUser.setHighScore(totalMoney);
				tip = tip + "<br/>你创造了自己新的最高分新记录！";
			}

			if (totalMoney > dhhUser.getRecentHighScore()) {
				// 更新数据里最近最高分记录
				getService().updateUser(
						"recent_high_score=curdate(),recent_score="
								+ totalMoney, "user_id=" + dhhUser.getUserId());
				// 更新内存中玩家最高分记录
				dhhUser.setRecentHighScore(totalMoney);
				tip = tip + "<br/>你刷新了自己的最近高分新记录！";
			}
			dhhUser.setGameResultTip(tip);
			
			service.updateUser("past_time=1", "user_id=" + loginUser.getId());
		}
	}

	
	/**
	 * @author liq
	 * @explain： 游戏买物品页面play.jsp
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void buy() {
		DhhCityBean city = getUserCity();
		int productId = StringUtil.toInt(request.getParameter("productId"));
		DhhCitProBean product = (DhhCitProBean)city.getProductMap().get(new Integer(productId));
		request.setAttribute("product", product);
	}

	/**
	 * @author macq
	 * @explain： 游戏卖物品页面play.jsp
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void sell() {
		// 获取准备买的物品Id
		int productId = StringUtil.toInt(request.getParameter("productId"));
		UserBagBean product = (UserBagBean)dhhUser.getProductMap().get(new Integer(productId));
		request.setAttribute("product", product);
	}
	
	/**
	 * @author macq
	 * @explain： 游戏买物品结果页面
	 * @datetime:2007-3-28 17:01:57
	 * @param request
	 * @return void
	 */
	public void buyresult() {
		int count = StringUtil.toInt(request.getParameter("count"));
		int productId = StringUtil.toInt(request.getParameter("productId"));

		DhhCityBean city = getUserCity();
		DhhCitProBean product = (DhhCitProBean) city.getProductMap().get(new Integer(productId));
		if (product == null)
			return;

		// 物品的价格
		int rate = product.getBuyrate();
		// 物品库存数量
		int number = product.getQuantity();
		if (rate * count > dhhUser.money)
			tip(null, "你的现金不够");
		else if (count > number)
			tip(null, "这里的库存数量不够");
		else if (count <= 0)
			tip(null, "参数填写错误");
		// 超过船的容积了
		else if ((dhhUser.getVolume() + count) > getUserShip().getVolume())
			tip(null, "你的船舱不够用了,换艘更大的船吧");
		// 可以购买
		else {
			// 减少库存
			product.setQuantity(number - count);
			// 船的容积加入
			dhhUser.setVolume(dhhUser.getVolume() + count);
			// 扣掉货款
			dhhUser.setMoney(dhhUser.getMoney() - rate * count);
			// 放入玩家物品栏

			if (dhhUser.getProductMap().get(new Integer(productId)) != null) { 	// 包中原来就有这种物品
			
				UserBagBean userbagbean = (UserBagBean) dhhUser.getProductMap().get(
						new Integer(productId));
				userbagbean.setBuyrate((userbagbean.getBuyrate()
						* userbagbean.getNumber() + count * rate)
						/ (userbagbean.getNumber() + count));
				userbagbean.setNumber(userbagbean.getNumber() + count);
			} else {		// 原来没有
				UserBagBean userbagbean = new UserBagBean();
				userbagbean.setBuyrate(rate);
				userbagbean.setNumber(count);
				userbagbean.setProductid(productId);
				userbagbean.setProductname(product.getProductname());
				dhhUser.getProductMap()
						.put(new Integer(productId), userbagbean);
			}

			addLog(StringUtil.toWml(loginUser.getNickName()) + "购买"
					+ product.getProductname() + "，单价" + rate + "数量"
					+ count);
			tip("success");
		}
	}
/**
 * @author macq
 * @explain： 游戏卖物品结果页面
 * @param request
 * @return void
 */
public void sellresult() {
	int count = StringUtil.toInt(request.getParameter("count"));
	int productId = StringUtil.toInt(request.getParameter("productId"));
	int rate = 0;
	// 现在我有多少这种物品
	UserBagBean sellProduct = (UserBagBean) dhhUser.getProductMap().get(
			new Integer(productId));
	if(sellProduct == null) {
		tip(null, "你的库存数量不够");
		return;
	}
	int number = sellProduct.getNumber();

	DhhCityBean city = getUserCity();
	DhhCitProBean product = (DhhCitProBean) city.getProductMap().get(
			new Integer(productId));

	if (count > number)
		tip(null, "你的库存数量不够");
	else if (count <= 0)
		tip(null, "参数填写错误");
	else {
		if (product == null) {	// 本城不卖这种产品
			// 卖价
			DhhProductBean noProduct = (DhhProductBean) getWorld().productMap
					.get(new Integer(productId));
			rate = noProduct.getSell();
			// 船的容积减少
			dhhUser.setVolume(dhhUser.getVolume() - count);
			addLog(StringUtil.toWml(loginUser.getNickName()) + "卖出"
					+ noProduct.getName() + "，单价" + rate + "数量" + count);

			// 添加货款
			dhhUser.setMoney(dhhUser.getMoney() + rate * count);
			// 删除玩家物品栏
			tip("success");
			if (number != count) { // 还剩下
				sellProduct.setNumber(sellProduct.getNumber() - count);
			} else {
				dhhUser.getProductMap().remove(new Integer(productId));
			}

		} else { // 没有那么多的物品
			rate = product.getSellrate();
			product.setQuantity(product.getQuantity() + count);
			// 船的容积减少
			dhhUser.setVolume(dhhUser.getVolume() - count);
			addLog(StringUtil.toWml(loginUser.getNickName()) + "卖出"
					+ product.getProductname() + "，单价" + rate + "数量"
					+ count);

			// 添加货款
			dhhUser.setMoney(dhhUser.getMoney() + rate * count);
			// 删除玩家物品栏
			tip("success");
			if (number != count) { // 包中原来就有这种物品
				UserBagBean userbagbean = (UserBagBean) dhhUser
						.getProductMap().get(new Integer(productId));
				userbagbean.setNumber(userbagbean.getNumber() - count);
			} else { // 原来没有
				dhhUser.getProductMap().remove(new Integer(productId));
			}

		}
	}
}

	
	/**
	 * 
	 * @author macq
	 * @explain： 游戏总排行榜
	 * @datetime:2007-3-27 11:39:21
	 * @return void
	 */
	public void topall() {
		int count = SqlUtil.getIntResult(
				"select count(id) from dhh_user where total_score> "
						+ dhhUser.getHighScore(), Constants.DBShortName);
		request.setAttribute("count", Integer.valueOf(count + 1));

		// 该查询在impl中启用缓存请注意，更新时间为1小时
		Vector userList = getService().getUserList(
				" total_score>0 order by total_score desc limit 100");
		int totalCount = userList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		
		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);
		
		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List fsUserList1 = userList.subList(start, end);
		request.setAttribute("fsUserList", fsUserList1);
	}
	
	// 最近排行榜，暂定5天
	/**
	 * 
	 * @author macq
	 * @explain： 游戏最近排行榜
	 * @datetime:2007-3-27 11:39:21
	 * @return void
	 */
	public void top() {
		int count = SqlUtil.getIntResult(
				"select count(id) from dhh_user where recent_score> "
						+ dhhUser.getRecentHighScore(), Constants.DBShortName);
		request.setAttribute("count", Integer.valueOf(count + 1));

		// 该查询在impl中启用缓存请注意，更新时间为1小时
		Vector fsUserList = getService().getUserList(
				" recent_score>0 order by recent_score desc limit 100");
		int totalCount = fsUserList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		
		PagingBean page = new PagingBean(pageIndex, totalCount, NUMBER_PER_PAGE);
		request.setAttribute("page", page);
		
		// 取得要显示的消息列表
		int start = page.getCurrentPageIndex() * NUMBER_PER_PAGE;
		int end = Math.min(start + NUMBER_PER_PAGE, totalCount);
		List fsUserList1 = fsUserList.subList(start, end);
		request.setAttribute("fsUserList", fsUserList1);
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行页面
	 * @datetime:2007-3-27 10:19:01
	 * @return void
	 */
	public void bank() {
		session.setAttribute("fsBank", "true");
	}

	/**
	 * 
	 * @author macq
	 * @explain：银行操作结果页面
	 * @datetime:2007-3-27 10:19:04
	 * @return void
	 */
	public void bankResult() {
		session.removeAttribute("fsBank");
		int saveMoney = StringUtil.toInt(request.getParameter("saveMoney"));
		int getMoney = StringUtil.toInt(request.getParameter("getMoney"));
		// 存钱
		if (saveMoney > 0) {
			if (dhhUser.getMoney() < saveMoney) {
				doTip(null, "你没有足够的钱存入银行！");
				return;
			}
			// 减去现金
			dhhUser.setMoney(dhhUser.getMoney() - saveMoney);
			// 增加存款
			dhhUser.setSaving(dhhUser.getSaving() + saveMoney);
			tip("success", "你成功的存入银行" + saveMoney + "￥.");
		}// 取钱
		else if (getMoney > 0) {
			if (dhhUser.getSaving() < getMoney) {
				doTip(null, "你在银行中没有足够的存款不能取钱！");
				return;
			}
			// 减去存款
			dhhUser.setSaving(dhhUser.getSaving() - getMoney);
			// 增加现金
			dhhUser.setMoney(dhhUser.getMoney() + getMoney);
			tip("success", "你成功的从银行取出" + getMoney + "￥.");
		}// 数据有问题
		else {
			// tip(null, "请确认操作方式");
			return;
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加市场动态log
	 * @datetime:2007-3-26 下午04:22:16
	 * @param content
	 * @return
	 */
	public void addLog(String content) {
		List log = getUserCity().getLog();
		synchronized (log) {
			// 判断场景log是否大于50条
			if (log.size() > logSize) {
				log.remove(0);
			}
			log.add(content);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 显示市场动态
	 * @datetime:2007-3-29 19:22:47
	 * @param log
	 * @return String
	 */
	public String toString() {
		List log = (List)getUserCity().getLog();
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
	 * Sysytem.out.println("jlkfyos");
	 * @author macq
	 * @explain：购买货船操作结果页面
	 * @return void
	 */
	public void buyShipResult() {
		session.removeAttribute("fsBank");
		int shipid = StringUtil.toInt(request.getParameter("shipid"));
		DhhShipBean ship = (DhhShipBean)getWorld().shipMap.get(new Integer(shipid));
		// 存钱
		if (dhhUser.getMoney() < ship.getPrice()) {
				tip("failure", "你身上没有足够的钱购买此船只！");
		}// 取钱
		else{
				tip("success", "购买成功！");
				dhhUser.setMoney(dhhUser.getMoney() - ship.getPrice());
				dhhUser.setShip(shipid);
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain：保存游戏页面
	 * @datetime:2007-3-27 10:19:04
	 * @return void
	 */
	public void saveGame() {
		 String message = null;
		// 存钱
		if (dhhUser.getProductMap().keySet().size() > 0) {
			message = "你的货仓中尚有未处理货品,为了避免你的货品发生损失,请清空货仓后再保存游戏!";
		}// 取钱
		else{
			getService().updateUser(
					"ship_id = "+dhhUser.getShip()
					+", city_id = "+dhhUser.getCity()
					+", money = "+dhhUser.getMoney()
					+", saving = "+dhhUser.getSaving()
					+", past_time = "+dhhUser.getPasttime()
					,"user_id = "+dhhUser.getUserId());
			message = "游戏保存成功,别忘了下次继续玩";
		}
		tip(null, message);
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
		HashMap userMap = DHHWorld.getDHHUserMap();
		if (userMap != null) {
			HashMap map = (HashMap) userMap.clone();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Integer id = (Integer) iter.next();
				DhhUserBean bean = (DhhUserBean) map.get(id);
				if (bean == null)
					continue;
				// 判断用户是否下线
				if (bean.userOffline()) {
					// 设置用户为离线
					bean.setOffline(true);
					//保存用户信息
					getService().updateUser(
							"ship_id = "+bean.getShip()
							+", city_id = "+bean.getCity()
							+", money = "+bean.getMoney()
							+", saving = "+bean.getSaving()
							+", past_time = "+bean.getPasttime()
							,"user_id = "+bean.getUserId());
					// 删除map中用户的信息
					DHHWorld.getDHHUserMap()
							.remove(new Integer(bean.getUserId()));
				}

			}
		}
	}
	
	public void result() {
		if(!dhhUser.isGameOver())
			dhhUser.setGameStatus(DhhUserBean.STATUS_END);
		
		if (dhhUser.isGameOver()) {
			synchronized (dhhUser) {
				if (!dhhUser.isRewarded()) {
					dhhUser.setRewarded(true);

					finishGame();
				}
				tip("success");
			}
		}
	}

	/**
	 * @return 当前玩家
	 */
	public DhhUserBean getDhhUser() {
		return dhhUser;
	}
	
	/**
	 * @return 当前玩家所在的港口
	 */
	public DhhCityBean getUserCity() {
		return (DhhCityBean)getWorld().cityMap.get(new Integer(dhhUser.getCity()));
	}
	
	/**
	 * @return 当前玩家的船只
	 */
	public DhhShipBean getUserShip() {
		return (DhhShipBean)getWorld().shipMap.get(new Integer(dhhUser.getShip()));
	}
	
	/**
	 * @return 某个产品在当前港口的价格
	 */
	public int getProductPrice(UserBagBean pro) {
		DhhCityBean city = getUserCity();
		return getWorld().getProductPrice(city, pro);
	}
	
	public DhhCitProBean getCityProduct(int productId) {
		DhhCityBean city = getUserCity();
		return (DhhCitProBean)city.getProductMap().get(new Integer(productId));
	}
}
