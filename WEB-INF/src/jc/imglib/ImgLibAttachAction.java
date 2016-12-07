package jc.imglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

public class ImgLibAttachAction extends BaseAction{

	public static ImgLibService service = new ImgLibService();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		    
			UserBean loginUser = getLoginUser(request);
			LibUser libUser = service.getLibUser(" user_id=" + loginUser.getId());
			if (libUser == null){
				request.setAttribute("tip", "用户不存在.");
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			
			if (libUser.getCount() >= ImgLibAction.MAX_COUNT){
				request.setAttribute("tip", "上传失败!当前图片数已达图库容量上限,请删除一些图片后在进行上传!");
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			
			DynaActionForm dynaForm = (DynaActionForm) form;
			String title = StringUtil.noEnter(dynaForm.getString("title"));
			FormFile file = (FormFile) dynaForm.get("file");
			if("".equals(title) || title.length() > 12){
				request.setAttribute("tip", "您没有输入标题或标题过长,请重新输入.");
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			if(file == null) {
				request.setAttribute("tip", "您还没有上传图片.");
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			String fileExt = StringUtil.convertNull(
					FileUtil.getFileExt(file.getFileName())).toLowerCase();
			int fileSize = file.getFileSize();
			// 检查文件大小
			if (fileSize == 0 || fileSize > 51200) {
				request.setAttribute("tip", "图片最大50k.");
				file.destroy();
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// 检查图片格式
			else if (!fileExt.equals("jpg") && !fileExt.equals("jpeg")
					&& !fileExt.equals("png") && !fileExt.equals("gif")
					&& !fileExt.equals("wbmp")) {
				request.setAttribute("tip", "请上传jpg、png、gif或wbmp格式的图片.");
				file.destroy();
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			// 上传文件
			String filePath = ImgLibAction.ATTACH_ROOT;
			String fileURL = FileUtil.uploadImage(file, filePath, fileExt);
			if (fileURL == null) {
				request.setAttribute("tip", "文件上传失败!");
				file.destroy();
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			
			if (loginUser != null){
				Lib lib  = new Lib();
				lib.setUserId(loginUser.getId());
				lib.setTitle(title);
				lib.setImg("o.gif");
				lib.setFlag(0);
				lib.setId(service.addNewLib(lib));
				
				if(fileURL != null){
					SqlUtil.executeUpdate("insert into img_check set id2=" + lib.getId() + ",type=6,create_time=now(),file='" + fileURL + "',bak=''");
				}
				// 成功
//				SqlUtil.executeUpdate("update img_lib_user set `count`=`count`+1 where user_id=" + loginUser.getId(), 5);
//	   			LibLog log = new LibLog();
//	   			log.setUserId(loginUser.getId());
//	   			log.setTitle(title);
//	   			log.setFlag(0);
//	   			log.setImgId(lib.getId());
//				service.addNewLog(log);
			} else {
				// 失败
				request.setAttribute("tip", "文件上传失败!");
				file.destroy();
				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
			}
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}