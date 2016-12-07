package net.joycool.wap.cache.util;

import java.io.File;
import java.sql.ResultSet;
import java.util.*;

import net.joycool.wap.action.jcforum.ForumAction;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.jcforum.ForumReplyBean;
import net.joycool.wap.bean.jcforum.ForumUserBean;
import net.joycool.wap.bean.jcforum.ForumVoteBean;
import net.joycool.wap.cache.*;
import net.joycool.wap.service.impl.JcForumServiceImpl;
import net.joycool.wap.servlet.SqlThread;
import net.joycool.wap.spec.buyfriends.ActionTrend;
import net.joycool.wap.spec.buyfriends.BeanTrend;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.db.DbOperation;

public class ForumCacheUtil {

	// private static HashMap forumContentCountMap1 = new HashMap(); //
	// 每隔论坛的聊天按时间

	// private static HashMap forumContentCountMap2 = new HashMap(); //
	// 每隔论坛的聊天按人气

	// private static HashMap forumContentCountMap3 = new HashMap(); //
	// 每隔论坛的聊天按精华

	// private static HashMap forumContentMap = new HashMap(); // 主题

	// private static HashMap forumPeakMap = new HashMap(); // 置顶主题

	//private static Vector topPeakList = new Vector();

	// liuyi 2007-01-04 最近缓存清除时间
	//private static long lastClearTime = 0;

	// 缓存

	// private static Object mapLock = new Object(); //
	// 当查询某个论坛天记录所有id在内存中不存在时,改为查询数据库时的同步锁

	// private static Object contentLock = new Object(); //
	// 当在内存中不存在查询的记录,改为查询数据库时的同步锁

	private static JcForumServiceImpl service = new JcForumServiceImpl();

	public static ICacheMap forumUserCache = CacheManage.forumUser;
	public static ICacheMap forumContentCache = CacheManage.forumContent;
	public static ICacheMap forumVoteCache = CacheManage.forumVote;
	public static ICacheMap forumReplyCache = CacheManage.forumReply;
	public static ICacheMap forumContentsCache = CacheManage.forumContents;
	public static ICacheMap forumReplysCache = CacheManage.forumReplys;
	public static ICacheMap forumCache = CacheManage.forum;
	public static ICacheMap forumTopCache = CacheManage.forumTop;
	public static ICacheMap stuffCache = CacheManage.stuff;

	/**
	 * 清空某条聊天记录的属性缓存。
	 * 
	 * @param contentId
	 */
	public static void flushForumContent(int contentId) {
		forumContentCache.srm(contentId);
	}

	public static void flushForumReply(int replyId) {
		forumReplyCache.srm(replyId);
	}

	public static void flushForum(int forumId) {
		Integer key = new Integer(forumId);
		synchronized(forumCache) {
			ForumBean forum = (ForumBean)forumCache.get(key);
			if(forum != null) {
				forumCache.rm(key);
				forumCache.rm("t" + forum.getTongId());
			}
		}
	}

	/**
	 * 取得某条聊天记录。
	 * 
	 * @param contentId
	 * @return
	 */
	public static ForumContentBean getForumContent(int contentId) {
		Integer key = Integer.valueOf(contentId);
		synchronized(forumContentCache) {
			ForumContentBean content = (ForumContentBean) forumContentCache.get(key);
			if (content == null) {
	
				content = service.getForumContent("id = " + contentId + " and del_mark=0");
	
				if (content == null) {
					content = service.getForumContentHis("id = " + contentId + " and del_mark=0");
					if (content == null) {
						return null;
					}
				}
				forumContentCache.put(key, content);
			}
			return content;
		}
	}
	
	public static ForumContentBean getForumContentHis(int contentId) {
		Integer key = Integer.valueOf(contentId);
		synchronized(forumContentCache) {
			ForumContentBean content = (ForumContentBean) forumContentCache.get(key);
			if (content == null) {

				content = service.getForumContentHis("id = " + contentId + " and del_mark=0");
				if (content == null) {
					return null;
				}

				forumContentCache.put(key, content);
			}
			return content;
		}
	}

