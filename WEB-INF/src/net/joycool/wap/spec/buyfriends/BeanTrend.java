package net.joycool.wap.spec.buyfriends;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserBean;
import net.joycool.wap.bean.friend.FriendMarriageBean;
import net.joycool.wap.util.StringUtil;
import net.joycool.wap.util.UserInfoUtil;;

public class BeanTrend {

	public static final int TYPE_BUY_FRIEND = 1;	//动态买卖朋友的动态
	public static final int TYPE_SELF_INTRODUCE = 2;	//个性签名的动态
	public static final int TYPE_MY_HEAD = 3;//修改我的头像的动态
	public static final int TYPE_DIARY = 4;//日记动态
	public static final int TYPE_ALBUM = 5;//相册动态
	public static final int TYPE_BE_FRIEND = 6;//成为好友动态
	public static final int TYPE_YUANFENCESHI = 7;
	public static final int TYPE_FORUM_CONTENT = 8;
	public static final int TYPE_WGAME = 9;	// 10 -> 修改心情
	public static final int TYPE_FAMILY = 12;	// 家族动态
	public static final int TYPE_CREATE = 15;	// 创建圈子、帮会、家园等
	
	private int id;
	private int uid;	//所属某个用户的动态  对应%1
	private String nickName;	//用户昵称
	private String content;		//动态内容 %1%,%2%,%3用于替换,没有相应的则替换为空
	private Date time;
	private int type;				//动态类型
	
	private int uid2;	// 对应%2
	private String nickName2;
	/*
	 * title2和link2组合成一个
	 * <a href="<%response.encodeURL(link1)%>">title1</a>链接，
	 * 并代替 content中的 %3 的内容"
	 */
	private String title;	
	private String link;
	
	
	
	public BeanTrend() {
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		
		return content;
	}
	
	public String getContentNoLink() {
		
		String result = content;
		
		if(nickName != null) {
			result = result.replace("%1", StringUtil.toWml(nickName));
		}
		
		if(nickName2 != null) {
			result = result.replace("%2", StringUtil.toWml(nickName2));
		}

		if(title != null) {
			return result.replace("%3", StringUtil.toWml(title));
		}

		return result;
	}
	
	public String getContentNoUserLink(int loginUid, HttpServletResponse response){
		StringBuilder userLink1 = new StringBuilder();
		if(uid == loginUid) {
			userLink1.append("我");
		} else {
			userLink1.append(StringUtil.toWml(nickName));
		}
		
		StringBuilder userLink2 = new StringBuilder();
		
		if(uid2 == loginUid) {
			userLink2.append("我");
		} else if(uid2 != 0){
			String encodeUserLink2 = ("/home/home2.jsp?userId=" + uid2);
			userLink2.append("<a href=\"");
			userLink2.append(encodeUserLink2);
			userLink2.append("\">");
			userLink2.append(StringUtil.toWml(nickName2));
			userLink2.append("</a>");
		}
		String result = content.replace("%1", userLink1.toString()).replace("%2", userLink2.toString());
		
		//System.out.println(result);
		
		if(title != null) {
			StringBuilder titleLink = new StringBuilder();
			//String encodeTitleLink = response.encodeURL(link);
			titleLink.append("<a href=\"");
			titleLink.append(link);
			titleLink.append("\">");
			titleLink.append(StringUtil.toWml(title));
			titleLink.append("</a>");
			//System.out.println(result.replaceAll("%3", titleLink.toString()));
			return result.replace("%3", titleLink.toString());
		}
		return result;
	}
	
	public String getBuyFriendContent(int loginUid, HttpServletResponse response) {
		StringBuilder userLink1 = new StringBuilder();
		if(uid == loginUid) {
			userLink1.append("我");
		} else {
			String encodeUserLink1 = "/beacon/bFri/info.jsp?uid=" + uid;
			userLink1.append("<a href=\"");
			userLink1.append(encodeUserLink1);
			userLink1.append("\">");
			userLink1.append(StringUtil.toWml(nickName));
			userLink1.append("</a>");
		}
		
		StringBuilder userLink2 = new StringBuilder();
		
		if(uid2 == loginUid) {
			userLink2.append("我");
		} else if(uid2 != 0){
			String encodeUserLink2 = ("/beacon/bFri/info.jsp?uid=" + uid2);
			userLink2.append("<a href=\"");
			userLink2.append(encodeUserLink2);
			userLink2.append("\">");
			userLink2.append(StringUtil.toWml(nickName2));
			userLink2.append("</a>");
		}
		String result = content.replace("%1", userLink1.toString()).replace("%2", userLink2.toString());
		
		//System.out.println(result);
		
		if(title != null) {
			StringBuilder titleLink = new StringBuilder();
			//String encodeTitleLink = response.encodeURL(link);
			titleLink.append("<a href=\"");
			titleLink.append(link);
			titleLink.append("\">");
			titleLink.append(StringUtil.toWml(title));
			titleLink.append("</a>");
			//System.out.println(result.replaceAll("%3", titleLink.toString()));
			return result.replace("%3", titleLink.toString());
		}

		return result;
	}	
	
