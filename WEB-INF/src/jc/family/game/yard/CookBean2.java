package jc.family.game.yard;

/**
 * 菜的详细做法，步骤
 * @author maning
 */
public class CookBean2 {
	int id;
	int recipeId;	// 食谱ID
	String content;	// 描述
	int needTime;	// 需要的时间。这里用int型来存储毫秒
	int step;		// 步骤
	int materialId;	// 需要的物品ID
	int count;		// 需要的数量
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getNeedTime() {
		return needTime;
	}
	public void setNeedTime(int needTime) {
		this.needTime = needTime;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getMaterialId() {
		return materialId;
	}
	public void setMaterialId(int materialId) {
		this.materialId = materialId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
