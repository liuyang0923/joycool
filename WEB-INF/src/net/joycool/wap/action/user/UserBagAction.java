package net.joycool.wap.action.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.TradeBean;
import net.joycool.wap.bean.TradeUserBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.home.HomeImageBean;
import net.joycool.wap.bean.home.HomeTypeBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.bean.home.HomeUserImageBean;
import net.joycool.wap.bean.item.ComposeBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.DummyServiceImpl;
import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.service.infc.IBankService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.shop.ShopUtil;
import net.joycool.wap.util.Arith;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author macq
 * @datetime 2006-12-13 上午10:12:37
 * @explain
 */
public class UserBagAction extends CustomAction{

	static int NUMBER_PER_PAGE = 5;
	
	UserBean loginUser = null;

	static IUserService userService = ServiceFactory.createUserService();

	static IDummyService dummyService = ServiceFactory.createDummyService();

	static IChatService chatService = ServiceFactory.createChatService();

	static IMessageService messageService = ServiceFactory.createMessageService();

	static IHomeService homeService = ServiceFactory.createHomeService();

	static IBankService bankService = ServiceFactory.createBankService();
    
	static int COMMUNION_PROP = 2;
	static int COMMUNION_PROP_EGG = 4;
	static int COMMUNION_PROP_BEER = 5;
	static int COMMUNION_PROP_KNIVES = 6;
	static int COMMUNION_PROP_MESSAGE = 7;
	static int COMMUNION_PROP_DACOITY = 10;
	static int COMMUNION_PROP_BODYGUARD = 11;
	static int PROP_DACOITY = 20;
	
	public static int CUFF_ITEM = 5151;
	public static int CUFF_TIME = 5 * 60 * 1000;

	public static HashMap stopSpeakMap = new HashMap();

