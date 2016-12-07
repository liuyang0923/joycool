package jc.family;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

public class FamilyHomeBean {

	/**
	 * 是否开通餐厅
	 */
	public final static int EATERY = (1 << 0);
	public final static int OPEN_CHAT = (1 << 1);
	public final static int OPEN_FORUM = (1 << 2);

	private int id;
	private String fm_name;
	private int fm_member_num;
	private int fm_level;
	private Date fm_time;
	private int create_id;
	private int leader_id;
	private String leader_name;
	private Date leader_leave;
	private int invite_number;
	private int fm_exploit;
	private long money;
	private long limit_money;
	private String bulletin;
	private int game_num;
	private int forumId;
	private String logoUrl;

	private int uvSelf;
	private int uv;
	private int uvSelfYes;
	private int uvYes;

	String info;// 简介
	int allyCount;// 加了多少个
	int allyCount2;// 被加了多少次

	int flag;// 是否开通
	
	int maxMember=30;

	public int getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(int maxMember) {
		this.maxMember = maxMember;
	}
	
	public HashSet userVisitSet = new HashSet();// 用户 访问
	public HashSet ipVisitSet = new HashSet();// ip访问

	LinkedList trendList; // 家族动态
	List medalList;	// 奖牌列表
	List allyList;	// 友链家族的id列表
	
	// 保存在fm_set表里
	String shortName;	// 家族短名称，将来可能会显示在昵称左边
	int chatOpen;	// 0 不开放 1 开放给友联 2 全开放
	String chatTop;	// 聊天室置顶
	String chatTopWml;	// chatTop转换为wml
	int setting;// 族长设置

	public String getChatTopWml() {
		return chatTopWml;
	}

	public void setChatTopWml(String chatTopWml) {
		this.chatTopWml = chatTopWml;
	}

	public int getChatOpen() {
		return chatOpen;
	}

	public void setChatOpen(int chatOpen) {
		this.chatOpen = chatOpen;
	}

	public String getChatTop() {
		return chatTop;
	}

	public void setChatTop(String chatTop) {
		this.chatTop = chatTop;
		if(chatTop.length() == 0)
			chatTopWml = "";
		else
			chatTopWml = StringUtil.toWml(chatTop) + "<br/>";
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List getAllyList() {
		if(allyList == null) {
			allyList = SqlUtil.getIntList("select ally_id from fm_ally where fm_id=" + id, 5);
		}
		return allyList;
	}

	public void setAllyList(List allyList) {
		this.allyList = allyList;
	}
	
	public boolean isAlly(int id) {
		return getAllyList().contains(new Integer(id));
	}

	public List getMedalList() {
		if(medalList == null) {
			medalList = FamilyAction.service.getMedalList("fm_id=" + id + " order by seq");
		}
		return medalList;
	}

	public void setMedalList(List medalList) {
		this.medalList = medalList;
	}

	public LinkedList getTrendList() {
		if (trendList == null) {
			trendList = (LinkedList) FamilyAction.getTrendList(id);
		}
		return trendList;
	}

	public LinkedList getTrendListDirect() {
		return trendList;
	}

	public void setTrendList(LinkedList trendList) {
		this.trendList = trendList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFm_name() {
		return fm_name;
	}

	public String getFm_nameWml() {
		return StringUtil.toWml(fm_name);
	}

	public void setFm_name(String fmName) {
		fm_name = fmName;
	}

	public int getFm_member_num() {
		return fm_member_num;
	}

	public void setFm_member_num(int fmMemberNum) {
		fm_member_num = fmMemberNum;
	}

	public int getFm_level() {
		return fm_level;
	}

	public void setFm_level(int fmLevel) {
		fm_level = fmLevel;
	}

	public Date getFm_time() {
		return fm_time;
	}

	public void setFm_time(Date fmTime) {
		fm_time = fmTime;
	}

	public int getCreate_id() {
		return create_id;
	}

	public void setCreate_id(int createId) {
		create_id = createId;
	}

	public int getLeader_id() {
		return leader_id;
	}

	public void setLeader_id(int leaderId) {
		leader_id = leaderId;
	}

	public String getLeader_name() {
		return leader_name;
	}

	public String getLeaderNickName() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(leader_id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

	public String getLeaderNickNameWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(leader_id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	public void setLeader_name(String leaderName) {
		leader_name = leaderName;
	}

	public Date getLeader_leave() {
		return leader_leave;
	}

	public void setLeader_leave(Date leaderLeave) {
		leader_leave = leaderLeave;
	}

	public int getInvite_number() {
		return invite_number;
	}

	public void setInvite_number(int inviteNumber) {
		invite_number = inviteNumber;
	}

	public int getFm_exploit() {
		return fm_exploit;
	}

	public void setFm_exploit(int fmExploit) {
		fm_exploit = fmExploit;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public long getLimit_money() {
		return limit_money;
	}

	public void setLimit_money(long limitMoney) {
		limit_money = limitMoney;
	}

	public String getBulletin() {
		return bulletin;
	}

	public void setBulletin(String bulletin) {
		this.bulletin = bulletin;
	}

	public int getGame_num() {
		return game_num;
	}

	public void setGame_num(int gameNum) {
		game_num = gameNum;
	}

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoUrlForImg() {
		return Constants.FAMILY_IMG_PATH + logoUrl;
	}

	/**
	 * 是否可以上传logo
	 * 
	 * @return
	 */
	public boolean isUploadLogo() {
		if (game_num >= Constants.MIN_GAME_POINT_UPLOAD) {
			return true;
		}
		return false;
	}

	public int getUvSelf() {
		return uvSelf;
	}

	public void setUvSelf(int uvSelf) {
		this.uvSelf = uvSelf;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getUvSelfYes() {
		return uvSelfYes;
	}

	public void setUvSelfYes(int uvSelfYes) {
		this.uvSelfYes = uvSelfYes;
	}

	public int getUvYes() {
		return uvYes;
	}

	public void setUvYes(int uvYes) {
		this.uvYes = uvYes;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getAllyCount() {
		return allyCount;
	}

	public void setAllyCount(int allyCount) {
		this.allyCount = allyCount;
	}

	public int getAllyLevel() {
		if (fm_level > Constants.FM_LEVEL_ALLY.length) {
			return 10;
		}
		return Constants.FM_LEVEL_ALLY[fm_level];
	}

	public int getAllyCount2() {
		return allyCount2;
	}

	public void setAllyCount2(int allyCount2) {
		this.allyCount2 = allyCount2;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getSetting() {
		return setting;
	}

	public void setSetting(int setting) {
		this.setting = setting;
	}

	/**
	 * 是否开通餐厅
	 * 
	 * @return
	 */
	public boolean isEatery() {
		return 0 != (flag & EATERY);
	}
	public boolean isFlagChat() {
		return 0 != (flag & OPEN_CHAT);
	}
	public boolean isFlagForum() {
		return 0 != (flag & OPEN_FORUM);
	}
	public void toggleFlag(int is) {
		flag ^= (1 << is);
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
}