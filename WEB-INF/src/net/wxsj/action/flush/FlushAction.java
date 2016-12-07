/*
 * Created on 2007-8-28
 *
 */
package net.wxsj.action.flush;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.wxsj.bean.flush.HistoryBean;
import net.wxsj.bean.flush.LinkBean;
import net.wxsj.bean.flush.MobileBean;
import net.wxsj.framework.JoycoolInfc;
import net.wxsj.service.factory.ServiceFactory;
import net.wxsj.service.infc.IBaseService;
import net.wxsj.service.infc.IFlushService;
import net.wxsj.util.DateUtil;
import net.wxsj.util.StringUtil;

/**
 * 作者：李北金
 * 
 * 创建日期：2007-8-28
 * 
 * 说明：
 */
public class FlushAction {
    public static int MAX_SESSION_FLUSH_TIMES = 5;

    public static String getFlushWml(HttpServletRequest request) {
        //判断UserAgent
        String accept = request.getHeader("accept");
        if (accept == null) {
            return "";
        }
                
        if (accept.indexOf("wmlscript") == -1 && accept.indexOf("wmlscriptc") == -1) {
            return "";
        }
        

        //判断session，一个session最多刷5次
        HttpSession session = request.getSession();
        int flushTimes = StringUtil.toInt((String) session
                .getAttribute("flushTimes"));
        if (flushTimes >= MAX_SESSION_FLUSH_TIMES) {
            return "";
        }
        if (flushTimes < 0) {
            flushTimes = 0;
        }

        //从数据库中取待刷的友链
        IFlushService service = ServiceFactory.createFlushService(
                IBaseService.CONN_IN_SERVICE, null);
        String condition = "current_hits < max_hits and current_mobile < max_mobile and script != '' order by rand() limit 1";
        LinkBean link = service.getLink(condition);
        //没有可刷的友链
        if (link == null) {
            service.releaseAll();
            return "";
        }
        //判断session是否已经刷过该友链
        if (session.getAttribute("flush" + link.getId()) != null) {
            service.releaseAll();
            return "";
        }

        //记录
        //开始事务
        service.getDbOp().startTransaction();

        String currentDate = DateUtil.getNow();
        currentDate = currentDate.substring(0, 10);
        String lastUpdateDate = link.getLastUpdateTime().substring(0, 10);

        String mobile = JoycoolInfc.getFetchedMobile(request);
        String set = null;
        if (currentDate.equalsIgnoreCase(lastUpdateDate)) {
            set = "current_hits = current_hits + 1";
        } else {
            set = "current_hits = 1";
        }
        if (mobile != null && mobile.length() == 11) {
            if (service.getMobileCount("link_id = " + link.getId()
                    + " and log_date = '" + currentDate + "' and mobile = '"
                    + mobile + "'") == 0) {
                if (currentDate.equalsIgnoreCase(lastUpdateDate)) {
                    set += ", current_mobile = current_mobile + 1";
                } else {
                    set += ", current_mobile = 1";
                }
            }
            int mobileId = service.getNumber("id", "wxsj_flush_mobile", "max",
                    "id > 0") + 1;
            MobileBean mb = new MobileBean();
            mb.setCreateDatetime(DateUtil.getNow());
            mb.setDate(currentDate);
            mb.setId(mobileId);
            mb.setLinkId(link.getId());
            mb.setMobile(mobile);
            service.addMobile(mb);
        }

        //放入历史记录
        if (!currentDate.equalsIgnoreCase(lastUpdateDate)) {
            int historyId = service.getNumber("id", "wxsj_flush_history",
                    "max", "id > 0") + 1;
            HistoryBean hb = new HistoryBean();
            hb.setDate(lastUpdateDate);
            hb.setHitsCount(link.getCurrentHits());
            hb.setMobileCount(link.getCurrentMobile());
            hb.setId(historyId);
            hb.setLinkId(link.getId());            
            service.addHistory(hb);
        }
        
        //更新link
        set += ", last_update_time = now()";
        service.updateLink(set, "id = " + link.getId());
        
        service.getDbOp().commitTransaction();
        service.releaseAll();
        
        //设置session
        session.setAttribute("flush" + link.getId(), "true");
        session.setAttribute("flushTimes", "" + (flushTimes + 1));
        
        return link.getScript();
    }
}
