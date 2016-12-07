/**
 * 处理攻击队列的线程
 */
package net.joycool.wap.spec.castle;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.joycool.wap.util.LogUtil;

public class AttackThread extends Thread {

//	public static SortedSet attackQuery = new TreeSet();
//	CastleService castleService = CastleService.getInstance();
	
	public AttackThread(){
//		CacheService.getAllCacheAttack();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(ResNeed.ATTACK_INTERVAL * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			LogUtil.logTime("AttackThread*****start");
			try {
				synchronized(AttackThread.class) {
					List list = CacheService.getAllCacheAttack();
					Iterator it = list.iterator();
					
					while(it.hasNext()) {
						AttackThreadBean bean = (AttackThreadBean)it.next();
						bean.execute();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				//处理商人
				MerchantBean.run();
			} catch(Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("AttackThread*****end");
		}
	}

	
	
}
