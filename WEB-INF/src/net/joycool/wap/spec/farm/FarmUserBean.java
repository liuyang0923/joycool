package net.joycool.wap.spec.farm;

import java.util.*;

import net.joycool.wap.spec.farm.bean.*;
import net.joycool.wap.util.LockUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 采集
 * @datetime:1007-10-24
 */
public class FarmUserBean {
	public static int FLAG_OFFLINE = (1 << 0);	// 已经下线
	
	public static String attrName[] = {"力量", "敏捷", "耐力", "智力", "精神"};
	public static String class1Name[] = {"平民", "剑客", "武夫", "道士", "刺客", "艺人", "行医"};
	public static String elementName[] = {"无", "金", "木", "水", "火", "土", "风", "雷"};
	
	public static int MAX_MONEY = 10000;		// 每个等级最大带1万个铜板
	public static int TOTAL_PRO = 15;			// 总共的专业数量
	public static int TOTAL_PART = 10;			// 装备部位总数
	public SimpleGameLog log = new SimpleGameLog();
	
	String nameWml;
	String name;		// 游戏中的名字
	
	int worldId = -1;		// 正在玩的世界，没有的话就是-1
	int userId;
	
	static int[] expRank = {0,20,50,100,180,300,440,600,780,1000,
		1250,1600,2000,2500,3150,4000,5000,6000,7000,8000,
		9000,10000,11000,12500,14000,15500,17000,19000,21000,
		23000,25000,28000,31000,34000,38000,42000,46000,51000,56000};		// 升级所需要的经验值
	int exp = 0;
	int rank = 1;		// 级别 1 - 60
	int maxRank = 10;	// 最高级别限制
	
	int money = 50;		// 网游专用币，和乐币的比值为1:10000
	
	int proPoint = 0;		// 剩余可以加的专业技能点 
	int class1;				// 职业
	TongUserBean tongUser;	// 门派
	int element;			// 五行
	
	int pos = 0;			// 正在的位置
	int bindPos = 0;		// 灵魂绑定的位置，死了就可以回到这里
	int npcId = 0;		// 正在联系的npc，0表示没有
	int factoryId = 0;		// 正在联系的加工厂，0表示没有
	
	long nextActionTime = 0;	// 下一次可以动作的时间
	
	List targetList = new ArrayList();		// 目标列表，攻击者和被攻击都会加入这个列表
	
	List quests = new ArrayList();		// 当前任务
	HashMap endQuests = new LinkedHashMap();		// 已经完成的任务
	HashMap cooldownMap = new HashMap();	// 冷却map
	HashMap endTriggers = new HashMap();		// 已经触发的触发器
	
	FarmUserProBean[] pro = new FarmUserProBean[TOTAL_PRO];
	FarmUserEquipBean[] equip = new FarmUserEquipBean[TOTAL_PART];
	
	HashMap npcMark = new HashMap();		// npc头上的标志，暂时两种(!,?)，分别表示有新任务和有需要接的任务
	public long savePos;		// 如果当前时间大于这个，保存当前位置
	
//	 人物基本属性
	int attr1;
	int attr2;
	int attr3;
	int attr4;
	int attr5;
	String questCreature;		// 保存到数据库的怪物猎杀数据
	
	List questCreatureFinish = new LinkedList();		// 猎杀任务记录，格式：任务id-怪物id-完成数量
	List questTalkFinish = new LinkedList();		// 谈话任务记录，格式：任务id-谈话id-完成数量，数量0表示未完成，1表示完成
	List questSearchFinish = new LinkedList();		// 搜索任务记录，格式：任务id-位置id-完成数量，到达地图某个位置就完成
	
	GroupBean group = null;		// 所属队伍
	
	List buffList = null;			// 附加战斗状态
	FarmCarBean car = null;			// 当前车辆
	int carPos;			// 当前在航线的第几个结点
	int flag;
	
	LinkedList curTrigger = new LinkedList();		// 已经触发的触发器，显示给用户之后就删除
	
	int inviteTongUser;		// 此人邀请你加入他的帮会
	
	public GroupBean getGroup() {
		return group;
	}
	public void setGroup(GroupBean group) {
		this.group = group;
	}
	public void resetAttr() {
		attr1 = attr2 = attr3 = attr4 = attr5 = 5;
	}
	int battlePoint;	// 可分配战斗属性点
	
