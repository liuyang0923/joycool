/*
 * Created on 2006-4-28
 *
 */
package net.joycool.wap.bean;

/**
 * @author lbj
 *  
 */
public class OnlineBean {
    int id;

    int userId;

    int status;

    int positionId;

    String sessionId;
    //macq_2006-12-25_所属城邦_start
    int tongId;
    //macq_2006-12-25_所属城邦_end
    //zhul_2006-08-22 添加position下的子位置属性
    int subId;

    /**
	 * @return Returns the subId.
	 */
	public int getSubId() {
		return subId;
	}

	/**
	 * @param subId The subId to set.
	 */
	public void setSubId(int subId) {
		this.subId = subId;
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
     * @return Returns the positionId.
     */
    public int getPositionId() {
        return positionId;
    }

    /**
     * @param positionId
     *            The positionId to set.
     */
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return Returns the status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status
     *            The status to set.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

	/**
	 * @return tongId
	 */
	public int getTongId() {
		return tongId;
	}

	/**
	 * @param tongId 要设置的 tongId
	 */
	public void setTongId(int tongId) {
		this.tongId = tongId;
	}
}
