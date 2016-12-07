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
public class TestBean {

    public int id;
    
    public String title;
    
    public String introduction;
    
    public String createDatetime;
    
    public int isClosed;

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public int getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(int isClosed) {
		this.isClosed = isClosed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
    
}