	public int hp = 1000, mp = 200, sp = 200;		// 当前值
	
	public FarmUserBean() {
		resetAttr();
	}
	
	BattleStatus curStat;	//  当前状态数值
	public BattleStatus getCurStat() {
		if(curStat == null) {
			curStat = new BattleStatus(this);
			if(hp > curStat.hp)
				hp = curStat.hp;
			if(mp > curStat.mp)
				mp = curStat.mp;
			if(sp > curStat.sp)
				sp = curStat.sp;
		}
		if(buffList == null || buffList.size() == 0)
			return curStat;
		return applyBuff(curStat, buffList);
	}
	public void addBuff(int buff, int value) {
		int[] b = {buff, value};
		if(buffList == null)
			buffList = new ArrayList(4);
		buffList.add(b);
	}
	public void removeBuff(int buff) {
		if(buffList == null)
			return;
		for(int i = 0;i < buffList.size();i++) {
			int[] b = (int[])buffList.get(i);
			if(b[0] == buff) {
				buffList.remove(i);
				i--;
			}
		}
	}
	public BattleStatus applyBuff(BattleStatus bs, List buffList) {
		BattleStatus bs2 = bs.cloneBS();
		for(int i = 0;i < buffList.size();i++) {
			int[] b = (int[])buffList.get(i);
			switch(b[0]) {
			case 1: {		// 增加防御百分比
				bs2.defense1 *= (1 + (float)b[1] / 100);
			} break;
			}
		}
		return bs2;
	}
	public void resetCurStat() {
		curStat = null;
	}

	LandUserBean landUser = new LandUserBean();
	
	Object lock = null;	// 用户操作的锁
	
