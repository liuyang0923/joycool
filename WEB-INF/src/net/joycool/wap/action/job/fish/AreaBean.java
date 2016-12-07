package net.joycool.wap.action.job.fish;

import java.util.List;


/**
 * @author bomb
 *
 */
public class AreaBean {
	
	int id;
	String name;
	int rand;		// 初始出现几率
	int curRand = 0;	// 当前的出现几率
	String image;
	
	public List fishList = null;		// 这个区域出现的鱼列表
	public List pullEventList = null;
	
	/**
	 * @return Returns the curRand.
	 */
	public int getCurRand() {
		return curRand;
	}
	/**
	 * @param curRand The curRand to set.
	 */
	public void setCurRand(int curRand) {
		this.curRand = curRand;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the rand.
	 */
	public int getRand() {
		return rand;
	}
	/**
	 * @param rand The rand to set.
	 */
	public void setRand(int rand) {
		this.rand = rand;
	}
	
	public void resetCurRand() {
		curRand = rand;
	}
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

}
