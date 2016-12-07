package net.joycool.wap.bean.job;

public class PsychologyAnswerBean {
	int id;

	int psychologyId;

	String answer;

	String explanation;

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
	 * @return Returns the explanation.
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation
	 *            The explanation to set.
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
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
	 * @return Returns the psychologyId.
	 */
	public int getPsychologyId() {
		return psychologyId;
	}

	/**
	 * @param psychologyId
	 *            The psychologyId to set.
	 */
	public void setPsychologyId(int psychologyId) {
		this.psychologyId = psychologyId;
	}
}
