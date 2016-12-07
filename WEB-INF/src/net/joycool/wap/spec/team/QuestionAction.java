package net.joycool.wap.spec.team;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.NoticeAction;
import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * 出题答题，缘分测试
 *
 */
public class QuestionAction extends CustomAction{
	public static ICacheMap questionSetCache = CacheManage.questionSet;
	
	UserBean loginUser;
	public static QuestionService service = new QuestionService();

	public QuestionAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		loginUser = super.getLoginUser();
	}

	public QuestionAction(HttpServletRequest request) {
		super(request);
		loginUser = super.getLoginUser();
	}
	
	public static QuestionSetBean getQuestionSet(int id) {
		Integer key = Integer.valueOf(id);
		synchronized(questionSetCache) {
			QuestionSetBean q = (QuestionSetBean)questionSetCache.get(key);
			if(q == null) {
				q = service.getQuestionSet("id=" + id);
				if(q != null) {
					q.setQuestionList(service.getQuestionList("set_id=" + id));
					
					questionSetCache.put(key, q);
				}
			}
			return q;
		}
	}
	public static void addQuestionSet(QuestionSetBean set) {
		service.updateQuestionSet(set, true);
		set.setQuestionList(new ArrayList());
		questionSetCache.spt(Integer.valueOf(set.getId()), set);
	}
	public static void addQuestionReply(QuestionReplyBean reply) {
		if(reply.getId() != 0)	// 已经保存过
			return;
		reply.setCreateTime(System.currentTimeMillis());
		QuestionSetBean set = getQuestionSet(reply.getSetId());
		// 计算得分
		List qs = set.getQuestionList();
		int correct = 0;
		for(int i = 0;i < qs.size();i++) {
			QuestionBean q = (QuestionBean)qs.get(i);
			if(q.getAnswer() == reply.getAnswer(i))
				correct++;
		}
		reply.generateAnswerString();
		reply.setScore(correct);
		service.updateQuestionReply(reply, true);
		set.setReplyCount(set.getReplyCount() + 1);
		set.addReply(reply.getUserId());
		SqlUtil.executeUpdate("update question_set set reply_count=" + set.getReplyCount() + " where id=" + set.getId(), 3);
	}
	public static void addQuestion(QuestionSetBean set, QuestionBean q) {
		service.updateQuestion(q, true);
		set.getQuestionList().add(q);
	}
	// 创建测试
	public void create() {
		if(isMethodGet()) {
			
		} else {
			String name = getParameterNoEnter("name");
			String info = getParameterNoEnter("info");
			if(name == null || name.length() < 2 || StringUtil.getCLength(name) > 40) {
				tip("tip", "标题最少2字最多20字");
				return;
			}
			if(info == null || StringUtil.getCLength(info) > 200) {
				tip("tip", "说明最多100字");
				return;
			}
			QuestionSetBean set = new QuestionSetBean();
			set.setName(name);
			set.setInfo(info);
			set.setCreateTime(System.currentTimeMillis());
			set.setUserId(loginUser.getId());
			addQuestionSet(set);
			setAttribute("id", set.getId());
			tip("create", "");	// 创建成功
		}
	}
	// 增加题目
	public void addQuestion() {
		int setId = getParameterInt("sid");
		QuestionSetBean set = getQuestionSet(setId);
		if(set == null || loginUser.getId() != set.getUserId() || set.isFlagRelease()) {
			tip("tip", "测试集不存在");
			return;
		}
		request.setAttribute("set", set);
		if(isMethodGet()) {
			
		} else {
			String title = getParameterNoEnter("title");
			String options[] = request.getParameterValues("option");
			if(title == null || options == null || options.length < 2) {
				return;
			}
			StringBuilder sb = new StringBuilder(128);
			int i = 0;
			for(;i < options.length;i++) {
				options[i] = StringUtil.noEnter(options[i]);
				if(options[i].length() == 0)
					break;
				if(i > 0)
					sb.append("|");
				sb.append(options[i].replace('|', '/'));
			}
			if(i == 0) {
				return;
			}
			QuestionBean q = new QuestionBean();
			q.setTitle(title);
			q.setContent(sb.toString());
			q.setSetId(set.getId());
			q.setAnswer(getParameterInt("answer"));
			addQuestion(set, q);
			tip("tip", "添加成功");		// 添加成功
		}
	}
	// 删除题目
	public void deleteQuestion() {
		int setId = getParameterInt("sid");
		QuestionSetBean set = getQuestionSet(setId);
		if(set == null || loginUser.getId() != set.getUserId() || set.isFlagRelease()) {
			tip("tip", "测试集不存在");
			return;
		}
		request.setAttribute("set", set);
		int questionId = getParameterInt("qid");
		deleteQuestion(set, questionId);
		tip("tip", "成功删除");
	}
	public static void deleteQuestion(QuestionSetBean set, int questionId) {
		int i = set.getQuestionPos(questionId);
		if(i != -1) {
			QuestionBean q = (QuestionBean)set.getQuestionList().remove(i);
			SqlUtil.executeUpdate("delete from question where id=" + q.getId(), 3);
		}
	}
	// 发布测试
	public void release() {
		int setId = getParameterInt("sid");
		QuestionSetBean set = getQuestionSet(setId);
		if(set == null || loginUser.getId() != set.getUserId() || set.isFlagRelease()) {
			tip("tip", "测试集不存在");
			return;
		}
		if(set.getQuestionList().size() < 3) {
			tip("tip", "至少需要有三道题才能发布");
			return;
		}
		request.setAttribute("set", set);
		set.setFlagRelease(true);
		SqlUtil.executeUpdate("update question_set set flag=" + set.getFlag() + " where id=" + set.getId(), 3);
		tip("tip", "测试集-[" + StringUtil.toWml(set.getName()) + "]成功发布");
		
		
		ActionTrend.addTrend(loginUser.getId(), 
				BeanTrend.TYPE_YUANFENCESHI, "%1发布缘分测试%3", 
				loginUser.getNickName(), set.getName(), 
				"/team/question/set.jsp?sid="+setId);
	}
	// 查看测试集
	public void set() {
		int setId = getParameterInt("sid");
		QuestionSetBean set = getQuestionSet(setId);
		if(set == null) {
			tip("tip", "测试集不存在");
			return;
		}
		request.setAttribute("set", set);
	}
	// 查看题目
	public void setQuestion() {
		int setId = getParameterInt("sid");
		QuestionSetBean set = getQuestionSet(setId);
		if(set == null || loginUser.getId() != set.getUserId()) {
			tip("tip", "测试集不存在");
			return;
		}
		request.setAttribute("set", set);
		int questionId = getParameterInt("qid");
		int i = set.getQuestionPos(questionId);
		if(i == -1) {
			tip("tip", "测试题不存在");
			return;
		}
		setAttribute("question", i);
	}
	
	public void index() {
		
	}
	// 答题
	public void reply() {
		int sid = getParameterInt("sid");	// 要答的测试集id
		QuestionReplyBean reply = (QuestionReplyBean)session.getAttribute("question-reply");	// 从session取得当前答题情况
		if(reply != null && sid != 0 && reply.getSetId() != sid)		// 重新做题或者做另一套题
			reply = null;
		if(reply == null) {			// 没有正在答题的记录，创建记录
			QuestionSetBean set = getQuestionSet(sid);
			if(set == null || set.getUserId() == loginUser.getId() || !set.isFlagRelease() || set.isReply(loginUser.getId())) {
				tip("tip", "不存在的测试集");
				return;
			}
			request.setAttribute("set", set);
			reply = new QuestionReplyBean();
			reply.init(loginUser.getId(), sid, set.getQuestionList().size());
			session.setAttribute("question-reply", reply);
		} else {
			QuestionSetBean set = getQuestionSet(reply.getSetId());
			request.setAttribute("set", set);
			int act = getParameterInt("a");
			switch(act) {
			case 1: {		// 确认答题完毕
				if(reply.getCurrent() >= set.getQuestionList().size()) {
					addQuestionReply(reply);
					ActionTrend.addTrend(loginUser.getId(), 
							BeanTrend.TYPE_YUANFENCESHI, "%1参与了测试%3", 
							loginUser.getNickName(), set.getName(), 
							"/team/question/set.jsp?sid=" + set.getId());
					tip("tip", "完成测试");
					return;
				}
			} break;
			case 2: {		// 重新答题
				reply.setCurrent(0);
			} break;
			case 3: {		// 重新答上一题
				if(reply.getCurrent() > 0)
					reply.setCurrent(reply.getCurrent() - 1);
			} break;
			case 4: {		// 答了一题
				int option = getParameterInt("o");
				reply.addAnswer(option);
			} break;
			}
			
			if(reply.getCurrent() >= set.getQuestionList().size()) {
				tip("confirm", "完成测试");
				return;
			}
		}
	}
	// 查看一个测试集的回答情况
	public void score() {
		
	}
	
	public void vreply() {
		if(hasParam("id")) {		// 通过reply id获得
			int id = getParameterInt("id");
			QuestionReplyBean reply = service.getQuestionReply("id="+id);
			if(reply == null) {
				tip("tip", "不存在的记录");
				return;
			}
			QuestionSetBean set = getQuestionSet(reply.getSetId());
			if(set == null || (loginUser.getId() != set.getUserId() && loginUser.getId() != reply.getUserId())) {
				tip("tip", "不存在的记录");
				return;
			}
			request.setAttribute("reply", reply);
			request.setAttribute("set", set);
		} else {		// 通过set id + userId获得
			int setId = getParameterInt("sid");
			QuestionSetBean set = getQuestionSet(setId);
			QuestionReplyBean reply = service.getQuestionReply("set_id=" + setId + " and user_id=" + loginUser.getId());
			if(set == null || reply == null || (loginUser.getId() != set.getUserId() && loginUser.getId() != reply.getUserId())) {
				tip("tip", "不存在的记录");
				return;
			}
			request.setAttribute("reply", reply);
			request.setAttribute("set", set);
		}
	}
	
	public static String getScoreString(QuestionSetBean set, QuestionReplyBean reply) {
		return formatNumber((float)reply.getScore() * 100 / set.getQuestionList().size());
	}
	
	static DecimalFormat numFormat = new DecimalFormat("0.0");
	public static String formatNumber(float number) {
		return numFormat.format(number);
	}
	
	public static List toInts(String ss) {
		List l = new ArrayList();
		if(ss != null) {
			String[] s = ss.split(",");
			for(int i = 0;i < s.length;i++) {
				int id = StringUtil.toInt(s[i]);
				if(id >= 0)
					l.add(Integer.valueOf(id));
			}
		}
		return l;
	}
}
