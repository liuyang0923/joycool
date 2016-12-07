package jc.guest.fish;

import javax.servlet.http.HttpServletRequest;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;

import net.joycool.wap.action.job.fish.AreaBean;
import net.joycool.wap.action.job.fish.FishBean;
import net.joycool.wap.action.job.fish.FishUserBean;
import net.joycool.wap.action.job.fish.PullEventBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 钓鱼
 * @datetime:2007-4-23 10:10
 */
public class FishAction extends CustomAction{
	public static String URL_PREFIX = "/guest/fish";
	public static long USER_INACTIVE = 30 * 1000 * 60;
	
	public static String FISH_USER_KEY = "fish_user2";
	int NUMBER_PER_PAGE = 10;

	public static String title = "钓鱼";

	GuestUserInfo guestUser = null;

	public static int logSize = 10;

	FishUserBean fishUser = null;

	static FishService service = new FishService();


	public FishAction(HttpServletRequest request) {
		super(request);
		guestUser = (GuestUserInfo)session.getAttribute(GuestHallAction.GUEST_KEY);
		if(guestUser != null) {
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
	
	/**
	 *  钓鱼上钩页面
	 */
	public void getFish() {
		int pullId = StringUtil.toInt(request.getParameter("pullId"));
		FishBean fish = fishUser.getFish();
		AreaBean area = fishUser.getArea();
		if(fish != null) {
			String nickname = StringUtil.toWml(guestUser.getUserName());
			if(pullId == fish.getPullId()) {
				int money = rewardMoney(fish.getPrice());
				addLog(nickname + "钓到" + fish.getName() + "啦，真好运气。");
				fishUser.addLog("钓到" + fish.getName() + "啦，真好运气。");
				request.setAttribute("fish", fish);
				tip("get", "你成功钓到了" + fish.getName() + "，折价" + money + "游币!");
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
		Integer key = new Integer(guestUser.getId());
		FishUserBean mapFishUser = (FishUserBean) world.getUserMap().get(key);
		if (fishUser == null) {
			if (mapFishUser == null) {
				fishUser = service.getUser("user_id=" + guestUser.getId());
				// 判断是否为新用户
				if (fishUser == null) {
					fishUser = new FishUserBean();
					fishUser.setUserId(guestUser.getId());
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
			GuestHallAction.addPoint(guestUser, 1);
	}

	// 奖励游币
	private int rewardMoney(int point) {
		if(point > 0 && point < 10000){
			GuestHallAction.updateMoney(guestUser.getId(), 1);
			return 1;
		}else if(point >= 10000 && point < 100000){
			GuestHallAction.updateMoney(guestUser.getId(), 2);
			return 2;
		}else if(point >= 100000){
			GuestHallAction.updateMoney(guestUser.getId(), 3);
			return 3;
		}
		return 0;
	}
}
