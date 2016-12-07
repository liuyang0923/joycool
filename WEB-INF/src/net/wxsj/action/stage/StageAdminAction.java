/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.action.stage;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;
import net.wxsj.bean.stage.AnswerBean;
import net.wxsj.bean.stage.QuestionBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IStageService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public class StageAdminAction {

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-7-30
     * 
     * 说明：信息列表
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void questionList(HttpServletRequest request,
            HttpServletResponse response) {
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        int COUNT_PER_PAGE = 50;

        IStageService service = ServiceFactory.createStageService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "id > 0";
        //取得总数
        int totalCount = service.getQuestionCount(condition);
        PagingBean paging = new PagingBean(pageIndex, totalCount,
                COUNT_PER_PAGE);
        pageIndex = paging.getCurrentPageIndex();
        //取得本页列表
        ArrayList list = service.getQuestionList(condition, paging
                .getCurrentPageIndex()
                * COUNT_PER_PAGE, COUNT_PER_PAGE, "ended, id desc");
        String prefixUrl = "questionList.jsp";
        paging.setPrefixUrl(prefixUrl);

        service.releaseAll();

        request.setAttribute("list", list);
        request.setAttribute("paging", paging);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-17
     * 
     * 说明：增加一个问题
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void addQuestion(HttpServletRequest request,
            HttpServletResponse response) {
        if ("post".equalsIgnoreCase(request.getMethod())) {
            String title = StringUtil.dealParam(request.getParameter("title"));
            if (StringUtil.isNull(title)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入标题");
                return;
            }
            String content = StringUtil.dealParam(request
                    .getParameter("content"));
            if (StringUtil.isNull(content)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入内容");
                return;
            }
            String tipUrl = StringUtil
                    .dealParam(request.getParameter("tipUrl"));
            if (StringUtil.isNull(tipUrl)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入提示网址");
                return;
            }
            int type = StringUtil.toInt(request.getParameter("type"));
            String correctAnswer = StringUtil.dealParam(request
                    .getParameter("correctAnswer"));
            if (type == QuestionBean.FILL) {
                if (StringUtil.isNull(correctAnswer)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入答案");
                    return;
                }
            }
            int maxCount = StringUtil.toInt(request.getParameter("maxCount"));
            if (maxCount <= 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置多少人回答了结束。");
                return;
            }
            int bonusType = StringUtil.toInt(request.getParameter("bonusType"));
            int bonus = StringUtil.toInt(request.getParameter("bonus"));
            if (bonus <= 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入奖励值。");
                return;
            }
            String answer1 = StringUtil.dealParam(request
                    .getParameter("answer1"));
            String answer2 = StringUtil.dealParam(request
                    .getParameter("answer2"));
            String answer3 = StringUtil.dealParam(request
                    .getParameter("answer3"));
            String answer4 = StringUtil.dealParam(request
                    .getParameter("answer4"));
            String answer5 = StringUtil.dealParam(request
                    .getParameter("answer5"));
            String answer6 = StringUtil.dealParam(request
                    .getParameter("answer6"));
            int correctAnswerIndex = StringUtil.toInt(request
                    .getParameter("correctAnswerIndex"));
            int answerCount = 0;
            boolean hasCorrectAnswer = false;
            ArrayList answerList = new ArrayList();
            AnswerBean answer = null;
            if (!StringUtil.isNull(answer1)) {
                answer = new AnswerBean();
                answer.setContent(answer1);
                answerCount++;
                if (correctAnswerIndex == 1) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer2)) {
                answer = new AnswerBean();
                answer.setContent(answer2);
                answerCount++;
                if (correctAnswerIndex == 2) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer3)) {
                answer = new AnswerBean();
                answer.setContent(answer3);
                answerCount++;
                if (correctAnswerIndex == 3) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer4)) {
                answer = new AnswerBean();
                answer.setContent(answer4);
                answerCount++;
                if (correctAnswerIndex == 4) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer5)) {
                answer = new AnswerBean();
                answer.setContent(answer5);
                answerCount++;
                if (correctAnswerIndex == 5) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer6)) {
                answer = new AnswerBean();
                answer.setContent(answer6);
                answerCount++;
                if (correctAnswerIndex == 6) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (type == QuestionBean.CHOOSE) {
                if (answerCount < 2) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "至少有两个候选答案。");
                    return;
                }
                if (!hasCorrectAnswer) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请设定正确答案。");
                    return;
                }
            }

            IStageService service = ServiceFactory.createStageService(
                    IBaseService.CONN_IN_SERVICE, null);

            //开始事务
            service.getDbOp().startTransaction();
            //问题
            int questionId = service.getNumber("id", "wxsj_question", "max",
                    "id > 0") + 1;
            QuestionBean question = new QuestionBean();
            question.setBonus(bonus);
            question.setBonusType(bonusType);
            question.setContent(content);
            if (type == QuestionBean.FILL) {
                question.setCorrectAnswer(correctAnswer);
            } else {
                question.setCorrectAnswer("");
            }
            question.setCorrectCount(0);
            question.setCreateDatetime(DateUtil.getNow());
            question.setCurrentCount(0);
            question.setEndDatetime(DateUtil.getNow());
            question.setEnded(QuestionBean.NOT_ENDED);
            question.setId(questionId);
            question.setMaxCount(maxCount);
            question.setTipUrl(tipUrl);
            question.setTitle(title);
            question.setType(type);
            if (!service.addQuestion(question)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "添加失败。");
                return;
            }
            if (type == QuestionBean.CHOOSE) {
                //答案
                Iterator itr = answerList.iterator();
                int correctAnswerId = 0;
                int answerId = 0;
                while (itr.hasNext()) {
                    answerId = service.getNumber("id", "wxsj_answer", "max",
                            "id > 0") + 1;
                    answer = (AnswerBean) itr.next();
                    answer.setId(answerId);
                    answer.setQuestionId(questionId);
                    if (!service.addAnswer(answer)) {
                        request.setAttribute("result", "failure");
                        request.setAttribute("tip", "添加失败。");
                        return;
                    }
                    if (answer.getIsCorrect() == 1) {
                        correctAnswerId = answerId;
                    }
                }

                service.updateQuestion("correct_answer = '" + correctAnswerId
                        + "'", "id = " + questionId);
            }

            service.getDbOp().commitTransaction();

            service.releaseAll();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-17
     * 
     * 说明：增加一个问题
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void editQuestion(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IStageService service = ServiceFactory.createStageService(
                IBaseService.CONN_IN_SERVICE, null);
        QuestionBean question = service.getQuestion("id = " + id);
        ArrayList oldAnswerList = service.getAnswerList("question_id = " + id,
                0, -1, "id");

        if ("get".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("question", question);
            request.setAttribute("answerList", oldAnswerList);
            service.releaseAll();
            return;
        }
        // post
        else if ("post".equalsIgnoreCase(request.getMethod())) {
            String title = StringUtil.dealParam(request.getParameter("title"));
            if (StringUtil.isNull(title)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入标题");
                return;
            }
            String content = StringUtil.dealParam(request
                    .getParameter("content"));
            if (StringUtil.isNull(content)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入内容");
                return;
            }
            String tipUrl = StringUtil
                    .dealParam(request.getParameter("tipUrl"));
            if (StringUtil.isNull(tipUrl)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入提示网址");
                return;
            }
            int type = StringUtil.toInt(request.getParameter("type"));
            String correctAnswer = StringUtil.dealParam(request
                    .getParameter("correctAnswer"));
            if (type == QuestionBean.FILL) {
                if (StringUtil.isNull(correctAnswer)) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请输入答案");
                    return;
                }
            }
            int maxCount = StringUtil.toInt(request.getParameter("maxCount"));
            if (maxCount <= 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置多少人回答了结束。");
                return;
            }
            int bonusType = StringUtil.toInt(request.getParameter("bonusType"));
            int bonus = StringUtil.toInt(request.getParameter("bonus"));
            if (bonus <= 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入奖励值。");
                return;
            }
            String answer1 = StringUtil.dealParam(request
                    .getParameter("answer1"));
            String answer2 = StringUtil.dealParam(request
                    .getParameter("answer2"));
            String answer3 = StringUtil.dealParam(request
                    .getParameter("answer3"));
            String answer4 = StringUtil.dealParam(request
                    .getParameter("answer4"));
            String answer5 = StringUtil.dealParam(request
                    .getParameter("answer5"));
            String answer6 = StringUtil.dealParam(request
                    .getParameter("answer6"));
            int correctAnswerIndex = StringUtil.toInt(request
                    .getParameter("correctAnswerIndex"));
            int answerCount = 0;
            boolean hasCorrectAnswer = false;
            ArrayList answerList = new ArrayList();
            AnswerBean answer = null;
            if (!StringUtil.isNull(answer1)) {
                answer = new AnswerBean();
                answer.setContent(answer1);
                answerCount++;
                if (correctAnswerIndex == 1) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer2)) {
                answer = new AnswerBean();
                answer.setContent(answer2);
                answerCount++;
                if (correctAnswerIndex == 2) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer3)) {
                answer = new AnswerBean();
                answer.setContent(answer3);
                answerCount++;
                if (correctAnswerIndex == 3) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer4)) {
                answer = new AnswerBean();
                answer.setContent(answer4);
                answerCount++;
                if (correctAnswerIndex == 4) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer5)) {
                answer = new AnswerBean();
                answer.setContent(answer5);
                answerCount++;
                if (correctAnswerIndex == 5) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (!StringUtil.isNull(answer6)) {
                answer = new AnswerBean();
                answer.setContent(answer6);
                answerCount++;
                if (correctAnswerIndex == 6) {
                    hasCorrectAnswer = true;
                    answer.setIsCorrect(1);
                }
                answerList.add(answer);
            }
            if (type == QuestionBean.CHOOSE) {
                if (answerCount < 2) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "至少有两个候选答案。");
                    return;
                }
                if (!hasCorrectAnswer) {
                    request.setAttribute("result", "failure");
                    request.setAttribute("tip", "请设定正确答案。");
                    return;
                }
            }
            int ended = StringUtil.toInt(request.getParameter("ended"));

            //开始事务
            service.getDbOp().startTransaction();
            //问题
            if (type == QuestionBean.CHOOSE) {
                correctAnswer = "";
            }
            String set = "title = '" + title + "', content = '" + content
                    + "', type = " + type + ", ended = " + ended
                    + ", tip_url = '" + tipUrl + "', correct_answer = '"
                    + correctAnswer + "', max_count = " + maxCount
                    + ", bonus_type = " + bonusType + ", bonus = " + bonus;
            if (!service.updateQuestion(set, "id = " + id)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "修改失败。");
                return;
            }
            if (type == QuestionBean.CHOOSE) {
                //答案
                Iterator itr = answerList.iterator();
                int correctAnswerId = 0;
                int answerId = 0;
                int index = 0;
                AnswerBean oldAnswer = null;
                while (itr.hasNext()) {
                    answer = (AnswerBean) itr.next();
                    if (oldAnswerList.size() > index) {
                        oldAnswer = (AnswerBean) oldAnswerList.get(index);
                        if (oldAnswer.getContent().equals(answer.getContent())
                                && oldAnswer.getIsCorrect() == answer
                                        .getIsCorrect()) {
                        	if (answer.getIsCorrect() == 1) {
                                correctAnswerId = oldAnswer.getId();
                            }
                            index++;
                            continue;
                        } else {
                            service.updateAnswer("content = '"
                                    + answer.getContent() + "', is_correct = "
                                    + answer.getIsCorrect(), "id = "
                                    + oldAnswer.getId());
                            if (answer.getIsCorrect() == 1) {
                                correctAnswerId = oldAnswer.getId();
                            }
                            index++;
                            continue;
                        }
                    } else {
                        answerId = service.getNumber("id", "wxsj_answer",
                                "max", "id > 0") + 1;
                        answer.setId(answerId);
                        answer.setQuestionId(id);
                        if (!service.addAnswer(answer)) {
                            request.setAttribute("result", "failure");
                            request.setAttribute("tip", "添加失败。");
                            return;
                        }
                        if (answer.getIsCorrect() == 1) {
                            correctAnswerId = answerId;
                        }
                        index++;
                        continue;
                    }
                }
                if (oldAnswerList.size() > answerList.size()) {
                    for (int i = answerList.size(); i < oldAnswerList.size(); i++) {
                        oldAnswer = (AnswerBean) oldAnswerList.get(i);
                        service.deleteAnswer("id = " + oldAnswer.getId());
                    }
                }

                service.updateQuestion("correct_answer = '" + correctAnswerId
                        + "'", "id = " + id);
            } else {
                service.deleteAnswer("question_id = " + id);
            }

            if("1".equalsIgnoreCase(request.getParameter("restart"))){
                service.deleteTry("question_id = " + id);
                service.updateQuestion("current_count = 0, correct_count = 0", "id = " + id);
            }
            service.getDbOp().commitTransaction();

            service.releaseAll();

            request.setAttribute("result", "success");
        }
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-17
     * 
     * 说明：增加一个问题
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void deleteQuestion(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IStageService service = ServiceFactory.createStageService(
                IBaseService.CONN_IN_SERVICE, null);
        service.getDbOp().startTransaction();
        service.deleteQuestion("id = " + id);
        service.deleteAnswer("question_id = " + id);
        service.deleteTry("question_id = " + id);
        service.getDbOp().commitTransaction();
        service.releaseAll();

        request.setAttribute("result", "success");
    }
}
