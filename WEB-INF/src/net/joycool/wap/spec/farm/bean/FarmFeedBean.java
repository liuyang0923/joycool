package net.joycool.wap.spec.farm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.joycool.wap.spec.farm.FarmWorld;
import net.joycool.wap.util.RandomUtil;
import net.joycool.wap.util.SimpleGameLog;
import net.joycool.wap.util.StringUtil;

/**
 * @author zhouj
 * @explain： 畜牧业
 * @datetime:1007-10-24
 */
public class FarmFeedBean {
	int id;
	int userId;
	int cropId;			// 0 表示没有种植
	long startTime;		// 开始种植的时间
	long lastActTime;	// 上一次灌溉的时间
	int actCount = 0;		// 总的灌溉次数（成功的并且在种植时间内的）
	FarmCropBean crop = null;		// null表示没有种植
	
	static FarmWorld world = FarmWorld.one;
	
	// 生产了多少个
	public int getProductCount() {
		return actCount / crop.getProductCount();
	}
	
	// 收割剩余时间
	public int harvestTimeLeft(long now) {
		return (int)((startTime - now) / 1000 + crop.getGrowTime());
	}
	
	// 灌溉剩余时间
	public int actTimeLeft(long now) {
		return (int)((lastActTime - now) / 1000 + crop.getActInterval());
	}
	
	// 可以灌溉
	public boolean canAct(long now) {
		return lastActTime + crop.getActInterval() * 1000 <= now;
	}
	
	// 可以收割
	public boolean canHarvest(long now) {
		return startTime + crop.getGrowTime() * 1000 <= now;
	}
	
	// 灌溉，灌溉前需要自行检查是否可以灌溉
	public void act(long now) {
		actCount++;
		lastActTime = now;
	}
	public void actFail(long now) {
		lastActTime = now;
	}
	
	
	/**
	 * @return Returns the cropId.
	 */
	public int getCropId() {
		return cropId;
	}
	/**
	 * @param cropId The cropId to set.
	 */
	public void setCropId(int cropId) {
		this.cropId = cropId;
		if(cropId == 0)
			crop = null;
		else
			crop = world.getCrop(cropId);
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return Returns the userId.
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return Returns the actCount.
	 */
	public int getActCount() {
		return actCount;
	}
	/**
	 * @param actCount The actCount to set.
	 */
	public void setActCount(int actCount) {
		this.actCount = actCount;
	}
	/**
	 * @return Returns the lastActTime.
	 */
	public long getLastActTime() {
		return lastActTime;
	}
	/**
	 * @param lastActTime The lastActTime to set.
	 */
	public void setLastActTime(long lastActTime) {
		this.lastActTime = lastActTime;
	}

	/**
	 * @return Returns the crop.
	 */
	public FarmCropBean getCrop() {
		return crop;
	}

	/**
	 * @param crop The crop to set.
	 */
	public void setCrop(FarmCropBean crop) {
		this.crop = crop;
	}
}
