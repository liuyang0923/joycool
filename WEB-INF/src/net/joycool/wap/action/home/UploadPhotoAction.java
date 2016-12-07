package net.joycool.wap.action.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.bean.home.HomeUserBean;
import net.joycool.wap.cache.util.HomeCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IHomeService;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
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
 * @author zhul 2006-06-21 我的家园上传照片
 */
public class UploadPhotoAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		int maxCount = 30;
//		if (SqlUtil.isTest){
//			maxCount = 3;
//		}
		IHomeService homeService = ServiceFactory.createHomeService();
		UserBean loginUser = getLoginUser(request);
		String tip = null;
		// macq_2007-1-29_判断用户发表照片是否超过5张_start
//		String sql = "SELECT count(id) from jc_home_photo where user_id="+ loginUser.getId();
//		int photoCount = SqlUtil.getIntResult(sql,Constants.DBShortName);
		HomeUserBean homeUser = HomeCacheUtil.getHomeCache(loginUser.getId());
		if (homeUser == null){
			tip = "家园用户不存在.";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		int photoCount = homeUser.getPhotoCount();
		if(photoCount>=maxCount){
			tip = "限制只能上传30张照片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// macq_2007-1-29_判断用户发表照片是否超过5张_end
		/**
		 * 取得参数
		 */
		DynaActionForm attForm = (DynaActionForm) form;
		String content = StringUtil.noEnter(attForm.getString("content"));
		int catId = StringUtil.toInt(attForm.getString("age"));
		FormFile file = (FormFile) attForm.get("attachment");
		/**
		 * 检查参数，如果格式有误直接返回用户输入页并给出提示信息！
		 */
		request.setAttribute("content", content);
		if (content == null || content.equals("")) {
			tip = "标题不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (content.length() > 18){
			tip = "标题太长！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (file == null || file.getFileSize() == 0) {
			tip = "请上传图片！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("home",loginUser.getId());
		if(forbid != null) {
			request.setAttribute("tip", "已经被封禁家园权限 - " + forbid.getBak());
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
		jc.imglib.ImgPoolBean pool = jc.util.ImageUtil.uploadImage(file.getFileData(), fileExt, loginUser.getId(), 5);
		if (pool == null) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		
		// 内容无误，文件上传成功后，将信息存入数据库
		HomePhotoBean photo = new HomePhotoBean();
		photo.setUserId(loginUser.getId());
		photo.setTitle(content);
		photo.setAttach(pool.getUseFileName());
		photo.setCatId(catId);
		if (!homeService.addHomePhoto(photo)) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		request.setAttribute("cid", new Integer(photo.getCatId()));

		jc.util.ImageUtil.insertCheck(pool, 5, photo.getId());
		
		//上传动态 add by leihy 08-12-21
		ActionTrend.addTrend(loginUser.getId(), BeanTrend.TYPE_DIARY, "%1上传照片%3", loginUser.getNickName(), content, "/home/homePhoto.jsp?userId="+loginUser.getId()+"&amp;cid="+catId);
		
		
		// 更新总数相片武翠霞
		// macq_2006-12-20_增加家园的缓存_start
		HomeCacheUtil.updateHomeCacheById(
				"photo_count=photo_count+1 ,last_modify_time = now()",
				"user_id=" + loginUser.getId(), loginUser.getId());
		// homeService.updateHomeUser("photo_count=photo_count+1
		// ,last_modify_time = now()","user_id="+loginUser.getId());
		// macq_2006-12-20_增加家园的缓存_end
		
		// 相应的分类+1
		if (catId == 0){
			SqlUtil.executeUpdate("update jc_home_user set photo_def_count=photo_def_count+1 where user_id=" + loginUser.getId(), 0);
		} else {
			SqlUtil.executeUpdate("update jc_home_photo_cat set count=count+1 where uid=" + loginUser.getId() + " and id=" + catId, 0);
		}
		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
