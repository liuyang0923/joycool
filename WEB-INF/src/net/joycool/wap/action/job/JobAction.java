package net.joycool.wap.action.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.JobWareHouseBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.UserInfoUtil;

public class JobAction extends CustomAction{
	UserBean loginUser;

	int winPoint = 100;

	int losePoint = 10;

	static IJobService jobService = ServiceFactory.createJobService();
	static IUserService service = ServiceFactory.createUserService();

	public JobAction(HttpServletRequest request) {
		super(request);
		loginUser = (UserBean) session.getAttribute(
				Constants.LOGIN_USER_KEY);
	}

	// 随机取得一道问答题目
	public void question(HttpServletRequest request) {
		JobWareHouseBean jobWareHouse = jobService.getJobWareHouse();
		request.setAttribute("jobWareHouse", jobWareHouse);
		session.setAttribute("job", "job");
	}

	// 判断选择的答案是否与标准答案一致,并更改数据库
	public void result(HttpServletRequest request, HttpServletResponse response) {
		UserStatusBean status = null;
		// 取得参数
		
		int id = getParameterInt("id");
		int rs1 = getParameterInt("rs");
		// 通过参数得到一条题目记录
		JobWareHouseBean jobWareHouse = jobService.getJobWareHouse("id=" + id);
		// 判断是否得到题目
		if (jobWareHouse != null) {
			// 判断标准答案是否和用户选择答案一致,如果一致加分
			if (jobWareHouse.getResult() == rs1) {
				// 取得用户状态
				status = this.getUserStatus(loginUser.getId());
				// 更新用户状态
				// zhul_modify_us 2006-08-14 start
				boolean flag = UserInfoUtil.updateUserCash(loginUser.getId(), winPoint, UserCashAction.GAME, "用户答对题给用户加100乐币");
				// zhul_modify_us 2006-08-14 end

				// 判断是否更新成功
				if (flag) {
					// loginUser.getUs().setGamePoint(
					// loginUser.getUs().getGamePoint() + winPoint);

					// add by zhangyi 2006-07-24 for stat user money history
					// start
					MoneyAction.addMoneyFlowRecord(Constants.JOB, winPoint,
							Constants.PLUS, loginUser.getId());
					// add by zhangyi 2006-07-24 for stat user money history end

					request.setAttribute("success", "success");
				} else {
					try {
						//response
						//		.sendRedirect(response
						//				.encodeURL("/job/question.jsp"));
						BaseAction.sendRedirect("/job/question.jsp", response);
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 断标准答案是否和用户选择答案一致,如果不一致扣分
			} else if (jobWareHouse.getResult() != rs1) {
				// 取得用户状态
				status = this.getUserStatus(loginUser.getId());
				// 更新用户状态
				// zhul_modify_us 2006-08-14 start
				boolean flag = UserInfoUtil.updateUserCash(loginUser.getId(), -losePoint, UserCashAction.GAME, "用户答错题用户扣10乐币");
				// zhul_modify_us 2006-08-14 end
				// 判断是否更新成功
				if (flag) {
					// loginUser.getUs().setGamePoint(
					// loginUser.getUs().getGamePoint() -losePoint);

					// add by zhangyi 2006-07-24 for stat user money history
					// start
					MoneyAction.addMoneyFlowRecord(Constants.JOB, losePoint,
							Constants.SUBTRATION, loginUser.getId());
					// add by zhangyi 2006-07-24 for stat user money history end

					request.setAttribute("error", "error");
				}
			} else {
				try {
					//response
					//		.sendRedirect(response
					//				.encodeURL("/job/question.jsp"));
					BaseAction.sendRedirect("/job/question.jsp", response);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		// 没有得到题目
		else {
			try {
				//response.sendRedirect(response
				//		.encodeURL("/job/question.jsp"));
				BaseAction.sendRedirect("/job/question.jsp", response);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 判断登陆用户在user_status表中的状态
	public UserStatusBean getUserStatus(int userId) {
		// UserStatusBean status = service.getUserStatus("user_id = " + userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			service.addUserStatus(status);
			// add by zhangyi 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			// add by zhangyi 2006-07-24 for stat user money history end
			return status;
		}
	}
}
