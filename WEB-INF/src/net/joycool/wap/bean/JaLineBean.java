/*
 * Created on 2005-11-28
 *
 */
package net.joycool.wap.bean;

import net.joycool.wap.util.Constants;
import net.joycool.wap.util.StringUtil;

/**
 * @author lbj
 * 
 */
public class JaLineBean {
	public static int LT_COLUMN = 0; // 父栏目

	public static int LT_LINK = 1; // 自定义链接

	public static int LT_NEWS_CATALOG = 2;

	public static int LT_NEWS = 3;

	public static int LT_IMAGE_CATALOG = 4;

	public static int LT_IMAGE = 5;

	public static int LT_IMAGE_NO_THUMBNAIL = 11;

	public static int LT_NO_LINK = 6;

	public static int LT_EBOOK_CATALOG = 7;

	public static int LT_EBOOK = 8;

	public static int LT_GAME_CATALOG = 9;

	public static int LT_GAME = 10;

	public static int LT_RING_CATALOG = 21;

	public static int LT_RING = 22;

	public static int LT_VIDEO_CATALOG = 23;

	public static int LT_VIDEO = 24;

	// 栏目显示类型
	public static int DT_NULL_ICON_NULL = 0;

	public static int DT_NULL_TEXT_NULL = 1;

	public static int DT_ICON_TEXT_NULL = 2;

	public static int DT_NULL_TEXT_ICON = 3;

	public static int DT_ICON_TEXT_ICON = 4;
	
	public static int DT_NULL_FUNC_NULL = 5;
	
	public static int DT_NULL_WML_NULL = 6;

	// 子封面分段格式
	public static int CPS_HAS_NUMBER = 1;

	public static int CPS_NO_NUMBER = 0;

	// id
	int id;

	// 父id
	int parentId;

	// 名称
	String name;

	// wml前端title
	String title;

	// 栏目置顶图片
	String topIcon;

	String topIconURL;

	// 栏目置顶文字
	String topText;

	// 栏目顺序
	int lineIndex;

	// 链接类型
	int linkType;

	// 栏目链接地址
	String link;

	String linkURL;

	// 栏目显示类型
	int displayType;

	// 内容
	String leftWap10;

	String leftWap20;

	String leftIcon;

	String leftIconURL;

	String centerWap10;

	String centerWap20;

	String centerIcon;

	String centerIconURL;

	String rightWap10;

	String rightWap20;

	String rightIcon;

	String rightIconURL;

	// 子封面一页显示数量
	int childCountPerPage;

	// 子封面段落分段格式
	int childParagraphStyle;

	// 子封面对其方式
	String childAlign;

	// 子封面背景图
	String childBgimage;

	String childBgimageURL;

	// 子封面背景音乐
	String childBgmusic;

	String childBgmusicURL;

	// 封面时间控制
	int dateControl;

	String dateStart;

	String dateEnd;

	// 封面定时控制
	int timeControl;

	int timeStart;

	int timeEnd;

	// 备注
	String description;

	// 结束符
	String lineEnd;

	// 返回上一级链接
	String backTo;

	// ua限制
	String uaRestrict;

	// ip限制
	String ipRestrict;

	String wap10Content;

	String wap20Content;

	String linkDescription;

	// 登录限制
	int loginRestrict;

	int mark;

	String rootBackTo;

	int wapType;

	int childWapType;

	/**
	 * @return Returns the childWapType.
	 */
	public int getChildWapType() {
		return childWapType;
	}

	/**
	 * @param childWapType
	 *            The childWapType to set.
	 */
	public void setChildWapType(int childWapType) {
		this.childWapType = childWapType;
	}

	/**
	 * @return Returns the wapType.
	 */
	public int getWapType() {
		return wapType;
	}

	/**
	 * @param wapType
	 *            The wapType to set.
	 */
	public void setWapType(int wapType) {
		this.wapType = wapType;
	}

	/**
	 * @return Returns the mark.
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * @param mark
	 *            The mark to set.
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * @return Returns the rootBackTo.
	 */
	public String getRootBackTo() {
		return rootBackTo;
	}

	/**
	 * @param rootBackTo
	 *            The rootBackTo to set.
	 */
	public void setRootBackTo(String rootBackTo) {
		this.rootBackTo = rootBackTo;
	}

	/**
	 * @return Returns the backTo.
	 */
	public String getBackTo() {
		return backTo;
	}

	/**
	 * @param backTo
	 *            The backTo to set.
	 */
	public void setBackTo(String backTo) {
		this.backTo = backTo;
	}

