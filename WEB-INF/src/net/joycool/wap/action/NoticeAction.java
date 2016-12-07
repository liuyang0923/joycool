/*
 * Created on 2006-1-13
 *
 */
package net.joycool.wap.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.framework.BaseAction;
import net.joycool.wap.service.impl.NoticeServiceImpl;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.NewNoticeCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;

public class NoticeAction extends BaseAction {
	public static NoticeServiceImpl noticeService = new NoticeServiceImpl();
	/**
	 * 查看一个通知。
	 * 
	 * @param request
	 */
	public static void viewNotice(HttpServletRequest request) {
		// 取得参数
		int noticeId = StringUtil.toInt(request.getParameter("noticeId"));
		if (noticeId == -1) {
			return;
		}
		String backTo = request.getParameter("backTo");

		UserBean loginUser = (UserBean) request.getSession().getAttribute(
				Constants.LOGIN_USER_KEY);
		if(loginUser == null) {
			return;
		}
		//mcq_2006-10-20_清空用户未读取通知消息计数器_start
		request.getSession().setAttribute(Constants.USER_NOTICE_COUNT,new Integer(0));
		//mcq_2006-10-20_清空用户未读取通知消息计数器_end
		// mcq_2006-10-18_从缓存中获取通知信息_start
		NoticeBean notice = NewNoticeCacheUtil.getNotice(noticeId);
		// mcq_2006-10-18_从缓存中获取通知信息_end

		// notice = noticeService.getNotice(condition);

		if (notice != null) {
			if(notice.getMark()==NoticeBean.HOME_REVIEW){
				Vector noticeList = noticeService.getNoticeList("user_id="+notice.getUserId()+" and status="+NoticeBean.NOT_READ+" and mark ="+NoticeBean.HOME_REVIEW);
				for (int i = 0; i < noticeList.size(); i++) {
					NoticeBean homeNotice = (NoticeBean)noticeList.get(i);
					noticeService.updateNotice("status = " + NoticeBean.READED,
							"id = " + homeNotice.getId());
					// 处理用户消息缓存_start
					NewNoticeCacheUtil.updateUserNoticeById(homeNotice.getUserId(),
							homeNotice.getId());
				}
			}else if(notice.getMark()==NoticeBean.MESSAGE){
				Vector noticeList = noticeService.getNoticeList("user_id="+notice.getUserId()+" and status="+NoticeBean.NOT_READ+" and mark ="+NoticeBean.MESSAGE);
				for (int i = 0; i < noticeList.size(); i++) {
					NoticeBean messageNotice = (NoticeBean)noticeList.get(i);
					noticeService.updateNotice("status = " + NoticeBean.READED,
							"id = " + messageNotice.getId());
					// 处理用户消息缓存_start
					NewNoticeCacheUtil.updateUserNoticeById(messageNotice.getUserId(),
							messageNotice.getId());
				}
			}else if(notice.getMark()==NoticeBean.CHAT){
				Vector noticeList = noticeService.getNoticeList("user_id="+notice.getUserId()+" and status="+NoticeBean.NOT_READ+" and mark ="+NoticeBean.CHAT);
				for (int i = 0; i < noticeList.size(); i++) {
					NoticeBean chatNotice = (NoticeBean)noticeList.get(i);
					noticeService.updateNotice("status = " + NoticeBean.READED,
							"id = " + chatNotice.getId());
					// 处理用户消息缓存_start
					NewNoticeCacheUtil.updateUserNoticeById(chatNotice.getUserId(),
							chatNotice.getId());
				}
			}else if(notice.getMark()==NoticeBean.SENDACTION){
				Vector noticeList = noticeService.getNoticeList("user_id="+notice.getUserId()+" and status="+NoticeBean.NOT_READ+" and mark ="+NoticeBean.SENDACTION);
				for (int i = 0; i < noticeList.size(); i++) {
					NoticeBean chatNotice = (NoticeBean)noticeList.get(i);
					noticeService.updateNotice("status = " + NoticeBean.READED,
							"id = " + chatNotice.getId());
					// 处理用户消息缓存_start
					NewNoticeCacheUtil.updateUserNoticeById(chatNotice.getUserId(),
							chatNotice.getId());
				}
			}else if (notice.getType() == NoticeBean.GENERAL_NOTICE) {
				noticeService.updateNotice("status = " + NoticeBean.READED, "id = " + noticeId);
				// mcq_2006-10-18_处理用户消息缓存_start
				NewNoticeCacheUtil.updateUserNoticeById(notice.getUserId(),
						notice.getId());
				// mcq_2006-10-18_处理用户消息缓存_end
			}else if (notice.getType() == NoticeBean.SYSTEM_NOTICE) {

				// 记录用户已经读取系统通知
				SqlUtil.executeUpdate("insert into jc_notice_history set notice_id = "
						+ notice.getId() + ", user_id = " + loginUser.getId()
						+ ", read_datetime = now()");

				// mcq_2006-10-18_处理系统消息缓存_start
				NewNoticeCacheUtil.updateSystemNoticeById(loginUser.getId(),
						notice.getId());
				// mcq_2006-10-18_处理系统消息缓存_end
			}
			request.setAttribute("notice", notice);
		}
		request.setAttribute("backTo", backTo);
	}
	
	public static void sendNotice(int userId, String title, String content, int type, String hideUrl, String link) {
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setType(type);
		notice.setHideUrl(hideUrl);
		notice.setLink(link);
		noticeService.addNotice(notice);
	}
	
	public static void sendNotice(int userId, String title, int type, String link) {
		NoticeBean notice = new NoticeBean();
		notice.setUserId(userId);
		notice.setTitle(title);
		notice.setType(type);
		notice.setHideUrl("");
		notice.setLink(link);
		noticeService.addNotice(notice);
	}
}
