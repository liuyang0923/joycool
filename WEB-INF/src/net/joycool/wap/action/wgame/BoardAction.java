/*
 * Created on 2006-1-12
 *
 */
package net.joycool.wap.action.wgame;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 *
 */
public class BoardAction extends WGameAction{
    public int NUMBER_PAGE = 10;
    
    public void getBoard(HttpServletRequest request) {
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return;
        }

        //取得参数
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));

        String condition = "id > 0";

        //分页相关
        int totalCount = userService.getUserStatusCount(condition);
        int totalPageCount = ((totalCount % NUMBER_PAGE == 0) ? totalCount
                / NUMBER_PAGE : totalCount / NUMBER_PAGE + 1);
        if (totalPageCount != 0 && pageIndex > (totalPageCount - 1)) {
            pageIndex = (totalPageCount - 1);
        }
        if (pageIndex <= 0) {
            pageIndex = 0;
        }
        String prefixUrl = "board.jsp";

        condition += " ORDER BY game_point DESC LIMIT " + pageIndex * NUMBER_PAGE
                + ", " + NUMBER_PAGE;
        Vector userList = userService.getUserStatusList(condition, true);
//      fanys2006-08-11
        UserStatusBean userStatus =UserInfoUtil.getUserStatus(loginUser.getId());
//        UserStatusBean userStatus = getUserStatus(loginUser.getId());
        condition = "game_point > " + userStatus.getGamePoint();
        int order = userService.getUserStatusCount(condition);
        
        request.setAttribute("userStatus", userStatus);
        request.setAttribute("order", new Integer(order));
        request.setAttribute("totalPageCount", new Integer(totalPageCount));
        request.setAttribute("pageIndex", new Integer(pageIndex));
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("userList", userList);
    }
}
