package net.joycool.wap.action.friend;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.friend.FriendActionBean;
import net.joycool.wap.bean.friend.FriendBadUserBean;
import net.joycool.wap.bean.friend.FriendBagBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.bean.friend.FriendCartoonBean;
import net.joycool.wap.bean.friend.FriendDrinkBean;
import net.joycool.wap.bean.friend.FriendGuestBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.bean.friend.FriendProposalBean;
import net.joycool.wap.bean.friend.FriendReviewBean;
import net.joycool.wap.bean.friend.FriendRingBean;
import net.joycool.wap.bean.friend.FriendUserBean;
import net.joycool.wap.bean.friend.FriendVoteBean;
import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.job.HappyCardSendBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IFriendAdverMessageService;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.shop.ShopAction;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.spec.shop.UserInfoBean;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.NewNoticeCacheUtil;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SmsUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class FriendAction extends CustomAction {
	// liuyi 2007-01-22 生日最短修改间隔(秒)
	public static long birthdayModifyInterval = 86400 * 30;

	// liuyi 2007-01-22 生日贺卡ID
	public static int birthdayCardId = 57;

	final static int NUMBER_PER_PAGE = 5;

	final static int FRIEND_SEARCH_NUMBER_PER_PAGE = 10;

	final static int PAGE_COUNT = 5;

	private HttpSession session = null;

	private UserBean loginUser;

	static IUserService userService = ServiceFactory.createUserService();

	static IFriendService friendService = ServiceFactory.createFriendService();

	static IChatService chatService = ServiceFactory.createChatService();

	static IFriendAdverService friendAdverService = ServiceFactory.createFriendAdverService();

	static IFriendAdverMessageService fmService = ServiceFactory.createFriendAdverMessageService();

	static IHomeService homeService = ServiceFactory.createHomeService();

	static IBankService bankService = ServiceFactory.createBankService();
	
	static INoticeService noticeService = ServiceFactory.createNoticeService();

	public FriendAction(HttpServletRequest request) {
		super(request);

		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
	}

	static public IUserService getUserService() {
		return userService;
	}

	static public IFriendService getFriendService() {
		return friendService;
	}

	static public IChatService getChatService() {
		return chatService;
	}

	static public IFriendAdverService getFriendAdverService() {
		return friendAdverService;
	}

	static public IFriendAdverMessageService getFriendAdverMessageService() {
		return fmService;
	}

	static public IHomeService getHomeService() {
		return homeService;
	}

	static public IBankService getBankService() {
		return bankService;
	}

	// 获取有交友资料的用户,按照同城异性在线前的规则排序
	public Vector getGenderList(int gender, String city) {
		Vector ret = null;
		if (gender == -1) {
			gender = 0;
		}
		if (city == null) {
			city = "";
		} else {
			city = StringUtil.toSql(city);
		}

		// 获取同城用户

		String sql = "SELECT user_id FROM jc_friend where city = '" + StringUtil.toSql(city)
				+ "' and gender=" + gender + " order by create_datetime desc";
		List townList = SqlUtil.getIntListCache(sql, 60);
		if (townList == null) {
			townList = new ArrayList(1);
		}

		// 在线优先
		ret = UserInfoUtil.getUserOrderByOnlineFromCache(townList, 15 * 60);

		// 获取非同城用户

		sql = "SELECT user_id FROM jc_friend where gender=" + gender + " order by create_datetime desc limit 200";
		List noTownList = SqlUtil.getIntListCache(sql, 60);
		if (noTownList == null) {
			noTownList = new ArrayList(1);
		}

		ret.addAll(noTownList);

		return ret;
	}

	/*
	 * 交友中心
	 */
	public void friendCenter(HttpServletRequest request) {
		String result = null;
		String tip = null;
//		if (loginUser.getFriend() == 0) {
//			result = "failure";
//			request.setAttribute("result", result);
//			return;
//		}
		int gender = getParameterIntS("gender");
		if (gender == -1) {
			gender = (loginUser != null && loginUser.getGender() == 0) ? 1 : 0;
		}
		FriendBean friend = null;
		if(loginUser != null)
			friend = getFriendService().getFriend(loginUser.getId());
		String city;
		if (friend == null || friend.getAttachType() == 0) {
			city = "";
		} else {
			city = friend.getCity();
		}
		// 交友广告分页
		int totalCount = getFriendAdverService().getFriendAdverCacheCount(
				"sex!=" + gender);
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
		String prefixUrl = "friendCenter.jsp?gender=" + gender;
		// 取得要显示的消息列表x
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector friendAdverList = getFriendAdverService()
				.getFriendAdverCacheList(
						"sex!=" + gender + " order by id desc limit " + start
								+ ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("friendAdverList", friendAdverList);

		// 获取有交友资料的用户,按照同城异性在线前的规则排序(有男/女2个导航)
		// liuyi 2006-11-29 按在线优先，同城先不管 start
		Vector userIdList = this.getGenderList(gender, city);

		int totalCount1 = userIdList.size();
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 == -1) {
			pageIndex1 = 0;
		}
		int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
		if (totalCount1 % NUMBER_PER_PAGE != 0) {
			totalPageCount1++;
		}
		if (pageIndex1 > totalPageCount1 - 1) {
			pageIndex1 = totalPageCount1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		String prefixUrl1 = "friendCenter.jsp?gender=" + gender;
		// 取得要显示的消息列表x
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex1 = Math.min(start1, totalCount1);
		int endIndex1 = Math.min(start1 + end1, totalCount1);
		List subUserIdList = userIdList.subList(startIndex1, endIndex1);
		List genderList = new Vector();
		for (int i = 0; i < subUserIdList.size(); i++) {
			Integer userId = (Integer) subUserIdList.get(i);
			if (loginUser != null && userId.intValue() == loginUser.getId()) {
				continue;
			}
			genderList.add(friendService.getFriend(userId));
		}

		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("genderList", genderList);
		result = "success";
		request.setAttribute("result", result);
		// liuyi 2006-11-29 按在线优先，同城先不管 end

		request.setAttribute("gender", Integer.valueOf(gender));
		return;
	}

	/**
	 * 
	 * @author macq
	 * @explain：看其他人的个性签名
	 * @datetime:2007-7-26 10:24:26
	 * @param request
	 * @return void
	 */
	public void seeIntroduction(HttpServletRequest request) {
		List list = getOnlineUserList();
		// 获取在线用户个数
		int count = list.size();
		HashMap hm = new HashMap();
		for (int i = 0; i < count; i++) {
			// 获取一个随机在线用户
			int pos = (int) RandomUtil.nextInt(list.size());
			// 判断用户是否为登录用户并且ID不是自己
			int userId = StringUtil.toInt((String) list.get(pos));
			if (userId <= 0 || userId == loginUser.getId()) {
				continue;
			}
			// 获取用户信息
			UserBean user = (UserBean) UserInfoUtil.getUser(userId);
			if (user == null) {
				continue;
			}
			if (user.getSelfIntroduction().startsWith("我是乐客")
					|| user.getSelfIntroduction().trim().equals("")) {
				continue;
			}
			if (hm.get(new Integer(user.getId())) == null) {
				hm.put(new Integer(user.getId()), user);
			}
			// 查看是否已经有5个人
			if (hm.size() >= 5) {
				break;
			}
		}
		request.setAttribute("hm", hm);
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：转换在线用户到list
	 * @datetime:2007-8-7 16:22:24
	 * @return
	 * @return List
	 */
	static byte[] lock = new byte[0];	
	
	public List getOnlineUserList(){
		String key = "seeUserInfo";
		List list = (List) OsCacheUtil.get(key,
				OsCacheUtil.FRIEND_CENTER_SEEINTRODUCTION,
				OsCacheUtil.FRIEND_CENTER_SEEINTRODUCTION_FLUSH_PERIOD);
		if (list == null) {
			synchronized (lock) {
				list = (List) OsCacheUtil.get(key,
						OsCacheUtil.FRIEND_CENTER_SEEINTRODUCTION,
						OsCacheUtil.FRIEND_CENTER_SEEINTRODUCTION_FLUSH_PERIOD);
				if (list == null) {
					// 获取所有在线用户
					Hashtable ht = OnlineUtil.getOnlineHash();
					// 转换数组
					list = new ArrayList(ht.keySet());
					OsCacheUtil.put(key, list,
							OsCacheUtil.FRIEND_CENTER_SEEINTRODUCTION);
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * @author macq
	 * @explain：新用户显示
	 * @datetime:2007-7-26 10:24:26
	 * @param request
	 * @return void
	 */
	public void seeYoungLing(HttpServletRequest request) {
		List list = getOnlineUserList();
		// 获取在线用户个数
		int count = list.size();
		HashMap hm = new HashMap();
		for (int i = 0; i < count; i++) {
			// 获取一个随机在线用户
			int pos = (int) RandomUtil.nextInt(list.size());
			// 判断用户是否为登录用户并且ID不是自己
			int userId = StringUtil.toInt((String) list.get(pos));
			if (userId <= 0 || userId == loginUser.getId()) {
				continue;
			}
			// 获取用户信息
			UserBean user = (UserBean) UserInfoUtil.getUser(userId);
			if (user == null) {
				continue;
			} else if (checkDatetime(user.getCreateDatetime())) {
				continue;
			}
			if (hm.get(new Integer(user.getId())) == null) {
				hm.put(new Integer(user.getId()), user);
			}
			// 查看是否已经有5个人
			if (hm.size() >= 10) {
				break;
			}
		}
		request.setAttribute("hm", hm);
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断时间
	 * @datetime:2007-8-7 15:34:36
	 * @return
	 * @return boolean
	 */
	public boolean checkDatetime(Date datetime) {
		if (datetime == null || datetime.getTime()<=0) {
			return true;
		}
		//注册小于30天的用户判断
		if(System.currentTimeMillis()-datetime.getTime()<1000*60*60*24*30l){
			return false;
		}
		return true;
	}

	/*
	 * 判断注册交友系统
	 */
	public void inputRegisterInfo(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 判断是否有个人家园,如果申请用户已经创建个人家园,保持资料一致
		if (loginUser.getHome() == 1) {
			FriendBean friend = getFriendService().getFriend(loginUser.getId());
			if (friend != null) {
				result = "autoCommit";
				request.setAttribute("result", result);
				return;
			}
			// macq_2006-12-20_增加家园的缓存_start
			HomeUserBean home = HomeCacheUtil.getHomeCache(loginUser.getId());
			// HomeUserBean home = getHomeService().getHomeUser(
			// "user_id=" + loginUser.getId());
			// macq_2006-12-20_增加家园的缓存_end
			friend = new FriendBean();
			friend.setUserId(loginUser.getId());
			friend.setName(home.getName());
			friend.setMobile(home.getMobile());
			friend.setCity(home.getCity());
			friend.setConstellation(home.getConstellation());
			friend.setHeight(home.getHeight());
			friend.setHeightType(LoadResource.getHeightType(home.getHeight()));
			friend.setWeight(home.getWeight());
			friend.setWeightType(LoadResource.getWeightType(home.getWeight()));
			friend.setWork(home.getWork());
			friend.setPersonality(home.getPersonality());
			friend.setMarriage(home.getMarriage());
			friend.setAim(home.getAim());
			friend.setFriendCondition(home.getFriendCondition());
			int a = home.getGender() + 1;
			friend.setAttach(a + "_1.gif");
			friend.setAttachType(1);
			friend.setGender(home.getGender());
			friend.setAge(home.getAge());
			friend.setAgeType(LoadResource.getAgeType(home.getAge()));
			// 保存创建交友系统资料到数据库
			boolean flag = getFriendService().addFriend(friend);
			if (flag) {
				loginUser.setFlag(0, true);
				flag = UserInfoUtil.updateUser("friend="
					+ loginUser.getFlag(), "id="
					+ loginUser.getId(), loginUser.getId() + "");
			}
			result = "autoCommit";
			request.setAttribute("result", result);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 用户输入交友系统信息,提交处理
	 */
	public void registerFriend(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接收页面所传参数
		int userId = loginUser.getId();
		String name = getParameterNoEnter("name");
		int gender = getParameterIntS("gender");
		int age = getParameterIntS("age");
		String mobile = getParameterNoEnter("mobile");
		String city = getParameterNoEnter("city");
		int constellation = getParameterIntS("constellation");
		int height = getParameterIntS("height");
		int weight = getParameterIntS("weight");
		String work = getParameterNoEnter("work");
		int personality = getParameterIntS("personality");
		int marriage = getParameterIntS("marriage");
		int aim = getParameterIntS("aim");
		String friendCondition = getParameterNoEnter("friendCondition");
		// liuyi 2007-01-22 生日信息 start
		int year = getParameterIntS("year");
		int month = getParameterIntS("month");
		int day = getParameterIntS("day");
		int open = getParameterInt("open");
		String birthday = year + "-"
				+ ((month < 10) ? ("0" + month) : "" + month) + "-"
				+ ((day < 10) ? ("0" + day) : "" + day);
		boolean isBirthdayChange = true;
		Date date = DateUtil.parseDate(birthday, DateUtil.normalDateFormat);
		boolean canModifyBirthday = true;
		// liuyi 2007-01-22 生日信息 end
		// 判断输入项
		if (name == null || name.equals("")) {
			doTip(null, "姓名不能为空");
			return;
		} else if (gender < 0 || gender > 1) {
			doTip(null, "请选择性别");
			return;
		} else if (age <= 0 || age > 200) {
			doTip(null, "年龄输入有误");
			return;
		} else if (city == null || city.equals("")) {
			doTip(null, "所在城市不能为空");
			return;
		} else if (mobile.length() != 11 || StringUtil.toLong(mobile) < 0) {
			doTip(null, "手机号码输入有误");
			return;
		} else if (height < 50 || height > 250) {
			doTip(null, "个人身高输入有误");
			return;
		} else if (weight < 0 || weight > 250) {
			doTip(null, "个人体重输入有误");
			return;
		} else if (constellation < 0 || constellation > 11) {
			doTip(null, "请选择星座");
			return;
		} else if (work == null || work.equals("")) {
			doTip(null, "职业不能为空");
			return;
		} else if (personality < 0 || personality > 5) {
			doTip(null, "请选择性格");
			return;
		} else if (marriage < 0 || marriage > 3) {
			doTip(null, "请选择婚姻状况");
			return;
		} else if (aim < 0 || aim > 4) {
			doTip(null, "请选择交友目的");
			return;
		} else if (friendCondition == null
				|| friendCondition.equals("")) {
			doTip(null, "择友条件不能为空");
			return;
		} else if (isBirthdayChange
				&& canModifyBirthday
				&& date.after(new Date())) {
			doTip(null, "请正确填写生日信息");
			return;
		}
		birthday = DateUtil.formatSqlDatetime(date);
		// 判断用户是否已经开通交友系统
		// liuyi 2006-12-18 交友修改 start
		FriendBean friendBean = getFriendService().getFriend(loginUser.getId());
		// liuyi 2007-01-22 生日信息 start
		if (friendBean != null) {
			if (friendBean.getAttachType() == 0) {
				String updateBirthdaySql = (isBirthdayChange && canModifyBirthday) ? ", birthday='"
						+ birthday + "'"
						: "";
				getFriendService().updateFriend(
						"attach_type=1" + updateBirthdaySql,
						"user_id=" + friendBean.getUserId());
				friendService.flushFriend(loginUser.getId());
				loginUser.setFlag(0, true);
				boolean flag = UserInfoUtil.updateUser("friend=" + loginUser.getFlag(),
						"id=" + loginUser.getId(), loginUser.getId() + "");
//				if (flag
//						&& (loginUser.getAge() != age || loginUser.getGender() != gender)) {
//					String set = " gender=" + gender + ", age=" + age;
//					String condition = " id = " + loginUser.getId();
//					UserInfoUtil.updateUser(set, condition, loginUser.getId()
//							+ "");
//					loginUser.setGender(gender);
//					loginUser.setAge(age);
//				}
				
			}
			result = "success";
			tip = "您已经开通交友系统.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// liuyi 2006-12-18 交友修改 end
		// 生成bean并set值
		FriendBean friend = new FriendBean();
		friend.setUserId(userId);
		friend.setName(name);
		friend.setMobile(mobile);
		friend.setCity(city);
		friend.setConstellation(constellation);
		friend.setHeight(height);
		friend.setHeightType(LoadResource.getHeightType(height));
		friend.setWeight(weight);
		friend.setWeightType(LoadResource.getWeightType(weight));
		friend.setWork(work);
		friend.setPersonality(personality);
		friend.setMarriage(marriage);
		friend.setAim(aim);
		friend.setFriendCondition(friendCondition);
		int a = gender + 1;
		friend.setAttach(a + "_1.gif");
		friend.setAttachType(1);
		friend.setGender(gender);
		friend.setAge(age);
		friend.setAgeType(LoadResource.getAgeType(age));
		friend.setBirthday(birthday);
		// 保存创建交友系统资料到数据库
		getFriendService().addFriend(friend);
		loginUser.setFlag(0, true);
		boolean flag = UserInfoUtil.updateUser("friend="
				+ loginUser.getFlag() , "id="
				+ loginUser.getId(), loginUser.getId() + "");
		// liuyi 2007-01-22 生日信息 end
//		if (flag
//				&& (loginUser.getAge() != age || loginUser.getGender() != gender)) {
//			String set = " gender=" + gender + ", age=" + age;
//			String condition = " id = " + loginUser.getId();
//			UserInfoUtil.updateUser(set, condition, loginUser.getId() + "");
//			loginUser.setGender(gender);
//			loginUser.setAge(age);
//		}
		// 更新用户session中的交友系统标志位
		
		result = "success";
		request.setAttribute("result", result);

		FriendAdverBean friendAdver = new FriendAdverBean();
		friendAdver.setUserId(loginUser.getId());
		friendAdver.setTitle(city + loginUser.getAge() + "岁"
				+ (loginUser.getGender() == 0 ? "美女" : "帅哥") + "找"
				+ getAim(aim));
		friendAdver.setSex(loginUser.getGender() == 0 ? 1 : 0);
		friendAdver.setAge(Constants.AGE18_20);
		friendAdver.setArea(0);
		friendAdver.setRemark(friendCondition);
		friendAdver.setCityno(loginUser.getCityno());
		friendAdver.setGender(loginUser.getGender());
		getChatService().updateJCRoomContentCount("count=count+1",
				"room_id=10000000");

		getFriendAdverService().addFriendAdver(friendAdver);
		friendAdver = getFriendAdverService().getFriendAdver(
				" user_id=" + loginUser.getId() + " order by id desc ");
		JCRoomContentBean jcRoomContent = new JCRoomContentBean();
		jcRoomContent.setFromId(loginUser.getId());
		jcRoomContent.setToId(0);
		jcRoomContent.setFromNickName(loginUser.getNickName());
		jcRoomContent.setToNickName("" + friendAdver.getId());
		jcRoomContent.setContent("我要交友，谁要应征吗？");
		jcRoomContent.setAttach("");
		jcRoomContent.setIsPrivate(0);
		jcRoomContent.setRoomId(0);
		jcRoomContent.setSecRoomId(-1);
		jcRoomContent.setMark(3);
		getChatService().addContent(jcRoomContent);
		return;
	}

	/*
	 * macq_2006-11-21_交友目的中文
	 */
	public String getAim(int aimId) {
		String[] aim = { "恋人", "知己", "玩伴", "解闷", "朋友" };
		return aim[aimId];
	}

	/*
	 * 判断修改个人加油信息
	 */
	public void editFriend(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (!loginUser.isFlagFriend()) {
			result = "failure";
			tip = "对不起,没有查询到您所创建的交友系统,请选择创建交友信息.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		FriendBean friend = getFriendService().getFriend(loginUser.getId());
		if (friend == null) {
			result = "failure";
			tip = "您还未开通交友系统.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("friend", friend);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 修改个人交友处理提交处理
	 */
	public void editCommit(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接收页面所传参数
		int userId = loginUser.getId();
		String name = getParameterNoEnter("name");
		int gender = getParameterIntS("gender");
		int age = getParameterIntS("age");
		String mobile = getParameterNoEnter("mobile");
		String city = getParameterNoEnter("city");
		int constellation = getParameterIntS("constellation");
		int height = getParameterIntS("height");
		int weight = getParameterIntS("weight");
		String work = getParameterNoEnter("work");
		int personality = getParameterIntS("personality");
		int marriage = getParameterIntS("marriage");
		int aim = getParameterIntS("aim");
		String friendCondition = getParameterNoEnter("friendCondition");
		// liuyi 2007-01-22 生日信息 start
		int year = getParameterIntS("year");
		int month = getParameterIntS("month");
		int day = getParameterIntS("day");
		int open = getParameterIntS("open");
		String birthday = year + "-"
				+ ((month < 10) ? ("0" + month) : "" + month) + "-"
				+ ((day < 10) ? ("0" + day) : "" + day);
		boolean isBirthdayChange = true;
		Date date = DateUtil.parseDate(birthday, DateUtil.normalDateFormat);
		
		boolean canModifyBirthday = true;
		// 判断输入项
		if (name == null || name.equals("")) {
			doTip(null, "姓名不能为空");
			return;
		} else if (gender < 0 || gender > 1) {
			doTip(null, "请选择性别");
			return;
		} else if (age <= 0 || age > 200) {
			doTip(null, "年龄输入有误");
			return;
		} else if (mobile.length() != 11 || StringUtil.toLong(mobile) < 0) {
			doTip(null, "手机号码输入有误");
			return;
		} else if (city == null || city.replace(" ", "").equals("")) {
			doTip(null, "城市不能为空");
			return;
		} else if (height < 50 || height > 250) {
			doTip(null, "个人身高输入有误");
			return;
		} else if (weight < 0 || weight > 250) {
			doTip(null, "个人体重输入有误");
			return;
		} else if (constellation < 0 || constellation > 11) {
			doTip(null, "请选择星座");
			return;
		} else if (work == null || work.equals("")) {
			doTip(null, "职业不能为空");
			return;
		} else if (personality < 0 || personality > 5) {
			doTip(null, "请选择性格");
			return;
		} else if (marriage < 0 || marriage > 3) {
			doTip(null, "请选择婚姻状况");
			return;
		} else if (aim < 0 || aim > 4) {
			doTip(null, "请选择交友目的");
			return;
		} else if (friendCondition == null
				|| friendCondition.equals("")) {
			doTip(null, "择友条件不能为空");
			return;
		} else if (isBirthdayChange
				&& canModifyBirthday
				&& (date.after(new Date()))) {
			doTip(null, "请正确填写生日信息");
			return;
		}
		birthday = DateUtil.formatSqlDatetime(date);
		// 判断用户是否已经开通交友系统
		if (!loginUser.isFlagFriend()) {
			doTip(null, "您还未开通交友系统");
			return;
		}
		// 保存数据库
		String updateBirthdaySql = (isBirthdayChange && canModifyBirthday) ? ", birthday='"
				+ birthday + "'"
				: "";
		boolean flag = getFriendService().updateFriend(
				" name='" + StringUtil.toSql(name) + "',gender=" + gender + ", age=" + age
						+ " ,age_type= " + LoadResource.getAgeType(age)
						+ ", mobile='" + StringUtil.toSql(mobile) + "',city='" + StringUtil.toSql(city)
						+ "',constellation='" + constellation + "',height="
						+ height + ",height_type= "
						+ LoadResource.getHeightType(height) + ",weight="
						+ weight + ",weight_type= "
						+ LoadResource.getWeightType(weight) + ",work='" + StringUtil.toSql(work)
						+ "',personality=" + personality + ",marriage="
						+ marriage + ",aim=" + aim + ",friend_condition='"
						+ StringUtil.toSql(friendCondition) + "'" + updateBirthdaySql,
				"user_id=" + loginUser.getId());
		if (flag) {
			friendService.flushFriend(loginUser.getId());
			if (loginUser.getHome() == 1) {
				// macq_2006-12-20_增加家园的缓存_start
				HomeCacheUtil.updateHomeCacheById(" name='" + StringUtil.toSql(name)
						+ "',gender=" + gender + ", age=" + age + " ,mobile='"
						+ StringUtil.toSql(mobile) + "',city='" + StringUtil.toSql(city) + "',constellation="
						+ constellation + ",height=" + height + ",weight="
						+ weight + ",work='" + StringUtil.toSql(work) + "',personality="
						+ personality + ",marriage=" + marriage + ",aim=" + aim
						+ ",friend_condition='" + StringUtil.toSql(friendCondition) + "'",
						"user_id=" + loginUser.getId(), loginUser.getId());
				// flag = getHomeService().updateHomeUser(
				// " name='" + name + "',gender=" + gender + ", age="
				// + age + " ,mobile='" + mobile + "',city='"
				// + city + "',constellation=" + constellation
				// + ",height=" + height + ",weight=" + weight
				// + ",work='" + work + "',personality="
				// + personality + ",marriage=" + marriage
				// + ",aim=" + aim + ",friend_condition='"
				// + friendCondition + "'",
				// "user_id=" + loginUser.getId());
				// macq_2006-12-20_增加家园的缓存_end

			}
			// 修改交友信息后不可以连带修改个人资料
//			if (flag
//					&& (loginUser.getAge() != age || loginUser.getGender() != gender)) {
//				String set = " gender=" + gender + ", age=" + age;
//				String condition = " id = " + loginUser.getId();
//				flag = UserInfoUtil.updateUser(set, condition, loginUser
//						.getId()
//						+ "");
//				loginUser.setGender(gender);
//				loginUser.setAge(age);
//			}
		}
		// liuyi 2007-01-22 生日信息 end
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public void advSearchResult(HttpServletRequest request) {
		String result = null;
		String prefixUrl = null;
		int gender = getParameterIntS("gender");
		int age = getParameterInt("age");
		String city = request.getParameter("city");
		// liuyi 2006-11-27 交友搜索的中文问题 start
		if (city != null) {
			HttpSession session = request.getSession();
			if (request.getMethod().equalsIgnoreCase("post")) {
				session.setAttribute("advSearchCity", city);
			} else {
				city = (String) session.getAttribute("advSearchCity");
			}
		}
		// liuyi 2006-11-27 交友搜索的中文问题 end
		int constellation = getParameterIntS("constellation");
		int height = getParameterInt("height");
		int weight = getParameterInt("weight");
		int personality = getParameterIntS("personality");
		int marriage = getParameterIntS("marriage");
		int aim = getParameterIntS("aim");
		int attach = getParameterIntS("attach");
		String sql = null;
		if (gender >= 0 && gender != 2) {
			sql = "gender = " + gender;
			prefixUrl = "advSearchResult.jsp?gender=" + gender;
		}
		if (age > 0) {
			if (sql == null) {
				sql = "age_type = " + age;
				prefixUrl = "advSearchResult.jsp?age=" + age;
			} else {
				sql = sql + " and age_type = " + age;
				prefixUrl = prefixUrl + "&amp;age=" + age;
			}
		}
		if (city != null && !city.equals("")) {
			if (sql == null) {
				sql = "city ='" + StringUtil.toSql(city) + "'";
				prefixUrl = "advSearchResult.jsp?city=" + city;
			} else {
				sql = sql + " and city = '" + StringUtil.toSql(city) + "'";
				prefixUrl = prefixUrl + "&amp;city=" + city;
			}
		}
		if (constellation >= 0 && constellation != 12) {
			if (sql == null) {
				sql = "constellation = " + constellation;
				prefixUrl = "advSearchResult.jsp?constellation="
						+ constellation;
			} else {
				sql = sql + " and constellation = " + constellation;
				prefixUrl = prefixUrl + "&amp;constellation=" + constellation;
			}
		}
		if (height > 0) {
			if (sql == null) {
				sql = "height_type =" + height;
				prefixUrl = "advSearchResult.jsp?height=" + height;
			} else {
				sql = sql + " and height_type =" + height;
				prefixUrl = prefixUrl + "&amp;height=" + height;
			}
		}
		if (weight > 0) {
			if (sql == null) {
				sql = "weight_type=" + weight;
				prefixUrl = "advSearchResult.jsp?weight=" + weight;
			} else {
				sql = sql + " and weight_type =" + weight;
				prefixUrl = prefixUrl + "&amp;weight=" + weight;
			}
		}
		if (personality >= 0 && personality != 6) {
			if (sql == null) {
				sql = "personality =" + personality;
				prefixUrl = "advSearchResult.jsp?personality=" + personality;
			} else {
				sql = sql + " and personality =" + personality;
				prefixUrl = prefixUrl + "&amp;personality=" + personality;
			}
		}
		if (marriage >= 0 && marriage != 4) {
			if (sql == null) {
				sql = "marriage =" + marriage;
				prefixUrl = "advSearchResult.jsp?marriage=" + marriage;
			} else {
				sql = sql + " and marriage =" + marriage;
				prefixUrl = prefixUrl + "&amp;marriage=" + marriage;
			}
		}
		if (aim >= 0 && aim != 5) {
			if (sql == null) {
				sql = "aim = " + aim;
				prefixUrl = "advSearchResult.jsp?aim=" + aim;
			} else {
				sql = sql + " and aim = " + aim;
				prefixUrl = prefixUrl + "&amp;aim=" + aim;
			}
		}
		if (attach >=0 && attach != 2) {
			if (sql == null) {
				sql = "attach_type = " + attach;
				prefixUrl = "advSearchResult.jsp?attach=" + attach;
			} else {
				sql = sql + " and attach_type = " + attach;
				prefixUrl = prefixUrl + "&amp;attach=" + attach;
			}
		}
		if (sql == null) {
			sql = "1=1";
			prefixUrl = "advSearchResult.jsp?type=all";
		}

		// liuyi 2006-11-30 交友优化 start
		String query = "SELECT user_id FROM jc_friend WHERE " + sql
				+ " order by create_datetime desc";
		List userIdList = SqlUtil.getIntListCache(query, 40);
		if (userIdList == null) {
			userIdList = new ArrayList();
		}

		int totalCount = userIdList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / FRIEND_SEARCH_NUMBER_PER_PAGE;
		if (totalCount % FRIEND_SEARCH_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * FRIEND_SEARCH_NUMBER_PER_PAGE;
		int end = FRIEND_SEARCH_NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);

		List subUserIdList = userIdList.subList(startIndex, endIndex);
		
		Vector friendList =  new Vector();

		for (int i = 0; i < subUserIdList.size(); i++) {
			Integer userId = (Integer) subUserIdList.get(i);
			if (userId != null && userId.intValue() != loginUser.getId()) {
				friendList.add(friendService.getFriend(userId));
			}
		}

		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("friendList", friendList);
		result = "success";
		request.setAttribute("result", result);
		// liuyi 2006-11-30 交友优化 end

		return;
	}

	/**
	 * 交友普通查找,(同城,星座,照片)
	 * 
	 * @param request
	 * @return
	 */
	public void searchResult(HttpServletRequest request) {
		String result = null;
		String prefixUrl = null;
		int gender = getParameterIntS("gender");
		int aim = getParameterIntS("aim");
		FriendBean friend = getFriendService().getFriend(loginUser.getId());
		if (friend == null) {
			result = "nullUser";
			request.setAttribute("result", result);
			return;
		}
		String sql = null;
		if (hasParam("city") && (gender == 1 || gender == 0)) {
			sql = " city = '" + StringUtil.toSql(friend.getCity()) + "' and gender = " + gender;
			prefixUrl = "search.jsp?gender=" + gender + "&amp;city=ture";
		} else if (hasParam("constellation")
				&& (gender == 1 || gender == 0)) {
			sql = " constellation = " + friend.getConstellation()
					+ " and gender = " + gender;
			prefixUrl = "search.jsp?gender=" + gender
					+ "&amp;constellation=ture";
		} else if (hasParam("attach") && (gender == 1 || gender == 0)) {
			sql = " attach_type = 1 and gender = " + gender;
			prefixUrl = "search.jsp?gender=" + gender + "&amp;attach=ture";
		} else if (aim >= 0) {
			sql = " aim = " + aim;
			prefixUrl = "search.jsp?aim=" + aim;
		}
		if (sql == null) {
			request.setAttribute("result", "failure");
			return;
		}

		// liuyi 2006-12-02 交友优化 start
		String query = "SELECT user_id FROM jc_friend WHERE " + sql
				+ " order by create_datetime desc";

		List userIdList = SqlUtil.getIntListCache(query, 40);
		if (userIdList == null) {
			userIdList = new ArrayList();
		}

		PagingBean paging = new PagingBean(this, userIdList.size(), FRIEND_SEARCH_NUMBER_PER_PAGE, "p");

		List subUserIdList = userIdList.subList(paging.getStartIndex(), paging.getEndIndex());
		Vector friendList = new Vector();
		for (int i = 0; i < subUserIdList.size(); i++) {
			Integer userId = (Integer) subUserIdList.get(i);
			if (userId != null && userId.intValue() != loginUser.getId()) {
				friendList.add(friendService.getFriend(userId));
			}
		}

		request.setAttribute("paging", paging);
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("friendList", friendList);
		result = "success";
		request.setAttribute("result", result);
		// liuyi 2006-11-30 交友优化 end
		return;
	}

	/**
	 * 交友资料个人信息
	 * 
	 * @param request
	 * @return
	 */
	public void friendInfo(HttpServletRequest request) {
		int userId = getParameterInt("userId");
		String result = null;
		String tip = null;
		if (userId == 0) {
			userId = loginUser.getId();
		}
		FriendBean friend = getFriendService().getFriend(userId);
		// liuyi 2006-12-18 交友修改 start
		if (friend == null) {
			if (userId == loginUser.getId()) {
				tip = "您还未开通交友系统.";
			} else {
				tip = "没有查询到该用户的交友资料";
			}
			result = "failure";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (friend.getAttachType() == 0) {
			getFriendService().updateFriend("attach_type=1",
					"user_id=" + friend.getUserId());
			friendService.flushFriend(userId);
		}
		// liuyi 2006-12-18 交友修改 end

		if (session.getAttribute("cartoonrefresh") == null) {
			request.setAttribute("friend", friend);
			result = "success";
			request.setAttribute("result", result);
			return;
		}

		session.removeAttribute("cartoonrefresh");
		String file = request.getParameter("img");
		if (file != null && file.endsWith(".gif")) {
			getFriendService().updateFriend(
					"attach='" + StringUtil.toSql(file) + "' , attach_type=1",
					"user_id=" + loginUser.getId());
			friendService.flushFriend(loginUser.getId());
			tip = "您已经成功选定头像！";

		}
		friend = getFriendService().getFriend(userId);
		request.setAttribute("tip", tip);
		request.setAttribute("friend", friend);
		result = "success";
		request.setAttribute("result", result);

	}

	/**
	 * 判断好友关系
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public void jyList(HttpServletRequest request) {
		// 获取所有好友ID列表
		// liuyi 2006-11-30 交友优化 start
		// ArrayList userFriendList = UserInfoUtil.getUserFriends(loginUser
		// .getId());
		// String userId = null;
		// UserFriendBean userFriend = null;
		List jvCheckList = getUserService().getJyFriendIds(loginUser.getId());
		// liuyi 2006-11-30 交友优化 end
		int totalCount = jvCheckList.size();
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
		String prefixUrl = "jyList.jsp?";
		// 取得要显示的消息列表x
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, jvCheckList.size());
		int endIndex = Math.min(start + end, jvCheckList.size());
		List jvList = jvCheckList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("jvList", jvList);
		return;
	}

	/**
	 * 判断好友关系
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public String getRelation(int userId, int friendId) {
		String relation = null;
		UserFriendBean userFriend = getUserService().getUserFriend(userId,
				friendId);
		if (userFriend == null) {
			return null;
		}
		int relationType = userFriend.getMark();
		String relationName = null;
		if (relationType == 2) {
			relationName = loginUser.getGender() == 0 ? "老公" : "老婆";
		} else if (relationType == 1) {
			relationName = "金兰";
		} else {
			relationName = "好友";
		}
		return relationName;
	}

	/**
	 * 获取2个用户之间的好友度
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */

	public String getFriendLevel(int userId, int friendId) {
		String Level = null;
		UserFriendBean userFriend = getUserService().getUserFriend(userId,
				friendId);
		if (userFriend == null) {
			return Level;
		}
		return userFriend.getLevelValue() + "";
	}
	
	public int getFriendLevelInt(int userId, int friendId) {
		String Level = null;
		UserFriendBean userFriend = getUserService().getUserFriend(userId,
				friendId);
		if (userFriend == null) {
			return 0;
		}
		return userFriend.getLevelValue();
	}

	/**
	 * 判断用户的钱是否足够买东西
	 * 
	 * @param money
	 * @return true or false
	 */
	public boolean haveEnoughpoint(int point) {

		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		
		if(point <= ShopUtil.PROPOSAL_LEBI) {
			int currentpoint = usb.getGamePoint();
			if (currentpoint >= point) {
				return true;
			} else {
				return false;
			}
		} else {
			point = point / ShopUtil.PROPOSAL_LEBI;
			UserInfoBean bean = ShopAction.shopService.getUserInfo(this.getLoginUser().getId());
			if(bean == null) {
				bean = ShopAction.shopService.addUserInfo(this.getLoginUser().getId());
			}
			if(bean.getGold() >= point) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 买戒指成功，则扣用户乐币从user_status,添加求婚记录，并发送消息给被求婚者
	 * 
	 * @param money
	 */
	public void deductUserpoint(int point, String ringName, int toId, int ringId) {
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		int setpoint = 0;
		int gold = 0;
		String info = "";
		int currentpoint = usb.getGamePoint();
		if (haveEnoughpoint(point)) {
			if(point <= ShopUtil.PROPOSAL_LEBI) {
				setpoint = currentpoint - point;
				info += point + "乐币";
			} else {
				setpoint = currentpoint;
				gold = point / ShopUtil.PROPOSAL_LEBI;
				info += ShopUtil.GOLD_NAME_PATH + gold;
			}
			
		}
		UserInfoUtil.updateUserStatus("mark=1,game_point=" + setpoint,
				"user_id=" + loginUser.getId(), loginUser.getId(),
				UserCashAction.OTHERS, "求婚更改状态位，买戒指扣钱" + info);
		
		if(gold > 0) {
			ShopUtil.updateUserGold(this.getLoginUser().getId(),(float)gold, ShopUtil.RECORD_TYPE_PROPOSAL);
		}
		
		
		// 添加求婚记录
		FriendProposalBean friendProposl = new FriendProposalBean();
		friendProposl.setFingerRingId(ringId);
		friendProposl.setFromId(loginUser.getId());
		friendProposl.setToId(toId);
		friendProposl.setMark(0);
		getFriendService().addFriendProposal(friendProposl);

		// 给被邀请者发送消息,当前登录用户为被邀请者
		NoticeBean notice = new NoticeBean();
		notice.setUserId(toId);
		notice.setTitle(loginUser.getNickName() + "用" + ringName + "向您求婚了！");
		notice.setContent("");
		notice.setHideUrl("");
		notice.setType(NoticeBean.KEEP_NOT_READ_NOTICE);
		notice.setLink("/friend/replyProposal.jsp?user=" + loginUser.getId()
				+ "&ringId=" + ringId);
		NoticeUtil.getNoticeService().addNotice(notice);

	}

	/**
	 * 通过friendRingId 取得 friendRing
	 * 
	 * @param friendRingId
	 * @return
	 */
	public FriendRingBean getFriendRing(int friendRingId) {
		FriendRingBean friendRing = null;
		friendRing = (FriendRingBean) OsCacheUtil.get("" + friendRingId,
				OsCacheUtil.FRIEND_RING_GROUP,
				OsCacheUtil.FRIEND_RING_FLUSH_PERIOD);
		if (friendRing == null) {
			friendRing = getFriendService()
					.getFriendRing(" id=" + friendRingId);
			if (friendRing != null)
				OsCacheUtil.put("" + friendRingId, friendRing,
						OsCacheUtil.FRIEND_RING_GROUP);
		}
		return friendRing;
	}

	public Vector getFriendRing() {
		Vector vecFriendRing = null;
		vecFriendRing = (Vector) OsCacheUtil.get("query",
				OsCacheUtil.FRIEND_RING_GROUP,
				OsCacheUtil.FRIEND_RING_FLUSH_PERIOD);
		if (vecFriendRing == null) {
			vecFriendRing = getFriendService().getFriendRingList(null);
			OsCacheUtil.put("query", vecFriendRing,
					OsCacheUtil.FRIEND_RING_GROUP);
		}
		return vecFriendRing;
	}

	// 答应求婚

	public boolean replyYes(int fromId, int ringId) {
		boolean flag = true;

		// 更改求婚消息
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId > 0) {
			noticeService.updateNotice("status = " + NoticeBean.READED, "id="
					+ noticeId);
			// 清当前用户消息缓存
			NewNoticeCacheUtil
					.updateUserNoticeById(loginUser.getId(), noticeId);
		}
		FriendProposalBean friendProposal = getFriendService()
				.getFriendProposal(
						"mark=0 and from_id=" + fromId + " and to_id="
								+ loginUser.getId());
		if (friendProposal != null && friendProposal.getMark() == 0) {
			getFriendService().updateFriendProposal(
					"mark=2",
					"mark=0 and from_id=" + fromId + " and to_id="
							+ loginUser.getId());
		} else {
			return false;
		}
		// liuyi 2006-11-02 同意结婚的判断条件 start
		int toId = loginUser.getId();

		UserStatusBean fromStatus = UserInfoUtil.getUserStatus(fromId);
		UserStatusBean toStatus = UserInfoUtil.getUserStatus(toId);

		if ((fromStatus != null && fromStatus.getMark() == 2)
				|| (toStatus != null && toStatus.getMark() == 2)) {
			if (fromStatus != null && fromStatus.getMark() == 1)
				UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId,
						fromId, UserCashAction.OTHERS, "求婚失败更改状态位");
			request.setAttribute("tip", "你们两人中有人已经结婚，现在不能再结婚！");
			return false;
		}

		UserFriendBean u1 = userService.getUserFriend(fromId, toId);
		UserFriendBean u2 = userService.getUserFriend(toId, fromId);
		if ((u1 != null && u1.getMark() == 2)
				|| (u2 != null && u2.getMark() == 2)) {
			request.setAttribute("tip", "你们两人已经结婚！");
			return false;
		}

		if (u1 == null || u2 == null) {
			if (fromStatus != null && fromStatus.getMark() == 1)
				UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId,
						fromId, UserCashAction.OTHERS, "求婚失败更改状态位");
			request.setAttribute("tip", "你们不是好友，现在不能再结婚！");
			return false;
		}

		// liuyi 2006-11-02 同意结婚的判断条件 end

		FriendMarriageBean friendMarrige = getFriendService()
				.getFriendMarriage(
						"from_id=" + fromId + " and to_id=" + loginUser.getId()
								+ " and mark=0");
		if (friendMarrige == null) {
			friendMarrige = new FriendMarriageBean();
			friendMarrige.setFingerRingId(ringId);
			friendMarrige.setFromId(fromId);
			friendMarrige.setMark(0);
			friendMarrige.setToId(loginUser.getId());
			friendMarrige.setCandyNum(0);
			friendMarrige.setCandyPrice(0);
			friendMarrige.setCandyRemain(0);
			friendMarrige.setGuestNum(0);
			friendMarrige.setMoney(0);
			friendMarrige.setPledge("");
			getFriendService().addFriendMarriage(friendMarrige);
		}
		// getUserService().updateFriend("mark=2",
		// "user_id=" + loginUser.getId() + " and friend_id=" + fromId);
		// getUserService().updateFriend("mark=2",
		// "user_id=" + fromId + " and friend_id=" + loginUser.getId());
		// 清除好友关系缓存
		// UserInfoUtil.updateUserStatus("mark=2", "user_id=" +
		// loginUser.getId(),
		// loginUser.getId(), UserCashAction.OTHERS, "结婚更改状态");
		// UserInfoUtil.updateUserStatus("mark=2", "user_id=" + fromId, fromId,
		// UserCashAction.OTHERS, "结婚更改状态");
		// liuyi 2006-11-03 交友事务处理 start
		String[] sqls = new String[4];
		sqls[0] = "UPDATE user_friend SET mark=2 WHERE " + "user_id="
				+ loginUser.getId() + " and friend_id=" + fromId;
		sqls[1] = "UPDATE user_friend SET mark=2 WHERE " + "user_id=" + fromId
				+ " and friend_id=" + loginUser.getId();
		sqls[2] = "UPDATE user_status SET mark=2 WHERE " + "user_id="
				+ loginUser.getId();
		sqls[3] = "UPDATE user_status SET mark=2 WHERE " + "user_id=" + fromId;
		SqlUtil.excuteWithinStraction(sqls, Constants.DBShortName);

		userService.flushUserFriend(fromId, loginUser.getId());
		
		UserInfoUtil.flushUserStatus(loginUser.getId());
		UserInfoUtil.flushUserStatus(fromId);
		// liuyi 2006-11-03 交友事务处理 end
		// 给求婚者发消息
		NoticeBean notice = new NoticeBean();
		notice.setUserId(fromId);
		notice.setTitle("恭喜，" + loginUser.getNickName() + "答应了您的求婚，选择婚礼佳期吧！");
		notice.setContent("");
		notice.setHideUrl("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setLink("/friend/marriage.jsp");
		NoticeUtil.getNoticeService().addNotice(notice);

		return flag;
	}

	// 拒绝求婚

	public boolean replyNo(int fromId) {
		boolean flag = true;
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId > 0) {
			noticeService.updateNotice("status = " + NoticeBean.READED, "id="
					+ noticeId);
			// 清当前用户消息缓存
			NewNoticeCacheUtil
					.updateUserNoticeById(loginUser.getId(), noticeId);
		}

		// 更改求婚消息
		FriendProposalBean friendProposal = getFriendService()
				.getFriendProposal(
						"mark=0 and from_id=" + fromId + " and to_id="
								+ loginUser.getId());
		if (friendProposal != null && friendProposal.getMark() == 0) {
			getFriendService().updateFriendProposal(
					"mark=2",
					"mark=0 and from_id=" + fromId + " and to_id="
							+ loginUser.getId());

		} else {
			return false;
		}

		// liuyi 2006-11-02 拒绝结婚的判断条件 start
		int toId = loginUser.getId();

		UserStatusBean fromStatus = UserInfoUtil.getUserStatus(fromId);
		UserStatusBean toStatus = UserInfoUtil.getUserStatus(toId);

		if ((fromStatus != null && fromStatus.getMark() == 2)
				|| (toStatus != null && toStatus.getMark() == 2)) {
			if (fromStatus != null && fromStatus.getMark() == 1)
				UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId,
						fromId, UserCashAction.OTHERS, "求婚失败更改状态位");
			request.setAttribute("tip", "你们两人中有人已经结婚！");
			return false;

		}

		UserFriendBean u1 = userService.getUserFriend(fromId, toId);
		UserFriendBean u2 = userService.getUserFriend(toId, fromId);
		if ((u1 != null && u1.getMark() == 2)
				&& (u2 != null && u2.getMark() == 2)) {
			if (fromStatus != null && fromStatus.getMark() == 1)
				UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId,
						fromId, UserCashAction.OTHERS, "求婚失败更改状态位");
			request.setAttribute("tip", "你们不是好友！");
			return false;
		}
		// liuyi 2006-11-02 拒绝结婚的判断条件 end
		// getFriendService().updateFriendProposal(
		// "mark=1",
		// "mark=0 and from_id=" + fromId + " and to_id="
		// + loginUser.getId());
		// 双方友好度都减50
		if (getUserService().updateFriend("level_value=level_value-50",
				"user_id=" + loginUser.getId() + " and friend_id=" + fromId)) {
			getUserService()
					.updateFriend(
							"level_value=level_value-50",
							"user_id=" + fromId + " and friend_id="
									+ loginUser.getId());
			// liuyi 2006-10-31 更新社交指数 start
			UserInfoUtil.updateUserStatus("social=social-50", "user_id="
					+ loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "拒绝求婚减50社交指数");
			UserInfoUtil.updateUserStatus("social=social-50", "user_id="
					+ fromId, fromId, UserCashAction.OTHERS, "拒绝求婚减50社交指数");
			// liuyi 2006-10-31 更新社交指数 end
			// 清除缓存
			userService.flushUserFriend(fromId, loginUser.getId());
		}

		UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId, fromId,
				UserCashAction.OTHERS, "求婚失败更改状态位");
		// 给求婚者发消息
		NoticeBean notice = new NoticeBean();
		notice.setUserId(fromId);
		notice.setTitle("遗憾，" + loginUser.getNickName() + "拒绝了您的求婚，继续努力吧！");
		notice.setContent("");
		notice.setHideUrl("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setLink("/friend/friendCenter.jsp");
		NoticeUtil.getNoticeService().addNotice(notice);

		return flag;

	}

	// 72小时无反映则视为拒绝求婚
	public static void replyNoAnswer() {
		Vector vec = friendService
				.getFriendProposalList("ADDDATE(create_datetime,interval 72 HOUR)<NOW() and mark=0");
		if (vec != null)

		{
			int fromId = 0;
			int toId = 0;
			// IFriendLevelService friendLevels = ServiceFactory
			// .createFriendLevelService();
			for (int i = 0; i < vec.size(); i++) {
				FriendProposalBean friendProposal = (FriendProposalBean) vec
						.get(i);
				fromId = friendProposal.getFromId();
				toId = friendProposal.getToId();
				UserBean toUser = (UserBean) UserInfoUtil.getUser(toId);
				// 友好度减50
				if (userService.updateFriend("level_value=level_value-50",
						"user_id=" + toId + " and friend_id=" + fromId)) {
					userService.updateFriend("level_value=level_value-50",
							"user_id=" + fromId + " and friend_id=" + toId);
					// liuyi 2006-10-31 更新社交指数 start
					UserInfoUtil.updateUserStatus("social=social-50",
							"user_id=" + toId, toId, UserCashAction.OTHERS,
							"求婚无回应减50社交指数");
					UserInfoUtil.updateUserStatus("social=social-50",
							"user_id=" + fromId, fromId, UserCashAction.OTHERS,
							"求婚无回应减50社交指数");
					// liuyi 2006-10-31 更新社交指数 end
					// 清除缓存
					userService.flushUserFriend(fromId, toId);
				}
				// 更改求婚状态位
				UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId,
						fromId, UserCashAction.OTHERS, "求婚失败更改状态位");
				// 给婚者发消息
				NoticeBean notice = new NoticeBean();
				notice.setUserId(fromId);
				notice
						.setTitle("遗憾，" + toUser.getNickName()
								+ "无视了您的求婚，继续努力吧！");
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setLink("/friend/marriage.jsp");
				NoticeUtil.getNoticeService().addNotice(notice);
				NoticeBean oldNotice = NoticeUtil
						.getNoticeService()
						.getNotice(
								"user_id="
										+ toId
										+ " and status= 0 and type=10 and ADDDATE(create_datetime,interval 72 HOUR)<NOW()");
				if (oldNotice != null) {
					int noticeId = oldNotice.getId();
					// 清接受求婚消息的用户消息缓存
					NewNoticeCacheUtil.updateUserNoticeById(toId, noticeId);
				}

			}
		}
		// 更改求婚者状态位
		friendService.updateFriendProposal("mark=1",
				"ADDDATE(create_datetime,interval 72 HOUR)<NOW() and mark=0");
		// 更改求婚消息状态位
		noticeService
				.updateNotice("status = " + NoticeBean.READED,
						"status= 0 and type=10 and ADDDATE(create_datetime,interval 72 HOUR)<NOW()");

	}

	public static int getJyCoutByRank(int rank) {
		// int ret = 3;
		// if (rank > 0) {
		// ret = 3 + rank % 5;
		// }
		return 3 + rank / 5;
	}

	/**
	 * liuyi 2006-10-28 结义处理
	 */
	public void jy() {
		int drinkId = StringUtil.toInt(request.getParameter("drinkId"));
		int toId = StringUtil.toInt(request.getParameter("toId"));

		UserBean loginUser = (UserBean) session
				.getAttribute(Constants.LOGIN_USER_KEY);

		if (toId == loginUser.getId()) {
			request.setAttribute("tip", "不能给自己结义！");
			return;
		}

		UserFriendBean friendBean = userService.getUserFriend(
				loginUser.getId(), toId);
		UserFriendBean friendBean2 = userService.getUserFriend(toId, loginUser
				.getId());
		if (friendBean == null || friendBean2 == null) {
			request.setAttribute("tip", "你们还不是好友，不能结为金兰！");
			return;
		}
		if (friendBean.getMark() >= 1) {
			request.setAttribute("tip", "你们已经是结义金兰或者夫妻！");
			return;
		}
		if (friendBean.getLevelValue() < Constants.MIN_FRIEND_LEVEL_FOR_JY
				|| friendBean.getLevelValue() < Constants.MIN_FRIEND_LEVEL_FOR_JY) {
			request.setAttribute("tip", "你们的友好度还不够，不能结为金兰！");
			return;
		}

		List list = userService.getJyFriendIds(loginUser.getId());
		int currentJyCount = list.size();
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		int maxJyCount = FriendAction.getJyCoutByRank(userStatus.getRank());
		if (currentJyCount >= maxJyCount) {
			request.setAttribute("tip", "您的金兰已经达到了最大个数，需要升级才能够继续添加金兰！");
			return;
		}

		FriendDrinkBean drink = friendService.getFriendDrink(drinkId);
		if (drink == null) {
			request.setAttribute("tip", "该结义酒不存在！");
			return;
		}
		FriendUserBean oldProposal = friendService.getFriendUser("user_id="
				+ loginUser.getId() + " and friend_id=" + toId + " and mark=0");
		if (oldProposal != null) {
			request.setAttribute("tip", "您之前已经向对方提出了结义请求，请静候回音！");
			return;
		}
		if (drink.getPrice() > userStatus.getGamePoint()) {
			request.setAttribute("tip", "您的乐币不够支付" + drink.getName() + "！");
			return;
		}
		UserInfoUtil.updateUserStatus("game_point= game_point-"
				+ drink.getPrice(), "user_id=" + loginUser.getId(), loginUser
				.getId(), UserCashAction.OTHERS, "买结义酒扣钱" + drink.getPrice());
		// 添加结义记录
		FriendUserBean friendProposl = new FriendUserBean();
		friendProposl.setDrinkId(drinkId);
		friendProposl.setUserId(loginUser.getId());
		friendProposl.setFriendId(toId);
		friendProposl.setMark(0);
		friendService.addFriendUser(friendProposl);

		// 给被邀请者发送消息,当前登录用户为被邀请者
		NoticeBean notice = new NoticeBean();
		notice.setUserId(toId);
		notice.setTitle(loginUser.getNickName() + "用" + drink.getName()
				+ "想跟您义结金兰！");
		notice.setContent("");
		notice.setHideUrl("");
		notice.setType(NoticeBean.KEEP_NOT_READ_NOTICE);
		notice.setLink("/friend/replyJyProposal.jsp?fromId=" + loginUser.getId()
				+ "&drinkId=" + drinkId);
		NoticeUtil.getNoticeService().addNotice(notice);

	}

	/**
	 * liuyi 2006-10-29 回复结义请求
	 * 
	 * @param fromId
	 * @param toId
	 * @param result
	 *            2:答应 1：拒绝
	 */
	public boolean replyJy(FriendUserBean friendUser, int result) {
		if (friendUser == null) {
			request.setAttribute("tip", "好友不存在！");
			return false;
		}

		int fromId = friendUser.getUserId();
		int toId = friendUser.getFriendId();

		// 修改消息状态
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId > 0) {
			noticeService.updateNotice("status = " + NoticeBean.READED, "id="
					+ noticeId);
			// 清当前用户消息缓存
			NewNoticeCacheUtil.updateUserNoticeById(toId, noticeId);
		}
		// 修改结义请求
		boolean flag = getFriendService().updateFriendUser("mark=" + result,
				"id=" + friendUser.getId());
		if (!flag) {
			request.setAttribute("tip", "操作失败！");
			return false;
		}

		if (fromId == toId) {
			request.setAttribute("tip", "数据错误！");
			return false;
		}

		UserFriendBean bean = userService.getUserFriend(fromId, toId);
		UserFriendBean bean2 = userService.getUserFriend(toId, fromId);
		if (bean == null || bean2 == null) {
			request.setAttribute("tip", "你们已经不是好友！");
			return false;
		}
		if (bean.getMark() >= 1) {
			request.setAttribute("tip", "你们已经是配偶或者金兰！");
			return false;
		}

		UserBean toUser = UserInfoUtil.getUser(toId);
		if (result == 1) {// 拒绝
			getFriendService().updateFriendUser("mark=1",
					"id=" + friendUser.getId());
			// 双方友好度都减50
			userService.updateFriend("level_value=level_value-50", "user_id="
					+ fromId + " and friend_id=" + toId);
			userService.updateFriend("level_value=level_value-50", "user_id="
					+ toId + " and friend_id=" + fromId);
			// liuyi 2006-10-31 更新社交指数 start
			UserInfoUtil.updateUserStatus("social=social-50",
					"user_id=" + toId, toId, UserCashAction.OTHERS,
					"拒绝结义减50社交指数");
			UserInfoUtil.updateUserStatus("social=social-50", "user_id="
					+ fromId, fromId, UserCashAction.OTHERS, "拒绝结义减50社交指数");
			// liuyi 2006-10-31 更新社交指数 end
			// 清除缓存
			userService.flushUserFriend(fromId, toId);
			// 给请求者发消息
			NoticeBean notice = new NoticeBean();
			notice.setUserId(fromId);
			notice.setTitle("遗憾，" + toUser.getNickName() + "拒绝了您的结义请求，继续努力吧！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/user/ViewUserInfo.do?userId=" + toId);
			NoticeUtil.getNoticeService().addNotice(notice);
		} else if (result == 2) {// 答应
			getFriendService().updateFriendUser("mark=2",
					"id=" + friendUser.getId());
			// 根据用户的等级，判断是否可以再加金兰
			List list = userService.getJyFriendIds(loginUser.getId());
			int currentJyCount = list.size();
			UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					.getId());
			int maxJyCount = FriendAction.getJyCoutByRank(userStatus.getRank());
			if (currentJyCount >= maxJyCount) {
				request.setAttribute("tip", "您的金兰已经达到了最大个数，需要升级才能够继续添加金兰！");
				return false;
			}

			// 修改好友关系表的mark
			// userService.updateFriend("mark=1", "user_id=" + fromId
			// + " and friend_id=" + toId);
			// userService.updateFriend("mark=1", "user_id=" + toId
			// + " and friend_id=" + fromId);
			String[] sqls = new String[2];
			sqls[0] = "UPDATE user_friend SET mark=1 WHERE " + "user_id="
					+ fromId + " and friend_id=" + toId;
			sqls[1] = "UPDATE user_friend SET mark=1 WHERE user_id=" + toId
					+ " and friend_id=" + fromId;
			SqlUtil.excuteWithinStraction(sqls, Constants.DBShortName);

			// 清除好友关系缓存
			userService.flushUserFriend(fromId, toId);
			
			list.add(new Integer(toId));
			List userList = userService.getJyFriendIds(toId);
			userList.add(new Integer(loginUser.getId()));

			// 给请求者发消息
			NoticeBean notice = new NoticeBean();
			notice.setUserId(fromId);
			notice.setTitle("您已与" + toUser.getNickName() + "义结金兰！");
			notice.setContent("您已与" + toUser.getNickName()
					+ "义结金兰，今后有福同享，有难同当，不能同年同月同日生，但求同年同月同日死！");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/user/ViewUserInfo.do?userId=" + toId);
			NoticeUtil.getNoticeService().addNotice(notice);
		}

		return true;

	}

	public void deleteJy(int fromId, int toId) {

		UserFriendBean friendBean = userService.getUserFriend(fromId, toId);
		UserFriendBean friendBean2 = userService.getUserFriend(toId, fromId);
		if (friendBean != null && friendBean2 != null
				&& (friendBean.getMark() == 1 || friendBean2.getMark() == 1)) {
			// 更新好友表的mark
			// userService.updateFriend(
			// "mark=0,level_value=0, update_datetime=now()", "user_id="
			// + loginUser.getId() + " and friend_id=" + toId);
			// userService.updateFriend(
			// "mark=0,level_value=0, update_datetime=now()", "user_id="
			// + toId + " and friend_id=" + loginUser.getId());
			String[] sqls = new String[2];
			sqls[0] = "UPDATE user_friend SET mark=0,level_value=0 WHERE "
					+ "user_id=" + loginUser.getId() + " and friend_id=" + toId;
			sqls[1] = "UPDATE user_friend SET mark=0,level_value=0 WHERE "
					+ "user_id=" + toId + " and friend_id=" + loginUser.getId();
			SqlUtil.excuteWithinStraction(sqls, Constants.DBShortName);
			// 清除好友关系缓存
			userService.flushUserFriend(fromId, toId);
			
			List list = userService.getJyFriendIds(toId);
			list.remove(new Integer(fromId));
			list = userService.getJyFriendIds(fromId);
			list.remove(new Integer(toId));
			// 添加割袍断义的记录
			FriendBadUserBean bean = new FriendBadUserBean();
			bean.setUserId(fromId);
			bean.setFriendId(toId);
			getFriendService().addFriendBadUser(bean);

			// 给对方发消息
			UserBean fromUser = (UserBean) UserInfoUtil.getUser(fromId);

			NoticeBean notice = new NoticeBean();
			notice.setUserId(toId);
			notice.setTitle("很遗憾," + fromUser.getNickName() + "已经与您割袍断义！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/user/ViewUserInfo.do?userId=" + fromId);
			NoticeUtil.getNoticeService().addNotice(notice);
		}
	}

	/**
	 * 拥有成功求婚契约的两名用户任何一人可以进入“结婚礼堂”，点击NPC“乐酷管理员”设定婚礼开始时间：
	 */

	public int isOrderTime() {
		int marriageId = 0;
		if (loginUser != null){
			UserStatusBean userStatus = (UserStatusBean) UserInfoUtil
					.getUserStatus(loginUser.getId());
			if (userStatus.getMark() == 2) {
	
				FriendMarriageBean marriage = infoMarriage(loginUser.getId());
				if (marriage != null) {
					if (marriage.getMarriageDatetime() == null)
						marriageId = marriage.getId();
					else {
						Date date = DateUtil.parseDate(marriage
								.getMarriageDatetime(), "yyyy-MM-dd HH:mm:ss");
						date = DateUtil.rollHour(date, 24);
						String marriageDate = DateUtil.formatDate(date,
								"yyyy-MM-dd HH:mm:ss");
						Date now = new Date();
						String nowDate = DateUtil.formatDate(now,
								"yyyy-MM-dd HH:mm:ss");
						if (nowDate.compareTo(marriageDate) < 0) {
							marriageId = marriage.getId();
	
						}
					}
				}
			}
		}
		return marriageId;
	}

	/**
	 * 判断能否更改婚期 ,即用户有无定国婚期
	 * 
	 */

	public int changeTime() {
		UserStatusBean userStatus = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		int marriageId = 0;
		if (userStatus.getMark() == 2) {

			FriendMarriageBean marriage = infoMarriage(loginUser.getId());
			if (marriage != null) {
				if (marriage.getMarriageDatetime() == null)
					marriageId = marriage.getId();
			}
		}
		return marriageId;

	}

	/**
	 * 用户是否在婚礼前
	 * 
	 * @return
	 */
	public int changeTimeBefore() {
		UserStatusBean userStatus = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		int marriageId = 0;
		if (userStatus.getMark() == 2) {

			FriendMarriageBean marriage = getFriendService().getFriendMarriage(
					"mark=0 and marriage_datetime>now()  and from_id="
							+ loginUser.getId());
			if (marriage == null)
				marriage = getFriendService().getFriendMarriage(
						"mark=0 and marriage_datetime>now() and to_id="
								+ loginUser.getId());
			if (marriage != null) {
				if (marriage.getMarriageDatetime() != null)
					marriageId = marriage.getId();
			}
		}
		return marriageId;
	}

	public int changeSuger() {
		UserStatusBean userStatus = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		int marriageId = 0;
		if (userStatus.getMark() == 2) {

			FriendMarriageBean marriage = getFriendService()
					.getFriendMarriage(
							"mark=0 and marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() and from_id="
									+ loginUser.getId());
			if (marriage == null)
				marriage = getFriendService()
						.getFriendMarriage(
								"mark=0 and marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() and to_id="
										+ loginUser.getId());
			if (marriage != null) {
				if (marriage.getMarriageDatetime() != null)
					marriageId = marriage.getId();
			}
		}
		return marriageId;

	}

	/**
	 * 其余举行的婚礼录像
	 */
	public void marriage(HttpServletRequest request) {
		int totalKinescope = 0;
		int tototal = 0;
		int total = 0;

		Vector idList = getMarriageIDList("select id from jc_friend_marriage where mark=0 and guest_num>0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() order by marriage_datetime ");
		totalKinescope = idList.size();
		idList = getMarriageIDList("select id from jc_friend_marriage where mark=0 and  marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() order by marriage_datetime ");
		total = idList.size();
		idList = getMarriageIDList("select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime");
		tototal = idList.size();
		request.setAttribute("totalKinescope", totalKinescope + "");
		request.setAttribute("tototal", tototal + "");
		request.setAttribute("total", total + "");

	}

	/**
	 * 婚礼录像的三条婚礼
	 */
	public Vector getTopMarriageKinescope() {

		Vector vec = (Vector) OsCacheUtil.get("query",
				OsCacheUtil.MARRIAGE_KINESCOPE_GROUP,
				OsCacheUtil.MARRIAGE_KINESCOPE_FLUSH_PERIOD);
		if (vec == null) {
			vec = (Vector) getFriendService()
					.getFriendMarriageList(
							" mark=0 and guest_num>0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW()  order by marriage_datetime desc limit 0,3");
			if (vec != null)
				OsCacheUtil.put("query", vec,
						OsCacheUtil.MARRIAGE_KINESCOPE_GROUP);
		}

		return vec;

	}

	/**
	 * 正在举行的三条婚礼
	 */
	public Vector getTopMarriage() {

		Vector vec = (Vector) OsCacheUtil.get("query",
				OsCacheUtil.MARRIAGE_NOW_GROUP,
				OsCacheUtil.MARRIAGE_NOW_FLUSH_PERIOD);
		if (vec == null) {
			vec = (Vector) getFriendService()
					.getFriendMarriageList(
							" mark=0 and marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW()  order by marriage_datetime limit 0,3");
			if (vec != null)
				OsCacheUtil.put("query", vec, OsCacheUtil.MARRIAGE_NOW_GROUP);
		}

		return vec;

	}

	/**
	 * 其余举行的婚礼
	 */
	public void getMarriage(HttpServletRequest request) {
		int totalCount = 0;
		int NUM_PER_PAGES = 10;
		int totalPage = 0;
		Vector currentPagePml = null;
		int pageIndex = 0;
		String sql = null;
		String nowtime = request.getParameter("nowtime");
		String t = request.getParameter("t");
		String oldtime = request.getParameter("oldtime");
		if (nowtime != null) {
			sql = "select id from jc_friend_marriage where mark=0 and  marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() order by marriage_datetime ";
		} else if (oldtime != null) {
			sql = "select id from jc_friend_marriage where mark=0 and guest_num>0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() order by marriage_datetime desc";
		} else if (t != null) {
			sql = "select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime";
		} else {
			sql = " ";
		}
		Vector idList = getMarriageIDList(sql);
		totalCount = idList.size();
		if (totalCount > 0) {
			// int checkIdSize = Math.min(idList.size(), 10);
			Vector pmlCheckList = getMarriageList(idList.subList(0, totalCount));
			if (pmlCheckList != null) {
				// 判断实际得到的结婚信息数量
				int pmlCheckListCount = pmlCheckList.size();
				// 如信息小于3条不做处理
				if (pmlCheckListCount <= 3) {

				} else {

					totalPage = (totalCount + NUM_PER_PAGES - 1 - 3)
							/ NUM_PER_PAGES;
					// 分页参数
					pageIndex = StringUtil.toInt(request
							.getParameter("pageIndex"));
					// 无传分页参数
					if (pageIndex == -1) {
						pageIndex = 0;
					}
					int start1 = 0;
					int end1 = 0;
					// 判断是否有分页参数

					start1 = pageIndex * NUM_PER_PAGES + 3;
					end1 = NUM_PER_PAGES;

					int startIndex = Math.min(start1, idList.size());
					int endIndex = Math.min(start1 + end1, idList.size());
					List currentPageContentIdList = idList.subList(startIndex,
							endIndex);
					currentPagePml = getMarriageList(currentPageContentIdList);

				}
			}

		}
		if (currentPagePml == null)
			currentPagePml = new Vector();
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGES + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("toMarriage", currentPagePml);
		request.setAttribute("nowtime", nowtime);
		request.setAttribute("oldtime", oldtime);
		request.setAttribute("t", t);

	}

	/**
	 * 即将举行的三条婚礼
	 */
	public Vector getTopToMarriage() {

		Vector vec = (Vector) OsCacheUtil.get("query",
				OsCacheUtil.MARRIAGE_TO_GROUP,
				OsCacheUtil.MARRIAGE_TO_FLUSH_PERIOD);
		if (vec == null) {
			vec = (Vector) getFriendService()
					.getFriendMarriageList(
							" mark=0 and marriage_datetime>now()  order by marriage_datetime  limit 0,3");
			if (vec != null)
				OsCacheUtil.put("query", vec, OsCacheUtil.MARRIAGE_TO_GROUP);
		}

		return vec;
	}

	/**
	 * 得到缓存的结婚ID列表 MARRIAGE_TO_GROUP即将，正在，已经结婚的都放在这个里面啦
	 * 
	 * @param sql
	 * @return
	 */
	public Vector getMarriageIDList(String sql) {
		Vector ret = new Vector();
		ret = (Vector) OsCacheUtil.get(sql, OsCacheUtil.MARRIAGE_TO_GROUP,
				OsCacheUtil.MARRIAGE_TO_FLUSH_PERIOD);

		if (ret == null) {
			List params = new ArrayList();
			String dbName = Constants.DBShortName;

			ret = (Vector) SqlUtil.getObjectList(sql, params, dbName);

			if (ret == null) {
				ret = new Vector();
			}
			OsCacheUtil.put(sql, ret, OsCacheUtil.MARRIAGE_TO_GROUP);
		}

		return ret;
	}

	/**
	 * 根据结婚id列表获取对应的结婚列表，该方法确保返回一个Vector实例；
	 * 
	 * @param contentIdList
	 * @return
	 */
	public Vector getMarriageList(List idList) {
		Vector ret = new Vector();

		if (idList == null)
			return ret;

		for (int i = 0; i < idList.size(); i++) {
			Integer id = (Integer) idList.get(i);
			if (id == null)
				continue;

			FriendMarriageBean marriage = getOnesMarriage(id.intValue());
			if (marriage != null) {
				ret.add(marriage);
			}
		}

		return ret;
	}

	/**
	 * 根据结婚号得到结婚信息
	 * 
	 */
	public FriendMarriageBean getOnesMarriage(int marriageId) {

		FriendMarriageBean marriage = (FriendMarriageBean) OsCacheUtil.get(
				"id=" + marriageId, OsCacheUtil.MARRIAGE_TO_GROUP,
				OsCacheUtil.MARRIAGE_TO_FLUSH_PERIOD);
		if (marriage == null) {
			marriage = getFriendService().getFriendMarriage("id=" + marriageId);

			OsCacheUtil.put("id=" + marriageId, marriage,
					OsCacheUtil.MARRIAGE_TO_GROUP);
		}

		return marriage;
	}

	/**
	 * 判断用户是否有足够的红包
	 */
	// 宾客送红包
	public boolean setRedBag(int money, int marriageId) {

		boolean result = false;
		String pledge = null;
		String marriageTime = null;
		UserStatusBean user = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		if (user.getGamePoint() < money || money < 0 || money == 0) {

			return result;

		} else {

			if (UserInfoUtil.updateUserStatus(
					"game_point= game_point-" + money, "user_id="
							+ loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "送红包扣钱" + money)) {
				FriendBagBean friendbag = new FriendBagBean();
				friendbag.setMarriageId(marriageId);
				friendbag.setPrice(money);
				friendbag.setUserId(loginUser.getId());
				getFriendService().addFriendBag(friendbag);
				result = true;
			}

			return result;

		}

	}

	/**
	 * 取消婚约是不是等于离婚，不举行了，还是离婚了 ,算已婚还是未婚
	 */
	public void cancelMarriage(int marriageId) {
		getFriendService().updateFriendMarriage("mark=1", "id=" + marriageId);
		// getFriendService().updateFriendMarriage("marriage_datetime=null",
		// "id=" + marriageId);
		FriendMarriageBean friendMarriage = friendService
				.getFriendMarriage("id=" + marriageId);

		// 发送取消婚约的消息
		int toId = friendMarriage.getToId();
		int fromId = friendMarriage.getFromId();
		UserBean toUser = UserInfoUtil.getUser(toId);
		UserBean fromUser = UserInfoUtil.getUser(fromId);
		// 个人婚否显示为0
		UserInfoUtil.updateUserStatus("mark=0", "user_id=" + fromId, fromId,
				UserCashAction.OTHERS, " 取消婚约更改状态位");
		UserInfoUtil.updateUserStatus("mark=0", "user_id=" + toId, toId,
				UserCashAction.OTHERS, " 取消婚约更改状态位");
		// 更新好友表的mark
		userService.updateFriend("mark=0", "user_id=" + fromId
				+ " and friend_id=" + toId);
		userService.updateFriend("mark=0", "user_id=" + toId
				+ " and friend_id=" + fromId);
		// 清除好友关系缓存
		userService.flushUserFriend(fromId, toId);

		ArrayList vec = (ArrayList) UserInfoUtil.getUserFriends(toId);
		for (int i = 0; i < vec.size(); i++) {
			String friendId = (String) vec.get(i);
			if (Integer.parseInt(friendId) != toId
					&& Integer.parseInt(friendId) != fromId) {
				NoticeBean notice = new NoticeBean();
				notice.setUserId(Integer.parseInt(friendId));
				notice.setTitle("您的好友" + toUser.getNickName() + "和"
						+ fromUser.getNickName() + "取消婚约了");
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				// notice.setLink(BaseAction.URL_PREFIX
				// + "/friend/redbag.jsp?marriageId="+marriageId);
				NoticeUtil.getNoticeService().addNotice(notice);
			}

		}

		vec = (ArrayList) UserInfoUtil.getUserFriends(fromId);
		for (int i = 0; i < vec.size(); i++) {
			String friendId = (String) vec.get(i);
			if (Integer.parseInt(friendId) != toId
					&& Integer.parseInt(friendId) != fromId) {
				NoticeBean notice = new NoticeBean();
				notice.setUserId(Integer.parseInt(friendId));
				notice.setTitle("您的好友" + toUser.getNickName() + "和"
						+ fromUser.getNickName() + "取消婚约了");
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				// notice.setLink(BaseAction.URL_PREFIX
				// + "/friend/redbag.jsp?marriageId="+marriageId);
				NoticeUtil.getNoticeService().addNotice(notice);
			}
		}
	}

	/**
	 * 吃喜糖，喜糖减少，宾客的乐币增加
	 */
	public void eatSuger(int price, int marriageId) {

		if (getFriendService().updateFriendGuest(
				"mark=1",
				"marriage_id=" + marriageId + " and user_id="
						+ loginUser.getId())) {
			UserInfoUtil.updateUserStatus("game_point= game_point+" + price,
					"user_id=" + loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "吃喜糖加钱" + price);
			getFriendService().updateFriendMarriage(
					"candy_remain=candy_remain-1", "id=" + marriageId);

			OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "id="
					+ marriageId);
		}
	}

	/**
	 * 添加宾客--只计一次
	 */
	public void addGuest(int marriageId)

	{
		FriendGuestBean friendGuest = getFriendService().getFriendGuest(
				"marriage_id=" + marriageId + " and user_id="
						+ loginUser.getId());
		if (friendGuest == null) {
			friendGuest = new FriendGuestBean();
			friendGuest.setMark(0);
			friendGuest.setMarriageId(marriageId);
			friendGuest.setUserId(loginUser.getId());
			getFriendService().addFriendGuest(friendGuest);

		}
	}

	/**
	 * 判断用户是否吃过喜糖
	 */
	public boolean eatOrNot(int marriageId) {
		boolean result = true;
		FriendGuestBean friendGuset = getFriendService().getFriendGuest(
				"user_id=" + loginUser.getId() + " and marriage_id="
						+ marriageId + " and mark=1");
		if (friendGuset != null)
			result = false;
		return result;
	}

	/**
	 * 添加祝福
	 */
	public void addReview(int marriageId, String review, int file) {
		// 未过期才可以添加评论
		if (marriageOver(marriageId) == 0) {
			FriendReviewBean friendReview = new FriendReviewBean();
			friendReview.setMarriageId(marriageId);
			friendReview.setReview(review);
			friendReview.setReviewUserId(loginUser.getId());
			friendReview.setFile(file);
			getFriendService().addFriendReview(friendReview);
		}
	}

	/**
	 * 我的祝福列表
	 * 
	 * @param request
	 */
	public void getReviewList(HttpServletRequest request) {
		int marriageId = getParameterInt("marriageId");
		String result = null;
		String review = request.getParameter("review");
		String reviewUser = request.getParameter("reviewUser");
		String infos = (String) session.getAttribute("info");
		String info = review + " " + reviewUser;
		if (!(info.equals(infos))) {
			if (review != null && !review.replace(" ", "").equals("")) {

				addReview(marriageId, review, 0);
				session.setAttribute("info", info);

			}

		} else {
			result = "包含相同的内容，不能连续发两遍.";
		}
		if (request.getParameter("action") != null) {
			String action = request.getParameter("action");
			FriendActionBean actionBean = null;
			if ("1".equals(action)) {
				if (getFriendService().getFriendGuest(
						"marriage_id=" + marriageId
								+ " and action1=0 and user_id="
								+ loginUser.getId()) != null) {
					actionBean = getFriendActionList(1);
					if (actionBean != null) {

						if (getFriendService().updateFriendGuest("action1=1",
								"user_id=" + loginUser.getId())) {
							addReview(marriageId, actionBean
									.getContent(), actionBean.getFile());
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"user_id=" + loginUser.getId()
											+ " and marriage_id=" + marriageId);
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"marriage_id=" + marriageId
											+ " and action1=0 and user_id="
											+ loginUser.getId());
						}

					}
				}

			} else if ("2".equals(action)) {
				if (getFriendService().getFriendGuest(
						"marriage_id=" + marriageId
								+ " and action2=0 and user_id="
								+ loginUser.getId()) != null) {
					actionBean = getFriendActionList(2);
					if (actionBean != null) {

						if (getFriendService().updateFriendGuest("action2=1",
								"user_id=" + loginUser.getId())) {
							addReview(marriageId, actionBean
									.getContent(), actionBean.getFile());
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"user_id=" + loginUser.getId()
											+ " and marriage_id=" + marriageId);
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"marriage_id=" + marriageId
											+ " and action2=0 and user_id="
											+ loginUser.getId());
						}

					}
				}

			} else if ("3".equals(action)) {
				if (getFriendService().getFriendGuest(
						"marriage_id=" + marriageId
								+ " and action3=0 and user_id="
								+ loginUser.getId()) != null) {
					actionBean = getFriendActionList(3);
					if (actionBean != null) {

						if (getFriendService().updateFriendGuest("action3=1",
								"user_id=" + loginUser.getId())) {
							addReview(marriageId, actionBean
									.getContent(), actionBean.getFile());
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"user_id=" + loginUser.getId()
											+ " and marriage_id=" + marriageId);
							OsCacheUtil.flushGroup(
									OsCacheUtil.FRIEND_GUEST_BEAN_GROUP,
									"marriage_id=" + marriageId
											+ " and action3=0 and user_id="
											+ loginUser.getId());
						}

					}
				}
			}
		}
		// 分页
		int NUM_PER_PAGE = 5;
		int totalCount = getFriendService().getFriendReviewCount(
				"marriage_id=" + marriageId);
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector reviewList = getFriendService().getFriendReviewList(
				"marriage_id=" + marriageId
						+ " order by create_datetime desc LIMIT " + pageIndex
						* NUM_PER_PAGE + "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("reviewList", reviewList);
		request.setAttribute("result", result);

	}

	/**
	 * 删除结婚评论
	 */
	public boolean deleteReview(HttpServletRequest request) {
		boolean del = false;
		int reviewId = StringUtil.toInt(request.getParameter("reviewId"));
		if (getFriendService().delFriendReview("id=" + reviewId)) {
			del = true;
		}
		return del;

	}

	/**
	 * 宾客红包统计，结束婚礼
	 */
	public static void statGuestAndBag() {
		Vector friendVec = friendService
				.getFriendMarriageList("mark=0 and guest_num=0 and redbag_num=0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW()");
		for (int i = 0; i < friendVec.size(); i++) {
			FriendMarriageBean friendMarriage = (FriendMarriageBean) friendVec
					.get(i);
			int guestCount = friendService.getFriendGuestCount("marriage_id="
					+ friendMarriage.getId());
			int redbagNum = friendService.getFriendBagCount("marriage_id="
					+ friendMarriage.getId());
			long bagValue = friendService.getFriendBagTotal("marriage_id="
					+ friendMarriage.getId());
			friendService.updateFriendMarriage("guest_num=" + guestCount + ",money="
					+ bagValue + ",redbag_num=" + redbagNum, "id="
					+ friendMarriage.getId());
			OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "id="
					+ friendMarriage.getId());
			OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP,
					"marriagecount_" + friendMarriage.getId());
			// 红包新郎新娘均分。
			if (UserInfoUtil.updateUserStatus("game_point= game_point+"
					+ bagValue / 2, "user_id=" + friendMarriage.getFromId(),
					friendMarriage.getFromId(), UserCashAction.OTHERS,
					"红包新郎新娘均分" + bagValue / 2 + "乐币"))
				UserInfoUtil.updateUserStatus("game_point= game_point+"
						+ bagValue / 2, "user_id=" + friendMarriage.getToId(),
						friendMarriage.getToId(), UserCashAction.OTHERS,
						"红包新郎新娘均分" + bagValue / 2 + "乐币");

		}
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_KINESCOPE_GROUP, "query");
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_NOW_GROUP, "query");
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "query");

		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and guest_num>0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() order by marriage_datetime ");
		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and  marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() order by marriage_datetime ");
		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime");
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
		StringBuilder sb = new StringBuilder();

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

	/**
	 * 判断婚礼时间超过24小时，是则结束婚礼
	 */
	public int marriageOver(int marriageId) {
		int del = 0;
		del = getFriendService().getFriendMarriageCount(
				"mark=0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() and id="
						+ marriageId);
		return del;

	}

	/**
	 * 个人家园查看某人的婚礼录像,是否结婚
	 */
	public int isMarriage(int userId) {
		int id = 0;
		// UserFriendBean userFriend = (UserFriendBean) getUserService()
		// .getUserFriend("mark=2 and user_id=" + loginUser.getId());
		// if (userFriend != null) {

		id = getFriendService().getFriendMarriageId(
				"mark=0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() and from_id="
						+ userId);
		if (id == 0)
			id = getFriendService().getFriendMarriageId(
					"mark=0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() and to_id="
							+ userId);

		// }
		return id;
	}

	/**
	 * 婚礼举行的24小时可以发表评论，删除评论
	 */
	public int isMarriageNow(int marriageId) {
		int id = StringUtil.toInt((String) OsCacheUtil.get("marriagecount_"
				+ marriageId, OsCacheUtil.MARRIAGE_TO_GROUP,
				OsCacheUtil.MARRIAGE_TO_FLUSH_PERIOD));
		if (id < 0) {
			id = getFriendService()
					.getFriendMarriageCount(
							"mark=0 and marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW()  and id="
									+ marriageId);
			OsCacheUtil.put("marriagecount_" + marriageId, id + "",
					OsCacheUtil.MARRIAGE_TO_GROUP);
		}

		return id;
	}

	public static void hourTask() {
		// liuyi 2006-10-29 结义邀请过期处理 start
		String set = "mark=4";
		String condition = "mark=0 and to_days(now()) - to_days(create_datetime)>=3";
		friendService.updateFriendUser(set, condition);
		// liuyi 2006-10-29 结义邀请过期处理 end
	}

	/**
	 * 得到某用户的结婚消息
	 * 
	 * 
	 */
	public FriendMarriageBean infoMarriage(int userId) {
		FriendMarriageBean friendMarriage = null;
		friendMarriage = getFriendService().getFriendMarriage("mark=0 and from_id=" + userId);
		if (friendMarriage == null)
			friendMarriage = getFriendService().getFriendMarriage("mark=0  and to_id=" + userId);
		return friendMarriage;
	}

	/**
	 * 离婚 user1 请求离婚 user2 接受离婚
	 * 
	 */

	/**
	 * 能离婚的条件
	 */
	public int divorce(int user1, int user2) {
		int count = SqlUtil
				.getIntResult(
						"select count(id) from user_friend where user_id="
								+ user2
								+ " and friend_id="
								+ user1
								+ " and (to_days(now())-to_days(update_datetime))>10 and mark=2",
						Constants.DBShortName);
		return count;
	}

	public void divorce(int type, int user1, int user2) {
		// 友好度清0
		UserFriendBean u1 = userService.getUserFriend(user1, user2);
		UserFriendBean u2 = userService.getUserFriend(user2, user1);
		if ((u1 != null && u1.getMark() != 2) && (u2 != null && u2.getMark() != 2)) {
			return;
		}
		// liuyi 2006-11-03 交友事务处理 start
		// if (userService.updateFriend(
		// "mark=0,level_value=0,update_datetime=now()", "user_id="
		// + user1 + " and friend_id=" + user2))
		// userService.updateFriend(
		// "mark=0,level_value=0,update_datetime=now()", "user_id="
		// + user2 + " and friend_id=" + user1);
		String[] sqls = new String[4];
		sqls[0] = "UPDATE user_friend SET mark=0,level_value=0 WHERE " + "user_id=" + user1
				+ " and friend_id=" + user2;
		sqls[1] = "UPDATE user_friend SET mark=0,level_value=0 WHERE " + "user_id=" + user2
				+ " and friend_id=" + user1;
		sqls[2] = "UPDATE user_status SET mark=0 WHERE " + "user_id=" + user1;
		sqls[3] = "UPDATE user_status SET mark=0 WHERE " + "user_id=" + user2;
		SqlUtil.excuteWithinStraction(sqls, Constants.DBShortName);
		// liuyi 2006-11-03 交友事务处理 end

		// wucx 2006-10-31 更新社交指数 start
		if (UserInfoUtil.updateUserStatus("social=social-" + u1.getLevelValue(),
				"user_id=" + user1, user1, UserCashAction.OTHERS, "离婚减" + u1.getLevelValue()
						+ "社交指数"))
			UserInfoUtil.updateUserStatus("social=social-" + u2.getLevelValue(),
					"user_id=" + user2, user2, UserCashAction.OTHERS, "离婚减" + u2.getLevelValue()
							+ "社交指数");
		// wucx 2006-10-31 更新社交指数 end

		// 清除好友关系缓存
		userService.flushUserFriend(user1, user2);

		UserStatusBean from = UserInfoUtil.getUserStatus(user1);
		UserStatusBean to = UserInfoUtil.getUserStatus(user2);
		// macq_2006-11-27_添加存款缓存_strat
		StoreBean store1 = BankCacheUtil.getBankStoreCache(user1);
		StoreBean store2 = BankCacheUtil.getBankStoreCache(user2);
		// StoreBean store1 = getBankService().getStore("user_id=" + user1);
		// StoreBean store2 = getBankService().getStore("user_id=" + user2);
		// macq_2006-11-27_添加存款缓存_end
		if (type == 1) {
			// 取消婚姻
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user1 + " and to_id=" + user2);
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user2 + " and to_id=" + user1);
			// 自负担5万乐币的离婚手续费。
			if (from.getGamePoint() > 50000)
				UserInfoUtil.updateUserStatus("game_point= game_point-50000,mark=0", "user_id="
						+ user1, user1, UserCashAction.OTHERS, "协议离婚负担5万手续费");
			else if (store1 != null && store1.getMoney() > (50000 - from.getGamePoint()))

			{
				UserInfoUtil.updateUserStatus("game_point=0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");
				// store1
				// .setMoney(store1.getMoney() - from.getGamePoint()
				// + 50000);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money="
				// + store1.getMoney(), "user_id=" + user1, user1);
				// bankService.updateStore("money=" + store1.getMoney(),
				BankCacheUtil.updateBankStoreCacheById(50000 - from.getGamePoint(), user1, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// "user_id=" + user1);
				// mcq_2006-11-27_更新银行存款缓存_end
			} else if (store1 != null) {
				UserInfoUtil.updateUserStatus("game_point=0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");
				// store1.setMoney(0);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money=0", "user_id="
				// + user1, user1);
				BankCacheUtil.updateBankStoreCacheById((-1) * store1.getMoney(), user1, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// bankService.updateStore("money=0", "user_id=" + user1);
				// mcq_2006-11-27_更新银行存款缓存_end
			} else
				UserInfoUtil.updateUserStatus("game_point=0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");
			if (to.getGamePoint() > 50000)
				UserInfoUtil.updateUserStatus("game_point= game_point-50000,mark=0",

				"user_id=" + user2, user2, UserCashAction.OTHERS, "协议离婚负担5万手续费");
			else if (store2 != null && store2.getMoney() > (50000 - to.getGamePoint())) {
				UserInfoUtil.updateUserStatus("game_point=0,mark=0",

				"user_id=" + user2, user2, UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");
				// store2.setMoney(store2.getMoney() - to.getGamePoint() +
				// 50000);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money="
				// + store2.getMoney(), "user_id=" + user2, user2);
				BankCacheUtil.updateBankStoreCacheById(50000 - to.getGamePoint(), user2, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// bankService.updateStore("money=" + store2.getMoney(),
				// "user_id=" + user2);
				// mcq_2006-11-27_更新银行存款缓存_end
			} else if (store2 != null) {
				UserInfoUtil.updateUserStatus("game_point=0,mark=0", "user_id=" + user2, user2,
						UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");
				// store2.setMoney(0);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money=0", "user_id="
				// + user2, user2);
				BankCacheUtil.updateBankStoreCacheById((-1) * store2.getMoney(), user2, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// bankService.updateStore("money=0", "user_id=" + user2);
				// mcq_2006-11-27_更新银行存款缓存_end
			} else
				UserInfoUtil.updateUserStatus("game_point=0,mark=0", "user_id=" + user2, user2,
						UserCashAction.OTHERS, "协议离婚负担5万手续费,您的费用不够,乐币清零");

			// 给离婚者发消息
			UserBean toUser = (UserBean) UserInfoUtil.getUser(user2);

			NoticeBean notice = new NoticeBean();
			notice.setUserId(user1);
			notice.setTitle("恭喜，" + toUser.getNickName() + "答应了您的离婚！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/friend/marriage.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);
		} else if (type == 2) {
			// 取消婚姻
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user1 + " and to_id=" + user2);
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user2 + " and to_id=" + user1);
			UserInfoUtil.updateUserStatus("mark=0", "user_id=" + user1, user1,
					UserCashAction.OTHERS, "请求宣告离婚变单身");
			UserInfoUtil.updateUserStatus("mark=0", "user_id=" + user2, user2,
					UserCashAction.OTHERS, "请求宣告离婚变单身");
			NoticeBean notice = new NoticeBean();
			notice.setUserId(user2);
			UserBean fromUser = (UserBean) UserInfoUtil.getUser(user1);
			notice.setTitle(fromUser.getNickName() + "与你请求宣告离婚了！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/friend/marriage.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);
		} else if (type == 3) {

			// 同时您要赔偿他（她）当初你们定情时购买信物的乐币。
			FriendMarriageBean friendMarriage = getFriendService().getFriendMarriage(
					"mark=0 and from_id=" + user2 + " and to_id=" + user1);
			if (friendMarriage == null)
				friendMarriage = getFriendService().getFriendMarriage(
						"mark=0 and from_id=" + user1 + " and to_id=" + user2);
			int ringId = 0;
			if (friendMarriage != null) {
				ringId = friendMarriage.getFingerRingId();
			}
			// 取消婚姻
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user1 + " and to_id=" + user2);
			getFriendService().updateFriendMarriage("mark=2",
					"mark=0 and from_id=" + user2 + " and to_id=" + user1);
			FriendRingBean ring = getFriendRing(ringId);
			int price = ring.getPrice();
			if (from.getGamePoint() > price)
				UserInfoUtil.updateUserStatus("game_point= game_point-" + price + ",mark=0",
						"user_id=" + user1, user1, UserCashAction.OTHERS, "强行离婚赔偿他（她）当初定情时购买信物的"
								+ price + "乐币");
			else if (store1 != null && store1.getMoney() > (price - from.getGamePoint()))

			{
				UserInfoUtil.updateUserStatus("game_point= 0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "强行离婚赔偿他（她）当初定情时购买信物的" + price + "乐币,您的费用不够,乐币清零");
				// store1
				// .setMoney(store1.getMoney() - from.getGamePoint()
				// + price);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money="
				// + store1.getMoney(), "user_id=" + user1, user1);
				BankCacheUtil.updateBankStoreCacheById(price - from.getGamePoint(), user1, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// bankService.updateStore("money=" + store1.getMoney(),
				// "user_id=" + user1);
				// mcq_2006-11-27_更新银行存款缓存_end

			} else if (store1 != null) {
				UserInfoUtil.updateUserStatus("game_point= 0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "强行离婚赔偿他（她）当初定情时购买信物的" + price + "乐币,您的费用不够,乐币清零");
				// store1.setMoney(0);
				// mcq_2006-11-27_更新银行存款缓存_start
				// BankCacheUtil.updateBankStoreCacheById("money=0", "user_id="
				// + user1, user1);
				BankCacheUtil.updateBankStoreCacheById((-1) * store1.getMoney(), user1, 0,
						Constants.BANK_FRIEND_DIVORCE_TYPE);
				// bankService.updateStore("money=0", "user_id=" + user1);
				// mcq_2006-11-27_更新银行存款缓存_end
			} else
				UserInfoUtil.updateUserStatus("game_point= 0,mark=0", "user_id=" + user1, user1,
						UserCashAction.OTHERS, "强行离婚赔偿他（她）当初定情时购买信物的" + price + "乐币,您的费用不够,乐币清零");

			UserInfoUtil.updateUserStatus("game_point= game_point+" + price + ",mark=0", "user_id="
					+ user2, user2, UserCashAction.OTHERS, "强行离婚被赔偿当初定情时购买信物的" + price + "乐币");
			NoticeBean notice = new NoticeBean();

			notice.setUserId(user2);
			UserBean fromUser = (UserBean) UserInfoUtil.getUser(user1);

			notice.setTitle(fromUser.getNickName() + "与你强行离婚了！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/friend/marriage.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);

		}
		FriendMarriageBean bean = getFriendService().getFriendMarriage(
				"mark=2 and from_id=" + user1 + " and to_id=" + user2);
		if (bean == null)
			bean = getFriendService().getFriendMarriage(
					"mark=2 and from_id=" + user2 + " and to_id=" + user1);
		if (bean != null)
			OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "id=" + bean.getId());
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_KINESCOPE_GROUP, "query");
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_NOW_GROUP, "query");
		OsCacheUtil.flushGroup(OsCacheUtil.MARRIAGE_TO_GROUP, "query");
		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and guest_num>0 and ADDDATE(marriage_datetime,interval 24 HOUR)<NOW() order by marriage_datetime ");
		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and  marriage_datetime<now() and ADDDATE(marriage_datetime,interval 24 HOUR)>NOW() order by marriage_datetime ");
		OsCacheUtil
				.flushGroup(
						OsCacheUtil.MARRIAGE_TO_GROUP,
						"select id from jc_friend_marriage where mark=0 and marriage_datetime>now() order by marriage_datetime");
	}

	/**
	 * 判断是否是正在举行或举行完的婚姻
	 */

	// 获取同城异性用户列表(同城在线优先,同城不在线其次,其他最后)
	public void getFriendCity(HttpServletRequest request, String url) {
		String result = null;
		String tip = null;
		if (!loginUser.isFlagFriend()) {
			return;
		}
		int gender = getParameterIntS("gender");
		if (gender == -1 || 2 == (gender)) {
			gender = loginUser.getGender() == 0 ? 1 : 0;
		}
		FriendBean friend = getFriendService().getFriend(loginUser.getId());
		if (friend == null) {
			return;
		}
		Vector townsmanIsOnlineTotal = new Vector();
		Vector townsmanNoOnlineTotal = new Vector();
		List townsman = SqlUtil.getIntListCache(
				"select user_id from jc_friend where city = '" + StringUtil.toSql(friend.getCity()) + 
				"' and gender=" + gender, 15);

		boolean flag = false;
		for (int i = 0; i < townsman.size(); i++) {
			Integer iid = (Integer) townsman.get(i);
			if (iid.intValue() == loginUser.getId()) {
				continue;
			}
			flag = OnlineUtil.isOnline(iid.intValue() + "");
			if (flag) {
				townsmanIsOnlineTotal.add(iid);
			} else {
				townsmanNoOnlineTotal.add(iid);
			}
		}
		townsmanIsOnlineTotal.addAll(townsmanNoOnlineTotal);
		if(townsmanIsOnlineTotal.size() < 50) {
			List noTownsman = SqlUtil.getIntListCache(
					"select user_id from jc_friend where gender=" + gender + " limit 100", 15);
			townsmanIsOnlineTotal.addAll(noTownsman);
		}

		int totalCount1 = townsmanIsOnlineTotal.size();
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 == -1) {
			pageIndex1 = 0;
		}
		int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
		if (totalCount1 % NUMBER_PER_PAGE != 0) {
			totalPageCount1++;
		}
		if (pageIndex1 > totalPageCount1 - 1) {
			pageIndex1 = totalPageCount1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		String prefixUrl1 = url + ".jsp?gender=" + gender;
		// 取得要显示的消息列表x
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex1 = Math.min(start1, townsmanIsOnlineTotal.size());
		int endIndex1 = Math.min(start1 + end1, townsmanIsOnlineTotal.size());
		List genderList = new ArrayList();
		Iterator iter = townsmanIsOnlineTotal.subList(startIndex1, endIndex1).iterator();
		while(iter.hasNext()) {
			genderList.add(friendService.getFriend((Integer)iter.next()));
		}
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("genderList", genderList);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("gender", Integer.valueOf(gender));
		return;
	}

	/**
	 * 帅哥美女投票
	 */
	public void friendVote(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取参数
		int userId = getParameterInt("userId");
		// 判断参数是否正确
		if (userId <= 0) {
			result = "failure";
			tip = "查询失败.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取交友信息
		FriendBean friend = getFriendService().getFriend(userId);
		// 判断交友信息正确性
		if (friend == null || friend.getAttach() == null
				|| friend.getAttach().equals("")) {
			result = "failure";
			tip = "查询失败.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 从session中获取投票信息
		Vector photoVote = (Vector) session.getAttribute("photoVote");
		// 判断是否给该用户投票过
		if (photoVote != null && photoVote.contains(new Integer(userId))) {
			result = "failure";
			tip = "对不起,每个用户每天只能投同一个人一票";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		FriendVoteBean friendVote = getFriendService().getFriendVote(
				"user_id=" + userId);
		if (friendVote == null) {
			friendVote = new FriendVoteBean();
			friendVote.setUserId(userId);
			friendVote.setCount(1);
			getFriendService().addFriendVote(friendVote);
		} else {
			getFriendService().updateFriendVote("count=count+1",
					"user_id=" + userId);
		}
		// 判断session中投票信息是否为空
		if (photoVote == null) {
			photoVote = new Vector();
			photoVote.add(new Integer(userId));
			session.setAttribute("photoVote", photoVote);
		} else {
			photoVote.add(new Integer(userId));
		}
		request.setAttribute("result", "success");
		return;
	}

	public static void cartoonSend() {
		Vector list = friendService.getFriendList("attach_type=0");

		for (int i = 0; i < list.size(); i++) {
			FriendBean friend = (FriendBean) list.get(i);
			int a = friend.getGender() + 1;
			// int mark = RandomUtil.nextInt(2);
			// if (mark == 0) {
			//
			// a = friend.getGender() + 1;
			// }

			int b = 1;
			// RandomUtil.nextInt(10) + 1;
			String fileURL = a + "_" + b + ".gif";
			friendService.updateFriend("attach='" + fileURL + "' , attach_type=1",
					"user_id=" + friend.getUserId());
			NoticeBean notice = new NoticeBean();
			notice.setUserId(friend.getUserId());
			notice.setTitle("交友中心头像功能完成，选个漂亮的吧！");
			notice.setContent("");
			notice.setHideUrl("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setLink("/friend/editPhotoTemp.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);

		}

	}

	public FriendActionBean getFriendActionList(int typeId) {

		Vector list = (Vector) OsCacheUtil.get("type_id=" + typeId,
				OsCacheUtil.FRIEND_ACTION_BEAN_GROUP,
				OsCacheUtil.FRIEND_ACTION_FLUSH_PERIOD);
		FriendActionBean action = null;
		if (list == null)

			list = getFriendService().getFriendActionList("type_id=" + typeId);
		if (list != null) {
			int random = RandomUtil.nextInt(list.size());
			action = (FriendActionBean) list.get(random);
		}

		return action;

	}

	public void cartoon(HttpServletRequest request) {
		FriendCartoonBean cartoon1 = getFriendCartoon(1);
		FriendCartoonBean cartoon2 = getFriendCartoon(2);
		FriendCartoonBean cartoon3 = getFriendCartoon(3);
		Vector vec1 = getFriendCartoonList(1);
		Vector vec2 = getFriendCartoonList(2);
		Vector vec3 = getFriendCartoonList(3);

		request.setAttribute("cartoon1", cartoon1);
		request.setAttribute("cartoon2", cartoon2);
		request.setAttribute("cartoon3", cartoon3);
		request.setAttribute("cartoon1size", vec1.size() + "");
		request.setAttribute("cartoon2size", vec2.size() + "");
		request.setAttribute("cartoon3size", vec3.size() + "");
	}

	public Vector getFriendCartoonList(int typeId) {

		Vector list = (Vector) OsCacheUtil.get("type=" + typeId,
				OsCacheUtil.CARTOON_GROUP, OsCacheUtil.CARTOON_FLUSH_PERIOD);

		if (list == null)

		{
			list = getFriendService().getFriendCartoonList("type=" + typeId);
			OsCacheUtil.put("type=" + typeId, list, OsCacheUtil.CARTOON_GROUP);
		}

		return list;

	}

	public FriendCartoonBean getFriendCartoon(int typeId) {

		FriendCartoonBean cartoon = (FriendCartoonBean) OsCacheUtil.get("type="
				+ typeId + "_only", OsCacheUtil.CARTOON_GROUP,
				OsCacheUtil.CARTOON_FLUSH_PERIOD);

		if (cartoon == null)

		{
			cartoon = getFriendService().getFriendCartoon("type=" + typeId);
			OsCacheUtil.put("type=" + typeId + "_only", cartoon,
					OsCacheUtil.CARTOON_GROUP);
		}

		return cartoon;

	}

	public void moreCartoon(HttpServletRequest request) {
		int type = StringUtil.toInt((String) request.getParameter("type"));
		Vector vec = getFriendCartoonList(type);
		int totalCount = 0;
		if (vec == null) {
			return;
		}
		if (vec != null)
			totalCount = vec.size() - 1;
		if (totalCount < 0)
			totalCount = 0;
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
		String prefixUrl1 = "moreCartoon.jsp?type=" + type;
		// 取得要显示的消息列表x
		int start1 = pageIndex * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex = Math.min(start1 + 1, vec.size());
		int endIndex = Math.min(start1 + end1 + 1, vec.size());
		List cartoonList = vec.subList(startIndex, endIndex);
		request.setAttribute("totalPage", totalPageCount + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("prefixUrl", prefixUrl1);
		request.setAttribute("cartoonList", cartoonList);

	}

	/**
	 * liuyi 2007-01-23 生日前一天发贺卡和消息
	 */
	public static void sendBirthdayCard() {
		// 获取第二天生日的用户
		String date = DateUtil.formatDate(DateUtil.rollDate(new Date(), 1));
		date = date.substring(4);
		String sql = "select id from user_info where birthday like '%" + date
				+ "%' and birthday not like '1901%'";
		List userIdList = SqlUtil.getIntList(sql, Constants.DBShortName);

		if (userIdList != null) {
			for (int i = 0; i < userIdList.size(); i++) {
				Integer id = (Integer) userIdList.get(i);
				if (id == null)
					continue;

				int userId = id.intValue();
				UserBean user = UserInfoUtil.getUser(userId);
				if (user == null)
					continue;

				// 贺卡
				String mobile = user.getMobile();
				if (mobile != null && (mobile.startsWith("13") || mobile.startsWith("15"))) {
					HappyCardSendBean sendBean = new HappyCardSendBean();
					sendBean.setUserId(userId);
					sendBean.setMobile(mobile);
					sendBean.setSender("乐酷");
					sendBean.setReceiver(user.getNickName());

					sendBean.setCardId(birthdayCardId);
					sendBean.setMark(0);
					sendBean.setSuccessMark(0);
					sendBean.setSendMark(1);

					sendBean.setReceiverId(-1);
					sendBean.setInOrOutMark(1);

					sendBean.setNewUserMark(0);

					ServiceFactory.createJobService().addHappyCardSend(sendBean);
					sendBean = ServiceFactory.createJobService().getHappyCardSend(
							" mobile='" + StringUtil.toSql(mobile) + "' and user_id=" + userId
									+ " order by id desc limit 0,1");
					String pushMessage = "乐酷祝您生日快乐!";
					String url = "wap.joycool.net/jcadmin/happyCardDeal.jsp?id=" + sendBean.getId();
					SmsUtil.sendPush(pushMessage, mobile, url);
				}

				// 好友消息
				sql = "select friend_id from user_friend where user_id=" + userId;
				List friendIdList = SqlUtil.getIntList(sql, Constants.DBShortName);
				if (friendIdList != null) {
					for (int k = 0; k < friendIdList.size(); k++) {
						Integer fId = (Integer) friendIdList.get(k);
						if (fId == null)
							continue;

						int friendId = fId.intValue();

						String message = "明天生日:" + user.getNickName() + ",记得祝福哦.";
						NoticeBean notice = new NoticeBean();
						notice.setUserId(friendId);
						notice.setTitle(message);
						notice.setContent(message);
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("/user/ViewUserInfo.do?userId=" + user.getId());
						notice.setLink("/user/ViewUserInfo.do?userId="
								+ user.getId());
						noticeService.addNotice(notice);
					}
				}
			}
		}
	}
}
