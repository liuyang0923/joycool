/*
 * Created on 2006-4-29
 *
 */
package jc.guest.wgame;

import java.util.Vector;

/**
 * @author lbj
 *
 */
public class WGameDataAction {
    static int MAX_LENGTH = 1000;
    public static Vector rumorList;
    
    /**
	 * 取得谣言列表
	 * 
	 * @return
	 */
	public static Vector getRumorList() {
		if (rumorList == null) {
			rumorList = new Vector();
		}
		return rumorList;
	}
	
	/**
	 * 加入一条谣言。
	 * 
	 * @param rumor
	 */
	public static void addRumor(String rumor) {
		Vector rs = getRumorList();
		//liuyi 2006-12-01 程序优化 start
		//synchronized (rs) 
		{
			if (rs.size() >= MAX_LENGTH) {
				rs.remove(MAX_LENGTH - 1);
				rs.insertElementAt(rumor, 0);
			} else {
				rs.insertElementAt(rumor, 0);
			}
		}
		//liuyi 2006-12-01 程序优化 end
	}
}
