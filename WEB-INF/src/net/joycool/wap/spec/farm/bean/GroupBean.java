package net.joycool.wap.spec.farm.bean;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.farm.bean.GroupUserBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 组队的队伍信息
 *
 */
public class GroupBean {
	public static int MAX_GROUP_PLAYER = 5;
	public static int MAX_CHAT_COUNT = 500;
	int id;		// 一般是组发起人id
	
	HashMap userMap = new HashMap();	// 所有成员
	HashSet allowUserId = new HashSet();		// 所有允许加入的成员，邀请过自动写入
	
	LinkedList chatList = new LinkedList();		// 玩家聊天
	
	int chatTotal;		// 总发过的聊天记录数量

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void removeUser(int id) {
		userMap.remove(Integer.valueOf(id));
	}
	
	public GroupUserBean getUser(int id) {
		return (GroupUserBean)userMap.get(Integer.valueOf(id));
	}

	public void addUser(GroupUserBean user) {
		userMap.put(Integer.valueOf(user.getUserId()), user);
	}
	public void addUser(int userId) {
		GroupUserBean user = getUser(userId);
		if(user == null) {
			user = new GroupUserBean();
			user.setUserId(userId);
			addUser(user);
		}
	}
	
	public void addChat(String str) {
		chatTotal++;
		synchronized(chatList) {
			chatList.addFirst(str);
			if(chatList.size() > MAX_CHAT_COUNT)
				chatList.removeLast();
		}
	}
	
	public int getChatCount() {
		return chatList.size();
	}
	
	// 返回有个用户没有读过的条数
	public int getUnreadTotal(int userId) {
		GroupUserBean user = getUser(userId);
		if(user != null) {
			int c = chatTotal - user.getReadTotal();
			if(c > MAX_CHAT_COUNT)
				return MAX_CHAT_COUNT;
			return c;
		}
		return 0;
	}
	
	public String getChatString(int start, int limit) {
		StringBuilder sb = new StringBuilder(128);
		Iterator iter = chatList.iterator();
		while(iter.hasNext()) {
			String chat = (String)iter.next();
			if(start < 0) {
				sb.append(chat);
				sb.append("<br/>");
				limit--;
				if(limit <= 0)
					break;
			} else {
				start--;
			}
		}
		return sb.toString();
	}

	public LinkedList getChatList() {
		return chatList;
	}

	public void setChatList(LinkedList chatList) {
		this.chatList = chatList;
	}

	public int getChatTotal() {
		return chatTotal;
	}

	public void setChatTotal(int chatTotal) {
		this.chatTotal = chatTotal;
	}

	public HashMap getUserMap() {
		return userMap;
	}

	public void setUserMap(HashMap userMap) {
		this.userMap = userMap;
	}

	public int getUserCount() {
		return userMap.size();
	}
	
	public boolean join(int userId) {
		Integer iid = Integer.valueOf(userId);
		if(!allowUserId.contains(iid))
			return false;
		if(userMap.get(iid) == null) {
			GroupUserBean user = new GroupUserBean();
			user.setUserId(userId);
			userMap.put(iid, user);
		}
		return true;
		
	}

	public void invite(int userId) {
		allowUserId.add(Integer.valueOf(userId));
	}

	public boolean isFull() {
		return userMap.size() >= MAX_GROUP_PLAYER;
	}
}
