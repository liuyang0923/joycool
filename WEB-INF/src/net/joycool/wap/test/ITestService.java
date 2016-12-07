package net.joycool.wap.test;

import java.util.Vector;
/**
 * fanys 2006-07-06
 * @author Administrator
 *
 */
public interface ITestService {
	//获取一个问题的答案
	public AnswerBean getAnswer(String condition);
	//获取一个问题
	public QuestionBean getQuestion(String condition);
	//获取用户的回答记录
	public RecordBean getRecord(String condition);
	//增加一条用户回答记录
	public boolean addRecord(RecordBean record);
	//获取一个问题所有答案
	public Vector getAnswerList(String condition);
	
	public int getQuestionCount(String condition);
}
