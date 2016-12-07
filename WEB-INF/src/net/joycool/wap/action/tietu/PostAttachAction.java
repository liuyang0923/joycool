/*
 * Created on 2005-11-15
 *
 */
package net.joycool.wap.action.tietu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.forum.ForumMessageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author lbj
 * 
 */
public class PostAttachAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IForumMessageService service = ServiceFactory
				.createForumMessageService();

		IChatService chatService = ServiceFactory.createChatService();

		UserBean loginUser = getLoginUser(request);
		String tip = null;
		boolean flag = true;
		/**
		 * 取得参数
		 */
		DynaActionForm attForm = (DynaActionForm) form;
		String title = attForm.getString("title");
		String content = attForm.getString("content");
		FormFile file = (FormFile) attForm.get("attachment");

		int forumId = Integer.parseInt(request.getParameter("forumId"));
		int parent = Integer.parseInt(request.getParameter("parent"));

		/**
		 * 检查参数
		 */
		if (title == null || title.equals("")) {
			tip = "标题不能为空！";
			flag = false;
			// } else if (content == null || content.equals("")) {
			// tip = "内容不能为空！";
			// flag = false;
		} else if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			flag = false;
		}

		// 检查文件大小
		int fileSize = file.getFileSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ATTACH_SIZE) {
			tip = "文件不能为空，或者文件大小太大！";
			flag = false;
			file.destroy();
		}

		// 检查扩展名
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			flag = false;
			file.destroy();
		}

		// 上传文件
		String filePath = Constants.TIETU_FILE_PATH;
		String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
		if (!FileUtil.uploadFile(file, filePath, fileURL)) {
			tip = "文件上传失败！";
			flag = false;
			file.destroy();
		}

		// 填写信息不正确
		if (flag == false) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", tip);
			request.setAttribute("forumId", new Integer(forumId));
			request.setAttribute("parent", new Integer(parent));
		}
		// 填写信息正确
		else {
			ForumMessageBean forumMessage = new ForumMessageBean();
			forumMessage.setContent(content);
			forumMessage.setForumId(forumId);
			forumMessage.setAttachment(fileURL);
			forumMessage.setParentId(parent);
			forumMessage.setTitle(title);
			forumMessage.setUserId(loginUser.getId());
			forumMessage.setUserNickname(loginUser.getNickName());

			if (!service.addForumMessage(forumMessage)) {
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// 添加贴图统计数
			chatService.updateJCRoomContentCount(" count=count+1", " room_id="
					+ Constants.TIETU_TOTAL_COUNT_ID);
			// 给聊天室发一个公告
			ForumMessageBean forumMessageBean = service
					.getForumMessage("user_id=" + loginUser.getId()
							+ " order by id desc");
			int id = forumMessageBean.getId();
			//liuyi 2006-09-16 聊天室加缓存 start
			JCRoomContentBean jcRoomContent = new JCRoomContentBean();
			jcRoomContent.setFromId(loginUser.getId());
			jcRoomContent.setToId(0);
			jcRoomContent.setFromNickName(loginUser.getNickName());
			jcRoomContent.setToNickName("" + id);
			jcRoomContent.setContent("我贴了张绝美的图，快来看看吧！");
			jcRoomContent.setAttach("");
			jcRoomContent.setIsPrivate(0);
			jcRoomContent.setRoomId(0);
			jcRoomContent.setSecRoomId(-1);
			jcRoomContent.setMark(4);
//			chatService.addContent(loginUser.getId() + ",0,'"
//					+ loginUser.getNickName() + "','" + id
//					+ "','我贴了张绝美的图，快来看看吧！'" + ",'',now(),0,0,-1,4");
			chatService.addContent(jcRoomContent);
			//liuyi 2006-09-16 聊天室加缓存 end
			// 清空聊天室在map中的记录
			//RoomCacheUtil.flushRoomContentId(0);
			// 清空聊天室在map中的记录
			request.setAttribute("result", "success");
			request.setAttribute("forumId", new Integer(forumId));
			request.setAttribute("parent", new Integer(parent));
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
