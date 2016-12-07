/*
 * Created on 2006-6-9
 *
 */
package net.joycool.wap.bean.wc;

/**
 * @author lbj
 *  
 */
public class WcAnswerBean {
    int id;

    int questionId;

    String title;

    float money;

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
     * @return Returns the money.
     */
    public float getMoney() {
        return money;
    }

    /**
     * @param money
     *            The money to set.
     */
    public void setMoney(float money) {
        this.money = money;
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

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
