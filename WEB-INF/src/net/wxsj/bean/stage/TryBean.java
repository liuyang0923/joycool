/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.bean.stage;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public class TryBean {
    public int id;

    public int userId;

    public int questionId;

    public String answer;

    public int isCorrect;

    public String createDatetime;

    /**
     * @return Returns the createDatetime.
     */
    public String getCreateDatetime() {
        return createDatetime;
    }

    /**
     * @param createDatetime
     *            The createDatetime to set.
     */
    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    /**
     * @return Returns the answer.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer
     *            The answer to set.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
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

    /**
     * @return Returns the isCorrect.
     */
    public int getIsCorrect() {
        return isCorrect;
    }

    /**
     * @param isCorrect
     *            The isCorrect to set.
     */
    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    /**
     * @return Returns the quesitonId.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * @param quesitonId
     *            The quesitonId to set.
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
     * @param userId
     *            The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
