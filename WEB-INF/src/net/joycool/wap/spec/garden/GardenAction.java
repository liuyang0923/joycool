package net.joycool.wap.spec.garden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.joycool.wap.bean.UserFriendBean;
import net.joycool.wap.cache.CacheManage;
import net.joycool.wap.cache.ICacheMap;
import net.joycool.wap.framework.CustomAction;
import net.joycool.wap.service.impl.UserServiceImpl;
import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SqlUtil;
import net.joycool.wap.util.UserInfoUtil;

public class GardenAction extends CustomAction {

	public static GardenService gardenService = GardenService.getInstance();
	public static UserServiceImpl userService = new UserServiceImpl();
	public GardenAction() {
	}

	public GardenAction(HttpServletRequest request) {
		super(request);
	}

	public GardenAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public static ICacheMap gardenUserCache = CacheManage.gardenUserCache;
	public static int FEED_EXP = 6;	//播种获得的经验
	public static int PICK_EXP = 3;	//收获获得的经验
	public static int DO_EXP = 2;	//种草，防虫加2点经验
	public static int EXPAND_EXP = 100;	//扩充土地100经验
	
	public static int ONE_DAY_EXP = 150;	//每天做多获得的经验
	
	//扩展土地
	public void expand(){
		int uid = this.getLoginUser().getId();
		GardenUserBean bean = GardenUtil.getUserBean(uid);
		
		if(bean == null) {
			tip("null");
			return;
		}
		
		if(bean.getFieldCount()>=GardenUtil.condition.length+GardenUtil.defaultCount) {
			tip("limit");
			return;
		}
		
		//钱是否足够等判断
		int index = bean.getFieldCount() - GardenUtil.defaultCount;
		
		if(bean.getGold() < GardenUtil.condition[index][0]||GardenUtil.getLevel(bean.getExp()) < GardenUtil.condition[index][1]) {
			tip("no");
			request.setAttribute("index", new Integer(index+1));
			return;
		}
		
		gardenService.addGardenField(uid);
		//加经验和田数量,还有减钱 GardenUtil.condition[index - 1][0]
		GardenUserBean userBean = GardenUtil.getUserBean(uid);
		int level1 = GardenUtil.getLevel(userBean.getExp());
		synchronized(userBean) {
			gardenService.updateGardenUser("field_count = field_count+1,exp = exp + " + EXPAND_EXP+", gold = gold - "+GardenUtil.condition[index][0], "uid = " + uid);
			userBean.setFieldCount(userBean.getFieldCount()+1);
			userBean.setExp(userBean.getExp()+EXPAND_EXP);
			userBean.setGold(userBean.getGold()-GardenUtil.condition[index][0]);
		}
		request.setAttribute("exp", new Integer(EXPAND_EXP));
		request.setAttribute("index", new Integer(index+GardenUtil.defaultCount+1));
		GardenUtil.addMsg(uid, "扩充了一块土地",0);
		int level2 = GardenUtil.getLevel(userBean.getExp());
		if(level2 - level1 == 1){
			request.setAttribute("level", new Integer(level2));
		}
		
		tip("success");
	}
	
