package net.joycool.wap.action.auction;

import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserHonorBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class PropAction extends CustomAction{
	UserBean loginUser = null;

	static IUserService userService = ServiceFactory.createUserService();;

	static IDummyService dummyService = ServiceFactory.createDummyService();;

	private UserBagBean userBagBean = null;
	//是否出现在道具商店标志
	final static int DUMMY_MARK = 1;
	//特殊道具-类型4
	final static int GAME_DUMMY = 4;
	//整人特殊道具
	final static int ZHENG_DUMMY = 2;
	//缓存key
	final static String GAME_LIST_KEY = DUMMY_MARK + "_" + GAME_DUMMY;
	//缓存key
	final static String ZHENG_LIST_KEY = DUMMY_MARK + "_" + ZHENG_DUMMY;
	final static String COMPOSE_LIST_KEY = DUMMY_MARK + "_" + 5;
	//同步锁
	static byte[] lock = new byte[0];

	public PropAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		if(loginUser != null){
			loginUser.setUs(UserInfoUtil.getUserStatus(loginUser.getId()));
		}
	}

	public IUserService getUserService() {
		return userService;
	}

	public IDummyService getDummyService() {
		return dummyService;
	}

	/**
	 * 首页
	 * @param request
	 */
	public void propShop(HttpServletRequest request) {
		String result = null;
		String tip = null;
		Vector product = new Vector();
		Vector productList = new Vector();
		int id = 0;
		String condition = "";
		int type = getParameterInt("type");
		switch(type) {
		case 0:
			condition = "brush>0 and rank=0 and mark=1";
			break;
		case 1:
			condition = "brush=0 and rank=0 and mark=1";
			break;
		case 2:
			condition = "rank>0 and mark=1";
			break;
		}
		condition += " and dummy_id=";
		// 游戏道具
		Vector gameProduct = getDummyService().getDummyProductLists(condition + GAME_DUMMY);
		// 整人道具
		Vector zhengProduct = getDummyService().getDummyProductLists(condition + ZHENG_DUMMY);
		
		Vector composeProduct = getDummyService().getDummyProductLists(condition + 5);
		request.setAttribute("gameProduct", gameProduct);
		request.setAttribute("zhengProduct", zhengProduct);
		request.setAttribute("composeProduct", composeProduct);
		result = "success";
		request.setAttribute("result", result);

	}

	public void propShow(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int dummyProductId = StringUtil.toInt(request.getParameter("dummyId"));
		if (dummyProductId <= 0) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(
				dummyProductId);
		String name = dummyProduct.getName();
		if (dummyProduct == null || !dummyProduct.isFlag(0)) {
			result = "failure";
			tip = "参数错误";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		//判断商品过期
		/*Timestamp times = dummyProduct.getStartTime();
		long startTime = times.getTime();
		int brush =dummyProduct.getBrush();
		long brushs = brush * 60 * 1000;
		long nowTime = System.currentTimeMillis();
		long countDown = nowTime - startTime;
		if(countDown<brushs)
		{
			result = "failure";
			tip = "该商品不能购买";
			request.setAttribute("result", result);
			request.setAttribute("tip",tip);
			return;
		}*/

		request.setAttribute("dummyProduct", dummyProduct);
		result = "success";
		request.setAttribute("result", result);
		session.setAttribute("auctionBuyCheck", "ture");
		return;
	}

	public void propBuy(HttpServletRequest request) {
		String result = null;
		String tip = null;
		int dummyId =0;
	
		// 防止刷新
		if (session.getAttribute("auctionBuyCheck") == null) {
			result = "refrush";
			request.setAttribute("result", result);
			return;
		}
		session.removeAttribute("auctionBuyCheck");

		int dummyProductId = StringUtil.toInt(request.getParameter("dummyId"));
		
		DummyProductBean dummyProduct = getDummyService().getDummyProducts(dummyProductId);
		//判断mark不等于0的商品id
		UserHonorBean honor = loginUser.getUserHonor();
		if(!dummyProduct.isFlag(0) || honor.getRank() < dummyProduct.getRank())
		{
			result = "failure";
			tip = "对不起,您不能购买该商品!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		float discount = honor.getDiscount();
		int buyPrice = (int) (dummyProduct.getBuyPrice() * discount);
		
		int dummyType = dummyProduct.getDummyId();
		String name = dummyProduct.getName();
		// 获取用户当前乐币
		UserStatusBean userStatus = loginUser.getUs();
		int gamePoint = userStatus.getGamePoint();
		if (buyPrice > gamePoint) {
			result = "failure";
			tip = "对不起,您身上的乐币不够参与购买!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}else if (!UserBagCacheUtil.checkUserBagCount(loginUser.getId())) {// 判断用户行囊
			result = "failure";
			tip = "对不起,您的行囊已满!";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}else {
			synchronized (lock) {// 同步判断
				Timestamp tamp = dummyProduct.getStartTime();
				long longTamp = tamp.getTime();
				int brush = dummyProduct.getBrush();
				long nowTime = System.currentTimeMillis();
				// 时间不符合
				if (nowTime - longTamp < brush * 60 * 1000) {
					result = "failure";
					tip = "这个道具已出售！";
					request.setAttribute("result", result);
					request.setAttribute("tip", tip);
					return;
				}
				String condition = "id = " + dummyProductId;
				String set = "start_time=now()";
				UserBagCacheUtil.addUserBagCache(loginUser.getId(), dummyProductId);
				// 更新道具时间
				getDummyService().updateDummyProduct(set, condition);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(loginUser.getId(),-buyPrice,
						UserCashAction.OTHERS,"用户购买道具商店物品花费"+buyPrice);
				tip = name + "购买成功，已存入行囊";
				dummyProduct.setStartTime(new java.sql.Timestamp(System.currentTimeMillis()));

				request.setAttribute("tip", tip);
				result = "success";
				request.setAttribute("result", result);
			}
		}
	}
	
	/**
	 * 判断是否出现 返回值为0代表出现，大于0代表冷却
	 * @param bean
	 * @return
	 */
	public long checkProductView(DummyProductBean bean){
		Timestamp times = bean.getStartTime();
		long startTime = times.getTime();
		int brush =bean.getBrush();
		long brushs = brush * 60 * 1000;
		long nowTime = System.currentTimeMillis();
		long countDown = nowTime - startTime;
		long countDowns = countDown / 60 / 1000;
		long middle = brush-countDowns;
		if(middle>0){
			return middle;
		}
		return 0;
	}
}
