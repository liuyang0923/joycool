package net.joycool.wap.test;

/**
 * fanys 2006-07-05
 * 
 * @author Administrator
 * 
 */
public class RecordBean {
	int id;

	int userId;

	int questionId;

	int answerId;

	String answerTime;// 回答时间

	int mark;

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
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
	 * @return Returns the answerTime.
	 */
	public String getAnswerTime() {
		return answerTime;
	}

	/**
	 * @param answerTime
	 *            The answerTime to set.
	 */
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
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
