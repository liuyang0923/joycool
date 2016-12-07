/**
 * 处理建筑队列的线程
 */
package net.joycool.wap.spec.castle;

import java.util.Iterator;
import java.util.List;

import net.joycool.wap.util.LogUtil;

public class BuildingThread extends Thread {
	
	//public static TreeSet buildingQuery = new TreeSet();
	
	public BuildingThread(){
		//CacheService.getAllCacheBuilding();
	}
	
	public void run() {
		
		while(true){
			
			try {
				Thread.sleep(ResNeed.BUILDING_INTERVAL * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			LogUtil.logTime("BuildingThread*****start");
			try {
				synchronized(BuildingThread.class) {
					List list = CacheService.getAllCacheBuilding();
					if(list == null)
						continue;
					
					Iterator it = list.iterator();
					
					while(it.hasNext()) {
						BuildingThreadBean bean = (BuildingThreadBean)it.next();
						bean.startBuild();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				synchronized(BuildingThread.class) {
					List list = CacheService.getAllCacheCommon();
					if(list == null)
						continue;
					
					Iterator it = list.iterator();
					
					while(it.hasNext()) {
						CommonThreadBean bean = (CommonThreadBean)it.next();
						bean.execute();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LogUtil.logTime("BuildingThread*****end");
		}
	}

}
