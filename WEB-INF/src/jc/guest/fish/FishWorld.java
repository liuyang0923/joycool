package jc.guest.fish;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.action.job.fish.AreaBean;
import net.joycool.wap.action.job.fish.FishBean;
import net.joycool.wap.action.job.fish.FishUserBean;
import net.joycool.wap.action.job.fish.GlobalEventBean;
import net.joycool.wap.action.job.fish.PullBean;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;

/**
 * @author bomb
 * @explain：	钓鱼
 * @datetime:2007-4-23 10:10
 */
public class FishWorld {
	
	
	static FishService service = new FishService();
	public static FishWorld world = new FishWorld();

	public static HashMap userMap = new HashMap();

	public List fishList = null;		// 鱼
	public Map fishMap = null;
	public List pullList = null;		// 拉杆
	public Map pullMap = null;
	public List areaList = null;		// 区域
	public Map areaMap = null;
	public List globalEventList = null;
	public List pullEventList = null;
	
	SimpleGameLog log = new SimpleGameLog();
	
	GlobalEventBean currentGlobalEvent = null;
	
	/**
	 * 定时处理
	 */
	public void timeTask() {
		if (userMap != null) {
			HashMap map = (HashMap) userMap.clone();
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				Integer id = (Integer) iter.next();
				FishUserBean bean = (FishUserBean) map.get(id);
				if (bean != null && bean.userOffline()) {
					// 设置用户为离线
					bean.setOffline(true);
					userMap.remove(id);
				}

			}
		}
		
		refreshGlobalEvent();
	}
	
	/**
	 * 返回钓鱼的数据world
	 * @return the world
	 */
	public static FishWorld getWorld() {
		return world;
	}
	
	
	/**
	 * 载入钓鱼的数据world
	 */
	void load() {
		fishList = service.getFishList(null);
		pullList = service.getPullList(null);	// 要求pull的id为1-n连续的
		areaList = service.getAreaList(null);
		globalEventList = service.getGlobalEventList(null);
		pullEventList = service.getPullEventList(null);
		
		// 写入数据倒map以备快速查找
		areaMap = new HashMap();
		Iterator iter = areaList.iterator();
		while(iter.hasNext()) {
			AreaBean bean = (AreaBean)iter.next();
			areaMap.put(new Integer(bean.getId()), bean);
			bean.fishList = new ArrayList();
			
			bean.pullEventList = service.getPullEventList("area_id=" + bean.getId());	// 每个场景单独的拉杆事件列表
		}
		
		pullMap = new HashMap();
		iter = pullList.iterator();
		while(iter.hasNext()) {
			PullBean bean = (PullBean)iter.next();
			pullMap.put(new Integer(bean.getId()), bean);
		}
		
		fishMap = new HashMap();
		iter = fishList.iterator();
		while(iter.hasNext()) {
			FishBean fish = (FishBean)iter.next();
			fishMap.put(new Integer(fish.getId()), fish);
			AreaBean area = getArea(fish.getAreaId());
			if(area != null)
				area.fishList.add(fish);
		}
		
		refreshGlobalEvent();
	}
	
	/**
	 * 返回鱼对应的拉杆方法
	 */
	public PullBean getPull(FishBean fish) {
		return (PullBean)pullMap.get(new Integer(fish.getPullId()));
	}

	/**
	 * 刷新global event，根据概率产生新的事件，现有事件如果过期则去除
	 */
	public void refreshGlobalEvent() {
//		 重新计算所有区域的概率
		Iterator iter = areaList.iterator();
		while(iter.hasNext()) {
			AreaBean bean = (AreaBean)iter.next();
			bean.resetCurRand();
		}
		
		if(currentGlobalEvent == null) {
			if(RandomUtil.percentRandom(30)) {
				currentGlobalEvent = (GlobalEventBean)RandomUtil.randomObject(globalEventList);
				currentGlobalEvent.reset();
				addLog(currentGlobalEvent.getBeginDesc());
			}
		} else {
			long cur = new Date().getTime();
			if(currentGlobalEvent.isOver(cur)) {	// 事件结束
				addLog(currentGlobalEvent.getEndDesc());
				currentGlobalEvent = null;
			}
		}
		
		if(currentGlobalEvent != null) {
			int areaId = currentGlobalEvent.getAreaId();
			if(areaId == 0) {	// 所有区域都发生变化的事件
				iter = areaList.iterator();
				while(iter.hasNext()) {
					AreaBean bean = (AreaBean)iter.next();
					bean.setCurRand(bean.getCurRand() + currentGlobalEvent.getRandChange());
				}
			} else {
				AreaBean bean = (AreaBean)areaMap.get(new Integer(areaId));
				bean.setCurRand(bean.getCurRand() + currentGlobalEvent.getRandChange());
			}
		}
	}

	public String getLogString() {
		return log.getLogString(5);
	}

	public void addLog(String content) {
		log.add(content);
	}
	
	/**
	 * 返回所有的区域事件
	 */
	public String getGlobalEventString() {
		if(currentGlobalEvent == null)
			return "欢乐渔场一切正常，大家正玩得开心。<br/>";
		else
			return currentGlobalEvent.getBeginDesc() + "<br/>";
	}

	public AreaBean getArea(int id) {
		return (AreaBean)areaMap.get(new Integer(id));
	}
	
	/**
	 * @return Returns the userMap.
	 */
	public HashMap getUserMap() {
		return userMap;
	}

	public FishWorld() {
		load();
	}
}