	//施化肥
	public void grown(){
		int id = this.getParameterInt("id");
		int fid = this.getParameterInt("fid");
		int uid = this.getLoginUser().getId();
		
		GardenUserSeedBean userSeedBean = gardenService.getUserSeed(id);
		if(userSeedBean == null) {
			tip("null");	//不能种植
			return;
		}
		if(uid != userSeedBean.getUid()){
			tip("null");	//不能种植
			return;
		}
		if(userSeedBean.getCount() <= 0) {
			tip("lack");//缺少种子
			return;
		}
		
		GardenFieldBean field = gardenService.getUserFieldBean(fid);
		
		if(field == null) {
			tip("null");	//不能种植
			return;
		}
		
		if(field.getSeedId() == 0) {
			tip("null");	//不能种植
			return;
		}
		
		if(field.getUid() != uid) {
			tip("null");	//不能种植
			return;
		}
		
		if(field.isGrown()) {
			tip("null");	//不能施肥
			return;
		}
		
		if(!SqlUtil.isTest){
			if(field.getGrownState() == field.getState()) {
				tip("has");		//该阶段不需要施化肥
				return;
			}
		}
		
		
		
		GardenSeedBean bean = gardenService.getSeedBean(userSeedBean.getSeedId());
		if(bean == null){
			tip("null");	//不能施化肥
			return;
		}
		
		String condition = "result_start_time = result_start_time - "+(field.getCurStateTimeLeft()>bean.getValue()?bean.getValue():field.getCurStateTimeLeft()) + ",state=" + field.getGrownState() + " where id = "+fid;
		gardenService.updateField(condition);
		
		if(!SqlUtil.isTest){
			gardenService.updateUserSeedCount2(uid, userSeedBean.getSeedId(), userSeedBean.getCount() - 1,userSeedBean.getType());
		}
		
		request.setAttribute("time", new Integer(bean.getValue()));
		tip("success");
	}
	
	
	//种植作物
	public void feed(){
		int id = this.getParameterInt("id");
		int fid = this.getParameterInt("fid");
//		GardenSeedBean bean = gardenService.getSeedBean(id);
//		if(bean == null) {
//			tip("null");	//没有该作物
//			return;
//		}
		int uid = this.getLoginUser().getId();
		GardenUserSeedBean userSeedBean = gardenService.getUserSeed(id);
		if(userSeedBean == null) {
			tip("null");	//不能种植
			return;
		}
		if(uid != userSeedBean.getUid()){
			tip("null");	//不能种植
			return;
		}
		if(userSeedBean.getCount() <= 0) {
			tip("lack");//缺少种子
			return;
		}
		
		GardenFieldBean field = gardenService.getUserFieldBean(fid);
		
		if(field == null) {
			tip("null");	//不能种植
			return;
		}
		
		if(field.getSeedId() > 0) {
			tip("has");		//已经有作物
			return;
		}		
		
		if(field.getUid() != uid) {
			tip("null");	//不能种植
			return;
		}
		
		GardenSeedBean bean = gardenService.getSeedBean(userSeedBean.getSeedId());
		if(bean == null){
			tip("null");	//不能种植
			return;
		}
		
		GardenUserBean userBean = GardenUtil.getUserBean(uid);
//		if(GardenUtil.getLevel(userBean.getExp()) < bean.getLevel()) {
//			tip("nolevel");	//不能种植
//			request.setAttribute("level", new Integer(bean.getLevel()));
//			return;
//		}
		
		//int endTime = (int)(System.currentTimeMillis() / 1000) + GardenUtil.QUARTER_SEC;
		int startTime = (int)(System.currentTimeMillis()/ 1000);
		String condition = "seed_id = " + userSeedBean.getSeedId() + ", result_start_time = " + startTime + ", quarter = 1 where id = " + fid;
		
		gardenService.updateField(condition);
		gardenService.updateUserSeedCount2(uid, userSeedBean.getSeedId(), userSeedBean.getCount() - 1,userSeedBean.getType());	//种子数减1
		
		
//		synchronized(GardenUtil.badTimeUser){
//			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
//			if(l == null) {
//				l = new Long(System.currentTimeMillis());
//				GardenUtil.badTimeUser.put(new Integer(uid), l);
//			} else {
//				long cur = System.currentTimeMillis();
//				int interval = (int)((cur - l.longValue())/1000);
//				if(interval > GardenUtil.DAY_SEC) {
//					userBean.setGrassCount(GRASS_COUNT);
//					userBean.setBugCount(BUG_COUNT);
//					userBean.setTodayExp(ONE_DAY_EXP);
//					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
//				}
//			}
//		}
		
		//if(userBean.getTodayExp() > 0) {
		int level1 = GardenUtil.getLevel(userBean.getExp());
		if(SqlUtil.isTest){
			GardenUtil.updateExp(uid, 300);
		} else {
			GardenUtil.updateExp(uid, FEED_EXP);
		}
		request.setAttribute("exp", new Integer(FEED_EXP));
		int level2 = GardenUtil.getLevel(userBean.getExp());
		if(level2 - level1 == 1){
			request.setAttribute("level", new Integer(level2));
		}
		//userBean.setTodayExp(userBean.getTodayExp()-FEED_EXP);
		//}
		
		
		tip("success");
		
	}
	
