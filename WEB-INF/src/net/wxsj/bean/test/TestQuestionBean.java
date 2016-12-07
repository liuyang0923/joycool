/*
 * Created on 2006-10-26
 *
 */

package net.wxsj.bean.test;

import java.util.ArrayList;

/**
 * 作者：张毅
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public class TestQuestionBean {
	
    public int id;
    
    public int testId;
    
    public int pageCode;
    
    public int code;
    
    public String question;
    
    public int multiple;
    
    public ArrayList questionAnswerList;

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

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getPageCode() {
		return pageCode;
	}

	public void setPageCode(int pageCode) {
		this.pageCode = pageCode;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public ArrayList getQuestionAnswerList() {
		return questionAnswerList;
	}

	public void setQuestionAnswerList(ArrayList questionAnswerList) {
		this.questionAnswerList = questionAnswerList;
	}
}
