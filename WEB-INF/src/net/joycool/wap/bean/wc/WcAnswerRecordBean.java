/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.bean.wc;

/**
 * @author lbj
 *  
 */
public class WcAnswerRecordBean {
    int id;

    int userId;

    int questionId;

    int answerId;

    int wager;
        
    /**
     * @return Returns the answerId.
     */
    public int getAnswerId() {
        return answerId;
    }
    /**
     * @param answerId The answerId to set.
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return Returns the questionId.
     */
    public int getQuestionId() {
        return questionId;
    }
    /**
     * @param questionId The questionId to set.
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }
    /**
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**
     * @return Returns the wager.
     */
    public int getWager() {
        return wager;
    }
    /**
     * @param wager The wager to set.
     */
    public void setWager(int wager) {
        this.wager = wager;
    }
}
