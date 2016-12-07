/*
 * Created on 2006-1-10
 *
 */
package net.joycool.wap.action.wgame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.wgame.HistoryBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.service.infc.IWGameService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class WGameAction {

	Vector title = null;
	
	static IUserService userService = ServiceFactory.createUserService();
	static IWGameService service = ServiceFactory.createWGameService();
	static IChatService chatrService = ServiceFactory.createChatService();
	static HomeServiceImpl homeService = new HomeServiceImpl();

	public void setSessionObject(HttpServletRequest request, String attrName,
			Object value) {
		HttpSession session = request.getSession();
		session.setAttribute(attrName, value);
	}

	public void setApplicationObject(HttpServletRequest request,
			String attrName, Object value) {
		ServletContext ctx = request.getSession().getServletContext();
		if (ctx.getAttribute(attrName) == null) {
			ctx.setAttribute(attrName, value);
		}
	}

	public Object getSessionObject(HttpServletRequest request, String attrName) {
		HttpSession session = request.getSession(false);
		if(session == null)
			return null;
		return session.getAttribute(attrName);
	}

	public UserBean getLoginUser(HttpServletRequest request) {
		return (UserBean) getSessionObject(request, Constants.LOGIN_USER_KEY);
	}

	public UserStatusBean getUserStatus(int userId) {
		
		// UserStatusBean status = userService
		// .getUserStatus("user_id = " + userId);
		// fanys2006-08-11
		UserStatusBean status = UserInfoUtil.getUserStatus(userId);
		if (status != null) {
			return status;
		} else {
			status = new UserStatusBean();
			status.setUserId(userId);
			status.setGamePoint(10000);
			status.setPoint(100);
			userService.addUserStatus(status);
			// add by mcq 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.USERREGISTER, 10000,
					Constants.PLUS, userId);
			// add by mcq 2006-07-24 for stat user money history end
			return status;
		}
	}

	/**
	 * @param request
	 * @return
	 */
	public UserStatusBean getUserStatus(HttpServletRequest request) {
		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return null;
		} else {
			return getUserStatus(loginUser.getId());
		}
	}

	public HistoryBean getHistory(int userId, int gameType, int gameId) {
		String condition = "user_id = " + userId + " and game_type = "
				+ gameType + " and game_id = " + gameId;
		HistoryBean history = service.getHistory(condition);
		if (history != null) {
			return history;
		} else {
			history = new HistoryBean();
			history.setUserId(userId);
			history.setGameType(gameType);
			history.setGameId(gameId);
			service.addHistory(history);
			return history;
		}
	}

	public void getRandImg(HttpServletRequest request) {
		String fileUrl = null;
		int catalogId = StringUtil.toInt(request.getParameter("type"));
		if (catalogId == -1) {
			catalogId = 91;
		}

		DbOperation dbOp = new DbOperation();
		dbOp.init();
		String query = "select file_url from ppicture where catalog_id = "
				+ catalogId + " order by rand() limit 0, 1";
		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				fileUrl = "/rep/picture/"
						+ rs.getString("file_url");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();

		request.setAttribute("fileUrl", fileUrl);
	}

	/**
	 * 统计战绩。
	 * 
	 * @param history
	 * @return
	 */
	public void updateHistory(HistoryBean history) {
		String condition = "user_id = " + history.getUserId()
				+ " and game_type = " + history.getGameType()
				+ " and game_id = " + history.getGameId();
		int count = service.getHistoryCount(condition);
		// 已经添加
		if (count > 0) {
			String set = "win_count = win_count + " + history.getWinCount()
					+ ", draw_count = draw_count + " + history.getDrawCount()
					+ ", lose_count = lose_count + " + history.getLoseCount()
					+ ", money = money + " + history.getMoney();
			service.updateHistory(set, condition);
		}
		// 尚未添加
		else {
			service.addHistory(history);
		}
	}

	/**
	 * 大厅
	 * 
	 * @author lbj
	 * 
	 */
	public void hall(HttpServletRequest request) {
		// 用户乐币数
		// UserStatusBean us = getUserStatus(request);
		// mcq_4_从Session中直接获取用户 status和info信息
		UserBean us = getLoginUser(request);
		if (us != null) {
			request.setAttribute("us", us);
		}
		// mcq_4_end
		// 在线总人数
		int onlineCount = userService.getOnlineUserCount("id > 0");

		// 当前战况条数
		int rumorCount = WGameDataAction.getRumorList().size();
		// macq_2006-6-20_随机显示社区名称_start
		String title = LoadResource.getTitleName();
		request.setAttribute("title", title);
		// macq_2006-6-20_随机显示社区名称_end

		// mcq_2006-6-20_取得2条聊天室最新的人与人之间的的交流信息_start
		// Vector chatList = null;
		
		// macq_2006-10-12_获取聊天大厅2条最新的信息_start (注: 传入的值是聊天大厅id)
//		Vector contentIdList = (Vector) RoomCacheUtil.getRoomContentCountMap(0);
//		Vector chatList = new Vector();
//		int contentCount = 0;
//		if (contentIdList != null) {
//			contentCount = contentIdList.size();
//		   }
//		JCRoomContentBean roomContent = null;
//		int roomContentId = 0;
//		int j=0;
//		for (int i = 0; i < contentCount; i++) {
//			roomContentId = ((Integer) contentIdList.get(i)).intValue();
//			roomContent = RoomCacheUtil.getRoomContent(roomContentId);
//			if(roomContent.getMark()==0 && roomContent.getToId()!=0){
//				j++;
//				chatList.add(roomContent);
//				if(j==2){
//					break;
//				}
//			}
//		}
		// macq_2006-10-12_获取聊天大厅2条最新的信息_end
		// chatList = chatrService
		// .getMessageList("is_private=0 AND to_id<>0 AND mark=0 AND room_id=0
		// ORDER BY send_datetime desc limit 0,2");
		//request.setAttribute("chatList", chatList);
		// mcq_2006-6-20_取得2条聊天室最新的人与人之间的的交流信息_end

		request.setAttribute("onlineCount", new Integer(onlineCount));
		request.setAttribute("rumorCount", new Integer(rumorCount));

		
		Vector diaryMark = homeService.getHomeDiaryTopList2("order by b.id desc limit 3");
		Vector photoMark = homeService.getHomePhotoTopList2("order by b.id desc limit 2");
		request.setAttribute("diaryMark", diaryMark);
		request.setAttribute("photoMark", photoMark);
		return;
	}

	/**
	 * 更改玩家状态信息
	 */
	public void updateInfo(UserBean loginUser, int point) {
		// fanys2006-08-11 start
		// mcq_1_增加用户更改过经验值 时间:2006-6-11
		// UserInfoUtil.addUserInfo(loginUser.getId());
		// 判断用户乐币是否更改过 时间:2006-6-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		// mcq_1_增加用户经验值 时间:2006-6-11
		// fanys2006-08-11 end
		// 增加用户经验值
		RankAction.addPoint(loginUser, point);
		// mcq_end
	}

	/**
	 * 更改玩家打平后,更改session中经验值. 打平增加5点经验值
	 */
	public void updateDrawInfo(UserBean loginUser) {
		// mcq_1_判断用户乐币是否更改过 时间:2006-6-11
		// fanys2006-08-11
		// loginUser = UserInfoUtil.getUserInfo(loginUser);
		// mcq_1_增加用户经验值 时间:2006-6-11
		// 增加用户经验值
		RankAction.addPoint(loginUser, Constants.RANK_PK_DRAW);
		// mcq_end
	}

}