	/**
	 * @return Returns the centerIcon.
	 */
	public String getCenterIcon() {
		return centerIcon;
	}

	/**
	 * @param centerIcon
	 *            The centerIcon to set.
	 */
	public void setCenterIcon(String centerIcon) {
		this.centerIcon = centerIcon;
	}

	/**
	 * @return Returns the centerIconURL.
	 */
	public String getCenterIconURL() {
		if (centerIcon.equals("")) {
			return "images/point.gif";
		} else {
			return Constants.JA_ICON_RESOURCE_ROOT_URL + centerIcon;
		}
	}

	/**
	 * @param centerIconURL
	 *            The centerIconURL to set.
	 */
	public void setCenterIconURL(String centerIconURL) {
		this.centerIconURL = centerIconURL;
	}

	/**
	 * @return Returns the centerWap10.
	 */
	public String getCenterWap10() {
		return centerWap10;
	}

	/**
	 * @param centerWap10
	 *            The centerWap10 to set.
	 */
	public void setCenterWap10(String centerWap10) {
		this.centerWap10 = centerWap10;
	}

	/**
	 * @return Returns the centerWap20.
	 */
	public String getCenterWap20() {
		return centerWap20;
	}

	/**
	 * @param centerWap20
	 *            The centerWap20 to set.
	 */
	public void setCenterWap20(String centerWap20) {
		this.centerWap20 = centerWap20;
	}

	/**
	 * @return Returns the childAlign.
	 */
	public String getChildAlign() {
		return childAlign;
	}

	/**
	 * @param childAlign
	 *            The childAlign to set.
	 */
	public void setChildAlign(String childAlign) {
		this.childAlign = childAlign;
	}

	/**
	 * @return Returns the childBgimage.
	 */
	public String getChildBgimage() {
		return childBgimage;
	}

	/**
	 * @param childBgimage
	 *            The childBgimage to set.
	 */
	public void setChildBgimage(String childBgimage) {
		this.childBgimage = childBgimage;
	}

	/**
	 * @return Returns the childBgimageURL.
	 */
	public String getChildBgimageURL() {
		if (childBgimage.equals("")) {
			return "images/point.gif";
		} else {
			return Constants.JA_ICON_RESOURCE_ROOT_URL + childBgimage;
		}
	}

	/**
	 * @param childBgimageURL
	 *            The childBgimageURL to set.
	 */
	public void setChildBgimageURL(String childBgimageURL) {
		this.childBgimageURL = childBgimageURL;
	}

	/**
	 * @return Returns the childBgmusic.
	 */
	public String getChildBgmusic() {
		return childBgmusic;
	}

	/**
	 * @param childBgmusic
	 *            The childBgmusic to set.
	 */
	public void setChildBgmusic(String childBgmusic) {
		this.childBgmusic = childBgmusic;
	}

	/**
	 * @return Returns the childBgmusicURL.
	 */
	public String getChildBgmusicURL() {
		return childBgmusicURL;
	}

	/**
	 * @param childBgmusicURL
	 *            The childBgmusicURL to set.
	 */
	public void setChildBgmusicURL(String childBgmusicURL) {
		this.childBgmusicURL = childBgmusicURL;
	}

	/**
	 * @return Returns the childCountPerPage.
	 */
	public int getChildCountPerPage() {
		return childCountPerPage;
	}

	/**
	 * @param childCountPerPage
	 *            The childCountPerPage to set.
	 */
	public void setChildCountPerPage(int childCountPerPage) {
		this.childCountPerPage = childCountPerPage;
	}

	/**
	 * @return Returns the childParagraphStyle.
	 */
	public int getChildParagraphStyle() {
		return childParagraphStyle;
	}

	/**
	 * @param childParagraphStyle
	 *            The childParagraphStyle to set.
	 */
	public void setChildParagraphStyle(int childParagraphStyle) {
		this.childParagraphStyle = childParagraphStyle;
	}

	/**
	 * @return Returns the dateControl.
	 */
	public int getDateControl() {
		return dateControl;
	}

	/**
	 * @param dateControl
	 *            The dateControl to set.
	 */
	public void setDateControl(int dateControl) {
		this.dateControl = dateControl;
	}

	/**
	 * @return Returns the dateEnd.
	 */
	public String getDateEnd() {
		return dateEnd;
	}

	/**
	 * @param dateEnd
	 *            The dateEnd to set.
	 */
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return Returns the dateStart.
	 */
	public String getDateStart() {
		return dateStart;
	}