	public UserBagAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
	}

	public static IHomeService getHomeService() {
		return homeService;
	}

	public static IMessageService getMessageService() {
		return messageService;
	}

	public static IUserService getUserService() {
		return userService;
	}

	public static IChatService getChatService() {
		return chatService;
	}

	public static IDummyService getDummyService() {
		return dummyService;
	}

	public static IBankService getBankService() {
		return bankService;
	}

	/**
	 * @author macq explain :展示用户行囊 datetime:2006-12-13 上午10:16:55
	 * @param request
	 */
	public void userBag(HttpServletRequest request) {
		// 获取用户行囊中物品id列表
		List userBagList = UserBagCacheUtil.getUserBagListCacheById(loginUser
				.getId());
		int totalCount = userBagList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		NUMBER_PER_PAGE=10;
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
		String prefixUrl = "userBag.jsp?";
		// 取得要显示的消息列表x
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, userBagList.size());
		int endIndex = Math.min(start + end, userBagList.size());
		List userBagList1 = userBagList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userBagCount", userBagList.size() + "");
		request.setAttribute("userBagList", userBagList1);
		return;
	}
	
	public void userBagCat() {
		int item = getParameterInt("item");
		if(item == 0) {	// 没有选定分类
			
		} else {
			/*DummyProductBean dummyProduct = dummyService.getDummyProducts(item);
			if(dummyProduct.getDue() == 0 && dummyProduct.getTime() == 1) {	// 这类物品没有区别，随便选一个
				
			} else {		// 每个物品有区别，列表选取
				
			}
			int id = UserBagCacheUtil.getUserBagById(item, loginUser.getId());
			if(id != -1) {	// 没有找到
				setAttribute("userBagId", id);
				tip("success");
			} else {
				tip(null, "没有找到物品");
			} */
		}
	}

	/**
	 * 
	 * @author macq explain : 用户行囊使用 datetime:2006-12-13 上午11:14:48
	 * @param request
	 */
	public void userBagUse(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 接受参数
		
		int userBagId = 0;
		int item = getParameterInt("item");
		if(item > 0) {
			userBagId = UserBagCacheUtil.getUserBagById(item, loginUser.getId());
		} else {
			userBagId = getParameterInt("userBagId");
		}
		// 判断参数有效性
		if (userBagId <= 0) {
			doTip(null, null);
			return;
		}
		// 用户用户行囊记录
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		// 判断用户是否拥有该商品
		if (userBag == null || userBag.getUserId() != loginUser.getId()) {
			doTip(null, "您的行囊中没有该类商品!");
			return;
		}
		// 获取虚拟物品信息
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
		if (dummyProduct == null) {
			doTip(null, "系统中没有该类物品");
			return;
		}
		request.setAttribute("dummyProduct", dummyProduct);
		request.setAttribute("userBag", userBag);
		//session.setAttribute("userBagUseCheck", "true");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 用户虚拟物品使用处理页面 datetime:2006-12-13 下午01:58:04
	 * @param request
	 */
	public void userBagResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		 // 防止刷新
		// if (session.getAttribute("userBagUseCheck") == null) {
		// result = "refrush";
		// request.setAttribute("result", result);
		// return;
		// }
		session.removeAttribute("userBagUseCheck");
		int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
		if (userBagId <= 0) {
			doTip(null, null);
			return;
		}
		synchronized(loginUser.getLock()) {
			// 用户用户行囊记录
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
			// 判断用户是否拥有该商品
			if (userBag == null || userBag.getUserId() != loginUser.getId()) {
				doTip(null, "您的行囊中没有该类商品");
				return;
			}
			// 获取虚拟物品信息
			DummyProductBean dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
			if (dummyProduct == null) {
				doTip(null, "系统中没有该类物品");
				return;
			}
			request.setAttribute("userBag", userBag);
			request.setAttribute("dummyProduct", dummyProduct);
			int present = StringUtil.toInt(request.getParameter("present"));
			// 判断赠送给好友
			if (present != -1) {
				ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser
						.getId());
				int totalCount = userFriends.size();
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
				String prefixUrl = "userBagResult.jsp?userBagId=" + userBagId
						+ "&amp;present=1";
				// 取得要显示的消息列表
				int start = pageIndex * NUMBER_PER_PAGE;
				int end = NUMBER_PER_PAGE;
				int startIndex = Math.min(start, totalCount);
				int endIndex = Math.min(start + end, totalCount);
				List friendList1 = userFriends.subList(startIndex, endIndex);
				request.setAttribute("totalCount", new Integer(totalCount));
				request.setAttribute("totalPageCount", new Integer(totalPageCount));
				request.setAttribute("pageIndex", new Integer(pageIndex));
				request.setAttribute("prefixUrl", prefixUrl);
				request.setAttribute("userFriendList", friendList1);
				result = "present";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				session.setAttribute("userBagCheck", "ture");
				return;
			}
			int lose = StringUtil.toInt(request.getParameter("lose"));
			if (lose != -1) {
				if(lose == 1) {
					if(userBag.isEnd()) {		// 物品过期，直接丢弃
						UserBagCacheUtil.updateUserBagCacheById("time=0",
								"id=" + userBagId, loginUser.getId(), userBagId);
						userService.addItemLog(loginUser.getId(), 0, userBag.getId(), userBag.getProductId(), userBag.getTime(), 6);
						doTip("success", "物品已丢弃");
					} else {	//	 确认提问
						doTip("lose", "跑过来一个小鬼，拿出" + StringUtil.bigNumberFormat(dummyProduct.getPrice() * userBag.getTime()) + "乐币想要和你交换");
					}
					return;
				} else {
					// 丢弃虚拟物品
					UserBagCacheUtil.updateUserBagCacheById("time=0",
							"id=" + userBagId, loginUser.getId(), userBagId);
					tip = dummyProduct.getName() + "被拿走了，得到了一些乐币";
					int totalMoney = dummyProduct.getPrice() * userBag.getTime();
					UserInfoUtil.updateUserCash(loginUser.getId(), totalMoney, 
							10, "用户npc物品得到了" + totalMoney + "乐币");
					userService.addItemLog(loginUser.getId(), 0, userBag.getId(), userBag.getProductId(), userBag.getTime(), 6);
					doTip("success", tip);
					return;
				}
			}
			int use = StringUtil.toInt(request.getParameter("use"));
			if (use != -1) {
				int cooldown = getCooldown(userBag, dummyProduct);
				if(cooldown > 0) {
					doTip(null, "该物品还要" + DateUtil.formatTimeInterval(cooldown) + "后才能使用");
					return;
				}
				// 判断使用物品更新乐币(随机更新1-50万)
				if (dummyProduct.getDummyId() != 2 && dummyProduct.getMode() == 2) {
					// 随机获取1-50万
					int gamePoint = RandomUtil.nextIntNoZero(dummyProduct
							.getValue()) * 10000;
					UserInfoUtil
							.updateUserCash(loginUser.getId(), gamePoint, 13,
									"使用宝箱增加乐币" + gamePoint);
					// 更新虚拟物品使用次数
					UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
					tip = "您获得了" + gamePoint / 10000 + "万乐币";
					result = "success";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				} // 更新用户经验值
				else if (dummyProduct.getDummyId() != 2
						&& dummyProduct.getMode() == 3) {
					// 随机获取500-1000之间的数字
					int point = RandomUtil.nextIntNoZero(dummyProduct.getValue()) + 500;
					// 增加用户经验值;
					RankAction.addPoint(loginUser, point);
					// 更新虚拟物品使用次数
					UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
					tip = "您获得了" + point + "点经验值";
					result = "success";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}// 增加固定的乐币
				else if (dummyProduct.getDummyId() != 2
						&& dummyProduct.getMode() == 3) {
					// 随机获取500-1000之间的数字
					int point = RandomUtil.nextIntNoZero(dummyProduct.getValue()) + 500;
					// 增加用户经验值;
					RankAction.addPoint(loginUser, point);
					// 更新虚拟物品使用次数
					UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
					tip = "您获得了" + point + "点经验值";
					result = "success";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}// 更新用户帽子	
				else if (dummyProduct.getDummyId() ==6) {
					UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
					if(us!=null && us.getImagePathId()<0){
						tip = "你现在戴着一顶系统指定的帽子，要更换的话请先联系管理员~";
						result = "success";
						request.setAttribute("tip", tip);
						request.setAttribute("result", result);
						return;
					}
					// 更新用户帽子
					UserInfoUtil
					.updateUserStatus("image_path_id="+userBagId,"user_id=" + loginUser.getId(), loginUser
									.getId(), UserCashAction.OTHERS,
							"更新用户帽子");
					//清除帽子缓存
					UserStatusBean.flushUserHat(loginUser.getId());
					tip = "成功更新用户帽子";
					result = "success";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				} else if (dummyProduct.getMode() == 5) {		// 荣誉卡
					UserInfoUtil.addUserHonor(loginUser.getId(), dummyProduct.getValue());
					UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
					doTip("success", "你的荣誉增加了" + dummyProduct.getValue() + "点");
					return;
				} else if (dummyProduct.getMode() == 8) {		// 增加论坛经验值
					ForumUserBean fu = ForumCacheUtil.getForumUser(loginUser.getId());
					if(fu == null) {
						doTip("success", "你还无法使用这个物品");
						return;
					}
					fu.addExp(dummyProduct.getValue());
					ForumCacheUtil.updateForumUser(fu);
					UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
					doTip("success", "你的论坛经验增加了" + dummyProduct.getValue() + "点");
					return;
				} // 添加特殊家具
				else if (dummyProduct.getDummyId() != 2
						&& dummyProduct.getMode() == 1) {
					if (loginUser.getHome() == 0) {
						doTip(null, "您还没有开通家园,请开通家园!");
						return;
					}
					HomeImageBean homeImage = getHomeService().getHomeImage(
							"id=" + dummyProduct.getValue());
					if (homeImage == null) {
						doTip(null, "页面过期");
						return;
					}
					// macq_2006-12-20_增加家园的缓存_start
					HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser
							.getId());
					HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());
					int count = getHomeService().getHomeUserImageCount(
							"home_id=0 and user_id=" + loginUser.getId());
					if (count + 1 > bean.getGoods()) {
						doTip(null, "对不起，您的房子太小放不下新物品了，先处理一下旧东西吧！");
						return;
					}
					// 更新虚拟物品使用次数
					UserBagCacheUtil.updateUserBagCacheById("time=0", "id="
							+ userBagId, loginUser.getId(), userBagId);
					// 添加特殊家具
					HomeUserImageBean userImage = new HomeUserImageBean();
					userImage.setHomeId(0);
					userImage.setImageId(dummyProduct.getValue());
					userImage.setUserId(loginUser.getId());
					userImage.setTypeId(homeImage.getTypeId());
					getHomeService().addHomeUserImage(userImage);
					LoadResource.deleteHomeUserImageList(loginUser.getId());
					tip = dummyProduct.getName() + "已经放入您的仓库里";
					result = "success";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				} else {
					userProductBean(userBag,dummyProduct);
					return;
				}
				
				
				
			}
			int stack = getParameterIntS("stack");
			if(stack == -2) {		// 合并
				if(UserBagCacheUtil.stack(userBag)) {
					tip = "物品成功合并";
				} else {
					tip = "没有可以合并的物品";
				}
				request.setAttribute("tip", tip);
				request.setAttribute("result", "success");
				return;
			} else if(stack >= 0) {		// 拆散
				if(UserBagCacheUtil.checkUserBagCount(loginUser.getId())) {
					if(UserBagCacheUtil.unstack(userBag, stack)) {
						tip = "物品堆拆散成功";
					} else {
						tip = "无法拆散";
					}
				} else
					tip = "行囊已满";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "success");
				return;
			}
			
			if(request.getParameter("compose") != null) {		// 合成提示
				doTip("compose", "");
				return;
			}
			if(request.getParameter("compose2") != null) {		// 合成完毕
				int composeId = UserBagCacheUtil.compose(loginUser.getId(), userBagId); 
				if(composeId > 0) {
					HashMap map = getUserService().getItemComposeMap("1");
					ComposeBean compose = (ComposeBean) map.get(Integer.valueOf(composeId)); // 得到物品对应的合成公式，不需要判断有效，前面已判断
					StringBuilder tipb = new StringBuilder(64);
					tipb.append("合成得到以下物品：<br/>");
					Iterator iter = compose.getProductList().iterator(); // 生成物品放入行囊
					while (iter.hasNext()) {
						Integer iid = (Integer) iter.next();
						DummyProductBean d = dummyService.getDummyProducts(iid.intValue());
						if(d != null) {
							tipb.append(d.getName());
							tipb.append(d.getTimeString());
							tipb.append("<br/>");
						}
					}
					tip = tipb.toString();
				} else if(composeId == -1){
					tip = "无法进行合成，请准备好所有的材料后再试！";
				} else  if(composeId == -3){
					tip = "无法进行合成，有些物品是唯一的！";
				} else{
					tip = "合成失败，材料也损失了！";
				}
				doTip("composeResult", tip);
				return;
			}
		}
		doTip(null, null);
		return;
	}

	/**
	 * 
	 * @author macq explain : 赠送虚拟物品给好友的页面 datetime:2006-12-13 下午01:58:04
	 * @param request
	 */
	public void presentResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int friendId = StringUtil.toInt(request.getParameter("friendId"));
		int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
		if (friendId <= 0 || userBagId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserBean friend = UserInfoUtil.getUser(friendId);
		if (friend == null) {
			result = "failure";
			tip = "赠送物品接受对象不存在!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}

		UserBagBean userBag;
		DummyProductBean dummyProduct;
		synchronized(bagLock) {
//			 用户用户行囊记录
			userBag = UserBagCacheUtil.getUserBagCache(userBagId);
			// 判断用户是否拥有该商品
			if (userBag == null || userBag.getUserId() != loginUser.getId()) {
				result = "failure";
				tip = "您的行囊中没有该类商品!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// 获取虚拟物品信息
			dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
			if (dummyProduct == null || dummyProduct.isBind() || userBag.isMarkBind()) {
				result = "failure";
				tip = "该物品无法赠送";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			request.setAttribute("userBag", userBag);
			request.setAttribute("dummyProduct", dummyProduct);
			UserStatusBean friendUserStatus = UserInfoUtil.getUserStatus(friendId);
			if (friendUserStatus.isBagFull()) {
				result = "failure";
				tip = "对方行囊已满，不能接受您的赠送 ";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// 清空用户虚拟物品缓存
			UserBagCacheUtil
					.updateUserBagChangeCache("user_id=" + friendId, "id="
							+ userBag.getId(), loginUser.getId(), friendId, userBag.getId());
		}
		getUserService().addItemLog(loginUser.getId(), friendId, userBag.getId(), userBag.getProductId(), userBag.getTime(), 0);
		
		NoticeAction.sendNotice(friendId, loginUser.getNickName() + "送给你" + dummyProduct.getName() + userBag.getTimeString(dummyProduct)
				, "", NoticeBean.GENERAL_NOTICE, "", "/user/userBag.jsp");

		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain :聊天室发言页面使用虚拟物品处理方法 datetime:2006-12-14 上午11:09:30
	 * @param request
	 */
	public void userBagPost(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String roomId = request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		int userId = StringUtil.toInt(request.getParameter("userId"));
		if (userId <= 0) {
			result = "failure";
			tip = "请选择要接收道具的用户!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取用户行囊中物品id列表
		List userBagList1 = UserBagCacheUtil.getUserBagListCacheById(loginUser
				.getId());
		Vector userBagList = new Vector();
		Integer userBagId = null;
		UserBagBean userBag = null;
		for (int i = 0; i < userBagList1.size(); i++) {
			userBagId = (Integer) userBagList1.get(i);
			userBag = this.getUserBag(userBagId.intValue());
			
			if (userBag == null) {
				continue;
			}
			if (userBag.getTypeId() == COMMUNION_PROP|| userBag.getProductId()==PROP_DACOITY|| userBag.getTypeId()==6
					||userBag.getProductId()==75 || userBag.getProductId() == CUFF_ITEM) {		// 小石头
				userBagList.add(userBag);
			}
		}
		int totalCount = userBagList.size();
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
		String prefixUrl = "userBagPost.jsp?roomId=" + roomId + "&amp;userId="
				+ userId;
		// 取得要显示的消息列表x
		int start = pageIndex * NUMBER_PER_PAGE;
		int end = NUMBER_PER_PAGE;
		int startIndex = Math.min(start, userBagList.size());
		int endIndex = Math.min(start + end, userBagList.size());
		List userBagList2 = userBagList.subList(startIndex, endIndex);
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userBagList", userBagList2);
		request.setAttribute("userId", userId + "");
		request.setAttribute("roomId", roomId);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("userBagPost", "true");
		return;
	}

	/**
	 * @author guip: 特殊道具使用处理打劫卡 datetime 2007-07-25
	 * @author macq explain : 道具使用结果处理页面 datetime:2006-12-14 下午01:43:38
	 * @param request
	 */
	public void userBagPostResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		String roomId = request.getParameter("roomId");
		// 获取接受道具用户Id
		int userId = StringUtil.toInt(request.getParameter("userId"));
		// 获取用户道具Id
		int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
		// 判断参数有效性
		if (userId <= 0 || userBagId <= 0) {
			result = "failure";
			tip = "请选择要接收道具的用户或参数错误!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("roomId", roomId);
			return;
		}
		if (userId == 431 || userId == 519610 || userId == 914727) {
			result = "failure";
			tip = "不可以对管理员使用道具!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("roomId", roomId);
			return;
		}
		// 取得用户道具记录
		UserBagBean userBag = this.getUserBag(userBagId);
		// 判断用户是否拥护这样物品
		if (userBag == null || userBag.getUserId() != loginUser.getId()) {
			result = "failure";
			tip = "您没有此类商品!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("roomId", roomId);
			return;
		}
		// 判断物品是否为交流道具和特殊道具
		if (userBag.getTime() < 1 ) {
       //判断物品是否为特殊道具	
			if(userBag.getProductId()!=20) {
				result = "failure";
				tip = "您没有此类商品!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("roomId", roomId);
				return;
			}
			
		} 
			DummyProductBean dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
			UserBean user = UserInfoUtil.getUser(userId);
			String content = null;
			int isPrivate = 0;
		
			//判断物品是否是保镖卡,如果满足返回
			if (userBag.getProductId() == COMMUNION_PROP_BODYGUARD) {
				result = "failure";
				tip = "保镖卡不能主动使用!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				request.setAttribute("roomId", roomId);
				return;
			} 
			//判断是否是手铐
			else if(userBag.getProductId() == CUFF_ITEM) {
				SendAction.CuffUser cuffUser = (new SendAction(request)).new CuffUser();
				cuffUser.fromUid = userBag.getUserId();
				cuffUser.time = System.currentTimeMillis() + CUFF_TIME;
				synchronized(SendAction.handcuff) {
					SendAction.handcuff.put(new Integer(userId), cuffUser);
				}
				
				if(dummyProduct.getIntroduction() == null)
					content = "XBX用手铐铐住了你的手，现在你无法做任何动作";
				else 
					content = dummyProduct.getIntroduction();
				
				UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
				
				NoticeAction.sendNotice(userId, UserInfoUtil.getUser(userBag.getUserId()).getNickNameWml() + "用手铐铐住了你的手，现在你无法做任何动作"
						, "", NoticeBean.GENERAL_NOTICE, "", "/chat/hall.jsp");
			}
			// 判断物品是否是臭鸡蛋
			else if (userBag.getProductId() == COMMUNION_PROP_EGG) {
				// 更新臭鸡蛋的使用次数
				UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
						+ userBag.getId(), loginUser.getId(), userBag.getId());
				// 添加到禁止发言5分钟的map中
				stopSpeakMap.put(new Integer(userId), "");
				// 判断2个用户是否为好友,如果是更新好友度
				changeUserFriend(loginUser.getId(), userId, -10);
				changeUserFriend(userId, loginUser.getId(), -10);
				// 发送聊天内容
				content = dummyProduct.getIntroduction();
				// 判断是否可以在公聊大厅显示
//				if (getSendDummyTime() == 0) {
//					isPrivate = 0;
//				} else {
					isPrivate = 1;
//				}
				this.addContent(loginUser.getId(), user.getId(), loginUser
						.getNickName(), user.getNickName(), content, 8, "",
						isPrivate);
			} // 判断物品是否是啤酒瓶
			else if (userBag.getProductId() == COMMUNION_PROP_BEER) {
				// 更新啤酒瓶的使用次数
				UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
						+ userBag.getId(), loginUser.getId(), userBag.getId());
				// 判断登录用户跟接受道具用户是否为好友
				changeUserFriend(loginUser.getId(), userId, -20);
				changeUserFriend(userId, loginUser.getId(), -20);
				// 获取接受物品者状态信息
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
				if (userStatus != null && userStatus.getGamePoint() >= 200) {
					// 挨酒瓶子砸减200乐币
					UserInfoUtil.updateUserCash(userId, -200, UserCashAction.OTHERS,
							"挨酒瓶子砸减200乐币");
				}
				// 发送聊天内容
				content = dummyProduct.getIntroduction();
				// 判断是否可以在公聊大厅显示
//				if (getSendDummyTime() == 0) {
//					isPrivate = 0;
//				} else {
					isPrivate = 1;
//				}
				this.addContent(loginUser.getId(), user.getId(), loginUser
						.getNickName(), user.getNickName(), content, 8, "",
						isPrivate);
			} else if (userBag.getProductId() == COMMUNION_PROP_KNIVES) {		// 判断物品是否是西瓜刀
				// 更新西瓜刀的使用次数
				UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
						+ userBag.getId(), loginUser.getId(), userBag.getId());
				// 判断登录用户跟接受道具用户是否为好友
				changeUserFriend(loginUser.getId(), userId, -20);
				changeUserFriend(userId, loginUser.getId(), -20);
				// 获取接受物品者状态信息
				UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
				if (userStatus != null && userStatus.getGamePoint() >= 200) {
					// 挨西瓜刀砍减200乐币
					UserInfoUtil.updateUserCash(userId, -200, UserCashAction.OTHERS,
							"挨西瓜刀砍减200乐币");
				}
				// 发送聊天内容
				content = dummyProduct.getIntroduction();
				// 判断是否可以在公聊大厅显示
//				if (getSendDummyTime() == 0) {
//					isPrivate = 0;
//				} else {
					isPrivate = 1;
//				}
				this.addContent(loginUser.getId(), user.getId(), loginUser
						.getNickName(), user.getNickName(), content, 8, "", isPrivate);
			} else if (userBag.getProductId() == 75) {		// 判断物品是否是小石头
				UserBagCacheUtil.updateUserBagChangeCache("user_id=" + userId, "id="
						+ userBagId, loginUser.getId(), userId, userBagId);

				UserStatusBean userStatus = UserInfoUtil.getUserStatus(userId);
				if (userStatus != null && userStatus.getGamePoint() >= 200) {
					UserInfoUtil.updateUserCash(userId, -5000000, UserCashAction.OTHERS, "挨小石头减500万乐币");
				}
				NoticeAction.sendNotice(userId, "有人偷偷拿小石头把你砸晕了，损失500万"
						, "", NoticeBean.GENERAL_NOTICE, "", "/chat/hall.jsp");
				content = dummyProduct.getIntroduction();
				
			} else if (dummyProduct.getDummyId() == 6) {
				UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
						+ userBag.getId(), loginUser.getId(), userBag.getId());
				if(dummyProduct.getMode() == 0) {			// 判断物品是否是发一句话的，例如鞭炮
	
					content = dummyProduct.getIntroduction().replace("XAX", loginUser.getNickName());
					content = content.replace("XBX", user.getNickName());
					NoticeAction.sendNotice(userId, content
							, "", NoticeBean.GENERAL_NOTICE, "", "/chat/hall.jsp");
					content = dummyProduct.getIntroduction();
				} else if(dummyProduct.getMode() == 6) {		// 发一个动作
					int actionId = dummyProduct.getValue();
					RankActionBean checkAction = SendAction.getRankAction(actionId);
					if(checkAction != null) {
						content = checkAction.getReceiveMessage();
						content = content.replace("XXX", loginUser.getNickName());

						NoticeAction.sendNotice(userId, content, null, NoticeBean.GENERAL_NOTICE, "/chat/hall.jsp", "/chat/hall.jsp");

						int[] secRoomId = JCRoomChatAction.getSecondRoomId(
								loginUser.getId(), user.getId(), Integer
										.parseInt(roomId));

						JCRoomContentBean jcRoomContent = new JCRoomContentBean();
						jcRoomContent.setFromId(loginUser.getId());
						jcRoomContent.setToId(user.getId());
						jcRoomContent.setFromNickName(loginUser.getNickName());
						jcRoomContent.setToNickName(user.getNickName());
						jcRoomContent.setContent(String.valueOf(actionId));
						jcRoomContent.setAttach("");
						jcRoomContent.setIsPrivate(0);
						jcRoomContent.setRoomId(secRoomId[0]);
						jcRoomContent.setSecRoomId(secRoomId[1]);
						jcRoomContent.setMark(1);
						chatService.addContent(jcRoomContent);
					} else {
						content = "错误的道具使用";
					}
				} else if(dummyProduct.getMode() == 7) {
					// 增加双方友好度
					changeUserFriend(loginUser.getId(), user.getId(), dummyProduct.getValue());
					changeUserFriend(user.getId(), loginUser.getId(), dummyProduct.getValue());
					content = dummyProduct.getIntroduction().replace("XAX", loginUser.getNickName());
					content = content.replace("XBX", user.getNickName());
					NoticeAction.sendNotice(userId, content
							, "", NoticeBean.GENERAL_NOTICE, "", "/chat/hall.jsp");
					content = dummyProduct.getIntroduction();
				}
			} else if (userBag.getProductId() == COMMUNION_PROP_MESSAGE) {	// 判断物品是否为短信炸弹
				// 更新短信炸弹的使用次数
				UserBagCacheUtil.updateUserBagCacheById("time=time-1", "id="
						+ userBag.getId(), loginUser.getId(), userBag.getId());
				// 给用户发送一份信件
				MessageBean message = new MessageBean();
				message.setFromUserId(loginUser.getId());
				message.setToUserId(userId);
				message.setContent("轰！恭喜！你拆开了" + loginUser.getNickName()
						+ "邮寄给你的信件炸弹，被炸得灰头土脸！");
				message.setMark(0);
				getMessageService().addMessage(message);
				// 判断登录用户跟接受道具用户是否为好友
				changeUserFriend(loginUser.getId(), userId, -20);
				changeUserFriend(userId, loginUser.getId(), -20);
				// 判断是否可以在公聊大厅显示
//				if (getSendDummyTime() == 0) {
//					isPrivate = 0;
//				} else {
					isPrivate = 1;
//				}
				// 发送聊天内容
				content = dummyProduct.getIntroduction();
				this.addContent(loginUser.getId(), user.getId(), loginUser
						.getNickName(), user.getNickName(), content, 8,
						"bomb.gif", isPrivate);
			} else if (userBag.getProductId() == COMMUNION_PROP_DACOITY) {	// macq_2007-1-11_判断物品是否为打劫卡
				UserBagCacheUtil.updateUserBagCacheById("time=time-1",
						"id=" + userBag.getId(), loginUser.getId(), userBag
								.getId());// 更新打劫卡的使用次数
				changeUserFriend(loginUser.getId(), userId, -20);// 判断登录用户跟接受道具用户是否为好友
				changeUserFriend(userId, loginUser.getId(), -20);
				// 获取用户存款
				StoreBean store = BankCacheUtil.getBankStoreCache(userId);
				long money=0;
				boolean flag = false;
				if (store == null || store.getMoney() <= 1000000) {//用户没有银行存款或者存款小于100万
					// 发送聊天内容
					content = "XAX打劫了XBX，结果XBX是个穷光蛋……";
					
				} else {
					UserBagBean bodyguard = this.getUserBagBodyguard(userId);
					if (bodyguard != null) {// 被抢用户有保镖卡
						// 更新保镖卡的使用次数
						UserBagCacheUtil.updateUserBagCacheById("time=time-1",
								"id=" + bodyguard.getId(), userId, userBag
										.getId());
						// 发送聊天内容
						content = "XAX想抢XBX，反被XBX的黑人保镖狠揍了一顿!";
					} else {// 被抢用户没有保镖卡
						money = (long) Arith.div(Arith.mul(store.getMoney(), 10), 100, 0);//取得道具接收者10%用户存款
//						BankCacheUtil.updateBankStoreCacheById("money=money-"+money, "user_id="+userId, userId);
						BankCacheUtil.updateBankStoreCacheById((-1)*money, userId,0,Constants.BANK_USER_BAG_LOOT_TYPE);
						money = (long) Arith.div(Arith.mul(money, 50), 100, 0);//取得抢钱的5%更新道具使用者存款
						StoreBean loginUserBankStore = BankCacheUtil.getBankStoreCache(loginUser.getId());
						if(loginUserBankStore==null){//增加用户存款记录
							loginUserBankStore= new StoreBean();
							loginUserBankStore.setUserId(loginUser.getId());
							loginUserBankStore.setMoney(money);
							BankCacheUtil.addBankStoreCache(loginUserBankStore);
						}else{//更新用户存款记录
//							BankCacheUtil.updateBankStoreCacheById("money=money+"+money, "user_id="+loginUser.getId(), loginUser.getId());
							BankCacheUtil.updateBankStoreCacheById(money, loginUser.getId(),0,Constants.BANK_USER_BAG_LOOT_TYPE);
						}
						NoticeBean notice = new NoticeBean();// 添加提示消息
						notice.setUserId(userId);
						notice.setTitle("很不幸，您被打劫了!");
						notice.setContent("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("");
						notice.setLink("/chat/hall.jsp");
						ServiceFactory.createNoticeService().addNotice(
								notice);
						content = "打打打打劫！把ICIPIQ卡都拿出来！XAX抢劫了XBX!";
						flag=true;
					}		
				}
//				if (getSendDummyTime() == 0) {// 判断是否可以在公聊大厅显示
//					isPrivate = 0;
//				} else {
					isPrivate = 1;
//				}
				this.addContent(loginUser.getId(), user.getId(),
						loginUser.getNickName(), user.getNickName(),
						content, 8, "", isPrivate);
				if(flag){
					content = content+"抢到"+money+"乐币(存入银行)";
				}
				
			} else if(userBag.getProductId() == PROP_DACOITY)	 {	//用户使用打劫卡抢行囊道具
				//获得被抢劫用户行囊的信息
				List userBagList=UserBagCacheUtil.getUserBagListCacheById(userId);
				int size =userBagList.size();
				if(size>=0){
			    //取得随机数
			    int rand = RandomUtil.nextInt(size);
			   //取得随机数得到对应的id
			    Integer bag= (Integer)userBagList.get(rand);
			    int BagId= bag.intValue();
			    UserBagBean userBags = (UserBagBean)UserBagCacheUtil.getUserBagCache(BagId);
			   // 判断用户行囊是否已经满
			   if(UserBagCacheUtil.checkUserBagCount(loginUser.getId())){
			    //更改被抢劫的卡到用户行囊
			    UserBagCacheUtil.updateUserBagChangeCache("user_id = "+loginUser.getId()," id =" + userBags.getId(),userId,loginUser.getId(),userBags.getId());
			    getUserService().addItemLog(loginUser.getId(), userId, userBag.getId(), userBag.getProductId(), userBag.getTime(), 2);
			    //更改以用的打劫卡为不可用
			    UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
			    int productId = userBags.getProductId();
			    DummyServiceImpl dummyService =new DummyServiceImpl();
			    DummyProductBean dummy = dummyService.getDummyProducts(productId);
			    content = "嗖！恭喜您！您成功的偷去了乐客XAX行囊中的"+dummy.getName()+"道具";
			   }else
			   {
				    result = "failure";
					tip = "对不起，您行囊已满";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("roomId", roomId);
					return;  
			   }
				}else
				{
					result = "failure";
					tip = "对不起，该用户行囊没有道具，您白白损失一张打劫卡!";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					request.setAttribute("roomId", roomId);
					return;
				}
			}
			content = content.replace("XAX", "您");
			content = content.replace("XBX", StringUtil.toWml(user.getNickName()));
			if (userId == loginUser.getId()) {
				content = content.replace("XCX", "你");
			} else {
				String gender = user.getGender() == 0 ? "她" : "他";
				content = content.replace("XCX", gender);
			}
			//userBag = this.getUserBag(userBagId);// 取得用户道具记录
			// 判断西瓜刀的使用次数是否为0,如果是加提示
			if (userBag.getProductId() == COMMUNION_PROP_KNIVES
					&& userBag.getTime() == 1) {
				content = content + "<br/>您的西瓜刀已经砍卷刃啦，一破铁片，已经被您扔了。<br/>";
			}
			request.setAttribute("content", content);
		
		request.setAttribute("roomId", roomId);
		request.setAttribute("userId", userId + "");
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 更新用户好友度,并清空缓存 datetime:2006-12-15 上午09:54:22
	 * @param fromUserId
	 * @param toUserId
	 */
	public static void changeUserFriend(int userId, int friendId, int value) {
		UserFriendBean userFriend = getUserService().getUserFriend(userId,
				friendId);
		if (userFriend != null) {
			userFriend.setLevelValue(userFriend.getLevelValue() + value);
			if(value >0) {
				userService.updateFriend("level_value=level_value+" + value,
						"id=" + userFriend.getId());
			} else {
				userService.updateFriend("level_value=level_value" + value,
						"id=" + userFriend.getId());
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain : 获取用户是否有保镖卡
	 * @datetime:2007-1-12 上午10:02:56
	 * @param userId
	 * @param friendId
	 * @param value
	 */
	public UserBagBean getUserBagBodyguard(int userId) {
		UserBagBean userBag = getUserService().getUserBag(
				"user_id=" + userId + " and product_id=11 and time>0");
		return userBag;
	}

	/**
	 * @author macq explain : 判断用户使用道具后是否可以显示在公聊大厅的方法 datetime:2006-12-15
	 *         下午01:22:44
	 */
	public int getSendDummyTime() {
		int count = 0;
		String key = "isShow";
		// 从缓存中取
		Integer contentId = (Integer) OsCacheUtil.get(key,
				OsCacheUtil.USER_BAG_POST_CACHE_GROUP,
				OsCacheUtil.USER_BAG_POST_FLUSH_PERIOD);
		// 缓存中没有
		if (contentId == null) {
			// 从数据库中取
			String sql = "SELECT id from jc_room_content where mark=8 and (time_to_sec(now()) -time_to_sec(send_datetime))<300 limit 0,1";
			count = SqlUtil.getIntResult(sql, Constants.DBShortName);
			// 为空
			if (count == -1) {
				return 0;
			}
			contentId = new Integer(count);
			// 放到缓存中
			OsCacheUtil.put(key, contentId,
					OsCacheUtil.USER_BAG_POST_CACHE_GROUP);
		}
		return contentId.intValue();
	}

	/**
	 * 
	 * @author macq explain : 虚拟物品放入用户行囊 datetime:2006-12-18 下午03:21:17
	 * @return
	 */
	public void keepProduct(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 防止刷新
		if (session.getAttribute("randomuserbag") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("randomuserbag");
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "failure";
			tip = "系统中没有该类物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
				.getId());
		List userBagList = UserBagCacheUtil.getUserBagListCacheById(loginUser
				.getId());
		if (userBagList != null) {
			if (userStatus.getUserBag() <= userBagList.size()) {
				result = "failure";
				tip = "您的行囊已满.存放该物品";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
		}
		UserBagCacheUtil.addUserBagCache(loginUser.getId(), productId);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 从机会卡赠送好友物品 datetime:2006-12-18 下午07:28:38
	 * @param request
	 */
	public void fromCardPersent(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取虚拟物品信息
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "failure";
			tip = "系统中没有该类物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("dummyProduct", dummyProduct);
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
		String prefixUrl = "fromCardPersent.jsp?productId=" + productId;
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
		return;
	}

	/**
	 * 
	 * @author macq explain : 从机会卡结果页面处理赠送好友虚拟物品 datetime:2006-12-18 下午07:37:59
	 * @param request
	 */
	public void fromCardpresentResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 防止刷新
		if (session.getAttribute("randomuserbag") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("randomuserbag");
		int friendId = StringUtil.toInt(request.getParameter("friendId"));
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (friendId <= 0 || productId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		UserBean friend = UserInfoUtil.getUser(friendId);
		if (friend == null) {
			result = "failure";
			tip = "赠送物品接受对象不存在!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取虚拟物品信息
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "failure";
			tip = "系统中没有该类物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("dummyProduct", dummyProduct);
		UserStatusBean friendUserStatus = UserInfoUtil.getUserStatus(friendId);
		int userBagSize = friendUserStatus.getUserBag();
		List friendUserBagList = UserBagCacheUtil
				.getUserBagListCacheById(friendId);
		if (friendUserBagList.size() >= userBagSize) {
			result = "failure";
			tip = "对方行囊已满，不能接受您的赠送 ";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 清空用户虚拟物品缓存
		UserBagBean userBag = new UserBagBean();
		userBag.setUserId(friendId);
		userBag.setProductId(dummyProduct.getId());
		userBag.setTypeId(dummyProduct.getDummyId());
		userBag.setTime(dummyProduct.getTime());
		userBag.setMark(0);
		UserBagCacheUtil.addUserBagCache(userBag);
		// 给拍卖最终获得者发布发送消息
		NoticeBean notice = new NoticeBean();
		notice.setUserId(friendId);
		notice.setTitle(loginUser.getNickName() + "赠送给您一个礼物!");
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/user/userBag.jsp");
		ServiceFactory.createNoticeService().addNotice(notice);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 从机会卡直接使用道具 datetime:2006-12-19 上午11:28:20
	 * @param request
	 */
	public void fromCardUse(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 防止刷新
		if (session.getAttribute("randomuserbag") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("randomuserbag");
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取虚拟物品信息
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "failure";
			tip = "系统中没有该类物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("dummyProduct", dummyProduct);
		// 判断使用物品更新乐币(随机更新1-50万)
		if (dummyProduct.getDummyId() != 2 && dummyProduct.getMode() == 2) {
			// 随机获取1-50万
			int gamePoint = RandomUtil.nextIntNoZero(dummyProduct.getValue()) * 10000;
			UserInfoUtil.updateUserCash(loginUser.getId(), gamePoint,
					13, "使用宝箱增加乐币");
			tip = "您获得了" + gamePoint / 10000 + "万乐币";
			result = "success";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		} // 更新用户经验值
		else if (dummyProduct.getDummyId() != 2 && dummyProduct.getMode() == 3) {
			// 随机获取500-1000之间的数字
			int point = RandomUtil.nextIntNoZero(dummyProduct.getValue()) + 500;
			// 增加用户经验值;
			RankAction.addPoint(loginUser, point);
			tip = "您获得了" + point + "点经验值";
			result = "success";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		} // 添加特殊家具
		else if (dummyProduct.getDummyId() != 2 && dummyProduct.getMode() == 1) {
			if (loginUser.getHome() == 0) {
				result = "failure";
				tip = "您还没有开通家园,请开通家园!";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			HomeImageBean homeImage = getHomeService().getHomeImage(
					"id=" + dummyProduct.getValue());
			if (homeImage == null) {
				result = "failure";
				tip = "页面过期";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// macq_2006-12-20_增加家园的缓存_start
			HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser
					.getId());
			HomeTypeBean bean = getHomeType("id=" + homeUser.getTypeId());
			int count = 0;
			Vector userImageList = LoadResource.getHomeUserImageList(loginUser
					.getId());
			if (userImageList != null) {
				count = userImageList.size();
			}
			// int count = getHomeService().getHomeUserImageCount(
			// "home_id=0 and user_id=" + loginUser.getId());
			if (count + 1 >= bean.getGoods()) {
				result = "failure";
				tip = "对不起，您的房子太小放不下新物品了，先处理一下旧东西吧！";
				request.setAttribute("result", result);
				request.setAttribute("tip", tip);
				return;
			}
			// 添加特殊家具
			HomeUserImageBean userImage = new HomeUserImageBean();
			userImage.setHomeId(0);
			userImage.setImageId(dummyProduct.getValue());
			userImage.setUserId(loginUser.getId());
			userImage.setTypeId(homeImage.getTypeId());
			getHomeService().addHomeUserImage(userImage);
			LoadResource.deleteHomeUserImageList(loginUser.getId());
			tip = dummyProduct.getName() + "已经放入您的仓库里";
			result = "success";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}
		result = "failure";
		tip = "参数错误";
		request.setAttribute("result", result);
		request.setAttribute("tip", tip);
		return;
	}

	/**
	 * 
	 * explain :取得缓存的key。 datetime:2006-12-13 上午09:53:39
	 * 
	 * @param userId
	 * @return
	 */
	public static String getKey(int count) {
		return "" + count;
	}

	/**
	 * 
	 * @author macq explain : 添加聊天信息 datetime:2006-12-15 上午09:05:32
	 * @param fromUserId
	 * @param toUserId
	 * @param fromNickName
	 * @param toNickName
	 * @param content
	 * @param mark
	 * @param attach
	 * @param isPrivate
	 */
	public void addContent(int fromUserId, int toUserId, String fromNickName,
			String toNickName, String content, int mark, String attach,
			int isPrivate) {
		JCRoomContentBean jcRoomContent = new JCRoomContentBean();
		jcRoomContent.setFromId(fromUserId);
		jcRoomContent.setToId(toUserId);
		jcRoomContent.setFromNickName(fromNickName);
		jcRoomContent.setToNickName(toNickName);
		jcRoomContent.setContent(content);
		jcRoomContent.setAttach(attach);
		jcRoomContent.setIsPrivate(isPrivate);
		jcRoomContent.setRoomId(0);
		jcRoomContent.setSecRoomId(-1);
		jcRoomContent.setMark(mark);
		getChatService().addContent(jcRoomContent);
	}

	/**
	 * 
	 * @author macq explain :获取一条虚拟物品信息 datetime:2006-12-13 上午10:27:57
	 * @param DummyProductId
	 * @return
	 */
	public DummyProductBean getDummyProduct(int DummyProductId) {
		DummyProductBean dummyProduct = dummyService.getDummyProducts(DummyProductId);
		return dummyProduct;
	}

	/**
	 * 
	 * @author macq explain :获取所有虚拟物品分类 datetime:2006-12-13 上午10:29:29
	 * @return
	 */
	public Vector getDummyTypeList() {
		Vector dummyTypeList = getDummyService().getDummyTypeList("1=1");
		return dummyTypeList;
	}

	/**
	 * 
	 * @author macq explain : 获取登录用户信息 datetime:2006-12-13 上午11:14:22
	 * @return
	 */
	public UserBean getLoginUser() {
		return loginUser;
	}

	/**
	 * 
	 * @author macq explain : 获取用户行囊信息 datetime:2006-12-13 上午11:14:22
	 * @return
	 */
	public UserBagBean getUserBag(int userBagId) {
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		return userBag;
	}

	/**
	 * 
	 * @author macq explain : 清空使用臭鸡蛋道具不能说话的用户id列表 datetime:2006-12-15
	 *         上午10:56:58
	 */
	public static void clearStopSpeakMap() {
		stopSpeakMap.clear();
	}

	public HomeTypeBean getHomeType(String condition) {
		HomeTypeBean bean = getHomeService().getHomeType(condition);
		return bean;
	}
	
	/**
	 * 交易物品，包括道具和乐币（银行）
	 * 1、显示选择物品的页面
	 */
	public void tradeItem() {
		List userBagList = UserBagCacheUtil.getUserBagListCacheById(loginUser.getId());
		request.setAttribute("userBagList", userBagList);
		
		int userId = getParameterInt("userId");
		setAttribute("userId", userId);
		UserBean toUser = UserInfoUtil.getUser(userId);
		request.setAttribute("toUser", toUser);
		
	}
	
	/**
	 * 交易物品，包括道具和乐币（银行）
	 * 有几个可能：
	 * 1、显示赠送或者交易页面 get
	 * 2、直接赠送或者申请交易 post
	 * 3、显示更改交易页面 get
	 * 4、提交更改或者不更改，同时接受对方条件 post
	 */
	static byte[] bagLock = UserBagCacheUtil.getBagLock();
	static byte[] bankLock = BankCacheUtil.getBankLock();
	public void tradeView() {
		int tradeId = getParameterInt("tr");		// tr = trade当前tradeId，其实也是发起人的id
		if(isMethodGet()) {
			TradeBean trade = null;
			if(tradeId > 0) {		// accept
				trade = UserBagCacheUtil.getTrade(tradeId);
			}
			
			if(trade == null || trade.getUser(loginUser.getId()) == null) {	// 不存在，或者存在但不是自己的……
				tip("tip", "交易已结束");
			} else {
				int accept = getParameterIntS("a");	// 接受的对方status
				request.setAttribute("trade", trade);
				TradeUserBean o = trade.getOUser(loginUser.getId());
				TradeUserBean m = trade.getUser(loginUser.getId());
				if(accept >= 0) {
					if(accept == 0 || accept == o.getStatus()) {
						trade.getUser(loginUser.getId()).setAccept(accept);
						if(trade.isComplete()) {
							if(completeTrade(trade))
								tip("tip", "交易完成");
						}else {
							tip(null, "交易提交，请耐心等待对方回复。");
							if(o.isRead()) {
								NoticeAction.sendNotice(o.getUserId(), loginUser.getNickName() + "申请交易", null, NoticeBean.GENERAL_NOTICE, "", "/user/tradeView.jsp?tr=" + trade.getId());
								o.setRead(false);
							}
						}
					} else {
						tip("show");
					}
				} else if (accept == -2){		// 取消交易
					UserBagCacheUtil.removeTrade(tradeId);
					tip(null, "交易被取消");
				} else if (accept == -3){		// 直接赠送
					o.empty();	// 对方设置的清除
					if(m.isEmpty()) {
						tip("tip", "请选择要赠送的项目");
					} else if(m.getMoney() > 0 && m.getMoney() < 100000000 && m.getItems().size() == 0) {
						tip("tip", "银行转帐金额至少1亿乐币");
					} else if(completeTrade(trade))
						tip(null, "成功赠送");
				} else {
					tip("show");
				}
			}
		} else {
			String items = request.getParameter("items");
			long money = getParameterLong("money");	// 乐币
			TradeBean trade;
			if(tradeId == 0) {
			
				int userId = getParameterInt("userId");
				UserBean toUser = UserInfoUtil.getUser(userId);
				if(toUser == null) {
					tip(null, "申请交易失败，对方用户不存在");
					return;
				}
				trade = UserBagCacheUtil.newTrade(loginUser.getId(), userId);
				if(trade == null) {
					tip(null, "请结束之前的交易再申请新的交易");
					request.setAttribute("trade", UserBagCacheUtil.getTrade(loginUser.getId()));
					return;
				}
				
			} else {
				trade = UserBagCacheUtil.getTrade(tradeId);
				if(trade == null || trade.getUser(loginUser.getId()) == null) {
					tip("tip", "交易已结束！");
					return;
				}
			}
			
			if(money < 0 || BankCacheUtil.getStoreMoney(loginUser.getId()) < money) {
				tip("tip", "银行存款不足！");
				return;
			}
			if(money < 1000000)
				money = 0;

//			if(items != null && items.length() > 0) {
//			String[] items2 = items.split(";");
//			for(int i = 0;i < items2.length;i++) {
//				int tmp = StringUtil.toInt(items2[i]);
//				if(tmp > 0)
//					this.items.add(Integer.valueOf(tmp));
//			}
//		}
			List itemList;
			if(items == null) {
				itemList = new ArrayList(0);
			} else {
				HashSet have = new HashSet();	// 用于确保没有重复物品（交易漏洞）
				String[] items2 = items.split(";");
				itemList = new ArrayList(items2.length);
				for(int i = 0;i < items2.length;i++) {
					int[] i2 = new int[2];
					i2[0] = StringUtil.toInt(items2[i]);
					Integer i2key = new Integer(i2[0]);
					if(i2[0] > 0 && !have.contains(i2key)) {
						UserBagBean item = UserBagCacheUtil.getUserBagCache(i2[0]);
						if(item != null && item.getUserId() == loginUser.getId()) {
							i2[1] = item.getTime();	// 使用次数也保存下来
							itemList.add(i2);
							have.add(i2key);
						}
					}
				}
			
			}
					
			trade.update(loginUser.getId(), -1, money, itemList);
			request.setAttribute("trade", trade);
			tip("show");
		}
	}
	
	public Object getTradeUserLock(int userId) {
		return LockUtil.userLock.getLock(userId);
	}

	public boolean completeTrade(TradeBean trade) {
		int userId1 = trade.getUser1().getUserId();
		int userId2 = trade.getUser2().getUserId();
		Object userLock1, userLock2;
		if(userId1 > userId2) {		// 防止交叉死锁，总是先加int小的锁
			userLock2 = getTradeUserLock(userId1);
			userLock1 = getTradeUserLock(userId2);
		} else {
			userLock1 = getTradeUserLock(userId1);
			userLock2 = getTradeUserLock(userId2);
		}
		synchronized(userLock1) { synchronized(userLock2) {
			if(!checkTrade(trade.getUser1(), trade.getUser2()))
				return false;
			if(!checkTrade(trade.getUser2(), trade.getUser1()))
				return false;
			
			completeTrade(trade.getUser1(), trade.getUser2());
			completeTrade(trade.getUser2(), trade.getUser1());
			OsCacheUtil.flushGroup(OsCacheUtil.MONEYLOG_CACHE_GROUP, String.valueOf(userId1));
			OsCacheUtil.flushGroup(OsCacheUtil.MONEYLOG_CACHE_GROUP, String.valueOf(userId2));
			UserBagCacheUtil.removeTrade(trade.getId());
		}}
		return true;
	}

	// 检查是否够钱够物品，对方是否够行囊
	public boolean checkTrade(TradeUserBean from, TradeUserBean to) {
		if(from.getMoney() > 0) {
			if(BankCacheUtil.getStoreMoney(from.getUserId()) < from.getMoney()) {
				tip("tip", "存款不足，交易失败");
				return false;
			}
		}
		List items = from.getItems();
		List returnItems = to.getItems();
		int add = items.size() - returnItems.size();
		if(add > 0) {
			UserStatusBean friendUserStatus = UserInfoUtil.getUserStatus(to.getUserId());
			if(friendUserStatus.isBagFull(add)) {
				tip("tip", "行囊空间不足，交易失败");
				return false;
			}
		}
		
		for(int i = 0;i < items.size();i++) {
			int[] i2 = (int[])items.get(i);
			UserBagBean item = UserBagCacheUtil.getUserBagCache(i2[0]);
			if(item == null || item.getUserId() != from.getUserId() || item.getTime() != i2[1]) {
				tip("tip", "物品不足，交易失败");
				return false;
			}
			DummyProductBean dummyProduct = dummyService.getDummyProducts(item.getProductId());
			if(dummyProduct == null || dummyProduct.isBind() || item.isMarkBind()) {		// 绑定的不可以赠送
				tip("tip", "有些物品无法交易，交易失败");
				return false;
			}
		}
		
		return true;
	}
	
	// 完成交易，不允许失败
	public void completeTrade(TradeUserBean from, TradeUserBean to) {
		StringBuilder noticeContent = new StringBuilder();
		if(from.getMoney() > 0) {
			BankCacheUtil.updateBankStoreCacheById(from.getMoney(), to.getUserId(), from
					.getUserId(), Constants.BANK_TRADE_TYPE);
			BankCacheUtil.updateBankStoreCacheById(-from.getMoney(), from.getUserId(), to
					.getUserId(), Constants.BANK_TRADE_TYPE);
			noticeContent.append(from.getMoney());
			noticeContent.append("乐币(银行转帐)\n");
		}
		
		List items = from.getItems();

		for(int i = 0;i < items.size();i++) {
			int[] i2 = (int[])items.get(i);
			UserBagBean item = UserBagCacheUtil.getUserBagCache(i2[0]);
			DummyProductBean dummyProduct = dummyService.getDummyProducts(item.getProductId());
			UserBagCacheUtil.updateUserBagChangeCache("user_id=" + to.getUserId(), "id="
					+ i2[0], from.getUserId(), to.getUserId(), i2[0]);
			getUserService().addItemLog(from.getUserId(), to.getUserId(), item.getId(), item.getProductId(), item.getTime(), 1);
			
			noticeContent.append(dummyProduct.getName());
			noticeContent.append(item.getTimeString(dummyProduct));
			noticeContent.append("\n");
		}
		UserBean fromUser = UserInfoUtil.getUser(from.getUserId());
		if(!from.isEmpty())
			NoticeAction.sendNotice(to.getUserId(), "收到" + fromUser.getNickName() + "交易物品", noticeContent.toString(), NoticeBean.GENERAL_NOTICE, "", "");
	}
	
	public static int getCooldown(UserBagBean userBag, DummyProductBean item) {
		if(item.getCooldown() == 0)
			return 0;
		long now = System.currentTimeMillis();
		int left = (int)((userBag.getUseTime() - now) / 1000 + item.getCooldown() * 60);
		return left;
	}
	
	
	/**
	 * 新增的道具使用
	 * @param userBag
	 * @param dummyProduct
	 */
	private void userProductBean(UserBagBean userBag, DummyProductBean dummyProduct) {
		if(dummyProduct.getMode() == 15) {//为城堡战争卡
			if(ShopUtil.toCastGold(userBag.getUserId(), dummyProduct)){
				UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
				doTip("success", "使用成功,你的城堡账号增加了"+dummyProduct.getValue() + "个金币");
			} else {
				doTip("success", "没有开通城堡账号，不能使用");
			}
		} else if(dummyProduct.getMode() == 16) {	//人物经验卡
			//TODO
			int point = dummyProduct.getValue();
			// 增加用户经验值;
			RankAction.addPoint(loginUser, point);
			// 更新虚拟物品使用次数
			UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
			doTip("success", "你获得了" + point + "点经验值");
		} else if(dummyProduct.getMode() == 17) {	//酷币卷
			int gold = dummyProduct.getValue();
			ShopUtil.charge(userBag.getUserId(), gold);
			UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
			doTip("success", "您获得了" + gold + "g酷币");
		} else if(dummyProduct.getMode() == 18){	// 更改 UserHonorBean.rank值
			
			int place = dummyProduct.getValue();
			UserHonorBean honorBean = UserInfoUtil.getUserHonor(userBag.getUserId());
			if(honorBean.getUserId() == 0) {
				honorBean = UserInfoUtil.addUserHonor(userBag.getUserId(), 1);
			}
			userService.updateUserHonor(" last_week=1,place = " + place, " user_id = " + userBag.getUserId());
		
			honorBean.setPlace(place);
			honorBean.setLastWeek(1);
			honorBean.calcHonorRank();

			UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
			doTip("success", "你获得了" + honorBean.getRank() + "星勋章");
		} else if(dummyProduct.getMode() == 19){	//论坛VIP卡 更改ForumUserBean.vip的值
			JcForumServiceImpl service = new JcForumServiceImpl();
			ForumUserBean forumUserBean = ForumCacheUtil.getAddForumUser(userBag.getUserId());
			int value = dummyProduct.getValue();	//以天数来计算vip时间
			long now = System.currentTimeMillis();
			long vip = 0;
			if(forumUserBean.getVip() > now)
				vip = forumUserBean.getVip() + value * DateUtil.MS_IN_DAY;
			else 
				vip = System.currentTimeMillis() + value * DateUtil.MS_IN_DAY;
			Date vipDate = new Date(vip);
			service.updateForumUser(" vip = '" + DateUtil.formatDate(vipDate, DateUtil.normalTimeFormat) + "'", "user_id = " + userBag.getUserId());
			forumUserBean.setVip(vipDate.getTime());
			
			UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
			doTip("success", "您获得了" + value + "天论坛VIP");
		} else if(dummyProduct.getMode() == 20){		//鹦鹉卡
			
			JcForumServiceImpl service = new JcForumServiceImpl();
			ForumUserBean forumUserBean = ForumCacheUtil.getAddForumUser(userBag.getUserId());
			int value = dummyProduct.getValue();	//论坛经验200点
			forumUserBean.setExp(forumUserBean.getExp() + value);
			//System.out.println(forumUserBean.getExp());
			service.updateForumUser(" exp = " + forumUserBean.getExp(), " user_id = " + userBag.getUserId());
			
			UserBagCacheUtil.UseUserBagCache(userBag, dummyProduct);
			doTip("success", "获得论坛经验" + value + "点");
		}
		
	}
	
	
}
