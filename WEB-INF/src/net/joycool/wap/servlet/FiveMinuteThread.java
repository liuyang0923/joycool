package net.joycool.wap.servlet;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import net.joycool.wap.action.user.UserBagAction;
import net.joycool.wap.bean.stock.StockBean;
import net.joycool.wap.bean.stock.StockGrailBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.spec.buyfriends.ThreadBuyFriend;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.db.DbOperation;
import net.joycool.wap.action.pet.StakeAction;

/**
 * 
 * @author macq
 * 
 */
public class FiveMinuteThread extends Thread {
	// liuyi 2006-12-28 股票日志 start
	public static int range = 50;
    
	public static int middle =1000 ;
	// 2007.4.9 LIq 股价波动
	public static final int MAX_SOLD = 1000000000;
	
	static IUserService userService = ServiceFactory.createUserService();

	public static HashMap TimeMap = null;

	public boolean flag =false;
	public static HashMap getTimes() {
		if (TimeMap != null) {
			return TimeMap;
		}
		TimeMap = new HashMap();
		TimeMap.put(new Integer(0), new Integer(48000));
		TimeMap.put(new Integer(1), new Integer(60000));
		TimeMap.put(new Integer(2), new Integer(66000));
		TimeMap.put(new Integer(3), new Integer(78000));
		TimeMap.put(new Integer(4), new Integer(84000));
		TimeMap.put(new Integer(5), new Integer(90000));
		TimeMap.put(new Integer(6), new Integer(96000));
		TimeMap.put(new Integer(7), new Integer(111000));
		TimeMap.put(new Integer(8), new Integer(120000));
		TimeMap.put(new Integer(9), new Integer(126000));
		TimeMap.put(new Integer(10), new Integer(132000));
		TimeMap.put(new Integer(11), new Integer(138000));
		return TimeMap;
	}

