package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 帮会
 * @datetime:1007-10-24
 */
public class TongBean {
	static int FLAG_NOSTOP = (1 << 0);		// 不可以中途下车
	
	public static String[] flagString = {"禁止下车"};
	public static int FLAG_COUNT = flagString.length;
	
	int id;
	String name;
	String nameWml;
	int group;		// 派系，涉及内功等等
	int count;		// 成员数量
	int rank;		// 帮派级别
	int duty1;		// 帮主
	int duty2;		// 四大法王
	int duty3;
	int duty4;
	int duty5;
	long createTime;
	
	int flag;
	
	
	public boolean isFlag(int is) {
		return (flag & (1 << is)) != 0;
	}
	public boolean isFlagNoStop() {
		return (flag & FLAG_NOSTOP) != 0;
	}


	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDuty1() {
		return duty1;
	}
	public void setDuty1(int duty1) {
		this.duty1 = duty1;
	}
	public int getDuty2() {
		return duty2;
	}
	public void setDuty2(int duty2) {
		this.duty2 = duty2;
	}
	public int getDuty3() {
		return duty3;
	}
	public void setDuty3(int duty3) {
		this.duty3 = duty3;
	}
	public int getDuty4() {
		return duty4;
	}
	public void setDuty4(int duty4) {
		this.duty4 = duty4;
	}
	public int getDuty5() {
		return duty5;
	}
	public void setDuty5(int duty5) {
		this.duty5 = duty5;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		nameWml = StringUtil.toWml(name);
	}
	public String getNameWml() {
		return nameWml;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

}
