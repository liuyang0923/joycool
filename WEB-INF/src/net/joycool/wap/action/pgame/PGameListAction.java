/*
 * Created on 2005-12-30
 *
 */
package net.joycool.wap.action.pgame;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.pgame.PGameProviderBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IPGameService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class PGameListAction extends BaseAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPGameService service = ServiceFactory.createPGameService();
        //取得参数
        int providerId = StringUtil.toInt(request.getParameter("providerId"));
        if (providerId == -1) {
            providerId = 6;
        }
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if (pageIndex == -1) {
            pageIndex = 0;
        }
        String orderBy = request.getParameter("orderBy");
        if (orderBy == null) {
            orderBy = "id";
        }

        //提供商
        PGameProviderBean provider = service.getProvider("id = " + providerId);
        if (provider == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }

        //分页相关
        int length = service.getPGameCount("provider_id = " + providerId);
        int totalPageCount = ((length % Constants.PGAME_PER_PAGE == 0) ? length
                / Constants.PGAME_PER_PAGE : length / Constants.PGAME_PER_PAGE
                + 1);
        if (totalPageCount != 0 && pageIndex > (totalPageCount - 1)) {
            pageIndex = (totalPageCount - 1);
        }
        if (pageIndex <= 0) {
            pageIndex = 0;
        }
        String prefixUrl = "PGameList.do?providerId=" + providerId
                + "&orderBy=" + orderBy;

        //取得游戏列表
        String condition = "provider_id = " + providerId + " ORDER BY "
                + orderBy + " DESC LIMIT " + pageIndex
                * Constants.PGAME_PER_PAGE + ", " + Constants.PGAME_PER_PAGE;
        Vector pgameList = service.getPGameList(condition);

        request.setAttribute("pageIndex", new Integer(pageIndex));
        request.setAttribute("totalPageCount", new Integer(totalPageCount));
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("pgameList", pgameList);
        request.setAttribute("provider", provider);
        request.setAttribute("orderBy", orderBy);

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
