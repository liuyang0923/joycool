package net.joycool.wap.action.jcforum;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import net.joycool.wap.action.jcforum.ForumAction;
import net.joycool.wap.action.user.RankAction;
import net.joycool.wap.bean.jcforum.ForumBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.jcforum.ForumReplyBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.cache.StaticCacheMap;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.util.DSUtil;
import net.joycool.wap.util.ForbidUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
/**
 *	论坛扩展（新闻、电子书等等）
 */
public class ForumxAction extends ForumAction{
	public static ForumxService service = new ForumxService();
	public static HashMap newsTypeMap = null;
	
	public ForumxAction(HttpServletRequest request) {
		super(request);
	}

	public static HashMap getNewsTypeMap() {
		if(newsTypeMap != null)
			return newsTypeMap;
		synchronized(ForumxAction.class) {
			if(newsTypeMap == null) {
				List list = service.getNewsTypeMap("1");
				newsTypeMap = new LinkedHashMap();
				for(int i = 0;i < list.size();i++) {
					NewsTypeBean bean = (NewsTypeBean)list.get(i);
					newsTypeMap.put(Integer.valueOf(bean.getId()), bean);
				}
			}
		}
		return newsTypeMap;
	}
	public static List getNewsTypeList() {
		return new ArrayList(getNewsTypeMap().values());
	}
	
	public static List getNewsTypeList(int parentId) {
		return service.getNewsTypeMap("parent_id=" + parentId);
	}

	public void index() {
		
	}
	
	public void news() {
		
	}
	
	public void reply() {
		
	}
	
	public NewsTypeBean getNewsType(int id) {
		return (NewsTypeBean)getNewsTypeMap().get(Integer.valueOf(id));
	}
	public NewsTypeBean getCurrentNewsType() {
		Integer iid = (Integer)session.getAttribute("ntId");	// news type id
		if(iid == null)
			return null;
		return (NewsTypeBean)getNewsTypeMap().get(iid);
	}
	
	public void addNews(ForumContentBean con, int newsType) {
		if(!con.isNews()) {
			con.setNews();
			service.updateForumContent("mark2="+con.getMark2(), "id=" + con.getId());
		}
		service.addNews(con.getId(), newsType);
		latestNews.srm(Integer.valueOf(newsType));
	}
	public void delNews(ForumContentBean con, int newsType) {
		if(!con.isNews())
			return;
		service.delNews(con.getId(), newsType);
		if(!SqlUtil.exist("select id from forum_news where id=" + con.getId(),2)) {
			con.cancelNews();
			service.updateForumContent("mark2="+con.getMark2(), "id=" + con.getId());
		}
		latestNews.srm(Integer.valueOf(newsType));
	}
	
	public void addReply(ForumContentBean con) {
		ForumBean forum = ForumCacheUtil.getForumCache(con.getForumId());
		String contents = getParameterString("content");

		if (contents != null) {
			request.setAttribute("con", con);
			contents = StringUtil.removeCtrlAsc(contents.trim());
			if (forum.getType() == 1 && loginUser == null) {
				String url = response
						.encodeURL("/user/login.jsp?backTo=/jcforum/index.jsp");
				String link = "<a href=\"" + url + "\">登录</a>";
				tip = "此板块不允许游客发言，请" + link + "后在发言!";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			if (contents.length() == 0) {
				tip = "回复不能为空！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			if (contents.length() > 1000) {
				contents = contents.substring(0, 1000);
			}
			if (con.isReadonly()) {
				tip = "这篇帖子无法回复！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			String infos = (String) session
					.getAttribute("forumreplyrepeat");
			String info = contents;
			if (loginUser != null) {
				info = contents + " " + loginUser.getId();
			}
			if ((info.equals(infos))) {
				doTip("error", "回复重复.");
				return;
			}
			if (!isCooldown("fourmSTime", 10 * 1000)) {
				tip = "你的发文太快了！请先休息一会再继续！";
				request.setAttribute("tip", tip);
				request.setAttribute("result", "error");
				return;
			}
			ForbidUtil.ForbidBean forbid = ForbidUtil.getForbid("forum",loginUser.getId());
			if(forbid != null) {
				doTip("error", "已经被禁止发贴 - " + forbid.getBak());
				return;
			}
			forbid = ForbidUtil.getForbid("f" + con.getForumId(), loginUser.getId());
			if(forbid != null) {
				doTip("tip", "已经被禁止发贴 - " + forbid.getBak());
				return;
			}
			
			ForumReplyBean replyBean = new ForumReplyBean();
			String cType = request.getParameter("cType");
			if (cType != null) {
				replyBean.setCType(1);
			}
			replyBean.setContent(contents);
			if (loginUser != null) {
				replyBean.setUserId(loginUser.getId());
			} else {
				replyBean.setUserId(0);
			}
			replyBean.setContentId(con.getId());
			ForumCacheUtil.addForumReply(replyBean, con);
			if (loginUser != null) {
				RankAction.addPoint(loginUser, 1);
				forumUser.addReplyCount();
				ForumCacheUtil.updateForumUser(forumUser);
			}
			session.setAttribute("fourmSTime", System
					.currentTimeMillis()
					+ "");
			session.setAttribute("forumreplyrepeat", info);

			doTip("success", "回帖发表成功!");
			return;
		}
		
	}
	// 新闻
	public static List nullList = new ArrayList(0);
	public static ICacheMap latestNews = CacheManage.addCache(new StaticCacheMap(16), "latestNews");
	public static long STAT_UPDATE_INTERVAL = 1800 * 1000;
	public static long latestNewsTime = 0;

	public static List getLatestNews(int type) { 
		Integer key = Integer.valueOf(type);
		synchronized(latestNews) {
			List list = (List)latestNews.get(key);
			if(list != null)
				return list;
			list = SqlUtil.getIntList("select id from forum_news where type=" + type + " order by id desc limit 6", 2);
			if(list != null) {
				latestNews.put(key, list);
				return list;
			}
		}
		return nullList;
	}
	public static List getRandomLatestNews(int type, int limit) {
		List list = getLatestNews(type);
		if (list.size() <= limit)
			return list;

		int rand = RandomUtil.nextInt(list.size() - limit);
		return DSUtil.sublist(list, rand, rand + limit);
	}
	// 更新所有的八卦
	public static void resetLatestNews() {
		latestNewsTime = 0;
	}
	public static void updateLatestNews() {
		long now = System.currentTimeMillis();
		if (now - latestNewsTime >= STAT_UPDATE_INTERVAL)
			latestNews.clear();
		latestNewsTime = now + STAT_UPDATE_INTERVAL;
	}
}
