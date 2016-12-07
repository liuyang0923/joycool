package net.joycool.wap.action.chat;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.action.money.MoneyAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.action.user.SendAction;
import net.joycool.wap.action.wgame.TigerAction;
import net.joycool.wap.bean.CrownBean;
import net.joycool.wap.bean.ModuleBean;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.OnlineBean;
import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.RankActionBean;
import net.joycool.wap.bean.UserBagBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.chat.ChatSpeakerBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.chat.JCRoomOnlineBean;
import net.joycool.wap.bean.chat.RoomApplyBean;
import net.joycool.wap.bean.chat.RoomGrantBean;
import net.joycool.wap.bean.chat.RoomHallInviteBean;
import net.joycool.wap.bean.chat.RoomInviteBean;
import net.joycool.wap.bean.chat.RoomInviteRankBean;
import net.joycool.wap.bean.chat.RoomInviteStatBean;
import net.joycool.wap.bean.chat.RoomManagerBean;
import net.joycool.wap.bean.chat.RoomPaymentBean;
import net.joycool.wap.bean.chat.RoomUserBean;
import net.joycool.wap.bean.chat.RoomVoteBean;
import net.joycool.wap.bean.top.UserTopBean;
import net.joycool.wap.cache.CacheAdmin;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.UserBagCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.ChatServiceImpl;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.ITopService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ContentList;
import net.joycool.wap.util.NoticeUtil;
import net.joycool.wap.util.ProbabilityUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.RoomCacheUtil;
import net.joycool.wap.util.RoomUtil;
import net.joycool.wap.util.SmsUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author Administrator
 * 
 */
public class JCRoomChatAction extends CustomAction{
	UserBean loginUser;
	boolean showHatImg = true;
	boolean showFaceImg = true;
	boolean showSendImg = true;
	
	public static String privateNote = "(悄悄)";

	static final int NUMBER_PER_PAGE = 4;

	static final int NUMBER_PER_PAGE_DIF = 0;

	static final int PUBLIC_NUMBER_PER_PAGE = 10;

	static final int ONLINE_NUMBER_PER_PAGE = 10;

	static final int ONLINE_NUMBER_PER_PAGE_FRIEND = 20;

	static final int PUBLIC_ROOM_PAGE = 8;

	static final int PAGE_COUNT = 5;

	public static Vector Message;

	public static IChatService chatService = ServiceFactory.createChatService();

	static IUserService userService = ServiceFactory.createUserService();

	static ITopService topService = ServiceFactory.createTopService();
	
	public static LinkedList speakerList = new LinkedList();

	public static ITopService getTopService() {
		return topService;
	}

	public JCRoomChatAction() {
	}

	public JCRoomChatAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
		if(loginUser != null && loginUser.getUserSetting() != null) {
			showHatImg = !loginUser.getUserSetting().isFlagHideHat();
			showSendImg = !loginUser.getUserSetting().isFlagHideSend();
			showFaceImg = !loginUser.getUserSetting().isFlagHideFace();
		}
	}

	public UserBean getLoginUser() {
		return loginUser;
	}

	/**
	 * 大厅
	 * 
	 * @param request
	 */
	public void hall(HttpServletRequest request, HttpServletResponse response) {
		String flag = (String) request.getSession().getAttribute(
				"hasEnterChatHall");
		final int NUMBER_PER_PAGE = 5;
		final int NUMBER_PER_PAGE_DIF = 0;
		int PUBLIC_NUMBER_PER_PAGE = 10;
		
		if (flag == null) {
			// 欢迎词
			Message = new Vector();
			Vector messagen = ContentList.getSalutatoryManList();
			Vector messagev = ContentList.getSalutatoryWomanList();
			int countn = messagen.size();
			int countv = messagev.size();
			if (countn > 0) {
				int nn = RandomUtil.nextInt(countn);
				Message.add(messagen.get(nn));
			}
			if (countv > 0) {
				int vv = RandomUtil.nextInt(countv);
				Message.add(messagev.get(vv));
			}
			request.setAttribute("Message", Message);
		}
		// 取得房间号
		int roomId = getParameterInt("roomId");

		HttpSession session = request.getSession();
		// JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		JCRoomBean room = RoomUtil.getRoomById(roomId);

		// 如果在session范围内不存在该聊天室信息
		if (room.getStatus() == 2 && room.getPayWay() == 1) {
			// 传值到页面
			request.setAttribute("result", "close");
			request.setAttribute("tip", room.getName() + "聊天室已经关闭");
			return;
		}
		session.setAttribute("status" + roomId, "true");
		// }
		int totalCount1 = 0;
		// zhul_2006-09-13 当前聊天室显示人数start
		int viewNum = RoomUtil.getDisplayNum(roomId);
		request.setAttribute("viewNum", viewNum + "");
		// zhul_2006-09-13 当前聊天室显示人数end
		if (loginUser != null) {
			int number = RoomUtil.getAuthority(loginUser.getId(), roomId, request);
			if (number == 0) {
				request.setAttribute("result", "kick");
				request.setAttribute("tip", "您已经被踢出该房间。");
				if (roomId == 0) {
					session.removeAttribute("oldRoomId");
				}
				return;// 如果用户已经申请过了，继续等待
			} else if (number == 2) {
				request.setAttribute("result", "waiting");
				request.setAttribute("tip", "您已经申请进入这个聊天室，但管理员尚未审批，请等待审批。");
				return;
			} else if (number == 3) {
				request.setAttribute("result", "apply");
				request.setAttribute("tip", "请提交审批通知。");
				return;
			} else {
			}

			// mcq_2006_6_30_更改房间信息_end
			String oldRoomId = (String) session.getAttribute("oldRoomId");
			int oldRoom = StringUtil.toInt(oldRoomId);
			if (oldRoomId == null || oldRoom != roomId) {
				// 判断该房间是否已经满员
				/*if (!isManager(roomId, loginUser.getId())) {

					// zhul 2006-09-05 聊天室显示人数,如果多于1000人，就显示聊天室人员满 start
					if (viewNum > 3000) {

						boolean isExist = RoomUtil.isUserInRoom(roomId, loginUser.getId());
						if (!isExist) {
							// 传值到页面
							// macq_2006-11-07_从缓存中获取聊天室列表_start
							Vector roomCacheList = RoomCacheUtil
									.getRoomListCache();
							request
									.setAttribute("roomCacheList",
											roomCacheList);
							// macq_2006-11-07_从缓存中获取聊天室列表_end
							request.setAttribute("roomName", room.getName());
							request.setAttribute("result", "full");
							request.setAttribute("tip", room.getName()
									+ "聊天室人员已满,请进入其他聊天室");
							return;
						}
						// fanys 2006-09-16 end)
					}
				}*/

				// fanys 2006-09-16 修改聊天室功能 挂线 start
				// zhul_2006-08-09 修改聊天室功能 挂线 start
				// JCRoomOnlineBean onlineuser = chatService
				// .getOnlineUser("user_id=" + loginUser.getId()
				// + " and room_id=" + roomId);
				if (RoomUtil.isUserInRoom(roomId, loginUser.getId()) == false) {
					// chatService.addOnlineUser(roomId + "," +
					// loginUser.getId()
					// + ",now()");
					RoomUtil.addRoomOnlineUser(roomId,
							loginUser.getId());
					// zhul 2006-06-29 当用户进入一个聊天室时发出一条公告,更新房间在线人数 start
					this.dealRoomTransform(loginUser, -1, roomId);
					// zhul 2006-06-29 当用户进入一个聊天室时发出一条公告 ，更新房间在线人数end
				}
				// zhul_2006-08-09 修改聊天室功能 挂线 end
				// fanys_2006-09-16 修改聊天室功能 挂线 end

				session.setAttribute("oldRoomId", String.valueOf(roomId));
			}

			// 获取房间信息
			// mcq_2006_6_30_更改房间信息_start
			room = RoomUtil.getRoomById(roomId);

			// 取得私聊参数
			Vector pmlIdList = RoomCacheUtil.getPrivateContentIDList(loginUser.getId());

			totalCount1 = pmlIdList.size();
			if (totalCount1 > 0) {
/*
				int checkIdSize = Math.min(pmlIdList.size(), 7);
				Vector pmlCheckList = RoomCacheUtil.getContentList(pmlIdList
						.subList(0, checkIdSize));
				// 判断用户是否有私聊信息
				if (pmlCheckList != null) {
					// 判断实际得到的聊天信息数量
					int pmlCheckListCount = pmlCheckList.size();
					// 如果聊天信息小于4条不做处理
					if (pmlCheckListCount <= 1) {
					} else {
						// 初始话聊天记录bean
						JCRoomContentBean contentCheckLast = null;
						// 循环取得第4条记录之后的所有记录
						int endIndex = Math.min(pmlCheckList.size(), 7);
						for (int i = 2; i <= endIndex; i++) {
							// 取得一条聊天记录
							contentCheckLast = (JCRoomContentBean) pmlCheckList
									.get(i - 1);
							// 判断是否为登陆用户发言
							if (loginUser.getId() == contentCheckLast
									.getFromId()) {
								if (i == 2 || i == 3 || i == 4) {
									// 该用户不同分页参数
									NUMBER_PER_PAGE_DIF = 4;
									// 标准分页参数
									NUMBER_PER_PAGE = NUMBER_PER_PAGE_DIF;
									break;
								} else {
									// 该用户不同分页参数
									NUMBER_PER_PAGE_DIF = i;
									// 标准分页参数
									NUMBER_PER_PAGE = NUMBER_PER_PAGE_DIF;
									break;
								}
							}
						}
					}
				}
				// macq_2006_8_9_判断私聊数量_end

				// 分页参数
				int pageIndex1 = StringUtil.toInt(request
						.getParameter("pageIndex1"));
				// 无传分页参数
				if (pageIndex1 == -1) {
					pageIndex1 = 0;
				}
				// 分页参数大于等于1
				if (pageIndex1 >= 1) {
					NUMBER_PER_PAGE = 4;
				}

				// 取得要显示的消息列表
				int start1 = 0;
				int end1 = 0;
				// 判断是否有分页参数
				if (pageIndex1 < 1) {
					start1 = pageIndex1 * NUMBER_PER_PAGE;
					end1 = NUMBER_PER_PAGE;
				}// 判断有分页参数
				else {
					// 判断当前分页页面显示数量是否小于等于4
					if (NUMBER_PER_PAGE_DIF <= 4) {
						start1 = pageIndex1 * NUMBER_PER_PAGE;
						end1 = NUMBER_PER_PAGE;
					}// 当前分页页面显示数量大于4
					else {
						// limit从第一页特殊分页以后的记录开始
						start1 = pageIndex1 * NUMBER_PER_PAGE
								+ (NUMBER_PER_PAGE_DIF - NUMBER_PER_PAGE);
						end1 = NUMBER_PER_PAGE;
					}
				}*/
				int npg = NUMBER_PER_PAGE;
				if(session.getAttribute("hide_public")!=null)
					npg += npg;
				PagingBean paging1 = new PagingBean(this, totalCount1, npg, "p1");
				int startIndex = paging1.getStartIndex();
				int endIndex = paging1.getEndIndex();
				List currentPageContentIdList = pmlIdList.subList(startIndex,
						endIndex);
				Vector currentPagePml = RoomCacheUtil
						.getContentList(currentPageContentIdList);

				String backTo1 = request.getParameter("backTo1");
				if (backTo1 == null) {
					backTo1 = BaseAction.INDEX_URL;
				}
				// 判断当前页面显示记录决定分页数量
				/*int totalPageCount1 = 0;
				if (NUMBER_PER_PAGE > 4) {
					totalPageCount1 = totalCount1 / 4;
					if (totalCount1 % 4 != 0) {
						// 每页分页4个时候的余数
						int test = totalCount1 % 4;
						// 第一页分页特殊情况多显示的数量
						int test1 = NUMBER_PER_PAGE_DIF - 4;
						if (test == test1) {
						} else if (test > test1) {
							totalPageCount1++;
						} else {
							totalPageCount1--;
						}
					}
				} else {
					totalPageCount1 = totalCount1 / 4;
					if (NUMBER_PER_PAGE_DIF > 0) {
						if (totalCount1 % 4 != 0) {
							// 每页分页4个时候的余数
							int test = totalCount1 % 4;
							// 第一页分页特殊情况多显示的数量
							int test1 = NUMBER_PER_PAGE_DIF - 4;
							if (test == test1) {
							} else if (test > test1) {
								totalPageCount1++;
							} else {
								totalPageCount1--;
							}
						}
					} else {
						if (totalCount1 % 4 != 0) {
							totalPageCount1++;
						}
					}
				}*/

				String prefixUrl1 = "hall.jsp?roomId=" + roomId;
				request.setAttribute("paging1", paging1);
				request.setAttribute("backTo1", backTo1);
				request.setAttribute("prefixUrl1", prefixUrl1);
				request.setAttribute("pml", currentPagePml);
			}
		}
		// 取得公聊参数
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "hall.jsp?roomId=" + roomId;
		// + "&amp;backTo="
		// + URLEncoder.encode(backTo);

		// mcq_2006-09-16_从缓存中获取公聊数量_start
		Vector contentIdList = (Vector) RoomCacheUtil
				.getRoomContentCountMap(roomId);
		int totalCount = contentIdList.size();
		// mcq_2006-09-16_从缓存中获取公聊数量_end

		// 判断公聊信息数量
		// mcq_2006-8-23_判断公聊显示数量,如果有私聊就显示5条或者显示10条!不管其他条件_start
		if (totalCount1 > 0) {
			PUBLIC_NUMBER_PER_PAGE = 6;
			/*
			 * if (a >= 4) { PUBLIC_NUMBER_PER_PAGE = PUBLIC_NUMBER_PER_PAGE -
			 * NUMBER_PER_PAGE; } else { PUBLIC_NUMBER_PER_PAGE =
			 * PUBLIC_NUMBER_PER_PAGE - a; }
			 */
		}
		// mcq_2006-8-23_判断公聊显示数量,如果有私聊就显示5条或者显示10条!不管其他条件_end
		PagingBean paging = new PagingBean(this, totalCount, PUBLIC_NUMBER_PER_PAGE, "p");
		
		// 取得要显示的消息列表
		int start = paging.getStartIndex();
		int end = paging.getEndIndex();

		// zhul_2006-08-22 修改聊天显示模式 start
		// mcq_2006-9-6_修改从缓存中读取数据_start
		Vector ml = new Vector();

		for (int i = start; i < end; i++) {
			int roomContentId = ((Integer) contentIdList.get(i)).intValue();
			JCRoomContentBean roomContent = RoomCacheUtil.getRoomContent(roomContentId);
			ml.add(roomContent);
		}

		request.setAttribute("paging", paging);
		// request.setAttribute("start", new Integer(start));
		// request.setAttribute("end", new Integer(end));
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("ml", ml);

		request.setAttribute("room", room);
/*
		// zhul_增加本聊天室聊客功能_2006-08-15 start
		// zhul 2006-09-16 获取本聊天室聊客
		Object[] users = this.getRoomChater(String.valueOf(roomId));
		request.setAttribute("peopleList", users);

		// zhul 2006-09-18 取不在聊天室的用户进行骚扰 start
		Object[] outRoomUsers = this.getOutChatPeop();
		request.setAttribute("userToFaze", outRoomUsers);
		// zhul 2006-09-18 取不在聊天室的用户进行骚扰 end
*/
	}

	/**
	 * 
	 * @author macq
	 * @explain：最近联系人
	 * @datetime:2007-5-22 9:14:21
	 * @param request
	 * @return void
	 */
	public void linkMan(HttpServletRequest request) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		if (loginUser != null) {
			LinkedList linkManList = RoomCacheUtil.getLinkManByIdList(loginUser
					.getId());
			request.setAttribute("linkManList", linkManList);
		}
		request.setAttribute("roomId", roomId);
	}

	/**
	 * fanys 2006-09-12 当有戴帽子的人进入聊天室的时候，公聊频道显示一条信息 macq_2007-8-13_此方法已经废弃绝对不能使用
	 * 
	 * @param roomId
	 */
	// public void addWelcomeCrownOwnerMsg(UserBean loginUser, int roomId) {
	// if (null != loginUser.getUs2().getImageUrl()) {
	// String info = null;
	// UserTopBean black = getTopService().getUserTop(
	// " mark=1 order by priority asc limit 0,1");
	// // String name = StringUtil.toWml(loginUser.getNickName());
	// if (black.getUserId() == loginUser.getId()) {
	// if (loginUser.getGender() == 0)
	// info = "恶名昭著的" + loginUser.getId() + "小姐进入了聊天室";
	// else
	// info = "恶名昭著的" + loginUser.getId() + "先生进入了聊天室";
	// } else {
	// if (loginUser.getGender() == 0)
	// info = "最受欢迎的" + loginUser.getId() + "小姐进入了聊天室";
	// else
	// info = "最受欢迎的" + loginUser.getId() + "先生进入了聊天室";
	// }
	// JCRoomContentBean content = null;
	// content = new JCRoomContentBean();
	// content.setFromId(0);
	// content.setToId(0);
	// content.setFromNickName(loginUser.getNickName());
	// content.setToNickName(loginUser.getId() + "");
	// content.setContent(info);
	// content.setAttach("");
	// content.setIsPrivate(0);
	// content.setRoomId(roomId);
	// content.setSecRoomId(-1);
	// content.setMark(5);
	// // liuyi 2006-09-16 聊天室加缓存 start
	// chatService.addContent(content);
	// // liuyi 2006-09-16 聊天室加缓存 end
	// // RoomCacheUtil.getRoomContentCountMap(roomId);
	// // RoomCacheUtil.flushRoomContentId(roomId);
	// }
	// }
	// /**
	// * wucx 2006-11-7 当恶人榜首进入聊天室的时候，公聊频道显示一条信息
	// *
	// * @param roomId
	// */
	// public void addWelcomeBlackMsg(UserBean loginUser, int roomId) {
	// UserTopBean black = getTopService().getUserTop(
	// " mark=1 order by priority asc limit 0,1");
	// if (black.getUserId() == loginUser.getId()) {
	// String info = null;
	// String name = StringUtil.toWml(loginUser.getNickName());
	// if (loginUser.getNickName() == null
	// || loginUser.getNickName().equals(""))
	// name = StringUtil.toWml(loginUser.getUserName());
	//
	// info = "恶名昭著的" + name + "进入了聊天室";
	// JCRoomContentBean content = null;
	// content = new JCRoomContentBean();
	// content.setFromId(0);
	// content.setToId(0);
	// content.setFromNickName(loginUser.getNickName());
	// content.setToNickName(loginUser.getId() + "");
	// content.setContent(info);
	// content.setAttach("");
	// content.setIsPrivate(0);
	// content.setRoomId(roomId);
	// content.setSecRoomId(-1);
	// content.setMark(5);
	// // liuyi 2006-09-16 聊天室加缓存 start
	// chatService.addContent(content);
	// // liuyi 2006-09-16 聊天室加缓存 end
	// // RoomCacheUtil.getRoomContentCountMap(roomId);
	// // RoomCacheUtil.flushRoomContentId(roomId);
	// }
	// }
	// 显示公聊聊天室记录
	public String getMessageDisplay(JCRoomContentBean content,
			HttpServletRequest request, HttpServletResponse response) {
		if (content == null) {
			return "";
		}
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		StringBuilder sb = new StringBuilder();
		String url = null;
		String actionId = null;
		RankActionBean rankAction = null;
		String rankActionName = null;
		// 自己发送的消息
		if (loginUser != null && content.getFromId() == loginUser.getId()) {
			if (content.getMark() == 1 && content.getIsPrivate() == 0) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getSendMessage();
					rankActionName = rankActionName.replace("XXX",
							getChatContent(roomId, false, content, response));

					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
						sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
							+ "/" + actionId + ".gif\" alt=\"\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			} // macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = response
							.encodeURL("/chat/post.jsp?roomId="
									+ roomId
									+ "&amp;toUserId="
									+ content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = response
							.encodeURL("/chat/post.jsp?roomId="
									+ roomId
									+ "&amp;toUserId="
									+ content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_end
			// zhul2006-09-06 你前面加头冠 start
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			// if (us != null && us.getImageUrl() != null)
			// sb.append("<img src=\"" + us.getImageUrl()
			// + "\" alt=\"loading...\" />");
			if (us != null)
				sb.append(showHatImg ? us.getHatShow() : us.getHatShowText());
			// zhul2006-09-06 你前面加头冠 end
			sb.append("你:");

			// 不是对所有人
			if (content.getToId() != 0) {
				if (content.getIsPrivate() == 1) {
					sb.append(privateNote);
				}
				sb.append(getChatContent(roomId, false, content, response));
				// sb.append("<a href=\"");
				// sb.append(("/chat/post.jsp?roomId=" + roomId + "&amp;toUserId="
				// + content.getToId()));
				// // + "&amp;backTo="+ PageUtil.getBackTo(request))
				// sb.append("\">");
				// sb.append(StringUtil.toWml(content.getToNickName()));
				// sb.append("</a>");
				sb.append(",");
			}
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"x\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
		}
		// 发送给自己的消息
		else if (loginUser != null && loginUser.getId() == content.getToId()) {
			if (content.getMark() == 1 && content.getIsPrivate() == 0) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getReceiveMessage();
					// rankActionName = rankActionName.replace("XXX", "<a
					// href=\""
					// + ("/chat/post.jsp?roomId=" + roomId
					// + "&amp;toUserId=" + content.getFromId()
					// // + "&amp;backTo=" +
					// // PageUtil.getBackTo(request)
					// + "\">"
					// + StringUtil.toWml(content.getFromNickName())
					// + "</a>"));
					rankActionName = rankActionName.replace("XXX",
							getChatContent(roomId, true, content, response));
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
						sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
								+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			} // macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_end
			// sb.append("<a href=\"");
			// sb.append(("/chat/post.jsp?roomId=" + roomId + "&amp;toUserId="
			// + content.getFromId()));
			// // + "&amp;backTo="
			// // + PageUtil.getBackTo(request)));
			// sb.append("\">");
			// sb.append(StringUtil.toWml(content.getFromNickName()));
			// sb.append("</a>");
			sb.append(getChatContent(roomId, true, content, response));
			sb.append(":");
			if (content.getIsPrivate() == 1) {
				sb.append(privateNote);
			}
			// zhul2006-09-06 你前面加头冠 start
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			// if (us != null && us.getImageUrl() != null)
			// sb.append("<img src=\"" + us.getImageUrl()
			// + "\" alt=\"loading...\" />");
			if (us != null)
				sb.append(showHatImg ? us.getHatShow() : us.getHatShowText());
			// zhul2006-09-06 你前面加头冠 end
			sb.append("你,");
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"x\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
		}
		// 其他人的消息
		else {
			if (content.getMark() == 1 && content.getIsPrivate() == 0) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getSendMessage();

					rankActionName = rankActionName.replace("你：",
							getChatContent(roomId, true, content, response) + ":");
					rankActionName = rankActionName.replace("XXX",
							getChatContent(roomId, false, content, response));
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
						sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
								+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			}
			if (content.getMark() == 2) {
				sb.append(StringUtil.toWml(content.getContent()));
				return sb.toString();
			}
			if (content.getMark() == 3) {

				sb.append(getChatContent(roomId, true, content, response));
				sb.append(":");
				String con = StringUtil.toWml(content.getContent());
				url = response
						.encodeURL("/friendadver/friendAdverMessage.jsp?id="
								+ content.getToNickName());
				con = con.replace("应征", "<a href=\"" + url + "\">应征</a>");
				sb.append(con);
				return sb.toString();
			}
			if (content.getMark() == 4) {
				if (content.getFromNickName() != null
						&& loginUser != null
						&& (content.getFromNickName()).equals(loginUser
								.getNickName())) {
					// zhul2006-09-06 你前面加头冠 start
					UserStatusBean us = UserInfoUtil.getUserStatus(loginUser
							.getId());
					// if (us != null && us.getImageUrl() != null)
					
						// sb.append("<img src=\"" + us.getImageUrl()
						// + "\" alt=\"loading...\" />");
					if (us != null)
						sb.append(showHatImg ? us.getHatShow() : us.getHatShowText());
					// zhul2006-09-06 你前面加头冠 end
					sb.append("你:");
				} else {

					sb.append(getChatContent(roomId, true, content, response));
					sb.append(":");
				}
				sb.append(StringUtil.toWml(content.getContent()));
				String tempStr = sb.toString();
				String tietuLink = "<a href=\""
						+ response
								.encodeURL("/tietu/forumMessage.jsp?id="
										+ content.getToNickName())
						// + "&amp;backTo="
						// + PageUtil.getBackTo(request))
						+ "\">看看</a>";
				tempStr = tempStr.replace("看看", tietuLink);
				return tempStr;
			}
			if (content.getMark() == 5) {
				String chatContent = null;
				UserStatusBean us = null;
				us = UserInfoUtil.getUserStatus(Integer.parseInt(content
						.getToNickName()));
				// if (us != null && us.getImageUrl() != null)
				// chatContent = "<img src=\"" + us.getImageUrl()
				// + "\" alt=\"loading...\" />";
				if (us != null)
					chatContent = showHatImg ? us.getHatShow() : us.getHatShowText();
				if (loginUser.getId() == Integer.parseInt(content
						.getToNickName())) {
					chatContent = chatContent
							+ StringUtil.toWml(content.getFromNickName());
				} else {
					chatContent = chatContent
							+ "<a href=\""
							+ ("/chat/post.jsp?roomId=" + roomId
									+ "&amp;toUserId="
									+ content.getToNickName()) + "\">"
							+ StringUtil.toWml(content.getFromNickName())
							+ "</a>";
				}
				String contentReplace = content.getContent();
				contentReplace = contentReplace.replace(
						content.getToNickName(), chatContent);
				sb.append(contentReplace);
				// sb.append(StringUtil.toWml(content.getContent()));
				return sb.toString();
			}

			// zhul 2006-09-29 聊天大厅显示家园更新提示 start
			if (content.getMark() == 6) {
				sb.append("提示");
				sb.append(":");
				String con = StringUtil.toWml(content.getFromNickName());
				url = response
						.encodeURL("/home/home.jsp?userId="
								+ Integer.parseInt(content.getToNickName()));
				con = "<a href=\"" + url + "\">" + con + "</a>"
						+ content.getContent();
				sb.append(con);
				return sb.toString();
			}
			// zhul 2006-09-29 聊天大厅显示家园更新提示 end
			// macq_2006-12-7_聊天大厅显示结婚提示_start
			if (content.getMark() == 7) {
				// 结婚人的用户信息链接
				UserBean fromUser1 = UserInfoUtil.getUser(StringUtil
						.toInt(content.getFromNickName()));
				UserBean toUser1 = UserInfoUtil.getUser(StringUtil
						.toInt(content.getToNickName()));
				if (fromUser1 == null || toUser1 == null || loginUser == null) {

				} else {
					String fromUser = StringUtil.toWml(fromUser1.getNickName());
					if (fromUser1.getId() == loginUser.getId()) {
						fromUser = "您";
					} else {
						url = ("/user/"
								+ "ViewUserInfo.do?userId="
								+ content.getFromNickName());
						fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
					}
					// 结婚人的用户信息链接
					String toUser = StringUtil.toWml(toUser1.getNickName());
					if (toUser1.getId() == loginUser.getId()) {
						toUser = "您";
					} else {
						url = ("/user/"
								+ "ViewUserInfo.do?userId="
								+ content.getToNickName());
						toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
					}
					// 婚礼的链接
					url = ("/friend/"
							+ "redbag.jsp?marriageId=" + content.getContent());
					String marriage = "大家快去吃喜糖啊!";
					marriage = "<a href=\"" + url + "\">" + marriage + "</a>";
					String con = null;
					if (fromUser1.getId() == loginUser.getId()
							|| toUser1.getId() == loginUser.getId()) {
						con = fromUser + "与" + toUser + "的婚礼开始了!";
					} else {
						con = fromUser + "与" + toUser + "的婚礼开始了!" + marriage;
					}
					sb.append(con);
					return sb.toString();
				}
			}
			// macq_2006-12-7_聊天大厅显示结婚提示_end
			// macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_end
			// macq_2006-12-7_聊天大厅显示新增拍卖商品_start
			if (content.getMark() == 9) {
				// 拍卖物品的链接
				url = response
						.encodeURL("/auction/auctionHall.jsp");
				String auction = "拍卖大厅";
				auction = "<a href=\"" + url + "\">" + auction + "</a>"
						+ "开始拍卖新物品了";
				sb.append(auction);
				return sb.toString();
			}

			sb.append(getChatContent(roomId, true, content, response));
			sb.append(":");
			// 不是对所有人
			if (content.getToId() != 0) {
				if (content.getIsPrivate() == 1) {
					sb.append(privateNote);
				}

				sb.append(getChatContent(roomId, false, content, response));
				sb.append(",");
			}
			if (content.getIsPrivate() == 1) {
				sb.append("***");
			} else {
				sb.append(StringUtil.toWml(content.getContent()));
				if (content.getAttach() != null
						&& !content.getAttach().equals("")) {
					sb.append("<a href=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach());
					sb.append("\">");
					if(showSendImg) {
						sb.append("<img src=\"");
						sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
						sb.append(content.getAttach() + "\" alt=\"图\"/>");
					} else 
						sb.append("(图片)");
					sb.append("</a>");
				}
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * fanys 2006-09-05
	 * 
	 * @param roomId
	 * @param fromUserOrToUser
	 *            true 为发送者,false为接收者
	 * @param content
	 * @param response
	 * @param isPrivate 如果是private则表示私聊中现实，不出现帽子
	 * @return
	 */
	public String getChatContent(String roomId, boolean fromUserOrToUser,
			JCRoomContentBean content, HttpServletResponse response) {
		return getChatContent(roomId, fromUserOrToUser, content, response, false);
	}
	public String getChatContent(String roomId, boolean fromUserOrToUser,
			JCRoomContentBean content, HttpServletResponse response, boolean isPrivate) {
		String chatContent = null;
		UserStatusBean us = null;
		if (fromUserOrToUser) {
			if(!isPrivate) {
				us = UserInfoUtil.getUserStatus(content.getFromId());
				if (us != null)
					chatContent = (showHatImg ? us.getHatShow() : us.getHatShowText());
				chatContent += "<a href=\""
					+ ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId()) + "\">";
			} else {
				chatContent = "<a href=\""
					+ ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId()) + "\">";
			}
			chatContent = chatContent
					+ StringUtil.toWml(content.getFromNickName());
			chatContent = chatContent + "</a>";
		} else {
			if(!isPrivate) {
				us = UserInfoUtil.getUserStatus(content.getToId());
				if (us != null)
					chatContent = (showHatImg ? us.getHatShow() : us.getHatShowText());
				chatContent += "<a href=\""
					+ ("/chat/post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId()) + "\">";
			} else {
				chatContent = "<a href=\""
					+ ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId()) + "\">";
			}
			chatContent = chatContent
					+ StringUtil.toWml(content.getToNickName());
			chatContent = chatContent + "</a>";
		}
		return chatContent;
	}

	/**
	 * 
	 * @author macq
	 * @explain：私聊空间
	 * @datetime:2007-6-5 2:30:31
	 * @param roomId
	 * @param fromUserOrToUser
	 * @param content
	 * @param response
	 * @return
	 * @return String
	 */
	public String getPMSpaceChatContent(String roomId,
			boolean fromUserOrToUser, JCRoomContentBean content,
			HttpServletResponse response) {
		String chatContent = null;
		UserStatusBean us = null;
		if (fromUserOrToUser) {
			chatContent = "<a href=\""
					+ ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId()) + "\">";
			us = UserInfoUtil.getUserStatus(content.getFromId());
			if (us != null )
				chatContent = chatContent + us.getHatShow();
			chatContent = chatContent
					+ StringUtil.toWml(content.getFromNickName());
			chatContent = chatContent + "</a>";
		} else {
			chatContent = "<a href=\""
					+ ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId()) + "\">";
			us = UserInfoUtil.getUserStatus(content.getToId());
			if (us != null)
				chatContent = chatContent + us.getHatShow();
			chatContent = chatContent
					+ StringUtil.toWml(content.getToNickName());
			chatContent = chatContent + "</a>";
		}
		return chatContent;
	}

	// mcq_2006-6-20_显示私聊信息的时候增加PK引导_start
	public String getPrivateMessageDisplay(JCRoomContentBean content,
			HttpServletRequest request, HttpServletResponse response) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		StringBuilder sb = new StringBuilder();
		String url = null;
		String actionId = null;
		RankActionBean rankAction = null;
		String rankActionName = null;
		// 自己发送的消息
		if (loginUser != null && content.getFromId() == loginUser.getId()) {
			if (content.getMark() == 1) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getSendMessage();
					if (content.getIsPrivate() == 1) {
						rankActionName = rankActionName.replace("你：",
								"你:" + privateNote);
					}

					rankActionName = rankActionName.replace("XXX",
							getChatContent(roomId, false, content, response, true));
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
						sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
								+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			}
			if (content.getMark() == 2) {
				sb.append(StringUtil.toWml(content.getContent()));
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_end
			
			// zhul2006-09-06 你前面加头冠 start
//			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
//			if (us != null)
//				sb.append(us.getHatShow());
			// zhul2006-09-06 你前面加头冠 end
			
			sb.append("你:");
			// 不是对所有人
			if (content.getToId() != 0) {
				if (content.getIsPrivate() == 1) {
					sb.append(privateNote);
				}

				sb.append(getChatContent(roomId, false, content, response, true));

				sb.append(",");
			}
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"图\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
		}
		// 发送给自己的消息
		else if (loginUser != null && loginUser.getId() == content.getToId()) {
			if (content.getMark() == 1) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getReceiveMessage();
					if (content.getIsPrivate() == 1) {

						rankActionName = rankActionName.replace("XXX：",
								getChatContent(roomId, true, content, response, true)
										+ ":" + privateNote);
					} else {
						rankActionName = rankActionName.replace("XXX：",
								getChatContent(roomId, true, content, response, true) + ":");

					}
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
					sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
							+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("post.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}

			sb.append(getChatContent(roomId, true, content, response, true));

			sb.append(":");
			if (content.getIsPrivate() == 1) {
				sb.append(privateNote);
			}
		// zhul2006-09-06 你前面加头冠 start
//			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
//			if (us != null)
//				sb.append(us.getHatShow());
			// zhul2006-09-06 你前面加头冠 end
			sb.append("你,");
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"图\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
			// zhul_2006-09-04 添加回复功能 start
			sb.append("<a href=\"");
			sb.append(("post.jsp?roomId=" + roomId + "&amp;toUserId="
					+ content.getFromId()));
			sb.append("\">");
			sb.append("回复");
			sb.append("</a>");
			// zhul_2006-09-04 添加回复功能 end
		}

		return sb.toString();
	}

	/**
	 * 
	 * @author macq
	 * @explain： 私聊空间信息显示
	 * @datetime:2007-6-5 2:18:42
	 * @param content
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	public String getPrivateMessageSpaceDisplay(JCRoomContentBean content,
			HttpServletRequest request, HttpServletResponse response) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		StringBuilder sb = new StringBuilder();
		String url = null;
		String actionId = null;
		RankActionBean rankAction = null;
		String rankActionName = null;
		// 自己发送的消息
		if (loginUser != null && content.getFromId() == loginUser.getId()) {
			if (content.getMark() == 1) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getSendMessage();
					if (content.getIsPrivate() == 1) {
						rankActionName = rankActionName.replace("你：",
								"你:" + privateNote);
					}

					rankActionName = rankActionName.replace("XXX",
							getPMSpaceChatContent(roomId, false, content,
									response));
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
					sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
							+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
				}
				return sb.toString();
			}
			if (content.getMark() == 2) {
				sb.append(StringUtil.toWml(content.getContent()));
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_end
			// zhul2006-09-06 你前面加头冠 start
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us != null)
				sb.append(us.getHatShow());
			// zhul2006-09-06 你前面加头冠 end
			sb.append("你:");
			// 不是对所有人
			if (content.getToId() != 0) {
				if (content.getIsPrivate() == 1) {
					sb.append(privateNote);
				}

				sb.append(getPMSpaceChatContent(roomId, false, content,
						response));

				sb.append(",");
			}
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"图\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
		}
		// 发送给自己的消息
		else if (loginUser != null && loginUser.getId() == content.getToId()) {
			if (content.getMark() == 1) {
				actionId = content.getContent();
				rankAction = SendAction.getRankAction(StringUtil.toInt(actionId));
				if (rankAction != null) {
					rankActionName = rankAction.getReceiveMessage();
					if (content.getIsPrivate() == 1) {

						rankActionName = rankActionName.replace("XXX：",
								getPMSpaceChatContent(roomId, true, content,
										response)
										+ ":" + privateNote);
					} else {
						rankActionName = rankActionName.replace("XXX：",
								getPMSpaceChatContent(roomId, true, content, response) + ":");

					}
					sb.append(rankActionName);
					// add by zhangyi for chat face 2006-07-12
					if(showFaceImg)
					sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH
							+ "/" + actionId + ".gif\" alt=\"x\"/>");
					sb.append("(");
					String[] datetime = content.getSendDateTime().split(" ");
					String[] time = datetime[1].split(":");
					StringBuilder newtime = new StringBuilder();
					newtime.append(time[0]);
					newtime.append(":");
					newtime.append(time[1]);
					sb.append(newtime);
					sb.append(")");
					return sb.toString();
				}
			}
			// macq_2006-12-15_聊天大厅显示使用道具结果_start
			if (content.getMark() == 8) {
				// 发送用户信息链接
				String fromUser = StringUtil.toWml(content.getFromNickName());
				if (content.getFromId() == loginUser.getId()) {
					fromUser = "你";
				} else {
					url = ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getFromId());
					fromUser = "<a href=\"" + url + "\">" + fromUser + "</a>";
				}
				// 接受用户信息链接
				String toUser = StringUtil.toWml(content.getToNickName());
				if (content.getToId() == loginUser.getId()) {
					toUser = "你";
				} else {
					url = ("/chat/pmSpace.jsp?roomId=" + roomId
							+ "&amp;toUserId=" + content.getToId());
					toUser = "<a href=\"" + url + "\">" + toUser + "</a>";
				}
				// 显示的信息
				String dummy = content.getContent();
				dummy = dummy.replace("XAX", fromUser);
				dummy = dummy.replace("XBX", toUser);
				if (content.getToId() == loginUser.getId()) {
					dummy = dummy.replace("XCX", "你");
				} else {
					UserBean toUser1 = UserInfoUtil.getUser(content.getToId());
					String gender = toUser1.getGender() == 0 ? "她" : "他";
					dummy = dummy.replace("XCX", gender);
				}
				// 判断是否要显示图片
				if (content != null
						&& !content.getAttach().replace(" ", "").equals("")) {
					dummy = dummy
							+ "<img src=\"/img/chat/"
							+ content.getAttach() + "\" alt=\"x\" />";
				}
				sb.append(dummy);
				return sb.toString();
			}

			sb.append(getPMSpaceChatContent(roomId, true, content, response));

			sb.append(":");
			if (content.getIsPrivate() == 1) {
				sb.append(privateNote);
			}
			// zhul2006-09-06 你前面加头冠 start
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			if (us != null)
				sb.append(us.getHatShow());
			// zhul2006-09-06 你前面加头冠 end
			sb.append("你,");
			sb.append(StringUtil.toWml(content.getContent()));
			if (content.getAttach() != null && !content.getAttach().equals("")) {
				sb.append("<a href=\"");
				sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
				sb.append(content.getAttach());
				sb.append("\">");
				if(showSendImg) {
					sb.append("<img src=\"");
					sb.append(JCRoomContentBean.ATTACH_URL_ROOT);
					sb.append(content.getAttach() + "\" alt=\"图\"/>");
				} else 
					sb.append("(图片)");
				sb.append("</a>");
			}
			sb.append("(");
			String[] datetime = content.getSendDateTime().split(" ");
			String[] time = datetime[1].split(":");
			StringBuilder newtime = new StringBuilder();
			newtime.append(time[0]);
			newtime.append(":");
			newtime.append(time[1]);
			sb.append(newtime);
			sb.append(")");
			// zhul_2006-09-04 添加回复功能 start
			sb.append("<a href=\"");
			sb.append(("/chat/pmSpace.jsp?roomId=" + roomId + "&amp;toUserId="
					+ content.getFromId()));
			sb.append("\">");
			sb.append("回复");
			sb.append("</a>");
			// zhul_2006-09-04 添加回复功能 end
		}

		return sb.toString();
	}

	// mcq_2006-6-20_显示私聊信息的时候增加PK引导_end

	/**
	 * 更多互动功能
	 */
	public void moreFunction(HttpServletRequest request) {
		int toUserId = StringUtil.toInt(request.getParameter("toUserId"));
		if (toUserId <= 0) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "接受用户有误！");
			return;
		}
		UserBean toUser = UserInfoUtil.getUser(toUserId);
		if (toUser == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "接受用户有误！");
			return;
		}
		request.setAttribute("toUser", toUser);
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 发表留言。
	 * 
	 * @param request
	 */
	public void post(HttpServletRequest request) {
		// 进入表单
		int actionId = this.getParameterInt("actionId");
		
		if (isMethodGet() && actionId == 0) {
			int toUserId = StringUtil.toInt(request.getParameter("toUserId"));
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			if (toUserId != -1) {
				request.setAttribute("target", "notNull");
				UserBean toUser = UserInfoUtil.getUser(toUserId);

				if (toUser != null) {
					request.setAttribute("toUser", toUser);
				}
			}
			// 没有目标
			else {
				request.setAttribute("target", "null");
			}
			request.setAttribute("backTo", backTo);
			request.setAttribute("enterMode", "get");
		}
		// 发表留言
		else {
			int roomId = getParameterInt("roomId");

			request.setAttribute("enterMode", "post");
			int toUserId = getParameterInt("toUserId");
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			request.setAttribute("backTo", backTo);
			// macq_2007-5-15_女性聊天室撤销_start
			// libj 2006-07-09 判断是否是女性聊天室 start
			// if ("2".equals(roomId)
			// && loginUser.getGender() == 1
			// && ("joycoolnulluser".equals(to) || request
			// .getParameter("isPrivate") == null)) {
			// request.setAttribute("result", "failure");
			// request.setAttribute("tip", "对不起,本聊天室只允许女性用户发言.请给对方发私聊信息.");
			// return;
			// }
			// libj 2006-07-09 判断是否是女性聊天室 end
			// macq_2007-5-15_女性聊天室撤销_end
			String content = StringUtil.removeCtrlAsc(StringUtil.trim(request.getParameter("content")));
			String isPrivate = request.getParameter("isPrivate");
			
			
			if(actionId == 0) {	//非动作
				// 输入项目检查
				if (content == null) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "请填写内容。");
					return;
				}
				content = content.trim();
				if (content.length() == 0 || content.length() > 100) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "每条信息不能超过100字。至少一个字");
					return;
				}
			}
			if (!isCooldown("chat", 5000)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "你的发言太快了！请先休息一会再继续。");
				return;
			}
			// }
			// zhul 2006-07-06 判断如果一个人发言内容与上次相同，返回提示信息 end
			if (toUserId == 0) {
				String s = request.getParameter("s");
				if(s != null && s.equals("s")) {	//发布喇叭
					synchronized(speakerList) {
						if(speakerList.size() >= 20) {
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "喇叭盒已满，请稍候再发布大喇叭");
							return;
						}
						int count = UserBagCacheUtil.getUserBagItemCount(ChatSpeakerBean.SPEAKER_ITEM,loginUser.getId());
						
						if(count <= 0) {
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "您还没有大喇叭");
							return;
						}
						ChatSpeakerBean bean = new ChatSpeakerBean();
						bean.setUid(loginUser.getId());
						bean.setCreateTime(new Date());
						bean.setMark(0);
						bean.setName(loginUser.getNickName());
						bean.setContent(content);
						
						ChatServiceImpl chatService = new ChatServiceImpl();
						chatService.addChatSpeaker(bean);
						
						int userBagId = UserBagCacheUtil.getUserBagById(
								ChatSpeakerBean.SPEAKER_ITEM, loginUser.getId());
						if(userBagId <= 0) {
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "您还没有大喇叭");
							return;
						}
						// 更新物品使用次数
						UserBagCacheUtil.UseUserBagCacheById(loginUser.getId(),userBagId);
						
						speakerList.addLast(bean);
					}
					request.setAttribute("result", "success");
					
					return;
				} else if(actionId > 0){
					RankActionBean checkAction = SendAction.getRankAction(actionId);
					
					if(checkAction == null) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "没有该动作");
						return;
					}
					
					if(UserBagCacheUtil.getUserBagItemCount(checkAction.getRankId() ,loginUser.getId()) > 0) {
						int useBagId = UserBagCacheUtil.getUserBagById(checkAction.getRankId(),loginUser.getId());
						UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
						if(useBagBean.getTimeLeft() <= 0){
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "该动作卡已经过期");
							return;
						}
					}else {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "您还没有该动作卡");
						return;
					}
					
					JCRoomContentBean jcRoomContent = new JCRoomContentBean();
					jcRoomContent.setContent(checkAction.getSendMessage().replace("$N", loginUser.getNickNameWml()));
					jcRoomContent.setFromId(loginUser.getId());
					if (loginUser.getNickName() == null
							|| loginUser.getNickName().equals("v")
							|| loginUser.getNickName().replace(" ", "").equals(
									"")) {
						jcRoomContent.setFromNickName("乐客" + loginUser.getId());
						// message.setFromNickName(loginUser.getUserName());
					} else {
						jcRoomContent.setFromNickName(loginUser.getNickName());
						// message.setFromNickName(loginUser.getNickName());
					}
					jcRoomContent.setAttach("");
					jcRoomContent.setIsPrivate(0);
					this.addContent(jcRoomContent);
					request.setAttribute("result", "success");
					// mcq_1_增加用户经验值 时间:2006-6-11
					// 增加用户经验值
					RankAction.addPoint(loginUser, Constants.RANK_GENERAL);
					// mcq_end
					return;
					
				} else {
					JCRoomContentBean jcRoomContent = new JCRoomContentBean();
					jcRoomContent.setContent(content);
					jcRoomContent.setFromId(loginUser.getId());
					// MessageBean message = new MessageBean();
					// message.setContent(content);
					// message.setDateTime(DateUtil.getCurrentTimeAsStr());
					// message.setFromUserName(loginUser.getUserName());
					if (loginUser.getNickName() == null
							|| loginUser.getNickName().equals("v")
							|| loginUser.getNickName().replace(" ", "").equals(
									"")) {
						jcRoomContent.setFromNickName("乐客" + loginUser.getId());
						// message.setFromNickName(loginUser.getUserName());
					} else {
						jcRoomContent.setFromNickName(loginUser.getNickName());
						// message.setFromNickName(loginUser.getNickName());
					}
					jcRoomContent.setAttach("");
					jcRoomContent.setIsPrivate(0);
					// zhul_2006-08-22 add for chat model start
					int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
							.getId(), 0, roomId);
					jcRoomContent.setRoomId(secRoomId[0]);
					jcRoomContent.setSecRoomId(secRoomId[1]);
					// zhul_2006-08-22 add for chat model end
					// message.setIsPrivate(0);
					// 添加公聊信息
					// ChatDataAction.addMessage(message);
					// 添加私聊信息
					// ChatDataAction.addPrivateMessage(message);
					// 添加聊天信息信息
					this.addContent(jcRoomContent);
					request.setAttribute("result", "success");
					// mcq_1_增加用户经验值 时间:2006-6-11
					// 增加用户经验值
					RankAction.addPoint(loginUser, Constants.RANK_GENERAL);
					// mcq_end
					return;
				}
			}
			// 对某个用户
			else {
				UserBean toUser = UserInfoUtil.getUser(toUserId);
				if (toUser == null) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "对方不在线。");
					return;
				}
				if (toUserId == loginUser.getId()) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "不能给您自己发信息。");
					return;
				}
				if (loginUser.getId() != 431 && loginUser.getId() != 519610
						&& loginUser.getId() != 914727) {
					if (userService.isUserBadGuy(toUser.getId(), loginUser
							.getId())) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "你在对方的黑名单里，不能给他发送消息！");
						return;
					}
				}
				// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
