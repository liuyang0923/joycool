package net.joycool.wap.action.auction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.auction.AuctionHistoryBean;
import net.joycool.wap.bean.bank.StoreBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.AuctionCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IAuctionService;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.BankCacheUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author macq
 * @datetime 2006-12-15 下午03:04:40
 * @explain 拍卖系统
 */
public class AuctionAction extends CustomAction{

	static long MAX_AUCTION_PRICE = 1000000000000l;

	static int NUMBER_PER_PAGE = 15;

	static int USR_AUCTION_NUMBER_PER_PAGE = 10;

	UserBean loginUser = null;

	static IUserService userService = ServiceFactory.createUserService();;

	static IDummyService dummyService = ServiceFactory.createDummyService();;

	static IChatService chatService = ServiceFactory.createChatService();;

	static IAuctionService auctionService = ServiceFactory.createAuctionService();;

	// 增加拍卖大厅出售日志记录
	public static List log = new ArrayList();

	// 日志记录大小
	final static int logSize = 20;

	public static HashMap stopSpeakMap = new HashMap();

	public AuctionAction(HttpServletRequest request) {
		super(request);
		loginUser = getLoginUser();
		if (loginUser != null){
			loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
		} else {
			loginUser = new UserBean();
		}
	}

	public IAuctionService getAuctionService() {
		return auctionService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public IChatService getChatService() {
		return chatService;
	}

	public IDummyService getDummyService() {
		return dummyService;
	}

	/**
	 * 拍卖物品首页
	 * 
	 * @author macq explain : datetime:2006-12-15 下午03:06:29
	 * @param request
	 */
	public void index(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取等待拍卖物品的ID
		int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
		if (userBagId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取用户行囊记录
		UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
		if (userBag == null || userBag.getTime() < 1
				|| userBag.getUserId() != loginUser.getId()) {
			result = "failure";
			tip = "您没有该类型物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取虚拟物品介绍记录
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
		if (dummyProduct == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("userBagId", userBagId + "");
		request.setAttribute("dummyProduct", dummyProduct);
		result = "success";
		session.setAttribute("auctionCheck", "ture");
		request.setAttribute("result", result);
	}

	static byte[] bagLock = UserBagCacheUtil.getBagLock();
	/**
	 * 
	 * @author macq explain : 拍卖物品提交页面 datetime:2006-12-15 下午03:27:21
	 * @param request
	 */
	public void sale(HttpServletRequest request) {
		String result = null;
		String tip = null;
		if (session.getAttribute("auctionCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("auctionCheck");
		// 获取等待拍卖物品的ID
		int userBagId = StringUtil.toInt(request.getParameter("userBagId"));
		if (userBagId <= 0) {
			doTip(null, null);
			return;
		}
		

		AuctionBean auction = new AuctionBean();
		synchronized(bagLock) {
			// 获取用户行囊记录
			UserBagBean userBag = UserBagCacheUtil.getUserBagCache(userBagId);
			if (userBag == null || userBag.getTime() < 1
					|| userBag.getUserId() != loginUser.getId()) {
				doTip(null, "您没有该类型物品");
				return;
			}
			// 获取虚拟物品介绍记录
			DummyProductBean dummyProduct = getDummyService().getDummyProducts(userBag.getProductId());
			if (dummyProduct == null || dummyProduct.isBind() || userBag.isMarkBind() || dummyProduct.getDummyId() > 10) {
				doTip(null, "该物品无法拍卖");
				return;
			}
			// 获取一口价跟起价的值
			long startPice = StringUtil.toLong(request.getParameter("startPice"));
			long bitePrice = StringUtil.toLong(request.getParameter("bitePrice"));
			if (startPice < 0 || bitePrice < 0) {
				doTip(null, "请确认输入金额!");
				return;
			}
			// 判断输入值是否大于500亿
			if (startPice > MAX_AUCTION_PRICE || bitePrice > MAX_AUCTION_PRICE) {
				doTip(null, "最大起价和最大一口价限制为" + MAX_AUCTION_PRICE + "乐币!");
				return;
			}
			// 判断起价大于等于一口价
			if (startPice >= bitePrice) {
				doTip(null, "起价必须小于一口价!");
				return;
			}
			if (startPice * 100 < bitePrice) {
				doTip(null, "起价不能小于一口价的百分之一!");
				return;
			}
			// 更新用户虚拟物品状态，这个物品变成无人的
			UserBagCacheUtil.updateUserBagCacheById("user_id=0", "id=" + userBagId,
					loginUser.getId(), userBagId);
			
			// 添加拍卖记录
			auction.setLeftUserId(loginUser.getId());
			auction.setRightUserId(0);
			auction.setProductId(dummyProduct.getId());
			auction.setDummyId(dummyProduct.getDummyId());
			auction.setTime(userBag.getTime());
			auction.setStartPrice(startPice);
			auction.setBitePrice(bitePrice);
			auction.setCurrentPrice(startPice);
			auction.setUserBagId(userBagId);
			auction.setMark(0);
		}

		AuctionCacheUtil.addAction(auction);
		// 在聊天大厅增加拍卖物品信息
		// JCRoomContentBean jcRoomContent = new JCRoomContentBean();
		// jcRoomContent = new JCRoomContentBean();
		// jcRoomContent.setFromId(0);
		// jcRoomContent.setToId(0);
		// jcRoomContent.setFromNickName("");
		// jcRoomContent.setToNickName("");
		// jcRoomContent.setContent("拍卖大厅开始拍卖新物品了!");
		// jcRoomContent.setAttach("");
		// jcRoomContent.setIsPrivate(0);
		// jcRoomContent.setRoomId(0);
		// jcRoomContent.setSecRoomId(-1);
		// jcRoomContent.setMark(9);
		// getChatService().addContent(jcRoomContent);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	public List getAuctionList() {
		String key = "auctionList";
		List auctionList = (List) OsCacheUtil.get(key,
				OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP,
				OsCacheUtil.USER_AUCTION_BY_ID_FLUSH_PERIOD);
		if (auctionList == null) {
			int maxId = SqlUtil.getIntResult("select max(id) from jc_auction");
			String sql = "SELECT id FROM jc_auction where mark=0 and id>" + (maxId - 5000);
			auctionList = SqlUtil.getIntList(sql, Constants.DBShortName);
			if (auctionList == null) {
				auctionList = new ArrayList(1);
			}
			OsCacheUtil.put(key, auctionList,
					OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP);
		}
		return auctionList;
	}
	
	/**
	 * @author macq explain : 获取所有拍卖记录 datetime:2006-12-15 下午05:49:14
	 */
	public void auctionHall(HttpServletRequest request) {
		List auctionList = getAuctionList();
		
		int type = getParameterInt("type");//	拍卖行分类
		// 物品列表
		Vector itemList = new Vector();
		Integer auctionId = null;
		AuctionBean auction = null;
		for (int i = 0; i < auctionList.size(); i++) {
			auctionId = (Integer) auctionList.get(i);
			auction = AuctionCacheUtil
					.getAuctionCacheById(auctionId.intValue());
			if (auction == null) {
				continue;
			}
			// 判断拍卖物品分类
			if (auction.getDummyId() == 1 && type == 1) {
				itemList.add(auction);
			}else if(auction.getDummyId() == 2 && type == 2) {
				itemList.add(auction);
			}else if(auction.getDummyId() > 2 && type == 0){
				itemList.add(auction);
			}
		}
		// 物品的分页
		
		PagingBean page = new PagingBean(this, itemList.size(), NUMBER_PER_PAGE, "p");
		request.setAttribute("page", page);
		
		int start = page.getStartIndex();
		int end = page.getEndIndex();
		request.setAttribute("itemList", itemList.subList(start, end));
	}
	
	// 搜索拍卖行
	public void search() {
		int s = getParameterInt("s");		// dummyid开始
		int e = getParameterInt("e");
		if(s > 0 && e > 0) {
			List auctionList = getAuctionList();
			
			int type = getParameterInt("type");//	拍卖行分类
			// 物品列表
			Vector itemList = new Vector();
			Integer auctionId = null;
			AuctionBean auction = null;
			for (int i = 0; i < auctionList.size(); i++) {
				auctionId = (Integer) auctionList.get(i);
				auction = AuctionCacheUtil
						.getAuctionCacheById(auctionId.intValue());
				if (auction == null) {
					continue;
				}
				// 判断拍卖物品分类
				if (auction.getProductId() >= s && auction.getProductId() <= e) {
					itemList.add(auction);
				}
			}
			// 物品的分页
			
			PagingBean page = new PagingBean(this, itemList.size(), NUMBER_PER_PAGE, "p");
			request.setAttribute("page", page);
			
			int start = page.getStartIndex();
			int end = page.getEndIndex();
			request.setAttribute("itemList", itemList.subList(start, end));
		} else {
			
		}
	}

	/**
	 * 
	 * @author macq explain : 用户购买物品页面 datetime:2006-12-15 下午07:47:56
	 * @param request
	 */
	public void buy(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int auctionId = StringUtil.toInt(request.getParameter("auctionId"));
		if (auctionId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		AuctionBean auction = AuctionCacheUtil.getAuctionCacheById(auctionId);
		if (auction == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		if (auction.getMark() == 1) {
			result = "failure";
			tip = "该商品已经被拍出!请选择其他商品!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		DummyProductBean dummyProduct = getDummyProduct(auction.getProductId());
		if (dummyProduct == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("auction", auction);
		request.setAttribute("dummyProduct", dummyProduct);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("auctionBuyCheck", "ture");
		return;
	}

	/**
	 * @author macq explain : 购买虚拟物品结果页面 datetime:2006-12-18 上午10:19:36
	 * @param request
	 */
	public static byte[] getAuctionLock() {
		return lock;
	}
	static byte[] lock = new byte[0]; 	// 防止两个人同时拍卖到同一个东西
	public void buyResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 防止刷新
		if (session.getAttribute("auctionBuyCheck") == null) {
			doTip("refrush", null);
			return;
		}
		session.removeAttribute("auctionBuyCheck");
		// 验证购买类型
		int type = StringUtil.toInt(request.getParameter("type"));
		if (type <= 0) {
			doTip(null, null);
			return;
		}
		// 获取购买记录id
		int auctionId = StringUtil.toInt(request.getParameter("auctionId"));
		if (auctionId <= 0) {
			doTip(null, null);
			return;
		}
		// 获取购买记录
		synchronized(lock) {
			AuctionBean auction = AuctionCacheUtil.getAuctionCacheById(auctionId);
			if (auction == null) {
				doTip(null, null);
				return;
			}
			if (auction.getMark() == 1) {
				doTip(null, "该商品已经被拍出!请选择其他商品!");
				return;
			}
			// 获取物品信息
			DummyProductBean dummyProduct = getDummyProduct(auction.getProductId());
			if (dummyProduct == null) {
				doTip(null, null);
				return;
			}
			// 参与竞价
			synchronized(loginUser.getLock()) {
				if (type == 1) {
					// 取得最新的竞价值
					long changePrice = Math.round(auction.getCurrentPrice() * 1.1);
					if(changePrice < 100000l)
						changePrice += 10000l;
					// UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					// .getId());
					// 获取用户存款
					StoreBean store = BankCacheUtil
							.getBankStoreCache(loginUser.getId());
					if (store == null || changePrice > store.getMoney()) {
						doTip(null, "您银行的的存款不够参与竞价,请先存款!");
						return;
					}
					// 退还上一个参与竞价用户乐币
					if (auction.getRightUserId() != 0) {
						// UserInfoUtil.updateUserStatus("game_point=game_point+"
						// + auction.getCurrentPrice(), "user_id="
						// + auction.getRightUserId(), auction.getRightUserId(),
						// UserCashAction.OTHERS, "退还前一个参与竞价用户的乐币");
						StoreBean rightUserBankStore = BankCacheUtil
								.getBankStoreCache(auction.getRightUserId());
						if (rightUserBankStore == null) {// 增加用户存款记录
							rightUserBankStore = new StoreBean();
							rightUserBankStore.setUserId(auction.getRightUserId());
							rightUserBankStore.setMoney(auction.getCurrentPrice());
							BankCacheUtil.addBankStoreCache(rightUserBankStore);
						} else {// 更新用户存款记录
						// BankCacheUtil.updateBankStoreCacheById("money=money+"+auction.getCurrentPrice(),
						// "user_id="+auction.getRightUserId(),
						// auction.getRightUserId());
							BankCacheUtil.updateBankStoreCacheById(auction
									.getCurrentPrice(), auction.getRightUserId(),0,Constants.BANK_ACUTION_TYPE);
						}
					}
					// 如果竞价大于一口价,直接购买成功
					if (changePrice > auction.getBitePrice()) {
						if(auction.getRightUserId() != 0 && auction.getRightUserId() != loginUser.getId()){
							NoticeBean notice = new NoticeBean();
							notice.setUserId(auction.getRightUserId());
							notice.setTitle("您竞价的"+dummyProduct.getName()+"已被别人买走");
							notice.setContent("");
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setHideUrl("");
							notice.setMark(NoticeBean.PK_GAME);
							notice.setLink("/auction/auctionHall.jsp");
							ServiceFactory.createNoticeService().addNotice(notice);
						}
						// 更新当前用户乐币
						// UserInfoUtil.updateUserStatus("game_point=game_point-"
						// + auction.getBitePrice(), "user_id="
						// + loginUser.getId(), loginUser.getId(),
						// UserCashAction.OTHERS, "参与一口价扣除用户乐币");
						// BankCacheUtil.updateBankStoreCacheById("money=money-"+auction.getBitePrice(),
						// "user_id="+loginUser.getId(), loginUser.getId());
						BankCacheUtil.updateBankStoreCacheById((-1)
								* auction.getBitePrice(), loginUser.getId(),0,Constants.BANK_ACUTION_TYPE);
						// 更新拍卖记录
						AuctionCacheUtil.updateActionCacheByUserId("right_user_id="
								+ loginUser.getId() + ",current_price="
								+ auction.getBitePrice() + ",mark=1",
								"id=" + auctionId, loginUser.getId(), auctionId);
						// 添加拍卖历史记录
						AuctionHistoryBean auctionHistory = new AuctionHistoryBean();
						auctionHistory.setAuctionId(auctionId);
						auctionHistory.setProductId(auction.getProductId());
						auctionHistory.setDummyId(auction.getDummyId());
						auctionHistory.setUserId(loginUser.getId());
						auctionHistory.setPrice(auction.getBitePrice());
						AuctionCacheUtil.addActionHistory(auctionHistory);
						// 更新用户行囊记录
						if(auction.getUserBagId() == 0) {		// 老数据格式兼容
							UserBagBean userBag = new UserBagBean();
							userBag.setUserId(loginUser.getId());
							userBag.setProductId(auction.getProductId());
							userBag.setTypeId(auction.getDummyId());
							userBag.setTime(auction.getTime());
							userBag.setMark(0);
							UserBagCacheUtil.addUserBagCache(userBag);
						} else {
							UserBagCacheUtil.changeUserBagOwner(loginUser.getId(), auction.getUserBagId());
							userService.addItemLog(auction.getLeftUserId(), loginUser.getId(), auction.getUserBagId(), auction.getProductId(), auction.getTime(), 7);
							setAttribute("userBagId", auction.getUserBagId());
						}
						// 给拍卖者加钱,收取拍卖金额的10%做为手续费
						long gamePoint = calcSellMoney(auction.getBitePrice());
						// UserInfoUtil.updateUserStatus("game_point=game_point+"
						// + gamePoint, "user_id=" + auction.getLeftUserId(),
						// auction.getLeftUserId(), UserCashAction.OTHERS,
						// "拍卖完成给拍卖用户加钱");
						StoreBean leftUserBankStore = BankCacheUtil
								.getBankStoreCache(auction.getLeftUserId());
						if (leftUserBankStore == null) {// 增加用户存款记录
							leftUserBankStore = new StoreBean();
							leftUserBankStore.setUserId(auction.getLeftUserId());
							leftUserBankStore.setMoney(gamePoint);
							BankCacheUtil.addBankStoreCache(leftUserBankStore);
						} else {// 更新用户存款记录
						// BankCacheUtil.updateBankStoreCacheById("money=money+"+gamePoint,
						// "user_id="+auction.getLeftUserId(), auction.getLeftUserId());
							BankCacheUtil.updateBankStoreCacheById(gamePoint, auction
									.getLeftUserId(),0,Constants.BANK_ACUTION_TYPE);
						}
						// 给拍卖发布者发布发送消息
						NoticeBean notice = new NoticeBean();
						notice.setUserId(auction.getLeftUserId());
						notice.setTitle(dummyProduct.getName() + "拍出获" + gamePoint
								+ "乐币");
						notice.setContent("");
						notice.setType(NoticeBean.GENERAL_NOTICE);
						notice.setHideUrl("");
						notice.setLink("/user/userBag.jsp");
						ServiceFactory.createNoticeService().addNotice(notice);
						result = "success";
						tip = "竞价价格大于一口价价格!购买成功!!";
						// macq_2007-7-3_增加拍卖物品成交记录日志
						addLog(dummyProduct.getName() + " " + StringUtil.bigNumberFormat(auction.getBitePrice()) + "乐币");
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						return;
					} else {
						if(auction.getRightUserId() != 0 && auction.getRightUserId() != loginUser.getId()){
							NoticeBean notice = new NoticeBean();
							notice.setUserId(auction.getRightUserId());
							notice.setTitle("您对"+dummyProduct.getName()+ "的竞价已被别人超过");
							notice.setContent("");
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setHideUrl("");
							notice.setMark(NoticeBean.PK_GAME);
							notice.setLink("/auction/buy.jsp?auctionId="+auctionId);
							ServiceFactory.createNoticeService().addNotice(notice);
						}
						// 参与竞价
						// 更新当前用户乐币
						// UserInfoUtil.updateUserStatus("game_point=game_point-"
						// + changePrice, "user_id=" + loginUser.getId(),
						// loginUser.getId(), UserCashAction.OTHERS, "参与竞价扣除用户乐币");]
						// BankCacheUtil.updateBankStoreCacheById("money=money-"+changePrice,
						// "user_id="+loginUser.getId(), loginUser.getId());
						BankCacheUtil.updateBankStoreCacheById((-1) * changePrice,
								loginUser.getId(),0,Constants.BANK_ACUTION_TYPE);
						// 更新拍卖记录
						AuctionCacheUtil.updateActionCacheByUserId("right_user_id="
								+ loginUser.getId() + ",current_price=" + changePrice + ",last_datetime=now()",
								"id=" + auctionId, loginUser.getId(), auctionId);
						// 添加拍卖历史记录
						AuctionHistoryBean auctionHistory = new AuctionHistoryBean();
						auctionHistory.setAuctionId(auctionId);
						auctionHistory.setProductId(auction.getProductId());
						auctionHistory.setDummyId(auction.getDummyId());
						auctionHistory.setUserId(loginUser.getId());
						auctionHistory.setPrice(StringUtil.toInt(changePrice + ""));
						AuctionCacheUtil.addActionHistory(auctionHistory);
						result = "success";
						tip = "竞价成功!!";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						return;
					}
				}// 参与一口价
				else if (type == 2) {
					// 取得最新的竞价值
					long getBitePrice = auction.getBitePrice();
					// UserStatusBean userStatus = UserInfoUtil.getUserStatus(loginUser
					// .getId());
					// 获取用户存款
					StoreBean store = BankCacheUtil
							.getBankStoreCache(loginUser.getId());
					if (store == null || getBitePrice > store.getMoney()) {
						result = "failure";
						tip = "您银行的的存款不够,不能直接购买该物品,请先存款!";
						request.setAttribute("result", result);
						request.setAttribute("tip", tip);
						return;
					}
					// 退还上一个参与竞价用户乐币
					if (auction.getRightUserId() != 0) {
						// UserInfoUtil.updateUserStatus("game_point=game_point+"
						// + auction.getCurrentPrice(), "user_id="
						// + auction.getRightUserId(), auction.getRightUserId(),
						// UserCashAction.OTHERS, "退还前一个参与竞价用户的乐币");
						StoreBean rightUserBankStore = BankCacheUtil
								.getBankStoreCache(auction.getRightUserId());
						if (rightUserBankStore == null) {// 增加用户存款记录
							rightUserBankStore = new StoreBean();
							rightUserBankStore.setUserId(auction.getRightUserId());
							rightUserBankStore.setMoney(auction.getCurrentPrice());
							BankCacheUtil.addBankStoreCache(rightUserBankStore);
						} else {// 更新用户存款记录
						// BankCacheUtil.updateBankStoreCacheById("money=money+"+auction.getCurrentPrice(),
						// "user_id="+auction.getRightUserId(),
						// auction.getRightUserId());
							BankCacheUtil.updateBankStoreCacheById(auction
									.getCurrentPrice(), auction.getRightUserId(),0,Constants.BANK_ACUTION_TYPE);
						}
						if (auction.getRightUserId() != loginUser.getId()) {
							NoticeBean notice = new NoticeBean();
							notice.setUserId(auction.getRightUserId());
							notice.setTitle("您竞价的" + dummyProduct.getName() + "已被别人买走");
							notice.setContent("");
							notice.setType(NoticeBean.GENERAL_NOTICE);
							notice.setHideUrl("");
							notice.setMark(NoticeBean.PK_GAME);
							notice.setLink("/auction/auctionHall.jsp");
							ServiceFactory.createNoticeService().addNotice(notice);
						}
					}
					// 更新当前用户乐币
					// UserInfoUtil.updateUserStatus("game_point=game_point-"
					// + getBitePrice, "user_id=" + loginUser.getId(), loginUser
					// .getId(), UserCashAction.OTHERS, "参与一口价扣除用户乐币");
					// BankCacheUtil.updateBankStoreCacheById("money=money-"+auction.getBitePrice(),
					// "user_id="+loginUser.getId(), loginUser.getId());
					BankCacheUtil.updateBankStoreCacheById((-1)
							* auction.getBitePrice(), loginUser.getId(),0,Constants.BANK_ACUTION_TYPE);
					// 更新拍卖记录
					AuctionCacheUtil.updateActionCacheByUserId("right_user_id="
							+ loginUser.getId() + ",current_price=" + getBitePrice
							+ ",mark=1", "id=" + auctionId, loginUser.getId(),
							auctionId);
					// 添加拍卖历史记录
					AuctionHistoryBean auctionHistory = new AuctionHistoryBean();
					auctionHistory.setAuctionId(auctionId);
					auctionHistory.setProductId(auction.getProductId());
					auctionHistory.setDummyId(auction.getDummyId());
					auctionHistory.setUserId(loginUser.getId());
					auctionHistory.setPrice(getBitePrice);
					AuctionCacheUtil.addActionHistory(auctionHistory);
					// 更新用户行囊记录
					if(auction.getUserBagId() == 0) {	// 兼容老的数据
						UserBagBean userBag = new UserBagBean();
						userBag.setUserId(loginUser.getId());
						userBag.setProductId(auction.getProductId());
						userBag.setTypeId(auction.getDummyId());
						userBag.setTime(auction.getTime());
						userBag.setMark(0);
						UserBagCacheUtil.addUserBagCache(userBag);
					} else {
						UserBagCacheUtil.changeUserBagOwner(loginUser.getId(), auction.getUserBagId());
						userService.addItemLog(auction.getLeftUserId(), loginUser.getId(), auction.getUserBagId(), auction.getProductId(), auction.getTime(), 7);
						setAttribute("userBagId", auction.getUserBagId());
					}
					// 给拍卖者加钱,收取拍卖金额的10%做为手续费
					long gamePoint = calcSellMoney(getBitePrice);
					// UserInfoUtil.updateUserStatus(
					// "game_point=game_point+" + gamePoint, "user_id="
					// + auction.getLeftUserId(), auction.getLeftUserId(),
					// UserCashAction.OTHERS, "拍卖完成给拍卖用户加钱");
					StoreBean leftUserBankStore = BankCacheUtil
							.getBankStoreCache(auction.getLeftUserId());
					if (leftUserBankStore == null) {// 增加用户存款记录
						leftUserBankStore = new StoreBean();
						leftUserBankStore.setUserId(auction.getLeftUserId());
						leftUserBankStore.setMoney(gamePoint);
						BankCacheUtil.addBankStoreCache(leftUserBankStore);
					} else {// 更新用户存款记录
					// BankCacheUtil.updateBankStoreCacheById("money=money+"+gamePoint,
					// "user_id="+auction.getLeftUserId(), auction.getLeftUserId());
						BankCacheUtil.updateBankStoreCacheById(gamePoint, auction
								.getLeftUserId(),0,Constants.BANK_ACUTION_TYPE);
					}
					// 给拍卖发布者发布发送消息
					NoticeBean notice = new NoticeBean();
					notice.setUserId(auction.getLeftUserId());
					notice.setTitle(dummyProduct.getName() + "拍出获" + gamePoint + "乐币");
					notice.setContent("");
					notice.setType(NoticeBean.GENERAL_NOTICE);
					notice.setHideUrl("");
					notice.setLink("/user/userBag.jsp");
					ServiceFactory.createNoticeService().addNotice(notice);
					result = "success";
					tip = "购买成功!!";
					// macq_2007-7-3_增加拍卖物品成交记录日志
					addLog(dummyProduct.getName() + " " + StringUtil.bigNumberFormat(getBitePrice) + "乐币");
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
			}
		}
	}

	/**
	 * 
	 * @author macq explain : 我拍卖的物品页面 datetime:2006-12-18 下午05:39:29
	 * @param request
	 */
	public void myAuction(HttpServletRequest request) {
		String result = null;
		String tip = null;
		List userAuctionList = AuctionCacheUtil
				.getAuctionCacheByUserId(loginUser.getId());
		// liuyi 2007-01-09 程序优化 start
		if (userAuctionList == null || userAuctionList.size() < 1) {
			result = "failure";
			tip = "您没有拍卖物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// liuyi 2007-01-09 程序优化 end
		int totalCount = userAuctionList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / USR_AUCTION_NUMBER_PER_PAGE;
		if (totalCount % USR_AUCTION_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "myAuction.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * USR_AUCTION_NUMBER_PER_PAGE;
		int end = USR_AUCTION_NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List userAuctionList1 = userAuctionList.subList(startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userAuctionList", userAuctionList1);
		result = "scuuess";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 我参与竞价的页面 datetime:2006-12-18 下午05:39:33
	 * @param request
	 */
	public void myBuy(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取我参与的竞价排名!
		String sql = "SELECT distinct(a.auction_id) as c_id , b.mark from jc_auction_history a "
				+ "left join jc_auction b on a.auction_id=b.id where a.user_id="
				+ loginUser.getId() + " order by a.id desc";
		List userAuctionHistoryList = (List) SqlUtil.getIntList(sql,
				Constants.DBShortName);
		if (userAuctionHistoryList == null) {
			result = "failure";
			tip = "您没有拍卖物品";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		int totalCount = userAuctionHistoryList.size();
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		int totalPageCount = totalCount / USR_AUCTION_NUMBER_PER_PAGE;
		if (totalCount % USR_AUCTION_NUMBER_PER_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		String prefixUrl = "myBuy.jsp";
		// 取得要显示的消息列表
		int start = pageIndex * USR_AUCTION_NUMBER_PER_PAGE;
		int end = USR_AUCTION_NUMBER_PER_PAGE;
		int startIndex = Math.min(start, totalCount);
		int endIndex = Math.min(start + end, totalCount);
		List userAuctionHistoryList1 = userAuctionHistoryList.subList(
				startIndex, endIndex);
		request.setAttribute("totalCount", new Integer(totalCount));
		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("userAuctionHistoryList", userAuctionHistoryList1);
		result = "scuuess";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : datetime:2006-12-18 下午07:16:05
	 * @param request
	 */
	public void fromCardIndex(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取等待拍卖物品的ID
		int product = StringUtil.toInt(request.getParameter("productId"));
		if (product < 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取虚拟物品介绍记录
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(product);
		if (dummyProduct == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		request.setAttribute("dummyProduct", dummyProduct);
		result = "success";
		request.setAttribute("result", result);
	}

	/**
	 * 
	 * @author macq explain : datetime:2006-12-18 下午07:21:37
	 * @param request
	 */
	public void fromCardResult(HttpServletRequest request) {
		String result = null;
		String tip = null;
		// 获取等待拍卖物品的ID
		int productId = StringUtil.toInt(request.getParameter("productId"));
		if (productId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取物品记录信息
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(productId);
		if (dummyProduct == null) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		// 获取一口价跟起价的值
		int startPice = StringUtil.toInt(request.getParameter("startPice"));
		int bitePrice = StringUtil.toInt(request.getParameter("bitePrice"));
		if (startPice < 0 || bitePrice < 0) {
			result = "parameterError";
			tip = "请确认输入金额!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("productId", productId + "");
			return;
		}
		// 判断输入值是否大于9亿
		if (startPice > MAX_AUCTION_PRICE || bitePrice > MAX_AUCTION_PRICE) {
			result = "parameterError";
			tip = "最大起价和最大一口价限制为" + MAX_AUCTION_PRICE + "乐币!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("productId", productId + "");
			return;
		}
		// 判断起价大于等于一口价
		if (startPice >= bitePrice) {
			result = "parameterError";
			tip = "起价必须小于一口价!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			request.setAttribute("productId", productId + "");
			return;
		}
		// 防止刷新
		if (session.getAttribute("randomuserbag") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("randomuserbag");
		// 更新用户虚拟物品状态
		// 添加拍卖记录
		AuctionBean auction = new AuctionBean();
		auction.setLeftUserId(loginUser.getId());
		auction.setRightUserId(0);
		auction.setProductId(dummyProduct.getId());
		auction.setDummyId(dummyProduct.getDummyId());
		auction.setTime(dummyProduct.getTime());
		auction.setStartPrice(startPice);
		auction.setBitePrice(bitePrice);
		auction.setCurrentPrice(startPice);
		auction.setMark(0);
		AuctionCacheUtil.addAction(auction);
		result = "success";
		request.setAttribute("result", result);
		return;
	}

	/**
	 * 
	 * @author macq explain : 数字分页 datetime:2006-12-15 下午04:34:37
	 * @param totalPageCount
	 * @param currentPageIndex
	 * @param prefixUrl
	 * @param addAnd
	 * @param separator
	 * @param pageIndex
	 * @param response
	 * @return String
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
		int startIndex = (currentPageIndex / NUMBER_PER_PAGE) * NUMBER_PER_PAGE;
		int endIndex = (currentPageIndex / NUMBER_PER_PAGE + 1)
				* NUMBER_PER_PAGE - 1;
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
	 * 
	 * @author macq explain : datetime:2006-12-18 下午05:59:16
	 * @param dummyProductId
	 * @return
	 */
	public DummyProductBean getDummyProduct(int dummyProductId) {
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(dummyProductId);
		return dummyProduct;
	}

	/**
	 * 
	 * @author macq explain : datetime:2006-12-18 下午05:59:11
	 * @param auctionId
	 * @return
	 */
	public AuctionBean getAuction(int auctionId) {
		AuctionBean auction = AuctionCacheUtil.getAuctionCacheById(auctionId);
		return auction;
	}

	/**
	 * 
	 * @author macq explain : datetime:2006-12-18 下午05:59:09
	 * @param auctionHistoryId
	 * @return
	 */
	public AuctionHistoryBean getAuctionHistory(int auctionHistoryId) {
		AuctionHistoryBean auctionHistory = AuctionCacheUtil
				.getAuctionHistoryCacheById(auctionHistoryId);
		return auctionHistory;
	}
	
	public static long MAX_TAX = 100000000l;
	// 扣除拍卖行手续费
	public static long calcSellMoney(long price) {
		long tax = Math.round(price * 0.05);
		if(tax >= MAX_TAX)	// 手续费最高不超过1亿
			return price - MAX_TAX;
		return price - tax;
	}

	/**
	 * 
	 * @author macq
	 * @explain : 添加大厅拍卖物品动态log
	 * @datetime:2007-7-3 下午01:22:16
	 * @param content
	 * @return
	 */
	public void addLog(String content) {
		synchronized (log) {
			// 判断log是否大于10条
			if (log.size() > 10) {
				log.remove(0);
			}
			// 添加log
			log.add(content);
		}
	}

	/**
	 * @param log
	 */
	public String toString(List log) {
		StringBuilder result = new StringBuilder("");
		int listSize = log.size();
		int startIndex = listSize - 1;
		int endIndex = 0;
		if (listSize > logSize) {
			endIndex = listSize - logSize;
		}
		int j=1;
		for (int i = startIndex; i >= endIndex; i--) {
			result.append(j+".");
			result.append(StringUtil.toWml((String) log.get(i)));
			result.append("<br/>");
			j++;
		}
		return result.toString();
	}
}
