package net.joycool.wap.spec.garden;

import java.util.ArrayList;
import java.util.List;

import net.joycool.wap.util.StringUtil;

public class GardenSeedBean {

	int id;
	String name;
	int price;
	int quarter;
	int type;
	int level;
	String info;
	int count;
	String growTime;
	String grown;
	int value;
	int exp;
	
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getQuarterTime(int quarter){
		List list = StringUtil.toInts(growTime);
		Integer ii = (Integer)list.get(quarter-1);
		return ii.intValue();
	}
	
	public static String[] grownStr = {"","种子","发芽","小叶","大叶","开花","成熟"};
	
	public static String getGrownStr(int i,int seedId){
		return grownStr[i];
	}
	
	public int getGrownState(int sec){
		int state = 0;
		int hour = sec / 3600;
		List list = getGrownList();
		int count = 0;
		//if(q == 1) {
			for(int i = 0;i<list.size();i++){
				Integer ii = (Integer)list.get(i);
				count += ii.intValue();
				if(count > hour) {
					state = i + 1;
					for(int j = i;j<list.size();j++) {
						Integer jj = (Integer)list.get(j);
						if(jj.intValue() == 0) {
							state++;
						} else {
							break;
						}
					}
					break;
				}
			}
//		} else {
//			for(int i = 3;i<list.size();i++){
//				Integer ii = (Integer)list.get(i);
//				count += ii.intValue();
//				if(count > hour) {
//					state = i + 1;
//					for(int j = i;j<list.size();j++) {
//						Integer jj = (Integer)list.get(j);
//						if(jj.intValue() == 0) {
//							state++;
//						} else {
//							break;
//						}
//					}
//					break;
//				}
//			}
//		}
		return state;
	}
	
	public List getGrownList(){
		return toInts(grown);
	}
	
	public static List toInts(String ss) {
		List l = new ArrayList();
		if(ss != null) {
			String[] s = ss.split(",");
			for(int i = 0;i < s.length;i++) {
				int id = StringUtil.toInt(s[i]);
				l.add(Integer.valueOf(id));
			}
		}
		return l;
	}
	
	public String getGrowTime() {
		return growTime;
	}
	public void setGrowTime(String growTime) {
		this.growTime = growTime;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getGrown() {
		return grown;
	}

	public void setGrown(String grown) {
		this.grown = grown;
	}
}
