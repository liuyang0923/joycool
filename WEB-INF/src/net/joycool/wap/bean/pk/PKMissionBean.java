package net.joycool.wap.bean.pk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author macq
 * @explain：
 * @datetime:2007-4-6 13:15:01
 */
public class PKMissionBean extends PKProtoBean {
	int id;

	String description;

	String objs;

	String prize;

	List objList = new ArrayList();

	List prizeList = new ArrayList();

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
	 * @return Returns the objs.
	 */
	public String getObjs() {
		return objs;
	}

	/**
	 * @param objs
	 *            The objs to set.
	 */
	public void setObjs(String objs) {
		this.objs = objs;
	}

	/**
	 * @return Returns the prize.
	 */
	public String getPrize() {
		return prize;
	}

	/**
	 * @param prize
	 *            The prize to set.
	 */
	public void setPrize(String prize) {
		this.prize = prize;
	}

	/**
	 * @return Returns the objList.
	 */
	public List getObjList() {
		return objList;
	}

	/**
	 * @param objList
	 *            The objList to set.
	 */
	public void setObjList(List objList) {
		this.objList = objList;
	}

	/**
	 * @return Returns the prizeList.
	 */
	public List getPrizeList() {
		return prizeList;
	}

	/**
	 * @param prizeList
	 *            The prizeList to set.
	 */
	public void setPrizeList(List prizeList) {
		this.prizeList = prizeList;
	}
}
