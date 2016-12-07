/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.service.impl;

import java.util.ArrayList;

import net.wxsj.bean.stage.AnswerBean;
import net.wxsj.bean.stage.QuestionBean;
import net.wxsj.bean.stage.TryBean;
import net.wxsj.service.infc.IStageService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public class StageServiceImpl extends BaseServiceImpl implements IStageService {
    public StageServiceImpl(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public StageServiceImpl() {
        this.useConnType = CONN_IN_METHOD;
    }
    
    public String questionTableName = "wxsj_question";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addQuestion(QuestionBean bean) {
        return addXXX(bean, questionTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteQuestion(String condition) {
        return deleteXXX(condition, questionTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public QuestionBean getQuestion(String condition) {
        return (QuestionBean) getXXX(condition, questionTableName,
                "net.wxsj.bean.stage.QuestionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getQuestionCount(String condition) {
        return getXXXCount(condition, questionTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getQuestionList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, questionTableName,
                "net.wxsj.bean.stage.QuestionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateQuestion(String set, String condition) {
        return updateXXX(set, condition, questionTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getQuestionList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, questionTableName,
                "net.wxsj.bean.stage.QuestionBean");
    }
    
    public String answerTableName = "wxsj_answer";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addAnswer(AnswerBean bean) {
        return addXXX(bean, answerTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteAnswer(String condition) {
        return deleteXXX(condition, answerTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public AnswerBean getAnswer(String condition) {
        return (AnswerBean) getXXX(condition, answerTableName,
                "net.wxsj.bean.stage.AnswerBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getAnswerCount(String condition) {
        return getXXXCount(condition, answerTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAnswerList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, answerTableName,
                "net.wxsj.bean.stage.AnswerBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateAnswer(String set, String condition) {
        return updateXXX(set, condition, answerTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getAnswerList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, answerTableName,
                "net.wxsj.bean.stage.AnswerBean");
    }
    
    public String tryTableName = "wxsj_try";

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTry(TryBean bean) {
        return addXXX(bean, tryTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTry(String condition) {
        return deleteXXX(condition, tryTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TryBean getTry(String condition) {
        return (TryBean) getXXX(condition, tryTableName,
                "net.wxsj.bean.stage.TryBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTryCount(String condition) {
        return getXXXCount(condition, tryTableName, "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTryList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, tryTableName,
                "net.wxsj.bean.stage.TryBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTry(String set, String condition) {
        return updateXXX(set, condition, tryTableName);
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTryList(String query, String fieldPrefix) {
        return getXXXList(query, fieldPrefix, tryTableName,
                "net.wxsj.bean.stage.TryBean");
    }
}
