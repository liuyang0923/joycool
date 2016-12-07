package jc.family;

import java.util.Date;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.OnlineUtil;
import net.joycool.wap.util.UserInfoUtil;

public class FamilyUserBean {

	/**
	 * 招募新人
	 */
	public final static int FLAG_NEW_MEMBER = (1 << 0);

	/**
	 * 踢除成员
	 */
	public final static int FLAG_REMOVE_MEMBER = (1 << 1);

	/**
	 * 家族公告
	 */
	public final static int FLAG_BULLETIN = (1 << 2);

	/**
	 * 家族通知
	 */
	public final static int FLAG_NOTICE = (1 << 3);

	/**
	 * 论坛管理
	 */
	public final static int FLAG_BBS = (1 << 4);

	/**
	 * 聊天管理
	 */
	public final static int FLAG_CHAT = (1 << 5);

	/**
	 * 游戏管理
	 */
	public final static int FLAG_GAME = (1 << 6);

	/**
	 * 基金管理
	 */
	public final static int FLAG_MONEY = (1 << 7);

	/**
	 * 族长权限
	 */
	public final static int FLAG_LEADER = (1 << 8);

	/**
	 * 餐厅权限
	 */
	public final static int FLAG_YARD = (1 << 9);
	// 相册管理
	public final static int FLAG_PHOTO = (1 << 10);
	
	public final static int FLAG_APPOINT = (1 << 11);	// 任命权限
	
	public final static int FLAG_PUBLIC = (1 << 12);	// 对外宣传，包括资料、友联、家族徽记

	/**
	 * 问答被禁
	 */
	public final static int BLOCKED_ASK = (1 << 0);

	/**
	 * 龙舟被禁
	 */
	public final static int BLOCKED_BOAT = (1 << 1);

	/**
	 * 雪仗被禁
	 */
	public final static int BLOCKED_SNOW = (1 << 2);

	/**
	 * 家族对战
	 */
	public final static int VS_GAME = (1 << 3);

	private int id;
	private int fm_id;
	private long gift_fm;
	private int con_fm;
	private String fm_name;
	private Date leave_fm_time;
	private Date create_time;
	private long fm_money_used;
	private int fm_state;
	private int fm_flags;
	
	
	public final static int SETTING_CHAT_LINK = (1 << 0);	// 聊天室里带发言人链接
	int setting;	// 家人自己的设置
	
	int alive;	// 活跃度
	int honor;	// 贡献度?族长分配的
	Date visitTime;	// 最后一次访问时间

	public FamilyUserBean() {

	}

	public FamilyUserBean(int id) {
		this.id = id;
	}

