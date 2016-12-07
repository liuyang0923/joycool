package net.joycool.wap.spec.friend;

public class AstroSupei {
	int id;
	int astro1;
	int astro2;
	int exp;
	String proportion;
	String content;
	int flag;	//如果flag=0，昨说明astro1是男玩家，astro2是女玩家。flag=1则相反。
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAstro1() {
		return astro1;
	}
	public void setAstro1(int astro1) {
		this.astro1 = astro1;
	}
	public int getAstro2() {
		return astro2;
	}
	public void setAstro2(int astro2) {
		this.astro2 = astro2;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
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