	public Object getLock() {
		if(lock == null) {
			lock = LockUtil.farmUserLock.getLock(userId);
		}
		return lock;
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
	public boolean isFlagOffline() {
		return (flag & FLAG_OFFLINE) != 0;
	}
	public void setFlag(int is, boolean set) {
		if(set)
			flag |= (1 << is);
		else
			flag &= ~(1 << is);
	}
	
	// 加经验值
	public boolean addExp(int add) {
		exp += add;
		if(rank >= maxRank)
			return true;		// 无法升级
		if(exp >= expRank[rank]) {
			if(rank < maxRank)
				rank++;
		}
		return true;
	}
	
	// 获得cooldown id剩余时间（毫秒）
	public long getCooldown(int cooldownId) {
		return getCooldown(cooldownId, System.currentTimeMillis());
	}
	public long getCooldown(int cooldownId, long now) {
		CooldownBean cb = (CooldownBean)cooldownMap.get(Integer.valueOf(cooldownId));
		if(cb == null || now > cb.getTime())
			return 0;
		return cb.getTime() - now;
	}
	// 判断一个cooldown id是否已经到时间
	public boolean isCooldown(int cooldownId) {
		return getCooldown(cooldownId) == 0;
	}
	public boolean isCooldown(int cooldownId, long now) {
		return getCooldown(cooldownId, now) == 0;
	}
	// 增加一个冷却
	// 返回true，如果是新增一个，返回false则只是更新
	public boolean addCooldown(int cooldownId, long time) {
		CooldownBean cb = (CooldownBean)cooldownMap.get(Integer.valueOf(cooldownId));
		if(cb == null) {
			cb = new CooldownBean();
			cb.setUserId(userId);
			cb.setCooldownId(cooldownId);
			cb.setTime(time);
			cooldownMap.put(Integer.valueOf(cooldownId), cb);
			return true;
		}
		cb.setTime(time);
		return false;	
	}
	
	public List getTargetList() {
		return targetList;
	}
	// 把目标放到第一目标
	public void moveTarget(int index, Object obj) {
		synchronized(targetList) {
			if(targetList.get(index) != obj) {
				targetList.remove(obj);
				targetList.add(index, obj);
			}
		}
	}
	// 添加目标到最后
	public void addTargetLast(Object obj) {
		targetList.add(obj);
	}
	public void addTarget2(Object obj) {
		synchronized(targetList) {
			if(!targetList.contains(obj))
				addTarget(obj);
		}
	}
	public void addTarget(Object obj) {
		if(obj instanceof CreatureBean) {
			CreatureBean c = (CreatureBean)obj;
			if(c.isAlive()) {
				synchronized(targetList) {
					targetList.add(0, obj);
				}
			}
			return;
		}
		if(obj instanceof FarmUserBean) {
			FarmUserBean c = (FarmUserBean)obj;
			if(c.isAlive()) {
				synchronized(targetList) {
					targetList.add(0, obj);
				}
			}
		}
	}
	public void removeTarget(Object obj) {
		synchronized(targetList) {
			targetList.remove(obj);
		}
	}
	
	public int getUpgradeExp() {
		return expRank[rank];
	}
	// 最大可携带金额
	public int getMaxMoney() {
		return (int) (rank * MAX_MONEY * Math.sqrt(rank));
	}
	
	public String getClass1Name() {
		return class1Name[class1];
	}
	// 五行
	public String getElementName() {
		return elementName[element];
	}
	
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Returns the pro.
	 */
	public FarmUserProBean[] getPro() {
		return pro;
	}

	/**
	 * @return Returns the nextActionTime.
	 */
	public long getNextActionTime() {
		return nextActionTime;
	}

	/**
	 * @param nextActionTime The nextActionTime to set.
	 */
	public void setNextActionTime(long nextActionTime) {
		this.nextActionTime = nextActionTime;
	}

	/**
	 * @return Returns the exp.
	 */
	public int getExp() {
		return exp;
	}

	/**
	 * @param exp The exp to set.
	 */
	public void setExp(int exp) {
		this.exp = exp;
	}

	/**
	 * @return Returns the rank.
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * @param rank The rank to set.
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * @return Returns the npcId.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @param npcId The npcId to set.
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * @return Returns the posId.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param posId The posId to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}

	/**
	 * @return Returns the money.
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * @param money The money to set.
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	public void addMoney(int add) {
		money += add;
		if(money < 0)
			money = 0;
		else {
			int max = getMaxMoney();
			if(money > max)
				money = max;
		}
	}

	/**
	 * @return Returns the proPoint.
	 */
	public int getProPoint() {
		return proPoint;
	}

	/**
	 * @param proPoint The proPoint to set.
	 */
	public void setProPoint(int proPoint) {
		this.proPoint = proPoint;
	}

	public void addProPoint(int add) {
		proPoint += add;
	}

	public FarmUserProBean getPro(int index) {
		return pro[index];
	}
	public int getProRank(int index) {
		if(pro[index] != null)
			return pro[index].getRank();
		return 0;
	}

	/**
	 * @return Returns the landUser.
	 */
	public LandUserBean getLandUser() {
		return landUser;
	}

	/**
	 * @return Returns the factoryId.
	 */
	public int getFactoryId() {
		return factoryId;
	}

	/**
	 * @param factoryId The factoryId to set.
	 */
	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	/**
	 * @return Returns the endQuests.
	 */
	public HashMap getEndQuests() {
		return endQuests;
	}

	/**
	 * @return Returns the quests.
	 */
	public List getQuests() {
		return quests;
	}
	
	// 开始任务
	public void beginQuest(FarmUserQuestBean userQuest) {
		quests.add(userQuest);
	}
	
	// 完成任务
	public void endQuest(FarmUserQuestBean userQuest) {
		removeQuest(userQuest);
		endQuests.put(Integer.valueOf(userQuest.getQuestId()), Integer.valueOf(userQuest.getId()));
	}
	// 完成触发
	public void endTrigger(UserTriggerBean userTrigger) {
		endTriggers.put(Integer.valueOf(userTrigger.getTriggerId()), Integer.valueOf(userTrigger.getId()));
	}
	
	// 中止任务
	public void removeQuest(FarmUserQuestBean userQuest) {
		for(int i = 0;i < quests.size();i++) {
			FarmUserQuestBean userQuest2 = (FarmUserQuestBean)quests.get(i);
			if(userQuest2.getId() == userQuest.getId()) {
				quests.remove(i);
				return;
			}
		}
	}
	
	// 这个任务是否是完成了并且不可再次接受状态？
	public boolean canDoQuest(FarmQuestBean quest, long now) {
		if(quest.isFlagRedo())
			return true;
		FarmUserQuestBean userQuest = getQuestEnd(quest.getId());
		if(userQuest == null)
			return true;
		if(quest.isFlagTimeRedo())
			return userQuest.getDoneTime() + quest.getInterval() < now;
		return false;
	}
	
	// 是否完成了某个任务
	public boolean isQuestEnd(int questId) {
		return endQuests.containsKey(Integer.valueOf(questId));
	}
	public boolean isQuestEnd(Integer iid) {
		return endQuests.containsKey(iid);
	}
	// 获得已经完成的对应任务，如果没有则返回null
	public FarmUserQuestBean getQuestEnd(int questId) {
		Integer iid = (Integer)endQuests.get(Integer.valueOf(questId));
		if(iid == null)
			return null;
		return FarmNpcWorld.one.getUserQuest(iid);
	}
	
	// 如果存在，返回对应的进行中任务
	public FarmUserQuestBean getStartQuest(int questId) {
		for(int i = 0;i < quests.size();i++) {
			FarmUserQuestBean userQuest = (FarmUserQuestBean)quests.get(i);
			if(userQuest.getQuestId() == questId)
				return userQuest;
		}
		return null;
	}
	
	// 是否进行着某个任务
	public boolean isQuestStart(int questId) {
		return getStartQuest(questId) != null;
	}
	
	// 是否触发了某个触发器
	public boolean isTriggered(int triggerId) {
		return endTriggers.containsKey(Integer.valueOf(triggerId));
	}
	public boolean isTriggered(Integer iid) {
		return endTriggers.containsKey(iid);
	}

	/**
	 * @param endQuests The endQuests to set.
	 */
	public void setEndQuests(HashMap endQuests) {
		this.endQuests = endQuests;
	}

	/**
	 * @param quests The quests to set.
	 */
	public void setQuests(List quests) {
		this.quests = quests;
	}

	/**
	 * @return Returns the attr1.
	 */
	public int getAttr1() {
		return attr1;
	}

	/**
	 * @param attr1 The attr1 to set.
	 */
	public void setAttr1(int attr1) {
		this.attr1 = attr1;
	}

	/**
	 * @return Returns the attr2.
	 */
	public int getAttr2() {
		return attr2;
	}

	/**
	 * @param attr2 The attr2 to set.
	 */
	public void setAttr2(int attr2) {
		this.attr2 = attr2;
	}

	/**
	 * @return Returns the attr3.
	 */
	public int getAttr3() {
		return attr3;
	}

	/**
	 * @param attr3 The attr3 to set.
	 */
	public void setAttr3(int attr3) {
		this.attr3 = attr3;
	}

	/**
	 * @return Returns the attr4.
	 */
	public int getAttr4() {
		return attr4;
	}

	/**
	 * @param attr4 The attr4 to set.
	 */
	public void setAttr4(int attr4) {
		this.attr4 = attr4;
	}

	/**
	 * @return Returns the attr5.
	 */
	public int getAttr5() {
		return attr5;
	}

	/**
	 * @param attr5 The attr5 to set.
	 */
	public void setAttr5(int attr5) {
		this.attr5 = attr5;
	}

	/**
	 * @return Returns the equip.
	 */
	public FarmUserEquipBean[] getEquip() {
		return equip;
	}
	public FarmUserEquipBean getEquip(int index) {
		return equip[index];
	}
	// 身上只要有一件装备就返回true
	public boolean hasEquip() {
		for(int i = 0;i < TOTAL_PART;i++)
			if(equip[i] != null && equip[i].getUserbagId() != 0)
				return true;
		return false;
	}
	
	public void flushNpcMark() {
		npcMark.clear();
	}
	public void flushNpcMark(int npcId) {
		npcMark.remove(Integer.valueOf(npcId));
	}
	public String getNpcMark(FarmNpcBean npc) {
		if(npc.isFlagQuest()) {
			Integer key = Integer.valueOf(npc.getId());
			String mark = (String)npcMark.get(key);
			if(mark == null) {
				mark = "";
				List begin = npc.getQuestBeginList();
				boolean hasQuest = false;
				for(int i=0;i<begin.size();i++){
					int id = ((Integer)begin.get(i)).intValue();
					FarmQuestBean quest = FarmNpcWorld.one.getQuest(id);
					if(isQuestStart(quest.getId())) {
						hasQuest = true;
					} else if(!isQuestEnd(quest.getId()) && FarmNpcWorld.canDoQuest(this, quest)) {
						mark = "!";
						break;
					}
				}
				if(mark.length() == 0 && hasQuest)		// 没有新任务，但是有接了的任务，显示?
					mark = "?";
				npcMark.put(key, mark);
			}
			return mark;
		}
		return "";
	}

	/**
	 * @return Returns the maxRank.
	 */
	public int getMaxRank() {
		return maxRank;
	}

	/**
	 * @param maxRank The maxRank to set.
	 */
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}

