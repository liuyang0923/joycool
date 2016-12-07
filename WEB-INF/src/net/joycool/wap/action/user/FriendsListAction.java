/*
 * Created on 2006-06-09
 *author zhul
 *根据参数option来判断要显示的用户类型，查询数据库并返回
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
public class FriendsListAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// 取得参数
		String option = request.getParameter("option");
		if (option == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		//判断用户id
		int userId = -1;
		if (request.getParameter("userId") != null) {
			userId = Integer.parseInt(request.getParameter("userId"));
		} else {
			UserBean loginUser = getLoginUser(request);
			if (loginUser == null) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			userId = loginUser.getId();
		}

		//zhul 2006-10-13 优化好友 start
		//zhul 2006-10-13 获取用户好友
		ArrayList userFriends = UserInfoUtil.getUserFriends(userId);
		Vector userList=new Vector();
		if (option.equals("onlinefriends")) {
			//在线好友
			for(int i=0;i<userFriends.size();i++)
			{
				String userIdKey=(String)userFriends.get(i);
				if(OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}
			
		} else if (option.equals("outlinefriends")) {
			//离线好友
			for(int i=0;i<userFriends.size();i++)
			{
				String userIdKey=(String)userFriends.get(i);
				if(!OnlineUtil.isOnline(userIdKey))
					userList.add(userIdKey);
			}
			
		}
		
		int PER_PAGE_NUM = 10;
		int totalCount =userList.size();
		int totalPage = (totalCount + PER_PAGE_NUM - 1) / PER_PAGE_NUM;
		int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
		if (pageIndex > totalPage - 1) {
			pageIndex = totalPage - 1;
		}
		if (pageIndex < 0) {
			pageIndex = 0;
		}

		int start=pageIndex*PER_PAGE_NUM;
		int end = pageIndex*PER_PAGE_NUM+PER_PAGE_NUM;

		request.setAttribute("start", start + "");
		request.setAttribute("end", end + "");
		request.setAttribute("userList", userList);
		request.setAttribute("totalPage", totalPage+"");
		request.setAttribute("pageIndex", pageIndex+"");
		request.setAttribute("option", option);
		
		//zhul 2006-10-13 优化好友 end
		/*
		Vector userList = new Vector();
		//zhul 2006-10-12 获取在线用户id列表 start
		ArrayList onlineUserId=service.getOnlineUserIdList();
		String onlineUserIdStr=onlineUserId.toString();
		String onlineUserStr=onlineUserIdStr.substring(1,onlineUserIdStr.length()-1);
		//zhul 2006-10-12 获取在线用户id列表 end
		if (option.equals("onlinefriends")) {
			userList = service
					.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id="
							+ userId
							+ " AND friend_id IN("+onlineUserStr+")) as temp on user_info.id=temp.friend_id order by id limit "
							+ (Integer.parseInt(pageIndex) * perPageRecords + 10)
							+ "," + perPageRecords);
			request.setAttribute("marker", "0");
			request.setAttribute("option", "onlinefriends");
		} else if (option.equals("outlinefriends")) {
			if (Integer.parseInt(marker) == 0) {
				userList = service
						.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id = "
								+ userId
								+ " and friend_id NOT IN ("+onlineUserStr+")) as temp on user_info.id=temp.friend_id order by id limit "
								+ Integer.parseInt(pageIndex)
								* perPageRecords
								+ "," + perPageRecords);
				request.setAttribute("marker", "0");
			} else {
				userList = service
						.getUserList("join (SELECT friend_id FROM user_friend WHERE user_id = "
								+ userId
								+ " and friend_id NOT IN ("+onlineUserStr+")) as temp on user_info.id=temp.friend_id order by id limit "
								+ (Integer.parseInt(pageIndex) * perPageRecords + 3)
								+ "," + perPageRecords);
				request.setAttribute("marker", "1");
			}
			request.setAttribute("option", "outlinefriends");
		} 
		/*
		else if (option.equals("strange")) {
			int strangeCount = service
					.getOnlineUserCount("user_id in (select from_id as id from jc_room_content where to_id = "
							+ userId
							+ " union select to_id as id from jc_room_content where from_id = "
							+ userId
							+ ") and user_id not in (select friend_id from user_friend where user_id = "
							+ userId + ") ");
			totaPages=((strangeCount+perPageRecords-1)/perPageRecords)+"";

			if (Integer.parseInt(marker) == 0) {
				userList = service
						.getUserList("join (select user_id from jc_online_user where user_id in (select from_id as id from jc_room_content where to_id = "
								+ userId
								+ " union select to_id as id from jc_room_content where from_id = "
								+ userId
								+ ") and user_id not in (select friend_id from user_friend where user_id = "
								+ userId
								+ ")) as temp on user_info.id=temp.user_id ORDER BY id limit "
								+ Integer.parseInt(pageIndex)
								* perPageRecords
								+ "," + perPageRecords);
				request.setAttribute("marker", "0");
			} else {
				userList = service
						.getUserList("join (select user_id from jc_online_user where user_id in (select from_id as id from jc_room_content where to_id = "
								+ userId
								+ " union select to_id as id from jc_room_content where from_id = "
								+ userId
								+ ") and user_id not in (select friend_id from user_friend where user_id = "
								+ userId
								+ ")) as temp on user_info.id=temp.user_id ORDER BY id limit "
								+ (Integer.parseInt(pageIndex) * perPageRecords + 3)
								+ "," + perPageRecords);
				request.setAttribute("marker", "1");
			}
			request.setAttribute("option", "strange");
		}
		*/
		
		
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
