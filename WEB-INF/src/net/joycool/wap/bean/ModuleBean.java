package net.joycool.wap.bean;

public class ModuleBean {
	
	public static int FLAG_RECENT = (1 << 0);	// 这个模块在需要在最近访问模块中显示
	public static int FLAG_VIRTUAL = (1 << 1);	// 这个模块是虚拟的并不存在，不用于模块切换等功能，只用于显示用户状态，例如@睡觉
	
	public static int CHAT = 11;
	
	private int id = 0;
	
	private String name = null;

	private String image = null;

	private String urlPattern = null;

	private int priority = 10;

	private String returnUrl = null;
	
	String entryUrl;	// 模块入口url
	
	String posName;		// positionbean中的name，用于显示这个用户当前状态的
	String shortName; 
	
	int flag;

	public boolean isFlagRecent() {
		return (flag & FLAG_RECENT) != 0;
	}
	public boolean isFlagVirtual() {
		return (flag & FLAG_VIRTUAL) != 0;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof ModuleBean))
			return false;
		ModuleBean o = (ModuleBean)obj;
		return image.equals(o.getImage());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String toString() {
		return image;
	}

	public String getEntryUrl() {
		return entryUrl;
	}

	public void setEntryUrl(String entryUrl) {
		this.entryUrl = entryUrl;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}
	public String getPositionName() {
		return posName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
