/*
 * Created on 2005-7-29
 *
 */
package net.joycool.wap.action.chat;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IChatService;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.service.infc.IUserService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.ContentList;
import net.joycool.wap.util.FileUtil;
import net.joycool.wap.util.RoomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;

/**
 * @author lbj
 * 
 */
public class InsertAttachAction extends BaseAction {
	static IChatService chatService = ServiceFactory.createChatService();
	static IUserService userService = ServiceFactory.createUserService();
	static IForumMessageService service = ServiceFactory.createForumMessageService();

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		int roomId = StringUtil.toInt(request.getParameter("roomId"));
		JCRoomChatAction jcRoomChat = new JCRoomChatAction();
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

		// zhul 2006-07-12 判断如果一个人在同一个聊天室发言内容与上次相同，返回提示信息 start
		// if(Integer.parseInt(roomId)==0)
		// {
		String isPrivate = request.getParameter("privateSubmit");
//		if (isPrivate == null || "joycoolnulluser".equals(to)) {
//			String lastContent = (String) request.getSession().getAttribute(
//					"lastContent");
//			String lastRoomId = (String) request.getSession().getAttribute(
//					"lastRoomId");
//			if (lastContent != null && lastRoomId.equals(roomId)) {
//				if (lastContent.equals(content)
//						|| lastContent.indexOf(content) != -1
//						|| (content).indexOf(lastContent) != -1) {
//					request.setAttribute("result", "failure");
//					request.setAttribute("tip", "包含相同的内容，不能连续发两遍.");
//					return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
//				}
//			}
//			request.getSession().setAttribute("lastRoomId", roomId);
//			request.getSession().setAttribute("lastContent", content);
//		}
		// }
		// zhul 2006-07-12 判断如果一个人发言内容与上次相同，返回提示信息 end

