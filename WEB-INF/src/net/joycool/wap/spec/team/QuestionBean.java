package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 缘分测试题目
 *
 */
public class QuestionBean {
	
	int id;
	int setId;
	String title;		// 题目
	String content;		// 可选答案，用|分割
	int answer;
	int flag;
	String[] options;		// 选项
	public char getAnswerChar() {
		return (char)('A' + answer);
	}
	// 返回对应选项的答案字符串
	public String getAnswerOption(int o) {
		if(o < 0 || o >= options.length)
			return "(无)";
		else
			return StringUtil.toWml(options[o]);
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		options = content.split("\\|");
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
	public int getSetId() {
		return setId;
	}
	public void setSetId(int setId) {
		this.setId = setId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String[] getOptions() {
		return options;
	}
	public void setOptions(String[] options) {
		this.options = options;
	}
}
