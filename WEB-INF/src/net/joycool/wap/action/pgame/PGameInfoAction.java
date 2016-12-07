/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.action.pgame;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.pgame.PGameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IPGameService;
import net.joycool.wap.util.Constants;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class PGameInfoAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IPGameService gameService = ServiceFactory.createPGameService();
        ICatalogService cataService = ServiceFactory.createCatalogService();

        /*
         * 取得参数gameId 游戏id pageIndex 分页码 orderBy 按xxx排序
         */
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        PGameBean pgame = gameService.getPGame("id = " + gameId);

        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || (orderBy.equals(""))) {
            orderBy = "id";
        }

        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        PGameBean prevGame = null;
        PGameBean nextGame = null;
        if (orderBy.equals("id")) {
            prevCondition = "provider_id = " + pgame.getProviderId() + " and "
                    + orderBy + " > " + pgame.getId() + " ORDER BY id ASC";
            nextCondition = "provider_id = " + pgame.getProviderId() + " and "
                    + orderBy + " < " + pgame.getId() + " ORDER BY id DESC";
            prevGame = gameService.getPGame(prevCondition);
            nextGame = gameService.getPGame(nextCondition);
        } else if (orderBy.equals("hits")) {
            prevCondition = "provider_id = " + pgame.getProviderId() + " and "
                    + orderBy + " >= " + pgame.getDownloadSum() + " and id != "
                    + pgame.getId() + " ORDER BY hits ASC, id DESC";
            prevGame = gameService.getPGame(prevCondition);
            if (prevGame != null) {
                nextCondition = "provider_id = " + pgame.getProviderId()
                        + " and " + orderBy + " <= " + pgame.getDownloadSum()
                        + " and id != " + pgame.getId() + " and id != "
                        + prevGame.getId() + " ORDER BY hits DESC, id DESC";
            } else {
                nextCondition = "provider_id = " + pgame.getProviderId()
                        + " and " + orderBy + " <= " + pgame.getDownloadSum()
                        + " and id != " + pgame.getId()
                        + " ORDER BY hits DESC, id DESC";
            }
            nextGame = gameService.getPGame(nextCondition);
        }

        if (prevGame != null) {
            String prevLink = ("PGameInfo.do?gameId="
                    + prevGame.getId() + "&amp;orderBy=" + orderBy);
            prevGame.setLinkUrl(prevLink);
        }
        if (nextGame != null) {
            String nextLink = ("PGameInfo.do?gameId="
                    + nextGame.getId() + "&amp;orderBy=" + orderBy);
            nextGame.setLinkUrl(nextLink);
        }
        
        String set = "download_sum = (download_sum + 1)";
        String condition = "id = " + gameId;
        gameService.updatePGame(set, condition);
        
        request.setAttribute("pgame", pgame);        
        request.setAttribute("prevGame", prevGame);
        request.setAttribute("nextGame", nextGame);
        request.setAttribute("orderBy", orderBy);

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }

}
