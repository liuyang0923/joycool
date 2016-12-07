package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 临时群，用于临时的聊天等等
 *
 */
public class TeamAction extends CustomAction{
	public static ICacheMap stuffCache = CacheManage.stuff;
	public static HashSet newChatUser = new HashSet();		// 有新圈子信息的玩家userid
	
	public static int COUNT_PER_PAGE = 10;
	static HashMap teamMap = new HashMap();
	UserBean loginUser;
	public static TeamService service = new TeamService();

	public static List getTeamList() {
		return new ArrayList(teamMap.values());
	}
	
	public TeamAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	public static void addTeam(TeamBean team) {
		service.addTeam(team);
		teamMap.put(Integer.valueOf(team.getId()), team);
		team.getUserMap();
	}
	
	public static void removeTeam(int id) {
		teamMap.remove(Integer.valueOf(id));
	}
	
	public static TeamBean getTeam(int id) {
		Integer key = Integer.valueOf(id);
		TeamBean team = (TeamBean)teamMap.get(key);
		if(team == null) {
			synchronized(teamMap) {
				team = (TeamBean)teamMap.get(key);
				if(team == null) {
					team = service.getTeam("id=" + id);
					if(team != null) {
						team.getUserMap();
						LinkedList chatList = team.getChatList();
						if(chatList.size() > 0)
							team.setLastChatId(((TeamChatBean)chatList.getLast()).getId());
						teamMap.put(key, team);
					}
				}
				return team;
			}
		}
		return team;
	}
	// 添加用户
	public static TeamUserBean addUser(int userId, int teamId, int duty) {
		TeamBean team = getTeam(teamId);
		if(team != null) {
			return addUser(userId, team, duty);
		}
		return null;
	}
	public static TeamUserBean addUser(int userId, TeamBean team, int duty) {
		TeamUserBean user = team.getUser(userId);
		if(user == null) {
			user = new TeamUserBean();
			user.setUserId(userId);
			user.setTeamId(team.getId());
			user.setDuty(duty);
			user.setCreateTime(System.currentTimeMillis());
			user.setName("");
			if(service.addUser(user)) {
				team.addUser(user);
				if(team.getDuty0() != userId)	// 自己加入圈子不用记录
					ActionTrend.addTrend(userId, BeanTrend.TYPE_WGAME, "%1加入了圈子%3", UserInfoUtil.getUser(userId).getNickName(), team.getName(), "/team/info.jsp?ti=" + team.getId());
				return user;
			}
		}
		return null;
	}
	// 踢出用户
	public static void kickUser(int userId, int teamId) {
		TeamBean team = getTeam(teamId);
		if(team != null) {
			deleteUser(userId, team);
		}
	}
	public static void deleteUser(int userId, TeamBean team) {
		TeamUserBean user = team.getUser(userId);
		if(user != null) {
			team.deleteUser(userId);
			SqlUtil.executeUpdate("delete from team_user where user_id=" + userId + " and team_id=" + team.getId(), 3);
		}
	}
	