	/**
	 * 
	 * @param loginUid
	 * @param response
	 * @param coupleUid:“另一伴”的Uid
	 * @param isStrangerView:是否外人浏览夫妻家园
	 * 
	 */
	public String getContent(int loginUid, HttpServletResponse response,int coupleUid,FriendMarriageBean marriageBean) {
		StringBuilder userLink1 = new StringBuilder();
		StringBuilder userLink2 = new StringBuilder();
		UserBean user = UserInfoUtil.getUser(loginUid);
		StringBuilder titleLink = new StringBuilder();
		String result = "";
		String encodeUserLink1 = "";
		String encodeUserLink2 = "";
		if (marriageBean == null){
			// 没结婚
			if (user != null){
				encodeUserLink1 = "/home/home2.jsp?userId=" + uid;
				userLink1.append("<a href=\"");
				userLink1.append(encodeUserLink1);
				userLink1.append("\">");
				userLink1.append(StringUtil.toWml(nickName));
				userLink1.append("</a>");
				result = userLink1.toString();
				if(uid2 == loginUid) {
					userLink2.append("我");
				} else if(uid2 != 0){
					encodeUserLink2 = ("/home/home2.jsp?userId=" + uid2);
					userLink2.append("<a href=\"");
					userLink2.append(encodeUserLink2);
					userLink2.append("\">");
					userLink2.append(StringUtil.toWml(nickName2));
					userLink2.append("</a>");
				}
			}
		} else {
			if (marriageBean.getFromId() == loginUid || marriageBean.getToId() == loginUid){
				// 已结婚，并且“我”就是老公/老婆
				user = UserInfoUtil.getUser(coupleUid);
				if (user != null){
					encodeUserLink1 = "/home/home2.jsp?userId=" + uid;
					userLink1.append("<a href=\"");
					userLink1.append(encodeUserLink1);
					userLink1.append("\">");
//					if (loginUid == coupleUid){
//						userLink1.append("我");
//					} else {
						if (user.getGender() == 1){
							userLink1.append("老婆");
						} else {
							userLink1.append("老公");
						}
//					}
//					userLink1.append(StringUtil.toWml(nickName));
					userLink1.append("</a>");
					result = userLink1.toString();
					if(uid2 == loginUid) {
						userLink2.append("我");
					} else if(uid2 != 0){
						encodeUserLink2 = ("/home/home2.jsp?userId=" + uid2);
						userLink2.append("<a href=\"");
						userLink2.append(encodeUserLink2);
						userLink2.append("\">");
						userLink2.append(StringUtil.toWml(nickName2));
						userLink2.append("</a>");
					}
				}
			} else {
				// 别人已结婚了，我是看热闹的...
				user = UserInfoUtil.getUser(coupleUid);
				if (user != null){
					encodeUserLink1 = "/home/home2.jsp?userId=" + uid;
					userLink1.append("<a href=\"");
					userLink1.append(encodeUserLink1);
					userLink1.append("\">");
					if (user.getGender() == 1){
						userLink1.append("他老婆");
					} else {
						userLink1.append("她老公");
					}
//					userLink1.append(StringUtil.toWml(nickName));
					userLink1.append("</a>");
					result = userLink1.toString();
					if(uid2 == loginUid) {
						userLink2.append("我");
					} else if(uid2 != 0){
						encodeUserLink2 = ("/home/home2.jsp?userId=" + uid2);
						userLink2.append("<a href=\"");
						userLink2.append(encodeUserLink2);
						userLink2.append("\">");
						userLink2.append(StringUtil.toWml(nickName2));
						userLink2.append("</a>");
					}
				}
			}
		}
		if(title != null) {
//		StringBuilder titleLink = new StringBuilder();
		//String encodeTitleLink = response.encodeURL(link);
		titleLink.append("<a href=\"");
		titleLink.append(link);
		titleLink.append("\">");
		titleLink.append(StringUtil.toWml(title));
		titleLink.append("</a>");
		//System.out.println(result.replaceAll("%3", titleLink.toString()));
//		result = content.replace("%1", userLink1.toString()).replace("%2", userLink2.toString());
//		return result.replace("%3", titleLink.toString());
	}
		result = content.replace("%1", userLink1.toString()).replace("%2", userLink2.toString());
		return result.replace("%3", titleLink.toString());
	}
	
	public String getContent(int loginUid, HttpServletResponse response) {
		StringBuilder userLink1 = new StringBuilder();
		if(uid == loginUid) {
			userLink1.append("我");
		} else {
			String encodeUserLink1 = "/user/ViewUserInfo.do?userId=" + uid;
			userLink1.append("<a href=\"");
			userLink1.append(encodeUserLink1);
			userLink1.append("\">");
			userLink1.append(StringUtil.toWml(nickName));
			userLink1.append("</a>");
		}
		
		StringBuilder userLink2 = new StringBuilder();
		
		if(uid2 == loginUid) {
			userLink2.append("我");
		} else if(uid2 != 0){
			String encodeUserLink2 = ("/user/ViewUserInfo.do?userId=" + uid2);
			userLink2.append("<a href=\"");
			userLink2.append(encodeUserLink2);
			userLink2.append("\">");
			userLink2.append(StringUtil.toWml(nickName2));
			userLink2.append("</a>");
		}
		String result = content.replace("%1", userLink1.toString()).replace("%2", userLink2.toString());
		
		//System.out.println(result);
		
		if(title != null) {
			StringBuilder titleLink = new StringBuilder();
			//String encodeTitleLink = response.encodeURL(link);
			titleLink.append("<a href=\"");
			titleLink.append(link);
			titleLink.append("\">");
			titleLink.append(StringUtil.toWml(title));
			titleLink.append("</a>");
			//System.out.println(result.replaceAll("%3", titleLink.toString()));
			return result.replace("%3", titleLink.toString());
		}

		return result;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getTime() {
		return BeanVisit.converDateToBefore(this.time);
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getUid2() {
		return uid2;
	}
	public void setUid2(int uid2) {
		this.uid2 = uid2;
	}
	public String getNickName2() {
		return nickName2;
	}
	public void setNickName2(String nickName2) {
		this.nickName2 = nickName2;
	}
}
