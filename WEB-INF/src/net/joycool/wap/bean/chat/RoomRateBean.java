/**
 * 
 */
package net.joycool.wap.bean.chat;

/**
 * @author Administrator
 * 
 */
public class RoomRateBean {

	private int id;
	
	private int roomId;

	private String name;

	private int rate;

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
	 * @return Returns the rate.
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate The rate to set.
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return Returns the roomId.
	 */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * @param roomId The roomId to set.
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	

}
