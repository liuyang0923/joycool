package jc.family.game.yard;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 工厂车间原型
 * @author maning
 */
public class YardPlantProtoBean {
	int id;
	String name;
	int price;			// 此车间的价格
	String describe;	// 车间描述
	String canPlants;	// 可供此车间使用的原料ID，以逗号分隔。
	int type;			// 类型。留用。
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getCanPlants() {
		return canPlants;
	}
	/**
	 * 把可使用的源料全拆成List
	 * @return
	 */
	public List getCanPlantsList(){
		return Arrays.asList(canPlants.split(","));
	}
	/**
	 * 把可使用的源料拆成HashSet
	 * @return
	 */
	public HashSet getCanPlantsSet(){
		return new HashSet(getCanPlantsList());
	}
	public void setCanPlants(String canPlants) {
		this.canPlants = canPlants;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