	/**
	 * @param dateStart
	 *            The dateStart to set.
	 */
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the displayType.
	 */
	public int getDisplayType() {
		return displayType;
	}

	/**
	 * @param displayType
	 *            The displayType to set.
	 */
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the ipRestrict.
	 */
	public String getIpRestrict() {
		return ipRestrict;
	}

	/**
	 * @param ipRestrict
	 *            The ipRestrict to set.
	 */
	public void setIpRestrict(String ipRestrict) {
		this.ipRestrict = ipRestrict;
	}

	/**
	 * @return Returns the leftIcon.
	 */
	public String getLeftIcon() {
		return leftIcon;
	}

	/**
	 * @param leftIcon
	 *            The leftIcon to set.
	 */
	public void setLeftIcon(String leftIcon) {
		this.leftIcon = leftIcon;
	}

	/**
	 * @return Returns the leftIconURL.
	 */
	public String getLeftIconURL() {
		if (leftIcon.equals("")) {
			return "images/point.gif";
		} else {
			return Constants.JA_ICON_RESOURCE_ROOT_URL + leftIcon;
		}
	}

	/**
	 * @param leftIconURL
	 *            The leftIconURL to set.
	 */
	public void setLeftIconURL(String leftIconURL) {
		this.leftIconURL = leftIconURL;
	}

	/**
	 * @return Returns the leftWap10.
	 */
	public String getLeftWap10() {
		return leftWap10;
	}

	/**
	 * @param leftWap10
	 *            The leftWap10 to set.
	 */
	public void setLeftWap10(String leftWap10) {
		this.leftWap10 = leftWap10;
	}

	/**
	 * @return Returns the leftWap20.
	 */
	public String getLeftWap20() {
		return leftWap20;
	}

	/**
	 * @param leftWap20
	 *            The leftWap20 to set.
	 */
	public void setLeftWap20(String leftWap20) {
		this.leftWap20 = leftWap20;
	}

	/**
	 * @return Returns the lineEnd.
	 */
	public String getLineEnd() {
		if (lineEnd.equals("newLine")) {
			return "<br/>";
		} else if (lineEnd.equals("space")) {
			return " ";
		} else if (lineEnd.equals("nothing")) {
			return "";
		}
		return lineEnd;
	}

	/**
	 * @param lineEnd
	 *            The lineEnd to set.
	 */
	public void setLineEnd(String lineEnd) {
		this.lineEnd = lineEnd;
	}

	/**
	 * @return Returns the lineIndex.
	 */
	public int getLineIndex() {
		return lineIndex;
	}

	/**
	 * @param lineIndex
	 *            The lineIndex to set.
	 */
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * @return Returns the link.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            The link to set.
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return Returns the linkType.
	 */
	public int getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType
	 *            The linkType to set.
	 */
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return Returns the loginRestrict.
	 */
	public int getLoginRestrict() {
		return loginRestrict;
	}

	/**
	 * @param loginRestrict
	 *            The loginRestrict to set.
	 */
	public void setLoginRestrict(int loginRestrict) {
		this.loginRestrict = loginRestrict;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parentId.
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            The parentId to set.
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return Returns the rightIcon.
	 */
	public String getRightIcon() {
		return rightIcon;
	}

	/**
	 * @param rightIcon
	 *            The rightIcon to set.
	 */
	public void setRightIcon(String rightIcon) {
		this.rightIcon = rightIcon;
	}

	/**
	 * @return Returns the rightIconURL.
	 */
	public String getRightIconURL() {
		if (rightIcon.equals("")) {
			return "images/point.gif";
		} else {
			return Constants.JA_ICON_RESOURCE_ROOT_URL + rightIcon;
		}
	}

	/**
	 * @param rightIconURL
	 *            The rightIconURL to set.
	 */
	public void setRightIconURL(String rightIconURL) {
		this.rightIconURL = rightIconURL;
	}

	/**
	 * @return Returns the rightWap10.
	 */
	public String getRightWap10() {
		return rightWap10;
	}

	/**
	 * @param rightWap10
	 *            The rightWap10 to set.
	 */
	public void setRightWap10(String rightWap10) {
		this.rightWap10 = rightWap10;
	}

	/**
	 * @return Returns the rightWap20.
	 */
	public String getRightWap20() {
		return rightWap20;
	}

	/**
	 * @param rightWap20
	 *            The rightWap20 to set.
	 */
	public void setRightWap20(String rightWap20) {
		this.rightWap20 = rightWap20;
	}

	/**
	 * @return Returns the timeControl.
	 */
	public int getTimeControl() {
		return timeControl;
	}

	/**
	 * @param timeControl
	 *            The timeControl to set.
	 */
	public void setTimeControl(int timeControl) {
		this.timeControl = timeControl;
	}

	/**
	 * @return Returns the timeEnd.
	 */
	public int getTimeEnd() {
		return timeEnd;
	}

	/**
	 * @param timeEnd
	 *            The timeEnd to set.
	 */
	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}