	//购买种子
	public void buy(){
		int id = this.getParameterInt("id");
		int amount = this.getParameterInt("amount");
		
		if(amount <= 0){
			tip("nozero");
			return;
		}
		
		if(amount > 99) {
			tip("max");
			return;
		}
		
		GardenSeedBean bean = gardenService.getSeedBean(id);
		
		if(bean == null) {
			tip("null");	//没有该作物
			return;
		}
		
		int uid = this.getLoginUser().getId();
		
		GardenUserBean userBean = GardenUtil.getUserBean(uid);
		
		if(GardenUtil.getLevel(userBean.getExp()) < bean.getLevel()) {
			tip("nograde");	//等级不够，不能购买
			request.setAttribute("level", new Integer(bean.getLevel()));
			return;
		}
		
		if(userBean.getGold() >= bean.getPrice()*amount) {
			userBean.setGold(userBean.getGold() - bean.getPrice()*amount);
			
			gardenService.updateUserGold(uid, userBean.getGold());
			
			if(gardenService.isContainSeed(uid, id)){
				gardenService.updateUserSeedCount(uid, id, amount,bean.getType());
			} else {
				gardenService.addGardenUserSeed(uid, id, amount,bean.getType());
			}
			request.setAttribute("name", bean.getName());
			request.setAttribute("amount", new Integer(amount));
			tip("success");	//购买成功
		} else {
			tip("lack");	//金钱不够
			return;
		}
	}
	
	//采摘
	public void pick(){
		int id = this.getParameterInt("id");
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field == null) {
			tip("null");	//	不能采摘
			return;
		}
		int uid = this.getLoginUser().getId();
		if(uid != field.getUid()) {
			tip("null");	//	不能采摘
			return;
		}
		
		if(!field.isGrown()){
			tip("notime");	//还未成熟
			return;
		}
		GardenSeedBean seedBean = gardenService.getSeedBean(field.getSeedId());
		if(seedBean == null) {
			tip("null");
			return;
		}
		int count = seedBean.getCount()-field.getBug()-field.getStealCount()-field.getGrass();
		gardenService.updateUserStoreCount(uid, seedBean.getId(), count, true);
		gardenService.updateUserHistoryStoreCount(uid, seedBean.getId(), count, true);
		
		if(seedBean.getQuarter() > field.getQuarter()) {
			int startTime = (int)(System.currentTimeMillis() / 1000);
			List list = seedBean.getGrownList();
			for(int i = 0; i < 3; i ++){
				Integer t = (Integer)list.get(i);
				startTime -= t.intValue() * 3600;
			}
			String condition = " state=0,bug = 0, grass = 0, quarter = " + (field.getQuarter() + 1) + ", result_start_time = " + startTime + ", steal_count = 0 where id = " + field.getId();
			gardenService.updateField(condition);
			
		} else {			
			String condition = "state=0,seed_id = 0, bug = 0, grass = 0, quarter = 0, result_start_time = 0, steal_count = 0 where id = " + field.getId();
			gardenService.updateField(condition);
		}

		synchronized(GardenUtil.fieldStrealUser) {
			GardenUtil.fieldStrealUser.rm(new Integer(id));
		}
		GardenUserBean userBean = GardenUtil.getUserBean(uid);
		
//		synchronized(GardenUtil.badTimeUser){
//			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
//			if(l == null) {
//				l = new Long(System.currentTimeMillis());
//				GardenUtil.badTimeUser.put(new Integer(uid), l);
//			} else {
//				long cur = System.currentTimeMillis();
//				int interval = (int)((cur - l.longValue())/1000);
//				if(interval > GardenUtil.DAY_SEC) {
//					userBean.setGrassCount(GRASS_COUNT);
//					userBean.setBugCount(BUG_COUNT);
//					userBean.setTodayExp(ONE_DAY_EXP);
//					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
//				}
//			}
//		}
		
		//if(userBean.getTodayExp() > 0) {
		int level1 = GardenUtil.getLevel(userBean.getExp());
		GardenUtil.updateExp(uid, PICK_EXP+seedBean.getExp());
		request.setAttribute("exp", new Integer(PICK_EXP+seedBean.getExp()));
		int level2 = GardenUtil.getLevel(userBean.getExp());
		if(level2 - level1 == 1){
			request.setAttribute("level", new Integer(level2));
		}
		
