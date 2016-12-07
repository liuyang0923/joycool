/*
 * Created on 2005-7-29
 *
 */
package net.joycool.wap.action.jcforum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.ForbidUtil;
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
public class ForumContentAttachAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		UserBean loginUser = getLoginUser(request);
		//if (loginUser == null) {
		//	return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		//}
		DynaActionForm dynaForm = (DynaActionForm) form;
		int forumId = StringUtil.toInt(dynaForm.getString("forumId"));
		String title = StringUtil.noEnter(dynaForm.getString("title"));
		String content = StringUtil.removeCtrlAsc(dynaForm.getString("content"));

		// 判断输入项
		if (forumId <= 0) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数错误。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		ForumBean forum = ForumCacheUtil.getForumCache(forumId);
		if (forum==null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "参数错误。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		if (title == null || title.equals("")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请填写标题内容。");
			request.setAttribute("forum", forum);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		if (content == null || content.equals("")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请填写内容。");
			request.setAttribute("forum", forum);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		FormFile file = (FormFile) dynaForm.get("file");
		if(file == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "没有上传图片");
			request.setAttribute("forum", forum);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		int fileSize = file.getFileSize();
		// 检查文件大小
		if (fileSize == 0 || fileSize > 51200) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "图片最大50k。");
			request.setAttribute("forum", forum);
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 检查图片格式
		else if (!fileExt.equals("jpg") && !fileExt.equals("jpeg")
				&& !fileExt.equals("png") && !fileExt.equals("gif")
				&& !fileExt.equals("wbmp")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请上传jpg、png、gif或wbmp格式的图片。");
			request.setAttribute("forum", forum);
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

//		FileUtil.dealImage(filePath + "/" + fileURL,
//				Constants.IMAGE_THUMBNAIL_WIDTH, filePath + "/"
//						+ FileUtil.getThumbnailName(fileURL));
		if (session.getAttribute("forumcontent") != null) {
			String infos = (String) session.getAttribute("fourmrepeat");
			String info = title;
			if (loginUser != null) {
				info = title + " " + loginUser.getId();
			}
			if (!(info.equals(infos))) {
				if (forumId > 0) {
					String fourmSTime = (String) session
							.getAttribute("fourmSTime");
					if (fourmSTime != null) {
						long time = StringUtil.toLong(fourmSTime);
						long cTime = System.currentTimeMillis();
						long count = cTime - time;
						if (count < 30 * 1000) {
							request.setAttribute("tip", "你的发文太快了！请先休息一会再继续！");
							request.setAttribute("result", "failure");
							request.setAttribute("forum", forum);
							return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
						}
					}
					if (forum.getType() == 1 && loginUser == null) {
						String url = response
								.encodeURL("/user/login.jsp?backTo=/jcforum/index.jsp");
						String link = "<a href=\"" + url + "\">登录</a>";
						request.setAttribute("tip", "此板块不允许游客发言，请" + link + "后在发言!");
						request.setAttribute("result", "failure");
						request.setAttribute("forum", forum);
						return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
					}
					if(forum.getType() == 2) {
						request.setAttribute("tip", "此板块不允许发言!");
						request.setAttribute("result", "failure");
						request.setAttribute("forum", forum);
						return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
					}
					request.setAttribute("forum", forum);
					if (loginUser != null) {
						ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",loginUser.getId());
						if(forbid != null) {
							request.setAttribute("tip", "已经被禁止发贴 - " + forbid.getBak());
							request.setAttribute("result", "failure");
							return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
						}
						forbid = ForbidUtil.getForbid("f" + forumId, loginUser.getId());
						if(forbid != null) {
							request.setAttribute("tip", "已经被禁止发贴 - " + forbid.getBak());
							request.setAttribute("result", "failure");
							return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
						}
					}
					ForumContentBean contentBean = new ForumContentBean();
					contentBean.setContent(content.trim());
					contentBean.setForumId(forumId);
					if (loginUser != null) {
						contentBean.setUserId(loginUser.getId());
					} else {
						contentBean.setUserId(0);
					}
					contentBean.setTitle(title.trim());
					
					// 上传文件
					jc.imglib.ImgPoolBean pool = jc.util.ImageUtil.uploadImage(file.getFileData(), fileExt, loginUser.getId(), 1);
					
					if (pool == null) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "文件上传失败！");
						request.setAttribute("forum", forum);
						file.destroy();
						return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
					}
					
					contentBean.setAttach(pool.getUseFileName());

					ForumCacheUtil.addForumContent(contentBean, forum);
					// 添加之后才能写记录，此时contentBean.getId才有
					jc.util.ImageUtil.insertCheck(pool, 1, contentBean.getId());
					
					session.setAttribute("fourmSTime", System
							.currentTimeMillis()
							+ "");
					session.removeAttribute("forumcontent");
					session.setAttribute("fourmrepeat", info);
				}
			} else {
				request.setAttribute("tip", "包含相同的主题，不能连续发两遍.");
				request.setAttribute("result", "failure");
				request.setAttribute("forum", forum);
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
		}
		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
