package jc.family.game.snow;

public class SnowActivityBean {

	private String fmName;// 家族名(0，为无家族)
	private String uName;// 用户名
	private int tType;// 1.小雪球2.中雪球3.大雪球.4.投雪机5.扫把6.铁锹7.推雪机
	private int aType;// 1.做了一2.使用了

	public String getFmName() {
		return fmName;
	}

	public void setFmName(String fmName) {
		this.fmName = fmName;
	}

	public String getUName() {
		return uName;
	}

	public void setUName(String name) {
		uName = name;
	}

	public int getTType() {
		return tType;
	}

	public void setTType(int type) {
		tType = type;
	}

	public int getAType() {
		return aType;
	}

	public void setAType(int type) {
		aType = type;
	}

}