	/**
	 * @return Returns the timeStart.
	 */
	public int getTimeStart() {
		return timeStart;
	}

	/**
	 * @param timeStart
	 *            The timeStart to set.
	 */
	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Returns the topIcon.
	 */
	public String getTopIcon() {
		return topIcon;
	}

	/**
	 * @param topIcon
	 *            The topIcon to set.
	 */
	public void setTopIcon(String topIcon) {
		this.topIcon = topIcon;
	}

	/**
	 * @return Returns the topIconURL.
	 */
	public String getTopIconURL() {
		if (topIcon.equals("")) {
			return "images/point.gif";
		} else {
			return Constants.JA_ICON_RESOURCE_ROOT_URL + topIcon;
		}
	}

	/**
	 * @param topIconURL
	 *            The topIconURL to set.
	 */
	public void setTopIconURL(String topIconURL) {
		this.topIconURL = topIconURL;
	}

	/**
	 * @return Returns the topText.
	 */
	public String getTopText() {
		return topText;
	}

	/**
	 * @param topText
	 *            The topText to set.
	 */
	public void setTopText(String topText) {
		this.topText = topText;
	}

	/**
	 * @return Returns the uaRestrict.
	 */
	public String getUaRestrict() {
		return uaRestrict;
	}

	/**
	 * @param uaRestrict
	 *            The uaRestrict to set.
	 */
	public void setUaRestrict(String uaRestrict) {
		this.uaRestrict = uaRestrict;
	}

	/**
	 * @return Returns the linkURL.
	 */
	public String getLinkURL() {
		if (this.linkType == JaLineBean.LT_COLUMN) {
			this.linkURL = "/Column.do?columnId="
					+ this.getId();
		} else if (this.linkType == JaLineBean.LT_LINK) {
			this.linkURL = this.link.replace("&", "&amp;");
		} else if (this.linkType == JaLineBean.LT_IMAGE
				|| this.linkType == JaLineBean.LT_IMAGE_NO_THUMBNAIL) {
			this.linkURL = "/image/ImageInfo.do?imageId="
					+ this.getLink() + "&amp;orderBy=line_index";
		} else if (this.linkType == JaLineBean.LT_IMAGE_CATALOG) {
			this.linkURL = "/image/ImageCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_NEWS) {
			this.linkURL = "/news/NewsInfo.do?newsId="
					+ this.getLink() + "&amp;orderBy=line_index";
		} else if (this.linkType == JaLineBean.LT_EBOOK_CATALOG) {
			this.linkURL = "/ebook/EBookCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_EBOOK) {
			this.linkURL = "/ebook/EBookInfo.do?ebookId="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_NEWS_CATALOG) {
			this.linkURL = "/news/NewsCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_GAME) {
			this.linkURL = "/game/GameInfo.do?gameId="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_GAME_CATALOG) {
			this.linkURL = "/game/GameCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_RING) {
			this.linkURL = "/ring/RingInfo.do?ringId="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_RING_CATALOG) {
			this.linkURL = "/ring/RingCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_VIDEO) {
			this.linkURL = "/video/VideoInfo.do?videoId="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_VIDEO_CATALOG) {
			this.linkURL = "/video/VideoCataList.do?id="
					+ this.getLink();
		} else if (this.linkType == JaLineBean.LT_NO_LINK) {
			this.linkURL = "";
		}

		return linkURL;
	}

