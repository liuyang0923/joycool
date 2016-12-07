/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.service.infc;

import java.util.ArrayList;

import net.wxsj.bean.stage.AnswerBean;
import net.wxsj.bean.stage.QuestionBean;
import net.wxsj.bean.stage.TryBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public interface IStageService extends IBaseService {
    //  question
    public boolean addQuestion(QuestionBean bean);

    public boolean updateQuestion(String set, String condition);

    public boolean deleteQuestion(String condition);

    public int getQuestionCount(String condition);

    public QuestionBean getQuestion(String condition);

    public ArrayList getQuestionList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getQuestionList(String query, String fieldPrefix);

    // answer
    public boolean addAnswer(AnswerBean bean);

    public boolean updateAnswer(String set, String condition);

    public boolean deleteAnswer(String condition);

    public int getAnswerCount(String condition);

    public AnswerBean getAnswer(String condition);

    public ArrayList getAnswerList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getAnswerList(String query, String fieldPrefix);
  
    // try
    public boolean addTry(TryBean bean);

    public boolean updateTry(String set, String condition);

    public boolean deleteTry(String condition);

    public int getTryCount(String condition);

    public TryBean getTry(String condition);

    public ArrayList getTryList(String condition, int index, int count,
            String orderBy);
    
    public ArrayList getTryList(String query, String fieldPrefix);
     
}
