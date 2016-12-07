package net.joycool.wap.spec.buyfriends;

import java.util.Iterator;
import java.util.List;

import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.util.DateUtil;

public class ThreadBuyFriend{

	public static void run() {
			ServiceSlave serviceSlave = ServiceSlave.getInstance();
			
			List list = serviceSlave.getAllPunish();
			Iterator iterator = list.iterator();
			
			while(iterator.hasNext()) {
				BeanFlag bean = (BeanFlag)iterator.next();
				bean.process();
			}
			
			//serviceSlave.deleteTempSlave();
	}
}