	/**
	 * @param linkURL
	 *            The linkURL to set.
	 */
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	/**
	 * @return Returns the wap10Content.
	 */
	public String getWap10Content() {
		StringBuilder sb = new StringBuilder(64);

		if (this.displayType == JaLineBean.DT_ICON_TEXT_ICON) {
			sb.append("<img src=\"" + this.getLeftIconURL() + "\" alt=\""
					+ this.getLeftWap10() + "\"/>");
			sb.append(StringUtil.toWml(this.centerWap10));
			sb.append("<img src=\"" + this.getRightIconURL() + "\" alt=\""
					+ this.getRightWap10() + "\"/>");
		} else if (this.displayType == JaLineBean.DT_ICON_TEXT_NULL) {
			sb.append("<img src=\"" + this.getLeftIconURL() + "\" alt=\""
					+ this.getLeftWap10() + "\"/>");
			sb.append(StringUtil.toWml(this.centerWap10));
		} else if (this.displayType == JaLineBean.DT_NULL_ICON_NULL) {
			sb.append("<img src=\"");
			sb.append(getCenterIconURL());
			sb.append("\" alt=\"");
			if(centerWap10.length() == 0)
				sb.append("o");
			else
				sb.append(centerWap10);
			sb.append("\"/>");
		} else if (this.displayType == JaLineBean.DT_NULL_TEXT_ICON) {
			sb.append(this.centerWap10);
			sb.append("<img src=\"" + this.getRightIconURL() + "\" alt=\""
					+ this.getRightWap10() + "\"/>");
		} else if (this.displayType == JaLineBean.DT_NULL_TEXT_NULL) {
			sb.append(StringUtil.toWml(this.centerWap10));
		} else if (this.displayType == JaLineBean.DT_NULL_WML_NULL) {
			sb.append(this.centerWap10);
		}

		if (sb.length() == 0) {
			return "空";
		}

		return sb.toString();
	}

	/**
	 * @param wap10Content
	 *            The wap10Content to set.
	 */
	public void setWap10Content(String wap10Content) {
		this.wap10Content = wap10Content;
	}

	/**
	 * @return Returns the wap20Content.
	 */
	public String getWap20Content() {
		StringBuffer sb = new StringBuffer();

		if (this.displayType == JaLineBean.DT_ICON_TEXT_ICON) {
			sb.append("<img src=\"" + this.getLeftIconURL()
					+ "\" alt=\"loading\"/>");
			sb.append(this.centerWap20);
			sb.append("<img src=\"" + this.getRightIconURL()
					+ "\" alt=\"loading\"/>");
		} else if (this.displayType == JaLineBean.DT_ICON_TEXT_NULL) {
			sb.append("<img src=\"" + this.getLeftIconURL()
					+ "\" alt=\"loading\"/>");
			sb.append(this.centerWap20);
		} else if (this.displayType == JaLineBean.DT_NULL_ICON_NULL) {
			sb.append("<img src=\"" + this.getCenterIconURL()
					+ "\" alt=\"loading\"/>");
		} else if (this.displayType == JaLineBean.DT_NULL_TEXT_ICON) {
			sb.append(this.centerWap20);
			sb.append("<img src=\"" + this.getRightIconURL()
					+ "\" alt=\"loading\"/>");
		} else if (this.displayType == JaLineBean.DT_NULL_TEXT_NULL) {
			sb.append(this.centerWap20);
		}
		if (sb.length() == 0) {
			return "空";
		}
		return sb.toString();
	}

	/**
	 * @param wap20Content
	 *            The wap20Content to set.
	 */
	public void setWap20Content(String wap20Content) {
		this.wap20Content = wap20Content;
	}

	/**
	 * @return Returns the linkDescription.
	 */
	public String getLinkDescription() {
		// if (linkDescription != null) {
		// return linkDescription;
		// }
		//        
		// if(this.linkType == JaLineBean.LT_COLUMN){
		// linkDescription = "";
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_NO_LINK){
		// linkDescription = "";
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_LINK){
		// linkDescription = "";
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_IMAGE){
		// IImageService imageService = ServiceFactory.createImageService();
		// ImageBean image = imageService.getImage("id = " + link);
		// linkDescription = image.getName();
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_IMAGE_CATALOG){
		// ICatalogService catalogService =
		// ServiceFactory.createCatalogService();
		// CatalogBean catalog = catalogService.getCatalog("id = " + link);
		// linkDescription = catalog.getName();
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_NEWS){
		// INewsService newsService = ServiceFactory.createNewsService();
		// NewsBean news = newsService.getNews("id = " + link);
		// linkDescription = news.getTitle();
		// return linkDescription;
		// }
		// else if(this.linkType == JaLineBean.LT_NEWS_CATALOG){
		// ICatalogService catalogService =
		// ServiceFactory.createCatalogService();
		// CatalogBean catalog = catalogService.getCatalog("id = " + link);
		// linkDescription = catalog.getName();
		// return linkDescription;
		// }
		return "";
	}

	/**
	 * @param linkDescription
	 *            The linkDescription to set.
	 */
	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}
}