		// 判断脏话
		Vector contentlist = ContentList.getContentList();
		if (contentlist != null) {
			int count = contentlist.size();
			String conName = null;
			for (int i = 0; i < count; i++) {
				conName = (String) contentlist.get(i);
				if (content.contains(conName)) {
					if (to != null && "joycoolnulluser".equals(to)) {
						request.setAttribute("result", "failure");
						request.setAttribute("tip", "请注意您的发言内容。");
						return mapping
								.findForward(Constants.ACTION_SUCCESS_KEY);
					}
					isPrivate = "1";
					// content = content.replace(conName, "***");
					chatService.addForBID(String.valueOf(loginUser.getId()));
					// chatService.updateOnlineUser("room_id=1", "user_id="
					// + loginUser.getId());
					// //zhul 当用户换房间时进行房间转换记录 start
					// JCRoomChatAction roomAction = new JCRoomChatAction();
					// roomAction.dealRoomTransform(loginUser, Integer
					// .parseInt(roomId), 1);
					// // zhul 当用户换房间时进行房间转换记录 end

					// zhul_2006-08-09 modify 挂线 start
					// fanys 2006-09-16 start 聊天室在线人数
					// chatService.addOnlineUser("1," + loginUser.getId()
					// + ",now()");
					RoomUtil.addRoomOnlineUser(1, loginUser.getId());
					// fanys 2006-09-16 end
					JCRoomChatAction roomAction = new JCRoomChatAction();
					roomAction.dealRoomTransform(loginUser, -1, 1);
					// zhul_2006-08-09 modify 挂线 end

					roomId = 1;
					break;
				}
			}
		}
		// 判断输入项
		if (content == null || content.replace(" ", "").equals("")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请填写内容。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		FormFile file = (FormFile) dynaForm.get("file");
		if(file == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请选择上传的文件。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		String fileExt = StringUtil.convertNull(
				FileUtil.getFileExt(file.getFileName())).toLowerCase();
		int fileSize = file.getFileSize();
		if (to == null) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请选择接收者。");
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 检查文件大小
		else if (fileSize == 0 || fileSize > 51200) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "图片最大50k。");
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 检查图片格式
		else if (!fileExt.equals("jpg") && !fileExt.equals("jpeg")
				&& !fileExt.equals("png") && !fileExt.equals("gif")
				&& !fileExt.equals("wbmp")) {
			request.setAttribute("result", "failure");
			request.setAttribute("tip", "请上传jpg、png、gif或wbmp格式的图片。");
			file.destroy();
			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// libj 2006-07-09 判断是否是女性聊天室 start
//		else if ("2".equals(roomId)
//				&& loginUser.getGender() == 1
//				&& ("joycoolnulluser".equals(to) || request
//						.getParameter("privateSubmit") == null)) {
//			request.setAttribute("result", "failure");
//			request.setAttribute("tip", "对不起,本聊天室只允许女性用户发言.请给对方发私聊信息.");
//			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
//		}
		// libj 2006-07-09 判断是否是女性聊天室 end
		// 对所有人
		else if ("joycoolnulluser".equals(to)) {

			JCRoomContentBean roomContent = new JCRoomContentBean();
			roomContent.setContent(content);
			roomContent.setFromId(loginUser.getId());
			if (loginUser.getNickName() == null
					|| loginUser.getNickName().equals("v")
					|| loginUser.getNickName().replace(" ", "").equals("")) {
				roomContent.setFromNickName("乐客" + loginUser.getId());
			} else {
				roomContent.setFromNickName(loginUser.getNickName());
			}
			// 上传文件
			String filePath = JCRoomContentBean.ATTACH_ROOT;
			String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
			if (!FileUtil.uploadImage(file, filePath, fileURL,true)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "文件上传失败！");
				file.destroy();
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
//			FileUtil.dealImage(filePath + "/" + fileURL,
//					Constants.IMAGE_THUMBNAIL_WIDTH, filePath + "/"
//							+ FileUtil.getThumbnailName(fileURL));
			roomContent.setAttach(fileURL);
			roomContent.setIsPrivate(0);

			// zhul_2006-08-22 add for chat model start
			int[] secRoomId = JCRoomChatAction.getSecondRoomId(loginUser
					.getId(), 0, roomId);
			roomContent.setSecRoomId(secRoomId[1]);
			roomContent.setRoomId(secRoomId[0]);
			// zhul_2006-08-22 add for chat model end
			jcRoomChat.addContent(roomContent);
			// mcq_1_增加用户经验值 时间:2006-6-11
			// 增加用户经验值
			RankAction.addPoint(loginUser,
					net.joycool.wap.util.Constants.RANK_GENERAL);
			// mcq_end
			request.setAttribute("result", "success");

			// add by zhangyi 2006-07-13 start
			// 给贴图模块添加信息
//			String filePath1 = Constants.TIETU_FILE_PATH;
//			String fileURL1 = FileUtil.getUniqueFileName() + "." + fileExt;
//			if (!FileUtil.uploadImage(file, filePath1, fileURL1,true)) {
//				request.setAttribute("result", "failure");
//				request.setAttribute("tip", "文件上传失败！");
//				file.destroy();
//				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
//			}
//			ForumMessageBean forumMessage = new ForumMessageBean();
//			forumMessage.setContent("");
//			forumMessage.setForumId(14);
//			forumMessage.setAttachment(fileURL1);
//			forumMessage.setParentId(0);
//			forumMessage.setTitle("贴张美图，不看后悔！");
//			forumMessage.setUserId(loginUser.getId());
//			forumMessage.setUserNickname(loginUser.getNickName());
//
//			
//			if (!service.addForumMessage(forumMessage)) {
//				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//			}
//			// 添加贴图统计数
//			chatService.updateJCRoomContentCount(" count=count+1", " room_id="
//					+ Constants.TIETU_TOTAL_COUNT_ID);
			// add by zhangyi 2006-07-13 end

			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
		// 对某个用户
		else {
			int toUserId = StringUtil.toId(request.getParameter("to"));
			UserBean toUser = UserInfoUtil.getUser(toUserId);
			if (toUser == null) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "对方不在线。");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			if (toUserId == loginUser.getId()) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "不能给您自己发信息。");
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
			//macq_2007-7-3_用户黑名单里的用户不能给用户贴图
			if (loginUser.getId() != 431 && loginUser.getId() != 519610
					&& loginUser.getId() != 914727) {
				if (userService.isUserBadGuy(toUser.getId(), loginUser
						.getId())) {
					request.setAttribute("result", "failure");
					request.setAttribute("tip", "你在对方的黑名单里，不能给他贴图！");
					return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
				}
			}
			JCRoomContentBean roomContent = new JCRoomContentBean();
			roomContent.setContent(content);
			roomContent.setFromId(loginUser.getId());
			if (loginUser.getNickName() == null
					|| loginUser.getNickName().equals("v")
					|| loginUser.getNickName().replace(" ", "").equals("")) {
				roomContent.setFromNickName("乐客" + loginUser.getId());
			} else {
				roomContent.setFromNickName(loginUser.getNickName());
			}
			roomContent.setToId(toUser.getId());
			if (toUser.getNickName() == null
					|| toUser.getNickName().equals("v")
					|| toUser.getNickName().replace(" ", "").equals("")) {
				roomContent.setToNickName("乐客" + toUser.getId());
			} else {
				roomContent.setToNickName(toUser.getNickName());
			}
			if (isPrivate != null) {
				roomContent.setIsPrivate(1);
			} else {
				roomContent.setIsPrivate(0);
			}
			// 上传文件
			String filePath = JCRoomContentBean.ATTACH_ROOT;
			String fileURL = FileUtil.getUniqueFileName() + "." + fileExt;
			if (!FileUtil.uploadImage(file, filePath, fileURL, true)) {
				request.setAttribute("result", "failure");
				request.setAttribute("tip", "文件上传失败！");
				file.destroy();
				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
			}
//			FileUtil.dealImage(filePath + "/" + fileURL,
//					Constants.IMAGE_THUMBNAIL_WIDTH, filePath + "/"
//							+ FileUtil.getThumbnailName(fileURL));
			roomContent.setAttach(fileURL);
			roomContent.setRoomId(roomId);
			jcRoomChat.addContent(roomContent);
			if(fileURL != null && fileURL.length() != 0)
				SqlUtil.executeUpdate("insert into img_check set id2=" + roomContent.getId() + ",type=3,create_time=now(),file='" + fileURL + "',bak=''");
			// mcq_1_增加用户经验值 时间:2006-6-11
			// 增加用户经验值
			RankAction.addPoint(loginUser,
					net.joycool.wap.util.Constants.RANK_GENERAL);
			// mcq_end
			request.setAttribute("result", "success");

			// add by zhangyi 2006-07-13 start
			// 给贴图模块添加信息
//			String filePath1 = Constants.TIETU_FILE_PATH;
//			String fileURL1 = FileUtil.getUniqueFileName() + "." + fileExt;
//			if (!FileUtil.uploadImage(file, filePath1, fileURL1, true)) {
//				request.setAttribute("result", "failure");
//				request.setAttribute("tip", "文件上传失败！");
//				file.destroy();
//				return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
//			}
//			ForumMessageBean forumMessage = new ForumMessageBean();
//			forumMessage.setContent("");
//			forumMessage.setForumId(14);
//			forumMessage.setAttachment(fileURL1);
//			forumMessage.setParentId(0);
//			forumMessage.setTitle("贴张美图，不看后悔！");
//			forumMessage.setUserId(loginUser.getId());
//			forumMessage.setUserNickname(loginUser.getNickName());
//
//			if (!service.addForumMessage(forumMessage)) {
//				return mapping.findForward(Constants.SYSTEM_FAILURE_KEY);
//			}
//			// 添加贴图统计数
//			chatService.updateJCRoomContentCount(" count=count+1", " room_id="
//					+ Constants.TIETU_TOTAL_COUNT_ID);
			// add by zhangyi 2006-07-13 end

			return mapping.findForward(Constants.ACTION_SUCCESS_KEY);
		}
	}
}
