package net.joycool.wap.test;

/**
 * fanys 2006-07-05
 * 
 * @author Administrator
 * 
 */
public class QuestionBean {
	int id;

	String question;// 问题

	int questionId;

	int multiple;// 是否可以多选

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
	 * @return Returns the multiple.
	 */
	public int getMultiple() {
		return multiple;
	}

	/**
	 * @param multiple
	 *            The multiple to set.
	 */
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	/**
	 * @return Returns the question.
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            The question to set.
	 */
	public void setQuestion(String question) {
		this.question = question;
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
