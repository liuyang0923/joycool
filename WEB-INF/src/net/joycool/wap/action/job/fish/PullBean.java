package net.joycool.wap.action.job.fish;


/**
 * @author bomb
 *
 */
public class PullBean {
	int id;

	String pattern;
	
	String pullMode;
	String image;

	/**
	 * @return Returns the image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image The image to set.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern The pattern to set.
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return Returns the pullMode.
	 */
	public String getPullMode() {
		return pullMode;
	}

	/**
	 * @param pullMode The pullMode to set.
	 */
	public void setPullMode(String pullMode) {
		this.pullMode = pullMode;
	}

	
}
