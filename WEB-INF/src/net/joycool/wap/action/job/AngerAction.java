package net.joycool.wap.action.job;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.RankBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.AngerCardBean;
import net.joycool.wap.bean.job.AngerExpressionBean;
import net.joycool.wap.bean.job.HuntQuarryBean;
import net.joycool.wap.bean.job.HuntUserQuarryBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class AngerAction extends CustomAction{
	UserBean loginUser;

	static IJobService jobService;

	private TreeMap expressionMap = null;

	public AngerAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();;
	}

	public IJobService getJobService() {
		if (jobService == null) {
			jobService = ServiceFactory.createJobService();
		}
		return jobService;
	}

	public void ventAnger(HttpServletRequest request) {
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String relation = request.getParameter("relation");
		String expression = null;
		String tip = null;
		String img = null;
		if (null != name) {
			if (name.equals("")) {
				tip = "请输入出气对象的姓名！";
				request.setAttribute("tip", tip);
				return;
			}

			// 把受气对象的名称放入session
			session.setAttribute("angername", name);
			// 删除上次出气结果
			session.removeAttribute("angercard");
			session.removeAttribute("angercardNum");
			session.removeAttribute("angergamePoint");
			session.removeAttribute("angerpoint");
			session.removeAttribute("angercontent");
			// // 插入一条记录
			// AngerBean anger = new AngerBean();
			// anger.setDegree(0);
			// anger.setGender(StringUtil.toInt(gender));
			// anger.setName(name);
			// anger.setMark(0);
			// anger.setRelation(StringUtil.toInt(relation));
			// anger.setUserId(loginUser.getId());
			// jobService.addAnger(anger);
			// 记录表情表中的对应关系
			int numberR = 0;
			if ("0".equals(gender))
				numberR = StringUtil.toInt(relation) * 2;
			else
				numberR = StringUtil.toInt(relation) * 2 - 1;
			img = relation + "_" + gender + "_9.gif";
			session.setAttribute("angerimg", img);
			session.setAttribute("angergender", gender + "");
			session.setAttribute("angernumber", numberR + "");
			session.setAttribute("angerrelation", relation + "");
			session.setAttribute("angerbleed", "100");
			session.setAttribute("angercount", "0");
			expression = getRandomExpression(numberR, 1);
			session.setAttribute("angerexpression", expression);
			return;
		}

		String count = (String) session.getAttribute("angercount");
		String count1 = (String) session.getAttribute("angercount1");
		session.setAttribute("angercount1", count);
		if (count1 != null && count1.equals(count)) {
			return;
		}
		gender = (String) session.getAttribute("angergender");
		relation = (String) session.getAttribute("angerrelation");
		if (gender == null || relation == null)
			return;
		name = (String) session.getAttribute("angername");
		String number = (String) session.getAttribute("angernumber");
		int bleeds = StringUtil.toInt((String) session
				.getAttribute("angerbleed"));
		if (bleeds > 100) {
			bleeds = 100;
			img = "9";
		} else if (bleeds > 89)
			img = "9";
		else if (bleeds > 79 && bleeds < 90)
			img = "8";
		else if (bleeds > 69 && bleeds < 80)
			img = "7";
		else if (bleeds > 59 && bleeds < 70)
			img = "6";
		else if (bleeds > 49 && bleeds < 60)
			img = "5";
		else if (bleeds > 39 && bleeds < 50)
			img = "4";
		else if (bleeds > 29 && bleeds < 40)
			img = "3";
		else if (bleeds > 19 && bleeds < 30)
			img = "2";
		else if (bleeds > 0 && bleeds < 20)
			img = "1";
		else {
			bleeds = 0;
			img = "0";
		}

		if (bleeds > 49)
			expression = getRandomExpression(StringUtil.toInt(number), 2);
		if (bleeds > 0 && bleeds < 50)
			expression = getRandomExpression(StringUtil.toInt(number), 3);
		if (count.equals("0"))
			expression = getRandomExpression(StringUtil.toInt(number), 1);
		if (expression == null)
			expression = "无语。。。。";
		img = relation + "_" + gender + "_" + img + ".gif";
		session.setAttribute("angerimg", img);
		session.setAttribute("angerbleed", bleeds + "");
		session.setAttribute("angerexpression", expression);

	}

	public void jump(HttpServletRequest request) {
		if (session.getAttribute("angerrefresh") != null) {
			return;
		}
		// 防止后退刷新
		String angersCount = request.getParameter("angersCount");
		if (session.getAttribute("angersCount") != null
				&& !(((String) session.getAttribute("angersCount"))
						.equals(angersCount))) {
			return;
		}
		int counts = StringUtil.toInt(angersCount) + 1;
		session.setAttribute("angersCount", counts + "");
		String gender = (String) session.getAttribute("angergender");
		String relation = (String) session.getAttribute("angerrelation");
		if (gender == null || relation == null)
			return;
		String actions = request.getParameter("actions");
		int bleeds = StringUtil.toInt((String) session
				.getAttribute("angerbleed"));
		String tip = null;
		UserStatusBean user = UserInfoUtil.getUserStatus(loginUser.getId());
		int count = StringUtil.toInt((String) session
				.getAttribute("angercount"));
		if (null != actions) {
			if (user.getGamePoint() < 10000) {
				tip = "您的乐币不够支付!";
				request.setAttribute("tip", tip);
				return;
			}
			if (UserInfoUtil.updateUserCash(loginUser.getId(), -10000,
					UserCashAction.GAME, "出气筒花钱100乐币")) {
				if (count < 0)
					count = 0;
				count = count + 1;

				// 插入或者更新记录
				int rand = 0;
				// liuyi 2007-02-08 每招回血0-2 start
				int restore = RandomUtil.nextInt(3);
				// liuyi 2007-02-08 每招回血0-2 end
				if ("1".equals(actions)) {
					rand = RandomUtil.nextInt(3) + 4;

				} else if ("2".equals(actions)) {
					rand = RandomUtil.nextInt(5) + 3;
				} else if ("3".equals(actions)) {
					rand = RandomUtil.nextInt(10) + 1;
				} else if ("4".equals(actions)) {
					rand = RandomUtil.nextInt(11);
				} else
					rand = 0;
				bleeds = bleeds - rand + restore;
			}

		}

		if (bleeds < 0)
			bleeds = 0;
		String img = relation + "_" + gender + "_" + "1" + actions + ".gif";
		session.setAttribute("angerimg", img);
		session.setAttribute("angerbleed", bleeds + "");
		session.setAttribute("angercount", count + "");
	}

	public void result(HttpServletRequest request) {
		String gender = (String) session.getAttribute("angergender");
		String relation = (String) session.getAttribute("angerrelation");
		String number = (String) session.getAttribute("angernumber");
		if (gender == null || relation == null || number == null)
			return;
		// 得到招数
		String bleed = (String) session.getAttribute("angerbleed");
		if (("0".equals(bleed))) {
			int count = StringUtil.toInt((String) session
					.getAttribute("angercount"));
			String card = null;
			int cardNum = 0;
			int gamePoint = 0;
			int mark = RandomUtil.nextInt(2);
			int point = 0;
			String content = null;
			// 根据招数奖励
			if (count <= 12) {
				card = "升级卡";
				UserStatusBean user = UserInfoUtil.getUserStatus(loginUser.getId());

				if (user.getRank() < 45) {
					card = "升级卡";
					RankAction.addRank(loginUser);
				} else {
					point = RandomUtil.nextInt(51) + 650;
					card = "金牛卡";
				}
			} else if (count > 12 && count <= 20) {
				// liuyi 2007-02-08 取消财富百分比增长 start
				UserStatusBean user = UserInfoUtil.getUserStatus(loginUser.getId());
				if (mark == 0 && user.getRank() < 36) {
					card = "升级卡";
					RankAction.addRank(loginUser);
				} else {
					point = RandomUtil.nextInt(51) + 650;
					card = "金牛卡";
				}
				// liuyi 2007-02-08 取消财富百分比增长 end
			} else if (count > 20 && count <= 25) {
				// liuyi 2007-02-08 取消财富百分比增长 start
				point = RandomUtil.nextInt(51) + 550;
				card = "金牛卡";
				// liuyi 2007-02-08 取消财富百分比增长 end
			} else if (count > 25 && count <= 30) {
				// liuyi 2007-02-08 取消财富百分比增长 start
				point = RandomUtil.nextInt(51) + 500;
				if (mark == 0) {
					card = "天使卡";
				} else {
					card = "怪兽卡";
					cardNum = RandomUtil.nextInt(8) + 1;
				}
				// liuyi 2007-02-08 取消财富百分比增长 end
			} else if (count > 30 && count <= 40) {
				point = RandomUtil.nextInt(101) + 300;
				card = "怪兽卡";
				cardNum = RandomUtil.nextInt(8) + 1;
			} else if (count > 40 && count <= 50) {
				point = RandomUtil.nextInt(151) + 200;
				if (mark == 0)

				{
					card = "怪兽卡";
					cardNum = RandomUtil.nextInt(8) + 1;
				} else {
					gamePoint = 10000;
				}
			} else if (count > 50 && count <= 60) {
				point = RandomUtil.nextInt(51) + 150;
				gamePoint = 10000;
			} else {
				point = 100;
				gamePoint = 5000;
			}
			AngerCardBean cardBean = null;
			if (cardNum > 0 && card != null) {
				cardBean = getJobService().getAngerCard(
						"name='" + StringUtil.toSql(card) + "' and number=" + cardNum);
				if (cardBean != null) {
					doAddHuntUserQuarry(cardBean.getTitle());
				}
			} else if (card != null)
				cardBean = getJobService().getAngerCard(
						"name='" + StringUtil.toSql(card) + "' and number=1");
			else
				;
			if (cardBean != null)
				content = cardBean.getContent();
			if(gamePoint > 0)
				UserInfoUtil.updateUserCash(loginUser.getId(), gamePoint, UserCashAction.GAME, "出气筒游戏获"
						+ gamePoint + "乐币！");
			if(point > 0)
				RankAction.addPoint(loginUser, point);
			session.removeAttribute("angergender");
			session.removeAttribute("angerrelation");
			session.removeAttribute("angernumber");
			session.removeAttribute("angercount1");
			String expression = getRandomExpression(StringUtil.toInt(number), 4);
			session.setAttribute("angerexpression", expression);
			String img = relation + "_" + gender + "_" + "0.gif";
			session.setAttribute("angerimg", img);
			session.setAttribute("angercard", card);
			session.setAttribute("angercardNum", cardNum + "");
			session.setAttribute("angergamePoint", gamePoint + "");
			session.setAttribute("angerpoint", point + "");
			session.setAttribute("angercontent", content);
		} else
			return;

	}

	public String getRandomExpression(int relation, int phase) {
		AngerExpressionBean bean = null;
		Vector expression = null;
		int id = 0;
		switch (phase) {
		case 1:

			bean = getExpression("relation=" + relation + " and phase=" + phase);
			break;

		case 2:

			id = StringUtil.toInt(getRandomHandbookingerId("relation="
					+ relation + " and phase=" + phase));
			bean = getExpression("id=" + id);

			break;

		case 3:
			id = StringUtil.toInt(getRandomHandbookingerId("relation="
					+ relation + " and phase=" + phase));
			bean = getExpression("id=" + id);
			break;

		case 4:
			bean = getExpression("relation=" + relation + " and phase=" + phase);
			break;

		}
		if (bean != null)
			return bean.getTitle() + ": " + bean.getContent();
		return "无语。。。。";
	}

	// liuyi 2006-12-01 程序优化 start
	public TreeMap getExpressionMap(String condition) {

		if (expressionMap != null) {
			return expressionMap;
		}
		// 获取机率对应表
		expressionMap = new TreeMap();

		Vector expressionList = getExpressionList(condition);
		int size = expressionList.size();
		int base = 0;
		for (int i = 0; i < size; i++) {
			AngerExpressionBean expression = (AngerExpressionBean) expressionList
					.get(i);
			base += expression.getRate();
			if (expression.getRate() != 0) {
				expressionMap.put(new Integer(base), expression.getId() + "");
			}
		}
		expressionMap.put(new Integer(Constants.RANDOM_BASE), base + "");

		return expressionMap;
	}

	// liuyi 2006-12-01 程序优化 end

	/**
	 * 获取随机expressionId
	 * 
	 * @return
	 */
	public String getRandomHandbookingerId(String condition) {
		TreeMap eMap = getExpressionMap(condition);
		Random random = new Random();
		int base = Integer.parseInt((String) eMap.get(new Integer(
				Constants.RANDOM_BASE)));
		if (base == 0)
			return "1";
		int rand = random.nextInt(base) + 1;
		Set set = eMap.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			if (key.intValue() >= rand) {
				String compensate = (String) eMap.get(key);
				AngerExpressionBean bean = getExpression("id=" + compensate);
				return bean.getId() + "";
			}
		}

		return "1";
	}

	public Vector getExpressionList(String condition) {

		Vector expressionList = (Vector) OsCacheUtil.get(condition,
				OsCacheUtil.ANGER_GROUP, OsCacheUtil.ANGER_FLUSH_PERIOD);
		if (expressionList == null) {
			expressionList = getJobService().getAngerExpressionList(condition);
			if (expressionList != null)
				OsCacheUtil.put(condition, expressionList,
						OsCacheUtil.ANGER_GROUP);
		}

		return expressionList;
	}

	public AngerExpressionBean getExpression(String condition) {

		AngerExpressionBean expression = (AngerExpressionBean) OsCacheUtil.get(
				condition, OsCacheUtil.ANGER_GROUP,
				OsCacheUtil.ANGER_FLUSH_PERIOD);
		if (expression == null) {
			expression = getJobService().getAngerExpression(condition);
			if (expression != null)
				OsCacheUtil.put(condition, expression, OsCacheUtil.ANGER_GROUP);
		}

		return expression;
	}

	/**
	 * 插入用户打到的猎物到用户的猎物表（jc_hunt_user_quarry)
	 * 
	 * @param money
	 */
	public void doAddHuntUserQuarry(String querry) {
		HuntQuarryBean bean = getJobService().getHuntQuarry(
				"name='" + StringUtil.toSql(querry) + "'");
		if (bean != null) {

			HuntUserQuarryBean userQuarryBean = new HuntUserQuarryBean();
			userQuarryBean.setUserId(loginUser.getId());
			userQuarryBean.setQuarryId(bean.getId());
			HuntUserQuarryBean uqb = jobService.getHuntUserQuarry(" user_id="
					+ loginUser.getId() + " and quarry_id=" + bean.getId());
			if (uqb == null) {
				getJobService().addHuntUserQuarry(userQuarryBean);
				uqb = jobService.getHuntUserQuarry(" user_id="
						+ loginUser.getId() + " and quarry_id=" + bean.getId());
				if (uqb != null) {
					getJobService().updateHuntUserQuarry(
							" quarry_count=quarry_count+2",
							" user_id=" + loginUser.getId() + " and quarry_id="
									+ bean.getId());
				}

			} else {
				getJobService().updateHuntUserQuarry(
						" quarry_count=quarry_count+3",
						" user_id=" + loginUser.getId() + " and quarry_id="
								+ bean.getId());
			}
		}
	}
}
