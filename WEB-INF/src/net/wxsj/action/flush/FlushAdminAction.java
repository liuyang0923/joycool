/*
 * Created on 2007-8-17
 *
 */
package net.wxsj.action.flush;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.PagingBean;
import net.wxsj.bean.flush.LinkBean;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IFlushService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-17
 * 
 * 说明：
 */
public class FlushAdminAction {

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
    public void linkList(HttpServletRequest request,
            HttpServletResponse response) {
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        int COUNT_PER_PAGE = 50;

        IFlushService service = ServiceFactory.createFlushService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "id > 0";
        //取得总数
        int totalCount = service.getLinkCount(condition);
        PagingBean paging = new PagingBean(pageIndex, totalCount,
                COUNT_PER_PAGE);
        pageIndex = paging.getCurrentPageIndex();
        //取得本页列表
        ArrayList list = service.getLinkList(condition, paging
                .getCurrentPageIndex()
                * COUNT_PER_PAGE, COUNT_PER_PAGE, "id desc");
        String prefixUrl = "linkList.jsp";
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
    public void addLink(HttpServletRequest request, HttpServletResponse response) {
        if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入标题");
                return;
            }
            String link = StringUtil.dealParam(request.getParameter("link"));
            if (StringUtil.isNull(link)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入链接地址");
                return;
            }
            String remark = StringUtil
                    .dealParam(request.getParameter("remark"));
            String script = StringUtil
                    .dealParam(request.getParameter("script"));
            int maxHits = StringUtil.toInt(request.getParameter("maxHits"));
            if (maxHits < 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置最大点击数。");
                return;
            }
            int maxMobile = StringUtil.toInt(request.getParameter("maxMobile"));
            if (maxMobile < 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置最大手机号。");
                return;
            }

            IFlushService service = ServiceFactory.createFlushService(
                    IBaseService.CONN_IN_SERVICE, null);

            //开始事务
            service.getDbOp().startTransaction();
            //问题
            int linkId = service.getNumber("id", "wxsj_flush_link", "max",
                    "id > 0") + 1;
            LinkBean bean = new LinkBean();
            bean.setCreateDatetime(DateUtil.getNow());
            bean.setCurrentHits(0);
            bean.setCurrentMobile(0);
            bean.setId(linkId);
            bean.setLastUpdateTime(DateUtil.getNow());
            bean.setMaxHits(maxHits);
            bean.setMaxMobile(maxMobile);
            bean.setName(name);
            bean.setRemark(remark);
            bean.setScript(script);
            bean.setLink(link);

            if (!service.addLink(bean)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "添加失败。");
                return;
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
    public void editLink(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IFlushService service = ServiceFactory.createFlushService(
                IBaseService.CONN_IN_SERVICE, null);
        LinkBean flushLink = service.getLink("id = " + id);

        if ("get".equalsIgnoreCase(request.getMethod())) {
            request.setAttribute("flushLink", flushLink);
            service.releaseAll();
            return;
        }
        // post
        else if ("post".equalsIgnoreCase(request.getMethod())) {
            String name = StringUtil.dealParam(request.getParameter("name"));
            if (StringUtil.isNull(name)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入标题");
                return;
            }
            String link = StringUtil.dealParam(request.getParameter("link"));
            if (StringUtil.isNull(link)) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请输入链接地址");
                return;
            }
            String remark = StringUtil
                    .dealParam(request.getParameter("remark"));
            String script = StringUtil
                    .dealParam(request.getParameter("script"));
            int maxHits = StringUtil.toInt(request.getParameter("maxHits"));
            if (maxHits < 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置最大点击数。");
                return;
            }
            int maxMobile = StringUtil.toInt(request.getParameter("maxMobile"));
            if (maxMobile < 0) {
                request.setAttribute("result", "failure");
                request.setAttribute("tip", "请设置最大手机号。");
                return;
            }

            //开始事务
            service.getDbOp().startTransaction();
            String set = "name = '" + name + "', link = '" + link
                    + "', remark = '" + remark + "', max_hits = " + maxHits
                    + ", max_mobile = " + maxMobile + ", script = '" + script
                    + "'";
            service.updateLink(set, "id = " + id);

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
    public void deleteLink(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));
        IFlushService service = ServiceFactory.createFlushService(
                IBaseService.CONN_IN_SERVICE, null);
        service.getDbOp().startTransaction();
        service.deleteHistory("link_id = " + id);
        service.deleteLink("id = " + id);
        service.deleteMobile("link_id = " + id);
        service.getDbOp().commitTransaction();
        service.releaseAll();

        request.setAttribute("result", "success");
    }
    
    public void history(HttpServletRequest request,
            HttpServletResponse response) {
        int id = StringUtil.toInt(request.getParameter("id"));

        IFlushService service = ServiceFactory.createFlushService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "link_id = " + id;
        //取得本页列表
        ArrayList list = service.getHistoryList(condition, 0, -1, "log_date desc, id desc");        

        service.releaseAll();

        request.setAttribute("list", list);
    }
}
