package net.joycool.wap.spec.garden;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.joycool.wap.util.*;

public class GardenFieldBean {

	int id;
	int seedId;
	int resultStartTime;	//单位为秒
	int bug;			//是否被人放虫
	int grass;			//是否被人放草
	int stealCount;		//被偷取的数量
	int uid;
	String stealUserList;	//偷取该田地的用户列表
	int quarter;		//所属第几季
	int state;		//表示某个阶段是否施肥过了
	
	

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public GardenFieldBean() {
	}
	
	public GardenFieldBean(int uid) {
		this.seedId = 0;
		this.resultStartTime = 0;
		this.bug = 0;
		this.grass = 0;
		this.stealCount = 0;
		this.uid = uid;
		this.stealUserList = "";
		this.quarter = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSeedId() {
		return seedId;
	}
	public void setSeedId(int seedId) {
		this.seedId = seedId;
	}
	public int getResultStartTime() {
		return resultStartTime;
	}

	public void setResultStartTime(int resultStartTime) {
		this.resultStartTime = resultStartTime;
	}

	public int getBug() {
		return bug;
	}
	public void setBug(int bug) {
		this.bug = bug;
	}
	public int getGrass() {
		return grass;
	}
	public void setGrass(int grass) {
		this.grass = grass;
	}
	public int getStealCount() {
		return stealCount;
	}
	public void setStealCount(int stealCount) {
		this.stealCount = stealCount;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getStealUserList() {
		return stealUserList;
	}
	public void setStealUserList(String stealUserList) {
		this.stealUserList = stealUserList;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	
	public boolean isGrown(){	//该作物是否成熟了
		GardenSeedBean bean = GardenAction.gardenService.getSeedBean(this.getSeedId());
		if(bean==null)
			return false;
		boolean flag = this.resultStartTime + bean.getQuarterTime(1)*3600 <= GardenUtil.getNowSec();
		return flag;
	}
	
	public int getGrownState(){
		GardenSeedBean bean = GardenAction.gardenService.getSeedBean(this.getSeedId());
		return bean.getGrownState(GardenUtil.getNowSec() - this.resultStartTime);
	}
	
	public int getNextGrownState(){
		GardenSeedBean bean = GardenAction.gardenService.getSeedBean(this.getSeedId());
		int s = getGrownState();
		List list = bean.getGrownList();
		int next = s;
		for(int i = s;i<list.size();i++){
			Integer ii = (Integer)list.get(i);
			if(ii.intValue() == 0) {
				next++;
			} else {
				break;
			}
		}
		return next+1;
	}
	
	public String getGrownStateStr(){
		return GardenSeedBean.getGrownStr(getGrownState(),this.getSeedId());
	}
	
	public int getTimeLeft(){
		GardenSeedBean bean = GardenAction.gardenService.getSeedBean(this.getSeedId());
		return bean.getQuarterTime(this.quarter)*3600 - GardenUtil.getNowSec() + this.resultStartTime;
	}
	
	public int getCurStateTimeLeft(){
		GardenSeedBean bean = GardenAction.gardenService.getSeedBean(this.getSeedId());
		int s = getGrownState();
		
		List list = bean.getGrownList();
		int ii = 0;
		//if(this.quarter == 1) {
			for(int i = 0;i<s-1;i++){
				Integer h = (Integer)list.get(i);
				ii += h.intValue();
			}
//		} else {
//			for(int i = 3;i<s-1;i++){
//				Integer h = (Integer)list.get(i);
//				ii += h.intValue();
//			}
//		}
//		
		
		int cur = 0;
		while(cur==0&&s<=list.size()){
			cur = ((Integer)list.get(s-1)).intValue();
			s++;
		}
		int left = cur * 3600 - (GardenUtil.getNowSec() - this.getResultStartTime() - ii * 3600);
		return left;
	}
	
	public boolean containStealUser(int uid){
		synchronized(GardenUtil.fieldStrealUser) {
			HashSet set = (HashSet)GardenUtil.fieldStrealUser.get(new Integer(this.id));
			if(set == null) {
				return false;
			} else if(set.contains(new Integer(uid))){
				return true;
			} else {
				return false;
			}
		}
	}
	
	public int getStealUserCount() {
		return 0;
	}
}
