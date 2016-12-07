package net.joycool.wap.action.dhh;

/**2007.4.20
 * @author Liq
 *大航海的wupinbean
 */
public class DhhProductBean {

	/**
	 * 物品表的id
	 */
	private int id;
	
	/**
	 * 物品表的卖基价
	 */
	private int sell;
	
	/**
	 * 物品的名称
	 */
	private String name;

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
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 sell。
	 */
	public int getSell() {
		return sell;
	}

	/**
	 * @param sell 要设置的 sell。
	 */
	public void setSell(int sell) {
		this.sell = sell;
	}
	
}
