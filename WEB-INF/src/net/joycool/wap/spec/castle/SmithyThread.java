package net.joycool.wap.spec.castle;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.joycool.wap.util.LogUtil;

public class SmithyThread extends Thread {
	
	//防御升级列表
//	public static SortedSet smithyQuery = new TreeSet();
	
//	CastleService castleService = CastleService.getInstance();
	
	public SmithyThread(){
//		CacheService.getAllCacheSoldierSmithy();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(ResNeed.DEFENCE_INTERVAL * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			LogUtil.logTime("SmithyThread*****start");
			try {
				synchronized(SmithyThread.class) {
					List list = CacheService.getAllCacheSoldierSmithy();
					Iterator it = list.iterator();
					
					while(it.hasNext()) {
						SmithyThreadBean bean = (SmithyThreadBean)it.next();
						bean.upgrade();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("SmithyThread*****end");
		}
	}

}
