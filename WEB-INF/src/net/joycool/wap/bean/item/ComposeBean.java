package net.joycool.wap.bean.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.joycool.wap.util.StringUtil;

/**
 * @author bomb
 * 物品合成公式，用于各种合成
 */

public class ComposeBean {

	int id;
	int itemId;		// 保存应该以这个为key
	String material;
	String product;
	int rank;		// 等级限制
	int flag;		// 其他参数，例如只有帮主能合成
	int fail;		// 合成失败的百分比几率
	int price;		// 需要消耗的乐币/10000，也就是铜板
	
	List materialList = new ArrayList();
	List productList = new ArrayList();
	
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
		makeMaterialList();
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
		makeProductList();
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
	
	/**
	 * 生成一个set包含所有的材料id
	 */
	public void makeMaterialList() {
		materialList.clear();
		String[] ms = material.split(",");
		for(int i = 0;i < ms.length;i++) {
			String[] ms2 = ms[i].split("-");
			int[] mat = new int[2];
			mat[0] = StringUtil.toInt(ms2[0]);
			if(ms2.length == 2) {
				mat[1] = StringUtil.toInt(ms2[1]);
				if(mat[1] <= 0)
					mat[1] = 1;
			} else {
				mat[1] = 1;
			}
			
			if(mat[0] > 0)
				materialList.add(mat);
		}
	}
	
	/**
	 * 生成一个list包含所有成品的id
	 */
	public void makeProductList() {
		productList.clear();
		String[] ms = product.split(",");
		for(int i = 0;i < ms.length;i++) {
			int matId = StringUtil.toInt(ms[i]); 
			if(matId > 0)
				productList.add(Integer.valueOf(matId));
		}
	}
	
	/**
	 * @return 复制后返回给应用，以免被修改
	 */
	public List getMaterialList() {
		return materialList;
	}
	
	public List getProductList() {
		return productList;
	}
	/**
	 * @return Returns the fail.
	 */
	public int getFail() {
		return fail;
	}
	/**
	 * @param fail The fail to set.
	 */
	public void setFail(int fail) {
		this.fail = fail;
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
}
