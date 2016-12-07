package net.joycool.wap.action.dhh;

/**2007.4.20
 * @author Liq
 *大航海的城市物品表
 */
public class DhhCitProBean {

	/**
	 * 对应表表的id
	 */
	private int id;
	
	/**
	 * 对应表的城市id
	 */
	private int cityid;
	
	/**
	 * 对应表的物品id
	 */
	private int productid;
	
	/**
	 * 对应表的物品的名称
	 */
	private String productname;
	
	/**
	 * 对应表的买入价
	 */
	private int buyrate;
	
	/**
	 * 对应表的卖出价
	 */
	private int sellrate;
	
	/**
	 * 对应表的物品基础数量
	 */
	private int quantity;
	
	int buyInit;	// 初始的买卖价格
	int sellInit;
	int quantityInit;

	/**
	 * @return 返回 buyrate。
	 */
	public int getBuyrate() {
		return buyrate;
	}

	/**
	 * @param buyrate 要设置的 buyrate。
	 */
	public void setBuyrate(int buyrate) {
		this.buyrate = buyrate;
	}

	/**
	 * @return 返回 cityid。
	 */
	public int getCityid() {
		return cityid;
	}

	/**
	 * @param cityid 要设置的 cityid。
	 */
	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 productid。
	 */
	public int getProductid() {
		return productid;
	}

	/**
	 * @param productid 要设置的 productid。
	 */
	public void setProductid(int productid) {
		this.productid = productid;
	}

	/**
	 * @return 返回 quantity。
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity 要设置的 quantity。
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
		if(quantity < 0)
			quantity = 0;
	}

	/**
	 * @return 返回 sellrate。
	 */
	public int getSellrate() {
		return sellrate;
	}

	/**
	 * @param sellrate 要设置的 sellrate。
	 */
	public void setSellrate(int sellrate) {
		this.sellrate = sellrate;
	}

	/**
	 * @return 返回 productname。
	 */
	public String getProductname() {
		return productname;
	}

	/**
	 * @param productname 要设置的 productname。
	 */
	public void setProductname(String productname) {
		this.productname = productname;
	}

	/**
	 * @return Returns the buyInit.
	 */
	public int getBuyInit() {
		return buyInit;
	}

	/**
	 * @param buyInit The buyInit to set.
	 */
	public void setBuyInit(int buyInit) {
		this.buyInit = buyInit;
	}

	/**
	 * @return Returns the sellInit.
	 */
	public int getSellInit() {
		return sellInit;
	}

	/**
	 * @param sellInit The sellInit to set.
	 */
	public void setSellInit(int sellInit) {
		this.sellInit = sellInit;
	}

	/**
	 * @return Returns the quantityInit.
	 */
	public int getQuantityInit() {
		return quantityInit;
	}

	/**
	 * @param quantityInit The quantityInit to set.
	 */
	public void setQuantityInit(int quantityInit) {
		this.quantityInit = quantityInit;
	}

	public void reset() {
		buyrate = buyInit;
		sellrate = sellInit;
		quantity = quantityInit;
	}
	
	public void resetQuantity() {
		
	}

	public void refresh() {
		if(quantity == quantityInit || quantityInit == 0)	// 初始数量不能为0！
			return;
		
		// 根据数量来计算下次价格的涨跌幅
		float rate = ((float)quantity) / quantityInit;
		rate = (rate - 1) / 2 + 1;		// 把涨跌幅度减半，作用于价格
		
		if(rate > 1.5f)		// 限制涨跌幅度
			rate = 1.5f;
		if(rate < 0.7f)
			rate = 0.7f;
		
		buyrate = (int) (buyInit / rate);
		sellrate = (int) (sellInit / rate);
		// 重置数量
		quantity = quantityInit;
	}
}
