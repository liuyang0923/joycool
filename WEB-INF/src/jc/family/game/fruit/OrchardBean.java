package jc.family.game.fruit;
/**
 * 
 * 这个类是单个果园的bean
 * 
 * @author guigui
 *
 */
public class OrchardBean {
	
	int x_Point;// 果园的x坐标
	int y_Point;// 果园的y坐标
	int fruitCount;// 水果数量
	int sunCount;//	阳光
	int sunGlassGrade;// 阳光透镜级数
	int side;// 归属家族的ID
	String  orchardName;// 花园名字
	int sunCaptureRate;// 阳光的采集率
	float sun0;// 阳光底数
	long time0;// 在这个级数开始的时间
	
	public void setSun0(float sun0) {
		this.sun0 = sun0;
	}
	public float getSun0() {
		return sun0;
	}
	public void setSun0(int sun0) {
		this.sun0 = sun0;
	}
	public long getTime0() {
		return time0;
	}
	public void setTime0(long time0) {
		this.time0 = time0;
	}
	public int getSide() {
		return side;
	}
	public void setSide(int side) {
		this.side = side;
	}
	public int getX_Point() {
		return x_Point;
	}
	public void setX_Point(int xPoint) {
		x_Point = xPoint;
	}
	public int getY_Point() {
		return y_Point;
	}
	public void setY_Point(int yPoint) {
		y_Point = yPoint;
	}
	public int getSunCaptureRate() {
		return sunCaptureRate;
	}
	public void setSunCaptureRate(int sunCaptureRate) {
		this.sunCaptureRate = sunCaptureRate;
	}
	public int getFruitCount() {
		return fruitCount;
	}
	public void setFruitCount(int fruitCount) {
		this.fruitCount = fruitCount;
	}
	public int getSunCount() {
		return sunCount;
	}
	public void setSunCount(int sunCount) {
		this.sunCount = sunCount;
	}
	public int getSunGlassGrade() {
		return sunGlassGrade;
	}
	public void setSunGlassGrade(int sunGlassGrade) {
		this.sunGlassGrade = sunGlassGrade;
	}
	
	public String getOrchardName() {
		return orchardName;
	}
	public void setOrchardName(String orchardName) {
		this.orchardName = orchardName;
	}
	
	public void setOrchardName() {//使用这个方法的前提是，这个果园bean的X和y坐标已经确定
		String abc[]={"A","B","C","D","E","F","G","H","I"};
		String row[]={"1","2","3","4","5","6","7","8","9","10","11","12"};
		int x=this.x_Point;
		int y=this.y_Point;
		orchardName=abc[y]+row[x];
		return;
	}
	
	public static String getOrchardName(int x,int y) {
		String abc[]={"A","B","C","D","E","F","G","H","I"};
		String row[]={"1","2","3","4","5","6","7","8","9","10","11","12"};
		String name=abc[y]+row[x];
		return name;
	}
	
	public static int sunR2=20000;
	
	/**
	 * 得到当前阳光的总量
	 * @return
	 */
	
	public int getSunCount(long now){
		float sun = ((float) ((now- time0)*sunCaptureRate)/sunR2 + sun0);// 算出现在的阳光总量
		
		return (int)sun;
	}
	

	/**
	 * 增加水果
	 * 
	 * @param fruit
	 */
	public void addFruit(int fruit){
		fruitCount = fruitCount + fruit;
		return;
	}
	public void addSun0(int add) {
		sun0 += add;
	}
	// 重新计算sun0, time0
	public void updateSun() {
		long now = System.currentTimeMillis();
		sun0 = (float) ((now- time0)*sunCaptureRate)/sunR2 + sun0;
		time0 = now;
	}
	
}
