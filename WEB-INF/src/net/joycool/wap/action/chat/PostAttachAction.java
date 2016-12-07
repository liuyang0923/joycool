/*
 * Created on 2005-7-29
 *
 */
package net.joycool.wap.action.chat;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.chat.MessageBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IUserService; 
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ContentList;
import net.joycool.wap.util.DateUtil;
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

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserBean loginUser = getLoginUser(request);
		if (loginUser == null) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}

		DynaActionForm dynaForm = (DynaActionForm) form;
		String to = dynaForm.getString("to");
		String backTo = request.getParameter("backTo");
		if (backTo == null) {
			backTo = BaseAction.INDEX_URL;
		}
		request.setAttribute("backTo", backTo);
		String content = dynaForm.getString("content");
		// 判断脏话
		Vector contentlist = ContentList.getContentList();
		if (contentlist != null) {
			int count = contentlist.size();
			String conName = null;
			for (int i = 0; i < count; i++) {
				conName = (String) contentlist.get(i);
				content = content.replace(conName, "***");
			}
		}
		// 判断输入项
		if (content == null || content.replace(" ", "").equals("")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请填写内容。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		FormFile file = (FormFile) dynaForm.get("file");
		String fileExt = StringUtil.convertNull(FileUtil.getFileExt(file.getFileName())).toLowerCase();
		int fileSize = file.getFileSize();
		if (to == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请选择接收者。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 检查文件大小
		else if (fileSize == 0 || fileSize > 1024000) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "图片最大100K。");
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 检查图片格式
		else if (!fileExt.equals("jpg") && !fileExt.equals("jpeg") && !fileExt.equals("png") && !fileExt.equals("gif") && !fileExt.equals("wbmp")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请上传jpg、png、gif或wbmp格式的图片。");
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 对所有人
		else if ("joycoolnulluser".equals(to)) {
			MessageBean message = new MessageBean();
			message.setContent(content);
			message.setDateTime(DateUtil.getCurrentTimeAsStr());
//			message.setFromUserName(loginUser.getUserName());
//			if (loginUser.getNickName() == null || loginUser.getNickName().equals("v") || loginUser.getNickName().replace(" ", "").equals("")) {
//				message.setFromNickName(loginUser.getUserName());
//			} else {
//				message.setFromNickName(loginUser.getNickName());
//			}

			// 上传文件
			String filePath = MessageBean.ATTACH_ROOT;
			String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
			if (!FileUtil.uploadFile(file, filePath, fileURL)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "文件上传失败！");
				file.destroy();
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			FileUtil.dealImage(filePath + "/" + fileURL, Constants.IMAGE_THUMBNAIL_WIDTH, filePath + "/" + FileUtil.getThumbnailName(fileURL));
			message.setAttach(fileURL);

			message.setIsPrivate(0);
			ChatDataAction.addMessage(message);
			ChatDataAction.addPrivateMessage(message);
			request.setAttribute("result", "success");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 对某个用户
		else {
			IUserService userService = ServiceFactory.createUserService();
			UserBean toUser = userService.getUser("user_name = '" + StringUtil.toSql(to) + "'");
			if (toUser == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "对方不在线。");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
//			if (toUser.getUserName().equals(loginUser.getUserName())) {
//				request.setAttribute("result", "failure");
//				request.setAttribute("tip", "不能给您自己发信息。");
//				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
//			}

			MessageBean message = new MessageBean();
			message.setContent(content);
			message.setDateTime(DateUtil.getCurrentTimeAsStr());
//			message.setFromUserName(loginUser.getUserName());
//			if (loginUser.getNickName() == null || loginUser.getNickName().equals("v") || loginUser.getNickName().replace(" ", "").equals("")) {
//				message.setFromNickName(loginUser.getUserName());
//			} else {
//				message.setFromNickName(loginUser.getNickName());
//			}
//			message.setToUserName(toUser.getUserName());
//			if (toUser.getNickName() == null || toUser.getNickName().equals("v") || toUser.getNickName().replace(" ", "").equals("")) {
//				message.setToNickName(toUser.getUserName());
//			} else {
//				message.setToNickName(toUser.getNickName());
//			}
			if (request.getParameter("privateSubmit") != null) {
				message.setIsPrivate(1);
			} else {
				message.setIsPrivate(0);
			}

			// 上传文件
			String filePath = MessageBean.ATTACH_ROOT;
			String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
			if (!FileUtil.uploadFile(file, filePath, fileURL)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "文件上传失败！");
				file.destroy();
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			FileUtil.dealImage(filePath + "/" + fileURL, Constants.IMAGE_THUMBNAIL_WIDTH, filePath + "/" + FileUtil.getThumbnailName(fileURL));
			message.setAttach(fileURL);
			request.setAttribute("result", "success");
			ChatDataAction.addMessage(message);
			ChatDataAction.addPrivateMessage(message);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
	}
}
