package net.joycool.wap.spec.team;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

/**
 * 缘分测试集
 *
 */
public class QuestionSetBean {
	static int FLAG_RELEASE = (1 << 0);			// 已发布
	int id;
	int userId;
	String name;		// 本测试标题
	String info;		// 本测试说明
	long createTime;
	int flag;
	int count;		// 题目数量
	List questionList;
	int replyCount;		// 答题总数
	HashSet replys = null;

	// 这个用户是否已经答题
	public boolean isReply(int userId) {
		return getReplys().contains(Integer.valueOf(userId));
	}
	public HashSet getReplys() {
		if(replys == null) {
			synchronized(this) {
				if(replys == null) {
					replys = new HashSet();
					replys.addAll(SqlUtil.getIntList("select user_id from question_reply where set_id=" + id, 3));
				}
			}
		}
		return replys;
	}
	public void addReply(int userId) {
		getReplys();
		synchronized(replys) {
			replys.add(Integer.valueOf(userId));
		}
	}
	
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public List getQuestionList() {
		return questionList;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public boolean isFlagRelease() {
		return (flag & FLAG_RELEASE) != 0;
	}
	public void setFlagRelease(boolean is) {
		if(is)
			flag |= FLAG_RELEASE;
		else
			flag &= ~FLAG_RELEASE;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setQuestionList(List questionList) {
		this.questionList = questionList;
	}
	// 根据id获得对应的question在数组中第几个
	public int getQuestionPos(int qid) {
		for(int i = 0;i < questionList.size();i++) {
			QuestionBean q = (QuestionBean)questionList.get(i);
			if(q.getId() == qid)
				return i;
		}
		return -1;
	}
}
