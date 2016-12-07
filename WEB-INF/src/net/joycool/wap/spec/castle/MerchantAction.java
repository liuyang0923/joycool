package net.joycool.wap.spec.castle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.util.SqlUtil;

public class MerchantAction extends CastleBaseAction {
	static 	CacheService cacheService = CacheService.getInstance();
	public MerchantAction() {
		super();
	}

	public MerchantAction(HttpServletRequest request,
			HttpServletResponse response) {
		super(request, response);
	}

	public MerchantAction(HttpServletRequest request) {
		super(request);
	}

	
	public void send() {
		if(this.hasParam("a")) {
			int marketGrade = userResBean.getBuildingGrade(ResNeed.MARKET_BUILD);
			if(marketGrade == 0) {
				request.setAttribute("msg", "你还未建造市场");
				return;
			}
			
			int x = getParameterInt("x");
			int y = getParameterInt("y");
			int castleId = CastleUtil.getMapCastleId(x, y);
			if(castleId == 0 || castleId == castle.getId()) {
				request.setAttribute("msg", "不能派往自己");
				return;
			}
			
			int wood = this.getParameterInt2("w");
			int fe = this.getParameterInt2("f");
			int grain = this.getParameterInt2("g");
			int stone = this.getParameterInt2("s");
			if(wood < 0 || fe < 0 || grain < 0 || stone < 0) {
				request.setAttribute("msg", "输入资源不合法");
				return;
			}
			long now = System.currentTimeMillis();
			int count;
			synchronized(this.userResBean) {
				if(wood > 0 && wood > this.userResBean.getWood(now)
						|| fe > 0 && fe > this.userResBean.getFe(now)
						|| grain > 0 && grain > this.userResBean.getGrain(now)
						|| stone > 0 && stone > this.userResBean.getStone(now)) {
					request.setAttribute("msg", "资源不足");
					return;
				}
				int sum = wood + fe + grain + stone;
				
				if(sum == 0) {
					request.setAttribute("msg", "资源不能为0");
					return;
				}
				count = getMerchantCount(sum);
				int merchantCount = marketGrade - userResBean.getMerchant();
				if(count > merchantCount) {
					request.setAttribute("msg", "商人不够");
					return;
				}
			
			
				userResBean.addMerchant(count);
				castleService.updateUserMerchant(this.castle.getId(), userResBean.getMerchant());
			

				//更新资源
				CastleUtil.decreaseUserRes(this.userResBean, wood, fe, grain, stone);
			}
			//加入线程
			MerchantBean merchantBean = new MerchantBean(this.castle.getId(),castleId, wood, fe, grain, stone,count,this.castle.getX(),this.castle.getY(),x,y, ResNeed.getMerchantSpeed(castle.getRace()));
			cacheService.addCacheMerchant(merchantBean);
			request.setAttribute("msg", "资源已经送出");
		}
	}
	