	/**
	 * @return Returns the cooldownMap.
	 */
	public HashMap getCooldownMap() {
		return cooldownMap;
	}

	/**
	 * @param cooldownMap The cooldownMap to set.
	 */
	public void setCooldownMap(HashMap cooldownMap) {
		this.cooldownMap = cooldownMap;
	}


	public String getName() {
		return name;
	}
	
	public String getNameWml() {
		if(name.length() == 0)
			return "(路人)";
		return nameWml;
	}

	public void setName(String name) {
		this.name = name;
		nameWml = StringUtil.toWml(name);
	}

	// 改名需要的钱
	public int getRenameMoney() {
		if(rank < 20)
			return 1000 * (int)Math.pow(2, rank);
		else
			return 1000 * (int)Math.pow(2, 20);	// 上限溢出
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(obj instanceof FarmUserBean) {
			return ((FarmUserBean)obj).getUserId() == userId;
		}
		return false;
	}
	public int getHp() {
		return hp;
	}
	public int getMp() {
		return mp;
	}
	public void decHp(int damage) {
		hp -= damage;
		if(hp <= 0) {
			hp = 0;
			FarmWorld.killPlayer(this);
		}
	}
	public void decMp(int damage) {
		mp -= damage;
		if(mp <= 0) {
			mp = 0;
		}
	}
	public void decSp(int damage) {
		sp -= damage;
		if(sp <= 0) {
			sp = 0;
		}
	}
	public void incHp(int add) {
		hp += add;
		if(hp > getCurStat().hp)
			hp = getCurStat().hp;
	}
	public void incMp(int add) {
		mp += add;
		if(mp > getCurStat().mp)
			mp = getCurStat().mp;
	}
	public void incSp(int add) {
		sp += add;
		if(sp > getCurStat().sp)
			sp = getCurStat().sp;
	}
	public void incHp(float add) {
		incHp((int)(add * getCurStat().hp));
	}
	public void incMp(float add) {
		incMp((int)(add * getCurStat().mp));
	}
	public void incSp(float add) {
		incSp((int)(add * getCurStat().sp));
	}
	public boolean isDead() {
		return hp <= 0;
	}
	public boolean isAlive() {
		return hp > 0;
	}
	
