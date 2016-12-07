package net.joycool.wap.spec.castle;

import java.util.*;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class CastleUserBean {
	public final static int FLAG_PALACE = (1 << 0);		// 已经开始或者结束建造了皇宫
	public static long protectInterval = CastleUtil.PROTECT_DAY * 24 * 3600 * 1000l;
	int uid;
	String name;		// 玩家名称
	String info = "";		// 个人资料
	int civil;			// 文明度
	float civilP;		// 文明度的小数部分保留
	int civilSpeed;		// 文明增加速度/天
	long civilTime;			// 文明度时间
	long lockTime;			// 帐号锁定时间
	int castleCount = 1;		// 拥有的城堡数量
	long createTime;
	long protectTime;		// 新手保护期
	long spTime;			// sp帐号
	int flag;
	int tong;		// 联盟
	int main;		// 主城堡id
	int unread;		// 未读战报
	
	int quest;	// 当前的新手任务
	int questStatus;	// 0表示任务未完成，1表示任务完成
	
	int gold;		// 拥有的金币
	int people = 2;		// 总人口
	
	int cur;		//  当前城堡cid
	int race;		// 种族
	
	long attackTotal;		// 总统计
	long defenseTotal;
	long robTotal;
	int attackWeek;		// 每周统计，周日清晨清空
	int defenseWeek;
	int robWeek;
	
	HeroBean hero = null;	// 这个玩家的英雄数据
	
	static HeroBean nullHero = new HeroBean();	// 如果没有英雄，保存这个 
	public HeroBean getHero() {
		if(hero == nullHero)
			return null;
		
		if(hero == null) {
			hero = CastleUtil.getHero(uid);
			if(hero == null) {
				hero = nullHero;		// 避免下次查询
				return null;
			}
		}
		hero.setRace(race);
		return hero;
	}
	public void deleteHero() {
		hero = nullHero;
	}
	
	public String getRaceName() {
		return ResNeed.raceNames[race];
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	List castleList;		// 城堡ID列表，内容为integer
	
	public int getCur() {
		return cur;
	}
	public void setCur(int cur) {
		this.cur = cur;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public CastleUserBean() {
		
	}
	public CastleUserBean(int id) {
		this.uid = id;
		createTime = System.currentTimeMillis();
		protectTime = createTime + protectInterval;
	}
	public int getCastleCount() {
		return castleCount;
	}
	public void setCastleCount(int castleCount) {
		this.castleCount = castleCount;
	}
	public int getCivil() {
		return civil;
	}
	public void setCivil(int civil) {
		this.civil = civil;
	}
	public long getCivilTime() {
		return civilTime;
	}
	public void setCivilTime(long civilTime) {
		this.civilTime = civilTime;
	}
	
	public void addFlag(int flagValue) {
		this.flag |= flagValue;
	}
	public void deleteFlag(int flagValue) {
		this.flag &= ~flagValue;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagPalace() {
		return (flag & FLAG_PALACE) != 0;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public String getNameWml() {
		return StringUtil.toWml(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getProtectTime() {
		return protectTime;
	}
	public void setProtectTime(long protectTime) {
		this.protectTime = protectTime;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getQuest() {
		return quest;
	}
	public void setQuest(int quest) {
		this.quest = quest;
	}
	public int getQuestStatus() {
		return questStatus;
	}
	public void setQuestStatus(int questStatus) {
		this.questStatus = questStatus;
	}
	public int getTong() {
		return tong;
	}
	public void setTong(int tong) {
		this.tong = tong;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public void addCivil(int value) {
		this.civil += value;
	}
	public int getCivilSpeed() {
		return civilSpeed;
	}
	public void setCivilSpeed(int civilSpeed) {
		this.civilSpeed = civilSpeed;
	}
	public void addCivilSpeed(int value) {
		civilSpeed += value;
		if(civilSpeed < 0)
			civilSpeed = 0;
	}
	public int getMain() {
		return main;
	}
	public void setMain(int main) {
		this.main = main;
	}
	public static long MS_IN_DAY = 86400l * 1000;
	public int getCivil(long now) {
		float days = (float)((now - civilTime)) / DateUtil.MS_IN_DAY;

		int w = civil + (int)(civilSpeed * days + civilP);
		
		return w;
	}
	public void reCalc(long now) {
		
		float days = (float)((now - civilTime)) / DateUtil.MS_IN_DAY;
		
		civilP += civilSpeed * days;
		
		civil = civil + (int)civilP;
		civilP -= (int)civilP;
		civilTime = now;
	}
	// 建造下一个城堡需要文明度？
	public int getCivilNeed() {
		return ResNeed.civilNeed[castleCount];
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo2() {
		String replace = "<img src=\"/cast/img/new.gif\" alt=\"和平鸽\"/>";
		if(protectTime < System.currentTimeMillis())
			replace += "创建于" + DateUtil.formatDate2(createTime);
		else
			replace += "新手保护期" + DateUtil.formatTimeInterval2(protectTime);
		replace += "<br/>";
		String ret = StringUtil.toWml(info).replaceFirst("\\[#0\\]", replace);
		return ret;
	}
	public long getSpTime() {
		return spTime;
	}
	public void setSpTime(long spTime) {
		this.spTime = spTime;
	}
	public boolean isSpAccount() {
		return spTime > System.currentTimeMillis();
	}
	public boolean isSpAccount(long now) {
		return spTime > now;
	}
	public long getLockTime() {
		return lockTime;
	}
	public void setLockTime(long lockTime) {
		this.lockTime = lockTime;
	}
	public boolean isLocked() {
		return System.currentTimeMillis() > lockTime;
	}
	public int getUnread() {
		return unread;
	}
	public void setUnread(int unread) {
		this.unread = unread;
	}
	public void addUnread() {
		unread++;
	}
	public void addPeople(int people) {
		this.people += people;
		if(this.people < 0)
			this.people = 0;
	}
	public List getCastleList() {
		if(castleList == null) {
			
			if(castleCount == 1) {
				castleList = new ArrayList(1);
				castleList.add(new Integer(main));
			} else {
				castleList = SqlUtil.getIntList("select id from castle where uid=" + uid, 5);
			}
		}
		return castleList;
	}
	public void setCastleList(List castleList) {
		this.castleList = castleList;
	}
	// 是否能创建新城堡
	public boolean canCreate(long now) {
		return ResNeed.getNeedCivil(castleCount) <= getCivil(now);
	}
	// 下一个新城堡需要的文明度
	public int getNextCivil() {
		return ResNeed.getNeedCivil(castleCount);
	}
	public void addCastle(CastleBean castle) {
		castleCount++;
		getCastleList().add(new Integer(castle.getId()));
	}
	public long getAttackTotal() {
		return attackTotal;
	}
	public void setAttackTotal(long attackTotal) {
		this.attackTotal = attackTotal;
	}
	public long getDefenseTotal() {
		return defenseTotal;
	}
	public void setDefenseTotal(long defenseTotal) {
		this.defenseTotal = defenseTotal;
	}
	public long getRobTotal() {
		return robTotal;
	}
	public void setRobTotal(long robTotal) {
		this.robTotal = robTotal;
	}
	public int getAttackWeek() {
		return attackWeek;
	}
	public void setAttackWeek(int attackWeek) {
		this.attackWeek = attackWeek;
	}
	public int getDefenseWeek() {
		return defenseWeek;
	}
	public void setDefenseWeek(int defenseWeek) {
		this.defenseWeek = defenseWeek;
	}
	public int getRobWeek() {
		return robWeek;
	}
	public void setRobWeek(int robWeek) {
		this.robWeek = robWeek;
	}
	public void setHero(HeroBean hero) {
		this.hero = hero;
	}
	public void addCastleCount(int i) {
		castleCount += i;
		if(castleCount < 0)
			castleCount = 0;
	}
}