	// 交易相关操作
	public void trade() {
		if(hasParam("supply")) {	// 添加交易
			TradeBean trade = new TradeBean();
			trade.setCid(castle.getId());
			int hour = getParameterInt("maxHour");
			if(hour == 0)
				hour = 1000;	// 无限制
			trade.setDistance(hour * hour * 256);
			trade.setSupply(getParameterInt("supply"));
			trade.setNeed(getParameterInt("need"));
			trade.setSupplyRes(getParameterInt("supplyRes"));
			trade.setNeedRes(getParameterInt("needRes"));
			trade.setX(castle.getX());
			trade.setY(castle.getY());
			trade.setSupplyMerchant(getMerchantCount(trade.getSupplyRes()));
			if(trade.getNeed() <= 0 || trade.getNeed() > 4)
				trade.setNeed(1);
			if(trade.getSupply() <= 0 || trade.getSupply() > 4)
				trade.setSupply(2);
			if(trade.getNeedRes() <= 0 || trade.getSupplyRes() <= 0
					|| trade.getNeedRes() > 100000 || trade.getSupplyRes() > 100000 || trade.getNeed() == trade.getSupply()) {
				tip("tip", "输入不正确");
				return;
			}
			if(trade.getNeedRes() + trade.getNeedRes() < trade.getSupplyRes()
					|| trade.getSupplyRes() + trade.getSupplyRes() < trade.getNeedRes()) {
				tip("tip", "最高比例为1:2");
				return;
			}
			if(userResBean.getBuildingGrade(ResNeed.MARKET_BUILD) - userResBean.getMerchant() < trade.getSupplyMerchant()) {
				tip("tip", "商人不足");
				return;
			}
			if(!userResBean.decreaseRes(trade.getSupply(), trade.getSupplyRes())) {
				tip("tip", "资源不足");
				return;
			}
			userResBean.addMerchant(trade.getSupplyMerchant());
			castleService.updateUserMerchant(castle.getId(), userResBean.getMerchant());

			castleService.addTrade(trade);
			tip("tip", "卖出交易已添加");
		} else if(hasParam("del")) {	// 删除交易
			int del = getParameterInt("del");
			synchronized(tradeLock) {
				TradeBean trade = castleService.getTradeBean("id=" + del);
				if(trade == null || trade.getCid() != castle.getId()) {
					tip("tip", "非法操作");
					return;
				}
				userResBean.addMerchant(-trade.getSupplyMerchant());
				castleService.updateUserMerchant(castle.getId(), userResBean.getMerchant());
				userResBean.increaseRes(trade.getSupply(), trade.getSupplyRes());
				deleteTrade(del);
			}
			tip("tip", "交易被删除");
		} else if(hasParam("id")) {		// 接受交易
			int id = getParameterInt("id");
			synchronized(tradeLock) {
				TradeBean trade = castleService.getTradeBean("id=" + id);
				if(trade == null || trade.getCid() == castle.getId()) {
					tip("tip", "交易无法执行");
					return;
				}
				trade.setNeedMerchant(getMerchantCount(trade.getNeedRes()));
				if(userResBean.getBuildingGrade(ResNeed.MARKET_BUILD) - userResBean.getMerchant() < trade.getNeedMerchant()) {
					tip("tip", "商人不足");
					return;
				}
				if(!userResBean.decreaseRes(trade.getNeed(), trade.getNeedRes())) {
					tip("tip", "资源不足");
					return;
				}
				
				userResBean.addMerchant(trade.getNeedMerchant());
				castleService.updateUserMerchant(castle.getId(), userResBean.getMerchant());
				acceptTrade(trade);
				deleteTrade(id);
			}
			tip("tip", "资源交易被接受");
		} else {
			tip("tip", "未执行操作");
		}
	}
	public static int[] tradeLock = new int[0];
	public static void deleteTrade(int id) {
		SqlUtil.executeUpdate("delete from castle_trade where id=" + id, 5);
	}
	// 交易开始，双方互相运送资源
	public void acceptTrade(TradeBean trade) {
		CastleBean toCastle = CastleUtil.getCastleById(trade.getCid());
		if(toCastle == null)
			return;
		MerchantBean merchantBean = new MerchantBean(castle.getId(), trade.getCid(), trade.getNeed(),
				trade.getNeedRes(), trade.getNeedMerchant(), castle.getX(), castle.getY(), trade.getX(), trade.getY(), ResNeed.getMerchantSpeed(castle.getRace()));
		cacheService.addCacheMerchant(merchantBean);
		
		merchantBean = new MerchantBean(trade.getCid(), castle.getId(), trade.getSupply(),
				trade.getSupplyRes(), trade.getSupplyMerchant(), trade.getX(), trade.getY(), castle.getX(), castle.getY(), ResNeed.getMerchantSpeed(toCastle.getRace()));
		cacheService.addCacheMerchant(merchantBean);
	}
	// 取消所有挂出的卖出订单
	public static void cancel(UserResBean res) {
		synchronized(tradeLock) {
			List mlist = castleService.getTradeList("cid=" + res.getId());
			if(mlist.size() == 0)
				return;
			for(int i = 0;i < mlist.size();i++) {
				TradeBean trade = (TradeBean)mlist.get(i);
				res.addMerchant(-trade.getSupplyMerchant());
				// 这里无需保存到数据库，本函数的作用是重新计算商人回来后的粮食产量，之后是被抢夺，会被再次保存
				res.increaseResNS(trade.getSupply(), trade.getSupplyRes());
				
				deleteTrade(trade.getId());
			}
			castleService.updateUserMerchant(res.getId(), res.getMerchant());
		}
	}
}
