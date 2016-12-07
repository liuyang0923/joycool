//20070207 liq
package net.joycool.wap.bean.question;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.cache.GameQuestionCacheUtil;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class QuestionAction extends CustomAction{
	public static int baseMoney = 100000;	// 基础奖励100000
	public static int dbaseMoney = 200000;
	public static int loseMoney = 100000; 
	
	public static int maxTry = 250;	// 每天最多创关300次（每次最多可以答10题）
	
	UserBean loginUser;

	public int Total = 0;
	public int today2 = 0;

	int winMon = 0;

	int winExp = 0;

	int loseMon = 0;

	int loseExp = 0;

	String say = "";

	String level = "";

	public int page = 0;

	static String[] right_str = { "神童!", "恭喜你答对了!", "无名世纪的人才呀!", "真是聪明呀!" };

	static String[] mistake_str = { "答错了!", "菜鸟!", "不明智的选择！", "无语中…" };

	static QuestionServiceImpl questionService = new QuestionServiceImpl();

	/*
	 * (non-Javadoc)
	 * 
	 * 输出
	 */
	public void pri(String xxx) {
		System.out.println(xxx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 构造函数
	 */
	public QuestionAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 判断是否是回答问题
	 */
	public int IfResult(HttpServletRequest request, HttpServletResponse response) {

		int x = getParameterInt("id");
		int y  = 0 ;

		QuestionWareHouseBean questionWareHouse = (QuestionWareHouseBean)session.getAttribute("questionWareHouse");
		if(questionWareHouse != null)
			y = questionWareHouse.getId();

		if (y == -1)
			request.setAttribute("questionerror", "error");

		if ((request.getParameter("rs") != null)
				&& x == y)// 防止用户刷新
		{
			int temp = 0;
			temp = result(request, response,y);
			setPage();
			return temp;
		} else {
			getLevle();
			setPage();
			return 0;
		}
	}

	/*
	 * 设置玩家等级 (non-Javadoc)
	 * 
	 * @param weight
	 */
	public void setPage() {
		if (Total < 100)// 书生
		{
			request.setAttribute("msgPage", "shusheng.gif");
		} else if ((Total >= 100) && (Total < 199))// 秀才
		{
			request.setAttribute("msgPage", "xiuchai.gif");
		} else if ((Total >= 199) && (Total < 298))// 举人
		{
			request.setAttribute("msgPage", "juren.gif");
		} else if ((Total >= 298) && (Total < 397))// 进士
		{
			request.setAttribute("msgPage", "jinshi.gif");
		} else if ((Total >= 397))// 状元
		{
			request.setAttribute("msgPage", "zhuangyuan.gif");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 随机产生一道题
	 */
	public void question(HttpServletRequest request) {
		QuestionWareHouseBean questionWareHouse;
		if (Total > 198)
			questionWareHouse = GameQuestionCacheUtil.getQuestionWareHouse(1);
		else
			questionWareHouse = GameQuestionCacheUtil.getQuestionWareHouse(0);
		session.setAttribute("questionWareHouse", questionWareHouse);
	}

	/*
	 * (non-Javadoc) // 判断选择的答案是否与标准答案一致,并更改数据库
	 */
	// 
	public int result(HttpServletRequest request, HttpServletResponse response,int temp_id) {
		int total = 0;
		int today = 0;
		int count = 0;

		// 取得参数
		String id = Integer.toString(temp_id);
		String rs = request.getParameter("rs");
		
		// 类型转换
		if (StringUtil.toInt(rs) == -1) {
			request.setAttribute("questionerror", "error");
			return 4;
		}
		int rs1 = StringUtil.toInt(rs);
		// 通过参数得到一条题目记录
		QuestionWareHouseBean questionWareHouse = GameQuestionCacheUtil
				.getQuestionWareHouse(id);
		// 判断是否得到题目
		if ((questionWareHouse == null)
				|| (request.getAttribute("questionerror") != null)) {

			try {
				request.setAttribute("questionerror", "error");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 4;

		}
		// 得到题目了
		if (session.getAttribute("winCount") == null) {
			session.setAttribute("winCount", new Integer(count));
		} else {
			if (StringUtil.toInt(session.getAttribute("winCount").toString()) == -1) {
				request.setAttribute("questionerror", "error");
				return 4;
			}
			count = StringUtil.toInt(session.getAttribute("winCount")
					.toString());
		}
		
		QuestionHistory history = questionService.getHistory(loginUser.getId());
		total = history.getTotal();
		today = history.getToday();
		today2 = history.getToday2();
		Total = total;
		// 判断标准答案是否和用户选择答案一致,如果一致加分
		if (questionWareHouse.getResult() == rs1) {
			if (count >= 9) {
				if(today2 <= maxTry)
					total++;
				today++;
				questionService.setValue(loginUser.getId(), total, today);
			} else if(count == 0) {
				// 增加当天闯成功第一关的次数
				SqlUtil.executeUpdate("update game_question_history set today2 = today2+1 where id = "+ loginUser.getId());
			}
			// System.out.println("对了");
			// 判断应该增加多少
			getWinPoint(total, count);
			// 更新用户状态

			boolean flag = UserInfoUtil.updateUserCash(loginUser.getId(), winMon,
					9, "用户答对题给用户加" + winMon + "乐币");
			// 增加经验值
			RankAction.addPoint(loginUser, winExp);

			// 判断是否更新成功
			if (flag) {
				// add by zhangyi 2006-07-24 for stat user money history
				// start
				MoneyAction.addMoneyFlowRecord(Constants.JOB, winMon,
						Constants.PLUS, loginUser.getId());
				// add by zhangyi 2006-07-24 for stat user money history end
				count++;
				if (count != 9)
					request.setAttribute("msg_1", "第" + Integer.toString(count)
							+ "关闯关成功");
				else
					request.setAttribute("msg_1", "第" + Integer.toString(count)
							+ "关闯关成功,胜利就在眼前喽-加油呀");
				request.setAttribute("msg_2", say + "你获得乐币" + winMon + ",经验增加"
						+ winExp);
				request.setAttribute("msg_3", level);
				if (count > 9)
					count = 0;
				session.setAttribute("winCount", Integer.toString(count));
			} else {
				try {
					// return;
					request.setAttribute("questionerror", "error");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return 1;
			// 断标准答案是否和用户选择答案一致,如果不一致扣分
		} else {
			// 判断应该减少多少
			if (StringUtil.toInt(session.getAttribute("winCount").toString()) == -1) {
				request.setAttribute("questionerror", "error");
				return 4;
			}
			getLostPoint(total, StringUtil.toInt(session.getAttribute(
					"winCount").toString()));
			// 更新用户状态

			UserInfoUtil.updateUserCash(loginUser.getId(), -loseMon, 9, "用户答错题用户扣" + loseMon + "乐币");

			// 增加经验值
			RankAction.addPoint(loginUser, (-1) * loseExp);

			// start
			MoneyAction.addMoneyFlowRecord(Constants.JOB, loseMon,
					Constants.SUBTRATION, loginUser.getId());
			// add by zhangyi 2006-07-24 for stat user money history end
			request.setAttribute("msg_1", "第" + Integer.toString(count + 1)
					+ "关闯关失败");
			request.setAttribute("msg_2", say + "您减少了" + loseMon + "乐币,"
					+ loseExp + "经验");
			request.setAttribute("msg_3", level);
			// 答错题之后的跳转
			if (count <= 4)
				count = 0;
			else if (count <= 8) {
				count = 1;
				SqlUtil.executeUpdate("update game_question_history set today2 = today2+1 where id = "+ loginUser.getId());
			} else if (count <= 10)
				count = 0;
			session.setAttribute("winCount", Integer.toString(count));

			return 2;
		}

	}

	/*
	 * (non-Javadoc) // 判断选择的答案是否与标准答案一致,并更改数据库
	 */
	// 
	public void getWinPoint(int use, int question) {
		// 游戏分书生（0<=x<=99）、秀才(100<=x<199)、举人(199<=x<298)、进士(298<=x<397)、状元(397<=x)5个等级
		int value = use;
		if (value < 100)// 书生
		{
			if (question == 9) {
				winExp = 30;
			} else {
				winExp = 15;
			}
			level = "等级:书生" + Integer.toString(value) + "阶";
		} else if ((value >= 100) && (value < 199))// 秀才
		{
			if (question == 9) {
				winExp = 40;
			} else {
				winExp = 20;
			}
			level = "等级:秀才" + Integer.toString(value - 99) + "阶";
		} else if ((value >= 199) && (value < 298))// 举人
		{
			if (question == 9) {
				winExp = 50;
			} else {
				winExp = 25;
			}
			level = "等级:举人" + Integer.toString(value - 198) + "阶";
		} else if ((value >= 298) && (value < 397))// 进士
		{
			if (question == 9) {
				winExp = 60;
			} else {
				winExp = 30;
			}
			level = "等级:进士" + Integer.toString(value - 297) + "阶";
		} else if ((value >= 397))// 状元
		{
			if (question == 9) {
				winExp = 70;
			} else {
				winExp = 30;
			}
			level = "等级:状元" + Integer.toString(value - 396) + "阶";
		}
		if(today2 > maxTry || DateUtil.dayHour(System.currentTimeMillis()) < 8 || value < 2000 && today2 > 100)
			loseMon = 0;
		else {
			if(value > spe_number)
				value =  spe_number + (int)Math.pow(value - spe_number, 0.8);
			if (question == 9) {
				winMon = dbaseMoney + value * 100;
			} else {
				winMon = baseMoney + value * 100;
			}
			if(winMon > 1000000)
				winMon = 1000000;
		}
		if (question == 9) {
			say = "太棒了，真是无名世纪的人才呀！";
			request.setAttribute("msg_4", "成功晋级,继续闯关");
		} else
			say = right_str[Math.abs(RandomUtil.nextInt(4))];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置用户提示信息
	 */
	// 
	static int spe_number = 2000;
	public void getLostPoint(int use, int question) {
		// 游戏分书生（0<=x<=99）、秀才(100<=x<199)、举人(199<=x<298)、进士(298<=x<397)、状元(397<=x)5个等级

		int value = use;
		if (value <= 99)// 书生
		{
			loseExp = 5;
			level = "等级:书生" + Integer.toString(value) + "阶";
		} else if ((value >= 100) && (value < 199))// 秀才
		{
			loseExp = 5;
			level = "等级:秀才" + Integer.toString(value - 99) + "阶";
		} else if ((value >= 199) && (value < 298))// 举人
		{
			loseExp = 10;
			level = "等级:举人" + Integer.toString(value - 198) + "阶";
		} else if ((value >= 298) && (value < 397))// 进士
		{
			loseExp = 10;
			level = "等级:进士" + Integer.toString(value - 297) + "阶";
		} else if ((value >= 397))// 状元
		{
			loseExp = 10;
			level = "等级:状元" + Integer.toString(value - 396) + "阶";
		}
		if(today2 > maxTry || DateUtil.dayHour(System.currentTimeMillis()) < 8)
			loseMon = 0;
		else {
			if(value > spe_number)
				value = spe_number + (int)Math.pow(value - spe_number, 0.8);
			loseMon = loseMoney + value * 100;
			if(loseMon > 1000000)
				loseMon = 1000000;
		}
		if (question == 9)
			say = "天呀，功亏一篑!";
		else
			say = mistake_str[Math.abs(RandomUtil.nextInt(4))];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 设置用户信息
	 */
	// 
	public void getLevle() {
		QuestionHistory history = questionService.getHistory(loginUser.getId());
		Total = history.getTotal();
		today2 = history.getToday2();
		if (Total <= 99)// 书生
		{
			level = "等级:书生" + Integer.toString(Total) + "阶";
		} else if ((Total >= 100) && (Total < 199))// 秀才
		{
			level = "等级:秀才" + Integer.toString(Total - 99) + "阶";
		} else if ((Total >= 199) && (Total < 298))// 举人
		{
			level = "等级:举人" + Integer.toString(Total - 198) + "阶";
		} else if ((Total >= 298) && (Total < 397))// 进士
		{
			level = "等级:进士" + Integer.toString(Total - 297) + "阶";
		} else if ((Total >= 397))// 状元
		{
			level = "等级:状元" + Integer.toString(Total - 396) + "阶";
		}
		request.setAttribute("msg_3", level);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 取得历史大排名
	 */
	// 
	public List getTotalList(HttpServletRequest request) {
		if (StringUtil.toInt(request.getParameter("topage")) == -1) {
			request.setAttribute("questionerror", "error");
			return null;
		}
		page = StringUtil.toInt(request.getParameter("topage"));
		List list = questionService.getTotalList();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 取得当日大排名
	 */
	// 
	public List getTodayList(HttpServletRequest request) {
		if (StringUtil.toInt(request.getParameter("topage")) == -1) {
			request.setAttribute("questionerror", "error");
			return null;
		}
		page = StringUtil.toInt(request.getParameter("topage"));
		List list = questionService.getTodayList();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 取得历史大排名
	 */
	// 
	public String getEum(eum temp, int order) {
		String a = "";
		if (UserInfoUtil.getUser(temp.getId()) != null) {
			a =
			// "第" + Integer.toString(order)
			// + "名 "
			// + StringUtil.convertString(UserInfoUtil.getUser(
			// temp.getId()).getNickName()) + " " +
			getPoint(temp.getTotalvalue()) + " "
					+ Integer.toString(temp.getTotalvalue() * 10);
		}
		return a;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 取得当日大排名
	 */
	// 
	public String getEumToday(eum temp, int order) {
		String a = "";
		if (UserInfoUtil.getUser(temp.getId()) != null) {
			a =
			// "第" + Integer.toString(order)
			// + "名 "
			// + StringUtil.convertString(UserInfoUtil.getUser(
			// temp.getId()).getNickName()) + " "+
			getPoint(temp.getTotalvalue()) + " "
					+ Integer.toString(temp.getTodayvalue() * 10);
		}
		return a;
	}

	public String getPoint(int use) {
		// 游戏分书生（0<=x<=99）、秀才(100<=x<199)、举人(199<=x<298)、进士(298<=x<397)、状元(397<=x)5个等级
		String str = "";
		if (use < 100)// 书生
		{
			str = Integer.toString(use) + "阶书生";
		} else if ((use >= 100) && (use < 199))// 秀才
		{
			str = Integer.toString(use - 99) + "阶秀才";
		} else if ((use >= 199) && (use < 298))// 举人
		{
			str = Integer.toString(use - 198) + "阶举人";
		} else if ((use >= 298) && (use < 397))// 进士
		{
			str = Integer.toString(use - 297) + "阶进士";
		} else if ((use >= 397))// 状元
		{
			str = Integer.toString(use - 396) + "阶状元";
		}
		return str;
	}

	public String getnexpage() {
		return Integer.toString(page + 1);
	}

	public String getprepage() {
		return Integer.toString(page - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * 取得玩家排名
	 */
	// 
	public int getOrderTotal(String value) {
		int temp = 0;
		if (value == "totalValue")
			temp = questionService.getOrderTotal("totalValue", questionService
					.getTotalValue(loginUser.getId()));
		if (value == "todayValue")
			temp = questionService.getOrderTotal("todayValue", questionService
					.getTodayValue(loginUser.getId()));

		return temp;
	}
}