	public static ForumReplyBean getForumReply(int replyId) {
		Integer key = Integer.valueOf(replyId);
		synchronized(forumReplyCache) {
			ForumReplyBean content = (ForumReplyBean) forumReplyCache.get(key);
			if (content == null) {
				content = service.getForumReply("id = " + replyId + " and del_mark=0");

				if (content == null) {
					return null;
				}
				forumReplyCache.put(key, content);
			}
			return content;
		}
	}

	public static void addForumContent(ForumContentBean bean, ForumBean forum) {
		if (service.addForumContent(bean)) {
			forum.setTodayCount(forum.getTodayCount() + 1);
			forum.setTotalCount(forum.getTotalCount() + 1);
			service.updateForum("today_count=today_count+1,total_count=total_count+1",
					"id=" + bean.getForumId());
			bean.setCreateTime(new Date());
			
			Integer key = new Integer(bean.getId());
			forumContentCache.spt(key, bean);
			addContentIds(bean);
			forumReplysCache.spt(key, new LinkedList());		// 顺便添加空的回复缓存
		}
	}

	public static void addForumReply(ForumReplyBean bean, ForumContentBean content) {
		if (bean == null)
			return;
		if (service.addForumReply(bean)) {
			content.setLastReTime(System.currentTimeMillis());
			SqlThread.addSql("update jc_forum_content set reply=reply+1,last_re_time='" + DateUtil.formatSqlDatetime(content.getLastReTime()) + "' where id=" + bean.getContentId());
			content.setReply(content.getReply() + 1);
			bean.setCreateTime(new Date());
			forumReplyCache.spt(new Integer(bean.getId()), bean);
			
			if((!content.isPeak()) && (!content.isTopPeak()) )		// 如果置顶，不需要改变缓存
				updateContentIds(content);
			
			addReplyIds(bean);
		}
		
	}

	/**
	 * 更新某条聊天记录。
	 * 
	 * @param set
	 * @param condition
	 * @param contentId
	 * @return
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean updateForumContent(String set, String condition,
			int contentId) {
		// 更新聊天记录
		if (!service.updateForumContent(set, condition)) {
			return false;
		}
		// 清空缓存
		flushForumContent(contentId);
		return true;
	}
	
	public static boolean updateForumContentNoFlush(String set, String condition) {
		if (!service.updateForumContent(set, condition)) {
			return false;
		}
		return true;
	}

	public static boolean updateForumReply(String set, String condition,
			int replyId) {
		// 更新聊天记录
		if (!service.updateForumReply(set, condition)) {
			return false;
		}
		// 清空缓存
		flushForumReply(replyId);
		return true;
	}

	public static boolean updateForum(String set, String condition, int forumId) {
		// 更新聊天记录
		if (!service.updateForum(set, condition)) {
			return false;
		}
		// 清空缓存
		flushForum(forumId);
		return true;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end

	/**
	 * 更新某条聊天记录。
	 * 
	 * @param condition
	 * @param contentId
	 * @return boolean
	 */
	// liuyi 2006-11-04 服务器缓慢原因查找修改 start
	public static boolean deleteForumContent(ForumContentBean content,
			ForumBean forum, int userId) {

//		if (service.updateForumReply("del_mark=1", "content_id=" + content.getId())) {
//			if (!service.updateForumContent("del_mark=1,duser_id=" + userId,
//					"id=" + content.getId())) {
//				return false;
//			}
//		}
		service.deleteForumContent(content, userId);
		if(forum.getTotalCount() > 0) {		// 避免论坛帖子数量成为负数
			if (DateUtil.dayDiff(content.getCreateTime()) == 0) {
				service.updateForum("today_count=today_count-1,total_count=total_count-1",
						"id=" + content.getForumId());
				forum.setTodayCount(forum.getTodayCount() - 1);
				forum.setTotalCount(forum.getTotalCount() - 1);
			} else {
				service.updateForum("total_count=total_count-1", "id="
						+ content.getForumId());
				forum.setTotalCount(forum.getTotalCount() - 1);
			}
		}
//		if(content.getAttach().length() > 5) {	// o.gif等待审核
//			File f = new File(ForumAction.ATTACH_ROOT, content.getAttach());
//			if (f.exists())
//				f.delete();
//		}
			
		flushForumContent(content.getId());
		deleteContentIds(content);
		return true;
	}

