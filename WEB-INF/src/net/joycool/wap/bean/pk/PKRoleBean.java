package net.joycool.wap.bean.pk;

/**
 * @author liuyi PK系统中角色的基类
 * 
 */
public class PKRoleBean {
	boolean isDeath;

	// 攻击时间
	long lastAttackTime;

	// 死亡时间
	long deathTime;

	// 动作时间
	long actionTime;

	// 离线标志位offline
	boolean offline;

	public boolean isDeath() {
		return isDeath;
	}

	public void setDeath(boolean isDeath) {
		this.isDeath = isDeath;
	}

	public long getLastAttackTime() {
		return lastAttackTime;
	}

	public void setLastAttackTime(long lastAttackTime) {
		this.lastAttackTime = lastAttackTime;
	}

	public long getDeathTime() {
		return deathTime;
	}

	public void setDeathTime(long deathTime) {
		this.deathTime = deathTime;
	}

	/**
	 * @return 返回 actionTime。
	 */
	public long getActionTime() {
		return actionTime;
	}

	/**
	 * @param actionTime
	 *            要设置的 actionTime。
	 */
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	/**
	 * @return 返回 offline。
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * @param offline
	 *            要设置的 offline。
	 */
	public void setOffline(boolean offline) {
		this.offline = offline;
	}
}
