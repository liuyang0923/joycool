package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 任务
 * @datetime:1007-10-24
 */
public class FarmQuestBean {
	static int FLAG_REDO = (1 << 0);			// 可重复完成
	static int FLAG_TIME_REDO = (1 << 1);		// 可以定期重复完成
	static int FLAG_HIDE_BEGIN = (1 << 2);		// 隐藏开始，用于聊天中开始任务
	static int FLAG_HIDE_END = (1 << 3);		// 隐藏结束，用于聊天中结束任务
	static int FLAG_HIDE_PRIZE = (1 << 4);		// 隐藏结束，用于聊天中结束任务
	static int FLAG_HIDE_HUNTER = (1 << 5);	// 隐藏任务目标
	static int FLAG_NO_ABORT = (1 << 6);		// 无法放弃
	static int FLAG_CLOSED = (1 << 7);			// 关闭
	
	public static String[] flagString = {"重复完成", "定期重复", "隐藏开始", "隐藏结束", "未知奖励", "未知目标", "无法放弃", "关闭"};
	public static int FLAG_COUNT = flagString.length;		// 使用的flag位数
	
	int id;
	int rank;
	int price;		// 任务需要的铜板
	int flag;		// 第一位表示这个任务是否可以重复完成
	long interval;	// 定期重复完成的间隔
	String name;
	String info;	// 任务描述
	String endInfo;	// 任务提交的描述
	String objective;	// 任务目标
	String requestInfo;		// 任务还未完成的提示
	int next;		// 后续任务
	
	String give;		// 接受任务就给的物品，如果已经拥有则不给
	String material;	// 材料
	String product;		// 产品
	String prize;		// 其他奖励
	String preQuestId;	// 先修任务的id列表字符串
	
	List giveList;
	List materialList;
	List productList;
	List preQuestList;
	List prizeList;
	
	String creature;	// 狩猎任务，格式：怪物id-数量
	List creatureList;
	
	String talk;	// 聊天任务
	List talkList;
	
	String search;	// 搜查任务
	List searchList;
	
	String mutex = "";	// 互斥
	List mutexList;
	
	String condition = "";		// 完成任务的条件
	List conditionList;
	String preCondition = "";		// 做任务的条件
	List preConditionList;
	
	static int[] materialDef = {0, 1};
	static int[] productDef = {0, 1};
	static int[] prizeDef = {0, 0};
	
	// 根据数据库读入的内容初始化
	public void init() {
		giveList = StringUtil.toIntss(give, 2, materialDef);
		materialList = StringUtil.toIntss(material, 2, materialDef);
		productList = StringUtil.toIntss(product, 2, productDef);
		preQuestList = StringUtil.toInts(preQuestId);
		prizeList = StringUtil.toIntss(prize);
		creatureList = StringUtil.toIntss(creature);
		talkList = StringUtil.toIntss(talk);
		searchList = StringUtil.toIntss(search);
		mutexList = StringUtil.toInts(mutex);
		conditionList = StringUtil.toIntss(condition);
		preConditionList = StringUtil.toIntss(preCondition);
	}

	/**
	 * @return Returns the cost.
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param cost The cost to set.
	 */
	public void setPrice(int price) {
		this.price = price;
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
	 * @return Returns the info.
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info The info to set.
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return Returns the material.
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * @param material The material to set.
	 */
	public void setMaterial(String material) {
		this.material = material;
	}

	/**
	 * @return Returns the materialList.
	 */
	public List getMaterialList() {
		return materialList;
	}

	/**
	 * @param materialList The materialList to set.
	 */
	public void setMaterialList(List materialList) {
		this.materialList = materialList;
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
	 * @return Returns the product.
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product The product to set.
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return Returns the productList.
	 */
	public List getProductList() {
		return productList;
	}

	/**
	 * @param productList The productList to set.
	 */
	public void setProductList(List productList) {
		this.productList = productList;
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

	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}
	
	public boolean isFlagRedo() {
		return (flag & FLAG_REDO) != 0;
	}
	public boolean isFlagTimeRedo() {
		return (flag & FLAG_TIME_REDO) != 0;
	}
	public boolean isFlagHideBegin() {
		return (flag & FLAG_HIDE_BEGIN) != 0;
	}
	public boolean isFlagHideEnd() {
		return (flag & FLAG_HIDE_END) != 0;
	}
	public boolean isFlagHidePrize() {
		return (flag & FLAG_HIDE_PRIZE) != 0;
	}
	public boolean isFlagHideHunter() {
		return (flag & FLAG_HIDE_HUNTER) != 0;
	}
	public boolean isFlagNoAbort() {
		return (flag & FLAG_NO_ABORT) != 0;
	}
	public boolean isFlagClosed() {
		return (flag & FLAG_CLOSED) != 0;
	}

	/**
	 * @return Returns the endInfo.
	 */
	public String getEndInfo() {
		return endInfo;
	}

	/**
	 * @param endInfo The endInfo to set.
	 */
	public void setEndInfo(String endInfo) {
		this.endInfo = endInfo;
	}

	/**
	 * @return Returns the preQuestId.
	 */
	public String getPreQuestId() {
		return preQuestId;
	}

	/**
	 * @param preQuestId The preQuestId to set.
	 */
	public void setPreQuestId(String preQuestId) {
		this.preQuestId = preQuestId;
	}

	/**
	 * @return Returns the preQuestList.
	 */
	public List getPreQuestList() {
		return preQuestList;
	}

	/**
	 * @param preQuestList The preQuestList to set.
	 */
	public void setPreQuestList(List preQuestList) {
		this.preQuestList = preQuestList;
	}

	/**
	 * @return Returns the prizeList.
	 */
	public List getPrizeList() {
		return prizeList;
	}

	/**
	 * @param prizeList The prizeList to set.
	 */
	public void setPrizeList(List prizeList) {
		this.prizeList = prizeList;
	}

	/**
	 * @return Returns the interval.
	 */
	public long getInterval() {
		return interval;
	}
	public int getIntervalMinute() {
		return (int)(interval / 60000);
	}

	/**
	 * @param interval The interval to set.
	 */
	public void setInterval(int interval) {
		this.interval = interval * 60000;
	}

	public String getCreature() {
		return creature;
	}

	public void setCreature(String creature) {
		this.creature = creature;
	}

	public List getCreatureList() {
		return creatureList;
	}

	public void setCreatureList(List creatureList) {
		this.creatureList = creatureList;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List getSearchList() {
		return searchList;
	}

	public void setSearchList(List searchList) {
		this.searchList = searchList;
	}

	public String getTalk() {
		return talk;
	}

	public void setTalk(String talk) {
		this.talk = talk;
	}

	public List getTalkList() {
		return talkList;
	}

	public void setTalkList(List talkList) {
		this.talkList = talkList;
	}

	public String getGive() {
		return give;
	}

	public void setGive(String give) {
		this.give = give;
	}

	public List getGiveList() {
		return giveList;
	}

	public void setGiveList(List giveList) {
		this.giveList = giveList;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(String requestInfo) {
		this.requestInfo = requestInfo;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public String getMutex() {
		return mutex;
	}

	public void setMutex(String mutex) {
		this.mutex = mutex;
	}

	public List getMutexList() {
		return mutexList;
	}

	public void setMutexList(List mutexList) {
		this.mutexList = mutexList;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List getConditionList() {
		return conditionList;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	public List getPreConditionList() {
		return preConditionList;
	}
}
