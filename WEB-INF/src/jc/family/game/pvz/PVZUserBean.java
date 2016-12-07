package jc.family.game.pvz;

import java.util.*;

import jc.family.game.vs.VsGameBean;
import jc.family.game.vs.VsUserBean;

public class PVZUserBean extends VsUserBean {

	int operNum = 0; // 个人操作数
	boolean plant; // 玩家控制的是否是植物
	boolean isenter;// 是否进入战场
	LinkedList plantList = new LinkedList(); // 玩家选用的植物列表
	LinkedList zombieList = new LinkedList(); // 玩家选用的僵尸列表

	public void init(VsGameBean game) {
		PVZGameBean pvz = (PVZGameBean) game;
		plant = (pvz.plantSide == getSide());
		if(pvz.plantSide == getSide()) {
			plant = true;
			pvz.getPlantUserList().add(this);
		} else {
			plant = false;
			pvz.getZombieUserList().add(this);
		}
	}

	public int getOperNum() {
		return operNum;
	}

	public void setOperNum(int operNum) {
		this.operNum = operNum;
	}

	public boolean isIsenter() {
		return isenter;
	}

	public void setIsenter(boolean isenter) {
		this.isenter = isenter;
	}

	public boolean isZombie() {
		return !plant;
	}

	public boolean isPlant() {
		return plant;
	}

	public LinkedList getPlantList() {
		return plantList;
	}

	public void setPlantList(LinkedList plantList) {
		this.plantList = plantList;
	}

	public LinkedList getZombieList() {
		return zombieList;
	}

	public void setZombieList(LinkedList zombieList) {
		this.zombieList = zombieList;
	}

}
