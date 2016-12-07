/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.wc.WcAnswerBean;
import net.joycool.wap.bean.wc.WcAnswerRecordBean;
import net.joycool.wap.bean.wc.WcQuestionBean;

/**
 * @author lbj
 *
 */
public interface IWorldCupService {
    public WcQuestionBean getQuestion(String condition);
    public boolean addQuestion(WcQuestionBean question);
    public Vector getQuestionList(String condition);
    public int getQuestionCount(String condition);
    public boolean updateQuestion(String set, String condition);
    public boolean deleteQuestion(String condition);
    
    public WcAnswerBean getAnswer(String condition);
    public boolean addAnswer(WcAnswerBean answer);
    public Vector getAnswerList(String condition);
    public int getAnswerCount(String condition);
    public boolean updateAnswer(String set, String condition);
    public boolean deleteAnswer(String condition);
    
    public WcAnswerRecordBean getAnswerRecord(String condition);
    public boolean addAnswerRecord(WcAnswerRecordBean answerRecord);
    public Vector getAnswerRecordList(String condition);
    public int getAnswerRecordCount(String condition);
    public boolean updateAnswerRecord(String set, String condition);
    public boolean deleteAnswerRecord(String condition);
}
