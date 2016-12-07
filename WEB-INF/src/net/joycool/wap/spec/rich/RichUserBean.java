package net.joycool.wap.spec.rich;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 大富翁
 * @datetime:1007-10-24
 */
public class RichUserBean {
	public static int STATUS_PLAY = 0;
	public static int STATUS_END = 1;
	public static int STATUS_LOSE = 2;
	public static int STATUS_INIT = 3;		// 游戏未初始化
	
	public static int MAX_BAG_SIZE = 8;
	
	public static String roleNames[] = {"阿土伯", "孙小美", "钱夫人", "金贝贝", "忍太朗", 
		"约翰乔", "小丹尼", "糖糖", "乌米", "宫本宝藏"};
	public static String roleMarks[] = {"A", "S", "Q", "J", "R",  
		"Y", "X", "T", "W", "G"};	// 房子标记字符
	public static int genders[] = {0,1,1,1,0,0,0,1,1,0};
	public SimpleGameLog log = new SimpleGameLog();
	
	String name;		// 游戏中的名字
	String wmlName;
	public String ip;			// 进入游戏时候的ip
	
	int worldId = -1;		// 正在玩的世界，没有的话就是-1
	int id;
	int userId;
	
	int role = -1;		// 选中的角色		没有选择为-1
	int status;			// 当前状态
	int position = 0;	// 所在结点的id
	
	int timeLeft;		// 剩余的行动点
	int gStatus;		// 在医院、监狱、外星人，状态不明
	
	int money = 0;		// 当前现金
	int money2 = 0;		// 当前点券
	int saving = 0;		// 存款
	
	int with = 0;		// 定时炸弹或者财神等
	int withCount = 0;	// 俯身的数字，如果是财神表示回合数，如果是炸弹表示格数
	
	int logCount = 0;		// 总计log数
	int readLogCount = 0;	// 阅读的log数
	
	long nextActionTime = 0;	// 下一次可以动作的时间
	long lastActionTime = 0;	// 最近一次go的时间
	
	public List dices = new ArrayList();		// 使用遥控骰子等，保存一堆Integer，表示下一个将要得到的骰子结果，例如乌龟卡
	
	int inPlace;		// 在特殊的地方 0 不在 1 买房子 2 商店 3 间谍
	boolean inBank = false;
	boolean reverse = false;		// 被转向
	
	int totalHouse;		// 总房产数，只用于最后发布结果
	int totalHouseValue;	// 总房产价值，只用于最后发布结果
	
	public List bag = new ArrayList();
	
	// 是否男性角色
	public boolean isMale() {
		return genders[role] == 0;
	}
	public static String getRoleMark(int role) {
		return roleMarks[role];
	}
	
	public String getRoleMark() {
		return getRoleMark(role);
	}
	
	public static String getRoleName(int role) {
		return roleNames[role];
	}
	
	public String getRoleName() {
		return getRoleName(role);
	}

	public RichUserBean() {
		reset();
	}
	
