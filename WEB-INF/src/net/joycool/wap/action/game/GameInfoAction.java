/*
 * Created on 2005-12-8
 *
 */
package net.joycool.wap.action.game;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.game.GameBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IGameService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *
 */
public class GameInfoAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        IGameService gameService = ServiceFactory.createGameService();
        ICatalogService cataService = ServiceFactory.createCatalogService();

        /*
         * 取得参数gameId 游戏id pageIndex 分页码 orderBy 按xxx排序
         */
        int gameId = StringUtil.toInt(request.getParameter("gameId"));
        GameBean game = gameService.getGame("id = " + gameId);
        if(game == null)
        	return mapping.findForward("success");

        String backTo = request.getParameter("backTo");
        if ((backTo == null) || (backTo.equals("")))
            backTo = "GameCataList.do?id=" + game.getCatalogId();
        
        String orderBy = request.getParameter("orderBy");
        if ((orderBy == null) || !(orderBy.equals("id")))
            orderBy = "hits";
        int pageIndex = 0;
        if (request.getParameter("pageIndex") != null) {
            pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        }

        //取得根据gameId得到的游戏文件；
        String condition = "id = " + gameId;
        int catalogId = game.getCatalogId();
        String buffCondition = "id = " + catalogId;
        CatalogBean catalog = cataService.getCatalog(buffCondition);

        //取得上一条和下一条
        String prevCondition = null;
        String nextCondition = null;
        GameBean prevGame = null;
        GameBean nextGame = null;
        if (orderBy.equals("id")) {
            prevCondition = "catalog_id = " + game.getCatalogId() + " and "
                    + orderBy + " > " + game.getId() + " ORDER BY id ASC";
            nextCondition = "catalog_id = " + game.getCatalogId() + " and "
                    + orderBy + " < " + game.getId() + " ORDER BY id DESC";
            prevGame = gameService.getGame(prevCondition);
            nextGame = gameService.getGame(nextCondition);
        } else if (orderBy.equals("hits")) {
            prevCondition = "catalog_id = " + game.getCatalogId() + " and "
                    + orderBy + " >= " + game.getHits() + " and id != "
                    + game.getId() + " ORDER BY hits ASC, id DESC";
            prevGame = gameService.getGame(prevCondition);
            if (prevGame != null) {
                nextCondition = "catalog_id = " + game.getCatalogId()
                        + " and " + orderBy + " <= " + game.getHits()
                        + " and id != " + game.getId() + " and id != "
                        + prevGame.getId() + " ORDER BY hits DESC, id DESC";
            } else {
                nextCondition = "catalog_id = " + game.getCatalogId()
                        + " and " + orderBy + " <= " + game.getHits()
                        + " and id != " + game.getId()
                        + " ORDER BY hits DESC, id DESC";
            }
            nextGame = gameService.getGame(nextCondition);
        }

        if (prevGame != null) {
            String prevImageLink = ("GameInfo.do?gameId="
                    + prevGame.getId() + "&amp;orderBy="
                    + orderBy);
            prevGame.setLinkUrl(prevImageLink);
        }
        //zhul_2006-07-31 修改backto  删除的代码：+ "&amp;backTo="+ URLEncoder.encode(backTo, "UTF-8")        
        if (nextGame != null) {
            String nextImageLink = ("GameInfo.do?gameId="
                    + nextGame.getId() + "&amp;orderBy="
                    + orderBy);
            nextGame.setLinkUrl(nextImageLink);
        }
        //zhul_2006-07-31 修改backto  删除的代码：+ "&amp;backTo="+ URLEncoder.encode(backTo, "UTF-8") 
        //prefixUrl
        String prefixUrl = ("GameInfo.do?gameId="
                + game.getId() + "&amp;orderBy="
                + orderBy);
        //zhul_2006-07-31 修改backto  删除的代码：+ "&amp;backTo="+ URLEncoder.encode(backTo, "UTF-8")
        //      更新浏览数
        if (pageIndex == 0) {
            String set = "hits = (hits + 1)";
            condition = "id = " + gameId;
            gameService.updateGame(set, condition);
        }
        //mcq_1_增加用户经验值  时间:2006-6-11
        //增加用户经验值
        HttpSession session =  request.getSession();
        UserBean user= (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
        RankAction.addPoint(user,Constants.RANK_GENERAL);
        //mcq_end
        request.setAttribute("game", game);
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("catalog", catalog);
        request.setAttribute("backTo", backTo);
        request.setAttribute("prevGame", prevGame);
        request.setAttribute("nextGame", nextGame);

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }

}
