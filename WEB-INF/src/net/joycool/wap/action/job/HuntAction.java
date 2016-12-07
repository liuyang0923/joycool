package net.joycool.wap.action.job;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntUserQuarryBean;
import net.joycool.wap.bean.job.HuntUserWeaponBean;
import net.joycool.wap.bean.job.HuntWeaponBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.JobServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.CountMap;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class HuntAction extends CustomAction{

	public static int DEALFAILURE = 3;

	public static int WRONGNUM = 2;

	public static int DEALOK = 1;
	
	public static int AWP_ID = 5;
	
	public static int HUNT_AWP_TYPE = 4;

	static UserServiceImpl userService = new UserServiceImpl();

	static JobServiceImpl jobService = new JobServiceImpl();
	
	static ICacheMap huntCache = CacheManage.hunt;
	
	public static CountMap countMap = new CountMap();

	UserBean loginUser;

	public HuntAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();;
	}

	/**
	 * 删除用户过期的武器（jc_hunt_user_weapon)
	 */
	public void doDeleteUserExpireWeapon() {

		String condition = " subdate(now(),INTERVAL "
				+ Constants.JOB_HUNT_EXPIRE_TIME + " HOUR) >=create_datetime"
				+ " and user_id=" + loginUser.getId();

		/*
		 * if (jobService.getHuntUserWeaponCount(condition) == 0){
		 * jobService.deleteHuntUserWeapon(condition); return false; }
		 */
		jobService.deleteHuntUserWeapon(condition);
	}

	/**
	 * 查找用户已买武器（jc_hunt_user_weapon)
	 * 
	 * @param condition
	 * @return
	 */
	public HuntUserWeaponBean doGetHuntUserWeapon() {
		return doGetHuntUserWeapon(loginUser.getId());
	}
	static HuntUserWeaponBean nullUserWeapon = new HuntUserWeaponBean();
	public static HuntUserWeaponBean doGetHuntUserWeapon(int userId) {
		Integer key = new Integer(userId);
		synchronized(huntCache) {
			HuntUserWeaponBean huw = (HuntUserWeaponBean)huntCache.get(key);
			if(huw == null) {
				huw = jobService.getHuntUserWeapon(" a.weapon_id=b.id and a.user_id="
					+ userId);
				if(huw != null)
					huntCache.put(key, huw);
				else {
					huntCache.put(key, nullUserWeapon);		// 没有武器就放入nullweapon，以免重复查询数据库
					return null;
				}
			} else if(huw.getWeaponId() == 0)		// 武器已经过期或者没有选择武器
				return null;
			if(huw != null && huw.getExpireDatetime() < System.currentTimeMillis()) {
				huw.setWeaponId(0);
				jobService.deleteHuntUserWeapon("id=" + huw.getId());
				return null;
			}
			return huw;
		}
	}

	/**
	 * 查找用户已买的可用武器（jc_hunt_user_weapon)
	 * 
	 * @return
	 */
	public HuntUserWeaponBean getUsableHuntUserWeapon() {
		return doGetHuntUserWeapon();
	}

	/**
	 * 查找用户可买武器（jc_hunt_weapon)
	 * 
	 * @return
	 */
	public Vector doGetHuntWeaponList() {
		Vector weaponList = new Vector();
		HashMap weaponMap = LoadResource.getWeaponMap();
		HuntUserWeaponBean saledUserWeapon = doGetHuntUserWeapon();
		Set keys = weaponMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			HuntWeaponBean weaponBean = (HuntWeaponBean) weaponMap.get(it
					.next());
			// 已买武器
			if (saledUserWeapon != null) {
				if (saledUserWeapon.getWeaponId() == weaponBean.getId()) {
					// 去掉已买武器
					continue;
				}
			}
			weaponList.add(weaponBean);
		}
		return weaponList;
	}

	/**
	 * 检测用户是否有足够的乐币买该武器
	 * 
	 * @param weaponId
	 * @return
	 */
	public String checkUserPoint(int weaponId) {
		HuntWeaponBean hw = getWeapon(weaponId);
		if(hw == null) {
			return "不存在的武器";
		}
		int weaponPrice = hw.getPrice();
		if (!haveEnoughMoney(weaponPrice)) {
			// 乐币不足
			return "您的乐币不足以购买该武器！";
		}
		return "";
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：
	 * @datetime:2007-6-28 5:43:32
	 * @param weaponId
	 * @return
	 * @return String
	 */
	public String checkUserBag(int userBagId) {
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		if (userBag==null) {
			return "您没有此道具！";
		}else if(userBag.getUserId()!=loginUser.getId()){
			return "您没有此道具！";
		}else if(userBag.getProductId()!= 12){
			return "您没有特殊股道具！";
		}
		return "";
	}

	/**
	 * 判断用户的钱是否足够
	 * 
	 * @param money
	 * @return true or false
	 */
	public boolean haveEnoughMoney(int money) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// int currentMoney = loginUser.getUs().getGamePoint();
		int currentMoney = usb.getGamePoint();
		if (currentMoney >= money) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 扣用户乐币从user_status
	 * 
	 * @param money
	 */
	public void deductUserMoney(int money) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// int currentMoney = loginUser.getUs().getGamePoint();
		int currentMoney = usb.getGamePoint();
		int num = currentMoney;
		if (haveEnoughMoney(money)) {
			num = money;
		}
		// zhul_2006-08-11 modify userstatus start
		UserInfoUtil.updateUserCash(loginUser.getId(), -money, UserCashAction.HUNT, "打猎中扣钱"+(num%10==0?num+"":num+"余额为0."));
		// zhul_2006-08-11 modify userstatus end

		// add by zhangyi 2006-07-24 for stat user money history start
		MoneyAction.addMoneyFlowRecord(Constants.HUNT, num,
				Constants.SUBTRATION, loginUser.getId());
		// add by zhangyi 2006-07-24 for stat user money history end
	}

	/**
	 * 插入用户购买武器到用户的武器状态表（jc_hunt_user_weapon)
	 * 
	 * @param money
	 */
	public void doAddHuntUserWeapon(HuntWeaponBean hw) {

		HuntUserWeaponBean userWeapon = new HuntUserWeaponBean();
		userWeapon.setUserId(loginUser.getId());
		userWeapon.setWeaponId(hw.getId());
		userWeapon.setName(hw.getName());
		
		jobService.addHuntUserWeapon(userWeapon);
		userWeapon.setCreateDatetime(System.currentTimeMillis());
		userWeapon.setExpireDatetime(userWeapon.getCreateDatetime() + 3600000l * Constants.JOB_HUNT_EXPIRE_TIME);

		huntCache.put(new Integer(loginUser.getId()), userWeapon);
	}

	/**
	 * 更改用户的武器状态表中用户武器（jc_hunt_user_weapon)
	 * 
	 * @param money
	 */
	private void doUpdateHuntUserWeapon(HuntWeaponBean hw) {
		String set = " weapon_id="
				+ hw.getId()
				+ ",create_datetime=now(),expire_datetime=adddate(now(),INTERVAL "
				+ Constants.JOB_HUNT_EXPIRE_TIME + " HOUR) ";
		String condition = " user_id=" + loginUser.getId();
		jobService.updateHuntUserWeapon(set, condition);
	}

	/**
	 * 处理用户武器状态表
	 * 
	 * @param weaponId
	 */
	public void dealHuntUserWeapon(int weaponId,HttpServletRequest request) {
		HuntWeaponBean hw = getWeapon(weaponId);
		if(hw == null)
			return;
		//macq_判断是否为特殊武器,后更新特殊武器使用次数
		if(weaponId==AWP_ID){
			int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
			UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(), userBagId);
		}
		HuntUserWeaponBean userWeapon = doGetHuntUserWeapon();
		if (userWeapon == null) {
			// 先扣去武器费用
			buyWeapon(weaponId);
			// 插入用户购买武器到用户的武器状态表
			doAddHuntUserWeapon(hw);
		} else {
			if (userWeapon.getWeaponId() != weaponId) {
				// 先扣去武器费用
				buyWeapon(weaponId);
				doUpdateHuntUserWeapon(hw);
				userWeapon.setWeaponId(weaponId);
				userWeapon.setName(hw.getName());
				userWeapon.setCreateDatetime(System.currentTimeMillis());
				userWeapon.setExpireDatetime(userWeapon.getCreateDatetime() + 3600000l * Constants.JOB_HUNT_EXPIRE_TIME);
			}
		}
	}

	/**
	 * 通过weaponId 取得 weapon
	 * 
	 * @param weaponId
	 * @return
	 */
	public HuntWeaponBean getWeapon(int weaponId) {
		HashMap weaponMap = LoadResource.getWeaponMap();
		return (HuntWeaponBean) weaponMap.get(new Integer(weaponId));
	}

	/**
	 * 通过quarryId 取得 quarry
	 * 
	 * @param quarryId
	 * @return
	 */
	public HuntQuarryBean getQuarry(int quarryId) {
		HashMap quarryMap = LoadResource.getQuarryMap();
		return (HuntQuarryBean) quarryMap.get(new Integer(quarryId));
	}

	/**
	 * 插入用户打到的猎物到用户的猎物表（jc_hunt_user_quarry)
	 * 
	 * @param money
	 */
	public void doAddHuntUserQuarry(int quarryId) {
		HuntUserQuarryBean userQuarryBean = new HuntUserQuarryBean();
		userQuarryBean.setUserId(loginUser.getId());
		userQuarryBean.setQuarryId(quarryId);
		HuntUserQuarryBean uqb = jobService.getHuntUserQuarry(" user_id="
				+ loginUser.getId() + " and quarry_id=" + quarryId);
		if (uqb == null) {
			jobService.addHuntUserQuarry(userQuarryBean);
		} else {
			jobService.updateHuntUserQuarry(" quarry_count=quarry_count+1",
					" user_id=" + loginUser.getId() + " and quarry_id="
							+ quarryId);
		}
	}

	/**
	 * 被猎物咬伤后扣除用户乐币
	 * 
	 * @param quarryId
	 */
	public void deductUserMoneyForHarm(int quarryId) {
		int harmPrice = getQuarry(quarryId).getHarmPrice();

		deductUserMoney(harmPrice);
	}

	/**
	 * 打猎给用户添加经验值
	 * 
	 * @param quarryId
	 * @param huntResult
	 */
	public int addUserFrankForHunt(int quarryId, int huntResult) {
		int point = 0;
		int hitPoint = getQuarry(quarryId).getHitPoint();
		if (huntResult == Constants.HIT) {
			// 打中×1
			point = hitPoint * Constants.JOB_HUNT_POINT_CG;
		} else if (huntResult == Constants.NOHIT) {
			// 没打到×0.3
			point = (int) (hitPoint * Constants.JOB_HUNT_POINT_SB);
		} else if (huntResult == Constants.HARM) {
			// 被咬伤×0.7
			point = (int) (hitPoint * Constants.JOB_HUNT_POINT_YS);
		}

		RankAction.addPoint(loginUser, point);
		return point;
	}

	// zhul_2006-07-18 调用此方法请输入你的武器id，返回武器射击的结果，返回1为击中，2为没击中，3为被猎物咬伤 start
	public int getHuntResult(int weaponId) {
		// 取得该武器的击中率
		HashMap weaponMap = LoadResource.getWeaponMap();
		HuntWeaponBean huntWeapon = (HuntWeaponBean) weaponMap.get(new Integer(
				weaponId));
		// 计算武器击中结果
		int random = RandomUtil.nextInt(100) + 1;
		int hitArea = huntWeapon.getHitRate();
		int nohitArea = huntWeapon.getHitRate() + huntWeapon.getNotHitRate();
		if (random <= hitArea)
			return Constants.HIT;
		else if (random > hitArea && random <= nohitArea)
			return Constants.NOHIT;
		else
			return Constants.HARM;
	}

	// zhul_2006-07-18 调用此方法请输入你的武器id，返回武器射击的结果，返回1为击中，2为没击中，3为被猎物咬伤 end

	// zhul_2006-07-18 调用此方法请输入你的武器ID，返回对应此武器可能会出现的猎物。start
	public HuntQuarryBean getQuarryByWeapon(int weaponId) {
		// 如果输入weaponId不在现有武器范围内，抛出没有这种武器的异常
		try {
			if (weaponId < Constants.ARROW || weaponId > Constants.AWP)
				throw new Exception("没有这种武器！");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 取得现有动物对应表
		HashMap quarryMap = LoadResource.getQuarryMap();
		// 根据武器得到此武器对应的各种动物出现的机率表
		TreeMap rateMap = null;
		switch (weaponId) {
		case Constants.ARROW:
			rateMap = LoadResource.getArrowMap();
			break;
		case Constants.HANDGUN:
			rateMap = LoadResource.getHandGunMap();
			break;
		case Constants.HUNTGUN:
			rateMap = LoadResource.getHuntGunMap();
			break;
		case Constants.AK47:
			rateMap = LoadResource.getAk47Map();
			break;
		case Constants.AWP:
			rateMap = LoadResource.getAWPMap();
			break;
		}

		// 获得对于此武器将会随机出现的动物
		int base = ((Integer) rateMap.get(new Integer(Constants.RANDOM_BASE)))
				.intValue();
		int random = RandomUtil.nextInt(base) + 1;
		Set keys = rateMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			if (key.intValue() >= random) {
				HuntQuarryBean quarry = (HuntQuarryBean) quarryMap
						.get((Integer) rateMap.get(key));
				return quarry;
			}
		}
		return null;
	}

	// zhul_2006-07-18 调用此方法请输入你的武器ID，返回对应此武器可能会出现的猎物。end

	// zhul_2006-07-18 买子弹 start
	public boolean buyBall(int weaponId) {
		HashMap weaponMap = LoadResource.getWeaponMap();
		HuntWeaponBean weapon = (HuntWeaponBean) weaponMap.get(new Integer(
				weaponId));
		boolean ok = this.haveEnoughMoney(weapon.getShotPrice());
		if (ok) {
			this.deductUserMoney(weapon.getShotPrice());
			return true;
		}
		return false;
	}

	// zhul_2006-07-18 买子弹 end

	// zhul_2006-07-18 买武器 start
	public boolean buyWeapon(int weaponId) {
		HashMap weaponMap = LoadResource.getWeaponMap();
		HuntWeaponBean weapon = (HuntWeaponBean) weaponMap.get(new Integer(
				weaponId));
		boolean ok = this.haveEnoughMoney(weapon.getPrice());
		if (ok) {
			this.deductUserMoney(weapon.getPrice());
			return true;
		}
		return false;
	}

	// zhul_2006-07-18 买武器 end

	// 显示猎物
	public void quarryList(HttpServletRequest request) {

		// 分页
		int NUM_PER_PAGE = 10;
		int totalCount = jobService.getHuntUserQuarryCount("user_id="
				+ loginUser.getId());
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String condition = " user_id=" + loginUser.getId()
				+ " ORDER BY quarry_id  LIMIT " + pageIndex * NUM_PER_PAGE
				+ "," + NUM_PER_PAGE;
		Vector quarryList = jobService.getHuntUserQuarryList(condition);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("quarryList", quarryList);
		return;

	}
	
	/**
	 *  
	 * @author macq
	 * @explain： 出售全部猎物
	 * @datetime:2007-6-8 2:03:46
	 * @param request
	 * @return void
	 */
	public void saleTotal(HttpServletRequest request) {
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		//获取用户全部猎物
		Vector userQuarryList = jobService.getHuntUserQuarryList("user_id="+ loginUser.getId());
		if(userQuarryList==null){
			request.setAttribute("tip","对不起，您现在没有猎物可卖呀，嘿嘿。");
			request.setAttribute("result","failure");
			return;
		}
		long totalMoney = 0;
		//得到猎物信息
		HashMap quarryMap = LoadResource.getQuarryMap();
		for (int i = 0; i < userQuarryList.size(); i++) {
			HuntUserQuarryBean userQuarry = (HuntUserQuarryBean)userQuarryList.get(i);
			int count = userQuarry.getQuarryCount();
			HuntQuarryBean quarry = (HuntQuarryBean) quarryMap.get(new Integer(
					userQuarry.getQuarryId()));
			int money = quarry.getPrice()*count;
			totalMoney=totalMoney+money;
		}
		int gamePoint = usb.getGamePoint();
		if (Constants.SYS_MAX_INT - totalMoney < gamePoint) {
			totalMoney = Constants.SYS_MAX_INT - gamePoint;
		}
		jobService.deleteHuntUserQuarry("user_id="+ loginUser.getId());
		UserInfoUtil.updateUserCash(loginUser.getId(), totalMoney, UserCashAction.HUNT, "出售全部卖猎物加钱"+totalMoney+"乐币");
		request.setAttribute("tip","您的全部猎物已卖出，获得"+totalMoney+"乐币!现有乐币"+(gamePoint+totalMoney));
		request.setAttribute("result","failure");
		return;
	}

	// 卖猎物
	public int saleQuarry(HttpServletRequest request) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		String sales = request.getParameter("sales");
		int quarryId = getParameterInt("quarryId");
		String deal = (String) request.getSession().getAttribute("deal");
		int saleNum = StringUtil.toInt(sales);
		// 如果deal为空，说明不是一次正常的交易
		if (deal == null) {
			return HuntAction.DEALFAILURE;
		} else {
			request.getSession().removeAttribute("deal");
		}

		// 判断如果用户所拥有的猎物及数量是否属实
		HuntUserQuarryBean userQuarry = jobService
				.getHuntUserQuarry("quarry_id=" + quarryId + " and user_id="
						+ loginUser.getId());
		if (userQuarry == null || saleNum < 0
				|| userQuarry.getQuarryCount() < saleNum) {
			return HuntAction.WRONGNUM;
		}

		// 得到猎物信息
		HashMap quarryMap = LoadResource.getQuarryMap();
		HuntQuarryBean quarry = (HuntQuarryBean) quarryMap.get(new Integer(quarryId));
		// 从数据库中减值
		jobService.updateHuntUserQuarry("quarry_count=quarry_count-" + saleNum,
				"user_id=" + loginUser.getId() + " and quarry_id="
						+ quarry.getId());
		jobService.deleteHuntUserQuarry("quarry_count=0 and user_id="
				+ loginUser.getId());
		// 给用户加钱
		int money = quarry.getPrice() * saleNum;
		CardAction cardAction = new CardAction();
		// money = cardAction.getNotOutOfNum(loginUser.getUs().getGamePoint(),
		// money);
		money = cardAction.getNotOutOfNum(usb.getGamePoint(), money);
		// zhul_2006-08-11 modify userstatus start
		UserInfoUtil.updateUserCash(loginUser.getId(), money, UserCashAction.HUNT, "卖猎物加钱"+money+"乐币");
		// zhul_2006-08-11 modify userstatus end

		// loginUser.getUs()
		// .setGamePoint(loginUser.getUs().getGamePoint() + money);
		// add by zhangyi 2006-07-24 for stat user money history start
		MoneyAction.addMoneyFlowRecord(Constants.HUNT, money, Constants.PLUS,
				loginUser.getId());
		// add by zhangyi 2006-07-24 for stat user money history end
		request.setAttribute("quarry", quarry.getName());
		request.setAttribute("sales", sales);
		request.setAttribute("money", money + "");
		return HuntAction.DEALOK;
	}

	/**
	 * zhul_2006-08-21 delete hunt_quarry
	 */
	public void deleteQuarry(HttpServletRequest request) {
		int quarryId = getParameterInt("quarryId");
		if (quarryId <= 0) {
			return;
		}
		HashMap quarryMap = LoadResource.getQuarryMap();
		HuntQuarryBean quarry = (HuntQuarryBean) quarryMap.get(new Integer(quarryId));
		if (quarry != null) {
			jobService.deleteHuntQuarry("id=" + quarryId);
			jobService.deleteHuntQuarryAppearRate("quarry_id=" + quarryId);
			File file = new File(Constants.HUNT_IMAGE_PATH + "/"
					+ quarry.getImage());
			file.delete();
		}else
		{
			request.setAttribute("tip", "猎物不存在!");	
			return;
		}
		// 更新打猎内存
		LoadResource resource = new LoadResource();
		resource.clearArrowMap();
		resource.clearHandGunMap();
		resource.clearHuntGunMap();
		resource.clearAk47Map();
		resource.clearQuarryMap();
		resource.clearWeaponMap();

		request.setAttribute("tip", "猎物" + quarry.getName() + "已经被删除!");

	}

	/**
	 * zhul_2006-08-21 modify quarry info
	 */
	public void alterQuarry(HttpServletRequest request) {
		String name = request.getParameter("name");
		int quarryId = getParameterInt("quarryId");
		String price = request.getParameter("price");
		String harmPrice = request.getParameter("harmPrice");
		String hitPoint = request.getParameter("hitPoint");

		String tip = null;
		/**
		 * 检查参数，如果信息格式有误直接跳回用户输入页面，并提示用户！
		 */
		if (name == null || name.equals("")) {
			tip = "猎物名称不能为空！";
			request.setAttribute("tip", tip);
			return;
		} else if (hitPoint.indexOf('-') != -1 && hitPoint.lastIndexOf('-') != 0) {
			tip = "您输入的打中经验值有误!";
			request.setAttribute("tip", tip);
			return;
		}
		// 判断此猎物是否已经存在
		HuntQuarryBean quarry = null;
		quarry = jobService.getHuntQuarry("name='" + StringUtil.toSql(name) + "' and id!="
				+ quarryId);
		if (quarry != null) {
			tip = "您输入的猎物已经存在!";
			request.setAttribute("tip", tip);
			return;
		}
		// 更新猎物信息
		jobService.updateHuntQuarry("name='" + StringUtil.toSql(name) + "', price=" + price
				+ ", harm_price=" + harmPrice + ", hit_point=" + hitPoint,
				"id=" + quarryId);
		// 更新内存
		LoadResource resource = new LoadResource();
		resource.clearQuarryMap();
		request.setAttribute("tip", "更新成功!");
	}

	/**
	 * zhul_2006-08-21 modify quarry appear rate
	 */
	public void updateQuarryRate(HttpServletRequest request) {
		int quarryId = getParameterInt("quarryId");
		int arrow = StringUtil.toInt(request.getParameter("arrow"));
		int handGun = StringUtil.toInt(request.getParameter("handGun"));
		int huntGun = StringUtil.toInt(request.getParameter("huntGun"));
		int ak47 = StringUtil.toInt(request.getParameter("ak47"));
		int awp = StringUtil.toInt(request.getParameter("awp"));
		if (quarryId <= 0)
			return;
		if (arrow < 0) {
			request.setAttribute("tip", "参数不正确!");
			return;
		} else if (handGun < 0) {
			request.setAttribute("tip", "参数不正确!");
			return;
		} else if (huntGun < 0) {
			request.setAttribute("tip", "参数不正确!");
			return;
		} else if (ak47 < 0) {
			request.setAttribute("tip", "参数不正确!");
			return;
		}else if (awp < 0) {
			request.setAttribute("tip", "参数不正确!");
			return;
		}
		// 更新猎物出现机率
		jobService.updateHuntQuarryAppearRate("appear_rate=" + arrow,
				"quarry_id=" + quarryId + " and weapon_id=" + Constants.ARROW);
		jobService
				.updateHuntQuarryAppearRate("appear_rate=" + handGun,
						"quarry_id=" + quarryId + " and weapon_id="
								+ Constants.HANDGUN);
		jobService
				.updateHuntQuarryAppearRate("appear_rate=" + huntGun,
						"quarry_id=" + quarryId + " and weapon_id="
								+ Constants.HUNTGUN);
		jobService.updateHuntQuarryAppearRate("appear_rate=" + ak47,
				"quarry_id=" + quarryId + " and weapon_id=" + Constants.AK47);
		jobService.updateHuntQuarryAppearRate("appear_rate=" + awp,
				"quarry_id=" + quarryId + " and weapon_id=" + Constants.AWP);
		// 更新内存
		LoadResource resource = new LoadResource();
		resource.clearAk47Map();
		resource.clearArrowMap();
		resource.clearHandGunMap();
		resource.clearHuntGunMap();
		resource.clearAWPMap();
		request.setAttribute("tip", "猎物机率更新成功!");
	}
}
