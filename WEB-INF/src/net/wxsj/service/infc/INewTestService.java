/*
 * Created on 2007-1-25
 *
 */
package net.wxsj.service.infc;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.wxsj.bean.test.TestAnswerBean;
import net.wxsj.bean.test.TestBean;
import net.wxsj.bean.test.TestPageBean;
import net.wxsj.bean.test.TestQuestionBean;
import net.wxsj.bean.test.TestRecordBean;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public interface INewTestService extends IBaseService {
    public int getLoginUserId(HttpServletRequest request);
    
	public boolean addTest(TestBean test);
	public boolean updateTest(String set, String condtiion);
	public boolean deleteTest(String condition);
	public int getTestCount(String condition);
	public TestBean getTest(String condition);
    public ArrayList getTestList(String condition, int index, int count,
            String orderBy);
    
	public boolean addTestAnswer(TestAnswerBean testAnswer);
	public boolean updateTestAnswer(String set, String condtiion);
	public boolean deleteTestAnswer(String condition);
	public int getTestAnswerCount(String condition);
	public TestAnswerBean getTestAnswer(String condition);
    public ArrayList getTestAnswerList(String condition, int index, int count,
            String orderBy);
    
	public boolean addTestPage(TestPageBean testPage);
	public boolean updateTestPage(String set, String condtiion);
	public boolean deleteTestPage(String condition);
	public int getTestPageCount(String condition);
	public TestPageBean getTestPage(String condition);
    public ArrayList getTestPageList(String condition, int index, int count,
            String orderBy);
    
	public boolean addTestQuestion(TestQuestionBean testQuestion);
	public boolean updateTestQuestion(String set, String condtiion);
	public boolean deleteTestQuestion(String condition);
	public int getTestQuestionCount(String condition);
	public TestQuestionBean getTestQuestion(String condition);
    public ArrayList getTestQuestionList(String condition, int index, int count,
            String orderBy);
    
	public boolean addTestRecord(TestRecordBean testRecord);
	public boolean updateTestRecord(String set, String condtiion);
	public boolean deleteTestRecord(String condition);
	public int getTestRecordCount(String condition);
	public TestRecordBean getTestRecord(String condition);
    public ArrayList getTestRecordList(String condition, int index, int count,
            String orderBy);
    
}
