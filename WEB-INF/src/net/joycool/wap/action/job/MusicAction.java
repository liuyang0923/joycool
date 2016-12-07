package net.joycool.wap.action.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.job.JobMusicBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class MusicAction extends CustomAction{
	UserBean loginUser;

	int point = 100;

	static IJobService jobService = ServiceFactory.createJobService();
	static IUserService service = ServiceFactory.createUserService();

	HttpSession session = null;

	public MusicAction(HttpServletRequest request) {
		loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		session = request.getSession();
	}

	// 随机取得一道问答题目
	public void question(HttpServletRequest request) {
		String types = (String) request.getParameter("types");
		if (types == null) {
			types = "mid";
		}
		JobMusicBean jobMusic = jobService.getJobMusicRand("types='" + StringUtil.toSql(types)
				+ "'");
		request.setAttribute("jobMusic", jobMusic);
		session.setAttribute("music", "music");
	}

	// 判断选择的答案是否与标准答案一致,并更改数据库
	public void result(HttpServletRequest request, HttpServletResponse response) {
		UserStatusBean status = null;
		// 取得参数
		int id = getParameterInt("id");
		int rs1 = getParameterInt("rs");
		// 通过参数得到一条题目记录
		JobMusicBean jobMusic = jobService.getJobMusic("id=" + id);
		// 判断是否得到题目
		if (jobMusic != null) {
			// 判断标准答案是否和用户选择答案一致,如果一致加分
			if (jobMusic.getResult() == rs1) {
				// 取得用户状态
				status = this.getUserStatus(loginUser.getId());
				// 更新用户状态
				// boolean flag = jobService.updateUserStatus("game_point="+
				// (status.getGamePoint()+point), "user_id=" +
				// loginUser.getId());
				// zhul_modify_us 2006-08-14 start
				boolean flag = UserInfoUtil.updateUserStatus("game_point=game_point+"
						+  point, "user_id="
						+ loginUser.getId(), loginUser.getId(), UserCashAction.GAME, "答对题目加分"+point);
				// zhul_modify_us 2006-08-14 end

				// 判断是否更新成功
				if (flag) {
					// add by zhangyi 2006-07-24 for stat user money history
					// start
					MoneyAction.addMoneyFlowRecord(Constants.MUSIC, point,
							Constants.PLUS, loginUser.getId());
					// add by zhangyi 2006-07-24 for stat user money history end
					request.setAttribute("success", "success");
				} else {
					try {
						//response
						//		.sendRedirect(response
						//				.encodeURL("/job/mindex.jsp"));
						BaseAction.sendRedirect("/job/mindex.jsp", response);
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 断标准答案是否和用户选择答案一致,如果不一致扣分
			} else if (jobMusic.getResult() != rs1) {
				/*
				 * //取得用户状态 status = this.getUserStatus(loginUser.getId());
				 * //更新用户状态 boolean flag =
				 * jobService.updateUserStatus("game_point="+
				 * (status.getGamePoint()-point), "user_id=" +
				 * loginUser.getId()); //判断是否更新成功 if (flag) {
				 */
				request.setAttribute("error", "error");
				/*
				 * } else { try {
				 * response.sendRedirect(("/job/mindex.jsp"));
				 * return; } catch (Exception e) { e.printStackTrace(); } }
				 */
			}
		}
		// 没有得到题目
		else {
			try {
				//response.sendRedirect(response
				//		.encodeURL("/job/mindex.jsp"));
				BaseAction.sendRedirect("/job/mindex.jsp", response);
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