		tip("success");
		request.setAttribute("count", new Integer(count));
		request.setAttribute("name", seedBean.getName());
	}
	
	//出售收获的物品
	public void sell(){
		GardenUserBean userBean = GardenUtil.getUserBean(getLoginUser().getId());
		if(this.hasParam("id")) {
			int id = this.getParameterInt("id");
			int uid = this.getLoginUser().getId();
			GardenStoreBean bean = gardenService.getStore(" id = " + id);
			if(bean == null) {
				tip("null");
				return;
			}
			if(bean.getUid() != uid) {
				tip("null");
				return;
			}
			GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
			if(seedBean == null) {
				tip("null");
				return;
			}
			int price = bean.getCount() * seedBean.getValue();
			userBean.addGold(price);
			gardenService.updateUserGold(uid, userBean.getGold());
			
			gardenService.deleteStore(" id = " + id);
			request.setAttribute("price", new Integer(price));
		} else {
			int uid = this.getLoginUser().getId();
			List list = GardenAction.gardenService.getStoreList(" uid = " + uid);
			int price = 0;
			for(int i=0;i<list.size();i++){
				GardenStoreBean bean = (GardenStoreBean)list.get(i);
				GardenSeedBean seedBean = GardenAction.gardenService.getSeedBean(bean.getSeedId());
				price += bean.getCount() * seedBean.getValue();
			}
			if(price > 0) {
				userBean.addGold(price);
				gardenService.updateUserGold(uid, userBean.getGold());
				
				gardenService.deleteStore(" uid = " + uid);
			}
			request.setAttribute("price", new Integer(price));
		}
		tip("success");
	}
	
	public static int GRASS_COUNT = 20;
	public static int FIELD_GRASS = 3;
	public static int BUG_COUNT = 20;
	public static int FIELD_BUG = 3;
	
	public static int STEAL_MAX_COUNT = 15;
	
	//放草
	public void doGrass(){
		int uid = this.getLoginUser().getId();
		int id = this.getParameterInt("id");
		
		GardenUserBean bean = GardenUtil.getUserBean(uid);
		
		int friendUid = this.getParameterInt("uid");
		List userFriends = UserInfoUtil.getUserFriends(getLoginUser().getId());
		boolean flag = userFriends.contains(friendUid + "");
		
		if(!flag) {
			tip("nofriend");	//不是自己的好友
			return;
		}
		synchronized(GardenUtil.badTimeUser){
			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
			if(l == null) {
				l = new Long(System.currentTimeMillis());
				GardenUtil.badTimeUser.put(new Integer(uid), l);
			} else {
				long cur = System.currentTimeMillis();
				int interval = (int)((cur - l.longValue())/1000);
				if(interval > GardenUtil.DAY_SEC) {
					bean.setGrassCount(GRASS_COUNT);
					bean.setBugCount(BUG_COUNT);
					bean.setTodayExp(ONE_DAY_EXP);
					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
				}
			}
		}
		if(bean.getGrassCount() <= 0) {
			tip("no");//超过一天最大限制
			return;
		}
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field == null) {
			tip("null");
			return;
		}
		
		if(field.isGrown()) {
			tip("grown");
			return;
		}
		
		if(field.getGrass() >= FIELD_GRASS) {
			tip("full");	//该地不能再放草了
			return;
		}
		if(uid == field.getUid()) {
			tip("self");	//不能给自己放草
			return;
		}
		
		gardenService.updateField("grass = grass + 1 where id = "+id);