	public void addLog(String str) {
		log.add(str);
	}
	public int getBattlePoint() {
		return battlePoint;
	}
	public void setBattlePoint(int battlePoint) {
		this.battlePoint = battlePoint;
	}
	public void addAttr(int[] set) {
		attr1 += set[0];
		attr2 += set[1];
		attr3 += set[2];
		attr4 += set[3];
		attr5 += set[4];
	}
	// 复活，有一定惩罚
	public void revive() {
		hp = mp = sp = 30;
	}
	public static int[] penal = {0, 0, 10, 15, 25, 40, 60, 80, 100, 120, 150, 200, 200, 200, 200, 200};
	public void kill() {
		FarmUserProBean pro = getPro(FarmProBean.PRO_BATTLE);
		if(pro != null) {
			int exp = pro.getExp();
			if(exp > pro.getRankExp()) {
				exp -= penal[pro.getRank() / 3];
				if(exp < pro.getRankExp())
					exp = pro.getRankExp();
				pro.setExp(exp);
			}
		}
		FarmWorld.decEquipsDurability(this, 0.1f);
		resetCurStat();
	}
	public List getQuestCreatureFinish() {
		return questCreatureFinish;
	}
	public void setQuestCreatureFinish(List questCreatureFinish) {
		this.questCreatureFinish = questCreatureFinish;
	}
	public List getQuestTalkFinish() {
		return questTalkFinish;
	}
	public void setQuestTalkFinish(List questTalkFinish) {
		this.questTalkFinish = questTalkFinish;
	}
	// 接受任务后设置怪物猎杀、聊天完成状态
	public void addQuestFinishStatus(FarmQuestBean quest) {
		for(int i = 0;i < quest.getCreatureList().size();i++) {
			int[] is = (int[])quest.getCreatureList().get(i);
			int[] add = {quest.getId(), is[0], 0};
			questCreatureFinish.add(add);
		}
		for(int i = 0;i < quest.getTalkList().size();i++) {
			int[] is = (int[])quest.getTalkList().get(i);
			int[] add = {quest.getId(), is[0], 0};
			questTalkFinish.add(add);
		}
		for(int i = 0;i < quest.getSearchList().size();i++) {
			int[] is = (int[])quest.getSearchList().get(i);
			int[] add = {quest.getId(), is[0], 0};
			questSearchFinish.add(add);
		}
	}
	
