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
 * @explain： 商店物品（买入、卖出）
 * @datetime:1007-10-24
 */
public class FarmShopBean {
	static int FLAG_PRICE_CHANGE = (1 << 0);		// 价格会随着买卖变化
	static int FLAG_STACK_CHANGE = (1 << 1);		// 有库存系统
	static int FLAG_MONEY_CHANGE = (1 << 2);		// 这个物品的买卖会影响npc携带资金
	static int FLAG_NO_SELL = (1 << 3);			// 玩家不能卖
	static int FLAG_NO_BUY = (1 << 4);				// 玩家不能买
	static int FLAG_PRICE_RANDOM = (1 << 5);		// 物价存在随机性
	
	public static String[] flagString = {"价格变化", "库存变化", "npc金钱", "禁止收购", "禁止出售", "物价随机"};
	public static int FLAG_COUNT = flagString.length;
	
	int id;
	int npcId;
	int itemId;
	long startTime;		// 上一次购买时间
	int brush;
	int buyPrice;		// npc出售价格（对于用户来说就是购买价格）
	int sellPrice;		// npc回收价格
	int stack;			// 当前库存
	int maxStack;		// 最大库存
	int flag;			// 标志
	
	int defStack;		// 默认库存，每一段时间，当前库存都会向默认库存靠拢
	int defBuyPrice;	// 默认买卖价格，每一段时间，都会根据当前库存计算当前买卖价格
	int defSellPrice;	
	
	
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagPriceChange() {
		return (flag & FLAG_PRICE_CHANGE) != 0;
	}
	public boolean isFlagStackChange() {
		return (flag & FLAG_STACK_CHANGE) != 0;
	}
	public boolean isFlagMoneyChange() {
		return (flag & FLAG_MONEY_CHANGE) != 0;
	}
	public boolean isFlagNoSell() {
		return (flag & FLAG_NO_SELL) != 0;
	}
	public boolean isFlagNoBuy() {
		return (flag & FLAG_NO_BUY) != 0;
	}
	public boolean isFlagPriceRandom() {
		return (flag & FLAG_PRICE_RANDOM) != 0;
	}
	
	// 判断是否可以增加或者减少库存
	public boolean canIncStack(int change) {
		if(isFlagStackChange()) {
			return change <= maxStack - stack;
		}
		return true;
	}
	public boolean canDecStack(int change) {
		if(isFlagStackChange()) {
			return change <= stack;
		}
		return true;
	}
	public void addStack(int change) {
		stack += change;
		if(stack < 0)
			stack = 0;
		else if(stack > maxStack)
			stack = maxStack;
	}
	
	/**
	 * @return Returns the brush.
	 */
	public int getBrush() {
		return brush;
	}
	/**
	 * @param brush The brush to set.
	 */
	public void setBrush(int brush) {
		this.brush = brush;
	}
	/**
	 * @return Returns the buyPrice.
	 */
	public int getBuyPrice() {
		return buyPrice;
	}
	/**
	 * @param buyPrice The buyPrice to set.
	 */
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
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
	 * @return Returns the itemId.
	 */
	public int getItemId() {
		return itemId;
	}
	/**
	 * @param itemId The itemId to set.
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return Returns the maxStack.
	 */
	public int getMaxStack() {
		return maxStack;
	}
	/**
	 * @param maxStack The maxStack to set.
	 */
	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
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
	 * @return Returns the sellPrice.
	 */
	public int getSellPrice() {
		return sellPrice;
	}
	/**
	 * @param sellPrice The sellPrice to set.
	 */
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	/**
	 * @return Returns the stack.
	 */
	public int getStack() {
		return stack;
	}
	/**
	 * @param stack The stack to set.
	 */
	public void setStack(int stack) {
		this.stack = stack;
	}
	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return Returns the defBuyPrice.
	 */
	public int getDefBuyPrice() {
		return defBuyPrice;
	}
	/**
	 * @param defBuyPrice The defBuyPrice to set.
	 */
	public void setDefBuyPrice(int defBuyPrice) {
		this.defBuyPrice = defBuyPrice;
	}
	/**
	 * @return Returns the defSellPrice.
	 */
	public int getDefSellPrice() {
		return defSellPrice;
	}
	/**
	 * @param defSellPrice The defSellPrice to set.
	 */
	public void setDefSellPrice(int defSellPrice) {
		this.defSellPrice = defSellPrice;
	}
	/**
	 * @return Returns the defStack.
	 */
	public int getDefStack() {
		return defStack;
	}
	/**
	 * @param defStack The defStack to set.
	 */
	public void setDefStack(int defStack) {
		this.defStack = defStack;
	}
	
	static float randomFloat1 = 1.2f;
	static float randomFloat2 = 0.8f;
	// 一段时间后更新价格、库存
	public void updatePriceStack() {
		if(isFlagPriceChange()) {
			int change = maxStack / 20;
			int dif = stack - defStack;
			float rate = (float)dif / maxStack;
			if(dif > change) {	// 库存太多，降低收购价
				sellPrice = Math.round(defSellPrice * (1 - rate));
				buyPrice = defBuyPrice;
			} else if(dif < -change) {	// 库存太少，增加出售价
				buyPrice = Math.round(defBuyPrice * (1 - rate));
				sellPrice = defSellPrice;
			} else {
				buyPrice = defBuyPrice;
				sellPrice = defSellPrice;
			}
			// 一定几率自发造成价格变化
			if(isFlagPriceRandom()) {
				int rand = RandomUtil.nextInt(20);
				switch(rand) {
				case 0:
					sellPrice = Math.round(sellPrice * randomFloat1);
					buyPrice = Math.round(buyPrice * randomFloat1);
					break;
				case 1:
					sellPrice = Math.round(sellPrice * randomFloat2);
					buyPrice = Math.round(buyPrice * randomFloat2);
					break;
				}
			}
		}
		if(isFlagStackChange()) {
			int change = maxStack / 10;		// 每次变化量是最大值的1/10
			int dif = stack - defStack;
			if(dif > change)
				stack -= change;
			else if(dif < -change)
				stack += change;
		}
	}
	
}
