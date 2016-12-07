package net.joycool.wap.action.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 俄罗斯轮盘
 * 
 * @author hy
 * 
 */
public class SquareWheelAction {
	UserBean loginUser = null;

	HttpSession session = null;

	int userMoney;

	UserStatusBean userStatusBean;

	private IJobService jobService = null;

	public SquareWheelAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
		userStatusBean = null;
		if (loginUser != null){
			userStatusBean = UserInfoUtil.getUserStatus(loginUser.getId());
		}
		if (userStatusBean != null) {
			userMoney = userStatusBean.getGamePoint();
		}
	}

	public IJobService getJobService() {
		if (jobService == null) {
			jobService = ServiceFactory.createJobService();
		}
		return jobService;
	}

	public void chipIn(HttpServletRequest request) {
		int money = StringUtil.toInt(request.getParameter("money"));
		int num = StringUtil.toInt(request.getParameter("num"));
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
			int afterMoney = this.guessProcess(money, num);
			session.setAttribute("num", String.valueOf(num));
			if (afterMoney == money && afterMoney != 0) {
				session.setAttribute("result", this.no[this.getNumber(3) - 1]
						+ ",损失" + afterMoney + "乐币!");
				session.setAttribute("flag", "false");
				UserInfoUtil.updateUserStatus("game_point=game_point-" + money,
						"user_id=" + loginUser.getId(), loginUser.getId(),
						UserCashAction.GAME, "完俄罗斯轮盘：" + (userMoney - money));
				session.setAttribute("userMoney", String.valueOf(userMoney
						- money));
			} else {
				session.setAttribute("result", this.yes[this.getNumber(5) - 1]
						+ ",赚" + afterMoney + "乐币!");
				session.setAttribute("flag", "true");
				UserInfoUtil.updateUserStatus("game_point=game_point+"
						+ afterMoney, "user_id=" + loginUser.getId(), loginUser
						.getId(), UserCashAction.GAME, "完俄罗斯轮盘："
						+ (userMoney + afterMoney));
				session.setAttribute("userMoney", String.valueOf(userMoney
						+ afterMoney));
			}
			return;
		} else {
			result = "failure";
			tip = "错误！";
			request.setAttribute("tip", tip);
			request.setAttribute("result", result);
			return;
		}

	}

	public void setSession() {
		session.setAttribute("isrunwheel", "true");
	}

	public boolean getSession() {
		if (session.getAttribute("isrunwheel") != null) {
			return true;
		}
		return false;
	}

	public void remSession() {
		session.removeAttribute("isrunwheel");
	}

	/**
	 * 
	 * @param money
	 *            投注钱数
	 * @param num
	 *            投注数字
	 * @return 要赢或者输的钱数
	 */
	private int guessProcess(int money, int num) {
		int randNum = this.getNumber(116);
		switch (num) {
		case 0:
			if (isRight(randNum, 0, 3)) {
				return money * 20;
			} else {
				return money;
			}
		case 1:
			if (isRight(randNum, 3, 7)) {
				return money * 15;
			} else {
				return money;
			}
		case 2:
			if (isRight(randNum, 7, 13)) {
				return money * 12;
			} else {
				return money;
			}
		case 3:
			if (isRight(randNum, 13, 21)) {
				return money * 10;
			} else {
				return money;
			}
		case 4:
			if (isRight(randNum, 21, 30)) {
				return money * 9;
			} else {
				return money;
			}
		case 5:
			if (isRight(randNum, 30, 42)) {
				return money * 8;
			} else {
				return money;
			}
		case 6:
			if (isRight(randNum, 42, 50)) {
				return money * 7;
			} else {
				return money;
			}
		case 7:
			if (isRight(randNum, 50, 68)) {
				return money * 5;
			} else {
				return money;
			}
		case 8:
			if (isRight(randNum, 68, 88)) {
				return money * 4;
			} else {
				return money;
			}
		case 9:
			if (isRight(randNum, 88, 116)) {
				return money * 3;
			} else {
				return money;
			}
		}
		return 0;
	}

	private boolean isRight(int num, int startNum, int endNum) {
		if (num > startNum && num <= endNum) {
			return true;
		}
		return false;
	}

	private int getNumber(int num) {

		return ((new java.util.Random()).nextInt(num) + 1);
	}

	private String[] yes = { "恭喜中了！", "正中下怀，恭喜恭喜啊！", "太悬了，正中啊！", "真是幸运呀！",
			"这都能中！可以买彩票去了！" };

	private String[] no = { "呜呜…没中", "没关系，再接再厉", "下次努力哦！" };

}
