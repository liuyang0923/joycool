/**
 *作者:macq
 *日期:2006-9-14
 *功能:我的家园Action
 */
package net.joycool.wap.action.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.friend.FriendAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.bean.home.HomeBean;
import net.joycool.wap.bean.home.HomeDiaryBean;
import net.joycool.wap.bean.home.HomeDiaryCat;
import net.joycool.wap.bean.home.HomeDiaryReviewBean;
//import net.joycool.wap.bean.home.HomeEnounce;
import net.joycool.wap.bean.home.HomeHitsBean;
import net.joycool.wap.bean.home.HomeImageBean;
import net.joycool.wap.bean.home.HomeImageTypeBean;
import net.joycool.wap.bean.home.HomeNeighborBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.bean.home.HomePhotoCat;
import net.joycool.wap.bean.home.HomePhotoReviewBean;
import net.joycool.wap.bean.home.HomeReviewBean;
import net.joycool.wap.bean.home.HomeTypeBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.home.HomeUserImageBean;
import net.joycool.wap.cache.CacheAdmin;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

public class HomeAction extends CustomAction{

	public static ArrayList homeOrder;

	public static HashMap hitsOrder;

	UserBean loginUser;

	static int NUMBER_PER_PAGE = 5;

	static int DIARY_NUMBER_PER_PAGE = 8;
	
	static int TOP_DIARY_NUMBER_PER_PAGE = 3;

	static int PHOTO_NUMBER_PER_PAGE = 2;

	static int PAGE_COUNT = 5;
	
	static int MAX_CAT_COUNT = 4;

	static HomeServiceImpl homeService = new HomeServiceImpl();

	static IUserService userService = ServiceFactory.createUserService();

	static IMessageService messageService = ServiceFactory.createMessageService();

	// mcq_2006-10-26_个人交友信息接口_start
	static IFriendService fService = ServiceFactory.createFriendService();

	// mcq_2006-10-26_个人交友信息接口_end

	public static IChatService chatService = ServiceFactory.createChatService();

	public static IFriendAdverService friendService = ServiceFactory
			.createFriendAdverService();

