package jc.guest.battle;

import java.util.ArrayList;
import java.util.Random;

public class Stored {
	NameProperty np1;
	NameProperty np2;
	ArrayList depict;//描述
	int firstdead;//第一次死亡
	Random ri;//随机数
	boolean error;//错误判断
	String errormessage;//错误信息

	public String getErrormessage() {
		return errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Random getRi() {
		return ri;
	}

	public void setRi(Random ri) {
		this.ri = ri;
	}

	public int getFirstdead() {
		return firstdead;
	}

	public void setFirstdead(int firstdead) {
		this.firstdead = firstdead;
	}

	public NameProperty getNp1() {
		return np1;
	}

	public void setNp1(NameProperty np1) {
		this.np1 = np1;
	}

	public NameProperty getNp2() {
		return np2;
	}

	public void setDepict(ArrayList depict) {
		this.depict = depict;
	}

	public void setNp2(NameProperty np2) {
		this.np2 = np2;
	}

	public ArrayList getDepict() {
		return depict;
	}


}
