/*
 * Created on 2007-1-26
 *
 */
package net.wxsj.action.test;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.INewTestService;
import net.wxsj.util.StringUtil;

public class WebTestAction {

	/**
     * 
     * 作者：张陶
     * 
     * 创建日期：2007-1-26
     * 
     * 说明：得到所有的调查问卷，包括已经关闭的。
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void allTestList(HttpServletRequest request,
            HttpServletResponse response) {

        // 创建service
        INewTestService testService = ServiceFactory.createNewTestService();
        ArrayList testList = testService.getTestList(null, 0, -1,
                "create_datetime desc");

        request.setAttribute("testList", testList);
    }

    /**
     * 
     * 作者：张陶
     * 
     * 创建日期：2007-1-26
     * 
     * 说明：根据问卷的ID，获取用户填写的调查信息
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void getRecordListByTestId(HttpServletRequest request,
            HttpServletResponse response) {
        int testId = StringUtil.toInt(request.getParameter("id"));
        // 创建service
        INewTestService testService = ServiceFactory.createNewTestService();
        String condition = "test_id = " + testId;
        ArrayList recordList = testService.getTestRecordList(condition, 0, -1,
                " user_id asc, question_code asc ");
        int questionCount = testService.getTestQuestionCount(condition);
        request.setAttribute("questionCount", new Integer(questionCount));
        request.setAttribute("recordList", recordList);
    }
}
 