	public void reset() {
		status = STATUS_INIT;
		worldId = -1;
		totalHouse = 0;
		totalHouseValue = 0;
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

	/**
	 * @return Returns the saving.
	 */
	public int getSaving() {
		return saving;
	}

	/**
	 * @param saving The saving to set.
	 */
	public void setSaving(int saving) {
		this.saving = saving;
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
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return Returns the role.
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * @return Returns the money2.
	 */
	public int getMoney2() {
		return money2;
	}

	/**
	 * @param money2 The money2 to set.
	 */
	public void setMoney2(int money2) {
		this.money2 = money2;
	}

	public void addMoney2(int value) {
		money2 += value;
		if(money2 < 0)
			money2 = 0;
	}
	public void addMoney(int value) {
		money += value;
		if(money < 0) {
			saving += money;
			money = 0;
		}
	}
	public void addSaving(int value) {
		saving += value;
	}
	public void addSavingRate(float value) {
		saving = (int) (saving * (1 + value));
	}
	public int decTimeLeft(int value) {
		timeLeft -= value;
		return timeLeft;
	}
	public int decTimeLeft() {
		return --timeLeft;
	}

	public void init() {
		timeLeft = 100;
		
		log.clear();
		logCount = readLogCount = 0;
		
		money = 5000;
		money2 = 50;
		saving = 5000;
		status = STATUS_PLAY;
		gStatus = 0;
		
		reverse = RandomUtil.percentRandom(50);
		
		bag.clear();
		bag.add(RichShop.getItem(1));	// 初始有三个道具
		bag.add(RichShop.getItem(2));
		bag.add(RichShop.getItem(3));
		
		inPlace = 0;
		inBank = false;
		
		with = 0;
		dices.clear();
	}

	/**
	 * @return Returns the timeLeft.
	 */
	public int getTimeLeft() {
		return timeLeft;
	}

	/**
	 * @param timeLeft The timeLeft to set.
	 */
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	/**
	 * @return Returns the inBank.
	 */
	public boolean isInBank() {
		return inBank;
	}

	/**
	 * @param inBank The inBank to set.
	 */
	public void setInBank(boolean inBank) {
		this.inBank = inBank;
	}


	public void setInPlace(int place) {
		inPlace = place;
	}
	public boolean isInHouse() {
		return inPlace == 1;
	}
	public void setInHouse(boolean in) {
		inPlace = in ? 1 : 0;
	}

	public boolean isInShop() {
		return inPlace == 2;
	}
	public void setInShop(boolean in) {
		inPlace = in ? 2 : 0;
	}
	
	public boolean isInSpy() {
		return inPlace == 3;
	}
	public void setInSpy(boolean in) {
		inPlace = in ? 3 : 0;
	}
	
	public boolean isInMagic() {
		return inPlace == 4;
	}
	public void setInMagic(boolean in) {
		inPlace = in ? 4 : 0;
	}
	
	public boolean isInCourt() {
		return inPlace == 5;
	}
	public void setInCourt(boolean in) {
		inPlace = in ? 5 : 0;
	}
	
	public boolean isInGame1() {
		return inPlace == 6;
	}
	public void setInGame1(boolean in) {
		inPlace = in ? 6 : 0;
	}
	
	public boolean isInGame2() {
		return inPlace == 7;
	}
	public void setInGame2(boolean in) {
		inPlace = in ? 7 : 0;
	}
	
	public boolean isInGame3() {
		return inPlace == 8;
	}
	public void setInGame3(boolean in) {
		inPlace = in ? 8 : 0;
	}
	
	public boolean isInGame4() {
		return inPlace == 9;
	}
	public void setInGame4(boolean in) {
		inPlace = in ? 9 : 0;
	}
	
	public boolean isInBusiness() {
		return inPlace == 10;
	}
	public void setInBusiness(boolean in) {
		inPlace = in ? 10 : 0;
	}
	

	public void addBag(RichItemBean item) {
		bag.add(item);
	}

	
	public int getDice() {
		if(dices.size() == 0)
			return -1;
		return ((Integer)dices.remove(0)).intValue();
	}
	/**
	 * @param dice The dice to set.
	 */
	public void addDice(int dice) {
		dices.add(Integer.valueOf(dice));
	}

	public void setDice(int dice) {
		dices.clear();
		addDice(dice);
	}

	/**
	 * @return Returns the reverse.
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * @param reverse The reverse to set.
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	public void reverse() {
		reverse = !reverse;
	}
	
	public String getDirection() {
		if(reverse)
			return "↓";
		else
			return "↑";
	}
	
	public boolean isBagFull() {
		return bag.size() >= MAX_BAG_SIZE;
	}
	public boolean isBagTooFull() {
		return bag.size() > MAX_BAG_SIZE;
	}

	/**
	 * @return Returns the nextActionTime.
	 */
	public long getNextActionTime() {
		return nextActionTime;
	}
	public void addNextActionTime(int add) {
		nextActionTime += add;
	}

	/**
	 * @param nextActionTime The nextActionTime to set.
	 */
	public void setNextActionTime(long nextActionTime) {
		this.nextActionTime = nextActionTime;
	}
	public void lockAction(long nextActionTime, int status) {
		this.nextActionTime = nextActionTime;
		gStatus = status;
	}
	

	public void delayAction(int cd) {
		nextActionTime = System.currentTimeMillis() + cd;
	}

	public long getLastActionTime() {
		return lastActionTime;
	}
	public void setLastActionTime(long lastActionTime) {
		this.lastActionTime = lastActionTime;
	}
	public void addLastActionTime(long add) {
		this.lastActionTime += add;
	}
	/**
	 * @return Returns the with.
	 */
	public int getWith() {
		return with;
	}

	/**
	 * @param with The with to set.
	 */
	public void setWith(int with) {
		this.with = with;
		withCount = 0;	// 重置
	}

	/**
	 * @return Returns the withCount.
	 */
	public int getWithCount() {
		return withCount;
	}
	public int addWithCount() {
		return ++withCount;
	}

	/**
	 * @param withCount The withCount to set.
	 */
	public void setWithCount(int withCount) {
		this.withCount = withCount;
	}
	
	public int readLog() {
		return logCount - readLogCount;
	}
	
	public void addLog(String content) {
		log.add(content);
		logCount++;
	}

	/**
	 * @return Returns the logCount.
	 */
	public int getLogCount() {
		return logCount;
	}

	/**
	 * @param logCount The logCount to set.
	 */
	public void setLogCount(int logCount) {
		this.logCount = logCount;
	}

	/**
	 * @return Returns the readLogCount.
	 */
	public int getReadLogCount() {
		return readLogCount;
	}

	/**
	 * @param readLogCount The readLogCount to set.
	 */
	public void setReadLogCount(int readLogCount) {
		this.readLogCount = readLogCount;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	public String getWmlName() {
		return getRoleName();// wmlName;
	}
	public String getFullName() {
		if(with == 0)
			return getWmlName();
		else
			return getWmlName() + "[" + RichNodeBean.eventNames[with] + "]";
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		wmlName = StringUtil.toWml(name);
	}

	public void broke() {
		status = STATUS_LOSE;
	}
	
	public boolean isBroke() {
		return status == STATUS_LOSE;
	}
	
	public boolean isStatusPlay() {
		return status == STATUS_PLAY;
	}

	public int getWithType() {
		return RichNodeBean.eventTypes[with];
	}

	/**
	 * @return Returns the worldId.
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * @param worldId The worldId to set.
	 */
	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	// 由于托管，取消人物控制，例如inhouse inshop
	public void noControl() {
		inPlace = 0;
		inBank = false;
	}

	/**
	 * @return Returns the gStatus.
	 */
	public int getGStatus() {
		return gStatus;
	}

	/**
	 * @param status The gStatus to set.
	 */
	public void setGStatus(int status) {
		gStatus = status;
	}
	public int getTotalHouse() {
		return totalHouse;
	}
	public void setTotalHouse(int totalHouse) {
		this.totalHouse = totalHouse;
	}
	public void addTotalHouse() {
		totalHouse++;
	}
	public void addTotalHouseValue(int value) {
		totalHouseValue += value;
	}
	public int getTotalHouseValue() {
		return totalHouseValue;
	}
	public void setTotalHouseValue(int totalHouseValue) {
		this.totalHouseValue = totalHouseValue;
	}
	
}
