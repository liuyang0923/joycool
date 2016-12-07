/*
 * Created on 2006-4-28
 *
 */
package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.joycool.wap.bean.NoticeBean;
import net.joycool.wap.cache.NoticeCacheUtil;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.service.infc.INoticeService;
import net.joycool.wap.util.NewNoticeCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

/**
 * @author lbj
 * 
 */
public class NoticeServiceImpl implements INoticeService {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#addNotice(net.joycool.wap.bean.NoticeBean)
	 */
	public boolean addNotice(NoticeBean notice) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		String query = "INSERT INTO jc_notice set user_id=?, title=?, content=?, link=?, hide_url=?, status=?, type=?,tong_id=?,mark=?, create_datetime=now()";

		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, notice.getUserId());
			pstmt.setString(2, notice.getTitle());
			pstmt.setString(3, notice.getContent());
			pstmt.setString(4, notice.getLink());
			pstmt.setString(5, notice.getHideUrl());
			pstmt.setInt(6, notice.getStatus());
			pstmt.setInt(7, notice.getType());
			pstmt.setInt(8, notice.getTongId());
			pstmt.setInt(9,notice.getMark());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		
		dbOp.executePstmt();
		
		notice.setId(SqlUtil.getLastInsertId(dbOp, "jc_notice"));

		dbOp.release();

		// lbj_2006-08-07_提高通知效率_start
		// 当用户接收到新通知时，把他从没有新通知的列表中删除
		//liuyi 2006-11-01 结义消息处理 start
		if (notice.getType() == NoticeBean.GENERAL_NOTICE || notice.getType()==NoticeBean.KEEP_NOT_READ_NOTICE) {
			NoticeCacheUtil.removeNoNoticeUserId(notice.getUserId());
			NewNoticeCacheUtil.noticeCache.spt(Integer.valueOf(notice.getId()), notice);
			Vector userGeneralNoticeList = (Vector) NewNoticeCacheUtil.noticeListCache.sgt(notice.getUserId());
			if (userGeneralNoticeList != null) {
				userGeneralNoticeList.add(0, Integer.valueOf(notice.getId()));
			}
		}
		//liuyi 2006-11-01 结义消息处理 end
		// 如果是添加了系统通知，把所有人从没有新通知的列表中删除
		else if (notice.getType() == NoticeBean.SYSTEM_NOTICE) {
			NewNoticeCacheUtil.noticeCache.spt(Integer.valueOf(notice.getId()), notice);
			if(notice.getTongId() == 0) {
				NoticeCacheUtil.removeAll();
				NewNoticeCacheUtil.addSystemNoticeById();
			} else {		// 帮会通知
				List tongUserList = TongCacheUtil.getTongUserListById(notice.getTongId());
				if(tongUserList != null) {
					Iterator iter = tongUserList.iterator();
					while(iter.hasNext()) {
						Integer iid = (Integer)iter.next();
						NoticeCacheUtil.removeNoNoticeUserId(iid.intValue());
						
						Vector tongSystemNoticeList = (Vector) OsCacheUtil.get(String.valueOf(notice.getTongId()),
								OsCacheUtil.TONG_SYSTEM_NOTICE_GROUP,
								OsCacheUtil.TONG_SYSTEM_NOTICE_FLUSH_PERIOD);
						if (tongSystemNoticeList != null) {
							tongSystemNoticeList.add(0, Integer.valueOf(notice.getId()));
						}
					}
				}
			}

		}
		// lbj_2006-08-07_提高通知效率_end
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#updateNotice(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateNotice(String set, String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "UPDATE jc_notice SET " + set + " WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#deleteNotice(java.lang.String)
	 */
	public boolean deleteNotice(String condition) {
		boolean result;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "DELETE FROM jc_notice WHERE " + condition;

		// 执行更新
		result = dbOp.executeUpdate(query);

		// 释放资源
		dbOp.release();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#getNoticeCount(java.lang.String)
	 */
	public int getNoticeCount(String condition) {
		int count = 0;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_notice WHERE "
				+ condition;

		ResultSet rs = dbOp.executeQuery(query);

		try {
			if (rs.next()) {
				count = rs.getInt("c_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#getNotice(java.lang.String)
	 */
	public NoticeBean getNotice(String condition) {
		NoticeBean notice = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		query = query + " LIMIT 0, 1";

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			if (rs.next()) {
				notice = new NoticeBean();
				notice.setId(rs.getInt("id"));
				notice.setTitle(rs.getString("title"));
				notice.setContent(rs.getString("content"));
				notice.setCreateDatetime(rs.getString("create_datetime"));
				notice.setHideUrl(rs.getString("hide_url"));
				notice.setLink(rs.getString("link"));
				notice.setStatus(rs.getInt("status"));
				notice.setType(rs.getInt("type"));
				notice.setUserId(rs.getInt("user_id"));
				notice.setTongId(rs.getInt("tong_id"));
				notice.setMark(rs.getInt("mark"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return notice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.joycool.wap.service.impl.INoticeService#getNoticeList(java.lang.String)
	 */
	public Vector getNoticeList(String condition) {
		Vector noticeList = new Vector();
		NoticeBean notice = null;

		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = "SELECT * FROM jc_notice";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				notice = new NoticeBean();
				notice.setId(rs.getInt("id"));
				notice.setTitle(rs.getString("title"));
				notice.setContent(rs.getString("content"));
				notice.setCreateDatetime(rs.getString("create_datetime"));
				notice.setHideUrl(rs.getString("hide_url"));
				notice.setLink(rs.getString("link"));
				notice.setStatus(rs.getInt("status"));
				notice.setType(rs.getInt("type"));
				notice.setUserId(rs.getInt("user_id"));
				notice.setTongId(rs.getInt("tong_id"));
				notice.setMark(rs.getInt("mark"));
				noticeList.add(notice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 释放资源
		dbOp.release();

		return noticeList;
	}
//	wucx2006-10-16START
	public String getNoticeID(String condition){
		ArrayList noticeId=new ArrayList();
		String idShow=null;
		int notice=0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();
		// 构建更新语句
		String query = "select notice_id from jc_notice_history  ";
		// 数据库操作类
	
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				notice = rs.getInt("notice_id");
				if(notice!=0)
				noticeId.add(notice+"");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		if(noticeId.size()>0)
		{
			idShow=noticeId.toString();
			idShow=idShow.substring(1,idShow.length()-1);
		}
		return idShow;
	}
//	wucx2006-10-16 END
	
	/**
	 * 获取系统消息ID列表 macq_2006-10-16
	 */
	public Vector getNoticeListById(String condition) {
		Vector noticeIdCountList = new Vector();
		int count = 0;
		// JCRoomContentBean roomContent = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation();
		dbOp.init();

		// 构建查询语句
		String query = condition;

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				count = rs.getInt(1);
				noticeIdCountList.add(new Integer(count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		dbOp.release();
		return noticeIdCountList;
	}
}
