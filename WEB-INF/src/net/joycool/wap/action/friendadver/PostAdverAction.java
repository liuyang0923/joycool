/**
 * 
 */
package net.joycool.wap.action.friendadver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author zhul 2006-06-21 交友中心模块，发表文本交友信息部分
 *         postAdver.jsp调用此action，用来处理用户交友广告信息的检验和保存，
 *         成功后跳到引导用户修改个人信息页面viewUserInfo.jsp
 */
public class PostAdverAction extends BaseAction {
	// macq_2007-6-19_交友广告5分钟内只出现一条_start
	public static long SendMsgTime = 60 * 1000 * 5;

	public static long SendMsgChangeTime = 0;

	public static byte[] lock = new byte[0];

	// macq_2007-6-19_交友广告5分钟内只出现一条_end

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IChatService chatService = ServiceFactory.createChatService();
		IFriendAdverService service = ServiceFactory.createFriendAdverService();
		UserBean loginUser = getLoginUser(request);
		String tip = null;
		/**
		 * 取得参数
		 */
		String title = StringUtil.noEnter(request.getParameter("title"));
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String area = request.getParameter("area");
		String remark = request.getParameter("remark");

		// 判断重复提交
		String submit = title + sex + age + area + remark;
		String lastSubmit = (String) request.getSession()
				.getAttribute("submit");
		if (lastSubmit != null) {
			if (lastSubmit.equals(submit)) {
				tip = "您的交友广告已经提交完毕，请继续！";
				request.setAttribute("tip", tip);
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
		}
		request.getSession().setAttribute("submit", submit);

		/**
		 * 检查参数，如果有误跳回进入页提示用户！
		 */
		if (title == null || title.equals("")) {
			tip = "标题不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward("BACK");
		} else if (remark == null || remark.equals("")) {
			tip = "择友要求不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward("BACK");
		}
		// 输入信息格式正确后将信息保存到数据库
		FriendAdverBean friendAdver = new FriendAdverBean();
		friendAdver.setUserId(loginUser.getId());
		friendAdver.setTitle(title);
		try{
			friendAdver.setSex(Integer.parseInt(sex));
			friendAdver.setAge(Integer.parseInt(age));
			friendAdver.setArea(Integer.parseInt(area));
			friendAdver.setCityno(loginUser.getCityno());
			friendAdver.setGender(loginUser.getGender());
		} catch(Exception e) {}
		friendAdver.setRemark(remark);

		if (!service.addFriendAdver(friendAdver)) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		friendAdver = service.getFriendAdver(" user_id=" + loginUser.getId()
				+ " order by id desc ");
		// macq_2007-6-19_交友广告5分钟内只出现一条_start
		synchronized (lock) {
			if (System.currentTimeMillis() - SendMsgChangeTime > SendMsgTime) {
				JCRoomContentBean jcRoomContent = new JCRoomContentBean();
				jcRoomContent.setFromId(loginUser.getId());
				jcRoomContent.setToId(0);
				jcRoomContent.setFromNickName(loginUser.getNickName());
				jcRoomContent.setToNickName("" + friendAdver.getId());
				jcRoomContent.setContent("我要交友，谁要应征吗？");
				jcRoomContent.setAttach("");
				jcRoomContent.setIsPrivate(0);
				jcRoomContent.setRoomId(0);
				jcRoomContent.setSecRoomId(-1);
				jcRoomContent.setMark(3);
				chatService.addContent(jcRoomContent);

				chatService.updateJCRoomContentCount("count=count+1",
						"room_id=10000000");
				SendMsgChangeTime = System.currentTimeMillis();
			}
		}
		// macq_2007-6-19_交友广告5分钟内只出现一条_end
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}

}
