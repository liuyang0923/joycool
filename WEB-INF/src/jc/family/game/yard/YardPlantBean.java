package jc.family.game.yard;
/**
 * 家族工厂车间
 * @author maning
 */
public class YardPlantBean {
	int id;
	int fmid;
	long plantTime;	
	int count;		// 地中当前种子的总数
	int rank;		// 这间车间的等级(rank不要大于127)
	int userId;		// 谁种的...
	int recipeId;	// 可使用的菜谱
	int type;		// ......
	int plantNow;	// 现在正在生产什么?
	
	// 不同等级的车间上限
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
	public int getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public int getPlantNow() {
		return plantNow;
	}
	public void setPlantNow(int plantNow) {
		this.plantNow = plantNow;
	}
	
	/**
	 * 取得车间中正在生产的菜谱
	 * @return
	 */
	public YardRecipeProtoBean getItemOnPlant(){
		return YardAction.getRecipeProto(recipeId);
	}
	
	/**
	 * 是否已经可以收获
	 * @return
	 */
	public boolean isOk(YardRecipeProtoBean recipe){
		if (recipe == null)
			return false;
		// 当前时间 - 种植时间 > 种植需要的时间
		return System.currentTimeMillis() - plantTime > recipe.getTime();
	}
	
	public int getTimeLeft(long now) {
		return (int)((YardAction.PLANT_TIME + plantTime - now) / 1000);
	}
}