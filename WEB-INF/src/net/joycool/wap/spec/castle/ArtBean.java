package net.joycool.wap.spec.castle;

//宝物的信息
public class ArtBean {
	public static String[] typeNames = {"未知", "建筑图纸", "稳固建筑", "快速军队", "强化侦察", "节省粮食", "快速训练", "高级储藏", "大型山洞", "奇异宝物",
		"", "", "稳固建筑2", "快速军队2", "强化侦察2", "节省粮食2", "快速训练2", "", "大型山洞2", ""};
	
	public static int FLAG_ACCOUNT = (1 << 0);	// 如果是则对全帐户生效
	public static int FLAG_BIG = (1 << 1);	// 需要20级宝库
	public static int FLAG_CHANGE = (1 << 2);	// 随意变换

	public boolean isFlagAccount() {
		return (flag & FLAG_ACCOUNT) != 0;
	}
	public boolean isFlagBig() {
		return (flag & FLAG_BIG) != 0;
	}
	public boolean isFlagChange() {
		return (flag & FLAG_CHANGE) != 0;
	}
	
	int id;
	int type;		// 宝物类型，1表示图纸
	int cid;		// 当前拥有村
	int uid;		// 当前拥有者
	int flag;
	long captureTime;	// 获取时间
	int x;	// 所在坐标，为了快速计算
	int y;
	int status;		// 0 未激活 1 激活
	int effect;		// 效果提升数值，一个整数，例如山洞就是200或者100
	
	String name;
	long createTime;
	public String getTypeName() {
		return typeNames[type];
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public long getCaptureTime() {
		return captureTime;
	}
	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isActive() {
		return status == 1;
	}
	public int getEffect() {
		return effect;
	}
	public void setEffect(int effect) {
		this.effect = effect;
	}
}
