package jc.exam.bag;

public class ExamType {
	int id;
	String typeName;
	int hypo;	// 从属于哪个项目
	int flag;
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getHypo() {
		return hypo;
	}
	public void setHypo(int hypo) {
		this.hypo = hypo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
