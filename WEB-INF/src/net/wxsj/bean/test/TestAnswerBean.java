/*
 * Created on 2006-10-26
 *
 */

package net.wxsj.bean.test;

/**
 * 作者：张毅
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public class TestAnswerBean {

    public int id;
    
    public int testId;
    
    public int questionCode;
    
    public int code;
    
    public String answer;
    
    public int nextPageCode;
    
    

    /**
     * @return Returns the nextPageCode.
     */
    public int getNextPageCode() {
        return nextPageCode;
    }
    /**
     * @param nextPageCode The nextPageCode to set.
     */
    public void setNextPageCode(int nextPageCode) {
        this.nextPageCode = nextPageCode;
    }
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuestionCode() {
		return questionCode;
	}

	public void setQuestionCode(int questionCode) {
		this.questionCode = questionCode;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

}
