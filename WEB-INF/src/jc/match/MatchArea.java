package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 地区信息
 * @author Administrator
 *
 */
public class MatchArea {
	int id;
	String areaName;	// 地区名
	String citys;		// 所包含的城市ID。以“,”号分割
	String describe;	// 地区描述
	long createTime;
	String provinces;	// 所包含的省份ID。以“,”号分割
	int count;			// 地区总人数
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAreaName() {
		return areaName;
	}
	public String getAreaNameWml() {
		return StringUtil.toWml(areaName);
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public void setAreaNameSql(String areaName) {
		this.areaName = StringUtil.toSql(areaName);
	}
	public String getCitys() {
		return citys;
	}
	public void setCitys(String citys) {
		this.citys = citys;
	}
	public String getDescribe() {
		return describe;
	}
	public String getDescribeWml() {
		return StringUtil.toWml(describe);
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public void setDescribeSql(String describe) {
		this.describe = StringUtil.toSql(describe);
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getProvinces() {
		return provinces;
	}
	public String getProvincesWml() {
		return StringUtil.toWml(provinces);
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
