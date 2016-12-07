package jc.family.game.fruit;

import java.util.ArrayList;
import java.util.List;
/**
 * 这个是游戏中，参赛家族的，一些临时信息的bean
 * @author guigui
 *
 */
public class FruitFamilyBean {
	
	 int fruitATKGrade;// 水果攻击力(尖刺果皮)
	 int fruitThrowProportion ;// 扔水果的比例
	 int fruitHP;// 水果生命值(加厚果皮)
	 int fruitYield;// 水果的产量(果影分身)
	 int fruitSpeed;// 水果行进速度(喷气水果)
	 int fruitBeatted;// 已消灭的水果数
	 int fruitSacrificed;// 水果烈士数
	 int sunTotalCount;// 阳光总量
	 int sunTotalCaptureRate;// 总的采集率(阳光透镜)
	 int fruitTotalcount;// 总的家族水果量
	 List userList;// 这个家族参加这次游戏的人的ID
	

	FruitFamilyBean() {
		
		this.fruitATKGrade = 0;
		this.fruitThrowProportion = 6;
		this.fruitHP = 0;
		this.fruitYield = 0;
		this.fruitSpeed = 0;
		this.fruitBeatted = 0;
		this.fruitSacrificed = 0;
		this.sunTotalCount = 200;
		this.sunTotalCaptureRate = 200;
		this.fruitTotalcount = 500;
		this.userList = new ArrayList();
	}


	public List getUserList() {
		return userList;
	}


	public void setUserList(List userList) {
		this.userList = userList;
	}


	public int getFruitTotalcount() {
		return fruitTotalcount;
	}

	public void setFruitTotalcount(int fruitTotalcount) {
		this.fruitTotalcount = fruitTotalcount;
	}

	public int getFruitATKGrade() {
		return fruitATKGrade;
	}
	public void setFruitATKGrade(int fruitATKGrade) {
		this.fruitATKGrade = fruitATKGrade;
	}
	public int getFruitThrowProportion() {
		return fruitThrowProportion;
	}
	public void setFruitThrowProportion(int fruitThrowProportion) {
		this.fruitThrowProportion = fruitThrowProportion;
	}
	public int getFruitHP() {
		return fruitHP;
	}
	public void setFruitHP(int fruitHP) {
		this.fruitHP = fruitHP;
	}
	public int getFruitYield() {
		return fruitYield;
	}
	public void setFruitYield(int fruitYield) {
		this.fruitYield = fruitYield;
	}
	public int getFruitSpeed() {
		return fruitSpeed;
	}
	public void setFruitSpeed(int fruitSpeed) {
		this.fruitSpeed = fruitSpeed;
	}
	public int getFruitBeatted() {
		return fruitBeatted;
	}
	public void setFruitBeatted(int fruitBeatted) {
		this.fruitBeatted = fruitBeatted;
	}
	public int getFruitSacrificed() {
		return fruitSacrificed;
	}
	public void setFruitSacrificed(int fruitSacrificed) {
		this.fruitSacrificed = fruitSacrificed;
	}
	public int getSunTotalCount() {
		return sunTotalCount;
	}
	public void setSunTotalCount(int sunTotalCount) {
		this.sunTotalCount = sunTotalCount;
	}
	public int getSunTotalCaptureRate() {
		return sunTotalCaptureRate;
	}
	public void setSunTotalCaptureRate(int sunTotalCaptureRate) {
		this.sunTotalCaptureRate = sunTotalCaptureRate;
	}
}
