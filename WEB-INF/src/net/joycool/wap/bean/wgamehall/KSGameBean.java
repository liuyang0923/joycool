package net.joycool.wap.bean.wgamehall;

import java.util.Iterator;

import net.joycool.wap.action.wgamehall.JinhuaAction;

/**
 * @author macq
 * @explain：多回合游戏快速开始
 * @datetime:2007-6-4 1:45:52
 */
public class KSGameBean {
	int userId;

	long createDatetime;

	int type;

	public long getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(long createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断快速开始
	 * @datetime:2007-6-4 2:12:22
	 * @return
	 * @return ksGameBean
	 */
	public KSGameBean getksGame() {
		KSGameBean ksGame = null;
		Iterator it = JinhuaAction.ksGameList.iterator();
		while (it.hasNext()) {
			KSGameBean ksGame1 = (KSGameBean) it.next();
			// 判断游戏创建时间是否大约60秒
			long time = countTime(ksGame1);
			if (time > JinhuaAction.KS_TIMEOVER) {
				synchronized (JinhuaAction.lock) {
					it.remove();
				}
				continue;
			}
			// 判断游戏赌注类型是否一致
			else if (ksGame1.getType() != type) {
				continue;
			} else {
				ksGame = ksGame1;
				synchronized (JinhuaAction.lock) {
					it.remove();
				}
				break;
			}
		}
		return ksGame;
	}

	/**
	 * 
	 * @author macq
	 * @explain：判断用户是否已经存在在快速开始中
	 * @datetime:2007-6-4 6:44:27
	 * @return
	 * @return KSGameBean
	 */
	public boolean checkksGame() {
		KSGameBean ksGame = null;
		Iterator it = JinhuaAction.ksGameList.iterator();
		while (it.hasNext()) {
			KSGameBean ksGame1 = (KSGameBean) it.next();
			if (ksGame1.getUserId() == userId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author macq
	 * @explain：计算时间
	 * @datetime:2007-6-4 2:13:31
	 * @param ksGame
	 * @return
	 * @return ksGameBean
	 */
	public long countTime(KSGameBean ksGame) {
		long a = ksGame.getCreateDatetime();
		long b = System.currentTimeMillis();
		return b - a;
	}
}
