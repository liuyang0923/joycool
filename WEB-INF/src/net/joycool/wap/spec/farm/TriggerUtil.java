package net.joycool.wap.spec.farm;

import java.text.DecimalFormat;
import java.util.*;

import net.joycool.wap.spec.farm.bean.TriggerBean;

/**
 * @author zhouj
 * @explain： trigger util工具类
 * @datetime:1007-10-24
 */
public class TriggerUtil {
	public static class TriggerMap { 
		HashMap map = new HashMap();
		
		public synchronized void addTrigger(int id, TriggerBean t) {
			Integer iid = new Integer(id);
			List list = getTrigger(iid);
			if(list == null) {
				list = new ArrayList(1);
				map.put(iid, list);
			}
			list.add(t);
		}
		public synchronized void deleteTrigger(int id, TriggerBean t) {
			List list = getTrigger(id);
			Iterator iter = list.iterator();
			while(iter.hasNext()) {
				TriggerBean t2 = (TriggerBean)iter.next();
				if(t2.getId() == t.getId())
					iter.remove();	
			}
		}
		public List getTrigger(int id) {
			return (List)map.get(Integer.valueOf(id));
		}
		public List getTrigger(Integer iid) {
			return (List)map.get(iid);
		}
		public void trigger(int id, FarmUserBean farmUser) {
			List list = getTrigger(id);
			if(list == null)
				return;
			for(int i = 0;i < list.size();i++) {
				TriggerBean t = (TriggerBean)list.get(i);
				if( (t.isFlagRedo() || !farmUser.isTriggered(t.getId()))
						&& !t.isFlagClosed()
						&& FarmWorld.isCondition(t.getConditionList(), farmUser)) {
					farmUser.getCurTrigger().addLast(t);
					FarmWorld.doAction(t.getActionList(), farmUser);
					FarmWorld.addUserTrigger(farmUser, t);
				}
			}
		}
	}
	
	public static TriggerMap talkTrigger = new TriggerMap();
	public static TriggerMap killTrigger = new TriggerMap();
	public static TriggerMap questTrigger = new TriggerMap();		// 完成任务
	public static TriggerMap posTrigger = new TriggerMap();		// 移动到某场景
	public static TriggerMap npcTrigger = new TriggerMap();		// 点某个npc触发
	public static TriggerMap itemTrigger = new TriggerMap();		// 获得某个物品触发
	
	public static TriggerMap[] triggerMapIndex = {null, questTrigger, killTrigger, posTrigger, talkTrigger, npcTrigger, itemTrigger};
	
	public static TriggerMap getTriggerMap(int index) {
		return triggerMapIndex[index];
	}
}
