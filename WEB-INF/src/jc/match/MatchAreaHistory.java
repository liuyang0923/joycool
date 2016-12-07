package jc.match;

import net.joycool.wap.util.StringUtil;

/**
 * 分区信息历史
 * @author maning
 */
public class MatchAreaHistory {
	int id;
	int matchId;
	String areaName;
	String citys;
	String describe;
	long createTime;
	String provinces;
	int count;
	int areaId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
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
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getAreaId() {
		return areaId;
	}
	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
}
