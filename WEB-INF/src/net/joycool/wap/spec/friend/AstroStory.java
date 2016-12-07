package net.joycool.wap.spec.friend;

public class AstroStory {
	int id;
	int astroId;
	String content;
	int flag;	// 0:星座的解说 1:星座文章 2:男性相关文章 3:女性相关文章
	String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAstroId() {
		return astroId;
	}
	public void setAstroId(int astroId) {
		this.astroId = astroId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
}
