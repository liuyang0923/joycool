/*
 * Created on 2006-2-21
 *
 */
package net.joycool.wap.action.wgamehall;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.UserStatusBean;

/**
 * @author lbj
 *  
 */
public class IndexAction extends HallBaseAction {
    public IndexAction(HttpServletRequest request) {
        super(request);
    }

    /**
     * 取得用户的乐币状态。
     * 
     * @return
     */
    public UserStatusBean getUserStatus() {
        return getUserStatus(loginUser.getId());
    }
}
