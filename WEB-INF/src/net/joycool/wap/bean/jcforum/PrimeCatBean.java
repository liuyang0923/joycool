package net.joycool.wap.bean.jcforum;

public class PrimeCatBean {
	int id;
	int parentId;	// 父目录
	int userId;		// 创建人
	int forumId;	// 所属板块
	String name;
	long createTime;
	int catCount;		// 分类下分类数
	int threadCount;	// 分类下帖子数
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getForumId() {
		return forumId;
	}
	public void setForumId(int forumId) {
		this.forumId = forumId;
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCatCount() {
		return catCount;
	}
	public void setCatCount(int catCount) {
		this.catCount = catCount;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
	
}
