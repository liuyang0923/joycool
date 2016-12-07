/*
 * Created on 2006-1-10
 *
 */
package jc.guest.wgame;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import jc.guest.GuestHallAction;
import jc.guest.GuestUserInfo;

import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.TigerBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 * 
 */
public class TigerAction extends WGameAction {
	
	private final int XXX=3;//星星星机率数,如要修改不能为0
	
	private final int XXT=7;//星星桃机率数,如要修改不能为0
	
	private final int XXG=10;//星星瓜机率数,如要修改不能为0
	
	private final int XX7=20;//星星7机率数,如要修改不能为0
	
	private final int XXBAA=200;//星星BAA机率数,如要修改不能为0
	
	private final int ONE_X=300;//一个星机率数,如要修改不能为0
	
	private final int NO_X=460;//没有星机率数,如要修改不能为0
	
	public String tigerSessionName = "tiger";

	public String winsSessionName = "tigerWins";

	public int NUMBER_PAGE = 10;

	//zhul_2006-07-28 修改老虎机概率，将概率及对应的结果映射到内存中，加属性及方法 start
	private static TreeMap tigerMap;

	public TreeMap getTigerMap() {
		if (tigerMap != null) {
			return tigerMap;
		}

		tigerMap = new TreeMap();
		int rate=0;		//初始化概率为0
		rate+=XXX;		//星星星概率范围
		tigerMap.put(new Integer(rate), new int[] { 80, 125 });
		rate+=XXT;		//星星桃概率范围
		tigerMap.put(new Integer(rate), new int[] { 40, 100 });
		rate+=XXG;		//星星瓜概率范围
		tigerMap.put(new Integer(rate), new int[] { 20, 75 });
		rate+=XX7;		//星星7概率范围
		tigerMap.put(new Integer(rate), new int[] { 10, 50 });
		rate+=XXBAA;	//星星BAA概率范围
		tigerMap.put(new Integer(rate), new int[] { 5, 25 });
		rate+=ONE_X;	//一个星概率范围
		tigerMap.put(new Integer(rate), new int[] { 1, 1 });
		rate+=NO_X;		//没有星概率范围
		tigerMap.put(new Integer(rate), new int[] { 0, 0 });
		tigerMap.put(new Integer(Constants.RANDOM_BASE),new Integer(rate));
		return tigerMap;
	}
	//zhul_2006-07-28 修改老虎机概率，将概率及对应的结果映射到内存中，加属性及方法 end
	/**
	 * @param request
	 */
	public void deal1(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
			return;
		}

		String tip = null;
		String result = "success";
		// 取得参数
		int wager = StringUtil.toInt(request.getParameter("wager"));
		if (wager <= 0) {
			tip = "赌注不能小于等于零!";
			result = "failure";
		} else if (wager > guestUser.getMoney()) {
			tip = "您的游币不够了!";
			result = "failure";
		} else if (wager > 1000) {
			tip = "乐酷提示:老虎机您最多能下注1000个游币!";
			result = "failure";
		}

