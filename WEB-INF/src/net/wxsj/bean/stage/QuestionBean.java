/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.bean.stage;

import java.util.ArrayList;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public class QuestionBean {
    public static int CHOOSE = 0;

    public static int FILL = 1;

    public static int NOT_ENDED = 0;

    public static int ENDED = 1;

    public static int GAME_POINT = 0;

    public static int ITEM = 1;

    public static int POINT = 2;

    public int id;

    public String title;

    public int type;

    public String getTypeStr() {
        if (type == QuestionBean.CHOOSE) {
            return "选择题";
        } else {
            return "填空题";
        }
    }

    public String correctAnswer;

    public String tipUrl;

    public int maxCount;

    public int ended;

    public String content;

    public ArrayList answerList;

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

    public String getEndedStr() {
        if (ended == QuestionBean.NOT_ENDED) {
            return "未结束";
        } else {
            return "已结束";
        }
    }

    public int bonus;

    public int bonusType;

    public String getBonusTypeStr() {
        if (bonusType == QuestionBean.GAME_POINT) {
            return "乐币";
        } else if (bonusType == QuestionBean.POINT) {
            return "经验";
        } else {
            return "道具";
        }
    }

    public String createDatetime;

    public String endDatetime;

    public int currentCount;

    public int correctCount;

    /**
     * @return Returns the bonus.
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * @param bonus
     *            The bonus to set.
     */
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    /**
     * @return Returns the bonusType.
     */
    public int getBonusType() {
        return bonusType;
    }

    /**
     * @param bonusType
     *            The bonusType to set.
     */
    public void setBonusType(int bonusType) {
        this.bonusType = bonusType;
    }

    /**
     * @return Returns the correctAnswer.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * @param correctAnswer
     *            The correctAnswer to set.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * @return Returns the correctCount.
     */
    public int getCorrectCount() {
        return correctCount;
    }

    /**
     * @param correctCount
     *            The correctCount to set.
     */
    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

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
     * @return Returns the currentCount.
     */
    public int getCurrentCount() {
        return currentCount;
    }

    /**
     * @param currentCount
     *            The currentCount to set.
     */
    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    /**
     * @return Returns the endDatetime.
     */
    public String getEndDatetime() {
        return endDatetime;
    }

    /**
     * @param endDatetime
     *            The endDatetime to set.
     */
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    /**
     * @return Returns the ended.
     */
    public int getEnded() {
        return ended;
    }

    /**
     * @param ended
     *            The ended to set.
     */
    public void setEnded(int ended) {
        this.ended = ended;
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
     * @return Returns the maxCount.
     */
    public int getMaxCount() {
        return maxCount;
    }

    /**
     * @param maxCount
     *            The maxCount to set.
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * @return Returns the tipUrl.
     */
    public String getTipUrl() {
        return tipUrl;
    }

    /**
     * @param tipUrl
     *            The tipUrl to set.
     */
    public void setTipUrl(String tipUrl) {
        this.tipUrl = tipUrl;
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

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(int type) {
        this.type = type;
    }
}
