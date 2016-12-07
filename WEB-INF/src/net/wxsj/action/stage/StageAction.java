/*
 * Created on 2007-8-19
 *
 */
package net.wxsj.action.stage;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.wxsj.bean.stage.QuestionBean;
import net.wxsj.bean.stage.TryBean;
import net.wxsj.framework.JoycoolInfc;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IStageService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-19
 * 
 * 说明：
 */
public class StageAction {
    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-19
     * 
     * 说明：首页
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void index(HttpServletRequest request, HttpServletResponse response) {
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        int COUNT_PER_PAGE = 10;
        
        //取未结束的问题
        IStageService service = ServiceFactory.createStageService(IBaseService.CONN_IN_SERVICE, null);
        String condition = "ended = " + QuestionBean.NOT_ENDED;
        int totalCount = service.getQuestionCount(condition);
        PagingBean paging = new PagingBean(pageIndex, totalCount,
                COUNT_PER_PAGE);
        pageIndex = paging.getCurrentPageIndex();
        //取得本页列表
        ArrayList list = service.getQuestionList(condition, paging
                .getCurrentPageIndex()
                * COUNT_PER_PAGE, COUNT_PER_PAGE, "id desc");
        String prefixUrl = ("index.jsp");
        paging.setPrefixUrl(prefixUrl);
        service.releaseAll();
        
        request.setAttribute("list", list);
        request.setAttribute("paging", paging);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-19
     * 
     * 说明：问题
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void question(HttpServletRequest request,
            HttpServletResponse response) {
        if (!JoycoolInfc.checkLogin(request, response, true)) {
            return;
        }

        UserBean user = JoycoolInfc.getLoginUser(request);

        int id = StringUtil.toInt(request.getParameter("id"));
        if (id <= 0) {
            return;
        }

        IStageService service = ServiceFactory.createStageService(
                IBaseService.CONN_IN_SERVICE, null);
        QuestionBean question = service.getQuestion("id = " + id);
        if (question == null) {
            return;
        }
        if (service.getTryCount("user_id = " + user.getId()
                + " and question_id = " + id) > 0) {
            request.setAttribute("question", question);
            request.setAttribute("result", "hasTry");
            service.releaseAll();
            return;
        }

        String condition = "question_id = " + id;
        ArrayList list = service.getAnswerList(condition, 0, -1, "id");
        question.setAnswerList(list);

        service.releaseAll();

        request.setAttribute("question", question);
    }

    /**
     * 作者：李北金
     * 
     * 创建日期：2007-8-19
     * 
     * 说明：回答
     * 
     * 参数及返回值说明：
     * 
     * @param request
     * @param response
     */
    public void answer(HttpServletRequest request, HttpServletResponse response) {
        if (!JoycoolInfc.checkLogin(request, response, true)) {
            return;
        }

        UserBean user = JoycoolInfc.getLoginUser(request);

        int id = StringUtil.toInt(request.getParameter("id"));
        if (id <= 0) {
            return;
        }
        String answer = StringUtil.dealParam(request.getParameter("answer"));
        if (StringUtil.isNull(answer)) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", "请输入答案。");
            return;
        }

        IStageService service = ServiceFactory.createStageService(
                IBaseService.CONN_IN_SERVICE, null);
        QuestionBean question = service.getQuestion("id = " + id);
        if (question == null) {
            return;
        }
        if (question.getEnded() == QuestionBean.ENDED) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", "这道题已经结束了。");
            service.releaseAll();
            return;
        }
        if (service.getTryCount("user_id = " + user.getId()
                + " and question_id = " + id) > 0) {
            request.setAttribute("question", question);
            request.setAttribute("result", "hasTry");
            service.releaseAll();
            return;
        }
        if (service.getTryCount("user_id = " + user.getId()
                + " and to_days(now()) - to_days(create_datetime) = 0") >= 3) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", "您今天已经回答过三道题了。");
            service.releaseAll();
            return;
        }

        //判断是否正确
        int isCorrect = 0;
        if (question.getCorrectAnswer().equalsIgnoreCase(answer)) {
            isCorrect = 1;
        }
        //开始事务
        service.getDbOp().startTransaction();

        int tryId = service.getNumber("id", "wxsj_try", "max", "id > 0") + 1;
        TryBean bean = new TryBean();
        bean.setAnswer(answer);
        bean.setId(tryId);
        bean.setIsCorrect(isCorrect);
        bean.setQuestionId(id);
        bean.setUserId(user.getId());
        bean.setCreateDatetime(DateUtil.getNow());
        if (!service.addTry(bean)) {
            request.setAttribute("result", "failure");
            service.releaseAll();
            return;
        }

        if (isCorrect == 1) {
            service
                    .updateQuestion(
                            "current_count = (current_count + 1), correct_count = (correct_count + 1)",
                            "id = " + id);
        } else {
            service.updateQuestion("current_count = (current_count + 1)",
                    "id = " + id);
        }
        if (question.getMaxCount() <= question.getCurrentCount() + 1) {
            service.updateQuestion("ended = 1", "id = " + id);
        }

        service.getDbOp().commitTransaction();

        //正确
        if (isCorrect == 1) {
            //给奖励
            Random rand = new Random();
            int r = rand.nextInt(100);
            String tip = "";
            //90%的几率得到设定的奖品
            if (r < 90) {
                //乐币
                if (question.getBonusType() == QuestionBean.GAME_POINT) {
                    int[] ts = new int[1];
                    ts[0] = JoycoolInfc.GAME_POINT;
                    int[] ss = new int[1];
                    ss[0] = question.getBonus();
                    JoycoolInfc.updateUserStatus(user.getId(), ts, ss, request);
                    tip = "你获得" + question.getBonus() + "乐币的奖励";
                }
                //经验
                else if (question.getBonusType() == QuestionBean.POINT) {
                    int[] ts = new int[1];
                    ts[0] = JoycoolInfc.POINT;
                    int[] ss = new int[1];
                    ss[0] = question.getBonus();
                    JoycoolInfc.updateUserStatus(user.getId(), ts, ss, request);
                    tip = "你获得" + question.getBonus() + "经验值的奖励";
                }
                //道具
                else {
                    DummyProductBean p = JoycoolInfc.addUserBag(user.getId(),
                            question.getBonus());
                    tip = "你获得一张" + p.getName() + "作为奖励";
                }
            }
            //10%的几率得到下载卡
            else {
                DummyProductBean p = JoycoolInfc.addUserBag(user.getId(),
                        JoycoolInfc.DOWNLOAD_CARD_ID);
                tip = "你获得一张" + p.getName() + "作为奖励";
            }

            request.setAttribute("result", "right");
            request.setAttribute("tip", tip);
        } else {
            int[] ts = new int[1];
            ts[0] = JoycoolInfc.GAME_POINT;
            int[] ss = new int[1];
            ss[0] = 100;
            JoycoolInfc.updateUserStatus(user.getId(), ts, ss, request);
            request.setAttribute("result", "wrong");
        }

        service.releaseAll();
    }
}
