package net.joycool.wap.action.dhh;

public class UserBagBean {

	/**
	 * 对应表的物品id
	 */
	private int productid;
	
	/**
	 * 对应物品的名称
	 */
	private String productname;
	
	/**
	 * 对应物品的买入价
	 */
	private int buyrate;
	
	/**
	 * 对应物品的数量
	 */
	private int number;

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
	 * @return 返回 number。
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number 要设置的 number。
	 */
	public void setNumber(int number) {
		this.number = number;
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
	


}
