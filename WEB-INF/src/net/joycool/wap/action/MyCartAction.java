/*
 * Created on 2006-1-13
 *
 */
package net.joycool.wap.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 *
 */
public class MyCartAction extends BaseAction{
    public int NUMBER_PAGE = 10;
    
    /**
     * @param request
     */
    public void getCart(HttpServletRequest request) {
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return;
        }
        IUserService service = ServiceFactory.createUserService();
        
        if(request.getParameter("delete") != null){
            int deleteId = StringUtil.toInt(request.getParameter("delete"));
            service.deleteCart("id = " + deleteId);
        }

        //取得参数
        int pageIndex = StringUtil.toInt(request.getParameter("pageIndex"));
        if(pageIndex == -1){
            pageIndex = 0;
        }
        String backTo = request.getParameter("backTo");
        if(backTo == null){
            backTo = BaseAction.INDEX_URL;
        }
        
        String condition = "user_id = " + loginUser.getId();

        //分页相关
        int totalCount = service.getCartCount(condition);
        int totalPageCount = ((totalCount % NUMBER_PAGE == 0) ? totalCount
                / NUMBER_PAGE : totalCount / NUMBER_PAGE + 1);
        if (totalPageCount != 0 && pageIndex > (totalPageCount - 1)) {
            pageIndex = (totalPageCount - 1);
        }
        if (pageIndex <= 0) {
            pageIndex = 0;
        }
        
        String oldBackTo = backTo;
        try {
            backTo = URLEncoder.encode(backTo, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String prefixUrl = "mycart.jsp?backTo=" + backTo;

        condition += " ORDER BY id DESC LIMIT " + pageIndex * NUMBER_PAGE
                + ", " + NUMBER_PAGE;
        Vector cartList = service.getCartList(condition);        
        
        request.setAttribute("backTo", oldBackTo);
        request.setAttribute("totalPageCount", new Integer(totalPageCount));
        request.setAttribute("pageIndex", new Integer(pageIndex));
        request.setAttribute("prefixUrl", prefixUrl);
        request.setAttribute("cartList", cartList);
    }
}
