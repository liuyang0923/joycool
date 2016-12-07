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
public class AnswerBean {
    public int id;

    public int questionId;

    public String content;

    public int isCorrect;

    /**
     * @return Returns the content.
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            The content to set.
     */
    public void setContent(String content) {
        this.content = content;
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
     * @return Returns the questionId.
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId
     *            The questionId to set.
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
