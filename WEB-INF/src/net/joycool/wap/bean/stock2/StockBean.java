package net.joycool.wap.bean.stock2;

import java.sql.Timestamp;
import java.util.Calendar;

import net.joycool.wap.action.stock2.StockAction;
import net.joycool.wap.util.StringUtil;

/**
 * @author macq
 * @explain： 个股信息
 * @datetime:2007-4-25 14:37:46
 */
public class StockBean {
	static String[] statusName = { "正常交易", "新股上市", "停牌", "退市" };

	public static int STATUS_NORMAL = 0;

	public static int STATUS_NEW = 1;

	public static int STATUS_STOP = 2;
	public static int STATUS_OFF = 3;

	static String[] typeName = { "栏目", "帮会", "游戏" };

	int id;

	String name; // 股票名称

	String code; // 股票代码

	float price; // 股票价格

	float endPrice; // 股票收盘价格

	float startPrice; // 股票发行价

	long count; // 股票当日成交量

	long totalCount; // 发行量

	String desc; // 股票介绍

	String url; // 栏目链接

	int status; // 股票状态 0 普通 1 新股 2 停牌

	int type; // 栏目股、帮会股

	long withdraw; // 系统回收用户持有股票

	// 股票创建时间
	Timestamp createDatetime;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(float endPrice) {
		this.endPrice = endPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMark() {
		if (status == 1)
			return "*";
		if (status == 2)
			return "x";
		float change = price - endPrice;
		if (change > 0)
			return "↑";
		else if (change < 0)
			return "↓";
		else
			return "#";
	}

	public String getChangePercent() {
		return StringUtil.numberFormat((price - endPrice) / endPrice * 100)
				+ "%";
	}

	/**
	 * @return Returns the startPrice.
	 */
	public float getStartPrice() {
		return startPrice;
	}

	/**
	 * @param startPrice
	 *            The startPrice to set.
	 */
	public void setStartPrice(float startPrice) {
		this.startPrice = startPrice;
	}

	/**
	 * @return Returns the totalCount.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            The totalCount to set.
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public float getMaxPrice() {
		return endPrice * StockAction.MAX_STOCK_PRICE_RATE;
	}

	public float getMinPrice() {
		return endPrice * StockAction.MIN_STOCK_PRICE_RATE;
	}

	/**
	 * @return Returns the status.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		if (status >= 0 && status < statusName.length)
			return statusName[status];
		else
			return "无";
	}

	/**
	 * @return 是否开市
	 */
	public static boolean isOpen() {
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		return currentHour > 10 && currentHour < 23;
	}

	public boolean isStatusStop() {
		return status == STATUS_STOP;
	}

	public boolean isStatusNew() {
		return status == STATUS_NEW;
	}
	public boolean isStatusOff() {
		return status == STATUS_OFF;
	}

	/**
	 * 新股申购的最大买入数量
	 */
	public long getNewCount() {
		return totalCount / 100;
	}

	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName[type];
	}

	static public String getTypeName(int type) {
		return typeName[type];
	}

	/**
	 * @return 返回 withdraw。
	 */
	public long getWithdraw() {
		return withdraw;
	}

	/**
	 * @param withdraw
	 *            要设置的 withdraw。
	 */
	public void setWithdraw(long withdraw) {
		this.withdraw = withdraw;
	}
}
