package jc.family.game.ask;

public class AskBean {

	private int id;
	private String question;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private int rightanswers;
	private int state = 0;

	public AskBean() {

	}

	public AskBean(String question, String answer1, String answer2,
			String answer3, String answer4, int rightanswers) {
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.rightanswers = rightanswers;
	}

	public AskBean(int askid, String question, String answer1, String answer2,
			String answer3, String answer4, int rightanswers) {
		this.id = askid;
		this.question = question;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.answer4 = answer4;
		this.rightanswers = rightanswers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public int getRightanswers() {
		return rightanswers;
	}

	public String getRightanswersStr() {
		if (rightanswers == 1) {
			return "A";
		}
		if (rightanswers == 2) {
			return "B";
		}
		if (rightanswers == 3) {
			return "C";
		}
		if (rightanswers == 4) {
			return "D";
		}
		return "*";
	}

	public void setRightanswers(int rightanswers) {
		this.rightanswers = rightanswers;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
