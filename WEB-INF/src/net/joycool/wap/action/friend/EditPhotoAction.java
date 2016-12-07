package net.joycool.wap.action.friend;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.friend.FriendBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IFriendService;
import net.joycool.wap.spec.admin.AdminAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author macq_2006-10-30 交友中心个人相片上传
 */
public class EditPhotoAction extends BaseAction {
	static IFriendService friendService = ServiceFactory.createFriendService();
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String tip = null;
		
		UserBean loginUser = getLoginUser(request);
		FriendBean friend = friendService.getFriend(loginUser.getId());
		/**
		 * 取得参数
		 */
		DynaActionForm attForm = (DynaActionForm) form;
		FormFile file = (FormFile) attForm.get("attachment");
		if (friend == null) {
			tip = "查询的内容不存在.";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		/**
		 * 检查参数，如果格式有误直接返回用户输入页并给出提示信息！
		 */
		if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("info",loginUser.getId());
		if(forbid != null) {
			tip = "已经被封禁修改权限 - " + forbid.getBak();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		// 检查文件大小
		int fileSize = file.getFileSize();
		if (fileSize == 0 || fileSize > Constants.MAX_ALBUMPHOTO_SIZE) {
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

		// 上传文件
		String filePath = Constants.FRIEND_FILE_PATH;
		// //"E:\\eclipse\\workspace\\joycool-portal\\img/friend";
		// "E:\\eclipse\\workspace\\joycool-portal\\img\\friend\\attach\\";
		String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
		if (!FileUtil.uploadFile(file, filePath, fileURL)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		if(fileURL != null && fileURL.length() != 0)
			SqlUtil.executeUpdate("insert into img_check set id2=" + loginUser.getId() + ",type=4,create_time=now(),file='" + fileURL + "',bak=''");

		// 内容无误，文件上传成功后，将信息存入数据库
		if (!friendService.updateFriend("attach='o.gif'", "user_id="
				+ friend.getUserId())) {

			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		AdminAction.addUserLog(loginUser.getId(), 6, fileURL);
		try {
			friendService.flushFriend(loginUser.getId());
			if (friend.getAttach().indexOf("_") == -1 && friend.getAttach().length() > 5) {
				File f = new File(filePath, friend.getAttach());
				if (f.exists()) {// 检查File.txt是否存在
					f.delete();// 删除File.txt文件
				}
			}

		} catch (Exception e) {
		}
		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
