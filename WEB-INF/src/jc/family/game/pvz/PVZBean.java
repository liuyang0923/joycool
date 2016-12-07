package jc.family.game.pvz;

import java.util.LinkedList;

public class PVZBean {
	int state = 0; // 0空地,1大门,2攻陷
	PlantBean plantBean = null;
	LinkedList zombieList = new LinkedList();

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public PlantBean getPlantBean() {
		return plantBean;
	}

	public void setPlantBean(PlantBean plantBean) {
		this.plantBean = plantBean;
	}

	public LinkedList getZombieList() {
		return zombieList;
	}

	public void setZombieList(LinkedList zombieList) {
		this.zombieList = zombieList;
	}

}