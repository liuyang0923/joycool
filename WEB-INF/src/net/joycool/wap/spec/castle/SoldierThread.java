package net.joycool.wap.spec.castle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.joycool.wap.util.LogUtil;
import net.joycool.wap.util.db.DbOperation;


public class SoldierThread extends Thread {

	//士兵生产的队列
//	public static SortedSet soilerQuery = new TreeSet();	
	
	public SoldierThread(){
//		CacheService.getAllCacheSoldier();
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(ResNeed.SOLDIER_INTERVAL * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			LogUtil.logTime("SoldierThread*****start");
			try {
				synchronized(SoldierThread.class) {
					List list = CacheService.getAllCacheSoldier();
					Iterator it = list.iterator();
					while(it.hasNext()) {
						SoldierThreadBean bean = (SoldierThreadBean)it.next();
						bean.produceSoldier();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("SoldierThread*****end");
		}
	}

	
	
}
