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
public class TestPageBean {
	
    public int id;
    
    public int testId;
    
    public int code;
    
    public String title;

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

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
