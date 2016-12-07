package net.joycool.wap.action.job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.HandbookingerBean;
import net.joycool.wap.bean.job.HandbookingerRecordBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class HandbookingerAction extends CustomAction{
	UserBean loginUser = null;

	private TreeMap handbookingerMap = null;

	private IJobService jobService = null;

	public HandbookingerAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}

	public IJobService getJobService() {
		if (jobService == null) {
			jobService = ServiceFactory.createJobService();
		}
		return jobService;
	}

	//liuyi 2006-12-01 程序优化 start
	public TreeMap getHandbookingerMap() {

		if (handbookingerMap != null) {
			return handbookingerMap;
		}
		// 获取机率对应表
		handbookingerMap = new TreeMap();

		Vector handbookingerList = getHandbookingerList();
		int size = handbookingerList.size();
		int base = 0;
		for (int i = 0; i < size; i++) {
			HandbookingerBean handbookinger = (HandbookingerBean) handbookingerList
					.get(i);
			base += handbookinger.getSuccess();
			if (handbookinger.getSuccess() != 0) {
				handbookingerMap.put(new Integer(base), handbookinger
						.getCompensate()
						+ "");
			}
		}
		handbookingerMap.put(new Integer(Constants.RANDOM_BASE), base + "");

		return handbookingerMap;
	}
	//liuyi 2006-12-01 程序优化 end

	/**
	 * 获取随机HandbookingerId
	 * 
	 * @return
	 */
	public String getRandomHandbookingerId() {
		TreeMap handbookingerMap = getHandbookingerMap();
		Random random = new Random();
		int base = Integer.parseInt((String) handbookingerMap.get(new Integer(
				Constants.RANDOM_BASE)));
		if (base == 0)
			return "1";
		int rand = random.nextInt(base) + 1;
		Set set = handbookingerMap.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			if (key.intValue() >= rand) {
				String compensate = (String) handbookingerMap.get(key);
				HandbookingerBean bean = getHandbookinger("compensate="
						+ compensate);
				return bean.getId() + "";
			}
		}

		return "1";
	}

	public int[] riffle(int[] array) {
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

	public Vector getHandbookingerList() {

		Vector handbookingerList = (Vector) OsCacheUtil.get("query",
				OsCacheUtil.HANDBOOKINGER_GROUP,
				OsCacheUtil.HANDBOOKINGER_FLUSH_PERIOD);
		if (handbookingerList == null) {
			handbookingerList = getJobService().getHandbookingerList(null);
			if (handbookingerList != null)
				OsCacheUtil.put("query", handbookingerList,
						OsCacheUtil.HANDBOOKINGER_GROUP);
		}
		int array[] = new int[8];
		for (int i = 0; i < 8; i++) {
			HandbookingerBean handbookinger = (HandbookingerBean) handbookingerList
					.get(i);
			array[i] = handbookinger.getId();
		}
		array = riffle(array);
		for (int i = 0; i < 8; i++) {
			HandbookingerBean handbookinger = getHandbookinger("id=" + array[i]);
			handbookingerList.set(i, handbookinger);
		}
		return handbookingerList;
	}

	public HandbookingerBean getHandbookinger(String condition) {

		HandbookingerBean handbookinger = (HandbookingerBean) OsCacheUtil.get(
				condition, OsCacheUtil.HANDBOOKINGER_GROUP,
				OsCacheUtil.HANDBOOKINGER_FLUSH_PERIOD);
		if (handbookinger == null) {
			handbookinger = getJobService().getHandbookinger(condition);
			if (handbookinger != null)
				OsCacheUtil.put(condition, handbookinger,
						OsCacheUtil.HANDBOOKINGER_GROUP);
		}

		return handbookinger;
	}

	public void getHorseList(HttpServletRequest request) {
		HashMap map = LoadResource.getHorse();
		HashMap map1 = new HashMap();
		int array[] = new int[39];
		int array1[] = new int[8];
		for (int i = 0; i < 39; i++) {
			array[i] = i;
		}
		array = riffle(array);
		for (int i = 0; i < 8; i++) {
			String name = (String) map.get(array[i] + "");
			map1.put(i + "", name);
			array1[i] = array[i];

		}
		session.setAttribute("horseList", map1);
		session.setAttribute("array", array1);
	}

	public void chipIn(HttpServletRequest request) {
		if (session.getAttribute("chipIn") != null)
			session.removeAttribute("chipIn");
		int money = getParameterInt("money");
		int horseId = getParameterInt("horseId");
		int compensateId = getParameterInt("compensateName");
		int id = getParameterInt("id");
		String result = null;
		String tip = null;
		UserStatusBean status = (UserStatusBean) UserInfoUtil
				.getUserStatus(loginUser.getId());
		if (money < 0 || money > 10000000 || money == 0) {
			result = "failure";
			tip = "赌注必须在(1-10000000)之间！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;

		} else if (status.getGamePoint() == 0 || status.getGamePoint() < money) {
			result = "failure";
			tip = "您的乐币不够买赌注";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;

		} else if (status.getGamePoint() > money
				|| status.getGamePoint() == money) {
			UserInfoUtil.updateUserStatus("game_point=game_point-" + money,
					"user_id=" + loginUser.getId(), loginUser.getId(),
					UserCashAction.GAME, "跑马游戏下赌注" + money + "乐币");
			// add by WUCX 2006-11-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.HANDBOOKINGER, money,
					Constants.SUBTRATION, loginUser.getId());
			// add by WUCX 2006-11-24 for stat user money history start
			request.setAttribute("money", money + "");
			request.setAttribute("horseId", String.valueOf(horseId));
			request.setAttribute("compensateId", String.valueOf(compensateId));
			request.setAttribute("id", String.valueOf(id));
			return;
		} else {
			result = "failure";
			tip = "错误！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}

	}

	public void result(HttpServletRequest request) {
		if (session.getAttribute("jump") != null)
			session.removeAttribute("jump");
		int id = getParameterInt("id");
		int winId = StringUtil.toInt(getRandomHandbookingerId());
		int myHorseId = getParameterInt("horseId");
		int myCompensateId = getParameterInt("compensateId");
		int money = getParameterInt("money");
		HandbookingerBean handbookinger = (HandbookingerBean) getJobService()
				.getHandbookinger("id=" + myCompensateId);
		int win = 0;
		if (winId == myCompensateId) {
			win = 1;
			UserInfoUtil.updateUserStatus("game_point=game_point+" + money
					* handbookinger.getCompensate(), "user_id="
					+ loginUser.getId(), loginUser.getId(),
					UserCashAction.GAME, "跑马游戏赢" + money
							* handbookinger.getCompensate() + "乐币");
			MoneyAction.addMoneyFlowRecord(Constants.HANDBOOKINGER,  money
					* handbookinger.getCompensate(),
					Constants.PLUS, loginUser.getId());
		}
		HandbookingerRecordBean bean = new HandbookingerRecordBean();
		bean.setCompensateId(myCompensateId);
		bean.setHorseId(myHorseId);
		bean.setMark(win);
		bean.setMoney(money);
		bean.setUserId(loginUser.getId());
		getJobService().addHandbookingerRecord(bean);
		request.setAttribute("win", win + "");
		request.setAttribute("winId", winId + "");
		request.setAttribute("horseId", myHorseId + "");
		request.setAttribute("id", String.valueOf(id));
		request.setAttribute("money", money * handbookinger.getCompensate()
				+ "");
	}

	public String getRandomResult(int a) {
		String[] result = new String[7];
		result[0] = "您买的horse功亏一篑屈居第二！看来您的马经看的还不够多，请再接再厉!";
		result[1] = "您买的horse跑进了3甲，位列第三！希望您下次运气好点！";
		result[2] = "您买的horse冲刺阶段丧失了领先优势！仅获得了第四名！唉，下次挑一匹耐力好点的马吧。";
		result[3] = "您买的horse后半程发力，但为时已晚，仅获得第五名！失败是成功之母，老兄，再玩一盘？";
		result[4] = "您买的horse似乎一直精神不大好，最后位居第六。看来您的眼光下次还得再准点儿哦！";
		result[5] = "您买的horse从一开始就落后，最后得了第七，没有垫底。他的精神值得学习！祝下次好运哦！";
		result[6] = "很遗憾，您买的horse当了倒数第一。别太难受，下次一定触底反弹！";
		return result[a];
	}

}
