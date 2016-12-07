package jc.answer;

public class BeanProblem {
	private int id;
	private int uid;
	private int isSovle;
	private int isOver;
	private int numReply;
	private int numView;
	private int numEgg;
	private int numFlower;
	private int del;
	private int flag;
	private String pTitle;
	private String pCont;
	private String prize;
	private long createTime;
	private long lastReplyTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getIsSovle() {
		return isSovle;
	}

	public void setIsSovle(int isSovle) {
		this.isSovle = isSovle;
	}

	public int getIsOver() {
		return isOver;
	}

	public void setIsOver(int isOver) {
		this.isOver = isOver;
	}

	public long getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(long lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public int getNumReply() {
		return numReply;
	}

	public void setNumReply(int numReply) {
		this.numReply = numReply;
	}

	public int getNumView() {
		return numView;
	}

	public void setNumView(int numView) {
		this.numView = numView;
	}

	public int getNumEgg() {
		return numEgg;
	}

	public void setNumEgg(int numEgg) {
		this.numEgg = numEgg;
	}

	public int getNumFlower() {
		return numFlower;
	}

	public void setNumFlower(int numFlower) {
		this.numFlower = numFlower;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getPCont() {
		return pCont;
	}

	public void setPCont(String cont) {
		pCont = cont;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getPTitle() {
		return pTitle;
	}

	public void setPTitle(String title) {
		pTitle = title;
	}

}
