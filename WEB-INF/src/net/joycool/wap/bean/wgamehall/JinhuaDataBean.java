/*
 * Created on 2006-3-9
 *
 */
package net.joycool.wap.bean.wgamehall;

/**
 * @author lbj
 *  
 */
public class JinhuaDataBean {
	//
    public static int ACTION_STAKE = 0;
    //开牌
    public static int ACTION_OPEN = 7;

    public static int ACTION_GIVE_UP = 1;

    public static int ACTION_SUE_FOR_PEACE = 2;

    public static int ACTION_DENY_PEACE = 6;

    public static int ACTION_INVITE = 3;

    public static int ACTION_ACCEPT_INVITATION = 4;

    public static int ACTION_DENY_INVITATION = 5;
    
    boolean end = false;		// 游戏是否已经结束，如果已经结束不删除，但是不给奖励

    long lastActiveTime;

    int lastActiveUserId;

    String uniqueMark;

    int actionType;

    int sueForPeaceUserId1;

    int sueForPeaceUserId2;

    //牌的类型
    int leftCardLevel;
    
    int rightCardLevel;
    
    int L_1_L;
    int L_1_R;
    int L_2_L;
    int L_2_R;
    int L_3_L;
    int L_3_R;
    
    int R_1_L;
    int R_1_R;
    int R_2_L;
    int R_2_R;
    int R_3_L;
    int R_3_R;
    //赌注
    int leftStake;
    
    int rightStake;
    //总赌池
    int totalStake;
    //每局下赌注的多少
    int stakeLevel;
    //1是LEFT 2是RIGHT
    int winner;
    
    int[] level_stake = {1000,10000,20000,50000,100000,1000000};
    
    int[] level_bottom = {500,1000,2000,5000,10000,100000};
    
    int[] exp = {0,30,20,10,10,10,5,5};

    /**
     * @return Returns the sueForPeaceUserId1.
     */
    public int getSueForPeaceUserId1() {
        return sueForPeaceUserId1;
    }

    /**
     * @param sueForPeaceUserId1
     *            The sueForPeaceUserId1 to set.
     */
    public void setSueForPeaceUserId1(int sueForPeaceUserId1) {
        this.sueForPeaceUserId1 = sueForPeaceUserId1;
    }

    /**
     * @return Returns the sueForPeaceUserId2.
     */
    public int getSueForPeaceUserId2() {
        return sueForPeaceUserId2;
    }

    /**
     * @param sueForPeaceUserId2
     *            The sueForPeaceUserId2 to set.
     */
    public void setSueForPeaceUserId2(int sueForPeaceUserId2) {
        this.sueForPeaceUserId2 = sueForPeaceUserId2;
    }

    /**
     * @return Returns the actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * @param actionType
     *            The actionType to set.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public JinhuaDataBean() {
//        leftCard = new int[3];
//        rightCard = new int[3];
    }

    /**
     * @return Returns the uniqueMark.
     */
    public String getUniqueMark() {
        return uniqueMark;
    }

    /**
     * @param uniqueMark
     *            The uniqueMark to set.
     */
    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    /**
     * @return Returns the lastActiveTime.
     */
    public long getLastActiveTime() {
        return lastActiveTime;
    }

    /**
     * @param lastActiveTime
     *            The lastActiveTime to set.
     */
    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    /**
     * @return Returns the lastActiveUserId.
     */
    public int getLastActiveUserId() {
        return lastActiveUserId;
    }

    /**
     * @param lastActiveUserId
     *            The lastActiveUserId to set.
     */
    public void setLastActiveUserId(int lastActiveUserId) {
        this.lastActiveUserId = lastActiveUserId;
    }

	/**
	 * @return 返回 leftCardLevel。
	 */
	public int getLeftCardLevel() {
		return leftCardLevel;
	}

	/**
	 * @param leftCardLevel 要设置的 leftCardLevel。
	 */
	public void setLeftCardLevel(int leftCardLevel) {
		this.leftCardLevel = leftCardLevel;
	}

	/**
	 * @return 返回 rightCardLevel。
	 */
	public int getRightCardLevel() {
		return rightCardLevel;
	}

	/**
	 * @param rightCardLevel 要设置的 rightCardLevel。
	 */
	public void setRightCardLevel(int rightCardLevel) {
		this.rightCardLevel = rightCardLevel;
	}

	/**
	 * @return 返回 l_1_L。
	 */
	public int getL_1_L() {
		return L_1_L;
	}

