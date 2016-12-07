package jc.family.game.yard;

import java.util.ArrayList;
import java.util.List;
import net.joycool.wap.util.StringUtil;

/**
 * 食谱
 * 
 * @author qiuranke
 * 
 */
public class YardRecipeProtoBean {

	int id;
	String name;
	String stuff;// 食材
	String describe;// 描述
	int time;
	int price;
	int rank;// 物品等级
	String material;// 材料
	String product;// 产品
	int type; // 类型.0:初级菜谱 1:中级菜谱 5:工厂用说明书

	List materialList = new ArrayList();
	List productList = new ArrayList();

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

	public String getStuff() {
		return stuff;
	}

	public void setStuff(String stuff) {
		this.stuff = stuff;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
		makeMaterialList();
	}

	/**
	 * 生成一个set包含所有的材料id
	 */
	public void makeMaterialList() {
		materialList.clear();
		String[] ms = material.split(",");
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
				materialList.add(mat);
		}
	}

	public List getMaterialList() {
		return materialList;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
		makeProductList();
	}

	/**
	 * 生成一个list包含所有成品的id
	 */
	public void makeProductList() {
		productList.clear();
		String[] ms = product.split(",");
		for (int i = 0; i < ms.length; i++) {
			int matId = StringUtil.toInt(ms[i]);
			if (matId > 0)
				productList.add(Integer.valueOf(matId));
		}
	}

	public List getProductList() {
		return productList;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 是否在原材料里,如果在返回需要的数量,不在返回1
	 * 
	 * @param itemId
	 * @return
	 */
	public int isinside(int itemId) {
		for (int i = 0; i < materialList.size(); i++) {
			int[] mat = (int[]) materialList.get(i);
			if (mat[0] == itemId) {
				return mat[1];
			}
		}
		return 1;
	}

}