	// liuyi 2006-11-04 服务器缓慢原因查找修改 end
	public static boolean deleteForumReply(ForumReplyBean reply) {

		ForumContentBean content = getForumContent(reply.getContentId());
		if (content == null)
			return false;
		if (reply == null)
			return false;

		if (!service.updateForumReply("del_mark=1", "id=" + reply.getId())) {
			return false;
		}
		updateForumContentNoFlush("reply=reply-1", "id=" + reply.getContentId());
		content.setReply(content.getReply() - 1);
		if(reply.getAttach().length() > 5) {	// o.gif等待审核
			File f = new File(ForumAction.ATTACH_ROOT + "/" + reply.getAttach());
			if (f.exists())
				f.delete();
		}
		flushForumReply(reply.getId());
		deleteReplyIds(reply);

		return true;
	}
	static String delContent = "(内容被删除)";
	// 假删除
	public static boolean deleteMyForumReply(String condition, int contentId,
			int replyId) {

		ForumReplyBean reply = getForumReply(replyId);
		if (reply == null)
			return false;
		service.updateForumReply("content='" + delContent + "'", "id=" + replyId);

		reply.setContent(delContent);

		return true;
	}
	/**
	 * 取得缓存的key。
	 * 
	 * @param contentId
	 * @return
	 */
	public static String getKey(int contentId) {
		return "content=" + contentId;
	}

	/**
	 * 
	 * 
	 * @param forumId
	 * @return type 1 时间 2人气 3 精华
	 */
	public static Vector getForumContentCountMap(int forumId, int type) {
		Vector contentList = null;
		if (type == 1)
			contentList = service
					.getForumIdCountList("select id from jc_forum_content where forum_id="
							+ forumId
							+ " and (mark2 & 1)=0 and del_mark=0 order by last_re_time desc , id desc");
		else if (type == 2)
			contentList = service
					.getForumIdCountList("select id from jc_forum_content where forum_id="
							+ forumId
							+ "  and (mark2 & 1)=0 and del_mark=0 order by count desc");
		else if (type == 3)
			contentList = service
					.getForumIdCountList("select id from jc_forum_content where forum_id="
							+ forumId
							+ " and mark1=1 and (mark2 & 1)=0 and del_mark=0 order by id desc");
		return contentList;
	}

	public static List getForumContentMap(int contentId, String order) {

		List contentList = null;
		// macq_2007-3-24_更改回复显示顺序_start
		contentList = SqlUtil.getIntList("select id from jc_forum_reply where content_id="
						+ contentId + " and del_mark=0 order by " + order, 2);
		return contentList;
	}

	/**
	 * 添加一条聊天记录到对应缓存对应id列表中
	 * 
	 * @param forumId
	 * @return
	 */
	public static void addForumContentId(int forumId, ForumContentBean bean,
			int type) {
		// liuyi 2006-09-16 聊天室加缓存 start

		Vector forumIdList = getForumContentCountMap(forumId, type);
		// liuyi 2006-12-01 程序优化 start
		// synchronized (forumIdList)
		{
			if (!forumIdList.contains(new Integer(bean.getId()))) {
				forumIdList.add(0, new Integer(bean.getId()));
			}
		}
		// liuyi 2006-12-01 程序优化 end
		// liuyi 2006-09-16 聊天室加缓存 end
	}

