package net.joycool.wap.spec.app;

import java.text.DecimalFormat;

public class AppBean {
	public static int FLAG_CLOSE = (1 << 0);	// 不开放，无法安装（如果已经安装了，可以使用）
	public static int FLAG_HIDE = (1 << 1);	// 列表中隐藏，无法查看
	public static int FLAG_OFFLINE = (1 << 2);	// 服务器离线，可以安装但无法访问，或者是维护中
	public static int FLAG_ICON = (1 << 3);	// 插件带图标
	public static int FLAG_LOCAL = (1 << 4);	// 本地应用，无需跳转，目录伪装成插件
	public static int FLAG_UID = (1 << 5);	// 如果不选择，uid是加密后的，不等于乐酷的userId
	public static int FLAG_PAY = (1 << 6);		// 开放支付(酷币)
	public static int FLAG_ALLOW = (1 << 7);	// 允许非手机访问
	public static int FLAG_LIMIT = (1 << 8);	// 限制访问速度
	public static int FLAG_TEST = (1 << 9);	// 测试期（或者开发期）
	public static int FLAG_LOGO = (1 << 10);	// 插件带大图标
	public static int FLAG_PAY_ITEM = (1 << 11);	// 开放支付（行囊）
	public static int FLAG_PAY_LB = (1 << 12);		// 开放支付（银行存款），不同的开放支付，支持不同的支付类型
	public static int FLAG_DIRECT = (1 << 13);	// 直接进入不需要安装，伪装成不是插件
	public static int FLAG_NO_SID = (1 << 14);	// 不需要编码所有的url带sid
	public static int FLAG_PRESENT = (1 << 15);	// 允许插件赠送物品或者乐币给用户
	public static int FLAG_HIDE_URL = (1 << 16);	// 允许插件赠送物品或者乐币给用户
	
	static DecimalFormat numFormat = new DecimalFormat("0.0");
	
	int id;
	String name;
	String name2;			// 简短名字，用于用户状态，例如城堡2和城堡4的状态都是城堡，这个名字不能用数字结尾，最多3个中文字
	String shortName;		// 简短名字，2-4字用于做快捷通道等等
	String info;
	int type;		// 应用分类
	
	String apiKey;		// api调用所需的普通密钥
	String secretKey;	// 同学密钥
	String email;		// 联系email
	String contact;		// 其他联系方式
	
	long createTime;
	
	String dir;	// 目录
	String dirf;		// 目录前后加上 / ，常用地址url
	String url;		// 目标地址的前缀，必须以/结尾
	String uri;		// 目标地址uri前缀（不带server）
	String index;
	
	String offline;		// 脱机信息，如果插件服务器挂了，显示什么
	
	int flag;
	int count;	// 安装人数
	
	int score;	// 总分
	int scoreCount;	// 总的评分人数
	int replyCount;	// 回复该组件的人数
	
	int favorCount;	// 加收藏的人数
	int activeCount;	// 活跃玩家数
	
	String author;	// 作者信息

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getDir() {
		return dir;
	}
	public String getDirFull() {
		return dirf;
	}

	public void setDir(String dir) {
		this.dir = dir;
		dirf = "/" + dir + "/";
	}

	public boolean isFlagClose() {
		return (flag & FLAG_CLOSE) != 0;
	}
	public boolean isFlagHide() {
		return (flag & FLAG_HIDE) != 0;
	}
	public boolean isFlagOffline() {
		return (flag & FLAG_OFFLINE) != 0;
	}
	public boolean isFlagIcon() {
		return (flag & FLAG_ICON) != 0;
	}
	public boolean isFlagLogo() {
		return (flag & FLAG_LOGO) != 0;
	}
	public boolean isFlagLocal() {
		return (flag & FLAG_LOCAL) != 0;
	}
	public boolean isFlagUid() {
		return (flag & FLAG_UID) != 0;
	}
	public boolean isFlagPay() {
		return (flag & FLAG_PAY) != 0;
	}
	public boolean isFlagAllow() {
		return (flag & FLAG_ALLOW) != 0;
	}
	public boolean isFlagLimit() {
		return (flag & FLAG_LIMIT) != 0;
	}
	public boolean isFlagTest() {
		return (flag & FLAG_TEST) != 0;
	}
	
	public boolean isFlagPayItem() {
		return (flag & FLAG_PAY_ITEM) != 0;
	}
	public boolean isFlagPayLb() {
		return (flag & FLAG_PAY_LB) != 0;
	}
	public boolean isFlagDirect() {
		return (flag & FLAG_DIRECT) != 0;
	}
	public boolean isFlagNoSid() {
		return (flag & FLAG_NO_SID) != 0;
	}
	public boolean isFlagPresent() {
		return (flag & FLAG_PRESENT) != 0;
	}
	public boolean isFlagHideUrl() {
		return (flag & FLAG_HIDE_URL) != 0;
	}
	
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		int pos = url.indexOf('/', 10);
		if(pos != -1)
			uri = url.substring(pos);
		else
			uri = "/";
	}

	public String getOffline() {
		return offline;
	}

	public void setOffline(String offline) {
		this.offline = offline;
	}

	public int getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getFavorCount() {
		return favorCount;
	}

	public void setFavorCount(int favorCount) {
		this.favorCount = favorCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(int scoreCount) {
		this.scoreCount = scoreCount;
	}
	
	public float getAveScore() {
		if(scoreCount > 0)
			return (float)score / scoreCount;
		return 0;
	}
	public String getAveScoreString() {
		return numFormat.format(getAveScore());
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
