/*
 * Created on 2007-1-25
 *
 */
package net.wxsj.service.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.util.Constants;
import net.wxsj.bean.test.TestAnswerBean;
import net.wxsj.bean.test.TestBean;
import net.wxsj.bean.test.TestPageBean;
import net.wxsj.bean.test.TestQuestionBean;
import net.wxsj.bean.test.TestRecordBean;
import net.wxsj.service.infc.INewTestService;
import net.wxsj.util.db.DbOperation;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-1-25
 * 
 * 说明：
 */
public class NewTestServiceImpl extends BaseServiceImpl implements
        INewTestService {
    public NewTestServiceImpl(int useConnType, DbOperation dbOp) {
        this.useConnType = useConnType;
        this.dbOp = dbOp;
    }

    public NewTestServiceImpl() {
        this.useConnType = CONN_IN_METHOD;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getLoginUserId(HttpServletRequest request) {
        UserBean loginUser = (UserBean) request.getSession().getAttribute(
                Constants.LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser.getId();
        }
        return -1;
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTest(TestBean cart) {
        return addXXX(cart, "new_test");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTestAnswer(TestAnswerBean cart) {
        return addXXX(cart, "new_test_answer");
    }

    public boolean addTestPage(TestPageBean cart) {
        return addXXX(cart, "new_test_page");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTestQuestion(TestQuestionBean cart) {
        return addXXX(cart, "new_test_question");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean addTestRecord(TestRecordBean cart) {
        return addXXX(cart, "new_test_record");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTest(String condition) {
        return deleteXXX(condition, "new_test");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTestAnswer(String condition) {
        return deleteXXX(condition, "new_test_answer");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTestPage(String condition) {
        return deleteXXX(condition, "new_test_page");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTestQuestion(String condition) {
        return deleteXXX(condition, "new_test_question");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean deleteTestRecord(String condition) {
        return deleteXXX(condition, "new_test_record");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TestBean getTest(String condition) {
        return (TestBean) getXXX(condition, "new_test",
                "net.wxsj.bean.test.TestBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TestAnswerBean getTestAnswer(String condition) {
        return (TestAnswerBean) getXXX(condition, "new_test_answer",
                "net.wxsj.bean.test.TestAnswerBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TestPageBean getTestPage(String condition) {
        return (TestPageBean) getXXX(condition, "new_test_page",
                "net.wxsj.bean.test.TestPageBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TestQuestionBean getTestQuestion(String condition) {
        return (TestQuestionBean) getXXX(condition, "new_test_question",
                "net.wxsj.bean.test.TestQuestionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public TestRecordBean getTestRecord(String condition) {
        return (TestRecordBean) getXXX(condition, "new_test_record",
                "net.wxsj.bean.test.TestRecordBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTestCount(String condition) {
        return getXXXCount(condition, "new_test", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTestPageCount(String condition) {
        return getXXXCount(condition, "new_test_page", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTestQuestionCount(String condition) {
        return getXXXCount(condition, "new_test_question", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTestAnswerCount(String condition) {
        return getXXXCount(condition, "new_test_answer", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public int getTestRecordCount(String condition) {
        return getXXXCount(condition, "new_test_record", "id");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTest(String set, String condition) {
        return updateXXX(set, condition, "new_test");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTestAnswer(String set, String condition) {
        return updateXXX(set, condition, "new_test_answer");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTestPage(String set, String condition) {
        return updateXXX(set, condition, "new_test_page");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTestQuestion(String set, String condition) {
        return updateXXX(set, condition, "new_test_question");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public boolean updateTestRecord(String set, String condition) {
        return updateXXX(set, condition, "new_test_record");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTestList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "new_test",
                "net.wxsj.bean.test.TestBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTestPageList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "new_test_page",
                "net.wxsj.bean.test.TestPageBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTestAnswerList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "new_test_answer",
                "net.wxsj.bean.test.TestAnswerBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTestQuestionList(String condition, int index,
            int count, String orderBy) {
        return getXXXList(condition, index, count, orderBy,
                "new_test_question", "net.wxsj.bean.test.TestQuestionBean");
    }

    /*
     * 请查看父类或接口对应的注释。
     */
    public ArrayList getTestRecordList(String condition, int index, int count,
            String orderBy) {
        return getXXXList(condition, index, count, orderBy, "new_test_record",
                "net.wxsj.bean.test.TestRecordBean");
    }
}
