/*
 * Created on 2005-12-5
 *
 */
package net.joycool.wap.framework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.joycool.wap.action.chat.JCRoomChatAction;
import net.joycool.wap.bean.CatalogBean;
import net.joycool.wap.bean.JaLineBean;
import net.joycool.wap.bean.MessageBean;
import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.UserStatusBean;
import net.joycool.wap.bean.auction.AuctionBean;
import net.joycool.wap.bean.chat.JCRoomBean;
import net.joycool.wap.bean.chat.JCRoomContentBean;
import net.joycool.wap.bean.dummy.DummyProductBean;
import net.joycool.wap.bean.ebook.EBookBean;
import net.joycool.wap.bean.friendadver.FriendAdverBean;
import net.joycool.wap.bean.game.GameBean;
import net.joycool.wap.bean.home.HomeDiaryBean;
import net.joycool.wap.bean.home.HomePhotoBean;
import net.joycool.wap.bean.image.ImageBean;
import net.joycool.wap.bean.jcforum.ForumContentBean;
import net.joycool.wap.bean.news.NewsBean;
import net.joycool.wap.bean.ring.PringBean;
import net.joycool.wap.bean.stock.StockGrailBean;
import net.joycool.wap.bean.tong.TongBean;
import net.joycool.wap.bean.video.VideoBean;
import net.joycool.wap.cache.OsCacheUtil;
import net.joycool.wap.cache.util.AuctionCacheUtil;
import net.joycool.wap.cache.util.ForumCacheUtil;
import net.joycool.wap.cache.util.TongCacheUtil;
import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.impl.HomeServiceImpl;
import net.joycool.wap.service.infc.ICatalogService;
import net.joycool.wap.service.infc.IDummyService;
import net.joycool.wap.service.infc.IEBookService;
import net.joycool.wap.service.infc.IForumMessageService;
import net.joycool.wap.service.infc.IFriendAdverService;
import net.joycool.wap.service.infc.IGameService;
import net.joycool.wap.service.infc.IImageService;
import net.joycool.wap.service.infc.IJaLineService;
import net.joycool.wap.service.infc.IJcForumService;
import net.joycool.wap.service.infc.IMessageService;
import net.joycool.wap.service.infc.INewsService;
import net.joycool.wap.service.infc.IRingService;
import net.joycool.wap.service.infc.IStockService;
import net.joycool.wap.service.infc.ITongService;
import net.joycool.wap.service.infc.IVideoService;
import net.joycool.wap.util.Constants;
import net.joycool.wap.util.LoadResource;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.RoomCacheUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;

/**
 * @author lbj
 * 
 */
public class JoycoolSpecialUtil {
	// liuyi 2007-02-05 首页链接推广
	public static String[][] randomLinks = {
			{ "零风险加盟特色餐饮稳获利", "http://wap.ebinf.com/column.jsp?id=581" },
			{ "麻将绝技 逢赌必赢", "http://wap.ebinf.com/column.jsp?id=599" },
			{ "两元超市风暴 赚钱不可挡", "http://wap.ebinf.com/column.jsp?id=612" },
			{ "神奇专利技术废塑料变黄金", "http://wap.ebinf.com/column.jsp?id=628" },
			{ "年利百万，河沙水泥造围栏", "http://wap.ebinf.com/column.jsp?id=664" },
			{ "名品时装加盟，财富第一波", "http://wap.ebinf.com/column.jsp?id=681" },
			{ "化妆品暴富项目狂赚女人钱", "http://wap.ebinf.com/column.jsp?id=761" },
			{ "秸秆变煤，绿色赚钱高科技", "http://wap.ebinf.com/column.jsp?id=821" } };

	// liuyi 2007-02-13 随机刷新项目
	public static String[][] randomLinks1 = { { "汽车美容 狂赚百万", "/Column.do?columnId=6980" },
			{ "家庭制袋机，狂赚方便钱", "/Column.do?columnId=6981" },
			{ "赚钱：俘虏情侣消费欲望", "/Column.do?columnId=6982" },
			{ "2007如何狂赚女人钱", "/Column.do?columnId=6983" },
			{ "赌博秘籍热卖，百战百胜", "/Column.do?columnId=6984" } };

	public static int getYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getOnlineUserCount(HttpServletRequest request) {
		Random rand = new Random();
		return JoycoolSessionListener.getOnlineUserCount() * 10 + rand.nextInt(10);
	}

	public static int getRealOnlineUserCount(HttpServletRequest request) {
		return JoycoolSessionListener.getOnlineUserCount() * 2;
	}

	// macq_12-20_增加交友中心人数_start
	public static int getFriendOnlineUserCount(HttpServletRequest request) {
		return JoycoolSessionListener.getOnlineUserCount() * 3;
	}

	// macq_12-20_增加交友中心人数_end
	public static int getRealOnlineUserCount0(HttpServletRequest request) {
		return JoycoolSessionListener.getOnlineUserCount();
	}

	/**
	 * liuyi 2007-02-05 随机链接添加
	 * 
	 * @param response
	 * @return
	 */
	public static String getRandomLink(HttpServletResponse response) {
		String ret = "";

		int index = RandomUtil.nextInt(randomLinks.length);
		String url = randomLinks[index][1];
		String desc = randomLinks[index][0];

		ret = "<a href=\"" + (url) + "\">" + StringUtil.toWml(desc) + "</a>";

		return ret;
	}

