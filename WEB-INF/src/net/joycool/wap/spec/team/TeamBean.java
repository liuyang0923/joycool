package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 圈子
 *
 */
public class TeamBean {
	static int FLAG_OUT_BROWSE = (1 << 0);			// 允许外人浏览
	static int FLAG_OUT_CHAT = (1 << 1);			// 允许外人发言
	static int FLAG_FORBID = (1 << 2);				// 群被禁止
	
	static TeamService service = TeamAction.service;
	
	public static int MAX_CHAT_COUNT = 200;
	int id;
	int count;		// 群内人员数量
	String name;	// 群名称
	String info;	// 群介绍
	int flag;
	int duty0;		// 群主id
	
	int class1;		// 所属分类
	int class2;
	
	int lastChatId;		// 最后一条聊天记录的id，用于辨认是否已读
	
	long createTime;		// 创建时间
	
	HashMap userMap = null;	// 所有成员
	
	LinkedList chatList = null;		// 玩家聊天
	
	int chatTotal;		// 总发过的聊天记录数量
	
	public HashSet inviteSet = new HashSet();
	public HashSet applySet = new HashSet();
	public HashSet getApplySet() {
		return applySet;
	}
	public HashSet getInviteSet() {
		return inviteSet;
	}
	public List getApplys() {
		return new ArrayList(applySet);
	}
	public void addInvite(int id) {
		synchronized(inviteSet) {
			inviteSet.add(Integer.valueOf(id));
		}
	}
	public boolean removeInvite(int id) {
		synchronized(inviteSet) {
			return inviteSet.remove(Integer.valueOf(id));
		}
	}
	public boolean isInvite(int id) {
		return inviteSet.contains(Integer.valueOf(id));
	}
	public void addApply(int id) {
		synchronized(applySet) {
			applySet.add(Integer.valueOf(id));
		}
	}
	public boolean removeApply(int id) {
		synchronized(applySet) {
			return applySet.remove(Integer.valueOf(id));
		}
	}
	public boolean isApply(int id) {
		return applySet.contains(Integer.valueOf(id));
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void removeUser(int id) {
		userMap.remove(Integer.valueOf(id));
	}
	public boolean isUser(int id) {
		return userMap.containsKey(Integer.valueOf(id));
	}
	
	public TeamUserBean getUser(int id) {
		return (TeamUserBean)userMap.get(Integer.valueOf(id));
	}

	public void deleteUser(int userId) {
		userMap.remove(Integer.valueOf(userId));
		if(count > 0) {
			count--;
			service.updateTeamCount(this);
		}
	}

	public void addUser(TeamUserBean user) {
		userMap.put(Integer.valueOf(user.getUserId()), user);
		count++;
		service.updateTeamCount(this);
	}
	
	public void addChat(int userId, String name, String str, boolean isAct) {
		TeamChatBean chat = new TeamChatBean();
		chat.setFromId(userId);
		chat.setContent(str);
		chat.setFromName(name);
		chat.setTime(System.currentTimeMillis());
		chat.setTeamId(id);
		chat.setFlagAct(isAct);
		addChat(chat);
	}
	
	public void addChat(TeamChatBean chat) {
		getChatList();
		chatTotal++;
		synchronized(chatList) {
			chatList.addFirst(chat);
			if(chatList.size() > MAX_CHAT_COUNT)
				chatList.removeLast();
			service.addChat(chat);
		}
		lastChatId = chat.getId();
	}
	
	public int getChatCount() {
		return chatList.size();
	}
	
	public String getChatString(int start, int limit) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = chatList.iterator();
		while(iter.hasNext()) {
			TeamChatBean chat = (TeamChatBean)iter.next();
			if(start < 0) {
				sb.append(StringUtil.toWml(chat.getFromName()));
				sb.append(':');
				sb.append(chat.getContent());
				sb.append('(');
				sb.append(DateUtil.formatTime(chat.getTime()));
				sb.append(')');
				sb.append("<br/>");
				limit--;
				if(limit < 0)
					break;
			} else {
				start--;
			}
		}
		return sb.toString();
	}

	public LinkedList getChatList() {
		if(chatList == null) {
			synchronized(this) {
				if(chatList == null) {
					List ret = service.getChatList("team_id=" + id + " order by id desc limit " + MAX_CHAT_COUNT);
					chatList = new LinkedList(ret);
				}
			}
		}
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
	public List getUserList() {
		return new ArrayList(getUserMap().values());
	}
	public HashMap getUserMap() {
		if(userMap == null) {
			synchronized(this) {
				if(userMap == null) {
					List ret = service.getUserList("team_id=" + id + " order by id desc");
					userMap = new LinkedHashMap();
					for(int i = 0;i < ret.size();i++) {
						TeamUserBean tu = (TeamUserBean)ret.get(i);
						userMap.put(new Integer(tu.getUserId()), tu);
					}
				}
			}
		}
		return userMap;
	}

	public void setUserMap(HashMap userMap) {
		this.userMap = userMap;
	}

	public boolean join(int userId) {
		Integer iid = Integer.valueOf(userId);
		if(!inviteSet.contains(iid))
			return false;
		if(userMap.get(iid) == null) {
			TeamUserBean user = new TeamUserBean();
			user.setUserId(userId);
			userMap.put(iid, user);
		}
		return true;
		
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagOutBrowse() {
		return (flag & FLAG_OUT_BROWSE) != 0;
	}
	public boolean isFlagOutChat() {
		return (flag & FLAG_OUT_CHAT) != 0;
	}
	public void toggleFlag(int flag) {
		this.flag ^= (1 << flag);
	}

	public int getDuty0() {
		return duty0;
	}

	public void setDuty0(int duty0) {
		this.duty0 = duty0;
	}

	public int getClass1() {
		return class1;
	}

	public void setClass1(int class1) {
		this.class1 = class1;
	}

	public int getClass2() {
		return class2;
	}

	public void setClass2(int class2) {
		this.class2 = class2;
	}
	public int getLastChatId() {
		return lastChatId;
	}
	public void setLastChatId(int lastChatId) {
		this.lastChatId = lastChatId;
	}
	// 是否有未读
	public boolean isNewChat(int userId) {
		TeamUserBean tu = getUser(userId);
		if(tu == null)
			return false;
		return tu.getReadId() < lastChatId;
	}

}
