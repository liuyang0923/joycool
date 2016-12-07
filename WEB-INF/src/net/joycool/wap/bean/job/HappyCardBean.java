package net.joycool.wap.bean.job;

import net.joycool.wap.util.Constants;

/**
 * fanys 2006-09-13 贺卡
 * 
 * @author Administrator
 * 
 */
public class HappyCardBean {
	int id;

	String title;

	String content;

	String image = "";

	int hits;

	int typeId;

	String createDateTime;

	/**
	 * @return Returns the createDateTime.
	 */
	public String getCreateDateTime() {
		return createDateTime;
	}

	/**
	 * @param createDateTime
	 *            The createDateTime to set.
	 */
	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	/**
	 * @return Returns the typeId.
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            The typeId to set.
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return Returns the hits.
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @param hits
	 *            The hits to set.
	 */
	public void setHits(int hits) {
		this.hits = hits;
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
	 * @return Returns the image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            The image to set.
	 */
	public void setImage(String image) {
		this.image = image;
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

	public String getImageUrl() {
		// liuyi 20070102 图片路径修改 start
		if (null != image)
			return Constants.RESOURCE_ROOT_URL + "img/job/happycard/" + image;
		return null;
		// liuyi 20070102 图片路径修改 end
	}
}
