package net.joycool.wap.action.friend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.friend.FriendCartoonBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class AddCartoonAction extends Action {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		IFriendService friendService = ServiceFactory.createFriendService();
		String tip = null;
		DynaActionForm cartoonForm = (DynaActionForm) form;

		int typeId = StringUtil.toInt(request.getParameter("id"));
		if (typeId < 1) {
			tip = "请选择卡通图片类型!";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		FormFile file = (FormFile) cartoonForm.get("image");
		request.setAttribute("typeId", typeId + "");
		if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查文件大小
		int fileSize = file.getFileSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ATTACH_SIZE) {
			tip = "文件不能为空，或者文件大小太大！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 检查扩展名
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		if (fileExt.equals("")
				|| Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		FriendCartoonBean cardBean = null;
		String name = typeId + "_" + file.getFileName();
		name = name.substring(0, name.length() - fileExt.length() - 1);
		cardBean = friendService.getFriendCartoon("pic='" + name + "'");
		if (cardBean != null) {
			request.setAttribute("tip", "该卡通图片已经存在!");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 上传文件
		String filePath = Constants.FRIEND_FILE_PATH;
		name = typeId + "_" + file.getFileName();
		if (!FileUtil.uploadFile(file, filePath, name)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		filePath = "E:\\eclipse\\workspace\\joycool-portal\\img\\friend\\attach\\";
		if (!FileUtil.uploadFile(file, filePath, name)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		if (cardBean == null) {
			if (null == request.getParameter("update")) {
				cardBean = new FriendCartoonBean();
				name = name.substring(0, name.length() - fileExt.length() - 1);
				cardBean.setPic(name);
				cardBean.setType(typeId);
				if (!friendService.addFriendCartoon(cardBean)) {
					request.setAttribute("tip", "数据库操作有误!检查数据库!");
					return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
				}
			}
		}
		OsCacheUtil.flushGroup(OsCacheUtil.CARTOON_GROUP, "type=" + typeId);
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}

}
