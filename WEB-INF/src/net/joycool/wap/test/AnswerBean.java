package net.joycool.wap.test;

/**
 * fanys 2006-07-05
 * 
 * @author Administrator
 * 
 */
public class AnswerBean {
	int id;

	int questionId;

	String answer;// 答案

	int nextQuestionId;// 下一个问题编号

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
	 * @return Returns the nextQuestionId.
	 */
	public int getNextQuestionId() {
		return nextQuestionId;
	}

	/**
	 * @param nextQuestionId
	 *            The nextQuestionId to set.
	 */
	public void setNextQuestionId(int nextQuestionId) {
		this.nextQuestionId = nextQuestionId;
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
