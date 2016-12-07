package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 缘分测试答题
 *
 */
public class QuestionReplyBean {
	int id;
	int setId;
	int userId;			// 答题人
	String answer;		// 每个题目的回答
	int score;			// 得分，满分100
	long createTime;	// 回答的时间
	int current;		// 当前回答的题目，0表示第一题
	List answers;
	// 获得第n+1题回答
	public int getAnswer(int i) {
		if(i >= answers.size())
			return -1;
		return ((Integer)answers.get(i)).intValue();
	}
	// 把答案list转换为string
	public void generateAnswerString() {
		StringBuilder sb = new StringBuilder(64);
		for(int i = 0;i < answers.size();i++) {
			if(i > 0)
				sb.append(',');
			sb.append(answers.get(i));
		}
		answer = sb.toString();
	}
	public void addAnswer(int a) {
		if(answers.size() <= current)
			answers.add(Integer.valueOf(a));
		else
			answers.set(current, Integer.valueOf(a));
		current++;
	}
	public List getAnswers() {
		return answers;
	}
	public void setAnswers(List answers) {
		this.answers = answers;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
		answers = QuestionAction.toInts(answer);
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getSetId() {
		return setId;
	}
	public void setSetId(int setId) {
		this.setId = setId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public void init(int userId, int setId, int size) {
		this.userId = userId;
		this.setId = setId;
		answers = new ArrayList(size);
	}
	
}