	// 邀请
	public void invite() {
		TeamBean team = getCurrentTeam();
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		request.setAttribute("team", team);
		int userId = getParameterInt("id");
		if(hasParam("d")) {		// 拒绝
			team.removeApply(userId);
			tip("tip", "已拒绝");
			return;
		}
		if(team.isUser(userId)) {
			tip("tip", "已是圈子成员");
			return;
		}
		TeamUserBean tu = team.getUser(loginUser.getId());
		if(tu == null || tu.getDuty() < 10) {
			tip("tip", "不是圈子主人，无法邀请");
			return;
		}
		if(team.getCount() >= 100) {
			tip("tip", "圈子人数已经达到上限:100人");
			return;
		}
		if(team.removeApply(userId)) {
			addUser(userId, team.getId(), 0);
			tip("tip", "成功邀请加入");
		} else {
			team.addInvite(userId);
			if(isCooldown("inT" + userId, 60000l)) {
				NoticeAction.sendNotice(userId, loginUser.getNickNameWml() + "邀请你加入圈子聊天", NoticeBean.GENERAL_NOTICE, "/team/info.jsp?ti=" + team.getId());
			}
			tip("tip", "成功邀请");
		}
	}
	// 邀请好友
	public void invites() {
		TeamBean team = getCurrentTeam();
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		request.setAttribute("team", team);
	}
//	 提交申请
	public void apply() {
		int teamId = getParameterInt("g");
		TeamBean team = getTeam(teamId);
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		int userId = loginUser.getId();
		
		request.setAttribute("team", team);
		if(team.isUser(userId)) {
			tip("tip", "已加入圈子");
			return;
		}
		if(team.removeInvite(userId)) {
			tip("tip", "成功加入圈子");
			addUser(userId, team.getId(), 0);
			session.setAttribute("team_id", Integer.valueOf(teamId));
		} else {
			tip("tip", "已提交申请");
			team.addApply(userId);
		}
	}
	// 踢出圈子
	public void kick() {
		TeamBean team = getCurrentTeam();
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		int userId = getParameterInt("id");
		TeamUserBean tu = team.getUser(loginUser.getId());
		int userDuty = -1;
		if(tu != null)
			userDuty = tu.getDuty();
		if(userDuty != 10 || loginUser.getId() == userId) {
			tip("tip", "无法执行操作");
			return;
		}
		
		tu = team.getUser(userId);
		if(tu == null || tu.getDuty() >= userDuty) {
			tip("tip", "不是成员");
			return;
		}
		deleteUser(userId, team);
	}
	// 离开当前群
	public void leave() {
		TeamBean team = getCurrentTeam();
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		TeamUserBean tu = team.getUser(loginUser.getId());
		if(tu == null || tu.getDuty() == 10) {
			tip("tip", "无法离开");
			return;
		}
		deleteUser(loginUser.getId(), team);
		tip("tip", "离开了");
	}
	// 获取正在查看的群
	public TeamBean getCurrentTeam() {
		TeamBean team = null;
		int teamId = getParameterInt("ti");
		if(teamId == 0) {
			Integer tid = (Integer)session.getAttribute("team_id");
			if(tid != null) {
				teamId = tid.intValue();
				team = getTeam(teamId);
			}
		} else {
			team = getTeam(teamId);
			if(team != null)
				session.setAttribute("team_id", Integer.valueOf(teamId));
		}
		return team;
	}
	// 查看聊天
	public void chat() {
		TeamBean team = getCurrentTeam();
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		TeamUserBean tu = team.getUser(loginUser.getId());
		if(!team.isFlagOutBrowse() && tu == null) {
			tip("tip", "非成员无法浏览");
			return;
		}
		request.setAttribute("team", team);
		if(hasParam("content") || hasParam("act")) {
			if(!team.isFlagOutChat() && tu == null) {
				tip("tip", "非成员不能发言");
				return;
			}
			if(!isCooldown("teamchat", 5000)) {
				tip("tip", "你的发言太快了！请先休息一会再继续。");
				return;
			}
			int act = getParameterInt("act");	// 发动作
			String content = getParameterNoEnter("content");
			int toId = getParameterInt("tid");
			UserBean to = null;
			boolean isAct = false;
			if(toId > 0 && team.isUser(toId))
				to = UserInfoUtil.getUser(toId);
			if(act > 0) {
				content = (to == null ? getAct(act) : getAct2(act));
				if(content == null) { 
					tip("tip", "不存在的动作");
					return;
				}
				isAct = true;

			} else {
				if(content.length() < 1 || content.length() > 100) {
					tip("tip", "发言字数要求在100以内且至少1个字");
					return;
				}
				String content2 = (to == null ? getAct(content) : getAct2(content));
				if(content2 != null) {
					content = content2;
					isAct = true;
				} else if(to != null){
					content = to.getNickName() + "," + content;
				}
			}
			if(isAct) {
				content = content.replace("self", loginUser.getNickName());
				if(to != null)
					content = content.replace("target", to.getNickName());
			} else {
				request.setAttribute("setvar", "");
			}
			team.addChat(loginUser.getId(), loginUser.getNickName(), content, isAct);
			// 圈子内有新人发言
			synchronized(newChatUser) {
				Iterator iter = team.getUserMap().values().iterator();
				while(iter.hasNext()) {
					TeamUserBean tu2 = (TeamUserBean)iter.next();
					if(tu2.getUserId() != loginUser.getId() && !tu2.isFlagNoNotice())
						newChatUser.add(new Integer(tu2.getUserId()));
				}
			}
		}
		if(tu != null && tu.getReadId() != team.getLastChatId()) {	// 设置为已读
			tu.setReadId(team.getLastChatId());
			SqlUtil.executeUpdate("update LOW_PRIORITY team_user set read_id=" + team.getLastChatId() + " where id=" + tu.getId(), 3);
		}
	}
	public static boolean isNewChatUser(int userId) {
		return newChatUser.contains(new Integer(userId));
	}
	public static void removeNewChatUser(int userId) {
		synchronized(newChatUser) {
			newChatUser.remove(new Integer(userId));
		}
	}
	public void removeNewChatUser() {
		removeNewChatUser(loginUser.getId());
	}
	
