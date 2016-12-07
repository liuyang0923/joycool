/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.spec.admin;

/**
 * @author bomb
 * 
 */
public class AdminGroupBean implements Cloneable{
	
	public static String deptNames[] = {"无", "无", "物流库存部", "客服部", "运营中心", "推广部", "商品采购部", "销售部", "平台部"};
	public static String securityLevelNames[] = {"普通用户", "普通用户", "普通用户", "普通用户", "普通用户", "普通管理员", 
		"普通用户", "普通用户", "普通用户", "高级管理员", "超级管理员"};
	
	int id;
	String name;
	String bak;
	
	long flag;		// 参数=权限设置
	long flag2;
	long flag3;
	long flag4;
	long flag5;
	int seq;		// 显示排序

	int dept;		// 部门;
	public String getDeptName() {
		return deptNames[dept];
	}

	int catalog;	// 分类（备用）

	public void setFlags(long[] flags) {
		flag = flags[0];
		flag2 = flags[1];
		flag3 = flags[2];
		flag4 = flags[3];
		flag5 = flags[4];
	}
	public void mergeFlags(AdminGroupBean mg) {
		flag |= mg.getFlag();
		flag2 |= mg.getFlag2();
		flag3 |= mg.getFlag3();
		flag4 |= mg.getFlag4();
		flag5 |= mg.getFlag5();
	}
	
	public boolean isFlag(int f) {
		if(f < 0)
			return false;
		if(f < 60)
			return (flag & (1l << f)) != 0;
		if(f < 120)
			return (flag2 & (1l << (f - 60))) != 0;
		if(f < 180)
			return (flag3 & (1l << (f - 120))) != 0;
		if(f < 240)
			return (flag4 & (1l << (f - 180))) != 0;
		if(f < 300)
			return (flag5 & (1l << (f - 240))) != 0;
		return false;
	}
	
	
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public int getDept() {
		return dept;
	}

	public void setDept(int dept) {
		this.dept = dept;
	}

	public long getFlag() {
		return flag;
	}

	public void setFlag(long flag) {
		this.flag = flag;
	}

	public long getFlag2() {
		return flag2;
	}

	public void setFlag2(long flag2) {
		this.flag2 = flag2;
	}



	public long getFlag3() {
		return flag3;
	}



	public void setFlag3(long flag3) {
		this.flag3 = flag3;
	}



	public long getFlag4() {
		return flag4;
	}



	public void setFlag4(long flag4) {
		this.flag4 = flag4;
	}



	public long getFlag5() {
		return flag5;
	}



	public void setFlag5(long flag5) {
		this.flag5 = flag5;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public AdminGroupBean copy() {
		try {
			return (AdminGroupBean)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