	public FamilyUserBean(int id, int allflages) {
		this.id = id;
		this.fm_flags = allflages;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFm_id() {
		return fm_id;
	}
	public int getFmId() {
		return fm_id;
	}

	public String getFamilyName() {
		if (fm_id == 0) {
			return "";
		}
		FamilyHomeBean fm = FamilyAction.getFmByID(fm_id);
		if (fm == null) {
			return "";
		}
		return fm.getFm_nameWml();
	}

	public void setFm_id(int fmId) {
		fm_id = fmId;
	}

	public long getGift_fm() {
		return gift_fm;
	}

	public void setGift_fm(long giftFm) {
		gift_fm = giftFm;
	}

	public int getCon_fm() {
		return con_fm;
	}

	public void setCon_fm(int conFm) {
		con_fm = conFm;
	}

	public String getFm_name() {
		return fm_name;
	}

	public void setFm_name(String fmName) {
		fm_name = fmName;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getLeave_fm_time() {
		return leave_fm_time;
	}

	public void setLeave_fm_time(Date leaveFmTime) {
		leave_fm_time = leaveFmTime;
	}

	public long getFm_money_used() {
		return fm_money_used;
	}

	public void setFm_money_used(long fmMoneyUsed) {
		fm_money_used = fmMoneyUsed;
	}

	public int getFm_state() {
		return fm_state;
	}

	public void setFm_state(int fmState) {
		fm_state = fmState;
	}

	public int getFm_flags() {
		return fm_flags;
	}

	public String getFmFlags() {
		if (fm_flags == 0) {
			return "无";
		}
		StringBuilder sbu = new StringBuilder(64);
		if (isflagLeader()) {
			sbu.append("(族长);");
		} 
		{
			if (isflagAppoint()) {
				sbu.append("权限助理");
				sbu.append(";");
			}
			if (isflagPublic()) {
				sbu.append("宣传部长");
				sbu.append(";");
			}
			if (isflag_new_member(fm_flags)) {
				sbu.append("招募新人");
				sbu.append(";");
			}
			if (isflag_remove_memberint(fm_flags)) {
				sbu.append("踢除成员");
				sbu.append(";");
			}
			if (isbulletin(fm_flags)) {
				sbu.append("家族公告");
				sbu.append(";");
			}
			if (isflag_notice(fm_flags)) {
				sbu.append("家族通知");
				sbu.append(";");
			}
			if (isflag_bbs(fm_flags)) {
				sbu.append("论坛管理");
				sbu.append(";");
			}
			if (isflag_chat(fm_flags)) {
				sbu.append("聊天管理");
				sbu.append(";");
			}
			if (isflag_game(fm_flags)) {
				sbu.append("游戏管理");
				sbu.append(";");
			}
			if (isflag_money(fm_flags)) {
				sbu.append("基金管理");
				sbu.append(";");
			}
			if (isflagYard()) {
				sbu.append("餐厅大厨");
				sbu.append(";");
			}
			if (isflagPhoto()) {
				sbu.append("相册管理");
				sbu.append(";");
			}
			if(sbu.length() > 0)
				sbu.setLength(sbu.length() - 1);
		}
		return sbu.toString();
	}

	/**
	 * 是否有权利招募新人
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isflagNewMember() {
		return (fm_flags & FLAG_NEW_MEMBER) != 0;
	}

	/**
	 * 是否有权利踢除成员
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isflagRemoveMemberint() {
		return (fm_flags & FLAG_REMOVE_MEMBER) != 0;
	}

	/**
	 * 是否有权利家族公告
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isBulletin() {
		return (fm_flags & FLAG_BULLETIN) != 0;
	}

	/**
	 * 是否有权利家族通知
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isflagNotice() {
		return (fm_flags & FLAG_NOTICE) != 0;
	}

	/**
	 * 是否有权利论坛管理
	 * 
	 * @param fm_flags
	 * @return
	 */
	public boolean isflagBbs() {
		return (fm_flags & FLAG_BBS) != 0;
	}

	/**
	 * 是否有权利聊天管理
	 * 
	 * @param fm_flags
	 * @return
	 */
	public boolean isflagChat() {
		return (fm_flags & FLAG_CHAT) != 0;
	}

	/**
	 * 是否有权利游戏管理
	 * 
	 * @param fm_flags
	 * @return
	 */
	public boolean isflagGame() {
		return (fm_flags & FLAG_GAME) != 0;
	}

	/**
	 * 是否有权利基金管理
	 * 
	 * @param fm_flags
	 * @return
	 */
	public boolean isflagMoney() {
		return (fm_flags & FLAG_MONEY) != 0;
	}

	public void setFm_flags(int fmFlags) {
		fm_flags = fmFlags;
	}
	public void toggleFlag(int is) {
		fm_flags ^= (1 << is);
	}
	public boolean isFlag(int is) {
		return (fm_flags & (1 << is)) != 0;
	}

	public String getNickName() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickName();
	}

	public String getNickNameWml() {
		UserBean userBean = (UserBean) UserInfoUtil.getUser(id);
		if (userBean == null) {
			return "";
		}
		return userBean.getNickNameWml();
	}

	/**
	 * 判断离开时间是否足够N天，如果足够则返回MIN_LEAVE_DATE，不足则返回不足的天数
	 * 
	 * @param date
	 * @return
	 */
	public int isLeaveDate() {
		if (leave_fm_time == null) {
			return Constants.MIN_LEAVE_DATE;
		}
		Date now = new Date();
		long leave_date = (now.getTime() - leave_fm_time.getTime()) / 1000 / 60 / 60 / 24;
		if (leave_date > Constants.MIN_LEAVE_DATE) {
			return Constants.MIN_LEAVE_DATE;
		}
		return (int) leave_date;
	}

	/**
	 * 判断加入时间是否足够N天，如果足够则返回MIN_CREATE_DATE，不足则返回不足的天数
	 * 
	 * @param date
	 * @return
	 */
	public int isCreateDate() {
		if (leave_fm_time == null) {
			return Constants.MIN_CREATE_DATE;
		}
		Date now = new Date();
		long leave_date = (now.getTime() - create_time.getTime()) / 1000 / 60 / 60 / 24;
		if (leave_date > Constants.MIN_CREATE_DATE) {
			return Constants.MIN_CREATE_DATE;
		}
		return (int) leave_date;
	}

	/**
	 * 是否有权利招募新人
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_new_member(int fmFlags) {
		return (fmFlags & FLAG_NEW_MEMBER) != 0;
	}

	/**
	 * 是否有权利踢除成员
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_remove_memberint(int fmFlags) {
		return (fmFlags & FLAG_REMOVE_MEMBER) != 0;
	}

	/**
	 * 是否有权利家族公告
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isbulletin(int fmFlags) {
		return (fmFlags & FLAG_BULLETIN) != 0;
	}

	/**
	 * 是否有权利家族通知
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_notice(int fmFlags) {
		return (fmFlags & FLAG_NOTICE) != 0;
	}

	/**
	 * 是否有权利论坛管理
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_bbs(int fmFlags) {
		return (fmFlags & FLAG_BBS) != 0;
	}

	/**
	 * 是否有权利聊天管理
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_chat(int fmFlags) {
		return (fmFlags & FLAG_CHAT) != 0;
	}

	/**
	 * 是否有权利游戏管理
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_game(int fmFlags) {
		return (fmFlags & FLAG_GAME) != 0;
	}

	/**
	 * 是否有权利基金管理
	 * 
	 * @param fmFlags
	 * @return
	 */
	public static boolean isflag_money(int fmFlags) {
		return (fmFlags & FLAG_MONEY) != 0;
	}

	/**
	 * 是否有餐厅管理
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isflagYard() {
		return (fm_flags & FLAG_YARD) != 0;
	}
	
	// 是否相册管理
	public boolean isflagPhoto() {
		return (fm_flags & FLAG_PHOTO) != 0;
	}
	// 是否有任命权，如果是族长，自动拥有这个权限
	public boolean isflagAppoint() {
		return isflagLeader() || (fm_flags & FLAG_APPOINT) != 0;
	}
	public boolean isflagPublic() {
		return (fm_flags & FLAG_PUBLIC) != 0;
	}
	// 是否族长
	public boolean isflagLeader() {
		return (fm_flags & FLAG_LEADER) != 0;
	}
	/**
	 * 返回族长权限, 也就是全部权限
	 * 
	 * @return
	 */
	public static int allflages() {
		return 0x4FFFFFFF;
	}

	/**
	 * 是否在线
	 * 
	 * @param fmFlags
	 * @return
	 */
	public boolean isOnline() {
		return OnlineUtil.isOnline(id + "");
	}

	/**
	 * 判断问答是否被禁
	 * 
	 * @param fm_state
	 * @return
	 */
	public static boolean isBlocked_ask(int fm_state) {
		return (fm_state & BLOCKED_ASK) != 0;
	}

	/**
	 * 判断龙舟是否被禁
	 * 
	 * @param fm_state
	 * @return
	 */
	public static boolean isBlocked_boat(int fm_state) {
		return (fm_state & BLOCKED_BOAT) != 0;
	}

	/**
	 * 判断雪仗是否被禁
	 * 
	 * @param fm_state
	 * @return
	 */
	public static boolean isBlocked_snow(int fm_state) {
		return (fm_state & BLOCKED_SNOW) != 0;
	}

	/**
	 * 判断是否有权限玩对战游戏
	 * 
	 * @param fm_state
	 * @return
	 */
	public static boolean isVsGame(int fm_state) {
		return (fm_state & VS_GAME) != 0;
	}

	public int getAlive() {
		return alive;
	}

	public void setAlive(int alive) {
		this.alive = alive;
	}

	public int getHonor() {
		return honor;
	}

	public void setHonor(int honor) {
		this.honor = honor;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public int getSetting() {
		return setting;
	}

	public void setSetting(int setting) {
		this.setting = setting;
	}
	public void toggleSetting(int is) {
		setting ^= (1 << is);
	}
	public boolean isSetting(int is) {
		return (setting & (1 << is)) != 0;
	}
	public boolean isSettingChatLink() {
		return (setting & SETTING_CHAT_LINK) != 0;
	}
	
}