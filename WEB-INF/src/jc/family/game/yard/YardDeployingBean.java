package jc.family.game.yard;

/**
 * 正在烹饪的菜谱
 * @author maning
 *
 */
public class YardDeployingBean {
	int id;
	int fmid;
	int uid;
	int deployId;	// 正在使用的配菜ID
	int recipeId;	// 正在制作的菜谱ID
	long createTime;// 这道菜开始制作的时间
	
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
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getDeployId() {
		return deployId;
	}
	public void setDeployId(int deployId) {
		this.deployId = deployId;
	}
	public int getRecipeId() {
		return recipeId;
	}
	public void setRecipeId(int recipeId) {
		this.recipeId = recipeId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