	public void run() {
		DbOperation dbOp = null;
		Integer timeObject = null;
		// liuyi 2007-01-19 线程时间修改 start
		int count = 0;
		int cu = 0;
		while (true) {
			count++;
			try {
				Thread.sleep(1000 * 60 * 2);
			} catch (InterruptedException e) {
				return;
			}
			LogUtil.logTime("FiveMinuteThread*****start");
			try {
				int nextValues;
				int nextObValue;
				String time = DateUtil.formatDate(new Date());
				Calendar cal = Calendar.getInstance();
				int currentHour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				int nowTime = currentHour * 60 * 100 + minute * 100;
/*				Vector stockList = stockService.getStockList("price>10");
				float grail = 0;
				dbOp = new DbOperation();
				dbOp.init();
				dbOp.startTransaction();
				if (stockList != null && stockList.size() > 0) {
					for (int i = 0; i < stockList.size(); i++) {
						StockBean stock = (StockBean) stockList.get(i);
						if (stock != null) {
							float stockPrice = (float) stock.getPrice();
							// liuyi 2007-01-19 股市调整 start
							long soldIn = stock.getSoldIn();
							long soldOut = stock.getSoldOut();
							int in = (int) (soldIn / MAX_SOLD);
							int out = (int) (soldOut / MAX_SOLD);
							int remainIn = (int) (soldIn % MAX_SOLD);
							int remainOut = (int) (soldOut % MAX_SOLD);
							LogUtil.logDebug("debug:" + soldIn + ":" + soldOut
									+ ":" + in + ":" + out + ":"
									+ stock.getId());
							if (in > 0 && out > 0) {
								int rate = in - out;

								stockPrice = stockPrice * (100 + rate) / 100;
								LogUtil.logDebug("stock:" + stock.getId() + ":"
										+ stockPrice + ":" + stock.getPrice()
										+ ":" + in + ":" + out + ":" + rate);
								dbOp
										.executeUpdate("UPDATE jc_stock SET sold_in="
												+ remainIn
												+ ",sold_out="
												+ remainOut
												+ ",price="
												+ stockPrice
												+ " where id= "
												+ stock.getId());

							} else if (in > 0) {
								stockPrice = stockPrice * (100 + in) / 100;
								LogUtil.logDebug("stock:" + stock.getId() + ":"
										+ stockPrice + ":" + stock.getPrice()
										+ ":" + in + ":" + out + ":0");
								dbOp
										.executeUpdate("UPDATE jc_stock SET sold_in="
												+ remainIn
												+ ", price="
												+ stockPrice
												+ ",sold_in=0 where id= "
												+ stock.getId());

							} else if (out > 0) {
								stockPrice = stockPrice * (100 - out) / 100;
								LogUtil.logDebug("stock:" + stock.getId() + ":"
										+ stockPrice + ":" + stock.getPrice()
										+ ":" + in + ":" + out + ":0");
								dbOp
										.executeUpdate("UPDATE jc_stock SET sold_out="
												+ remainOut
												+ ",price="
												+ stockPrice
												+ ",sold_out=0 where id= "
												+ stock.getId());
							}
							// liuyi 2007-01-19 股市调整 end
							grail = grail + stockPrice;
						}
					}
					OsCacheUtil.flushGroup(OsCacheUtil.STOCK_TIME_GROUP);
				}
				float grailPrice = grail;
				StockGrailBean stockGrail = stockService
						.getStockGrail("1=1 order by create_datetime desc limit 0,1");
				if (stockGrail != null) {

					// stockService.updateStockGrail("now_price=" + grailPrice,
					// "id=" + stockGrail.getId());
					dbOp.executeUpdate("UPDATE jc_stock_grail SET now_price="
							+ grailPrice + " where id=" + stockGrail.getId());
					// liuyi 2006-12-28 股票日志 start
					LogUtil.logDebug("stockgrail:" + stockGrail.getId() + ":"
							+ grailPrice);
					// liuyi 2006-12-28 股票日志 end
				} else {
					// stockGrail = new StockGrailBean();
					// stockGrail.setNowPrice(grailPrice);
					// stockGrail.setTodayPrice(grailPrice);
					// stockService.addStockGrail(stockGrail);
					dbOp
							.executeUpdate("INSERT INTO jc_stock_grail(now_price,today_price,yesterday_price,create_datetime) VALUES("
									+ grailPrice
									+ ","
									+ grailPrice
									+ ",0,now())");
				}
				dbOp.commitTransaction();

				// liuyi 2006-12-28 股票日志 start
				OsCacheUtil.flushGroup(OsCacheUtil.GRAIL_TIME_GROUP);
				// liuyi 2006-12-28 股票日志 end
				dbOp.release();
*/
				// macq_2006-12-15_清空被别人使用臭鸡蛋不能发言的用户列表_start
				if (count >= 3) {
					count = 0;
					UserBagAction.clearStopSpeakMap();
				}
				//宠物比赛自动开始下注
				if (timeObject == null) {
					for (int i = 0; i < getTimes().size(); i++) {
						
						timeObject = (Integer) getTimes().get(new Integer(i));
						int timeValues = timeObject.intValue();
						Integer nextObject = (Integer) getTimes().get(
								new Integer(i + 1));
						if (nextObject == null) {
						 nextValues = 144000;
						}else{
						 nextValues = nextObject.intValue();
						}
						if (nowTime >= timeValues && nowTime <= nextValues) {
							StakeAction.stakeMatch();
							cu = i;
							cu++;
							
						}
						if (cu >= getTimes().size()) {
							cu = 0;
						}
					}
					
					timeObject = (Integer) getTimes().get(new Integer(cu));
				} else {
					int timeValue = timeObject.intValue();
					
					timeObject = (Integer) getTimes().get(new Integer(cu));
					Integer nextOb = (Integer) getTimes().get(
							new Integer(cu + 1));
					if (nextOb == null) {
						 nextObValue =144000;
					}else{

					 nextObValue = nextOb.intValue();
					}
					if (nowTime >= timeValue && nowTime <= nextObValue) {
						StakeAction.stakeMatch();
						cu++;
						flag = false;
					}
					//计数器清零
					if (cu >= getTimes().size()) {
						cu = 0;
					}
					timeObject = (Integer) getTimes().get(new Integer(cu));
					//宠物比赛在下一场下注前开跑
					if(timeObject.intValue()-nowTime>0&&timeObject.intValue()-nowTime<=middle && flag==false)
					{
						StakeAction.stakeMatchStart();
						flag =true;
					}
					//宠物比赛在24点准时开始最后一场
					if(nowTime>=143800&&nowTime<=144000)
					{
						StakeAction.stakeMatchStart();
					}
				}

				// macq_2006-12-15_清空被别人使用臭鸡蛋不能发言的用户列表_end
			} catch (Exception e) {
				e.printStackTrace();

			}

			try {
				ThreadBuyFriend.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("FiveMinuteThread*****end");
		}
		// liuyi 2007-01-19 线程时间修改 end
	}
}