	public HomeAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
//		if (SqlUtil.isTest){
//			MAX_CAT_COUNT = 3;
//		}
	}

	public static IMessageService getMessageService() {
		return messageService;
	}

	public static HomeServiceImpl getHomeService() {
		return homeService;
	}

	public static IUserService getUserService() {
		return userService;
	}

	// mcq_2006-10-26_个人交友信息接口_start
	public IFriendService getFriendService() {
		return fService;
	}

	public static int getMAX_CAT_COUNT() {
		return MAX_CAT_COUNT;
	}

	public static void setMAX_CAT_COUNT(int mAXCATCOUNT) {
		MAX_CAT_COUNT = mAXCATCOUNT;
	}
	
	// ===宣言用图片===
	public static List picList = new ArrayList();
	static {
		picList.add("无");
		picList.add("无聊");
		picList.add("可爱");
		picList.add("开心");
		picList.add("高兴");
		picList.add("偷笑");
		picList.add("生气");
		picList.add("抓狂");
		picList.add("无奈");
		picList.add("痛哭");
		picList.add("诅咒");
		picList.add("晕~");
		picList.add("寒~");
		picList.add("疲倦");
		picList.add("羞愧");
		picList.add("惊讶");
		picList.add("欢呼");
		picList.add("喜爱");
		picList.add("得意");
		picList.add("恐惧");
		picList.add("囧~");
		picList.add("呼呼");
		picList.add("调皮");
		picList.add("亲亲");
		picList.add("疑惑");
		picList.add("沉默");
		picList.add("沮丧");
		picList.add("一般");
		picList.add("奋斗");
		picList.add("鄙视");
		picList.add("猪头");
	}
	
	public static List getPicList() {
		return picList;
	}
	
	// mcq_2006-10-26_个人交友信息接口_start
	/*
	 * 判断注册个人家园
	 */
	public void inputRegisterInfo(HttpServletRequest request) {
		String result = null;
		String tip = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getRank() < 3) {
			result = "failure";
			tip = "哈哈，这位仁兄,您的等级还不够,怕您没足够的经验经营家园呀,也怕您没有足够多的朋友捧场啊,还是先锻炼锻炼吧^_^ 3级以后再来哦.(5秒钟跳转)";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (loginUser.isFlagFriend()) {
			FriendBean friend = getFriendService().getFriend(loginUser.getId());
			result = "friend";
			request.setAttribute("friend", friend);
			request.setAttribute("result", result);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 注册个人家园提交处理
	 */
	public void registerHome(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接收页面所传参数
		if (loginUser == null) {
			result = "failure";
			tip = "页面过期";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int userId = loginUser.getId();
		String name = getParameterNoEnter("name");
		String mobile = request.getParameter("mobile");
		String city = getParameterNoEnter("city");
		String constellation1 = request.getParameter("constellation");
		int constellation = StringUtil.toInt(constellation1);
		String height1 = request.getParameter("height");
		int height = StringUtil.toInt(height1);
		String weight1 = request.getParameter("weight");
		int weight = StringUtil.toInt(weight1);
		String work = getParameterNoEnter("work");
		String personality1 = request.getParameter("personality");
		int personality = StringUtil.toInt(personality1);
		String marriage1 = request.getParameter("marriage");
		int marriage = StringUtil.toInt(marriage1);
		String aim1 = request.getParameter("aim");
		int aim = StringUtil.toInt(aim1);
		String friendCondition = getParameterNoEnter("friendCondition");
		// 判断输入项
		if (name == null || name.replace(" ", "").equals("")) {
			result = "failure";
			tip = "姓名不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (city == null || city.replace(" ", "").equals("")) {
			result = "failure";
			tip = "所在城市不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (mobile.length() != 11 || StringUtil.toLong(mobile) < 0) {
			result = "failure";
			tip = "手机号码输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (height < 50 || height > 250) {
			result = "failure";
			tip = "个人身高输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (weight < 0 || weight > 250) {
			result = "failure";
			tip = "个人体重输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (constellation < 0 || constellation > 11) {
			result = "failure";
			tip = "请选择星座";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (work == null || work.replace(" ", "").equals("")) {
			result = "failure";
			tip = "职业不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (personality < 0 || personality > 5) {
			result = "failure";
			tip = "请选择性格";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (marriage < 0 || marriage > 3) {
			result = "failure";
			tip = "请选择婚姻状况";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (aim < 0 || aim > 4) {
			result = "failure";
			tip = "请选择交友目的";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (friendCondition == null
				|| friendCondition.replace(" ", "").equals("")) {
			result = "failure";
			tip = "择友条件不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}

		// 判断用户是否已经开通个人家园
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + userId);
		// macq_2006-12-20_增加家园的缓存_end
		if (homeUser != null) {
			result = "failure";
			tip = "您已经开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 生成bean并set值
		homeUser = new HomeUserBean();
		homeUser.setUserId(userId);
		homeUser.setName(name);
		homeUser.setMobile(mobile);
		homeUser.setCity(city);
		homeUser.setConstellation(constellation);
		homeUser.setHeight(height);
		homeUser.setWeight(weight);
		homeUser.setWork(work);
		homeUser.setPersonality(personality);
		homeUser.setMarriage(marriage);
		homeUser.setAim(aim);
		homeUser.setGender(loginUser.getGender());
		homeUser.setAge(loginUser.getAge());
		homeUser.setFriendCondition(friendCondition);
		// homeUser.setTypeId(Constants.HOME_HOUSE_INIT_TYPE);
		// 保存创建个人家园到数据库
		// macq_2006-12-20_增加家园的缓存_start
		boolean flag = HomeCacheUtil.addHomeCache(homeUser);
		// boolean flag = getHomeService().addHomeUser(homeUser);
		// macq_2006-12-20_增加家园的缓存_end
		// macq_2006-10-26_判断个人交友信息是否建立,如果没有,自动建立交友信息_start
		if (flag) {
			ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_CREATE, "%1创建了%3", loginUser.getNickName(), "家园", "/home/home2.jsp?userId="+loginUser.getId());
			// wucx_2006-12-4_自动为主人产生新日记_start
			//HomeDiaryBean diary = new HomeDiaryBean();
			//diary.setContent("我开通家园拉，欢迎访问我的家园！");
			//diary.setTitel("欢迎欢迎！");
			////diary.setUserId(loginUser.getId());
			//getHomeService().addHomeDiary(diary);
			// 更新日记总数
			// macq_2006-12-11_增加家园的缓存_start
			//HomeCacheUtil.updateHomeCacheById(
			//		"diary_count=diary_count+1  ,last_modify_time = now()",
			//		"user_id=" + loginUser.getId(), loginUser.getId());
			// getHomeService().updateHomeUser(
			// "diary_count=diary_count+1 ,last_modify_time = now()",
			// "user_id=" + loginUser.getId());
			// macq_2006-12-11_增加家园的缓存_end
			// wucx_2006-12-4_自动为主人产生新日记_start
			if (!loginUser.isFlagFriend()) {
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
				friend.setAttach("");
				friend.setGender(loginUser.getGender());
				friend.setAge(loginUser.getAge());
				friend.setAgeType(LoadResource.getAgeType(loginUser.getAge()));
				flag = getFriendService().addFriend(friend);
				if (flag)
					flag = UserInfoUtil.updateUser("friend="
							+ Constants.USER_INFO_FRIEND_MARK, "id="
							+ loginUser.getId(), loginUser.getId() + "");
				if (flag) {
					loginUser.setFriend(Constants.USER_INFO_FRIEND_MARK);
				}
			} else {
				getFriendService().updateFriend(
						" name='" + StringUtil.toSql(name) + "',gender=" + loginUser.getGender()
								+ ", age=" + loginUser.getAge() + ",age_type= "
								+ LoadResource.getAgeType(loginUser.getAge())
								+ ",mobile='" + StringUtil.toSql(mobile) + "',city='" + StringUtil.toSql(city)
								+ "',constellation=" + constellation
								+ ",height=" + height + ",height_type= "
								+ LoadResource.getHeightType(height)
								+ ",weight=" + weight + ",weight_type= "
								+ LoadResource.getWeightType(weight)
								+ ",work='" + StringUtil.toSql(work) + "',personality="
								+ personality + ",marriage=" + marriage
								+ ",aim=" + aim + ",friend_condition='"
								+ StringUtil.toSql(friendCondition) + "'",
						"user_id=" + loginUser.getId());
			}
		}
		// macq_2006-10-26_判断个人交友信息是否建立,如果没有,自动建立交友信息_end
		// 更新用户创建过个人家园标志位
		UserInfoUtil.updateUser("home=" + Constants.USER_INFO_HOME_MARK, "id="
				+ loginUser.getId(), loginUser.getId() + "");
		// 更新用户session中的个人家园标志位
		// 注册个人家园赠送50点经验值
		RankAction.addPoint(loginUser, Constants.REGISTER_HOME_POINT);
		loginUser.setHome(Constants.USER_INFO_HOME_MARK);
		// 初始创建家园背景图片
		HomeUserImageBean userImage = new HomeUserImageBean();
		userImage.setUserId(userId);
		userImage.setImageId(Constants.HOME_HOUSE_INIT_BACKGROUND);
		userImage.setTypeId(Constants.HOME_IMAGE_BACKGROUND_TYPE);
		userImage.setHomeId(1);
		getHomeService().addHomeUserImage(userImage);
		result = "success";
		request.setAttribute("result", result);

		// 获取注册家园用户的所有好友
		Vector UserFriendList = getUserService().getUserFriendList(
				"user_id=" + userId);
		// 初始化bean
		UserFriendBean userFriend = null;
		HomeUserBean userFriendHome = null; // 循环判断添加好友为邻居
		for (int i = 0; i < UserFriendList.size(); i++) {
			userFriend = (UserFriendBean) UserFriendList.get(i); // 判断好友是否有家园
			// 判断是否自己加自己为邻居
			if (userId != userFriend.getFriendId()) {
				// macq_2006-12-20_增加家园的缓存_start
				userFriendHome = HomeCacheUtil.getHomeCache(userFriend
						.getFriendId());
				// userFriendHome = getHomeService().getHomeUser(
				// "user_id=" + userFriend.getFriendId());
				// macq_2006-12-20_增加家园的缓存_end
				// 如果好友有家园自动添加为邻居
				if (userFriendHome != null) {
					// 添加好友为我的邻居,并发送一封信件給好友
					this.addNeighbor(userId, userFriend.getFriendId());
				}
			}
		}

		// 获取所有加我为好友的用户,把我加为他们的邻居
		Vector friendUserList = getUserService().getUserFriendList(
				"friend_id=" + userId);
		// 初始化bean
		UserFriendBean friendUser = null;
		HomeNeighborBean neighborHome = null;
		HomeUserBean frienduserHome = null;
		// 循环判断添加好友为邻居
		for (int i = 0; i < friendUserList.size(); i++) {
			friendUser = (UserFriendBean) friendUserList.get(i);
			// 判断是否自己加自己为邻居
			if (userId != friendUser.getUserId()) {
				// 获取好友的家园
				// macq_2006-12-20_增加家园的缓存_start
				frienduserHome = HomeCacheUtil.getHomeCache(friendUser
						.getUserId());
				// frienduserHome = getHomeService().getHomeUser(
				// "user_id=" + friendUser.getUserId());
				// macq_2006-12-20_增加家园的缓存_end
				// 判断好友是否有家园
				if (frienduserHome != null) {
					// 查询该好友是否已经加我为邻居
					neighborHome = getHomeService().getHomeNeighbor(
							"user_id=" + friendUser.getUserId()
									+ " and neighbor_id=" + userId);
					// 如果好友有家园自动添加为邻居
					if (neighborHome == null) {
						neighborHome = new HomeNeighborBean();
						neighborHome.setUserId(friendUser.getUserId());
						neighborHome.setNeighborId(userId);
						getHomeService().addHomeNeighbor(neighborHome);
					}
				}
			}
		}

		// zhul_2006-09-21 用户注册家园的同时发一条本用户的交友广告 ,并在聊天大厅有公告start
		FriendAdverBean friendAdver = new FriendAdverBean();
		friendAdver.setUserId(loginUser.getId());
		//guip 2007-08-21交友修改，去掉征友拉！
		friendAdver.setTitle(city + loginUser.getAge() + "岁"
				+ (loginUser.getGender() == 0 ? "美女" : "帅哥"));
		//friendAdver.setTitle(city + loginUser.getAge() + "岁"
				//+ (loginUser.getGender() == 0 ? "美女" : "帅哥") + "征友啦~~");
		friendAdver.setSex(loginUser.getGender() == 0 ? 1 : 0);
		friendAdver.setAge(Constants.AGE18_20);
		friendAdver.setArea(0);
		friendAdver.setRemark(friendCondition);
		friendAdver.setCityno(loginUser.getCityno());
		friendAdver.setGender(loginUser.getGender());
		chatService.updateJCRoomContentCount("count=count+1",
				"room_id=10000000");

		friendService.addFriendAdver(friendAdver);
		friendAdver = friendService.getFriendAdver(" user_id="
				+ loginUser.getId() + " order by id desc ");
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
		chatService.addContent(jcRoomContent);
		// zhul_2006-09-21 用户注册家园的同时发一条本用户的交友广告，并在聊天大厅有公告 end
		return;
	}

	/*
	 * 判断修改个人家园
	 */
	public void editHome(HttpServletRequest request) {
		String result = null;
		String tip = null;
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if (us.getRank() < 3) {
			result = "failure";
			tip = "哈哈，这位仁兄,您的等级还不够,怕您没足够的经验经营家园呀,也怕您没有足够多的朋友捧场啊,还是先锻炼锻炼吧^_^ 3级以后再来哦.(3秒钟跳转)";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		if (homeUser == null) {
			result = "failure";
			tip = "对不起,没有查询到您所创建的家园,请选择创建家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("homeUser", homeUser);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 修改个人家园处理提交处理
	 */
	public void editCommit(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接收页面所传参数
		int userId = loginUser.getId();
		String name = getParameterNoEnter("name");
		String gender1 = request.getParameter("gender");
		int gender = loginUser.getGender();	// StringUtil.toInt(gender1);
		String age1 = request.getParameter("age");
		int age = StringUtil.toInt(age1);
		String mobile = request.getParameter("mobile");
		String city = getParameterNoEnter("city");
		String constellation1 = request.getParameter("constellation");
		int constellation = StringUtil.toInt(constellation1);
		String height1 = request.getParameter("height");
		int height = StringUtil.toInt(height1);
		String weight1 = request.getParameter("weight");
		int weight = StringUtil.toInt(weight1);
		String work = getParameterNoEnter("work");
		String personality1 = request.getParameter("personality");
		int personality = StringUtil.toInt(personality1);
		String marriage1 = request.getParameter("marriage");
		int marriage = StringUtil.toInt(marriage1);
		String aim1 = request.getParameter("aim");
		int aim = StringUtil.toInt(aim1);
		String friendCondition = getParameterNoEnter("friendCondition");
		// 判断输入项
		if (name == null || name.replace(" ", "").equals("")) {
			result = "failure";
			tip = "姓名不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (gender < 0 || gender > 1) {
			result = "failure";
			tip = "请选择性别";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (age < 0 || age > 200) {
			result = "failure";
			tip = "年龄输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (city == null || city.replace(" ", "").equals("")) {
			result = "failure";
			tip = "所在城市不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (mobile.length() != 11 || StringUtil.toLong(mobile) < 0) {
			result = "failure";
			tip = "手机号码输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (height < 50 || height > 250) {
			result = "failure";
			tip = "个人身高输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (weight < 0 || weight > 250) {
			result = "failure";
			tip = "个人体重输入有误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (constellation < 0 || constellation > 11) {
			result = "failure";
			tip = "请选择星座";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (work == null || work.replace(" ", "").equals("")) {
			result = "failure";
			tip = "职业不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (personality < 0 || personality > 5) {
			result = "failure";
			tip = "请选择性格";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (marriage < 0 || marriage > 3) {
			result = "failure";
			tip = "请选择婚姻状况";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (aim < 0 || aim > 4) {
			result = "failure";
			tip = "请选择交友目的";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		} else if (friendCondition == null
				|| friendCondition.replace(" ", "").equals("")) {
			result = "failure";
			tip = "择友条件不能为空";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}

		// 判断用户是否已经开通个人家园
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + userId);
		// macq_2006-12-20_增加家园的缓存_end
		if (homeUser == null) {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 保存数据库
		// macq_2006-12-11_增加家园的缓存_start
		boolean flag = HomeCacheUtil.updateHomeCacheById(
				" name='" + StringUtil.toSql(name) + "',gender=" + gender + ", age=" + age
						+ ",mobile='" + StringUtil.toSql(mobile) + "',city='" + StringUtil.toSql(city)
						+ "',constellation=" + constellation + ",height="
						+ height + ",weight=" + weight + ",work='" + StringUtil.toSql(work)
						+ "',personality=" + personality + ",marriage="
						+ marriage + ",aim=" + aim + ",friend_condition='"
						+ StringUtil.toSql(friendCondition) + "'", "id=" + homeUser.getId(),
				homeUser.getId());
		// boolean flag = getHomeService().updateHomeUser(
		// " name='" + name + "',gender=" + gender + ", age=" + age
		// + ",mobile='" + mobile + "',city='" + city
		// + "',constellation=" + constellation + ",height="
		// + height + ",weight=" + weight + ",work='" + work
		// + "',personality=" + personality + ",marriage="
		// + marriage + ",aim=" + aim + ",friend_condition='"
		// + friendCondition + "'", "id=" + homeUser.getId());
		if (flag) {
			if (!loginUser.isFlagFriend()) {
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
				friend.setAttach("");
				friend.setGender(gender);
				friend.setAge(age);
				friend.setAgeType(LoadResource.getAgeType(loginUser.getAge()));
				flag = getFriendService().addFriend(friend);
				if (flag)
					flag = UserInfoUtil.updateUser("friend="
							+ Constants.USER_INFO_FRIEND_MARK, "id="
							+ loginUser.getId(), loginUser.getId() + "");
				if (flag) {
					loginUser.setFriend(Constants.USER_INFO_FRIEND_MARK);
				}
			} else {
				flag = getFriendService().updateFriend(
						" name='" + StringUtil.toSql(name) + "',gender=" + gender + ", age="
								+ age + ",age_type= "
								+ LoadResource.getAgeType(age) + ",mobile='"
								+ StringUtil.toSql(mobile) + "',city='" + StringUtil.toSql(city)
								+ "',constellation=" + constellation
								+ ",height=" + height + ",height_type= "
								+ LoadResource.getHeightType(height)
								+ ",weight=" + weight + ",weight_type= "
								+ LoadResource.getWeightType(weight)
								+ ",work='" + StringUtil.toSql(work) + "',personality="
								+ personality + ",marriage=" + marriage
								+ ",aim=" + aim + ",friend_condition='"
								+ StringUtil.toSql(friendCondition) + "'",
						"user_id=" + loginUser.getId());
			}
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
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 修改个人家园处理提交处理
	 */
	public void home(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 发表日记
		String content = getParameterNoCtrl("content");
		String title = getParameterNoEnter("title");
		if (content != null && title != null) {
			if (content == null || content.trim().equals("")) {
				tip = "主题内容不能为空!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "info");
				return;
			}
			if (title == null || title.trim().equals("")) {
				tip = "主题标题不能为空!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "info");
				return;
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
			if(forbid != null) {
				doTip("info", "已经被封禁家园权限 - " + forbid.getBak());
				return;
			}
			String infos = (String) session.getAttribute("homeDiary");
			String info = content + " " + loginUser.getId();
			if (!(info.equals(infos))) {
				HomeDiaryBean homeDiary = new HomeDiaryBean();
				homeDiary.setUserId(loginUser.getId());
				homeDiary.setTitel(title);
				homeDiary.setContent(content);
				getHomeService().addHomeDiary(homeDiary);
				ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_DIARY, "%1发表日记%3", loginUser.getNickName(), title, "/home/homeDiary.jsp?userId="+loginUser.getId()+"&amp;diaryId="+homeDiary.getId());
				session.setAttribute("homeDiary", info);
				// 更新日记总数
				// macq_2006-12-11_增加家园的缓存_start
				HomeCacheUtil.updateHomeCacheById(
						"diary_count=diary_count+1  ,last_modify_time = now()",
						"user_id=" + loginUser.getId(), loginUser.getId());
				tip = "日记添加成功！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "addOk");
				return;
			} else {
				tip = "包含相同的标题或内容，不能连续发两遍.";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "addOk");
				return;
			}
		}
		// 删除日记
		String del = request.getParameter("del");
		if (del != null) {
			int delId = StringUtil.toInt(del);
			if (delId <= 0) {
				tip = "参数错误！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "info");
				return;
			}
			HomeDiaryBean homeDiary = getHomeService().getHomeDiary(
					"id=" + delId);
			if (homeDiary == null) {
				tip = "该片日记已不存在！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "delOk");
				return;
			}
			// 删除日记跟日记的回复
			deleteHomeDiary(delId, loginUser.getId());
			tip = "日记删除成功！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "delOk");
			return;
		}

		int userId = getParameterInt("userId");
		String in = request.getParameter("in");
		String homeId = request.getParameter("homeId");
		if (userId <= 0) {
			userId = loginUser.getId();
		}

		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + userId);
		// macq_2006-12-20_增加家园的缓存_end
		// 判断家园是否存在
		if (homeUser == null) {
			result = "failure";
			tip = "您还未开通个人家园";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", new Integer(userId));
			request.setAttribute("result", result);
		} else {
			boolean allow = true;
			if(loginUser.getId() != homeUser.getUserId())
			switch(homeUser.getAllow()) {
			case 2:
				{
				if(getUserService().getUserFriend(homeUser.getUserId(), loginUser.getId()) == null)
					allow = false;
				}
				break;
			case 3:
				allow = false;
				break;
			}
			if(!allow) {		// 不允许访问
				request.setAttribute("tip", "家园主人设置了家园保密，您还没有访问权限！");
				request.setAttribute("result", "info");
				return;
			}
			// 进入别人的家
			HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());
			if (in != null) {
				Vector homeVec = getUserHome(homeUser.getUserId());
				request.setAttribute("homeVec", homeVec);
				request.setAttribute("homeId", homeId);
				request.setAttribute("homeTypes", bean);
				// 是好友，则友好度加1
				UserFriendBean userFriend = getUserService().getUserFriend(
						loginUser.getId(), homeUser.getUserId());
				if (userFriend != null) {
					getUserService().addOrupdateFriendLevel(loginUser.getId(),
							homeUser.getUserId());
				}
			} else {
				request.setAttribute("homeType", bean);
			}
			// 更新房间当日访问次数(加一操作)
			// macq_2006-12-11_增加家园的缓存_start
			homeService.updateHomeUser(
					"hits=hits+1,total_hits=total_hits+1", "user_id="
							+ homeUser.getUserId());
			homeUser.setHits(homeUser.getHits() + 1);
			homeUser.setTotalHits(homeUser.getTotalHits() + 1);
			// getHomeService().updateHomeUser(
			// "hits=hits+1,total_hits=total_hits+1",
			// "user_id=" + homeUser.getUserId());
			// macq_2006-12-11_增加家园的缓存_end
			// int totalHits = homeUser.getHits()
			// + HomeHitsCacheUtil.getHomeHitsCount(homeUser.getUserId());
			// homeUser.setTotalHits(totalHits);

			// 日记的分页
			int totalCount = getHomeService().getHomeDiaryCount(
					"user_id=" + userId + " and del=0");
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / DIARY_NUMBER_PER_PAGE;
			if (totalCount % DIARY_NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}

			String prefixUrl = "home.jsp?userId=" + userId;

			// 取得要显示的消息列表
			int start = pageIndex * DIARY_NUMBER_PER_PAGE;
			int end = DIARY_NUMBER_PER_PAGE;
			Vector homeDiaryList = getHomeService().getHomeDiaryList(
					"user_id=" + userId + " and del=0 order by id desc limit " + start
							+ ", " + end);
			request.setAttribute("totalCount", new Integer(totalCount));
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("diaryIdList", homeDiaryList);
			// 判断查看的是否是自己的房间
			if (homeUser.getUserId() == loginUser.getId()) {
				result = "success";
				request.setAttribute("result", result);
				request.setAttribute("homeUser", homeUser);
			} else {
				// 查询别人的家园室主的信息
				UserBean user = UserInfoUtil.getUser(homeUser.getUserId());
				if (user == null) {
					result = "failure";
					tip = "查询的房间正在维护,请稍后访问!";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
				} else {
					result = "success";
					request.setAttribute("result", result);
					request.setAttribute("user", user);
					request.setAttribute("homeUser", homeUser);
				}
			}
		}
		return;
	}

	/*
	 * 管理个人家园
	 */
	public void homeManage(HttpServletRequest request) {
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		request.setAttribute("homeUser", homeUser);
		return;
	}

	/*
	 * 访客留言列表
	 */
	public void homeReview(HttpServletRequest request) {
		String noticeId = request.getParameter("noticeId");
		if (noticeId != null) {
			// macq_2006-12-11_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("notice=0", "user_id="
					+ loginUser.getId(), loginUser.getId());
			// getHomeService().updateHomeUser("notice=0",
			// "user_id=" + loginUser.getId());
			// macq_2006-12-11_增加家园的缓存_end
		}
		HttpSession session = request.getSession();
		String result = null;
		String tip = null;
		String review = getParameterNoEnter("review");
		int userId = getParameterInt("userId");
		HomeUserBean homeUser;
		if(userId > 0)
			homeUser = HomeCacheUtil.getHomeCache(userId);
		else if(loginUser.getHome() == 1) {
			userId = loginUser.getId();
			homeUser = HomeCacheUtil.getHomeCache(userId);
		} else {
			doTip("failure", "您还未开通个人家园.");
			return;
		}
		review = StringUtil.noEnter(review);
		if (review != null && review.length() > 0
				&& userId > 0) {
			if (session.getAttribute("homeReview") == null) {
				result = "refrush";
				setAttribute("userId", userId);
				request.setAttribute("result", result);
				return;
			}
			session.removeAttribute("homeReview");

			String infos = (String) session.getAttribute("homeinfoinfo");
			String info = review + " " + loginUser.getId();
			if (!(info.equals(infos))) {
				if (review != null && review.length() > 0) {

					if(homeUser != null && homeUser.getAllow() == 1) {
						if(homeUser.getUserId() != loginUser.getId() && getUserService().getUserFriend(homeUser.getUserId(), loginUser.getId()) == null) {
							request.setAttribute("tip", "你不是此家园主人的好友,无法在此留言.");
							request.setAttribute("result", "addSuccess");
							setAttribute("userId", userId);
							return;
						}
					}
					ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
					if(forbid != null) {
						doTip("addSuccess", "已经被封禁家园权限 - " + forbid.getBak());
						setAttribute("userId", userId);
						return;
					}
					HomeReviewBean homeReview = new HomeReviewBean();
					homeReview.setUserId(userId);
					homeReview.setReviewUserId(loginUser.getId());
					homeReview.setReview(review);
					getHomeService().addHomeReview(homeReview);
					homeUser.setReviewCount(homeUser.getReviewCount() + 1);
					SqlUtil.executeUpdate("update jc_home_user set review_count=" + homeUser.getReviewCount() + " where user_id=" + userId);
					result = "addSuccess";
					tip = "留言发表成功,3秒钟后自动返回";
					session.setAttribute("homeinfoinfo", info);
					// macq_2006-12-20_增加家园的缓存_start

					// HomeUserBean homeUser = getHomeService().getHomeUser(
					// "user_id=" + userId);
					// macq_2006-12-20_增加家园的缓存_end
					// if (homeUser != null && homeUser.getNotice() == 0) {
					if (homeUser != null && homeUser.getUserId() != loginUser.getId()) {
						// macq_2006-12-11_增加家园的缓存_start
						HomeCacheUtil.updateHomeCacheById("notice=1",
								"user_id=" + userId, userId);
						// getHomeService().updateHomeUser("notice=1",
						// "user_id=" + userId);
						// macq_2006-12-11_增加家园的缓存_end
						NoticeBean notice = new NoticeBean();
						notice.setUserId(userId);
						notice.setTitle("您的家园有新的评论！");
						notice.setContent("");
						notice.setHideUrl("/home/homeReview.jsp");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setLink("/home/homeReview.jsp");
						// macq_2007-5-16_增加普通消息类型_start
						notice.setMark(NoticeBean.HOME_REVIEW);
						// macq_2007-5-16_增加普通消息类型_end
						NoticeUtil.getNoticeService().addNotice(notice);

					}

				}

			} else {
				result = "addSuccess";
				tip = "包含相同的内容，不能连续发两遍.";
			}

			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			setAttribute("userId", userId);
			return;
		} else if(hasParam("delete")){
			int viewId=getParameterInt("delete");
			if(viewId > 0 && homeService.deleteHomeReview("id=" + viewId) && homeUser.getReviewCount() > 0){
				homeUser.setReviewCount(homeUser.getReviewCount() - 1);
				SqlUtil.executeUpdate("update jc_home_user set review_count=" + homeUser.getReviewCount() + " where user_id=" + userId);
			}
		}
		PagingBean paging = new PagingBean(this, homeUser.getReviewCount(), 8, "p");

		// 取得要显示的消息列表
		int start = paging.getStartIndex();
		int end = paging.getCountPerPage();
		Vector homeReviewList = getHomeService().getHomeReviewList(
				"user_id=" + userId + " order by id desc limit " + start + ", "
						+ end);
		request.setAttribute("paging", paging);
		request.setAttribute("homeReviewList", homeReviewList);
		setAttribute("userId", userId);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 浏览日记列表或发表日记
	 */
	public void diaryList(HttpServletRequest request) {

		String result = null;
		String tip = null;
		String title = getParameterNoEnter("title");
		String content = getParameterNoCtrl("content");
		String userId = request.getParameter("userId");
		if (userId == null){
			userId = request.getParameter("uid");
		}
		
		if (title != null && title.length() > 0
				&& title.length() <= 25
				&& content != null && content.length() > 0
				&& userId != null
				&& request.getMethod().equalsIgnoreCase("post")) {
			if (session.getAttribute("diaryList") == null) {
				result = "refrush";
				request.setAttribute("userId", userId);
				request.setAttribute("result", result);
				return;
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
			if(forbid != null) {
				doTip("addSuccess", "已经被封禁家园权限 - " + forbid.getBak());
				request.setAttribute("userId", userId);
				return;
			}
			session.removeAttribute("diaryList");
			String infos = (String) session.getAttribute("homeDiary");
			String info = content + " " + loginUser.getId();
			int catId = this.getParameterInt("catId");
			if (catId == 0){
				catId = this.getParameterInt("cid");
			}
			if (!(info.equals(infos))) {
				
				if (content.length() > 1000){
					content = content.substring(0, 1000);
				}
				HomeDiaryBean homeDiary = new HomeDiaryBean();
				homeDiary.setUserId(StringUtil.toInt(userId));
				homeDiary.setTitel(title);
				homeDiary.setContent(content);
				homeDiary.setCatId(catId);
				getHomeService().addHomeDiary(homeDiary);
				
				//添加日记动态 add by leihy 08-12-21
				ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_DIARY, "%1发表日记%3", loginUser.getNickName(), title, "/home/homeDiary.jsp?userId="+loginUser.getId()+"&amp;diaryId="+homeDiary.getId());
				
				// 相应的分类数量+1
				if (catId == 0){
					// 默认分类
					SqlUtil.executeUpdate("update jc_home_user set diary_def_count = diary_def_count + 1 where user_id=" + loginUser.getId(),0);
				} else {
					SqlUtil.executeUpdate("update jc_home_diary_cat set count=count+1 where id=" + catId, 0);
				}
				
				// 更新日记总数
				// macq_2006-12-11_增加家园的缓存_start
				HomeCacheUtil.updateHomeCacheById(
						"diary_count=diary_count+1  ,last_modify_time = now()",
						"user_id=" + StringUtil.toInt(userId), StringUtil
								.toInt(userId));
				// getHomeService().updateHomeUser(
				// "diary_count=diary_count+1 ,last_modify_time = now()",
				// "user_id=" + StringUtil.toInt(userId))
				// macq_2006-12-11_增加家园的缓存_end
				result = "addSuccess";
				tip = "日记发表成功";
				request.setAttribute("tip", tip);
				request.setAttribute("result", result);
				request.setAttribute("userId", userId);
				request.setAttribute("catId", new Integer(catId));
				// session.setAttribute("addSuccess","ok");
				return;
			} else {
				tip = "包含相同的标题或内容，不能连续发两遍.";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "addSuccess");
				request.setAttribute("userId", userId);
				return;
			}
		}
		if (userId == null) {
			if (loginUser.getHome() == 1) {
				userId = String.valueOf(loginUser.getId());
			} else {
				result = "failure";
				tip = "您还未开通个人家园.";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		int totalCount = 0;
		
		if(this.hasParam("cid")){
			int catId = this.getParameterInt("cid");
			
			totalCount = getHomeService()
			.getHomeDiaryCount("user_id=" + userId + " and cat_id = " + catId + " and del=0");
		} else {
			totalCount = getHomeService()
			.getHomeDiaryCount("user_id=" + userId + " and del=0");
		}
		
		
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / DIARY_NUMBER_PER_PAGE;
		if (totalCount % DIARY_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String prefixUrl = "homeDiaryList.jsp?userId=" + userId;
		
		// 取得要显示的消息列表
		int start = pageIndex * DIARY_NUMBER_PER_PAGE;
		int end = DIARY_NUMBER_PER_PAGE;
		
		//根据日记薄id取得日记列表
		
		
		Vector homeDiaryList = null;
		boolean flag = false;
		int catId = this.getParameterInt("cid");
		HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
		HomeDiaryCat catBean = null;
		if (catId == 0){
			catBean = new HomeDiaryCat();
			catBean.setCatName("默认分类");
			catBean.setPrivacy(HomeDiaryCat.PRIVACY_ALL);
			catBean.setUid(this.getLoginUser().getId());
		} else {
			catBean = homeServiceImpl.getHomeDiaryCat(catId);
		}
			
			
		if(catBean == null) {
			flag = false;
		} else if(catBean.getUid() == loginUser.getId()) {
			flag = true;
		} else {
			if(catBean.getPrivacy() == HomeDiaryCat.PRIVACY_FRIEND) {
				ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
				flag = userFriends.contains(userId + "");
			} else if(catBean.getPrivacy() == HomeDiaryCat.PRIVACY_ALL) {
				flag = true;
			}
		}
			
		homeDiaryList = getHomeService().getHomeDiaryList(
					"user_id=" + userId + " and cat_id = " + catId + " and del=0 order by id desc limit " + start + ", "
					+ end);
		if(!this.hasParam("cid"))	
			prefixUrl += "&amp;cid="+catId;
		
			
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeDiaryList", homeDiaryList);
		request.setAttribute("userId", userId);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("flag", new Boolean(flag));
		request.setAttribute("catName", catBean.getCatName());
		return;
	}

	/*
	 * 浏览具体一篇日记的内容
	 */
	public void diary(HttpServletRequest request) {
		String orderBy = request.getParameter("orderBy");
		if(orderBy==null || "null".equals(orderBy)){
			orderBy="home";
		}
		request.setAttribute("orderBy", orderBy);
		String result = null;
		String tip = null;
		String diaryId = request.getParameter("diaryId");
		String userId = request.getParameter("userId");
		if (userId == null) {
			if (loginUser.getHome() == 1) {
				userId = String.valueOf(loginUser.getId());
			} else {
				result = "failure";
				tip = "您还未开通个人家园.";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		HomeDiaryBean homeDiary = null;
		if (diaryId == null) {
			result = "failure";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", userId);
			return;
		} else {
			homeDiary = getHomeService().getHomeDiary("id=" + diaryId);
		}
		if (homeDiary == null || homeDiary.getDel() == 1) {
			result = "noExist";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", userId);
			return;
		}
		
		HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
		HomeDiaryCat catBean = null;
		
		if (homeDiary.getCatId() == 0){
			catBean = new HomeDiaryCat();
			catBean.setCatName("默认分类");
			catBean.setPrivacy(HomeDiaryCat.PRIVACY_ALL);
			catBean.setUid(homeDiary.getUserId());
		} else {
			catBean = homeServiceImpl.getHomeDiaryCat(homeDiary.getCatId());
		}
		
		if (catBean == null ) {
			result = "noExist";
			tip = "分类不存在.";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", userId);
			return;
		}
		
		//增加日记薄权限浏览 start
		boolean flag = false;
		if(homeDiary.getUserId() == loginUser.getId()) {
			flag = true;
		} else {
			if(catBean.getPrivacy() == HomeDiaryCat.PRIVACY_FRIEND) {
				ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
				flag = userFriends.contains(userId + "");
			} else if(catBean.getPrivacy() == HomeDiaryCat.PRIVACY_ALL) {
				flag = true;
			}
		}
		request.setAttribute("flag", new Boolean(flag));
		//增加日记薄权限浏览 end
		
		if(this.hasParam("editor")) {
			int catId = this.getParameterInt("catId");
			if (catId == 0){
				catId = this.getParameterInt("cid");
			}
			int diarId = this.getParameterInt("diaryId");
		}
		
		// 删除评论
		String delete = request.getParameter("delete");
		if (delete != null) {
			if (StringUtil.toInt(userId) == loginUser.getId()) {
				this.deleteHomeDiaryReview(request);
				result = "delOk";
				tip = "评论删除成功！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("diaryId", diaryId);
				request.setAttribute("result", result);
				return;
			}
		}
		// 添加评论
		String review = getParameterNoEnter("review");
		if (review != null && !review.replace(" ", "").equals("")
				&& userId != null && diaryId != null
				&& request.getMethod().equalsIgnoreCase("post")) {
			String infos = (String) session.getAttribute("homeinfo");
			String info = review + " " + loginUser.getId();
			if (!(info.equals(infos))) {
				if (review != null && !review.replace(" ", "").equals("")) {

					HomeUserBean homeUser = HomeCacheUtil
							.getHomeCache(StringUtil.toInt(userId));
					if(homeUser != null && homeUser.getAllow() == 1) {
						if(homeUser.getUserId() != loginUser.getId() && getUserService().getUserFriend(homeUser.getUserId(), loginUser.getId()) == null) {
							request.setAttribute("tip", "你不是此家园主人的好友,无法在此留言.");
							request.setAttribute("result", "addSuccess");
							request.setAttribute("userId", userId);
							request.setAttribute("diaryId", diaryId);
							return;
						}
					}
					ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
					if(forbid != null) {
						doTip("addSuccess", "已经被封禁家园权限 - " + forbid.getBak());
						request.setAttribute("userId", userId);
						request.setAttribute("diaryId", diaryId);
						return;
					}
					HomeDiaryReviewBean homeDiaryReview = new HomeDiaryReviewBean();
					homeDiaryReview.setDiaryId(StringUtil.toInt(diaryId));
					homeDiaryReview.setReviewUserId(loginUser.getId());
					homeDiaryReview.setReview(review);
					getHomeService().addHomeDiaryReview(homeDiaryReview);
					// zhul 2006-10-11 同步更新home_diary表的review_count字段 start
					getHomeService().updateHomeDiary(
							"review_count=review_count+1", "id=" + diaryId);
					// zhul 2006-10-11 同步更新home_diary表的review_count字段 end
					result = "addSuccess";
					tip = "评论发表成功,3秒钟后自动返回";
					session.setAttribute("homeinfo", info);
					// macq_2006-12-20_增加家园的缓存_start
					// HomeUserBean homeUser = getHomeService().getHomeUser(
					// "user_id=" + userId);
					// macq_2006-12-20_增加家园的缓存_end
					if (homeUser != null && homeUser.getNotice() == 0 && homeUser.getUserId() != loginUser.getId()) {
						// macq_2006-12-11_增加家园的缓存_start
						HomeCacheUtil.updateHomeCacheById("notice=1",
								"user_id=" + userId, StringUtil.toInt(userId));
						// getHomeService().updateHomeUser("notice=1",
						// "user_id=" + userId);
						// macq_2006-12-11_增加家园的缓存_end
						NoticeBean notice = new NoticeBean();
						notice.setUserId(StringUtil.toInt(userId));
						notice.setTitle("您的家园有新的评论！");
						notice.setContent("");
						notice.setHideUrl("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setLink("/home/homeDiary.jsp?userId=" + userId
								+ "&diaryId=" + diaryId + "&mynotice=3");
						NoticeUtil.getNoticeService().addNotice(notice);

					}

				}

			} else {
				tip = "包含相同的内容，不能连续发两遍.";
				result = "addSuccess";
			}

			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", userId);
			request.setAttribute("diaryId", diaryId);
			return;
		}
		// 增加点击次数
		if (loginUser.getId() != homeDiary.getUserId()) {
			String set = "hits = (hits + 1),daily_hits=daily_hits+1";
			String condition = "id = " + diaryId;
			getHomeService().updateHomeDiary(set, condition);
			homeDiary.setHits(homeDiary.getHits() + 1);
		}
		//取得上一条和下一条
		String prevCondition = null;
		String nextCondition = null;
		HomeDiaryBean prevDiary = null;
		HomeDiaryBean nextDiary = null;
		int prev = 0;
		int next = 0;

		if("id".equals(orderBy)){
			String sql = " jc_home_diary where del=0 order by id desc";
			DbOperation dbOp = new DbOperation();
			dbOp.init(Constants.DBShortName);
			try {
				SqlUtil.createRownum(dbOp,sql);
				int rownum = SqlUtil.getRownumById(dbOp,homeDiary.getId());
				prev = SqlUtil.getIdByRownum(dbOp,rownum - 1);
				next = SqlUtil.getIdByRownum(dbOp,rownum + 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbOp.release();
			}
			if (prev > 0) {
				prevDiary = getHomeService().getHomeDiary("id="+prev);
			}
			if (next > 0) {
				nextDiary = getHomeService().getHomeDiary("id="+next);
			}
		}else if("mark".equals(orderBy)){
			String sql = " jc_home_diary_top order by id desc";
			DbOperation dbOp = new DbOperation();
			dbOp.init(Constants.DBShortName);
			try {
				SqlUtil.createRownum(dbOp,sql);
				int rownum = SqlUtil.getRownumById(dbOp,homeDiary.getId());
				prev = SqlUtil.getIdByRownum(dbOp,rownum - 1);
				next = SqlUtil.getIdByRownum(dbOp,rownum + 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbOp.release();
			}
			if (prev > 0) {
				prevDiary = getHomeService().getHomeDiary("id="+prev);
			}
			if (next > 0) {
				nextDiary = getHomeService().getHomeDiary("id="+next);
			}
		}else if ("hits".equals(orderBy)){
			String sql = " jc_home_diary where del=0 order by hits desc";
			DbOperation dbOp = new DbOperation();
			dbOp.init(Constants.DBShortName);
			try {
				SqlUtil.createRownum(dbOp,sql);
				int rownum = SqlUtil.getRownumById(dbOp,homeDiary.getId());
				prev = SqlUtil.getIdByRownum(dbOp,rownum - 1);
				next = SqlUtil.getIdByRownum(dbOp,rownum + 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dbOp.release();
			}
			if (prev > 0) {
				prevDiary = getHomeService().getHomeDiary("id="+prev);
			}
			if (next > 0) {
				nextDiary = getHomeService().getHomeDiary("id="+next);
			}
		}else{
			prevCondition = "id < " + diaryId + " and user_id=" + userId
					+ " and del=0 ORDER BY id desc";
			nextCondition = "id > " + diaryId + " and user_id=" + userId
					+ " and del=0 ORDER BY id asc";
			prevDiary = getHomeService().getHomeDiary(prevCondition);
			nextDiary = getHomeService().getHomeDiary(nextCondition);
		}
		request.setAttribute("prevDiary", prevDiary);
		request.setAttribute("nextDiary", nextDiary);
		

		//获取评论总数
		int reviewCount = getHomeService().getHomeDiaryReviewCount(
				"diary_id=" + diaryId + " and del=0");
		request.setAttribute("homeDiary", homeDiary);
		request.setAttribute("reviewCount", reviewCount + "");
		request.setAttribute("result", result);
		request.setAttribute("userId", userId);
		request.setAttribute("diaryId", diaryId);
		// 评论分页显示
		int totalCount = getHomeService().getHomeDiaryReviewCount(
				"diary_id=" + diaryId + " and del=0");
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / DIARY_NUMBER_PER_PAGE;
		if (totalCount % DIARY_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		String prefixUrl = "homeDiary.jsp?userId=" + userId + "&amp;diaryId="
				+ diaryId+"&amp;orderBy="+orderBy;

		// 取得要显示的消息列表
		int start = pageIndex * DIARY_NUMBER_PER_PAGE;
		int end = DIARY_NUMBER_PER_PAGE;
		Vector homeDiaryReviewList = getHomeService().getHomeDiaryReviewList(
				"diary_id=" + diaryId + " and del=0 order by id desc limit " + start
						+ ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeDiaryReviewList", homeDiaryReviewList);
		request.setAttribute("userId", userId);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("catName", catBean != null?catBean.getCatName():"");
		return;
	}

	/**
	 *  
	 * @author macq
	 * @explain：续写日记
	 * @datetime:2007-7-6 10:59:03
	 * @param request
	 * @return void
	 */
	public void diaryEdit(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int diaryId = StringUtil.toInt(request.getParameter("diaryId"));
		if (diaryId <= 0) {
			result = "failure";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		HomeDiaryBean homeDiary = getHomeService().getHomeDiary("id=" + diaryId);
		if (homeDiary == null) {
			result = "failure";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (homeDiary.getUserId()!= loginUser.getId()) {
			result = "failure";
			tip = "您不是该篇日记的作者,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		request.setAttribute("homeDiary", homeDiary);
		result = "success";
		request.setAttribute("result", result);
		return;
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：
	 * @datetime:2007-7-6 11:10:39
	 * @param request
	 * @return void
	 */
	public void diaryResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		
		
		
		int diaryId = StringUtil.toInt(request.getParameter("diaryId"));
		
		
		
		if (diaryId <= 0) {
			result = "failure";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		HomeDiaryBean homeDiary = getHomeService().getHomeDiary("id=" + diaryId);
		if (homeDiary == null) {
			result = "failure";
			tip = "没有查询到该篇日记,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		if (homeDiary.getUserId()!= loginUser.getId()) {
			result = "failure";
			tip = "您不是该篇日记的作者,3秒钟后自动返回";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		
		if(this.hasParam("catId")) {
			int catId = this.getParameterInt("catId");
			HomeUserBean homeUser = HomeCacheUtil.getHomeCache(homeDiary.getUserId());
			if (catId != homeDiary.getCatId() && homeUser != null){
				homeDiary.getCatId();
				getHomeService().updateHomeDiary("cat_id = " + catId,
						"id=" + diaryId);
				// 原分类-1
				if (homeDiary.getCatId() == 0){
					SqlUtil.executeUpdate("update jc_home_user set diary_def_count=diary_def_count-1 where diary_def_count>0 and user_id=" + homeDiary.getUserId(), 0);
					if (homeUser.getDiaryDefCount() > 0){
						homeUser.setDiaryDefCount(homeUser.getDiaryDefCount() - 1);
					}
				} else {
					SqlUtil.executeUpdate("update jc_home_diary_cat set count=count-1 where count>0 and id=" + homeDiary.getCatId(), 0);
				}
				// 新分类+1
				if (catId == 0){
					homeUser.setDiaryDefCount(homeUser.getDiaryDefCount() + 1);
					SqlUtil.executeUpdate("update jc_home_user set diary_def_count=diary_def_count+1 where user_id=" + homeDiary.getUserId(), 0);
				} else {
					SqlUtil.executeUpdate("update jc_home_diary_cat set count=count+1 where id=" + catId, 0);
				}
			}
			tip = "修改成功!";
			result = "success";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}	
		
		String contents = getParameterNoCtrl("content");
		if (contents == null || contents.trim().equals("")) {
			tip = "续写内容不能为空!";
			request.setAttribute("tip", tip);
			request.setAttribute("result", "success");
			return;
		}
		String changeDiaryTime = (String) request.getSession().getAttribute("changeDiaryTime");
		if (changeDiaryTime != null) {
			long time = StringUtil.toLong(changeDiaryTime);
			long cTime = System.currentTimeMillis();
			long count = cTime - time;
			if (count < 5 * 1000) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "请先休息一会再继续。");
				return;
			}
		}
		request.getSession().setAttribute("changeDiaryTime", System.currentTimeMillis()+ "");
		StringBuilder sb = new StringBuilder();
		sb.append(homeDiary.getContent());
		sb.append("\n\r");
		sb.append(contents);
		getHomeService().updateHomeDiary("content='" + StringUtil.toSql(sb.toString()) + "'",
				"id=" + homeDiary.getId());
		request.setAttribute("homeDiary", homeDiary);
		tip = "续写成功!";
		request.setAttribute("tip", tip);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	
	/*
	 * 评论发表的日记
	 */
	public void diaryReview(HttpServletRequest request) {
		String noticeId = request.getParameter("mynotice");
		if (noticeId != null) {
			// macq_2006-12-11_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("notice=0", "user_id="
					+ loginUser.getId(), loginUser.getId());
			// getHomeService().updateHomeUser("notice=0",
			// "user_id=" + loginUser.getId());
			// macq_2006-12-11_增加家园的缓存_end
		}
		String result = null;
		String tip = null;
		String diaryId = request.getParameter("diaryId");
		String review = getParameterNoEnter("review");
		String userId = request.getParameter("userId");
		if (review != null && !review.replace(" ", "").equals("")
				&& userId != null && diaryId != null
				&& request.getMethod().equalsIgnoreCase("post")) {
			if (session.getAttribute("diaryReview") == null) {
				result = "refrush";
				request.setAttribute("diaryId", diaryId);
				request.setAttribute("userId", userId);
				request.setAttribute("result", result);
				return;
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
			if(forbid != null) {
				doTip("addSuccess", "已经被封禁家园权限 - " + forbid.getBak());
				request.setAttribute("userId", userId);
				request.setAttribute("diaryId", diaryId);
				return;
			}
			session.removeAttribute("diaryReview");
			String infos = (String) session.getAttribute("homeinfo");
			String info = review + " " + loginUser.getId();
			if (!(info.equals(infos))) {
				if (review != null && !review.replace(" ", "").equals("")) {

					HomeDiaryReviewBean homeDiaryReview = new HomeDiaryReviewBean();
					homeDiaryReview.setDiaryId(StringUtil.toInt(diaryId));
					homeDiaryReview.setReviewUserId(loginUser.getId());
					homeDiaryReview.setReview(review);
					getHomeService().addHomeDiaryReview(homeDiaryReview);
					// zhul 2006-10-11 同步更新home_diary表的review_count字段 start
					getHomeService().updateHomeDiary(
							"review_count=review_count+1", "id=" + diaryId);
					// zhul 2006-10-11 同步更新home_diary表的review_count字段 end
					result = "addSuccess";
					tip = "评论发表成功,3秒钟后自动返回";
					session.setAttribute("homeinfo", info);
					// macq_2006-12-20_增加家园的缓存_start
					HomeUserBean homeUser = HomeCacheUtil
							.getHomeCache(StringUtil.toInt(userId));
					// HomeUserBean homeUser = getHomeService().getHomeUser(
					// "user_id=" + userId);
					// macq_2006-12-20_增加家园的缓存_end
					if (homeUser != null && homeUser.getNotice() == 0 && homeUser.getUserId() != loginUser.getId()) {
						// macq_2006-12-11_增加家园的缓存_start
						HomeCacheUtil.updateHomeCacheById("notice=1",
								"user_id=" + userId, StringUtil.toInt(userId));
						// getHomeService().updateHomeUser("notice=1",
						// "user_id=" + userId);
						// macq_2006-12-11_增加家园的缓存_end
						NoticeBean notice = new NoticeBean();
						notice.setUserId(StringUtil.toInt(userId));
						notice.setTitle("您的家园有新的评论！");
						notice.setContent("");
						notice.setHideUrl("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setLink("/home/homeDiary.jsp?userId=" + userId
								+ "&diaryId=" + diaryId + "&mynotice=3");
						NoticeUtil.getNoticeService().addNotice(notice);

					}

				}

			} else {
				tip = "包含相同的内容，不能连续发两遍.";
				result = "addSuccess";
			}

			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			request.setAttribute("userId", userId);
			request.setAttribute("diaryId", diaryId);
			return;
		}
		HomeDiaryBean homeDiary = getHomeService()
				.getHomeDiary("id=" + diaryId);
		if (userId == null) {
			if (loginUser.getHome() == 1) {
				userId = String.valueOf(loginUser.getId());
			} else {
				result = "failure";
				tip = "您还未开通个人家园.";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		int totalCount = getHomeService().getHomeDiaryReviewCount(
				"diary_id=" + diaryId + " and del=0");
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

		String prefixUrl = "homeDiary.jsp?diaryId=" + diaryId
				+ "&amp;userId=" + userId;

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector homeDiaryReviewList = getHomeService().getHomeDiaryReviewList(
				"diary_id=" + diaryId + " and del=0 order by id desc limit " + start
						+ ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeDiaryReviewList", homeDiaryReviewList);
		request.setAttribute("userId", userId);
		request.setAttribute("homeDiary", homeDiary);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 装饰房屋
	 */
	public void adornmentRoom(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String userId = null;
		if (loginUser.getHome() == 1) {
			userId = String.valueOf(loginUser.getId());
		} else {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int homeTypeId = StringUtil.toInt((String) request
				.getParameter("homeTypeId"));
		int homeId = StringUtil.toInt((String) request.getParameter("homeId"));
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());
		String[] roomIds = bean.getRoomIds().split("_");
		// 根据类型得到房间
		Vector homeVec = new Vector();

		for (int i = 0; i < roomIds.length; i++) {
			// 获取房间类型
			HomeBean homeBean = getHomeService().getHome("id=" + roomIds[i]);

			if (homeBean != null)
				homeVec.add(homeBean);
		}
		// 未选择房间的时候
		if (homeTypeId < 0) {
			request.setAttribute("homeVec", homeVec);
			request.setAttribute("homeTypeId", homeTypeId + "");
			request.setAttribute("homeId", homeId + "");
			request.setAttribute("bean", bean);
			return;
		}
		if (getHomeService().getHome("type_id=" + homeTypeId) == null) {
			result = "error";
			tip = "您要装修的房间不存在.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (getHomeService().getHomeUserImage(
				"home_id=" + homeId + " and type_id=" + homeTypeId
						+ " and user_id=" + loginUser.getId()) == null) {
			result = "error";
			tip = " 您没有该房间.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}

		request.setAttribute("homeVec", homeVec);
		request.setAttribute("homeTypeId", homeTypeId + "");
		request.setAttribute("homeId", homeId + "");
		request.setAttribute("bean", bean);
		String imageId = request.getParameter("imageId");
		if (imageId != null && userId != null) {
			if (session.getAttribute("adornmentRoom") == null) {
				result = "refrush";
				request.setAttribute("result", result);
				return;
			}
			session.removeAttribute("adornmentRoom");

			HomeImageBean homeImage = getHomeService().getHomeImage(
					"id=" + imageId);
			if (homeImage == null) {
				result = "searchError";
				tip = "页面过期";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			int typeId = homeImage.getTypeId();
			int gamePoint = homeImage.getPrice();
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us.getGamePoint() < gamePoint) {
				result = "moneyError";
				tip = "您没有足够的钱更新家园图片,本次更新需" + gamePoint + "乐币,你现在只有"
						+ us.getGamePoint() + "乐币";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			HomeUserImageBean homeUserImage = getHomeService()
					.getHomeUserImage(
							"user_id=" + userId + " and type_id=" + typeId
									+ " and home_id=" + homeId);
			boolean flag = false;
			if (homeUserImage != null) {
				tip = "旧房间背景已原价卖出!";
				int oldImageId = homeUserImage.getImageId();
				homeImage = getHomeService().getHomeImage("id=" + oldImageId);
				UserInfoUtil.updateUserStatus("game_point=game_point+"
						+ homeImage.getPrice(), "user_id=" + loginUser.getId(),
						loginUser.getId(), UserCashAction.OTHERS,
						"更换房间家具图片如果用户有该类型家具对应乐币,先退还对应家具价格"
								+ homeImage.getPrice() + "乐币");
				flag = getHomeService().updateHomeUserImage(
						"image_id=" + imageId,
						"user_id=" + userId + " and type_id=" + typeId
								+ " and home_id=" + homeId);
			} else {
				tip = "";
				homeUserImage = new HomeUserImageBean();
				homeUserImage.setUserId(loginUser.getId());
				homeUserImage.setImageId(StringUtil.toInt(imageId));
				homeUserImage.setTypeId(typeId);
				homeUserImage.setHomeId(homeId);
				flag = getHomeService().addHomeUserImage(homeUserImage);
			}
			HomeBean home = getHomeService().getHome("type_id=" + typeId);

			if (flag) {
				UserInfoUtil.updateUserStatus("game_point=game_point-"
						+ gamePoint, "user_id=" + loginUser.getId(), loginUser
						.getId(), UserCashAction.OTHERS, "更换房间背景图片扣钱"
						+ gamePoint + "乐币");
				LoadResource.deleteHomeUserHomeImageList(loginUser.getId(),
						home.getId());
			}

			result = "buyOk";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("home", home);
			return;
		}

		int totalCount = getHomeService().getHomeImageCount(
				"type_id=" + homeTypeId);
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

		String prefixUrl = "adornmentRoom.jsp";

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector homeImangeList = getHomeService().getHomeImageList(
				"type_id=" + homeTypeId + " order by id desc limit " + start
						+ ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeImangeList", homeImangeList);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 购买家具
	 */
	public void myImage(HttpServletRequest request) {
		// String result = null;
		// String tip = null;
		// String userId = null;
		// // 判断得到用户id,如果用户没有家园提示用户建立家园
		// if (loginUser.getHome() == 1) {
		// userId = String.valueOf(loginUser.getId());
		// } else {
		// result = "failure";
		// tip = "您还未开通个人家园.";
		// request.setAttribute("result", result);
		// request.setAttribute("tip", tip);
		// return;
		// }
		// String imageId = request.getParameter("imageId");
		// if (imageId != null && userId != null) {
		// if (session.getAttribute("myImage") == null) {
		// result = "refrush";
		// request.setAttribute("result", result);
		// return;
		// }
		// session.removeAttribute("myImage");
		// int imageIdCheck = StringUtil.toInt(imageId);
		// if (imageIdCheck < 0) {
		// result = "parameterError";
		// request.setAttribute("result", result);
		// return;
		// }
		// HomeImageBean homeImage = getHomeService().getHomeImage(
		// "id=" + imageId);
		// if (homeImage == null) {
		// result = "searchError";
		// tip = "页面过期";
		// request.setAttribute("result", result);
		// request.setAttribute("tip", tip);
		// return;
		// }
		// int typeId = homeImage.getTypeId();
		// // 出售家具按原价的50%出售
		// int gamePoint = homeImage.getPrice() / 2;
		// // UserStatusBean us =
		// // UserInfoUtil.getUserStatus(loginUser.getId());
		// boolean flag = getHomeService().deleteHomeUserImage(
		// "type_id=" + typeId + " and user_id=" + userId);
		// if (flag) {
		// UserInfoUtil.updateUserStatus("game_point=game_point+"
		// + gamePoint, "user_id=" + loginUser.getId(), loginUser
		// .getId(), UserCashAction.OTHERS, "删除房间家具退还用户乐币"
		// + gamePoint + "乐币");
		// LoadResource.deleteHomeUserImageList(loginUser.getId());
		// }
		// result = "deleteOk";
		// request.setAttribute("result", result);
		// request.setAttribute("fitmentName", homeImage.getName());
		// request.setAttribute("gamePoint", gamePoint + "");
		// return;
		// }
		//
		// Vector homeUserImageList = (Vector) LoadResource
		// .getHomeUserImageList(loginUser.getId());
		// Vector homeImageTypeList = (Vector) getHomeService()
		// .getHomeImageTypeList("1=1");
		// request.setAttribute("homeUserImageList", homeUserImageList);
		// request.setAttribute("homeImageTypeList", homeImageTypeList);
		// result = "success";
		// request.setAttribute("result", result);
		// return;
	}

	/*
	 * 购买家具
	 */
	public void buyFitment(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String userId = null;
		if (loginUser.getHome() == 1) {
			userId = String.valueOf(loginUser.getId());
		} else {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}

		String imageId = request.getParameter("imageId");
		if (imageId != null && userId != null) {
			if (session.getAttribute("buyFitment") == null) {
				result = "refrush";
				request.setAttribute("result", result);
				return;
			}
			session.removeAttribute("buyFitment");
			HomeImageBean homeImage = getHomeService().getHomeImage(
					"id=" + imageId + " and mark=0");
			if (homeImage == null) {
				result = "searchError";
				tip = "页面过期";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// macq_2006-12-20_增加家园的缓存_start
			HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser
					.getId());
			// HomeUserBean homeUser = getHomeService().getHomeUser(
			// "user_id=" + loginUser.getId());
			// macq_2006-12-20_增加家园的缓存_end
			HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());
			int count = getHomeService().getHomeUserImageCount(
					"home_id=0 and user_id=" + loginUser.getId());
			if (count + 1 > bean.getGoods()) {
				result = "goodsError";
				tip = "对不起，您的房子太小放不下新物品了，先处理一下旧东西吧！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}

			int typeId = homeImage.getTypeId();
			int gamePoint = homeImage.getPrice();
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us.getGamePoint() < gamePoint) {
				result = "moneyError";
				tip = "您没有足够的钱更新家园图片,本次更新需" + gamePoint + "乐币,你现在只有"
						+ us.getGamePoint() + "乐币";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}

			// HomeUserImageBean homeUserImage = getHomeService()
			// .getHomeUserImage(
			// "user_id=" + userId + " and type_id=" + typeId);
			// boolean flag = false;
			// if (homeUserImage != null) {
			// tip = "旧家具已卖出";
			// int oldImageId = homeUserImage.getImageId();
			// homeImage = getHomeService().getHomeImage("id=" + oldImageId);
			// UserInfoUtil.updateUserStatus("game_point=game_point+"
			// + homeImage.getPrice(), "user_id=" + loginUser.getId(),
			// loginUser.getId(), UserCashAction.OTHERS,
			// "更换房间家具图片如果用户有该类型家具对应乐币,先退还对应家具价格"
			// + homeImage.getPrice() + "乐币");
			// flag = getHomeService().updateHomeUserImage(
			// "image_id=" + imageId,
			// "user_id=" + userId + " and type_id=" + typeId);
			// } else {
			// tip = "";
			// homeUserImage = new HomeUserImageBean();
			// homeUserImage.setUserId(loginUser.getId());
			// homeUserImage.setImageId(StringUtil.toInt(imageId));
			// homeUserImage.setTypeId(typeId);
			// flag = getHomeService().addHomeUserImage(homeUserImage);
			// }
			// if (flag) {
			// UserInfoUtil.updateUserStatus("game_point=game_point-"
			// + gamePoint, "user_id=" + loginUser.getId(), loginUser
			// .getId(), UserCashAction.OTHERS, "更换房间家具图片扣钱"
			// + gamePoint + "乐币");
			// LoadResource.deleteHomeUserImageList(loginUser.getId());
			// }

			if (UserInfoUtil.updateUserCash(loginUser.getId(), -gamePoint, UserCashAction.OTHERS, "更换房间家具图片扣钱" + gamePoint
					+ "乐币")) {
				HomeUserImageBean userbean = new HomeUserImageBean();
				userbean.setHomeId(0);
				userbean.setImageId(StringUtil.toInt(imageId));
				userbean.setUserId(loginUser.getId());
				userbean.setTypeId(typeId);
				getHomeService().addHomeUserImage(userbean);
				LoadResource.deleteHomeUserImageList(loginUser.getId());
				result = "buyOk";
				request.setAttribute("result", result);
				return;
			}

		}

		String typeId = request.getParameter("typeId");
		if (typeId == null || typeId.replace(" ", "").equals("")
				|| StringUtil.toInt(typeId) < 0) {
			result = "parameterError";
			request.setAttribute("result", result);
			return;
		}

		int totalCount = getHomeService().getHomeImageCount(
				"type_id =" + typeId + " and mark=0");
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

		String prefixUrl = "buyFitment.jsp?typeId=" + typeId;

		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector homeImageList = getHomeService().getHomeImageList(
				"type_id =" + typeId + " and mark=0 order by id desc limit "
						+ start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeImageList", homeImageList);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/*
	 * 获取用户信息
	 */
	public UserBean getUser(int userId) {
		// UserBean user = getUserService().getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		return user;
	}

	/*
	 * 获取家园图片信息
	 */
	public HomeImageBean getHomeImage(int imageId) {
		HomeImageBean homeImage = getHomeService()
				.getHomeImage("id=" + imageId);
		return homeImage;
	}

	/*
	 * 拆分时间模块
	 */
	public String getTime(String datetime) {
		StringBuilder sb = new StringBuilder();
		String[] dt = datetime.split(" ");
		String[] date = dt[0].split("-");
		sb.append(date[1]);
		sb.append("-");
		sb.append(date[2]);
		sb.append(" ");
		String[] time = dt[1].split(":");
		sb.append(time[0]);
		sb.append(":");
		sb.append(time[1]);
		return sb.toString();
	}

	/*
	 * 拆分时间模块
	 */
	public String getDate(String datetime) {
		StringBuilder sb = new StringBuilder();
		String[] dt = datetime.split(" ");
		String[] date = dt[0].split("-");
		sb.append(date[0]);
		sb.append("年");
		sb.append(date[1]);
		sb.append("月");
		sb.append(date[2]);
		sb.append("日");
		return sb.toString();
	}

	/**
	 * zhul 2006-09-14 取得点击率前十的图片
	 * 
	 * @param request
	 */
	public void getPopPhotos(HttpServletRequest request) {
		// 分页 zhul 2006-09-11
		int NUM_PER_PAGE = 5;
		int totalCount = getHomeService().getHomePhotoCount("0=0");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		Vector photos = getHomeService().getPhotoList(
				" 0=0 order by hits desc LIMIT " + pageIndex * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("photos", photos);

	}

	/**
	 * zhul 2006-09-15 显示图片评论
	 */
	public void photoReview(HttpServletRequest request) {
		String noticeId = request.getParameter("noticeId");
		if (noticeId != null) {
			// macq_2006-12-11_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("notice=0", "user_id="
					+ loginUser.getId(), loginUser.getId());
			// getHomeService().updateHomeUser("notice=0",
			// "user_id=" + loginUser.getId());
			// macq_2006-12-11_增加家园的缓存_end
		}

		// 取得参数
		int photoId = StringUtil.toInt(request.getParameter("photoId"));
		// 评论分页
		int NUM_PER_PAGE = 5;
		int totalCount = getHomeService().getHomePhotoReviewCount(
				"photo_id=" + photoId);
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector photoReviews = getHomeService().getHomePhotoReviewList(
				" photo_id=" + photoId + " order by id desc LIMIT " + pageIndex
						* NUM_PER_PAGE + "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("photoReviews", photoReviews);
		return;
	}

	/**
	 * zhul 2006-09-15 添加图片评论
	 * 
	 * @param request
	 */
	public void addReview(HttpServletRequest request) {

		String photoId = request.getParameter("photoId");
		String content = getParameterNoCtrl("content");

		int reviewUserId = loginUser == null ? -1 : loginUser.getId();

		HomePhotoReviewBean review = new HomePhotoReviewBean();
		review.setPhotoId(Integer.parseInt(photoId));
		review.setReviewUserId(reviewUserId);
		review.setReview(content);
		
		HomePhotoBean bean = getHomeService().getHomePhoto("id=" + photoId);
		if (bean != null) {
			// macq_2006-12-20_增加家园的缓存_start
			HomeUserBean homeUser = HomeCacheUtil
					.getHomeCache(bean.getUserId());
			if(homeUser != null && homeUser.getAllow() == 1) {
				if(homeUser.getUserId() != loginUser.getId() && getUserService().getUserFriend(homeUser.getUserId(), loginUser.getId()) == null) {
					return;
				}
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
			if(forbid != null) {
				return;
			}
			getHomeService().addHomePhotoReview(review);
			// macq_2006-12-20_增加家园的缓存_end
			if (homeUser != null && homeUser.getNotice() == 0 && homeUser.getUserId() != loginUser.getId()) {
				// macq_2006-12-11_增加家园的缓存_start
				HomeCacheUtil.updateHomeCacheById("notice=1", "user_id="
						+ bean.getUserId(), bean.getUserId());
				// getHomeService().updateHomeUser("notice=1",
				// "user_id=" + userId);
				// macq_2006-12-11_增加家园的缓存_end
				NoticeBean notice = new NoticeBean();
				notice.setUserId(bean.getUserId());
				notice.setTitle("您的家园有新的评论！");
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setLink("/home/photoReview.jsp?photoId=" + photoId
						+ "&noticeId=3");
				NoticeUtil.getNoticeService().addNotice(notice);

			}
		}

		// 为了增加图片的点击率，用户每发一条图片评论，点击率+1
		// 相片浏览总数加1武翠霞
		getHomeService()
				.updateHomePhoto(
						"daily_hits=daily_hits+1,hits=hits+1,review_count=review_count+1",
						"id=" + photoId);

		return;
	}

	/**
	 * zhul 2006-09-15 获取家园信息
	 * 
	 * @param request
	 */
	public void getHome(HttpServletRequest request) {
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId < 0) {
			return;
		}
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + userId);
		// macq_2006-12-20_增加家园的缓存_end
		UserBean user = null;
		if (homeUser != null) {
			user = getUserService().getUser("id=" + homeUser.getUserId());
		}

		request.setAttribute("homeUser", homeUser);
		request.setAttribute("user", user);
		return;
	}

	/**
	 * zhul 2006-09-15 给访问的家园增加浏览量
	 * 
	 * @param homeId
	 */
	public void addVisitCounts(String homeId) {
		// macq_2006-12-20_增加家园的缓存_start
		HomeCacheUtil.updateHomeCacheById(
				"hits=hits+1,total_hits=total_hits+1", " id=" + homeId,
				StringUtil.toInt(homeId));
		// getHomeService().updateHomeUser("hits=hits+1,total_hits=total_hits+1",
		// " id=" + homeId);
		// macq_2006-12-20_增加家园的缓存_end
	}

	/**
	 * zhul 2006-09-18 返回用户家园相册
	 * 
	 * @param request
	 */
	public void getMyAlbum(HttpServletRequest request) {

		String userId = request.getParameter("userId");
		if (userId == null) {
			if (loginUser.getHome() != 1)
				return;
			else
				userId = loginUser.getId() + "";
		}
		Vector photos = null;
		
		boolean flag = false;
		int catId = this.getParameterInt("cid");
		HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
		HomePhotoCat catBean = homeServiceImpl.getHomePhotoCat(catId);
			
		if(catBean == null) {
			flag = false;
		} else if(catBean.getUid() == loginUser.getId()) {
			flag = true;
		} else {
			if(catBean.getPrivacy() == HomePhotoCat.PRIVACY_FRIEND) {
				ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
				flag = userFriends.contains(userId + "");
			} else if(catBean.getPrivacy() == HomePhotoCat.PRIVACY_ALL) {
				flag = true;
			}
		}
		photos = getHomeService().getPhotoList(
				" user_id=" + userId + " and cat_id = " + catId + " order by id desc");
		
		
		request.setAttribute("photos", photos);
		
		request.setAttribute("flag", new Boolean(flag));
		return;
	}

	/**
	 * zhul 2006-09-18 删除用户图片
	 * 
	 * @param request
	 */
	public void deletePhoto(HttpServletRequest request) {

		int photoId = getParameterInt("photoId");
		int catId = getParameterInt("cid");
		HomePhotoBean photo = homeService.getHomePhoto("id=" + photoId);
		// wucx 2006-10-16 start 删除照片时同时删除评论和更新用户的图片总数
		if (photo != null && photo.getUserId() == this.getLoginUser().getId() && (getHomeService().deleteHomePhotoReview("photo_id=" + photoId))
				&& (getHomeService().deleteHomePhoto("id=" + photoId))) {
			// 更新相片总数
			// macq_2006-12-11_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("photo_count=photo_count-1",
					"photo_count>0 and user_id=" + loginUser.getId(), loginUser.getId());
			// getHomeService().updateHomeUser("photo_count=photo_count-1",
			// "user_id=" + loginUser.getId());
			// macq_2006-12-11_增加家园的缓存_end
			// wucx 2006-10-16 end删除照片时同时删除评论和更新用户的图片总数
			
			// 相应的分类总数－1
			if (catId == 0){
				SqlUtil.executeUpdate("update jc_home_user set photo_def_count=photo_def_count-1 where photo_def_count>0 and user_id=" + this.getLoginUser().getId(), 0);
			} else {
				SqlUtil.executeUpdate("update jc_home_photo_cat set count=count-1 where id=" + catId + " and count > 0 and uid=" + this.getLoginUser().getId(), 0);
			}
			
//			if(photo.getAttach() != null && photo.getAttach().length() > 5) {
//				File pic = new File(Constants.MYALBUM_FILE_PATH + photo.getAttach()); // "E:\\eclipse\\workspace\\joycool-portal\\img/home/"
//				// +
//				// attach);//
//				if (pic.exists())
//					pic.delete();
//			}
		}
	}

	/**
	 * zhul 2006-09-19 全部家园分类显示
	 * 
	 * @param request
	 */
	public void viewAllHome(HttpServletRequest request) {
		// 家园总数
		int totalHome = getHomeService().getHomeUserCount("0=0");
		request.setAttribute("totalHome", totalHome + "");
		// 今日之星
//		Vector todayStar = getHomeService().getHomeUserList("mark=1");
//		request.setAttribute("todayStar", todayStar);
		//推荐用户
		Vector commendStar = getHomeService().getHomeUserList("mark=2");
		request.setAttribute("commendStar", commendStar);

		// 推荐日记
		Vector commendDiary = (Vector) CacheAdmin.getFromCache("commendDiary",
				OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,
				OsCacheUtil.COMMEND_DIARYANDPHOTO_FLUSH_PERIOD);
		if (commendDiary == null || commendDiary.size() == 0) {
			commendDiary = getHomeService().getHomeDiaryTopList2("order by b.id desc limit 3");
			OsCacheUtil.put("commendDiary", commendDiary,
					OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP);
		}
		int PER_PAGE_DIARY = 3;
		int totalCount1 = commendDiary.size();
		int totalPage1 = (totalCount1 + PER_PAGE_DIARY - 1) / PER_PAGE_DIARY;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalPage1 - 1) {
			pageIndex1 = totalPage1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}

		int start = pageIndex1 * PER_PAGE_DIARY;
		int end = pageIndex1 * PER_PAGE_DIARY + PER_PAGE_DIARY;

		request.setAttribute("start", start + "");
		request.setAttribute("end", end + "");
		request.setAttribute("totalCount1", totalCount1 + "");
		request.setAttribute("totalPage1", totalPage1 + "");
		request.setAttribute("pageIndex1", pageIndex1 + "");
		request.setAttribute("commendDiary", commendDiary);

		// 推荐照片2张
		Vector commendPhoto = (Vector) CacheAdmin.getFromCache("commendPhoto",
				OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP,
				OsCacheUtil.COMMEND_DIARYANDPHOTO_FLUSH_PERIOD);
		if (commendPhoto == null || commendPhoto.size() == 0) {
			commendPhoto = getHomeService().getHomePhotoTopList2("order by b.id desc limit 2");
			OsCacheUtil.put("commendPhoto", commendPhoto,
					OsCacheUtil.COMMEND_DIARYANDPHOTO_GROUP);
		}
		request.setAttribute("commendPhoto", commendPhoto);
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：今日人气家园
	 * @datetime:2007-7-16 16:08:06
	 * @param request
	 * @return void
	 */
	public void hotHomeList(HttpServletRequest request) {
		//家园总数
		int totalHome = getHomeService().getHomeUserCount("1=1");
		// 人气家园
		int NUM_PER_PAGE = 10;
		int totalCount = totalHome;
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector hitsHome = getHomeService().getHomeUserList(
				" 1=1 order by hits desc LIMIT " + pageIndex * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("hitsHome", hitsHome);
	}

	/**
	 * ZHUL 2006-10-10 新成立家园
	 * 
	 * @param request
	 */
	public void viewNewHome(HttpServletRequest request) {
		// 最新家园
		int NUM_PER_PAGE = 10;
		int totalCount = getHomeService().getHomeUserCount(
				"(TO_DAYS(NOW())-TO_DAYS(create_datetime))<=7");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector newHome = getHomeService().getHomeUserList(
				" (TO_DAYS(NOW())-TO_DAYS(create_datetime))<=7 order by id desc LIMIT "
						+ pageIndex * NUM_PER_PAGE + "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("newHome", newHome);

	}

	/**
	 * zhul 2006-10-10 家园推荐相册
	 * 
	 * @param request
	 */
	public void viewCommendPhoto(HttpServletRequest request) {
		// 推荐相册
		int NUM_PER_PAGE = 5;
		int totalCount = getHomeService().getHomePhotoCount("mark=1 ");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector commendPhoto = getHomeService().getHomePhotoList(
				"mark=1 order by id desc limit " + pageIndex * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("commendPhoto", commendPhoto);

	}

	/**
	 * zhul 2006-09-19 搜索家园
	 * 
	 * @param request
	 */
	public void searchHome(HttpServletRequest request) {
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId<=0) {
			return;
		}
		UserBean user = UserInfoUtil.getUser(userId);
		HomeUserBean home = null;
		if (user != null) {
			// macq_2006-12-20_增加家园的缓存_start
			home = HomeCacheUtil.getHomeCache(user.getId());
			// home = getHomeService().getHomeUser("user_id=" + user.getId());
			// macq_2006-12-20_增加家园的缓存_end
		}
		request.setAttribute("user", user);
		request.setAttribute("homeUser", home);

	}

	/**
	 * zhul2006-09-20 获取家园人气排名
	 * 
	 * @return
	 */
	public ArrayList getHomeOrder() {
		if (homeOrder == null) {
			homeOrder = homeService.getHomeOrder();
		}

		return homeOrder;
	}

	/**
	 * zhul 2006-09-20 获取用户家园与人气的对应表
	 * 
	 * @return
	 */
	public HashMap getHitsOrder() {
		if (hitsOrder == null) {
			hitsOrder = new HashMap();
			for (int i = 0; i < getHomeOrder().size(); i++) {
				HomeHitsBean hit = (HomeHitsBean) getHomeOrder().get(i);
				hitsOrder.put(hit.getUserId() + "", hit.getHits() + "");
			}
		}
		return hitsOrder;
	}

	/**
	 * zhul 2006-09-20 返回用户家园邻居
	 * 
	 * @param request
	 */
	public void getNeighbors(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		if (userId == null) {
			if (loginUser.getHome() != 1)
				return;
			else
				userId = loginUser.getId() + "";
		}
		// 分页
		int NUM_PER_PAGE = 10;
		int totalCount = getHomeService().getHomeNeighborCount(
				"user_id=" + userId);
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector neighbors = getHomeService().getHomeNeighborList(
				" user_id=" + userId + " order by id desc LIMIT " + pageIndex
						* NUM_PER_PAGE + "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("neighbors", neighbors);

	}

	/**
	 * wucx 2006-10-10 我的邻居列表
	 * 
	 * @param request
	 */
	public void getNeighborsList(HttpServletRequest request) {
		String userId = request.getParameter("userId");
		if (userId == null) {
			if (loginUser.getHome() != 1)
				return;
			else
				userId = loginUser.getId() + "";
		}
		// 分页
		int NUM_PER_PAGE = 10;
		int totalCount = getHomeService().getHomeNeighborCount(
				"user_id=" + userId);
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String neighborId = getHomeService().getHomeNeighborID(
				"user_id=" + userId);
		Vector neighbors = getHomeService().getHomeUserList(
				"user_id IN(" + neighborId
						+ ") order by last_modify_time desc LIMIT " + pageIndex
						* NUM_PER_PAGE + "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("neighbors", neighbors);

	}

	/**
	 * zhul 2006-09-20删除邻居,同时将此好友删除
	 * 
	 * @param neighborId
	 * @return
	 */
	public boolean deleteNeighbor(int neighborId) {
		boolean del = false;
		if (getHomeService().deleteHomeNeighbor(
				"user_id=" + loginUser.getId() + " and neighbor_id="
						+ neighborId)) {
			getUserService().deleteFriend(loginUser.getId(), neighborId);
			del = true;
		}
		return del;
	}

	/**
	 * zhul 2006-09-20 加邻居
	 * 
	 * @param neighborId
	 * @return
	 */
	public boolean addNeighbor(int neighborId) {
		// 如果用户没有家园 退出
		if (loginUser.getHome() != 1)
			return false;
		boolean add = false;
		// 如果用户已经有此邻居，不做操作
		HomeNeighborBean neighbor = null;
		neighbor = getHomeService().getHomeNeighbor(
				"user_id=" + loginUser.getId() + " and neighbor_id="
						+ neighborId);
		if (neighbor != null)
			return true;
		// 将用户加为邻居
		neighbor = new HomeNeighborBean();
		neighbor.setUserId(loginUser.getId());
		neighbor.setNeighborId(neighborId);
		if (getHomeService().addHomeNeighbor(neighbor)) {
			add = true;
		}

		// 给用户发一邮件
//		MessageBean message = new MessageBean();
//		message.setFromUserId(loginUser.getId());
//		message.setToUserId(neighborId);
//		message.setContent("您好!我已经将你加为我的邻居，有空常来玩哦~");
//		message.setMark(0);
//		getMessageService().addMessage(message);

		return add;
	}

	/**
	 * mcq 2006-09-25 加邻居
	 * 
	 * @param userId
	 * @param neighborId
	 * @return
	 */
	public void addNeighbor(int userId, int ToUserId) {
		HomeNeighborBean neighbor = new HomeNeighborBean();
		neighbor.setUserId(userId);
		neighbor.setNeighborId(ToUserId);
//		MessageBean message = new MessageBean();
//		message.setFromUserId(userId);
//		message.setToUserId(ToUserId);
//		message.setContent("您好!我已经将你加为我的邻居，有空常来玩哦~");
//		message.setMark(0);
//		getMessageService().addMessage(message);
	}

	/**
	 * zhul 2006-09-21 判断用户是否有自建聊天室,有返回聊天室ID，没有返回-1
	 * 
	 * @param userId
	 * @return
	 */
	public int hasChatRoom(int userId) {
		JCRoomBean room = chatService.getJCRoom("creator_id=" + userId);
		if (room != null)
			return room.getId();
		return -1;
	}

	/**
	 * zhul 2006-09-22 增加图片点击率
	 * 
	 * @param photoId
	 * @return
	 */
	public boolean addPhotoHits(int photoId) {
		if (getHomeService().updateHomePhoto(
				"daily_hits=daily_hits+1,hits=hits+1", "id=" + photoId))
			return true;
		return false;
	}

	/**
	 * zhul 2006-09-19 全部家园日记分类显示
	 * 
	 * @param request
	 */
	public void viewHomeDiary(HttpServletRequest request) {
		// 人气家园日记
		int NUM_PER_PAGE = 5;
		int totalCount = getHomeService().getHomeDiaryCount("del=0");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector hitsDiary = getHomeService().getHomeDiaryList(
				" del=0 order by hits desc LIMIT " + pageIndex * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("hitsDiary", hitsDiary);

		// 最新家园日记
		int totalCount1 = totalCount;
		int totalPage1 = (totalCount1 + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalPage1 - 1) {
			pageIndex1 = totalPage1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		Vector newDiary = getHomeService().getHomeDiaryList(
				" del=0 order by id desc LIMIT " + pageIndex1 * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("totalCount1", totalCount1 + "");
		request.setAttribute("totalPage1", totalPage1 + "");
		request.setAttribute("pageIndex1", pageIndex1 + "");
		request.setAttribute("newDiary", newDiary);

	}

	/**
	 * zhul 2006-09-19 全部家园图片分类显示
	 * 
	 * @param request
	 */
	public void viewHomePhoto(HttpServletRequest request) {
		// 人气家园图片
		int NUM_PER_PAGE = 5;
		int totalCount = getHomeService().getHomePhotoCount("0=0");
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		Vector hitsPhoto = getHomeService().getHomePhotoList(
				" 0=0 order by hits desc LIMIT " + pageIndex * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", totalCount + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("hitsPhoto", hitsPhoto);

		// 最新家园图片
		int totalCount1 = totalCount;
		int totalPage1 = (totalCount1 + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalPage1 - 1) {
			pageIndex1 = totalPage1 - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}
		Vector newPhoto = getHomeService().getHomePhotoList(
				" 0=0 order by id desc LIMIT " + pageIndex1 * NUM_PER_PAGE
						+ "," + NUM_PER_PAGE);

		request.setAttribute("totalCount1", totalCount1 + "");
		request.setAttribute("totalPage1", totalPage1 + "");
		request.setAttribute("pageIndex1", pageIndex1 + "");
		request.setAttribute("newPhoto", newPhoto);

	}

	/**
	 * mcq 2006-10-10 推荐日记
	 * 
	 * @param request
	 */
	public void newDiaryTop(HttpServletRequest request) {
		String orderBy = request.getParameter("orderBy");
		if (orderBy == null) {
			orderBy = "id";
		}
		String sql = null;
		if ("hits".equals(orderBy)) {// 按人气
			sql = "del=0 order by hits desc";
		} else if ("mark".equals(orderBy)) {// 按推荐，特殊处理
			int totalCount = 1000;
			int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
			if (pageIndex == -1) {
				pageIndex = 0;
			}
			int totalPageCount = totalCount / DIARY_NUMBER_PER_PAGE;
			if (totalCount % DIARY_NUMBER_PER_PAGE != 0) {
				totalPageCount++;
			}
			if (pageIndex > totalPageCount - 1) {
				pageIndex = totalPageCount - 1;
			}
			if (pageIndex < 0) {
				pageIndex = 0;
			}
			String prefixUrl = "newDiaryTop.jsp?orderBy="+orderBy;
			// 取得要显示的消息列表
			int start = pageIndex * DIARY_NUMBER_PER_PAGE;
			int end = DIARY_NUMBER_PER_PAGE;
			Vector diaryList = getHomeService().getHomeDiaryTopList2("order by b.id desc limit " + start + ", " + end);
			request.setAttribute("totalPageCount", new Integer(totalPageCount));
			request.setAttribute("pageIndex", new Integer(pageIndex));
			request.setAttribute("prefixUrl", prefixUrl);
			request.setAttribute("diaryList", diaryList);
			request.setAttribute("orderBy", orderBy);
			return;
			
		} else {// 按时间
			sql = "del=0 order by id desc";
		}
		// 精华排序分页
		int totalCount = getHomeService().getHomeDiaryCount(sql);
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / DIARY_NUMBER_PER_PAGE;
		if (totalCount % DIARY_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "newDiaryTop.jsp?orderBy="+orderBy;
		// 取得要显示的消息列表
		int start = pageIndex * DIARY_NUMBER_PER_PAGE;
		int end = DIARY_NUMBER_PER_PAGE;
		Vector diaryList = getHomeService().getHomeDiaryList(
				sql + " limit " + start + ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("diaryList", diaryList);
		request.setAttribute("orderBy", orderBy);
		return;
	}

	/**
	 * mcq 2006-10-10 推荐日记
	 * 
	 * @param request
	 */
	public void diaryTop(HttpServletRequest request) {
		// 推荐日记分页
		int totalCount = 1000;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / TOP_DIARY_NUMBER_PER_PAGE;
		if (totalCount % TOP_DIARY_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "diaryTop.jsp?";
		// 取得要显示的消息列表
		int start = pageIndex * TOP_DIARY_NUMBER_PER_PAGE;
		int end = TOP_DIARY_NUMBER_PER_PAGE;
		Vector diaryMark = getHomeService().getHomeDiaryTopList2("order by b.id desc limit " + start + ", "
						+ end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("diaryMark", diaryMark);

		// 总人气排行分页
		int totalCount1 = getHomeService().getHomeDiaryTopCount(
				"1=1 and cat_id=0 order by hits desc");
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
		String prefixUrl1 = "diaryTop.jsp?";
		// 取得要显示的消息列表
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		Vector diaryTotalTop = getHomeService().getHomeDiaryTopList(
				"1=1 and cat_id=0 order by hits desc limit " + start1 + ", " + end1);
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("diaryTotalTop", diaryTotalTop);

		// 每日人气排行分页
		int totalCount2 = getHomeService().getHomeDiaryTopCount(
				"1=1 and cat_id=0 order by daily_hits desc");
		int pageIndex2 = StringUtil.toInt(request.getParameter("pageIndex2"));
		if (pageIndex2 == -1) {
			pageIndex2 = 0;
		}
		int totalPageCount2 = totalCount2 / NUMBER_PER_PAGE;
		if (totalCount2 % NUMBER_PER_PAGE != 0) {
			totalPageCount2++;
		}
		if (pageIndex2 > totalPageCount2 - 1) {
			pageIndex2 = totalPageCount2 - 1;
		}
		if (pageIndex2 < 0) {
			pageIndex2 = 0;
		}
		String prefixUrl2 = "diaryTop.jsp?";
		// 取得要显示的消息列表
		int start2 = pageIndex2 * NUMBER_PER_PAGE;
		int end2 = NUMBER_PER_PAGE;
		Vector diaryTop = getHomeService().getHomeDiaryTopList(
				"1=1 and cat_id=0 order by daily_hits desc limit " + start2 + ", " + end2);
		request.setAttribute("totalPageCount2", new Integer(totalPageCount2));
		request.setAttribute("pageIndex2", new Integer(pageIndex2));
		request.setAttribute("prefixUrl2", prefixUrl2);
		request.setAttribute("diaryTop", diaryTop);

		// 新增日记排行分页
		int totalCount3 = getHomeService().getHomeDiaryTopCount(
				"1=1 and cat_id=0 order by id desc");
		int pageIndex3 = StringUtil.toInt(request.getParameter("pageIndex3"));
		if (pageIndex3 == -1) {
			pageIndex3 = 0;
		}
		int totalPageCount3 = totalCount3 / NUMBER_PER_PAGE;
		if (totalCount3 % NUMBER_PER_PAGE != 0) {
			totalPageCount3++;
		}
		if (pageIndex3 > totalPageCount3 - 1) {
			pageIndex3 = totalPageCount3 - 1;
		}
		if (pageIndex3 < 0) {
			pageIndex3 = 0;
		}
		String prefixUrl3 = "diaryTop.jsp?";
		// 取得要显示的消息列表
		int start3 = pageIndex3 * NUMBER_PER_PAGE;
		int end3 = NUMBER_PER_PAGE;
		Vector diaryNew = getHomeService().getHomeDiaryTopList(
				"1=1 and cat_id=0 order by id desc limit " + start3 + ", " + end3);
		request.setAttribute("totalPageCount3", new Integer(totalPageCount3));
		request.setAttribute("pageIndex3", new Integer(pageIndex3));
		request.setAttribute("prefixUrl3", prefixUrl3);
		request.setAttribute("diaryNew", diaryNew);

		return;
	}

	/**
	 * mcq 2006-10-10 推荐照片
	 * 
	 * @param request
	 */
	public void photoTop(HttpServletRequest request) {
		// 推荐照片排行分页
		int totalCount = 100;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / PHOTO_NUMBER_PER_PAGE;
		if (totalCount % PHOTO_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "photoTop.jsp?";
		// 取得要显示的消息列表
		int start = pageIndex * PHOTO_NUMBER_PER_PAGE;
		int end = PHOTO_NUMBER_PER_PAGE;
		Vector photoMark = getHomeService().getHomePhotoTopList2(
				"order by b.id desc limit " + start + "," + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("photoMark", photoMark);

		// 总人气照片排行分页
		int totalCount1 = getHomeService().getHomePhotoTopCount(
				"1=1 and cat_id=0 order by hits desc");
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
		String prefixUrl1 = "photoTop.jsp?";
		// 取得要显示的消息列表
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		Vector photoTotalTop = getHomeService().getHomePhotoTopList(
				"1=1 and cat_id=0 order by hits desc limit " + start1 + ", " + end1);
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("photoTotalTop", photoTotalTop);

		// 每日人气照片排行分页
		int totalCount2 = getHomeService().getHomePhotoTopCount(
				"1=1 and cat_id=0 order by daily_hits desc");
		int pageIndex2 = StringUtil.toInt(request.getParameter("pageIndex2"));
		if (pageIndex2 == -1) {
			pageIndex2 = 0;
		}
		int totalPageCount2 = totalCount2 / NUMBER_PER_PAGE;
		if (totalCount2 % NUMBER_PER_PAGE != 0) {
			totalPageCount2++;
		}
		if (pageIndex2 > totalPageCount2 - 1) {
			pageIndex2 = totalPageCount2 - 1;
		}
		if (pageIndex2 < 0) {
			pageIndex2 = 0;
		}
		String prefixUrl2 = "photoTop.jsp?";
		// 取得要显示的消息列表
		int start2 = pageIndex2 * NUMBER_PER_PAGE;
		int end2 = NUMBER_PER_PAGE;
		Vector photoTop = getHomeService().getHomePhotoTopList(
				"1=1 and cat_id=0 order by daily_hits desc limit " + start2 + ", " + end2);
		request.setAttribute("totalPageCount2", new Integer(totalPageCount2));
		request.setAttribute("pageIndex2", new Integer(pageIndex2));
		request.setAttribute("prefixUrl2", prefixUrl2);
		request.setAttribute("photoTop", photoTop);

		// 新增照片排行分页
		int totalCount3 = getHomeService().getHomePhotoTopCount(
				"1=1 and cat_id=0 order by id desc");
		int pageIndex3 = StringUtil.toInt(request.getParameter("pageIndex3"));
		if (pageIndex3 == -1) {
			pageIndex3 = 0;
		}
		int totalPageCount3 = totalCount3 / NUMBER_PER_PAGE;
		if (totalCount3 % NUMBER_PER_PAGE != 0) {
			totalPageCount3++;
		}
		if (pageIndex3 > totalPageCount3 - 1) {
			pageIndex3 = totalPageCount3 - 1;
		}
		if (pageIndex3 < 0) {
			pageIndex3 = 0;
		}
		String prefixUrl3 = "photoTop.jsp?";
		// 取得要显示的消息列表
		int start3 = pageIndex3 * NUMBER_PER_PAGE;
		int end3 = NUMBER_PER_PAGE;
		Vector photoNew = getHomeService().getHomePhotoTopList(
				"1=1 and cat_id=0 order by id desc limit " + start3 + ", " + end3);
		request.setAttribute("totalPageCount3", new Integer(totalPageCount3));
		request.setAttribute("pageIndex3", new Integer(pageIndex3));
		request.setAttribute("prefixUrl3", prefixUrl3);
		request.setAttribute("photoNew", photoNew);

		return;
	}

	/**
	 * 数字分页
	 * 
	 * @param totalPageCount
	 * @param currentPageIndex
	 * @param prefixUrl
	 * @param addAnd
	 * @param separator
	 * @param pageIndex
	 * @param response
	 * @return
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
	 * wucx 2006-10-9 个人家园首页显示2个日记
	 * 
	 * @param request
	 */
	public Vector viewPersonHomeDiary(int uid) {
		Vector vec = homeService.getHomeDiaryList("user_id=" + uid
				+ " and del=0 order by create_datetime desc limit 0,2 ");
		return vec;
	}

	/**
	 * wucx 2006-10-9 个人家园首页显示1个图片
	 * 
	 * @param request
	 */
	public HomePhotoBean viewPersonHomePicture(int uid) {
		HomePhotoBean homePhoto = homeService.getHomePhoto("user_id=" + uid
				+ " order by create_datetime desc limit 0,1 ");
		return homePhoto;
	}

	/**
	 * wucx 2006-10-9 我的邻居日记更新总数
	 * 
	 * @param request
	 */
	public int neighborHomeDiary(int uid) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = df.format(c.getTime());

		String key = startDate + uid + "";
		String smyCount = (String) OsCacheUtil.get(key,
				OsCacheUtil.NEIGHBOR_DIARY_GROUP,
				OsCacheUtil.NEIGHBOR_DIARY_FLUSH_PERIOD);

		int count = 0;

		if (smyCount == null) {
			String neighborId = getHomeService().getHomeNeighborID(
					"user_id=" + uid);
			count = homeService.getHomeDiaryCount(" create_datetime> '"
					+ startDate + "' and user_id in(" + neighborId + ")");
			OsCacheUtil.put(key, count + "", OsCacheUtil.NEIGHBOR_DIARY_GROUP);
		} else
			count = Integer.parseInt(smyCount);

		return count;
	}

	/**
	 * wucx 2006-10-9 我的邻居相片更新总数
	 * 
	 * @param request
	 */
	public int neighborHomePicture(int uid) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startDate = df.format(c.getTime());
		String key = startDate + uid + "";
		String smyCount = (String) OsCacheUtil.get(key,
				OsCacheUtil.NEIGHBOR_PHOTO_GROUP,
				OsCacheUtil.NEIGHBOR_PHOTO_FLUSH_PERIOD);
		int count = 0;
		if (smyCount == null) {
			String neighborId = getHomeService().getHomeNeighborID(
					"user_id=" + uid);
			count = homeService.getHomePhotoCount(" create_datetime >  '"
					+ startDate + "' and user_id in(" + neighborId + ")");
			OsCacheUtil.put(key, count + "", OsCacheUtil.NEIGHBOR_PHOTO_GROUP);
		} else
			count = Integer.parseInt(smyCount);

		return count;
	}

	// 删除家园评论
	// public void deleteHomeReview(int userId,int reviewUserId)
	// {
	// homeService.deleteHomeReview("user_id="+userId+" and
	// review_user_id="+reviewUserId);

	// }
	public boolean deleteHomeReview(int id) {
		boolean del = false;
		if (getHomeService().deleteHomeReview("id=" + id)) {
			del = true;
		}
		return del;

	}

	// 删除日记
	public boolean deleteHomeDiary(int id, int userId) {
		boolean del = false;
		HomeDiaryBean diary = getHomeService().getHomeDiary("id=" + id);
		if (diary == null || diary.getUserId() != userId || diary.getDel() == 1){
			return del;
		}
		if (getHomeService().deleteHomeDiary("id=" + id)) {

			// 更新日记总数
			// macq_2006-12-20_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("diary_count=diary_count-1",
					"diary_count>0 and user_id=" + userId, userId);
			// getHomeService().updateHomeUser("diary_count=diary_count-1",
			// "user_id=" + userId);
			// macq_2006-12-10_增加家园的缓存_start
			
			
			// 相应分类的总数-1
			if (diary.getCatId() == 0){
				// 默认分类
				SqlUtil.executeUpdate("update jc_home_user set diary_def_count=diary_def_count-1 where diary_def_count>0 and user_id=" + diary.getUserId(),0);
			} else {
				SqlUtil.executeUpdate("update jc_home_diary_cat c,jc_home_diary d set c.`count` = c.`count`-1  where c.count>0 and d.id = " + id + " and d.cat_id = c.id" , 0);
			}
			del = true;
		}
		return del;

	}

	// 删除相片
	// 暂不使用该函数，使用deletePhoto
	public boolean deleteHomePhoto(int id, int userId) {
		boolean del = false;
		HomePhotoBean photo = getHomeService().getHomePhoto("id=" + id);
		if (photo == null || photo.getUserId() != userId){
			return del;
		}
		if (getHomeService().deleteHomePhoto("id=" + id)
				&& (getHomeService().deleteHomePhotoReview("photo_id=" + id))) {
			del = true;

			// 更新相片总数
			// macq_2006-12-20_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("photo_count=photo_count-1",
					"photo_count>0 and user_id=" + userId, userId);
			// getHomeService().updateHomeUser("photo_count=photo_count-1",
			// "user_id=" + userId);
			
			// 相应分类总数-1
			
			
			// macq_2006-12-20_增加家园的缓存_end
		}
		return del;
	}

	// 删除日记评论
	public boolean deleteHomeDiaryReview(HttpServletRequest request) {
		boolean del = false;
		int diaryReviewId = StringUtil.toInt(request
				.getParameter("diaryReviewId"));
		int diaryId = StringUtil.toInt(request.getParameter("diaryId"));
		HomeDiaryReviewBean diaryReview = getHomeService().getHomeDiaryReview("id="+diaryReviewId);
		if(diaryReview==null){
			return del;
		}
		if (getHomeService().deleteHomeDiaryReview("id=" + diaryReviewId)) {
			del = true;

			// 更新日记 评论总数
			getHomeService().updateHomeDiary("review_count=review_count-1",
					"id=" + diaryId);

		}
		return del;

	}

	// 删除相片评论
	public boolean deleteHomePhotoReview(HttpServletRequest request) {
		boolean del = false;
		int photoId = StringUtil.toInt(request.getParameter("photoId"));
		int reviewId = StringUtil.toInt(request.getParameter("reviewId"));
		if (getHomeService().deleteHomePhotoReview("id=" + reviewId)) {
			del = true;
			// 更新相片评论总数
			getHomeService().updateHomePhoto("review_count=review_count-1",
					"id=" + photoId);

		}
		return del;

	}

	// 每个小时，由系统对每个个人家园随机增加0－10的访问量。
	public static void setRandomHits() {
		IHomeService myhomeService = ServiceFactory.createHomeService();
		Vector vec = (Vector) myhomeService.getHomeUserList("1=1");
		for (int i = 0; i < vec.size(); i++) {
			int random = RandomUtil.nextInt(11);
			HomeUserBean homehits = (HomeUserBean) vec.get(i);
			int hits = homehits.getHits();
			int totalHits = homehits.getTotalHits();
			hits = hits + random;
			totalHits = totalHits + random;
			// macq_2006-12-11_增加家园的缓存_start
			HomeCacheUtil.updateHomeCacheById("hits=" + hits + ",total_hits="
					+ totalHits, "id=" + homehits.getId(), homehits.getId());
			// myhomeService.updateHomeUser("hits=" + hits + ",total_hits="
			// + totalHits, "id=" + homehits.getId());
		}

	}

	public Vector getHomeList(String condition) {
		Vector vec = getHomeService().getHomeList(condition);

		return vec;
	}

	public Vector getHomeTypeList(String condition) {
		Vector vec = getHomeService().getHomeTypeList(condition);
		return vec;
	}

	public HomeTypeBean getHomeType(String condition) {
		HomeTypeBean bean = getHomeService().getHomeType(condition);
		return bean;
	}

	public HomeBean getHome(String condition) {
		HomeBean bean = getHomeService().getHome(condition);
		return bean;
	}

	/**
	 * 更换房间
	 */
	public void buyHome(HttpServletRequest request) {
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		request.setAttribute("homeUser", homeUser);
		int totalCount = getHomeService().getHomeTypeCount("1=1");
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
		String prefixUrl = "buyHome.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		Vector homeTypeList = getHomeTypeList("1=1 order by id limit " + start
				+ ", " + end);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeTypeList", homeTypeList);
		request.setAttribute("homeUser", homeUser);
		session.setAttribute("buyHomeRefrush", "ok");
	}

	/**
	 * 更换房间结果页面
	 */
	public void buyHomeResult(HttpServletRequest request) {
		String tip = null;
		String result = null;
		// 取得参数
		int buy = StringUtil.toInt(request.getParameter("buy"));
		if (buy < 0) {
			tip = "参数错误";
			result = "addSuccess";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取用户家园信息
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		// 获取现在用户房子的外观
		HomeTypeBean homeUserOldType = getHomeType("id=" + homeUser.getTypeId());
		// 获取房屋外观
		HomeTypeBean bean = getHomeType("id=" + buy);
		if (bean != null) {
			// 获取用户状态信息
			UserStatusBean user = UserInfoUtil.getUserStatus(loginUser.getId());
			// 判断用户是否有等级购买该房间
			String[] roomIds = bean.getRoomIds().split("_");
			if ((user.getRank() >= 3 && user.getRank() < 5 && roomIds.length == 1)
					|| (user.getRank() >= 5 && user.getRank() < 8 && (roomIds.length == 1 || roomIds.length == 2))
					|| ((user.getRank() == 8 || user.getRank() == 9) && (roomIds.length == 1
							|| roomIds.length == 2 || roomIds.length == 3))
					|| (user.getRank() >= 10)) {
				// 判断用户乐币是否可是够买该房间
				if (user.getGamePoint() > bean.getMoney()) {
					// 买个小房子则全部家具清空，提示用户
					if (homeUser.getTypeId() > buy) {
						if (request.getParameter("ku") != null) {
							getHomeService().deleteHomeUserImage(
									"user_id=" + loginUser.getId()
											+ " and home_id=0");
							LoadResource.deleteHomeUserImageList(loginUser
									.getId());
						} else if (request.getParameter("goods") != null) {
							getHomeService().deleteHomeUserImage(
									"user_id=" + loginUser.getId()
											+ " and home_id!=0");
							for (int i = 1; i < getHomeList("1=1").size() + 1; i++)
								LoadResource.deleteHomeUserHomeImageList(
										loginUser.getId(), i);
						}
						// 以前仓库的物品数目
						int goodsCount = 0;
						int k = 1;
						while (goodsCount == 0 && k < roomIds.length + 1) {
							goodsCount = (LoadResource
									.getHomeUserHomeImageList(
											loginUser.getId(), k++)).size() - 1;
						}
						if (goodsCount > 0) {
							tip = "更换房子之前请先将各房间里的家具放入仓库，不然会被当废品扔掉哦！";
							result = "goodsfailure";
							request.setAttribute("result", result);
							request.setAttribute("tip", tip);
							request.setAttribute("buy", buy + "");
							session.setAttribute("buyHomeRefrush", "ok");
							return;
						}
						//
						int count = (LoadResource
								.getHomeUserImageList(loginUser.getId()))
								.size();
						if (count > bean.getGoods()) {
							tip = "更换房子后物品数量超过仓库容量，请先处理旧东西,否则系统帮您一次性清空仓库哟！";
							result = "kufailure";
							request.setAttribute("result", result);
							request.setAttribute("tip", tip);
							request.setAttribute("buy", buy + "");
							session.setAttribute("buyHomeRefrush", "ok");
							return;
						}
					}
					// 更新用户乐币
					UserInfoUtil.updateUserStatus("game_point=game_point-"
							+ bean.getMoney(), "user_id=" + loginUser.getId(),
							loginUser.getId(), UserCashAction.OTHERS, "买家园房间"
									+ bean.getMoney() + "乐币！");
					// 查分用户拥有房间的字符串
					String[] oldRoomIds = homeUserOldType.getRoomIds().split(
							"_");
					for (int i = 0; i < oldRoomIds.length; i++) {
						// 获取房间类型
						HomeBean homeBean = getHomeService().getHome(
								"id=" + oldRoomIds[i]);
						// 删除用户房间装修图片
						getHomeService().deleteHomeUserImage(
								"user_id=" + loginUser.getId()
										+ " and type_id="
										+ homeBean.getTypeId());
					}
					// 插入用户拥有的新房间
					for (int i = 0; i < roomIds.length; i++) {
						HomeBean homeBean = getHomeService().getHome(
								"id=" + roomIds[i]);
						HomeImageBean homeImage = getHomeService()
								.getHomeImage(
										"type_id=" + homeBean.getTypeId()
												+ " order by id limit 0,1");
						HomeUserImageBean imageBean = new HomeUserImageBean();
						imageBean.setHomeId(StringUtil.toInt(roomIds[i]));
						imageBean.setImageId(homeImage.getId());
						imageBean.setTypeId(homeImage.getTypeId());
						imageBean.setUserId(loginUser.getId());
						getHomeService().addHomeUserImage(imageBean);
					}
					LoadResource.deleteHomeUserImageList(loginUser.getId());
					// macq_2006-12-20_增加家园的缓存_start
					HomeCacheUtil.updateHomeCacheById("type_id=" + buy,
							"user_id=" + loginUser.getId(), loginUser.getId());
					// getHomeService().updateHomeUser("type_id=" + buy,
					// "user_id=" + loginUser.getId());
					// macq_2006-12-20_增加家园的缓存_end
				} else {
					tip = "您的乐币不够，买一个便宜一点的房子暂住吧！";
					result = "failure";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
			} else {
				tip = "对不起，因为您的等级不够，买一个小一点的房子暂住吧！";
				result = "failure";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		} else {
			bean = getHomeType("id=" + homeUser.getTypeId());
		}
		tip = "您已经成功更换房屋，旧房子已处理掉了！(3秒后自动跳转)";
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 更换家具
	 */

	public void changeGoods(HttpServletRequest request) {
		int homeId = StringUtil.toInt((String) request.getParameter("homeId"));
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());

		String[] roomIds = bean.getRoomIds().split("_");
		// 根据类型得到房间
		Vector homeVec = new Vector();

		for (int i = 0; i < roomIds.length; i++) {
			// 获取房间类型
			HomeBean homeBean = getHomeService().getHome("id=" + roomIds[i]);

			if (homeBean != null)
				homeVec.add(homeBean);
		}

		// 未选择房间的时候
		if (homeId < 0) {
			request.setAttribute("homeVec", homeVec);
			request.setAttribute("homeId", homeId + "");
			request.setAttribute("bean", bean);
			return;
		}
		// 得到仓库的家具
		Vector userImage = LoadResource.getHomeUserHomeImageList(loginUser
				.getId(), homeId);
		// Vector userImage = getHomeService().getHomeUserImageList(
		// "home_id=" + homeBean.getId());
		// 得到家具类型
		HomeBean home = getHomeService().getHome("id=" + homeId);
		Vector imageType = new Vector();
		if (home != null) {
			HomeImageTypeBean type = getHomeService().getHomeImageType(
					"id=" + home.getTypeId());
			if (type != null) {
				String[] types = type.getType().split("_");

				for (int i = 0; i < types.length; i++) {

					HomeImageTypeBean subtype = getHomeService()
							.getHomeImageType("id=" + types[i]);

					if (subtype != null)
						imageType.add(subtype);
				}
			}

		}
		// imageType = getHomeService().getHomeImageTypeList("id=" + homeId);

		request.setAttribute("homeVec", homeVec);
		request.setAttribute("homeId", homeId + "");
		request.setAttribute("userImage", userImage);
		request.setAttribute("imageType", imageType);
		request.setAttribute("bean", bean);

	}

	/**
	 * 我的某种物品列表
	 */

	public void goodsList(HttpServletRequest request) {
		int typeId = StringUtil.toInt((String) request.getParameter("typeId"));
		int imageId = StringUtil
				.toInt((String) request.getParameter("imageId"));

		int homeId = StringUtil.toInt((String) request.getParameter("homeId"));
		Vector homeVec = null;
		HomeImageTypeBean bean = null;
		String over = null;
		if (typeId > 0 && homeId > 0) {

			boolean change = false;
			HomeBean home = getHomeService().getHome("id=" + homeId);
			if (home != null) {
				HomeImageTypeBean type = getHomeService().getHomeImageType(
						"id=" + home.getTypeId());
				if (type != null) {
					String[] types = type.getType().split("_");

					for (int i = 0; i < types.length; i++) {

						HomeImageTypeBean subtype = getHomeService()
								.getHomeImageType("id=" + types[i]);
						if (subtype.getId() == typeId) {
							change = true;
							break;
						}
					}
					if (!change) {

						request.setAttribute("homeId", homeId + "");

						return;

					}
					if (request.getParameter("goods") != null) {
						getHomeService().updateHomeUserImage(
								"home_id=0",
								"home_id=" + homeId + " and type_id=" + typeId
										+ " and user_id=" + loginUser.getId());
						LoadResource.deleteHomeUserHomeImageList(loginUser
								.getId(), homeId);
						// homeVec = getHomeService().getHomeUserImageList(
						// "type_id=" + typeId + " and home_id=0 and user_id="
						// + loginUser.getId());
						// Vector homeUserImageList = (Vector) LoadResource
						// .getHomeUserImageList(loginUser.getId());
						// HomeUserImageBean homeUserImageType = null;
						// homeVec = new Vector();
						// for (int i = 0; i < homeUserImageList.size(); i++) {
						// homeUserImageType = (HomeUserImageBean)
						// homeUserImageList
						// .get(i);
						// if (homeUserImageType.getTypeId() == typeId)
						// homeVec.add(homeUserImageType);
						// }
						over = "ok";
						request.setAttribute("homeId", homeId + "");
						request.setAttribute("over", over);
						return;
					} else if (imageId > 0) {
						if (getHomeService().updateHomeUserImage(
								"home_id=0",
								"home_id=" + homeId + " and type_id=" + typeId
										+ " and user_id=" + loginUser.getId())) {

							getHomeService().updateHomeUserImage(
									"home_id=" + homeId, "id=" + imageId);
							LoadResource.deleteHomeUserHomeImageList(loginUser
									.getId(), homeId);
							over = "ok";
							request.setAttribute("homeId", homeId + "");
							request.setAttribute("over", over);
							return;
						}

					}

					if (homeVec == null || homeVec.size() == 0) {
						HomeUserImageBean imageBean = getHomeService()
								.getHomeUserImage(
										"user_id=" + loginUser.getId()
												+ " and home_id=" + homeId
												+ " and type_id=" + typeId);
						homeVec = new Vector();
						HomeImageBean homeImage = null;
						Vector homeImageVector = null;

						if (imageBean == null) {
							homeImageVector = getHomeService()
									.getHomeImageList("type_id=" + typeId);

						}

						else {
							homeImageVector = getHomeService()
									.getHomeImageList(
											"type_id=" + typeId + " and id!="
													+ imageBean.getImageId());

						}
						for (int i = 0; i < homeImageVector.size(); i++) {
							homeImage = (HomeImageBean) homeImageVector.get(i);
							if (homeImage != null) {
								imageBean = getHomeService().getHomeUserImage(
										"type_id=" + typeId
												+ " and home_id=0 and user_id="
												+ loginUser.getId()
												+ " and image_id="
												+ homeImage.getId());
								if (imageBean != null)
									homeVec.add(imageBean);
							}

						}
					}
				}
				bean = getHomeService().getHomeImageType("id=" + typeId);

			}
		}
		request.setAttribute("homeVec", homeVec);
		request.setAttribute("typeId", typeId + "");
		request.setAttribute("homeId", homeId + "");
		request.setAttribute("bean", bean);
		request.setAttribute("over", over);
	}

	/**
	 * 我的仓库列表
	 */

	public void myStore(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String userId = null;
		if (loginUser.getHome() == 1) {
			userId = String.valueOf(loginUser.getId());
		} else {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// String imageId = request.getParameter("imageId");
		// if (imageId != null && userId != null) {
		// if (session.getAttribute("myImage") == null) {
		// result = "refrush";
		// request.setAttribute("result", result);
		// return;
		// }
		// session.removeAttribute("myImage");
		// int imageIdCheck = StringUtil.toInt(imageId);
		// if (imageIdCheck < 0) {
		// result = "parameterError";
		// request.setAttribute("result", result);
		// return;
		// }
		// HomeImageBean homeImage = getHomeService().getHomeImage(
		// "id=" + imageId);
		// if (homeImage == null) {
		// result = "searchError";
		// tip = "页面过期";
		// request.setAttribute("result", result);
		// request.setAttribute("tip", tip);
		// return;
		// }
		// int typeId = homeImage.getTypeId();
		// int gamePoint = homeImage.getPrice();
		// boolean flag = getHomeService().deleteHomeUserImage(
		// "type_id=" + typeId + " and user_id=" + userId);
		// if (flag) {
		// UserInfoUtil.updateUserStatus("game_point=game_point+"
		// + gamePoint, "user_id=" + loginUser.getId(), loginUser
		// .getId(), UserCashAction.OTHERS, "删除房间家具退还用户乐币"
		// + gamePoint + "乐币");
		// LoadResource.deleteHomeUserImageList(loginUser.getId());
		// }
		// result = "deleteOk";
		// request.setAttribute("result", result);
		// return;
		// }
		// 用户仓库家具列表
		String sql = "SELECT distinct(type_id) from jc_home_user_image where "
				+ "user_id=" + userId + " and home_id=0 order by type_id asc";
		List homeUserImageTypeList = (List) SqlUtil.getIntList(sql,
				Constants.DBShortName);
		int totalCount1 = homeUserImageTypeList.size();
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
		String prefixUrl1 = "myStore.jsp";
		// 取得要显示的消息列表
		int start1 = pageIndex1 * NUMBER_PER_PAGE;
		int end1 = NUMBER_PER_PAGE;
		int startIndex1 = Math.min(start1, totalCount1);
		int endIndex1 = Math.min(start1 + end1, totalCount1);
		List homeUserImageTypeList1 = homeUserImageTypeList.subList(
				startIndex1, endIndex1);
		request.setAttribute("totalCount1", new Integer(totalCount1));
		request.setAttribute("totalPageCount1", new Integer(totalPageCount1));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("homeUserImageTypeList", homeUserImageTypeList1);

		// 家园家具列表
		Vector homeImageTypeList = (Vector) getHomeService()
				.getHomeImageTypeList("id<1000");
		int totalCount = homeImageTypeList.size();
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
		String prefixUrl = "myStore.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List homeImageTypeList1 = homeImageTypeList.subList(startIndex,
				endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeImageTypeList", homeImageTypeList1);

		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 我的仓库列表
	 */
	public void myStoreSort(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String userId = null;
		if (loginUser.getHome() == 1) {
			userId = String.valueOf(loginUser.getId());
		} else {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String typeId = request.getParameter("typeId");
		if (typeId == null || StringUtil.toInt(typeId) == -1) {
			result = "typeError";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		HomeImageTypeBean homeImageType = getHomeService().getHomeImageType(
				"id=" + typeId);
		if (homeImageType == null) {
			result = "typeError";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		Vector homeUserImageList = (Vector) LoadResource
				.getHomeUserImageList(loginUser.getId());
		Vector homeUserImageTypeList = new Vector();
		HomeUserImageBean homeUserImageType = null;
		for (int i = 0; i < homeUserImageList.size(); i++) {
			homeUserImageType = (HomeUserImageBean) homeUserImageList.get(i);
			if (homeUserImageType.getTypeId() == StringUtil.toInt(typeId))
				homeUserImageTypeList.add(homeUserImageType);
		}
		if (homeUserImageTypeList.size() == 0) {
			result = "typeError";
			tip = "您没有" + homeImageType.getName() + "类别的家具";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}

		int totalCount = homeUserImageTypeList.size();
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
		String prefixUrl = "myStoreSort.jsp?typeId=" + typeId;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List homeUserImageTypeList1 = homeUserImageTypeList.subList(startIndex,
				endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("homeImageType", homeImageType);
		request.setAttribute("homeUserImageList", homeUserImageTypeList1);

		String imageId = request.getParameter("imageId");
		String id = request.getParameter("id");
		if (imageId != null && userId != null && id != null) {
			if (session.getAttribute("myStore") == null) {
				result = "refrush";
				request.setAttribute("result", result);
				return;
			}
			session.removeAttribute("myStore");
			int imageIdCheck = StringUtil.toInt(imageId);
			int idCheck = StringUtil.toInt(id);
			if (imageIdCheck < 0 || idCheck < 0) {
				result = "parameterError";
				request.setAttribute("result", result);
				return;
			}
			HomeImageBean homeImage = getHomeService().getHomeImage(
					"id=" + imageId);
			if (homeImage == null) {
				result = "searchError";
				tip = "页面过期";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			boolean flag = getHomeService()
					.deleteHomeUserImage("id=" + idCheck);
			if (flag) {
				// 接受参数判读用户是否要求出售商品,如出售商品退还50%商品价格
				String sale = request.getParameter("sale");
				if (sale != null) {
					int gamePoint = homeImage.getPrice() / 2;
					UserInfoUtil.updateUserStatus("game_point=game_point+"
							+ gamePoint, "user_id=" + loginUser.getId(),
							loginUser.getId(), UserCashAction.OTHERS,
							"删除房间家具退还用户乐币" + gamePoint + "乐币");
					request.setAttribute("tip", "您的" + homeImage.getName()
							+ "已按照原价50％出售！（3秒钟跳转）共获得" + gamePoint + "乐币!");
				} else {
					request.setAttribute("tip", "您的" + homeImage.getName()
							+ "已被你扔出!（3秒钟跳转）");
				}
				LoadResource.deleteHomeUserImageList(loginUser.getId());
			}
			result = "deleteOk";
			request.setAttribute("result", result);
			return;
		}
		result = "success";
		request.setAttribute("result", result);
		return;

	}

	/**
	 * 用户家园仓库每个类别家具拥有明细
	 * 
	 * @param request
	 */
	public void moreStore(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String userId = null;
		if (loginUser.getHome() == 1) {
			userId = String.valueOf(loginUser.getId());
		} else {
			result = "failure";
			tip = "您还未开通个人家园.";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String imageId = request.getParameter("imageId");
		if (imageId != null && userId != null) {
			if (session.getAttribute("myImage") == null) {
				result = "refrush";
				request.setAttribute("result", result);
				return;
			}
			session.removeAttribute("myImage");
			int imageIdCheck = StringUtil.toInt(imageId);
			if (imageIdCheck < 0) {
				result = "parameterError";
				request.setAttribute("result", result);
				return;
			}
			HomeImageBean homeImage = getHomeService().getHomeImage(
					"id=" + imageId);
			if (homeImage == null) {
				result = "searchError";
				tip = "页面过期";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			int typeId = homeImage.getTypeId();
			int gamePoint = homeImage.getPrice();
			boolean flag = getHomeService().deleteHomeUserImage(
					"type_id=" + typeId + " and user_id=" + userId);
			if (flag) {
				UserInfoUtil.updateUserStatus("game_point=game_point+"
						+ gamePoint, "user_id=" + loginUser.getId(), loginUser
						.getId(), UserCashAction.OTHERS, "删除房间家具退还用户乐币"
						+ gamePoint + "乐币");
				LoadResource.deleteHomeUserImageList(loginUser.getId());
			}
			result = "deleteOk";
			request.setAttribute("result", result);
			return;
		}

		// Vector homeUserImageList = (Vector) LoadResource
		// .getHomeUserImageList(loginUser.getId());
		Vector homeImageTypeList = (Vector) getHomeService()
				.getHomeImageTypeList("1=1");

		// request.setAttribute("homeUserImageList", homeUserImageList);
		request.setAttribute("homeImageTypeList", homeImageTypeList);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 赠送家园页面
	 * 
	 * @param request
	 */
	public void present(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String fromBuyPage = request.getParameter("fromBuyPage");
		if (fromBuyPage == null) {
			fromBuyPage = "no";
		}
		request.setAttribute("fromBuyPage", fromBuyPage);
		// 接受赠送shangpin
		String imageId = request.getParameter("imageId");
		if (StringUtil.toInt(imageId) == -1) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		HomeImageBean image = getHomeService().getHomeImage("id=" + imageId);
		if (image == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		String userId = null;
		UserBean frienduser = null;
		Vector userFriendList = new Vector();
		for (int i = 0; i < userFriends.size(); i++) {
			userId = (String) userFriends.get(i);
			frienduser = UserInfoUtil.getUser(StringUtil.toInt(userId));
			if (frienduser != null) {
				userFriendList.add(frienduser);
			}
		}
		int totalCount = userFriendList.size();
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
		String prefixUrl = "present.jsp?imageId=" + imageId
				+ "&amp;fromBuyPage=" + fromBuyPage;
		// 取得要显示的消息列表
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List friendList1 = userFriendList.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userFriendList", friendList1);
		result = "success";
		request.setAttribute("result", result);
		request.setAttribute("image", image);
		session.setAttribute("persentCheck", "ok");

		return;
	}

	/**
	 * 赠送家园页面
	 * 
	 * @param request
	 */
	public void presentResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("persentCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("persentCheck");
		String friendId = request.getParameter("friendId");
		if (friendId == null) {
			result = "failure";
			tip = "请选择赠送接受对象";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (StringUtil.toInt(friendId) == -1) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		String imageId = request.getParameter("imageId");
		if (StringUtil.toInt(imageId) == -1) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		HomeImageBean image = getHomeService().getHomeImage("id=" + imageId);
		if (image == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserBean friendUser = UserInfoUtil.getUser(StringUtil.toInt(friendId));
		if (friendUser == null || friendUser.getHome() == 0) {
			result = "failure";
			tip = "您赠送的用户没有开通家园您不能送礼物!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean friendHomeUser = HomeCacheUtil.getHomeCache(StringUtil
				.toInt(friendId));
		// HomeUserBean friendHomeUser = getHomeService().getHomeUser(
		// "user_id=" + friendId);
		// macq_2006-12-20_增加家园的缓存_end
		if (friendHomeUser == null) {
			result = "failure";
			tip = "您赠送的用户没有开通家园您不能送礼物!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// int friendHomeSize = friendHomeUser.getTypeId() * 10;
		HomeTypeBean type = getHomeType("id=" + friendHomeUser.getTypeId());
		if (type != null) {
			int friendHomeSize = type.getGoods();

			Vector friendHomeImageList = LoadResource
					.getHomeUserImageList(StringUtil.toInt(friendId));
			if (friendHomeImageList.size() >= friendHomeSize) {
				result = "failure";
				tip = "您赠送的用户家园仓库已满,不能接受您赠送的礼物!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}

		String fromBuyPage = request.getParameter("fromBuyPage");
		if (fromBuyPage == null) {
			fromBuyPage = "no";
		}
		// 从仓库赠送
		HomeUserImageBean bean = null;
		if (fromBuyPage.equals("no")) {
			bean = getHomeService().getHomeUserImage(
					"user_id=" + loginUser.getId() + " and image_id=" + imageId
							+ " and home_id=0");
			if (bean != null) {
				getHomeService().updateHomeUserImage("user_id=" + friendId,
						"id=" + bean.getId());
				NoticeBean notice = new NoticeBean();
				notice.setUserId(StringUtil.toInt(friendId));
				notice.setTitle(loginUser.getNickName() + "赠送了您一个"
						+ image.getName());
				notice.setContent("");
				notice.setHideUrl("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setLink("/home/myStore.jsp");
				NoticeUtil.getNoticeService().addNotice(notice);
				// 清空缓存
				LoadResource.deleteHomeUserImageList(loginUser.getId());
				LoadResource
						.deleteHomeUserImageList(StringUtil.toInt(friendId));
			} else {
				result = "failure";
				tip = "您的仓库中没有" + image.getName() + ",不能赠送!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		} else {
			// 从购买页面赠送
			UserStatusBean user = UserInfoUtil.getUserStatus(loginUser.getId());
			if (user.getGamePoint() >= image.getPrice()) {
				HomeUserImageBean homeUserImage = new HomeUserImageBean();
				homeUserImage.setUserId(StringUtil.toInt(friendId));
				homeUserImage.setImageId(image.getId());
				homeUserImage.setTypeId(image.getTypeId());
				homeUserImage.setHomeId(0);
				if (getHomeService().addHomeUserImage(homeUserImage)) {
					UserInfoUtil.updateUserStatus("game_point=game_point-"
							+ image.getPrice(), "user_id=" + loginUser.getId(),
							loginUser.getId(), UserCashAction.OTHERS, "买家具"
									+ image.getPrice() + "乐币！");
					NoticeBean notice = new NoticeBean();
					notice.setUserId(StringUtil.toInt(friendId));
					notice.setTitle(loginUser.getNickName() + "赠送了您一个"
							+ image.getName());
					notice.setContent("");
					notice.setHideUrl("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setLink("/home/myStore.jsp");
					NoticeUtil.getNoticeService().addNotice(notice);
				}
				// 清空缓存
				LoadResource
						.deleteHomeUserImageList(StringUtil.toInt(friendId));
			} else {
				result = "failure";
				tip = "您没有足够的钱购买" + image.getName() + ",不能赠送!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		result = "persent";
		tip = "赠送成功!(3秒后跳转我的仓库)";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 获取用户个人家园拥有物品名字ID组合 作者:马长青 时间:2006-11-30
	 */
	public String getHomeUserImage(int userId, int homeId) {
		String fileName = null;
		// 获取用户所有家具图片
		Vector userImageList = (Vector) LoadResource.getHomeUserHomeImageList(
				userId, homeId);
		if (userImageList != null) {
			HomeUserImageBean userImage = null;
			int background = 0;
			// 循环得到用户家园每个家具的id并按"1_2"样式拼值
			for (int i = 0; i < userImageList.size(); i++) {
				userImage = (HomeUserImageBean) userImageList.get(i);
				if (userImage.getTypeId() >= 1000) {
					background = userImage.getImageId();
					continue;
				}
				if (fileName == null) {
					fileName = userImage.getImageId() + "";
				} else {
					fileName = fileName + "_" + userImage.getImageId();
				}
			}
			fileName = background + "_" + fileName + "_0";
		}
		return fileName;
	}

	/**
	 * 判断操作系统,返回图片路径 作者:马长青 时间:2006-11-30
	 */
	public String getImageUrl(int imageType, int userId, int homeId) {
		// 获取用户家具图片名称
		String fileName = getHomeUserImage(userId, homeId);
		String imagePath = null;
		String imageFile = null;
//		// 判断操作系统
//		boolean isWindows = false;
//		String osName = System.getProperty("os.name");
//		if (osName != null) {
//			isWindows = (osName.toLowerCase().indexOf("windows") != -1);
//		}

		// 判断大图还是小图,生成图片判断,文件路径选择
		if (imageType == 0) {

			imagePath = "/rep/home/small/";

			HomeCacheUtil.getHomeSamllImageFileNameList(fileName);
		} else {
			imagePath = "/rep/home/big/";

			HomeCacheUtil.getHomeBigImageFileNameList(fileName);
		}

		// 生成文件完整路径
		imageFile = imagePath + fileName + ".gif";

		return imageFile;
	}

	public static void weekTask() {
		IHomeService service = ServiceFactory.createHomeService();
		Vector vec = service
				.getHomeUserList(" to_days(now())-to_days(last_modify_time)>7");
		for (int i = 0; i < vec.size(); i++) {
			HomeUserBean user = (HomeUserBean) vec.get(i);
			// 破败版图片
			service.updateHomeUserImage("image_id=20", "user_id="
					+ user.getUserId() + " and type_id>999");

		}
	}

	/**
	 * 乐酷生涯记录 / }
	 */
	public void careerList(HttpServletRequest request) {
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		String userId = null;
		UserBean frienduser = null;
		Vector userFriendList = new Vector();
		for (int i = userFriends.size(); i > 0 && i > userFriends.size() - 3; i--) {
			userId = (String) userFriends.get(i - 1);
			frienduser = UserInfoUtil.getUser(StringUtil.toInt(userId));
			if (frienduser != null) {
				userFriendList.add(frienduser);
			}
		}
		// List friendList1 = userFriendList.subList(0, 2);

		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());

		FriendMarriageBean marriage = null;

		List jvCheckList = getUserService().getJyFriendIds(loginUser.getId());
		Vector userJyList = new Vector();
		if (jvCheckList.size() > 0) {
			for (int i = jvCheckList.size(); i > 0
					&& i > jvCheckList.size() - 3; i--) {
				Integer userd = (Integer) jvCheckList.get(i - 1);
				frienduser = UserInfoUtil.getUser(userd.intValue());
				if (frienduser != null) {
					userJyList.add(frienduser);
				}
			}
		}

		request.setAttribute("userFriendList", userFriendList);
		request.setAttribute("moreuserFriendList", userFriendList.size() + "");

		request.setAttribute("jyFriendList", userJyList);
		request.setAttribute("moreuserJyList", jvCheckList.size() + "");
		if (status.getMark() == 2) {

			marriage = getFriendService().getFriendMarriage(
					"from_id=" + loginUser.getId() + " and mark=0");
			if (marriage == null) {
				marriage = getFriendService().getFriendMarriage("to_id="

				+ loginUser.getId() + " and mark=0");
			}
		}
		request.setAttribute("marriage", marriage);
	}

	/**
	 * 更多乐酷生涯记录 / }
	 */
	public void morecareerList(HttpServletRequest request) {

		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		String userId = null;
		UserBean frienduser = null;
		Vector userFriendList = new Vector();

		int totalCount2 = 0;
		int pageIndex2 = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex2 == -1) {
			pageIndex2 = 0;
		}

		String prefixUrl2 = null;
		// 取得要显示的消息列表
		int start2 = pageIndex2 * NUMBER_PER_PAGE;
		int end2 = NUMBER_PER_PAGE;

		if (request.getParameter("friend") != null) {
			totalCount2 = userFriends.size() - 2;
			prefixUrl2 = "morecareerList.jsp?friend=1";
			for (int i = totalCount2 - start2; i > 0
					&& i > totalCount2 - end2 - start2; i--) {
				userId = (String) userFriends.get(i - 1);
				frienduser = UserInfoUtil.getUser(StringUtil.toInt(userId));
				if (frienduser != null) {
					userFriendList.add(frienduser);
				}
			}

		} else if (request.getParameter("jy") != null) {
			// Vector userJyist = new Vector();
			// for (int i = userFriends.size(); i > 0; i--) {
			// userId = (String) userFriends.get(i - 1);
			// frienduser = UserInfoUtil.getUser(StringUtil.toInt(userId));
			// UserFriendBean userF = getUserService().getUserFriend(
			// loginUser.getId(), StringUtil.toInt(userId));
			// if (frienduser != null && userF != null && userF.getMark() == 1)
			// {
			// userJyist.add(frienduser);
			// }
			// }
			List jvCheckList = getUserService().getJyFriendIds(
					loginUser.getId());
			totalCount2 = jvCheckList.size() - 2;
			prefixUrl2 = "morecareerList.jsp?jy=1";

			for (int i = totalCount2 - start2; i > 0
					&& i > totalCount2 - end2 - start2; i--) {
				Integer userd = (Integer) jvCheckList.get(i - 1);
				frienduser = UserInfoUtil.getUser(userd.intValue());

				if (frienduser != null) {
					userFriendList.add(frienduser);
				}
			}
		}

		// List friendList1 = userFriendList.subList(0, 2);
		int totalPageCount2 = totalCount2 / NUMBER_PER_PAGE;
		if (totalCount2 % NUMBER_PER_PAGE != 0) {
			totalPageCount2++;
		}
		if (pageIndex2 > totalPageCount2 - 1) {
			pageIndex2 = totalPageCount2 - 1;
		}
		if (pageIndex2 < 0) {
			pageIndex2 = 0;
		}

		request.setAttribute("totalPageCount", new Integer(totalPageCount2));
		request.setAttribute("pageIndex", new Integer(pageIndex2));
		request.setAttribute("prefixUrl", prefixUrl2);
		request.setAttribute("userFriendList", userFriendList);

	}

	public Vector getUserHome(int userId) {
		// macq_2006-12-20_增加家园的缓存_start
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(userId);
		// HomeUserBean homeUser = getHomeService().getHomeUser(
		// "user_id=" + userId);
		// macq_2006-12-20_增加家园的缓存_end
		HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());

		String[] roomIds = bean.getRoomIds().split("_");
		// 根据类型得到房间
		Vector homeVec = new Vector();

		for (int i = 0; i < roomIds.length; i++) {
			// 获取房间类型
			HomeBean homeBean = getHomeService().getHome("id=" + roomIds[i]);

			if (homeBean != null)
				homeVec.add(homeBean);
		}
		return homeVec;
	}

	public HomeImageTypeBean getHomeImageType(int typeId) {
		HomeImageTypeBean homeImageType = getHomeService().getHomeImageType(
				"id=" + typeId);
		return homeImageType;
	}
	
	public void editAllow() {		// 设置保密
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		if(homeUser == null) {
			tip(null, "您还没有自己的家园");
		} else if(isMethodGet()) {
			setAttribute("homeUser", homeUser);
			tip("edit");
		} else {
			
			int allow = getParameterIntS("allow");	// home allow change 家园保密设置
			String password = request.getParameter("password");		// 家园密码

			if(allow > 2 || allow < 0)
				allow = 0;
			if(password == null)
				password = "";
			homeUser.setAllow(allow);
			homeUser.setPassword(password);

			getHomeService().updateHomeUser("allow=" + allow + ",password='" + StringUtil.toSql(password) + "'", "id="
						+ homeUser.getId());
			setAttribute("homeUser", homeUser);
			tip(null, "家园保密已成功设置");
		}
	}
	
	/**
	 * 添加家园日记的默认分类。请谨慎调用，一个用户只能有一个默认分类
	 * type=0:日记分类;1:相册分类
	 */
	public void addDefualtCat(int uid,int type){
		if (uid > 0){
			if (type == 0){
				if (homeService.getHomeDiaryCat(" uid=" + uid + " and def=1") == null){
					HomeDiaryCat cat = new HomeDiaryCat();
					cat.setCatName("系统分类");
					cat.setUid(uid);
					cat.setCount(0);
					cat.setPrivacy(9);
					cat.setDef(1);
					homeService.addHomeDiaryCat(cat);
				}
			} else if (type == 1){
				if (homeService.getHomePhotoCat(" uid=" + uid + " and def=1") == null){
					HomePhotoCat cat = new HomePhotoCat();
					cat.setCatName("系统分类");
					cat.setUid(uid);
					cat.setCount(0);
					cat.setPrivacy(9);
					cat.setDef(1);
					homeService.addHomePhotoCat(cat);
				}
			}
		}
	}
	
	// 根据日记查找其分类的bean = =#
	public HomeDiaryCat getCatFromList(HomeDiaryBean bean,List list){
		HomeDiaryCat cat = null;
		if (bean == null || list == null || list.size() == 0){
			return cat;
		} else {
			if (bean.getCatId() == 0){
				// 默认分类
				cat = new HomeDiaryCat();
				cat.setCatName("默认分类");
				cat.setPrivacy(9);
				cat.setUid(bean.getUserId());
				return cat;
			}
			for (int i = 0 ; i < list.size() ; i++){
				if (list.get(i) instanceof HomeDiaryCat){
					cat = (HomeDiaryCat)list.get(i);
					if (cat.getId() == bean.getCatId()){
						return cat;
					}
				}
			}
		}
		return cat;
	}
	
	// 根据相册查找其分类的bean = =#
	public HomePhotoCat getCatFromList(HomePhotoBean bean,List list){
		HomePhotoCat cat = null;
		if (bean == null || list == null || list.size() == 0){
			return cat;
		} else {
			if (bean.getCatId() == 0){
				// 默认分类
				cat = new HomePhotoCat();
				cat.setCatName("默认分类");
				cat.setPrivacy(9);
				cat.setUid(bean.getUserId());
				return cat;
			}
			for (int i = 0 ; i < list.size() ; i++){
				if (list.get(i) instanceof HomePhotoCat){
					cat = (HomePhotoCat)list.get(i);
					if (cat.getId() == bean.getCatId()){
						return cat;
					}
				}
			}
		}
		return cat;
	}
	
	/**
	 * 编辑家园日记 分类
	 */
	public void homeDiaryCat(){
		HomeUserBean homeUser = null;
		String action = request.getParameter("action");
		HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
		int uid = this.getLoginUser().getId();
		if(action == null) {
			return;
		}
		
		if(action.equals("a")) {
			homeUser = HomeCacheUtil.getHomeCache(uid);
			String title = getParameterNoEnter("title");
			if(title == null || title.length() == 0) {
				request.setAttribute("msg", "名称不能为空");
				return;
			}
			if(title.length()>10) {
				request.setAttribute("msg", "名称不能大于10个字符");
				return;
			}
			// 检查分类是否达到最大
			if (homeUser.getDiaryCatCount() >= MAX_CAT_COUNT){
				request.setAttribute("msg", "已达到最大分类限制,请删除一些分类后再创建.");
			} else {
				int privacy = this.getParameterInt("privacy");
				
				HomeDiaryCat cat = new HomeDiaryCat();
				cat.setCatName(title);
				cat.setPrivacy(privacy);
				cat.setUid(uid);
				cat.setDef(0);
				
				homeServiceImpl.addHomeDiaryCat(cat);
				// 分类总数+1
				SqlUtil.executeUpdate("update jc_home_user set diary_cat_count=diary_cat_count+1 where id=" + homeUser.getId(), 0);
				// 操作缓存
				homeUser.setDiaryCatCount(homeUser.getDiaryCatCount()+1);
//				OsCacheUtil.put(HomeCacheUtil.getKey(this.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
				request.setAttribute("msg", "添加成功");
			}
		} else if(action.equals("d")) {
			int id = this.getParameterInt("id");
			
			homeServiceImpl.deleteHomeDiaryCat(id, uid);
			
			request.setAttribute("msg", "删除成功");
		} else if(action.equals("e")) {
			int id = this.getParameterInt("id");
			String title = getParameterNoEnter("title");
			if(title == null || title.length() == 0) {
				request.setAttribute("msg", "名称不能为空");
				request.setAttribute("catId", new Integer(id));
				return;
			}
			if(title.length()>10) {
				request.setAttribute("msg", "名称不能大于10个字符");
				request.setAttribute("catId", new Integer(id));
				return;
			}
			int privacy = this.getParameterInt("privacy");
			
			HomeDiaryCat cat = new HomeDiaryCat();
			cat.setId(id);
			cat.setCatName(title);
			cat.setPrivacy(privacy);
			cat.setUid(uid);
			cat.setDef(0);
			homeServiceImpl.updateHomeDiaryCat(cat);
			
			request.setAttribute("catId", new Integer(cat.getId()));
			request.setAttribute("msg", "修改成功");
		}
	}
	/**
	 * 编辑家园相册 分类
	 */
	public void homePhotoCat(){
		HomeUserBean homeUser = null;
		String action = request.getParameter("action");
		HomeServiceImpl homeServiceImpl = new HomeServiceImpl();
		int uid = this.getLoginUser().getId();
		if(action == null) {
			return;
		}
		
		if(action.equals("a")) {
			String title = getParameterNoEnter("title");
			if(title == null || title.length() == 0) {
				request.setAttribute("msg", "名称不能为空");
				return;
			}
			if(title.length()>10) {
				request.setAttribute("msg", "名称不能大于10个字符");
				return;
			}
			homeUser = HomeCacheUtil.getHomeCache(this.getLoginUser().getId());
			if (homeUser.getPhotoCatCount() >= MAX_CAT_COUNT){
//				request.setAttribute("catId", new Integer());
				request.setAttribute("msg", "已达到最大分类限制,请删除一些分类后再创建.");
			} else {
				int privacy = this.getParameterInt("privacy");
				
				HomePhotoCat cat = new HomePhotoCat();
				cat.setCatName(title);
				cat.setPrivacy(privacy);
				cat.setUid(uid);
				
				// 分类总数+1
				SqlUtil.executeUpdate("update jc_home_user set photo_cat_count=photo_cat_count+1 where id=" + homeUser.getId(), 0);
				// 操作缓存
				homeUser.setPhotoCatCount(homeUser.getPhotoCatCount()+1);
//				OsCacheUtil.put(HomeCacheUtil.getKey(this.getLoginUser().getId()),homeUser,OsCacheUtil.HOME_USER_CACHE_GROUP);
				
				homeServiceImpl.addHomePhotoCat(cat);
				
				request.setAttribute("msg", "添加成功");
			}
		} else if(action.equals("d")) {
			int id = this.getParameterInt("id");
			
			homeServiceImpl.deleteHomePhotoCat(id, uid);
			
			request.setAttribute("msg", "删除成功");
		} else if(action.equals("e")) {
			int id = this.getParameterInt("id");
			String title = getParameterNoEnter("title");
			if(title == null || title.length() == 0) {
				request.setAttribute("msg", "名称不能为空");
				request.setAttribute("catId", new Integer(id));
				return;
			}
			if(title.length()>10) {
				request.setAttribute("msg", "名称不能大于10个字符");
				request.setAttribute("catId", new Integer(id));
				return;
			}
			int privacy = this.getParameterInt("privacy");
			
			HomePhotoCat cat = new HomePhotoCat();
			cat.setId(id);
			cat.setCatName(title);
			cat.setPrivacy(privacy);
			cat.setUid(uid);
			homeServiceImpl.updateHomePhotoCat(cat);
			
			request.setAttribute("catId", new Integer(cat.getId()));
			request.setAttribute("msg", "修改成功");
		}
		
	}
	
	/**
	 * 返回老婆（老公）的UID
	 */
	public int getCoupleUid(int uid){
		FriendAction friendAction = new FriendAction(request);
		FriendMarriageBean marriageBean = friendAction.infoMarriage(uid);
		// 得到另一半的用户ID
		if (marriageBean != null){
			if (marriageBean.getFromId() == uid){
				return marriageBean.getToId();
			} else {
				return marriageBean.getFromId();
			}
		}
		return 0;
	}
	/**
	 * 
	 * @param homeUser:要浏览的家用Bean
	 * @param coupleUid:"另一伴"的Uid
	 * @return 返回特殊称谓。如“他老婆，她老公”
	 */
	public String getOtherTitle(HomeUserBean homeUser,int coupleUid){
		if (homeUser == null){
			return "";
		}
		String otherTitle = "";
		if (coupleUid != 0){
			if (homeUser.getUserId() != this.getLoginUser().getId() && coupleUid != this.getLoginUser().getId()){
				otherTitle = homeUser.getGender() == 0?"他":"她";	
			}
			otherTitle += homeUser.getGender() == 0?"老婆":"老公";
		} else {
			otherTitle = UserInfoUtil.getUser(homeUser.getUserId()).getNickNameWml() ;
		}
		return otherTitle;
	}
//	// 增加新宣言
//	public boolean addNewEnounce(HomeEnounce bean){
//		boolean result = false;
//		if (bean.getUserId() != this.getLoginUser().getId()){
//			this.setAttribute("tip", "不可代替别人发表宣言哦.");
//			return result;			
//		}
//		if ("".equals(bean.getContent()) || bean.getContent().length() > 20){
//			this.setAttribute("tip", "宣言太长或没有输入宣言.");
//			return result;
//		}
//		if (bean.getPic() < 1 || bean.getPic() > picList.size()){
//			bean.setPic(1);
//		}
//		result = homeService.addNewEnounce(bean);
//		return result;
//	}
//
//	public static long statRankTime = 0;
//	public static long RANK_INTERVAL = 300 * 1000;
//	public static byte[] initLock = new byte[0];
	
//	// 更新参赛用户排名
//	public static void updateRank() {
//		long now = System.currentTimeMillis();
//		if(now < statRankTime)
//			return;
//		synchronized(initLock) {
//			if(now < statRankTime)
//				return;
//			DbOperation db = new DbOperation();
//			db.init();
//			db.executeUpdate("truncate table jc_home_player_rank");
//			db.executeUpdate("insert into jc_home_player_rank (user_id,vote_count) (select user_id,vote_count from jc_home_player order by vote_count desc,user_id desc)");
//			db.release();
//			statRankTime = now + RANK_INTERVAL;
//			if(SqlUtil.isTest)
//				RANK_INTERVAL /= 1000;
//		}
//	}
}