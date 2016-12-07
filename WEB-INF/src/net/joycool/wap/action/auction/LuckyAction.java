package net.joycool.wap.action.auction;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.PageUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class LuckyAction extends CustomAction{
	
	public static int LUCK_PLAY_GAMEPOINT = 300000000;
	public static int LUCK_REWARD_GAMEPOINT = 100000000;
	
	private UserBean loginUser = null;

	private static IUserService userService = ServiceFactory.createUserService();

	private static IDummyService dummyService = ServiceFactory.createDummyService();

	public static SimpleGameLog log = new SimpleGameLog();

	public static int rewards[] = {0, 12, 13, 14, 15, 16, 17, 25, 26, 
		42, 43, 44, 46, 
		48, 49, 50, 51, 52, 53, 54, 55,
		70, 71, 72, 75, 76, 114,115,117,118,119,121,122,125,126,127};		// 对应的奖品id，就是dummy id，0表示奖励乐币
	public static int rewardsRandomRate[] = {1500, 60, 10, 4, 50, 50, 1, 5, 5,
		0, 4, 4, 3, 
		30, 30, 30, 30, 30, 30, 5, 20,
		60, 16, 10, 50, 18, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};		// 各种奖品的概率
	public static int rewardsRandomRate2[] = {800, 20, 0, 0, 5, 5, 0, 0, 0,
		0, 0, 0, 0, 
		3, 3, 3, 3, 3, 3, 2, 2,
		30, 10, 1, 10, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};		// 低级用户各种奖品的概率
	
	public static int pickup[] = {75, 75, 75, 76, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149};	
	public static int pickupPlace[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};		// 各地当前能拣的东西
	
	public static int rewardsRandomTotal;
	public static int rewardsRandomTotal2;
	static {
		rewardsRandomTotal = RandomUtil.sumRate(rewardsRandomRate);
		rewardsRandomTotal2 = RandomUtil.sumRate(rewardsRandomRate2);
	}

	public LuckyAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}

	public static IUserService getUserService() {
		return userService;
	}

	public void play() {
		
	}
	
	public static int MAX_GOOD = 10;
	
	public void playResult() {
		boolean good = false;
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser.getId());
		if(UserInfoUtil.isAddUserInterval(0, loginUser.getId())) {		// 已经参加过一次，要给钱！
			if(!hasParam("f")) {		// 不带参数的回到上一个页面
				tip("return");
				return;
			}
			
			if(userStatus.getGamePoint() < LUCK_PLAY_GAMEPOINT) {
				tip(null, "身上没有足够的乐币！");
				return;
			}
			UserInfoUtil.updateUserCash(loginUser.getId(), -LUCK_PLAY_GAMEPOINT,
					12, "幸运转盘扣" + LUCK_PLAY_GAMEPOINT + "乐币");
			good = true;
		} else {		// 免费
			int count = SqlUtil.getIntResult("select play_count from mcoolgame.lucky where user_id=" + loginUser.getId(), 5);
			if(userStatus.getRank() > 16) {
				if(count < MAX_GOOD)
					good = true;
				if(count == -1)
					SqlUtil.executeUpdate("insert into mcoolgame.lucky set user_id=" + loginUser.getId() + ",play_count=1,last_play_time=now()", 5);
				else
					SqlUtil.executeUpdate("update mcoolgame.lucky set play_count=play_count+1,last_play_time=now() where user_id=" + loginUser.getId(), 5);
			}
		}
		int reward = 0;
		if(good)
			reward = rewards[RandomUtil.randomRateInt(rewardsRandomRate, rewardsRandomTotal)];
		else
			reward = rewards[RandomUtil.randomRateInt(rewardsRandomRate2, rewardsRandomTotal2)];
		if(reward == 0) {		// 奖励乐币
			int rewardMoney = LUCK_REWARD_GAMEPOINT;
			if(!good || RandomUtil.percentRandom(90))
				rewardMoney = 10000;
			UserInfoUtil.updateUserCash(loginUser.getId(), rewardMoney,
					12, "幸运转盘赢" + rewardMoney + "乐币");
			setAttribute("rewardMoney", rewardMoney);
		} else {				// 奖励卡片
			UserBagCacheUtil.addUserBagCache(loginUser.getId(), reward);
			DummyProductBean dummyProduct = dummyService.getDummyProducts(reward);
			setAttribute("rewardItem", dummyProduct);
			
			log.add(StringUtil.toWml("恭喜" + loginUser.getNickName()) + "获得了" + dummyProduct.getName());
		}
		setAttribute("reward", reward);
		tip("success");
	}

	/**
	 * @return Returns the loginUser.
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}
	
	// 本页面是否有东西可以拣
	public static String viewPickup(int pos, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		if(pickupPlace[pos] > 0) {
			request.getSession().setAttribute("pickup", Integer.valueOf(pos));
			StringBuilder sb = new StringBuilder();
			sb.append("**地上好像有东西，");
			sb.append("<a href=\"");
			sb.append(("/user/pickup.jsp?back="));
			sb.append(URLEncoder.encode(PageUtil.getCurrentPageURL(request), "utf-8"));
			sb.append("\">拣起来看看</a><br/>");
			return sb.toString();
		} else
			return "";
	}
	
	// 拣起来东西
	public void pickup() {
		Integer ipos = (Integer)session.getAttribute("pickup");
		if(ipos != null) {
			int pos = ipos.intValue();
			if(pickupPlace[pos] > 0) {
				DummyProductBean item = dummyService.getDummyProducts(pickupPlace[pos]);
				if(item != null) {
					int itemId = pickupPlace[pos];
					pickupPlace[pos] = 0;
					UserBagCacheUtil.addUserBagCache(loginUser.getId(), itemId);
					tip("success", "你拣到了一个" + item.getName());
					session.removeAttribute("pickup");
				}
			}
		}
		if(!isResult("success")) {
			tip("success", "被别人抢先一步拣走了……");
		}
	}
	
	// 地上长出来东西啦
	public static void addPickup() {
		int pos = RandomUtil.nextInt(pickupPlace.length);
		pickupPlace[pos] = pickup[RandomUtil.nextInt(pickup.length)];
	}

	/**
	 * @return Returns the dummyService.
	 */
	public static IDummyService getDummyService() {
		return dummyService;
	}
}
