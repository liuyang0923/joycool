package net.joycool.wap.spec.castle;

import java.util.Date;

import net.joycool.wap.util.DateUtil;
import net.joycool.wap.util.StringUtil;

public class OasisBean {
	public static int maxRes = 400;
	
	int id;
	private int uid;
	private int x;			//坐标x
	private int y;			//坐标y
	long createTime;
	long updateTime;		// 怪物最近一次更新时间
	int race;		// 种族
	int type;		// 类型，1 普通444地形，之后分别是345, 453, 534，5表示9田，6表示15田， 7 8 9分别是三种资源绿洲，10 11 12分别是三种资源加粮食绿洲，13 14分别是粮食和大粮食绿洲
	int cid;		// 所属城堡
	
	long time;
	int wood;
	int woodSpeed;		//木头的增加速度
	int fe;				//铁的增加速度
	int feSpeed;
	int grain;			//粮食增长速度
	int grainSpeed;
	int stone;
	int stoneSpeed;		//石头增长速度
	
	long robTime;		// 占领的绿洲最近一次抢夺时间
	
	public long getRobTime() {
		return robTime;
	}
	public void setRobTime(long robTime) {
		this.robTime = robTime;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		int[] speed = ResNeed.oasisResSpeed[type];
		woodSpeed = speed[0];
		stoneSpeed = speed[1];
		feSpeed = speed[2];
		grainSpeed = speed[3];
	}
	public String getRaceName() {
		return ResNeed.raceNames[race];
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	
	public OasisBean(){
		
	}
	
	public double calcDistance(int x2, int y2) {
		return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		if(id >= 0) {
			x = CastleUtil.pos2X(id);
			y = CastleUtil.pos2Y(id);
		}
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public int getFe() {
		return fe;
	}
	public void setFe(int fe) {
		this.fe = fe;
	}
	public int getFeSpeed() {
		return feSpeed;
	}
	public void setFeSpeed(int feSpeed) {
		this.feSpeed = feSpeed;
	}
	public int getGrain() {
		return grain;
	}
	public void setGrain(int grain) {
		this.grain = grain;
	}
	public int getGrainSpeed() {
		return grainSpeed;
	}
	public void setGrainSpeed(int grainSpeed) {
		this.grainSpeed = grainSpeed;
	}
	public int getStone() {
		return stone;
	}
	public void setStone(int stone) {
		this.stone = stone;
	}
	public int getStoneSpeed() {
		return stoneSpeed;
	}
	public void setStoneSpeed(int stoneSpeed) {
		this.stoneSpeed = stoneSpeed;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getWood() {
		return wood;
	}
	public void setWood(int wood) {
		this.wood = wood;
	}
	public int getWoodSpeed() {
		return woodSpeed;
	}
	public void setWoodSpeed(int woodSpeed) {
		this.woodSpeed = woodSpeed;
	}
	public void reCalc(long now) {
		setWood(getWood(now));
		setFe(getFe(now));
		setGrain(getGrain(now));
		setStone(getStone(now));
		setTime(now);
	}
	
	public int getWood(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int w = this.wood + (int)(woodSpeed * hours) ;
		if(w > maxRes)
			return maxRes;
		return w;
	}
	public int getFe(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int w = this.fe + (int)(feSpeed * hours) ;
		if(w > maxRes)
			return maxRes;
		return w;
	}
	public int getStone(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int w = this.stone + (int)(stoneSpeed * hours) ;
		if(w > maxRes)
			return maxRes;
		return w;
	}
	public int getGrain(long now) {
		float hours = (float)((now - this.time)) / DateUtil.MS_IN_HOUR;
		int w = this.grain + (int)(grainSpeed * hours) ;
		if(w > maxRes)
			return maxRes;
		return w;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
}
