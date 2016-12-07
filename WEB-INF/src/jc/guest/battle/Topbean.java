package jc.guest.battle;

import java.util.ArrayList;

public class Topbean {
	public ArrayList winname;
	public int times;
	public ArrayList losename;
	public ArrayList getWinname() {
		return winname;
	}

	public ArrayList getLosename() {
		return losename;
	}

	public void setLosename(ArrayList losename) {
		this.losename = losename;
	}
 
	public void setWinname(ArrayList winname) {
		this.winname = winname;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
 