	/**
	 * @param l_1_l 要设置的 l_1_L。
	 */
	public void setL_1_L(int l_1_l) {
		L_1_L = l_1_l;
	}

	/**
	 * @return 返回 l_1_R。
	 */
	public int getL_1_R() {
		return L_1_R;
	}

	/**
	 * @param l_1_r 要设置的 l_1_R。
	 */
	public void setL_1_R(int l_1_r) {
		L_1_R = l_1_r;
	}

	/**
	 * @return 返回 l_2_L。
	 */
	public int getL_2_L() {
		return L_2_L;
	}

	/**
	 * @param l_2_l 要设置的 l_2_L。
	 */
	public void setL_2_L(int l_2_l) {
		L_2_L = l_2_l;
	}

	/**
	 * @return 返回 l_2_R。
	 */
	public int getL_2_R() {
		return L_2_R;
	}

	/**
	 * @param l_2_r 要设置的 l_2_R。
	 */
	public void setL_2_R(int l_2_r) {
		L_2_R = l_2_r;
	}

	/**
	 * @return 返回 l_3_L。
	 */
	public int getL_3_L() {
		return L_3_L;
	}

	/**
	 * @param l_3_l 要设置的 l_3_L。
	 */
	public void setL_3_L(int l_3_l) {
		L_3_L = l_3_l;
	}

	/**
	 * @return 返回 l_3_R。
	 */
	public int getL_3_R() {
		return L_3_R;
	}

	/**
	 * @param l_3_r 要设置的 l_3_R。
	 */
	public void setL_3_R(int l_3_r) {
		L_3_R = l_3_r;
	}

	/**
	 * @return 返回 r_1_L。
	 */
	public int getR_1_L() {
		return R_1_L;
	}

	/**
	 * @param r_1_l 要设置的 r_1_L。
	 */
	public void setR_1_L(int r_1_l) {
		R_1_L = r_1_l;
	}

	/**
	 * @return 返回 r_1_R。
	 */
	public int getR_1_R() {
		return R_1_R;
	}

	/**
	 * @param r_1_r 要设置的 r_1_R。
	 */
	public void setR_1_R(int r_1_r) {
		R_1_R = r_1_r;
	}

	/**
	 * @return 返回 r_2_L。
	 */
	public int getR_2_L() {
		return R_2_L;
	}

	/**
	 * @param r_2_l 要设置的 r_2_L。
	 */
	public void setR_2_L(int r_2_l) {
		R_2_L = r_2_l;
	}

	/**
	 * @return 返回 r_2_R。
	 */
	public int getR_2_R() {
		return R_2_R;
	}

	/**
	 * @param r_2_r 要设置的 r_2_R。
	 */
	public void setR_2_R(int r_2_r) {
		R_2_R = r_2_r;
	}

	/**
	 * @return 返回 r_3_L。
	 */
	public int getR_3_L() {
		return R_3_L;
	}

	/**
	 * @param r_3_l 要设置的 r_3_L。
	 */
	public void setR_3_L(int r_3_l) {
		R_3_L = r_3_l;
	}

	/**
	 * @return 返回 r_3_R。
	 */
	public int getR_3_R() {
		return R_3_R;
	}

	/**
	 * @param r_3_r 要设置的 r_3_R。
	 */
	public void setR_3_R(int r_3_r) {
		R_3_R = r_3_r;
	}

	public int getLeftStake() {
		return leftStake;
	}

	public void setLeftStake(int leftStake) {
		this.leftStake = leftStake;
	}

	public int getRightStake() {
		return rightStake;
	}

	public void setRightStake(int rightStake) {
		this.rightStake = rightStake;
	}

	public int getStakeLevel() {
		return stakeLevel;
	}

	public void setStakeLevel(int stakeLevel) {
		this.stakeLevel = stakeLevel;
	}

	public int getTotalStake() {
		return totalStake;
	}

	public void setTotalStake(int totalStake) {
		this.totalStake = totalStake;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int[] getLevel_bottom() {
		return level_bottom;
	}

	public void setLevel_bottom(int[] level_bottom) {
		this.level_bottom = level_bottom;
	}

	public int[] getLevel_stake() {
		return level_stake;
	}

	public void setLevel_stake(int[] level_stake) {
		this.level_stake = level_stake;
	}

	public int[] getExp() {
		return exp;
	}

	public void setExp(int[] exp) {
		this.exp = exp;
	}

	/**
	 * @return Returns the end.
	 */
	public boolean isEnd() {
		return end;
	}

	/**
	 * @param end The end to set.
	 */
	public void setEnd(boolean end) {
		this.end = end;
	}
}
