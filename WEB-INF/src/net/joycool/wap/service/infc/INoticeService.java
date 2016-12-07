/*
 * Created on 2006-4-28
 *
 */
package net.joycool.wap.service.infc;

import java.util.Vector;

import net.joycool.wap.bean.NoticeBean;

/**
 * @author lbj
 * 
 */
public interface INoticeService {
	public boolean addNotice(NoticeBean notice);

	public boolean updateNotice(String set, String condition);

	public boolean deleteNotice(String condition);

	public int getNoticeCount(String condition);

	public NoticeBean getNotice(String condition);

	public Vector getNoticeList(String condition);

	// macq_2006-10-16_获取系统消息id_start
	public Vector getNoticeListById(String condition);
	// macq_2006-10-16_获取系统消息id＿end

	// wucx2006-10-16START
	public String getNoticeID(String condition);
	// wucx2006-10-16end
}
