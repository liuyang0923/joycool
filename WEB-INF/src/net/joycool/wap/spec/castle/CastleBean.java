package net.joycool.wap.spec.castle;

import java.util.Date;

import net.joycool.wap.util.StringUtil;

public class CastleBean {
	public static int TYPE_NATAR = (1 << 5);
	public static int TYPE_POWER = (1 << 6);		// 无法攻击，所有的攻击都会被自动返回
	public static int TYPE_LOCK = (1 << 7);		// 锁定单个城堡
	public static int TYPE_ART = (1 << 8);		// 宝库村，一旦没有了宝物自动消失
	int id;
	private int uid;
	private String castleName;
	private int x;			//坐标x
	private int y;			//坐标y
	long createTime;
	int expand;		// 扩张了多少个城堡（最多3个）
	int expand2;		// 扩张了多少个绿洲（最多3个）
	int race;		// 种族
	int type;		// 类型，1 普通444地形，之后分别是345, 453, 534，5表示9田，6表示15田， 7 8 9分别是三种资源绿洲，10 11 12分别是三种资源加粮食绿洲，13 14分别是粮食和大粮食绿洲
	public int getType() {
		return type;
	}
	// 判断地形，0-31
	public int getType2() {
		return type & 0x1f;
	}
	// 是可以造奇迹的城堡
	public boolean isNatar() {
		return (type & TYPE_NATAR) != 0;
	}
	public boolean isPower() {
		return (type & TYPE_POWER) != 0;
	}
	public boolean isLock() {
		return (type & TYPE_LOCK) != 0;
	}
	public boolean isArt() {
		return (type & TYPE_ART) != 0;
	}
	public int getPos() {
		return CastleUtil.xy2Pos(x, y);
	}
	public void setType(int type) {
		this.type = type;
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
	/**
	 * 比如map=10341324 8个字符，每个字符代表一个
	 * 则此地图就是
	 * 103
	 * 3 4
	 * 324
	 */
	private String map;
	
	public CastleBean(){
		
	}
	
	public double calcDistance(int x2, int y2) {
		return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
	}
	
	public String getCastleName() {
		return castleName;
	}
	public String getCastleNameWml() {
		return StringUtil.toWml(castleName);
	}
	public void setCastleName(String castleName) {
		this.castleName = castleName;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
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
	}

	public int getExpand() {
		return expand;
	}

	public void setExpand(int expand) {
		this.expand = expand;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	// 行宫等级和扩张度是否足够，皇宫是palace2
	public boolean canExpand(int palaceGrade, int palace2Grade) {
		if(palaceGrade == 0)
			return expand == 2 && palace2Grade == 20 || expand == 1 && palace2Grade >= 15 || expand == 0 && palace2Grade >= 10;
		return expand == 1 && palaceGrade == 20 || expand == 0 && palaceGrade >= 10;
	}
	public int getExpand2() {
		return expand2;
	}
	public void setExpand2(int expand2) {
		this.expand2 = expand2;
	}
	public boolean canExpand2(int buildingGrade) {
		return expand2 == 2 && buildingGrade == 20 || expand2 == 1 && buildingGrade >= 15 || expand2 == 0 && buildingGrade >= 10;
	}
	//判断是否是自然界的军队
	public boolean isNature() {
		return id == -1;
	}
}
