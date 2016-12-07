package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.spec.farm.FarmNpcWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： NPC
 * @datetime:1007-10-24
 */
public class FarmNpcBean {
	static int FLAG_SELL = (1 << 0);
	static int FLAG_BUY = (1 << 1);
	static int FLAG_SKILL = (1 << 2);
	static int FLAG_QUEST = (1 << 3);
	static int FLAG_EQUIP_SELL = (1 << 4);
	static int FLAG_AUCTION = (1 << 5);
	static int FLAG_REPAIR = (1 << 6);
	static int FLAG_HIDE = (1 << 7);
	static int FLAG_COLLECT = (1 << 8);
	
	public static String[] flagString = {"出售", "回收", "技能", "任务", "装备回收", "拍卖", "修理", "隐藏","收藏盒"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	String name;
	String intro;
	String learnSkill;		// 从这个npc可以学的技能
	String talk;			// npc聊天内容，farm_talk表id
	int defaultType;		// 默认打开的栏目，0购买1出售
	int flag;				// 标志位，前三位暂时表示sell buy skill
	int pos;				// 所在的map_node_id
	
	String questBegin;		// 开始任务
	String questEnd;		// 结束任务
	String cars = "";		// 驿站线路
	
	List questBeginList;
	List questEndList;
	List talkList;
	List carList;
	
	List skills = new ArrayList();			// 从这个npc可以学的技能
	
	public void init() {
		questBeginList = StringUtil.toInts(questBegin);
		questEndList = StringUtil.toInts(questEnd);
		carList = StringUtil.toInts(cars);
	}
	public void initTalk(FarmNpcWorld world) {
		talkList = new ArrayList();
		List l = StringUtil.toInts(talk);
		for(int j = 0;j < l.size();j++) {
			Integer iid = (Integer)l.get(j);
			talkList.add(world.getTalk(iid));
		}
	}
	
	// 判断是否用某个任务
	public boolean hasQuestBegin(int questId) {
		return questBeginList.contains(Integer.valueOf(questId));
	}
	public boolean hasQuestEnd(int questId) {
		return questEndList.contains(Integer.valueOf(questId));
	}
	
	/**
	 * @return Returns the defaultType.
	 */
	public int getDefaultType() {
		return defaultType;
	}
	/**
	 * @param defaultType The defaultType to set.
	 */
	public void setDefaultType(int defaultType) {
		this.defaultType = defaultType;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the intro.
	 */
	public String getIntro() {
		return intro;
	}
	/**
	 * @param intro The intro to set.
	 */
	public void setIntro(String intro) {
		this.intro = intro;
	}
	/**
	 * @param learnSkill The learnSkill to set.
	 */
	public void setLearnSkill(String learnSkill) {
		this.learnSkill = learnSkill;
		skills.clear();
		String[] s = learnSkill.split(",");
		for(int i = 0;i < s.length;i++) {
			int id = StringUtil.toInt(s[i]);
			if(id > 0)
				skills.add(Integer.valueOf(id));
		}
	}
	
	public String getLearnSkill() {
		return learnSkill;
	}
	public List getLearnSkillList() {
		return skills;
	}
	/**
	 * @return Returns the flag.
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagSell() {
		return (flag & FLAG_SELL) != 0;
	}
	public boolean isFlagBuy() {
		return (flag & FLAG_BUY) != 0;
	}
	public boolean isFlagSkill() {
		return (flag & FLAG_SKILL) != 0;
	}
	public boolean isFlagQuest() {
		return (flag & FLAG_QUEST) != 0;
	}
	public boolean isFlagEquipSell() {
		return (flag & FLAG_EQUIP_SELL) != 0;
	}
	public boolean isFlagAuction() {
		return (flag & FLAG_AUCTION) != 0;
	}
	public boolean isFlagRepair() {
		return (flag & FLAG_REPAIR) != 0;
	}
	public boolean isFlagHide() {
		return (flag & FLAG_HIDE) != 0;
	}
	public boolean isFlagCollect() {
		return (flag & FLAG_COLLECT) != 0;
	}
	/**
	 * @return Returns the questBegin.
	 */
	public String getQuestBegin() {
		return questBegin;
	}
	/**
	 * @param questBegin The questBegin to set.
	 */
	public void setQuestBegin(String questBegin) {
		this.questBegin = questBegin;
	}
	/**
	 * @return Returns the questEnd.
	 */
	public String getQuestEnd() {
		return questEnd;
	}
	/**
	 * @param questEnd The questEnd to set.
	 */
	public void setQuestEnd(String questEnd) {
		this.questEnd = questEnd;
	}
	/**
	 * @return Returns the questBeginList.
	 */
	public List getQuestBeginList() {
		return questBeginList;
	}
	/**
	 * @return Returns the questEndList.
	 */
	public List getQuestEndList() {
		return questEndList;
	}

	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return Returns the talk.
	 */
	public String getTalk() {
		return talk;
	}

	/**
	 * @param talk The talk to set.
	 */
	public void setTalk(String talk) {
		this.talk = talk;
	}

	/**
	 * @return Returns the talkList.
	 */
	public List getTalkList() {
		return talkList;
	}
	public List getCarList() {
		return carList;
	}
	public void setCarList(List carList) {
		this.carList = carList;
	}
	public String getCars() {
		return cars;
	}
	public void setCars(String cars) {
		this.cars = cars;
	}
}
