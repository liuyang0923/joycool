/*
 * 
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
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.StringUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author zhul 2006-06-21 交友中心模块，发表有图片的交友信息部分
 *         页面postAdverAttach.jsp调用此类，功能：检查用户输入的帖图信息并保存 成功后提示用户发表成功，完成此流程
 */
public class PostAttachAction extends BaseAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		IFriendAdverService service = ServiceFactory.createFriendAdverService();
		IChatService chatService = ServiceFactory.createChatService();
		UserBean loginUser = getLoginUser(request);
		String tip = null;
		/**
		 * 取得参数
		 */
		DynaActionForm attForm = (DynaActionForm) form;
		String title = attForm.getString("title");
		String sex = attForm.getString("sex");
		String age = attForm.getString("age");
		String area = attForm.getString("area");
		String content = attForm.getString("content");
		FormFile file = (FormFile) attForm.get("attachment");
		/**
		 * 检查参数，如果信息格式有误直接跳回用户输入页面，并提示用户！
		 */
		request.setAttribute("title", title);
		request.setAttribute("content", content);
		if (title == null || title.equals("")) {
			tip = "标题不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (content == null || content.equals("")) {
			tip = "内容不能为空！";
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		} else if (file == null || file.getFileSize() == 0) {
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
		if (fileExt.equals("") || Constants.ATTACTH_TYPES.indexOf(fileExt) == -1) {
			tip = "文件类型不正确！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 上传文件
		String filePath = Constants.FRIENDADVER_FILE_PATH;
		String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
		if (!FileUtil.uploadFile(file, filePath, fileURL)) {
			tip = "文件上传失败！请重试！";
			file.destroy();
			request.setAttribute("tip", tip);
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}

		// 输入信息无误，上传文件成功后，将用户信息保存到数据库
		FriendAdverBean friendAdver = new FriendAdverBean();
		friendAdver.setTitle(title);
		friendAdver.setSex(Integer.parseInt(sex));
		friendAdver.setAge(Integer.parseInt(age));
		friendAdver.setArea(Integer.parseInt(area));
		friendAdver.setRemark(content);
		friendAdver.setAttachment(fileURL);
		friendAdver.setUserId(loginUser.getId());
		friendAdver.setCityno(loginUser.getCityno());
		friendAdver.setGender(loginUser.getGender());
		if (!service.addFriendAdver(friendAdver)) {
			return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
		}
		friendAdver = service.getFriendAdver(" user_id=" + loginUser.getId()
				+ " order by id desc ");
		//liuyi 2006-09-16 聊天室加缓存 start
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
//		chatService.addContent(loginUser.getId() + ",0,'"
//				+ loginUser.getNickName() + "','" + friendAdver.getId()
//				+ "','我要交友，谁要应征吗？','',now(),0,0,-1,3");
		chatService.addContent(jcRoomContent);
		//liuyi 2006-09-16 聊天室加缓存 end
		// 清空聊天室在map中的记录
		//RoomCacheUtil.flushRoomContentId(0);
		// 清空聊天室在map中的记录
		chatService.updateJCRoomContentCount("count=count+1",
				"room_id=10000000");
		request.setAttribute("result", "success");
		return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
	}
}