//				UserBean onlineUser = (UserBean) OnlineUtil
//						.getOnlineBean(toUser.getId() + "");
//				if (onlineUser != null) {
//					if (onlineUser.noticeMark()) {
//						request.setAttribute("result", "failure");
//						request.setAttribute("tip", "对方设置自己状态为免打扰，您不能给他发送消息！");
//						return;
//					}
//				}
				// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_end
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				if(actionId > 0){
					RankActionBean checkAction = SendAction.getRankAction(actionId);
					if(checkAction == null) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "没有该动作");
						return;
					}
					
					if(UserBagCacheUtil.getUserBagItemCount(checkAction.getRankId() ,loginUser.getId()) > 0) {
						int useBagId = UserBagCacheUtil.getUserBagById(checkAction.getRankId(),loginUser.getId());
						UserBagBean useBagBean = UserBagCacheUtil.getUserBagCache(useBagId);
						if(useBagBean.getTimeLeft() <= 0){
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "该动作卡已经过期");
							return;
						}
					}else {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "您还没有该动作卡");
						return;
					}
					jcRoomContent.setContent(checkAction.getReceiveMessage().replace("$N", loginUser.getNickNameWml()).replace("$n", toUser.getNickName()));
				} else {
					jcRoomContent.setContent(content);
				}
				jcRoomContent.setFromId(loginUser.getId());
				// MessageBean message = new MessageBean();
				// message.setContent(content);
				// message.setDateTime(DateUtil.getCurrentTimeAsStr());
				// message.setFromUserName(loginUser.getUserName());
				if (loginUser.getNickName() == null
						|| loginUser.getNickName().equals("v")
						|| loginUser.getNickName().replace(" ", "").equals(
								"")) {
					jcRoomContent.setFromNickName("乐客" + loginUser.getId());
					// message.setFromNickName(loginUser.getUserName());
				} else {
					jcRoomContent.setFromNickName(loginUser.getNickName());
					// message.setFromNickName(loginUser.getNickName());
				}
				jcRoomContent.setToId(toUser.getId());
				// message.setToUserName(toUser.getUserName());
				if (toUser.getNickName() == null
						|| toUser.getNickName().equals("v")
						|| toUser.getNickName().replace(" ", "").equals("")) {
					jcRoomContent.setToId(toUser.getId());
					// message.setToNickName(toUser.getUserName());
				} else {
					jcRoomContent.setToNickName(toUser.getNickName());
					// message.setToNickName(toUser.getNickName());
				}
				if (isPrivate != null) {
					jcRoomContent.setIsPrivate(Integer.parseInt(isPrivate));
					// message.setIsPrivate(1);
				} else {
					jcRoomContent.setIsPrivate(0);
					// message.setIsPrivate(0);
				}
				jcRoomContent.setAttach("");
				// zhul_2006-08-22 add for chat model start
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
						.getId(), toUser.getId(), roomId);
				if (secRoomId[0] == -1 && secRoomId[1] == -1) {
					jcRoomContent.setIsPrivate(1);
					request.setAttribute("privateNotice", "提示："
							+ StringUtil.toWml(toUser.getNickName())
							+ "不在当前聊天室，您的发言转为私聊。");
				}
				jcRoomContent.setRoomId(secRoomId[0]);
				jcRoomContent.setSecRoomId(secRoomId[1]);
				// zhul_2006-08-22 add for chat model end
				request.setAttribute("result", "success");
				// macq_2006-10-22_接收信息用户id_start
				request.setAttribute("toUserId", String.valueOf(toUserId));
				// macq_2006-10-22_接收信息用户id_end
				// 添加公聊信息
				// ChatDataAction.addMessage(message);
				// 添加私聊信息
				// ChatDataAction.addPrivateMessage(message);
				// 添加聊天信息
				this.addContent(jcRoomContent);

				// macq_2007-5-22_添加最近联系人名单_start
				RoomCacheUtil.addLinkManList(loginUser.getId(), toUser.getId());
				// macq_2007-5-22_添加最近联系人名单_end

				// 加入消息系统
				toUser.notice[0]++;
				/*
				NoticeBean notice = new NoticeBean();
				notice.setTitle(NoticeUtil.getChatNoticeTitle(loginUser
						.getNickName(), content));
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setUserId(toUser.getId());
				notice.setHideUrl("/chat/hall.jsp");
				notice.setLink("/chat/hall.jsp?roomId=" + roomId);
				// macq_2007-5-16_增加聊天消息类型_start
				notice.setMark(NoticeBean.CHAT);
				// macq_2007-5-16_增加聊天消息类型_end
				NoticeUtil.addNotice(notice);
				*/
				// mcq_1_增加用户经验值 时间:2006-6-11
				// 增加用户经验值
				RankAction.addPoint(loginUser, Constants.RANK_GENERAL);
				// mcq_end
				return;
			}
		}
	}

	/**
	 * 
	 * @author macq
	 * @explain： 私聊空间
	 * @datetime:2007-5-30 8:55:16
	 * @param request
	 * @return void
	 */
	public void pmSpace(HttpServletRequest request) {
		// 进入表单
		if (request.getMethod().equalsIgnoreCase("get")) {
			String to = request.getParameter("to");
			int toUserId = StringUtil.toInt(request.getParameter("toUserId"));
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			// 有目标
			if (to != null) {
				request.setAttribute("target", "notNull");
				UserBean toUser = userService.getUser("user_name = '" + StringUtil.toSql(to)
						+ "'");
				if (toUser != null) {
					request.setAttribute("toUser", toUser);
				}
			}
			// 用户ID
			else if (toUserId != -1) {
				request.setAttribute("target", "notNull");
				UserBean toUser = null;
				if (toUser == null) {
					// IUserService userService = ServiceFactory
					// .createUserService();
					// toUser = userService.getUser("id = " + toUserId);
					// zhul 2006-10-12_优化用户信息查询
					toUser = UserInfoUtil.getUser(toUserId);
				}
				if (toUser != null) {
					request.setAttribute("toUser", toUser);
				}
			}
			// 没有目标
			else {
				request.setAttribute("target", "null");
			}
			request.setAttribute("backTo", backTo);
			request.setAttribute("enterMode", "get");
			LinkedList linkManList = RoomCacheUtil.getLinkManByIdList(loginUser
					.getId());
			int listSize = linkManList.size();
			int startIndex = listSize - 1;
			int endIndex = 0;
			if (listSize > 4) {
				endIndex = listSize - 4;
			}
			LinkedList linkManList1 = new LinkedList();
			for (int i = startIndex; i >= endIndex; i--) {
				Integer userId = (Integer) linkManList.get(i);
				linkManList1.add(userId);
			}
			request.setAttribute("linkManList", linkManList1);
		}
		// 发表留言
		else {
			String roomId = (String) request.getParameter("roomId");
			if (roomId == null) {
				roomId = "0";
			}
			request.setAttribute("enterMode", "post");
			String to = request.getParameter("to");
			String backTo = request.getParameter("backTo");
			if (backTo == null) {
				backTo = BaseAction.INDEX_URL;
			}
			request.setAttribute("backTo", backTo);
			// macq_2007-5-15_女性聊天室撤销_start
			// libj 2006-07-09 判断是否是女性聊天室 start
			// if ("2".equals(roomId)
			// && loginUser.getGender() == 1
			// && ("joycoolnulluser".equals(to) || request
			// .getParameter("isPrivate") == null)) {
			// request.setAttribute("result", "failure");
			// request.setAttribute("tip", "对不起,本聊天室只允许女性用户发言.请给对方发私聊信息.");
			// return;
			// }
			// libj 2006-07-09 判断是否是女性聊天室 end
			// macq_2007-5-15_女性聊天室撤销_end
			String content = StringUtil.trim(request.getParameter("content"));
			String isPrivate = request.getParameter("isPrivate");
			// 判断脏话
			Vector contentlist = ContentList.getContentList();
			if (contentlist != null) {
				// if (contentlist != null && roomId.equals("0")) {
				int count = contentlist.size();
				String conName = null;
				for (int i = 0; i < count; i++) {
					conName = (String) contentlist.get(i);
					if (content.contains(conName)) {
						if (to != null && "joycoolnulluser".equals(to)) {
							request.setAttribute("result", "failure");
							request.setAttribute("tip", "请注意您的发言内容。");
							return;
						}
						isPrivate = "1";
						// zhul 2006-09-30 如果用户说脏话就转为私聊
						// isPrivate="1";
						// content = content.replace(conName, "***");
						chatService
								.addForBID(String.valueOf(loginUser.getId()));
						// chatService.updateOnlineUser("room_id=1", "user_id="
						// + loginUser.getId());
						// // zhul 当用户换房间时进行房间转换记录 start
						// this.dealRoomTransform(loginUser, Integer
						// .parseInt(roomId), 1);
						// // zhul 当用户换房间时进行房间转换记录 end

						// zhul_2006-08-09 modify 挂线 start
						// fanys 2006-09-16 start
						// chatService.addOnlineUser("1," + loginUser.getId()
						// + ",now()");
						RoomUtil.addRoomOnlineUser(1, loginUser.getId());
						// fanys 2006-09-16 end
						this.dealRoomTransform(loginUser, -1, 1);
						// zhul_2006-08-09 modify 挂线 end
						roomId = "1";
						session.setAttribute("oldRoomId", "1");
						break;
					}
				}
			}
			// 输入项目检查
			if (content == null || content.replace(" ", "").equals("")) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "请填写内容。");
				return;
			}

			// zhul 2006-07-06 判断如果一个人在同一个聊天室发言内容与上次相同，返回提示信息 start
			// if(Integer.parseInt(roomId)==0)
			// {
			if (isPrivate == null || to.equals("joycoolnulluser")) {
				String lastContent = (String) session.getAttribute("lastContent");
				String lastRoomId = (String) session.getAttribute("lastRoomId");
				if (lastContent != null && lastRoomId.equals(roomId)) {
					if (lastContent.equals(content)
							|| lastContent.indexOf(content) != -1
							|| (content).indexOf(lastContent) != -1) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "包含相同的内容，不能连续发两遍.");
						return;
					}
				}
				session.setAttribute("lastRoomId", roomId);
				session.setAttribute("lastContent", content);
			}
			// }
			// zhul 2006-07-06 判断如果一个人发言内容与上次相同，返回提示信息 end

			if (to == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "请选择接收者。");
				return;
			}
			// 对所有人
			else if ("joycoolnulluser".equals(to)) {
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setContent(content);
				jcRoomContent.setFromId(loginUser.getId());
				// MessageBean message = new MessageBean();
				// message.setContent(content);
				// message.setDateTime(DateUtil.getCurrentTimeAsStr());
				// message.setFromUserName(loginUser.getUserName());
				if (loginUser.getNickName() == null
						|| loginUser.getNickName().equals("v")
						|| loginUser.getNickName().replace(" ", "").equals(
								"")) {
					jcRoomContent.setFromNickName("乐客" + loginUser.getId());
					// message.setFromNickName(loginUser.getUserName());
				} else {
					jcRoomContent.setFromNickName(loginUser.getNickName());
					// message.setFromNickName(loginUser.getNickName());
				}
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				// zhul_2006-08-22 add for chat model start
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
						.getId(), 0, Integer.parseInt(roomId));
				jcRoomContent.setRoomId(secRoomId[0]);
				jcRoomContent.setSecRoomId(secRoomId[1]);
				// zhul_2006-08-22 add for chat model end
				// message.setIsPrivate(0);
				// 添加公聊信息
				// ChatDataAction.addMessage(message);
				// 添加私聊信息
				// ChatDataAction.addPrivateMessage(message);
				// 添加聊天信息信息
				this.addContent(jcRoomContent);
				request.setAttribute("result", "success");
				// mcq_1_增加用户经验值 时间:2006-6-11
				// 增加用户经验值
				RankAction.addPoint(loginUser, Constants.RANK_GENERAL);
				// mcq_end
				return;
			}
			// 对某个用户
			else {
				int toUserId = getParameterInt("toUserId");
				UserBean toUser = UserInfoUtil.getUser(toUserId);
				if (toUser == null) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "对方不在线。");
					return;
				}
				if (toUserId == loginUser.getId()) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "不能给您自己发信息。");
					return;
				}
				if (loginUser.getId() != 431 && loginUser.getId() != 519610
						&& loginUser.getId() != 914727) {
					if (userService.isUserBadGuy(toUser.getId(), loginUser
							.getId())) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "你在对方的黑名单里，不能给他发送消息！");
						return;
					}
				}
				// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_start
				UserBean onlineUser = (UserBean) OnlineUtil
						.getOnlineBean(toUser.getId() + "");
				if (onlineUser != null) {
					if (onlineUser.noticeMark()) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "对方设置自己状态为免打扰，您不能给他发送消息！");
						return;
					}
				}
				// macq_2006-10-22_判断接收方是否在线,如在线并判断是否开启免骚扰功能_end
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setContent(content);
				jcRoomContent.setFromId(loginUser.getId());
				// MessageBean message = new MessageBean();
				// message.setContent(content);
				// message.setDateTime(DateUtil.getCurrentTimeAsStr());
				// message.setFromUserName(loginUser.getUserName());
				if (loginUser.getNickName() == null
						|| loginUser.getNickName().equals("v")
						|| loginUser.getNickName().replace(" ", "").equals(
								"")) {
					jcRoomContent.setFromNickName("乐客" + loginUser.getId());
					// message.setFromNickName(loginUser.getUserName());
				} else {
					jcRoomContent.setFromNickName(loginUser.getNickName());
					// message.setFromNickName(loginUser.getNickName());
				}
				jcRoomContent.setToId(toUser.getId());
				// message.setToUserName(toUser.getUserName());
				if (toUser.getNickName() == null
						|| toUser.getNickName().equals("v")
						|| toUser.getNickName().replace(" ", "").equals("")) {
					jcRoomContent.setToId(toUser.getId());
					// message.setToNickName(toUser.getUserName());
				} else {
					jcRoomContent.setToNickName(toUser.getNickName());
					// message.setToNickName(toUser.getNickName());
				}
				if (isPrivate != null) {
					jcRoomContent.setIsPrivate(Integer.parseInt(isPrivate));
					// message.setIsPrivate(1);
				} else {
					jcRoomContent.setIsPrivate(0);
					// message.setIsPrivate(0);
				}
				jcRoomContent.setAttach("");
				// zhul_2006-08-22 add for chat model start
				int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
						.getId(), toUser.getId(), Integer.parseInt(roomId));
				if (secRoomId[0] == -1 && secRoomId[1] == -1) {
					jcRoomContent.setIsPrivate(1);
					request.setAttribute("privateNotice", "提示："
							+ StringUtil.toWml(toUser.getNickName())
							+ "不在当前聊天室，您的发言转为私聊。");
				}
				jcRoomContent.setRoomId(secRoomId[0]);
				jcRoomContent.setSecRoomId(secRoomId[1]);
				// zhul_2006-08-22 add for chat model end
				request.setAttribute("result", "success");
				// macq_2006-10-22_接收信息用户id_start
				request.setAttribute("toUserId", to);
				// macq_2006-10-22_接收信息用户id_end
				// 添加公聊信息
				// ChatDataAction.addMessage(message);
				// 添加私聊信息
				// ChatDataAction.addPrivateMessage(message);
				// 添加聊天信息
				this.addContent(jcRoomContent);

				// macq_2007-5-22_添加最近联系人名单_start
				RoomCacheUtil.addLinkManList(loginUser.getId(), toUser.getId());
				// macq_2007-5-22_添加最近联系人名单_end

				// 加入消息系统
				NoticeBean notice = new NoticeBean();
				notice.setTitle(NoticeUtil.getChatNoticeTitle(loginUser
						.getNickName(), content));
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setUserId(toUser.getId());
				notice.setHideUrl("/chat/hall.jsp");
				notice.setLink("/chat/pmSpace.jsp?toUserId=" + loginUser.getId()
						+ "&amp;roomId=" + roomId);
				// macq_2007-5-16_增加聊天消息类型_start
				notice.setMark(NoticeBean.CHAT);
				// macq_2007-5-16_增加聊天消息类型_end
				NoticeUtil.addNotice(notice);
				// mcq_1_增加用户经验值 时间:2006-6-11
				// 增加用户经验值
				RankAction.addPoint(loginUser, Constants.RANK_GENERAL);
				// mcq_end
				return;
			}
		}
	}

	// 通过JCRoomContentBean添加聊天室聊天信息
	public void addContent(JCRoomContentBean jcRoomContent) {
		if (jcRoomContent.getToNickName() == null) {
			jcRoomContent.setToNickName("");
		}
		// liuyi 2006-09-16 聊天室加缓存 start
		jcRoomContent.setMark(0);
		// chatService.addContent(jcRoomContent.getFromId() + ","
		// + jcRoomContent.getToId() + ",'"
		// + jcRoomContent.getFromNickName() + "','"
		// + jcRoomContent.getToNickName() + "','"
		// + jcRoomContent.getContent() + "','"
		// + jcRoomContent.getAttach() + "',now(),"
		// + jcRoomContent.getIsPrivate() + ","
		// + jcRoomContent.getRoomId() + ","
		// + jcRoomContent.getSecRoomId() + ",0");
		chatService.addContent(jcRoomContent);
		// liuyi 2006-09-16 聊天室加缓存 end

		// 清空聊天室在map中的记录
		// RoomCacheUtil.flushRoomContentId(jcRoomContent.getRoomId());
		// RoomCacheUtil.flushRoomContentId(jcRoomContent.getSecRoomId());
		// 清空聊天室在map中的记录
		// zhul 2006-07-06 start 如果此信息为公聊信息，将此房间的公聊信息数+1
/*		if (jcRoomContent.getIsPrivate() == 0) {
			JCRoomContentCountBean roomContentCount = chatService
					.getJCRoomContentCount("room_id="
							+ jcRoomContent.getRoomId());
			if (roomContentCount == null) {
				roomContentCount = new JCRoomContentCountBean();
				roomContentCount.setRoomId(jcRoomContent.getRoomId());
				chatService.addJCRoomContentCount(roomContentCount);
			}
			chatService.updateJCRoomContentCount("count=count+1", "room_id="
					+ jcRoomContent.getRoomId());
		}*/
		// zhul 2006-07-06 end
	}

	/**
	 * 准备投诉数据。
	 * 
	 * @param request
	 */
	public void compain(HttpServletRequest request) {
		int userId = getParameterInt("userId");
		// IUserService userService = ServiceFactory.createUserService();
		// UserBean toUser = userService.getUser("id = " + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean toUser = UserInfoUtil.getUser(userId);
		request.setAttribute("toUser", toUser);
		request.getSession().setAttribute("compain", "compain");
	}

	/**
	 * 投诉处理。
	 * 
	 * @param request
	 */
	public void compainr(HttpServletRequest request) {
		String tip = null;
		String result = "success";
		int Id = getParameterInt("id");
		String content = request.getParameter("content");
		if (content.equals("")) {
			tip = "请输入投诉内容";
			result = "failure";
			request.setAttribute("result", result);
			request.setAttribute("tip", tip);
			return;
		}
		chatService.addJCRoomCompain(loginUser.getId() + "," + Id + ",'"
				+ content + "',0,now()");
		result = "success";
		request.setAttribute("result", result);
	}

	// fanys 2006-07-03 start
	// 通过条件得到聊天室人数列表
	public void getOnlineList(HttpServletRequest request,
			HttpServletResponse response) {
		Vector vecOnline = new Vector();

		int pageIndex = 0;
		int roomId = 0;

		int totalPages = 0;
		int totalUserCount = 0;
		int startIndex = 0;
		int endIndex = 0;
		int gender = Integer.parseInt((String) request.getAttribute("gender"));

		if (request.getParameter("roomId") != null)
			roomId = Integer.parseInt(request.getParameter("roomId"));
		String prefixUrl;
		if (gender == 0)
			prefixUrl = "onlineListWoman.jsp?roomId=" + roomId;
		else
			prefixUrl = "onlineListMan.jsp?roomId=" + roomId;
		if (request.getParameter("pageIndex") != null)
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));

		StringBuilder strsql = new StringBuilder();
		// 如果该用户的地区编码不为0，也即该用户的地区名称不为空
		List roomOnlineUserList = RoomCacheUtil.getRoomOnlineUserIdList(roomId);

		if (loginUser.getCityno() != 0) {

			// 先得到本地区的用户列表
			int localOnlineUserCount = 0;
			int nonLocalOnlineUserCount = 0;

			// 本地区总人数
			// strsql
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where
			// jc_room_online.room_id=");
			// strsql.append(roomId);
			// strsql.append(" and user_info.gender=");
			// strsql.append(gender);
			// strsql.append(" and user_info.cityno=");
			// strsql.append(loginUser.getCityno());
			// strsql.append(" and user_info.id!=");
			// strsql.append(loginUser.getId());
			// localOnlineUserCount =
			// userService.getUserCount(strsql.toString());
			// 非本地区总人数
			// strsql = new StringBuilder();
			// strsql
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where
			// jc_room_online.room_id=");
			// strsql.append(roomId);
			// strsql.append(" and user_info.gender=");
			// strsql.append(gender);
			// strsql.append(" and cityno!=");
			// strsql.append(loginUser.getCityno());
			// strsql.append(" and user_info.id!=");
			// strsql.append(loginUser.getId());
			// nonLocalOnlineUserCount =
			// userService.getUserCount(strsql.toString());
			int userId = 0;
			UserBean userBean = null;
			Vector localOnlineUsers = new Vector();
			Vector nonLocalOnlineUsers = new Vector();
			for (int i = 0; i < roomOnlineUserList.size(); i++) {
				userId = ((Integer) roomOnlineUserList.get(i)).intValue();
				userBean = UserInfoUtil.getUser(userId);

				if (userBean == null) {
					continue;
				}

				if (userBean.getGender() == gender
						&& userBean.getCityno() == loginUser.getCityno()
						&& userBean.getId() != loginUser.getId()) {
					localOnlineUserCount++;
					localOnlineUsers.add(userBean);
				} else if (userBean.getGender() == gender
						&& userBean.getCityno() != loginUser.getCityno()
						&& userBean.getId() != loginUser.getId()) {
					nonLocalOnlineUserCount++;
					nonLocalOnlineUsers.add(userBean);
				}
			}

			totalUserCount = localOnlineUserCount + nonLocalOnlineUserCount;
			totalPages = totalUserCount / ONLINE_NUMBER_PER_PAGE_FRIEND;
			if (totalUserCount % ONLINE_NUMBER_PER_PAGE_FRIEND != 0)
				totalPages++;
			pageIndex = getCurIndex(totalPages, pageIndex);
			startIndex = pageIndex * ONLINE_NUMBER_PER_PAGE_FRIEND;
			endIndex = Math.min(
					(pageIndex + 1) * ONLINE_NUMBER_PER_PAGE_FRIEND,
					totalUserCount);
			String limit = " limit " + startIndex + ","
					+ ONLINE_NUMBER_PER_PAGE_FRIEND;
			// StringBuilder strsql1 = new StringBuilder();
			// strsql1
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where
			// jc_room_online.room_id=");
			// strsql1.append(roomId);
			// strsql1.append(" and user_info.gender=");
			// strsql1.append(gender);
			// strsql1.append(" and user_info.cityno=");
			// strsql1.append(loginUser.getCityno());
			// strsql1.append(" and user_info.id!=");
			// strsql1.append(loginUser.getId());
			// strsql1.append(" limit ");
			// strsql1.append(startIndex);
			// strsql1.append(",");
			// strsql1.append(ONLINE_NUMBER_PER_PAGE_FRIEND);

			// StringBuilder strsql2 = new StringBuilder();
			//
			// strsql2
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where
			// jc_room_online.room_id=");
			// strsql2.append(roomId);
			// strsql2.append(" and user_info.gender=");
			// strsql2.append(gender);
			// strsql2.append(" and user_info.cityno!=");
			// strsql2.append(loginUser.getCityno());
			// strsql2.append(" and user_info.id!=");
			// strsql2.append(loginUser.getId());

			// 分页处理
			if (startIndex > localOnlineUserCount) {
				// strsql2.append(" limit ");
				// strsql2.append(startIndex - localOnlineUserCount);
				// strsql2.append(",");
				// strsql2.append(ONLINE_NUMBER_PER_PAGE_FRIEND);
				// vecOnline = userService.getUserList(strsql2.toString());

				int fromIndex = Math.min(startIndex - localOnlineUserCount,
						nonLocalOnlineUsers.size());
				int toIndex = Math.min(startIndex - localOnlineUserCount
						+ ONLINE_NUMBER_PER_PAGE_FRIEND, nonLocalOnlineUsers
						.size());

				for (int i = fromIndex; i < toIndex; i++) {
					UserBean user = (UserBean) nonLocalOnlineUsers.get(i);
					if (user == null)
						continue;

					vecOnline.add(user);
				}
			} else if (startIndex + ONLINE_NUMBER_PER_PAGE_FRIEND < localOnlineUserCount) {
				// vecOnline = userService.getUserList(strsql1.toString());

				int fromIndex = Math.min(startIndex, localOnlineUsers.size());
				int toIndex = Math.min(startIndex
						+ ONLINE_NUMBER_PER_PAGE_FRIEND, localOnlineUsers
						.size());

				for (int i = fromIndex; i < toIndex; i++) {
					UserBean user = (UserBean) localOnlineUsers.get(i);
					if (user == null)
						continue;

					vecOnline.add(user);
				}
			} else {
				// 其它情况
				// vecOnline = userService.getUserList(strsql1.toString());
				// Vector temp = null;
				// strsql2.append(" limit 0,");
				// strsql2.append(ONLINE_NUMBER_PER_PAGE_FRIEND + startIndex
				// - localOnlineUserCount);
				// temp = userService.getUserList(strsql2.toString());
				// for (int i = 0; i < temp.size(); i++) {
				// vecOnline.add(temp.get(i));
				// }

				int fromIndex = 0;
				int toIndex = localOnlineUsers.size();

				for (int i = fromIndex; i < toIndex; i++) {
					UserBean user = (UserBean) localOnlineUsers.get(i);
					if (user == null)
						continue;

					vecOnline.add(user);
				}
				toIndex = Math.min(ONLINE_NUMBER_PER_PAGE_FRIEND + startIndex
						- localOnlineUserCount, nonLocalOnlineUsers.size());
				for (int i = fromIndex; i < toIndex; i++) {
					UserBean user = (UserBean) nonLocalOnlineUsers.get(i);
					if (user == null)
						continue;

					vecOnline.add(user);
				}
			}

		} else {// 如果用户的地区名称为空
			// strsql = new StringBuilder();
			// strsql
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id
			// where jc_room_online.room_id=");
			// strsql.append(roomId);
			// strsql.append(" and user_info.gender=");
			// strsql.append(gender);
			// strsql.append(" and user_info.id!=");
			// strsql.append(loginUser.getId());
			// totalUserCount = userService.getUserCount(strsql.toString());
			// strsql = new StringBuilder();
			// strsql
			// .append(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where
			// jc_room_online.room_id=");
			// strsql.append(roomId);
			// strsql.append(" and user_info.gender=");
			// strsql.append(gender);
			// strsql.append(" and user_info.id!=");
			// strsql.append(loginUser.getId());
			// strsql.append(" limit ");
			// strsql.append(startIndex);
			// strsql.append(",");
			// strsql.append(ONLINE_NUMBER_PER_PAGE_FRIEND);
			// vecOnline = userService.getUserList(strsql.toString());
			// 获取对应聊天房间的所有挂线用户ID
			int userId = 0;
			UserBean userBean = null;
			Vector cityNoNull = new Vector();
			// 迭代获取总的满足条件的挂线用户数,和挂线用户信息列表
			for (int i = 0; i < roomOnlineUserList.size(); i++) {
				userId = ((Integer) roomOnlineUserList.get(i)).intValue();
				userBean = UserInfoUtil.getUser(userId);
				if (userBean == null) {
					continue;
				}
				if (userBean.getGender() == gender
						&& userBean.getId() != loginUser.getId()) {
					totalUserCount++;
					cityNoNull.add(userBean);
				}
			}
			totalPages = totalUserCount / ONLINE_NUMBER_PER_PAGE_FRIEND;
			if (totalUserCount % ONLINE_NUMBER_PER_PAGE_FRIEND != 0)
				totalPages++;
			pageIndex = getCurIndex(totalPages, pageIndex);
			startIndex = pageIndex * ONLINE_NUMBER_PER_PAGE_FRIEND;
			int maxAndMin = startIndex + ONLINE_NUMBER_PER_PAGE_FRIEND;
			if (maxAndMin > cityNoNull.size()) {
				maxAndMin = cityNoNull.size();
			}
			for (int i = startIndex; i < maxAndMin; i++) {
				userBean = (UserBean) cityNoNull.get(i);
				vecOnline.add(userBean);
			}
		}
		request.setAttribute("onlineCount", totalUserCount + "");
		String pagination = pagination(totalPages, pageIndex, prefixUrl,
				"pageIndex", response);
		request.setAttribute("pagination", pagination);
		request.setAttribute("vecOnline", vecOnline);

	}

	// fanys 2006-06-23 end

	// 通过条件得到用户信息
	public UserBean getUser(String condition) {
		UserBean user = userService.getUser(condition);
		return user;
	}

	// 通过条件得到聊天室名称
	public JCRoomBean getRoomName(String condition) {
		JCRoomBean room = chatService.getJCRoom(condition);
		return room;
	}

	// 显示聊天室人数
	public int getOnlinecount(String condition) {
		int count = 0;
		count = chatService.getOnlineCount(condition);
		return count;
	}

	// 数字分页
	public String shuzifenye(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;pageIndex1=";
		} else {
			prefixUrl += "?pageIndex1=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

	// 数字分页
	public String shuzifenyeNew(int totalPageCount, int currentPageIndex,
			String prefixUrl, boolean addAnd, String separator,
			HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;pageIndex3=";
		} else {
			prefixUrl += "?pageIndex3=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / 3) * 3;
		int endIndex = (currentPageIndex / 3 + 1) * 3 - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb.append("<a href=\""
					+ (prefixUrl + (startIndex - 1)));
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i));
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param totalPageCount
	 *            总页数
	 * @param currentPageIndex
	 *            当前页号 0对应第1页
	 * @param prefixUrl
	 *            jsp页面地址，例如roomList.jsp
	 * @param pageParamName
	 *            就是网页之间传递的参数名
	 * @param addAnd
	 *            参数方式，是第一个参数呢？还是以后的参数，第一个参数应该以？开头，后面的参数以&开头
	 * @param
	 *            endUrl在参数pageParamName后面的其它参数，如传&amp;backTo="/chat/hall.jsp?roomId=85"
	 * @param separator
	 *            页之间分隔符号,如以”|“为分隔符号，”1|2|3|4|5“
	 * @param response
	 *            页面的相应，传递当前jsp的response即可
	 * 
	 * @return
	 */
	public String pagination(int totalPageCount, int currentPageIndex,
			String prefixUrl, String pageParamName, boolean addAnd,
			String separator, String endUrl, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder();

		if (addAnd) {
			prefixUrl += "&amp;" + pageParamName + "=";
		} else {
			prefixUrl += "?" + pageParamName + "=";
		}

		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (currentPageIndex / PAGE_COUNT) * PAGE_COUNT;
		int endIndex = (currentPageIndex / PAGE_COUNT + 1) * PAGE_COUNT - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}

		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}

		if (hasPrevPage == 1) {
			sb
					.append("<a href=\""
							+ (prefixUrl + (startIndex - 1))
							+ endUrl);
			sb.append("\">&lt;&lt;</a>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != currentPageIndex) {
				sb.append("<a href=\"" + (prefixUrl + i)
						+ endUrl);
				sb.append("\">" + (i + 1) + "</a>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			sb.append("<a href=\""
					+ (prefixUrl + (endIndex + 1)));
			sb.append("\">&gt;&gt;</a>");
		}

		return sb.toString();
	}

	// mcq_2006-6-27_聊天室列表_start
	/**
	 * 聊天室列表
	 * 
	 * @param request
	 */
	public void roomList(HttpServletRequest request) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		// 推荐聊天室
		Vector top = chatService
				.getJCRoomList("mark=1 order by current_online_count desc,max_online_count desc limit 0,2");
		request.setAttribute("top", top);

		// 不需要认证的聊天室数量
		// int totalCount1 = chatService.getJCRoomCount("grant_mode=0 order by
		// current_online_count desc,max_online_count desc limit 0,3");
		Vector room1 = chatService
				.getJCRoomList("grant_mode=0 order by current_online_count desc,max_online_count desc limit 0,3");
		int totalCount1 = room1.size();
		if (totalCount1 > 0) {
			int pageIndex1 = StringUtil.toInt(request
					.getParameter("pageIndex1"));
			if (pageIndex1 == -1) {
				pageIndex1 = 0;
			}
			String backTo1 = request.getParameter("backTo1");
			if (backTo1 == null) {
				backTo1 = BaseAction.INDEX_URL;
			}
			int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
			if (totalCount1 % NUMBER_PER_PAGE != 0) {
				totalPageCount1++;
			}
			if (pageIndex1 > totalPageCount1 - 1) {
				pageIndex1 = totalPageCount1 - 1;
			}
			if (pageIndex1 < 0) {
				pageIndex1 = 0;
			}

			String prefixUrl1 = "roomList.jsp";
			// + "&amp;backTo=" + URLEncoder.encode(backTo1);

			// 取得要显示的消息列表
			// int start1 = pageIndex1 * NUMBER_PER_PAGE;
			// int end1 = NUMBER_PER_PAGE;
			// Vector room1 = chatService
			// .getJCRoomList("grant_mode=0 order by current_online_count
			// desc,max_online_count desc limit "
			// + start1 + ", " + end1);
			// macq_2007-3-30_只显示3条人气最旺的聊天室_start
			// Vector room1 = chatService
			// .getJCRoomList("grant_mode=0 order by current_online_count
			// desc,max_online_count desc limit 0,3");
			// macq_2007-3-30_只显示3条人气最旺的聊天室_start
			request.setAttribute("totalPageCount1",
					new Integer(totalPageCount1));
			request.setAttribute("pageIndex1", new Integer(pageIndex1));
			request.setAttribute("backTo1", backTo1);
			request.setAttribute("prefixUrl1", prefixUrl1);
			request.setAttribute("room1", room1);
		}

		// 需要认证的聊天室数量
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "roomList.jsp";
		// + "&amp;backTo="
		// + URLEncoder.encode(backTo);

		int totalCount = chatService.getJCRoomCount("grant_mode=1");
		// 判断公聊信息数量
		/*
		 * if (totalCount1 > 0) { int a = totalCount1; if (a >= 4) {
		 * PUBLIC_NUMBER_PER_PAGE = PUBLIC_NUMBER_PER_PAGE - 4; } else {
		 * PUBLIC_NUMBER_PER_PAGE = PUBLIC_NUMBER_PER_PAGE - a; } }
		 */
		int totalPageCount = totalCount / PUBLIC_ROOM_PAGE;
		if (totalCount % PUBLIC_ROOM_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * PUBLIC_ROOM_PAGE;
		int end = PUBLIC_ROOM_PAGE;

		Vector room2 = chatService
				.getJCRoomList("grant_mode=1 order by current_online_count desc,max_online_count desc limit "
						+ start + ", " + end);

		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		// request.setAttribute("start", new Integer(start));
		// request.setAttribute("end", new Integer(end));
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("room2", room2);

		// 所有聊天室列表 按后台指定顺序
		int totalCount3 = chatService.getJCRoomCount("1=1");
		int pageIndex3 = StringUtil.toInt(request.getParameter("pageIndex3"));
		if (pageIndex3 == -1) {
			pageIndex3 = 0;
		}
		String backTo3 = request.getParameter("backTo3");
		if (backTo3 == null) {
			backTo3 = BaseAction.INDEX_URL;
		}
		int totalPageCount3 = totalCount3 / 20;
		if (totalCount3 % 20 != 0) {
			totalPageCount3++;
		}
		if (pageIndex3 > totalPageCount3 - 1) {
			pageIndex3 = totalPageCount3 - 1;
		}
		if (pageIndex3 < 0) {
			pageIndex3 = 0;
		}

		String prefixUrl3 = "roomList.jsp";
		// + "&amp;backTo=" + URLEncoder.encode(backTo1);

		// 取得要显示的消息列表
		int start3 = pageIndex3 * 20;
		int end3 = 20;
		Vector room3 = chatService.getJCRoomList("1=1 order by top desc limit "
				+ start3 + ", " + end3);
		request.setAttribute("totalPageCount3", new Integer(totalPageCount3));
		request.setAttribute("pageIndex3", new Integer(pageIndex3));
		request.setAttribute("backTo3", backTo3);
		request.setAttribute("prefixUrl3", prefixUrl3);
		request.setAttribute("room3", room3);

		return;

	}

	// mcq_2006-6-27_聊天室列表_end

	// zhul 2006-06-27 add method start
	public void getChatRoom(HttpServletRequest request) {

		// 从jc_room中取出用户自建聊天室.分页
		// int OWNER_NUM_PER_PAGE = 3;
		// int ownerTotalCount =
		// chatService.getJCRoomCount("creator_id="+loginUser.getId());
		// int
		// ownerTotalPage=(ownerTotalCount+OWNER_NUM_PER_PAGE-1)/OWNER_NUM_PER_PAGE;
		// int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// if (pageIndex > ownerTotalCount-1) {
		// pageIndex = ownerTotalCount -1;
		// }
		// if (pageIndex < 0 ) {
		// pageIndex = 0;
		// }
		//		
		// String condition="creator_id="+loginUser.getId()+" ORDER BY id LIMIT
		// "+pageIndex*OWNER_NUM_PER_PAGE+","+OWNER_NUM_PER_PAGE;
		// Vector roomList=chatService.getJCRoomList(condition);
		//		
		// request.setAttribute("ownerTotalPage",ownerTotalPage+"");
		// request.setAttribute("pageIndex",pageIndex+"");
		// request.setAttribute("roomList",roomList);

		// 从jc_room_manager中取出用户为管理员的聊天室，分页
		// int MANA_NUM_PER_PAGE= 3;
		// int
		// manaTotalCount=chatService.getJCRoomManagerCount("user_id="+loginUser.getId());
		// int
		// manaTotalPage=(manaTotalCount+MANA_NUM_PER_PAGE-1)/MANA_NUM_PER_PAGE;
		// int pageIndex1=StringUtil.toInt(request.getParameter("pageIndex1"));
		// if(pageIndex1>manaTotalPage-1)
		// {
		// pageIndex1=manaTotalPage-1;
		// }
		// if(pageIndex1<0)
		// {
		// pageIndex1=0;
		// }
		//	
		// String condition="user_id="+loginUser.getId()+" ORDER BY id LIMIT
		// "+pageIndex1*MANA_NUM_PER_PAGE+","+MANA_NUM_PER_PAGE;
		// Vector manaRoomList=chatService.getJCRoomManagerList(condition);
		//
		// request.setAttribute("manaTotalPage",manaTotalPage+"");
		// request.setAttribute("pageIndex1",pageIndex1+"");
		// request.setAttribute("manaRoomList",manaRoomList);

		// 从jc_room中取出用户自建聊天室，不考虑分页
		Vector roomList = chatService.getJCRoomList("creator_id="
				+ loginUser.getId() + " order by id");
		request.setAttribute("roomList", roomList);

		// 从jc_room_manager中取出用户为管理员的聊天室,不考虑分页
		Vector manaRoomList = chatService.getJCRoomManagerList("user_id="
				+ loginUser.getId() + " and mark=0 order by id");
		request.setAttribute("manaRoomList", manaRoomList);
		//
		// 最近去过的3个聊天室
		if (roomList.size() < 1 && manaRoomList.size() < 1) {
			Vector nearlyRoomList = chatService
					.getJCRoomList(" inner join (select room_id from jc_room_content where from_id="
							+ loginUser.getId()
							+ " group by room_id order by id desc limit 0,3) as temp on jc_room.id=temp.room_id ");
			request.setAttribute("nearlyRoomList", nearlyRoomList);
		}
		return;
	}

	public void getRooms(HttpServletRequest request, String curRoomId) {
		/*
		 * // 从jc_room中取出用户自建聊天室，不考虑分页 Vector roomList =
		 * chatService.getJCRoomList("creator_id=" + loginUser.getId() + " order
		 * by id"); int owner = roomList.size(); //
		 * 从jc_room_manager中取出用户为管理员的聊天室,不考虑分页 Vector manaRoomList =
		 * chatService.getJCRoomManagerList("user_id=" + loginUser.getId() + "
		 * and mark=0 order by id"); int mana = manaRoomList.size();
		 * 
		 * if (owner > 3) { while (roomList.size() > 3)
		 * roomList.remove(roomList.size() - 1); request.setAttribute("more",
		 * "1"); } else if ((owner + mana) > 4) { int j = -1; for (int i =
		 * owner; i < 3; i++) { RoomManagerBean roomMana = (RoomManagerBean)
		 * manaRoomList .get(++j); JCRoomBean room = chatService.getJCRoom("id=" +
		 * roomMana.getRoomId()); roomList.add(room); }
		 * request.setAttribute("more", "1"); } else if (owner + mana == 4) {
		 * int j = -1; for (int i = owner; i < 4; i++) { RoomManagerBean
		 * roomMana = (RoomManagerBean) manaRoomList .get(++j); JCRoomBean room =
		 * chatService.getJCRoom("id=" + roomMana.getRoomId());
		 * roomList.add(room); } } else if (owner + mana < 4 && owner + mana >
		 * 0) { for (int i = 0; i < mana; i++) { RoomManagerBean roomMana =
		 * (RoomManagerBean) manaRoomList .get(i); JCRoomBean room =
		 * chatService.getJCRoom("id=" + roomMana.getRoomId());
		 * roomList.add(room); } // JCRoomBean nearlyRoom = chatService //
		 * .getJCRoom("inner join (select room_id from jc_room_content where //
		 * from_id="+loginUser.getId()+" group by room_id order by id desc //
		 * limit 0,1) as temp on jc_room.id=temp.room_id "); //
		 * roomList.add(nearlyRoom); } else { roomList =
		 * chatService.getJCRoomList("id!=1 and id!=" + curRoomId + " order by
		 * rand() limit 0,3"); }
		 */
		int[] roomList = this.getRoomId(StringUtil.toId(curRoomId));
		request.setAttribute("roomList", roomList);
		return;
	}

	// zhul 2006-06-27 add method end

	// mcq_2006-6-27_聊天室查询_start
	public void search(HttpServletRequest request) {
		String roomName = request.getParameter("roomName");
		String nickName = request.getParameter("nickName");
		if (roomName != null) {
			JCRoomBean room = chatService.getJCRoom("name like '%" + StringUtil.toSql(roomName)
					+ "%'");
			if (room == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您查询的聊天室不存在。");
				return;
			}
			request.setAttribute("result", "success");
			request.setAttribute("room", room);
			return;
		} else if (nickName != null) {
			UserBean user = this.getUser("nickname = '" + StringUtil.toSql(nickName) + "'");
			if (user == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您查询的用户昵称不存在。");
				return;
			}
			Vector roomList = chatService.getJCRoomList("creator_id='"
					+ user.getId() + "'");
			if (roomList.size() <= 0) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您查询的用户没有创建聊天室。");
				return;
			}
			request.setAttribute("result", "success");
			request.setAttribute("roomList", roomList);
			return;
		}
	}

	// mcq_2006-6-27_聊天室查询_end
	// fanys 2006-06-28 start
	/**
	 * 设置房间是否为推荐房间
	 * 
	 * @param roomId
	 *            房间ID
	 * @param recommendFlag
	 *            推荐标志位0为不推荐，1为推荐
	 */
	public void setRecommendRoom(int roomId, int recommendFlag) {
		chatService.updateJCRoom(" mark=" + recommendFlag, "id=" + roomId);
	}

	/**
	 * 聊天室列表 与roomList的区别在于获取推荐聊天室的时候取出所有的记录，而不是2条记录
	 * 
	 * @param request
	 */
	public void roomList2(HttpServletRequest request) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		// 推荐聊天室
		Vector top = chatService
				.getJCRoomList("mark=1 order by current_online_count desc,max_online_count desc ");
		request.setAttribute("top", top);

		// 不需要认证的聊天室数量
		int totalCount1 = chatService.getJCRoomCount("grant_mode=0");
		if (totalCount1 > 0) {
			int pageIndex1 = StringUtil.toInt(request
					.getParameter("pageIndex1"));
			if (pageIndex1 == -1) {
				pageIndex1 = 0;
			}
			String backTo1 = request.getParameter("backTo1");
			if (backTo1 == null) {
				backTo1 = BaseAction.INDEX_URL;
			}
			int totalPageCount1 = totalCount1 / NUMBER_PER_PAGE;
			if (totalCount1 % NUMBER_PER_PAGE != 0) {
				totalPageCount1++;
			}

			pageIndex1 = Math.min(totalPageCount1 - 1, pageIndex1);
			pageIndex1 = Math.max(0, pageIndex1);

			String prefixUrl1 = "roomList.jsp?roomId=" + roomId;
			// + "&amp;backTo=" + URLEncoder.encode(backTo1);

			// 取得要显示的消息列表
			int start1 = pageIndex1 * NUMBER_PER_PAGE;
			int end1 = NUMBER_PER_PAGE;
			Vector room1 = chatService
					.getJCRoomList("grant_mode=0 order by current_online_count desc,max_online_count desc limit "
							+ start1 + ", " + end1);
			request.setAttribute("totalPageCount1",
					new Integer(totalPageCount1));
			request.setAttribute("pageIndex1", new Integer(pageIndex1));
			request.setAttribute("backTo1", backTo1);
			request.setAttribute("prefixUrl1", prefixUrl1);
			request.setAttribute("room1", room1);
		}

		// 需要认证的聊天室数量
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "roomList.jsp?roomId=" + roomId;
		// + "&amp;backTo="
		// + URLEncoder.encode(backTo);

		int totalCount = chatService.getJCRoomCount("grant_mode=1");
		// 判断公聊信息数量

		int totalPageCount = totalCount / PUBLIC_ROOM_PAGE;
		if (totalCount % PUBLIC_ROOM_PAGE != 0) {
			totalPageCount++;
		}
		pageIndex = Math.min(totalPageCount - 1, pageIndex);
		pageIndex = Math.max(0, pageIndex);

		// 取得要显示的消息列表
		int start = pageIndex * PUBLIC_ROOM_PAGE;
		int end = PUBLIC_ROOM_PAGE;

		Vector room2 = chatService
				.getJCRoomList("grant_mode=1 order by current_online_count desc,max_online_count desc limit "
						+ start + ", " + end);

		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("room2", room2);
		return;
	}

	// fanys 2006-06-28 end

	/**
	 * 添加聊天室管理员 macq_2006-6-28
	 * 
	 * @param request
	 */
	public void checkAddUser(HttpServletRequest request) {

	}

	/**
	 * 
	 * 显示房间里的人
	 * 
	 * @param request
	 */

	public void viewRoomPeops(HttpServletRequest request,
			HttpServletResponse response) {

		int roomId = getParameterInt("roomId");

		// 如果用户无权进入，直接无信息返回
		// fanys 2006-09-16 start
		if (1 != RoomUtil.getAuthority(loginUser.getId(), roomId, request)) {
			request.setAttribute("tag", "1");
			return;
		}
		// fanys 2006-09-16 end
		// if (!canEntry(loginUser.getId(), Integer.parseInt(roomId))) {
		// request.setAttribute("tag", "1");
		// return;
		// }
		// 分页
		int NUM_PER_PAGE = 10;
		int totalCount = chatService.getOnlineCount("room_id=" + roomId) - 1;
		int totalPage = (totalCount + NUM_PER_PAGE - 1) / NUM_PER_PAGE;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// String condition = " inner join jc_room_online on
		// user_info.id=jc_room_online.user_id where jc_room_online.room_id="
		// + roomId
		// + " and jc_room_online.user_id !="
		// + loginUser.getId()
		// + " ORDER BY user_info.id LIMIT "
		// + pageIndex
		// * NUM_PER_PAGE
		// + "," + NUM_PER_PAGE;
		// Vector peopleList = userService.getUserList(condition);
		// zhul 2006-10-16 优化用户信息查询 start
		String condition = " room_id=" + roomId + " and user_id !="
				+ loginUser.getId() + " ORDER BY user_id  LIMIT " + pageIndex
				* NUM_PER_PAGE + "," + NUM_PER_PAGE;
		Vector peopleList = chatService.getOnlineList(condition);
		// zhul 2006-10-16 优化用户信息查询 end
		request.setAttribute("NUM_PER_PAGE", NUM_PER_PAGE + "");
		request.setAttribute("totalCount", (totalCount + 1) + "");
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("peopleList", peopleList);
		return;

	} // 判断用户是否有权进入房间，如果有权返回true 否则返回flase

	public boolean canEntry(int userId, int roomId) {
		boolean ok = false;
		JCRoomBean jcRoom = chatService.getJCRoom(" id=" + roomId);
		if (jcRoom.getGrantMode() == 1) {
			RoomUserBean rub = chatService.getJCRoomUser("room_id=" + roomId
					+ " and user_id=" + loginUser.getId() + " and status=1");
			if (rub != null && rub.getStatus() == 1)
				ok = true;
		} else {
			ok = true;
		}
		return ok;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public void searchkickUser(HttpServletRequest request) {
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		String kickId = (String) request.getParameter("kickId");
		int userId = StringUtil.toInt(kickId);
		if (userId < 0) {
			request.setAttribute("roomId", roomId);
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请输入用户ID");
			return;
		}
		String condition = " room_id=" + roomId + " and user_id=" + kickId;
		JCRoomBean room = chatService.getJCRoom(" id=" + roomId);
		UserBean user = null;
		if (room.getGrantMode() == 1) {
			// 授权用户
			// user = userService
			// .getUser(" join jc_room_user on user_info.id=jc_room_user.user_id
			// where "
			// + condition + " and status=1");
			// zhul 2006-10-16 优化用户信息查询 start
			RoomUserBean roomUser = chatService.getJCRoomUser(condition
					+ " and status=1");
			user = UserInfoUtil.getUser(roomUser.getUserId());
			// zhul 2006-10-16 优化用户信息查询 end
		} else {// 在线用户
			// user = userService
			// .getUser(" join jc_room_online on
			// user_info.id=jc_room_online.user_id
			// where "
			// + condition);
			// zhul 2006-10-16 优化用户信息查询 start
			JCRoomOnlineBean online = chatService.getOnlineUser(condition);
			user = UserInfoUtil.getUser(online.getUserId());
			// zhul 2006-10-16 优化用户信息查询 end
		}
		request.setAttribute("roomId", roomId);
		request.setAttribute("user", user);
		request.setAttribute("result", "success");
	}

	/**
	 * 从聊天室踢出某个用户 fanys 2006-06-29 start 如果踢人成功返回true，失败返回false 踢人和取消踢人 status=0
	 * 踢人, status＝1取消踢人
	 * 
	 * @param request
	 */

	public boolean kickUser(HttpServletRequest request) {
		int userId = Integer.parseInt(request.getParameter("userId"));
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		int status = Integer.parseInt(request.getParameter("status"));

		// 管理员不允许被踢出
		if (isManager(roomId, userId))
			return false;
		JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		RoomUserBean roomUser = chatService.getJCRoomUser("room_id=" + roomId
				+ " and user_id=" + userId);
		if (room.getGrantMode() == 1) {
			if (roomUser != null) {
				chatService.updateJCRoomUser("status=" + status, "room_id="
						+ roomId + " and user_id=" + userId);
			}
		} else {
			if (status == 0) {// 不需要审批的聊天室，踢人时往jc_room_user加入一条记录
				if (roomUser != null) {
					chatService.updateJCRoomUser("status=" + status, "room_id="
							+ roomId + " and user_id=" + userId);
				} else {
					roomUser = new RoomUserBean();
					roomUser.setRoomId(roomId);
					roomUser.setUserId(userId);
					roomUser.setStatus(0);
					roomUser.setManagerId(loginUser.getId());
					chatService.addJCRoomUser(roomUser);
				}
			} else {// 不需要审批的聊天室,取消踢人时jc_room_user删除一条记录
				chatService.deleteJCRoomUser(" room_id=" + roomId
						+ " and user_id=" + userId);
			}
		}
		if (status == 0) {
			NoticeBean notice = new NoticeBean();
			notice.setUserId(userId);
			notice.setTitle("您已经被踢出" + room.getName() + "聊天室");
			notice.setContent("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setHideUrl("");
			notice.setLink("/chat/hall.jsp");
			NoticeUtil.getNoticeService().addNotice(notice);
		} else {
			NoticeBean notice = new NoticeBean();
			notice.setUserId(userId);
			notice.setTitle("管理员恢复您进入" + room.getName() + "聊天室");
			notice.setContent("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setHideUrl("");
			notice.setLink("/chat/hall.jsp?roomId=" + roomId);
			NoticeUtil.getNoticeService().addNotice(notice);
		}
		RoomUtil.addChangedAuthority(userId, roomId);
		// UserBean user = getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);
		// 加入授权记录
		RoomGrantBean grant = new RoomGrantBean();
		grant.setUserId(userId);
		grant.setRoomId(roomId);
		grant.setManagerId(loginUser.getId());
		grant.setGrantType(status);
		chatService.addJCRoomGrant(grant);
		// request.getSession().setAttribute("oldRoomId", "-1");
		// 被踢出去，加一条记录,status=0为不许进入
		if (status == 0) {
			// 判断用户是否在线,如果在线就删除,不在线直写号外
			// 删除用户
			// fanys 2006-09-16 start
			boolean isExist = RoomUtil.isUserInRoom(roomId, userId);
			if (isExist) {
				RoomUtil.deleteRoomOnlineUser(roomId, userId);
				chatService.updateJCRoom(
						" current_online_count=current_online_count-1", " id="
								+ roomId);
				userService.updateOnlineUser("position_id=0", "user_id="
						+ userId);
			}
			// JCRoomOnlineBean onlineUser =
			// chatService.getOnlineUser("user_id="
			// + userId + " and room_id=" + roomId);
			// if (onlineUser != null) {
			// chatService.deleteOnlineUser("user_id=" + userId
			// + " and room_id=" + roomId);
			// chatService.updateJCRoom(
			// " current_online_count=current_online_count-1", " id="
			// + roomId);
			// userService.updateOnlineUser("position_id=0", "user_id="
			// + userId);
			// }
			// fanys 2006-09-16 end
			// (from_id,to_id,from_nickname,to_nickname,content,attach,send_datetime,is_private,room_id,mark)
			if (roomId != 0) {
				// liuyi 2006-09-16 聊天室加缓存 start
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(0);
				jcRoomContent.setToId(0);
				jcRoomContent.setFromNickName("");
				jcRoomContent.setToNickName("");
				jcRoomContent.setContent("号外：" + user.getNickName() + "被踢出");
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(roomId);
				jcRoomContent.setSecRoomId(-1);
				jcRoomContent.setMark(2);
				// chatService.addContent("0,0,'','','号外：" + user.getNickName()
				// + "被踢出" + "','',now(),0," + roomId + ",-1,2");
				chatService.addContent(jcRoomContent);
				// liuyi 2006-09-16 聊天室加缓存 end
				// 清空聊天室在map中的记录
				// RoomCacheUtil.flushRoomContentId(roomId);
				// 清空聊天室在map中的记录
			}
		}
		RoomUtil.addChangedRoom(roomId);
		return true;
	}

	/**
	 * 判断当前用户是不是聊天室管理员
	 * 
	 * @param request
	 * @return
	 */
	public void isManager(HttpServletRequest request,
			HttpServletResponse response) {
		int roomId = 0;
		if (request.getParameter("roomId") != null)
			roomId = Integer.parseInt(request.getParameter("roomId"));
		if (!isManager(roomId, loginUser.getId())) {
			try {
				// response.sendRedirect(("hall.jsp"));
				BaseAction.sendRedirect("/chat/hall.jsp", response);
				return;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean isManager(int roomId, int userId) {
		RoomManagerBean manager = chatService.getJCRoomManager(" room_id="
				+ roomId + " and user_id=" + userId);
		if (manager != null)
			return true;
		return false;
	}

	public boolean isSuperManager(int roomId, int userId) {
		RoomManagerBean manager = chatService.getJCRoomManager(" room_id="
				+ roomId + " and user_id=" + userId);
		if (manager != null) {
			if (manager.getMark() == 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 向房间管理员提出申请，申请进入聊天室 mcq_2006_6-29_start
	 * 
	 * @param request
	 * @return
	 */
	public void apply(HttpServletRequest request, HttpServletResponse response) {
		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		RoomUserBean roomUser = chatService.getJCRoomUser("room_id=" + roomId
				+ " and user_id=" + loginUser.getId());
		if (roomUser != null) {
			if (roomUser.getStatus() == 2) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您已经申请进入这个聊天室，但管理员尚未审批，请等待审批");
				return;
			} else if (roomUser.getStatus() == 1) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您可以进入该房间");
				return;
			}
		}
		JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		String condition = "select jc_room_online.* from jc_room_online join jc_room_manager on jc_room_online.user_id=jc_room_manager.user_id and jc_room_manager.room_id="
				+ roomId;
		Vector managerOnlineList = chatService.getOnlineList(condition);
		// 初始化消息bean
		NoticeBean notice = null;
		if (managerOnlineList != null) {
			JCRoomOnlineBean online = null;
			for (int i = 0; i < managerOnlineList.size(); i++) {
				online = (JCRoomOnlineBean) managerOnlineList.get(i);
				if (online.getUserId() == loginUser.getId())
					break;
				notice = new NoticeBean();
				notice.setUserId(online.getUserId());
				notice.setTitle("有人请求进入" + room.getName() + "聊天室");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("/chat/manager.jsp");
				notice.setLink("/chat/manager.jsp?roomId=" + room.getId());
				NoticeUtil.getNoticeService().addNotice(notice);
			}
		} else {
			// 得到该房间所有管理员
			Vector managerList = chatService.getJCRoomManagerList("roomid="
					+ roomId);
			// 初始化聊天室管理员bean
			RoomManagerBean manager = null;
			// 给所有该房间管理员发送消息
			for (int i = 0; i < managerList.size(); i++) {
				manager = (RoomManagerBean) managerList.get(i);
				if (manager.getUserId() == loginUser.getId())
					break;
				notice = new NoticeBean();
				notice.setUserId(manager.getUserId());
				notice.setTitle("有人请求进入" + room.getName() + "聊天室");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("/chat/manager.jsp");
				notice.setLink("/chat/manager.jsp?roomId=" + room.getId());
				NoticeUtil.getNoticeService().addNotice(notice);
			}
		}
		if (roomUser == null) {
			RoomUserBean roomUser1 = new RoomUserBean();
			roomUser1.setRoomId(room.getId());
			roomUser1.setUserId(loginUser.getId());
			roomUser1.setStatus(2);
			chatService.addJCRoomUser(roomUser1);
		} else {
			chatService.updateJCRoomUser("status=2", "room_id=" + roomId
					+ " and user_id=" + loginUser.getId());
		}
		request.setAttribute("result", "success");
		RoomUtil.addChangedAuthority(loginUser.getId(), StringUtil
				.toInt(roomId));
		request.setAttribute("tip", "申请成功，请等待管理员响应!");
		return;
	}

	/**
	 * 聊天室房间管理员管理用户申请进入聊天室 mcq_2006_6-29_start
	 * 
	 * @param request
	 * @return
	 */
	public void manager(HttpServletRequest request, HttpServletResponse response) {

		// 取得房间号
		String roomId = (String) request.getParameter("roomId");
		if (roomId == null) {
			roomId = "0";
		}
		// 取得房间信息
		JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		// 判断用户是否用权限做审批权限
		boolean flag = this.isManager(Integer.parseInt(roomId), loginUser
				.getId());
		// 如果用户没有权限进入得话直接返回聊天大厅
		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "您没有权限执行这个操作!");
			return;
		}
		// 判断管理员是否点击操作
		String action = (String) request.getParameter("action");
		if (action != null) {
			// 取得用户Id
			String userId = (String) request.getParameter("userId");
			// 申请通过
			// 自己申请自己批准的情况
			if (Integer.parseInt(userId) == loginUser.getId()) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "您不能自己审批自己!");
				return;
			}
			NoticeBean notice = null;
			RoomGrantBean grant = null;
			if (action.equals("1")) {
				if (room.getGrantMode() == 1) {
					chatService.updateJCRoomUser("status=1,manager_id="
							+ loginUser.getId() + ",grant_datetime=now()",
							"room_id=" + roomId + " and user_id=" + userId);
				} else if (room.getGrantMode() == 0) {
					chatService.deleteJCRoomUser("room_id=" + roomId
							+ " and user_id=" + userId);
				}
				grant = new RoomGrantBean();
				grant.setRoomId(Integer.parseInt(roomId));
				grant.setUserId(Integer.parseInt(userId));
				grant.setManagerId(loginUser.getId());
				grant.setGrantType(1);
				chatService.addJCRoomGrant(grant);
				notice = new NoticeBean();
				notice.setUserId(Integer.parseInt(userId));
				notice.setTitle(room.getName() + "聊天室管理员通过了您的申请");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("");
				notice.setLink("/chat/hall.jsp?roomId=" + room.getId());
				NoticeUtil.getNoticeService().addNotice(notice);
			}
			// 申请不通过
			else if (action.equals("2")) {
				if (room.getGrantMode() == 1) {
					chatService.deleteJCRoomUser("room_id=" + roomId
							+ " and user_id=" + userId);
				} else {
					chatService.updateJCRoomUser("status=0,manager_id="
							+ loginUser.getId() + ",grant_datetime=now()",
							"room_id=" + roomId + " and user_id=" + userId);
				}
				grant = new RoomGrantBean();
				grant.setRoomId(Integer.parseInt(roomId));
				grant.setUserId(Integer.parseInt(userId));
				grant.setManagerId(loginUser.getId());
				grant.setGrantType(0);
				chatService.addJCRoomGrant(grant);
				notice = new NoticeBean();
				notice.setUserId(Integer.parseInt(userId));
				notice.setTitle(room.getName() + "聊天室管理员拒绝了您得申请");
				notice.setContent("");
				notice.setType(NoticeBean.GENERAL_NOTICE);
				notice.setHideUrl("");
				notice.setLink("/chat/hall.jsp");
				NoticeUtil.getNoticeService().addNotice(notice);
			}
			RoomUtil.addChangedAuthority(Integer.parseInt(userId), Integer
					.parseInt(roomId));
		}

		// 判断申请用户信息sql语句
		String condition = "JOIN (select * from jc_room_user  where room_id="
				+ roomId + " and status=2) bb on user_info.id=bb.user_id";
		// 执行查询
		Vector userList = userService.getUserList(condition);
		// 传值到页面
		request.setAttribute("userList", userList);
		request.setAttribute("result", "success");
		return;
	}

	/**
	 * 判断当前用户是不是房间创建者
	 * 
	 * @param request
	 * @return
	 */
	public void isRoomCreator(HttpServletRequest request,
			HttpServletResponse response) {
		JCRoomBean room = null;
		room = chatService.getRoomName(" creator_id=" + loginUser.getId());
		if (room == null) {
			try {
				// response.sendRedirect(("hall.jsp"));
				BaseAction.sendRedirect("/chat/hall.jsp", response);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * zhul 2006-10-13 判断用户是否是聊天室成员
	 * 
	 * @param userId
	 * @param roomId
	 * @return
	 */
	public boolean isRoomMember(String userId, String roomId) {
		RoomUserBean user = chatService.getJCRoomUser(" room_id=" + roomId
				+ " and user_id=" + userId + " and status=1");
		if (user == null)
			return false;
		return true;
	}

	/**
	 * 房间创建者获取用户列表＝管理人员列表＋好友列表＋在线人员列表
	 * 
	 * @param request
	 * @param response
	 */
	public void getUserList2(HttpServletRequest request,
			HttpServletResponse response) {
		int managerCount = 0;// 管理人员总数
		int friendCount = 0;// 踢出用户总数
		int onlineCount = 0;// 在线用户总数
		int curManagerIndex = 0;// 管理人员页号
		int curFriendIndex = 0;// 踢出用户页号
		int curOnlineIndex = 0;// 在线用户页号
		int roomId = 0;// 房间号
		int startIndex = 0;
		int endIndex = 0;
		int totalManagerPages = 0;
		int totalFriendPages = 0;
		int totalOnlinePages = 0;
		Vector vecManager = null;
		Vector vecFriend = new Vector();
		Vector vecOnline = null;
		String condition = "";
		String pagination = "";// 分页
		String prefixUrl = "";

		RoomManagerBean manager = null;
		UserBean friend = null;
		JCRoomOnlineBean onlineUser = null;

		if (request.getParameter("roomId") != null)
			roomId = Integer.parseInt(request.getParameter("roomId"));

		JCRoomBean room = chatService.getJCRoom("id=" + roomId);

		if (request.getParameter("managerIndex") != null) {
			curManagerIndex = Integer.parseInt(request
					.getParameter("managerIndex"));
		}
		if (request.getParameter("friendIndex") != null) {
			curFriendIndex = Integer.parseInt(request
					.getParameter("friendIndex"));
		}
		if (request.getParameter("onlineIndex") != null) {
			curOnlineIndex = Integer.parseInt(request
					.getParameter("onlineIndex"));
		}
		// 管理人员列表
		condition = " room_id=" + roomId + " and user_id!=" + loginUser.getId();

		vecManager = chatService.getJCRoomManagerList(condition);

		// 好友列表
		condition = "user_id=" + loginUser.getId();
		// vecFriend = userService
		// .getUserList(" join user_friend on user_info.id=user_friend.friend_id
		// where user_friend.user_id="
		// + loginUser.getId());
		// zhul 2006-10-17 优化用户好友查询 start
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		for (int i = 0; i < userFriends.size(); i++) {
			String id = (String) userFriends.get(i);
			UserBean ub = UserInfoUtil.getUser(Integer.parseInt(id));
			if (ub != null)
				vecFriend.add(ub);
		}
		// zhul 2006-10-17 优化用户好友查询 end
		// 好友列表里面删除已经是管理员的好友
		for (int i = 0; i < vecManager.size(); i++) {
			manager = (RoomManagerBean) vecManager.get(i);
			for (int j = 0; j < vecFriend.size(); j++) {
				friend = (UserBean) vecFriend.get(j);
				if (friend.getId() == manager.getUserId()) {
					vecFriend.remove(j);
					break;
				}
			}
		}
		// 在线人员列表
		if (room.getGrantMode() == 1) {// 授权聊天室
			condition = " select a.* from jc_room_online a join jc_room_user b on a.user_id=b.user_id and a.room_id =b.room_id where b.room_id="
					+ roomId + " and b.status=1";
		} else {// 非授权聊天室
			condition = " room_id= " + roomId;
		}
		vecOnline = chatService.getOnlineList(condition);
		// 在线用户列表里面删除已经是管理员的在线用户
		for (int i = 0; i < vecManager.size(); i++) {
			manager = (RoomManagerBean) vecManager.get(i);
			for (int j = 0; j < vecOnline.size(); j++) {
				onlineUser = (JCRoomOnlineBean) vecOnline.get(j);
				if (onlineUser.getUserId() == manager.getUserId()) {
					vecOnline.remove(j);
					break;
				}
			}
		}
		// 在线用户列表里面删除已经是好友的在线用户
		for (int i = 0; i < vecFriend.size(); i++) {
			friend = (UserBean) vecFriend.get(i);
			for (int j = 0; j < vecOnline.size(); j++) {
				onlineUser = (JCRoomOnlineBean) vecOnline.get(j);
				if (onlineUser.getUserId() == friend.getId()) {
					vecOnline.remove(j);
					break;
				}
			}
		}
		// 在线用户列表里面删除当前管理员
		for (int i = 0; i < vecOnline.size(); i++) {
			onlineUser = (JCRoomOnlineBean) vecOnline.get(i);
			if (onlineUser.getUserId() == loginUser.getId()) {
				vecOnline.remove(i);
				break;
			}
		}
		// 管理员列表删除超级管理员

		for (int i = 0; i < vecManager.size(); i++) {
			manager = (RoomManagerBean) vecManager.get(i);
			if (manager.getUserId() == room.getCreatorId())
				vecManager.remove(i);
		}
		// 管理人员总数
		managerCount = vecManager.size();
		totalManagerPages = managerCount / NUMBER_PER_PAGE;
		if (managerCount % NUMBER_PER_PAGE != 0) {
			totalManagerPages++;
		}

		// 好友总数
		friendCount = vecFriend.size();
		totalFriendPages = friendCount / NUMBER_PER_PAGE;
		if (friendCount % NUMBER_PER_PAGE != 0) {
			totalFriendPages++;
		}
		// 在线人员总数
		onlineCount = vecOnline.size();
		totalOnlinePages = onlineCount / NUMBER_PER_PAGE;
		if (onlineCount % NUMBER_PER_PAGE != 0) {
			totalOnlinePages++;
		}
		// 管理人员分页
		curManagerIndex = getCurIndex(totalManagerPages, curManagerIndex);
		startIndex = curManagerIndex * NUMBER_PER_PAGE;
		Vector vecTemp = new Vector();
		endIndex = startIndex + NUMBER_PER_PAGE;
		endIndex = Math.min(endIndex, managerCount);
		UserBean tempUser = null;
		for (int i = startIndex; i < endIndex; i++) {
			manager = (RoomManagerBean) vecManager.get(i);
			// tempUser = userService.getUser("id=" + manager.getUserId());
			// zhul 2006-10-12_优化用户信息查询
			tempUser = UserInfoUtil.getUser(manager.getUserId());
			vecTemp.add(tempUser);
		}
		request.setAttribute("vecManager", vecTemp);
		prefixUrl = "roomManager.jsp?roomId=" + roomId + "&amp;friendIndex="
				+ curFriendIndex + "&amp;onlineIndex=" + curOnlineIndex;
		pagination = pagination(totalManagerPages, curManagerIndex, prefixUrl,
				"managerIndex",
				"&amp;backTo=" + request.getParameter("backTo"), response);
		request.setAttribute("managerPagination", pagination);
		// 好友分页
		curFriendIndex = getCurIndex(totalFriendPages, curFriendIndex);
		startIndex = curFriendIndex * NUMBER_PER_PAGE;
		vecTemp = new Vector();
		endIndex = startIndex + NUMBER_PER_PAGE;
		endIndex = Math.min(endIndex, friendCount);
		for (int i = startIndex; i < endIndex; i++) {
			vecTemp.add(vecFriend.get(i));
		}
		request.setAttribute("vecFriend", vecTemp);
		prefixUrl = "roomManager.jsp?roomId=" + roomId + "&amp;managerIndex="
				+ curManagerIndex + "&amp;onlineIndex=" + curOnlineIndex;
		pagination = pagination(totalFriendPages, curFriendIndex, prefixUrl,
				"friendIndex", "&amp;backTo=" + request.getParameter("backTo"),
				response);
		request.setAttribute("friendPagination", pagination);
		// 在线用户分页
		curOnlineIndex = getCurIndex(totalOnlinePages, curOnlineIndex);
		startIndex = curOnlineIndex * NUMBER_PER_PAGE;
		vecTemp = new Vector();
		endIndex = startIndex + NUMBER_PER_PAGE;
		endIndex = Math.min(endIndex, onlineCount);
		for (int i = startIndex; i < endIndex; i++) {
			onlineUser = (JCRoomOnlineBean) vecOnline.get(i);
			// tempUser = userService.getUser("id=" + onlineUser.getUserId());
			// zhul 2006-10-12_优化用户信息查询
			tempUser = UserInfoUtil.getUser(onlineUser.getUserId());
			vecTemp.add(tempUser);
		}
		request.setAttribute("vecOnline", vecTemp);
		prefixUrl = "roomManager.jsp?roomId=" + roomId + "&amp;managerIndex="
				+ curManagerIndex + "&amp;friendIndex=" + curFriendIndex;
		pagination = pagination(totalOnlinePages, curOnlineIndex, prefixUrl,
				"onlineIndex", "&amp;backTo=" + request.getParameter("backTo"),
				response);
		request.setAttribute("onlinePagination", pagination);
		// request.setAttribute("managerIndex",curManagerIndex+"");
		// request.setAttribute("friendIndex",curFriendIndex+"");
		// request.setAttribute("onlineIndex",curOnlineIndex+"");
		if (loginUser.getId() == room.getCreatorId())
			request.setAttribute("totalManager", (managerCount + 1) + "");
		else
			request.setAttribute("totalManager", (managerCount + 2) + "");
		String navigateTo = "roomManagerInfo.jsp?roomId=" + roomId
				+ "&amp;managerIndex=" + curManagerIndex + "&amp;friendIndex="
				+ curFriendIndex + "&amp;onlineIndex=" + curOnlineIndex;
		request.setAttribute("navigateTo", navigateTo);

	}

	public int getCurIndex(int totalPages, int curIndex) {
		curIndex = Math.min(totalPages - 1, curIndex);
		curIndex = Math.max(0, curIndex);
		return curIndex;
	}

	/**
	 * 删除或者添加房间管理员 deleteOrAdd=0 删除管理员, deleteOrAdd＝1 添加管理员
	 * 
	 * @param request
	 * @param response
	 */
	public void deleteOrAddRoomManager(HttpServletRequest request) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end
		int roomId = Integer.parseInt(request.getParameter("roomId"));
		int userId = Integer.parseInt(request.getParameter("userId"));
		int deleteOrAdd = Integer.parseInt(request.getParameter("deleteOrAdd"));
		JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		// UserBean user = getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean user = UserInfoUtil.getUser(userId);

		if (deleteOrAdd == 1) {
			// int gamePoint = loginUser.getUs().getGamePoint();
			int gamePoint = usb.getGamePoint();
			if (gamePoint < 15000) {
				request.setAttribute("result", "failure");
				// request.setAttribute("tip", "您查询的用户没有创建聊天室。");
				return;
			}
			gamePoint = gamePoint - 15000;
			// userService.updateUserStatus("game_point=" + gamePoint,
			// "user_id="
			// + loginUser.getId());
			// zhul_2006-08-11 modify userstatus start
			UserInfoUtil.updateUserStatus("game_point=" + gamePoint, "user_id="
					+ loginUser.getId(), loginUser.getId(),
					UserCashAction.OTHERS, "添加房间管理员扣15000乐币");
			// zhul_2006-08-11 modify userstatus end

			// loginUser.getUs().setGamePoint(gamePoint);

			// add by zhangyi 2006-07-24 for stat user money history start
			MoneyAction.addMoneyFlowRecord(Constants.OTHER, 15000,
					Constants.SUBTRATION, loginUser.getId());
			// add by zhangyi 2006-07-24 for stat user money history end

			RoomManagerBean manager = new RoomManagerBean();
			manager.setUserId(userId);
			manager.setRoomId(roomId);
			chatService.addJCRoomManager(manager);
			NoticeBean notice = new NoticeBean();
			if (room.getGrantMode() == 1) {
				RoomUserBean roomUser = chatService.getJCRoomUser("user_id="
						+ userId + " and room_id=" + roomId);
				if (roomUser == null) {
					roomUser = new RoomUserBean();
					roomUser.setRoomId(roomId);
					roomUser.setUserId(userId);
					roomUser.setManagerId(loginUser.getId());
					roomUser.setStatus(1);
					chatService.addJCRoomUser(roomUser);
				} else {
					if (roomUser.getStatus() != 1) {
						chatService.updateJCRoomUser("status=1,manager_id="
								+ loginUser.getId() + ",grant_datetime=now()",
								"room_id=" + roomId + " and user_id=" + userId);
					}
				}
			}
			notice.setUserId(userId);
			notice.setTitle("您已经被授权为" + room.getName() + "聊天室的管理员");
			notice.setContent("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setHideUrl("");
			notice.setLink("/chat/hall.jsp?roomId=" + roomId);
			NoticeUtil.getNoticeService().addNotice(notice);
			if (roomId != 0) {
				// liuyi 2006-09-16 聊天室加缓存 start
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(0);
				jcRoomContent.setToId(0);
				jcRoomContent.setFromNickName("");
				jcRoomContent.setToNickName("");
				jcRoomContent
						.setContent("号外：" + user.getNickName() + "被授权为管理员");
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(roomId);
				jcRoomContent.setSecRoomId(-1);
				jcRoomContent.setMark(2);
				// chatService.addContent("0,0,'','','号外：" + user.getNickName()
				// + "被授权为管理员" + "','',now(),0," + roomId + ",-1,2");
				chatService.addContent(jcRoomContent);
				// liuyi 2006-09-16 聊天室加缓存 end
				// 清空聊天室在map中的记录
				// RoomCacheUtil.flushRoomContentId(roomId);
				// 清空聊天室在map中的记录
			}
		} else {
			String condition = "user_id=" + userId + " and room_id=" + roomId;
			chatService.deleteJCRoomManager(condition);
			NoticeBean notice = new NoticeBean();
			notice.setUserId(userId);
			notice.setTitle("您在" + room.getName() + "聊天室的管理员权限被取消");
			notice.setContent("");
			notice.setType(NoticeBean.GENERAL_NOTICE);
			notice.setHideUrl("");
			notice.setLink("/chat/hall.jsp?roomId="
					+ roomId);
			NoticeUtil.getNoticeService().addNotice(notice);
			if (roomId != 0) {
				// liuyi 2006-09-16 聊天室加缓存 start
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(0);
				jcRoomContent.setToId(0);
				jcRoomContent.setFromNickName("");
				jcRoomContent.setToNickName("");
				jcRoomContent.setContent("号外：" + user.getNickName()
						+ "的管理员权限被取消");
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(roomId);
				jcRoomContent.setSecRoomId(-1);
				jcRoomContent.setMark(2);
				// chatService.addContent("0,0,'','','号外：" + user.getNickName()
				// + "的管理员权限被取消" + "','',now(),0," + roomId + ",-1,2");
				chatService.addContent(jcRoomContent);
				// liuyi 2006-09-16 聊天室加缓存 end
				// 清空聊天室在map中的记录
				RoomCacheUtil.flushRoomContentId(roomId);
				// 清空聊天室在map中的记录
			}
		}
		OsCacheUtil.flushGroup(OsCacheUtil.ROOM_MANAGERS_GROUP);
		// 加入授权记录
		RoomGrantBean grant = new RoomGrantBean();
		grant.setUserId(userId);
		grant.setRoomId(roomId);
		grant.setManagerId(loginUser.getId());
		if (deleteOrAdd == 0)
			grant.setGrantType(4);
		else
			grant.setGrantType(3);
		chatService.addJCRoomGrant(grant);
		// UserBean admin = this.getUser("id=" + userId);
		// zhul 2006-10-12_优化用户信息查询
		UserBean admin = UserInfoUtil.getUser(userId);

		RoomUtil.addChangedAuthority(userId, roomId);
		request.setAttribute("result", "success");
		request.setAttribute("admin", admin);
	}

	public String pagination(int totalItemCount, int currentPageIndex,
			String prefixUrl, String pageParamName, String endUrl,
			HttpServletResponse response) {
		return pagination(totalItemCount, currentPageIndex, prefixUrl,
				pageParamName, true, "|", endUrl, response);
	}

	public String pagination(int totalItemCount, int currentPageIndex,
			String prefixUrl, String pageParamName, HttpServletResponse response) {
		return pagination(totalItemCount, currentPageIndex, prefixUrl,
				pageParamName, "", response);
	}

	/**
	 * 获取用户列表＝目前聊天室里面的人＋踢出用户列表 目前聊天室里面的人＝（在线用户 并集 授权用户）差集 管理员
	 * 
	 * @param request
	 * @param response
	 */
	public void getUserList(HttpServletRequest request,
			HttpServletResponse response) {
		int onlineUserCount = 0;// 在线用户总数
		int kickUserCount = 0;// 踢出用户总数
		int curOnlinePageIndex = 0;// 当前在线用户页号
		int curKickPageIndex = 0;// 当前踢出页号
		int roomId = 0;// 房间号
		int startIndex = 0;
		int endIndex = NUMBER_PER_PAGE;
		int totalOnlinePages = 0;
		int totalKickPages = 0;
		Vector vecOnlineUser = new Vector();
		Vector vecKickUser = null;
		Vector vecManager = null;
		String condition = "";
		String pagination = "";
		UserBean onlineUser = null;
		RoomManagerBean manager = null;
		if (request.getParameter("roomId") != null)
			roomId = Integer.parseInt(request.getParameter("roomId"));
		// 在线用户
		if (request.getParameter("onlineIndex") != null) {
			curOnlinePageIndex = Integer.parseInt(request
					.getParameter("onlineIndex"));
		}

		condition = " room_id=" + roomId;
		vecManager = chatService.getJCRoomManagerList(condition);
		JCRoomBean room = chatService.getJCRoom(" id=" + roomId);
		if (room.getGrantMode() == 1) {
			// 授权用户
			vecOnlineUser = userService
					.getUserList(" join jc_room_user on user_info.id=jc_room_user.user_id where "
							+ condition + " and status=1");
		} else {// 在线用户
			// vecOnlineUser = userService
			// .getUserList(" join jc_room_online on
			// user_info.id=jc_room_online.user_id where "
			// + condition);
			// 从缓存中取得所有对应聊天室的所有用户ID
			List roomOnlineUserIdList = RoomCacheUtil
					.getRoomOnlineUserIdList(roomId);
			Integer userId = null;
			UserBean userBean = null;
			vecOnlineUser = new Vector();
			// 循环得到所有在该聊天室的用户信息放入vecOnlineUser中.
			for (int i = 0; i < roomOnlineUserIdList.size(); i++) {
				userId = (Integer) roomOnlineUserIdList.get(i);
				userBean = UserInfoUtil.getUser(userId.intValue());
				if (userBean == null)
					continue;
				vecOnlineUser.add(userBean);
			}
		}
		// 删除管理员
		for (int i = 0; i < vecManager.size(); i++) {
			manager = (RoomManagerBean) vecManager.get(i);
			for (int j = 0; j < vecOnlineUser.size(); j++) {
				onlineUser = (UserBean) vecOnlineUser.get(j);
				if (onlineUser.getId() == manager.getUserId()) {
					vecOnlineUser.remove(j);
					break;
				}
			}
		}
		//
		if (room.getGrantMode() == 0) {
			RoomUserBean roomUser = null;
			int i = 0;
			while (i < vecOnlineUser.size()) {
				onlineUser = (UserBean) vecOnlineUser.get(i);
				roomUser = chatService.getJCRoomUser(" user_id="
						+ onlineUser.getId() + " and room_id=" + roomId);
				if (roomUser != null) {
					if (roomUser.getStatus() != 1) {
						vecOnlineUser.remove(i);
						i--;
					}
				}
				i++;
			}
		}

		onlineUserCount = vecOnlineUser.size();
		totalOnlinePages = onlineUserCount / NUMBER_PER_PAGE;
		if (onlineUserCount % NUMBER_PER_PAGE != 0) {
			totalOnlinePages++;
		}

		curOnlinePageIndex = getCurIndex(totalOnlinePages, curOnlinePageIndex);
		startIndex = curOnlinePageIndex * NUMBER_PER_PAGE;
		Vector vecTemp = new Vector();
		int tempEndIndex = 0;
		tempEndIndex = startIndex + endIndex;
		tempEndIndex = Math.min(tempEndIndex, onlineUserCount);
		for (int i = startIndex; i < tempEndIndex; i++) {

			vecTemp.add(vecOnlineUser.get(i));
		}
		// condition = condition + " limit " + startIndex + "," + endIndex;
		// vecGrantUser = chatService.getJCRoomUserList(condition);
		// request.setAttribute("grantUserCount",grantUserCount+"");
		request.setAttribute("onlineUsers", vecTemp);

		if (request.getParameter("kickIndex") != null) {
			curKickPageIndex = Integer.parseInt(request
					.getParameter("kickIndex"));
		}

		// 被踢出的用户
		condition = " room_id=" + roomId + " and status=0 ";
		kickUserCount = chatService.getJCRoomUserCount(condition);
		totalKickPages = kickUserCount / NUMBER_PER_PAGE;
		if (kickUserCount % NUMBER_PER_PAGE != 0) {
			totalKickPages++;
		}
		curKickPageIndex = getCurIndex(totalKickPages, curKickPageIndex);
		startIndex = curKickPageIndex * NUMBER_PER_PAGE;
		condition = condition + " limit " + startIndex + "," + endIndex;
		vecKickUser = chatService.getJCRoomUserList(condition);
		// request.setAttribute("kickUserCount",kickUserCount+"");
		request.setAttribute("kickUsers", vecKickUser);

		pagination = pagination(totalOnlinePages, curOnlinePageIndex,
				"kick.jsp?roomId=" + roomId + "&amp;kickIndex="
						+ curKickPageIndex, "onlineIndex", "&amp;backTo="
						+ request.getParameter("backTo"), response);
		request.setAttribute("onlinePagination", pagination);
		pagination = pagination(totalKickPages, curKickPageIndex,
				"kick.jsp?roomId=" + roomId + "&amp;onlineIndex="
						+ curOnlinePageIndex, "kickIndex", "&amp;backTo="
						+ request.getParameter("backTo"), response);
		request.setAttribute("kickPagination", pagination);
		request.setAttribute("onlineIndex", curOnlinePageIndex + "");
		request.setAttribute("kickIndex", curKickPageIndex + "");
	}

	// add by zhangyi for payment chat 2006-06-29 start
	public String checkAddPayment(HttpServletRequest request) {

		StringBuilder message = new StringBuilder();

		String payType = payType = request.getParameter("payType");
		String money = request.getParameter("money");
		String remark = request.getParameter("remark");

		if (payType == null || payType.length() == 0) {
			message.append("付款类型不能为空!<br/>");
		}
		if (StringUtil.toInt(money) <= 0) {
			message.append("付款金额应是大于零的数字!<br/>");
		}
		if (remark == null || remark.length() == 0) {
			message.append("冲值描述不能为空!<br/>");
		}
		if (message.length() > 0) {
			return message.toString();
		} else {
			return "";
		}
	}

	public boolean addRoomPayment(HttpServletRequest request) {

		int roomId = getParameterInt("roomId");

		RoomPaymentBean roomPayment = new RoomPaymentBean();
		roomPayment.setUserId(loginUser.getId());
		roomPayment.setRoomId(roomId);
		roomPayment.setPayType(Integer
				.parseInt(request.getParameter("payType")));
		roomPayment.setMoney(Integer.parseInt(request.getParameter("money")));
		roomPayment.setRemark(request.getParameter("remark"));

		return chatService.addJCRoomPayment(roomPayment);

	}

	public boolean updateUserPoint(HttpServletRequest request) {
		int money = Integer.parseInt(request.getParameter("money"));
		// return userService.updateUserStatus("game_point = game_point+" +
		// money,
		// "user_id = " + loginUser.getId());
		// zhul_modify_us 2006-08-14 start
		return UserInfoUtil.updateUserStatus(
				"game_point = game_point+" + money, "user_id = "
						+ loginUser.getId(), loginUser.getId(),
				UserCashAction.OTHERS, "聊天室中增加用户乐币" + money);
		// zhul_modify_us 2006-08-14 end

	}

	// add by zhangyi for payment chat 2006-06-29 start

	// zhul 2006-06-30 当一个用户从一个房间到另一个房间时做的记录
	public void dealRoomTransform(UserBean loginUser, int fromRoomId,
			int toRoomId) {
		if (fromRoomId == toRoomId)
			return;

		if (loginUser == null || loginUser.getId() < 1)
			return;

		int userId = loginUser.getId();

		if (toRoomId <= 0 && fromRoomId > 0)// 从私人聊天室离开
		{
			// chatService.addContent("0,0,' ','','" + loginUser.getNickName()
			// + "退出聊天室','',now(),0," + fromRoomId + ",2");
			chatService.updateJCRoom(
					" current_online_count=current_online_count-1", " id="
							+ fromRoomId);

			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(fromRoomId);
				roomOnlineUserIds.remove(new Integer(userId));
			}
			// liuyi 2006-10-17 程序优化 end

			RoomUtil.addChangedRoom(fromRoomId);

		} else if (toRoomId > 0 && fromRoomId <= 0)// 进入私人聊天室
		{
			// chatService.addContent("0,0,' ','','" + loginUser.getNickName()
			// + "进入聊天室','',now(),0," + toRoomId + ",2");

			chatService.updateJCRoom(
					" current_online_count=current_online_count+1", " id="
							+ toRoomId);
			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(toRoomId);
				if (!roomOnlineUserIds.contains(new Integer(userId))) {
					roomOnlineUserIds.add(new Integer(userId));
				}
			}
			// liuyi 2006-10-17 程序优化 end
			RoomUtil.addChangedRoom(toRoomId);
		} else if (toRoomId > 0 && fromRoomId > 0)// 从一个私人聊天室到另一个私人聊天室
		{
			// chatService.addContent("0,0,' ','','" + loginUser.getNickName()
			// + "退出聊天室','',now(),0," + fromRoomId + ",2");
			chatService.updateJCRoom(
					" current_online_count=current_online_count-1", " id="
							+ fromRoomId);
			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(fromRoomId);
				roomOnlineUserIds.remove(new Integer(userId));
			}
			// liuyi 2006-10-17 程序优化 end
			RoomUtil.addChangedRoom(fromRoomId);
			// chatService.addContent("0,0,' ','','" + loginUser.getNickName()
			// + "进入聊天室','',now(),0," + toRoomId + ",2");
			chatService.updateJCRoom(
					" current_online_count=current_online_count+1", " id="
							+ toRoomId);
			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(toRoomId);
				if (!roomOnlineUserIds.contains(new Integer(userId))) {
					roomOnlineUserIds.add(new Integer(userId));
				}
			}
			// liuyi 2006-10-17 程序优化 end
			RoomUtil.addChangedRoom(toRoomId);
		}

		if (toRoomId == 0)// 进入聊天大厅，聊天大厅在线人数加1
		{
			chatService.updateJCRoom(
					" current_online_count=current_online_count+1", " id=0");
			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(0);
				if (!roomOnlineUserIds.contains(new Integer(userId))) {
					roomOnlineUserIds.add(new Integer(userId));
				}
			}
			// liuyi 2006-10-17 程序优化 end
			RoomUtil.addChangedRoom(toRoomId);
		} else if (fromRoomId == 0)// 退出聊天大厅，聊天大厅在线人数-1
		{
			chatService.updateJCRoom(
					" current_online_count=current_online_count-1", " id=0");
			// liuyi 2006-10-17 程序优化 start
			if (userId > 0) {
				List roomOnlineUserIds = RoomCacheUtil
						.getRoomOnlineUserIdList(0);
				roomOnlineUserIds.remove(new Integer(userId));
			}
			// liuyi 2006-10-17 程序优化 end
			RoomUtil.addChangedRoom(fromRoomId);
		}
		// macq_2007-5-18_取消进入聊天室的提示_start
		/*
		 * if (toRoomId >= 0) { addWelcomeCrownOwnerMsg(loginUser, toRoomId); //
		 * 添加恶人进入消息 // addWelcomeBlackMsg(loginUser, toRoomId); }
		 */
		// macq_2007-5-18_取消进入聊天室的提示_end
	}

	// 邀请用户加入自己管理的聊天室
	public void invite(HttpServletRequest request) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		int userId = getParameterInt("userId");
		int roomId = getParameterInt("roomId");

		int count = chatService.getRoomHallInviteCount(" user_id="
				+ loginUser.getId()
				+ " and DAYOFMONTH(create_datetime)=DAYOFMONTH(NOW())");
		if (count > 99) {
			request.setAttribute("tip", "您每天最多只能加100个好友！");
			return;
		}
		// 扣除发邀人乐币
		// if (loginUser.getUs().getGamePoint() < 10000) {
		if (usb.getGamePoint() < 10000) {
			request.setAttribute("tip", "您的乐币已经不足10000！");
			return;
		}
		// userService.updateUserStatus("game_point=game_point-10000",
		// "user_id="
		// + loginUser.getId());
		// zhul_modify_us 2006-08-14 start
		UserInfoUtil.updateUserStatus("game_point=game_point-10000", "user_id="
				+ loginUser.getId(), loginUser.getId(), UserCashAction.OTHERS,
				"邀请别人加入聊天室扣10000乐币");
		// zhul_modify_us 2006-08-14 end

		// loginUser.getUs()
		// .setGamePoint(loginUser.getUs().getGamePoint() - 10000);

		// add by zhangyi 2006-07-24 for stat user money history start
		MoneyAction.addMoneyFlowRecord(Constants.OTHER, 10000,
				Constants.SUBTRATION, loginUser.getId());
		// add by zhangyi 2006-07-24 for stat user money history end

		JCRoomBean jcRoom = chatService.getJCRoom(" id=" + roomId);
		// 如果聊天室需要权限，则先赋予用户权限
		if (jcRoom.getGrantMode() == 1) {
			RoomUserBean roomUserBean = chatService.getJCRoomUser(" user_id="
					+ userId + " and room_id=" + roomId + " and status=0");
			if (roomUserBean != null) {
				chatService.updateJCRoomUser(" set status=1", " user_id="
						+ userId + " and room_id=" + roomId + " and status=0");
			} else {
				RoomUserBean roomUser = new RoomUserBean();
				roomUser.setUserId(userId);
				roomUser.setRoomId(roomId);
				roomUser.setManagerId(loginUser.getId());
				roomUser.setStatus(1);
				chatService.addJCRoomUser(roomUser);
			}

			// 在授权记录表中做记录
			RoomGrantBean roomGrant = new RoomGrantBean();
			roomGrant.setRoomId(roomId);
			roomGrant.setUserId(userId);
			roomGrant.setManagerId(loginUser.getId());
			roomGrant.setGrantType(1);
			chatService.addJCRoomGrant(roomGrant);
		} else {
			RoomUserBean roomUser = chatService.getJCRoomUser(" user_id="
					+ userId + " and room_id=" + roomId + " and status=0");
			if (roomUser != null) {
				chatService.deleteJCRoomUser(" user_id=" + userId
						+ " and room_id=" + roomId + " and status=0");

				// 在授权记录表中做记录
				RoomGrantBean roomGrant = new RoomGrantBean();
				roomGrant.setRoomId(roomId);
				roomGrant.setUserId(userId);
				roomGrant.setManagerId(loginUser.getId());
				roomGrant.setGrantType(1);
				chatService.addJCRoomGrant(roomGrant);
			}
		}
		RoomHallInviteBean hallInvite = new RoomHallInviteBean();
		hallInvite.setUserId(loginUser.getId());
		hallInvite.setToId(userId);
		chatService.addRoomHallInvite(hallInvite);
		// 向被邀请的用户发消息
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle("您已经被邀请进入" + jcRoom.getName() + "聊天室");
		notice.setContent("");
		notice.setType(NoticeBean.GENERAL_NOTICE);
		notice.setHideUrl("");
		notice.setLink("/chat/hall.jsp?roomId="
				+ roomId);
		NoticeUtil.getNoticeService().addNotice(notice);

		return;
	}

	/*
	 * // 在显示首页显示不下的情况下在单独的页面中进行显示 public void
	 * inviteMoreFriends(HttpServletRequest request) {
	 * 
	 * int perPageRecords = 10; // 取得参数 String option =
	 * request.getParameter("option"); String totaPages =
	 * request.getParameter("totaPages"); String pageIndex =
	 * request.getParameter("pageIndex"); String marker =
	 * request.getParameter("marker"); String roomId =
	 * request.getParameter("roomId"); Vector userList = new Vector();
	 * 
	 * if (option.equals("onlinefriends")) { userList = userService
	 * .getUserList("join (SELECT friend_id FROM user_friend WHERE user_id=" +
	 * loginUser.getId() + " AND friend_id IN(SELECT user_id FROM
	 * jc_online_user) and friend_id not in(select user_id from jc_room_user
	 * where status=1 and room_id=" + roomId + ")) as temp on
	 * user_info.id=temp.friend_id order by id limit " +
	 * (Integer.parseInt(pageIndex) * perPageRecords + 10) + "," +
	 * perPageRecords); request.setAttribute("marker", "0");
	 * request.setAttribute("option", "onlinefriends"); } else if
	 * (option.equals("outlinefriends")) { if (Integer.parseInt(marker) == 0) {
	 * userList = userService .getUserList("join (SELECT friend_id FROM
	 * user_friend WHERE user_id = " + loginUser.getId() + " and friend_id NOT
	 * IN (SELECT user_id FROM jc_online_user) and friend_id not in(select
	 * user_id from jc_room_user where status=1 and room_id=" + roomId + ")) as
	 * temp on user_info.id=temp.friend_id order by id limit " +
	 * Integer.parseInt(pageIndex) perPageRecords + "," + perPageRecords);
	 * request.setAttribute("marker", "0"); } else { userList = userService
	 * .getUserList("join (SELECT friend_id FROM user_friend WHERE user_id = " +
	 * loginUser.getId() + " and friend_id NOT IN (SELECT user_id FROM
	 * jc_online_user) and friend_id not in(select user_id from jc_room_user
	 * where status=1 and room_id=" + roomId + ")) as temp on
	 * user_info.id=temp.friend_id order by id limit " +
	 * (Integer.parseInt(pageIndex) * perPageRecords + 3) + "," +
	 * perPageRecords); request.setAttribute("marker", "1"); }
	 * request.setAttribute("option", "outlinefriends"); } else if
	 * (option.equals("strange")) { if (Integer.parseInt(marker) == 0) {
	 * userList = userService .getUserList("join (select user_id from
	 * jc_online_user where user_id in (select from_id as id from
	 * jc_room_content where to_id = " + loginUser.getId() + " union select
	 * to_id as id from jc_room_content where from_id = " + loginUser.getId() + ")
	 * and user_id not in (select friend_id from user_friend where user_id = " +
	 * loginUser.getId() + ") and user_id not in (select user_id from
	 * jc_room_user where status=1 and room_id=" + roomId + ")) as temp on
	 * user_info.id=temp.user_id ORDER BY id limit " +
	 * Integer.parseInt(pageIndex) perPageRecords + "," + perPageRecords);
	 * request.setAttribute("marker", "0"); } else { userList = userService
	 * .getUserList("join (select user_id from jc_online_user where user_id in
	 * (select from_id as id from jc_room_content where to_id = " +
	 * loginUser.getId() + " union select to_id as id from jc_room_content where
	 * from_id = " + loginUser.getId() + ") and user_id not in (select friend_id
	 * from user_friend where user_id = " + loginUser.getId() + ") and user_id
	 * not in (select user_id from jc_room_user where status=1 and room_id=" +
	 * roomId + ")) as temp on user_info.id=temp.user_id ORDER BY id limit " +
	 * (Integer.parseInt(pageIndex) * perPageRecords + 3) + "," +
	 * perPageRecords); request.setAttribute("marker", "1"); }
	 * request.setAttribute("option", "strange"); }
	 * 
	 * request.setAttribute("userList", userList);
	 * request.setAttribute("totaPages", totaPages);
	 * request.setAttribute("pageIndex", pageIndex);
	 * 
	 * return; }
	 */
	/**
	 * zhul 206-10-16 用户当前好友邀请列表
	 * 
	 * @param request
	 */
	public void inviteMoreFriends(HttpServletRequest request) {
		// 取得参数
		String option = request.getParameter("option");
		int roomId = getParameterInt("roomId");

		// zhul 2006-10-13 获取用户好友
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		Vector userList = new Vector();
		if (option.equals("onlinefriends")) {
			// 在线好友
			for (int i = 0; i < userFriends.size(); i++) {
				String userIdKey = (String) userFriends.get(i);
				if (!this.isRoomMember(userIdKey, String.valueOf(roomId))
						&& OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}

		} else if (option.equals("outlinefriends")) {
			// 离线好友
			for (int i = 0; i < userFriends.size(); i++) {
				String userIdKey = (String) userFriends.get(i);
				if (!this.isRoomMember(userIdKey, String.valueOf(roomId))
						&& !OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}

		}
		// 分页
		int PER_PAGE_NUM = 10;
		int totalCount = userList.size();
		int totalPage = (totalCount + PER_PAGE_NUM - 1) / PER_PAGE_NUM;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		int start = pageIndex * PER_PAGE_NUM;
		int end = pageIndex * PER_PAGE_NUM + PER_PAGE_NUM;

		request.setAttribute("start", start + "");
		request.setAttribute("end", end + "");
		request.setAttribute("userList", userList);
		request.setAttribute("totalPage", totalPage + "");
		request.setAttribute("pageIndex", pageIndex + "");
		request.setAttribute("option", option);

	}

	/*
	 * // 显示管理员的不在此聊天室里的 在线好友， 离线好友，陌生人 public void
	 * inviteFriends(HttpServletRequest request) { // 取得参数 String roomId =
	 * request.getParameter("roomId"); // zhul alter 2006/06/09 start
	 * 由于要将用户分类细化，所以将此类功能改为返回在线好友，离线好友，及陌生人的人数 int onlineFriendsCount =
	 * userService .getFriendCount("user_id = " + loginUser.getId() + " and
	 * friend_id in (select user_id from jc_online_user) and friend_id not
	 * in(select user_id from jc_room_user where status=1 and room_id=" + roomId +
	 * ")");
	 * 
	 * int outlineFriendsCount = userService .getFriendCount("user_id = " +
	 * loginUser.getId() + " and friend_id not in (select user_id from
	 * jc_online_user) and friend_id not in(select user_id from jc_room_user
	 * where status=1 and room_id=" + roomId + ")"); // DbOperation dbOp = new
	 * DbOperation(); // dbOp.init(); // String query = "select count(u_id) as
	 * c_id from (select u_id from // (select // friend_id as u_id from
	 * user_friend where user_id = " // + userId // + " union select user_id as
	 * u_id from jc_online_user) as temp group // by u_id // having count(u_id) =
	 * 1) as temp1"; // ResultSet rs = dbOp.executeQuery(query); // int
	 * outlineFriendsCount = 0; // if (rs.next()) { // outlineFriendsCount =
	 * rs.getInt("c_id"); // } // dbOp.release(); int strangeCount = userService
	 * .getOnlineUserCount("user_id in (select from_id as id from
	 * jc_room_content where to_id = " + loginUser.getId() + " union select
	 * to_id as id from jc_room_content where from_id = " + loginUser.getId() + ")
	 * and user_id not in (select friend_id from user_friend where user_id = " +
	 * loginUser.getId() + ") and user_id not in(select user_id from
	 * jc_room_user where status=1 and room_id=" + roomId + ")"); // zhul add
	 * code 2006-06-15 start 如果在线好友小于10人则显示好友，3个离线好友和3个陌生人 if
	 * (onlineFriendsCount <= 10) { Vector onlineFriendsList = new Vector();
	 * onlineFriendsList = userService .getUserList("join (SELECT friend_id FROM
	 * user_friend WHERE user_id=" + loginUser.getId() + " AND friend_id
	 * IN(SELECT user_id FROM jc_online_user) and friend_id not in(select
	 * user_id from jc_room_user where status=1 and room_id=" + roomId + ")) as
	 * temp on user_info.id=temp.friend_id order by id ");
	 * 
	 * Vector outlineFriendsList = new Vector(); outlineFriendsList =
	 * userService .getUserList("join (SELECT friend_id FROM user_friend WHERE
	 * user_id = " + loginUser.getId() + " and friend_id NOT IN (SELECT user_id
	 * FROM jc_online_user) and friend_id not in(select user_id from
	 * jc_room_user where status=1 and room_id=" + roomId + ")) as temp on
	 * user_info.id=temp.friend_id order by id limit 0,3");
	 * 
	 * Vector strangerList = new Vector(); strangerList = userService
	 * .getUserList("join (select user_id from jc_online_user where user_id in
	 * (select from_id as id from jc_room_content where to_id = " +
	 * loginUser.getId() + " union select to_id as id from jc_room_content where
	 * from_id = " + loginUser.getId() + ") and user_id not in (select friend_id
	 * from user_friend where user_id = " + loginUser.getId() + ") and user_id
	 * not in(select user_id from jc_room_user where status=1 and room_id=" +
	 * roomId + ")) as temp on user_info.id=temp.user_id ORDER BY id limit
	 * 0,3");
	 * 
	 * request.setAttribute("onlineFriendsList", onlineFriendsList);
	 * request.setAttribute("outlineFriendsList", outlineFriendsList);
	 * request.setAttribute("strangerList", strangerList); } //
	 * 如果在线好友多于10人则显示10个好友加离线好友和陌生人链接 else { Vector onlineFriendsList = new
	 * Vector(); onlineFriendsList = userService .getUserList("join (SELECT
	 * friend_id FROM user_friend WHERE user_id=" + loginUser.getId() + " AND
	 * friend_id IN(SELECT user_id FROM jc_online_user) and friend_id not
	 * in(select user_id from jc_room_user where status=1 and room_id=" + roomId +
	 * ")) as temp on user_info.id=temp.friend_id order by id limit 0,10");
	 * request.setAttribute("onlineFriendsList", onlineFriendsList); }
	 * request.setAttribute("onlineFriendsCount", onlineFriendsCount + "");
	 * request.setAttribute("outlineFriendsCount", outlineFriendsCount + "");
	 * request.setAttribute("strangeCount", strangeCount + ""); // zhul alter
	 * 2006/06/09 end 由于要将用户分类细化，所以将此类功能改为返回在线好友，离线好友，及陌生人的人数 return; }
	 */
	/**
	 * zhul 2006-10-16 邀请好友优化，获取当前不在本聊天室的在线好友和离线好友
	 */
	public void inviteFriends(HttpServletRequest request) {

		int roomId = getParameterInt("roomId");

		// zhul 2006-10-13 获取用户好友
		ArrayList userFriends = UserInfoUtil.getUserFriends(loginUser.getId());
		// 在线不在本聊天室好友
		Vector onlineFriendsList = new Vector();
		// 离线不在本聊天室好友
		Vector outlineFriendsList = new Vector();

		for (int i = 0; i < userFriends.size(); i++) {
			String userIdKey = (String) userFriends.get(i);
			if (!this.isRoomMember(userIdKey, String.valueOf(roomId))) {
				if (OnlineUtil.isOnline(userIdKey))
					onlineFriendsList.add(userIdKey);
				else
					outlineFriendsList.add(userIdKey);
			}
		}

		request.setAttribute("onlineFriendsList", onlineFriendsList);
		request.setAttribute("outlineFriendsList", outlineFriendsList);

	}

	// 获取一个房间内得所有人员信息
	public void roomUserList(HttpServletRequest request) {
		int roomId = getParameterInt("roomId");
		Vector managerList = chatService.getJCRoomManagerList("room_id="
				+ roomId);
		JCRoomBean room = chatService.getJCRoom("id=" + roomId);
		Vector userList = null;
		if (room.getGrantMode() == 1) {
			userList = chatService.getJCRoomUserList("room_id=" + roomId
					+ " and status=1");
		} else {
			userList = chatService.getOnlineList("room_id=" + roomId
					+ " and user_id<>" + room.getCreatorId());
		}
		request.setAttribute("userList", userList);
		request.setAttribute("managerList", managerList);
		request.setAttribute("room", room);
	}

	public void roomApply(HttpServletRequest request) {
		String orderBy = request.getParameter("orderBy");
		if ((orderBy == null) || (orderBy.equals("")))
			orderBy = "vote_count";
		request.setAttribute("orderBy", orderBy);
		// 取得公聊参数
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex == -1) {
			pageIndex = 0;
		}
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		String prefixUrl = "roomApply.jsp?orderBy=" + orderBy;
		// + "&amp;backTo="
		// + URLEncoder.encode(backTo);

		int totalCount = chatService.getRoomApplyCount("1=1");

		int totalPageCount = totalCount / PUBLIC_ROOM_PAGE;
		if (totalCount % PUBLIC_ROOM_PAGE != 0) {
			totalPageCount++;
		}
		if (pageIndex > totalPageCount - 1) {
			pageIndex = totalPageCount - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		// 取得要显示的消息列表
		int start = pageIndex * PUBLIC_ROOM_PAGE;
		int end = PUBLIC_ROOM_PAGE;

		Vector roomApplyList = chatService.getRoomApplyList("1=1 order by "
				+ orderBy + " desc limit " + start + ", " + end);

		request.setAttribute("totalPageCount", new Integer(totalPageCount));
		request.setAttribute("pageIndex", new Integer(pageIndex));
		request.setAttribute("backTo", backTo);
		request.setAttribute("prefixUrl", prefixUrl);
		request.setAttribute("roomApplyList", roomApplyList);
		// macq_2006-10-12_获取聊天大厅2条最新的信息_start (注: 传入的值是聊天大厅id)
		Vector contentIdList = (Vector) RoomCacheUtil.getRoomContentCountMap(0);
		Vector chatList = new Vector();
		int contentCount = 0;
		if (contentIdList != null) {
			contentCount = contentIdList.size();
		}
		JCRoomContentBean roomContent = null;
		int roomContentId = 0;
		int j = 0;
		for (int i = 0; i < contentCount; i++) {
			roomContentId = ((Integer) contentIdList.get(i)).intValue();
			roomContent = RoomCacheUtil.getRoomContent(roomContentId);
			if (roomContent.getMark() == 0 && roomContent.getToId() != 0) {
				j++;
				chatList.add(roomContent);
				if (j == 2) {
					break;
				}
			}
		}
		// macq_2006-10-12_获取聊天大厅2条最新的信息_end
		// Vector chatList = chatrService
		// .getMessageList("is_private=0 AND to_id<>0 AND room_id=0 ORDER BY
		// send_datetime desc limit 0,2");
		request.setAttribute("chatList", chatList);
	}

	public void myApply(HttpServletRequest request) {
		// zhul_modify us _2006-08-14_获取用户状态信息 start
		UserStatusBean usb = UserInfoUtil.getUserStatus(loginUser.getId());
		// zhul_modify us _2006-08-14_获取用户状态信息 end

		// if (loginUser.getUs().getRank() < 10) {
		if (usb.getRank() < 10) {
			request.setAttribute("result", "rankError");
			request.setAttribute("tip", "申请失败");
			return;
		}
		int userId = loginUser.getId();
		RoomApplyBean roomApply = chatService.getRoomApply("user_id=" + userId);
		RoomVoteBean roomVote = chatService.getRoomVote("user_id=" + userId);
		if (roomApply != null || roomVote != null) {
			request.setAttribute("result", "only");
			request.setAttribute("tip", "申请失败");
			return;
		}
		request.setAttribute("result", "success");
		return;
	}

	public void addApply(HttpServletRequest request) {
		int userId = loginUser.getId();
		RoomApplyBean roomApply = chatService.getRoomApply("user_id=" + userId);
		RoomVoteBean roomVote = chatService.getRoomVote("user_id=" + userId);
		if (roomApply != null || roomVote != null) {
			request.setAttribute("result", "exist");
			request.setAttribute("tip", "您已经申请或支持过一个聊天室(3秒种返回)");
			return;
		}
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");
		String enounce = request.getParameter("enounce");
		if (isEmpty(name)) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "聊天室名称不能为空(3秒种返回)");
			return;
		} else if (isEmpty(subject)) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "聊天室主题不能为空(3秒种返回)");
			return;
		} else if (isEmpty(enounce)) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "聊天室宣言不能为空(3秒种返回)");
			return;
		} else if (name.length() > 7) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "聊天室名称限7字以内(3秒种返回)");
			return;
		}
		// 看房间名是否已存在
		int count = chatService.getRoomApplyCount("room_name ='" + StringUtil.toSql(name) + "'");
		if (count > 0) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "聊天室名称已经存在(3秒种返回)");
			return;
		}
		RoomApplyBean apply = new RoomApplyBean();
		apply.setUserId(loginUser.getId());
		apply.setRoomName(name);
		apply.setRoomSubject(subject);
		apply.setRoomEnounce(enounce);
		apply.setMark(0);
		boolean flag = chatService.addRoomApply(apply);
		if (flag) {
			request.setAttribute("result", "success");
			// request.setAttribute("tip", "提交成功(3秒种返回)");
			return;
		} else {
			request.setAttribute("result", "failure");
			// request.setAttribute("tip", "提交失败(3秒种返回)");
		}
	}

	public void roomEnter(HttpServletRequest request) {
		// int userId = loginUser.getId();
		// RoomApplyBean roomApply = chatService.getRoomApply("user_id=" +
		// userId);
		// RoomVoteBean roomVote = chatService.getRoomVote("user_id=" + userId);
		// if (roomApply != null || roomVote != null) {
		// request.setAttribute("result", "failure");
		// request.setAttribute("tip", "您已经申请或支持过一个聊天室(3秒种返回)");
		// return;
		// }
		int applyId = getParameterInt("applyId");
		RoomApplyBean roomEnter = chatService.getRoomApply("id=" + applyId);
		request.setAttribute("result", "success");
		request.setAttribute("roomEnter", roomEnter);

	}

	public void addVote(HttpServletRequest request) {
		int userId = loginUser.getId();
		RoomApplyBean roomApply = chatService.getRoomApply("user_id=" + userId);
		RoomVoteBean roomVote = chatService.getRoomVote("user_id=" + userId);
		if (roomApply != null || roomVote != null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "您已经申请或支持过一个聊天室(3秒种返回)");
			return;
		}
		int roomId = getParameterInt("roomId");
		chatService.updateRoomApply("vote_count=vote_count+1", "id=" + roomId);
		RoomVoteBean vote = new RoomVoteBean();
		vote.setUserId(userId);
		vote.setApplyId(roomId);
		chatService.addRoomVote(vote);
		request.setAttribute("result", "success");
		request.setAttribute("tip", "加入成功(3秒种返回)");
		return;
	}

	public boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * fanys 2006-7-13 验证邀请页面的输入合法性，如果合法，则添加一条邀请记录
	 * 
	 * @param request
	 * @return
	 */
	public boolean invite(HttpServletRequest request, String[] illegalStrs) {

		String mobile = request.getParameter("mobile");
		String name = request.getParameter("name").trim();
		String friend = request.getParameter("friend").trim();
		String message = request.getParameter("message");
		Calendar c = Calendar.getInstance();
		Timestamp ts = new Timestamp(c.getTimeInMillis());
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = String.valueOf(dateFormatter.format(ts));
		// 每天最多邀请30次
		int inviteCount = chatService.getRoomInviteCount(" user_id="
				+ loginUser.getId() + " and send_dateTime>='" + date + "'");
		int DAY_INVITE_COUNT = 20;
		if (inviteCount >= DAY_INVITE_COUNT) {
			request.setAttribute("info", "您一天最多只能邀请" + DAY_INVITE_COUNT + "次！");
			return false;
		}
		// 每月最多邀请150次
		int monthInviteCount = 0;

		monthInviteCount = chatService.getRoomInviteCount("user_id="
				+ loginUser.getId() + " and send_datetime>='"
				+ getFirstDayOfMonth() + "' ");
		if (monthInviteCount >= 150) {
			request.setAttribute("info", "您一月最多只能邀请150次！");
			return false;
		}

		if (mobile.length() != 11) {
			request.setAttribute("info", "手机号码必须为11位!");
			return false;
		}
		if (isNum(mobile) == false) {
			request.setAttribute("info", "手机号码必须为数字!");
			return false;
		}
		String prefix = mobile.substring(0, 3);
		boolean isExistMobilePrefix = false;
		String[] mobilePrefix = new String[] { "134", "135", "136", "137",
				"138", "139", "159","158","157","156" };
		for (int i = 0; i < mobilePrefix.length; i++) {
			if (prefix.equals(mobilePrefix[i])) {
				isExistMobilePrefix = true;
				break;
			}

		}
		if (!isExistMobilePrefix) {
			request.setAttribute("info", "对不起，您的好友须为移动用户！");
			return false;
		}

		// AreaBean area = AreaUtil.getAreaByMobile(mobile);
		// if (area == null) {
		// request.setAttribute("info", "北京");
		// return false;
		// }
		// if (area.getCityname().equals("北京")) {
		// request.setAttribute("info", "北京");
		// return false;
		// }

		if (name.equals("")) {
			request.setAttribute("info", "请输入您的姓名!");
			return false;
		}
		if (name.length() > 4) {
			request.setAttribute("info", "您的姓名不能超过4个字!");
			return false;
		}
		name = name.replace("'", "");
		if (friend.equals("")) {
			request.setAttribute("info", "请输入您好友的姓名!");
			return false;
		}
		if (friend.length() > 4) {
			request.setAttribute("info", "您的好友的姓名不能超过4个字!");
			return false;
		}
		friend = friend.replace("'", "");
		if (message.length() > 14) {
			request.setAttribute("info", "给好友留言不能超过14个字!");
			return false;
		}
		if (message.trim().equals("")) {
			request.setAttribute("info", "给好友留言不能为空!");
			return false;
		}
		message = message.replace("'", "");

		int count = chatService.getRoomInviteCount(" user_id="
				+ loginUser.getId() + " and mobile='" + mobile
				+ "' and send_dateTime>='" + date + "'");
		if (count >= 3) {
			request.setAttribute("info", "您每天只能邀请同一位好友3次！");
			return false;
		}
		message = replaceIllegalStr(message, illegalStrs);
		// 往jc_room_invite表里面添加一条记录
		UserBean user = userService.getUser("mobile='" + StringUtil.toSql(mobile) + "'");
		RoomInviteBean invite = new RoomInviteBean();
		invite.setUserId(loginUser.getId());
		invite.setMobile(mobile);
		invite.setName(name);
		invite.setContent(message);
		invite.setMark(0);
		if (user != null)
			invite.setNewUserMark(0);
		else {
			RoomInviteBean tempInvite = chatService.getRoomInvite("mobile='"
					+ StringUtil.toSql(mobile) + "'");
			if (tempInvite == null)
				invite.setNewUserMark(1);
			else
				invite.setNewUserMark(0);
		}
		chatService.addRoomInvite(invite);
		request.getSession()
				.setAttribute("inviteCount", (inviteCount + 1) + "");

		String condition = " user_id=" + loginUser.getId() + " and mobile='"
				+ StringUtil.toSql(mobile) + "' and name='" + StringUtil.toSql(name) + "' and content='" + StringUtil.toSql(message)
				+ "' and mark=0 ";
		int maxId = chatService.getMaxRoomInviteId(condition);

		// mcq_2006-7-20_判断不能在0点到8点之间发送push_start
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		// mcq_2006-7-20_判断不能在0点到8点之间发送push_end
		if (currentHour >= 0 && currentHour < 8) {
			chatService.updateRoomInvite("send_mark=0,success_mark=0", "id="
					+ maxId);
			request.setAttribute("info", "未到发送时间");
		} else {

			// liuyi_2006-11-23_修改push发送方法 start
			String pushMessage = friend + "," + message + "-" + name;
			String url = "wap.joycool.net/jcadmin/chatInviteDeal.jsp?id="
					+ maxId;
			boolean success = false;
			try {
				success = SmsUtil.sendPush(pushMessage, mobile, url);
			} catch (Exception ex) {
				success = false;
			}
			// liuyi_2006-11-23_修改push发送方法 end
			if (!success)
				chatService.updateRoomInvite("success_mark=0,send_mark=1",
						"id=" + maxId);
			else
				chatService.updateRoomInvite("success_mark=1,send_mark=1",
						"id=" + maxId);
		}

		return true;
	}

	/**
	 * fanys 2006-08-15 替换非法字符
	 * 
	 * @param message
	 * @param illegalStrs
	 * @return
	 */
	public String replaceIllegalStr(String message, String[] illegalStrs) {
		for (int i = 0; i < illegalStrs.length; i++) {
			message = message.replaceAll(illegalStrs[i], "*");
		}
		return message;
	}

	/**
	 * 判断字符串是不是都是数字 fanys 2006-7-13
	 * 
	 * @param mobile
	 * @return
	 */
	private boolean isNum(String mobile) {
		char nums[] = "0123456789".toCharArray();
		boolean isExist = false;
		for (int i = 0; i < mobile.length(); i++) {
			isExist = false;
			for (int j = 0; j < nums.length; j++) {
				if (nums[j] == mobile.charAt(i))
					isExist = true;
			}
			if (isExist == false)
				return false;
		}
		return true;
	}

	/**
	 * 得到用户发送的最后一条邀请记录 fanys 2006-7-13
	 * 
	 * @param request
	 */
	public void getLastInviteMessage(HttpServletRequest request) {
		String condition = " id=(select max(id) from jc_room_invite where user_id="
				+ loginUser.getId() + ") ";
		RoomInviteBean invite = chatService.getRoomInvite(condition);
		if (invite != null) {
			request.setAttribute("name", invite.getName());
			request.setAttribute("message", invite.getContent());
		}
	}
	public static int NUMBER_PER_PAGE_P = 8;
	public void viewMessage(HttpServletRequest request,
			HttpServletResponse response, String prefixUrl) {
		int toUserId = getParameterInt("toUserId");

		// liuyi 2006-09-18 两人聊天记录加缓存 start
		// String strsql = "select count(id) as c_id from (select id from
		// jc_room_content where from_id="
		// + loginUser.getId()
		// + " and to_id="
		// + toUserId
		// + " union all "
		// + "select id from jc_room_content where from_id="
		// + toUserId
		// + " and to_id=" + loginUser.getId() + " ) as temp ";
		// int messageCount = chatService.getMessageCount0(strsql);
		Vector messageIdList = RoomCacheUtil.getTwoUserContentIDList(loginUser
				.getId(), toUserId);
		PagingBean paging = new PagingBean(this, messageIdList.size(), NUMBER_PER_PAGE_P, "p");

		// liuyi 2006-09-18 两人聊天记录加缓存 start
		// strsql = "select * from jc_room_content where from_id="
		// + loginUser.getId() + " and to_id=" + toUserId + " union all "
		// + " select * from jc_room_content where from_id= " + toUserId
		// + " and to_id=" + loginUser.getId()
		// + " order by id desc limit " + pageIndex * NUMBER_PER_PAGE
		// + "," + NUMBER_PER_PAGE;
		// Vector vecMessages = chatService.getMessageList0(strsql);
		int endIndex = paging.getEndIndex();
		Vector currentPageIdList = new Vector();
		for (int i = paging.getStartIndex(); i < endIndex; i++) {
			currentPageIdList.add(messageIdList.get(i));
		}
		Vector vecMessages = RoomCacheUtil.getContentList(currentPageIdList);
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (request.getParameter("toUserId") != null)
			prefixUrl = prefixUrl + "?toUserId="
					+ request.getParameter("toUserId");
		if (prefixUrl.endsWith("&amp;"))
			prefixUrl = prefixUrl.substring(0, prefixUrl.length() - 5);
		String pagination = paging.shuzifenye(prefixUrl, true, "|", response);
		request.setAttribute("pagination", pagination);
		request.setAttribute("messages", vecMessages);
	}

	/**
	 * 
	 * @author macq
	 * @explain：
	 * @datetime:2007-5-30 9:24:48
	 * @param request
	 * @return void
	 */
	public void pmViewMessage(HttpServletRequest request,
			HttpServletResponse response, String prefixUrl, int NUMBER_PER_PAGE) {
		int toUserId = getParameterInt("toUserId");
		int pageIndex1 = 0;
		int pageCount1 = 0;
		if (request.getParameter("pageIndex1") != null)
			pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		int messageCount = 0;
		Vector messageIdList = RoomCacheUtil.getPrivateContentIDList(loginUser
				.getId());
		messageCount = messageIdList.size();
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (messageCount > 50)
			messageCount = 50;
		pageCount1 = messageCount / NUMBER_PER_PAGE;
		if (messageCount % NUMBER_PER_PAGE != 0)
			pageCount1++;
		int startIndex = Math.min(pageIndex1 * NUMBER_PER_PAGE, messageIdList
				.size());
		int endIndex = Math.min(pageIndex1 * NUMBER_PER_PAGE + NUMBER_PER_PAGE,
				messageIdList.size());
		Vector currentPageIdList = new Vector();
		for (int i = startIndex; i < endIndex; i++) {
			currentPageIdList.add(messageIdList.get(i));
		}
		Vector vecMessages = RoomCacheUtil.getContentList(currentPageIdList);
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (request.getParameter("toUserId") != null)
			prefixUrl = prefixUrl + "?toUserId=" + toUserId;
		if (prefixUrl.endsWith("&amp;"))
			prefixUrl = prefixUrl.substring(0, prefixUrl.length() - 5);
		String pagination = pagination(pageCount1, pageIndex1, prefixUrl,
				"pageIndex1", response);
		request.setAttribute("pmPagination", pagination);
		request.setAttribute("pmMessages", vecMessages);
		request.setAttribute("pmPageCount", pageCount1 + "");
	}

	/**
	 * 
	 * @author macq
	 * @explain： 没有接受对象的私聊页面信息
	 * @datetime:2007-5-31 8:09:52
	 * @param request
	 * @param response
	 * @param prefixUrl
	 * @param NUMBER_PER_PAGE
	 * @return void
	 */
	public void noUserPMViewMessage(HttpServletRequest request,
			HttpServletResponse response, String prefixUrl, int NUMBER_PER_PAGE) {
		int toUserId = getParameterInt("toUserId");
		int pageIndex1 = 0;
		int pageCount1 = 0;
		if (request.getParameter("pageIndex1") != null)
			pageIndex1 = StringUtil.toInt(request.getParameter("pageIndex1"));
		int messageCount = 0;
		Vector messageIdList = RoomCacheUtil.getPrivateContentIDList(loginUser
				.getId());
		messageCount = messageIdList.size();
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (messageCount > 50)
			messageCount = 50;
		pageCount1 = messageCount / NUMBER_PER_PAGE;
		if (messageCount % NUMBER_PER_PAGE != 0)
			pageCount1++;
		int startIndex = Math.min(pageIndex1 * NUMBER_PER_PAGE, messageIdList
				.size());
		int endIndex = Math.min(pageIndex1 * NUMBER_PER_PAGE + NUMBER_PER_PAGE,
				messageIdList.size());
		Vector currentPageIdList = new Vector();
		for (int i = startIndex; i < endIndex; i++) {
			currentPageIdList.add(messageIdList.get(i));
		}
		prefixUrl = prefixUrl + "?mark=1";
		Vector vecMessages = RoomCacheUtil.getContentList(currentPageIdList);
		String pagination = pagination(pageCount1, pageIndex1, prefixUrl,
				"pageIndex1", response);
		request.setAttribute("pmPagination", pagination);
		request.setAttribute("pmMessages", vecMessages);
		request.setAttribute("pmPageCount", pageCount1 + "");
	}

	/**
	 * 
	 * @author macq
	 * @explain：私聊通讯器2个人之间的聊天记录
	 * @datetime:2007-5-30 9:16:21
	 * @param request
	 * @param response
	 * @param prefixUrl
	 * @return void
	 */
	public void pmSpaceViewMessage(HttpServletRequest request,
			HttpServletResponse response, String prefixUrl) {
		int NUMBER_PER_PAGE = 3;
		int toUserId = getParameterInt("toUserId");
		int pageIndex = 0;
		int pageCount = 0;
		if (request.getParameter("pageIndex") != null)
			pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		// liuyi 2006-09-18 两人聊天记录加缓存 start
		// String strsql = "select count(id) as c_id from (select id from
		// jc_room_content where from_id="
		// + loginUser.getId()
		// + " and to_id="
		// + toUserId
		// + " union all "
		// + "select id from jc_room_content where from_id="
		// + toUserId
		// + " and to_id=" + loginUser.getId() + " ) as temp ";
		// int messageCount = chatService.getMessageCount0(strsql);
		int messageCount = 0;
		Vector messageIdList = RoomCacheUtil.getTwoUserContentIDList(loginUser
				.getId(), toUserId);
		messageCount = messageIdList.size();
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (messageCount > 10)
			messageCount = 10;
		pageCount = messageCount / NUMBER_PER_PAGE;
		if (messageCount % NUMBER_PER_PAGE != 0)
			pageCount++;

		// liuyi 2006-09-18 两人聊天记录加缓存 start
		// strsql = "select * from jc_room_content where from_id="
		// + loginUser.getId() + " and to_id=" + toUserId + " union all "
		// + " select * from jc_room_content where from_id= " + toUserId
		// + " and to_id=" + loginUser.getId()
		// + " order by id desc limit " + pageIndex * NUMBER_PER_PAGE
		// + "," + NUMBER_PER_PAGE;
		// Vector vecMessages = chatService.getMessageList0(strsql);
		int startIndex = Math.min(pageIndex * NUMBER_PER_PAGE, messageIdList
				.size());
		int endIndex = Math.min(pageIndex * NUMBER_PER_PAGE + NUMBER_PER_PAGE,
				messageIdList.size());
		Vector currentPageIdList = new Vector();
		for (int i = startIndex; i < endIndex; i++) {
			currentPageIdList.add(messageIdList.get(i));
		}
		Vector vecMessages = RoomCacheUtil.getContentList(currentPageIdList);
		// liuyi 2006-09-18 两人聊天记录加缓存 end
		if (request.getParameter("toUserId") != null)
			prefixUrl = prefixUrl + "?toUserId="
					+ request.getParameter("toUserId");
		if (prefixUrl.endsWith("&amp;"))
			prefixUrl = prefixUrl.substring(0, prefixUrl.length() - 5);
		String pagination = pagination(pageCount, pageIndex, prefixUrl,
				"pageIndex", response);
		request.setAttribute("pagination", pagination);
		request.setAttribute("messages", vecMessages);
		request.setAttribute("pageCount", pageCount + "");
	}

	/**
	 * fanys 2006-08-15 中转页面
	 * 
	 * @param url目的页面的url
	 * @return
	 */
	public static String getTransferPage(String url) {
		return getTransferPage(url, 1);
	}

	/**
	 * fanys 2006-08-15
	 * 
	 * @param url
	 *            目的页面的url
	 * @param seconds
	 *            从中转页面跳转到目的页面的时间,单位秒
	 * @return
	 */
	public static String getTransferPage(String url, int seconds) {
		StringBuilder wml = new StringBuilder();
		wml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		wml
				.append("<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">");
		wml.append("<wml>");
		wml.append("<card id=\"main\" title=\"载入中...\">");
		wml.append("<onevent type=\"ontimer\">");
		wml.append("<go href=\"" + url + "\"/>");
		wml.append("</onevent>");
		wml.append("<timer value=\"" + seconds + "\"/>");
		wml.append("<p mode=\"nowrap\" align=\"left\">载入中...</p>");
		wml.append("</card>");
		wml.append("</wml>");

		return wml.toString();
	}

	/**
	 * /chat/inviteRank.jsp fanys 2006-09-07
	 * 
	 * @param request
	 */
	public void getCurInviteRank(HttpServletRequest request) {
		// Vector vecResource = chatService.getRoomInviteResourceList(" 1=1 ");
//		Vector vecResource = userService.getCrownList(" image_path_id=1 ");
//		request.setAttribute("resource", vecResource);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(c.getTime());
		String strTimeWhere = " login_datetime>='" + today + "' ";
		Vector rankList = chatService.getCurRoomInviteRankList(strTimeWhere
				+ "  group by user_id order by count desc limit 0,7 ");
		StringBuilder strsql = new StringBuilder();

		strsql.append("select count(*) as c_id from( ");
		strsql
				.append("  select count(user_id)  as count from jc_room_invite where "
						+ strTimeWhere + " and invitee_id is not null ");
		strsql.append(" group by user_id having count> ");
		strsql.append(" (select count(user_id) from jc_room_invite  ");
		strsql
				.append(" where  " + strTimeWhere + " and user_id="
						+ loginUser.getId()
						+ " and invitee_id is not null )) as temp ");
		int inviteRank = chatService.getRoomInviteCount(strsql.toString());
		request.setAttribute("rank", String.valueOf(inviteRank + 1));
		request.setAttribute("rankList", rankList);
	}

	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfWeek() {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}

	/**
	 * 得到每月的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth() {
		Calendar c = Calendar.getInstance();
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, -(dayOfMonth - 1));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}

	/**
	 * 取得某日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());

	}

	/**
	 * fanys 2006-08-22 得到当前日期得上一周的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfLastWeek() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, 1);
		return getFirstDayOfWeek(c.getTime());
	}

	/**
	 * /chat/lastRank.jsp
	 * 
	 * @param request
	 */
	public void getLastWeekRank(HttpServletRequest request) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(c.getTime());
		int day = -1;
		if (null != request.getParameter("day"))
			day = StringUtil.toInt(request.getParameter("day"));
		day = Math.min(-1, day);
		day = Math.max(-7, day);
		c.add(Calendar.DAY_OF_YEAR, day);
		StringBuilder strsql = new StringBuilder();
		String strTimeWhere = " login_datetime>='" + today + "' ";
		strsql.append("select count(*) as c_id from( ");
		strsql
				.append("  select count(user_id)  as count from jc_room_invite where "
						+ strTimeWhere + " and invitee_id is not null ");
		strsql.append(" group by user_id having count> ");
		strsql.append(" (select count(user_id) from jc_room_invite  ");
		strsql
				.append(" where  " + strTimeWhere + " and user_id="
						+ loginUser.getId()
						+ " and invitee_id is not null )) as temp ");
		int inviteRank = chatService.getRoomInviteCount(strsql.toString());
		Vector rankList = chatService
				.getRoomInviteRankList(" a.create_datetime='"
						+ sdf.format(c.getTime()) + "' ");
		RoomInviteRankBean rankBean = null;
		for (int i = 0; i < rankList.size(); i++) {
			rankBean = (RoomInviteRankBean) rankList.get(i);
			rankBean.setCrown(userService.getCrown("id="
					+ rankBean.getResourceId()));
		}
		request.setAttribute("rank", (inviteRank + 1) + "");
		request.setAttribute("rankList", rankList);
		request.setAttribute("day", Math.abs(day) + "");
		sdf = new SimpleDateFormat("MM月dd日");
		request.setAttribute("date", sdf.format(c.getTime()));

	}

	public static void hourTask() {
		try {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
//			if (hour == 8)
//				sendInviteMessage();
			if (hour == 1) {
				//statInvite();
				// int weekday = c.get(Calendar.DAY_OF_WEEK);
				// if (weekday == 2) {// 周一为2
				// weeklyCalInviteCount();
				dayCalInviteCount();
				// LoadResource.loadLastWeekInviteRank();
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送每天0－8点用户发送的邀请信息
	 * 
	 */
	public static void sendInviteMessage() {
		Vector vecInvite = chatService.getRoomInviteList("send_mark=0");
		RoomInviteBean inviteBean = null;
		for (int i = 0; i < vecInvite.size(); i++) {
			inviteBean = (RoomInviteBean) vecInvite.get(i);
			// liuyi_2006-11-23_修改push发送方法 start
			String pushMessage = inviteBean.getName() + "对你说:"
					+ inviteBean.getContent();
			String url = "wap.joycool.net/jcadmin/chatInviteDeal.jsp?id="
					+ inviteBean.getId();
			boolean success = SmsUtil.sendPush(pushMessage, inviteBean
					.getMobile(), url);
			// liuyi_2006-11-23_修改push发送方法 end
			if (!success)
				chatService.updateRoomInvite("send_mark=1,success_mark=0",
						"id=" + inviteBean.getId());
			else
				chatService.updateRoomInvite("send_mark=1,success_mark=1",
						"id=" + inviteBean.getId());
		}
	}

	/**
	 * 每周一统计一次上周的邀请次数，建立上一周的群英排行榜
	 * 
	 */
	public static void dayCalInviteCount() {
		RoomInviteRankBean rankBean = null;
		CrownBean crown = null;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last7day = sdf.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		String last8day = sdf.format(c.getTime());
		// 去除7天前王冠获得者的王冠,如果改用户在最近7天获取过王冠，则不处理
		Vector vecLast8CrownOwner = chatService
				.getRoomInviteRankList(" a.create_datetime='" + last8day + "'");
		Vector vecNowCrownOwner = chatService
				.getRoomInviteRankList(" a.create_datetime>='" + last7day
						+ "' ");
		RoomInviteRankBean tempRankBean = null;
		for (int i = 0; i < vecLast8CrownOwner.size(); i++) {
			rankBean = (RoomInviteRankBean) vecLast8CrownOwner.get(i);
			boolean hasCrown = false;
			for (int j = 0; j < vecNowCrownOwner.size(); j++) {
				tempRankBean = (RoomInviteRankBean) vecNowCrownOwner.get(j);
				if (tempRankBean.getUserId() == rankBean.getUserId()) {
					hasCrown = true;
					break;
				}
			}
			if (true == hasCrown)
				continue;
			UserInfoUtil.updateUserStatus("image_path_id=0",
					"user_id=" + rankBean.getUserId(), rankBean.getUserId(),
					UserCashAction.OTHERS, "删除用户王冠");
			// 如果该用户有其它王冠，则把其它王冠加回去
			UserTopBean userTop = topService.getUserTop("user_id="
					+ rankBean.getUserId() + " and mark=2");
			if (null != userTop) {
				//crown = userService.getCrown("id=" + userTop.getImageId());
				UserInfoUtil.updateUserStatus("image_path_id=-"+userTop.getImageId(), "user_id="
						+ rankBean.getUserId(), rankBean.getUserId(),
						UserCashAction.OTHERS, "添加用户信誉王冠");
			}
			UserStatusBean.flushUserHat(rankBean.getUserId());
		}
		c = Calendar.getInstance();
		String today = sdf.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		String yesterday = sdf.format(c.getTime());
		// 给昨天前7名加上王冠
		Vector rankList = chatService
				.getCurRoomInviteRankList(" login_datetime>='" + yesterday
						+ "'  and login_datetime<'" + today
						+ "' group by user_id order by count desc limit 0,7 ");
//		Vector crownList = userService
//				.getCrownList("  image_path_id=1 order by rand() limit 0,7");
		for (int i = 0; i < rankList.size(); i++) {
			rankBean = (RoomInviteRankBean) rankList.get(i);
			//crown = (CrownBean) crownList.get(i);
			rankBean.setResourceId(i+1);
			rankBean.setCreateDateTime(yesterday);
			chatService.addRoomInviteRankBean(rankBean);
			UserInfoUtil.updateUserStatus("image_path_id=-"+(i+1), "user_id=" + rankBean.getUserId(),
					UserCashAction.OTHERS, rankBean.getUserId(), "添加用户王冠");
			UserStatusBean.flushUserHat(rankBean.getUserId());

		}

	}

	/**
	 * 每周一统计一次上周的邀请次数，建立上一周的群英排行榜
	 * 
	 */
	public static void weeklyCalInviteCount() {
		RoomInviteRankBean rankBean = null;
		CrownBean crown = null;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -14);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 去除上上周王冠获得者的王冠
		Vector vecLastCrownOwner = chatService
				.getRoomInviteRankList(" a.create_datetime='"
						+ sdf.format(c.getTime()) + "'");
		for (int i = 0; i < vecLastCrownOwner.size(); i++) {
			rankBean = (RoomInviteRankBean) vecLastCrownOwner.get(i);
			UserInfoUtil.updateUserStatus("image_path_id=0",
					"user_id=" + rankBean.getUserId(), rankBean.getUserId(),
					UserCashAction.OTHERS, "删除用户王冠");
			UserTopBean userTop = topService.getUserTop("user_id="
					+ rankBean.getUserId() + " and mark=2");
			if (null != userTop) {
				//crown = userService.getCrown("id=" + userTop.getImageId());
				UserInfoUtil.updateUserStatus("image_path_id=-"+userTop.getImageId(), "user_id="
						+ rankBean.getUserId(), rankBean.getUserId(),
						UserCashAction.OTHERS, "添加用户信誉王冠");
				UserStatusBean.flushUserHat(rankBean.getUserId());
			}

		}
		// 给上周前7名加上王冠
		Vector rankList = chatService
				.getCurRoomInviteRankList(" login_datetime>='"
						+ getFirstDayOfLastWeek() + "'  and login_datetime<'"
						+ getFirstDayOfWeek()
						+ "' group by user_id order by count desc limit 0,7 ");
		//Vector crownList = userService
		//		.getCrownList("  image_path_id=1 order by rand() limit 0,7");
		for (int i = 0; i < rankList.size(); i++) {
			rankBean = (RoomInviteRankBean) rankList.get(i);
			//crown = (CrownBean) crownList.get(i);
			rankBean.setResourceId(i+1);
			rankBean.setCreateDateTime(getFirstDayOfLastWeek());
			chatService.addRoomInviteRankBean(rankBean);
			UserInfoUtil.updateUserStatus("image_path_id=-"+(i+1), "user_id=" + rankBean.getUserId(),
					rankBean.getUserId(), UserCashAction.OTHERS, "添加用户王冠");
			UserStatusBean.flushUserHat(rankBean.getUserId());
		}

	}

	/**
	 * fanys 2006-08-17 邀请好友来乐酷－－统计
	 */
	public static void statInvite() {
		int inviteCount = 0;// 下发总数
		int acceptNewCount = 0;// 接收的新用户数
		int replyCount = 0;// 回复的用户总数
		int replyNewcount = 0;// 回复的新用户数
		int reachLimitCount = 0;// 发送达到上限的人数

		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = df.format(c.getTime());
		c.add(Calendar.DAY_OF_YEAR, -1);
		String startDate = df.format(c.getTime());
		String strWhere = " send_datetime>='" + startDate
				+ "' and send_datetime<'" + endDate + "' ";
		inviteCount = chatService.getRoomInviteCount(strWhere);
		acceptNewCount = chatService.getRoomInviteCount(strWhere
				+ " and new_user_mark=1");
		replyCount = chatService.getRoomInviteCount(strWhere + " and mark=1");
		replyNewcount = chatService.getRoomInviteCount(strWhere
				+ " and  invitee_id is not null ");
		reachLimitCount = chatService
				.getRoomInviteCount("select count(*) as c_id from (select count(*) as c_id from jc_room_invite where "
						+ strWhere
						+ " group by user_id having c_id>=5) as temp ");
		RoomInviteStatBean statBean = new RoomInviteStatBean();
		statBean.setInviteCount(inviteCount);
		statBean.setAcceptNewCount(acceptNewCount);
		statBean.setReplyCount(replyCount);
		statBean.setReplyNewCount(replyNewcount);
		statBean.setReachLimitCount(reachLimitCount);
		statBean.setStatDatetime(startDate);
		chatService.deleteRoomInviteStat(" stat_datetime>='" + startDate
				+ "' and stat_datetime<' " + endDate + "'");
		chatService.addRoomInviteStat(statBean);

	}

	/**
	 * fanys 2006-08-21 分页
	 * 
	 * @param totalPageCount
	 *            总的页数
	 * @param curPageIndex
	 *            当前页号
	 * @param pageCount
	 *            界面一次显示几页
	 * @param prefixUrl
	 *            前缀url参数
	 * @param separator
	 *            分隔符
	 * @param parameterMap
	 *            参数Map
	 * @param response
	 * @return
	 */
	public static String pagination(int totalPageCount, int curPageIndex,
			int pageCount, String prefixUrl, String separator,
			HashMap parameterMap, HttpServletResponse response) {
		if (totalPageCount == 1) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int hasPrevPage = 0;
		int hasNextPage = 0;
		int startIndex = (curPageIndex / pageCount) * pageCount;
		int endIndex = (curPageIndex / pageCount + 1) * pageCount - 1;
		if (endIndex > totalPageCount - 1) {
			endIndex = totalPageCount - 1;
		}
		if (startIndex > 0) {
			hasPrevPage = 1;
		}
		if (endIndex < totalPageCount - 1) {
			hasNextPage = 1;
		}
		String key = null;
		StringBuilder parameter = new StringBuilder();
		for (Iterator it = parameterMap.keySet().iterator(); it.hasNext();) {
			key = (String) it.next();
			parameter.append("<postfield name=\"" + key + "\"" + " value=\""
					+ (String) parameterMap.get(key) + "\" />");
		}
		if (hasPrevPage == 1) {

			sb.append("<anchor title=\"go\">");
			sb.append("&lt;&lt;");
			sb.append("<go href=\"" + (prefixUrl)
					+ "\" method=\"post\">");
			sb.append(parameter);
			sb.append("<postfield name=\"pageIndex\" value=\""
					+ (startIndex - 1) + "\" />");
			sb.append("</go></anchor>");
		}
		for (int i = startIndex; i <= endIndex; i++) {
			if (sb.length() > 0) {
				sb.append(separator);
			}
			if (i != curPageIndex) {

				sb.append("<anchor title=\"go\">");
				sb.append((i + 1));
				sb.append("<go href=\"" + (prefixUrl)
						+ "\" method=\"post\">");
				sb.append(parameter);
				sb.append("<postfield name=\"pageIndex\" value=\"" + i
						+ "\" />");
				sb.append("</go></anchor>");
			} else {
				sb.append((i + 1));
			}
		}
		if (hasNextPage == 1) {
			if (sb.length() > 0) {
				sb.append(separator);
			}

			sb.append("<anchor title=\"go\">");
			sb.append("&gt;&gt;");
			sb.append("<go href=\"" + (prefixUrl)
					+ "\" method=\"post\">");
			sb.append(parameter);
			sb.append("<postfield name=\"pageIndex\" value=\"" + (endIndex + 1)
					+ "\" />");
			sb.append("</go></anchor>");
		}
		return sb.toString();
	}

	/**
	 * fanys 2006-08-21
	 * 
	 * @param totalPageCount
	 *            总页数
	 * @param curPageIndex
	 *            当前页号
	 * @param prefixUrl
	 *            url前缀
	 * @param parameterMap
	 *            参数map
	 * @param response
	 * @return
	 */
	public static String pagination(int totalPageCount, int curPageIndex,
			String prefixUrl, HashMap parameterMap, HttpServletResponse response) {
		return pagination(totalPageCount, curPageIndex, 5, prefixUrl, "|",
				parameterMap, response);
	}

	/**
	 * fanys 2006-08-22
	 * 
	 * @param fromUserId
	 *            发言者
	 * @param toUserId
	 *            接收者
	 * @param fromUserRoomId
	 *            发言者所在聊天室
	 * @return
	 */
	public static int[] getSecondRoomId(int fromUserId, int toUserId,
			int fromUserRoomId) {
		int[] ret = new int[] { -1, -1 };
		int toUserRoomId = -1;
		OnlineBean online = userService.getOnlineUser("user_id=" + toUserId);
		if (toUserId == 0) {// 对所有人发言，此时传过来的toUserId＝0
			ret[0] = fromUserRoomId;
			ret[1] = -1;
			return ret;
		}
		if (online != null) {// 乙在线的时候
			toUserRoomId = online.getSubId();
			if (online.getPositionId() != ModuleBean.CHAT) {// 乙不在聊天
				ret[0] = fromUserRoomId;
				ret[1] = -1;
			} else {// 乙在聊天
				boolean fromRoomUserExist = false;
				boolean toRoomUserExist = false;
				fromRoomUserExist = RoomUtil.isUserInRoom(fromUserRoomId,
						toUserId);
				// fromRoomUser = chatService.getOnlineUser("user_id=" +
				// toUserId
				// + " and room_id=" + fromUserRoomId);
				// toRoomUser = chatService.getOnlineUser("user_id=" +
				// fromUserId
				// + " and room_id=" + toUserRoomId);
				toRoomUserExist = RoomUtil.isUserInRoom(toUserRoomId,
						fromUserId);
				if (fromRoomUserExist) {// 乙曾经在甲所在聊天室
					if (toUserRoomId == fromUserRoomId) {// 当前同一聊天室
						ret[0] = fromUserRoomId;
						ret[1] = fromUserRoomId;
					} else if (toRoomUserExist) {// 甲曾经在乙所在聊天室
						ret[0] = fromUserRoomId;
						ret[1] = toUserRoomId;
					} else {// 甲从未在乙所在聊天室
						ret[0] = fromUserRoomId;
						ret[1] = -1;
					}
				} else {// 乙从未在甲所在聊天室
					if (toRoomUserExist) {// 甲如果曾经来过乙所在聊天室
						ret[0] = -1;
						ret[1] = toUserRoomId;
					} else {// 甲从未来过乙所在聊天室
						ret[0] = -1;
						ret[1] = -1;
					}
				}
			}
		} else { // 乙不在线的时候
			ret[0] = fromUserRoomId;
			ret[1] = -1;
		}
		return ret;
	}

	/**
	 * zhul 2006-09-16
	 * 
	 * @param roomId
	 * @return 本聊天室的n个聊客
	 */
	public Object[] getRoomChater(String roomId) {
		// 从缓存中取聊天室聊客
		Vector roomChater = (Vector) CacheAdmin.getFromCache(roomId,
				OsCacheUtil.ROOM_CHATER_GROUP, OsCacheUtil.CHATER_FLUSH_PERIOD);
		// 如果缓存中没有,从数据库加载并放入缓存
		if (roomChater == null || roomChater.size() == 0) {
			String condition = "";
			if (loginUser == null) {
				condition = " room_id=" + roomId + " limit 30 ";
			} else {
				condition = " room_id=" + roomId + " and user_id !="
						+ loginUser.getId() + " limit 30 ";
			}
			roomChater = chatService.getOnlineList(condition);
			OsCacheUtil.put(roomId, roomChater, OsCacheUtil.ROOM_CHATER_GROUP);
		}
		// 将聊客随机化
		Object[] objs = roomChater.toArray();
		if (objs.length > 5)
			shuffle(objs);

		return objs;
	}

	/**
	 * zhul 2006-09-16 对对象数组洗牌
	 * 
	 * @param array
	 * @return
	 */
	public Object[] shuffle(Object[] array) {
		Random random = new Random();
		int len = array.length;
		for (int i = len; i > 1; i--) {
			int a = random.nextInt(i);
			Object temp = array[a];
			array[a] = array[i - 1];
			array[i - 1] = temp;
		}
		return array;
	}

	/**
	 * zhul_2006-09-16 获取除了小黑屋及当前聊天室之外的其他聊天室ID
	 * 
	 * @param currentRoomId
	 * @return
	 */
	public int[] getRoomId(int currentRoomId) {
		int[] rooms = (int[]) CacheAdmin.getFromCache("roomNum",
				OsCacheUtil.ROOM_ID_GROUP, OsCacheUtil.ROOMID_FLUSH_PERIOD);
		if (rooms == null) {
			rooms = chatService.getAllRoomId();
			OsCacheUtil.put("roomNum", rooms, OsCacheUtil.ROOM_ID_GROUP);
		}
		int[] room = null;
		if (rooms.length < 3)
			return rooms;
		else if (currentRoomId == 1)
			room = new int[rooms.length - 1];
		else
			room = new int[rooms.length - 2];
		int j = -1;
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] != 1 && rooms[i] != currentRoomId)
				room[++j] = rooms[i];
		}
		return TigerAction.riffle(room);
	}

	/**
	 * zhul 2006-09-18 获取不在本聊天室的用户
	 * 
	 * @param roomId
	 * @return
	 */
	public Object[] getOutChatPeop() {
		// 从缓存中取不在聊天室的人
		Vector userList = (Vector) CacheAdmin.getFromCache("outRooms",
				OsCacheUtil.OUT_ROOM_GROUP, OsCacheUtil.OUTROOM_FLUSH_PERIOD);
		// 如果缓存中没有,从数据库加载并放入缓存
		if (userList == null || userList.size() == 0) {
			String condition = "";
			if (loginUser == null) {
				// macq_2006-10-24_更改查询内容,增加获取聊天室用户_start
				// condition = " c.position_id <> 0 AND c.position_id <> 1 AND
				// a.id <> 0 ORDER BY rand() limit 0,100";
				condition = " c.position_id <> 0  AND a.id <> 0 ORDER BY rand() limit 0,100";
				// macq_2006-10-24_更改查询内容,增加获取聊天室用户_end
			} else {
				// macq_2006-10-24_更改查询内容,增加获取聊天室用户_start
				// condition = " a.gender<>"
				// + loginUser.getGender()
				// + " AND c.position_id <> 0 AND c.position_id <> 1 AND a.id <>
				// 0 ORDER BY rand() limit 0,100";
				condition = " a.gender<>"
						+ loginUser.getGender()
						+ " AND c.position_id <> 0 AND a.id <> 0 ORDER BY rand() limit 0,100";
				// macq_2006-10-24_更改查询内容,增加获取聊天室用户_end
			}
			userList = userService.getOnlineUserList(condition);
			OsCacheUtil.put("outRooms", userList, OsCacheUtil.OUT_ROOM_GROUP);
		}
		// 获取模块的随机概率
		int probability = this.getProbability();
		UserBean user = null;
		Vector probabilityVector = new Vector();
		for (int i = 0; i < userList.size(); i++) {
			// 取得一个在线用户
			user = (UserBean) userList.get(i);
			// 判断用户位置是否和随机抽取模块位置一致,如果一致添加到列表中
			if (user.getPositionId() == probability) {
				probabilityVector.add(user);
			}
		}
		// 将聊客随机化
		Object[] objs = null;
		// 判断如果有2个用户所在位置和随机抽取模块位置一致,覆盖当前userList
		if (probabilityVector.size() > 2) {
			objs = probabilityVector.toArray();
		} else {
			objs = userList.toArray();
		}
		if (objs.length > 2)
			shuffle(objs);
		return objs;
	}

	/*
	 * 获取随机生成
	 */
	public int getProbability() {
		int result = 0;
		// 得到模块概率map
		TreeMap probabilityMap = ProbabilityUtil.getCatalogProbabilityMap();
		// 概率基数
		Integer base = (Integer) probabilityMap.get(new Integer(
				Constants.RANDOM_BASE));
		// 得到一个随机数
		int random = RandomUtil.nextInt(base.intValue()) + 1;
		// 迭代probabilityMap中所有概率
		Set keys = probabilityMap.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			// 找到与随机结果相符的值返回
			if (key.intValue() >= random) {
				result = ((Integer) probabilityMap.get(key)).intValue();
				return result;
			}
		}
		return result;
	}

	/**
	 * 设置用户免骚扰功能开关
	 * 
	 * @param request
	 */
	public void isHarass(HttpServletRequest request) {
		String flag = request.getParameter("flag");
		if (flag == null) {
			return;
		} else {
			UserBean user = (UserBean) OnlineUtil.getOnlineBean(loginUser
					.getId()
					+ "");
			if (flag.equals("1")) {
				loginUser.setHarass(1);
				user.setHarass(1);
			} else {
				loginUser.setHarass(0);
				user.setHarass(0);
			}
		}
		return;
	}

	// liq 2007.03.30
	// 通过条件得到聊天室人数列表
	public void getOnlineList_1(HttpServletRequest request,
			HttpServletResponse response) {
		Vector vecOnline = new Vector();

		int pageIndex = 0;
		int roomId = 0;

		int totalPages = 0;
		int totalUserCount = 0;
		int startIndex = 0;
		int endIndex = 0;
		// int gender = Integer.parseInt((String)
		// request.getAttribute("gender"));

		if (request.getParameter("roomId") != null)
			roomId = Integer.parseInt(request.getParameter("roomId"));
		String prefixUrl = "prisoner.jsp?roomId=" + roomId;
		// if (gender == 0)
		// prefixUrl = "onlineListWoman.jsp?roomId=" + roomId;
		// else
		// prefixUrl = "onlineListMan.jsp?roomId=" + roomId;
		if (request.getParameter("pageIndex") != null)
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));

		StringBuilder strsql = new StringBuilder();
		// 如果该用户的地区编码不为0，也即该用户的地区名称不为空
		List roomOnlineUserList = RoomCacheUtil.getRoomOnlineUserIdList(roomId);

		int userId = 0;
		UserBean userBean = null;
		Vector cityNoNull = new Vector();
		// 迭代获取总的满足条件的挂线用户数,和挂线用户信息列表
		for (int i = 0; i < roomOnlineUserList.size(); i++) {
			userId = ((Integer) roomOnlineUserList.get(i)).intValue();
			userBean = UserInfoUtil.getUser(userId);
			if (userBean == null) {
				continue;
			}
			if (userBean.getId() != loginUser.getId()) {
				totalUserCount++;
				cityNoNull.add(userBean);
			}
		}
		totalPages = totalUserCount / ONLINE_NUMBER_PER_PAGE_FRIEND;
		if (totalUserCount % ONLINE_NUMBER_PER_PAGE_FRIEND != 0)
			totalPages++;
		pageIndex = getCurIndex(totalPages, pageIndex);
		startIndex = pageIndex * ONLINE_NUMBER_PER_PAGE_FRIEND;
		int maxAndMin = startIndex + ONLINE_NUMBER_PER_PAGE_FRIEND;
		if (maxAndMin > cityNoNull.size()) {
			maxAndMin = cityNoNull.size();
		}
		for (int i = startIndex; i < maxAndMin; i++) {
			userBean = (UserBean) cityNoNull.get(i);
			vecOnline.add(userBean);
		}

		request.setAttribute("onlineCount", totalUserCount + "");
		String pagination = pagination(totalPages, pageIndex, prefixUrl,
				"pageIndex", response);
		request.setAttribute("pagination", pagination);
		request.setAttribute("vecOnline", vecOnline);

	}

}
