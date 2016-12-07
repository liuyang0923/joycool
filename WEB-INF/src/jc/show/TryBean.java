package jc.show;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TryBean {
	int gender;
	int[] maleTry;
	int[] femaleTry;
	LinkedList tryList = new LinkedList();		// 试穿列表
	public static int MAX_TRY_LIST = 50;
	
	public static int[] maleDefaultTry;	// 默认穿着(男)
	public static int[] femaleDefaultTry;
	public static int[] maleDefaultTryData = { 1, 4, 2, 5, 3, 6, 4, 9, 5, 10, 7, 11, };
	public static int[] femaleDefaultTryData = { 1, 1, 2, 2, 3, 3, 4, 7, 5, 8, 7, 11, };
	static {
		CoolShowAction.preparePart();
		maleDefaultTry = new int[CoolShowAction.partCount];
		femaleDefaultTry = new int[CoolShowAction.partCount];
		for(int i = 0;i < maleDefaultTryData.length / 2;i++) {
			int index = CoolShowAction.getPart(maleDefaultTryData[i * 2]).getIndex();
			maleDefaultTry[index] = maleDefaultTryData[i * 2 + 1];
		}
		for(int i = 0;i < femaleDefaultTryData.length / 2;i++) {
			int index = CoolShowAction.getPart(femaleDefaultTryData[i * 2]).getIndex();
			femaleDefaultTry[index] = femaleDefaultTryData[i * 2 + 1];
		}
	}
	
	public TryBean(int gender) {
		this.gender = gender;
		femaleTry = (int[])femaleDefaultTry.clone();
		maleTry = (int[])maleDefaultTry.clone();
	}
	public int[] getCurrentTry() {
		if(gender == 0)
			return femaleTry;
		else
			return maleTry;
	}
	public int[] getDefaultTry() {
		if(gender == 0)
			return femaleDefaultTry;
		else
			return maleDefaultTry;
	}
	// 试穿一件/脱下一件，注意，一件可能带多个part
	public void tryOne(Commodity comm) {
		int index = comm.getIndex();
		int[] t = getCurrentTry();
		if(t[index] == comm.getId()) {	// 脱下
			takeOff(t, comm);
		} else {	// 穿上
			addTryList(comm);
			takeOff(t, index);
			t[index] = comm.getId();
			
			if(comm.getPartOtherCount() > 0) {	// 穿上其他部分
				for(int i = 0;i < comm.getPartOtherCount();i++) {
					int ii = comm.getPartOtherIndex()[i];
					takeOff(t, ii);
					t[ii] = -comm.getId();		// 覆盖位置，保存-id，不用绘制
				}
			}
		}
	}
	// 添加试穿记录
	public void addTryList(Commodity comm) {
		if(!tryList.remove(comm))	// 删除相同的元素
			if(tryList.size() >= MAX_TRY_LIST)		// 最多保存多少个
				tryList.removeLast();
		tryList.addFirst(comm);
	}
	public List getTryList() {
		return new ArrayList(tryList);
	}
	public int getTryCount() {
		return tryList.size();
	}
	// 获得前三个试穿记录
	public List getTryList(int limit) {
		if(tryList.size() < limit)
			return new ArrayList(tryList);
		List list = new ArrayList(limit);
		Iterator iter = tryList.iterator();
		for(int i = 0;i < limit;i++)
			list.add(iter.next());
			
		return list;
	}
	// 脱下某个part
	public void takeOff(int[] t, int part) {
		int item = t[part];
		if(item < 0)
			item = -item;
		if(item > 100) {
			Commodity comm = CoolShowAction.getCommodity(item);
			takeOff(t, comm);
		} else {
			t[part] = getDefaultTry()[part];
		}
	}
	public void takeOff(int[] t, Commodity comm) {
		int index = comm.getIndex();
		t[index] = getDefaultTry()[index];
		if(comm.getPartOtherCount() > 0) {
			for(int i = 0;i < comm.getPartOtherCount();i++) {
				int ii = comm.getPartOtherIndex()[i];
				t[ii] = getDefaultTry()[ii];
			}
		}
	}
	// 脱下相应的物品
	public void takeOff(Commodity comm) {
		int[] t = getCurrentTry();
		takeOff(t, comm);
	}
	// 返回穿着的
	public List getCommodityList() {
		int[] t = getCurrentTry();
		List list = new ArrayList();
		for(int i = 0;i < CoolShowAction.partCount;i++) {
			if(t[i] <= 100)
				continue;
			Commodity comm = CoolShowAction.getCommodity(t[i]);
			list.add(comm);
		}
		return list;
	}
	// 返回需要绘制的物品
	public List getGoodsList() {
		int[] t = getCurrentTry();
		List list = new ArrayList();
		for(int i = 0;i < CoolShowAction.partCount;i++) {
			if(t[i] <= 0)		// 小于0也不需要绘制（覆盖区域，例如套装）
				continue;
			Commodity comm = CoolShowAction.getCommodity(t[i]);
			list.add(comm.getGoodsImg());
		}
		return list;
	}
	// 切换性别
	public void toggleGender() {
		gender = 1 - gender;
	}
	// 全部脱下或者重置
	public void reset() {
		if(gender == 0)
			femaleTry = (int[])femaleDefaultTry.clone();
		else
			maleTry = (int[])maleDefaultTry.clone();
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
}
