package jc.family.game.yard;
/**
 * 家族土地
 * @author maning
 */
public class YardLandBean {
	int id;
	int fmid;
	long plantTime;	
	int count;		// 地中当前种子的总数
	int rank;		// 这块地的等级(rank不要大于127)
	int userId;		// 谁种的...
	int seedId;		// 种植的物品ID
	int type;		// ......
	
	// 不同等级的土地上限
	public static int[] maxCount = { 50, 100, 150, 200, 250, 300, 350, 400, 450, 500 };
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFmid() {
		return fmid;
	}
	public void setFmid(int fmid) {
		this.fmid = fmid;
	}
	public long getPlantTime() {
		return plantTime;
	}
	public void setPlantTime(long plantTime) {
		this.plantTime = plantTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMaxCount() {
		return maxCount[rank];
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSeedId() {
		return seedId;
	}
	public void setSeedId(int seedId) {
		this.seedId = seedId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * 取得正在种植在这块地上的item
	 * @return
	 */
	public YardItemProtoBean getItemOnLand(){
		return YardAction.getItmeProto(seedId);
	}
	
	/**
	 * 是否已经可以收获
	 * @return
	 */
	public boolean isOk(){
		// 当前时间 - 种植时间 > 种植需要的时间
		return System.currentTimeMillis() - plantTime > YardAction.PLANT_TIME;
	}
	public int getTimeLeft(long now) {
		return (int)((YardAction.PLANT_TIME + plantTime - now) / 1000);
	}
}