/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamehall;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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
import net.joycool.wap.bean.wgamehall.JinhuaDataBean;
import net.joycool.wap.bean.wgamehall.KSGameBean;
import net.joycool.wap.bean.wgamehall.WGameHallBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class JinhuaAction extends HallBaseAction {
	public int ONLINE_NUMBER_PER_PAGE = 5;

	public int TIME_OUT = 180;

	public int NUMBER_PER_PAGE = 5;
	
	//快速开始游戏锁
	public static byte[] lock = new byte[0];
	
	public static int KS_TIMEOVER = 60 * 1000 ;
	
	//快速开始游戏列表容器
	public static LinkedList ksGameList= new LinkedList();
	
	public JinhuaAction(HttpServletRequest request) {
		super(request);
	}

	public JinhuaAction() {
	}
	
	/**
	 *  
	 * @author macq
	 * @explain：游戏快速开始
	 * @datetime:2007-6-4 2:25:16
	 * @param request
	 * @param response
	 * @return void
	 */
	public void ksGame(HttpServletRequest request) {
		//获取赌注类型
		int type = StringUtil.toInt(request.getParameter("type"));
		if(type<0 ||type>5){
			type=0;
		}
		KSGameBean ks = new KSGameBean();
		ks.setUserId(loginUser.getId());
		ks.setType(type);
		ks.setCreateDatetime(System.currentTimeMillis());
		//判断用户是否已经快速开始游戏
		if(ks.checkksGame()){
			request.setAttribute("tip", "您已选择快速开始，60秒之内请等待系统自动帮你找玩家。");
			request.setAttribute("result", "ksWait");
			request.setAttribute("type",type+"");
			return;
		}
		//判断用户是否在游戏中
		String condition = "(left_user_id="+loginUser.getId()+" or right_user_id = " + loginUser.getId() + ") and mark != "
				+ HallBean.GS_END + " and game_id = " + HallBean.JINHUA;
		if (hallService.getWGameHallCount(condition) > 0) {
			request.setAttribute("tip", "您正在游戏中，完成游戏之后才能选择快速开始。");
			request.setAttribute("result", "failure");
			request.setAttribute("type",type+"");
			return;
		}
		KSGameBean ksGameCount = ks.getksGame();
		if(ksGameCount==null){
			synchronized(JinhuaAction.lock){
				ksGameList.add(ks);
			}
			request.setAttribute("tip", "系统正在帮您找玩家，请等待60秒。");
			request.setAttribute("result", "wait");
			request.setAttribute("type",type+"");
			return;
		}else{
			UserBean enemy = null;
			if (OnlineUtil.isOnline(ksGameCount.getUserId() + ""))
				enemy = UserInfoUtil.getUser(ksGameCount.getUserId());
			// zhul 2006-10-17 优化用户信息查询 end
			// 用户不在线
			if (enemy == null) {
				request.setAttribute("tip", "此用户不在线");
				request.setAttribute("result", "failure");
				request.setAttribute("type",type+"");
				return;
			}
			// 用户已经被邀请
			condition = "(left_user_id="+enemy.getId()+" or right_user_id = " + enemy.getId() + ") and mark != "
					+ HallBean.GS_END + " and game_id = " + HallBean.JINHUA;
			if (hallService.getWGameHallCount(condition) > 0) {
				request.setAttribute("tip", "此用户已在游戏中");
				request.setAttribute("result", "failure");
				request.setAttribute("type",type+"");
				return;
			}
			// 已经邀请了1人
			condition = "left_user_id = " + loginUser.getId() + " and mark = "
					+ HallBean.GS_WAITING + " and game_id = " + HallBean.JINHUA;
			if (hallService.getWGameHallCount(condition) >= WGameBean.MAX_INVITE_COUNT) {
				request.setAttribute("tip", "你已经邀请了" + WGameBean.MAX_INVITE_COUNT
						+ "人,不能再邀请");
				request.setAttribute("result", "failure");
				request.setAttribute("type",type+"");
				return;
			}

			String uniqueMark = StringUtil.getUniqueStr();

			// 增加一个赌局
			WGameHallBean jinhua = new WGameHallBean();
			jinhua.setGameId(HallBean.JINHUA);
			jinhua.setLeftNickname(loginUser.getNickName());
			jinhua.setLeftUserId(loginUser.getId());
			jinhua.setLeftSessionId(request.getSession().getId());
			jinhua.setLeftStatus(HallBean.PS_READY);
			jinhua.setRightNickname(enemy.getNickName());
			jinhua.setRightUserId(enemy.getId());
			jinhua.setUniqueMark(uniqueMark);
			jinhua.setMark(HallBean.GS_PLAYING);
			hallService.addWGameHall(jinhua);

			condition = "left_user_id = " + loginUser.getId()
					+ " and right_user_id = " + enemy.getId() + " and game_id = "
					+ HallBean.JINHUA + " and mark = " + HallBean.GS_PLAYING;
			jinhua = hallService.getWGameHall(condition);

			// 保存游戏数据
			JinhuaDataBean data = new JinhuaDataBean();
			data.setLastActiveTime(DateUtil.getCurrentTime());
			data.setLastActiveUserId(loginUser.getId());
			data.setUniqueMark(uniqueMark);
			data.setActionType(JinhuaDataBean.ACTION_ACCEPT_INVITATION);
			data.setStakeLevel(type);
			initCard(data);
			GameDataAction.putJinhuaData(uniqueMark, data);
			
			//加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "砸金花",
					"下注"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(enemy.getId());
			notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
			notice.setLink("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);
			
			request.setAttribute("result", "success");
			request.setAttribute("jinhua", jinhua);
			request.setAttribute("type",type+"");
			return;
		}
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
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// macq_2006-10-18_更改pk游戏排序规则_start
		String orderBy = "id";
		// macq_2006-10-18_更改pk游戏排序规则_end
		String condition = "game_id = " + WGameBean.HALL_JINHUA;// *改
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
		// liuyi 2006-12-02 程序优化 end

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
					+ HallBean.JINHUA;// *改
			WGameHallBean currentJinHua = hallService.getWGameHall(condition);// *改
			if (currentJinHua != null) {
				request.setAttribute("currentJinHua", currentJinHua);
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
		WGameHallBean jinhua = null;

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
				+ " and game_id = " + HallBean.JINHUA;
		jinhua = hallService.getWGameHall(condition);
		if (jinhua != null) {
			// 取得赌局数据
			JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
					.getUniqueMark());
			// 初始化玩家的牌
			initCard(data);
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
					"砸金花"));// *改
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(jinhua.getLeftUserId());
			notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
			notice.setLink("/wgamehall/jinhua/playing.jsp?gameId="
					+ jinhua.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.setActionType(JinhuaDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
			}
			// liuyi 2006-12-01 程序优化 end
			request.setAttribute("result", "success");
			request.setAttribute("jinhua", jinhua);
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
				+ HallBean.GS_END + " and game_id = " + HallBean.JINHUA;
		if (hallService.getWGameHallCount(condition) > 0) {
			request.setAttribute("tip", "此用户已在游戏中");
			request.setAttribute("result", "failure");
			return;
		}
		// 已经邀请了5人
		condition = "left_user_id = " + loginUser.getId() + " and mark = "
				+ HallBean.GS_WAITING + " and game_id = " + HallBean.JINHUA;
		if (hallService.getWGameHallCount(condition) >= WGameBean.MAX_INVITE_COUNT) {
			request.setAttribute("tip", "你已经邀请了" + WGameBean.MAX_INVITE_COUNT
					+ "人,不能再邀请");
			request.setAttribute("result", "failure");
			return;
		}

		String uniqueMark = StringUtil.getUniqueStr();

		// 增加一个赌局
		jinhua = new WGameHallBean();
		jinhua.setGameId(HallBean.JINHUA);
		jinhua.setLeftNickname(loginUser.getNickName());
		jinhua.setLeftUserId(loginUser.getId());
		jinhua.setLeftSessionId(request.getSession().getId());
		jinhua.setLeftStatus(HallBean.PS_READY);
		jinhua.setRightNickname(enemy.getNickName());
		jinhua.setRightUserId(enemy.getId());
		jinhua.setUniqueMark(uniqueMark);
		jinhua.setMark(HallBean.GS_WAITING);
		hallService.addWGameHall(jinhua);

		condition = "left_user_id = " + loginUser.getId()
				+ " and right_user_id = " + enemy.getId() + " and game_id = "
				+ HallBean.JINHUA + " and mark = " + HallBean.GS_WAITING;
		jinhua = hallService.getWGameHall(condition);

		// 保存游戏数据
		JinhuaDataBean data = new JinhuaDataBean();
		data.setLastActiveTime(DateUtil.getCurrentTime());
		data.setLastActiveUserId(loginUser.getId());
		data.setUniqueMark(uniqueMark);
		data.setActionType(JinhuaDataBean.ACTION_INVITE);
		// /////////////////////////////////////////////////游戏赌注类型
		int type = StringUtil.toInt(request.getParameter("type"));
		// 用户ID不对
		if (type == -1) {
			type = 1;
		}
		data.setStakeLevel(type);
		// //////////////////////////////////////////////////
		GameDataAction.putJinhuaData(uniqueMark, data);

		// **更新用户状态**
		// **加入消息系统**

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getInviteNoticeTitle(loginUser.getNickName(), "砸金花"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(jinhua.getRightUserId());
		notice.setHideUrl("");
		notice.setLink("/wgamehall/jinhua/viewInvitation.jsp?gameId="
				+ jinhua.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		request.setAttribute("result", "success");
		request.setAttribute("jinhua", jinhua);
	}

	/**
	 * 取消邀请
	 * 
	 * @param request
	 */
	public void cancelInvitation(HttpServletRequest request) {
		// 取得参数
		String condition = null;
		WGameHallBean jinhua = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		if (jinhua == null) {
			return;
		} else if (jinhua.getMark() != HallBean.GS_WAITING) {
			return;
		} else if (jinhua.getLeftUserId() != loginUser.getId()) {
			return;
		} else {
			// 删除砸金花
			hallService.deleteWGameHall(condition);
			// 删除游戏数据
			GameDataAction.removeJinhuaData(jinhua.getUniqueMark());
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
		WGameHallBean jinhua = hallService.getWGameHall(condition);
		if (jinhua == null) {
			return;
		}

		// 取得赌局数据
		JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
				.getUniqueMark());
		String result = null, tip = null, active = null;
		//当前时间
		long currentTime = DateUtil.getCurrentTime();
		int seconds = 0;

		if (jinhua.getMark() != HallBean.GS_END) {
			if (data == null) {
				return;
			}
			seconds = (int) ((currentTime - data.getLastActiveTime()) / 1000);
		}

		String timeoutNickname = null;
		if (loginUser.getId() == jinhua.getLeftUserId()) {
			timeoutNickname = jinhua.getRightNickname();
		} else {
			timeoutNickname = jinhua.getLeftNickname();
		}

		// 赌局状态
		/**
		 * 邀请中
		 */
		if (jinhua.getMark() == HallBean.GS_WAITING) {
			// 超时了
			if (seconds >= TIME_OUT) {
				// fanys2006-08-11
				UserInfoUtil.updateUserCash(loginUser.getId(), 100, UserCashAction.GAME,
						"wgamehall--砸金花--加100乐币");
				// mcq_1_更改超时后玩家的乐币数
				updateTimeOut();
				// mcq_end

				// 更新游戏状态
				condition = "id = " + jinhua.getId();
				hallService.updateWGameHall("mark = " + HallBean.GS_END
						+ ", result = '" + StringUtil.toSql(timeoutNickname) + "超时没接受邀请'",
						condition);
//				// 删除本局数据
//				GameDataAction.removeJinhuaData(jinhua.getUniqueMark());
				jinhua.setResult(timeoutNickname + "超时没接受邀请");
				jinhua.setMark(HallBean.GS_END);
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
		if (jinhua.getMark() == HallBean.GS_PLAYING) {
			// 上一次是对方操作
			if (data.getLastActiveUserId() != loginUser.getId()) {
				// 超时了
				if (seconds >= TIME_OUT) {
					int winUserId = (jinhua.getLeftUserId() == loginUser
							.getId() ? jinhua.getRightUserId() : jinhua
							.getLeftUserId());
					jinhua.setMark(HallBean.GS_END);
					jinhua.setWinUserId(winUserId);
					jinhua.setResult(loginUser.getNickName() + "超时没回应");
					jinhua.setMark(HallBean.GS_END);
					end(jinhua, data, winUserId, loginUser.getNickName() + "超时");
				}
				// 对方应邀
				else if (data.getActionType() == JinhuaDataBean.ACTION_ACCEPT_INVITATION) {
					active = "1";// 
//					result = "kick";
//					tip = "对方应邀进入游戏<br/>现在轮到您射门";
				}
				// 对方下赌注
				else if (data.getActionType() == JinhuaDataBean.ACTION_STAKE) {
					active = "1";
//					result = "save";
//					tip = "对方射门,请您扑救";
				}

			}
			// 上一次是自己操作
			else {
				// 对方超时自动判负
				if (seconds > TIME_OUT) {
					int winUserId = loginUser.getId();
					jinhua.setMark(HallBean.GS_END);
					jinhua.setWinUserId(winUserId);
					String gameResult = null;
					if (jinhua.getLeftUserId() == loginUser.getId()) {
						gameResult = jinhua.getRightNickname() + "超时";
					} else {
						gameResult = jinhua.getLeftNickname() + "超时";
					}
					jinhua.setResult(gameResult);
					jinhua.setMark(HallBean.GS_END);
					end(jinhua, data, winUserId, gameResult);
				}
				// 应邀进入
				else if (data.getActionType() == JinhuaDataBean.ACTION_ACCEPT_INVITATION) {
					active = "0";
//					result = "kick";
//					tip = "您应邀进入游戏<br/>请等待对方射门";
				}
				// 自己下赌注
				else if (data.getActionType() == JinhuaDataBean.ACTION_STAKE) {
					active = "0";
//					result = "save";
//					tip = "您摆腿大力抽射<br/>请等待对方扑救";
				}
			}
		}
		/**
		 * 游戏已经结束
		 */
		if (jinhua.getMark() == HallBean.GS_END) {
			if (jinhua.getWinUserId() == loginUser.getId()) {
				result = "win";
			} else if (jinhua.getWinUserId() == 0) {
				result = "draw";
			} else {
				result = "lose";
			}
			int inviteUserId = jinhua.getLeftUserId() == loginUser.getId() ? jinhua
					.getRightUserId()
					: jinhua.getLeftUserId();
			request.setAttribute("inviteUserId", new Integer(inviteUserId));
		}

		request.setAttribute("leftSeconds", new Integer(TIME_OUT - seconds));
		request.setAttribute("result", result);
		request.setAttribute("active", active);
		request.setAttribute("tip", tip);
		request.setAttribute("jinhua", jinhua);
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
		WGameHallBean jinhua = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (jinhua.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		} else {
			// 对手积分和乐币
			// fanys2006-08-11
			// fanys2006-08-11
			UserStatusBean enemyUs = UserInfoUtil.getUserStatus(jinhua
					.getLeftUserId());
			// 取得赌局数据
			JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
					.getUniqueMark());
			
			request.setAttribute("data", data);
			request.setAttribute("enemyUs", enemyUs);
			request.setAttribute("result", "success");
			request.setAttribute("jinhua", jinhua);
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
		WGameHallBean jinhua = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			return;
		}
		// 赌局已结束
		else if (jinhua.getMark() != HallBean.GS_WAITING) {
			return;
		}
		// 正常
		else {
			// 删除赌局
			hallService.deleteWGameHall(condition);

			// 加入消息系统
			NoticeBean notice = new NoticeBean();
			notice.setTitle(getDenyInviteNoticeTitle(loginUser.getNickName(),
					"砸金花"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(jinhua.getLeftUserId());
			notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
			notice.setLink("/wgamehall/jinhua/index.jsp");
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
		WGameHallBean jinhua = null;

		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		// 游戏ID不对
		if (gameId == -1) {
			request.setAttribute("result", "failure");
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			request.setAttribute("result", "failure");
			return;
		}
		// 赌局已结束
		else if (jinhua.getMark() != HallBean.GS_WAITING) {
			request.setAttribute("result", "failure");
			return;
		}
		// 取得赌局数据
		JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
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
					"砸金花"));
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setUserId(jinhua.getLeftUserId());
			notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
			notice.setLink("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
			notice.setMark(NoticeBean.PK_GAME_HALL);
			//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
			noticeService.addNotice(notice);

			// 更新游戏数据
			// liuyi 2006-12-01 程序优化 start
			// synchronized (data)
			{
				data.setActionType(JinhuaDataBean.ACTION_ACCEPT_INVITATION);
				data.setLastActiveTime(DateUtil.getCurrentTime());
				data.setLastActiveUserId(loginUser.getId());
				// 初始化玩家的牌
				initCard(data);
			}
			// liuyi 2006-12-01 程序优化 end
			request.setAttribute("jinhua", jinhua);
		}
	}

	/**
	 * 加赌注。
	 * 
	 * @param request
	 * @return
	 */
	public void addStake(HttpServletRequest request) {
		WGameHallBean jinhua = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			return;
		}
		// 取得赌局数据
		JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
				.getUniqueMark());
		// 数据出错
		if (data == null) {
			return;
		}
		// liuyi 2006-12-01 程序优化 start
		// synchronized (data)
		{
//			data.setActionType(FootballDataBean.ACTION_KICK);
			data.setLastActiveTime(DateUtil.getCurrentTime());
			data.setLastActiveUserId(loginUser.getId());
		}
		// liuyi 2006-12-01 程序优化 end
		int toId = 0;
		// 加赌注
		if (loginUser.getId() == jinhua.getLeftUserId()) {
			toId = jinhua.getRightUserId();
           //正常下注
			if (data.getLeftStake() > 0) {
				data.setLeftStake(data.getLeftStake()
						+ data.getLevel_stake()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLevel_stake()[data.getStakeLevel()], UserCashAction.GAME,
						"wgamehall-砸金花--toId"
								+ data.getLevel_stake()[data.getStakeLevel()]
								+ "乐币");
				
				//第一次下注,加入底钱
			} else {
				data.setLeftStake(data.getLevel_stake()[data.getStakeLevel()]
						+ data.getLevel_bottom()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLeftStake(),
						UserCashAction.GAME, "wgamehall-砸金花--toId"
								+ data.getLeftStake() + "乐币");
			}
		} else {
			toId = jinhua.getLeftUserId();
			 //正常下注
			if (data.getRightStake() > 0) {
				data.setRightStake(data.getRightStake()
						+ data.getLevel_stake()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLevel_stake()[data.getStakeLevel()], UserCashAction.GAME,
						"wgamehall-砸金花--toId"
								+ data.getLevel_stake()[data.getStakeLevel()]
								+ "乐币");
            //第一次下注,加入底钱
			} else {
				data.setRightStake(data.getLevel_stake()[data.getStakeLevel()]
						+ data.getLevel_bottom()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getRightStake(),
						UserCashAction.GAME, "wgamehall-砸金花--toId"
								+ data.getRightStake() + "乐币");
			}
		}
		
		//更新总的赌注
		data.setTotalStake(data.getLeftStake() + data.getRightStake());
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "砸金花",
				"下注"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
		notice.setLink("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);
	}

	/**
	 * 开牌。
	 * 
	 * @param request
	 * @return
	 */
	public void finish(HttpServletRequest request) {
		
		WGameHallBean jinhua = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}
		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			return;
		}
		// 取得赌局数据
		JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
				.getUniqueMark());

		// 数据出错
		if (data == null || data.isEnd()) {
			return;
		}
		// liuyi 2006-12-01 程序优化 start
		// synchronized (data)
		{
//			data.setActionType(FootballDataBean.ACTION_SAVE);
			data.setLastActiveTime(DateUtil.getCurrentTime());
			data.setLastActiveUserId(loginUser.getId());
		}

		int toId = 0;
		if (loginUser.getId() == jinhua.getLeftUserId()) {
			toId = jinhua.getRightUserId();
			//正常开牌
			if (data.getLeftStake() > 0) {
				data.setLeftStake(data.getLeftStake()
						+ data.getLevel_stake()[data.getStakeLevel()]*2);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLevel_stake()[data.getStakeLevel()]*2, UserCashAction.GAME,
						"wgamehall-砸金花--toId"
								+ data.getLevel_stake()[data.getStakeLevel()]*2
								+ "乐币");
				//第一次下注就开牌
			} else {
				data.setLeftStake(data.getLevel_stake()[data.getStakeLevel()]*2
						+ data.getLevel_bottom()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLeftStake(),
						UserCashAction.GAME, "wgamehall-砸金花--toId"
								+ data.getLeftStake() + "乐币");
			}
		} else {
			toId = jinhua.getLeftUserId();
//			正常开牌
			if (data.getRightStake() > 0) {
				data.setRightStake(data.getRightStake()
						+ data.getLevel_stake()[data.getStakeLevel()]*2);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getLevel_stake()[data.getStakeLevel()]*2, UserCashAction.GAME,
						"wgamehall-砸金花--toId"
								+ data.getLevel_stake()[data.getStakeLevel()]*2
								+ "乐币");
//				第一次下注就开牌
			} else {
				data.setRightStake(data.getLevel_stake()[data.getStakeLevel()]*2
						+ data.getLevel_bottom()[data.getStakeLevel()]);
				// 更新用户乐币
				UserInfoUtil.updateUserCash(toId, -data.getRightStake(),
						UserCashAction.GAME, "wgamehall-砸金花--toId"
								+ data.getRightStake() + "乐币");
			}
		}

		data.setTotalStake(data.getLeftStake() + data.getRightStake());
		// liuyi 2006-12-01 程序优化 end
        //结束标记
		jinhua.setMark(HallBean.GS_END);
		jinhua.setWinUserId(data.getWinner());
		jinhua.setResult(loginUser.getNickName() + "开牌");
		int winUserId = 0;
		if (data.getWinner() == 1)
			winUserId = jinhua.getLeftUserId();
		else
			winUserId = jinhua.getRightUserId();

		end(jinhua, data, winUserId, loginUser.getNickName() + "开牌");

		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "砸金花",
				"开牌"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
		notice.setLink("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
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
		WGameHallBean jinhua = null;
		String condition = null;
		// 取得参数
		int gameId = StringUtil.toInt(request.getParameter("gameId"));
		request.setAttribute("gameId", new Integer(gameId));

		// 游戏ID不对
		if (gameId == -1) {
			return;
		}

		condition = "id = " + gameId;
		jinhua = hallService.getWGameHall(condition);
		// 赌局已删除
		if (jinhua == null) {
			return;
		}
		// 取得赌局数据
		JinhuaDataBean data = GameDataAction.getJinhuaData(jinhua
				.getUniqueMark());
		// 数据出错
		if (data == null || data.isEnd()) {
			return;
		}

		int toId = 0;
		if (loginUser.getId() == jinhua.getLeftUserId()) {
			toId = jinhua.getRightUserId();
		} else {
			toId = jinhua.getLeftUserId();
		}
		// 加入消息系统
		NoticeBean notice = new NoticeBean();
		notice.setTitle(getActionNoticeTitle(loginUser.getNickName(), "砸金花",
				"认输"));
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setUserId(toId);
		notice.setHideUrl("/wgamehall/jinhua/playing.jsp");
		notice.setLink("/wgamehall/jinhua/playing.jsp?gameId=" + jinhua.getId());
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_start
		notice.setMark(NoticeBean.PK_GAME_HALL);
		//macq_2007-5-16_增加人人多回合对战游戏消息类型_end
		noticeService.addNotice(notice);

		int winUserId = (jinhua.getLeftUserId() == loginUser.getId() ? jinhua
				.getRightUserId() : jinhua.getLeftUserId());

		jinhua.setMark(HallBean.GS_END);
		end(jinhua, data, winUserId, loginUser.getNickName() + "认输");
	}

	/**
	 * 结束一局，处理输赢
	 */
	public void end(WGameHallBean jinhua, JinhuaDataBean data, int winUserId,
			String result) {
		jinhua.setResult(result);
		// 分出胜负
		int loseUserId = 0;
		if(data.isEnd())
			return;
		data.setEnd(true);
		if (winUserId == jinhua.getLeftUserId()) {
			loseUserId = jinhua.getRightUserId();
			data.setWinner(1);
			// 增加经验值
			updateInfo(winUserId, data.getExp()[data.getLeftCardLevel()]);
			
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(winUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_JINHUA);
			history.setWinCount(1);
			history.setMoney(data.getRightStake());
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(loseUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_JINHUA);
			history.setLoseCount(1);
			history.setMoney((-1)*data.getRightStake());
			updateHistory(history);
		}else
		{
			loseUserId = jinhua.getLeftUserId();
			data.setWinner(2);
			// 增加经验值
			updateInfo(winUserId, data.getExp()[data.getRightCardLevel()]);
			
			// 更新历史战绩
			HistoryBean history = new HistoryBean();
			history.setUserId(winUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_JINHUA);
			history.setWinCount(1);
			history.setMoney(data.getLeftStake());
			updateHistory(history);

			history = new HistoryBean();
			history.setUserId(loseUserId);
			history.setGameType(WGameBean.GT_HALL);
			history.setGameId(WGameBean.HALL_JINHUA);
			history.setLoseCount(1);
			history.setMoney((-1)*data.getLeftStake());
			updateHistory(history);
		}
		// 更新双方积分
		getUserStatus(jinhua.getLeftUserId());
		getUserStatus(jinhua.getRightUserId());
		// 超时
		if (result != null && result.indexOf("超时") != -1) {
			// fanys2006-08-11
			UserInfoUtil.updateUserCash(winUserId, data.getTotalStake(), UserCashAction.GAME, "wgamehall--砸金花--加"
							+ data.getTotalStake() + "乐币");
			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
			updateInfo(winUserId, Constants.RANK_WIN);
			updateInfo(loseUserId, Constants.RANK_LOSE);
			// mcq_end
			// 开牌
		} else if (result != null && result.indexOf("开牌") != -1) {
			UserInfoUtil.updateUserCash(winUserId, data.getTotalStake(), UserCashAction.GAME, "wgamehall-砸金花--加"
							+ data.getTotalStake() + "乐币");
			updateInfo(winUserId, Constants.RANK_WIN);
			updateInfo(loseUserId, Constants.RANK_LOSE);
			// 认输
		} else {
			UserInfoUtil.updateUserCash(winUserId, data.getTotalStake(), UserCashAction.GAME, "wgamehall-砸金花--加"
							+ data.getTotalStake() + "乐币");
			// mcq_1_更新玩家胜利后Session的乐币数和经验值 时间 2006-6-12
			updateInfo(winUserId, Constants.RANK_WIN);
			updateInfo(loseUserId, Constants.RANK_LOSE);
		}
		String set = "win_user_id = " + winUserId + ", mark = "
				+ HallBean.GS_END + ", result = '" + result + "'";
		hallService.updateWGameHall(set, "id = " + jinhua.getId());
		// 删除游戏数据
//		GameDataAction.removeJinhuaData(jinhua.getUniqueMark());
	}

	// /////////////////////////////////////////////////////////////////////////////////////////我后加的
	/**
	 * @param request
	 */
	public void history(HttpServletRequest request) {
		HistoryBean history = getHistory(loginUser.getId(), WGameBean.GT_HALL,
				WGameBean.HALL_JINHUA);
		request.setAttribute("history", history);
	}

	// 1、 游戏规则
	// 牌面大小顺序依次为：豹子〉同花顺（顺金）〉同花〉顺子〉对子〉单张，喜儿牌〉豹子。
	// 其中豹子就是三张牌的点数一样，喜儿牌就是玩家手中的牌点数为：2、3、5时成为喜儿牌，喜儿牌大于豹子，但是比其他任何牌的点数都小。
	// 如果牌一样，黑桃〉红桃〉草花〉方片。
	// 假如对方与你的牌型一样，则比较单张大小。例如：三张8肯定比三张5大；黑桃A、10、9肯定比黑K、8、3大，杂顺4、5、6肯定比杂2、3、5大等以次类推。

	public void initCard(JinhuaDataBean data) {
		if (data == null)
			return;
		int index;
		int[] temp = new int[6];
		// 生成双方牌面
		for (int i = 0; i < 6; i++) {
			index = RandomUtil.nextInt(52);
			for (int j = 0; j < i; j++) {
				while ((index == temp[j])) {
					index = RandomUtil.nextInt(52);
				}
			}
			temp[i] = index;
//			System.out.println("temp[" + i + "] =" + temp[i]);
		}
		// 设置双方牌面
		int[] left = new int[3];
		left[0] = temp[0];
		left[1] = temp[1];
		left[2] = temp[2];
		ruleArray(left);
		data.setL_1_L(left[0] / 13);
		data.setL_1_R(left[0] % 13 + 2);
		data.setL_2_L(left[1] / 13);
		data.setL_2_R(left[1] % 13 + 2);
		data.setL_3_L(left[2] / 13);
		data.setL_3_R(left[2] % 13 + 2);

		int[] right = new int[3];
		right[0] = temp[3];
		right[1] = temp[4];
		right[2] = temp[5];
		ruleArray(right);
		data.setR_1_L(right[0] / 13);
		data.setR_1_R(right[0] % 13 + 2);
		data.setR_2_L(right[1] / 13);
		data.setR_2_R(right[1] % 13 + 2);
		data.setR_3_L(right[2] / 13);
		data.setR_3_R(right[2] % 13 + 2);
        //2---14代表2,3,---J,Q,K,A
		compCard(data);
	}

	public void compCard(JinhuaDataBean data) {
		data.setLeftCardLevel(cardLeft(data));
		data.setRightCardLevel(cardRight(data));
		// 比类型大小
		// left胜利
		if ((data.getLeftCardLevel() < data.getRightCardLevel())
				|| ((data.getLeftCardLevel() == 7) && (data.getRightCardLevel() == 1)))// 豹子和特殊比较
		{
			data.setWinner(1);
		} else
		// right胜利
		if ((data.getLeftCardLevel() > data.getRightCardLevel())
				|| ((data.getLeftCardLevel() == 1) && (data.getRightCardLevel() == 7)))// 豹子和特殊比较
		{
			data.setWinner(2);
		} else
		// 类型相同
		{
			switch (data.getLeftCardLevel()) {
			case 1:
				// left胜利   正常大                           三个A
				if ((data.getL_1_R() > data.getR_1_R())&& (data.getR_1_R() != 0)) {
					data.setWinner(1);
				} else// right胜利
				{
					data.setWinner(2);
				}
				break;
			case 2:
				//同样大小的花色相同的三张连牌
				if(data.getL_1_R() == data.getR_1_R())
				{
					//比花色
					if(data.getL_1_L() > data.getR_1_L()){
						data.setWinner(1);
					}else{
						data.setWinner(2);
					}
				//大小不同
				}else if(data.getL_1_R() > data.getR_1_R()){
					data.setWinner(1);
				}else{
					data.setWinner(2);
				}
				break;
			case 3:
				//最大的牌相同
				if(data.getL_3_R() == data.getR_3_R()){
					//第二大的牌相同
					if(data.getL_2_R() == data.getR_2_R()){
						//第三大的牌相同
						if(data.getL_1_R() == data.getR_1_R()){
							//比花色
							if(data.getL_1_L() > data.getR_1_L()){
								data.setWinner(1);
							}else{
								data.setWinner(2);
							}
						}else if(data.getL_1_R() > data.getR_1_R()){
							data.setWinner(1);
						}else{
							data.setWinner(2);
						}
					}else if(data.getL_2_R() > data.getR_2_R()){
						data.setWinner(1);
					}else{
						data.setWinner(2);
					}
				}else if(data.getL_3_R() > data.getR_3_R()){
					data.setWinner(1);
				}else{
					data.setWinner(2);
				}
				break;
			case 4:
				//比最大的牌
				if(data.getL_3_R() == data.getR_3_R()){
					//一样大小的话就比最大牌的颜色
					if(data.getL_3_L() == data.getR_3_L()){
						data.setWinner(1);
					}else{
						data.setWinner(2);
					}
				}else if(data.getL_3_R() > data.getR_3_R()){
					data.setWinner(1);
				}else{
					data.setWinner(2);
				}
				break;
			case 5:
				//三张牌中有两张同样大小的牌
				if(data.getL_2_R() == data.getR_2_R())
				{
					//判断比第三张
					if(data.getL_2_R() == data.getL_1_R())
					{
						//比第三张
						if(data.getL_3_R() == data.getR_3_R()){
							if(data.getL_3_L() == data.getR_3_L()){
								data.setWinner(1);
							}else{
								data.setWinner(2);
							}
						}else if(data.getL_3_R() > data.getR_3_R()){
							data.setWinner(1);
						}else{
							data.setWinner(2);
						}
                   //判断比第一张
					}else{
						//比第一张
						if(data.getL_1_R() == data.getR_1_R()){
							if(data.getL_1_L() == data.getR_1_L()){
								data.setWinner(1);
							}else{
								data.setWinner(2);
							}
						}else if(data.getL_1_R() > data.getR_1_R()){
							data.setWinner(1);
						}else{
							data.setWinner(2);
						}
					}
				//大小不同
				}else if(data.getL_2_R() > data.getR_2_R()){
					data.setWinner(1);
				}else{
					data.setWinner(2);
				}
				break;
			case 6:
			case 7:
				//最大的牌相同
				if(data.getL_3_R() == data.getR_3_R()){
					//第二大的牌相同
					if(data.getL_2_R() == data.getR_2_R()){
						//第三大的牌相同
						if(data.getL_1_R() == data.getR_1_R()){
							//比花色
							if(data.getL_3_L() > data.getR_3_L()){
								data.setWinner(1);
							}else{
								data.setWinner(2);
							}
						}else if(data.getL_1_R() > data.getR_1_R()){
							data.setWinner(1);
						}else{
							data.setWinner(2);
						}
					}else if(data.getL_2_R() > data.getR_2_R()){
						data.setWinner(1);
					}else{
						data.setWinner(2);
					}
				}else if(data.getL_3_R() > data.getR_3_R()){
					data.setWinner(1);
				}else{
					data.setWinner(2);
				}
				break;
			}
		}
	}

	/**
	 * 设置牌是什么类型,为比大小做准备, 1--豹子：三张同样大小的牌 2--顺金：花色相同的三张连牌 3--金花：三张花色相同的牌
	 * 4--顺子：三张花色不全相同的连牌 5--对子：三张牌中有两张同样大小的牌 6--特殊：花色不同的 235 7--单张：除以上牌型的牌
	 */
	public int cardLeft(JinhuaDataBean data) {
		// 1--豹子：三张同样大小的牌
		if ((data.getL_1_R() == data.getL_2_R())
				&& (data.getL_2_R() == data.getL_3_R())) {
			return 1;
		}

		// 2--顺金：花色相同的三张连牌
		// 花色相同
		if ((data.getL_1_L() == data.getL_2_L())
				&& (data.getL_2_L() == data.getL_3_L()))// 花色相同
		{
			// 连牌
			if ((data.getL_2_R() - data.getL_1_R() == 1)
					&& (data.getL_3_R() - data.getL_2_R() == 1)) {
				return 2;
			}
		}

		// 3--金花：三张花色相同的牌
		// 花色相同
		if ((data.getL_1_L() == data.getL_2_L())
				&& (data.getL_2_L() == data.getL_3_L()))// 花色相同
		{
			return 3;
		}

		// 4--顺子：三张花色不全相同的连牌
		// 花色不完全相同
		// if(!(((left[0]/13) == (left[1]/13))&&((left[1]/13) == (left[2]/13))))
		// {
		// 连牌
		if ((data.getL_2_R() - data.getL_1_R() == 1)
				&& (data.getL_3_R() - data.getL_2_R() == 1)) {
			return 4;
		}
		// }

		// 5--对子：三张牌中有两张同样大小的牌
		if ((data.getL_1_R() == data.getL_2_R())
				|| (data.getL_1_R() == data.getL_3_R())
				|| (data.getL_2_R() == data.getL_3_R())) {
			return 5;
		}

		// 7--特殊：花色不同的 235
		// 花色不同
		if ((data.getL_1_L() != data.getL_2_L())
				&& (data.getL_1_L() != data.getL_3_L())
				&& (data.getL_2_L() != data.getL_3_L())) {
			if ((data.getL_1_R() == 2) && (data.getL_2_R() == 3)
					&& (data.getL_3_R() == 5)) {
				return 6;
			}
		}

		// 6--单张：除以上牌型的牌
		return 7;
	}

	/**
	 * 设置牌是什么类型,为比大小做准备, 1--豹子：三张同样大小的牌 2--顺金：花色相同的三张连牌 3--金花：三张花色相同的牌
	 * 4--顺子：三张花色不全相同的连牌 5--对子：三张牌中有两张同样大小的牌 6--特殊：花色不同的 235 7--单张：除以上牌型的牌
	 */
	public int cardRight(JinhuaDataBean data) {
		// 1--豹子：三张同样大小的牌
		if ((data.getR_1_R() == data.getR_2_R())
				&& (data.getR_2_R() == data.getR_3_R())) {
			return 1;
		}

		// 2--顺金：花色相同的三张连牌
		// 花色相同
		if ((data.getR_1_L() == data.getR_2_L())
				&& (data.getR_2_L() == data.getR_3_L()))// 花色相同
		{
			// 连牌
			if ((data.getR_2_R() - data.getR_1_R() == 1)
					&& (data.getR_3_R() - data.getR_2_R() == 1)) {
				return 2;
			}
		}

		// 3--金花：三张花色相同的牌
		// 花色相同
		if ((data.getR_1_L() == data.getR_2_L())
				&& (data.getR_2_L() == data.getR_3_L()))// 花色相同
		{
			return 3;
		}

		// 4--顺子：三张花色不全相同的连牌
		// 花色不完全相同
		// if(!(((left[0]/13) == (left[1]/13))&&((left[1]/13) == (left[2]/13))))
		// {
		// 连牌
		if ((data.getR_2_R() - data.getR_1_R() == 1)
				&& (data.getR_3_R() - data.getR_2_R() == 1)) {
			return 4;
		}
		// }

		// 5--对子：三张牌中有两张同样大小的牌
		if ((data.getR_1_R() == data.getR_2_R())
				|| (data.getR_1_R() == data.getR_3_R())
				|| (data.getR_2_R() == data.getR_3_R())) {
			return 5;
		}

		// 6--特殊：花色不同的 235
		// 花色不同
		if ((data.getR_1_L() != data.getR_2_L())
				&& (data.getR_1_L() != data.getR_3_L())
				&& (data.getR_2_L() != data.getR_3_L())) {
			if ((data.getR_1_R() == 2) && (data.getR_2_R() == 3)
					&& (data.getR_3_R() == 5)) {
				return 6;
			}
		}

		// 7--单张：除以上牌型的牌
		return 7;
	}

	/**
	 * 
	 * 从小到大排序
	 */
	public static void ruleArray(int[] array) {
		int hold;
		for (int pass = 1; pass < array.length; pass++) {
			for (int element = 0; element < array.length - 1; element++) {
				if (array[element] % 13 > array[element + 1] % 13) {
					hold = array[element];
					array[element] = array[element + 1];
					array[element + 1] = hold;
				}
			}
		}
	}

	/**
	 * 
	 * 定时清除catch同GameDataAction.removeJinhuaData(jinhua.getUniqueMark());
	 */
	public static void gcCatch() {
		long currentTime = DateUtil.getCurrentTime();
		
		Hashtable jinhuaData;
		if(GameDataAction.getJinhuaData() == null){
			return;
		}
			jinhuaData = (Hashtable)GameDataAction.getJinhuaData().clone();
			Iterator iter = jinhuaData.keySet().iterator();
			while (iter.hasNext()) {
				String uniqueMark = (String) iter.next();
				JinhuaDataBean data = (JinhuaDataBean) jinhuaData.get(uniqueMark);
				if(((currentTime - data.getLastActiveTime()) / 1000) > 21600)
					GameDataAction.removeJinhuaData(uniqueMark);
			}
	}
	// /////////////////////////////////////////////////////////////////////////////////////////我后加的}
}
