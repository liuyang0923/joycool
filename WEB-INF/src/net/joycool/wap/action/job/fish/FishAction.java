package net.joycool.wap.action.job.fish;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.CountMap;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author zhouj
 * @explain： 钓鱼
 * @datetime:2007-4-23 10:10
 */
public class FishAction extends CustomAction{
	public static String URL_PREFIX = "/job/fish";
	public static long USER_INACTIVE = 30 * 1000 * 60;
	
	public static int oldUserId = 4213122;
	public static int oldUserRate = 120;
	public static int newUserRate = 120;
	
	public static String FISH_USER_KEY = "fish_user_key";
	int NUMBER_PER_PAGE = 10;

	public static String title = "钓鱼";

	UserBean loginUser = null;

	public static int logSize = 10;

	FishUserBean fishUser = null;

	static FishService service = new FishService();
	public static CountMap countMap = new CountMap();

	public FishAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		if(loginUser != null) {
			check();
			fishUser.setActionTime(System.currentTimeMillis());
		}
	}

	public static FishService getService() {
		return service;
	}
	
	public static FishWorld getWorld() {
		return FishWorld.world;
	}

	/**
	 *  索引页面
	 */
	public void index() {
		
	}
	
	/**
	 *  区域列表
	 */
	public void areaList() {
		int areaId = StringUtil.toInt(request.getParameter("areaId"));
		if(areaId > 0) {	// 切换场景
			AreaBean area = (AreaBean)getWorld().areaMap.get(new Integer(areaId));
			if(area != null) {	// 存在这个场景，才给予切换
				fishUser.setArea(area);
				tip("change");
			}
		}
	}

	
	/**
	 *  钓鱼准备页面
	 */
	public void fish() {
		
	}
	
	/**
	 *  查看鱼信息
	 */
	public void fishInfo() {
		int id = StringUtil.toInt(request.getParameter("id"));
		FishWorld world = getWorld();
		FishBean fish = (FishBean)world.fishMap.get(new Integer(id));
		request.setAttribute("fish", fish);
		request.setAttribute("pull", world.getPull(fish));
	}
	
	/**
	 *  钓鱼页面
	 */
	public void waitFish() {
		fishUser.setFish(null);
		AreaBean area = fishUser.getArea();
		if(area != null) {
			if(RandomUtil.percentRandom(fishUser.getArea().getCurRand())) {	// 钓鱼成功
				FishBean fish = (FishBean)RandomUtil.randomObject(area.fishList);
				fishUser.setFish(fish);
				
			}
		}
	}
	
	/**
	 *  钓鱼页面
	 */
	public void doFish() {
		FishBean fish = fishUser.getFish();
		if(fish != null) {
			request.setAttribute("pull", getWorld().getPull(fish));
			tip("catch");
		}
	}
	
	public static int[] card = {35, 48, 49, 50};
	/**
	 *  钓鱼上钩页面
	 */
	public void getFish() {
		int pullId = StringUtil.toInt(request.getParameter("pullId"));
		FishBean fish = fishUser.getFish();
		AreaBean area = fishUser.getArea();
		if(fish != null) {
			String nickname = StringUtil.toWml(loginUser.getNickName());
			if(pullId == fish.getPullId()) {
				int[] count = countMap.getCount(loginUser.getId());
				count[0]++;
				addLog(nickname + "钓到" + fish.getName() + "啦，真好运气。");
				fishUser.addLog("钓到" + fish.getName() + "啦，真好运气。");
				request.setAttribute("fish", fish);
				tip("get", "你成功钓到了" + fish.getName() + "，折价" + fish.getPrice() + "乐币!");
				// 一定概率获得卡片，得到随机以下其中一张
				if(count[0] < 500) {
					rewardMoney(fish.getPrice());
					int rnd;
					if(loginUser.getId() < oldUserId)
						rnd = RandomUtil.nextInt(oldUserRate);
					else
						rnd = RandomUtil.nextInt(newUserRate);
					if(rnd < 4) {
						int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
						if(hour > 7) {
							UserBagCacheUtil.addUserBagCacheNotice(loginUser.getId(), card[rnd], null);
						}
					}
				}
			} else {
				if(RandomUtil.percentRandom(20)) {	// 触发事件
					PullEventBean pullEvent = (PullEventBean)RandomUtil.randomObject(area.pullEventList);
					rewardMoney(pullEvent.getMoney());
					rewardExp(pullEvent.getExp());
					addLog(pullEvent.getLog().replace("%%", nickname));
					fishUser.addLog(pullEvent.getDesc());
					tip("get", pullEvent.getDesc());
					request.setAttribute("pullEvent", pullEvent);
				} else {
					tip("get", "拉竿方式不对，上钩的鱼儿跑掉啦，不懂就向别的鱼友学习一下吧！");
				}
			}
			fishUser.setFish(null);
		}
	}
	
	/**
	 * @author 检查当前的fishuser情况，保证其正确
	 */
	public void check() {
		FishWorld world = getWorld();
		fishUser = (FishUserBean)session.getAttribute(FISH_USER_KEY);
		if(fishUser != null && fishUser.isOffline())
			fishUser = null;
		// 获得map中的用户
		Integer key = new Integer(loginUser.getId());
		FishUserBean mapFishUser = (FishUserBean) world.getUserMap().get(key);
		if (fishUser == null) {
			if (mapFishUser == null) {
				fishUser = service.getUser("user_id=" + loginUser.getId());
				// 判断是否为新用户
				if (fishUser == null) {
					fishUser = new FishUserBean();
					fishUser.setUserId(loginUser.getId());
					// 添加用户
					service.addUser(fishUser);
				}
				world.getUserMap().put(key, fishUser);
			}
			else {
				fishUser = mapFishUser;
			}

		} else {
			if (fishUser.isOffline()) {
				if (mapFishUser != null) {
					fishUser = mapFishUser;
				} else {
					world.getUserMap().put(key, fishUser);
					fishUser.setOffline(false);
				}
			}
		}
		session.setAttribute(FISH_USER_KEY, fishUser);
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
		getWorld().addLog(content);
	}

	public FishUserBean getFishUser() {
		return fishUser;
	}
	
	// 奖励经验值
	private void rewardExp(int point) {
		if(point > 0)
			RankAction.addPoint(loginUser, point);
	}

	// 奖励乐币
	private void rewardMoney(int point) {
		if(point != 0)
			UserInfoUtil.updateUserCash(loginUser.getId(), point,
				11, "钓鱼给用户增加" + point + "乐币");

	}
}
