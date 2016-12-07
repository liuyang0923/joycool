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
public class TongUserBean {
	public static String[] dutyName = {"弟子","","","","","","","","法王","","帮主"};
	int id;
	int tongId;
	int userId;
	int duty;			// 职位
	long createTime;	// 入帮时间
	public String getDutyName() {
		return dutyName[duty];
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getDuty() {
		return duty;
	}
	public void setDuty(int duty) {
		this.duty = duty;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTongId() {
		return tongId;
	}
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