	/**
	 * 删除一条聊天记录到对应缓存对应id列表中
	 * 
	 * @param forumId
	 * @return
	 */
	public static void deleteForumContentId(int forumId, int contentId, int type) {
		// liuyi 2006-09-16 聊天室加缓存 start
		Vector contentIdList = getForumContentCountMap(forumId, type);
		// liuyi 2006-12-01 程序优化 start
		// synchronized (forumIdList)
		{
			contentIdList.remove(new Integer(contentId));
		}
		// liuyi 2006-12-01 程序优化 end
		// liuyi 2006-09-16 聊天室加缓存 end
	}

	/**
	 * macq 2006-11-07 论坛列表缓存；
	 * 
	 * @param userId
	 * @return
	 */
	public static List getForumListCache() {
		List forumList = null;
		String key = "Forum";
		forumList = (List) OsCacheUtil.get(key, OsCacheUtil.FORUM_CACHE_GROUP,
				OsCacheUtil.FORUM_CACHE_FLUSH_PERIOD);
		if (forumList == null) {
			forumList = service.getForumList("mark=0 and title!='美图秀场'");
			OsCacheUtil.put(key, forumList, OsCacheUtil.FORUM_CACHE_GROUP);
		}
		return forumList;
	}

	/**
	 * macq 2006-11-07 城邦论坛列表缓存；
	 * 
	 * @param userId
	 * @return
	 */
	public static Vector getTongForumListCache() {
		Vector forumList = null;
		String key = "TongForum";
		forumList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FORUM_CACHE_GROUP,
				OsCacheUtil.FORUM_CACHE_FLUSH_PERIOD);
		if (forumList == null) {
			forumList = service.getForumList("mark=1");
			OsCacheUtil.put(key, forumList, OsCacheUtil.FORUM_CACHE_GROUP);
		}
		return forumList;
	}

	public static Vector getForumContentList(String condition) {
		Vector forumList = null;
		String key = "TongForum";
		forumList = (Vector) OsCacheUtil.get(key,
				OsCacheUtil.FORUM_CACHE_GROUP,
				60 * 60);
		if (forumList == null) {
			forumList = service.getForumContentList(condition);
			OsCacheUtil.put(key, forumList, OsCacheUtil.FORUM_CACHE_GROUP);
		}
		return forumList;
	}
	
	// 缓存论坛，以id为key
	public static ForumBean getForumCache(int forumId) {
		Integer key = new Integer(forumId);
		synchronized(forumCache) {
			ForumBean forum = (ForumBean) forumCache.get(key);
			if (forum == null) {
				forum = service.getForum("id=" + forumId);
				if(forum == null)
					return null;
				forumCache.put(key, forum);
				if(forum.getTongId() > 0)
					forumCache.put("t" + forum.getTongId(), forum);
			}
			return forum;
		}
	}
