package net.joycool.wap.action.fs;

import net.joycool.wap.util.RandomUtil;

/**
 * @author macq
 * @explain：
 * @datetime:2007-3-27 9:14:34
 */
public class FSUserBagBean {
	int productId;

	int price;

	int count;
	
	String name;

	public FSUserBagBean(){
		
	}
	
	public FSUserBagBean(FSProductBean product) {
		//随机浮动价格
		price = product.getPriceBase()+RandomUtil.nextInt(product.getPriceChange());
		productId = product.getId();
		name = product.getName();
	}

	/**
	 * @return 返回 count。
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 *            要设置的 count。
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return 返回 price。
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            要设置的 price。
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return 返回 productId。
	 */
	public int getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            要设置的 productId。
	 */
	public void setProductId(int productId) {
		this.productId = productId;
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
}
