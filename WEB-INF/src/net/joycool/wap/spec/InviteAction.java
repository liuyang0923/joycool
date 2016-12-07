package net.joycool.wap.spec;

import java.util.LinkedList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import net.joycool.wap.action.NoticeAction;
//import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.chat.RoomInviteBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.framework.CustomAction;

import net.joycool.wap.service.impl.ChatServiceImpl;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;

import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 邀请好友，321414.joycool.net域名功能等
 *
 */
public class InviteAction extends CustomAction{
	public static String inviteUserKey = "inviteUser";
	public static String inviteTypeKey = "inviteType";
	
	static UserServiceImpl service = new UserServiceImpl();
	static ChatServiceImpl chatService = new ChatServiceImpl();
	
	int userId;
	int type;
	// 返回域名对应的用户id
	
	public void getUserIdFromDomain() {
		String domain = request.getParameter("f");

		if(domain != null && domain.length() > 0) {
			if(domain.endsWith("m")) {
				type = 1;
				domain = domain.substring(0, domain.length() - 1);
			}
			userId = StringUtil.toId(domain);
			return;
		}
	}
	
	// 直接输入uid和type
	public void setTypeAndUid(int type,int uid){
		if (uid > 0){
			this.userId = uid;
			this.type = 1;
			session.setAttribute(inviteUserKey, new Integer(userId));
			session.setAttribute(inviteTypeKey, new Integer(type));
		} 
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void index() {
		getUserIdFromDomain();
		if(userId != 0) {
			if(UserInfoUtil.getUser(userId) != null) {
				session.setAttribute(inviteUserKey, new Integer(userId));
				session.setAttribute(inviteTypeKey, new Integer(type));
			} else
				userId = 0;
		}
	}
	
	public int getSessionInviteUser() {
		Integer id = (Integer)session.getAttribute(inviteUserKey);
		if(id == null)
			return 0;
		return id.intValue();
	}
	public static int getSessionInviteUser(HttpSession session) {
		Integer id = (Integer)session.getAttribute(inviteUserKey);
		if(id == null)
			return 0;
		return id.intValue();
	}
	
	public InviteAction(HttpServletRequest request) {
		super(request);
	};
	public InviteAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	// 邀请页面
	public void invite() {
		
	}
	
	/**
	 * 取得邀请信息的bean
	 */
	public RoomInviteBean getMyInvite(){
		return chatService.getRoomInvite(" invitee_id=" + this.getLoginUser().getId() + " limit 1");
	}
	
	/**
	 * 根据输入的UID来确定邀请关系
	 * @param uid:输入的邀请人
	 */
	public int confirm(int uid){
		UserBean loginUser = this.getLoginUser();
		if (loginUser == null){
			// 失败
			return 2;
		}
		if (getMyInvite() != null){
			// 失败。此用户已经填写过邀请信息了。
			return 2;
		}
		if (uid <= 0){
			// 失败
			return 2;
		} 
		UserBean sendUser = UserInfoUtil.getUser(uid);
		if (sendUser == null){
			// 失败
			return 2;
		}
		CacheManage.friendTrend.spt(new Integer(loginUser.getId()), new LinkedList());	// 新注册的人动态列表为空
		
		//邀请者与被邀请者相互必须加为好友
		service.addFriend(loginUser.getId(), sendUser.getId());
		service.addFriend(sendUser.getId(), loginUser.getId());
		ActionTrend.addTrend(sendUser.getId(), BeanTrend.TYPE_BE_FRIEND, "%1 和 %2 成为好友", 
				sendUser.getNickName(),loginUser.getId(), loginUser.getNickName());
				
		ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_BE_FRIEND, "%1 和 %2 成为好友", 
				loginUser.getNickName(),sendUser.getId(), sendUser.getNickName());
		// 谁邀请了谁
		RoomInviteBean invite = new RoomInviteBean();
		invite.setUserId(sendUser.getId());
		invite.setMobile(sendUser.getMobile());
		invite.setName(sendUser.getNickName());
		invite.setContent("");
		invite.setMark(0);
		invite.setNewUserMark(1);
		invite.setInviteeId(loginUser.getId());
		chatService.addRoomInviteOk(invite);
		
//		// 奖励给邀请我的人5000W
//		UserInfoUtil.updateUserCash(sendUser.getId(), 50000000, 20, "邀请" + loginUser.getId() + "邀请获得5000万");
//		NoticeAction.sendNotice(sendUser.getId(), "邀请成功!赠五千万!", "", 
//				NoticeBean.GENERAL_NOTICE, "", "/user/invite/ts.jsp?uid=" + loginUser.getId());
		return 3;
	}
	
	/**
	 * 我总共邀请的人数
	 */
	public int totalInvite(){
		return SqlUtil.getIntResult("select count(id) from jc_room_invite where user_id=" + this.getLoginUser().getId(), 0);
	}
	
	/**
	 * 我本周共邀请的人数
	 */
	public int inviteInThisWeek(){
		return SqlUtil.getIntResult("select count(id) from jc_room_invite where user_id=" + this.getLoginUser().getId() + " and send_datetime>='" + DateUtil.getFirstDayOfWeek() + "'", 0);
	}
	
	/**
	 * 取得邀请List
	 */
	public Vector getInviteList(String cond){
		return chatService.getRoomInviteList(cond);
	}
}
