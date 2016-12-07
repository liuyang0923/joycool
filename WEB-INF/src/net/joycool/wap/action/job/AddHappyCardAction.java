package net.joycool.wap.action.job;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.job.HappyCardBean;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class AddHappyCardAction extends Action {

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
		IJobService jobService = ServiceFactory.createJobService();
		String tip = null;
		DynaActionForm happyCardForm = (DynaActionForm) form;
		String title = happyCardForm.getString("title");
		String content = happyCardForm.getString("content");
		FormFile file = (FormFile) happyCardForm.get("image");
		int typeId = StringUtil.toInt(request.getParameter("id"));
		int categoryId = StringUtil.toInt(request.getParameter("categoryId"));
		request.setAttribute("typeId", typeId + "");
		request.setAttribute("categoryId", categoryId + "");
		if (title == null || title.equals("")) {
			tip = "名称不能为空";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (content == null || content.equals("")) {
			tip = "描述不能为空";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		HappyCardBean cardBean = null;

		cardBean = jobService.getHappyCard("title='" + StringUtil.toSql(title) + "' and type_id="
				+ typeId);
		if (cardBean != null) {
			request.setAttribute("tip", "贺卡已经存在!");
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
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 上传文件
		String filePath = Constants.HAPPY_CARD_IMG_PATH;
		String fileName = file.getFileName();
		if (!FileUtil.uploadFile(file, filePath, fileName)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		if (cardBean == null) {
			if (null == request.getParameter("update")) {
				cardBean = new HappyCardBean();
				cardBean.setTitle(title);
				cardBean.setContent(content);
				cardBean.setImage(fileName);
				cardBean.setTypeId(typeId);
				if (!jobService.addHappyCard(cardBean)) {
					request.setAttribute("tip", "数据库操作有误!检查数据库!");
					return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
				}
			} else {
				int cardId = StringUtil.toInt(request.getParameter("update"));
				jobService.updateHappyCard("title='" + StringUtil.toSql(title) + "',content='"
						+ StringUtil.toSql(content) + "',image='" + StringUtil.toSql(fileName) + "'", "id="
						+ cardId);
			}
		}

		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}

}