	/**
	 * 
	 * @author macq
	 * @explain： 家园推荐照片
	 * @datetime:2007-3-6 13:59:06
	 * @param request
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getPhotoMark(HttpServletResponse response) {
		String ret = "";
		Vector photoMark = homeService.getHomePhotoTopList2("order by b.id desc limit 2");
		HomePhotoBean homePhoto = null;
		for (int i = 0; i < photoMark.size(); i++) {
			homePhoto = (HomePhotoBean) photoMark.get(i);
			String url = "/home/homePhoto.jsp?userId=" + homePhoto.getUserId() + "&amp;hit="
					+ homePhoto.getId();
			ret = ret + "<a href=\"" + (url) + "\"><img src=\"/rep"
					+ homePhoto.getAttach()
					+ "\" alt=\"网友照片\"/></a>";
		}
		ret = ret + "<br/>";
		// <a href="<%=(
		// "/home/homePhoto.jsp?userId="+homePhoto.getUserId()+"&amp;hit="+homePhoto.getId())%>">
		// <img
		// src="<%=Constants.MYALBUM_RESOURCE_ROOT_URL%><%=homePhoto.getAttach()%>"
		// alt="网友照片"/></a>&nbsp;
		// <br/>
		// sb.append("<img src=\"" + Constants.CHAT_IMG_FACE_PATH + "/"
		// + actionId + ".gif\" alt=\"loading...\"/>");
		return ret;
	}

	/**
	 * liuyi 2007-02-13 随机链接添加
	 * 
	 * @param response
	 * @return
	 */
	public static String getRandomLink(HttpServletResponse response, String[][] randomLinks) {
		String ret = "";

		int index = RandomUtil.nextInt(randomLinks.length);
		String url = randomLinks[index][1];
		String desc = randomLinks[index][0];

		ret = "<a href=\"" + (url) + "\">" + StringUtil.toWml(desc) + "</a>";

		return ret;
	}

	public static String getWGameHallJinhuaCount(HttpServletResponse response) {
		String ret = "";
		// int count = SqlUtil.getIntResult("select count(id) from wgame_hall
		// where mark != "+ HallBean.GS_END + " and game_id = " +
		// HallBean.JINHUA, Constants.DBShortName);
		// if(count<5){
		// count=RandomUtil.nextInt(10)*2;
		// }
		int count = RandomUtil.nextInt(20) * 2;
		String url = "/wgamehall/jinhua/ksGameIndex.jsp";
		ret = "<a href=\"" + (url) + "\">快速开始游戏</a>(" + count + "人)";
		return ret;
	}

	public static int getWorldCupPostCount(HttpServletRequest request) {
		IForumMessageService service = ServiceFactory.createForumMessageService();
		return service.getForumMessageCount("forum_id = 13");
	}

	public static int getNewMessageCount(HttpServletRequest request) {
		UserBean loginUser = null;
		loginUser = new BaseAction().getLoginUser(request);
		if (loginUser == null) {
			return 0;
		}
		IMessageService messageService = net.joycool.wap.service.factory.ServiceFactory
				.createMessageService();
		int newMessageCount = messageService.getMessageCount("to_user_id = " + loginUser.getId()
				+ " and mark = " + MessageBean.NEW_MESSAGE);
		return newMessageCount;
	}

