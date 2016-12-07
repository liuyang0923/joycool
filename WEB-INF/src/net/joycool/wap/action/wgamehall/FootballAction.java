/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamehall;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.bean.wgame.WGameBean;
import net.joycool.wap.bean.wgamehall.FootballDataBean;
import net.joycool.wap.bean.wgamehall.HallBean;
import net.joycool.wap.bean.wgamehall.WGameHallBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class FootballAction extends HallBaseAction {
	public int ONLINE_NUMBER_PER_PAGE = 5;

	public int TIME_OUT = 180;

	public int NUMBER_PER_PAGE = 5;

	public FootballAction(HttpServletRequest request) {
		super(request);
	}

	public FootballAction() {
	}

	/**
	 * 首页。
	 * 
	 * @param request
	 * @param response
	 */
	public void dealIndex(HttpServletRequest request,
			HttpServletResponse response) {
		// 取得参数
		// 坐庄页码
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// 玩家页码
		// int pageIndex1 =
		// StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex < 0) {
			pageIndex = 0;
		}
		// if (pageIndex1 < 0) {
		// pageIndex1 = 0;
		// }

		// 取得坐庄列表
		// String orderBy = request.getParameter("orderBy");
		// if (pageIndex == 0) {
		// orderBy = "rand()";
		// }
		// if (orderBy == null) {
		// orderBy = "id";
		// }
		// macq_2006-10-18_更改pk游戏排序规则_start
		String orderBy = "id";
		// macq_2006-10-18_更改pk游戏排序规则_end
		String condition = "game_id = " + WGameBean.HALL_FOOTBALL;
		// 取得总数
		int totalHallCount = hallService.getWGameHallCount(condition);
		int totalHallPageCount = totalHallCount / NUMBER_PER_PAGE;
		if (totalHallCount % NUMBER_PER_PAGE != 0) {
			totalHallPageCount++;
		}
		if (pageIndex > totalHallPageCount - 1) {
			pageIndex = totalHallPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		/**
		 * 取得庄家列表
		 */
		condition += " order by " + orderBy + " desc limit " + pageIndex
				* NUMBER_PER_PAGE + ", " + NUMBER_PER_PAGE;
		Vector hallList = hallService.getWGameHallList(condition);

		/*
		 * // 取得在线玩家列表 orderBy = request.getParameter("orderBy"); if (pageIndex1 ==
		 * 0) { orderBy = "id"; } if (orderBy == null) { orderBy = "id"; }
		 * 
		 * condition = "id > 0"; // 取得总数 int totalOnlineCount =
		 * userService.getOnlineUserCount(condition); int totalOnlinePageCount =
		 * totalOnlineCount / ONLINE_NUMBER_PER_PAGE; if (totalOnlineCount %
		 * ONLINE_NUMBER_PER_PAGE != 0) { totalOnlinePageCount++; } if
		 * (pageIndex1 > totalOnlinePageCount - 1) { pageIndex1 =
		 * totalOnlinePageCount - 1; } if (pageIndex1 < 0) { pageIndex1 = 0; } //
		 * 李北金_2006-06-20_查询优化_start // zhul_2006-09-07 如果是非登录用户也允许用户进入游戏首页
		 * start if (loginUser != null) { condition = "join jc_online_user on
		 * user_info.id=jc_online_user.user_id where user_info.id != " +
		 * loginUser.getId(); } else { condition = "join jc_online_user on
		 * user_info.id=jc_online_user.user_id "; } // zhul_2006-09-07
		 * 如果是非登录用户也允许用户进入游戏首页 end condition += " order by user_info." + orderBy + "
		 * desc limit " + pageIndex1 * ONLINE_NUMBER_PER_PAGE + ", " +
		 * ONLINE_NUMBER_PER_PAGE; // 李北金_2006-06-20_查询优化_end Vector userList =
		 * userService.getUserList(condition);
		 */
		/**
		 * 取得玩家列表
		 */
		// zhul 2006-10-18 优化获取在线用户 start
		// 获取所有在线用户的正序序列
		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
//		liuyi 2006-12-02 程序优化 start
		//ArrayList onlineUser=OnlineUtil.getAllOnlineUser();
		List onlineUserIds = UserInfoUtil.getOnlineUserOrderByPKFromCache();
		//zhul 2006-10-20 按PK度进行排序
		
		//分页
		int totalOnlineCount =onlineUserIds.size();
		int totalOnlinePageCount = (totalOnlineCount + ONLINE_NUMBER_PER_PAGE - 1) / ONLINE_NUMBER_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalOnlinePageCount - 1) {
			pageIndex1 = totalOnlinePageCount - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}

		int start=pageIndex1*ONLINE_NUMBER_PER_PAGE;
		int end = pageIndex1*ONLINE_NUMBER_PER_PAGE+ONLINE_NUMBER_PER_PAGE;
		//玩家列表
		Vector userList = new Vector();
		for (int i = start; i < end; i++) {
			if (i > totalOnlineCount - 1)
				break;
			String userId = (String) onlineUserIds.get(i);
			int id = StringUtil.toInt(userId);
			if (id < 1) {
				continue;
			}
			if (loginUser != null && loginUser.getId() == id) {
				continue;
			}
			UserBean user = UserInfoUtil.getUser(id);
			if (user != null)
				userList.add(user);
		}
		//liuyi 2006-12-02 程序优化 end
		
		// zhul 2006-10-18 优化获取在线用户 end
		Iterator itr = userList.iterator();
		UserBean user = null;
		while (itr.hasNext()) {
			user = (UserBean) itr.next();
			user.setUs(getUserStatus(user.getId()));
		}

		String prefixUrl = "index.jsp?pageIndex1=" + pageIndex1;
		String prefixUrl1 = "index.jsp?pageIndex=" + pageIndex;

		// 取得当前个人战局
		if (loginUser != null) {// zhul_2006-09-07 当前用户已经登录的情况下进行个人战局查询
			condition = "(left_user_id = " + loginUser.getId()
					+ " or right_user_id = " + loginUser.getId()
					+ ") and mark = " + HallBean.GS_PLAYING + " and game_id = "
					+ HallBean.FOOTBALL;
			WGameHallBean currentFootball = hallService.getWGameHall(condition);
			if (currentFootball != null) {
				request.setAttribute("currentFootball", currentFootball);
			}
		}
		request.setAttribute("totalHallCount", new Integer(totalHallCount));
		request.setAttribute("totalHallPageCount", new Integer(
				totalHallPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("hallList", hallList);

		request.setAttribute("totalOnlineCount", new Integer(totalOnlineCount));
		request.setAttribute("totalOnlinePageCount", new Integer(
				totalOnlinePageCount));
		request.setAttribute("pageIndex1", new Integer(pageIndex1));
		request.setAttribute("prefixUrl1", prefixUrl1);
		request.setAttribute("userList", userList);
	}

	public UserStatusBean getUserStatus() {
		return getUserStatus(loginUser.getId());
	}

	/**
	 * 邀请别人进入游戏
	 * 
	 * @param request
	 */
	public void invite(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean football = null;

		int userId = StringUtil.toInt(request.getParameter("userId"));
		// 用户ID不对
		if (userId == -1) {
			request.setAttribute("tip", "此用户不存在");
			request.setAttribute("result", "failure");
			return;
		}
		// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
		UserBean onlineUser = (UserBean) OnlineUtil.getOnlineBean(userId + "");
		if (onlineUser != null) {
			if (onlineUser.noticeMark()) {
				request.setAttribute("tip", "对方设置自己状态为免打扰.不能pk！");
				request.setAttribute("result", "failure");
				return;
			}
		}
		// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_end
		/**
		 * 如果正好对方邀请你进入游戏
		 */
		condition = "left_user_id = " + userId + " and right_user_id = "
				+ loginUser.getId() + " and mark = " + HallBean.GS_WAITING
				+ " and game_id = " + HallBean.FOOTBALL;
		football = hallService.getWGameHall(condition);
		if (football != null) {
			// 取得赌局数据
			FootballDataBean data = GameDataAction.getFootballData(football
					.getUniqueMark());
			// 数据出错
			if (data == null) {
				request.setAttribute("result", "failure");
				return;
			}

			// 更新赌局状态为游戏中
			String set = "mark = " + HallBean.GS_PLAYING;
			hallService.updateWGameHall(set, condition);

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getAcceptInviteNoticeTitle(loginUser.getNickName(),
					"点球决战"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(football.getLeftUserId());
			notice.setHideUrl("/wgamehall/football/playing.jsp");
			notice.setLink("/wgamehall/football/playing.jsp?gameId="
					+ football.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.setActionType(FootballDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
			}
			// liuyi 2006-12-01 程序优化 end
			request.setAttribute("result", "success");
			request.setAttribute("football", football);
			return;
		}
		// 李北金_2006-06-20_查询优化_start
		// condition = "join jc_online_user on
		// user_info.id=jc_online_user.user_id where jc_online_user.user_id = "
		// + userId;
		// 李北金_2006-06-20_查询优化_end
		// UserBean enemy = userService.getUser(condition);
		// zhul 2006-10-17 优化用户信息查询 start
		UserBean enemy = null;
		if (OnlineUtil.isOnline(userId + ""))
			enemy = UserInfoUtil.getUser(userId);
		// zhul 2006-10-17 优化用户信息查询 end
		// 用户不在线
		if (enemy == null) {
			request.setAttribute("tip", "此用户不在线");
			request.setAttribute("result", "failure");
			return;
		}
		// 用户已经被邀请
		condition = "right_user_id = " + enemy.getId() + " and mark != "
				+ HallBean.GS_END + " and game_id = " + HallBean.FOOTBALL;
		if (hallService.getWGameHallCount(condition) > 0) {
			request.setAttribute("tip", "此用户已在游戏中");
			request.setAttribute("result", "failure");
			return;
		}
		// 已经邀请了5人
		condition = "left_user_id = " + loginUser.getId() + " and mark = "
				+ HallBean.GS_WAITING + " and game_id = " + HallBean.FOOTBALL;
		if (hallService.getWGameHallCount(condition) >= WGameBean.MAX_INVITE_COUNT) {
			request.setAttribute("tip", "你已经邀请了" + WGameBean.MAX_INVITE_COUNT
					+ "人,不能再邀请");
			request.setAttribute("result", "failure");
			return;
		}

		String uniqueMark = StringUtil.getUniqueStr();

		// 增加一个赌局
		football = new WGameHallBean();
		football.setGameId(HallBean.FOOTBALL);
		football.setLeftNickname(loginUser.getNickName());
		football.setLeftUserId(loginUser.getId());
		football.setLeftSessionId(request.getSession().getId());
		football.setLeftStatus(HallBean.PS_READY);
		football.setRightNickname(enemy.getNickName());
		football.setRightUserId(enemy.getId());
		football.setUniqueMark(uniqueMark);
		football.setMark(HallBean.GS_WAITING);
		hallService.addWGameHall(football);

		condition = "left_user_id = " + loginUser.getId()
				+ " and right_user_id = " + enemy.getId() + " and game_id = "
				+ HallBean.FOOTBALL + " and mark = " + HallBean.GS_WAITING;
		football = hallService.getWGameHall(condition);

		// 保存游戏数据
		FootballDataBean data = new FootballDataBean();
		data.setLastActiveTime(DateUtil.getCurrentTime());
		data.setLastActiveUserId(loginUser.getId());
		data.setUniqueMark(uniqueMark);
		data.setActionType(FootballDataBean.ACTION_INVITE);
		GameDataAction.putFootballData(uniqueMark, data);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getInviteNoticeTitle(loginUser.getNickName(), "点球决战"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(football.getRightUserId());
		notice.setHideUrl("/wgamehall/football/playing.jsp");
		notice.setLink("/wgamehall/football/viewInvitation.jsp?gameId="
				+ football.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		request.setAttribute("result", "success");
		request.setAttribute("football", football);
	}

	/**
	 * 取消邀请
	 * 
	 * @param request
	 */
	public void cancelInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean football = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		if (football == null) {
			return;
		} else if (football.getMark() != HallBean.GS_WAITING) {
			return;
		} else if (football.getLeftUserId() != loginUser.getId()) {
			return;
		} else {
			// 删除点球决战
			hallService.deleteWGameHall(condition);
			// 删除游戏数据
			GameDataAction.removeFootballData(football.getUniqueMark());
		}
	}

	/**
	 * 游戏中。
	 * 
	 * @param request
	 */
	public void playing(HttpServletRequest request) {
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		if (gameId == -1) {
			return;
		}

		// 取得赌局
		String condition = "id = " + gameId;
		WGameHallBean football = hallService.getWGameHall(condition);
		if (football == null) {
			return;
		}

		// 取得赌局数据
		FootballDataBean data = GameDataAction.getFootballData(football
				.getUniqueMark());
		String result = null, tip = null, active = null;
		long currentTime = DateUtil.getCurrentTime();
		int seconds = 0;

		if (football.getMark() != HallBean.GS_END) {
			if (data == null) {
				return;
			}
			seconds = (int) ((currentTime - data.getLastActiveTime()) / 1000);
		}

		String timeoutNickname = null;
		if (loginUser.getId() == football.getLeftUserId()) {
			timeoutNickname = football.getRightNickname();
		} else {
			timeoutNickname = football.getLeftNickname();
		}

		// 赌局状态
		/**
		 * 邀请中
		 */
		if (football.getMark() == HallBean.GS_WAITING) {
			// 超时了
			if (seconds >= TIME_OUT) {
				UserInfoUtil.updateUserCash(loginUser.getId(), 100, UserCashAction.GAME,
						"wgamehall--足球--加100乐币");
				// mcq_1_更改超时后玩家的乐币数
				updateTimeOut();
				// mcq_end

				// 更新游戏状态
				condition = "id = " + football.getId();
				hallService.updateWGameHall("mark = " + HallBean.GS_END
						+ ", result = '" + StringUtil.toSql(timeoutNickname) + "超时没接受邀请'",
						condition);
				// 删除本局数据
				GameDataAction.removeFootballData(football.getUniqueMark());
				football.setResult(timeoutNickname + "超时没接受邀请");
				football.setMark(HallBean.GS_END);
			} else {
				request.setAttribute("condition", "waiting");
				request.setAttribute("leftSeconds", new Integer(TIME_OUT
						- seconds));
			}
		}

		/**
		 * active为1表示轮到自己操作，为0表示轮到对方操作。 active为1时：result表示自己应该做的操作。
		 * active为0时：result表示对方应该做的操作。
		 */
		/**
		 * 游戏中
		 */
		if (football.getMark() == HallBean.GS_PLAYING) {
			int currentRound = (data.getLeftRound() < data.getRightRound() ? data
					.getLeftRound()
					: data.getRightRound()) + 1;
			String currentSit = null;
			// 当前战况
			if (loginUser.getId() == football.getLeftUserId()) {
				currentSit = data.getLeftWinCount() + ":"
						+ data.getRightWinCount();
			} else {
				currentSit = data.getRightWinCount() + ":"
						+ data.getLeftWinCount();
			}
			request.setAttribute("currentRound", new Integer(currentRound));
			request.setAttribute("currentSit", currentSit);

			// 上一次是对方操作
			if (data.getLastActiveUserId() != loginUser.getId()) {
				// 超时了
				if (seconds >= TIME_OUT) {
					int winUserId = (football.getLeftUserId() == loginUser
							.getId() ? football.getRightUserId() : football
							.getLeftUserId());
					football.setMark(HallBean.GS_END);
					football.setWinUserId(winUserId);
					football.setResult(loginUser.getNickName() + "超时没回应");
					football.setMark(HallBean.GS_END);
					end(football, data, winUserId, loginUser.getNickName()
							+ "超时");
				}
				// 对方应邀
				else if (data.getActionType() == FootballDataBean.ACTION_ACCEPT_INVITATION) {
					active = "1";
					result = "kick";
					tip = "对方应邀进入游戏<br/>现在轮到您射门";
				}
				// 对方射门
				else if (data.getActionType() == FootballDataBean.ACTION_KICK) {
					active = "1";
					result = "save";
					if (data.getHistoryKick() != null) {
						if (data.getHistoryKick() != null
								&& data.getHistoryKick().equals(
										data.getHistorySave())) {
							tip = "上轮对方扑对方向<br/>对方射门,请您扑救";
						} else {
							tip = "上轮对方扑错方向<br/>对方射门,请您扑救";
						}
					} else {
						tip = "对方射门,请您扑救";
					}
				}
				// 对方守门
				else if (data.getActionType() == FootballDataBean.ACTION_SAVE) {
					active = "0";
					result = "kick";
					if (data.getLastKick() != null
							&& data.getLastKick().equals(data.getLastSave())) {
						tip = "上轮对方扑对方向<br/>现在轮到对方射门";
					} else {
						tip = "上轮对方扑错方向<br/>现在轮到对方射门";
					}
				}
			}
			// 上一次是自己操作
			else {
				// 对方超时自动判负
				if (seconds > TIME_OUT) {
					int winUserId = loginUser.getId();
					football.setMark(HallBean.GS_END);
					football.setWinUserId(winUserId);
					String gameResult = null;
					if (football.getLeftUserId() == loginUser.getId()) {
						gameResult = football.getRightNickname() + "超时";
					} else {
						gameResult = football.getLeftNickname() + "超时";
					}
					football.setResult(gameResult);
					football.setMark(HallBean.GS_END);
					end(football, data, winUserId, gameResult);
				}
				// 应邀进入
				else if (data.getActionType() == FootballDataBean.ACTION_ACCEPT_INVITATION) {
					active = "0";
					result = "kick";
					tip = "您应邀进入游戏<br/>请等待对方射门";
				}
				// 自己射门
				else if (data.getActionType() == FootballDataBean.ACTION_KICK) {
					active = "0";
					result = "save";
					tip = "您摆腿大力抽射<br/>请等待对方扑救";
				}
				// 自己守门
				else if (data.getActionType() == FootballDataBean.ACTION_SAVE) {
					active = "1";
					result = "kick";
					if (data.getLastKick() != null
							&& data.getLastKick().equals(data.getLastSave())) {
						tip = "上轮您扑对方向<br/>现在轮到您射门";
					} else {
						tip = "上轮您扑错方向<br/>现在轮到您射门";
					}
				}
			}
		}
		/**
		 * 游戏已经结束
		 */
		if (football.getMark() == HallBean.GS_END) {
			if (football.getWinUserId() == loginUser.getId()) {
				result = "win";
			} else if (football.getWinUserId() == 0) {
				result = "draw";
			} else {
				result = "lose";
			}
			int inviteUserId = football.getLeftUserId() == loginUser.getId() ? football
					.getRightUserId()
					: football.getLeftUserId();
			request.setAttribute("inviteUserId", new Integer(inviteUserId));
		}

		request.setAttribute("leftSeconds", new Integer(TIME_OUT - seconds));
		request.setAttribute("result", result);
		request.setAttribute("active", active);
		request.setAttribute("tip", tip);
		request.setAttribute("football", football);
		request.setAttribute("data", data);
	}

	/**
	 * 查看邀请
	 * 
	 * @param request
	 */
	public void viewInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean football = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (football.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		} else {
			// 对手积分和乐币
			// fanys2006-08-11
			// fanys2006-08-11
			UserStatusBean enemyUs = UserInfoUtil.getUserStatus(football
					.getLeftUserId());
			// UserStatusBean enemyUs = userService.getUserStatus("user_id = "
			// + football.getLeftUserId());

			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", "success");
			request.setAttribute("football", football);
		}
	}

	/**
	 * 拒绝邀请
	 * 
	 * @param request
	 */
	public void denyInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean football = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			return;
		}
		// 赌局已结束
		else if (football.getMark() != HallBean.GS_WAITING) {
			return;
		}
		// 正常
		else {
			// 删除赌局
			hallService.deleteWGameHall(condition);

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getDenyInviteNoticeTitle(loginUser.getNickName(),
					"点球决战"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(football.getLeftUserId());
			notice.setHideUrl("/wgamehall/football/playing.jsp");
			notice.setLink("/wgamehall/football/index.jsp");
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);
		}
	}

	/**
	 * 接受邀请
	 * 
	 * @param request
	 */
	public void acceptInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean football = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			request.setAttribute("result", "failure");
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (football.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		}
		// 取得赌局数据
		FootballDataBean data = GameDataAction.getFootballData(football
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 正常
		else {
			// 更新赌局状态为游戏中
			String set = "mark = " + HallBean.GS_PLAYING
					+ ", right_session_id = '" + request.getSession().getId()
					+ "'";
			hallService.updateWGameHall(set, condition);

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getAcceptInviteNoticeTitle(loginUser.getNickName(),
					"点球决战"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(football.getLeftUserId());
			notice.setHideUrl("/wgamehall/football/playing.jsp");
			notice.setLink("/wgamehall/football/playing.jsp?gameId="
					+ football.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.setActionType(FootballDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
			}
			// liuyi 2006-12-01 程序优化 end
			request.setAttribute("football", football);
		}
	}

	/**
	 * 射门。
	 * 
	 * @param request
	 * @return
	 */
	public void kick(HttpServletRequest request) {
		WGameHallBean football = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}
		String position = request.getParameter("position");
		if (position == null) {
			return;
		}
		if (!position.equals("l") && !position.equals("m")
				&& !position.equals("r")) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			return;
		}
		// 取得赌局数据
		FootballDataBean data = GameDataAction.getFootballData(football
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}
		// liuyi 2006-12-01 程序优化 start
		// synchronized (data)
		{
			data.setActionType(FootballDataBean.ACTION_KICK);
			data.setLastActiveTime(DateUtil.getCurrentTime());
			data.setLastActiveUserId(loginUser.getId());
			data.setLastKick(position);
		}
		// liuyi 2006-12-01 程序优化 end
		int toId = 0;
		if (loginUser.getId() == football.getLeftUserId()) {
			toId = football.getRightUserId();
		} else {
			toId = football.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "点球决战",
				"射门"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/football/playing.jsp");
		notice.setLink("/wgamehall/football/playing.jsp?gameId=" + football.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);
	}

	/**
	 * 守门。
	 * 
	 * @param request
	 * @return
	 */
	public void save(HttpServletRequest request) {
		WGameHallBean football = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}
		String position = request.getParameter("position");
		if (position == null) {
			return;
		}
		if (!position.equals("l") && !position.equals("m")
				&& !position.equals("r")) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			return;
		}
		// 取得赌局数据
		FootballDataBean data = GameDataAction.getFootballData(football
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}
		// liuyi 2006-12-01 程序优化 start
		// synchronized (data)
		{
			data.setActionType(FootballDataBean.ACTION_SAVE);
			data.setLastActiveTime(DateUtil.getCurrentTime());
			data.setLastActiveUserId(loginUser.getId());
			data.setLastSave(position);
			// 加到历史中
			data.setHistoryKick(data.getLastKick());
			data.setHistorySave(data.getLastSave());
			// 增加轮数
			if (loginUser.getId() == football.getLeftUserId()) {
				data.setRightRound(data.getRightRound() + 1);
			} else {
				data.setLeftRound(data.getLeftRound() + 1);
			}
			// 没扑对
			if (!position.equals(data.getLastKick())) {
				if (football.getLeftUserId() == loginUser.getId()) {
					data.getRightResults()[data.getRightRound() - 1] = FootballDataBean.GOAL;
					data.setRightWinCount(data.getRightWinCount() + 1);
				} else {
					data.getLeftResults()[data.getLeftRound() - 1] = FootballDataBean.GOAL;
					data.setLeftWinCount(data.getLeftWinCount() + 1);
				}
			}
			// 扑对了
			else {
				if (football.getLeftUserId() == loginUser.getId()) {
					data.getRightResults()[data.getRightRound() - 1] = FootballDataBean.SAVED;
				} else {
					data.getLeftResults()[data.getLeftRound() - 1] = FootballDataBean.SAVED;
				}
			}
		}
		// liuyi 2006-12-01 程序优化 end

		// 判断输赢
		int winUserId = checkResult(data, football);
		int currentRound = data.getLeftRound() > data.getRightRound() ? data
				.getLeftRound() : data.getRightRound();
		if (winUserId != 0) {
			end(football, data, winUserId, "踢了" + currentRound + "轮,比分"
					+ data.getLeftWinCount() + ":" + data.getRightWinCount());
		}
		// 打平
		if (data.getLeftRound() == data.getRightRound()
				&& data.getLeftRound() == 10) {
			end(football, data, winUserId, "踢了十轮还没分出胜负");
		}

		int toId = 0;
		if (loginUser.getId() == football.getLeftUserId()) {
			toId = football.getRightUserId();
		} else {
			toId = football.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "点球决战",
				"扑救"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/football/playing.jsp");
		notice.setLink("/wgamehall/football/playing.jsp?gameId=" + football.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);
	}

	/**
	 * 认输。
	 * 
	 * @param request
	 * @return
	 */
	public void giveUp(HttpServletRequest request) {
		WGameHallBean football = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		football = hallService.getWGameHall(condition);
		// 赌局已删除
		if (football == null) {
			return;
		}
		// 取得赌局数据
		FootballDataBean data = GameDataAction.getFootballData(football
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == football.getLeftUserId()) {
			toId = football.getRightUserId();
		} else {
			toId = football.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "点球决战",
				"认输"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/football/playing.jsp");
		notice.setLink("/wgamehall/football/playing.jsp?gameId=" + football.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		int winUserId = (football.getLeftUserId() == loginUser.getId() ? football
				.getRightUserId()
				: football.getLeftUserId());
		football.setMark(HallBean.GS_END);
		end(football, data, winUserId, loginUser.getNickName() + "认输");
	}

	/**
	 * 判断输赢。
	 * 
	 * @param data
	 * @param football
	 * @return
	 */
	public int checkResult(FootballDataBean data, WGameHallBean football) {
		if (data.getLeftRound() <= 5 && data.getRightRound() <= 5) {
			if (data.getLeftWinCount() - data.getRightWinCount() > 5 - data
					.getRightRound()) {
				return football.getLeftUserId();
			} else if (data.getRightWinCount() - data.getLeftWinCount() > 5 - data
					.getLeftRound()) {
				return football.getRightUserId();
			}
		} else {
			if (data.getLeftRound() == data.getRightRound()) {
				if (data.getLeftWinCount() > data.getRightWinCount()) {
					return football.getLeftUserId();
				} else if (data.getRightWinCount() > data.getLeftWinCount()) {
					return football.getRightUserId();
				}
			}
		}

		return 0;
	}

	/**
	 * 结束一局，处理输赢
	 */
	public void end(WGameHallBean football, FootballDataBean data,
			int winUserId, String result) {
		football.setResult(result);
		// 分出胜负
		if (winUserId != 0) {
			int loseUserId = football.getLeftUserId() == winUserId ? football
					.getRightUserId() : football.getLeftUserId();
			// 更新双方积分
			getUserStatus(football.getLeftUserId());
			getUserStatus(football.getRightUserId());

			if (result != null && result.indexOf("超时") != -1) {
				// fanys2006-08-11
				UserInfoUtil.updateUserCash(winUserId, 500,
						UserCashAction.GAME, "wgamehall--足球--加500乐币");
				// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
				updateInfo(winUserId, Constants.RANK_WIN);
				// mcq_end
			} else {
				UserInfoUtil.updateUserCash(winUserId, 5000,
						UserCashAction.GAME, "wgamehall-足球--加5000乐币");
				// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
				updateInfo(winUserId, Constants.RANK_WIN);
				// mcq_end
				if (football.getLeftUserId() != winUserId) {
					// mcq_1_更新玩家输了后Session的乐币数和经验值 时间 2006-6-12
					updateInfo(football.getLeftUserId(), Constants.RANK_LOSE);
					// mcq_end
				} else {
					// mcq_1_更新玩家输了后Session的乐币数和经验值 时间 2006-6-12
					updateInfo(football.getRightUserId(), Constants.RANK_LOSE);
					// mcq_end
				}
			}
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(winUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_FOOTBALL);
			history.setWinCount(1);
			history.setMoney(5000);
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(loseUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_FOOTBALL);
			history.setLoseCount(1);
			history.setMoney(0);
			updateHistory(history);
		}
		// 打平
		else {
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(football.getLeftUserId());
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_FOOTBALL);
			history.setDrawCount(1);
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(football.getRightUserId());
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_FOOTBALL);
			history.setDrawCount(1);
			updateHistory(history);
			// mcq_1_更改用户积分
			updateInfo(football.getLeftUserId(), Constants.RANK_DRAW);
			updateInfo(football.getRightUserId(), Constants.RANK_DRAW);
			// mcq_end
		}
		// 更新游戏状态
		String set = "win_user_id = " + winUserId + ", mark = "
				+ HallBean.GS_END + ", result = '" + result + "'";
		hallService.updateWGameHall(set, "id = " + football.getId());
		// 删除游戏数据
		GameDataAction.removeFootballData(football.getUniqueMark());
	}

	public static void main(String[] args) throws Exception {
	}
}