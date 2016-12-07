/*
 * mcq_1_total 2006-6-6
 */
package net.joycool.wap.bean;

public class RankActionBean {
	int id;

	int RankId;

	int needGamePoint;

	String ActionName;

	String sendMessage;

	String receiveMessage;

	/**
	 * @return Returns the actionName.
	 */
	public String getActionName() {
		return ActionName;
	}

	/**
	 * @param actionName
	 *            The actionName to set.
	 */
	public void setActionName(String actionName) {
		ActionName = actionName;
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
	 * @return Returns the needGamePoint.
	 */
	public int getNeedGamePoint() {
		return needGamePoint;
	}

	/**
	 * @param needGamePoint
	 *            The needGamePoint to set.
	 */
	public void setNeedGamePoint(int needGamePoint) {
		this.needGamePoint = needGamePoint;
	}

	/**
	 * @return Returns the rankId.
	 */
	public int getRankId() {
		return RankId;
	}

	/**
	 * @param rankId
	 *            The rankId to set.
	 */
	public void setRankId(int rankId) {
		RankId = rankId;
	}

	/**
	 * @return Returns the receiveMessage.
	 */
	public String getReceiveMessage() {
		return receiveMessage;
	}

	/**
	 * @param receiveMessage
	 *            The receiveMessage to set.
	 */
	public void setReceiveMessage(String receiveMessage) {
		this.receiveMessage = receiveMessage;
	}

	/**
	 * @return Returns the sendMessage.
	 */
	public String getSendMessage() {
		return sendMessage;
	}

	/**
	 * @param sendMessage
	 *            The sendMessage to set.
	 */
	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}
}