	public static String getGameSearchPost(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<input type=\"text\" name=\"keyword\" maxlength=\"20\"/>");
		sb.append("<anchor title =\"search Game\">搜");
		sb.append("<go href=\"" + ("game/GameCataList.do")
				+ "\" method=\"post\">");
		sb.append("<postfield name=\"gameName\" value=\"$(keyword)\"/>");
		sb.append("</go>");
		sb.append("</anchor>");
		sb.append("<br/>");
		return sb.toString();
	}

	public static String getGameMobileListPost(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		LinkedHashMap mobileMap = LoadResource.getMobileMap();
		int i = 1;
		for (Iterator it = mobileMap.keySet().iterator(); it.hasNext();) {
			String name = (String) it.next();
			sb.append("<anchor title =\"mobile info\">" + name);
			sb.append("<go href=\"" + ("/game/GameCataList.do")
					+ "\" method=\"post\">");
			sb.append("<postfield name=\"mobileType\" value=\"" + name + "\"/>");
			sb.append("</go>");
			sb.append("</anchor>");
			if (i % 3 == 0) {
				sb.append(" <br/> ");
			} else {
				sb.append("|");
			}
			i++;
		}
		return sb.toString();
	}

	public static String getRingSearch(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		Vector ringSearchList = LoadResource.getRingSearchList();
		int i = 1;
		for (Iterator it = ringSearchList.iterator(); it.hasNext();) {
			String name = (String) it.next();
			sb.append("<anchor title =\"ring search\">" + name);

			sb.append("<go href=\"" + ("/ring/RingCataList.do")
					+ "\" method=\"post\">");
			sb.append("<postfield name=\"fileType\" value=\"" + name + "\"/>");
			sb.append("</go>");
			sb.append("</anchor>");
			if (i % 2 == 1)
				sb.append(" | ");
			if (i % 2 == 0)
				sb.append("<br/>");
			i++;
		}
		return sb.toString();
	}

	public static String getIndexSearch(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<input type=\"text\" name=\"keyword\" maxlength=\"20\"/><br/>");
		sb.append("<anchor title=\"search Image\">找图片");
		sb.append("<go href=\"" + ("image/SearchImage.do")
				+ "\" method=\"post\">");
		sb.append("<postfield name=\"imageName\" value=\"$(keyword)\"/>");
		sb.append("</go>");
		sb.append("</anchor>|");
		sb.append("<anchor title =\"search Game\">游戏");
		sb.append("<go href=\"" + ("game/SearchGame.do") + "\" method=\"post\">");
		sb.append("<postfield name=\"gameName\" value=\"$(keyword)\"/>");
		sb.append("</go>");
		sb.append("</anchor>|");
		sb.append("<anchor title =\"search EBook\">电子书");
		sb.append("<go href=\"" + ("ebook/SearchEBook.do")
				+ "\" method=\"post\">");
		sb.append("<postfield name=\"ebookName\" value=\"$(keyword)\"/>");
		sb.append("</go>");
		sb.append("</anchor>");
		return sb.toString();
	}

	static IJaLineService lineService = ServiceFactory.createJaLineService();

	public static String getRootBackTo(JaLineBean line) {
		if (line.getMark() == 1) {
			return line.getRootBackTo();
		} else {
			if (line.getParentId() != 0) {
				JaLineBean parentLine = lineService.getLine(line.getParentId());
				return getRootBackTo(parentLine);
			} else {
				return "";
			}
		}
	}

	public static int getWapType(JaLineBean line) {
		if (line == null) {
			return 0;
		}
		if (line.getWapType() != 0) {
			return line.getWapType();
		} else {
			if (line.getParentId() != 0) {
				JaLineBean parentLine = lineService.getLine(line.getParentId());
				return getChildWapType(parentLine);
			} else {
				return 0;
			}
		}
	}

	public static int getChildWapType(JaLineBean line) {
		if (line == null) {
			return 0;
		}
		if (line.getChildWapType() != 0) {
			return line.getChildWapType();
		} else {
			if (line.getParentId() != 0) {
				JaLineBean parentLine = lineService.getLine(line.getParentId());
				return getChildWapType(parentLine);
			} else {
				return 0;
			}
		}
	}

	public static String getRandomRecord(HttpServletResponse response, String specialMark) {
		if (specialMark == null || specialMark.indexOf("%JCSP%RANDOM_RECORD_") == -1) {
			return "";
		}

		String[] marks = specialMark.split("_");
		if (marks.length != 5) {
			return "";
		}

		String recordType = marks[2];
		String catalogId = marks[3];
		String day = marks[4];
		Vector randList = null;
		// catalogId = catalogId.replace(":", ",");
		StringBuilder sb = new StringBuilder();
		// macq_2006-12-21_聊天大厅随机显示_start
		if ("chat".equals(recordType)) {
			// 从缓存中获取聊天室
			Vector roomRandList = RoomCacheUtil.getRoomListCache();
			JCRoomBean room = null;
			if (roomRandList.size() > 0) {
				room = (JCRoomBean) roomRandList.get(RandomUtil.nextInt(roomRandList.size()));
			}
			if (room != null) {
				// 判断是否为小黑屋
				if (room.getId() == 1) {
					roomRandList.remove(room);
					room = (JCRoomBean) roomRandList.get(RandomUtil.nextInt(roomRandList.size()));
				}
				if (room != null) {
					sb.append("<a href=\""
							+ ("/chat/hall.jsp?roomId=" + room.getId()) + "\">");
					sb.append(StringUtil.toWml(room.getName()));
					sb.append("</a>");
					return sb.toString();
				}
			}
		}
		// macq_2006-12-21_聊天大厅随机显示_end
		// macq_2006-12-21_交友广告随机显示_start
		if ("friendAdver".equals(recordType)) {
			IFriendAdverService friendAdversService = ServiceFactory.createFriendAdverService();
			// 从缓存中获取交友广告信息
			Vector friendAdverRandList = friendAdversService
					.getFriendAdverCacheList("1=1 order by id desc limit 0,50");
			FriendAdverBean friendAdver = null;
			if (friendAdverRandList.size() > 0) {
				friendAdver = (FriendAdverBean) friendAdverRandList.get(RandomUtil
						.nextInt(friendAdverRandList.size()));
			}
			if (friendAdver != null) {
				sb.append("<a href=\""
						+ ("/friendadver/friendAdverMessage.jsp?id="
								+ friendAdver.getId()) + "\">");
				String title = friendAdver.getTitle();
				if (title.length() > 8)
					title = title.substring(0, 8);
				sb.append(StringUtil.toWml(title));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// macq_2006-12-21_交友广告随机显示_end
		// macq_2006-12-21_mp3资源随机显示_start
		if ("mp3".equals(recordType)) {
			IRingService ringService = ServiceFactory.createRingService();
			// 收费铃声的catalogID
			String[] chargeRingCatalogIds = new String[] { "642", "643", "644" };
			String strWhere = " file_type like '" + recordType + "%'";
			// 排除收费的铃声分类id
			String catalogWhere = " and catalog_id not in(";
			for (int i = 0; i < chargeRingCatalogIds.length; i++) {
				catalogWhere += chargeRingCatalogIds[i] + ",";
			}
			catalogWhere = catalogWhere.substring(0, catalogWhere.length() - 1) + ")";
			strWhere = strWhere + catalogWhere;
			String condition = "select  distinct a.* from   pring as a join pring_file as b on a.id=b.pring_id where "
					+ strWhere + " order by download_sum desc ,a.name " + " limit 0,50";
			randList = ringService.getPringsList(condition);
			PringBean pRring = null;
			if (randList.size() > 0) {
				pRring = (PringBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (pRring != null) {
				sb.append("<a href=\""
						+ ("/ring/RingInfo.do?ringId=" + pRring.getId()) + "\">");
				String name = pRring.getName();
				if (name.length() > 8)
					name = name.substring(0, 8);
				sb.append(StringUtil.toWml(name));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// macq_2006-12-21_mp3资源随机显示_end
		// macq_2007-6-14_论坛随机显示_start
		else if ("jcforum".equals(recordType)) {
			StringBuilder ret = new StringBuilder();
			String condition = "";
			IJcForumService forumService = ServiceFactory.createJcForumService();
			// macq_2007-3-31_更改_start
			condition = "forum_id= " + catalogId + " and mark1=1 " + " order by id DESC limit 0,20";// 需要修改查询条件
			Vector formList = (Vector) ForumCacheUtil.getForumContentList(condition);
			if (formList.size() == 0) {
				return ret.toString();
			}
			int temp = RandomUtil.nextInt(formList.size());
			ForumContentBean forum = (ForumContentBean) formList.get(temp);
			// macq_2007-3-31_更改_end
			if (forum != null) {
				ret.append("<a href=\""
						+ ("/jcforum/viewContent.jsp?forumId="
								+ forum.getForumId() + "&amp;contentId=" + forum.getId()) + "\">");
				ret.append(StringUtil.toWml(StringUtil.limitString(forum.getTitle(), 14)));
				ret.append("</a>");
				return ret.toString();
			}
		}
		// 书城添加论坛的精华帖
		else if ("Sjcforum".equals(recordType)) {
			StringBuilder ret = new StringBuilder();
			String condition = "";
			IJcForumService forumService = ServiceFactory.createJcForumService();

			condition = "forum_id= " + catalogId
					+ " and mark1=1 and TO_DAYS(now()) - TO_DAYS(create_datetime) < " + day
					+ " order by id DESC limit 0,3";
			Vector formList = (Vector) forumService.getForumContentList(condition);
			if (formList.size() == 0) {
				return ret.toString();
			}
			// int temp = RandomUtil.nextInt(formList.size());
			for (int i = 0; i < formList.size(); i++) {
				ForumContentBean forum = (ForumContentBean) formList.get(i);

				if (forum != null) {
					ret.append("<a href=\""
							+ ("/jcforum/viewContent.jsp?forumId="
									+ forum.getForumId() + "&amp;contentId=" + forum.getId())
							+ "\">");
					ret.append(StringUtil.toWml(forum.getTitle()));
					ret.append("</a>");
					return ret.toString();
				}
			}
		}
		// macq_2007-6-14_论坛随机显示_end
		// 新闻
		else if ("news".equals(recordType)) {
			INewsService newsService = ServiceFactory.createNewsService();
			String condition = "catalog_id in (" + catalogId
					+ ") and TO_DAYS(now()) - TO_DAYS(release_date) <= " + day
					+ " order by id desc limit 0, 50";
			randList = newsService.getNewsList(condition);
			NewsBean news = null;
			if (randList.size() > 0) {
				news = (NewsBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (news != null) {
				sb.append("<a href=\""
						+ ("/news/NewsInfo.do?newsId=" + news.getId()
								+ "&amp;backTo=/Column.do?columnId=3840") + "\">");
				sb.append(StringUtil.toWml(news.getTitle()));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 电子书
		else if ("ebook".equals(recordType)) {
			IEBookService ebookService = ServiceFactory.createEBookService();
			String condition = "catalog_id in (" + catalogId
					+ ") and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by id desc limit 0, 50";
			randList = ebookService.getEBooksList(condition);
			EBookBean ebook = null;
			if (randList.size() > 0) {
				ebook = (EBookBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (ebook != null) {
				sb.append("<a href=\""
						+ ("/ebook/EBookInfo.do?ebookId=" + ebook.getId())
						+ "\">");
				String name = ebook.getName();
				if (name.length() > 8)
					name = name.substring(0, 8);
				sb.append(StringUtil.toWml(name));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 游戏
		else if ("game".equals(recordType)) {
			IGameService gameService = ServiceFactory.createGameService();
			String condition = "catalog_id in (" + catalogId
					+ ") and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by id desc limit 0, 50";
			randList = gameService.getGamesList(condition);
			GameBean game = null;
			if (randList.size() > 0) {
				game = (GameBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (game != null) {
				sb.append("<a href=\""
						+ ("/game/GameInfo.do?gameId=" + game.getId()) + "\">");
				sb.append(StringUtil.toWml(game.getName()));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 视频
		// macq_2006-12-21_视频资源随机显示_start
		else if ("video".equals(recordType)) {
			IVideoService videoService = ServiceFactory.createVideoService();
			String condition = "catalog_id in (" + catalogId
					+ ") and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by id desc limit 0, 50";
			randList = videoService.getVideoList(condition);
			VideoBean video = null;
			if (randList.size() > 0) {
				video = (VideoBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (video != null) {
				sb.append("<a href=\""
						+ ("/video/VideoInfo.do?videoId=" + video.getId())
						+ "\">");
				String name = video.getName();
				if (name.length() > 8)
					name = name.substring(0, 8);
				sb.append(StringUtil.toWml(name));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// macq_2006-12-21_视频资源随机显示_start
		// 图片
		// macq_2006-12-21_图片资源随机显示_start
		else if ("image".equals(recordType)) {
			ICatalogService catalogService = ServiceFactory.createCatalogService();
			String condition = "id in (" + catalogId + ") order by id desc limit 0, 50";
			randList = catalogService.getCatalogList(condition);
			CatalogBean catalog = null;
			if (randList.size() > 0) {
				catalog = (CatalogBean) randList.get(RandomUtil.nextInt(randList.size()));
			}
			if (catalog != null) {
				sb.append("<a href=\""
						+ ("/image/ImageCataList.do?id=" + catalog.getId())
						+ "\">");
				String name = catalog.getName();
				if (name.length() > 8)
					name = name.substring(0, 8);
				sb.append(StringUtil.toWml(name));
				sb.append("</a>");
				return sb.toString();
			}
		}
		// macq_2006-12-21_图片资源随机显示_end
		// else if ("image".equals(recordType)) {
		// IImageService imageService = ServiceFactory.createImageService();
		// String condition = "catalog_id in (" + catalogId
		// + ") and TO_DAYS(now()) - TO_DAYS(create_datetime) <= "
		// + day + " order by id desc limit 0, 50";
		// randList = imageService.getImagesList(condition);
		// ImageBean image = null;
		// if (randList.size() > 0) {
		// image = (ImageBean) randList.get(RandomUtil.nextInt(randList
		// .size()));
		// }
		// if (image != null) {
		// sb
		// .append("<a href=\""
		// + response
		// .encodeURL("/image/ImageInfo.do?imageId="
		// + image.getId())
		// + "\">");
		// sb.append(StringUtil.toWml(image.getName()));
		// sb.append("</a>");
		// return sb.toString();
		// }
		// }
		return "";
	}

	public static String getRandomRecord(HttpServletResponse response, int[] newsCatalogs,
			int[] gameCatalogs, int[] ebookCatalogs, int[] imageCatalogs) {
		Vector rts = new Vector();
		if (newsCatalogs != null) {
			rts.add("news");
		}
		if (gameCatalogs != null) {
			rts.add("game");
		}
		if (ebookCatalogs != null) {
			rts.add("ebook");
		}
		if (imageCatalogs != null) {
			rts.add("image");
		}

		int i = RandomUtil.nextInt(rts.size());
		String recordType = (String) rts.get(i);
		int catalogId = 0;
		int day = 0;

		StringBuilder sb = new StringBuilder();
		// 新闻
		if ("news".equals(recordType)) {
			catalogId = newsCatalogs[RandomUtil.nextInt(newsCatalogs.length)];
			day = 1;
			INewsService newsService = ServiceFactory.createNewsService();
			String condition = "catalog_id = " + catalogId
					+ " and TO_DAYS(now()) - TO_DAYS(release_date) <= " + day + " order by rand()";
			NewsBean news = newsService.getNews(condition);
			if (news != null) {
				sb.append("<a href=\""
						+ ("/news/NewsInfo.do?newsId=" + news.getId()) + "\">");
				sb.append(news.getTitle());
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 电子书
		else if ("ebook".equals(recordType)) {
			catalogId = ebookCatalogs[RandomUtil.nextInt(ebookCatalogs.length)];
			day = 500;
			IEBookService ebookService = ServiceFactory.createEBookService();
			String condition = "catalog_id = " + catalogId
					+ " and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by rand()";
			EBookBean ebook = ebookService.getEBook(condition);
			if (ebook != null) {
				sb.append("<a href=\""
						+ ("/ebook/EBookInfo.do?ebookId=" + ebook.getId())
						+ "\">");
				sb.append(ebook.getName());
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 游戏
		else if ("game".equals(recordType)) {
			catalogId = gameCatalogs[RandomUtil.nextInt(gameCatalogs.length)];
			day = 500;
			IGameService gameService = ServiceFactory.createGameService();
			String condition = "catalog_id = " + catalogId
					+ " and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by rand()";
			GameBean game = gameService.getGame(condition);
			if (game != null) {
				sb.append("<a href=\""
						+ ("/game/GameInfo.do?gameId=" + game.getId()) + "\">");
				sb.append(game.getName());
				sb.append("</a>");
				return sb.toString();
			}
		}
		// 图片
		else if ("image".equals(recordType)) {
			catalogId = imageCatalogs[RandomUtil.nextInt(imageCatalogs.length)];
			day = 500;
			IImageService imageService = ServiceFactory.createImageService();
			String condition = "catalog_id = " + catalogId
					+ " and TO_DAYS(now()) - TO_DAYS(create_datetime) <= " + day
					+ " order by rand()";
			ImageBean image = imageService.getImage(condition);
			if (image != null) {
				sb.append("<a href=\""
						+ ("/image/ImageInfo.do?imageId=" + image.getId())
						+ "\">");
				sb.append(image.getName());
				sb.append("</a>");
				return sb.toString();
			}
		}
		return "";
	}

	/**
	 * 
	 * @author macq
	 * @explain：随机获取任意条聊天大厅信息
	 * @datetime:2007-3-6 11:20:28
	 * @param response
	 * @param room//希望获取的信息条数
	 * @param num//希望获取的信息条数
	 * @return
	 * @return String
	 */
	public static String getChatMessage(HttpServletRequest request, HttpServletResponse response,
			int room, int num) {
		StringBuilder ret = new StringBuilder(64);
		// 获取聊天大厅num条最新的信息_start (注: 传入的值是聊天大厅id)
		Vector contentIdList = (Vector) RoomCacheUtil.getRoomContentCountMap(room);
		Vector chatList = new Vector();
		int contentCount = 0;
		if (contentIdList != null) {
			contentCount = contentIdList.size();
		}
		JCRoomContentBean roomContent = null;
		int roomContentId = 0;
		int j = 0;
		for (int i = 0; i < contentCount; i++) {
			roomContentId = ((Integer) contentIdList.get(i)).intValue();
			roomContent = RoomCacheUtil.getRoomContent(roomContentId);
			if (roomContent.getMark() == 0 && roomContent.getToId() != 0
					&& roomContent.getAttach().equals("")) {
				j++;
				chatList.add(roomContent);
				if (j == num) {
					break;
				}
			}
		}
		JCRoomChatAction chatAction = new JCRoomChatAction();
		JCRoomContentBean content = null;
		for (int i = 0; i < chatList.size(); i++) {
			content = (JCRoomContentBean) chatList.get(i);
			ret.append(chatAction.getMessageDisplay(content, request, response));
			ret.append("<br/>");
		}
		return ret.toString();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @author Liq
	 * @explain：获取美容美体最新更新的6条信息
	 * @datetime:2007-3-16
	 * @param response
	 * @param cond
	 *            //是区别是美容美体_0,还是女人心情_1,
	 * @return
	 * @return String
	 */
	public static String getWomanMessage(HttpServletRequest request, HttpServletResponse response,
			int x) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		INewsService newsService = ServiceFactory.createNewsService();
		if (x == 0)
			condition = "catalog_id = 745 ORDER BY id DESC LIMIT 0, 6";// 美容美体
		else
			condition = "catalog_id = 746 ORDER BY id DESC LIMIT 0, 6";// 女人心情
		Vector chatList = (Vector) newsService.getNewsList(condition);
		int contentCount = 0;
		if (chatList != null) {
			contentCount = chatList.size();
		}
		for (int i = 0; i < contentCount; i++) {
			NewsBean news = (NewsBean) chatList.get(i);
			if (news != null) {
				ret.append("<a href=\""
						+ ("/news/NewsInfo.do?newsId=" + news.getId()) + "\">");
				ret.append(StringUtil.toWml(news.getTitle()));
				ret.append("</a><br/>");

			}
		}
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:取得电子书指定栏目最新20本书的随机一本_start
	 * @datetime:2007-3-19
	 * @param response
	 * @param cond
	 *            //是区别是哪个栏目
	 * @return
	 * @return String
	 */
	public static String getBook(HttpServletResponse response, int x) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		Random rand = new Random();
		IEBookService ebookSer = ServiceFactory.createEBookService();
		if (x == 19)
			condition = "catalog_id in (260,261,262,263,264,265,266,267,268,269)  ORDER BY id DESC LIMIT 0, 20";// 需要修改查询条件
		else
			condition = "catalog_id = " + Integer.toString(x) + " ORDER BY id DESC LIMIT 0, 20";// 需要修改查询条件
		Vector chatList = (Vector) ebookSer.getEBooksList(condition);
		if (chatList.size() != 0) {
			int content = Math.abs(rand.nextInt() % 20);
			// System.out.println("content ="+content);
			EBookBean ebook = (EBookBean) chatList.get(content);
			// System.out.println("ebook.name ="+ebook.getName());
			if (ebook != null) {
				ret.append("<a href=\""
						+ ("/ebook/EBookInfo.do?ebookId=" + ebook.getId())
						+ "\">");
				ret.append(StringUtil.toWml(ebook.getName()));
				ret.append("</a><br/>");
			}
		} else
			ret.append(StringUtil.toWml("书库更新中..."));
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:取得 两性常识,性爱技巧 栏目最新更新的五条内容_start
	 * @datetime:2007-3-19
	 * @param response
	 * @param cond
	 *            //是区别是哪个栏目
	 * @return
	 * @return String
	 */
	public static String getSex(HttpServletRequest request, HttpServletResponse response, int x) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		INewsService newsService = ServiceFactory.createNewsService();
		if (x == 1)
			condition = "catalog_id = 748 ORDER BY id DESC LIMIT 0, 5";// 需要修改查询条件
		else if (x == 2)
			condition = "catalog_id = 749 ORDER BY id DESC LIMIT 0, 5";// 需要修改查询条件
		else if (x == 3)
			condition = "catalog_id = 750 ORDER BY id DESC LIMIT 0, 5";// 需要修改查询条件

		Vector chatList = (Vector) newsService.getNewsList(condition);
		int contentCount = 0;
		if (chatList != null) {
			contentCount = chatList.size();
		}
		for (int i = 0; i < contentCount; i++) {
			NewsBean news = (NewsBean) chatList.get(i);
			if (news != null) {
				ret.append("<a href=\""
						+ ("/news/NewsInfo.do?newsId=" + news.getId()) + "\">");
				ret.append(StringUtil.toWml(news.getTitle()));
				ret.append("</a><br/>");

			}
		}
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在最新上传的80本书中随机选取5本_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getRandBook(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		String condition = "catalog_id > 0 ORDER BY id DESC LIMIT 0, 80";// 需要修改查询条件
		String result;
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			result = (String) OsCacheUtil.get(condition, OsCacheUtil.EBOOK_CACHE_GROUP_PAGE_5,
					OsCacheUtil.EBOOK_CACHE_GROUP_PAGE_5_FLUSH_PERIOD);
			if (result != null) {
				return result;
			}
		}

		Random rand = new Random();
		IEBookService ebookSer = ServiceFactory.createEBookService();

		Vector chatList = (Vector) ebookSer.getEBooksList(condition);
		int content = 0;
		for (int i = 0; i < 5; i++) {
			content = Math.abs(rand.nextInt() % 16) + 16 * i;
			EBookBean ebook = (EBookBean) chatList.get(content);
			// System.out.println("ebook.name ="+ebook.getName());
			if (ebook != null) {
				ret.append("<a href=\""
						+ ("/ebook/EBookInfo.do?ebookId=" + ebook.getId())
						+ "\">");
				ret.append(StringUtil.toWml(ebook.getName()));
				ret.append("</a><br/>");
			}
		}

		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			if (ret != null) {
				OsCacheUtil.put(condition, ret.toString(), OsCacheUtil.EBOOK_CACHE_GROUP_PAGE_5);
			}
		}

		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:随机选择5种图书,每种图书选一本_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getTypeBook(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		String result;
		String key = "catalog_id > 0 and catalog_id <200  Group by catalog_id order by rand() limit 0,5";
		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			result = (String) OsCacheUtil.get(key, OsCacheUtil.EBOOK_CACHE_GROUP_PAGE,
					OsCacheUtil.EBOOK_CACHE_GROUP_PAGE_FLUSH_PERIOD);
			if (result != null) {
				return result;
			}
		}

		String condition = "catalog_id > 0 and catalog_id <200  Group by catalog_id order by rand() limit 0,5";
		IEBookService ebookSer = ServiceFactory.createEBookService();
		ICatalogService catalogservice = ServiceFactory.createCatalogService();
		Vector chatList = (Vector) ebookSer.getEBooksList(condition);
		for (int i = 0; i < 5; i++) {
			EBookBean ebook = (EBookBean) chatList.get(i);
			// System.out.println("ebook.name ="+ebook.getName());
			condition = "id =" + ebook.getCatalogId();
			CatalogBean catalog = catalogservice.getCatalog(condition);

			if (ebook != null) {
				ret.append("[<a href=\""
						+ ("/ebook/EBookCataList.do?id=" + catalog.getId())
						+ "\">");
				ret.append(StringUtil.toWml(catalog.getName()));
				ret.append("</a>]");
				ret.append(getBook(response, catalog.getId()));
			}
		}

		// 判断是否是用缓存
		if (OsCacheUtil.USE_CACHE) {
			if (ret != null) {
				OsCacheUtil.put(key, ret.toString(), OsCacheUtil.EBOOK_CACHE_GROUP_PAGE);
			}
		}

		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在两性频道添加午夜私语动态聊天室信息_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getSexForum(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		IJcForumService forumService = ServiceFactory.createJcForumService();
		condition = "forum_id = 6 order by id DESC limit 0,3";// 需要修改查询条件
		Vector form_list = (Vector) forumService.getForumContentList(condition);
		for (int i = 0; i < 3; i++) {
			ForumContentBean forum = (ForumContentBean) form_list.get(i);
			// System.out.println("forum ="+forum.getId()+" content=
			// "+forum.getContent());
			if (forum != null) {
				ret.append("<a href=\""
						+ ("/jcforum/viewContent.jsp?forumId=6&amp;contentId="
								+ forum.getId()) + "\">");
				ret.append(StringUtil.toWml(forum.getTitle()));
				ret.append("</a><br/>");
			}
		}
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:// Liq_-乐酷主页,判断是否登录,显示不同的链接_start
	 * @datetime:2007-3-28
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getLoginMessage(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		HttpSession session = request.getSession(false);
		UserBean loginUser = null;
		if (session != null)
			loginUser = (UserBean) session.getAttribute(Constants.LOGIN_USER_KEY);

		if (loginUser != null) {
			UserStatusBean us = UserInfoUtil.getUserStatus(loginUser.getId());
			int friendCount = UserInfoUtil.getUserOnlineFriendsCount(loginUser.getId());
			
			
			
			ret.append("<a href=\"/user/ViewFriends.do\">好友");
			ret.append(friendCount);
			ret.append("</a>|");

			ret.append("<a href=\"/bottom.jsp\">ME</a>|");
			// guip_2007-7-20 去掉信箱链接
			/*
			 * ret .append("<a href=\"" + response
			 * .encodeURL("/user/messageIndex.jsp") + "\"
			 *>"); ret.append(StringUtil.toWml("信箱")); ret.append("</a>|");
			 */
			ret.append("<a href=\"/lswjs/index.jsp\">导航</a>|");
			
			if (loginUser.getHome() == 1) {
				ret.append("<a href=\"" + ("/home/home.jsp") + "\">家园</a>");
			} else {
				ret.append("<a href=\"" + ("/home/viewAllHome.jsp") + "\">家园</a>");
			}
			ret.append("<br/>");
			// macq_2007-7-2_增加通知列表链接
			// guip_2007-7-20 去掉通知链接
			/*
			 * ret.append("<a href=\"" + response
			 * .encodeURL("/user/userMessageList.jsp") +
			 * "\">"); ret.append(StringUtil.toWml("通知"));
			 * ret.append("</a>|");
			 */

//			if (us.getTong() > 0) {
//				ret.append("<a href=\"/tong/tong.jsp?tongId=" + us.getTong() + "\">帮会</a><br/>");
//			} else {
//				ret.append("<a href=\"/tong/tongList.jsp\">帮会</a><br/>");
//			}

		} else {
			ret.append("<a href=\"/Column.do?columnId=9438\">新手入门</a>|");

			// ret
			// .append("<a href=\""
			// + response
			// .encodeURL("/jcadmin/autoRegisterNoMobile.jsp")
			// + "\">");
			// ret.append(StringUtil.toWml("快速注册"));
			// ret.append("</a>|");

			ret.append("<a href=\"/user/login.jsp?dir=1\">登录</a>|");

			ret.append("<a href=\"/register.jsp\">注册</a><br/>");
			ret.append("<a href=\"/lswjs/index.jsp\">导航中心>乐酷地图</a><br/>");
		}

		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:// Liq_-乐酷首页,取得两张相片_start
	 * @datetime:2007-3-28
	 * @param response
	 * @return
	 * @return String
	 */
	static HomeServiceImpl homeService = new HomeServiceImpl();
	public static String getPhoto(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		
		Vector photoMark = homeService.getHomePhotoTopList2("order by b.id desc limit 2");
		HomePhotoBean homePhoto = null;
		for (int i = 0; i < photoMark.size(); i++) {
			homePhoto = (HomePhotoBean) photoMark.get(i);
			ret.append("<a href=\""
					+ ("/home/homePhoto.jsp?userId=" + homePhoto.getUserId()
							+ "&amp;hit=" + homePhoto.getId() + "") + "\">");
			ret.append("<img src=\"/rep" + homePhoto.getAttach()
					+ "\" alt=\"网友照片\"/>");
			ret.append("</a>");
		}
		ret.append("<br/>");

		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:显示拍卖大厅一条信息
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getPaimai(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		AuctionBean auction = new AuctionBean();
		IDummyService dummyProduct = ServiceFactory.createDummyService();

		DummyProductBean dummybean = new DummyProductBean();
		String key = "randOneAuction";
		List auctionList = (List) OsCacheUtil.get(key, OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP,
				OsCacheUtil.USER_AUCTION_BY_ID_FLUSH_PERIOD);
		do {
			if (auctionList == null) {
				String sql = "SELECT id FROM jc_auction where mark=0 order by rand() limit 0,1";
				auctionList = SqlUtil.getIntList(sql, Constants.DBShortName);
				if (auctionList == null) {
					auctionList = new ArrayList(1);
				}
				OsCacheUtil.put(key, auctionList, OsCacheUtil.USER_AUCTION_BY_ID_CACHE_GROUP);
			}
			if (auctionList.size() == 0)
				break;
			Integer auctionId = (Integer) auctionList.get(0);
			auction = AuctionCacheUtil.getAuctionCacheById(auctionId.intValue());
			dummybean = dummyProduct.getDummyProduct("id = " + auction.getProductId());
		} while (dummybean == null);

		if ((auction != null) && (auctionList.size() != 0)) {
			ret.append("<a href=\""
					+ ("/auction/buy.jsp?auctionId=" + auction.getId() + "")
					+ "\">");
			ret.append(StringUtil.toWml(dummybean.getName()));
			ret.append("</a>");
			ret.append(StringUtil.toWml(auction.getCurrentPrice() + "乐币"));
		} else
			ret.append(StringUtil.toWml("正在结算,停止拍卖"));

		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在首页添加随意一个论坛的信息_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getRandForum(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder();
		String condition = "";
		IJcForumService forumService = ServiceFactory.createJcForumService();
		// macq_2007-3-31_更改_start
		condition = "mark1 = 1 order by id DESC limit 0,20";// 需要修改查询条件
		Vector formList = (Vector) forumService.getForumContentList(condition);
		int temp = RandomUtil.nextInt(formList.size());
		ForumContentBean forum = (ForumContentBean) formList.get(temp);
		// macq_2007-3-31_更改_end
		if (forum != null) {
			ret.append("<a href=\""
					+ ("/jcforum/viewContent.jsp?forumId=" + forum.getForumId()
							+ "&amp;contentId=" + forum.getId()) + "\">");
			ret.append(StringUtil.toWml(forum.getTitle()));
			ret.append("</a>");
		}
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在首页添加最新的交友信息_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	static IFriendAdverService friendAdverService = ServiceFactory.createFriendAdverService();
	public static String getFriend(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		
		Vector friendAdverList = friendAdverService
				.getFriendAdverCacheList("1=1  order by id desc limit 0,1");
		FriendAdverBean fab = (FriendAdverBean) friendAdverList.get(0);
		ret.append("<a href=\""
				+ ("/friendadver/friendAdverMessage.jsp?id=" + fab.getId())
				+ "\">");
		ret.append(StringUtil.toWml(fab.getTitle()));
		ret.append("</a>");
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在首页添加帮会信息_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getTong(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		List tongList = TongCacheUtil.getTongListById("userCount");
		ITongService lineService = ServiceFactory.createTongService();
		TongBean tongbean = lineService.getTong("id = " + ((Integer) tongList.get(0)).intValue());

		ret.append("<a href=\"" + ("/tong/tong.jsp?tongId=" + tongbean.getId())
				+ "\">");
		ret.append(StringUtil.toWml(tongbean.getTitle()));
		ret.append("</a>");
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在首页添加日记信息_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getDiary(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder(64);
		Vector diaryMark = homeService.getHomeDiaryTopList2("order by rand() limit 1");
		HomeDiaryBean homeDiary = (HomeDiaryBean) diaryMark.get(0);
		ret.append("<a href=\""
				+ ("/home/homeDiary.jsp?userId=" + homeDiary.getUserId())
				+ "&amp;diaryId=" + homeDiary.getId() + "\">");
		ret.append(StringUtil.toWml(homeDiary.getTitel()));
		ret.append("</a>");
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:Liq_2007-3-19_取得游戏栏目最新20个中的随机一个_start
	 * @datetime:2007-3-19
	 * @param response
	 * @param cond
	 * @return
	 * @return String
	 */
	public static String getGame(HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		Random rand = new Random();
		IGameService ebookSer = ServiceFactory.createGameService();
		condition = "catalog_id in (31,32,33,34,35,36,37,38,39,40,41,42,43)  ORDER BY id DESC LIMIT 0, 20";// 需要修改查询条件
		Vector chatList = (Vector) ebookSer.getGamesList(condition);
		if (chatList.size() != 0) {
			int content = Math.abs(rand.nextInt() % 20);
			// System.out.println("content ="+content);
			GameBean ebook = (GameBean) chatList.get(content);
			// System.out.println("ebook.name ="+ebook.getName());
			if (ebook != null) {
				ret.append("<a href=\""
						+ ("/game/GameInfo.do?gameId=" + ebook.getId()) + "\">");
				ret.append(StringUtil.toWml(ebook.getName()));
				ret.append("</a><br/>");
			}
		} else
			ret.append(StringUtil.toWml("游戏更新中..."));
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:// Liq_2007-3-19_取得图片栏目最新20个中的随机一个_start
	 * @datetime:2007-3-19
	 * @param response
	 * @param cond
	 *            //是区别是哪个栏目
	 * @return
	 * @return String
	 */
	public static String getRing(HttpServletResponse response) {
		StringBuilder ret = new StringBuilder("");
		String condition = "";
		Random rand = new Random();
		IRingService ebookSer = ServiceFactory.createRingService();
		condition = "catalog_id in (642,643,644,666,667,668,669,670,671,672,673)  ORDER BY id DESC LIMIT 0, 20";// 需要修改查询条件
		Vector chatList = (Vector) ebookSer.getPringsList(condition);
		if (chatList.size() != 0) {
			int content = Math.abs(rand.nextInt() % 20);
			// System.out.println("content ="+content);
			PringBean ebook = (PringBean) chatList.get(content);
			// System.out.println("ebook.name ="+ebook.getName());
			if (ebook != null) {
				ret.append("<a href=\""
						+ ("/ring/RingInfo.do?ringId=" + ebook.getId()) + "\">");
				ret.append(StringUtil.toWml(ebook.getName()));
				ret.append("</a><br/>");
			}
		} else
			ret.append("铃音更新中...");
		return ret.toString();
	}

	/**
	 * 
	 * @author Liq
	 * @explain:在首页添加大盘指数_start
	 * @datetime:2007-3-19
	 * @param response
	 * @return
	 * @return String
	 */
	public static String getSTOCK(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder ret = new StringBuilder();
		IStockService stockService = ServiceFactory.createStockService();
		StockGrailBean stockGrail = stockService
				.getStockGrail("1=1 order by create_datetime desc limit 0,1");
		ret.append(stockGrail.getNowPrice());
		return ret.toString();
	}

}