//	 缓存论坛，以tong_id为key
	public static ForumBean getForumCacheBean(int tongId) {
		String key = "t" + tongId;
		synchronized(forumCache) {
			ForumBean forum = (ForumBean) forumCache.get(key);
			if (forum == null) {
				forum = service.getForum("tong_id=" + tongId);
				forumCache.put(key, forum);
				forumCache.put(new Integer(forum.getId()), forum);
			}
			return forum;
		}
	}

	/**
	 * liuyi 2006-09-16 根据聊天记录id列表获取对应的聊天记录列表，该方法确保返回一个Vector实例；
	 * 
	 * @param contentIdList
	 * @return
	 */
	public static Vector getContentList(List contentIdList) {
		Vector ret = new Vector();

		if (contentIdList == null)
			return ret;

		for (int i = 0; i < contentIdList.size(); i++) {
			Integer id = (Integer) contentIdList.get(i);
			if (id == null)
				continue;

			ForumContentBean content = getForumContent(id.intValue());
			if (content != null) {
				ret.add(content);
			}
		}

		return ret;
	}

	public static boolean deleteContentID(int contentId) {
		return service.delForumContent("id=" + contentId);
	}

	public static boolean deleteReplyID(int contentId) {
		return service.delForumReply("content_id=" + contentId);

	}

	/**
	 * 加精
	 * 
	 * @param contentId
	 */
	public static boolean addPrime(ForumContentBean con, ForumBean forum) {

		if (service.updateForumContent("mark1=1", "id=" + con.getId())) {
			service.updateForum("prime_count=prime_count+1", "id=" + forum.getId());
			con.setMark1(1);
			SqlUtil.executeUpdate("insert into jc_forum_prime set id=" + con.getId() + ",forum_id=" + con.getForumId(), 2);
			forum.setPrimeCount(forum.getPrimeCount() + 1);
			return true;
		}
		return false;
	}

	/**
	 * 置顶
	 * 
	 * @param contentId
	 */
	public static boolean addPeak(ForumContentBean con) {

		con.setPeak();
		if (service.updateForumContent("mark2="+con.getMark2(), "id=" + con.getId())) {			
			SqlUtil.executeUpdate("insert into jc_forum_top set id=" + con.getId() + ",forum_id=" + con.getForumId(), 2);
			deleteContentIds(con);
			synchronized(forumTopCache) {
				getForumPeakMap(con.getForumId()).add(0, new Integer(con.getId()));
			}
			return true;
		}
		return false;
	}

	/**
	 * 总置顶
	 * 
	 * @param contentId
	 */
	public static boolean addTopPeak(ForumContentBean con) {
		con.setTopPeak();
		if (service.updateForumContent("mark2="+con.getMark2(), "id=" + con.getId())) {
			SqlUtil.executeUpdate("insert into jc_forum_top set id=" + con.getId(), 2);	// forum_id默认为0，表示为总置顶
			deleteContentIds(con);
			synchronized(forumTopCache) {
				getForumPeakMap(0).add(0, new Integer(con.getId()));
			}
			return true;
		}
		return false;
	}

	/**
	 * 取消加精
	 * 
	 * @param contentId
	 */
	public static boolean cancelPrime(ForumContentBean con, ForumBean forum) {

		if (service.updateForumContent("mark1=0", "id=" + con.getId())) {
			service.updateForum("prime_count=prime_count-1", "id=" + forum.getId());
			con.setMark1(0);
			int catId = SqlUtil.getIntResult("select cat_id from jc_forum_prime where id=" + con.getId(), 2);
			if(catId > 0)
				SqlUtil.executeUpdate("update jc_forum_prime_cat set thread_count=thread_count-1 where id=" + catId, 2);
			SqlUtil.executeUpdate("delete from jc_forum_prime where id=" + con.getId(), 2);
			forum.setPrimeCount(forum.getPrimeCount() - 1);
			return true;
		}
		return false;
	}

	/**
	 * 取消 置顶
	 * 
	 * @param contentId
	 */
	public static boolean cancelPeak(ForumContentBean con) {
		con.cancelPeak();
		if (service.updateForumContent("mark2=" + con.getMark2() + ",last_re_time=now()", "id=" + con.getId())) {
			
			SqlUtil.executeUpdate("delete from jc_forum_top where id=" + con.getId(), 2);
			addContentIds(con);
			synchronized(forumTopCache) {
				ForumCacheUtil.getForumPeakMap(con.getForumId()).remove(new Integer(con.getId()));
			}
			return true;
		}
		return false;
	}
	
	public static boolean setReadonly(ForumContentBean con) {
		if (service.updateForumContent("readonly=1", "id=" + con.getId())) {
			con.setReadonly(1);
			return true;
		}
		return false;
	}
	
	public static boolean cancelReadonly(ForumContentBean con) {
		if (service.updateForumContent("readonly=0", "id=" + con.getId())) {
			con.setReadonly(0);
			return true;
		}
		return false;
	}

	/**
	 * 取消总置顶
	 * 
	 * @param contentId
	 */
	public static boolean cancelTopPeak(ForumContentBean con) {
		con.cancelTopPeak();
		if (service.updateForumContent("mark2=" + con.getMark2() + ",last_re_time=now()", "id=" + con.getId())) {
			SqlUtil.executeUpdate("delete from jc_forum_top where id=" + con.getId(), 2);
			addContentIds(con);
			synchronized(forumTopCache) {
				ForumCacheUtil.getForumPeakMap(0).remove(new Integer(con.getId()));
			}
			return true;
		}
		return false;
	}
	/**
	 * 置顶列表
	 * 
	 * @param forumId
	 * @return
	 */
	public static List getForumPeakMap(int forumId) {
		Integer key = Integer.valueOf(forumId);
		synchronized(forumTopCache) {
			List contentList = (List) forumTopCache.get(key);
			if (contentList == null) {
				contentList = SqlUtil.getIntList("select id from jc_forum_top where forum_id="
						+ forumId, 2);
				forumTopCache.put(key, contentList);
			}
			return contentList;
		}
	}
	public static void flushForumPeakMap(int forumId) {
		forumTopCache.srm(forumId);
	}

	// public static void changeHits(int forumId) {
	// // liuyi 2007-01-04 人气排行缓存5分钟 start
	// if (System.currentTimeMillis() - lastClearTime > 5 * 60 * 1000) {
	// // forumContentCountMap2.remove(new Integer(forumId));
	// lastClearTime = System.currentTimeMillis();
	// }
	// // liuyi 2007-01-04 人气排行缓存5分钟 end
	//	}

	public static void deleteForumList() {
		String key = "Forum";
		OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP, key);
		forumCache.clear();
	}

	public static void deleteTongForumList() {
		String key = "TongForum";
		OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP, key);

	}

	public static void deleteForum(int forumId) {
		String key = "ForumId=" + forumId;
		OsCacheUtil.flushGroup(OsCacheUtil.FORUM_CACHE_GROUP, key);

	}

	public static void deleteTongForum(int tongId) {
		ForumBean f = getForumCacheBean(tongId);
		if(f != null) {
			flushForum(f.getId());
		}
	}
	
	public static ForumUserBean nullForumUser = new ForumUserBean();	// 用于将null作用于oscache，就算没有查询到记录，也要cache，避免每次都查询
	
	public static ForumUserBean getForumUser(int UserId) {
		Integer key = Integer.valueOf(UserId);
		synchronized(forumUserCache) {
			ForumUserBean bean = (ForumUserBean) forumUserCache.get(key);
	
			if (bean == null) {
				bean = service.getForumUser(UserId);
	
				if (bean != null) {
					forumUserCache.put(key, bean);
				} else {
					forumUserCache.put(key, nullForumUser);
				}
			} else if(bean == nullForumUser) {
				return null;
			}
			
			return bean;
		}
	}
	
	public static ForumUserBean getForumUser(UserBean user) {
		if(user == null)
			return null;
		else
			return getForumUser(user.getId());
	}
	
	public static void addForumUser(ForumUserBean bean) {
		service.addForumUser(bean);
		flushForumUser(bean.getUserId());
	}
	
	public static ForumUserBean getAddForumUser(int userId) {
		ForumUserBean bean = getForumUser(userId);
		if(bean == null) {
			bean = new ForumUserBean();
			bean.setUserId(userId);
			addForumUser(bean);
			bean = getForumUser(userId);
		}
		return bean;
	}
	
	public static void updateForumUser(ForumUserBean bean) {
		service.updateForumUser("thread_count=" + bean.getThreadCount() + 
				",reply_count=" + bean.getReplyCount() + ",exp=" + bean.getExp() + ",rank=" + bean.getRank() + ",vip='"+DateUtil.formatSqlDatetime(new Date(bean.getVip()))+"',signature='"+bean.getSignature() + "' ", "user_id=" + bean.getUserId());
	}
	
	public static void flushForumUser(int userId) {
		forumUserCache.srm(Integer.valueOf(userId));
	}
	
	public static List getForumTop10() {
		List forumList = null;
		String key = "top10";
		forumList = (List) OsCacheUtil.get(key, OsCacheUtil.FORUM_CACHE_GROUP,
				60 * 30);
		if (forumList == null) {
			int maxContent = SqlUtil.getIntResult("select max(id) from jc_forum_content", 2);
			forumList = SqlUtil.getIntList("select id from jc_forum_content where id>=" + (maxContent - 10000) + " and create_datetime>curdate() order by reply desc limit 10", 2);
			OsCacheUtil.put(key, forumList, OsCacheUtil.FORUM_CACHE_GROUP);
		}
		return forumList;
	}

	public static void vote(UserBean user, ForumContentBean con, int vote) {
		ForumVoteBean voteData = getVoteData(con.getId());
		Integer iid = Integer.valueOf(user.getId());
		if(voteData.contains(iid)) {
			voteData.update(iid, vote);
			service.updateVote(user.getId(), con.getId(), vote);
		} else {
			voteData.add(iid, vote);
			service.addVote(user.getId(), con.getId(), vote);
			ActionTrend.addTrend(user.getId(), BeanTrend.TYPE_FORUM_CONTENT, "%1参与了投票%3", user.getNickName(), con.getTitle(), "/jcforum/viewContent.jsp?contentId="+con.getId());
		}
	}
	
	public static HashMap getVoteSet(int contentId) {
		return getVoteData(contentId).getSet();
	}
	
	public static ForumVoteBean getVoteData(int contentId) {
		Integer key = Integer.valueOf(contentId);
		synchronized(forumVoteCache) {
			ForumVoteBean vote = (ForumVoteBean)forumVoteCache.get(key);
			if(vote == null) {
				List list = SqlUtil.getIntsList("select user_id,vote from jc_forum_vote where content_id=" + 
						contentId, 2);
				vote = new ForumVoteBean();
				vote.setData(list);
				forumVoteCache.put(key, vote);
			}
			
			return vote;
		}
	}
	
	public static int getVoteCount(int contentId) {
		return getVoteSet(contentId).size();
	}
	static Object nullObject = new Object();
	public static int contentsInitCacheCount = 50;
	public static int contentsMaxCacheCount = 200;
	// 返回记录，如果超过缓存范围则返回null
	public static List getContentsCache(int forumId, int start, int limit) {
		LinkedList map = getContentsCache(forumId);
		if(map == null)
			return null;
		synchronized(map) {
			if(map.size() < start + limit)
				return null;
			List list = new ArrayList(limit);
			Iterator iter = map.iterator();
			for(int i = 0;i < start;i++)
				iter.next();
			for(int i = 0;i < limit;i++)
				list.add(iter.next());
			return list;
		}
	}
	// 返回主题的帖子map，最多只包含前n个记录
	public static LinkedList getContentsCache(int forumId) {
		Integer key = new Integer(forumId);
		synchronized(forumContentsCache) {
			LinkedList map = (LinkedList)forumContentsCache.get(key);
			if(map != null)
				return map;
			map = new LinkedList();
			
			DbOperation dbOp = new DbOperation(2);
			ResultSet rs = null;
			try {
				rs = dbOp.executeQuery("select id from jc_forum_content where forum_id=" + forumId + " and (mark2 & 1)=0 and del_mark=0 order by last_re_time desc limit " + contentsInitCacheCount);
				if (rs != null) {
					while (rs.next()) {
						map.addLast(new Integer(rs.getInt(1)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return map;			// 出错则不保存
			} finally {
				dbOp.release();
			}
			
			forumContentsCache.put(key, map);
			return map;
		}
	}
	// (缓存)把对应的帖子移动到最前(更新)
	public static void updateContentIds(ForumContentBean content) {
		LinkedList map = getContentsCache(content.getForumId());
		if(map == null)
			return;
		Integer key = new Integer(content.getId());
		
		synchronized(map) {
			boolean del = map.remove(key);
			map.addFirst(key);
			if(!del)		// 没有删除，只是添加
				if(map.size() > contentsMaxCacheCount)
					map.removeLast();
		}
	}
	// (缓存)把对应的帖子移动到最前(添加)
	public static void addContentIds(ForumContentBean content) {
		LinkedList map = getContentsCache(content.getForumId());
		if(map == null)
			return;
		Integer key = new Integer(content.getId());
		
		synchronized(map) {
			map.addFirst(key);
			if(map.size() > contentsMaxCacheCount)
				map.removeLast();
		}
	}
	// (缓存)把对应的帖子移动到最前(更新)
	public static void deleteContentIds(ForumContentBean content) {
		LinkedList map = getContentsCache(content.getForumId());
		if(map == null)
			return;
		Integer key = new Integer(content.getId());
		
		synchronized(map) {
			map.remove(key);
		}
	}
	
	public static int replysInitCacheCount = 100;
	public static int replysMaxCacheCount = 200;
	
	// 返回回复的帖子map，最多只包含前n个记录
	public static LinkedList getReplysCache(int contentId) {
		Integer key = new Integer(contentId);
		synchronized(forumReplysCache) {
			LinkedList map = (LinkedList)forumReplysCache.get(key);
			if(map != null)
				return map;
			map = new LinkedList();
			
			DbOperation dbOp = new DbOperation(2);
			ResultSet rs = null;
			try {
				rs = dbOp.executeQuery("select id from jc_forum_reply where content_id=" + contentId + " and del_mark=0 order by id limit " + replysInitCacheCount);
				if (rs != null) {
					while (rs.next()) {
						map.addLast(new Integer(rs.getInt(1)));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return map;			// 出错则不保存
			} finally {
				dbOp.release();
			}
			
			forumReplysCache.put(key, map);
			return map;
		}
	}
	// (缓存)把对应的帖子移动到最前(添加)
	public static void addReplyIds(ForumReplyBean reply) {
		LinkedList map = getReplysCache(reply.getContentId());
		if(map == null)
			return;
		Integer key = new Integer(reply.getId());
		
		synchronized(map) {
			if(map.size() < replysMaxCacheCount)
				map.addLast(key);
		}
	}
	// (缓存)把对应的帖子移动到最前(更新)
	public static void deleteReplyIds(ForumReplyBean reply) {
		LinkedList map = getReplysCache(reply.getContentId());
		if(map == null)
			return;
		Integer key = new Integer(reply.getId());
		
		synchronized(map) {
			map.remove(key);
		}
	}
	// 返回记录，如果超过缓存范围则返回null
	public static List getReplysCache(ForumContentBean con, int start, int limit) {
		LinkedList map = getReplysCache(con.getId());
		if(map == null)
			return null;
		synchronized(map) {
			if(map.size() < start + limit)
				return null;
			List list = new ArrayList(limit);
			Iterator iter = map.iterator();
			for(int i = 0;i < start;i++)
				iter.next();
			for(int i = 0;i < limit;i++)
				list.add(iter.next());
			return list;
		}
	}
	// 返回记录，如果超过缓存范围则返回null，这个是反序，count表示一共有多少，start是反序后的序号
	public static List getReplysCacheR(ForumContentBean con, int start, int limit, int count) {
		LinkedList map = getReplysCache(con.getId());
		if(map == null)
			return null;
		synchronized(map) {
			if(map.size() < count - start)
				return null;
			List list = new ArrayList(limit);
			ListIterator iter = map.listIterator(count - start);
			for(int i = 0;i < limit;i++)
				list.add(iter.previous());
			return list;
		}
	}

	public static List getReplys(ForumContentBean con, int startIndex, int count, boolean reverse) {
		List replyList = null;
		if(reverse)
			replyList = getReplysCacheR(con, startIndex, count, con.getReply());
		else
			replyList = getReplysCache(con, startIndex, count);
		if(replyList == null) {
			String order;
			if(reverse)
				order = "id desc";
			else
				order = "id";
			
			replyList = ForumCacheUtil.getForumContentMap(con.getId(), order + " limit " + startIndex + "," + count);
		}
		return replyList;
	}
}