	public void index() {
		List list = SqlUtil.getIntList("select team_id from team_user where user_id=" + loginUser.getId(), 3);
		if(list == null || list.size() == 0) {
			tip("tip", "没有加入任何圈子");
			return;
		}
		request.setAttribute("teamList", list);
	}
	public void all() {
		List list = SqlUtil.getIntList("select id from team", 3);
		if(list == null || list.size() == 0) {
			tip("tip", "没有查询到圈子");
			return;
		}
		request.setAttribute("teamList", list);
	}
	public void info() {
		TeamBean team = getCurrentTeam();
		
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		request.setAttribute("team", team);
	}
	public void member() {
		TeamBean team = getCurrentTeam();
		
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		request.setAttribute("team", team);
	}
	// 查看申请书
	public void applys() {
		TeamBean team = getCurrentTeam();
		
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		request.setAttribute("team", team);
	}
	// 创建圈子
	public void create() {
		UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
		if(us.getOnlineTime() < 2000) {
			tip("tip", "还无法创建圈子，再等一段时间吧");
			return;
		}
		int count = getUserTeamCount();
		// 2000小时在线可以创建一个圈子，4000以上两个
		if(count >= 2 || us.getOnlineTime() < 4000 && count >= 1) {
			tip("tip", "无法创建更多的圈子");
			return;
		}
		if(isMethodGet()) {
			
		} else {
			String name = getParameterNoEnter("name");
			if(name.length() < 2 || name.length() > 6 || !StringUtil.isChinese(name)) {
				tip("tip", "圈子名必须是2-6字的中文");
				return;
			}
			String info = getParameterNoEnter("info");
			if(info.length() > 100) {
				tip("tip", "圈子介绍不能超过100字");
				return;
			}
			int flag = getParameterFlag("flag");
			TeamBean team = new TeamBean();
			team.setDuty0(loginUser.getId());
			team.setFlag(flag);
			team.setName(name);
			team.setCreateTime(System.currentTimeMillis());
			team.setInfo(info);
			addTeam(team);
			ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_CREATE, "%1创建了圈子%3", loginUser.getNickName(), team.getName(), "/team/info.jsp?ti="+team.getId());
			addUser(loginUser.getId(), team, 10);
			session.setAttribute("team_id", Integer.valueOf(team.getId()));
			tip("redirect");
		}
	}
	// 修改圈子设定
	public void set() {
		TeamBean team = getCurrentTeam();
		
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		TeamUserBean tu = team.getUser(loginUser.getId());
		if(tu == null || tu.getDuty() != 10) {
			tip("tip", "不是圈子主人");
			return;
		}
		request.setAttribute("team", team);
		int flag = getParameterIntS("flag");
		if(flag >= 0) {
			team.toggleFlag(flag);
			SqlUtil.executeUpdate("update team set flag=" + team.getFlag() + " where id=" + team.getId(), 3);
		}
		
		String name = getParameterNoEnter("name");
		if(name != null) {
			if(name.length() < 2 || name.length() > 6 || !StringUtil.isChinese(name)) {
				tip("tip", "圈子名必须是2-6字的中文");
				return;
			}
			team.setName(name);
			SqlUtil.executeUpdate("update team set name='" + StringUtil.toSql(name) + "' where id=" + team.getId(), 3);
		}
		String info = getParameterNoEnter("info");
		if(info != null) {
			if(info.length() > 100) {
				tip("tip", "圈子介绍不能超过100字");
				return;
			}
			team.setInfo(info);
			SqlUtil.executeUpdate("update team set info='" + StringUtil.toSql(info) + "' where id=" + team.getId(), 3);
		}
		
		int class1 = getParameterIntS("class1");
		if(class1 > 0 && class1 <= 10) {
			team.setClass1(class1);
			SqlUtil.executeUpdate("update team set class1=" + class1 + " where id=" + team.getId(), 3);
		}
	}
	
