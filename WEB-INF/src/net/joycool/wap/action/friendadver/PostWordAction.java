/*
 * 
 *
 */
package net.joycool.wap.action.friendadver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.friendadver.FriendAdverMessageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendAdverMessageService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author zhul 2006-06-21 交友中心模块，回复文本评论部分
 * 由postWord.jsp调用此类，功能：检查并保存用户回复信息
 * 成功后给出成功信息，完成此流程。用户可选择回到交友中心
 */
public class PostWordAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IFriendAdverMessageService service = ServiceFactory
				.createFriendAdverMessageService();
		UserBean loginUser = getLoginUser(request);
		String tip = null;
		/**
		 * 取得参数
		 */
		String friendAdverId = request.getParameter("friendAdverId");
		String content = request.getParameter("content");
		//判断重复提交
		String sub=friendAdverId+content;
		String lastSub = (String)request.getSession().getAttribute("sub");
		if(lastSub!=null)
		{
			if(lastSub.equals(sub))
			{
				tip="您的评论已经提交完毕，请继续！";
				request.setAttribute("result", "success");
				request.setAttribute("tip",tip);
				request.setAttribute("id", friendAdverId);
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);								
			}
		}
		request.getSession().setAttribute("sub",sub);
		
		/**
		 * 检查参数，如果有误将直接返回用户输入信息页，提示用户！
		 */
		if (content == null || content.equals("")) {
			tip = "内容不能为空！";
			request.setAttribute("tip", tip);
			request.setAttribute("id", friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		//信息无误，将信息保存
		FriendAdverMessageBean friendAdverMessage = new FriendAdverMessageBean();
		friendAdverMessage.setFriendAdverId(StringUtil.toInt(friendAdverId));
		friendAdverMessage.setUserId(loginUser.getId());
		friendAdverMessage.setUserNickname(loginUser.getNickName());
		friendAdverMessage.setContent(content);

		if (!service.addFriendAdverMessage(friendAdverMessage)) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		request.setAttribute("result", "success");
		request.setAttribute("id", friendAdverId);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
