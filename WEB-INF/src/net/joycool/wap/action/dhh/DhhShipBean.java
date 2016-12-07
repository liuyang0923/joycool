package net.joycool.wap.action.dhh;

/**2007.4.20
 * @author liq	
 *大航海船的bean
 */
public class DhhShipBean {

	/**
	 * ship表的id
	 */
	int id;
	
	/**
	 * ship的名称
	 */
	String name;
	
	/**
	 * ship的价格
	 */
	int price;
	
	/**
	 * ship的速度
	 */
	int speed;
	
	/**
	 * ship的容量
	 */
	int volume;

	/**
	 * ship的水手数量
	 */
	int sailor;
	String image;	// 图片文件
	
	
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
	 * @return 返回 id。
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id 要设置的 id。
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 price。
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price 要设置的 price。
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return 返回 speed。
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param speed 要设置的 speed。
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return 返回 volume。
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * @param volume 要设置的 volume。
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * @return 返回 sailor。
	 */
	public int getSailor() {
		return sailor;
	}

	/**
	 * @param sailor 要设置的 sailor。
	 */
	public void setSailor(int sailor) {
		this.sailor = sailor;
	}
}
