package net.joycool.wap.spec.admin;

import java.util.*;

public class UserApplyBean {
	
	public static String[] typeNames = {
		"无",
		"找回登陆密码",
		"找回银行密码",
		"被盗号要求取回",
		"举报骗子/小偷",
		"举报挂机行为/游戏漏洞",
		"投诉版主/监察/管理",
		"举报色情/违法等不良信息",
	};
	public static String[] statusNames = {
		"未处理",
		"处理中",
		"已处理",
	};
	
	int id;
	int userId;		// 申诉人，如果是游客则为0
	int type;		// 申诉类型
	int toId;		// 投诉对方的id，如果不存在则为0
	String content;	// 申诉内容（复杂）
	String bak;		// 管理员备注
	int operator;	// 处理人id
	int status;		// 状态，0标识未处理
	long createTime;	// 创建时间
	long solveTime;		// 解决时间
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOperator() {
		return operator;
	}
	public void setOperator(int operator) {
		this.operator = operator;
	}
	public long getSolveTime() {
		return solveTime;
	}
	public void setSolveTime(long solveTime) {
		this.solveTime = solveTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getToId() {
		return toId;
	}
	public void setToId(int toId) {
		this.toId = toId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeNames[type];
	}
	public String getStatusName() {
		return statusNames[status];
	}
}