//	 修改自己的圈子消息设定
	public void set2() {
		TeamBean team = getCurrentTeam();
		
		if(team == null) {
			tip("tip", "圈子不存在");
			return;
		}
		TeamUserBean tu = team.getUser(loginUser.getId());
		if(tu == null) {
			tip("tip", "不是圈子成员");
			return;
		}
		request.setAttribute("team", team);
		request.setAttribute("tu", tu);
		int flag = getParameterIntS("flag");
		if(flag >= 0) {
			tu.toggleFlag(flag);
			SqlUtil.executeUpdate("update team_user set flag=" + tu.getFlag() + " where id=" + tu.getId(), 3);
		}
	}
	// 返回玩家创建的圈子总数
	public int getUserTeamCount() {
		return SqlUtil.getIntResult("select count(*) from team_user where user_id=" + loginUser.getId() + " and duty=10", 3);
	}
	// 返回玩家加入的圈子总数
	public int getUserJoinTeamCount() {
		return SqlUtil.getIntResult("select count(*) from team_user where user_id=" + loginUser.getId(), 3);
	}

	public UserBean getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(UserBean loginUser) {
		this.loginUser = loginUser;
	}
	// 关于动作的数据载入( from team_act )
	public static HashMap getActMap() {
		HashMap actMap = (HashMap)stuffCache.get("actMap");
		if(actMap == null) {
			synchronized(stuffCache) {
				actMap = (HashMap)stuffCache.get("actMap");
				if(actMap == null) {
					actMap = new HashMap();
					List actList = service.getActList("1");
					for(int i = 0;i < actList.size();i++) {
						TeamActBean act = (TeamActBean)actList.get(i);
						actMap.put(act.getName(), act);
						actMap.put(act.getName2(), act);
						actMap.put(Integer.valueOf(act.getId()), act);
					}
					stuffCache.put("actMap", actMap);
					stuffCache.put("actList", actList);
				}
			}
		}
		return actMap;
	}
	public static List getActList() {
		getActMap();
		return (List)stuffCache.sgt("actList");
	}
	public static String getAct(int id) {
		TeamActBean act = (TeamActBean)getActMap().get(Integer.valueOf(id));
		if(act == null)
			return null;
		return act.getContent();
	}
	public static String getAct2(int id) {
		TeamActBean act = (TeamActBean)getActMap().get(Integer.valueOf(id));
		if(act == null)
			return null;
		return act.getContent2();
	}
	public static String getAct(String c) {
		TeamActBean act = (TeamActBean)getActMap().get(c);
		if(act == null)
			return null;
		return act.getContent();
	}
	public static String getAct2(String c) {
		TeamActBean act = (TeamActBean)getActMap().get(c);
		if(act == null)
			return null;
		return act.getContent2();
	}
	// 关于圈子分类信息的载入( from team_class)
	public static HashMap getTeamClassMap() {
		HashMap actMap = (HashMap)stuffCache.get("teamClassMap");
		if(actMap == null) {
			synchronized(stuffCache) {
				actMap = (HashMap)stuffCache.get("teamClassMap");
				if(actMap == null) {
					actMap = new HashMap();
					List actList = service.getTeamClassList("1 order by id");
					for(int i = 0;i < actList.size();i++) {
						TeamClassBean bean = (TeamClassBean)actList.get(i);
						actMap.put(Integer.valueOf(bean.getId()), bean);
					}
					stuffCache.put("teamClassMap", actMap);
					stuffCache.put("teamClassList", actList);
				}
			}
		}
		return actMap;
	}
	public static List getTeamClassList() {
		getTeamClassMap();
		return (List)stuffCache.sgt("teamClassList");
	}
	public static String getTeamClassName(int class1) {
		if(class1 == 0)
			return "(无)";
		TeamClassBean tc = (TeamClassBean)getTeamClassMap().get(Integer.valueOf(class1));
		if(tc == null)
			return "(无)";
		return tc.getName();
	}
	
	public String getChatString(List chatList, int start, int limit, HttpServletResponse response) {
		int userId = loginUser.getId();
		if(start >= chatList.size())
			return "";
		StringBuilder sb = new StringBuilder();
		ListIterator iter = chatList.listIterator(start);
		while(iter.hasNext()) {
			TeamChatBean chat = (TeamChatBean)iter.next();
			if(!chat.isFlagAct()) {
				if(chat.getFromId() != userId && chat.getFromId() != 0) {		// 0 表示加入等信息
					sb.append("<a href=\"");
					sb.append(("chat.jsp?to=" + chat.getFromId()));
					sb.append("\">");
					sb.append(StringUtil.toWml(chat.getFromName()));
					sb.append("</a>");
				} else {
					sb.append(StringUtil.toWml(chat.getFromName()));
				}
				sb.append(':');
			}
			sb.append(StringUtil.toWml(chat.getContent()));
			sb.append('(');
			sb.append(DateUtil.formatTime(chat.getTime()));
			sb.append(')');
			sb.append("<br/>");
			if(limit <= 1)
				break;
			limit--;
		}
		return sb.toString();
	}
}
