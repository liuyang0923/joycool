/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamehall;

import java.awt.image.BufferedImage;
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
import net.joycool.wap.bean.wgamehall.HallBean;
import net.joycool.wap.bean.wgamehall.OthelloDataBean;
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
public class OthelloAction extends HallBaseAction {

	public int ONLINE_NUMBER_PER_PAGE = 5;

	public int NUMBER_PER_PAGE = 5;

	public int TIME_OUT = 180;

	public OthelloAction(HttpServletRequest request) {
		super(request);
	}

	public OthelloAction() {
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
		String condition = "game_id = " + WGameBean.HALL_OTHELLO;
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
		 * totalOnlinePageCount - 1; } if (pageIndex1 < 0) { pageIndex1 = 0; }
		 * 
		 *  // 李北金_2006-06-20_查询优化_start // condition = "join jc_online_user on //
		 * user_info.id=jc_online_user.user_id where user_info.id != " // +
		 * loginUser.getId(); // zhul_2006-09-07 如果是非登录用户也允许用户进入游戏首页 start if
		 * (loginUser != null) { condition = "join jc_online_user on
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
		// liuyi 2006-12-02 程序优化 start
		// ArrayList onlineUser=OnlineUtil.getAllOnlineUser();
		List onlineUserIds = UserInfoUtil.getOnlineUserOrderByPKFromCache();
		// zhul 2006-10-20 按PK度进行排序	

		// 分页
		int totalOnlineCount = onlineUserIds.size();
		int totalOnlinePageCount = (totalOnlineCount + ONLINE_NUMBER_PER_PAGE - 1)
				/ ONLINE_NUMBER_PER_PAGE;
		int pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		if (pageIndex1 > totalOnlinePageCount - 1) {
			pageIndex1 = totalOnlinePageCount - 1;
		}
		if (pageIndex1 < 0) {
			pageIndex1 = 0;
		}

		int start = pageIndex1 * ONLINE_NUMBER_PER_PAGE;
		int end = pageIndex1 * ONLINE_NUMBER_PER_PAGE + ONLINE_NUMBER_PER_PAGE;
		// 玩家列表
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

		/*
		 * ArrayList array=new ArrayList(); for(int i=0;i<onlineUser.size();i++){
		 * Integer userId = new
		 * Integer(Integer.parseInt((String)onlineUser.get(i)));
		 * if(loginUser!=null&&loginUser.getId()==userId.intValue()) continue;
		 * array.add(userId); } TreeSet ts=new TreeSet(); ts.addAll(array);
		 * Object [] onlineU= ts.toArray(); //分页处理 int totalOnlineCount
		 * =onlineU.length; int totalOnlinePageCount = (totalOnlineCount +
		 * ONLINE_NUMBER_PER_PAGE - 1) / ONLINE_NUMBER_PER_PAGE; int pageIndex1 =
		 * StringUtil.toInt(request.getParameter("pageIndex1")); if (pageIndex1 >
		 * totalOnlinePageCount - 1) { pageIndex1 = totalOnlinePageCount - 1; }
		 * if (pageIndex1 < 0) { pageIndex1 = 0; }
		 * 
		 * int start=onlineU.length-1-pageIndex1*ONLINE_NUMBER_PER_PAGE; int end =
		 * onlineU.length-1-pageIndex1*ONLINE_NUMBER_PER_PAGE-ONLINE_NUMBER_PER_PAGE;
		 * 
		 * Vector userList = new Vector(); for(int i=start;i>end;i--){ if(i<0)
		 * break; Integer idKey=(Integer)onlineU[i]; UserBean
		 * user=UserInfoUtil.getUser(idKey.intValue()); if(user!=null)
		 * userList.add(user); }
		 */
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
					+ HallBean.OTHELLO;
			WGameHallBean currentOthello = hallService.getWGameHall(condition);
			if (currentOthello != null) {
				request.setAttribute("currentOthello", currentOthello);
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
		WGameHallBean othello = null;

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
				+ " and game_id = " + HallBean.OTHELLO;
		othello = hallService.getWGameHall(condition);
		if (othello != null) {
			// 取得赌局数据
			OthelloDataBean data = GameDataAction.getOthelloData(othello
					.getUniqueMark());
			// 数据出错
			if (data == null) {
				request.setAttribute("result", "failure");
				return;
			}

			// 更新赌局状态为游戏中
			String set = "mark = " + HallBean.GS_PLAYING;
			hallService.updateWGameHall(set, condition);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.updateImage();
				data.setActionType(OthelloDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
			}
			// liuyi 2006-12-01 程序优化 end

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getAcceptInviteNoticeTitle(loginUser.getNickName(),
					"黑白棋"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(othello.getLeftUserId());
			notice.setHideUrl("/wgamehall/othello/playing.jsp");
			notice.setLink("/wgamehall/othello/playing.jsp?gameId="
					+ othello.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			request.setAttribute("result", "success");
			request.setAttribute("othello", othello);
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
				+ HallBean.GS_END + " and game_id = " + HallBean.OTHELLO;
		if (hallService.getWGameHallCount(condition) > 0) {
			request.setAttribute("tip", "此用户已在游戏中");
			request.setAttribute("result", "failure");
			return;
		}
		// 已经邀请了5人
		condition = "left_user_id = " + loginUser.getId() + " and mark = "
				+ HallBean.GS_WAITING + " and game_id = " + HallBean.OTHELLO;
		if (hallService.getWGameHallCount(condition) >= WGameBean.MAX_INVITE_COUNT) {
			request.setAttribute("tip", "你已经邀请了" + WGameBean.MAX_INVITE_COUNT
					+ "人,不能再邀请");
			request.setAttribute("result", "failure");
			return;
		}

		String uniqueMark = StringUtil.getUniqueStr();

		// 增加一个赌局
		othello = new WGameHallBean();
		othello.setGameId(HallBean.OTHELLO);
		othello.setLeftNickname(loginUser.getNickName());
		othello.setLeftUserId(loginUser.getId());
		othello.setLeftSessionId(request.getSession().getId());
		othello.setLeftStatus(HallBean.PS_READY);
		othello.setRightNickname(enemy.getNickName());
		othello.setRightUserId(enemy.getId());
		othello.setUniqueMark(uniqueMark);
		othello.setMark(HallBean.GS_WAITING);
		hallService.addWGameHall(othello);

		condition = "left_user_id = " + loginUser.getId()
				+ " and right_user_id = " + enemy.getId() + " and game_id = "
				+ HallBean.OTHELLO + " and mark = " + HallBean.GS_WAITING;
		othello = hallService.getWGameHall(condition);

		// 保存游戏数据
		OthelloDataBean data = new OthelloDataBean();
		data.setLastActiveTime(DateUtil.getCurrentTime());
		data.setLastActiveUserId(loginUser.getId());
		data.setUniqueMark(uniqueMark);
		data.setActionType(OthelloDataBean.ACTION_INVITE);
		GameDataAction.putOthelloData(uniqueMark, data);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getInviteNoticeTitle(loginUser.getNickName(), "黑白棋"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(othello.getRightUserId());
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/viewInvitation.jsp?gameId="
				+ othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		request.setAttribute("result", "success");
		request.setAttribute("othello", othello);
	}

	/**
	 * 取消邀请
	 * 
	 * @param request
	 */
	public void cancelInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean othello = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		if (othello == null) {
			return;
		} else if (othello.getMark() != HallBean.GS_WAITING) {
			return;
		} else if (othello.getLeftUserId() != loginUser.getId()) {
			return;
		} else {
			// 删除黑白棋
			hallService.deleteWGameHall(condition);
			// 删除游戏数据
			GameDataAction.removeOthelloData(othello.getUniqueMark());
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
		WGameHallBean othello = hallService.getWGameHall(condition);
		if (othello == null) {
			return;
		}

		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		String result = null, tip = null, active = null;
		long currentTime = DateUtil.getCurrentTime();
		int seconds = 0;

		if (othello.getMark() != HallBean.GS_END) {
			if (data == null) {
				return;
			}
			seconds = (int) ((currentTime - data.getLastActiveTime()) / 1000);
		}

		String timeoutNickname = null;
		if (loginUser.getId() == othello.getLeftUserId()) {
			timeoutNickname = othello.getRightNickname();
		} else {
			timeoutNickname = othello.getLeftNickname();
		}

		// 赌局状态
		/**
		 * 邀请中
		 */
		if (othello.getMark() == HallBean.GS_WAITING) {
			// 超时了
			if (seconds >= TIME_OUT) {
				// 增加100个乐币
				condition = "user_id = " + loginUser.getId();
				// fanys2006-08-11
				UserInfoUtil.updateUserCash(loginUser.getId(), 100, UserCashAction.GAME,
						"Othello--加100乐币");
				// mcq_1_更改超时后玩家的乐币数
				updateTimeOut();
				// mcq_end
				// 更新游戏状态
				condition = "id = " + othello.getId();
				hallService.updateWGameHall("mark = " + HallBean.GS_END
						+ ", result = '" + StringUtil.toSql(timeoutNickname) + "超时没接受邀请'",
						condition);
				// 删除本局数据
				GameDataAction.removeOthelloData(othello.getUniqueMark());
				othello.setResult(timeoutNickname + "超时没接受邀请");
				othello.setMark(HallBean.GS_END);
			} else {
				request.setAttribute("condition", "waiting");
				request.setAttribute("leftSeconds", new Integer(TIME_OUT
						- seconds));
			}
		}
		/**
		 * 游戏中
		 */
		if (othello.getMark() == HallBean.GS_PLAYING) {
			// 上一次是对方操作
			if (data.getLastActiveUserId() != loginUser.getId()) {
				active = "1";
				// 自己超时自动判负
				if (seconds > TIME_OUT) {
					int winUserId = (othello.getLeftUserId() == loginUser
							.getId() ? othello.getRightUserId() : othello
							.getLeftUserId());
					othello.setMark(HallBean.GS_END);
					othello.setWinUserId(winUserId);
					othello.setResult(loginUser.getNickName() + "超时没回应");
					othello.setMark(HallBean.GS_END);
					end(othello, data, winUserId, loginUser.getNickName()
							+ "超时");
				}
				// 对方应邀
				else if (data.getActionType() == OthelloDataBean.ACTION_ACCEPT_INVITATION) {
					result = "acceptInvitation";
					tip = "对方应邀进入游戏<br/>现在轮到您下子";
				}
				// 对方下子
				else if (data.getActionType() == OthelloDataBean.ACTION_CONTINUE) {
					result = "continue";
					tip = "对方下子完毕<br/>现在轮到您下子";
				}
				// 求和
				else if (data.getActionType() == OthelloDataBean.ACTION_SUE_FOR_PEACE) {
					result = "sueForPeace";
					tip = "对方求和!请作决定";
				}
				// 对方拒绝和局
				else if (data.getActionType() == OthelloDataBean.ACTION_DENY_PEACE) {
					result = "denyPeace";
					tip = "对方拒和!请下子";
				}
				// 判断是否可以求和（只能求和一次）
				if (data.getSueForPeaceUserId1() != loginUser.getId()
						&& data.getSueForPeaceUserId2() != loginUser.getId()) {
					request.setAttribute("canSueForPeace", "true");
				}
			}
			// 上一次是自己操作
			else {
				active = "0";
				// 对方超时自动判负
				if (seconds > TIME_OUT) {
					int winUserId = loginUser.getId();
					othello.setMark(HallBean.GS_END);
					othello.setWinUserId(winUserId);
					String gameResult = null;
					if (othello.getLeftUserId() == loginUser.getId()) {
						gameResult = othello.getRightNickname() + "超时";
					} else {
						gameResult = othello.getLeftNickname() + "超时";
					}
					othello.setResult(gameResult);
					othello.setMark(HallBean.GS_END);
					end(othello, data, winUserId, gameResult);
				}
				// 应邀进入
				else if (data.getActionType() == OthelloDataBean.ACTION_ACCEPT_INVITATION) {
					result = "acceptInvitation";
					tip = "您应邀进入游戏<br/>请等待对方下子";
				}
				// 自己下子
				else if (data.getActionType() == OthelloDataBean.ACTION_CONTINUE) {
					result = "continue";
					tip = "下子完毕,等对方下子";
				}
				// 求和
				else if (data.getActionType() == OthelloDataBean.ACTION_SUE_FOR_PEACE) {
					result = "sueForPeace";
					tip = "您求和!请等对方响应";
				}
				// 拒绝和局
				else if (data.getActionType() == OthelloDataBean.ACTION_DENY_PEACE) {
					result = "sueForPeace";
					tip = "您拒和!请等对方下子";
				}
			}
		}
		/**
		 * 游戏已经结束
		 */
		if (othello.getMark() == HallBean.GS_END) {
			if (othello.getWinUserId() == loginUser.getId()) {
				result = "win";
			} else if (othello.getWinUserId() == 0) {
				result = "draw";
			} else {
				result = "lose";
			}
			int inviteUserId = othello.getLeftUserId() == loginUser.getId() ? othello
					.getRightUserId()
					: othello.getLeftUserId();
			request.setAttribute("inviteUserId", new Integer(inviteUserId));
		}

		request.setAttribute("leftSeconds", new Integer(TIME_OUT - seconds));
		request.setAttribute("result", result);
		request.setAttribute("active", active);
		request.setAttribute("tip", tip);
		request.setAttribute("othello", othello);
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
		WGameHallBean othello = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (othello.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		} else {
			// 对手积分和乐币
			// fanys2006-08-11
			UserStatusBean enemyUs = UserInfoUtil.getUserStatus(loginUser
					.getId());
			// UserStatusBean enemyUs = userService.getUserStatus("user_id = "
			// + othello.getLeftUserId());

			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", "success");
			request.setAttribute("othello", othello);
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
		WGameHallBean othello = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 赌局已结束
		else if (othello.getMark() != HallBean.GS_WAITING) {
			return;
		}
		// 正常
		else {
			// 删除赌局
			hallService.deleteWGameHall(condition);

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getDenyInviteNoticeTitle(loginUser.getNickName(),
					"黑白棋"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(othello.getLeftUserId());
			notice.setHideUrl("/wgamehall/othello/playing.jsp");
			notice.setLink("/wgamehall/othello/index.jsp");
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
		WGameHallBean othello = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			request.setAttribute("result", "failure");
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (othello.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
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
					"黑白棋"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(othello.getLeftUserId());
			notice.setHideUrl("/wgamehall/othello/playing.jsp");
			notice.setLink("/wgamehall/othello/playing.jsp?gameId="
					+ othello.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.updateImage();
				data.setActionType(OthelloDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
			}
			// liuyi 2006-12-01 程序优化 end
			request.setAttribute("othello", othello);
		}
	}

	/**
	 * 取得一局游戏的图。
	 * 
	 * @param request
	 * @return
	 */
	public BufferedImage getImage(HttpServletRequest request) {
		WGameHallBean othello = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return null;
		}
		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return null;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());

		if (data == null) {
			return null;
		}
		return data.getImage();
	}

	/**
	 * 下子。
	 * 
	 * @param request
	 * @return
	 */
	public void move(HttpServletRequest request) {
		WGameHallBean othello = null;
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
		position = position.toLowerCase();
		char[] chars = position.toCharArray();
		if (chars.length != 2) {
			return;
		}
		if (chars[0] < 'a' || chars[0] > 'h' || chars[1] < 'a'
				|| chars[1] > 'h') {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		// 左边是黑棋
		int y = 7 - (chars[0] - 'a');
		int x = chars[1] - 'a';
		if (data.getBoard()[x][y] != 0) {
			return;
		}

		boolean res;
		if (loginUser.getId() == othello.getLeftUserId()) {
			res = data.addMove(x, y, -1);
		} else {
			res = data.addMove(x, y, 1);
		}

		int toId = 0;
		if (loginUser.getId() == othello.getLeftUserId()) {
			toId = othello.getRightUserId();
		} else {
			toId = othello.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "黑白棋",
				"下子"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		if (res) {
			// 判断输赢
			if (data.getStatus() != OthelloDataBean.GS_INPLAY) {
				othello.setMark(HallBean.GS_END);
				int win;
				String gameResult = null;
				if (data.getStatus() == OthelloDataBean.GS_DRAW) {
					win = 0;
					gameResult = "双方子一样多";
				} else if (data.getStatus() == OthelloDataBean.GS_BLACKWIN) {
					win = othello.getLeftUserId();
					gameResult = othello.getLeftNickname() + "子多";
				} else {
					win = othello.getRightUserId();
					gameResult = othello.getLeftNickname() + "子多";
				}
				end(othello, data, win, gameResult);
				return;
			}
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.updateImage();
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
				data.setActionType(OthelloDataBean.ACTION_CONTINUE);
			}
			// liuyi 2006-12-01 程序优化 end
		}
	}

	/**
	 * 求和。
	 * 
	 * @param request
	 * @return
	 */
	public void sueForPeace(HttpServletRequest request) {
		WGameHallBean othello = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		// 已经求和
		if (data.getSueForPeaceUserId1() == loginUser.getId()
				|| data.getSueForPeaceUserId2() == loginUser.getId()) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == othello.getLeftUserId()) {
			toId = othello.getRightUserId();
		} else {
			toId = othello.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "黑白棋",
				"求和"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		data.setLastActiveTime(DateUtil.getCurrentTime());
		data.setLastActiveUserId(loginUser.getId());
		data.setActionType(OthelloDataBean.ACTION_SUE_FOR_PEACE);
		if (data.getSueForPeaceUserId1() == 0) {
			data.setSueForPeaceUserId1(loginUser.getId());
		} else {
			data.setSueForPeaceUserId2(loginUser.getId());
		}
	}

	/**
	 * 拒绝和局。
	 * 
	 * @param request
	 * @return
	 */
	public void denyPeace(HttpServletRequest request) {
		WGameHallBean othello = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == othello.getLeftUserId()) {
			toId = othello.getRightUserId();
		} else {
			toId = othello.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "黑白棋",
				"不同意和局"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		data.setLastActiveTime(DateUtil.getCurrentTime());
		data.setLastActiveUserId(loginUser.getId());
		data.setActionType(OthelloDataBean.ACTION_DENY_PEACE);
	}

	/**
	 * 接受和局。
	 * 
	 * @param request
	 * @return
	 */
	public void agreeToPeace(HttpServletRequest request) {
		WGameHallBean othello = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == othello.getLeftUserId()) {
			toId = othello.getRightUserId();
		} else {
			toId = othello.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "黑白棋",
				"同意和局"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		othello.setMark(HallBean.GS_END);
		end(othello, data, 0, "双方同意和局");
	}

	/**
	 * 认输。
	 * 
	 * @param request
	 * @return
	 */
	public void giveUp(HttpServletRequest request) {
		WGameHallBean othello = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		othello = hallService.getWGameHall(condition);
		// 赌局已删除
		if (othello == null) {
			return;
		}
		// 取得赌局数据
		OthelloDataBean data = GameDataAction.getOthelloData(othello
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == othello.getLeftUserId()) {
			toId = othello.getRightUserId();
		} else {
			toId = othello.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "黑白棋",
				"认输"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/othello/playing.jsp");
		notice.setLink("/wgamehall/othello/playing.jsp?gameId=" + othello.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		int winUserId = (othello.getLeftUserId() == loginUser.getId() ? othello
				.getRightUserId() : othello.getLeftUserId());
		othello.setResult(loginUser.getNickName() + "认输");
		othello.setMark(HallBean.GS_END);
		end(othello, data, winUserId, loginUser.getNickName() + "认输");
	}

	/**
	 * 判断输赢。
	 * 
	 * @param points
	 * @param x
	 * @param y
	 * @param point
	 * @return
	 */
	public boolean checkPoints(int[][] points, int x, int y, int point) {
		int i, j;
		int num = 1;

		// 水平方向
		for (i = x - 1; i >= 0; i--) {
			if (points[i][y] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		for (i = x + 1; i <= 7; i++) {
			if (points[i][y] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		num = 1;
		// 垂直方向
		for (i = y - 1; i >= 0; i--) {
			if (points[x][i] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		for (i = y + 1; i <= 7; i++) {
			if (points[x][i] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		num = 1;
		// 右上方向
		for (i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
			if (points[i][j] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		for (i = x + 1, j = y + 1; i <= 14 && j <= 7; i++, j++) {
			if (points[i][j] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		num = 1;
		// 左上方向
		for (i = x - 1, j = y + 1; i >= 0 && j <= 7; i--, j++) {
			if (points[i][j] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}
		for (i = x + 1, j = y - 1; i <= 14 && j >= 0; i++, j--) {
			if (points[i][j] == point) {
				num++;
				if (num == 5) {
					return true;
				}
			} else {
				break;
			}
		}

		return false;
	}

	/**
	 * 结束一局，处理输赢
	 */
	public void end(WGameHallBean othello, OthelloDataBean data, int winUserId,
			String result) {
		othello.setResult(result);
		// 分出胜负
		if (winUserId != 0) {
			int loseUserId = othello.getLeftUserId() == winUserId ? othello
					.getRightUserId() : othello.getLeftUserId();
			// 更新双方积分
			getUserStatus(othello.getLeftUserId());
			getUserStatus(othello.getRightUserId());

			if (result != null && result.indexOf("超时") != -1) {
				// fanys2006-08-11
				UserInfoUtil.updateUserCash(winUserId, 500,
						UserCashAction.GAME, "Othello--加500乐币");
				// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
				updateInfo(winUserId, Constants.RANK_WIN);
				// mcq_end

			} else {
				// fanys2006-08-11
				UserInfoUtil.updateUserCash(winUserId, 5000,
						UserCashAction.GAME, "Othello--加5000乐币");
				// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
				updateInfo(winUserId, Constants.RANK_WIN);
				// mcq_end
				if (othello.getLeftUserId() != winUserId) {
					// mcq_1_更新玩家输了后Session的乐币数和经验值 时间 2006-6-12
					updateInfo(othello.getLeftUserId(), Constants.RANK_LOSE);
					// mcq_end
				} else {
					// mcq_1_更新玩家输了后Session的乐币数和经验值 时间 2006-6-12
					updateInfo(othello.getRightUserId(), Constants.RANK_LOSE);
					// mcq_end
				}
			}
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(winUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_OTHELLO);
			history.setWinCount(1);
			history.setMoney(5000);
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(loseUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_OTHELLO);
			history.setLoseCount(1);
			history.setMoney(0);
			updateHistory(history);
		}
		// 打平
		else {
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(othello.getLeftUserId());
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_OTHELLO);
			history.setDrawCount(1);
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(othello.getRightUserId());
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_OTHELLO);
			history.setDrawCount(1);
			updateHistory(history);
			// mcq_1_更改用户积分
			updateInfo(othello.getLeftUserId(), Constants.RANK_DRAW);
			updateInfo(othello.getRightUserId(), Constants.RANK_DRAW);
			// mcq_end
		}
		// 更新游戏状态
		String set = "win_user_id = " + winUserId + ", mark = "
				+ HallBean.GS_END + ", result='" + result + "'";
		hallService.updateWGameHall(set, "id = " + othello.getId());
		// 删除游戏数据
		GameDataAction.removeOthelloData(othello.getUniqueMark());
	}

	public static void main(String[] args) throws Exception {
		int[][] points = new int[15][15];
		points[1][1] = 1;
		points[1][2] = 2;
		points[1][3] = 1;
		points[1][4] = 1;
		points[2][1] = 2;
		points[3][1] = 1;
		points[4][1] = 2;
		// BufferedImage image = new OthelloAction().getOthelloImage(points);
		// FileOutputStream fos = new FileOutputStream(new File("D:/ttt.wbmp"));
		// ImageIO.write(image, "WBMP", fos);

	}

	/**
	 * @param request
	 */
	public void history(HttpServletRequest request) {
		HistoryBean history = getHistory(loginUser.getId(), WGameBean.GT_HALL,
				WGameBean.HALL_OTHELLO);
		request.setAttribute("history", history);
	}
}