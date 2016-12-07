package net.joycool.wap.action.job;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.JCLotteryGuessBean;
import net.joycool.wap.bean.job.JCLotteryNumberBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class JCLotteryAction extends CustomAction{
	UserBean loginUser;

	int MAX_GAMEPOINT = 1000000000;

	IJobService jobService = ServiceFactory.createJobService();

	// 构造函数
	public JCLotteryAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
	}

	// 得到当前猜数字游戏相关信息.
	public void lottery(HttpServletRequest request) {
		// 获得当前时间
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		Timestamp ts = null;
		SimpleDateFormat dateFormatter = null;
		// 得到最大期号
		int number = jobService.getMaxJCLotteryNumber();
		// 初始化JCLotteryNumberBean
		JCLotteryNumberBean jcLotteryNumber = new JCLotteryNumberBean();
		// 初始化总下注金额
		long count = 0;
		long count1 = 0;
		long totalcount = 0;
		// 判断期号是否为空
		if (number == 0) {
			// 如果为空set当前期号为1
			jcLotteryNumber.setGuessId(1);
			number = jcLotteryNumber.getGuessId();
			// 获得总下注金额
			count = jobService.getSumWager("guess_id=" + number);
			// 把总下注金额set到bean中
			jcLotteryNumber.setLeftWager(count);
		} else {
			jcLotteryNumber.setGuessId(number + 1);
			// 获得总下注金额
			count = jobService.getSumWager("guess_id=" + (number + 1));
			count1 = jobService.getJCLotteryNumberCount("guess_id=" + (number));
			totalcount = count + count1;
			// 把总下注金额set到bean中
			jcLotteryNumber.setLeftWager(totalcount);
		}
		// 设置开奖时间
		String date1 = null;
		String date2 = null;
		if (hour < 20) {
			ts = new Timestamp(c.getTimeInMillis());
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			date1 = String.valueOf(dateFormatter.format(ts));
			date2 = date1 + " 20:00";
		} else {
			c.add(Calendar.DATE, 1);
			ts = new Timestamp(c.getTimeInMillis());
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			date1 = String.valueOf(dateFormatter.format(ts));
			date2 = date1 + " 20:00";
		}
		// 把开奖时间set到bean中
		jcLotteryNumber.setLotteryDate(date2);
		// 把bean放在请求范围内
		request.setAttribute("jcLotteryNumber", jcLotteryNumber);
		session.setAttribute("Lottery", "Lottery");
	}

	// 判断更新用户提交下注信息
	public void userCommit(HttpServletRequest request) {
		JCLotteryGuessBean jcLotteryGuess = null;
		String tip = null;
		String result = "success";
		// 取得参数
		int wager = StringUtil.toInt(request.getParameter("wager"));
		int number = StringUtil.toInt(request.getParameter("number"));
		int guessId = StringUtil.toInt(request.getParameter("guessId"));
		// 得到当前用户ID
		// int userId = getUserStatus(loginUser.getId()).getUserId();
		// 获取用户下注次数
		int count = jobService.getJCLotteryGuessCount("user_id="
				+ loginUser.getId() + " and guess_id=" + guessId);
		// 判断输入项
		if (number < 1 || number > 100) {
			tip = "请输入1~100之间的数字!";
			result = "failure";
		}
		// mcq_2006-6-22_更改最小下注_start
		if (wager < 1000) {
			tip = "乐酷提示:猜数字赢大奖最少下注1000乐币!";
			// mcq_2006-6-22_更改最小下注_end
			result = "failure";
		} else if (wager > MAX_GAMEPOINT) {
			tip = "乐酷提示:猜数字赢大奖最多下注十亿乐币!";
			result = "failure";
		} else if (wager > (getUserStatus(loginUser.getId())).getGamePoint()) {
			tip = "您的乐币不够了!";
			result = "failure";
		}
		// 输入项有错
		if ("failure".equals(result)) {
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		} else {
			// 得到当前用户积分
			int point = getUserStatus(loginUser.getId()).getGamePoint();
			// 通过条件查询用户下注记录
			jcLotteryGuess = jobService.getJCLotteryGuess(" user_id="
					+ loginUser.getId() + " and number=" + number
					+ " and guess_id=" + guessId);
			// 判断用户下注记录
			if (jcLotteryGuess == null) {
				// 判断用户下注次数
				if (count >= 5) {
					tip = "乐酷提示:本轮猜数字您下注已超过5次,请您下期继续参加活动!";
					result = "failure";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}
				// 如果下注信息为空,set信息到bean中
				jcLotteryGuess = new JCLotteryGuessBean();
				jcLotteryGuess.setGuessId(guessId);
				jcLotteryGuess.setUserId(loginUser.getId());
				jcLotteryGuess.setNumber(number);
				jcLotteryGuess.setWager(wager);
				jcLotteryGuess.setGuessDatetime("now()");
				// 插入用户下注信息
				boolean flag = jobService.addJCLotteryGuess(jcLotteryGuess);
				// 判断是否插入成功,如果成功减去用户积分
				if (flag) {
					UserInfoUtil.updateUserStatus("game_point=game_point-"
							+ wager, "user_id=" + loginUser.getId(), loginUser
							.getId(), UserCashAction.WAGER, "彩票下注成功减去用户"
							+ wager + "乐币");

				}
				// MoneyAction.addMoneyFlowRecord(Constants.LOTTERY, wager,
				// Constants.SUBTRATION, loginUser.getId());
				request.setAttribute("result", result);
				request.setAttribute("wager", String.valueOf(wager));
				return;
			} else {
				long totalWager = jcLotteryGuess.getWager() + wager;
				// 判断用户下注金额
				if (totalWager > MAX_GAMEPOINT) {
					tip = "乐酷提示:您累计下注" + number + "这个号码乐币数超过" + MAX_GAMEPOINT
							+ "乐币,请下注其他号码";
					result = "failure";
					request.setAttribute("tip", tip);
					request.setAttribute("result", result);
					return;
				}
				boolean flag = jobService.updateJCLotteryGuess("wager="
						+ totalWager, "id=" + jcLotteryGuess.getId());
				// 判断是否更新成功,如果成功减去用户积分
				if (flag) {
					UserInfoUtil.updateUserStatus("game_point=game_point-"
							+ wager, "user_id=" + loginUser.getId(), loginUser
							.getId(), UserCashAction.WAGER, "彩票下注成功减去用户积分"
							+ wager + "乐币");
				}
				// MoneyAction.addMoneyFlowRecord(Constants.LOTTERY, wager,
				// Constants.SUBTRATION, loginUser.getId());
				request.setAttribute("result", result);
				request.setAttribute("wager", String.valueOf(totalWager));
				return;
			}
		}
	}

	public void lotteryHistory(HttpServletRequest request) {
		// 得到最大期号
		int number = jobService.getMaxJCLotteryNumber();
		number = number < 0 ? 1 : number;
		JCLotteryNumberBean jcLottertNumber = jobService
				.getJCLotteryNumber("guess_id=" + number);
		Vector jcLotteryHistoryList = jobService
				.getJCLotteryHistoryList("guess_id=" + number);
		request.setAttribute("jcLotteryHistoryList", jcLotteryHistoryList);
		request.setAttribute("jcLottertNumber", jcLottertNumber);
	}

	public void myLottery(HttpServletRequest request) {
		int guessId = getParameterInt("guessId");
		Vector jcLotteryGuessList = jobService.getJCLotteryGuessList("user_id="
				+ loginUser.getId() + " and guess_id=" + guessId);
		request.setAttribute("jcLotteryGuessList", jcLotteryGuessList);
	}

	// 判断登陆用户在user_status表中的状态
	public UserStatusBean getUserStatus(int userId) {
		IUserService service = ServiceFactory.createUserService();
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			service.addUserStatus(status);
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			return status;
		}
	}
}