//		GardenUserBean userBean = GardenUtil.getUserBean(uid);
//		int level1 = GardenUtil.getLevel(userBean.getExp());
//		gardenService.updateGardenUser("grass_count = grass_count+1, exp = exp+"+DO_EXP, "uid = "+uid);
//		int level2 = GardenUtil.getLevel(userBean.getExp());
//		if(level2 - level1 == 1){
//			request.setAttribute("level", new Integer(level2));
//		}
//		gardenUserCache.srm(uid);
		synchronized(GardenUtil.fieldGrassUser) {
			HashSet set = (HashSet)GardenUtil.fieldGrassUser.get(new Integer(id));
			if(set == null) {
				set = new HashSet();
				GardenUtil.fieldGrassUser.put(new Integer(id), set);
			}
			set.add(new Integer(uid));
		}
		bean.setGrassCount(bean.getGrassCount()-1);
		GardenUtil.addMsg(friendUid, "给你的农场放了草",uid);
		
		GardenUserBean friendBean = GardenUtil.getUserBean(friendUid);
		friendBean.setMsgCount(friendBean.getMsgCount()+1);
		
		tip("success");
	}
	
	//放虫
	public void doBug(){
		int uid = this.getLoginUser().getId();
		int id = this.getParameterInt("id");
		
		GardenUserBean bean = GardenUtil.getUserBean(uid);
		int friendUid = this.getParameterInt("uid");
		List userFriends = UserInfoUtil.getUserFriends(getLoginUser().getId());
		boolean flag = userFriends.contains(friendUid + "");
		
		if(!flag) {
			tip("nofriend");	//不是自己的好友
			return;
		}
		synchronized(GardenUtil.badTimeUser){
			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
			if(l == null) {
				l = new Long(System.currentTimeMillis());
				GardenUtil.badTimeUser.put(new Integer(uid), l);
			} else {
				long cur = System.currentTimeMillis();
				int interval = (int)((cur - l.longValue())/1000);
				if(interval > GardenUtil.DAY_SEC) {
					bean.setGrassCount(GRASS_COUNT);
					bean.setBugCount(BUG_COUNT);
					bean.setTodayExp(ONE_DAY_EXP);
					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
				}
			}
		}
		
		if(bean.getBugCount() <= 0) {
			tip("no");//超过一天最大限制
			return;
		}
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field == null) {
			tip("null");
			return;
		}
		if(field.isGrown()) {
			tip("grown");
			return;
		}
		if(field.getBug() >= FIELD_BUG) {
			tip("full");	//该地不能再放虫了
			return;
		}
		if(uid == field.getUid()) {
			tip("self");	//不能给自己放虫
			return;
		}
		gardenService.updateField("bug = bug + 1 where id = "+id);