		// 有错
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		} else {
			// 选择美女
			if (getSessionObject(request, winsSessionName) == null) {
				int girlId = StringUtil.toInt(request.getParameter("girlId"));
				if (girlId <= 0) {
					girlId = 1;
				}
				WGameBean wins = new WGameBean();
				wins.setGirlId(girlId);
				setSessionObject(request, winsSessionName, wins);
			}

			TigerBean tiger = new TigerBean();
			tiger.setWager(wager);
			setSessionObject(request, tigerSessionName, tiger);
			request.setAttribute("result", result);
			return;
		}
	}

	/**
	 * @param request
	 */
	public void deal2(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
			return;
		}
		TigerBean tiger = (TigerBean) getSessionObject(request,
				tigerSessionName);
		if (tiger == null) {
			return;
		}
		request.getSession().removeAttribute(tigerSessionName);

		// 得到老虎机结果
		int[] results = getResult();
		int win = results[3];
		tiger.setResults(results);
		tiger.setResult(win);

		String result = null;

		// 赢了
		if (win > 0) {
			WGameBean wgame = (WGameBean) getSessionObject(request,
					winsSessionName);
			if (wgame != null) {
				wgame.setWins(wgame.getWins() + 1);
			}

			// 加钱
			GuestHallAction.updateMoney(guestUser.getId(), tiger.getWager() * win);
			HistoryBean history = new HistoryBean();
			history.setUserId(guestUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_TIGER);
			history.setWinCount(1);
			history.setMoney(tiger.getWager());
			updateHistory(history);

			result = "win";
		} else {
			// 扣钱
			GuestHallAction.updateMoney(guestUser.getId(), 0-tiger.getWager());
			HistoryBean history = new HistoryBean();
			history.setUserId(guestUser.getId());
			history.setGameType(WGameBean.GT_DC);
			history.setGameId(WGameBean.DC_TIGER);
			history.setLoseCount(1);
			history.setMoney(-tiger.getWager());
			updateHistory(history);

			result = "lose";
		}

		request.setAttribute("result", result);
		request.setAttribute("tiger", tiger);
	}

	public int[] getResults() {
		int[] results = new int[3];
		results[0] = RandomUtil.nextInt(5) + 1;
		results[1] = RandomUtil.nextInt(5) + 1;
		results[2] = RandomUtil.nextInt(5) + 1;
		return results;
	}

	public int getResult(int[] results) {
		if (results == null || results.length != 3) {
			return 0;
		}

		int c = results[0] * results[1] * results[2];
		if (c == 125) {
			return 80;
		}
		if (c == 100) {
			return 40;
		}
		if (c == 75) {
			return 20;
		}
		if (c == 50) {
			return 10;
		}
		if (c == 25) {
			return 5;
		}
		if (c % 5 == 0) {
			return 1;
		}

		return 0;
	}

	// zhul_2006-07-27 修改老虎机的概率问题，新增方法。start
	/**
	 * zhul_2006-07-27 得到老虎机的运行结果 返回数组int[0]-int[2]为结果，int[3]为赔率
	 * 
	 * @return
	 */
	public int[] getResult() {

		int[] result = null;
		// 得到老虎机概率map
		TreeMap tigerMap = this.getTigerMap();
		//概率基数
		Integer base=(Integer)tigerMap.get(new Integer(Constants.RANDOM_BASE));
		// 得到一个随机数
		int random = RandomUtil.nextInt(base.intValue()) + 1;
		// 迭代tigerMap中所有概率
		Set keys = tigerMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			
			// 找到与随机结果相符的值返回
			if (key.intValue() >= random) {
				int[] value = (int[]) tigerMap.get(key);
				// 根据老虎机结果，(如2个5 1 个4)重新对结果随机排列
				int[] res;
				switch (value[1]) {
				case 125:
					res = riffle(new int[] { 5, 5, 5 });	//星星星
					break;
				case 100:
					res = riffle(new int[] { 5, 5, 4 });	//星星桃
					break;
				case 75:
					res = riffle(new int[] { 5, 5, 3 });	//星星瓜
					break;
				case 50:
					res = riffle(new int[] { 5, 5, 2 });	//星星7
					break;
				case 25:
					res = riffle(new int[] { 5, 5, 1 });	//星星BAA
					break;
				case 1:
					res = getTigerCard(true);
					break;
				default:
					res = getTigerCard(false);
				}
				// 将得到的随机结果写到一个新的有4个元素的数组，第4 个元素为赔率
				result = new int[4];
				for (int i = 0; i < res.length; i++) {
					result[i] = res[i];
				}
				result[3] = value[0];
				// 返回老虎机的结果
				return result;
			}
		}
		return result;
	}

	/**
	 * 
	 * 得到一个随机排列的新数组
	 * 
	 * @param array
	 *            数组
	 * @return
	 */
	public static int[] riffle(int[] array) {
		Random random = new Random();
		int len = array.length;
		for (int i = len; i > 0; i--) {
			int a = random.nextInt(i);
			int temp = array[a];
			array[a] = array[i - 1];
			array[i - 1] = temp;
		}
		return array;
	}

	/**
	 * 得到一个随机排列的新数组
	 * 
	 * @param bool
	 *            有5 bool为true 没有5 bool为false
	 * @return
	 */
	public int[] getTigerCard(boolean bool) {
		Random random = new Random();
		// bool为true 是有1个5的情况，得到一个随机排列的含有1个5的结果
		if (bool) {
			int[] k = new int[] { 5, random.nextInt(4) + 1,
					random.nextInt(4) + 1 };
			return riffle(k);
		} else// bool为false 是没有5的情况，得到一个随机排列的没有5的结果
		{
			int[] k = new int[] { random.nextInt(4) + 1, random.nextInt(4) + 1,
					random.nextInt(4) + 1 };
			return k;
		}
	}

	// zhul_2006-07-27 修改老虎机的概率问题，end
	/**
	 * @param request
	 */
	public void history(HttpServletRequest request,GuestUserInfo guestUser) {
		if (guestUser == null) {
			return;
		}

		HistoryBean history = getHistory(guestUser.getId(), WGameBean.GT_DC,
				WGameBean.DC_TIGER);
		request.setAttribute("history", history);
	}
}
