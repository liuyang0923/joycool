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
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author zhul 2006-06-21 交友中心模块，发表帖图评论部分
 *  由postMap.jsp调用此类，功能：检查并保存用户评论内容
 *         成功后提示用户发表成功，完成此步流程
 */
public class PostMapAction extends BaseAction {
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
		DynaActionForm attForm = (DynaActionForm) form;
		String friendAdverId = attForm.getString("title");
		String content = attForm.getString("content");
		FormFile file = (FormFile) attForm.get("attachment");
		/**
		 * 检查参数，如果格式有误直接返回用户输入页并给出提示信息！
		 */
		request.setAttribute("content", content);
		if (content == null || content.equals("")) {
			tip = "内容不能为空！";
			request.setAttribute("tip", tip);
			request.setAttribute("id",friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			request.setAttribute("id",friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查文件大小
		int fileSize = file.getFileSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ATTACH_SIZE) {
			tip = "文件不能为空，或者文件大小太大！";
			file.destroy();
			request.setAttribute("tip", tip);
			request.setAttribute("id",friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查扩展名
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			file.destroy();
			request.setAttribute("tip", tip);
			request.setAttribute("id",friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 上传文件
		String filePath = Constants.FRIENDADVER_FILE_PATH;
		String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
		if (!FileUtil.uploadFile(file, filePath, fileURL)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			request.setAttribute("id",friendAdverId);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 内容无误，文件上传成功后，将信息存入数据库
		FriendAdverMessageBean friendAdverM = new FriendAdverMessageBean();
		friendAdverM.setFriendAdverId(Integer.parseInt(friendAdverId));
		friendAdverM.setContent(content);
		friendAdverM.setAttachment(fileURL);
		friendAdverM.setUserId(loginUser.getId());
		friendAdverM.setUserNickname(loginUser.getNickName());
		if (!service.addFriendAdverMessage(friendAdverM)) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		request.setAttribute("result", "success");
		request.setAttribute("id", friendAdverId);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