	public void removeQuestFinishStatus(FarmQuestBean quest) {
		if(quest.getCreatureList().size() > 0) {
			Iterator iter = questCreatureFinish.iterator();
			while(iter.hasNext()) {
				int[] is = (int[])iter.next();
				if(is[0] == quest.getId()) {
					iter.remove();
				}
			}
		}
		if(quest.getTalkList().size() > 0) {
			Iterator iter = questTalkFinish.iterator();
			while(iter.hasNext()) {
				int[] is = (int[])iter.next();
				if(is[0] == quest.getId()) {
					iter.remove();
				}
			}
		}
		if(quest.getSearchList().size() > 0) {
			Iterator iter = questSearchFinish.iterator();
			while(iter.hasNext()) {
				int[] is = (int[])iter.next();
				if(is[0] == quest.getId()) {
					iter.remove();
				}
			}
		}
	}
	
	public int getCreatureFinishCount(int creatureId) {
		Iterator iter = questCreatureFinish.iterator();
		while(iter.hasNext()) {
			int[] is = (int[])iter.next();
			if(is[1] == creatureId) {
				return is[2];
			}
		}
		return 0;
	}
	
	public int getTalkFinishCount(int talkId) {
		Iterator iter = questTalkFinish.iterator();
		while(iter.hasNext()) {
			int[] is = (int[])iter.next();
			if(is[1] == talkId) {
				return is[2];
			}
		}
		return 0;
	}
	
	public int getSearchFinishCount(int searchId) {
		Iterator iter = questSearchFinish.iterator();
		while(iter.hasNext()) {
			int[] is = (int[])iter.next();
			if(is[1] == searchId) {
				return is[2];
			}
		}
		return 0;
	}
	public int getBindPos() {
		return bindPos;
	}
	public void setBindPos(int bindPos) {
		this.bindPos = bindPos;
	}
	public boolean isInBattle() {
		return targetList.size() > 0;
	}
	public boolean isCombat() {
		return targetList.size() > 0;
	}
	public int getClass1() {
		return class1;
	}
	public void setClass1(int class1) {
		this.class1 = class1;
	}
	public String getQuestCreature() {
		return questCreature;
	}
	public void setQuestCreature(String questCreature) {
		this.questCreature = questCreature;
	}
	public void updateQuestStatus() {
		if(questCreature.length() == 0)
			return;
		List questCreatureList = StringUtil.toIntss(questCreature);
		for(int i = 0;i < questCreatureList.size();i++) {
			int[] qc = (int[])questCreatureList.get(i);
			if(qc.length != 3)
				continue;
			Iterator iter = questCreatureFinish.iterator();
			while(iter.hasNext()) {
				int[] cr = (int[])iter.next();
				if(qc[1] == cr[1] && qc[0] == cr[0]) {
					cr[2] = qc[2];
					break;
				}
			}
		}
	}
	public int getElement() {
		return element;
	}
	public void setElement(int element) {
		this.element = element;
	}
	public FarmCarBean getCar() {
		return car;
	}
	public void setCar(FarmCarBean car) {
		this.car = car;
	}
	public int getCarPos() {
		return carPos;
	}
	public void setCarPos(int carPos) {
		this.carPos = carPos;
	}
	public void startCar(FarmCarBean car) {
		carPos = 0;
		this.car = car;
	}
	public TongUserBean getTongUser() {
		return tongUser;
	}
	public void setTongUser(TongUserBean tongUser) {
		this.tongUser = tongUser;
	}
	public HashMap getEndTriggers() {
		return endTriggers;
	}
	public LinkedList getCurTrigger() {
		return curTrigger;
	}
	public int getInviteTongUser() {
		return inviteTongUser;
	}
	public void setInviteTongUser(int inviteTongUser) {
		this.inviteTongUser = inviteTongUser;
	}
	public void setCurTrigger(LinkedList curTrigger) {
		this.curTrigger = curTrigger;
	}
}
