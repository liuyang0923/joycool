/*
 * Created on 2006-12-9
 *
 */
package net.wxsj.bean;

import java.util.ArrayList;

/**
 * 作者：李北金
 * 
 * 创建日期：2006-12-9
 * 
 * 说明：瑞士军刀问题
 */
public class KnifeQuestionBean {
    int id;

    String question;
    
    ArrayList answerList;

    int answerId;

    /**
     * @return Returns the question.
     */
    public String getQuestion() {
        return question;
    }
    /**
     * @param question The question to set.
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * @return Returns the answerId.
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     * @param answerId
     *            The answerId to set.
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    /**
     * @return Returns the answerList.
     */
    public ArrayList getAnswerList() {
        return answerList;
    }

    /**
     * @param answerList
     *            The answerList to set.
     */
    public void setAnswerList(ArrayList answerList) {
        this.answerList = answerList;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}
