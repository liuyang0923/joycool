/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.jcadmin.UserCashAction;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lbj
 *  
 */
public class SendMessageAction extends BaseAction {
	
	static IMessageService messageService = ServiceFactory.createMessageService();
	static IUserService service = ServiceFactory.createUserService();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        
        UserBean loginUser = getLoginUser(request);
        if (loginUser == null) {
            return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
        }
        HttpSession session = request.getSession();
        String tip = null;
        boolean flag = true;
        /**
         * 取得参数
         */
        String content = request.getParameter("content");
        String backTo = request.getParameter("backTo");        
        if(backTo == null){
            backTo = BaseAction.INDEX_URL;
        }
        int toUserId = StringUtil.toId(request.getParameter("toUserId"));
        /**
         * 检查参数
         */
        String info = null;
        content = StringUtil.noEnter(content);
        if (content == null || content.length() == 0) {
            tip = "消息内容不能为空！";
            flag = false;
        } else if (content.length() > 200) {
            tip = "消息内容过长！";
            flag = false;
        } else if (toUserId == 100 || toUserId == 0) {
            tip = "无效的收件人！";
            flag = false;
        } else if(service.isUserBadGuy(toUserId, loginUser.getId())){
            tip = "你在对方的黑名单里，不能给他发送消息！";
            flag = false;
        } else {
	        //macq_2007-6-19_放置重复发言_start
	        String infos = (String) session.getAttribute("sendMessageCheck");
			info = loginUser.getId()+content +toUserId;
			if (info.equals(infos)) {
				tip = "不能发送内容重复的信件！";
	            flag = false;
			} else {
				ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("mail",loginUser.getId());
				if(forbid != null) {
					tip = "已经被禁止发送信件 - " + forbid.getBak();
		            flag = false;
				}
			}
        }
        //macq_2007-6-19_放置重复发言_end
        //填写信息不正确
        if (flag == false) {
            request.setAttribute("result", "failure");
            request.setAttribute("tip", tip);
            request.setAttribute("backTo", backTo);
        }
        //填写信息正确
        else {
        	session.setAttribute("sendMessageCheck", info);
            
            MessageBean message = new MessageBean();
            message.setFromUserId(loginUser.getId());
            message.setToUserId(toUserId);
            message.setContent(content);
            message.setMark(0);
            
            if(!messageService.addMessage(message)){
                return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
            }
            //mcq_1_增加用户经验值  时间:2006-6-11
            //增加用户经验值
            UserBean user= (UserBean)session.getAttribute(Constants.LOGIN_USER_KEY);
            user.notice[1]++;
            RankAction.addPoint(user,Constants.RANK_GENERAL);
            //mcq_end
//    		wucx 用户有好度2006－10－19 start
//service.updateFriend("level_value=level_value+1,update_datetime=now()","user_id="+loginUser.getId()+" and friend_id="+StringUtil.toInt(toUserId));
            service.addOrupdateFriendLevel(loginUser.getId(),toUserId);
            	UserInfoUtil.updateUserStatus("social=social+1" , "user_id="+loginUser.getId(), loginUser.getId(), UserCashAction.OTHERS, "写信增加1个社交指数");
   	
            		//wucx 用户有好度2006－10－19 end
            request.setAttribute("result", "success");
            request.setAttribute("backTo", backTo);
        }

        return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
    }
}