//		GardenUserBean userBean = GardenUtil.getUserBean(uid);
//		int level1 = GardenUtil.getLevel(userBean.getExp());
//		gardenService.updateGardenUser("bug_count = bug_count+1, exp = exp+"+DO_EXP, "uid = "+uid);
//		int level2 = GardenUtil.getLevel(userBean.getExp());
//		if(level2 - level1 == 1){
//			request.setAttribute("level", new Integer(level2));
//		}
//		gardenUserCache.srm(uid);
		synchronized(GardenUtil.fieldBugUser) {
			HashSet set = (HashSet)GardenUtil.fieldBugUser.get(new Integer(id));
			if(set == null) {
				set = new HashSet();
				GardenUtil.fieldBugUser.put(new Integer(id), set);
			}
			set.add(new Integer(uid));
		}
		
		bean.setBugCount(bean.getBugCount()-1);
		GardenUtil.addMsg(friendUid, "给你的农场放了虫",uid);
		
		
		GardenUserBean friendBean = GardenUtil.getUserBean(friendUid);
		friendBean.setMsgCount(friendBean.getMsgCount()+1);
		
		tip("success");
	}
	
	//除草
	public void deGrass(){
		
		int uid = this.getParameterInt("uid");
		if(uid == 0)
			uid = this.getLoginUser().getId();
		int id = this.getParameterInt("id");
		
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field==null){
			tip("null");
			return;
		}
		if(uid != field.getUid()) {
			tip("no");
			return;
		}
		
		if(field.getGrass()==0){
			tip("nograss");
			return;
		}
		
		if(uid != this.getLoginUser().getId()){
			synchronized(GardenUtil.fieldGrassUser) {
				HashSet set = (HashSet)GardenUtil.fieldGrassUser.get(new Integer(id));
				if(set == null) {
					set = new HashSet();
					GardenUtil.fieldGrassUser.put(new Integer(id), set);
				} else if(set.contains(new Integer(this.getLoginUser().getId()))){
					tip("has");	//已经摘取过了
					return;
				}
			}
		}
		gardenService.updateField("grass = 0 where id = "+id);
		//GardenUtil.updateExp(uid, DE_EXP*field.getGrass());
		
		GardenUserBean userBean = GardenUtil.getUserBean(this.getLoginUser().getId());
		
		synchronized(GardenUtil.badTimeUser){
			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
			if(l == null) {
				l = new Long(System.currentTimeMillis());
				GardenUtil.badTimeUser.put(new Integer(uid), l);
			} else {
				long cur = System.currentTimeMillis();
				int interval = (int)((cur - l.longValue())/1000);
				if(interval > GardenUtil.DAY_SEC) {
					userBean.setGrassCount(GRASS_COUNT);
					userBean.setBugCount(BUG_COUNT);
					userBean.setTodayExp(ONE_DAY_EXP);
					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
				}
			}
		}
		
		if(userBean.getTodayExp() > 0) {
			int level1 = GardenUtil.getLevel(userBean.getExp());
			gardenService.updateGardenUser("exp = exp+"+DO_EXP*field.getGrass(), "uid = "+this.getLoginUser().getId());
			//userBean.setGrassCount(userBean.getGrassCount()+1);
			request.setAttribute("count", new Integer(field.getGrass()));
			userBean.setExp(userBean.getExp()+DO_EXP*field.getGrass());
			request.setAttribute("exp", new Integer(DO_EXP*field.getGrass()));
			int level2 = GardenUtil.getLevel(userBean.getExp());
			if(level2 - level1 == 1){
				request.setAttribute("level", new Integer(level2));
			}
			userBean.setTodayExp(userBean.getTodayExp()-DO_EXP*field.getGrass());
		}
		
		synchronized(GardenUtil.fieldGrassUser) {
			GardenUtil.fieldGrassUser.rm(new Integer(id));
		}
		if(uid == this.getLoginUser().getId()) {
			GardenUtil.addMsg(uid, "清理了农场里的杂草",0);
		} else {
			GardenUtil.addMsg(uid, "清理了农场里的杂草",this.getLoginUser().getId());
		}
		
		
		GardenUserBean friendBean = GardenUtil.getUserBean(uid);
		friendBean.setMsgCount(friendBean.getMsgCount()+1);
		
		tip("success");
	}
	//杀虫
	public void debug(){
		int uid = this.getParameterInt("uid");
		if(uid == 0)
			uid = this.getLoginUser().getId();
		int id = this.getParameterInt("id");
		
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field==null){
			tip("null");
			return;
		}
		if(uid != field.getUid()) {
			tip("no");
			return;
		}
		if(field.getBug()==0){
			tip("nobug");
			return;
		}
		if(uid != this.getLoginUser().getId()){
			synchronized(GardenUtil.fieldBugUser) {
				HashSet set = (HashSet)GardenUtil.fieldBugUser.get(new Integer(id));
				if(set == null) {
					set = new HashSet();
					GardenUtil.fieldBugUser.put(new Integer(id), set);
				} else if(set.contains(new Integer(this.getLoginUser().getId()))){
					tip("has");	//已经摘取过了
					return;
				}
			}
		}
		
		gardenService.updateField("bug = 0 where id = "+id);
		//GardenUtil.updateExp(uid, DE_EXP*field.getBug());
		
		GardenUserBean userBean = GardenUtil.getUserBean(this.getLoginUser().getId());
		
		
		synchronized(GardenUtil.badTimeUser){
			Long l = (Long)GardenUtil.badTimeUser.get(new Integer(uid));
			if(l == null) {
				l = new Long(System.currentTimeMillis());
				GardenUtil.badTimeUser.put(new Integer(uid), l);
			} else {
				long cur = System.currentTimeMillis();
				int interval = (int)((cur - l.longValue())/1000);
				if(interval > GardenUtil.DAY_SEC) {
					userBean.setGrassCount(GRASS_COUNT);
					userBean.setBugCount(BUG_COUNT);
					userBean.setTodayExp(ONE_DAY_EXP);
					GardenUtil.badTimeUser.put(new Integer(uid), new Long(cur));
				}
			}
		}
		
		if(userBean.getTodayExp() > 0) {
			int level1 = GardenUtil.getLevel(userBean.getExp());
			gardenService.updateGardenUser("exp = exp+"+DO_EXP*field.getBug(), "uid = "+this.getLoginUser().getId());
			request.setAttribute("count", new Integer(field.getBug()));
			//userBean.setBugCount(userBean.getBugCount()+1);
			userBean.setExp(userBean.getExp()+DO_EXP*field.getBug());
			request.setAttribute("exp", new Integer(DO_EXP*field.getBug()));
			int level2 = GardenUtil.getLevel(userBean.getExp());
			if(level2 - level1 == 1){
				request.setAttribute("level", new Integer(level2));
			}
			userBean.setTodayExp(userBean.getTodayExp()-DO_EXP*field.getBug());
		}
		
		synchronized(GardenUtil.fieldBugUser) {
			GardenUtil.fieldBugUser.rm(new Integer(id));
		}	
		
		if(uid == this.getLoginUser().getId()) {
			GardenUtil.addMsg(uid, "清理了农场里的虫子",0);
		} else {
			GardenUtil.addMsg(uid, "清理了农场里的虫子",this.getLoginUser().getId());
		}
		
		GardenUserBean friendBean = GardenUtil.getUserBean(uid);
		friendBean.setMsgCount(friendBean.getMsgCount()+1);
		
		tip("success");
	}
	
	public void steal(){
		int uid = getLoginUser().getId();
		int id = this.getParameterInt("id");
		int friendUid = this.getParameterInt("uid");
		List userFriends = UserInfoUtil.getUserFriends(getLoginUser().getId());
		boolean flag = userFriends.contains(friendUid + "");
		
		if(!flag) {
			tip("nofriend");	//不是自己的好友
			return;
		}
		
		GardenFieldBean field = gardenService.getUserFieldBean(id);
		if(field == null) {
			tip("null");
			return;
		}
		if(!field.isGrown()){
			tip("null");
			return;
		}
		if(uid == field.getUid()) {
			tip("self");	//不能摘自己的
			return;
		}
		if(field.getStealCount() >= STEAL_MAX_COUNT) {
			tip("out");	//被摘取达到最大数量了
			return;
		}
		
		synchronized(GardenUtil.fieldStrealUser) {
			HashSet set = (HashSet)GardenUtil.fieldStrealUser.get(new Integer(id));
			if(set == null) {
				set = new HashSet();
				GardenUtil.fieldStrealUser.put(new Integer(id), set);
			} else if(set.contains(new Integer(uid))){
				tip("has");	//已经摘取过了
				return;
			}
		}
			
		GardenSeedBean seedBean = gardenService.getSeedBean(field.getSeedId());
		if(seedBean == null) {
			tip("null");
		}
		int count = 0;
		int random = RandomUtil.nextIntNoZero(10);
		if(random < 6) {
			count = 1;
		} else if(random >=6 && random < 9) {
			count = 2;
		} else if(random == 9) {
			count = 3;
		} else if(random == 10){
			count = 5;
		} else {
			count = 1;
		}
		
		gardenService.updateField(" steal_count = steal_count + " + count+" where id = " + id);
		gardenService.updateUserStoreCount(uid, seedBean.getId(), count, true);
		gardenService.updateUserHistoryStoreCount(uid, seedBean.getId(), count, true);
		
		synchronized(GardenUtil.fieldStrealUser) {
			HashSet set = (HashSet)GardenUtil.fieldStrealUser.get(new Integer(id));
			set.add(new Integer(uid));
		}
		
		GardenUtil.addMsg(friendUid, "来你的农场摘取了"+count+"个"+seedBean.getName(),uid);
		
		GardenUserBean friendBean = GardenUtil.getUserBean(friendUid);
		friendBean.setMsgCount(friendBean.getMsgCount()+1);
		
		request.setAttribute("count", new Integer(count));
		request.setAttribute("name", seedBean.getName());
		tip("success");
	}
	
	public static String interval(int sec) {
		int sec2 = sec;
		sec2 /= 60;
		if(sec < 60) {
			if(sec < 0)
				return "0秒";
			else
				return sec + "秒";
		}
		sec /= 60;
		if(sec < 60)
			return sec + "分钟";
		sec /= 60;
		int min = sec2 % 60;
		if(min > 30)
			sec++;
		return sec + "小时";
	}
	
	public static void main(String[] args) {
		long now = System.currentTimeMillis();
		
		System.out.println(now);
		System.out.println(now / 1000);
		int i = (int)(now/1000);
		
		System.out.println(i);
	}
	
}
