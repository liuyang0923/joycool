package net.joycool.wap.service.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import net.joycool.wap.bean.jcforum.ForumActionBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean;
import net.joycool.wap.bean.jcforum.ForumRcmdBean2;
import net.joycool.wap.bean.jcforum.ForumReplyBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;
import net.joycool.wap.bean.jcforum.PrimeCatBean;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.util.db.DbOperation;

public class JcForumServiceImpl implements IJcForumService {
	public boolean addForum(ForumBean bean) {

		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT INTO jc_forum(title,total_count,today_count,description,user_id,mark,tong_id,bad_user) VALUES(?,?,?,?,?,?,?,'')";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setString(1, bean.getTitle());
			pstmt.setInt(2, bean.getTotalCount());
			pstmt.setInt(3, bean.getTodayCount());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getUserId());
			pstmt.setInt(6, bean.getMark());
			pstmt.setInt(7, bean.getTongId());

		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}
	// 简化版，输入forumid的创建
	public boolean addForum2(ForumBean bean) {

		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT INTO jc_forum(id,title,description,user_id,bad_user) VALUES(?,?,?,'','')";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getId());
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getDescription());

		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	/*
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#delFriendVote(java.lang.String)
	 */
	public boolean delForum(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建更新语句
		String query = "DELETE FROM jc_forum WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVote(java.lang.String)
	 */
	public ForumBean getForum(String condition) {
		ForumBean forum = null;
		String key = condition;
		// forum = (ForumBean) OsCacheUtil.get(key,
		// OsCacheUtil.FORUM_GROUP,
		// OsCacheUtil.FORUM__FLUSH_PERIOD);
		if (forum == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation(2);
			// 构建查询语句
			String query = "SELECT * from jc_forum";
			if (condition != null) {
				query = query + " WHERE " + condition;
			}
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				if (rs.next()) {
					forum = this.getForum(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
			// OsCacheUtil.put(key, forum,
			// OsCacheUtil.FORUM_GROUP);
		}
		return forum;
	}

	/*
	 * （
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteCount(java.lang.String)
	 */
	public int getForumCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_forum";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
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
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#getFriendVoteList(java.lang.String)
	 */
	public Vector getForumList(String condition) {
		Vector forumList = new Vector();
		ForumBean forum = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = "SELECT * from jc_forum";
		// 构建更新语句
		if (condition != null) {
			if (condition.toLowerCase().indexOf("left join") != -1) {
				query = condition;
			} else {
				query = query + " WHERE " + condition;
			}
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				forum = this.getForum(rs);
				forumList.add(forum);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return forumList;
	}

	/*
	 * 
	 * 
	 * @see net.joycool.wap.service.infc.IFriendService#updateFriendVote(java.lang.String,
	 *      java.lang.String)
	 */
	public boolean updateForum(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = "UPDATE jc_forum SET " + set + " WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	private ForumBean getForum(ResultSet rs) throws SQLException {
		ForumBean forum = new ForumBean();
		forum.setId(rs.getInt("id"));
		forum.setUserId(rs.getString("user_id"));
		forum.setTitle(rs.getString("title"));
		forum.setTodayCount(rs.getInt("today_count"));
		forum.setTotalCount(rs.getInt("total_count"));
		forum.setPrimeCount(rs.getInt("prime_count"));
		forum.setDescription(rs.getString("description"));
		forum.setMark(rs.getInt("mark"));
		forum.setTongId(rs.getInt("tong_id"));
		forum.setBadUser(rs.getString("bad_user"));
		forum.setType(rs.getInt("type"));
		forum.setRule(rs.getString("rule"));
		forum.setPrimeCat(rs.getInt("prime_cat"));
		return forum;
	}

	public PrimeCatBean getPrimeCat(ResultSet rs) throws SQLException {
		PrimeCatBean bean = new PrimeCatBean();
		bean.setId(rs.getInt("id"));
		bean.setUserId(rs.getInt("user_id"));
		bean.setForumId(rs.getInt("forum_id"));
		bean.setName(rs.getString("name"));
		bean.setParentId(rs.getInt("parent_id"));
		bean.setCreateTime(rs.getTimestamp("create_time").getTime());
		bean.setCatCount(rs.getInt("cat_count"));
		bean.setThreadCount(rs.getInt("thread_count"));
		return bean;
	}
	
	public List getPrimeCatList(String condition) {
		List list = new ArrayList();

		DbOperation dbOp = new DbOperation(2);
		String query = "SELECT * from jc_forum_prime_cat";

		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				list.add(getPrimeCat(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}
	
	public boolean addPrimeCat(PrimeCatBean bean) {
		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT INTO jc_forum_prime_cat(forum_id,parent_id,name,user_id,create_time) VALUES(?,?,?,?,now())";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getForumId());
			pstmt.setInt(2, bean.getParentId());
			pstmt.setString(3, bean.getName());
			pstmt.setInt(4, bean.getUserId());
			
			dbOp.executePstmt();
			bean.setId(dbOp.getLastInsertId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.release();
		return true;
	}
	
	public PrimeCatBean getPrimeCat(String condition) {
		PrimeCatBean bean = null;

		DbOperation dbOp = new DbOperation(2);
		String query = "SELECT * from jc_forum_prime_cat";

		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {
			if (rs.next()) {
				bean = getPrimeCat(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return bean;
	}
	
	public ForumContentBean getForumContent(String condition) {
		ForumContentBean forumcontent = null;

		// 构建查询语句
		String query = "SELECT * from jc_forum_content";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		String key = condition;
		// forumcontent = (ForumContentBean) OsCacheUtil.get(key,
		// OsCacheUtil.FORUM_GROUP,
		// OsCacheUtil.FORUM_FLUSH_PERIOD);
		if (forumcontent == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation(2);
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				if (rs.next()) {
					forumcontent = this.getForumContent(rs);
					// OsCacheUtil.put(key, forumcontent,
					// OsCacheUtil.FORUM_GROUP);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
		}

		return forumcontent;
	}
	
	public ForumContentBean getForumContentHis(String condition) {
		ForumContentBean forumcontent = null;

		// 构建查询语句
		String query = "SELECT * from jc_forum_content_his";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		String key = condition;
		// forumcontent = (ForumContentBean) OsCacheUtil.get(key,
		// OsCacheUtil.FORUM_GROUP,
		// OsCacheUtil.FORUM_FLUSH_PERIOD);
		if (forumcontent == null) {
			// 数据库操作类
			DbOperation dbOp = new DbOperation(2);
			// 查询
			ResultSet rs = dbOp.executeQuery(query);
			try {
				// 结果不为空
				if (rs.next()) {
					forumcontent = this.getForumContent(rs);
					// OsCacheUtil.put(key, forumcontent,
					// OsCacheUtil.FORUM_GROUP);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// 释放资源
			dbOp.release();
		}

		return forumcontent;
	}

	public Vector getForumContentList(String condition) {
		Vector contentList = new Vector();
		ForumContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建查询语句
		String query = "SELECT * from jc_forum_content";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				content = this.getForumContent(rs);
				contentList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return contentList;
	}
	
	public Vector getForumContentListNew(String condition) {
		Vector contentList = new Vector();
		ForumContentBean content = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = condition;
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				content = new ForumContentBean();
				content.setId(rs.getInt("id"));
				content.setContent(rs.getString("content"));
				content.setCount(rs.getInt("count"));
				content.setReply(rs.getInt("reply"));
				content.setForumId(rs.getInt("forum_id"));
				content.setUserId(rs.getInt("user_id"));
				content.setMark1(rs.getInt("mark1"));
				content.setMark2(rs.getInt("mark2"));
				content.setReadonly(rs.getInt("readonly"));
				content.setType(rs.getInt("type"));
				content.setTitle(rs.getString("title"));
				content.setCreateTime(rs.getTimestamp("create_datetime"));
				content.setTongId(rs.getInt("tong_id"));
				contentList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return contentList;
	}

	public boolean addForumContent(ForumContentBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT low_priority INTO jc_forum_content(forum_id,title,content,user_id,reply,count,mark1,mark2,create_datetime,last_re_time,attach,type) VALUES(?,?,?,?,?,?,?,?,now(),now(),?,?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getForumId());
			pstmt.setString(2, bean.getTitle());
			pstmt.setString(3, bean.getContent());
			pstmt.setInt(4, bean.getUserId());
			pstmt.setInt(5, bean.getReply());
			pstmt.setInt(6, bean.getCount());
			pstmt.setInt(7, bean.getMark1());
			pstmt.setInt(8, bean.getMark2());
			pstmt.setString(9,bean.getAttach());
			pstmt.setInt(10,bean.getType());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		// 释放资源
		bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}

	public boolean delForumContent(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = "DELETE low_priority FROM jc_forum_content WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateForumContent(String set, String condition) {
		boolean result;
		DbOperation dbOp = new DbOperation(2);
		String query = "UPDATE low_priority jc_forum_content SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public int getForumContentCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);

		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_forum_content";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
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

	private ForumContentBean getForumContent(ResultSet rs) throws SQLException {
		ForumContentBean content = new ForumContentBean();
		content.setId(rs.getInt("id"));
		content.setContent(rs.getString("content"));
		content.setCount(rs.getInt("count"));
		content.setReply(rs.getInt("reply"));
		content.setForumId(rs.getInt("forum_id"));
		content.setUserId(rs.getInt("user_id"));
		content.setMark1(rs.getInt("mark1"));
		content.setMark2(rs.getInt("mark2"));
		content.setReadonly(rs.getInt("readonly"));
		content.setType(rs.getInt("type"));
		content.setTitle(rs.getString("title"));
		content.setCreateTime(rs.getTimestamp("create_datetime"));
		content.setDelMark(rs.getInt("del_mark"));
		content.setDUserId(rs.getInt("duser_id"));
		content.setLastReTime(rs.getTimestamp("last_re_time").getTime());
		content.setAttach(rs.getString("attach"));
		return content;
	}

	public ForumReplyBean getForumReply(String condition) {
		ForumReplyBean friendProposal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建查询语句
		String query = "SELECT * from jc_forum_reply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			if (rs.next()) {
				friendProposal = this.getForumReply(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendProposal;
	}

	public Vector getForumReplyList(String condition) {
		Vector friendProposalList = new Vector();
		ForumReplyBean friendProposal = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建查询语句
		String query = "SELECT * from jc_forum_reply";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}
		// 查询
		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				friendProposal = this.getForumReply(rs);
				friendProposalList.add(friendProposal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 释放资源
		dbOp.release();
		return friendProposalList;
	}
	
	public List getForumReplyHisList(String condition) {
		List list = new ArrayList();
		DbOperation dbOp = new DbOperation(2);
		String query = "SELECT * from jc_forum_reply_his";
		if (condition != null) {
			query = query + " WHERE " + condition;
		}

		ResultSet rs = dbOp.executeQuery(query);
		try {
			// 结果不为空
			while (rs.next()) {
				list.add(getForumReply(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbOp.release();
		return list;
	}

	public boolean addForumReply(ForumReplyBean bean) {
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT low_priority INTO jc_forum_reply(content_id,user_id,content,ctype,create_datetime,attach) VALUES(?,?,?,?,now(),?)";
		// 准备
		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}
		// 传递参数
		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getContentId());
			pstmt.setInt(2, bean.getUserId());
			pstmt.setString(3, bean.getContent());
			pstmt.setInt(4, bean.getCType());
			pstmt.setString(5,bean.getAttach());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}
		// 执行
		dbOp.executePstmt();
		bean.setId(dbOp.getLastInsertId());
		dbOp.release();
		return true;
	}

	public boolean delForumReply(String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建更新语句
		String query = "DELETE low_priority FROM jc_forum_reply WHERE " + condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public boolean updateForumReply(String set, String condition) {
		boolean result;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建更新语句
		String query = "UPDATE low_priority jc_forum_reply SET " + set + " WHERE "
				+ condition;
		// 执行更新
		result = dbOp.executeUpdate(query);
		// 释放资源
		dbOp.release();
		return result;
	}

	public int getForumReplyCount(String condition) {
		int count = 0;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);
		// 构建更新语句
		String query = "SELECT count(id) as c_id FROM jc_forum_reply WHERE "
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

	private ForumReplyBean getForumReply(ResultSet rs) throws SQLException {
		ForumReplyBean reply = new ForumReplyBean();
		reply.setId(rs.getInt("id"));
		reply.setContent(rs.getString("content"));
		reply.setContentId(rs.getInt("content_id"));
		reply.setUserId(rs.getInt("user_id"));
		reply.setCreateTime(rs.getTimestamp("create_datetime"));
		reply.setDelMark(rs.getInt("del_mark"));
		reply.setCType(rs.getInt("ctype"));
		reply.setAttach(rs.getString("attach"));
		return reply;
	}

	public Vector getForumIdCountList(String condition) {
		Vector forumIdCountList = new Vector();
		int count = 0;
		// JCRoomContentBean roomContent = null;
		// 数据库操作类
		DbOperation dbOp = new DbOperation(2);

		// 构建查询语句
		String query = condition;

		// 查询
		ResultSet rs = dbOp.executeQuery(query);

		try {
			// 结果不为空
			while (rs.next()) {
				// roomContent = new JCRoomContentBean();
				count = rs.getInt("id");
				forumIdCountList.add(new Integer(count));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			dbOp.release();
		}
		return forumIdCountList;

	}

	public ForumUserBean getForumUser(int userId) {
		ForumUserBean bean = null;

		DbOperation dbOp = new DbOperation(2);
		ResultSet rs = dbOp.executeQuery("select * from forum_user where user_id=" + userId);
		try {
			if (rs.next()) {
				bean = getForumUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();

		return bean;
	}
	
	private ForumUserBean getForumUser(ResultSet rs) throws SQLException {
		ForumUserBean bean = new ForumUserBean();
		bean.setUserId(rs.getInt("user_id"));
		bean.setMark(rs.getInt("mark"));
		bean.setThreadCount(rs.getInt("thread_count"));
		bean.setReplyCount(rs.getInt("reply_count"));
		bean.setExp(rs.getInt("exp"));
		bean.setPoint(rs.getInt("point"));
		bean.setRank(rs.getInt("rank"));
		bean.setMForumId(rs.getInt("m_forum_id"));
		bean.setInfo(rs.getString("info"));
		bean.setVip(rs.getTimestamp("vip").getTime());
		bean.setSignature(rs.getString("signature"));
		return bean;
	}

	public boolean addForumUser(ForumUserBean bean) {
		DbOperation dbOp = new DbOperation(2);
		String query = "INSERT INTO forum_user(user_id) VALUES(?)";

		if (!dbOp.prepareStatement(query)) {
			dbOp.release();
			return false;
		}

		PreparedStatement pstmt = dbOp.getPStmt();
		try {
			pstmt.setInt(1, bean.getUserId());
		} catch (SQLException e) {
			e.printStackTrace();
			dbOp.release();
			return false;
		}

		dbOp.executePstmt();

		dbOp.release();
		return true;
	}

	public boolean updateForumUser(String set, String condition) {
		boolean result = false;

		DbOperation dbOp = new DbOperation(2);

		String query = "UPDATE forum_user SET " + set + " WHERE " + condition;

		result = dbOp.executeUpdate(query);

		dbOp.release();
		return result;
	}

	public void updateVote(int userId, int contentId, int vote) {

		DbOperation dbOp = new DbOperation(2);

		String query = "UPDATE jc_forum_vote SET vote=" + vote + " WHERE " + 
			"content_id=" + contentId + " and user_id=" + userId;

		dbOp.executeUpdate(query);

		dbOp.release();
	}

	public void addVote(int userId, int contentId, int vote) {
		DbOperation dbOp = new DbOperation(2);

		String query = "insert into jc_forum_vote SET vote=" + vote + ",content_id=" + 
			contentId + ",user_id=" + userId;

		dbOp.executeUpdate(query);

		dbOp.release();
	}

	private ForumActionBean getForumAction(ResultSet rs) throws SQLException{
		ForumActionBean bean = new ForumActionBean();
		bean.setId(rs.getInt("id"));
		bean.setName(rs.getString("name"));
		bean.setContent(rs.getString("content"));
		bean.setType(rs.getInt("type"));
		
		return bean;
	}
	
	public List getForumActionList(String condition) {
		DbOperation dbOp = new DbOperation(4);
		List list = new ArrayList();
		String query = "select * from forum_action";
		if(condition != null){
			query += " where " + condition;
		}
		
		ResultSet rs = dbOp.executeQuery(query);
		
		try{
			while(rs.next()) {
				ForumActionBean bean = getForumAction(rs);
				list.add(bean);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			dbOp.release();
		}
		
		return list;
	}
	
	public ForumActionBean getForumActionBean(String condition) {
		DbOperation dbOp = new DbOperation(4);
		String query = "select * from forum_action";
		if(condition != null){
			query += " where " + condition;
		}
		ResultSet rs = dbOp.executeQuery(query);
		try{
			if(rs.next()) {
				ForumActionBean bean = getForumAction(rs);
				return bean;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally{
			dbOp.release();
		}
		return null;
	}

	public boolean deleteForumContent(ForumContentBean content, int userId) {
		boolean result;
		DbOperation dbOp = new DbOperation(2);
		
		result = dbOp.executeUpdate("UPDATE low_priority jc_forum_content SET del_mark=1 WHERE id=" + content.getId());
		result &= dbOp.executeUpdate("insert ignore into jc_forum_del set create_time=now(),user_id=" + userId + ",content_id=" + content.getId() + ",forum_id=" + content.getForumId());

		dbOp.release();
		return result;
	}
	// 根据查询，返回简单的帖子列表
	public List getForumContentList2(String query) {
		List contentList = new ArrayList(32);
		ForumContentBean content = null;

		DbOperation dbOp = new DbOperation(2);

		ResultSet rs = dbOp.executeQuery(query);
		try {
			while (rs.next()) {
				content = new ForumContentBean();
				content.setId(rs.getInt("id"));
				content.setContent(rs.getString("content"));
				content.setCount(rs.getInt("count"));
				content.setReply(rs.getInt("reply"));
				content.setForumId(rs.getInt("forum_id"));
				content.setUserId(rs.getInt("user_id"));
				content.setType(rs.getInt("type"));
				content.setTitle(rs.getString("title"));
				content.setCreateTime(rs.getTimestamp("create_datetime"));
				content.setDUserId(rs.getInt("duser"));
				contentList.add(content);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbOp.release();
		return contentList;
	}
	
    // 取得推荐的帖子
    public ForumRcmdBean getRcmd(String cond){
    	ForumRcmdBean bean = null;
    	DbOperation db = new DbOperation(2);
    	ResultSet rs = db.executeQuery(" select * from jc_forum_rcmd where " + cond);
    	try {
			if (rs.next()){
				bean = getRcmd(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
    	return bean;
    }
    
	public int addRcmd(ForumRcmdBean bean) {
		int lastId = 0;
		DbOperation db = new DbOperation(2);
		String query = "insert into jc_forum_rcmd(content_id,rcmd_time) values (?,now())";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getContentId());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	public HashMap getRcmdMap(String cond) {
		ForumRcmdBean bean = null;
		HashMap map = new HashMap();
    	DbOperation db = new DbOperation(2);
    	ResultSet rs = db.executeQuery(" select * from jc_forum_rcmd where " + cond);
    	try {
			while (rs.next()){
				bean = getRcmd(rs);
				map.put(new Integer(bean.getContentId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	ForumRcmdBean getRcmd(ResultSet rs) throws SQLException{
		ForumRcmdBean bean = new ForumRcmdBean();
		bean.setId(rs.getInt("id"));
		bean.setContentId(rs.getInt("content_id"));
		bean.setRcmdTime(rs.getTimestamp("rcmd_time").getTime());
		return bean;
	}
	
	// 论坛推荐管理
	public int addRcmd2(ForumRcmdBean2 bean) {
		int lastId = 0;
		DbOperation db = new DbOperation(2);
		String query = "insert into jc_forum_rcmd2(forum_id,content,rcmd_time,week) values (?,?,now(),?)";
		if(!db.prepareStatement(query)) {
			db.release();
			return lastId;
		}
		PreparedStatement pstmt = db.getPStmt();
		try{
			pstmt.setInt(1, bean.getForumId());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getWeek());
			pstmt.execute();
		}catch(SQLException e) {
			e.printStackTrace();
			return lastId;
		}finally{
			lastId = db.getLastInsertId();
			db.release();
		}
		return lastId;
	}
	
	public ForumRcmdBean2 getRcmd2(String cond) {
    	ForumRcmdBean2 bean = null;
    	DbOperation db = new DbOperation(2);
    	ResultSet rs = db.executeQuery(" select * from jc_forum_rcmd2 where " + cond);
    	try {
			if (rs.next()){
				bean = getRcmd2(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
    	return bean;
	}
	
	public LinkedHashMap getRcmdMap2(String cond) {
		ForumRcmdBean2 bean = null;
		LinkedHashMap map = new LinkedHashMap();
    	DbOperation db = new DbOperation(2);
    	ResultSet rs = db.executeQuery(" select * from jc_forum_rcmd2 where " + cond);
    	try {
			while (rs.next()){
				bean = getRcmd2(rs);
				map.put(new Integer(bean.getId()), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.release();
		}
		return map;
	}
	
	ForumRcmdBean2 getRcmd2(ResultSet rs) throws SQLException{
		ForumRcmdBean2 bean = new ForumRcmdBean2();
		bean.setId(rs.getInt("id"));
		bean.setForumId(rs.getInt("forum_id"));
		bean.setContent(rs.getString("content"));
		bean.setRcmdTime(rs.getTimestamp("rcmd_time").getTime());
		bean.setWeek(rs.getInt("week"));
		return bean;
	}
}
