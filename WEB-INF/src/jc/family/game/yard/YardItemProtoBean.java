package jc.family.game.yard;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 物品原型
 * 
 * @author qiuranke
 * 
 */
public class YardItemProtoBean {

	/**
	 * 是否能卖
	 */
	static final int IS_SELL = (1 << 0);
	/**
	 * 是否能买
	 */
	static final int IS_BUY = (1 << 1);

	int id;
	String name;
	int price;
	int flags;
	int type;// 0种子,1食材,2调味品,3成品
	String product;
	int buyCount;
	int rank;
	int capacity;// 上限
	int basePrice;// 基价
	int time;// 生成时间

	List productList = new ArrayList();
	int[] capacityArray;

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
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

	public String getNameWml() {
		return StringUtil.toWml(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
		makeProductlList();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isSell() {
		return (flags & IS_SELL) != 0;
	}

	public boolean isBuy() {
		return (flags & IS_BUY) != 0;
	}

	/**
	 * 生成一个List包含所有的材料id
	 */
	public void makeProductlList() {
		productList.clear();
		String[] ms = product.split(",");
		for (int i = 0; i < ms.length; i++) {
			String[] ms2 = ms[i].split("-");
			int[] mat = new int[2];
			mat[0] = StringUtil.toInt(ms2[0]);
			if (ms2.length == 2) {
				mat[1] = StringUtil.toInt(ms2[1]);
				if (mat[1] <= 0)
					mat[1] = 1;
			} else {
				mat[1] = 1;
			}

			if (mat[0] > 0)
				productList.add(mat);
		}

		productRate = new int[productList.size()];
		for (int i = 0; i < productList.size(); i++) {
			int[] r = (int[]) productList.get(i);
			productRate[i] = r[1];
		}

		productRandomTotal = RandomUtil.sumRate(productRate);
	}

	public List getProductList() {
		return productList;
	}

	public void setProductList(List productList) {
		this.productList = productList;
	}

	public int productRandomTotal;
	int[] productRate;

	public int getRandomProduct() {
		int index = RandomUtil.randomRateInt(productRate, productRandomTotal);
		int[] r = (int[]) productList.get(index);
		return r[0];
	}
}
