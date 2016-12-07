package net.joycool.wap.action.fs;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-26 15:45:35
 */
public class FSProductBean {
	int id;

	String name;

	int priceBase;

	int priceChange;

	int probability;	// 出现概率

	public FSProductBean() {
		
	}
	
	public FSProductBean(FSProductBean product) {
		this.id = product.id;
		this.name = product.name;
		this.priceBase = product.priceBase;
		this.priceChange = product.priceChange;
		this.probability = product.probability;
	}
	
	/**
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id。
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
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 priceBase。
	 */
	public int getPriceBase() {
		return priceBase;
	}

	/**
	 * @param priceBase
	 *            要设置的 priceBase。
	 */
	public void setPriceBase(int priceBase) {
		this.priceBase = priceBase;
	}

	/**
	 * @return 返回 priceChange。
	 */
	public int getPriceChange() {
		return priceChange;
	}

	/**
	 * @param priceChange
	 *            要设置的 priceChange。
	 */
	public void setPriceChange(int priceChange) {
		this.priceChange = priceChange;
	}

	/**
	 * @return 返回 probability。
	 */
	public int getProbability() {
		return probability;
	}

	/**
	 * @param probability
	 *            要设置的 probability。
	 */
	public void setProbability(int probability) {
		this.probability = probability;
	}
}
