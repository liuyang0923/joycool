/**
 * 
 */
package net.joycool.wap.action.job;

import java.util.HashMap;
import java.util.Random;

import net.joycool.wap.service.factory.ServiceFactory;
import net.joycool.wap.service.infc.IJobService;
import net.joycool.wap.util.Constants;

/**
 * @author zhul 2006-08-29 将一天的运势信息加载到内存中
 * 
 */
public class LuckAction {

	private static HashMap luckMap = null;

	//liuyi 2006-12-01 程序优化 start
	static{
          init();
	}
	
	public static void init(){
//		 随机获取一天的12星座运势
		Random random = new Random();
		luckMap = new HashMap();
		IJobService jobService = ServiceFactory.createJobService();
		while (luckMap.size() == 0) {
			int day = random.nextInt(Constants.LUCK_DAYS) + 1;
			luckMap = jobService.getLuckBeanMap("day=" + day + " order by id");
		}
		// 显示内存信息
	}
	
	public static HashMap getLuckMap() {	
		return luckMap;
	}
	//liuyi 2006-12-01 程序优化 end

	// 清空缓存
	public static void clearLuckMap() {
		init();
	}

}
