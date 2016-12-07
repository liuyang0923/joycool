/*
 * Created on 2005-11-16
 *
 */
package net.joycool.wap.action.user;

import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author lbj
 * 
 */
public class ViewFriendsAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserBean loginUser = getLoginUser(request);
		int userId = -1;
		if (request.getParameter("userId") != null) {
			userId = Integer.parseInt(request.getParameter("userId"));
		} else {
			if (loginUser == null) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			userId = loginUser.getId();
		}

		/*
		// zhul alter 2006/06/09 start 由于要将用户分类细化，所以将此类功能改为返回在线好友，离线好友，及陌生人的人数
		//zhul 2006-10-12 获取在线用户id列表 start
		ArrayList onlineUserId=service.getOnlineUserIdList();
		String onlineUserIdStr=onlineUserId.toString();
		String onlineUserStr=onlineUserIdStr.substring(1,onlineUserIdStr.length()-1);
		//zhul 2006-10-12 获取在线用户id列表 end
	
		int onlineFriendsCount = service.getFriendCount("user_id = " + userId
				+ " and friend_id in ("+onlineUserStr+")");

		
		int outlineFriendsCount = service.getFriendCount("user_id = " + userId
				+ " and friend_id not in ("+onlineUserStr+")");

		// DbOperation dbOp = new DbOperation();
		// dbOp.init();
		// String query = "select count(u_id) as c_id from (select u_id from
		// (select
		// friend_id as u_id from user_friend where user_id = "
		// + userId
		// + " union select user_id as u_id from jc_online_user) as temp group
		// by u_id
		// having count(u_id) = 1) as temp1";
		// ResultSet rs = dbOp.executeQuery(query);
		// int outlineFriendsCount = 0;
		// if (rs.next()) {
		// outlineFriendsCount = rs.getInt("c_id");
		// }
		// dbOp.release();
		// int strangeCount = service
		// .getOnlineUserCount("user_id in (select from_id as id from
		// jc_room_content where to_id = "
		// + userId
		// + " union select to_id as id from jc_room_content where from_id = "
		// + userId
		// + ") and user_id not in (select friend_id from user_friend where
		// user_id = "
		// + userId + ") ");
		// zhul add code 2006-06-15 start 如果在线好友小于10人则显示好友，3个离线好友和3个陌生人
		if (onlineFriendsCount <= 10) {
			Vector onlineFriendsList = new Vector();
			/*
			//liuyi 2006-09-30 sql优化 start (去掉order by id)
			onlineFriendsList = service
					.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id="
							+ userId
							+ " AND friend_id IN("+onlineUserStr+")) as temp on user_info.id=temp.friend_id ");
			//liuyi 2006-09-30 sql优化 end
			 Vector outlineFriendsList = new Vector();
			outlineFriendsList = service
					.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id = "
							+ userId
							+ " and friend_id NOT IN ("+onlineUserStr+")) as temp on user_info.id=temp.friend_id order by id limit 0,3");

			// Vector strangerList = new Vector();
			// strangerList = service
			// .getUserList("join (select user_id from jc_online_user where
			// user_id in (select from_id as id from jc_room_content where to_id
			// = "
			// + userId
			// + " union select to_id as id from jc_room_content where from_id =
			// "
			// + userId
			// + ") and user_id not in (select friend_id from user_friend where
			// user_id = "
			// + userId
			// + ")) as temp on user_info.id=temp.user_id ORDER BY id limit
			// 0,3");
			

			request.setAttribute("onlineFriendsList", onlineFriendsList);
			request.setAttribute("outlineFriendsList", outlineFriendsList);
			// request.setAttribute("strangerList", strangerList);

		}
		// 如果在线好友多于10人则显示10个好友加离线好友和陌生人链接
		else {
			Vector onlineFriendsList = new Vector();
			onlineFriendsList = service
					.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id="
							+ userId
							+ " AND friend_id IN("+onlineUserStr+")) as temp on user_info.id=temp.friend_id order by id limit 0,10");
			request.setAttribute("onlineFriendsList", onlineFriendsList);
		}
		*/
		//zhul 2006-10-13 优化好友 start
		//zhul 2006-10-13 获取用户好友
		ArrayList userFriends = UserInfoUtil.getUserFriends(userId);
		//在线好友
		Vector onlineFriendsList = new Vector();
		//离线好友
		Vector outlineFriendsList = new Vector();

		for(int i=0;i<userFriends.size();i++)
		{
			String userIdKey=(String)userFriends.get(i);
			if(OnlineUtil.isOnline(userIdKey))
				onlineFriendsList.add(userIdKey);
			else
				outlineFriendsList.add(userIdKey);
		}

		request.setAttribute("onlineFriendsList", onlineFriendsList);
		request.setAttribute("outlineFriendsList", outlineFriendsList);
		//zhul 2006-10-13 优化好友 end
		
		//zhul 2006-10-16 优化三个同城市用户 start
		Vector vecOnline = new Vector();
		//异性
		int gender = loginUser.getGender()==0?1:0;
		//所有在线用户
		ArrayList onlineUser=OnlineUtil.getAllOnlineUser();
		//非手机用户
		if (loginUser.getUserAgent() != null
				&& loginUser.getUserAgent().toLowerCase().indexOf("opera") != -1) {
			for(int i=0;i<onlineUser.size();i++)
			{
				String id=(String)onlineUser.get(i);
				UserBean user=UserInfoUtil.getUser(StringUtil.toInt(id));
				if(user!=null && user.getGender()==gender){
					vecOnline.add(id);
					if(vecOnline.size()>3) break;
				}
			}
		}else{//手机用户
			int cityNo = loginUser.getCityno();
			cityNo = cityNo == 0 ? 55 : loginUser.getCityno();

			for(int i=0;i<onlineUser.size();i++)
			{
				String id=(String)onlineUser.get(i);
				UserBean user=UserInfoUtil.getUser(StringUtil.toInt(id));
				if(user!=null && user.getGender()==gender && user.getCityno()==cityNo){
					vecOnline.add(id);
					if(vecOnline.size()>3) break;
				}
			}		
		}
		request.setAttribute("vecOnline", vecOnline);
		//zhul 2006-10-16 优化三个同城市用户 end
				
		// request.setAttribute("strangeCount", strangeCount + "");
		// zhul alter 2006/06/09 end 由于要将用户分类细化，所以将此类功能改为返回在线好友，离线好友，及陌生人的人数
		// request.setAttribute("backTo", backTo);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
