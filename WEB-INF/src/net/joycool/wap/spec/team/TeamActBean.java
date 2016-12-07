package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 *  聊天动作
 *
 */
public class TeamActBean {
	int id;
	String name;	// 英文简写
	String name2;	// 中文简写
	String content;	// 动作内容，包括%1 = 发送方名
	String content2;	// 动作内容，包括%1 = 发送方名，%2=目标名，用于选择目标的动作
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
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
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}

}
